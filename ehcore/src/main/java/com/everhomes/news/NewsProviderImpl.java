// @formatter:off
package com.everhomes.news;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhNewsDao;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.util.ConvertHelper;

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
		getReadWriteDao().insert(news);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNews.class, null);
	}

	@Override
	public void updateNews(News news) {
		assert (news.getId() != null);
		getReadWriteDao().update(news);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNews.class, news.getId());
	}

	@Override
	public News findNewsById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), News.class);
	}

	@Override
	public List<News> listNews(Integer namespaceId, Long from, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_NEWS).where(Tables.EH_NEWS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_NEWS.STATUS.eq(NewsStatus.ACTIVE.getCode()))
				.orderBy(Tables.EH_NEWS.TOP_INDEX.desc(), Tables.EH_NEWS.CREATE_TIME.desc())
				.limit(from.intValue(), pageSize.intValue()).fetch().map(r -> ConvertHelper.convert(r, News.class));
	}

	@Override
	public Long getMaxTopIndex(Integer namespaceId){
		return getReadOnlyContext().select(Tables.EH_NEWS.TOP_INDEX.max()).from(Tables.EH_NEWS).where(Tables.EH_NEWS.NAMESPACE_ID.eq(namespaceId))
		.fetchOne().value1();
	}
	
	private EhNewsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhNewsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhNewsDao getDao(DSLContext context) {
		return new EhNewsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
