package com.jtang.console.dwr.bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.jtang.common.table.TableInfo;
import com.jtang.common.tenant.Tenant;
import com.jtang.console.dwr.web.ClientEntity;
import com.jtang.console.dwr.web.TableMapWeb;
import com.jtang.syn.netty.ClientDescription;
import com.jtang.syn.netty.DBDescription;

@Component
public class TanentBean {

	public boolean testTanent(String ip, int port, String DBName,
			String tableName, String userName, String password) {

		Tenant tanent = new Tenant(ip, port, DBName, userName, password);
		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost(ip).setPath("/testConnect")
				.setParameter("tanent", tanent.toString())
				.setParameter("tableName", tableName).setPort(8889);

		URI uri;
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			String result = builder.toString();
			if (result.equalsIgnoreCase("true")) {
				return true;
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Map<String, Object> testBoth(String sIp, int sPort, String sDBName,
			String sTableName, String sUserName, String sPassword, String dIp,
			int dPort, String dDBName, String dTableName, String dUserName,
			String dPassword) {

		boolean sFlag = this.testTanent(sIp, sPort, sDBName, sTableName,
				sUserName, sPassword);
		boolean dFlag = this.testTanent(dIp, dPort, dDBName, dTableName,
				dUserName, dPassword);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("sFlag", sFlag);
		data.put("dFlag", dFlag);
		return data;

	}

	public String getTableSchema(String ip, int port, String DBName,
			String tableName, String userName, String password) {

		Tenant tanent = new Tenant(ip, port, DBName, userName, password);
		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost(ip).setPath("/getTableSchema")
				.setParameter("tanent", tanent.toString())
				.setParameter("tableName", tableName).setPort(8889);

		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<String, List<String>> getBothSchemas(String sIp, int sPort,
			String sDBName, String sTableName, String sUserName,
			String sPassword, String dIp, int dPort, String dDBName,
			String dTableName, String dUserName, String dPassword) {
		String sTableSchema = this.getTableSchema(sIp, sPort, sDBName,
				sTableName, sUserName, sPassword);
		String dTableSchema = this.getTableSchema(dIp, dPort, dDBName,
				dTableName, dUserName, dPassword);
		if (StringUtils.isBlank(sTableSchema)
				|| StringUtils.isBlank(dTableSchema)) {
			return null;
		}
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		try {
			TableInfo sTableInfo = new TableInfo(sTableSchema);
			TableInfo dTableInfo = new TableInfo(dTableSchema);
			List<String> sColumns = sTableInfo.getColumnName();
			List<String> dColumns = dTableInfo.getColumnName();
			for (int i = 0; i < sColumns.size(); i++) {
				String name = sColumns.get(i);
				String type = sTableInfo.getColumnType().get(name);
				List<String> s2d = new ArrayList<String>();
				for (int j = 0; j < dColumns.size(); j++) {
					String dType = dTableInfo.getColumnType().get(
							dColumns.get(j));
					if (dType.equalsIgnoreCase(type)) {
						s2d.add(dColumns.get(j));
					}
				}
				data.put(name, s2d);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;

	} 
	public boolean addLogmnrBothTableMap(String sIp, int sPort, String sDBName,String sTableSpace,
			String sTableName, String sUserName, String sPassword, String dIp,int dPort, 
			String dDBName, String dTableSpace,String dTableName, String dUserName,
			String dPassword, List<Map<String, String>> data,String logsPath,String dictPath) {

		Tenant sTanent = new Tenant(sIp, sPort, sDBName, sUserName, sPassword);
		Tenant dTanent = new Tenant(dIp, dPort, dDBName, dUserName, dPassword);
		String dTableInfoJson = this.getTableSchema(dIp, dPort, dDBName,
				dTableName, dUserName, dPassword);
		Map<String, String> fieldsMap = new HashMap<String, String>();
		List<String> valiFields = new ArrayList<String>();
		for (Map<String, String> map : data) {
			String sColumn = map.get("sColumn");
			String dColumn = map.get("dColumn");
			System.out.println(sColumn + ":" + dColumn);
			if (StringUtils.isBlank(sColumn) || StringUtils.isBlank(dColumn)) {
				continue;
			}
			valiFields.add(sColumn);
			fieldsMap.put(sColumn, dColumn);
		}
		String sResult = this.addLogmnrSourceTableMap(sTanent, dTanent, sTableSpace,sTableName,
				dTableSpace,dTableName, dTableInfoJson,
				new JSONArray(valiFields).toString(),
				new JSONObject(fieldsMap).toString(),logsPath,dictPath);
		if (sResult.equalsIgnoreCase("invalid_dTableInfoJson")
				|| sResult.equalsIgnoreCase("no_client")
				|| sResult.equalsIgnoreCase("config_failed")
				|| sResult.equalsIgnoreCase("monitor_failed")
				|| sResult.equalsIgnoreCase("invalid_source_table")) {
			return false;
		}
		System.out.println("sResult: " + sResult);
		String dResult = this.addDestinationTableMap(dIp, dPassword, sResult);
		if (dResult.equalsIgnoreCase("false")) {
			return false;
		}
		return true;
	}
	
	
	public boolean addBothTableMap(String sIp, int sPort, String sDBName,
			String sTableName, String sUserName, String sPassword, String dIp,
			int dPort, String dDBName, String dTableName, String dUserName,
			String dPassword, List<Map<String, String>> data) {

		Tenant sTanent = new Tenant(sIp, sPort, sDBName, sUserName, sPassword);
		Tenant dTanent = new Tenant(dIp, dPort, dDBName, dUserName, dPassword);
		String dTableInfoJson = this.getTableSchema(dIp, dPort, dDBName,
				dTableName, dUserName, dPassword);
		Map<String, String> fieldsMap = new HashMap<String, String>();
		List<String> valiFields = new ArrayList<String>();
		for (Map<String, String> map : data) {
			String sColumn = map.get("sColumn");
			String dColumn = map.get("dColumn");
			System.out.println(sColumn + ":" + dColumn);
			if (StringUtils.isBlank(sColumn) || StringUtils.isBlank(dColumn)) {
				continue;
			}
			valiFields.add(sColumn);
			fieldsMap.put(sColumn, dColumn);
		}
		String sResult = this.addSourceTableMap(sTanent, dTanent, sTableName,
				dTableName, dTableInfoJson,
				new JSONArray(valiFields).toString(),
				new JSONObject(fieldsMap).toString());
		if (sResult.equalsIgnoreCase("invalid_dTableInfoJson")
				|| sResult.equalsIgnoreCase("no_client")
				|| sResult.equalsIgnoreCase("config_failed")
				|| sResult.equalsIgnoreCase("monitor_failed")
				|| sResult.equalsIgnoreCase("invalid_source_table")) {
			return false;
		}
		System.out.println("sResult: " + sResult);
		String dResult = this.addDestinationTableMap(dIp, dPassword, sResult);
		if (dResult.equalsIgnoreCase("false")) {
			return false;
		}
		return true;
	}

	public String addLogmnrSourceTableMap(Tenant sTanent, Tenant dTanent,String sTableSpace,
			String sTableName, String dTableSpace,String dTableName, String dTableInfoJson,
			String valiFields, String fieldsMap,String logsPath,String dictPath) {

		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost(sTanent.getIp())
				.setPath("/tableMap/addLogmnrSourceTableMap")
				.setParameter("sTanent", sTanent.toString())
				.setParameter("dTanent", sTanent.toString())
				.setParameter("sTableSpace", sTableSpace)
				.setParameter("sTableName", sTableName)
				.setParameter("dTableSpace", dTableSpace)
				.setParameter("dTableName", dTableName)
				.setParameter("dTableInfoJson", dTableInfoJson)
				.setParameter("valiFields", valiFields)
				.setParameter("logsPath", logsPath)
				.setParameter("dictPath", dictPath)
				.setParameter("fieldsMap", fieldsMap).setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String addSourceTableMap(Tenant sTanent, Tenant dTanent,
			String sTableName, String dTableName, String dTableInfoJson,
			String valiFields, String fieldsMap) {

		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost(sTanent.getIp())
				.setPath("/tableMap/addTableMap")
				.setParameter("sTanent", sTanent.toString())
				.setParameter("dTanent", sTanent.toString())
				.setParameter("sTableName", sTableName)
				.setParameter("dTableName", dTableName)
				.setParameter("dTableInfoJson", dTableInfoJson)
				.setParameter("valiFields", valiFields)
				.setParameter("fieldsMap", fieldsMap).setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String addDestinationTableMap(String dIp, String dPassword,
			String tableMapJson) {

		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost(dIp).setPath("/addDestinationTableMap")
				.setParameter("tableMapJson", tableMapJson)
				.setParameter("password", dPassword).setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<ClientEntity> getClients() {

		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost("127.0.0.1").setPath("/getClients")
				.setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ClientDescription
		List<ClientDescription> clients = new ArrayList<ClientDescription>();
		if (StringUtils.isNotBlank(result)) {

			try {
				JSONArray arrays = new JSONArray(result);
				for (int i = 0; i < arrays.length(); i++) {
					String cDs = arrays.getString(i);
					ClientDescription cds = new ClientDescription(cDs);
					if (cds != null) {
						clients.add(cds);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		System.out.println(result);
		List<ClientEntity> entities = new ArrayList<ClientEntity>();
		for (int i = 0; i < clients.size(); i++) {
			ClientDescription cds = clients.get(i);
			ClientEntity ce = new ClientEntity();
			ce.setIp(cds.getWatchIp());
			Vector<DBDescription> dbs = cds.getDBs();
			for (int j = 0; j < dbs.size(); j++) {
				DBDescription db = dbs.get(j);
				ce.getDbs().add(db.getDBName());
				Vector<TableInfo> tables = db.getTables();
				for (int k = 0; k < tables.size(); k++) {
					System.out.println(tables.get(k).getTableName());
				}
			}
			entities.add(ce);
		}
		return entities;
	}

	public List<TableMapWeb> getAllTableMapWebs() {
		List<TableMapWeb> webs = new ArrayList<TableMapWeb>();
		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost("127.0.0.1").setPath("/getTableMapWebs")
				.setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();

			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ClientDescription
		if (StringUtils.isNotBlank(result)) {
			try {
				JSONArray arrays = new JSONArray(result);
				for (int i = 0; i < arrays.length(); i++) {
					String maps = arrays.getString(i);
					TableMapWeb web = new TableMapWeb(maps);
					if (web != null) {
						webs.add(web);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return webs;
	}

	public boolean startTableMapById(int mapId) {

		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost("127.0.0.1").setPath("/startTableMapById")
				.setParameter("id", new Integer(mapId).toString())
				.setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();
			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Boolean.parseBoolean(result);
	}

	public boolean stopTableMapById(int mapId) {

		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost("127.0.0.1").setPath("/stopTableMapById")
				.setParameter("id", new Integer(mapId).toString())
				.setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();
			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Boolean.parseBoolean(result);
	}

	public Map<String, List<Object>> initGetMapData() {
		
		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost("127.0.0.1").setPath("/initGetMapData")
				.setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();
			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("result"+result);
		List<Object> mapWebs = new ArrayList<Object>();
		List<Object> qushi = new ArrayList<Object>();
		List<Object> cuowu = new ArrayList<Object>();
		try {
			JSONObject obj = new JSONObject(result);
			String mapsArrayStr = obj.getString("maps");
			JSONArray mapsArray = new JSONArray(mapsArrayStr);
			if(mapsArray.length()==0){
				return null;
			}
			for(int i=0;i<mapsArray.length();i++){
				mapWebs.add(new TableMapWeb(mapsArray.getString(i)));
			}
			String dataObjStr = obj.getString("data");
			JSONObject dataObj = new JSONObject(dataObjStr);
			String cuowuArrayStr = dataObj.getString("cuowu");
			JSONArray cuowuArray = new JSONArray(cuowuArrayStr);
			for(int j=0;j<cuowuArray.length();j++){
				cuowu.add(cuowuArray.getLong(j));
			}
			String qushiArrayStr = dataObj.getString("qushi");
			JSONArray qushiArray = new JSONArray(qushiArrayStr);
			for(int k=0;k<qushiArray.length();k++){
				qushi.add(qushiArray.getLong(k));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String,List<Object>> data = new HashMap<String, List<Object>>();
		data.put("maps", mapWebs);
		data.put("qushi", qushi);
		data.put("cuowu", cuowu);
		return data;
	}
	
public Map<String, List<Long>> getMapData(int id,Long day,int dayOrWeek) {
		
		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder bd = new URIBuilder();
		bd.setScheme("http").setHost("127.0.0.1").setPath("/getMapData")
		        .setParameter("id", new Integer(id).toString())
		        .setParameter("day", day.toString())
		        .setParameter("dayOrWeek", new Integer(dayOrWeek).toString())
				.setPort(8889);
		URI uri;
		String result = "";
		try {
			uri = bd.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = client.execute(get);
			InputStream rd = response.getEntity().getContent();
			byte[] bytes = new byte[1024];
			int length = 0;
			StringBuilder builder = new StringBuilder();
			while ((length = rd.read(bytes)) > 0) {
				String str = new String(bytes, 0, length);
				builder.append(str);
			}
			result = builder.toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("result"+result);
		List<Long> qushi = new ArrayList<Long>();
		List<Long> cuowu = new ArrayList<Long>();
		try {
			JSONObject obj = new JSONObject(result);
			String cuowuArrayStr = obj.getString("cuowu");
			JSONArray cuowuArray = new JSONArray(cuowuArrayStr);
			for(int j=0;j<cuowuArray.length();j++){
				cuowu.add(cuowuArray.getLong(j));
			}
			String qushiArrayStr = obj.getString("qushi");
			JSONArray qushiArray = new JSONArray(qushiArrayStr);
			for(int k=0;k<qushiArray.length();k++){
				qushi.add(qushiArray.getLong(k));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(qushi==null||cuowu==null||qushi.size()<1||cuowu.size()<1){
			return null;
		}else{
			Map<String,List<Long>> data = new HashMap<String, List<Long>>();
			data.put("qushi", qushi);
			data.put("cuowu", cuowu);
			return data;
		}
	}
	
	
	public static void main(String args[]){
		
		new TanentBean().initGetMapData();
		
		
	}
	
}
