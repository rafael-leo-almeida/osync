package com.jtang.monitor.config.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.TableInfo;
public class UserConfig {

	
	private String url;
	private String userName;
	private String password;
	
    public UserConfig(String url,String userName,String password){
    	this.url=url;
    	this.userName=userName;
    	this.password=password;
    }
	public UserConfig(){
				
	}
	public boolean addUser(String userName,String password,String database,String url,String newUserName,String newPassword) throws SQLException{
		Connection con = null;
		if(userName==null){
			con = OracleConnection.getConnection();
			database = "orcl";
		}
		else
		  con=OracleConnection.getConnection(url, newUserName, password);
        Statement stat = con.createStatement();
        String sql = "create user "+newUserName+" identified by "+newPassword;

        if( stat.execute(sql)){
        	this.grantToUser(newUserName, database,stat);
        	stat.close();
        	con.close();
        	return true;
        }
       return false;
	}
    public void grantToUser(String userName,String database,Statement stat) throws SQLException{
    	
    
    	
    	String sql1 = "grant create session, create table, create view, create sequence, create procedure, " +
    			"create trigger, create type, create job  to "+userName;
    	String sql2 = "grant query rewrite to "+userName;
    	String sql3 = "grant execute on dbms_alert to "+userName;
    	String sql4 = "grant execute on sys.dbms_lock to " +userName;
    	String sql5 = "grant select on sys.v_$database to "+userName;
    	String sql6 = "grant execute on sys.dbms_aq to "+userName;
    	String sql7="grant execute on sys.dbms_aqadm to " +userName;
    	String sql8 ="grant execute on sys.dbms_aqin to "+userName;
    	String sql9 = "grant execute on sys.dbms_aq_bqview to "+userName;
    	String sql10 = "grant connect,resource to "+userName;
    	stat.execute(sql1);
    	stat.execute(sql2);
    	stat.execute(sql3);
    	stat.execute(sql4);
    	stat.execute(sql5);
    	stat.execute(sql6);
    	stat.execute(sql7);
    	stat.execute(sql8);
    	stat.execute(sql9);
    	stat.execute(sql10);

    } 
    
    public String getDatabaseName( ) throws SQLException{
    	
    	Connection con = OracleConnection.getConnection(url, userName, password);
    	Statement stat = con.createStatement();
    	ResultSet set = stat.executeQuery("select sys_context('userenv', 'db_name') from dual");
    	String str = null;
    	while(set.next()){
    		str=set.getString(0);
    		break;
    	}
    	stat.close();
    	con.close();
    	return str;
    }
    
    public String getDefaultTableSpace() throws SQLException{
    	
    	Connection con = OracleConnection.getConnection(url, userName, password);
    	Statement stat = con.createStatement();
    	String tableSpace=null;
    	String sql = "select username,default_tablespace from user_users";
    	ResultSet set = stat.executeQuery(sql);
    	if(set.next()){
    		tableSpace = set.getString("default_tablespace");
    	}
    	stat.close();
    	con.close();
    	return tableSpace;
    }
    
    public List<String> getUserSysPrivs() throws SQLException{
    	
    	Connection con = OracleConnection.getConnection(url, userName, password);
    	Statement stat = con.createStatement();
    	String sql = "select * from user_sys_privs";
        ResultSet set = stat.executeQuery(sql);
        List<String> list = new ArrayList<String>();
        while(set.next()){
        	String str = set.getString("PRIVILEGE");
        	list.add(str);
        }
        stat.close();
    	con.close();
        return list;
    }
    
	public List<String> getUserTabPrivs() throws SQLException{
		Connection con = OracleConnection.getConnection(url, userName, password);
    	Statement stat = con.createStatement();
		String sql = " select table_name, privilege from user_tab_privs";
		ResultSet set = stat.executeQuery(sql);
		List<String> list = new ArrayList<String>();
		while(set.next()){
			
			list.add(set.getString("privilege")+" on "+set.getString("table_name"));
		}
		stat.close();
    	con.close();
		return list;
	}
	
	public Map<String,String> getUserTables() throws SQLException{
		
		Connection con = OracleConnection.getConnection(url, userName, password);
    	Statement stat = con.createStatement();
		String sql = "select table_name from user_tables";
		ResultSet set = stat.executeQuery(sql);
		List<String> list= new ArrayList<String>();
		while(set.next()){	
			list.add(set.getString("table_name"));
		}
		Map<String,String> map = new HashMap<String, String>();
		
		for (String string : list) {
		    
			sql="select TABLESPACE_NAME from all_tables where table_name="+"'"+string.toUpperCase()+"'";
		    set = stat.executeQuery(sql);
		    if(set.next()){
		    	String tableSpace = set.getString("TABLESPACE_NAME");
		    	map.put(string, tableSpace);
		    }else{
		    	map.put(string, null);
		    }  
		}
		stat.close();
    	con.close();
		return map;
	}

	public  Vector<TableInfo> getAllTableInfos() throws SQLException{
		Vector<TableInfo> tableInfos = new Vector<TableInfo>();
		Connection con = OracleConnection.getConnection(url, userName, password);
    	Statement stat = con.createStatement();
		String sql = "select table_name from user_tables";
		ResultSet set = stat.executeQuery(sql);
		List<String> list= new ArrayList<String>();
		while(set.next()){	
			String tableName = set.getString("table_name");
			if(tableName.equalsIgnoreCase("sy$sources")||
					tableName.equalsIgnoreCase("sy$txlog")||
					tableName.equalsIgnoreCase("PATCH_VERCONTROL")||
					tableName.equalsIgnoreCase("SYNC_CORE_SETTINGS")||
					tableName.startsWith("sy$de")||
					tableName.startsWith("SY$DE")
					)
				continue;
			list.add(tableName);
		}
		CreateSchema cs = new CreateSchema(null);
		for (String tableName : list) {
			TableInfo tableInfo = cs.getSchemaWithDB(tableName, con);
			if(tableInfo!=null)
				tableInfos.add(tableInfo);
		}
		stat.close();
		con.close();
		return tableInfos;
	}
	
	
	
}
