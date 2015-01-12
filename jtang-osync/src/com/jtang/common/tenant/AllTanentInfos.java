package com.jtang.common.tenant;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllTanentInfos {

	Vector<TenantInfos> tanentInfos = new Vector<TenantInfos>();

	public Vector<TenantInfos> getTanentInfos() {
		return tanentInfos;
	}

	public void setTanentInfos(Vector<TenantInfos> tanentInfos) {
		this.tanentInfos = tanentInfos;
	}
	
	public String toString(){
		
		return new JSONObject(this).toString();
		
	}
	public AllTanentInfos(String json) throws JSONException{
		
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("tanentInfos");
		int size = array.length();
		for(int i=0;i<size;i++){
			
			TenantInfos tanentInfos = new TenantInfos(array.getString(i));
			this.tanentInfos.add(tanentInfos);
			
		}
	}
	public AllTanentInfos(){
		super();
	}
	
	public void add(TenantInfos tanentInfos){
		this.tanentInfos.add(tanentInfos);
	}
	
}
