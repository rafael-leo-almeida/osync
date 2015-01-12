package com.jtang.common.record;

import java.util.Date;

public interface RecordDao {

	//加入一个记录
	public Record add(Record record);
	
	//得到一个小时之内的数据比如3：00－4：00
	public long getCountByHour(Date day);
	//得到一天之内同步的数据
	public long getCountByDay(Date day);
	//按小时得到每一个同步映射的数据
	public long getCountByHourMap(Date day,int mapId);
	//按天得到每一个同步映射的数据
	public long getCountByDayMap(Date day,int mapId);
			
	//得到一个映射的错误总数
	public long getWrongCountByMap(int mapId);
	public long getWrongCountByMapDay(int mapId,Date day);
	public long getWrongCountByMapWeek(int mapId,Date day);
	//得到一个映射的正确总数
	public long getRightCountByMap(int mapId);
	public long getRightCountByMapDay(int mapId,Date day);
	public long getRightCountByMapWeek(int mapId,Date day);
	//得到所有映射的错误数量
	public long getWrongCount();
	//得到所有映射的正确数量
	public long getRightCount();
	
	
	//分别得到insert/update/delete操作数量
	//插入采用2，更新采用1，删除采用0
	public long getCountByType(int type); 
	public long getCountByTypeMap(int type,int mapId);
	
	//更新record
	public void updateRTStatusTypeById(long id,Date receiveTime,int status,int type);
	public void updateRTStatusById(long id,Date receiveTime,int status);
	public Record getRecordById(long id);
	
	
	
	
}
