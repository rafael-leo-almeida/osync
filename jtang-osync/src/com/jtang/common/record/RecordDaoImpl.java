package com.jtang.common.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class RecordDaoImpl implements RecordDao{

	public long getCountByHour(Date day) {
		String sql = "select count(*) from record where send_time>=? and send_time<?";
		@SuppressWarnings("deprecation")
		long startTime = day.getTime()-(day.getMinutes()*60+day.getSeconds())*1000;
		long endTime = startTime+60*60*1000;
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		PreparedStatement pStat = null;
		long count = 0;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setTimestamp(1, new Timestamp(startTime));
			pStat.setTimestamp(2, new Timestamp(endTime));
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public long getCountByDay(Date day) {
		String sql = "select count(*) from record where send_time>=? and send_time<?";
		@SuppressWarnings("deprecation")
		long startTime = day.getTime()-(day.getHours()*60*60+day.getMinutes()*60+day.getSeconds())*1000;
		long endTime = startTime+24*60*60*1000;
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		PreparedStatement pStat = null;
		long count = 0;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setTimestamp(1, new Timestamp(startTime));
			pStat.setTimestamp(2, new Timestamp(endTime));
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}


	public long getCountByHourMap(Date day, int mapId) {
		
		String sql = "select count(*) from record where send_time>=? and send_time<? and map_id=?";
		@SuppressWarnings("deprecation")
		long startTime = day.getTime()-(day.getMinutes()*60+day.getSeconds())*1000;
		long endTime = startTime+60*60*1000;
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		PreparedStatement pStat = null;
		long count = 0;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setTimestamp(1, new Timestamp(startTime));
			pStat.setTimestamp(2, new Timestamp(endTime));
			pStat.setInt(3, mapId);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public long getCountByDayMap(Date day, int mapId) {
		String sql = "select count(*) from record where send_time>=? and send_time<? and map_id=?";
		@SuppressWarnings("deprecation")
		long startTime = day.getTime()-(day.getHours()*60*60+day.getMinutes()*60+day.getSeconds())*1000;
		long endTime = startTime+24*60*60*1000;
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		PreparedStatement pStat = null;
		long count = 0;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setTimestamp(1, new Timestamp(startTime));
			pStat.setTimestamp(2, new Timestamp(endTime));
			pStat.setInt(3, mapId);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}
	

	public long getWrongCountByMap(int mapId) {

		String sql = "select count(*) from record where status=0 and map_id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, mapId);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public long getRightCountByMap(int mapId) {
		String sql = "select count(*) from record where status=1 and map_id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, mapId);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public long getWrongCount() {
		String sql = "select * from record where status=0";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public long getRightCount() {
		String sql = "select * from record where status=1";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public long getCountByType(int type) {
		String sql = "select * from record where type=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setInt(1, type);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
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
		return count;
	}

	public long getCountByTypeMap(int type, int mapId) {
		String sql = "select * from record where type=? and map_id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setInt(1, type);
			pStat.setInt(2, mapId);
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
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
		return count;
	}

	public Record add(Record record) {
		String sql = "insert into record(map_id,key_value,status,type,get_time,send_time,return_time) " +
				" values(?,?,?,?,?,?,?)";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		long id = -1;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pStat.setInt(1,record.getMapId());
			if(StringUtils.isBlank(record.getKeyValue())){
				pStat.setNull(2, Types.VARCHAR);
			}else{
				pStat.setString(2, record.getKeyValue());
			}
			pStat.setInt(3, record.getStatus());
			pStat.setInt(4, record.getType());
			if(record.getGetTime()==null){
				pStat.setNull(5, Types.TIMESTAMP);
			}else{
				pStat.setTimestamp(5, new Timestamp(record.getGetTime().getTime()));
			}
			
			if(record.getSendTime()==null){
			    pStat.setNull(6, Types.TIMESTAMP);	
			}else{
				pStat.setTimestamp(6, new Timestamp(record.getSendTime().getTime()));
			}
			
			if(record.getReturnTime()==null){
			    pStat.setNull(7, Types.TIMESTAMP);	
			}else{
				pStat.setTimestamp(7, new Timestamp(record.getReturnTime().getTime()));
			}
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return this.getRecordById(id);
	}

	public void updateRTStatusTypeById(long id, Date receiveTime, int status,
			int type) {
		String sql = "update record set return_time=?,status=?,type=? where id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return;
		}
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			if(receiveTime==null){
				pStat.setNull(1, Types.TIMESTAMP);
			}else{
				pStat.setTimestamp(1, new Timestamp(receiveTime.getTime()));
			}
			pStat.setInt(2, status);
			pStat.setInt(3, type);
			pStat.setLong(4, id);
			pStat.executeUpdate();
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
		return; 
	}

	public void updateRTStatusById(long id, Date receiveTime, int status) {
		String sql = "update record set return_time=?,status=? where id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return;
		}
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			if(receiveTime==null){
				pStat.setNull(1, Types.TIMESTAMP);
			}else{
				pStat.setTimestamp(1, new Timestamp(receiveTime.getTime()));
			}
			pStat.setInt(2, status);
			pStat.setLong(3, id);
			pStat.executeUpdate();
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
		return;
	}

    public List<Record> createRecord(ResultSet set) throws SQLException{
    	
    	List<Record> records = new ArrayList<Record>();
    	if(set==null){
    		return records;
    	}
    	while(set.next()){
    		Record record = new Record();
    		record.setGetTime(set.getTimestamp("get_time"));
    		record.setId(set.getLong("id"));
    		record.setKeyValue(set.getString("key_value"));
    		record.setMapId(set.getInt("map_id"));
    		record.setReturnTime(set.getTimestamp("return_time"));
    		record.setSendTime(set.getTimestamp("send_time"));
    		record.setStatus(set.getInt("status"));
    		record.setType(set.getInt("type"));
    		records.add(record);
    	}
    	return records;
    }

	public Record getRecordById(long id) {
		String sql = "select * from record where id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		PreparedStatement pStat = null;
		List<Record> records = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setLong(1, id);
			ResultSet set = pStat.executeQuery();
			records = this.createRecord(set);
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
		if(records==null||records.size()<1){
			return null;
		}
		return records.get(0);
	}

	public long getWrongCountByMapDay(int mapId, Date day) {
		
		if(day==null){
			return 0;
		}
		long startTime = day.getTime()-(day.getHours()*60*60+day.getMinutes()*60+day.getSeconds())*1000;
		long endTime = startTime+24*60*60*1000;
		String sql = "select count(*) from record where status=0 and map_id=? " +
				" and send_time>=? and send_time<?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, mapId);
		    pStat.setTimestamp(2, new Timestamp(startTime));
		    pStat.setTimestamp(3, new Timestamp(endTime));
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
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
		return count;
	}

	public long getWrongCountByMapWeek(int mapId, Date day) {
		if(day==null){
			return 0;
		}
		long dayTime = day.getTime()-(day.getHours()*60*60+day.getMinutes()*60+day.getSeconds())*1000;
		long startTime = dayTime-6*24*60*60*1000;
		long endTime = dayTime+24*60*60*1000;
		String sql = "select count(*) from record where status=0 and map_id=? " +
				" and send_time>=? and send_time<?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, mapId);
		    pStat.setTimestamp(2, new Timestamp(startTime));
		    pStat.setTimestamp(3, new Timestamp(endTime));
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
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
		return count;
	}

	public long getRightCountByMapDay(int mapId, Date day) {
		if(day==null){
			return 0;
		}
		long startTime = day.getTime()-(day.getHours()*60*60+day.getMinutes()*60+day.getSeconds())*1000;
		long endTime = startTime+24*60*60*1000;
		String sql = "select count(*) from record where status=1 and map_id=? " +
				" and send_time>=? and send_time<?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, mapId);
		    pStat.setTimestamp(2, new Timestamp(startTime));
		    pStat.setTimestamp(3, new Timestamp(endTime));
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
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
		return count;
	}

	public long getRightCountByMapWeek(int mapId, Date day) {
		if(day==null){
			return 0;
		}
		long dayTime = day.getTime()-(day.getHours()*60*60+day.getMinutes()*60+day.getSeconds())*1000;
		long startTime = dayTime-6*24*60*60*1000;
		long endTime = dayTime+24*60*60*1000;
		String sql = "select count(*) from record where status=1 and map_id=? " +
				" and send_time>=? and send_time<?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return 0;
		}
		long count = 0;
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, mapId);
		    pStat.setTimestamp(2, new Timestamp(startTime));
		    pStat.setTimestamp(3, new Timestamp(endTime));
		    ResultSet set = pStat.executeQuery();
		    if(set.next()){
		    	count = set.getLong(1);
		    }
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
		return count;
	}
    

}
