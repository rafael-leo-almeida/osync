package com.jtang.syn.netty;

import java.util.Vector;

import org.json.JSONArray;

import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.TenantManager;
import com.jtang.monitor.manager.MonitorThreadManager;

public class StaticInfo {

	public static TenantManager tanentPool;
	public static ClientManager clientManager;
	public static TableMapManager tableMaps;
	public static MonitorThreadManager monitorThreadManager;
	
	
	public static String getClients(){
		
		Vector<ClientDescription> clients = clientManager.getClients();
		JSONArray json = new JSONArray(clients);
		return json.toString();
	}
	
	public static void main(String args[]){
		
		Vector<String> strs = new Vector<String>();
		strs.add("chen");
		strs.add("yang");
		strs.add("lei");
		JSONArray json = new JSONArray(strs);
		System.out.println(json.toString());
		
	}
	
}
