package com.jtang.common.tenant;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.tenant.Privs;

public class Tenant {

	private String ip;
	private int port;
	private String DBName;
	private String url;
	private String userName;
	private String password;
	
	public Tenant(String ip,int port,String DBName,String userName,String password){
		
		this.ip=ip;
		this.port=port;
		this.userName=userName;
		this.DBName=DBName;
		this.password=password;
		this.url="jdbc:oracle:thin:@"+this.ip+":"+this.port+":"+this.DBName;
		
	}
	
	public Tenant(String jsonString) throws JSONException{
		
		JSONObject obj = new JSONObject(jsonString);
		this.ip=obj.getString("ip");
		this.port=obj.getInt("port");
		this.userName=obj.getString("userName");
		this.DBName=obj.getString("DBName");
		this.password=obj.getString("password");
		this.url="jdbc:oracle:thin:@"+this.ip+":"+this.port+":"+this.DBName;
		
	}
		
	public Tenant(){
		super();
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof Tenant)){
			return false;
		}
		Tenant tanent = (Tenant) obj;
		if(tanent.getIp().equalsIgnoreCase(this.getIp())&&tanent.getPort()==this.getPort()&&tanent.getDBName().equalsIgnoreCase(this.getDBName())
				&&tanent.getUserName().equals(this.getUserName())){
			return true;
		}
		
		return false;
	}
	
	public String toString(){
		return new JSONObject(this).toString();
	}
	

	

}
