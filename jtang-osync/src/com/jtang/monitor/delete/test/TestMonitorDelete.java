package com.jtang.monitor.delete.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import com.jtang.common.table.DataTranslator;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableMap;
import com.jtang.monitor.delete.MonitorDeleteThread;
import com.jtang.syn.comm.Message;


public class TestMonitorDelete {

	@Test
	public void testRun(){
		ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();
		String monitorTableName="sy$delog";
		TableMap tableMap = null;
		try {
			tableMap = TableMap.createDefaultTableMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataTranslator translator = new DataTranslator(tableMap);
		SourceInfo sourceInfo = tableMap.getFromTable().getSourceInfo();
		MonitorDeleteThread monitor = new MonitorDeleteThread(queue, monitorTableName, translator, sourceInfo);
		monitor.run();
	}
}
