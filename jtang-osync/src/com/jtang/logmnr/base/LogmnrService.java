package com.jtang.logmnr.base;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.jtang.common.table.TableInfo;
import com.jtang.syn.comm.Message;

public class LogmnrService {

	/*
	 * 
	 * String ip 本机ip为了与其它机器上的客户端同步，不要写127.0.0.1，只有第一次设置的时候有效
	 * int port 数据库端口，默认1521，只有第一次设置的时候有效
	 * String DBName 数据库名称，只有第一次设置的时候有效
	 * tableSpace表示表空间
	 * tableName表名称
	 * logsPath日志目录，如果不是第一次配置，可以为空 
	 * dictPath字典目录,如果不是第一次配置，可以为空
	 * tableInfo 需要监空表的详细信息
	 * dbaUserName dba用户名
	 * dbaPassword dba用户密码
	 */
	public static void addSource(String ip,int port,String dbName,String tableSpace,String tableName,TableInfo tableInfo,
			List<String> logsPath,String dictPath,String dbaUserName,String dbaPassword){
		
		//如果是第一次加载监空对象
		if(StringUtils.isBlank(LogmnrBase.dict_path)||LogmnrBase.logs_path==null||LogmnrBase.logmnrConnection==null){
			ConnectionFactory.dbaUser = dbaUserName;
			ConnectionFactory.dbaPassword = dbaPassword;
			LogmnrBase.ip=ip;
			LogmnrBase.port=port;
			LogmnrBase.DBName=dbName;
			ConnectionFactory.url="jdbc:oracle:thin:@"+ip+":"+port+":"+dbName;
			LogmnrBase.dict_path = dictPath;
			LogmnrBase.logs_path = logsPath;
			LogmnrBase.logmnrConnection = ConnectionFactory.getLogmnrConnection();
			//添加监空DML
			AddMonitorDML.monitorDML(LogmnrBase.logmnrConnection);
			//加载日志文件
			if(logsPath!=null&&logsPath.size()>0){
				AddLogfile.addLogfiles(LogmnrBase.logs_path, LogmnrBase.logmnrConnection);
			}
			LogmnrBase.logmnrMonitorTableManager.addMonitorTable(tableInfo);
			//启动分析
			LogmnrStart.startLogmnr(LogmnrBase.dict_path, LogmnrBase.logmnrConnection);
		}else{
			//是否要重加载日志文件或dictPath
			if((logsPath==null||logsPath.size()==0)&&StringUtils.isBlank(dictPath)){
				LogmnrBase.logmnrMonitorTableManager.addMonitorTable(tableInfo);
			}else{
				LogmnrStart.StopLogmnr(LogmnrBase.logmnrConnection);
				if(logsPath!=null&&logsPath.size()>0){
					LogmnrBase.logs_path=logsPath;
					AddLogfile.addLogfiles(LogmnrBase.logs_path, LogmnrBase.logmnrConnection);
				}
				if(StringUtils.isNotBlank(dictPath)){
					LogmnrBase.dict_path = dictPath;
				}
				LogmnrBase.logmnrMonitorTableManager.addMonitorTable(tableInfo);
				//启动分析
				LogmnrStart.startLogmnr(LogmnrBase.dict_path, LogmnrBase.logmnrConnection);
				
			}
		}
		
	}
	
}
