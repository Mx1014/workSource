package com.everhomes.news;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;

public class NewsTest extends CoreServerTestCase {
	@Autowired
	private NewsProvider newsProvider;
	
	@Test
	public void testListNews(){
		List<News> list = newsProvider.listNews(0L,0L, 0, 0L, 20);
		list.forEach(s->System.err.println(s));
	}
}
