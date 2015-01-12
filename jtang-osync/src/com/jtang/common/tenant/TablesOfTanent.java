package com.jtang.common.tenant;

import java.util.List;

import org.json.JSONObject;

import com.jtang.common.table.TableInfo;


public class TablesOfTanent {

	List<TableInfo> tableInfos;

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
