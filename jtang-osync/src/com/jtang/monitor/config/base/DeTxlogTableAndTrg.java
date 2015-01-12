package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.jtang.common.table.TableInfo;

public class DeTxlogTableAndTrg {

	private TableInfo table;
	private String url;
	private String userName;
	private String password;
	private String deTableName;

	public DeTxlogTableAndTrg(TableInfo table, String url, String userName,
			String password) {
		this.table = table;
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.deTableName = "sy$de" + this.table.getTableName();
	}

	public boolean createDeTable() throws SQLException {

		if (this.isExistDeTable())
			return true;
		String sql = "create table " + this.deTableName + " ( ";
		List<String> keys = this.table.getKeys();
		Map<String, String> types = this.table.getColumnType();
		if (keys == null || keys.size() == 0) {
			return false;
		}
		boolean first = true;
		for (String key : keys) {

			String type = types.get(key);
			if(!(type.equalsIgnoreCase("number")||type.equalsIgnoreCase("varchar2")))
				continue;
			if(type.equalsIgnoreCase("varchar2"))
				type+="(100)";
			if (first == false) {
				sql += " , ";
			} else {
				first = false;
			}
			sql += key + "  " + type;
		}
		sql += " , issyn number(1) )";

		Connection con = OracleConnection
				.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		stat.execute(sql);
		stat.close();
		con.close();
       return true;
	}

	public boolean isExistDeTable() throws SQLException {

		String sql = "select table_name from user_tables where table_name = '"
				+ this.deTableName.toUpperCase() + "'";
		Connection con = OracleConnection
				.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql);
		if (set.next()) {
			stat.close();
			con.close();
			return true;
		}
		stat.close();
		con.close();
		return false;

	}
	
	public void createDeTrigger() throws SQLException {

		if(!isExistDeTable())
			return ;
		String proc0;
		String proc01 = "CREATE or replace TRIGGER " + this.table.getTableName()
				+ "_DETRG before delete on " + this.table.getTableName()
				+ " referencing old as old new as new for each row begin ";
		String proc02 = " insert into " + this.deTableName + "(";// id,issyn)																	
		String proc03 = " values(";
		String proc04 =  " end;";
		
		List<String> keys = this.table.getKeys();
		Map<String, String> types = this.table.getColumnType();
		boolean first = true;
		for (String key : keys) {
			String type = types.get(key);
			if (type.equalsIgnoreCase("number")|| type.equalsIgnoreCase("varchar2")) {
				if (first == false) {
					proc02 += " , ";
					proc03 += " , ";
				}
				if (first) {
					first = false;
				}
				if (type.equalsIgnoreCase("number")) {
                     
					proc02 += " "+key+" ";
					proc03 += " "+":old."+key;
					
				}else if (type.equalsIgnoreCase("varchar2")) {
         
		            proc02+=" "+key+" ";
		            proc03+=" "+":old."+key+" ";
				}

			} else {
				continue;
			}
		}
		proc02+=" ,ISSYN) ";
		proc03+=" ,0);";
        proc0 = proc01+proc02+proc03+proc04;
        System.out.println(proc0);
        Connection con = OracleConnection.getConnection(url, userName, password);
        Statement stat = con.createStatement();
        stat.execute(proc0);
        stat.close();
        con.close();
        
	}

}
