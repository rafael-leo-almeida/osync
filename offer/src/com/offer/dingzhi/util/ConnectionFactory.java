package com.offer.dingzhi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_CONNECT_NAME = "jdbc:mysql://localhost:3306/offer?characterEncoding=utf8";
	public static final String DB_USER_NAME = "root";
	public static final String DB_PASSWORD = "chenyang";
	public ConnectionFactory() {
		// TODO Auto-generated constructor stub
	}
	static {
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		/************百度BAE数据库连接************/    	
		Connection con = null;
		try {
			con = DriverManager.getConnection(DB_CONNECT_NAME, DB_USER_NAME, DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
}
