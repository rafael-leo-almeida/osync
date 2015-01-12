package com.jtang.monitor.manager;

import java.util.List;
import java.util.Map;

public class Command {

	String path;
	Map<String,List<String>> params;
	
	public Command(String path,Map<String,List<String>>params){
		this.path=path;
		this.params=params;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, List<String>> getParams() {
		return params;
	}

	public void setParams(Map<String, List<String>> params) {
		this.params = params;
	}
	
	
}
