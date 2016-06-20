package com.everhomes.news;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.tables.daos.EhNewsDao;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.util.ConvertHelper;

@Component
public class NewsProviderImpl implements NewsProvider {

	@Autowired
	private DbProvider dbProvider;
	
	@Override
	public void createNews(News news) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNewsDao dao = new EhNewsDao(context.configuration());
		EhNews ehNews = ConvertHelper.convert(news, EhNews.class);
		dao.insert(ehNews);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNews.class, null);
	}
	
}
