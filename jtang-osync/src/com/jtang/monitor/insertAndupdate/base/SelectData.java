package com.jtang.monitor.insertAndupdate.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.syn.comm.Message;


public class SelectData {

	public SourceInfo sourceInfo;
	public Connection con;
	public Statement stat;
	public MonitorTableManager manager;
	public SelectData(SourceInfo sourceInfo ,Connection con,MonitorTableManager manager) throws SQLException{
		
		this.sourceInfo = sourceInfo;
		this.con = con;
		this.stat= con.createStatement();
		this.manager=manager;
	}
	
	public List<Message> select(Txlog log) throws SQLException{
		
		List<Message> datas = new ArrayList<Message>();
		TableInfo table = manager.getTableByName(log.getMaskName());
		if(table==null)
			return datas;
		List<String> fields = table.getColumnName();
		Map<String,String> types = table.getColumnType();
		ResultSet set = this.stat.executeQuery("select * from "+log.getMaskName()+" where txn="+log.getTxn());
		while(set.next()){
			Message data = new Message();
			SourceInfo sourceInfo = new SourceInfo();
			sourceInfo.setIp(this.sourceInfo.getIp());
			sourceInfo.setPort(this.sourceInfo.getPort());
			sourceInfo.setDBName(this.sourceInfo.getDBName());
			sourceInfo.setTableName(log.getMaskName());
			sourceInfo.setUserName(this.sourceInfo.getUserName());
			data.setSourceInfo(sourceInfo);
			Map<String,Object> datasMap = new HashMap<String, Object>();
			for (String field : fields) {
				//TODO: add more datas
				String type = types.get(field);
				if(type.equalsIgnoreCase("number")){
					
					datasMap.put(field, set.getLong(field));
				    
				}else if(type.equalsIgnoreCase("varchar2")){
					datasMap.put(field, set.getString(field));
					
				}else if(type.equalsIgnoreCase("date")){
					
					java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
							"yyyymmdd hh:mm:ss");
					String date = format.format(set.getDate(field));
					datasMap.put(field, date);
				}
			}
			data.setDatas(datasMap);
			if(log.getOpt()==0)
				data.setOperation("update");
			else 
				data.setOperation("insert");
			datas.add(data);
		}
		return datas;
	} 
	
}
