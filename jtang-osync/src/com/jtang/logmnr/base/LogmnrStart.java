package com.jtang.logmnr.base;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogmnrStart {

	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void startLogmnr(String dictFileName,
			LogmnrConnection logmnrCon) {

		if (logmnrCon == null) {
			return;
		}
		String sql = "dbms_logmnr.start_logmnr(dictfilename => '"
				+ dictFileName + "')";
		sql = "{call " + sql + "}";
		CallableStatement cst = null;
		try {
			cst = logmnrCon.getCon().prepareCall(sql);
			cst.execute();
			cst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		logmnrCon.setStatus(2);
		if (LogmnrBase.logmnrContentsThread != null) {
			LogmnrBase.logmnrContentsThread.breakFlag = false;
			LogmnrBase.logmnrContentsThread.start();
		}
	}

	public static void startLogmnr(String dictFileName, Date startTime,
			LogmnrConnection logmnrCon) {

		if (startTime == null) {
			startLogmnr(dictFileName, logmnrCon);
			return;
		}
		if (logmnrCon == null) {
			return;
		}
		String startTimeStr = dateTimeFormat.format(startTime);
		String sql = "dbms_logmnr.start_logmnr(dictfilename => '"
				+ dictFileName + "',startTime=>to_date('" + startTimeStr
				+ "','YYYY-MM-DD HH24:MI:SS'))";
		CallableStatement cst = null;
		try {
			cst = logmnrCon.getCon().prepareCall(sql);
			cst.execute();
			cst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logmnrCon.setStatus(2);
		if (LogmnrBase.logmnrContentsThread != null) {
			LogmnrBase.logmnrContentsThread.breakFlag = false;
			LogmnrBase.logmnrContentsThread.start();
		}
	}

	public static void startLogmnr(String dictFileName, Date startTime,
			Date endTime, LogmnrConnection logmnrCon) {

		if (startTime == null) {
			startLogmnr(dictFileName, logmnrCon);
			return;
		}
		if (logmnrCon == null) {
			return;
		}
		String startTimeStr = dateTimeFormat.format(startTime);
		String endTimeStr = dateTimeFormat.format(endTime);
		String sql = "dbms_logmnr.start_logmnr(dictfilename => '"
				+ dictFileName + "',startTime=>to_date('" + startTimeStr
				+ "','YYYY-MM-DD HH24:MI:SS'),endTime=>to_date('" + endTimeStr
				+ "','YYYY-MM-DD HH24:MI:SS'))";
		CallableStatement cst = null;
		try {
			cst = logmnrCon.getCon().prepareCall(sql);
			cst.execute();
			cst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logmnrCon.setStatus(2);
		if (LogmnrBase.logmnrContentsThread != null) {
			LogmnrBase.logmnrContentsThread.breakFlag = false;
			LogmnrBase.logmnrContentsThread.start();
		}
	}

	public static void StopLogmnr(LogmnrConnection logmnrCon) {

		if (logmnrCon == null) {
			return;
		}
		String sql = "dbms_logmnr.end_logmnr();";
		sql = "{call " + sql + "}";
		CallableStatement cst = null;
		try {
			cst = logmnrCon.getCon().prepareCall(sql);
			cst.execute();
			cst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		logmnrCon.setStatus(3);
		if (LogmnrBase.logmnrContentsThread != null) {
			LogmnrBase.logmnrContentsThread.breakFlag = true;
		}

	}

}
