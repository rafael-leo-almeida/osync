package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.TableInfo;

public class AddSource {

	String sourceName;
	String url;
	String userName;
	String password;

	public AddSource(String sourceName, String url, String userName,
			String password) throws SQLException {

		this.sourceName = sourceName;
		this.url = url;
		this.userName = userName;
		this.password = password;

	}

	public Map<String, String> getFields() throws SQLException {

		String sql = "select COLUMN_NAME,DATA_TYPE from user_tab_columns "
				+ " where table_name ='" + this.sourceName.toUpperCase() + "'";
		Connection con = OracleConnection
				.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql);
		Map<String, String> fields = new HashMap<String, String>();
		while (set.next()) {
			fields.put(set.getString("COLUMN_NAME"), set.getString("DATA_TYPE"));
		}
		stat.close();
		con.close();
		return fields;
	}

	public boolean addNewSource() throws SQLException {
		if (isExistSource() == false)
			return false;
		if (isInit()) {
			this.Init();
		}else{
			if(this.hasAdd())
				return true;
		}
		if (isFirst()) {
			this.first();
		}
		if (this.isExistTXN() != 1) {
			this.addTxn(this.isExistTXN());
		}
		//增加删除操作监控
		if(!this.isExistDeTable()){	
			if(!this.addDeTableAndTrg())
				return false;
		}
		new ProcOneConfig(url, userName, password, sourceName).createProc1();
		new TriggerConfig(url, userName, password, sourceName).createTrigger();
		new InsertSource(url, userName, password, sourceName).insert();
		return true;
	}

	public boolean isFirst() throws SQLException {

		Connection con = OracleConnection
				.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery("select name,type from user_source");
		while (set.next()) {

			if (set.getString("name").equalsIgnoreCase("sync_core")) {
				stat.close();
				con.close();
				return false;
			}

		}
		stat.close();
		con.close();
		return true;
	}

	public boolean isInit() throws SQLException {
		boolean sequence0 = false;
		boolean sequence1 = false;
		Connection con = OracleConnection
				.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		ResultSet set = stat
				.executeQuery("select sequence_name from user_sequences");
		while (set.next()) {
			if (set.getString("sequence_name").equalsIgnoreCase("SY$SCN_SEQ")) {
				sequence0 = true;
			} else if (set.getString("sequence_name").equalsIgnoreCase(
					"VERCONTROL_SEQ")) {
				sequence1 = true;
			}
		}
		boolean table1 = false;
		boolean table2 = false;
		boolean table3 = false;
		boolean table4 = false;

		List<String> list = TableConfig.getTables(url, userName, password);
		for (String string : list) {
			if (string.equalsIgnoreCase("PATCH_VERCONTROL")) {
				table1 = true;
			} else if (string.equalsIgnoreCase("sy$sources")) {
				table2 = true;
			} else if (string.equalsIgnoreCase("sy$txlog")) {
				table3 = true;
			} else if (string.equalsIgnoreCase("sync_core_settings")) {
				table4 = true;
			}
		}
		boolean view1 = false;
		ResultSet viewSet = stat
				.executeQuery("select view_name from user_views");
		while (viewSet.next()) {
			if (viewSet.getString("view_name").equalsIgnoreCase("DB_MODE")) {
				view1 = true;
			}
		}
		stat.close();
		con.close();
		if (table1 && table2 && table3 && table4 && view1 && sequence0
				&& sequence1)
			return false;
		return true;
	}

	public void Init() throws SQLException {

		SequenceConfig.createSequences(url, userName, password);
		TableConfig.createTables(url, userName, password);
		ViewConfig.createView(url, userName, password);
		SettingConfig.set(url, userName, password);

	}

	public void first() throws SQLException {

		new ProcConfig(sourceName, url, userName, password).createProc();

	}

	public boolean isExistSource() throws SQLException {

		if (this.sourceName == null || this.sourceName == "")
			return false;
		List<String> tables = TableConfig.getTables(url, userName, password);
		for (String string : tables) {
			if (string.equalsIgnoreCase(this.sourceName))
				return true;
		}
		return false;

	}

	/*
	 * 1 reprents there is the field named txn and the type is number 2 reprents
	 * there is the field named txn but the type is not number 0 reprents there
	 * is no field named txn
	 */
	public int isExistTXN() throws SQLException {

		Map<String, String> fields = this.getFields();
		Set<String> names = fields.keySet();
		for (String string : names) {
			if (string.equalsIgnoreCase("txn")
					&& fields.get(string).equalsIgnoreCase("number"))
				return 1;
			else if (string.equalsIgnoreCase("txn"))
				return 2;
		}
		return 0;
	}

	public void addTxn(int isExist) throws SQLException {

		Connection con = OracleConnection
				.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		String sql0 = "alter table " + this.sourceName + " drop column txn";
		String sql = "alter table " + this.sourceName + " add txn number";

		if (isExist == 2)
			stat.execute(sql0);

		stat.execute(sql);
		stat.close();
		con.close();

	}
	
	public boolean isExistDeTable() throws SQLException{
		
		if (this.sourceName == null || this.sourceName == "")
			return false;
		List<String> tables = TableConfig.getTables(url, userName, password);
		for (String string : tables) {
			if (string.equalsIgnoreCase("SY$DE"+this.sourceName))
				return true;
		}
		return false;
	}
	
	public boolean addDeTableAndTrg() throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		TableInfo table = new CreateSchema(null).getSchemaWithDB(this.sourceName, con);
		DeTxlogTableAndTrg deTableConfig = new DeTxlogTableAndTrg(table, url, userName, password);
		if(deTableConfig.createDeTable()){
			deTableConfig.createDeTrigger();
		    con.close();
		    return true;
		}else{
			con.close();
			return false;
		}
	}

	public boolean hasAdd() throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		String sql = "select * from sy$sources";
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql);
		while(set.next()){
			if(set.getString("name").equalsIgnoreCase(this.sourceName))
				return true;
		}
		return false;
	}
	
}
