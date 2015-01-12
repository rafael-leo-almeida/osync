package com.jtang.monitor.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableMap;
import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;
import com.jtang.monitor.config.base.AddSource;
import com.jtang.syn.comm.Message;
import com.jtang.syn.comm.OracleConnection;
import com.jtang.syn.netty.ClientManager;


public class MonitorService {

	public static String addNewMonitorSource(MonitorThreadManager monitorThreadManager,
			ConcurrentLinkedQueue<Message> queue,TableMapManager tableMaps,ClientManager clientManager,
			Tenant tanent,String tableName) throws SQLException{
		
		MonitorThread monitorThread = monitorThreadManager.getMonitorThreadByTanent(tanent);
		if(monitorThread==null){
			monitorThread = new MonitorThread(tanent, queue, tableMaps, clientManager);
		    monitorThreadManager.addMonitorThread(monitorThread);
		}
		if(isExistSourceTables(tanent, tableName)){
			Connection con = OracleConnection.getConnection(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
			TableInfo table = new CreateSchema(null).getSchemaWithDB(tableName, con);
			TableInfo deTable = new CreateSchema(null).getSchemaWithDB("sy$de"+tableName, con);
			monitorThread.addMonitorDeleteTable(deTable);
			monitorThread.addMonitorInserAndUpdateTable(table);
			return "true";
		}
		return "false";
	}
	
	public static boolean isExistSourceTables(Tenant tanent,String sourceName) throws SQLException{
		String sql = "select table_name from user_tables";
		Connection con = OracleConnection.getConnection(tanent.getUrl(), tanent.getUserName(), tanent.getPassword());
		Statement stat = con.createStatement();
		ResultSet set = stat.executeQuery(sql);
		boolean f0=false;
		boolean f1=false;
		while(set.next()){
			if(set.getString("table_name").equalsIgnoreCase(sourceName)){
				f0=true;
			}else if(set.getString("table_name").equalsIgnoreCase("sy$de"+sourceName)){
				f1=true;
			}
		}
		return (f0&&f1);
	}
	
	public static void initMonitorService(TenantManager tenantManager,TableMapManager tableMapManager,
			MonitorThreadManager monitorThreadManager,ConcurrentLinkedQueue<Message> queue,
			ClientManager clientManager){
		
		List<Tenant> tenants = tenantManager.getTanents();
		List<TableMap> tableMaps = tableMapManager.getTableMaps();
		if(tenants==null||tenants.size()<1||tableMaps==null||tableMaps.size()<1){
			return;
		}
		for (TableMap tableMap : tableMaps) {
			SourceInfo sInfo = tableMap.getFromTable().getSourceInfo();
			Tenant tenant = tenantManager.getTanentByOthers(sInfo.getIp(), sInfo.getPort(),
					sInfo.getDBName(), sInfo.getUserName());
			String tableName = sInfo.getTableName();
			if(tenant==null){
				continue;
			}
			try {
				AddSource addSource = new AddSource(tableName, tenant.getUrl(), tenant.getUserName(), tenant.getPassword());
				addSource.addNewSource();
				MonitorService.addNewMonitorSource(monitorThreadManager, queue, 
						tableMapManager, clientManager, tenant, tableName);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	
}
