package com.offer.dingzhi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.offer.dingzhi.dao.UniversityDao;
import com.offer.dingzhi.entity.Offer;
import com.offer.dingzhi.entity.University;
import com.offer.dingzhi.util.ConnectionFactory;
import com.offer.dingzhi.util.OfferConst;

public class UniversityDaoImpl implements UniversityDao {

	@Override
	public List<University> getUniversityByCityCode(String code) {
		if(StringUtils.isBlank(code)){
			return null;
		}
		String sql = "select * from university where city_code=?";
		Connection con = ConnectionFactory.getConnection();
		if(con==null){
			return null;
		}
	    PreparedStatement pStat = null;
	    List<University> us = null;
	    try {
			pStat = con.prepareStatement(sql);
		    pStat.setString(1, code);
		    ResultSet set = pStat.executeQuery();
		    us = this.createUniversity(set);
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
	    return us;
	}
	@Override
	public University addUniversity(University u) {

		String sql = "insert into university(full_name,ab_name,city_code,is_elite) " +
				" values(?,?,?,?)";
		Connection con = ConnectionFactory.getConnection();
		if(con==null){
			return null;
		}
	    PreparedStatement pStat = null;
	    int id = -1;
	    try {
			pStat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		    if(StringUtils.isBlank(u.getFullName())){
		    	pStat.setNull(1, Types.VARCHAR);
		    }else{
		    	pStat.setString(1, u.getFullName());
		    }
		    if(StringUtils.isBlank(u.getAbName())){
		    	pStat.setNull(2, Types.VARCHAR);
		    }else{
		    	pStat.setString(2, u.getAbName());
		    }
		    if(StringUtils.isBlank(u.getCityCode())){
		    	pStat.setNull(3, Types.VARCHAR);
		    }else{
		    	pStat.setString(3, u.getCityCode());
		    }
		    pStat.setInt(4, u.getIsElite());
		    pStat.executeUpdate();
		    ResultSet set = pStat.getGeneratedKeys();
		    if(set.next()){
		    	id=set.getInt(1);
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
		return this.getUniversityById(id);
	}

	@Override
	public University getUniversityById(int id) {
		String sql = "select * from university where id=?";
		Connection con = ConnectionFactory.getConnection();
		if(con==null){
			return null;
		}
	    PreparedStatement pStat = null;
	    List<University> us = null;
	    try {
			pStat = con.prepareStatement(sql);
		    pStat.setInt(1, id);
		    ResultSet set = pStat.executeQuery();
		    us = this.createUniversity(set);
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
	    if(us==null||us.size()<1){
	    	return null;
	    }
	    return us.get(0);
	}

	public List<University> createUniversity(ResultSet set) throws SQLException{
		if(set==null){
			return null;
		}
		List<University> us = new ArrayList<University>();
		while(set.next()){
			University u = new University();
			u.setAbName(set.getString("ab_name"));
			u.setFullName(set.getString("full_name"));
			u.setCityCode(set.getString("city_code"));
			u.setId(set.getInt("id"));
			u.setIsElite(set.getInt("is_elite"));
			us.add(u);
		}
		return us;
	}
	
	public static void main(String args[]){
		
		UniversityDaoImpl udl = new UniversityDaoImpl();
		for(int i=0;i<OfferConst.US.length;i++){
			University u = new University();
			u.setFullName(OfferConst.US[i][0]);
			u.setAbName(OfferConst.US[i][1]);
			u.setCityCode(OfferConst.US[i][2]);
			u.setIsElite(Integer.parseInt(OfferConst.US[i][3]));
			udl.addUniversity(u);
		}
		
	}
	
	
}
