// @formatter:off
package com.everhomes.news;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.news.NewsTagValsDTO;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhNewsCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhNewsTagRecord;
import com.everhomes.user.UserContext;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.news.NewsOwnerType;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class NewsProviderImpl implements NewsProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public Long createNews(News news) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNews.class));
		news.setId(id);
		news.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if (news.getPublishTime() == null) {
			news.setPublishTime(news.getCreateTime());
		}
		
		news.setCreatorUid(UserContext.currentUserId());
		
		getReadWriteDao().insert(news);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNews.class, null);
		return  id;
	}

	@Override
	public void createNewsCategory(NewsCategory newsCategory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNewsCategories.class));
		newsCategory.setId(id);
		newsCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		new EhNewsCategoriesDao(getContext(AccessSpec.readWrite()).configuration()).insert(newsCategory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNewsCategories.class, null);
	}

	@Override
	public void createNewsTagVals(NewsTagVals newsTagVals) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNewsTagVals.class));
		newsTagVals.setId(id);
		new EhNewsTagValsDao(getContext(AccessSpec.readWrite()).configuration()).insert(newsTagVals);
		DaoHelper.publishDaoAction(DaoAction.CREATE,EhNewsTagVals.class,null);
	}

	@Override
	public void deletNewsTagVals(Long newsId) {
		DeleteQuery query = getContext(AccessSpec.readWrite()).deleteQuery(Tables.EH_NEWS_TAG_VALS);
		query.addConditions(Tables.EH_NEWS_TAG_VALS.NEWS_ID.eq(newsId));
		query.execute();
	}

	@Override
	public List<NewsTagVals> listNewsTagVals(Long newsId) {
		return dbProvider.getDslContext(AccessSpec.readOnlyWith(EhNewsTagVals.class)).select().from(Tables.EH_NEWS_TAG_VALS)
				.where(Tables.EH_NEWS_TAG_VALS.NEWS_ID.eq(newsId)).fetch().map(r->ConvertHelper.convert(r, NewsTagVals.class));
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
	public void updateNewsCategory(NewsCategory newsCategory) {
		new EhNewsCategoriesDao(getContext(AccessSpec.readWrite()).configuration()).update(newsCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNewsCategories.class, null);
	}

	@Override
	public Long createNewsTag(NewsTag newsTag) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNewsTag.class));
		newsTag.setId(id);
		newsTag.setDefaultOrder(id);
		newsTag.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		new EhNewsTagDao(getContext(AccessSpec.readWrite()).configuration()).insert(newsTag);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNewsTag.class, null);
		return id;
	}

	//@CacheEvict(value="findNewsTagById", key="#id")
	@CacheEvict(value="findNewsTagById", allEntries=true)
	@Override
	public void updateNewsTag(NewsTag newsTag) {
		new EhNewsTagDao(getContext(AccessSpec.readWrite()).configuration()).update(newsTag);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNewsTag.class, null);
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
	public News findNewsByNamespaceAndId(Integer namespaceId, Long id) {
		
		Record rec = getReadOnlyContext()
		.select()
		.from(Tables.EH_NEWS)
		.where(
				Tables.EH_NEWS.ID.eq(id)
				.and(Tables.EH_NEWS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_NEWS.STATUS.ne(NewsStatus.INACTIVE.getCode()))).fetchOne();
		
		if (null == rec) {
			return null;
		}
		
		return ConvertHelper.convert(rec, News.class);
	}
	
	@Override
	public List<News> findAllActiveNewsByPage(Long from, Integer pageSize){
		return getReadOnlyContext().select().from(Tables.EH_NEWS)
		.where(Tables.EH_NEWS.STATUS.eq(NewsStatus.ACTIVE.getCode()).or(Tables.EH_NEWS.STATUS.eq(NewsStatus.DRAFT.getCode())))
		.limit(from.intValue(), pageSize.intValue()).fetch().map(r -> ConvertHelper.convert(r, News.class));
	}
	

	@Override
	public List<News> listNews(Long ownerId, List<Long> communityIds, Long categoryId, Integer namespaceId, Long from, Integer pageSize,  boolean isScene, Byte status) {
		SelectJoinStep<Record> step =  getReadOnlyContext().select().from(Tables.EH_NEWS);

		Condition cond = Tables.EH_NEWS.NAMESPACE_ID.eq(namespaceId);
		if(status!=null){
			cond = cond.and(Tables.EH_NEWS.STATUS.eq(status));
		}
		else{
			cond = cond.and(Tables.EH_NEWS.STATUS.eq(NewsStatus.ACTIVE.getCode()).or(Tables.EH_NEWS.STATUS.eq(NewsStatus.DRAFT.getCode())));
		}
		
		//作为多应用的标识categoryId必须传
		cond = cond.and(Tables.EH_NEWS.CATEGORY_ID.eq(categoryId));
		
		if (null != ownerId && isScene) {
			// 只用于客户端或前端使用
			step.join(Tables.EH_NEWS_COMMUNITIES).on(Tables.EH_NEWS_COMMUNITIES.NEWS_ID.eq(Tables.EH_NEWS.ID));
			cond = cond.and(Tables.EH_NEWS_COMMUNITIES.COMMUNITY_ID.eq(ownerId));
		}
		
		if (communityIds != null && !communityIds.isEmpty()) {
			//所有查询都需要限定在管理的项目id下
			cond = cond.and(Tables.EH_NEWS.OWNER_TYPE.eq(NewsOwnerType.COMMUNITY.getCode()));
			cond = cond.and(Tables.EH_NEWS.OWNER_ID.in(communityIds));
		}

		return step.where(cond).orderBy(Tables.EH_NEWS.STATUS.asc(),Tables.EH_NEWS.TOP_INDEX.desc(), Tables.EH_NEWS.PUBLISH_TIME.desc(), Tables.EH_NEWS.ID.desc())
				.limit(from.intValue(), pageSize).fetch().map(new DefaultRecordMapper(Tables.EH_NEWS.recordType(), News.class));
	}

	@Override
	public List<NewsTag> listNewsTag(Integer namespaceId, String ownerType, Long ownerId, Byte isSearch,Long parentId,Long pageAnchor, Integer pageSize,Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhNewsTag.class));
		SelectQuery<EhNewsTagRecord> query = context.selectQuery(Tables.EH_NEWS_TAG);
		if (!StringUtils.isEmpty(ownerType)) {
			query.addConditions(Tables.EH_NEWS_TAG.OWNER_TYPE.eq(ownerType));
		}

		if (null != ownerId) {
			query.addConditions(Tables.EH_NEWS_TAG.OWNER_ID.eq(ownerId));
		}

		query.addConditions(Tables.EH_NEWS_TAG.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_NEWS_TAG.DELETE_FLAG.eq((byte)0));
		if (isSearch != null)
			query.addConditions(Tables.EH_NEWS_TAG.IS_SEARCH.eq(isSearch));
		if (parentId != null)
			query.addConditions(Tables.EH_NEWS_TAG.PARENT_ID.eq(parentId));
		if(null != pageAnchor && pageAnchor != 0)
			query.addConditions(Tables.EH_NEWS_TAG.DEFAULT_ORDER.gt(pageAnchor));
		if(categoryId !=null){
			query.addConditions(Tables.EH_NEWS_TAG.CATEGORY_ID.eq(categoryId));
		}
		if(null != pageSize)
			query.addLimit(pageSize);
		query.addOrderBy(Tables.EH_NEWS_TAG.DEFAULT_ORDER.asc());

		List<NewsTag> result = query.fetch().stream().map(r->ConvertHelper.convert(r,NewsTag.class))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public Boolean getCommentForbiddenFlag(Long categoryId, Integer namespaceId) {
		final Integer[] count = new Integer[1];
		count[0] = getReadOnlyContext().selectCount().from(Tables.EH_NEWS_COMMENT_RULE)
				.where(Tables.EH_NEWS_COMMENT_RULE.CATEGORY_ID.eq(categoryId)
						.and(Tables.EH_NEWS_COMMENT_RULE.NAMESPACE_ID.eq(namespaceId))).fetchOneInto(Integer.class);

		if(count[0] > 0) {
			return true;
		}
		return false;
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

		EhNewsCommunitiesDao dao = new EhNewsCommunitiesDao(getContext(AccessSpec.readWrite()).configuration());
		dao.insert(newsCommunity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNewsCommunities.class, null);
	}

	@Override
	public List<Long> listNewsCommunities(Long newsId) {
		SelectQuery<EhNewsCommunitiesRecord> query = getReadOnlyContext().selectQuery(Tables.EH_NEWS_COMMUNITIES);
		query.addConditions(Tables.EH_NEWS_COMMUNITIES.NEWS_ID.eq(newsId));

		return query.fetch(Tables.EH_NEWS_COMMUNITIES.COMMUNITY_ID);
	}

	@Override
	public void deleteNewsCommunity(Long newsId) {
		DeleteQuery query = getContext(AccessSpec.readWrite()).deleteQuery(Tables.EH_NEWS_COMMUNITIES);
		query.addConditions(Tables.EH_NEWS_COMMUNITIES.NEWS_ID.eq(newsId));

		query.execute();
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

	@Override
	public void increaseViewCount(Long newsId, Long nViewCount) {
		Long newViewCount = (nViewCount==null?0:nViewCount)+1;
		dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_NEWS).set(Tables.EH_NEWS.VIEW_COUNT,newViewCount)
				.where(Tables.EH_NEWS.ID.eq(newsId)).execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNews.class, null);
	}

	@Cacheable(value="findNewsTagById", key="{#id}", unless="#result == null")
	@Override
	public NewsTag findNewsTagById(Long id) {
		assert (id!=null);
		return ConvertHelper.convert(new EhNewsTagDao(getContext(AccessSpec.readOnly()).configuration()).findById(id),NewsTag.class);
	}

	@Override
	public NewsCategory getCategoryIdByEntryId(Integer entryId,Integer namespaceId) {
		List<NewsCategory> categories = dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_NEWS_CATEGORIES)
				.where(Tables.EH_NEWS_CATEGORIES.ENTRY_ID.eq(entryId)).and(Tables.EH_NEWS_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
				.fetch().map(r -> ConvertHelper.convert(r, NewsCategory.class));
		if(categories!=null && categories.size()>0){
			return categories.get(0);
		}
		return null;
	}

	@Override
	public Long createNewPreview(News news) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNewPreview.class));
		news.setId(id);
		news.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if (news.getPublishTime() == null) {
			news.setPublishTime(news.getCreateTime());
		}
		DSLContext context = getReadWriteContext();
		EhNewPreviewDao dao = new EhNewPreviewDao(context.configuration());
		dao.insert(ConvertHelper.convert(news,EhNewPreview.class));
		return id;
	}

	@Override
	public News findNewPreview(Long id) {
		DSLContext context = getReadOnlyContext();
		EhNewPreviewDao dao = new EhNewPreviewDao(context.configuration());
		EhNewPreview result = dao.findById(id);
		return ConvertHelper.convert(result,News.class);
	}

	@Override
	public List<NewsTag> listParentTags(String ownerType, Long ownerId, Long categoryId) {
		return listNewsTag(UserContext.getCurrentNamespaceId(), ownerType, ownerId, null, 0L, null, null, categoryId);
	}

	@Override
	public void deleteProjectNewsTags(Long projectId, Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_NEWS_TAG)
		.where(
				Tables.EH_NEWS_TAG.OWNER_TYPE.eq(NewsOwnerType.COMMUNITY.getCode())
				.and(Tables.EH_NEWS_TAG.OWNER_ID.eq(projectId))
				.and(Tables.EH_NEWS_TAG.CATEGORY_ID.eq(categoryId))
				)
		.execute();
	}

}
