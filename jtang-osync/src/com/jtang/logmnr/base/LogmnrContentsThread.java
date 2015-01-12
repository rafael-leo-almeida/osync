package com.jtang.logmnr.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.syn.comm.Message;

public class LogmnrContentsThread extends Thread {

	public static boolean breakFlag = false;
	public static long fromScn = 0;
	public static ConcurrentLinkedQueue<Message> queue;
    
	@Override
	public void run() {
		while (true) {
			if (breakFlag || (!LogmnrBase.logmnrConnection.isOk())) {
				break;
			}
			while (true) {
				PreparedStatement pStat = null;
				ResultSet set = null;
				String sql = "select scn,sql_redo,sql_undo,operation from v$logmnr_contents ";
				String sql1 = createSqlWhere();
				if (StringUtils.isBlank(sql1)) {
					try {
						this.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				String sql2 = "  and scn>? order by scn";
				sql = sql + sql1 + sql2;
				try {
					pStat = LogmnrBase.logmnrConnection.getCon()
							.prepareStatement(sql);
					pStat.setLong(1, fromScn);
					set = pStat.executeQuery();
					while (set.next()) {
						fromScn = set.getLong("scn");
						Message msg = sqlToMessage(set.getString("sql_redo"),
								set.getString("sql_undo"),
								set.getString("operation"),
								set.getString("table_name"),
								set.getString("table_space"));
						if (LogmnrContentsThread.queue != null) {
							LogmnrContentsThread.queue.offer(msg);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (set != null) {
						try {
							set.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (pStat != null) {
						try {
							pStat.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				try {
					this.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Message sqlToMessage(String redoSql, String undoSql,
			String operation, String table_name, String table_space) {

		Message msg = new Message();
		if (StringUtils.isBlank(redoSql) || StringUtils.isBlank(undoSql)) {
			return null;
		}
		SourceInfo sourceInfo = new SourceInfo();
		sourceInfo.setDBName(LogmnrBase.DBName);
		sourceInfo.setIp(LogmnrBase.ip);
		sourceInfo.setPort(LogmnrBase.port);
		sourceInfo.setTableName(table_name);
		sourceInfo.setUserName(table_space);
		msg.setOperation(operation);
		msg.setSourceInfo(sourceInfo);
		msg.setMonitorTime(new Date().getTime());
		Map<String,Object> datasMap = new HashMap<String,Object>();
        TableInfo tableInfo = LogmnrBase.logmnrMonitorTableManager.getTableByNameAndSpace(table_name, table_space);
        Map<String,String> strData = new HashMap<String,String>();
        Map<String,String> typeMap = tableInfo.getColumnType();
        if(operation.equalsIgnoreCase("insert")){
        	strData = resolveInsertWithRegex(redoSql);
        }else if(operation.equalsIgnoreCase("delete")){
        	strData = resolveInsertWithRegex(undoSql);
        }else{
        	strData = resolveUpdateWithRegex(redoSql);
        }
		Set<String> keys = strData.keySet();
        for (String key : keys) {
			String type = typeMap.get(key);
			String strVal = strData.get(key);
			if(type.equalsIgnoreCase("number")){
				datasMap.put(key, Long.parseLong(strVal));
			}else if(type.equalsIgnoreCase("varchar2")){
				datasMap.put(key, strVal);
			}else if(type.equalsIgnoreCase("date")){
				//待测试
			}
		}
		msg.setDatas(datasMap);
		return msg;
	}

	public static String createSqlWhere() {
		if (LogmnrBase.logmnrMonitorTableManager == null
				|| LogmnrBase.logmnrMonitorTableManager.getAllMonitorTables() == null
				|| LogmnrBase.logmnrMonitorTableManager.getAllMonitorTables()
						.size() == 0) {
			return "";
		}
		String str = "";
		Vector<TableInfo> tables = LogmnrBase.logmnrMonitorTableManager
				.getAllMonitorTables();
		for (TableInfo tableInfo : tables) {
			if (StringUtils.isBlank(str)) {
				str += " (table_name='"
						+ tableInfo.getTableName().toUpperCase()
						+ "' AND table_space='"
						+ tableInfo.getTableSpace().toUpperCase() + "')";
			} else {
				str += " or (table_name='"
						+ tableInfo.getTableName().toUpperCase()
						+ "' AND table_space='"
						+ tableInfo.getTableSpace().toUpperCase() + "')";
			}

		}
		if (StringUtils.isNotBlank(str)) {
			str = "(" + str + ")";
			return "where " + str;
		}
		return str;
	}

	public static Map<String,String> resolveInsertWithRegex(String insertSql){
		
		Map<String,String> map = new HashMap<String,String>();
		if(StringUtils.isBlank(insertSql)){
			return map;
		}
		String regex1 = "\\((.+?)\\)";
		String regex2 = "\\\"(.+?)\\\"";
		String regex3 = "\\'(.+?)\\'";
		String colStr="",valStr="";
		List<String> colList = new ArrayList<String>();
		List<String> valList = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(insertSql);
		int i = 0;
		while(matcher.find()){
			if(i==0){
				colStr = matcher.group(1);
			}else{
				valStr = matcher.group(1);
			}
			i++;
		}
		pattern=Pattern.compile(regex2);
		matcher = pattern.matcher(colStr);
		while(matcher.find()){
			colList.add(matcher.group(1));
		}
		pattern=Pattern.compile(regex3);
		matcher = pattern.matcher(valStr);
		while(matcher.find()){
			valList.add(matcher.group(1));
		}
		for(int k=0;k<colList.size();k++){
			map.put(colList.get(k),valList.get(k));
		}
		return map;
	}
	
	
	public static Map<String,String> resolveUpdateWithRegex(String updateSql){
		
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtils.isBlank(updateSql)){
			return map;
		}
		String regex1 = "\"([^\"]+?)\\\" = \\'(.+?)\\'";
		String regex2 = "\\\"(.+?)\\\"";
		String regex3 = "'(.+?)'";
		List<String> strList = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(updateSql);
		while(matcher.find()){
			strList.add(matcher.group());
		}
		for(int i=strList.size()-1;i>=0;i--){
			
			String str = strList.get(i);
			String colName = "";
			String val = "";
			pattern = Pattern.compile(regex2);
			matcher = pattern.matcher(str);
            if(matcher.find()){
            	colName = matcher.group(1);
            }
            pattern = Pattern.compile(regex3);
            matcher = pattern.matcher(str);
            if(matcher.find()){
            	val = matcher.group(1);
            }
            if(StringUtils.isNotBlank(colName)&&StringUtils.isNotBlank(val)){
            	map.put(colName, val);
            }
		}
		return map;
	}
	
	public static void main(String args[]) {

		String s1 = "insert into \"CHENYANG\".\"WORKER\"(\"ID\",\"NAM\",\"MONEY\") values ('2','shifew','34');";
		String s2 = "\"ID\",\"NAM\",\"MONEY\"";
		String s = "{\"internal_1\": [{\"version\": 4,\"addr\": \"192.160.1.11\"}]}";
		String s3 = "'2','shifew','34'";
        String s4 = "update \"CHENYANG\".\"WORKER\" set \"MONEY\" = '10' where \"ID\" = '1' and \"MONEY\" = '999' and ROWID = 'AAASNTAAEAAAAIPAAE';";
		
		String regex = ".+?\\[(.+?)\\].+?";
		String regex1 = "\\((.+?)\\)";
		String regex2 = "\\\"(.+?)\\\"";
		String regex3 = "\\'(.+?)\\'";
		String regex4 = "\"([^\"]+?)\\\" = \\'(.+?)\\'";
		Pattern pattern = Pattern.compile(regex4);
		Matcher matcher = pattern.matcher(s4);
		System.out.println(resolveUpdateWithRegex(s4));
		
		
//		while(matcher.find()){
//			System.out.println(matcher.group());
//		}
		
		//System.out.println(resolveInsertWithRegex(s1));
		
//		while(matcher.find()){
//			System.out.println(matcher.group(1));
//		}
		
		// String files[] = { "D:\\app\\chenyang\\oradata\\jtang\\REDO01.LOG",
		// "D:\\app\\chenyang\\oradata\\jtang\\REDO02.LOG",
		// "D:\\app\\chenyang\\oradata\\jtang\\REDO03.LOG" };
		// List<String> fileList = new ArrayList<String>();
		// LogmnrConnection logmnrCon = ConnectionFactory.getLogmnrConnection();
		// if (logmnrCon == null) {
		// return;
		// }
		// for (int i = 0; i < files.length; i++) {
		// fileList.add(files[i]);
		// }
		//
		// AddMonitorDML.monitorDML(logmnrCon);
		// AddLogfile.addLogfiles(fileList, logmnrCon);
		// String dictPath = "D:\\oracel11g\\dictionary\\logmnr.dat";
		// LogmnrStart.startLogmnr(dictPath, logmnrCon);
		//
		// String sql =
		// " select * from (select scn from v$logmnr_contents order by scn desc) where rownum<2";
		// try {
		// PreparedStatement pStat = logmnrCon.getCon().prepareStatement(sql);
		// ResultSet set = pStat.executeQuery();
		// while (set.next()) {
		// System.out.println(set.getLong("scn"));
		// System.out.println();
		// }
		// set.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// try {
		// Thread.currentThread().sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// long scn = 0;
		// while (true) {
		//
		// PreparedStatement pStat = null;
		// ResultSet set = null;
		// sql =
		// "select scn,sql_redo,sql_undo,operation from v$logmnr_contents where table_name='WORKER' and scn>? order by scn";
		// try {
		// pStat = logmnrCon.getCon().prepareStatement(sql);
		// pStat.setLong(1, scn);
		// set = pStat.executeQuery();
		// while (set.next()) {
		// scn = set.getLong("scn");
		// System.out.println(set.getLong("scn"));
		// System.out.println(set.getString("operation"));
		// System.out.println(set.getString("sql_redo"));
		// System.out.println(set.getString("sql_undo"));
		// System.out.println();
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// if (set != null) {
		// try {
		// set.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		// if (pStat != null) {
		// try {
		// pStat.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// try {
		// Thread.currentThread().sleep(100);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// }

	}

}
