package com.jtang.monitor.insertAndupdate.test;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.monitor.insertAndupdate.base.MonitorTableManager;
import com.jtang.monitor.insertAndupdate.base.MonitorTxlog;
import com.jtang.monitor.insertAndupdate.base.OracleConnection;
import com.jtang.monitor.insertAndupdate.base.SelectData;
import com.jtang.monitor.insertAndupdate.base.Txlog;
import com.jtang.monitor.insertAndupdate.thread.MonitorInsertAndUpdateThread;
import com.jtang.syn.comm.Message;


public class BaseTest {

	Connection con = OracleConnection.getConnection(null, "zhang", "zhang");
	MonitorTxlog txlog = new MonitorTxlog(con);
	
	@Test
	public void testMonitorTxlog() throws SQLException{
		Map<String,String> values = new HashMap<String,String>();
		values.put("viewName", "students");
		List<Txlog> list = txlog.monitor(new BigInteger("0"));
		TableInfo teachers = new CreateSchema(values).getSchemaWithDB("teachers",this.con);
		TableInfo students = new CreateSchema(values).getSchemaWithDB("students",this.con);
		MonitorTableManager manager = new MonitorTableManager();
		manager.addMonitorTable(students);
		manager.addMonitorTable(teachers);
		SourceInfo sourceInfo= new SourceInfo("localhost", 1521, "orcl", "students","zhang");
		SelectData selectData = new SelectData(sourceInfo, con, manager);
		ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();
//	    for (Txlog txlog : list) {
//	    	
//			System.out.println(txlog);
//			System.out.println("\n");
//			List<Message> datas = selectData.select(txlog);
//			for (Message message : datas) {
//			     System.out.println(message);	
//			}
//			System.out.println("\n\n\n");
//			
//		}
	    MonitorInsertAndUpdateThread thread = new MonitorInsertAndUpdateThread(manager, sourceInfo, "zhang", "zhang",queue);
	    thread.start();
	    try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
