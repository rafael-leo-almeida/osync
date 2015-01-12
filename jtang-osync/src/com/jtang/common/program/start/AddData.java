package com.jtang.common.program.start;

import java.util.Date;

import com.jtang.common.record.Record;
import com.jtang.common.record.RecordDaoImpl;

public class AddData {

	
	public static void main(String args[]){
		
		
		RecordDaoImpl recordDao = new RecordDaoImpl();
//		int[] nums = {12,36,78,24,30,25,36,90,17,68,13,46,
//		              20,21,39,87,60,0,10,13,26,38,21,34};
		int[] nums ={6,31,8,4,0,50,6,0,7,8,3,6,
	              20,19,9,8,6,0,10,1,6,8,1,34};
		
		Date now = new Date();
		long startTime = now.getTime()-(now.getHours()*60*60+now.getMinutes()*60+now.getSeconds())*1000;
		startTime = startTime - 1*24*60*60*1000;
		for(int i=0;i<24;i++){
			int num = nums[i];
			Date d = new Date();
			if(d.getHours()<i){
				num=0;
			}
			for(int j=0;j<num;j++){
				
				now=new Date(startTime+i*60*60*1000+j*2*60*1000);
				Record record = new Record();
				record.setGetTime(new Date(now.getTime()-20));
				record.setSendTime(new Date(now.getTime()+10));
				record.setKeyValue("ID");
				record.setMapId(1);
				record.setStatus(1);
				record.setType(1);
				record.setReturnTime(new Date(now.getTime()+40));
				recordDao.add(record);
				
			}
			
		}
		
		
		
		
		
	}
	
}
