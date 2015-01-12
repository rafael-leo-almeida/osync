package com.jtang.monitor.insertAndupdate.base;

import java.util.Vector;

import com.jtang.common.table.TableInfo;


public class MonitorTableManager {

    private Vector<TableInfo> tables = new Vector<TableInfo>();	
    private int size = 0;
    
    public Vector<TableInfo> getAllMonitorTables(){
    	return this.tables;
    }
    public void addMonitorTable(TableInfo table){
    	
       boolean f = true;
       for(int i=0;i<size;i++){
    	   TableInfo t = this.tables.get(i);
    	   if(t.getTableName().equalsIgnoreCase(table.getTableName())){
    		   f=false;
    		   break;
    	   }
       }	
       if(f){
    	   this.tables.add(table);
    	   this.size++;
       }
    }
    
    public TableInfo getTableByName(String name){
    	
    	for(int i=0;i<size;i++){
    		
    		TableInfo t = this.tables.get(i);
    		if(t.getTableName().equalsIgnoreCase(name)){
    			return t;
    		}
    	}
    	return null;
    }
    
    
}
