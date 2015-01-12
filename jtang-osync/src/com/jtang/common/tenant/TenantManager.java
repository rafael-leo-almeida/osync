package com.jtang.common.tenant;

import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.record.TenantDaoImpl;

public class TenantManager {

	Vector<Tenant> tanents = new Vector<Tenant>();
	String str = "{\"class\":\"class com.jtang.syn.comm.TanentPool\",\"tanents\":[\"{\"port\":1521,\"class\":\"class com.jtang.syn.comm.Tanent\",\"userName\":\"chen\",\"password\":\"chen\",\"DBName\":\"orcl\",\"url\":\"jdbc:oracle:thin:@127.0.0.1:1521:orcl\",\"ip\":\"127.0.0.1\"}\"]}";
	public TenantManager(){
		super();
	}
	
    public TenantManager(String json) throws JSONException{
		
		JSONObject obj = new JSONObject(json);
		JSONArray objArray = obj.getJSONArray("tanents");
		int size = objArray.length();
		Vector<Tenant> tanents = new Vector<Tenant>();
		
		for(int i=0;i<size;i++){
			String str0 = objArray.getString(i);
			Tenant tanent = new Tenant(str0);
		    tanents.add(tanent);	
		}
		this.tanents=tanents;
	}
	
	
	public Vector<Tenant> getTanents() {
		return tanents;
	}

	public void setTanents(Vector<Tenant> tanents) {
		this.tanents = tanents;
	}

	public boolean isExistTanent(Tenant tanent){
		
		int size = this.tanents.size();
		for(int i=0;i<size;i++){
			
			Tenant t = this.tanents.get(i);
			if(t.equals(tanent))
				return true;
		}
		return false;	
	}
	
	public void addTanent(Tenant tanent){
		if(this.isExistTanent(tanent)){
			return;
		}else{
			//将其写入数据库
			TenantDaoImpl tDao = new TenantDaoImpl();
			tDao.add(tanent);
		}
		this.tanents.add(tanent);
	}
	
	public void initAddTanent(Tenant tenant){
		if(this.isExistTanent(tenant)){
			return;
		}
		this.tanents.add(tenant);
	}
	
	
	public String toString(){
		
		return new JSONObject(this).toString();
	
	}
	
	public Tenant getTanentByOthers(String ip,int port,String DBName,String userName){
		int size = this.tanents.size();
		for(int i=0;i<size;i++){
			
			Tenant t = this.tanents.get(i);
			if(t.getIp().equals(ip)&&t.getPort()==port&&
					t.getDBName().equalsIgnoreCase(DBName)&&t.getUserName().equals(userName))
				return t;
		}
		return null;
	}
	
	
	
	
}
