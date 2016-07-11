package com.everhomes.forum;

public interface HotPostService {
	
	public HotPost pull();
	public void push(HotPost post);

}
