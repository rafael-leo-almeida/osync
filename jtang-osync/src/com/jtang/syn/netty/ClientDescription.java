package com.jtang.syn.netty;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.tenant.Tenant;


public class ClientDescription {

	private String watchIp;
	private int watchPort;
	private Vector<DBDescription> DBs=new Vector<DBDescription>();
	
	public String getWatchIp() {
		return watchIp;
	}
	public void setWatchIp(String watchIp) {
		this.watchIp = watchIp;
	}
	public int getWatchPort() {
		return watchPort;
	}
	public void setWatchPort(int watchPort) {
		this.watchPort = watchPort;
	}
	
	public Vector<DBDescription> getDBs() {
		return DBs;
	}
	public void setDBs(Vector<DBDescription> dBs) {
		DBs = dBs;
	}
	
	public String toString(){
		
		return new JSONObject(this).toString();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
        if(!(obj instanceof ClientDescription)){
        	return false;
        }
        ClientDescription client = (ClientDescription)obj;
        if(client.getWatchIp().equalsIgnoreCase(this.getWatchIp())&&client.getWatchPort()==this.getWatchPort())
        	return true;
        else 
        	return false;
        
	}
	public boolean isExistDB(DBDescription db){
		
		int size = this.DBs.size();
		for(int i=0;i<size;i++){
			DBDescription db0 = this.DBs.get(i);
			if(db0.equals(db)){
				return true;
			}
		}
		return false;
	}
	
	public void addDB(DBDescription db){
		if(this.isExistDB(db))
			return;
		this.DBs.add(db);
	}
	
	public DBDescription getDBByTanent(Tenant tanent){
		int size=this.DBs.size();
		for(int i=0;i<size;i++){
			DBDescription db = this.DBs.get(i);
			if(db.getIp().equalsIgnoreCase(tanent.getIp())&&
					db.getPort()==tanent.getPort()&&
					db.getDBName().equalsIgnoreCase(tanent.getDBName())&&
					db.getUserName().equals(tanent.getUserName())){
				return db;
			}
		}
		return null;
	}
	
	public ClientDescription(){
		super();
	}
	
	public ClientDescription(String json){
		
		try {
			JSONObject obj = new JSONObject(json);
			this.setWatchIp(obj.getString("watchIp"));
			this.setWatchPort(obj.getInt("watchPort"));
			JSONArray array = obj.getJSONArray("DBs");
			for(int i=0;i<array.length();i++){
				DBDescription db = new DBDescription(array.getString(i));
				if(db!=null){
					this.DBs.add(db);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
}
