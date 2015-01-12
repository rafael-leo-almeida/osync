package com.jtang.logmnr.base;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public class AddLogfile {

	public static void addLogfiles(List<String> files) {

		if (files == null || files.size() < 1) {
			return;
		}
		Connection con = ConnectionFactory.getConnection();
		if (con == null) {
			return;
		}
		for (int i = 0; i < files.size(); i++) {
			String sql = "";
			if (i == 0) {
				sql += "dbms_logmnr.add_logfile(options => dbms_logmnr.new,logfilename=>'"
						+ files.get(i) + "')";
			} else {
				sql += "dbms_logmnr.add_logfile(options => dbms_logmnr.addfile,logfilename=>'"
						+ files.get(i) + "')";
			}
			sql = "{call " + sql + "}";
			try {
				CallableStatement cst = con.prepareCall(sql);
				cst.execute();
				cst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addLogfiles(List<String> files,
			LogmnrConnection logmnrCon) {

		if (files == null || files.size() < 1) {
			return;
		}
		if (logmnrCon == null || (!logmnrCon.isOk())) {
			return;
		}
		for (int i = 0; i < files.size(); i++) {
			String sql = "";
			if (i == 0) {
				sql += "dbms_logmnr.add_logfile(options => dbms_logmnr.new,logfilename=>'"
						+ files.get(i) + "')";
			} else {
				sql += "dbms_logmnr.add_logfile(options => dbms_logmnr.addfile,logfilename=>'"
						+ files.get(i) + "')";
			}
			sql = "{call " + sql + "}";
			try {
				CallableStatement cst = logmnrCon.getCon().prepareCall(sql);
				cst.execute();
				cst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
		// ��ʾ�Ѿ�����־�ļ����ؽ���
		logmnrCon.setStatus(1);
	}

}
