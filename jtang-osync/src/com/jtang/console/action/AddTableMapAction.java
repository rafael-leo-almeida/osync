package com.jtang.console.action;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.opensymphony.xwork2.ActionSupport;

public class AddTableMapAction extends ActionSupport {

	String sip;
	int sport;
	String sDBName;
	String stableName;
	String suserName;
	String spassword;

	String dip;
	int dport;
	String dDBName;
	String dtableName;
	String duserName;
	String dpassword;

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		DefaultHttpClient client = new DefaultHttpClient();
	    URIBuilder bd = new URIBuilder();
	    bd.setScheme("http").setHost("127.0.0.1").setPath("/tableMap/AddTableMap")
	    .setParameter("sip", this.sip)
	    .setParameter("sport", new Integer(this.sport).toString())
	    .setParameter("sDBName", this.sDBName)
	    .setParameter("stableName", this.stableName)
	    .setParameter("suserName",this.suserName)
	    .setParameter("spassword", this.spassword)
	    .setParameter("dip", this.dip)
	    .setParameter("dport", new Integer(this.dport).toString())
	    .setParameter("dDBName", this.dDBName)
	    .setParameter("dtableName", this.dtableName)
	    .setParameter("duserName", this.duserName)
	    .setParameter("dpassword", this.dpassword)
	    .setPort(8889);
	    URI uri = bd.build();
	    HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
        InputStream rd = response.getEntity().getContent();
        byte[] bytes = new byte[1024];
        int length=0;
        StringBuilder builder = new StringBuilder();
        
        while((length=rd.read(bytes))>0){
        	String str = new String(bytes, 0, length);
            builder.append(str);    
        }
        String result = builder.toString();
		if(result!=null&&result.equalsIgnoreCase("true")){
			return SUCCESS;
		}else{
			return "error";
		}
	}
	
	public String getSip() {
		return sip;
	}

	public void setSip(String sip) {
		this.sip = sip;
	}

	public int getSport() {
		return sport;
	}

	public void setSport(int sport) {
		this.sport = sport;
	}

	public String getsDBName() {
		return sDBName;
	}

	public void setsDBName(String sDBName) {
		this.sDBName = sDBName;
	}

	public String getStableName() {
		return stableName;
	}

	public void setStableName(String stableName) {
		this.stableName = stableName;
	}

	public String getSuserName() {
		return suserName;
	}

	public void setSuserName(String suserName) {
		this.suserName = suserName;
	}

	public String getSpassword() {
		return spassword;
	}

	public void setSpassword(String spassword) {
		this.spassword = spassword;
	}

	public String getDip() {
		return dip;
	}

	public void setDip(String dip) {
		this.dip = dip;
	}

	public int getDport() {
		return dport;
	}

	public void setDport(int dport) {
		this.dport = dport;
	}

	public String getdDBName() {
		return dDBName;
	}

	public void setdDBName(String dDBName) {
		this.dDBName = dDBName;
	}

	public String getDtableName() {
		return dtableName;
	}

	public void setDtableName(String dtableName) {
		this.dtableName = dtableName;
	}

	public String getDuserName() {
		return duserName;
	}

	public void setDuserName(String duserName) {
		this.duserName = duserName;
	}

	public String getDpassword() {
		return dpassword;
	}

	public void setDpassword(String dpassword) {
		this.dpassword = dpassword;
	}

	

}
