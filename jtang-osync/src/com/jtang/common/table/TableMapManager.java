package com.jtang.common.table;

import java.util.Vector;

import com.jtang.common.record.MapJson;
import com.jtang.common.record.MapJsonDaoImpl;
import com.jtang.common.table.SourceInfo;


public class TableMapManager {

	Vector<TableMap> tableMaps = new Vector<TableMap>();
  
	//根据源表得到已经启动的映射
	public Vector<TableMap> getTableMapsBySourceInfo(SourceInfo sourceInfo){
		
		Vector<TableMap> maps = new Vector<TableMap>();
		for (TableMap tableMap : this.tableMaps) {
			if(tableMap.getStatus()==1&&tableMap.getFromTable().getSourceInfo().equals(sourceInfo)){
				maps.add(tableMap);
			}
		}	
		return maps;
	}
	
	public boolean isExistTableMap(TableMap tableMap){
		
		int size = this.tableMaps.size();
		for(int i=0;i<size;i++){
		
		    TableMap map = this.tableMaps.get(i);
		    if(map.equals(tableMap))
		    	return true;
			
		}
		
	   return false;
	}
	
	public void add(TableMap tableMap){
		
		if(this.isExistTableMap(tableMap)){
			return ;
		}else{
			//如果不存在这个map，则加入数据库进行记录
			MapJson json = new MapJson();
			json.setMapJson(tableMap.toString());
			json.setStatus(0);
			json = new MapJsonDaoImpl().add(json);
			if(json!=null){
				tableMap.setId(json.getId());
			}
		}
		this.tableMaps.add(tableMap);
		
	}
	
    public void initAdd(TableMap tableMap){
    	
		if(this.isExistTableMap(tableMap)){
			return ;
		}
		this.tableMaps.add(tableMap);
		
	}
	
	public boolean startTableMap(TableMap tableMap){
		
		int size = this.tableMaps.size();
		boolean  flag = false;
		for(int i=0;i<size;i++){
		    TableMap map = this.tableMaps.get(i);
		    if(map.equals(tableMap)){
		    	flag=true;
		    	map.status=1;
		    }
		}
		return flag;
	}
	
	public TableMap getTableMapById(int id){
		
		int size = this.tableMaps.size();
		TableMap tMap = null;
		for(int i=0;i<size;i++){
		    TableMap map = this.tableMaps.get(i);
		    if(map.getId()==id){
		    	tMap = map;
		    	break;
		    }
		}
		return tMap;
	}
	
	public boolean  stopTableMapById(int id){
		
		int size = this.tableMaps.size();
		for(int i=0;i<size;i++){
		    TableMap map = this.tableMaps.get(i);
		    if(map.getId()==id){
		    	map.setStatus(0);
		    	return true;
		    }
		}
		return false;
	}
	
	public boolean startTableMapById(int id){
		
		int size = this.tableMaps.size();
		for(int i=0;i<size;i++){
		    TableMap map = this.tableMaps.get(i);
		    if(map.getId()==id){
		    	map.setStatus(1);
		    	return true;
		    }
		}
		return false; 
	}

	public Vector<TableMap> getTableMaps() {
		return tableMaps;
	}

	public void setTableMaps(Vector<TableMap> tableMaps) {
		this.tableMaps = tableMaps;
	}
	
	
	
}
