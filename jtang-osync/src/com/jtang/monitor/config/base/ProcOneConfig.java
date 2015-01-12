package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.monitor.config.base.OracleConnection;

public class ProcOneConfig {
	
	String url;
	String userName;
	String password;
	String sourceName;
	String proc1;
	String proc0 = "create or replace package sync_alert as " +
			"function registerSourceWithVersion(source in varchar, version in number) return number; " +
			"procedure unregisterAllSources; " +
			"function waitForEvent(maxWait in number) return varchar; " +
			"end sync_alert;";
	String proc10 = "create or replace package body sync_alert as " +
			"is_registered boolean := FALSE;	" +
			"function registerSourceWithVersion(source in varchar, version in number) return number " +
			"as " +
			"view_name varchar(30); " +
			"bitnum number; " +
			"source_name varchar(100); " +
			"v_mode varchar(15); " +
			"begin " +
			"if version > 0 then " +
			"source_name := source || '_' || version; " +
			"else " +
			"source_name := source; " +
			"end if; " +
			"begin " +
			"select open_mode into v_mode from db_mode; " +
			"exception when others then " +
			"v_mode :=null; " +
			" end; " +
			"begin " +
			"select view_name into view_name from user_views where upper(view_name)=upper('sy$' || source_name); " +
			"exception when no_data_found then " +
			"view_name := null; " +
			"end; " +
			"begin " +
			"select bitnum into bitnum from sy$sources where name=source; " +
			"exception when no_data_found then " +
			"bitnum := null; " +
			"end;  " +
			"IF v_mode='READ WRITE'  THEN " +
			"IF not is_registered THEN " +
			"begin " +
			"dbms_alert.register('sy$alert_";
	
	String proc11="'); exception when others then " +
			"null; " +
			"end; " +
			"is_registered := TRUE; " +
			"END IF;  " +
			"END IF; " +
			"if (view_name is null or bitnum is null) then " +
			"return null; " +
			"end if; " +
			"return bitnum; " +
			"end; " +
			"procedure unregisterAllSources as " +
			"begin  " +
			"IF is_registered THEN " +
			"dbms_alert.remove('sy$alert_";
   String proc12="'); " +
			"is_registered := FALSE; " +
			"END IF; " +
			"end; " +
			"function waitForEvent(maxWait in number) return varchar " +
			"as " +
			"msg     varchar2(1800); " +
			"status  number; " +
			"begin " +
			"DBMS_ALERT.WAITONE('sy$alert_";
   
	String proc13 = "', msg, status, maxWait); " +
			"if status = 0 then " +
			"return msg; " +
			"end if; " +
			"return null; " +
			"end; " +
			"end sync_alert;";
	
	
	
	public ProcOneConfig(String url,String userName,String password,String sourceName){
		
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.sourceName = sourceName;
		this.proc1=this.proc10+this.sourceName+this.proc11+this.sourceName+this.proc12+this.sourceName+this.proc13;
		
	}
	public void createProc1() throws SQLException{
		
		Connection con = OracleConnection.getConnection(this.url, this.userName, this.password);
		Statement stat = con.createStatement();
		stat.execute(proc0);
		stat.execute(proc1);
		
	}
	
	
	

}
