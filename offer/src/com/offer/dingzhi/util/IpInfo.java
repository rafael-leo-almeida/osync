package com.offer.dingzhi.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.offer.dingzhi.entity.Offer;


public class IpInfo {

	
	public static String getCityCode(String ip){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
		CloseableHttpResponse response1 = null;
		String result = null;
		String regionCode="";
		String cityCode="";
		try {
			response1 = httpclient.execute(httpGet);
		    HttpEntity entity1 = response1.getEntity();
		    byte[] bytes = new byte[1024];
            InputStream in = entity1.getContent();
            StringBuilder sb = new StringBuilder();
            while(in.read(bytes)>0){
            	sb.append(new String(bytes));
            }
            result = sb.toString();
			EntityUtils.consume(entity1);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		    try {
		    	if(response1!=null){
					response1.close();
		    	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isBlank(result)){
			return cityCode;
		}
		try {
			JSONObject obj = new JSONObject(result);
			int code = obj.getInt("code");
			if(code==0){
				JSONObject data = obj.getJSONObject("data");
				regionCode=data.getString("region_id");
				cityCode=data.getString("city_id");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(regionCode)||StringUtils.isBlank(cityCode)){
			return "";
		}
		//找到匹配的cityCode
		for(int i=0;i<OfferConst.CITYS.length;i++){
			if(cityCode.equalsIgnoreCase(OfferConst.CITYS[i][2])){
				return cityCode;
			}
		}
		//如果没有匹配的citycode，找到对应的省会
		for(int i=0;i<OfferConst.CITYS.length;i++){
			if(regionCode.equalsIgnoreCase(OfferConst.CITYS[i][1])){
				return OfferConst.CITYS[i][2];
			}
		}
		return "";
	}
	
	public static void main(String args[]){
		
		String res = IpInfo.getCityCode("222.179.218.111");
		for(int i=0;i<OfferConst.CITYS.length;i++){
			if(res.equalsIgnoreCase(OfferConst.CITYS[i][2])){
				System.out.println(OfferConst.CITYS[i][0]);
				return;
			}
		}
	}
	
	
}
