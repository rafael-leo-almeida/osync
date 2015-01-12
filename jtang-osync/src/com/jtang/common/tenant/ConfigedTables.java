package com.jtang.common.tenant;

import java.util.List;

import org.json.JSONObject;

public class ConfigedTables {

	List<String> configedTables;

	public List<String> getConfigedTables() {
		return configedTables;
	}

	public void setConfigedTables(List<String> configedTables) {
		this.configedTables = configedTables;
	}
	
	public String toString(){
		
		return new JSONObject(this).toString();
	
	}
	
	
}
