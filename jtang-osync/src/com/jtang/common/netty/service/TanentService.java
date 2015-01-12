package com.jtang.common.netty.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.TableInfo;
import com.jtang.common.tenant.AllTanentInfos;
import com.jtang.common.tenant.ConfigedTables;
import com.jtang.common.tenant.Privs;
import com.jtang.common.tenant.TablesOfTanent;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantInfos;
import com.jtang.common.tenant.TenantManager;
import com.jtang.monitor.config.base.AddSource;
import com.jtang.monitor.config.base.UserConfig;
import com.jtang.syn.comm.OracleConnection;

public class TanentService {

	

	/*
	 * get all permissions of the tanent
	 */
	public static String getTanentPrivs(Tenant tanent) throws SQLException{
		
		UserConfig config = new UserConfig(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
		List<String> tabPrivs = config.getUserTabPrivs();
		List<String> sysPrivs = config.getUserSysPrivs();
		tabPrivs.addAll(sysPrivs);
		Privs p = new Privs();
		p.setAllPrivs(tabPrivs);
		return p.toString();
		
	}
	/*
	 * add a new tanent 
	 * 
	 */
	public static boolean AddTanent(TenantManager tanentPool,Tenant tanent){
		
		try{
		    /*
		     * make sure that the tanent is valid
		     */
			Connection con = OracleConnection.getConnection(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
		    if(con!=null){
		       tanentPool.addTanent(tanent);
		       con.close();
		       return true;
		    }else
		    	return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * get the table of the Tanent
	 * 
	 */
	public static String getTablesOfTanent(Tenant tanent) throws SQLException{
		
		UserConfig config = new UserConfig(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
		List<TableInfo> tableInfos = config.getAllTableInfos();
		if(tableInfos==null||tableInfos.size()==0)
			return null;
		TablesOfTanent tables = new TablesOfTanent();
		tables.setTableInfos(tableInfos);
		return tables.toString();
		
	}
	public static String getAllTanents(TenantManager tanentPool) throws SQLException{
		
		AllTanentInfos allTanentInfos = new AllTanentInfos();
		int size = tanentPool.getTanents().size();
		for(int i =0;i<size;i++){
			
			TenantInfos tanentInfos = new TenantInfos();
			Tenant tanent = tanentPool.getTanents().get(i);
			UserConfig config = new UserConfig(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
			List<String> tabPrivs = config.getUserTabPrivs();
			List<String> sysPrivs = config.getUserSysPrivs();
			tabPrivs.addAll(sysPrivs);
			Privs privs = new Privs();
			privs.setAllPrivs(tabPrivs);
			List<TableInfo> tableInfos = config.getAllTableInfos();
			tanentInfos.setTanent(tanent);
			tanentInfos.setTableInfos(tableInfos);
			tanentInfos.setPrivs(privs);
			allTanentInfos.add(tanentInfos);
			
		}
		
		return allTanentInfos.toString();
	}
	
	public static String getAllConfigedTables(Tenant tanent) throws SQLException{
		
		String sql = "select * from sy$sources";
		Connection con = OracleConnection.getConnection(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql);
		List<String> tables = new ArrayList<String>();
		while(set.next()){
			tables.add(set.getString("name"));
		}
		ConfigedTables configedTables = new ConfigedTables();
		configedTables.setConfigedTables(tables);
		return configedTables.toString();
	}
	
	public static String configNewSource(Tenant tanent,String sourceName) throws SQLException{
		
		AddSource addSource = new AddSource(sourceName, tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
		if(addSource.addNewSource())
			return "true";
		else
			return "false";
	}
	
	public static boolean testConnect(Tenant tanent,String tableName){
		
		Connection con = com.jtang.monitor.delete.OracleConnection.getConnection(tanent.getIp(),
				tanent.getPort(), tanent.getDBName(), tanent.getUserName(), tanent.getPassword());
		boolean flag = false;
		if(con==null){
			return flag;
		}else{
			String sql = "select * from user_tables where table_name=?";
			PreparedStatement pStat = null;
			
			try {
				pStat = con.prepareStatement(sql);
				pStat.setString(1, tableName.toUpperCase());
				ResultSet set = pStat.executeQuery();
				if(set.next()){
					flag=true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					pStat.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	public static String getTableSchema(Tenant tanent,String tableName){
		
		Connection con = com.jtang.monitor.delete.OracleConnection.getConnection(tanent.getIp(),
				tanent.getPort(), tanent.getDBName(), tanent.getUserName(), tanent.getPassword());
		String result = null;
		if(con!=null){
			try {
				TableInfo tableInfo = CreateSchema.getSchemaWithDB(tableName, con);
				if(tableInfo!=null){
					result = tableInfo.toString();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	
}
