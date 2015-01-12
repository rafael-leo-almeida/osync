package com.jtang.syn.netty;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.jtang.common.table.SourceInfo;
import com.jtang.syn.comm.Message;

public class GenerateJsonTest {

	public String createJson(int id){
		SourceInfo sourceInfo = new SourceInfo("127.0.0.1", 8080, "DB",
				"person","person");
		String operation = "update";
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("id", new Integer(id).toString());
		datas.put("first_name", "chenchenchen");
		datas.put("last_name", "yang");
		datas.put("deleted", "false");
		datas.put("birth_date", "20130909 09:09:09");
		Message info = new Message(sourceInfo, operation, datas);
		return new JSONObject(info).toString();
	}
	
}
