package com.jtang.common.table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;




public class TableMap {
	
      public TableItem fromTable;
      public TableItem toTable;
      public List<String> valiFields;
      public Map<String,String> fieldsMap;
      public int id;
      //表示这个tableMap 的状态,0表示停止，1表示启动
      public int status;
      
      
    public TableMap(){
    	super();
    }   
    public TableMap(TableItem fromTable,TableItem toTable,List<String> valiFields,Map<String,String>fieldsMap){
    	this.fromTable=fromTable;
    	this.toTable=toTable;
    	this.valiFields=valiFields;
    	this.fieldsMap=fieldsMap;
    	this.status=0;
    }
    public TableMap(String jsonTableMap) throws JSONException{
        
    	JSONObject obj = new JSONObject(jsonTableMap);	
    	JSONObject fromTableObj = new JSONObject(obj.getString("fromTable"));
    	JSONObject toTableObj = new JSONObject(obj.getString("toTable"));
    	String fromSourceInfoJson = fromTableObj.getString("sourceInfo");
    	String fromTableInfoJson = fromTableObj.getString("tableInfo");
    	SourceInfo fromSourceInfo = new SourceInfo(fromSourceInfoJson);
    	TableInfo fromTableInfo = new TableInfo(fromTableInfoJson);
    	TableItem  fromTableItem = new TableItem(fromSourceInfo, fromTableInfo);
    	
    	String toSourceInfoJson = toTableObj.getString("sourceInfo");
    	String toTableInfoJson = toTableObj.getString("tableInfo");
    	SourceInfo toSourceInfo = new SourceInfo(toSourceInfoJson);
    	TableInfo toTableInfo = new TableInfo(toTableInfoJson);
    	TableItem  toTableItem = new TableItem(toSourceInfo, toTableInfo);
 
    	List<String> fields = new ArrayList<String>();
    	JSONArray array = new JSONArray(obj.getString("valiFields"));
    	for(int i=0;i<array.length();i++){
    		fields.add(array.getString(i));
    	}
    	Map<String,String> fMaps = new HashMap<String, String>();
    	JSONObject obj2 = new JSONObject(obj.getString("fieldsMap"));
    	Iterator<String> t = obj2.keys();
		while(t.hasNext()){
			String key = (String)t.next();
			String type = obj2.getString(key);
			fMaps.put(key, type);
		}
    	
		this.fromTable = fromTableItem;
		this.toTable = toTableItem;
		this.valiFields = fields;
		this.fieldsMap = fMaps;
		this.id = obj.getInt("id");
		this.status=obj.getInt("status");
    	
    }
    public boolean ifValidate(){
    	boolean f=true;
    	if(fromTable==null||toTable==null||valiFields==null||fieldsMap==null)
    		return false;
    	for(String name : this.valiFields){
    	    boolean fHave = this.fromTable.getTableInfo().ifContains(name);
    		boolean tHave = this.toTable.getTableInfo().ifContains(this.fieldsMap.get(name));
    	    boolean typeSame = this.fromTable.getTableInfo().getColumnType().get(name).equals(
    	    		           this.toTable.getTableInfo().getColumnType().get(this.fieldsMap.get(name))
    	    		);
    	    if(!fHave||!tHave||typeSame){
    	    	f=false;
    	    	break;
    	    }
    	}
    	return f;
    }
      
	public TableItem getFromTable() {
		return fromTable;
	}
	public void setFromTable(TableItem fromTable) {
		this.fromTable = fromTable;
	}
	public TableItem getToTable() {
		return toTable;
	}
	public void setToTable(TableItem toTable) {
		this.toTable = toTable;
	}
	public List<String> getValiFields() {
		return valiFields;
	}
	public void setValiFields(List<String> valiFields) {
		this.valiFields = valiFields;
	}
	public Map<String, String> getFieldsMap() {
		return fieldsMap;
	}
	public void setFieldsMap(Map<String, String> fieldsMap) {
		this.fieldsMap = fieldsMap;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
		
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public static TableMap createDefaultTableMap() throws IOException, SQLException{
		 
		TableMap tableMap = new TableMap();
		TableItem fromTable = new TableItem();
		TableItem toTable = new TableItem();
		List<String> valiFields = new ArrayList<String>();
		Map<String,String> fieldsMap = new HashMap<String, String>();
		
		String ip = "127.0.0.1";
		int fromPort = 0;
		int toPort = 8888;
		String DBName = "DB";
		//sy$person is a view
		String fromTableName = "sy$person";
		String toTableName = "students";
		Map<String,String> fromValues = new HashMap<String, String>();
		fromValues.put("viewName", fromTableName);
		Map<String,String> toValues = new HashMap<String, String>();
		toValues.put("viewName", toTableName);
		TableInfo fromTableInfo=null;
		try {
			fromTableInfo = new CreateSchema(fromValues).getSchemaWithDatabus();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TableInfo toTableInfo = new CreateSchema(toValues).getSchemaWithDB();
		SourceInfo fromSourceInfo = new SourceInfo(ip, fromPort, DBName, fromTableName,"chen");
		SourceInfo toSourceInfo = new SourceInfo(ip, toPort, DBName, toTableName,"chen");
		fromTable.setSourceInfo(fromSourceInfo);
		fromTable.setTableInfo(fromTableInfo);
		toTable.setSourceInfo(toSourceInfo);
		toTable.setTableInfo(toTableInfo);
		
		valiFields.add("BIRTH_DATE:birthDate");
		valiFields.add("KEY:key");
		valiFields.add("FIRST_NAME:firstName");
		valiFields.add("LAST_NAME:lastName");
		valiFields.add("DELETED:deleted");
		
		fieldsMap.put("KEY:key", "ID");
		fieldsMap.put("BIRTH_DATE:birthDate", "BIRTH_DATE");
		fieldsMap.put("FIRST_NAME:firstName", "FIRST_NAME");
		fieldsMap.put("LAST_NAME:lastName", "LAST_NAME");
		fieldsMap.put("DELETED:deleted", "DELETED");
		tableMap.setFromTable(fromTable);
		tableMap.setToTable(toTable);
		tableMap.setValiFields(valiFields);
		tableMap.setFieldsMap(fieldsMap);
		
//		System.out.println(new JSONObject(fromTableInfo).toString());
//		System.out.println(new JSONObject(toTableInfo).toString());
		
		return tableMap;
		
	}
	@Override
	public String toString() {
		return new JSONObject(this).toString();
	}
	@Override
	public boolean equals(Object obj) {
        if(!(obj instanceof TableMap))
        	return false;
        TableMap map = (TableMap) obj;
        if(map.getFromTable().equals(this.getFromTable())&&map.getToTable().equals(this.getToTable())){
        	return true;
        }
        return false;
	}
      
	
      
}
