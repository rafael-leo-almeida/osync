package com.jtang.syn.test;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.jtang.common.table.SourceInfo;
import com.jtang.syn.comm.Message;

public class InfoTest {

	@Test
	public void getJson() throws JSONException {
		SourceInfo sourceInfo = new SourceInfo("127.0.0.1", 8080, "DB",
				"person","chen");
		String operation = "delete";
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("id", new Integer(100).toString());
		datas.put("first_name", "chen");
		datas.put("last_name", "yang");
		datas.put("deleted", "false");
		Message info = new Message(sourceInfo, operation, datas);
		JSONObject obj = new JSONObject(info);
		System.out.println(obj.toString());
        Message  info2 = new Message(obj.toString());
        System.out.println(new JSONObject(info2).toString());
		
		
		
		
		

	}
}