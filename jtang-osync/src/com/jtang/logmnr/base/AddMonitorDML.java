package com.jtang.logmnr.base;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMonitorDML {

	//��oracle 11g �б���ִ����sql���Ż���dml����
	public static boolean monitorDML(){
		 String sql = "ALTER DATABASE ADD SUPPLEMENTAL LOG DATA (PRIMARY KEY, UNIQUE INDEX) COLUMNS";
		 Connection con = ConnectionFactory.getConnection();
		 if(con==null){
			 return false;
		 }
		 PreparedStatement pStat = null;
		 try {
			pStat = con.prepareStatement(sql);
			pStat.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
		    if(pStat!=null){
		    	try {
					pStat.close();
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
	
	public static boolean monitorDML(LogmnrConnection logmnrCon){
		 String sql = "ALTER DATABASE ADD SUPPLEMENTAL LOG DATA (PRIMARY KEY, UNIQUE INDEX) COLUMNS";
		 if(logmnrCon==null){
			 return false;
		 }
		 PreparedStatement pStat = null;
		 try {
			pStat = logmnrCon.getCon().prepareStatement(sql);
			pStat.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
		    if(pStat!=null){
		    	try {
					pStat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		}
	    return true;
	}
	
	
	
}
