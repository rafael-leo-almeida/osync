package com.jtang.common.record;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

	public static final String driver = "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://192.168.3.152:3306/synRecord?characterEncoding=utf-8";
	public static final String user = "root";
	public static final String password ="123";
	static{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		
		Connection con = null;
		try {
			con=DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void main(String args[]){
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			System.out.println("null");
		}else{
			System.out.println("ok");
		}
		
	}
	
}
