package com.jtang.monitor.insertAndupdate.base;
import java.math.BigInteger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jtang.monitor.insertAndupdate.base.Txlog;

public class MonitorTxlog {

	public Connection con;
	public Statement stat;
	public Statement stat0;
	public boolean isOk = false;

	public MonitorTxlog(Connection con) {

		this.con = con;
		try {
			this.stat = con.createStatement();
			this.stat0 = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Txlog> monitor(BigInteger fromScn) throws SQLException {

		List<Txlog> list = new ArrayList<Txlog>();
		if (isOk == false) {
			if (this.isExistTxlog() && this.isExistSource()) {
				isOk = true;
			}else{
				return list;
			}
		}
        
		String sql = "select * from sy$txlog where scn>" + fromScn + "and scn<"
				+ Txlog.infinity;
		ResultSet set = stat.executeQuery(sql);
		while (set.next()) {

			long txn = (Long) set.getLong("txn");
			int opt = set.getInt("opt");
			BigInteger scn = set.getBigDecimal("scn").toBigInteger();
			int mask = set.getInt("mask");
			Timestamp ts = set.getTimestamp("ts");
			String maskName = null;
			int i = 0;
			int m = 1;
			boolean valide = false;
			while (true) {
				if (m > mask){
					break;
				}else if (m == mask) {
					valide = true;
					break;
				}
				m = m*2;
				i++;
			}
			if(valide){
				String sql0 = "select name from sy$sources where BITNUM="+i;
				ResultSet set0 = stat0.executeQuery(sql0);
				if(set0.next()){
					maskName=set0.getString("name");
				}
			}
			if(maskName!=null){
				Txlog txlog = new Txlog();
				txlog.setTxn(txn);
				txlog.setTs(ts);
				txlog.setScn(scn);
				txlog.setMaskName(maskName);
				txlog.setMask(mask);
				txlog.setOpt(opt);
				list.add(txlog);
			}

		}
		return list;
	}
	public boolean isExistTxlog() throws SQLException {

		boolean f = false;
		ResultSet set = stat
				.executeQuery("select table_name from user_tables where table_name='SY$TXLOG'");
		if (set.next()) {
			f = true;
		}
		return f;
	}

	public boolean isExistSourceTable(String sourceTableName)
			throws SQLException {

		boolean f = false;
		ResultSet set = stat
				.executeQuery("select table_name from user_tables where table_name='"
						+ sourceTableName + "'");
		if (set.next()) {
			f = true;
		}
		return f;
	}

	public boolean isExistSource() throws SQLException {
		boolean f = false;
		ResultSet set = stat
				.executeQuery("select table_name from user_tables where table_name='SY$SOURCES'");
		if (set.next()) {
			f = true;
		}
		return f;
	}
	public void close() throws SQLException {
		this.stat.close();
		this.stat0.close();
	}

}
