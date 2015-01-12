package com.jtang.monitor.config.base;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.monitor.config.base.OracleConnection;

public class CompileObjects {

	public static void compile(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		CallableStatement callStmt = null;  
		callStmt = con.prepareCall("{call COMPILE_ALLOBJECTS}");
		callStmt.execute();  
		stat.close();
		con.close();
		
		
		
	}
	
	
}
