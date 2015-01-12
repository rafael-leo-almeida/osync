package com.jtang.common.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MapJsonDaoImpl implements MapJsonDao {

	public MapJson add(MapJson mapJson) {
		String sql = "insert into mapJson(map_json,status) values(?,?)";
	    Connection con = MysqlConnection.getConnection();
	    if(con==null){
	    	return null;
	    }
	    PreparedStatement pStat = null;
	    int id = -1;
	    try {
			pStat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pStat.setString(1, mapJson.getMapJson());
			pStat.setInt(2, mapJson.getStatus());
			pStat.executeUpdate();
			ResultSet set = pStat.getGeneratedKeys();
			if(set.next()){
				id=set.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    return this.getMapJsonById(id);
	}

	public List<MapJson> getAllMap() {

		String sql = "select * from mapJson";
		Connection con = MysqlConnection.getConnection();
        if(con==null){
        	return null;
        }
        PreparedStatement pStat = null;
        List<MapJson> mapJsons = new ArrayList<MapJson>();
        try {
			pStat = con.prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			mapJsons = this.createMapJsons(set);
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
		return mapJsons;
	}

	public void setAllStop() {
		String sql = "update mapJson set status=0";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return;
		}
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
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

	public MapJson setMapJson(int id, int status) {
		String sql = "update mapJson set status=? where id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		PreparedStatement pStat = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setInt(1, status);
			pStat.setInt(2, id);
			pStat.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.getMapJsonById(id);
	}

	public MapJson getMapJsonById(int id) {
		
		String sql = "select * from mapJson where id=?";
		Connection con = MysqlConnection.getConnection();
		if(con==null){
			return null;
		}
		PreparedStatement pStat = null;
		List<MapJson> mapJsons = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setInt(1, id);
			ResultSet set = pStat.executeQuery();
			mapJsons = this.createMapJsons(set);
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
	    if(mapJsons==null&&mapJsons.size()>0){
	    	return mapJsons.get(0);
	    }
	    return null;
	}

	public List<MapJson> createMapJsons(ResultSet set) throws SQLException{
		
		List<MapJson> mapJsons = new ArrayList<MapJson>();
		if(set==null){
			return mapJsons;
		}
		while(set.next()){
			MapJson mapJson = new MapJson();
			mapJson.setId(set.getInt("id"));
			mapJson.setMapJson(set.getString("map_json"));
			mapJson.setStatus(set.getInt("status"));
			mapJsons.add(mapJson);
		}
		return mapJsons;
	}
}
