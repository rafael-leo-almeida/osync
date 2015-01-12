package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.monitor.config.base.OracleConnection;

public class ViewConfig {

	public static final String view1 = "create or replace force view DB_MODE " +
			"as select open_mode from sys.v_$database";  
	public static final String view2 = "CREATE OR REPLACE FORCE VIEW sy$person " +
			"AS " +
			"SELECT " +
			"txn, " +
			"id key, " +
			"first_name, " +
			"last_name, " +
			"birth_date, " +
			"deleted " +
			"FROM " +
			" person ";
	public static void createView(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		stat.execute(view1);	
	}
	
	public static void createExampleTableView(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		stat.execute(view2);
	} 
	
	
	
}
