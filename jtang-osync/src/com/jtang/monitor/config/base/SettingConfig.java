package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.monitor.config.base.OracleConnection;

public class SettingConfig {
	
	public static String set="insert into sync_core_settings values ('N')";

	public static void set(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		ResultSet r = stat.executeQuery("select * from sync_core_settings");
		if(r.next())
			return;
		stat.execute(set);
		
	}
	
}
