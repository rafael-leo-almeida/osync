package com.jtang.monitor.manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;

import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.syn.comm.Message;
import com.jtang.syn.netty.ClientManager;


public class MonitorCommandThread extends Thread{

	private LinkedBlockingQueue<Command> commandQueue;
    private MonitorThreadManager monitorThreadManager;
    private ConcurrentLinkedQueue<Message> queue;
    private TableMapManager tableMaps;
    private ClientManager clientManager;
    
    public MonitorCommandThread(LinkedBlockingQueue<Command> commandQueue,MonitorThreadManager monitorThreadManager,
    		ConcurrentLinkedQueue<Message>queue,TableMapManager tableMaps,ClientManager clientManager){
    	this.commandQueue=commandQueue;
    	this.monitorThreadManager=monitorThreadManager;
    	this.queue=queue;
    	this.tableMaps=tableMaps;
    	this.clientManager=clientManager;
    }

	public void run(){
		
		while(true){
			
			Command cmd = this.commandQueue.poll();
			if(cmd!=null&&cmd.getPath().equalsIgnoreCase("/monitor/addNewMonitorSource")){
				Map<String,List<String>> params = cmd.getParams();
				if(params==null)continue;
				List<String>ps0=params.get("tanent");
				List<String>ps1=params.get("tableName");
				if(ps0==null||ps1==null)continue;
				String tanentJson = ps0.get(0);
				String tableName=ps1.get(0);
				if(tanentJson==null||tanentJson.length()==0||
						tableName==null||tableName.length()==0)continue;
				try {
					Tenant tanent = new Tenant(tanentJson);
					MonitorService.addNewMonitorSource(monitorThreadManager, queue, tableMaps, clientManager, tanent, tableName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
				
			}
			
		}
		
	}
	
	
	
	
	
	
}
