package com.everhomes.news;

import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhNewsDao;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.server.schema.tables.records.EhNewsRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class NewsProviderImpl implements NewsProvider {

	@Autowired
	private DbProvider dbProvider;
	
	@Override
	public void createNews(News news) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNewsRecord record = ConvertHelper.convert(news, EhNewsRecord.class);
		InsertQuery<EhNewsRecord> insertQuery = context.insertQuery(Tables.EH_NEWS);
		insertQuery.setRecord(record);
		insertQuery.setReturning(Tables.EH_NEWS.ID);
		insertQuery.execute();
		news.setId(insertQuery.getReturnedRecord().getId());
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNews.class, null);
	}

	@Override
	public void updateNews(News news) {
		assert(news.getId()==null);
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNewsDao dao = new EhNewsDao(context.configuration());
		dao.update(news);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNews.class, news.getId());
	}
	
}
