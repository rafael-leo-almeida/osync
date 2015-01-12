package com.jtang.common.record;

import java.util.List;

public interface DelayDao {
	
	public Delay add(Delay delay);
	//得到最近的几个delay
	public List<Delay> getLatestDelays(int n);
	public Delay getDelayById(long id);
}
