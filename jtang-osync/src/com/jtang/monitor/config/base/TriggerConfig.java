package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.monitor.config.base.OracleConnection;

public class TriggerConfig {

	String url;
	String userName;
	String password;
	String sourceName;
	String proc;
	String proc00="CREATE or replace TRIGGER ";
	String proc01="_TRG " +
			"before insert or update on  ";
	String proc02=" referencing old as old new as new " +
			"for each row " +
			"begin " +
			" if (updating) then "+
			"if (updating and :new.txn < 0) then " +
			":new.txn := -:new.txn; " +
			"else " +
			":new.txn := sync_core.getTxn('";
	String proc03="', 0); end if; " +
			"else  :new.txn:=sync_core.getTxn('";
	
	String proc04="', 1); "+
			" end if; " +
			"end;";
	public TriggerConfig(String url,String userName,String password,String sourceName){
		
		this.url=url;
		this.userName=userName;
		this.password=password;
		this.sourceName=sourceName;
		this.proc=this.proc00+this.sourceName+this.proc01+this.sourceName+this.proc02+this.sourceName+this.proc03+
				this.sourceName+this.proc04;
	}
	public void createTrigger() throws SQLException{
		
		String sql = "CREATE or replace TRIGGER students_TRG before insert or" +
				" update on  students referencing old as old new as new for each row begin  " +
				"if (updating) then if (updating and :new.txn < 0) then :new.txn := -:new.txn; " +
				"else :new.txn := 1; end if; else  " +
				":new.txn := 2;  end if; end;";
		System.out.println(this.proc);
		Connection con = OracleConnection.getConnection(this.url, this.userName, this.password);
		Statement stat = con.createStatement();
		stat.execute(this.proc);
		stat.close();
		con.close();
	
	}
	
	
	
}
