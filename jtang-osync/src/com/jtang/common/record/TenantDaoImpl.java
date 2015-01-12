package com.jtang.common.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jtang.common.tenant.Tenant;

public class TenantDaoImpl implements TenantDao {

	public Tenant add(Tenant tenant) {
		String sql = "insert into tenant(ip,port,DBName,userName,password) "
				+ "values(?,?,?,?,?)";
		Connection con = MysqlConnection.getConnection();
		if (con == null) {
			return null;
		}
		PreparedStatement pStat = null;
		int id = -1;
		try {
			pStat = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pStat.setString(1, tenant.getIp());
			pStat.setInt(2, tenant.getPort());
			pStat.setString(3, tenant.getDBName());
			pStat.setString(4, tenant.getUserName());
			pStat.setString(5, tenant.getPassword());
			pStat.executeUpdate();
			ResultSet set = pStat.getGeneratedKeys();
			if (set.next()) {
				id=set.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.getTenantById(id);
	}

	public String getPwdByTenant(Tenant tenant) {
		String sql = "select password from tenant where ip=? and port=? and "
				+ " DBName=? and userName=?";
		Connection con = MysqlConnection.getConnection();
		if (con == null) {
			return "";
		}
		PreparedStatement pStat = null;
		String pwd = "";
		try {
			pStat = con.prepareStatement(sql);
			pStat.setString(1, tenant.getIp());
			pStat.setInt(2, tenant.getPort());
			pStat.setString(3, tenant.getDBName());
			pStat.setString(4, tenant.getUserName());
			ResultSet set = pStat.executeQuery();
			if (set.next()) {
				set.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pwd;
	}

	public List<Tenant> getAllTenants() {
		String sql = "select * from tenant";
		Connection con = MysqlConnection.getConnection();
		if (con == null) {
			return null;
		}
		PreparedStatement pStat = null;
		List<Tenant> tenants = null;
		try {
			pStat = con.prepareStatement(sql);
			ResultSet set = pStat.executeQuery();
			tenants = this.createTenants(set);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tenants;
	}

	public Tenant getTenantById(int id) {
		String sql = "select * from tenant where id=?";
		Connection con = MysqlConnection.getConnection();
		if (con == null) {
			return null;
		}
		PreparedStatement pStat = null;
		List<Tenant> tenants = null;
		try {
			pStat = con.prepareStatement(sql);
			pStat.setInt(1, id);
			ResultSet set = pStat.executeQuery();
			tenants = this.createTenants(set);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pStat.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (tenants != null && tenants.size() > 0) {
			return tenants.get(0);
		}
		return null;
	}

	public List<Tenant> createTenants(ResultSet set) throws SQLException {
		List<Tenant> tenants = new ArrayList<Tenant>();
		if (set == null) {
			return tenants;
		}
		while (set.next()) {
			String dBName = set.getString("DBName");
			int port = set.getInt("port");
			String ip = set.getString("ip");
			String password = set.getString("password");
			String userName = set.getString("userName");
			Tenant tenant = new Tenant(ip, port, dBName, userName, password);
			tenants.add(tenant);
		}
		return tenants;
	}

}
