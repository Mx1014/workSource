package com.everhomes.techpark.punch;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;

public class PunchTest extends CoreServerTestCase {
	@Autowired
	private PunchService punchService;
	
	@Test
	public void testListNews(){
		List<News> list = newsProvider.listNews(0L,0L, 0, 0L, 20);
		list.forEach(s->System.err.println(s));
	}

}
