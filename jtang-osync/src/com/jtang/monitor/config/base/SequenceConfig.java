package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jtang.monitor.config.base.OracleConnection;
import com.jtang.monitor.config.base.SequenceConfig;

public class SequenceConfig {
	
	public static final String s1 ="CREATE SEQUENCE vercontrol_seq "+
	 "INCREMENT BY 1 "+
	 "START WITH 101 "+
	 "MAXVALUE 999999999999999999999999999 "+
	 "MINVALUE 1 "+
	 "NOCYCLE "+
	 "NOCACHE ";


	public static final String s2 = "create sequence SY$SCN_SEQ increment by 1 start with 1000 "+
	 " maxvalue 999999999999999999999999999 minvalue 1000 nocycle cache 20";
	
	
	public static boolean createSequences(String url,String userName,String password) throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		List<String> list = SequenceConfig.getSequences(url, userName, password);
		String d1 = "drop sequence vercontrol_seq";
		String d2 = "drop sequence SY$SCN_SEQ";
		for (String string : list) {
			if(string.equalsIgnoreCase("vercontrol_seq")){
				stat.execute(d1);
			}else if(string.equalsIgnoreCase("SY$SCN_SEQ")){
				stat.execute(d2);
			}
		}
		stat.execute(s1);
		stat.execute(s2);
		
		return true;
	}
	public static List<String> getSequences(String url,String userName,String password) throws SQLException{
		
		List<String> list = new ArrayList<String>();
		String sql = " select  SEQUENCE_NAME from user_sequences";
		Connection con = OracleConnection.getConnection(url, userName, password);
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql);
		while(set.next()){	
			list.add(set.getString("SEQUENCE_NAME"));
		}
		return list;
	}
	
	public static void main(String args[]){
		
		System.out.println(s1);
		System.out.println(s2);
		
	}
	
	

}
