package com.jtang.syn.comm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.avro.Schema;

public class SynDataThread extends Thread {

	ConcurrentLinkedQueue<Message> queue;

	public SynDataThread(ConcurrentLinkedQueue<Message> queue) {

		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		if (queue == null)
			return;
		Connection con = OracleConnection.getConnection();
		if (con == null)
			return;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (stmt != null) {
			if (queue.peek() != null) {
				Message info = queue.remove();
				String operation = info.getOperation();
				if (operation.equals("delete")) {
					int id = Integer.parseInt((String) info.getDatas().get("id"));
					String querySql = "select * from person where id="+id;
					try {
						ResultSet querySet = stmt.executeQuery(querySql);
						if(querySet.next())
							continue;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String sql = "delete from students where ID =" + id;
					try {
						System.out.println(sql);
						stmt.execute(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					int id = Integer.parseInt((String) info.getDatas().get("id"));
					String first_name = (String) info.getDatas().get("first_name");
					String last_name = (String) info.getDatas().get("last_name");
					String deleted = (String) info.getDatas().get("deleted");
					String birth_date = (String) info.getDatas().get("birth_date");
					if(birth_date!=null)
						birth_date="to_date('"+ birth_date + "','YYYYMMDD HH24:MI:SS')";
					String sql1 = "insert into students(id,first_name,last_name,deleted,BIRTH_DATE) "
							+ "VALUES("
							+ id
							+ ",'"
							+ first_name
							+ "','"
							+ last_name
							+ "','"
							+ deleted
							+ "',"
							+birth_date+ ")";
					
					String sql2 = "update students set first_name='"
							+ first_name + "'," + "last_name='" + last_name
							+ "',deleted='" + deleted
							+ "',birth_date=" + birth_date
							+ " where id=" + id;

					String sql3 = "select * from students where id=" + id;
					try {
						// stmt.execute(sql4);
						ResultSet rs = stmt.executeQuery(sql3);
						if (rs.next() == false) {
							System.out.println(sql1);
							stmt.execute(sql1);
						} else {
							System.out.println(sql2);
							stmt.execute(sql2);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

	}

}
