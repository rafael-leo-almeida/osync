package com.jtang.monitor.config.test;

import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.jtang.monitor.config.base.AddSource;
import com.jtang.monitor.config.base.CompileObjects;
import com.jtang.monitor.config.base.InitConfig;
import com.jtang.monitor.config.base.InitExample;
import com.jtang.monitor.config.base.InsertSource;
import com.jtang.monitor.config.base.OracleConnection;
import com.jtang.monitor.config.base.ProcConfig;
import com.jtang.monitor.config.base.ProcOneConfig;
import com.jtang.monitor.config.base.SequenceConfig;
import com.jtang.monitor.config.base.SettingConfig;
import com.jtang.monitor.config.base.TableConfig;
import com.jtang.monitor.config.base.TriggerConfig;
import com.jtang.monitor.config.base.UserConfig;
import com.jtang.monitor.config.base.ViewConfig;

public class BaseConfigTest {

	UserConfig userConfiger = new UserConfig();
	SequenceConfig seConfiger = new SequenceConfig();
	
	@Test
	public void testAddUser(){
		try {
			boolean f = userConfiger.addUser(null, null, null, null, "chenyang", "chenyang");
			if(f){
				System.out.println("add successful");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGrant() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		try {
			Connection con = OracleConnection.getConnectionAsSysdba();
			Statement stat = con.createStatement();
			userConfiger.grantToUser("yaoxianglong", "orcl", stat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGet() throws SQLException{
		
		UserConfig configer = new UserConfig(null,"chenyang","chenyang");
		List<String> list =configer.getUserSysPrivs();
		System.out.println("������������������������������");
		for (String string : list) {
			System.out.println(string);
		}
		list=configer.getUserTabPrivs();
		System.out.println("���������������������������:");
		for (String string : list) {
			System.out.println(string);
		}
		
		Map<String,String> map=configer.getUserTables();
		System.out.println("������������������������:");
		
		Set<String> keys =  map.keySet();
		for (String string : keys) {
			System.out.println(string+" : "+map.get(string));
		}
		
		String tableSpace = userConfiger.getDefaultTableSpace();
		System.out.println("���������������������:");
		System.out.println(tableSpace);
		
	}
	
	@Test
	public void testAddSequence() throws SQLException{
		
		if(SequenceConfig.createSequences(null, "yaoxianglong", "yao")){
			System.out.println("create sequence sucessfully");
			List<String>  list = SequenceConfig.getSequences(null, "yaoxianglong", "yao");
			for (String string : list) {
				System.out.println(string);
			}
		}
		
		
	}
	
	@Test
	public void testTableConfig() throws SQLException{
		
		TableConfig.createTables(null, "zhang", "zhang");
		
	}
	
	@Test
	public void testAddExampleTable() throws SQLException{
		
		TableConfig.createExampleTable(null, "yaoxianglong", "yao");
		
	}
	@Test 
	public void testProc() throws SQLException{
		
		ProcConfig procConfig = new ProcConfig("students", null, "zhang", "zhang");
		procConfig.createProc();
		
	}
	
	
	@Test
	public void testView() throws SQLException{
		
		ViewConfig.createView(null, "yaoxianglong", "yao");
		ViewConfig.createExampleTableView(null, "yaoxianglong", "yao");
		
	}
	
	@Test
	public void testProc1() throws SQLException{
		
		ProcOneConfig config = new ProcOneConfig(null, "yaoxianglong", "yao", "person");
		config.createProc1();
	}
	@Test
	public void testTrigger() throws SQLException{
		
		TriggerConfig config = new TriggerConfig(null, "zhang", "zhang", "students");
		config.createTrigger();
	
	}
	
	@Test
	public void testComplile() throws SQLException{
		
		CompileObjects.compile(null, "zhang", "zhang");
		
	}
	
	@Test
	public void testSetting() throws SQLException{
		
		SettingConfig.set(null, "yaoxianglong", "yao");
		
	}
	
	@Test
	public void testInsertSource() throws SQLException{
		
		new InsertSource(null, "yaoxianglong", "yao", "person").insert();
		
	}
	
	@Test
	public void testInit() throws SQLException{
		
		InitConfig config = new InitConfig(null, "chenyang", "chenyang");
		config.init();	
		new InitExample(null, "chenyang", "chenyang").init();
		
	}
	
	@Test
	public void testAddSource() throws SQLException{
		
		AddSource source = new AddSource("teachers", null, "chen", "chen");
		source.addNewSource();
		
	}
	
	
}
