package com.jtang.monitor.config.base;

import java.sql.SQLException;


import com.jtang.monitor.config.base.SequenceConfig;
import com.jtang.monitor.config.base.SettingConfig;
import com.jtang.monitor.config.base.TableConfig;
import com.jtang.monitor.config.base.ViewConfig;

public class InitConfig {

	String url;
	String userName;
	String password;
	
	public InitConfig(String url,String userName,String password){
		this.url=url;
		this.userName=userName;
		this.password=password;
	}
	public void init() throws SQLException{
		
		SequenceConfig.createSequences(url, userName, password);
		TableConfig.createTables(url, userName, password);
		ViewConfig.createView(url, userName, password);
		SettingConfig.set(url, userName, password);
		//CompileObjects.compile(url, userName, password);
	}
	
	
}
