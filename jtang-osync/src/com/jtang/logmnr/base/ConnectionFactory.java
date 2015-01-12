package com.jtang.logmnr.base;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionFactory {

	static String dbaUser="system";
	static String dbaPassword = "chenyang";
	static String user = "chenyang";
	static String password = "chenyang";
    static String driver = "oracle.jdbc.driver.OracleDriver";
    static String url = "jdbc:oracle:thin:@chenyang-PC:1521:jtang";
    static LogmnrConnection logmnrCon = null;
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
			con = DriverManager.getConnection(url, dbaUser, dbaPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return con;
    }
    
    public static LogmnrConnection getLogmnrConnection(){
    	
    	if(ConnectionFactory.logmnrCon!=null&&ConnectionFactory.logmnrCon.isOk()){
    		return ConnectionFactory.logmnrCon;
    	}
    	Connection con = ConnectionFactory.getConnection();
    	if(con==null){
    		return null;
    	}
    	ConnectionFactory.logmnrCon = new LogmnrConnection(con);
    	return ConnectionFactory.logmnrCon;
    	
    }
    
    public static void main(String args[]){
    	
    	Connection con = ConnectionFactory.getConnection();
    	if(con==null){
    		System.out.println("con is null");
    	}
    }
    
	
}
