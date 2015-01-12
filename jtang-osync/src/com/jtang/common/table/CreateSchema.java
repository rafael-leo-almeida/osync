package com.jtang.common.table;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import com.jtang.common.table.TableInfo;
import com.jtang.syn.comm.OracleConnection;
public class CreateSchema {

    public static final String DEFAULT_NAMESPACE = "com.linkedin.events.example.person";
    public static final String DEFAULT_RECORDNAME="Person";
    public static final String DEFAULT_VIEWNAME="sy$person";
    public static final String DEFAULT_AVROOUTDIR="/home/oracle/test/schemas_registry";
    public static final int DEFAULT_AVROOUTVERSION=1;
    public static final String DEFAULT_JAVAOUTDIR="/home/oracle/test/databus2-example-events/databus2-example-person/src/main/java";
    private static final String[] DEFAULT_JDBC_DRIVERS = {"oracle.jdbc.driver.OracleDriver"};
    private static final String DEFAULT_DATABASE = "jdbc:oracle:thin:@localhost:1521:db";
    private static final String DEFAULT_USERNAME = "person";
    private static final String DEFAULT_PASSWORD = "person";
    String nameSpace;
    String recordName;
    String viewName;
    String avroOutDir;
    Integer avroOutVersion;
    String javaOutDir;
    String userName;
    String password;
    String[] args;
    String database;
    public CreateSchema(Map<String,String> values){	
    	//this.viewName = values.get("viewName");
    	if(this.viewName==null){
    		System.out.println("the values is not validate!");
    		return;
    	}
    	this.viewName = values.get("viewName");
    	if((this.nameSpace=values.get("nameSpace"))==null){
    		this.nameSpace="com.linkedin.events.example."+viewName;
    	}
    	if((this.recordName=values.get("recordName"))==null){
    		this.recordName=this.viewName;
    	}
    	if((this.avroOutDir=values.get("avroOutDir"))==null){
    		this.avroOutDir=CreateSchema.DEFAULT_AVROOUTDIR;
    	}
    	if(values.get("avroOutVersion")==null){
    		this.avroOutVersion=CreateSchema.DEFAULT_AVROOUTVERSION;
    	}else{
    		this.avroOutVersion=Integer.parseInt(values.get("avroOutVersion"));
    	}
    	if((this.javaOutDir=values.get("javaOutDir"))==null){
    		this.javaOutDir=CreateSchema.DEFAULT_JAVAOUTDIR;
    	}
    	if((this.userName=values.get("userName"))==null){
    		this.userName=CreateSchema.DEFAULT_USERNAME;
    	}
    	if((this.password=values.get("password"))==null){
    		this.password=CreateSchema.DEFAULT_PASSWORD;
    	}
    	if((this.database=values.get("database"))==null)
    		this.database=CreateSchema.DEFAULT_DATABASE;
    	this.args=new String[]{"-namespace",this.nameSpace,"-recordName", this.recordName, 
    "-viewName",this.viewName, "-avroOutDir",this.avroOutDir, "-avroOutVersion",this.avroOutVersion.toString(),
    "-javaOutDir",this.javaOutDir, "-userName",this.userName, "-password", this.password,"-database",this.database};
    	
    }

    public TableInfo getSchemaWithDatabus() throws IOException, JSONException{
    	
    	return null;
    }
    
    public TableInfo getSchemaWithDB() throws SQLException{
    	
    	TableInfo tableInfo = new TableInfo();
    	tableInfo.setTableName(this.viewName);
    	List<String>columnName = new ArrayList<String>();
    	List<String> keys = new ArrayList<String>();
    	Map<String,String> columnType=new HashMap<String, String>();

    	Connection con = OracleConnection.getConnection(this.database, this.userName, this.password);
    	Statement stmt = con.createStatement();
    	String sql="select COLUMN_NAME,DATA_TYPE from user_tab_columns "+
    	            " where table_name ='"+this.viewName.toUpperCase()+"'";
    	ResultSet set = stmt.executeQuery(sql);
    	while(set.next()){
    		String name = set.getString("COLUMN_NAME");
    		String type = set.getString("DATA_TYPE");
    		columnName.add(name);
    		columnType.put(name, type);
    	}
    	String keySql = "select col.column_name from user_constraints con,  user_cons_columns col "+
    	                " where con.constraint_name = col.constraint_name and con.constraint_type='P' "+
    	                " and col.table_name = '"+this.viewName.toUpperCase()+"'";
        set=stmt.executeQuery(keySql);
        while(set.next()){
        	String name = set.getString("column_name");
        	keys.add(name);
        }
        tableInfo.setColumnName(columnName);
        tableInfo.setColumnType(columnType);
        tableInfo.setKeys(keys);
    	con.close();
    	return tableInfo;
    }
    
 public static TableInfo getSchemaWithDB(String tableName,Connection con) throws SQLException{
    	
    	TableInfo tableInfo = new TableInfo();
    	tableInfo.setTableName(tableName);
    	List<String>columnName = new ArrayList<String>();
    	List<String> keys = new ArrayList<String>();
    	Map<String,String> columnType=new HashMap<String, String>();
    	Statement stmt = con.createStatement();
    	String sql="select COLUMN_NAME,DATA_TYPE from user_tab_columns "+
    	            " where table_name ='"+tableName.toUpperCase()+"'";
    	ResultSet set = stmt.executeQuery(sql);
    	while(set.next()){
    		String name = set.getString("COLUMN_NAME");
    		String type = set.getString("DATA_TYPE");
    		columnName.add(name);
    		columnType.put(name, type);
    	}
    	String keySql = "select col.column_name from user_constraints con,  user_cons_columns col "+
    	                " where con.constraint_name = col.constraint_name and con.constraint_type='P' "+
    	                " and col.table_name = '"+tableName.toUpperCase()+"'";
        set=stmt.executeQuery(keySql);
        while(set.next()){
        	String name = set.getString("column_name");
        	keys.add(name);
        }
        tableInfo.setColumnName(columnName);
        tableInfo.setColumnType(columnType);
        tableInfo.setKeys(keys);
        stmt.close();
    	return tableInfo;
    }
    
	
	
}
