package com.offer.dingzhi.dao;

import java.util.List;

import com.offer.dingzhi.entity.University;

public interface UniversityDao {
	
	public List<University> getUniversityByCityCode(String code);
	public University addUniversity(University u);
	public University getUniversityById(int id);
	
}
