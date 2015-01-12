package com.jtang.monitor.delete.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import com.jtang.monitor.delete.MonitorDeleteThread;
import com.jtang.monitor.delete.OracleConnection;
import com.jtang.syn.comm.Message;

public class OracleConnectionTest {

	
	public static void main(String args[]){
		
		new TestThread().start();
		
		
	}
	
	
	@Test
	public void testGetConnection(){
		Connection con=OracleConnection.getConnection();
		if(con==null){
			System.out.println("failed");
		}else{
			System.out.println("ok");
			
		} 	
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OracleConnection.closeConnection();
	}
	@Test
	public void testMonitorDelete(){
		ConcurrentLinkedQueue<Message>queue = new ConcurrentLinkedQueue<Message>();
		Thread t = new MonitorDeleteThread(queue);
		t.start();	
		while(true){
		}
	}
	
}

class TestThread extends Thread{
	
	public void run(){	
		while(true){
			
			try {
				String sql = "insert into sy$delog(ID,ISSYN) VALUES(12,0)";
				Connection con = OracleConnection.getConnection();
				try {
					Statement stmt = con.createStatement();
					stmt.execute(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
}

