package com.jtang.syn.comm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableMap;

public class OperateDB {

	Message info;
	TableInfo tableInfo;
	TableInfo fromTable;
	Connection con;
	TableMap tableMap;
	public OperateDB(Message info,TableMap tableMap,Connection con){
		this.info = info;
		this.tableMap=tableMap;
		this.con=con;
	}
	
	//0表示失败，1表示删除成功，2表示更新成功，3表示插入成功
	public int synData() throws SQLException{
		int status = 0;
		if(this.info==null)
			return status;
		if(con==null)
			return status;
		Statement stmt = con.createStatement();
		if(stmt==null)
			return status;
		
		if(this.info.getOperation().equals("delete")){
			//TODO: delete operation
			String deleteSQL = GenerateSQL.getDeleteSQL(this.tableMap, info);
			stmt.execute(deleteSQL);
			status = 1;
		}else{
			String insertSQL = GenerateSQL.getInsertSQL(this.tableMap, info);
			String updateSQL = GenerateSQL.getUpdateSQL(this.tableMap, info);
			if(GenerateSQL.isExist(this.tableMap, info, stmt)){
				stmt.executeUpdate(updateSQL);
				status = 2;
			}else{
				stmt.execute(insertSQL);
				status = 3;
			}
		}
		stmt.close();
		return status;
	}
	
	
	
}
