package com.jtang.console.action;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.jtang.common.tenant.Tenant;
import com.opensymphony.xwork2.ActionSupport;

public class AddTanentAction extends ActionSupport {

	
	
	    String userName;
	    String password;
	    String ip;
	    int port;
	    String DBName;
	    String message;
	    
		
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	
		
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getDBName() {
			return DBName;
		}

		public void setDBName(String dBName) {
			DBName = dBName;
		}
	@Override
	public String execute() throws Exception {	    
		    // TODO Auto-generated method stub
		    Tenant tanent = new Tenant(ip, port, DBName, userName, password);
		    //System.out.println(tanent.toString());
		    DefaultHttpClient client = new DefaultHttpClient();
		    URIBuilder bd = new URIBuilder();
		    bd.setScheme("http").setHost("127.0.0.1").setPath("/tanent/AddTanent")
		    .setParameter("tanent", tanent.toString())
		    .setPort(8889);

		    URI uri = bd.build();
		    HttpGet get = new HttpGet(uri);
	        HttpResponse response = client.execute(get);
	        InputStream rd = response.getEntity().getContent();
	        byte[] bytes = new byte[1024];
	        int length=0;
	        StringBuilder builder = new StringBuilder();
	        
	        while((length=rd.read(bytes))>0){
	        	String str = new String(bytes, 0, length);
	            builder.append(str);    
	        }
	        String result = builder.toString();
	        System.out.println(result);
	        if(result!=null&&result.equalsIgnoreCase("true")){
	        	this.message="Congratunations,add the new user successfully!";
	        	return SUCCESS;
	        }else{
	        	this.message="Unfortunately,add the new user unsuccessfully!";
	        	return "failed";
	        }
	        	
	}

	
	
	
}
