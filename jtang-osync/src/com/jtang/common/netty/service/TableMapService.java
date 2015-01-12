package com.jtang.common.netty.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jtang.common.record.RecordDaoImpl;
import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableItem;
import com.jtang.common.table.TableMap;
import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.Tenant;
import com.jtang.common.tenant.TenantManager;
import com.jtang.console.dwr.web.TableMapWeb;
import com.jtang.logmnr.base.LogmnrService;
import com.jtang.monitor.config.base.AddSource;
import com.jtang.monitor.manager.MonitorService;
import com.jtang.monitor.manager.MonitorThreadManager;
import com.jtang.syn.comm.Message;
import com.jtang.syn.comm.OracleConnection;
import com.jtang.syn.netty.ClientDescription;
import com.jtang.syn.netty.ClientManager;
import com.jtang.syn.netty.DBDescription;

public class TableMapService {

	public static TenantManager tanentPool;
	public static ClientManager clientManager;
	public static TableMapManager tableMaps;
	public static MonitorThreadManager monitorThreadManager;
	public static ConcurrentLinkedQueue<Message> queue;

	/*
	 * 在监控端增加映射配置 int monitorType，0表示选择日志监控方法，1表示选择触发器监控方法
	 */
	public static String addLogmnrSourceTableMap(Tenant sTanent,
			Tenant dTanent, String sTableSpace,String sTableName,String dTableSpace,String dTableName,
			String dTableInfoJson, List<String> valiFields,
			Map<String, String> fieldsMap,
			List<String> logsPath, String dictPath) {
		
		SourceInfo dSourceInfo = new SourceInfo();
		dSourceInfo.setIp(dTanent.getIp());
		dSourceInfo.setPort(dTanent.getPort());
		dSourceInfo.setDBName(dTanent.getDBName());
		dSourceInfo.setTableName(dTableName);
		dSourceInfo.setUserName(dTanent.getUserName());
		TableInfo dTableInfo = null;
		try {
			dTableInfo = new TableInfo(dTableInfoJson);
		} catch (JSONException e) {
			e.printStackTrace();
			return "invalid_dTableInfoJson";
		}
		ClientDescription cd = clientManager.getClientByIp(dTanent.getIp());
		if (cd == null) {
			cd = new ClientDescription();
			cd.setWatchIp(dTanent.getIp());
			cd.setWatchPort(8880);
			clientManager.addClient(cd);
		}
		DBDescription db = cd.getDBByTanent(dTanent);
		if (db == null) {
			db = new DBDescription();
			db.setIp(dTanent.getIp());
			db.setPort(dTanent.getPort());
			db.setDBName(dTanent.getDBName());
			db.setUserName(dTanent.getUserName());
			cd.addDB(db);
		}
		db.addTable(dTableInfo);
		SourceInfo sSourceInfo = new SourceInfo();
		sSourceInfo.setIp(sTanent.getIp());
		sSourceInfo.setPort(sTanent.getPort());
		sSourceInfo.setTableName(sTableName);
		sSourceInfo.setDBName(sTanent.getDBName());
		sSourceInfo.setUserName(sTanent.getUserName());
		Connection con = OracleConnection.getConnection(sTanent.getUrl(),
				sTanent.getUserName(), sTanent.getPassword());
		TableInfo sTableInfo = null;
		try {
			if (con != null) {
				sTableInfo = CreateSchema.getSchemaWithDB(sTableName, con);
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "invalid_source_table";
		}
		if (sTableInfo == null) {
			return "invalid_source_table";
		}
		// 配置source表,其实就是加入监控
		LogmnrService.addSource(sTanent.getIp(), sTanent.getPort(),
				sTanent.getDBName(), sTableSpace, sTableName, sTableInfo,
				logsPath, dictPath, sTanent.getUserName(),
				sTanent.getPassword());
		// 加入tanentpool 和 tableMaps
		tanentPool.addTanent(sTanent);
		TableMap tableMap = new TableMap();
		TableItem sTableItem = new TableItem();
		sTableItem.setSourceInfo(sSourceInfo);
		sTableItem.setTableInfo(sTableInfo);
		tableMap.setFromTable(sTableItem);
		TableItem dTableItem = new TableItem(dSourceInfo, dTableInfo);
		tableMap.setToTable(dTableItem);
		tableMap.setValiFields(valiFields);
		tableMap.setFieldsMap(fieldsMap);
		tableMaps.add(tableMap);
		tableMaps.startTableMap(tableMap);
		return tableMap.toString();
	}

	public static String addSourceTableMap(Tenant sTanent, Tenant dTanent,
			String sTableName, String dTableName, String dTableInfoJson,
			List<String> valiFields, Map<String, String> fieldsMap) {
		// 找到目的数据库相关client
		SourceInfo dSourceInfo = new SourceInfo();
		dSourceInfo.setIp(dTanent.getIp());
		dSourceInfo.setPort(dTanent.getPort());
		dSourceInfo.setDBName(dTanent.getDBName());
		dSourceInfo.setTableName(dTableName);
		dSourceInfo.setUserName(dTanent.getUserName());
		TableInfo dTableInfo = null;
		try {
			dTableInfo = new TableInfo(dTableInfoJson);
		} catch (JSONException e) {
			e.printStackTrace();
			return "invalid_dTableInfoJson";
		}
		ClientDescription cd = clientManager.getClientByIp(dTanent.getIp());
		if (cd == null) {
			cd = new ClientDescription();
			cd.setWatchIp(dTanent.getIp());
			cd.setWatchPort(8880);
			clientManager.addClient(cd);
		}
		DBDescription db = cd.getDBByTanent(dTanent);
		if (db == null) {
			db = new DBDescription();
			db.setIp(dTanent.getIp());
			db.setPort(dTanent.getPort());
			db.setDBName(dTanent.getDBName());
			db.setUserName(dTanent.getUserName());
			cd.addDB(db);
		}
		db.addTable(dTableInfo);

		SourceInfo sSourceInfo = new SourceInfo();
		sSourceInfo.setIp(sTanent.getIp());
		sSourceInfo.setPort(sTanent.getPort());
		sSourceInfo.setTableName(sTableName);
		sSourceInfo.setDBName(sTanent.getDBName());
		sSourceInfo.setUserName(sTanent.getUserName());
		Connection con = OracleConnection.getConnection(sTanent.getUrl(),
				sTanent.getUserName(), sTanent.getPassword());
		TableInfo sTableInfo = null;
		try {
			if (con != null) {
				sTableInfo = CreateSchema.getSchemaWithDB(sTableName, con);
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "invalid_source_table";
		}
		if (sTableInfo == null) {
			return "invalid_source_table";
		}
		// 配置source表,其实就是加入监控
		try {
			AddSource addSource = new AddSource(sTableName, sTanent.getUrl(),
					sTanent.getUserName(), sTanent.getPassword());
			if (!addSource.addNewSource()) {
				return "config_failed!";
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return "config_failed!";
		}

		try {
			String mr = MonitorService.addNewMonitorSource(
					monitorThreadManager, queue, tableMaps, clientManager,
					sTanent, sTableName);
			if (mr.equalsIgnoreCase("false")) {
				return "monitor_failed";
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return "monitor_failed";
		}
		// 加入tanentpool 和 tableMaps
		tanentPool.addTanent(sTanent);
		TableMap tableMap = new TableMap();
		TableItem sTableItem = new TableItem();
		sTableItem.setSourceInfo(sSourceInfo);
		sTableItem.setTableInfo(sTableInfo);
		tableMap.setFromTable(sTableItem);
		TableItem dTableItem = new TableItem(dSourceInfo, dTableInfo);
		tableMap.setToTable(dTableItem);
		tableMap.setValiFields(valiFields);
		tableMap.setFieldsMap(fieldsMap);
		tableMaps.add(tableMap);
		tableMaps.startTableMap(tableMap);
		return tableMap.toString();
	}

	public static boolean addDestinationTableMap(String password,
			String tableMapJson) {

		TableMap tableMap = null;
		System.out.println("first: " + tableMapJson);
		try {
			tableMap = new TableMap(tableMapJson);
			System.out.println("second: " + tableMap.toString());
			// 加入并启动一个tableMap
			tableMaps.add(tableMap);
			tableMaps.startTableMap(tableMap);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		SourceInfo sInfo = tableMap.getFromTable().getSourceInfo();
		SourceInfo dInfo = tableMap.getToTable().getSourceInfo();
		ClientDescription scd = clientManager.getClientByIp(sInfo.getIp());
		if (scd == null) {
			scd = new ClientDescription();
			scd.setWatchIp(sInfo.getIp());
			scd.setWatchPort(8880);
			clientManager.addClient(scd);
		}
		ClientDescription dcd = clientManager.getClientByIp(dInfo.getIp());
		if (dcd == null) {
			dcd = new ClientDescription();
			dcd.setWatchIp(dInfo.getIp());
			dcd.setWatchPort(8880);
			clientManager.addClient(dcd);
		}

		Tenant sTanent = new Tenant(sInfo.getIp(), sInfo.getPort(),
				sInfo.getDBName(), sInfo.getUserName(), "");
		Tenant dTanent = new Tenant(dInfo.getIp(), dInfo.getPort(),
				dInfo.getDBName(), dInfo.getUserName(), password);
		// 将目的tenant 加入到tenantmanager中
		tanentPool.addTanent(dTanent);

		DBDescription sdb = scd.getDBByTanent(sTanent);
		if (sdb == null) {
			sdb = new DBDescription();
			sdb.setIp(sTanent.getIp());
			sdb.setPort(sTanent.getPort());
			sdb.setDBName(sTanent.getDBName());
			sdb.setUserName(sTanent.getUserName());
			scd.addDB(sdb);
		}
		DBDescription ddb = dcd.getDBByTanent(dTanent);
		if (ddb == null) {
			ddb = new DBDescription();
			ddb.setIp(dTanent.getIp());
			ddb.setPort(dTanent.getPort());
			ddb.setDBName(dTanent.getDBName());
			ddb.setUserName(dTanent.getUserName());
			dcd.addDB(ddb);
		}
		sdb.addTable(tableMap.getFromTable().getTableInfo());
		ddb.addTable(tableMap.getToTable().getTableInfo());
		return true;

	}

	// 从映射管理器中获得已经添加的映射列表
	// 返回映射web显示的东西
	public static String getTableMapWebs() {
		List<TableMap> maps = tableMaps.getTableMaps();
		List<TableMapWeb> mapWebs = new ArrayList<TableMapWeb>();
		RecordDaoImpl rDao = new RecordDaoImpl();
		if (maps != null) {
			for (TableMap map : maps) {
				String sUrl = "";
				String dUrl = "";
				SourceInfo sInfo = map.getFromTable().getSourceInfo();
				sUrl = sInfo.getIp() + ":" + sInfo.getPort() + "<br/>:"
						+ sInfo.getDBName() + ":" + sInfo.getTableName();
				SourceInfo dInfo = map.getToTable().getSourceInfo();
				dUrl = dInfo.getIp() + ":" + dInfo.getPort() + "<br/>:"
						+ dInfo.getDBName() + ":" + dInfo.getTableName();
				long wrong = rDao.getWrongCountByMap(map.getId());
				long right = rDao.getRightCountByMap(map.getId());
				long total = wrong + right;
				int status = map.getStatus();
				TableMapWeb web = new TableMapWeb();
				web.setDiZhid(dUrl);
				web.setStatus(status);
				web.setDiZhis(sUrl);
				web.setTotal(total);
				web.setWrong(wrong);
				web.setMapId(map.getId());
				mapWebs.add(web);
			}
		}
		System.out.println(new JSONArray(mapWebs).toString());
		return new JSONArray(mapWebs).toString();
	}

	// 启动一个已经存在的映射
	public static boolean startMap(int mapId) {
		return tableMaps.startTableMapById(mapId);
	}

	public static boolean stopMap(int mapId) {
		return tableMaps.stopTableMapById(mapId);
	}

	public static String getMapData(int mapId, Date date, int dayOrWeek) {

		RecordDaoImpl recordDao = new RecordDaoImpl();
		long time = date.getTime()
				- (date.getHours() * 60 * 60 + date.getMinutes() * 60 + date
						.getSeconds()) * 1000;
		List<Long> qushi = new ArrayList<Long>();
		List<Long> cuowu = new ArrayList<Long>();

		// dayOrWeek为0表示获取一天的数据
		if (dayOrWeek == 0) {
			Date hour = new Date(time);
			for (int i = 0; i < 24; i++) {
				hour = new Date(time + i * 60 * 60 * 1000);
				qushi.add(recordDao.getCountByHourMap(hour, mapId));
			}
			long wrongCount = recordDao.getWrongCountByMap(mapId);
			long rightCount = recordDao.getRightCountByMap(mapId);
			cuowu.add(wrongCount + rightCount);
			cuowu.add(wrongCount);
			cuowu.add(rightCount);
			long dayWrongCount = recordDao.getWrongCountByMapDay(mapId, date);
			long dayRightCount = recordDao.getRightCountByMapDay(mapId, date);
			cuowu.add(dayWrongCount + dayRightCount);
			cuowu.add(dayWrongCount);
			cuowu.add(dayRightCount);
		} else {
			// 得到一周的数据
			Date day = new Date(time);
			for (int i = 6; i >= 0; i--) {
				day = new Date(time - i * 24 * 60 * 60 * 1000);
				qushi.add(recordDao.getCountByDayMap(day, mapId));
			}
			long wrongCount = recordDao.getWrongCountByMap(mapId);
			long rightCount = recordDao.getRightCountByMap(mapId);
			cuowu.add(wrongCount + rightCount);
			cuowu.add(wrongCount);
			cuowu.add(rightCount);
			long weekWrongCount = recordDao.getWrongCountByMapWeek(mapId, date);
			long weekRightCount = recordDao.getRightCountByMapWeek(mapId, date);
			cuowu.add(weekRightCount + weekWrongCount);
			cuowu.add(weekWrongCount);
			cuowu.add(weekRightCount);
		}
		String qushiStr = new JSONArray(qushi).toString();
		String cuowuStr = new JSONArray(cuowu).toString();
		Map<String, String> data = new HashMap<String, String>();
		data.put("qushi", qushiStr);
		data.put("cuowu", cuowuStr);
		return new JSONObject(data).toString();
	}

	public static String initGetMapData() {

		List<TableMap> maps = tableMaps.getTableMaps();
		List<TableMapWeb> mapWebs = new ArrayList<TableMapWeb>();
		RecordDaoImpl rDao = new RecordDaoImpl();
		if (maps != null) {
			for (TableMap map : maps) {
				String sUrl = "";
				String dUrl = "";
				SourceInfo sInfo = map.getFromTable().getSourceInfo();
				sUrl = sInfo.getIp() + ":" + sInfo.getPort() + "<br/>:"
						+ sInfo.getDBName() + ":" + sInfo.getTableName();
				SourceInfo dInfo = map.getToTable().getSourceInfo();
				dUrl = dInfo.getIp() + ":" + dInfo.getPort() + "<br/>:"
						+ dInfo.getDBName() + ":" + dInfo.getTableName();
				long wrong = rDao.getWrongCountByMap(map.getId());
				long right = rDao.getRightCountByMap(map.getId());
				long total = wrong + right;
				int status = map.getStatus();
				TableMapWeb web = new TableMapWeb();
				web.setDiZhid(dUrl);
				web.setStatus(status);
				web.setDiZhis(sUrl);
				web.setTotal(total);
				web.setWrong(wrong);
				web.setMapId(map.getId());
				mapWebs.add(web);
			}
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put("maps", new JSONArray(mapWebs).toString());
		if (maps != null && maps.size() > 0) {
			int mapId = maps.get(0).getId();
			Date date = new Date();
			int dayOrWeek = 0;
			String initDataStr = TableMapService.getMapData(mapId, date,
					dayOrWeek);
			data.put("data", initDataStr);
		}
		return new JSONObject(data).toString();

	}

}
