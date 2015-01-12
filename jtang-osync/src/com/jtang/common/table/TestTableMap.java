package com.jtang.common.table;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


public class TestTableMap {

	
	@Test
	public void  test() throws JSONException{
		Map<String, String> values = new HashMap<String, String>();
		String schema=null;
		values.put("viewName", "person");
		try {
			System.out.println(new JSONObject(new CreateSchema(values).getSchemaWithDatabus()).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        try {
			System.out.println(new JSONObject(new CreateSchema(values).getSchemaWithDB()).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testIp(){
//		try {
//			//SourceInfo.localIp();
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	@Test
	public void testCreateDefaultTableMap() throws JSONException{
		
		TableMap tableMap=null;
		try {
			tableMap = TableMap.createDefaultTableMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new JSONObject(tableMap).toString());
	
		
	}
	
	
	@Test
	public void test1() throws JSONException{
		
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableName("person");
		List<String> fields = new ArrayList<String>();
		fields.add("id");fields.add("name");
		List<String> keys = new ArrayList<String>();
		keys.add("id");
		Map<String,String> types = new HashMap<String, String>();
		types.put("id", "number");
		types.put("name", "varchar2");
		tableInfo.setColumnName(fields);
		tableInfo.setColumnType(types);
		tableInfo.setKeys(keys);
		System.out.println(tableInfo.toString());
		JSONObject obj = new JSONObject(tableInfo.toString());
		JSONObject obj2 = obj.getJSONObject("columnType");
		Iterator<String> t = obj2.keys();
		
		while(t.hasNext()){
			String str = (String)t.next();
			System.out.println(str+":"+obj2.getString(str));
		}
		
		
		
	}
}
