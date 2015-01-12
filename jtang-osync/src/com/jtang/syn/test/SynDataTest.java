package com.jtang.syn.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import com.jtang.syn.comm.Message;
import com.jtang.syn.comm.SynDataThread;
import com.jtang.syn.netty.SynClient;
import com.jtang.syn.netty.SynServer;


public class SynDataTest {

	@Test
	public void test(){
		
		ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();
		Message info = new Message();
		info.setOperation("insert");
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("id",new Integer(51).toString());
		data.put("first_name","chen");
		data.put("last_name", "yang");
		data.put("deleted", "false");
		data.put("birth_date", "20090908 23:09:00");
		info.setDatas(data);
		queue.offer(info);
		
		SynDataThread thread = new SynDataThread(queue);
		thread.start();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testSynServer(){
		
		SynClient client = new SynClient("127.0.0.1", 8080);
		
		SynServer server = new SynServer(8080, null);
		server.run();
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		client.run();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
