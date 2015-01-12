/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.jtang.syn.netty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.channel.Channel;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jtang.common.record.Record;
import com.jtang.common.record.RecordDaoImpl;
import com.jtang.common.table.TableItem;
import com.jtang.common.table.TableMap;
import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;
import com.jtang.syn.comm.Message;
import com.jtang.syn.comm.OracleConnection;


/**
 * Simplistic telnet client.
 */
public class SynClient extends Thread {

	private  String host;
	private int port;
	private ConcurrentLinkedQueue<Message> queue;
	private TableMap tableMap;
	private ClientManager clientManager;
	private ChannelManager channelManager;
	private TableMapManager tableMaps;
	private String userName = "person";
	private String password = "person";
	private int millSeconds=1;
    private TenantManager tanentPool;
    RecordDaoImpl recordDao = new RecordDaoImpl();
	public SynClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public SynClient(String host, int port,
			ConcurrentLinkedQueue<Message> queue, TableMap tableMap) {
		this.host = host;
		this.port = port;
		this.queue = queue;
		this.tableMap = tableMap;
	}

	public SynClient(String host, int port,
			ConcurrentLinkedQueue<Message> queue, TableMap tableMap,
			String userName, String password) {

		this.host = host;
		this.port = port;
		this.queue = queue;
		this.userName = userName;
		this.password = password;
		this.tableMap = tableMap;

	}
	public SynClient(String userName, String password, ClientManager clientManager,ConcurrentLinkedQueue<Message> queue, 
			TableMapManager tableMaps,ChannelManager channelManager) {
		
		this.queue = queue;
		this.userName = userName;
		this.password = password;
		this.tableMaps = tableMaps;
		this.clientManager = clientManager;
		this.channelManager = channelManager;

	}

	public SynClient(TenantManager tanentPool, ClientManager clientManager,ConcurrentLinkedQueue<Message> queue, 
			TableMapManager tableMaps,ChannelManager channelManager) {
		
		this.queue = queue;
		this.tanentPool=tanentPool;
		this.tableMaps = tableMaps;
		this.clientManager = clientManager;
		this.channelManager = channelManager;

	}
	/*
	public void run() {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Configure the pipeline factory.
		bootstrap.setPipelineFactory(new SynClientPipelineFactory());

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));

		// Wait until the connection attempt succeeds or fails.
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		// Read commands from the stdin.
		ChannelFuture lastWriteFuture = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		for (;;) {
			if (!this.queue.isEmpty()) {

				Message msg = this.queue.poll();
				String str = new JSONObject(msg).toString();
				// 对delete的操作，有待完善
				if (msg.getOperation().equalsIgnoreCase("delete")) {

					StringBuilder url = new StringBuilder("jdbc:oracle:thin:@");
					url.append(this.tableMap.getFromTable().getSourceInfo()
							.getIp());
					url.append(":");
					url.append(this.tableMap.getFromTable().getSourceInfo()
							.getPort());
					url.append(":");
					url.append(this.tableMap.getFromTable().getSourceInfo()
							.getDBName());
					Connection con = OracleConnection.getConnection(
							url.toString(), this.userName, this.password);
					try {
						Statement stmt = con.createStatement();
						String sql = "select * from "
								+ this.tableMap.getFromTable().getTableInfo()
										.getTableName()
								+ msg.getDeleteSqlWhere();
						ResultSet set = stmt.executeQuery(sql);
						if (!set.next()) {
							lastWriteFuture = channel.write(str + "\r\n");
						} else {
							continue;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					lastWriteFuture = channel.write(str + "\r\n");
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (str.equals("bye"))
					break;
			}
		}
    
		// Wait until all messages are flushed before closing the channel.
		if (lastWriteFuture != null) {
			lastWriteFuture.awaitUninterruptibly();
		}

		// Close the connection. Make sure the close operation ends because
		// all I/O operations are asynchronous in Netty.
		channel.close().awaitUninterruptibly();

		// Shut down all thread pools to exit.
		bootstrap.releaseExternalResources();
	}
 */
	public static void main(String[] args) throws Exception {
		// Print usage if no argument is specified.
		if (args.length != 2) {
			System.err.println("Usage: " + SynClient.class.getSimpleName()
					+ " <host> <port>");
			return;
		}

		// Parse options.
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		new SynClient(host, port).run();
	}
	public void sendMessage(Message msg) {

		
		List<TableMap> tableMapList = this.tableMaps
				.getTableMapsBySourceInfo(msg.getSourceInfo());
		//System.out.println("operation:"+msg.getOperation());
		for (TableMap tableMap : tableMapList) {

			TableItem toTable = tableMap.getToTable();
			ClientDescription client = this.clientManager
					.getClientByTableItem(toTable);
			if (client == null)
				continue;

			Channel channel = this.channelManager.getChannelByIpAndPort(
					client.getWatchIp(), client.getWatchPort());
			if (channel == null)
				continue;
			
			//同步记录
			Record record = new Record();
			record.setGetTime(new Date(msg.getMonitorTime()));
			record.setKeyValue(new JSONArray(tableMap.getFromTable().getTableInfo().getKeys()).toString());
			record.setMapId(tableMap.getId());
			record.setSendTime(new Date());
			record.setStatus(0);
			if(msg.getOperation().equalsIgnoreCase("delete")){
				record.setType(0);
			}else{
				//先将insert类型和update类型都设置成为更新类型
				//待返回的时候再判断是插入类型还是更新类型
				record.setType(1);
			}
			record = this.recordDao.add(record);
			if(record!=null){
				msg.setRecordId(record.getId());
			}
			String str = new JSONObject(msg).toString();
			// 对delete的操作
			if (msg.getOperation().equalsIgnoreCase("delete")) {

				StringBuilder url = new StringBuilder("jdbc:oracle:thin:@");
				url.append(msg.getSourceInfo().getIp());
				url.append(":");
				url.append(msg.getSourceInfo()
						.getPort());
				url.append(":");
				url.append(msg.getSourceInfo()
						.getDBName());
				Tenant tanent = this.tanentPool.getTanentByOthers(msg.getSourceInfo().getIp(), msg.getSourceInfo().getPort(),
						msg.getSourceInfo().getDBName(), msg.getSourceInfo().getUserName());
				//System.out.println(url.toString());
				Connection con = OracleConnection.getConnection(url.toString(),
						tanent.getUserName(), tanent.getPassword());
				try {
					Statement stmt = con.createStatement();
					String sql = "select * from "
							+ msg.getSourceInfo().getTableName()+" " + msg.getDeleteSqlWhere();
					//System.out.println(sql);
					ResultSet set = stmt.executeQuery(sql);
					if (!set.next()) {
						
						System.out.println("sendMessage:\n"+msg);
						channel.write(str + "\r\n");
						
					} else {
						continue;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				
				System.out.println("sendMessage:\n"+msg);
				channel.write(str + "\r\n");
				
			}
		}
	}
	public void monitorMessage(){
		while(true){
			
			if(!(this.queue.isEmpty())){
				
				Message msg= this.queue.poll();
				this.sendMessage(msg);
				//System.out.println("synClient: "+msg);
				try {
					Thread.sleep(this.millSeconds);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	public void run(){
		
		this.monitorMessage();
	
	}
	

}
