package com.jtang.common.tenant;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.table.TableInfo;


public class TenantInfos {

	private Tenant tanent;
    private Privs privs;
    private List<TableInfo> tableInfos;
    
    public TenantInfos(Tenant tanent,Privs privs,List<TableInfo> tableInfos){
    	this.tanent=tanent;
    	this.privs=privs;
    	this.tableInfos=tableInfos;
    }

    public TenantInfos(String json) throws JSONException{
    	
    	JSONObject obj = new JSONObject(json);
    	String tanentJson = obj.getString("tanent");
    	String privsJson = obj.getString("privs");
    	JSONArray array = obj.getJSONArray("tableInfos");
    	this.tanent= new Tenant(tanentJson);
    	this.privs = new Privs(privsJson);
    	this.tableInfos = new ArrayList<TableInfo>();
    	int size = array.length();
    	
    	for(int i =0;i<size;i++){
    		TableInfo tableInfo = new TableInfo(array.getString(i));
    		tableInfos.add(tableInfo);
    	}
    	
    }
    
    public TenantInfos(){
    	super();
    }
    
	public Tenant getTanent() {
		return tanent;
	}

	public void setTanent(Tenant tanent) {
		this.tanent = tanent;
	}

	public Privs getPrivs() {
		return privs;
	}

	public void setPrivs(Privs privs) {
		this.privs = privs;
	}

	public List<TableInfo> getTableInfos() {
		return tableInfos;
	}

	public void setTableInfos(List<TableInfo> tableInfos) {
		this.tableInfos = tableInfos;
	}
    
    public String toString(){
    	return new JSONObject(this).toString();   	
    }
    
    
    
    
	
	
}
