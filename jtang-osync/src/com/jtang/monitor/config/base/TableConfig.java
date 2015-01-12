package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jtang.monitor.config.base.OracleConnection;

public class TableConfig {

	public static final String table1 = "CREATE  TABLE PATCH_VERCONTROL(" +
			"ID            NUMBER, " +
			"DB_VERSION    VARCHAR2(10), " +
			"PATCH_NUMBER  VARCHAR2(15), " +
			"TIMESTAMP     DATE " +
			") " +
			"INITRANS 1 " +
			"MAXTRANS 255 " +
			"PCTUSED 80 " +
			"PCTFREE 10 "  +
			"NOCACHE";
	
	public static final String table2="create  table sy$sources (" +
			"name    varchar2(30), " +
			"bitnum  number constraint sy$sources_n1 not null " +
			") " +
			"INITRANS 1 " +
			"MAXTRANS 255 " +
			"PCTUSED 80 " +
			"PCTFREE 10 ";
	public static final String table3 = "create  table sy$txlog ( " +
			" txn    number, " +
			"scn    number constraint sy$txlog_n1 not null, " +
			"mask   number, " +
			" opt   number,"+
			"ts     timestamp default systimestamp constraint sy$txlog_n2 not null " +
			") rowdependencies " +
			"INITRANS 1 " +
			"MAXTRANS 255 " +
			"PCTUSED 80 " +
			"PCTFREE 10";
	public static final String table4 = "create  table sync_core_settings ( " +
			" raise_dbms_alerts char(1) constraint sync_core_settings_n1 not null" +
			") " +
			"INITRANS 1 " +
			"MAXTRANS 255 " +
			"PCTUSED 90 " +
			"PCTFREE 5";
	
	public static void createTables(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		List<String> list = getTables(url, userName, password);
		for (String string : list) {
			if(string.equalsIgnoreCase("PATCH_VERCONTROL")){
				String sql = "drop table PATCH_VERCONTROL";
				stat.execute(sql);
			}else if(string.equalsIgnoreCase("sy$sources")){
				String sql = "drop table sy$sources";
				stat.execute(sql);
			}else if(string.equalsIgnoreCase("sy$txlog")){
				String sql = "drop table sy$txlog";
				stat.execute(sql);
			}else if(string.equalsIgnoreCase("sync_core_settings")){
				String sql = "drop table sync_core_settings";
				stat.execute(sql);
			}
		}
		
		stat.execute(table1);
		stat.execute(table2);
		stat.execute(table3);
		stat.execute(table4);
		createCon(url, userName, password);
		createindex(url, userName, password);
		stat.close();
		con.close();
	}

	public static List<String> getTables(String url,String userName,String password) throws SQLException{
		
		List<String> list = new ArrayList<String>();
		String sql = "select table_name from user_tables";
		Connection con = OracleConnection.getConnection(url, userName, password);
		ResultSet set = con.createStatement().executeQuery(sql);
		while(set.next()){
			list.add(set.getString("table_name"));
		}
		return list;
	}
    private static void createCon(String url,String userName,String password) throws SQLException{
    	
    	String sql="ALTER TABLE SY$TXLOG ADD (" +
    			"CONSTRAINT SY$TXLOG_PK PRIMARY KEY " +
    			"  (TXN) " +
    			"USING INDEX " +
    			"INITRANS 2 " +
    			"MAXTRANS 255 " +
    			"PCTFREE 5)";
    	Connection con = OracleConnection.getConnection(url, userName, password);
    	con.createStatement().execute(sql);
    }
	private static void createindex(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		String sql = "create index sy$txlog_I1 on sy$txlog(scn) " +
				"INITRANS 2 " +
				"MAXTRANS 255 " +
				"PCTFREE 10 ";
	    con.createStatement().execute(sql);
	}
	
	public static void createExampleTable(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		String sql = "create table person( " +
				"id number primary key, " +
				"first_name varchar(120) not null, " +
				"last_name varchar(120) not null, " +
				"birth_date date, " +
				"deleted varchar(5) default 'false' not null, " +
				"txn number " +
				") " +
				"INITRANS 1 " +
				"MAXTRANS 255 " +
				"PCTUSED 40 " +
				"PCTFREE 10  " +
				"NOCACHE";
		String sql1 = "select table_name from user_tables where table_name='PERSON'";
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql1);
		if(set.next())
		  return;
		con.createStatement().execute(sql);
		
	}
	
	public static void main(String args[]){
		
		System.out.println(table1);
        System.out.println(table2);
        System.out.println(table3);
        System.out.println(table4);
		
	}
	
		
}
