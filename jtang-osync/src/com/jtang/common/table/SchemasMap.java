package com.jtang.common.table;

import java.util.List;

public class SchemasMap {

	List<TableItem> fromTables;
	List<TableItem> toTables;
	List<TableMap> tablesMap;
	public SchemasMap(){
		super();
	}
	public SchemasMap(List<TableItem> fromTables,List<TableItem> toTables,List<TableMap>tableMap){
		this.fromTables = fromTables;
		this.toTables=toTables;	
		this.tablesMap=tableMap;
	}
	
	public List<TableItem> getFromTables() {
		return fromTables;
	}
	public void setFromTables(List<TableItem> fromTables) {
		this.fromTables = fromTables;
	}
	public List<TableItem> getToTables() {
		return toTables;
	}
	public void setToTables(List<TableItem> toTables) {
		this.toTables = toTables;
	}
	public List<TableMap> getTablesMap() {
		return tablesMap;
	}
	public void setTablesMap(List<TableMap> tablesMap) {
		this.tablesMap = tablesMap;
	}
	
	
}
