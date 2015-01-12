package com.jtang.syn.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.syn.comm.GenerateSQL;
import com.jtang.syn.comm.Message;


public class TestGenerateSQL {

	@Test
	public void testGetSQL(){
		
		SourceInfo sourceInfo = new SourceInfo("127.0.0.1", 8080, "DB",
				"person","chen");
		String operation = "delete";
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("id", new Integer(100).toString());
		datas.put("first_name", "chen");
		datas.put("last_name", "yang");
		datas.put("deleted", "false");
		datas.put("birth_date", "20030909 09:09:09");
		Message info = new Message(sourceInfo, operation, datas);
		TableInfo tableInfo = TableInfo.createDefaultTable();
		String deleteSQL = GenerateSQL.getDeleteSQL(null, info);
		String insertSQL = GenerateSQL.getInsertSQL(null, info);
		String updateSQL = GenerateSQL.getUpdateSQL(null, info);
		
		System.out.println("deleteSQL: "+ deleteSQL);
		System.out.println("insertSQL: "+insertSQL);
		System.out.println("updateSQL: "+updateSQL);
		
		
		
		
	}
	
}
