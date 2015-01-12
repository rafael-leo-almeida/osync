package com.jtang.monitor.insertAndupdate.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class OracleConnection {

	public final static String ORACLDRIVER = "oracle.jdbc.driver.OracleDriver";
	public final static String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static String userName = "SYSTEM";
	public static String pwd = "CHENyang13";
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
	   if(url==null){		   
		    url=URL;
	   }
	   Connection con = null;
		try {
			con = DriverManager.getConnection(url, userName, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
   }
   
   public static Connection getConnection(String ip,int port,String DBName,
		   String userName,String pwd){
	   if(userName==null||pwd==null){
		   return getConnection();
	   }
	   String url = "";
	   if(StringUtils.isBlank(ip)||StringUtils.isBlank(DBName)){		   
		    url=URL;
	   }else{
		   url="jdbc:oracle:thin:@"+ip+":"+port+":"+DBName;
	   }
	   Connection con = null;
		try {
			con = DriverManager.getConnection(url, userName, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
   }
   
   public static Connection getConnectionAsSysdba() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
	   
   /*
	  1、首先try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
       利用Oracle获取一个新的实例。

	 */  
	   
	   
	   Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	   Properties conProps = new Properties();
       conProps.put("user", "system");
       conProps.put("password", "CHENyang13");
       //conProps.put("defaultRowPrefetch", "15");
       /*
        * internal_logon  sysdba do not work
        * only internal_logon nomal
        */
       conProps.put("internal_logon", "sysdba");
	   
       return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", conProps);

   }
   
 
   
   

}
