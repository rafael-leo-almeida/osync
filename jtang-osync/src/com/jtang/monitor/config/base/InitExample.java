package com.jtang.monitor.config.base;

import java.sql.SQLException;

import com.jtang.monitor.config.base.CompileObjects;
import com.jtang.monitor.config.base.InsertSource;
import com.jtang.monitor.config.base.ProcConfig;
import com.jtang.monitor.config.base.ProcOneConfig;
import com.jtang.monitor.config.base.TableConfig;
import com.jtang.monitor.config.base.TriggerConfig;
import com.jtang.monitor.config.base.ViewConfig;

public class InitExample {

	
	String url;
	String userName;
	String password;
	
	public InitExample(String url,String userName,String password){
		
		this.url = url;
		this.userName = userName;
		this.password = password;
		
	}
	
	public void init() throws SQLException{
		
		TableConfig.createExampleTable(url, userName, password);
		ViewConfig.createExampleTableView(url, userName, password);
		new ProcConfig("PERSON", null, userName, password).createProc();
		new ProcOneConfig(null, userName, password, "PERSON").createProc1();
		new TriggerConfig(url, userName, password, "PERSON").createTrigger();
		new InsertSource(url, userName, password, "PERSON").insert();
		CompileObjects.compile(url, userName, password);
		
	}
	
}
