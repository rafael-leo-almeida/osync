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

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.table.DataTranslator;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableMap;
import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;
import com.jtang.syn.comm.Message;
import com.jtang.syn.comm.OperateDB;
import com.jtang.syn.comm.OracleConnection;
import com.jtang.syn.comm.ReturnMsg;


/**
 * Handles a server-side channel.
 */
public class SynServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = Logger.getLogger(
            SynServerHandler.class.getName());
    ConcurrentLinkedQueue<String> jsonStrings;
    private TableMapManager tableMaps;
    private String url;
    private String userName;
    private String password;
    private Connection con;
    private ConPool conPool = new ConPool();
    private TenantManager tanentPool ;
    public SynServerHandler(ConcurrentLinkedQueue<String> jsonStrins){
    	this.jsonStrings = jsonStrins;
    }
    
    public SynServerHandler(ConcurrentLinkedQueue<String> jsonStrings,TableMapManager tableMaps,String url,
    		String userName,String password){
    	this.jsonStrings=jsonStrings;
    	this.tableMaps=tableMaps;
    	this.url=url;
    	this.userName=userName;
    	this.password=password;
    	this.con=OracleConnection.getConnection(this.url, this.userName, this.password);
    
    }
    
    public SynServerHandler(ConcurrentLinkedQueue<String> jsonStrings,TableMapManager tableMaps,TenantManager tanentPool){
    	this.jsonStrings=jsonStrings;
    	this.tableMaps=tableMaps;
        this.tanentPool = tanentPool;
    }
    @Override
    public void handleUpstream(
            ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
 // Send greeting for a new connection.
//        e.getChannel().write(
//                "Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
//        e.getChannel().write("It is " + new Date() + " now.\r\n");
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        // Cast to a String first.
        // We know it is a String because we put some codec in TelnetPipelineFactory.
        String requestJson = (String) e.getMessage();
        System.out.println("received Message:\n "+requestJson);
        if(this.jsonStrings!=null){
            this.jsonStrings.offer(requestJson);
        }
        Message info = null;
		try {
			info = new Message(requestJson);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int resFlag = 0;
		long receiveTime = new Date().getTime();
        try {
        	List<TableMap> maps = this.tableMaps.getTableMapsBySourceInfo(info.getSourceInfo());
        	if(maps==null||maps.size()==0)
        		System.out.println("maps is null");
        	System.out.println(info.getSourceInfo());
        	for (TableMap tableMap : maps) {
        		
        		Message msg = DataTranslator.transData(info, tableMap);
        		SourceInfo sourceInfo = tableMap.getToTable().getSourceInfo();
        		Tenant tanent = this.tanentPool.getTanentByOthers(sourceInfo.getIp(), sourceInfo.getPort(), sourceInfo.getDBName(), sourceInfo.getUserName());
        		resFlag=new OperateDB(msg, tableMap,this.conPool.getCon(tanent.getUrl(), tanent.getUserName(), tanent.getPassword())).synData();
			
        	}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        // Generate and write a response.
        String response = "";
        ReturnMsg returnMsg = new ReturnMsg();
        returnMsg.setStatus(resFlag);
        returnMsg.setRecordId(info.getRecordId());
        returnMsg.setReceiveTime(receiveTime);
        response = returnMsg.toString();
        e.getChannel().write(response+"\r\n");
        System.out.println("sendReturnMsg:"+response);

    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
    
    class ConItem{
    	
    	Connection con;
    	String url;
    	String userName;
    	String password;
    	
    	public ConItem(Connection con,String url,String userName,String password){
    		this.con=con;
    		this.url=url;
    		this.userName=userName;
    		this.password=password;
    	}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			if(!(obj instanceof ConItem)){
				return false;
			}
			ConItem conItem = (ConItem)obj;
			if(conItem.url.equalsIgnoreCase(this.url)&&
					conItem.userName.equals(this.userName)&&
					conItem.password.equals(this.password)){
				return true;
			}
			return false;
		}
    }
    
    class ConPool{
    	
    	List<ConItem> cons = new ArrayList<ConItem>();
    	public Connection getCon(String url,String userName,String password){
    		
    		int size = this.cons.size();
    		ConItem con = new ConItem(null, url, userName, password);
    		for(int i=0;i<size;i++){
    			if(con.equals(this.cons.get(i)))
    				return this.cons.get(i).con;
    		}
    		Connection c = OracleConnection.getConnection(url, userName, password);
    		con.con=c;
    		this.cons.add(con);
    		return con.con;
    	}
      	
    }
    
    
}
