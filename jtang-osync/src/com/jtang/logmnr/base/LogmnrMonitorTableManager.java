package com.jtang.logmnr.base;

import java.util.Vector;

import com.jtang.common.table.TableInfo;

public class LogmnrMonitorTableManager {
	
	private Vector<TableInfo> tables = new Vector<TableInfo>();
	private int size = 0;
	public Vector<TableInfo> getAllMonitorTables() {
		return this.tables;
	}
	public void addMonitorTable(TableInfo table) {

		boolean f = true;
		for (int i = 0; i < size; i++) {
			TableInfo t = this.tables.get(i);
			if (t.getTableName().equalsIgnoreCase(table.getTableName())) {
				f = false;
				break;
			}
		}
		if (f) {
			this.tables.add(table);
			this.size++;
		}
	}

	public TableInfo getTableByName(String name) {

		for (int i = 0; i < size; i++) {
			TableInfo t = this.tables.get(i);
			if (t.getTableName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}
	
	public TableInfo getTableByNameAndSpace(String name,String space) {

		for (int i = 0; i < size; i++) {
			TableInfo t = this.tables.get(i);
			if (t.getTableName().equalsIgnoreCase(name)&&space.equalsIgnoreCase(t.getTableSpace())) {
				return t;
			}
		}
		return null;
	}
}
