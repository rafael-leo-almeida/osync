package com.jtang.syn.netty;


import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jtang.common.table.TableInfo;
public class DBDescription {

	private String ip;
	private int port;
	private String DBName;
	private String userName;
	Vector<TableInfo> tables=new Vector<TableInfo>();
	
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

	public Vector<TableInfo> getTables() {
		return tables;
	}
	public void setTables(Vector<TableInfo> tables) {
		this.tables = tables;
	}
	public String toString(){
		
		return new JSONObject(this).toString();
	
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
       if(!(obj instanceof DBDescription)){
    	   return false;
       }
       DBDescription db = (DBDescription)obj;
       if(db.getIp().equals(this.getIp())&&db.getPort()==this.getPort()&&
    		   db.getDBName().equalsIgnoreCase(this.getDBName())&&db.getUserName().equals(this.getUserName()))
    	   return true;
       else
    	   return false;
	}
	public void addTable(TableInfo table){
		
	   int size = this.tables.size();
	   for(int i=0;i<size;i++){
		   if(this.tables.get(i).getTableName().equalsIgnoreCase(table.getTableName()))
			   return;
	   }
	   this.tables.add(table);
	}
	
	
	public DBDescription(){
		super();
	}
	
	//从json构造一个
	public DBDescription(String json){
		try {
			JSONObject obj = new JSONObject(json);
			this.setDBName(obj.getString("DBName"));
			this.setIp(obj.getString("ip"));
			this.setPort(obj.getInt("port"));
			this.setUserName(obj.getString("userName"));
			JSONArray array = obj.getJSONArray("tables");
			for(int i=0;i<array.length();i++){
				TableInfo t = new TableInfo(array.getString(i));
				if(t!=null){
					this.tables.add(t);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
