package com.jtang.console.action;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.jtang.common.tenant.AllTanentInfos;
import com.jtang.common.tenant.TenantInfos;
import com.opensymphony.xwork2.ActionSupport;

public class TanentAction extends ActionSupport {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<TenantInfos> tanents;

	String message;
	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public List<TenantInfos> getTanents() {
		return tanents;
	}

	public void setTanents(List<TenantInfos> tanents) {
		this.tanents = tanents;
	}
	
	


	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
        
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://127.0.0.1:8889/tanent/getalltanents");
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
        this.tanents =  new AllTanentInfos(result).getTanentInfos();
        System.out.println(result);        
		return SUCCESS;
		
		
	}
	
	
	
	
	
}
