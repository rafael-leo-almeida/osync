package com.jtang.logmnr.base;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDictionary {
    //��־�ļ�filename��ƣ����ʽΪ*.data
	//path ��ʾ�洢����ֵ��Ŀ¼��ע����Ҫ�����
	public static boolean createNewDict(String fileName,String path){
		
		Connection con = ConnectionFactory.getConnection();
	    if(con==null){
	    	return false;
	    }
		String sql = "dbms_logmnr_d.build(dictionary_filename => '"+fileName+
				"',dictionary_location => '"+path+"')";
	    String spName="{call "+sql+"}";  
	    CallableStatement cstmt= null;  
		
		try {
			cstmt = con.prepareCall(spName);
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(cstmt!=null){
				try {
					cstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
		
	}
	
	public static void main(String args[]){
		
		System.out.println(CreateDictionary.createNewDict("logmnr.dat", "D:\\oracel11g\\dictionary"));
		
	}
	
}
