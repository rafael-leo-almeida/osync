package com.offer.dingzhi.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.offer.dingzhi.dao.UniversityDao;
import com.offer.dingzhi.dao.impl.UniversityDaoImpl;
import com.offer.dingzhi.entity.University;
import com.offer.dingzhi.service.UniversityService;
import com.offer.dingzhi.util.IpInfo;
import com.offer.dingzhi.util.OfferConst;

public class UniversityServiceImpl implements UniversityService {

	UniversityDao uDao = new UniversityDaoImpl();
	@Override
	public String buildNavHtml(String ip) { 
		
		if(StringUtils.isBlank(ip)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		//首先产生所在城市的大学列表
		String cityCode = IpInfo.getCityCode(ip);
        sb.append(this.createHtmlByCityCode(cityCode));
        for(int i=0;i<OfferConst.CITYS.length;i++){
        	if(OfferConst.CITYS[i][2].equalsIgnoreCase(cityCode)){
        		continue;
        	}
        	sb.append(this.createHtmlByCityCode(OfferConst.CITYS[i][2]));
        }
		return sb.toString();
	}
	
	public  String createHtmlByCityCode(String cityCode){
		/*
		  <div class="menu_box">
						<div class="menu_main">
							<h2>
								杭州 <span></span>
							</h2>
							<a href="javascript:void(0)">浙江大学</a> 
							<a href="javascript:void(0)">杭州电子科技大学</a> 
							<a href="javascript:void(0)">更多>></a>
						</div>
						<div class="menu_sub dn" style="top: 10px;">
							<ul class="reset">
								<li>
								<a href="javascript:void(0)" class="curr">浙江工业大学</a><br>
									<a href="javascript:void(0)">浙江理工大学</a><br> <a
									href="javascript:void(0)" class="curr">浙江工商大学</a><br> <a
									href="javascript:void(0)">中国计量学院</a><br>
									</li>
							</ul>
						</div>
					</div>
		 
		 */
		if(StringUtils.isBlank(cityCode)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(cityCode)){
			List<University> us = uDao.getUniversityByCityCode(cityCode);
			if(us!=null&&us.size()>0){
				String cityName = "";
				for(int i=0;i<OfferConst.CITYS.length;i++){
					if(cityCode.equalsIgnoreCase(OfferConst.CITYS[i][2])){
						cityName = OfferConst.CITYS[i][0];
					}
				}
				sb.append("<div class=\"menu_box\"><div class=\"menu_main\"><h2>");
				sb.append(cityName);
				sb.append("<span></span>");
				sb.append("</h2>");
				for (University university : us) {
					if(university.getIsElite()==1){
						sb.append("<a href=\"javascript:void(0)\">");
						sb.append(university.getFullName());
						sb.append("</a>");
					}
				}
				sb.append("<a href=\"javascript:void(0)\">更多>></a></div>");
				sb.append("<div class=\"menu_sub dn\" style=\"top: 10px;\">");
				sb.append("<ul class=\"reset\"><li>");
				for (University university : us) {
					if(university.getIsElite()==0){
						sb.append("<a href=\"javascript:void(0)\" class=\"curr\">");
						sb.append(university.getFullName());
						sb.append("</a></br>");
					}
				}
				sb.append("</li></ul></div>");
				sb.append("</div>");
			}
		}
		return sb.toString();
	} 
	
}
