package com.jtang.syn.comm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {

	public final static String ORACLDRIVER = "oracle.jdbc.driver.OracleDriver";
	public final static String URL = "jdbc:oracle:thin:@localhost:1521:DB";
	public static String userName = "person";
	public static String pwd = "person";
	static {
		try {
			Class.forName(ORACLDRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {

		Connection con = null;
		try {
			con = DriverManager.getConnection(URL, userName, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
   public static Connection getConnection(String url,String userName,String pwd){
	   if(userName==null||pwd==null)
		   return getConnection();
	   if(url==null)
		   url=URL;
	   Connection con = null;
		try {
			con = DriverManager.getConnection(url, userName, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
   }

}
