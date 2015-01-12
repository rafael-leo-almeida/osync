package com.jtang.console.dwr.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ClientEntity {

	String ip;
	List<String> dbs = new ArrayList<String>();
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<String> getDbs() {
		return dbs;
	}
	public void setDbs(List<String> dbs) {
		this.dbs = dbs;
	}
	
}
