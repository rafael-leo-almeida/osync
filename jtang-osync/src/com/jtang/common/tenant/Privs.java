package com.jtang.common.tenant;

import java.util.ArrayList;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class Privs {

	List<String> allPrivs = new ArrayList<String>() ;

	public Privs(){
		super();
	}
	public Privs(String json) throws JSONException{
		
		JSONObject obj = new JSONObject(json);
		JSONArray array = obj.getJSONArray("allPrivs");
		List<String>allPrivs = new ArrayList<String>();
		int size = array.length();
		for(int i=0;i<size;i++){
			allPrivs.add(array.getString(i));
		}
		this.allPrivs = allPrivs;
	}
	
	
	public List<String> getAllPrivs() {
		return allPrivs;
	}

	public void setAllPrivs(List<String> allPrivs) {
		this.allPrivs = allPrivs;
	}
	
	public String toString(){
		
		return new JSONObject(this).toString();
	
	}
	


	
}
