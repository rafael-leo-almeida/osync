package com.jtang.monitor.config.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.jtang.common.table.CreateSchema;
import com.jtang.common.table.TableInfo;
import com.jtang.monitor.config.base.DeTxlogTableAndTrg;
import com.jtang.monitor.config.base.OracleConnection;

public class DeTxlogTableAndTrgTest {


	
	@Test
	public void test() throws SQLException{
		
		Connection con = OracleConnection.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "chen", "chen");
		TableInfo table = new CreateSchema(null).getSchemaWithDB("students", con);
		DeTxlogTableAndTrg deTxlog = new DeTxlogTableAndTrg(table, "jdbc:oracle:thin:@localhost:1521:orcl", "chen", "chen");
		System.out.println(table);
		deTxlog.createDeTable();
		deTxlog.createDeTrigger();
		
	}
	
}
