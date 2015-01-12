package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.monitor.config.base.OracleConnection;

public class InsertSource {

	String sourceName;
	String url;
	String userName;
	String password;
	
	String in;
	String in0="insert into sy$sources(NAME,BITNUM) values('";
	String in1= "',";
	String in2=	")";
	
	public InsertSource(String url,String userName,String password,String sourceName){
		
		this.sourceName=sourceName;
		this.url=url;
		this.userName=userName;
		this.password=password;
	}
	
	public void insert() throws SQLException{
		
		Connection con =OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		String sql = "select * from sy$sources";
		ResultSet set = stat.executeQuery(sql);
		int i = 0;
		boolean exit=false;
		while(set.next()){
			
			String name = set.getString("NAME");
			int t = set.getInt("BITNUM");
			if(name.equalsIgnoreCase(sourceName))
				exit=true;
			if(t>=i)
				i=t+1;
		}
		this.in=this.in0+this.sourceName+this.in1+i+this.in2;
		System.out.println(this.in);
		if(!exit){
			stat.execute(this.in);
		}
		stat.close();
		con.close();
	}
	
}
