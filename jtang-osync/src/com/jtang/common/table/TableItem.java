package com.jtang.common.table;

import org.json.JSONObject;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;

public class TableItem {

	public SourceInfo sourceInfo;
    public TableInfo tableInfo;
    
    public TableItem(){
    	super();
    }
    public TableItem(SourceInfo sourceInfo,TableInfo tableInfo){
    	this.sourceInfo = sourceInfo;
    	this.tableInfo=tableInfo;
    }
	
	public SourceInfo getSourceInfo() {
		return sourceInfo;
	}
	public void setSourceInfo(SourceInfo sourceInfo) {
		this.sourceInfo = sourceInfo;
	}
	public TableInfo getTableInfo() {
		return tableInfo;
	}
	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	@Override
	public String toString() {
		return new JSONObject(this).toString();
		
	}
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof TableItem))
			return false;
		TableItem item = (TableItem)obj;
		if(item.getSourceInfo().equals(this.getSourceInfo()))
		  return true;
		else
		  return false;
		
	}
	
    
    
    
}
