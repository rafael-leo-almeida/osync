package com.jtang.common.table;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

public class SourceInfo implements Serializable {
	
	String ip;
	int port;
	String DBName;
	//username 其实是表空间名称，默认为用户名，这个变量名需要改一下
    String userName;
	String tableName;
	
	public SourceInfo() {
		super();
	}

	public SourceInfo(String json){
		JSONObject obj;
		try {
			obj = new JSONObject(json);
			this.ip = obj.getString("ip");
			this.port = obj.getInt("port");
			this.DBName = obj.getString("DBName");
			this.tableName = obj.getString("tableName");
			this.userName = obj.getString("userName");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public SourceInfo(String ip, int port, String DBName, String tableName,String userName) {
		this.ip = ip;
		this.port = port;
		this.DBName = DBName;
		this.tableName = tableName;
		this.userName = userName;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public boolean equals(Object obj) {

		// TODO Auto-generated method stub
		SourceInfo sourceInfo;
		if (obj instanceof SourceInfo) {
			sourceInfo = (SourceInfo) obj;
		} else {
			return false;
		}
		if (sourceInfo.ip.equals(this.ip) && sourceInfo.port == this.port
				&& sourceInfo.DBName.equals(this.DBName)&&this.userName.equalsIgnoreCase(sourceInfo.getUserName())
				&& sourceInfo.tableName.equals(this.tableName)) {
			return true;
		} else {
			return false;
		}

	}

	public static String localIp() throws SocketException {

		Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		String addr;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
					.nextElement();
			System.out.println(netInterface.getName());
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					System.out.println("本机的IP = " + ip.getHostAddress());
				}
			}
		}
     if(ip!=null)
    	 return ip.getHostAddress();
     else
    	 return null;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new JSONObject(this).toString();
	}
	
	

}
