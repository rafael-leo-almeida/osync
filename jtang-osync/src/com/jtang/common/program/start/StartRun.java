package com.jtang.common.program.start;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONException;
import com.jtang.common.netty.HttpSnoopServer;
import com.jtang.common.netty.service.TableMapService;
import com.jtang.common.record.MapJson;
import com.jtang.common.record.MapJsonDaoImpl;
import com.jtang.common.record.TenantDaoImpl;
import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableItem;
import com.jtang.common.table.TableMap;
import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;
import com.jtang.logmnr.base.LogmnrBase;
import com.jtang.monitor.config.base.OracleConnection;
import com.jtang.monitor.manager.Command;
import com.jtang.monitor.manager.MonitorCommandThread;
import com.jtang.monitor.manager.MonitorService;
import com.jtang.monitor.manager.MonitorThread;
import com.jtang.monitor.manager.MonitorThreadManager;
import com.jtang.syn.comm.Message;
import com.jtang.syn.netty.ChannelManager;
import com.jtang.syn.netty.ClientDescription;
import com.jtang.syn.netty.ClientManager;
import com.jtang.syn.netty.DBDescription;
import com.jtang.syn.netty.StaticInfo;
import com.jtang.syn.netty.SynClient;
import com.jtang.syn.netty.SynServer;


public class StartRun extends Thread{
	
	
    public ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();
    public ClientManager clientManager;
    public TableMapManager tableMaps = new TableMapManager();
    public LinkedBlockingQueue<Command> commandQueue = new LinkedBlockingQueue<Command>();
	public ChannelManager channelManager = new ChannelManager();
	public MonitorThreadManager monitorThreadManager = new MonitorThreadManager();
	public TenantManager tanentPool = new TenantManager();
    public MonitorCommandThread monitorCommandThread;
    public HttpSnoopServer httpSoopServer;
    public SynClient synClient;
    public SynServer synServer;
    //public JettyServerStart jettyServerStart;
    
    public StartRun(int watchPort,int httpPort,int consolePort){
    	//自身是一个客户端
    	this.clientManager = new ClientManager(watchPort);
    	this.monitorCommandThread=new MonitorCommandThread(commandQueue, monitorThreadManager, queue, tableMaps, clientManager);
        this.httpSoopServer = new HttpSnoopServer(httpPort, tanentPool, commandQueue);
        if(this.queue==null){
        	System.out.println("queue is null");
        }
        this.synClient=new SynClient(tanentPool, clientManager, this.queue, this.tableMaps, this.channelManager);
        this.synServer = new SynServer(watchPort, null, tableMaps, tanentPool);
        //this.jettyServerStart=new JettyServerStart(consolePort);
        
        
        LogmnrBase.queue = queue;
        //对tablemapservice 的静态变量进行初始化
        TableMapService.tanentPool = this.tanentPool;
        TableMapService.clientManager = this.clientManager;
        TableMapService.tableMaps = this.tableMaps;
        TableMapService.monitorThreadManager = this.monitorThreadManager;
        TableMapService.queue = this.queue;
        //初始化staticInfo
        StaticInfo.tanentPool = this.tanentPool;
        StaticInfo.clientManager = this.clientManager;
        StaticInfo.monitorThreadManager = this.monitorThreadManager;
        StaticInfo.tableMaps = this.tableMaps;
        //从数据库中初始化租户管理
        //初始化tanentManager
        List<Tenant> tanents = new TenantDaoImpl().getAllTenants(); 
        if(tanents!=null){
        	for (Tenant tenant : tanents) {
				this.tanentPool.initAddTanent(tenant);
			}
        }
        //初始化tablemapmanager
        List<MapJson> mapJsons = new MapJsonDaoImpl().getAllMap();
        if(mapJsons!=null){
        	for (MapJson mapJson : mapJsons) {
				TableMap tableMap = null;
				try {
					tableMap = new TableMap(mapJson.getMapJson());
					tableMap.setId(mapJson.getId());
					//开始启动时都设置为停止状态
					tableMap.setStatus(0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(tableMap!=null){
					this.tableMaps.initAdd(tableMap);
				}
			}
        }
        //初始化clientmanager
        for (TableMap tableMap : this.tableMaps.getTableMaps()) {
			this.clientManager.addClientByTableItem(tableMap.getToTable());
		}
        MonitorService.initMonitorService(tanentPool, tableMaps, 
        		monitorThreadManager, queue, clientManager);
        
    }
    
	public  static void main(String[] args) throws SQLException{
		
		StartRun startRun = new StartRun(8880,8889,9000);
		startRun.start();
		//startRun.test();
		//startRun.testAddMonitorThread();
		
		
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    public void run(){
    	
    	this.httpSoopServer.start();
    	this.monitorCommandThread.start();
    	this.synServer.start();
    	this.synClient.start();
    	//this.jettyServerStart.start();
    	while(true){
    		
    		try {
				Thread.currentThread().join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
	public void test() throws SQLException{
		
		SourceInfo sourceInfo = new SourceInfo();
		sourceInfo.setDBName("orcl");
		sourceInfo.setIp("127.0.0.1");
		sourceInfo.setPort(1521);
		sourceInfo.setTableName("sy$destudents");
		String userName = "chen";
		String password = "chen";
		String url="jdbc:oracle:thin:@localhost:1521:orcl";
	    
		Connection con = OracleConnection.getConnection(url, userName, password);
		TableInfo table1 = new CreateSchema(null).getSchemaWithDB("students", con);
		TableInfo table2 = new CreateSchema(null).getSchemaWithDB("teachers", con);
		TableInfo table3 = new CreateSchema(null).getSchemaWithDB("sy$destudents", con);
		TableInfo table4 = new CreateSchema(null).getSchemaWithDB("workers", con);
		
		ClientDescription client = new ClientDescription();
		client.setWatchIp("127.0.0.1");
		client.setWatchPort(8880);
		DBDescription db = new DBDescription();
		db.setIp("127.0.0.1");
		db.setPort(1521);
		db.setDBName("orcl");
		db.addTable(table1);
		db.addTable(table2);
		db.addTable(table3);
		db.addTable(table4);
		client.addDB(db);
		this.clientManager.addClient(client);
		
		TableMap tableMap1 = new TableMap();
	    TableItem fromItem1 = new TableItem();
	    TableItem toItem1 = new TableItem();
	    SourceInfo fromSourceInfo = new SourceInfo();
	    SourceInfo toSourceInfo = new SourceInfo();
	    TableInfo fromTable1 = new CreateSchema(null).getSchemaWithDB("students", con);
	    TableInfo toTable1 = new CreateSchema(null).getSchemaWithDB("workers", con);
	    fromSourceInfo.setIp("127.0.0.1");
	    fromSourceInfo.setPort(1521);
	    fromSourceInfo.setDBName("orcl");
	    fromSourceInfo.setTableName("students");
	    fromSourceInfo.setUserName("chen");
	    fromItem1.setSourceInfo(fromSourceInfo);
		fromItem1.setTableInfo(fromTable1);
		toSourceInfo.setIp("127.0.0.1");
		toSourceInfo.setPort(1521);
		toSourceInfo.setDBName("orcl");
		toSourceInfo.setTableName("workers");
		toSourceInfo.setUserName("chen");
		toItem1.setSourceInfo(toSourceInfo);
		toItem1.setTableInfo(toTable1);
		tableMap1.setFromTable(fromItem1);
		tableMap1.setToTable(toItem1);
		List<String> valiedFields = new ArrayList<String>();
		valiedFields.add("ID");
		valiedFields.add("NAME");
		Map<String,String> fieldsMap = new HashMap<String, String>();
		fieldsMap.put("ID", "ID");
		fieldsMap.put("NAME", "NAME");
		tableMap1.setValiFields(valiedFields);
		tableMap1.setFieldsMap(fieldsMap);
		this.tableMaps.add(tableMap1);

		Tenant tanent = new Tenant("127.0.0.1", 1521, "orcl", "chen", "chen");
		this.tanentPool.addTanent(tanent);
		MonitorThread thread = new MonitorThread(tanent, this.queue, this.tableMaps, this.clientManager);
		//MonitorService.addNewMonitorSource(monitorThreadManager, queue, tableMaps, clientManager, tanent, "students");
		thread.addMonitorInserAndUpdateTable(table1);
		thread.addMonitorDeleteTable(table3);
		this.monitorThreadManager.addMonitorThread(thread);
		
	}
	
	public void testAddMonitorThread() throws SQLException{
		
		Tenant tanent = new Tenant("127.0.0.1", 1521, "orcl", "chen", "chen");
		this.tanentPool.addTanent(tanent);
		MonitorThread thread = new MonitorThread(tanent, this.queue, this.tableMaps, this.clientManager);
		MonitorService.addNewMonitorSource(monitorThreadManager, queue, tableMaps, clientManager, tanent, "students");
		this.monitorThreadManager.addMonitorThread(thread);
		
	}
	
	
	
}
