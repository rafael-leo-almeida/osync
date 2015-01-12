package com.jtang.common.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DelayDaoImpl implements DelayDao {

	public Delay add(Delay delay) {
		
		String sql = "insert into delay(record_id,syn_delay," +
				"net_delay,time) values(?,?,?,?)";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		PreparedStatement pStat = null;
		long id = -1;
		try {
			pStat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pStat.setLong(1, delay.getRecordId());
			pStat.setLong(2, delay.getSynDelay());
			pStat.setLong(3, delay.getNetDelay());
			pStat.setTimestamp(4, new Timestamp(delay.getDateTime().getTime()));
			pStat.executeUpdate();
			ResultSet set = pStat.getGeneratedKeys();
			if(set.next()){
				id=set.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return this.getDelayById(id);
	}

	public List<Delay> getLatestDelays(int n) {
		String sql = "select * from delay order by id DESC limit 0,?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		List<Delay> delays= null;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setInt(1, n);
			ResultSet set = pStat.executeQuery();
			delays = this.createDelays(set);
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
	    return delays;
	}

	public Delay getDelayById(long id) {

		String sql = "select * from delay where id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		List<Delay> delays= null;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setLong(1, id);
			ResultSet set = pStat.executeQuery();
			delays = this.createDelays(set);
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
	    if(delays!=null&&delays.size()>0){
	    	return delays.get(0);
	    }
	    return null;
	}

	public List<Delay> createDelays(ResultSet set) throws SQLException{
		
		List<Delay> delays = new ArrayList<Delay>();
		if(set==null){
			return delays;
		}
		while(set.next()){
			Delay delay = new Delay();
			delay.setId(set.getLong("id"));
			delay.setRecordId(set.getLong("record_id"));
			delay.setSynDelay(set.getLong("syn_delay"));
			delay.setNetDelay(set.getLong("net_delay"));
			delay.setDateTime(new Date(set.getTimestamp("time").getTime()));
			delays.add(delay);
		}
		return delays;
	}
	
}
