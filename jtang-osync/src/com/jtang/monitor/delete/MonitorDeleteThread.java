package com.jtang.monitor.delete;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.DataTranslator;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.monitor.insertAndupdate.base.MonitorTableManager;
import com.jtang.syn.comm.Message;


public class MonitorDeleteThread extends Thread {

	private Connection con;
	private String sql = "";
	private ConcurrentLinkedQueue<Message> queue;
	private String monitorTableName;
	private TableInfo monitorTableInfo;
	private SourceInfo sourceInfo;
	private DataTranslator translator;
	private String url;
	private String userName;
	private String password;
	private MonitorTableManager monitorTableManager;

	public MonitorDeleteThread(ConcurrentLinkedQueue<Message> queue) {
		this.con = OracleConnection.getConnection();
		this.queue = queue;
	}

	public MonitorDeleteThread(ConcurrentLinkedQueue<Message> queue,
			String monitorTableName, DataTranslator translator,
			SourceInfo sourceInfo) {
		this.con = OracleConnection.getConnection();
		this.queue = queue;
		this.monitorTableName = monitorTableName;
		Map<String, String> values = new HashMap<String, String>();
		values.put("viewName", this.monitorTableName);
		values.put("database", OracleConnection.URL);
		values.put("userName", OracleConnection.userName);
		values.put("password", OracleConnection.pwd);
		try {
			this.monitorTableInfo = new CreateSchema(values).getSchemaWithDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sql = "select * from " + this.monitorTableName + " where ISSYN=0";
		this.translator = translator;
		this.sourceInfo = sourceInfo;
	}

	public MonitorDeleteThread(MonitorTableManager monitorTableManager,
	        SourceInfo sourceInfo,String userName,
			String password,ConcurrentLinkedQueue<Message> queue) {

		this.sourceInfo = sourceInfo;
		this.url="jdbc:oracle:thin:@"+this.sourceInfo.getIp()+":"+this.sourceInfo.getPort()+":"+this.sourceInfo.getDBName();
		this.userName = userName;
		this.password = password;
		this.con = OracleConnection.getConnection(this.url, this.userName,
				this.password);
		this.queue = queue;
		//this.sql = "select * from " + this.monitorTableName + " where ISSYN=0";
		this.monitorTableManager = monitorTableManager;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run();
		Statement stmt = null;
		if (con == null || queue == null) {
			System.out.println("monitorDeletedThread start failed!");
			return;
		}
		if (con != null) {
			try {
				stmt = con.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (stmt != null) {

				int size = this.monitorTableManager.getAllMonitorTables().size();
				for(int i=0;i<size;i++){
					
					this.monitorTableName = this.monitorTableManager.getAllMonitorTables().get(i).getTableName();
					this.monitorTableInfo = this.monitorTableManager.getAllMonitorTables().get(i);
					this.sql = "select * from " + this.monitorTableName + " where ISSYN=0";
					ResultSet rs = null;
					try {
						rs = stmt.executeQuery(sql);
						while (rs != null && rs.next()) {
							// TO-DO 向同步模块发送消息,该部分可以替换
							// Message info = this.getInfoTest(rs, stmt);
							Message info = this.getInfo(rs, stmt);
							info.setMonitorTime(new Date().getTime());
							//System.out.println(info.toString());
							queue.offer(info);
						}
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						if (this.con != null)
							try {
								con.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	public Message getInfo(ResultSet rs, Statement stmt) throws SQLException {

		Map<String, Object> values = new HashMap<String, Object>();
		String sql = "update " + this.monitorTableName + " set ISSYN=1 where ";
		String sqlWhere = " where ";
		boolean first = true;
		for (String fieldName : this.monitorTableInfo.getColumnName()) {
			String fieldType = this.monitorTableInfo.getColumnType().get(
					fieldName);
			if (fieldType.equalsIgnoreCase("number")) {
				values.put(fieldName, rs.getLong(fieldName));
				if (first) {
					sql += fieldName + "="
							+ ((Long) rs.getLong(fieldName)).toString();
					first = false;
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += fieldName + "="
								+ ((Long) rs.getLong(fieldName)).toString();
					}
				} else {
					sql += " and " + fieldName + "="
							+ ((Long) rs.getLong(fieldName)).toString();
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += " and " + fieldName + "="
								+ ((Long) rs.getLong(fieldName)).toString();
					}
				}
			} else if (fieldType.equalsIgnoreCase("varchar2")) {

				values.put(fieldName, rs.getString(fieldName));
				if (first) {
					sql += fieldName + "=" + "'" + rs.getString(fieldName)
							+ "'";
					first = false;
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += fieldName + "=" + "'"
								+ rs.getString(fieldName) + "'";
					}
				} else {
					sql += " and " + fieldName + "=" + "'"
							+ rs.getString(fieldName) + "'";
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += " and " + fieldName + "=" + "'"
								+ rs.getString(fieldName) + "'";
					}
				}
			} else if (fieldType.equalsIgnoreCase("date")) {

				Long value = rs.getLong(fieldName);
				values.put(fieldName, value);
				java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
						"yyyymmdd hh:mm:ss");
				Date d = new Date(value);
				String strD = format.format(d);
				strD = "to_date('" + strD + "','YYYYMMDD HH24:MI:SS')";
				if (first) {
					sql += fieldName + "=" + strD;
					first = false;
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += fieldName + "=" + strD;
					}
				} else {
					sql += " and " + fieldName + "=" + strD;
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += " and " + fieldName + "=" + strD;
					}
				}
			}
			// TODO: add more rules;
		}
		// TODO: set the sourceInfo
		stmt.executeUpdate(sql);
		String operation = "delete";
		// Message info=translator.transData(values, this.sourceInfo,
		// operation);
		Message info = new Message();
		info.setDatas(values);
		info.setOperation(operation);
		String sourceName = this.monitorTableName.substring(5);
		this.sourceInfo.setTableName(sourceName);
		info.setSourceInfo(this.sourceInfo);
		info.setDeleteSqlWhere(sqlWhere);
		return info;

	}

	// 这里为了测试sy$delog的id转换为key，由于databus改变了主键的名字导致
	// 将来所有字段的名字和源表一致，这样就可以利用通用的方法getInfo()
	public Message getInfoTest(ResultSet rs, Statement stmt)
			throws SQLException {
		Map<String, Object> values = new HashMap<String, Object>();
		String sql = "update " + this.monitorTableName + " set ISSYN=1 where ";
		String sqlWhere = " where ";
		boolean first = true;
		for (String fieldName : this.monitorTableInfo.getColumnName()) {

			String fieldType = this.monitorTableInfo.getColumnType().get(
					fieldName);
			if (fieldType.equalsIgnoreCase("number")) {
				if (fieldName.equalsIgnoreCase("id"))
					values.put("KEY", rs.getLong(fieldName));
				else
					values.put(fieldName, rs.getLong(fieldName));

				if (first) {
					sql += fieldName + "="
							+ ((Long) rs.getLong(fieldName)).toString();
					first = false;
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += "KEY" + "="
								+ ((Long) rs.getLong(fieldName)).toString();
					}
				} else {
					sql += " and " + fieldName + "="
							+ ((Long) rs.getLong(fieldName)).toString();
					if (!fieldName.equalsIgnoreCase("ISSYN")) {
						sqlWhere += " and " + "KEY" + "="
								+ ((Long) rs.getLong(fieldName)).toString();
					}
				}
			}
			// TODO: add more rules;
		}
		// TODO: set the sourceInfo
		stmt.executeUpdate(sql);
		String operation = "delete";
		Message info = translator.transData(values, this.sourceInfo, operation);
		info.setDeleteSqlWhere(sqlWhere);
		return info;
	}

}
