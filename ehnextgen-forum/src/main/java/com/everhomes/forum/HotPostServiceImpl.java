package com.everhomes.forum;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.stereotype.Component;

@Component
public class HotPostServiceImpl implements HotPostService {
	
	private ArrayBlockingQueue<HotPost> hotPostQueen = new ArrayBlockingQueue<HotPost>(100000);

	@Override
	public HotPost pull() {
		return hotPostQueen.poll();
	}

	@Override
	public void push(HotPost post) {
		hotPostQueen.offer(post);
		
	}

}
