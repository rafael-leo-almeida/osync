package com.jtang.logmnr.base;

import java.sql.Connection;
import java.sql.SQLException;

//由于增加日志文件，启动logmnr和获取分析结果需要在一个会话里面
//所以需要有一个专门的Connection做这件事情
public class LogmnrConnection {
	// 0表示该connection是新的，没有增加过日志文件，没有启动
	// 1表是已经增加完日志文件
	// 2表示已经启动分析
	// 3表示logmnr已经停止，需要重先启动
	private int status;
	private Connection con;

	public LogmnrConnection(Connection con) {
		this.con = con;
		this.status = 0;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	//判断这个连接是否可用
	public boolean isOk() {
		try {
			if (this.con == null || this.con.isClosed()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
