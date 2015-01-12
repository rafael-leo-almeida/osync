package com.jtang.logmnr.base;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.syn.comm.Message;

public class LogmnrBase {

	public static List<String> logs_path = null;
	public static String dict_path = null;
	public static LogmnrConnection logmnrConnection = null;
	public static final LogmnrMonitorTableManager logmnrMonitorTableManager = new LogmnrMonitorTableManager();
	public static LogmnrContentsThread logmnrContentsThread = null;
	public static String ip="";
	public static int port=0;
	public static String DBName="";
	public static java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
			"yyyymmdd hh:mm:ss");
	public static ConcurrentLinkedQueue<Message> queue;
	
}
