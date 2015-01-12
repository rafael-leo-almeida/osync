package com.jtang.common.record;

import java.util.List;

public interface MapJsonDao {

	public MapJson add(MapJson mapJson);
	public List<MapJson> getAllMap();
	//将所有的映射设置为停止状态
	public void setAllStop();
	//设置一个映射的的状态
	public MapJson setMapJson(int id,int status);
	
	public MapJson getMapJsonById(int id);
}
