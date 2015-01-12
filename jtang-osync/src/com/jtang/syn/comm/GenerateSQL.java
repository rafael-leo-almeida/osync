package com.jtang.syn.comm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableMap;

public class GenerateSQL {

	private static String getKeyString( TableMap tableMap,Message info){
		
		TableInfo tableInfo = tableMap.getToTable().getTableInfo();
		TableInfo fromTable = tableMap.getFromTable().getTableInfo();
		Map<String,String> fieldsMap=tableMap.getFieldsMap();
		
		StringBuilder sql=new StringBuilder("");
		boolean effective = false;
		for (String key : fromTable.getKeys()) {
			
			String type = tableInfo.getColumnType().get(fieldsMap.get(key));
			String value = (String) info.getDatas().get(fieldsMap.get(key));
			if(value==null||value.length()==0||value.equalsIgnoreCase("null"))
				continue;
            if(type.equalsIgnoreCase("binary_double")){
				
			}else if(type.equalsIgnoreCase("binary_float")){
				
				
			}else if(type.equalsIgnoreCase("blob")){
				
			}else if(type.equalsIgnoreCase("clob")){
				
			}else if(type.equalsIgnoreCase("char")){
				
			}else if(type.equalsIgnoreCase("date")){
				
			}else if(type.equalsIgnoreCase("interval day to second")){
				
			}else if(type.equalsIgnoreCase("interval year to month")){
				
			}else if(type.equalsIgnoreCase("long")){
				
			}else if(type.equalsIgnoreCase("long raw")){
				
			}else if(type.equalsIgnoreCase("nclob")){
				
			}else if(type.equalsIgnoreCase("number")){
				if(effective==false){
					sql.append(" where "+fieldsMap.get(key)+"="+Integer.parseInt(value));
					effective=true;
				}else{
					sql.append(" and ");
					sql.append(fieldsMap.get(key)+"="+Integer.parseInt(value));
				}
			}else if(type.equalsIgnoreCase("nvarchar2")){
				
			}else if(type.equalsIgnoreCase("raw")){
				
			}else if(type.equalsIgnoreCase("timestamp")){
				
			}else if(type.equalsIgnoreCase("timestamp with local zone")){
				
			}else if(type.equalsIgnoreCase("timestamp with time zone")){
				
			}else if(type.equalsIgnoreCase("varchar2")){
				if(effective==false){
					sql.append(" where "+fieldsMap.get(key)+"="+"'"+value+"'");
					effective=true;
				}else{
					sql.append(" and ");
					sql.append(fieldsMap.get(key)+"="+"'"+value+"'");
				}
			}
			
		}
		return sql.toString();
	}
	
	
	public static String getInsertSQL(TableMap tableMap,Message info){
		
		TableInfo tableInfo = tableMap.getToTable().getTableInfo();
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(tableInfo.getTableName());
		StringBuilder columnBuilder = new StringBuilder("( ");
		StringBuilder datasBuilder = new StringBuilder("values( ");
		boolean effective = false;
		for (String column  : tableInfo.getColumnName()) {
			
			String type = tableInfo.getColumnType().get(column);
			String value = (String) info.getDatas().get(column);
			if(value==null||value.length()==0||value.equalsIgnoreCase("null"))
				 continue;
			/*
			 * append the column
			 * 
			 */
			if(effective==false){
				columnBuilder.append(column);
			}else{
				columnBuilder.append(',');
				columnBuilder.append(column);
			}
			/*
			 * append the value
			 * 
			 */
			if(type.equalsIgnoreCase("binary_double")){
				
			}else if(type.equalsIgnoreCase("binary_float")){
				
				
			}else if(type.equalsIgnoreCase("blob")){
				
			}else if(type.equalsIgnoreCase("clob")){
				
			}else if(type.equalsIgnoreCase("char")){
				
			}else if(type.equalsIgnoreCase("date")){
				if(effective==false){
					datasBuilder.append("to_date('"+ value + "','YYYYMMDD HH24:MI:SS')");
					effective=true;
				}else{
					datasBuilder.append(" , ");
					datasBuilder.append("to_date('"+ value + "','YYYYMMDD HH24:MI:SS')");
				}
			}else if(type.equalsIgnoreCase("interval day to second")){
				
			}else if(type.equalsIgnoreCase("interval year to month")){
				
			}else if(type.equalsIgnoreCase("long")){
				
			}else if(type.equalsIgnoreCase("long raw")){
				
			}else if(type.equalsIgnoreCase("nclob")){
				
			}else if(type.equalsIgnoreCase("number")){
				if(effective==false){
					datasBuilder.append(Integer.parseInt(value));
					effective=true;
				}else{
					datasBuilder.append(" , ");
					datasBuilder.append(Integer.parseInt(value));
				}
			}else if(type.equalsIgnoreCase("nvarchar2")){
				
			}else if(type.equalsIgnoreCase("raw")){
				
			}else if(type.equalsIgnoreCase("timestamp")){
				
			}else if(type.equalsIgnoreCase("timestamp with local zone")){
				
			}else if(type.equalsIgnoreCase("timestamp with time zone")){
				
			}else if(type.equalsIgnoreCase("varchar2")){
				if(effective==false){
					datasBuilder.append("'"+value+"'");
					effective=true;
				}else{
					datasBuilder.append(" , ");
					datasBuilder.append("'"+value+"'");
				}
			}
			
			
		}
		columnBuilder.append(" ) ");
		datasBuilder.append(" ) ");
		sql.append(columnBuilder.toString());
		sql.append(datasBuilder.toString());
		if(effective)
			return sql.toString();
		else
			return "";
	}
	public static String getUpdateSQL(TableMap tableMap,Message info){
	    TableInfo tableInfo = tableMap.getToTable().getTableInfo();
	    StringBuilder sql = new StringBuilder("update "+tableInfo.getTableName()+" set ");
	    boolean effective = false;
	    for (String column : tableInfo.getColumnName()) {
			
	    	String type = tableInfo.getColumnType().get(column);
			String value = (String) info.getDatas().get(column);
			if(value==null||value.length()==0||value.equalsIgnoreCase("null")){
				continue;
			}
			/*
			 * append the update sql
			 */
            if(type.equalsIgnoreCase("binary_double")){
				
			}else if(type.equalsIgnoreCase("binary_float")){
				
				
			}else if(type.equalsIgnoreCase("blob")){
				
			}else if(type.equalsIgnoreCase("clob")){
				
			}else if(type.equalsIgnoreCase("char")){
				
			}else if(type.equalsIgnoreCase("date")){
				if(effective==false){
					sql.append(column+"="+"to_date('"+value+"','YYYYMMDD HH24:MI:SS')");
					effective=true;
				}else{
					sql.append(" , ");
					sql.append(column+"="+"to_date('"+value+"','YYYYMMDD HH24:MI:SS')");
				}
				
			}else if(type.equalsIgnoreCase("interval day to second")){
				
			}else if(type.equalsIgnoreCase("interval year to month")){
				
			}else if(type.equalsIgnoreCase("long")){
				
			}else if(type.equalsIgnoreCase("long raw")){
				
			}else if(type.equalsIgnoreCase("nclob")){
				
			}else if(type.equalsIgnoreCase("number")){
				if(effective==false){
					sql.append(column+"="+Integer.parseInt(value));
					effective=true;
				}else{
					sql.append(" , ");
					sql.append(column+"="+Integer.parseInt(value));
				}
			}else if(type.equalsIgnoreCase("nvarchar2")){
				
			}else if(type.equalsIgnoreCase("raw")){
				
			}else if(type.equalsIgnoreCase("timestamp")){
				
			}else if(type.equalsIgnoreCase("timestamp with local zone")){
				
			}else if(type.equalsIgnoreCase("timestamp with time zone")){
				
			}else if(type.equalsIgnoreCase("varchar2")){
				if(effective==false){
					sql.append(column+"='"+value+"'");
					effective=true;
				}else{
					sql.append(" , ");
					sql.append(column+"='"+value+"'");
				}
			}
           
		}
	    sql.append(getKeyString(tableMap, info));
		if(effective)
			return sql.toString();
		else 
			return "";
	}
	
	public static boolean isExist(TableMap tableMap,Message info,Statement stmt){
		TableInfo tableInfo = tableMap.getToTable().getTableInfo();
		boolean f = false;
		StringBuilder sql = new StringBuilder("select * from ");
		String where = getKeyString(tableMap, info);
		//表示没有主关键字
		if(where==null||where.length()==0){
			return false;
		}
		sql.append(tableInfo.getTableName());
		sql.append(getKeyString(tableMap, info));
		try {
			ResultSet set = stmt.executeQuery(sql.toString());
			f=set.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
	public static String getDeleteSQL(TableMap tableMap,Message info){
		TableInfo tableInfo = tableMap.getToTable().getTableInfo();
		String sql="delete from "+tableInfo.getTableName();
		String where = getKeyString(tableMap, info);
		if(where==null||where.length()==0){
			System.out.println("the delete sql is notvalide!");
			return "";
		}else{
			return (sql+where);
		}
	}
	
	
	
}
