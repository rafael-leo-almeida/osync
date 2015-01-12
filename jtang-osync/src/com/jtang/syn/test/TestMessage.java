package com.jtang.syn.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.jtang.common.table.DataTranslator;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableMap;
import com.jtang.syn.comm.Message;

public class TestMessage {

	
	@Test
	public void testMessage() throws JSONException{
		
		
//		Message msg = new Message();
//		SourceInfo sourceInfo = new SourceInfo();
//		sourceInfo.setIp("localhost");
//		sourceInfo.setPort(8888);
//		sourceInfo.setDBName("DB");
//		sourceInfo.setTableName("person");
//		msg.setSourceInfo(sourceInfo);
//		System.out.println(new JSONObject(msg).toString());
		
		TableMap tableMap = null;
		try {
			tableMap = TableMap.createDefaultTableMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataTranslator translator = new DataTranslator(tableMap);
		Message info = new Message();
		info.setSourceInfo(tableMap.getFromTable().getSourceInfo());
		System.out.println("\n\n\n");
		System.out.println(new JSONObject(tableMap.getFromTable().getSourceInfo()).toString());
		System.out.println("\n\n\n");
		System.out.println(new JSONObject(info).toString());
		System.out.println("\n\n\n");
		SourceInfo sourceInfo = new SourceInfo();
		Message msg = translator.transData(new HashMap<String,Object>(), tableMap.getFromTable().getSourceInfo(), "delete");
		System.out.println(new JSONObject(msg).toString());
		sourceInfo.setIp("127.0.0.1");
		sourceInfo.setPort(9090);
		sourceInfo.setDBName("DB");
		sourceInfo.setTableName("person");
		info.setOperation("delete");
		info.setSourceInfo(sourceInfo);
		System.out.println("\n\n\n");
		System.out.println(new JSONObject(info).toString());
		System.out.println(new JSONObject(info.getSourceInfo()).toString());
	}
	
	
}
