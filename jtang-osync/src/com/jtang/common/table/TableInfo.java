package com.jtang.common.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableInfo {

	String tableSpace="";
	String tableName;
	List<String>columnName = new ArrayList<String>();
	List<String> keys = new ArrayList<String>();
	Map<String,String> columnType = new HashMap<String,String>();
	
	public TableInfo(){
		super();
	}
	public TableInfo(String json) throws JSONException{
		
		JSONObject obj = new JSONObject(json);
		this.tableName = obj.getString("tableName");
		JSONArray columnNameArray = obj.getJSONArray("columnName");
		int size = columnNameArray.length();
		for(int i=0;i<size;i++){
			this.columnName.add(columnNameArray.getString(i));
		}
		JSONArray keysArray = obj.getJSONArray("keys");
		size = keysArray.length();
		for(int i=0;i<size;i++){
			this.keys.add(keysArray.getString(i));
		}
		JSONObject obj2 = obj.getJSONObject("columnType");
		Iterator<String> t = obj2.keys();
		while(t.hasNext()){
			String key = (String)t.next();
			String type = obj2.getString(key);
			this.columnType.put(key, type);
		}

		
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<String> getColumnName() {
		return columnName;
	}
	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}
	public Map<String, String> getColumnType() {
		return columnType;
	}
	public void setColumnType(Map<String, String> columnType) {
		this.columnType = columnType;
	}
	public List<String> getKeys() {
		return keys;
	}
	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	
    public String getTableSpace() {
		return tableSpace;
	}
	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}
	public static TableInfo createDefaultTable(){
    	TableInfo table = new TableInfo();
    	String tableName = "students";
    	List<String>columnName = new ArrayList<String>();
    	columnName.add("id");
    	columnName.add("first_name");
    	columnName.add("last_name");
    	columnName.add("deleted");
    	columnName.add("birth_date");
    	List<String> keys=new ArrayList<String>();
    	keys.add("id");
    	Map<String,String> columnType = new HashMap<String, String>();
    	columnType.put("id", "number");
    	columnType.put("first_name", "varchar2()");
    	columnType.put("last_name", "varchar2()");
    	columnType.put("deleted", "varchar2()");
    	columnType.put("birth_date", "date");
	
    	table.setTableName(tableName);
    	table.setColumnType(columnType);
    	table.setKeys(keys);
    	table.setColumnName(columnName);
    	return table;
    }
    public boolean ifContains(String fieldName){
    	boolean f = false;
    	if(fieldName==null)
    		return f;
    	for (String name : this.columnName) {
			if(fieldName.equals(name)){
				f=true;
				break;
			}
		}
    	return f;
    }
    public String findTypeWithDatabus(String name){
    	
    	String type = null;
    	for (String fieldName : this.getColumnName()) {
			
    		String[] strs= fieldName.split(":");
    		if(strs!=null&&strs.length>=2&&strs[1].equalsIgnoreCase(name)){
                type=this.getColumnType().get(fieldName);
                break;
    		}
    		
		}
    	return type;	
    }
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new JSONObject(this).toString();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof TableInfo)){
			return false;
		}
		TableInfo table = (TableInfo)obj;
		if(table.getTableName().equalsIgnoreCase(this.getTableName())){
			return true;
		}else{
			return false;
		}
		
	}
	
}
