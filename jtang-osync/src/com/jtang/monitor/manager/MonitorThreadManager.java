package com.jtang.monitor.manager;

import java.util.Vector;

import com.jtang.common.tenant.Tenant;

public class MonitorThreadManager {

	private Vector<MonitorThread> threads = new Vector<MonitorThread>();
	
	public MonitorThread getMonitorThreadByTanent(Tenant tanent){	
		int size = this.threads.size();
		for(int i=0;i<size;i++){
			MonitorThread t = this.threads.get(i);
			if(t.getTanent().equals(tanent)){
				return t;
			}
		}
		return null;
	}
	public void addMonitorThread(MonitorThread thread){
		
		if(this.getMonitorThreadByTanent(thread.getTanent())==null){
			this.threads.add(thread);
			thread.start();
		}
		
	}
	
	
	
	
}
