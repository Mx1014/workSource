// @formatter:off
package com.everhomes.news;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.server.schema.tables.daos.EhNewsCommunitiesDao;
import com.everhomes.server.schema.tables.pojos.EhNewsCommunities;
import com.everhomes.user.UserContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
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
import com.everhomes.server.schema.tables.daos.EhNewsCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhNewsDao;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
		news.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if (news.getPublishTime() == null) {
			news.setPublishTime(news.getCreateTime());
		}
		getReadWriteDao().insert(news);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNews.class, null);
	}

	
	
	@Override
	public void createNewsList(List<News> newsList) {
		List<EhNews> list = newsList.stream().map(news->{
			Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNews.class));
			news.setId(id);
			news.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			if (news.getPublishTime() == null) {
				news.setPublishTime(news.getCreateTime());
			}
			return ConvertHelper.convert(news, EhNews.class);
		}).collect(Collectors.toList());
		getReadWriteDao().insert(list);
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
	public List<News> findAllActiveNewsByPage(Long from, Integer pageSize){
		return getReadOnlyContext().select().from(Tables.EH_NEWS)
		.where(Tables.EH_NEWS.STATUS.eq(NewsStatus.ACTIVE.getCode()))
		.limit(from.intValue(), pageSize.intValue()).fetch().map(r -> ConvertHelper.convert(r, News.class));
	}

	@Override
	public List<News> listNews(Long categoryId,Integer namespaceId, Long from, Integer pageSize) {
		SelectConditionStep<Record> step =  getReadOnlyContext().select().from(Tables.EH_NEWS).where(Tables.EH_NEWS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_NEWS.STATUS.eq(NewsStatus.ACTIVE.getCode())) ;
		if(null != categoryId)
			step.and(Tables.EH_NEWS.CATEGORY_ID.eq(categoryId));
		return step.orderBy(Tables.EH_NEWS.TOP_INDEX.desc(), Tables.EH_NEWS.PUBLISH_TIME.desc(), Tables.EH_NEWS.ID.desc())
				.limit(from.intValue(), pageSize.intValue()).fetch().map(r -> ConvertHelper.convert(r, News.class));
	}

	@Override
	public Long getMaxTopIndex(Integer namespaceId){
		return getReadOnlyContext().select(Tables.EH_NEWS.TOP_INDEX.max()).from(Tables.EH_NEWS).where(Tables.EH_NEWS.NAMESPACE_ID.eq(namespaceId))
		.fetchOne().value1();
	}

	@Override
	public void createNewsCommunity(NewsCommunity newsCommunity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNewsCommunities.class));
		newsCommunity.setId(id);
		newsCommunity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		newsCommunity.setCreatorUid(UserContext.current().getUser().getId());

		EhNewsCommunitiesDao dao = new EhNewsCommunitiesDao(context.configuration());
		getReadWriteDao().insert(newsCommunity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNewsCommunities.class, null);
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



	@Override
	public NewsCategory findNewsCategoryById(Long categoryId) { 
		assert(categoryId != null);
		EhNewsCategoriesDao dao = new EhNewsCategoriesDao(getReadOnlyContext().configuration());
		return ConvertHelper.convert(dao.findById(categoryId), NewsCategory.class);
	}
}
