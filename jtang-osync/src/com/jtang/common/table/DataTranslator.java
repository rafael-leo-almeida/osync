package com.jtang.common.table;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jtang.common.table.SourceInfo;
import com.jtang.syn.comm.Message;

public class DataTranslator {

	private TableMap tableMap;

	public DataTranslator(TableMap tableMap) {
		this.tableMap = tableMap;
	}

	public Message transData(Map<String, Object> values, SourceInfo sourceInfo,
			String operation) {
		
		Message info = new Message();
		info.setOperation(operation);
		info.setSourceInfo(sourceInfo);
		Map<String, Object> datas = new HashMap<String, Object>();
		// TODO:
		for (String fieldName : this.tableMap.getValiFields()) {

			String fromName = null;
			if (operation.equalsIgnoreCase("delete")) {
				fromName = (fieldName.split(":"))[0];
			} else {
				fromName = (fieldName.split(":"))[1];
			}
			String toName = this.tableMap.getFieldsMap().get(fieldName);
			String fromType = this.tableMap.getFromTable().getTableInfo()
					.getColumnType().get(fieldName);
			String toType = this.tableMap.getToTable().getTableInfo()
					.getColumnType().get(toName);
			if (fromType.equalsIgnoreCase("string")
					&& toType.equalsIgnoreCase("VARCHAR2")) {
				datas.put(toName, (String) values.get(fromName));
			} else if (fromType.equalsIgnoreCase("long")
					&& toType.equalsIgnoreCase("number")) {
				Long value = (Long) values.get(fromName);
				if(value==null)
					datas.put(toName, null);
				else
				   datas.put(toName, value.toString());
			} else if (fromType.equalsIgnoreCase("long")
					&& toType.equalsIgnoreCase("date")) {
				java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
						"yyyymmdd hh:mm:ss");
				Long value = (Long) values.get(fromName);
				if(value==null){
					datas.put(toName, null);
				}else{			
				Date d = new Date(value);
				String strD = format.format(d);
				datas.put(toName, strD);
				}
			}
			// TODO: add more rules;
		}
		info.setDatas(datas);
		return info;
	}
	
	public static Message transData(Message sourceMsg,TableMap tableMap){
		Map<String, Object> values = sourceMsg.getDatas();
		SourceInfo sourceInfo = sourceMsg.getSourceInfo();
		String operation = sourceMsg.getOperation();
		Message info = new Message();
		Map<String, Object> datas = new HashMap<String, Object>();
		// TODO:
//		System.out.println(tableMap.getFromTable().getTableInfo());
//		System.out.println(tableMap.getToTable().getTableInfo());
//		System.out.println(sourceMsg);
		
		for (String fieldName : tableMap.getValiFields()) {

			String toName = tableMap.getFieldsMap().get(fieldName);
			String fromType = tableMap.getFromTable().getTableInfo()
					.getColumnType().get(fieldName);
			String toType = tableMap.getToTable().getTableInfo()
					.getColumnType().get(toName);
			if (fromType.equalsIgnoreCase("varchar2")
					&& toType.equalsIgnoreCase("VARCHAR2")) {
				
				datas.put(toName, (String) values.get(fieldName));
			
			} else if (fromType.equalsIgnoreCase("number")
					&& toType.equalsIgnoreCase("number")) {
			    //System.out.println("name:"+fieldName);
				String value = (String) values.get(fieldName);
				   datas.put(toName, value.toString());
		
			} else if (fromType.equalsIgnoreCase("date")
					&& toType.equalsIgnoreCase("date")) {
				
				datas.put(toName, datas.get(fieldName));

			}
			// TODO: add more rules;
		}
		info.setOperation(operation);
		info.setSourceInfo(sourceInfo);
		info.setDatas(datas);
		return info;
	}
	

}
