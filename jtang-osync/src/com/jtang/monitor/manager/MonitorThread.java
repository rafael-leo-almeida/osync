package com.jtang.monitor.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.monitor.delete.MonitorDeleteThread;
import com.jtang.monitor.insertAndupdate.base.MonitorTableManager;
import com.jtang.monitor.insertAndupdate.thread.MonitorInsertAndUpdateThread;
import com.jtang.syn.comm.Message;
import com.jtang.syn.comm.OracleConnection;
import com.jtang.syn.netty.ClientManager;

public class MonitorThread extends Thread{

	public Tenant tanent;
	public ConcurrentLinkedQueue<Message> queue;
	public TableMapManager tableMaps;
	public ClientManager clientManager;
	private MonitorTableManager monitorInsertAndUpdateManager=new MonitorTableManager();
	private MonitorTableManager monitorDeleteManager=new MonitorTableManager();
	private MonitorInsertAndUpdateThread monitorInsertAndUpdateThread;
	private MonitorDeleteThread monitorDeleteThread;
    private SourceInfo sourceInfo;
    private long threadId;
    public MonitorThread(Tenant tanent,ConcurrentLinkedQueue<Message>queue,TableMapManager talbeMaps,ClientManager clientManager) throws SQLException{
    	
    	this.tanent=tanent;
    	this.queue=queue;
    	this.tableMaps=talbeMaps;
    	this.clientManager=clientManager;
    	this.sourceInfo=new SourceInfo(tanent.getIp(), tanent.getPort(), tanent.getDBName(), "all", tanent.getUserName());
    	this.monitorInsertAndUpdateThread = new MonitorInsertAndUpdateThread(monitorInsertAndUpdateManager, sourceInfo, this.tanent.getUserName(), this.tanent.getPassword(), this.queue);
    	this.monitorDeleteThread = new MonitorDeleteThread(monitorDeleteManager, sourceInfo, this.tanent.getUserName(), this.tanent.getPassword(), this.queue);
    }
    
    public Tenant getTanent() {
		return tanent;
	}

	public void setTanent(Tenant tanent) {
		this.tanent = tanent;
	}

	public ConcurrentLinkedQueue<Message> getQueue() {
		return queue;
	}

	public void setQueue(ConcurrentLinkedQueue<Message> queue) {
		this.queue = queue;
	}

	public TableMapManager getTableMaps() {
		return tableMaps;
	}

	public void setTableMaps(TableMapManager tableMaps) {
		this.tableMaps = tableMaps;
	}

	public ClientManager getClientManager() {
		return clientManager;
	}

	public void setClientManager(ClientManager clientManager) {
		this.clientManager = clientManager;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public void run(){
    	super.run();
    	this.monitorDeleteThread.start();
    	this.monitorInsertAndUpdateThread.start();
    	this.threadId=Thread.currentThread().getId();
    	while(true){
    		try {
				Thread.currentThread().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
	
    public boolean addMonitorInserAndUpdateTable(TableInfo table) throws SQLException{
    	
    	if(!this.isExistTable(table.getTableName()))
    		return false;
    	this.monitorInsertAndUpdateManager.addMonitorTable(table);
    	return true;
    }
    
    public boolean addMonitorDeleteTable(TableInfo table) throws SQLException{
    	
    	if(!this.isExistTable(table.getTableName())){
    		return false;
    	}
    	this.monitorDeleteManager.addMonitorTable(table);
    	return true;
    
    }
    
    public boolean isExistTable(String tableName) throws SQLException{
    	
    	Connection con = OracleConnection.getConnection(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
    	Statement stat = con.createStatement();
    	ResultSet set = stat.executeQuery("select table_name from user_tables");
    	while(set.next()){
    		if(set.getString("table_name").equalsIgnoreCase(tableName)){
    			stat.close();
    			con.close();
    			return true;
    		}
    	}
    	return false;
    }

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof MonitorThread)){
			return false;
		}
		MonitorThread t = (MonitorThread) obj;
		if(t.getTanent().equals(this.getTanent())){
			return true;
		}
		return false;
	}
    
    
	
	
	
}
