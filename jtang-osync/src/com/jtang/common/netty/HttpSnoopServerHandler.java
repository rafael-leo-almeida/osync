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
package com.jtang.common.netty;

import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.netty.service.TableMapService;
import com.jtang.common.netty.service.TanentService;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;
import com.jtang.monitor.manager.Command;
import com.jtang.syn.netty.StaticInfo;

public class HttpSnoopServerHandler extends SimpleChannelUpstreamHandler {

    private static final String String = null;
	private HttpRequest request;
    private boolean readingChunks;
    /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();
    private TenantManager tanentPool;
    private LinkedBlockingQueue<Command> commandQueue;
    public HttpSnoopServerHandler(){
    	super();
    }
    public HttpSnoopServerHandler(TenantManager tanentPool,LinkedBlockingQueue<Command> commandQueue){
    	this.tanentPool=tanentPool;
    	this.commandQueue=commandQueue;
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (!readingChunks) {
            HttpRequest request = this.request = (HttpRequest) e.getMessage();

            if (is100ContinueExpected(request)) {
                send100Continue(e);
            }
            buf.setLength(0);
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
            String path = queryStringDecoder.getPath();
            Map<String, List<String>> params = queryStringDecoder.getParameters();
            if(this.tanentPool!=null){
                this.getValues(buf, path, params);
            }
            if (request.isChunked()) {
                readingChunks = true;
            } else {
                ChannelBuffer content = request.getContent();
                if (content.readable()) {
                    buf.append("CONTENT: " + content.toString(CharsetUtil.UTF_8) + "\r\n");
                }
                writeResponse(e);
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            if (chunk.isLast()) {
                readingChunks = false;
                buf.append("END OF CONTENT\r\n");
                HttpChunkTrailer trailer = (HttpChunkTrailer) chunk;
                if (!trailer.getHeaderNames().isEmpty()) {
                    buf.append("\r\n");
                    for (String name: trailer.getHeaderNames()) {
                        for (String value: trailer.getHeaders(name)) {
                            buf.append("TRAILING HEADER: " + name + " = " + value + "\r\n");
                        }
                    }
                    buf.append("\r\n");
                }
                writeResponse(e);
            } else {
                buf.append("CHUNK: " + chunk.getContent().toString(CharsetUtil.UTF_8) + "\r\n");
            }
        }
    }

    private void writeResponse(MessageEvent e) {
        // Decide whether to close the connection or not.
        boolean keepAlive = isKeepAlive(request);

        // Build the response object.
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        response.setContent(ChannelBuffers.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.setHeader(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Encode the cookie.
        String cookieString = request.getHeader(COOKIE);
        if (cookieString != null) {
            CookieDecoder cookieDecoder = new CookieDecoder();
            Set<Cookie> cookies = cookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                CookieEncoder cookieEncoder = new CookieEncoder(true);
                for (Cookie cookie : cookies) {
                    cookieEncoder.addCookie(cookie);
                    response.addHeader(SET_COOKIE, cookieEncoder.encode());
                }
            }
        } else {
            // Browser sent no cookie.  Add some.
            CookieEncoder cookieEncoder = new CookieEncoder(true);
            cookieEncoder.addCookie("key1", "value1");
            response.addHeader(SET_COOKIE, cookieEncoder.encode());
            cookieEncoder.addCookie("key2", "value2");
            response.addHeader(SET_COOKIE, cookieEncoder.encode());
        }

        // Write the response.
        ChannelFuture future = e.getChannel().write(response);

        // Close the non-keep-alive connection after the write operation is done.
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void send100Continue(MessageEvent e) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
        e.getChannel().write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
    
    public void getValues(StringBuilder builder,String uri,Map<String,List<String>> params) throws SQLException{
    	
    	if(uri==null)
    		return;
    	if(uri.equalsIgnoreCase("/tanent/getalltanents")){
    		String json = TanentService.getAllTanents(tanentPool);
    	    if(json!=null&&json.length()>0)
    	    	builder.append(json+"\r\n");
    	}else if(uri.equalsIgnoreCase("/tanent/getTanentPrivs")){
    		
    		List<String> ps= params.get("tanent");
    		if(ps==null)
    			return;
    		String json = ps.get(0);
    		if(json==null)return;
    		try {
    			Tenant tanent = new Tenant(json);
				String result = TanentService.getTanentPrivs(tanent);
				if(result!=null&&result.length()>0)
				  builder.append(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
    	}else if(uri.equalsIgnoreCase("/tanent/getTablesOfTanent")){
    		
    		List<String> ps = params.get("tanent");
    		if(ps==null)
    			return;
    		String json = ps.get(0);
    		if(json==null)return;
    		try{
    				Tenant tanent = new Tenant(json);
    				String result = TanentService.getTablesOfTanent(tanent);
    				if(result!=null&&result.length()>0)
    					builder.append(result);
    		}catch(Exception e){
    			e.printStackTrace();
    			return;
    		}
    		
    	}else if(uri.equalsIgnoreCase("/tanent/AddTanent")){
    		
    		System.out.println("request to add tanents!");
    		List<String> ps = params.get("tanent");
    		if(ps==null)
    			return;
    		String json = ps.get(0);
    		if(json==null)return;
    		try{
    				Tenant tanent = new Tenant(json);
    			    if(TanentService.AddTanent(tanentPool, tanent))
    				   builder.append("true");
    			    else 
    			       builder.append("false");
    		}catch(Exception e){
    			e.printStackTrace();
    			return;
    		}
    	}else if(uri.equalsIgnoreCase("/tanent/getAllConfigedTables")){
    		
    		List<String> ps = params.get("tanent");
    		if(ps==null)
    			return;
    		String json = ps.get(0);
    		if(json==null)return;
    		try{
    				Tenant tanent = new Tenant(json);
    			    String result = TanentService.getAllConfigedTables(tanent);
    			    if(result!=null&&result.length()>0)
    			    	builder.append(result);
    		}catch(Exception e){
    			e.printStackTrace();
    			return;
    		}
    	}else if(uri.equalsIgnoreCase("/tanent/configNewSource")){
    		List<String> ps = params.get("tanent");
    		List<String> ps2 = params.get("tableName");
    		if(ps==null)
    			return;
    		String json = ps.get(0);
    		String tableName = ps2.get(0);
    		if(json==null)return;
    		try{
    				Tenant tanent = new Tenant(json);
    			    String result = TanentService.configNewSource(tanent, tableName);
    			    if(result!=null&&result.length()>0)
    			    	builder.append(result);
    		}catch(Exception e){
    			e.printStackTrace();
    			return;
    		}
    	}else if(uri.equalsIgnoreCase("/monitor/addNewMonitorSource")){
    		Command cmd = new Command(uri, params);
    		this.commandQueue.offer(cmd);
    		builder.append("true");
    	}else if(uri.equalsIgnoreCase("/addDestinationTableMap")){
    		List<String> tableMapJsonStr = params.get("tableMapJson");
    		List<String> ps = params.get("password");
    		String password = "";
    		if(ps!=null&&ps.size()>0){
    			password = ps.get(0);
    		}
    		if(tableMapJsonStr==null||tableMapJsonStr.size()<1){
    			builder.append("false");
    			return;
    		}
    		String tableMapJson = tableMapJsonStr.get(0);
    		System.out.println("tableMapJson: "+tableMapJson);
    		if(TableMapService.addDestinationTableMap(password,tableMapJson)){
    			builder.append("true");
    		}else{
    			builder.append("false");
    		}
    	
    	}else if(uri.equalsIgnoreCase("/tableMap/addTableMap")){
    		
    		List<String> sTanentStr = params.get("sTanent");
    		if(sTanentStr==null||sTanentStr.size()<1){
    			return;
    		}
    		Tenant sTanent = null;
    		try {
				sTanent = new Tenant(sTanentStr.get(0));
			} catch (JSONException e1) {
				e1.printStackTrace();
				return;
			}
    		System.out.println("sTanent: "+sTanent.toString());
    		List<String> dTanentsStr = params.get("dTanent");
    		if(sTanentStr==null||sTanentStr.size()<1){
    			return;
    		}
    		Tenant dTanent = null;
    		try {
			    dTanent = new Tenant(dTanentsStr.get(0));
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
    		System.out.println("dTanent:"+dTanent.toString());
    		List<String> dTableInfoStr = params.get("dTableInfoJson");
    		if(dTableInfoStr==null||dTableInfoStr.size()<1){
    			return;
    		}
    		String dTableInfoJson = dTableInfoStr.get(0);
    		System.out.println("dTableInfoJson:"+dTableInfoJson);
    		List<String> valiFieldsStr = params.get("valiFields");
    		if(valiFieldsStr==null||valiFieldsStr.size()<1){
    			return;
    		}
    		List<String> valiFields = new ArrayList<String>();
    		JSONArray fArray = null;
			try {
				fArray = new JSONArray(valiFieldsStr.get(0));
				for(int i=0;i<fArray.length();i++){
	    			valiFields.add(fArray.getString(i));
	    		}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
    		System.out.println("valiFields:"+valiFieldsStr.get(0));
    		List<String> fieldsMapStr = params.get("fieldsMap");
    		if(fieldsMapStr==null||fieldsMapStr.size()<1){
    			return;
    		}
    		Map<String,String> fieldsMap = new HashMap<String, String>();
    		JSONObject obj;
			try {
				obj = new JSONObject(fieldsMapStr.get(0));
				Iterator<String> keys = obj.keys();
	    		while(keys.hasNext()){
	    			String sColumn = (String)keys.next();
	    			String dColumn = obj.getString(sColumn);
	    			fieldsMap.put(sColumn, dColumn);
	    		}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
			System.out.println("fieldsMapStr:"+fieldsMapStr);
    		List<String> sTableNames = params.get("sTableName");
    		List<String> dTableNames = params.get("dTableName");
    		if(sTableNames==null||sTableNames.size()<1){
    			return;
    		}
    		if(dTableNames==null||dTableNames.size()<1){
    			return;
    		}
    		System.out.println("sTableName:"+sTableNames);
    		System.out.println("dTableName:"+dTableNames);
    		String sTableName = sTableNames.get(0);
    		String dTableName = dTableNames.get(0);
    		String result = TableMapService.addSourceTableMap(sTanent, dTanent, 
    				sTableName, dTableName, dTableInfoJson, valiFields, fieldsMap);
    		System.out.println("result:"+result);
    		builder.append(result);
    		
    	}else if(uri.equalsIgnoreCase("/tableMap/addLogmnrSourceTableMap")){
    		
    		List<String> sTanentStr = params.get("sTanent");
    		if(sTanentStr==null||sTanentStr.size()<1){
    			return;
    		}
    		Tenant sTanent = null;
    		try {
				sTanent = new Tenant(sTanentStr.get(0));
			} catch (JSONException e1) {
				e1.printStackTrace();
				return;
			}
    		System.out.println("sTanent: "+sTanent.toString());
    		List<String> dTanentsStr = params.get("dTanent");
    		if(sTanentStr==null||sTanentStr.size()<1){
    			return;
    		}
    		Tenant dTanent = null;
    		try {
			    dTanent = new Tenant(dTanentsStr.get(0));
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
    		System.out.println("dTanent:"+dTanent.toString());
    		List<String> dTableInfoStr = params.get("dTableInfoJson");
    		if(dTableInfoStr==null||dTableInfoStr.size()<1){
    			return;
    		}
    		String dTableInfoJson = dTableInfoStr.get(0);
    		System.out.println("dTableInfoJson:"+dTableInfoJson);
    		List<String> valiFieldsStr = params.get("valiFields");
    		if(valiFieldsStr==null||valiFieldsStr.size()<1){
    			return;
    		}
    		List<String> valiFields = new ArrayList<String>();
    		JSONArray fArray = null;
			try {
				fArray = new JSONArray(valiFieldsStr.get(0));
				for(int i=0;i<fArray.length();i++){
	    			valiFields.add(fArray.getString(i));
	    		}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
    		System.out.println("valiFields:"+valiFieldsStr.get(0));
    		List<String> fieldsMapStr = params.get("fieldsMap");
    		if(fieldsMapStr==null||fieldsMapStr.size()<1){
    			return;
    		}
    		Map<String,String> fieldsMap = new HashMap<String, String>();
    		JSONObject obj;
			try {
				obj = new JSONObject(fieldsMapStr.get(0));
				Iterator<String> keys = obj.keys();
	    		while(keys.hasNext()){
	    			String sColumn = (String)keys.next();
	    			String dColumn = obj.getString(sColumn);
	    			fieldsMap.put(sColumn, dColumn);
	    		}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
			System.out.println("fieldsMapStr:"+fieldsMapStr);
    		List<String> sTableNames = params.get("sTableName");
    		List<String> dTableNames = params.get("dTableName");
    		List<String> sTableSpaces = params.get("sTableSpace");
    		List<String> dTableSpaces = params.get("dTableSpace");
    		List<String> logsPathJsons = params.get("logsPathJson");
    		List<String> dictPaths = params.get("dictPath");
    		String dictPath="";
    		if(dictPaths!=null&&dictPaths.size()>0){
    			dictPath = dictPaths.get(0);
    		}
    		String logsPathJson="[]";
    		if(logsPathJsons!=null||logsPathJsons.size()<1){
    			logsPathJson = logsPathJsons.get(0);
    		}
    		List<String> logsPath = new ArrayList<String>();
    		try {
				JSONArray array = new JSONArray(logsPathJson);
				for(int i=0;i<array.length();i++){
					logsPath.add(array.getString(i));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		if(sTableNames==null||sTableNames.size()<1){
    			return;
    		}
    		if(dTableNames==null||dTableNames.size()<1){
    			return;
    		}
    		System.out.println("sTableName:"+sTableNames);
    		System.out.println("dTableName:"+dTableNames);
    		String sTableName = sTableNames.get(0);
    		String dTableName = dTableNames.get(0);
    		String sTableSpace = sTableSpaces.get(0);
    		String dTableSpace = dTableSpaces.get(0);
    		String result = TableMapService.addLogmnrSourceTableMap(sTanent, dTanent, sTableSpace, 
    				sTableName, dTableSpace, dTableName, 
    		        dTableInfoJson, valiFields, fieldsMap, logsPath, dictPath); 
    		System.out.println("result:"+result);
    		builder.append(result);
    	}else if(uri.equalsIgnoreCase("/testConnect")){
    		
    		List<String> strs = params.get("tanent");
    		if(strs==null||strs.size()<1){
    			builder.append("false");
    			return;
    		}
    		Tenant tanent;
			try {
				tanent = new Tenant(strs.get(0));
			} catch (JSONException e) {
				builder.append("false");
				e.printStackTrace();
    			return;
			}
    		strs = params.get("tableName");
    		if(strs==null||strs.size()<1){
    			builder.append("false");
    			return;
    		}
    		String tableName = strs.get(0);
    	    boolean flag = TanentService.testConnect(tanent, tableName);
    	    if(flag){
    	    	builder.append("true");
    	    }else{
    	    	builder.append("false");
    	    }
    	    
    	}else if(uri.equalsIgnoreCase("/getTableSchema")){
    		List<String> strs = params.get("tanent");
    		if(strs==null||strs.size()<1){
    			return;
    		}
    		Tenant tanent;
			try {
				tanent = new Tenant(strs.get(0));
			} catch (JSONException e) {
				e.printStackTrace();
    			return;
			}
    		strs = params.get("tableName");
    		if(strs==null||strs.size()<1){
    			return;
    		}
    		String tableName = strs.get(0);
    		String result = TanentService.getTableSchema(tanent, tableName);
    		if(StringUtils.isNotBlank(result)){
    			builder.append(result);
    		}
    	}else if(uri.equalsIgnoreCase("/getClients")){
    	    String result = StaticInfo.getClients();
    	    if(StringUtils.isNotBlank(result)){
    	    	builder.append(result);
    	    }
        }else if(uri.equalsIgnoreCase("/getTableMapWebs")){
        	
        	String result = TableMapService.getTableMapWebs();
        	if(StringUtils.isNotBlank(result)){
        		builder.append(result);
        	}
        	
        }else if(uri.equalsIgnoreCase("/startTableMapById")){
        	List<String> strs = params.get("id");
        	if(strs==null||strs.size()<1){
        		builder.append(false);
        		return;
        	}
        	int id = Integer.parseInt(strs.get(0));
        	builder.append(TableMapService.startMap(id));
        	
        }else if(uri.equalsIgnoreCase("/stopTableMapById")){
        	
        	List<String> strs = params.get("id");
        	if(strs==null||strs.size()<1){
        		builder.append(false);
        		return;
        	}
        	int id = Integer.parseInt(strs.get(0));
        	builder.append(TableMapService.stopMap(id));
        }else if(uri.equalsIgnoreCase("/getMapData")){
        	
        	List<String> strs = params.get("id");
        	if(strs==null||strs.size()<1){
        		builder.append(false);
        		return;
        	}
        	int id = Integer.parseInt(strs.get(0));
        	strs = params.get("day");
        	if(strs==null||strs.size()<1){
        		builder.append(false);
        		return;
        	}
        	long time = Long.parseLong(strs.get(0));
        	strs = params.get("dayOrWeek");
        	if(strs==null||strs.size()<1){
        		builder.append(false);
        		return;
        	}
        	int dayOrWeek = Integer.parseInt(strs.get(0));
        	builder.append(TableMapService.getMapData(id, new Date(time), dayOrWeek));
        } else if(uri.equalsIgnoreCase("/initGetMapData")){
        	builder.append(TableMapService.initGetMapData());
        }
    	return;
    }
    
    public static void main(String[] args) throws JSONException{
    	
    	List<String> names = new ArrayList<String>();
    	names.add("chen");
    	names.add("yang");
    	JSONArray array1 = new JSONArray(names);
    	System.out.println(array1.toString());
    	JSONArray array = new JSONArray(array1.toString());
    	for(int i=0;i<array.length();i++){
    		System.out.println(array.getString(i));
    	}
    	
    }
    
}
