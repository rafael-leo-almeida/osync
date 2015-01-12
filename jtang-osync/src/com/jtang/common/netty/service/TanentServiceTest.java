package com.jtang.common.netty.service;

import org.junit.Test;

import com.jtang.common.netty.HttpSnoopServer;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;


public class TanentServiceTest {

	@Test
	public void testGetAllTanent(){
		
		TenantManager tanentPool = new TenantManager();
		Tenant tanent1 = new Tenant("127.0.0.1", 1521, "orcl", "chen", "chen");
		System.out.println(tanent1);
		Tenant tanent2 = new Tenant("127.0.0.1", 1521, "orcl", "zhang", "zhang");
		//tanentPool.addTanent(tanent1);
		//tanentPool.addTanent(tanent2);
		HttpSnoopServer server = new HttpSnoopServer(8081, tanentPool,null);
		server.start();
		
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
