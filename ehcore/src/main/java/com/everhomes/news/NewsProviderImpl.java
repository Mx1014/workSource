// @formatter:off
package com.everhomes.news;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhNewsDao;
import com.everhomes.server.schema.tables.pojos.EhNews;

@Component
public class NewsProviderImpl implements NewsProvider {

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired 
	private SequenceProvider sequenceProvider;
	
	@Override
	public void createNews(News news) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNews.class));
		news.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNewsDao dao = new EhNewsDao(context.configuration());
		dao.insert(news);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNews.class, null);
	}

	@Override
	public void updateNews(News news) {
		assert(news.getId()!=null);
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNewsDao dao = new EhNewsDao(context.configuration());
		dao.update(news);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNews.class, news.getId());
	}
	
}
