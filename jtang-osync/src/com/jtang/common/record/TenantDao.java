package com.jtang.common.record;

import java.util.List;

import com.jtang.common.tenant.Tenant;

public interface TenantDao {

	public Tenant add(Tenant tenant);
	//传入的tenant没有密码
	public String getPwdByTenant(Tenant tenant);
	public List<Tenant> getAllTenants();
	public Tenant getTenantById(int id);
	
}
