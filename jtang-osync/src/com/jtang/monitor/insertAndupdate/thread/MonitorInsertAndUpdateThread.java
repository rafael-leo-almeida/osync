package com.jtang.monitor.insertAndupdate.thread;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.common.table.SourceInfo;
import com.jtang.monitor.insertAndupdate.base.MonitorTableManager;
import com.jtang.monitor.insertAndupdate.base.MonitorTxlog;
import com.jtang.monitor.insertAndupdate.base.OracleConnection;
import com.jtang.monitor.insertAndupdate.base.SelectData;
import com.jtang.monitor.insertAndupdate.base.Txlog;
import com.jtang.syn.comm.Message;

public class MonitorInsertAndUpdateThread extends Thread{

	private MonitorTableManager manager;
	private SourceInfo sourceInfo;
	private String url;
	private String userName;
	private String password;
	private Connection con;
	private MonitorTxlog monitorTxlog;
	private SelectData select;
	private int seconds= 100;
	private BigInteger fromScn=new BigInteger("0");
	private ConcurrentLinkedQueue<Message> queue;
	public MonitorInsertAndUpdateThread(MonitorTableManager monitorTableManager,SourceInfo sourceInfo,String userName,String password,
			ConcurrentLinkedQueue<Message>queue) throws SQLException{
		this.manager=monitorTableManager;
		this.sourceInfo=sourceInfo;
		this.userName=userName;
		this.password=password;
		this.queue = queue;
		this.url="jdbc:oracle:thin:@"+this.sourceInfo.getIp()+":"+this.sourceInfo.getPort()+":"+this.sourceInfo.getDBName();
	    this.con=OracleConnection.getConnection(this.url, this.userName, this.password);
	    this.monitorTxlog = new MonitorTxlog(this.con);
	    this.select = new SelectData(this.sourceInfo, this.con, this.manager);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
			
			List<Txlog> txlogs = new ArrayList<Txlog>();
			try {
				txlogs = this.monitorTxlog.monitor(this.fromScn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Txlog txlog : txlogs) {
				
				System.out.println(txlog);
				if(this.fromScn.compareTo(txlog.getScn())==-1)
					this.fromScn=txlog.getScn();
				List<Message> datas = new ArrayList<Message>();
				try {
					datas = this.select.select(txlog);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Message message : datas) {
					message.setMonitorTime(new Date().getTime());
					queue.offer(message);
					//System.out.println("msg:"+message);
				}
			}
			try {
				Thread.sleep(this.seconds);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
}
