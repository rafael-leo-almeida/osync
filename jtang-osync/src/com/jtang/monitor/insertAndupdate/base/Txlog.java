package com.jtang.monitor.insertAndupdate.base;

import java.math.BigInteger;

import java.sql.Timestamp;

import org.json.JSONObject;


public class Txlog {

	public static BigInteger infinity = new BigInteger(
			"9999999999999999999999999999");
	public long txn;
	public BigInteger scn;
	public int mask;
	public String maskName;
	public Timestamp ts;
	public int opt;
	public long getTxn() {
		return txn;
	}
	public void setTxn(long txn) {
		this.txn = txn;
	}

	public BigInteger getScn() {
		return scn;
	}

	public void setScn(BigInteger scn) {
		this.scn = scn;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public String getMaskName() {
		return maskName;
	}

	public void setMaskName(String maskName) {
		this.maskName = maskName;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public int getOpt() {
		return opt;
	}
	public void setOpt(int opt) {
		this.opt = opt;
	}
	public String toString(){
		
		return new JSONObject(this).toString();
	}
	
	
}
