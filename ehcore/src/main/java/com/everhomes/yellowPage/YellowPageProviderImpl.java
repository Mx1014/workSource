package com.everhomes.yellowPage;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.module.GetServiceModuleCommand;
import com.everhomes.rest.yellowPage.*;
import com.everhomes.rest.yellowPage.standard.ConfigCommand;
import com.everhomes.rest.yellowPage.stat.ServiceAndTypeNameDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class YellowPageProviderImpl implements YellowPageProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageProviderImpl.class);
	
	private final com.everhomes.server.schema.tables.EhServiceAlliances SA_TABLE = Tables.EH_SERVICE_ALLIANCES;
	private final Class<com.everhomes.server.schema.tables.EhServiceAlliances> SA_CLASS = com.everhomes.server.schema.tables.EhServiceAlliances.class;

	private final com.everhomes.server.schema.tables.EhServiceAllianceCategories SA_TYPE_TABLE = Tables.EH_SERVICE_ALLIANCE_CATEGORIES;
	private final Class<com.everhomes.server.schema.tables.EhServiceAllianceCategories> SA_TYPE_CLASS = com.everhomes.server.schema.tables.EhServiceAllianceCategories.class;


	private final com.everhomes.server.schema.tables.EhServiceAllianceApplicationRecords SA_COMMITS_TABLE = Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS;
	private final Class<com.everhomes.server.schema.tables.EhServiceAllianceApplicationRecords> SA_COMMITS_CLASS = com.everhomes.server.schema.tables.EhServiceAllianceApplicationRecords.class;


	@Autowired
	private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
	public void createYellowPage(YellowPage yellowPage){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhYellowPages.class));
        yellowPage.setId(id);
        if(yellowPage.getStatus() == null) {
            yellowPage.setStatus(YellowPageStatus.ACTIVE.getCode());    
        }
        yellowPage.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhYellowPagesDao dao = new EhYellowPagesDao(context.configuration());
        dao.insert(yellowPage);
	}
	

    @Override
    public void updateYellowPage(YellowPage yellowPage) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhYellowPagesDao dao = new EhYellowPagesDao(context.configuration());
        dao.update(yellowPage);
    }
    
    @Override
 	public void createYellowPageAttachments(YellowPageAttachment yellowPageAttachment){
 		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
 		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhYellowPageAttachments.class));
         yellowPageAttachment.setId(id); 
         yellowPageAttachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
         EhYellowPageAttachmentsDao dao = new EhYellowPageAttachmentsDao(context.configuration());
         dao.insert(yellowPageAttachment);
 	}
 	
    
    
	@Override
	public void populateYellowPagesAttachment(YellowPage yellowPage) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhYellowPageAttachmentsRecord> query = context.selectQuery(Tables.EH_YELLOW_PAGE_ATTACHMENTS);
        query.addConditions(Tables.EH_YELLOW_PAGE_ATTACHMENTS.OWNER_ID.in(yellowPage.getId()));
        query.fetch().map((EhYellowPageAttachmentsRecord record) -> {
        	 yellowPage.getAttachments().add(ConvertHelper.convert(record, YellowPageAttachment.class));
             return null;
         });
	}

	@Override
	public YellowPage getYellowPageById(Long id) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhYellowPagesDao dao = new EhYellowPagesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), YellowPage.class);
		 
	}
	
	@Override
	public List<YellowPage> queryYellowPages(CrossShardListingLocator locator,
			int pageSize, String ownerType, Long ownerId, Long parentId, Byte type,
			String serviceType,String keywords) {
	        List<YellowPage> yellowPages = this.queryYellowPagesByOwnerId(locator, ownerId, pageSize, new ListingQueryBuilderCallback() {

	            @Override
	            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
	                    SelectQuery<? extends Record> query) {
	            	if (!StringUtils.isEmpty(ownerType) )
	            		query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_TYPE.eq(ownerType));
	                query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_ID.eq(ownerId));
	                query.addConditions(Tables.EH_YELLOW_PAGES.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
	                
	                if(!org.springframework.util.StringUtils.isEmpty(keywords)){
	                	query.addConditions(Tables.EH_YELLOW_PAGES.NAME.like(keywords + "%"));
	                }
	                
	                if(null!=parentId){
	                	query.addConditions(Tables.EH_YELLOW_PAGES.PARENT_ID.eq(parentId));
	                }
                	else {
                		query.addConditions(Tables.EH_YELLOW_PAGES.PARENT_ID.ne(0L));
					}
	                if(!StringUtils.isEmpty(serviceType)&&type.equals(YellowPageType.SERVICEALLIANCE.getCode())){
	                	query.addConditions(Tables.EH_YELLOW_PAGES.STRING_TAG3.eq(serviceType));
	                }
	                if(null!=type)
	                	query.addConditions(Tables.EH_YELLOW_PAGES.TYPE.eq(type));
	                return query;
	            }
	            
	        });
	        
	        if(yellowPages != null && yellowPages.size() > 0) {
	            return yellowPages;
	        }
	        return null;
	}
	
	public List<YellowPage> queryYellowPagesByOwnerId(ListingLocator locator, Long ownerId
            , int count, ListingQueryBuilderCallback queryBuilderCallback){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhYellowPagesRecord> query = context.selectQuery(Tables.EH_YELLOW_PAGES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
 
        query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_ID.eq(ownerId));
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_YELLOW_PAGES.ID.gt(locator.getAnchor()));
            }
        
        //query.addOrderBy(Tables.EH_ENTERPRISE_CONTACTS.CREATE_TIME.desc());
        query.addLimit(count);
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, YellowPage.class);
        });
	}


	@Override
	public void deleteYellowPageAttachmentsByOwnerId(Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		 
        SelectQuery<EhYellowPageAttachmentsRecord> query = context.selectQuery(Tables.EH_YELLOW_PAGE_ATTACHMENTS);
        query.addConditions(Tables.EH_YELLOW_PAGE_ATTACHMENTS.OWNER_ID.eq(ownerId));
        
        
        query.fetch().map((r) -> {
        	YellowPageAttachment ece = ConvertHelper.convert(r, YellowPageAttachment.class);
        	this.deleteYellowPageAttachment(ece);
        	return null;
        });
	}
	
	@Override
    public void deleteYellowPageAttachment(YellowPageAttachment attachment) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
        EhYellowPageAttachmentsDao dao = new EhYellowPageAttachmentsDao(context.configuration());
        dao.delete(attachment);        
    }


	@Override
	public YellowPage queryYellowPageTopic(String ownerType, Long ownerId,
			Byte type) {
		   CrossShardListingLocator locator = new CrossShardListingLocator();
	        locator.setAnchor(0L);
	        List<YellowPage> yellowPages = this.queryYellowPagesByOwnerId(locator, ownerId, 20, new ListingQueryBuilderCallback() {

	            @Override
	            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
	                    SelectQuery<? extends Record> query) {
	            	if (!StringUtils.isEmpty(ownerType) )
	            		query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_TYPE.eq(ownerType));
	                query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_ID.eq(ownerId));
	                //topic
	                query.addConditions(Tables.EH_YELLOW_PAGES.PARENT_ID.eq(0L));
	                query.addConditions(Tables.EH_YELLOW_PAGES.TYPE.eq(type));
	                query.addConditions(Tables.EH_YELLOW_PAGES.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
	                return query;
	            }
	            
	        });
	        
	        if(yellowPages != null && yellowPages.size() > 0) {
	            return yellowPages.get(0);
	        }
	        return null;
	}


	@Override
	public List<YellowPage> getYellowPagesByCategoryId(Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhYellowPagesRecord> query = context.selectQuery(Tables.EH_YELLOW_PAGES);
 
        query.addConditions(Tables.EH_YELLOW_PAGES.INTEGRAL_TAG2.eq(categoryId));
        
        return query.fetch().map((r) -> {
            return ConvertHelper.convert(r, YellowPage.class);
        });
	}

	/**   
	* @Function: YellowPageProviderImpl.java
	* @Description: 后台查询服务联盟使用
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月1日 下午4:11:45 
	*
	*/
	@Override
	public List<ServiceAlliances> queryServiceAllianceAdmin(
			CrossShardListingLocator locator, int pageSize, String ownerType,
			Long ownerId, Long parentId, Long categoryId, List<Long> childTagIds, String keywords, Byte displayFlag, ConfigCommand cmd) {
		return queryServiceAlliance(locator, pageSize, ownerType, ownerId, null, parentId, categoryId, childTagIds, keywords, displayFlag, false, cmd);
	}
	
	
	private List<ServiceAlliances> queryServiceAlliance(
			CrossShardListingLocator locator, int pageSize, String ownerType,
			Long ownerId, List<Long> authProjectIds, Long parentId, Long categoryId, List<Long> childTagIds, String keywords, Byte displayFlag, boolean isByScene, ConfigCommand configCmd) {
		List<ServiceAlliances> saList = new ArrayList<ServiceAlliances>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		
		// 取别名
		com.everhomes.server.schema.tables.EhServiceAlliances ALLIANCES = Tables.EH_SERVICE_ALLIANCES;
		com.everhomes.server.schema.tables.EhAllianceTag TAGS = Tables.EH_ALLIANCE_TAG;
		com.everhomes.server.schema.tables.EhAllianceTagVals TAG_VAL = Tables.EH_ALLIANCE_TAG_VALS;
		com.everhomes.server.schema.tables.EhAllianceServiceCategoryMatch MATCH = Tables.EH_ALLIANCE_SERVICE_CATEGORY_MATCH;
		
		
        SelectQuery<Record> query = context.selectQuery();;
         
        query.addSelect(ALLIANCES.fields());
        
		if (CollectionUtils.isEmpty(childTagIds) && null == categoryId) {
			query.addFrom(ALLIANCES);
		} else {
			
			Table<Record> tmpFrom = null;
			if (!CollectionUtils.isEmpty(childTagIds)) {
				tmpFrom = ALLIANCES
						.leftOuterJoin(TAG_VAL).on( //连接关联表，获取改服务关联的标签记录
								TAG_VAL.OWNER_ID.eq(ALLIANCES.ID)
								.and(TAG_VAL.TAG_ID.in(childTagIds))
								)
						.leftOuterJoin(TAGS).on( //连接该表是为了获取标签状态，以过滤被删掉的标签
								TAGS.ID.eq(TAG_VAL.TAG_ID));
				
				if (null != categoryId) {
					tmpFrom = tmpFrom.leftOuterJoin(MATCH).on(
							MATCH.OWNER_TYPE.eq(configCmd.getOwnerType())
							.and(MATCH.OWNER_ID.eq(configCmd.getOwnerId()))
							.and(MATCH.SERVICE_ID.eq(ALLIANCES.ID))
							.and(MATCH.CATEGORY_ID.eq(categoryId))
							);
				}
			}
			
			if (null != categoryId) {
				tmpFrom = ALLIANCES.leftOuterJoin(MATCH).on(
						MATCH.OWNER_TYPE.eq(configCmd.getOwnerType())
						.and(MATCH.OWNER_ID.eq(configCmd.getOwnerId()))
						.and(MATCH.SERVICE_ID.eq(ALLIANCES.ID))
						.and(MATCH.CATEGORY_ID.eq(categoryId))
						);
				
				if (!CollectionUtils.isEmpty(childTagIds)) {
					tmpFrom = tmpFrom
							.leftOuterJoin(TAG_VAL).on( //连接关联表，获取改服务关联的标签记录
									TAG_VAL.OWNER_ID.eq(ALLIANCES.ID)
									.and(TAG_VAL.TAG_ID.in(childTagIds))
									)
							.leftOuterJoin(TAGS).on( //连接该表是为了获取标签状态，以过滤被删掉的标签
									TAGS.ID.eq(TAG_VAL.TAG_ID));
				}
			}
			

		    query.addFrom(tmpFrom);

			// 过滤被删掉的标签
		    if (!CollectionUtils.isEmpty(childTagIds)) {
				query.addConditions(TAGS.ID.isNotNull().and(TAGS.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode())));
				query.addGroupBy(ALLIANCES.ID);
				query.addHaving(ALLIANCES.ID.count().eq(childTagIds.size())); //获取到的标签个数要与传入的个数一样，才能说明该服务关联的标签和搜索的标签完全一致。
		    }
		    
		    if (null != categoryId) {
		    	query.addConditions(MATCH.ID.isNotNull());
		    }
		}
        
        if (isByScene) {
			query.addConditions(Tables.EH_SERVICE_ALLIANCES.RANGE.like("%" + ownerId + "%")
					.or(Tables.EH_SERVICE_ALLIANCES.RANGE.eq("all")));

			if (CollectionUtils.isEmpty(authProjectIds)) {
				query.addConditions(ALLIANCES.OWNER_ID.eq(ownerId));
			} else {
				query.addConditions(ALLIANCES.OWNER_ID.in(authProjectIds));
			}

        } else {
    		query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_TYPE.eq(ownerType));
    		query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_ID.eq(ownerId));
        }

        if (null != displayFlag) {
        	query.addConditions(Tables.EH_SERVICE_ALLIANCES.DISPLAY_FLAG.eq(displayFlag));
        }

		if (locator.getAnchor() != null) {
			query.addConditions(Tables.EH_SERVICE_ALLIANCES.DEFAULT_ORDER.gt(locator.getAnchor()));
		}
        
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
        
        if(!org.springframework.util.StringUtils.isEmpty(keywords)){
        	query.addConditions(Tables.EH_SERVICE_ALLIANCES.NAME.like("%" + keywords + "%"));
        }
        
        // 必须传对应parentId，如旧版本有数据问题需通过sql解决
    	query.addConditions(ALLIANCES.PARENT_ID.eq(parentId).and(ALLIANCES.PARENT_ID.ne(0L)));

        //by dengs,按照defaultorder排序，20170525
        query.addOrderBy(Tables.EH_SERVICE_ALLIANCES.DEFAULT_ORDER.asc());
        query.addLimit(pageSize);

        LOGGER.info(query.toString());

        saList = query.fetchInto(ServiceAlliances.class);
        
        if(saList != null && saList.size() > 0) {

        	//优化性能 #37643
        	for (ServiceAlliances sa : saList) {
        		sa.setDescription(null);
        	}

            return saList;
        }
        return saList;
	}
	
	/** 
	* @see com.everhomes.yellowPage.YellowPageProvider#queryServiceAllianceByScene(com.everhomes.listing.CrossShardListingLocator, int, java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String)   
	* @Function: YellowPageProviderImpl.java
	* @Description: 客户端查询时使用
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年6月1日 下午4:13:54 
	*/
	@Override
	public List<ServiceAlliances> queryServiceAllianceByScene(CrossShardListingLocator locator, int pageSize, String ownerType,
			Long ownerId, List<Long> authProjectIds, Long parentId, Long categoryId, List<Long> childTagIds, String keywords, ConfigCommand cmd) {
		return queryServiceAlliance(locator, pageSize, ownerType, ownerId, authProjectIds, parentId, categoryId, childTagIds, keywords, DisplayFlagType.SHOW.getCode(), true, cmd);
	}

	@Override
	public YellowPage findYellowPageById(Long id, String ownerType, Long ownerId) {
		
		List<YellowPage> yellowPages = new ArrayList<YellowPage>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhYellowPagesRecord> query = context.selectQuery(Tables.EH_YELLOW_PAGES);
 
    	query.addConditions(Tables.EH_YELLOW_PAGES.ID.eq(id));
    	query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_ID.eq(ownerId));
        
        query.fetch().map((r) -> {
        	yellowPages.add(ConvertHelper.convert(r, YellowPage.class));
            return null;
        });
        
        if(yellowPages == null || yellowPages.size() == 0) {
        	return null;
        }
        
        return yellowPages.get(0);
	}


	@Override
	public ServiceAllianceCategories findCategoryById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceAllianceCategoriesDao dao = new EhServiceAllianceCategoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceAllianceCategories.class);
	}


	@Override
	public void createCategory(ServiceAllianceCategories category) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		 
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceCategories.class));
		category.setId(id);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		category.setCreatorUid(UserContext.current().getUser().getId());
		category.setDefaultOrder(id);
		category.setStatus(CategoryAdminStatus.ACTIVE.getCode());

		EhServiceAllianceCategoriesDao dao = new EhServiceAllianceCategoriesDao(context.configuration());
		dao.insert(category);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceCategories.class, null);
		
	}


	@Override
	public void updateCategory(ServiceAllianceCategories category) {
		assert(category.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceAllianceCategoriesDao dao = new EhServiceAllianceCategoriesDao(context.configuration());
        dao.update(ConvertHelper.convert(category, EhServiceAllianceCategories.class));
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAllianceCategories.class, category.getId());
	}

	@Caching(evict = { @CacheEvict(value="queryServiceAlliance", allEntries=true)})
	@Override
	public void createServiceAlliances(ServiceAlliances sa) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAlliances.class));
        sa.setId(id);
        
        //如果是“policydeclare”类型则把日期格式为为排序序号
        if (null != sa.getEndTime()) {
        	 sa.setDefaultOrder(getDateDefaultOrder(sa));
        } else {
        	  sa.setDefaultOrder(-id); //缺陷 #29826：旧的排序保持，新的排序按id倒叙排
        }
        
        if(sa.getStatus() == null) {
            sa.setStatus(YellowPageStatus.ACTIVE.getCode());    
        }
		sa.setCreatorUid(UserContext.current().getUser().getId());
        sa.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhServiceAlliancesDao dao = new EhServiceAlliancesDao(context.configuration());
        dao.insert(sa);
	}
	

	@Caching(evict = { @CacheEvict(value="queryServiceAlliance", allEntries=true)})
	@Override
	public void updateServiceAlliances(ServiceAlliances sa) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAlliancesDao dao = new EhServiceAlliancesDao(context.configuration());
		
		//如果修改了结束时间，则更新defaulOrder
		if (null != sa.getEndTime()) {
			sa.setDefaultOrder(getDateDefaultOrder(sa));
		}
		sa.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		sa.setUpdateUid(UserContext.currentUserId());
        dao.update(sa);
	}


	@Override
	public void createServiceAllianceAttachments(
			ServiceAllianceAttachment attachment) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
 		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceAttachments.class));
        
 		attachment.setId(id); 
		attachment.setCreatorUid(UserContext.current().getUser().getId());
 		attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
 		EhServiceAllianceAttachmentsDao dao = new EhServiceAllianceAttachmentsDao(context.configuration());
 		dao.insert(attachment);
	}


	@Override
	public void deleteServiceAllianceAttachmentsByOwnerId(String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		 
        SelectQuery<EhServiceAllianceAttachmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS);
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.OWNER_ID.eq(ownerId));
        
        
        query.fetch().map((r) -> {
        	ServiceAllianceAttachment saa = ConvertHelper.convert(r, ServiceAllianceAttachment.class);
        	this.deleteServiceAllianceAttachment(saa);
        	return null;
        });
		
	}
	
	private void deleteServiceAllianceAttachment(ServiceAllianceAttachment attachment) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhServiceAllianceAttachmentsDao dao = new EhServiceAllianceAttachmentsDao(context.configuration());
        dao.delete(attachment);        
    }

	@Override
	public ServiceAlliances findServiceAllianceById(Long id, String ownerType,
			Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceAlliancesDao dao = new EhServiceAlliancesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceAlliances.class);
	}


	@Override
	public void populateServiceAlliancesAttachment(ServiceAlliances sa, String ownerType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhServiceAllianceAttachmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS);
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.OWNER_ID.in(sa.getId()));
        query.addOrderBy(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.DEFAULT_ORDER.asc()); //获取图片按defaultOrder  服务联盟v3.4需求
        query.fetch().map((EhServiceAllianceAttachmentsRecord record) -> {
			if(ServiceAllianceAttachmentType.BANNER.equals(ServiceAllianceAttachmentType.fromCode(record.getAttachmentType()))) {
				sa.getAttachments().add(ConvertHelper.convert(record, ServiceAllianceAttachment.class));
			} else if(ServiceAllianceAttachmentType.FILE_ATTACHMENT.equals(ServiceAllianceAttachmentType.fromCode(record.getAttachmentType()))) {
				sa.getFileAttachments().add(ConvertHelper.convert(record, ServiceAllianceAttachment.class));
			} else if(ServiceAllianceAttachmentType.COVER_ATTACHMENT.equals(ServiceAllianceAttachmentType.fromCode(record.getAttachmentType()))) {
				sa.getCoverAttachments().add(ConvertHelper.convert(record, ServiceAllianceAttachment.class));
			} 


             return null;
         });
		
	}


	@Override
	public ServiceAllianceCategories findCategoryByName(Integer namespaceId,
			String name) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceAllianceCategories.class));
        SelectQuery<EhServiceAllianceCategoriesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_CATEGORIES);
        if(null != namespaceId) 
        	query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        if(null != name)
        	query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAME.eq(name));
        
        return ConvertHelper.convert(query.fetchOne(), ServiceAllianceCategories.class);
	}

	@Override
	public List<ServiceAllianceCategories> listCategories(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Integer namespaceId, Long parentId, Long type,  List<Byte> displayDestination, boolean queryAllChilds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        List<ServiceAllianceCategories> result = new ArrayList<ServiceAllianceCategories>();
        
        SelectQuery<EhServiceAllianceCategoriesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_CATEGORIES);
        Condition condition = DSL.trueCondition();
        
		if (parentId != null) {
			condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.eq(parentId));
		} else {
			if (queryAllChilds) {
				condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.ne(0L));
			}
		}

        if (null != type) {
        	condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.TYPE.eq(type));
        }

        condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode()));
        condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        
        if (null != ownerId) {
        	condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.OWNER_ID.eq(ownerId));
        }
        
        if (!StringUtils.isBlank(ownerType)) {
        	condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.OWNER_TYPE.eq(ownerType));
        }


		if(displayDestination != null && displayDestination.size() > 0) {
			condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.DISPLAY_DESTINATION.in(displayDestination));
		}

		if (null != locator && null != locator.getAnchor()) {
			condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ID.ge(locator.getAnchor()));
		}

        query.addConditions(condition);

    	if (null != pageSize) {
			query.addLimit(pageSize+1);
		}

    	query.addOrderBy(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.DEFAULT_ORDER.asc(), Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ID.asc());

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query child categories, sql=" + query.getSQL());
            LOGGER.debug("Query child categories, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhServiceAllianceCategoriesRecord record) -> {
        	result.add(ConvertHelper.convert(record, ServiceAllianceCategories.class));
            return null;
        });
        
		if (null != locator) {
			int size = result.size();
			if (null != pageSize && size > pageSize) {
				locator.setAnchor(result.get(size - 1).getId());
				result.remove(size - 1);
			} else {
				locator.setAnchor(null);
			}
		}

        return result;
	}


	@Override
	public void createNotifyTarget(ServiceAllianceNotifyTargets target) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceNotifyTargets.class));
		target.setId(id);
		target.setStatus((byte) 1);
        target.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhServiceAllianceNotifyTargetsDao dao = new EhServiceAllianceNotifyTargetsDao(context.configuration());
        dao.insert(target);
		
	}

	@Override
	public void createServiceAllianceCategory(ServiceAllianceCategories serviceAllianceCategories) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		serviceAllianceCategories.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhServiceAllianceCategoriesDao dao = new EhServiceAllianceCategoriesDao(context.configuration());
		dao.insert(serviceAllianceCategories);

	}


	@Override
	public void updateServiceAllianceCategory(ServiceAllianceCategories serviceAllianceCategories) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceCategoriesDao dao = new EhServiceAllianceCategoriesDao(context.configuration());
		dao.update(serviceAllianceCategories);
	}

	@Override
	public void updateNotifyTarget(ServiceAllianceNotifyTargets target) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceNotifyTargetsDao dao = new EhServiceAllianceNotifyTargetsDao(context.configuration());
        dao.update(target);
		
	}


	@Override
	public void deleteNotifyTarget(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceAllianceNotifyTargets.class));
		EhServiceAllianceNotifyTargetsDao dao = new EhServiceAllianceNotifyTargetsDao(context.configuration());
		dao.deleteById(id);
	}


	@Override
	public ServiceAllianceNotifyTargets findNotifyTarget(String ownerType,
			Long ownerId, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceAllianceNotifyTargetsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS);
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.ID.eq(id));
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.OWNER_ID.eq(ownerId));
		 
		List<ServiceAllianceNotifyTargets> result = new ArrayList<ServiceAllianceNotifyTargets>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ServiceAllianceNotifyTargets.class));
			return null;
		});
		if(result.size()==0)
			return null;
		return result.get(0);
	}


	@Override
	public List<ServiceAllianceNotifyTargets> listNotifyTargets(
			Integer namespaceId, Byte contactType, Long categoryId,
			CrossShardListingLocator locator, int pageSize) {
		List<ServiceAllianceNotifyTargets> targets = new ArrayList<ServiceAllianceNotifyTargets>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhServiceAllianceNotifyTargetsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS);
 
//        if (!StringUtils.isEmpty(ownerType) )
//    		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.OWNER_TYPE.eq(ownerType));
//        
//        if(ownerId != null)
//        	query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.OWNER_ID.eq(ownerId));
//        
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.NAMESPACE_ID.eq(namespaceId));
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.ID.gt(locator.getAnchor()));
            
        }
        
        if(categoryId != null) {
        	query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.CATEGORY_ID.eq(categoryId));
        }
    		
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.CONTACT_TYPE.eq(contactType));
        query.addLimit(pageSize);
       
        query.fetch().map((r) -> {
        	targets.add(ConvertHelper.convert(r, ServiceAllianceNotifyTargets.class));
            return null;
        });
        
        return targets;
	}


	@Override
	public ServiceAllianceNotifyTargets findNotifyTarget(String ownerType,
			Long ownerId, Long categoryId, Byte contactType, String contactToken) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceAllianceNotifyTargetsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS);
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.CATEGORY_ID.eq(categoryId));
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.CONTACT_TYPE.eq(contactType));
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.CONTACT_TOKEN.eq(contactToken));
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.OWNER_ID.eq(ownerId));
		 
		List<ServiceAllianceNotifyTargets> result = new ArrayList<ServiceAllianceNotifyTargets>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, ServiceAllianceNotifyTargets.class));
			return null;
		});
		if(result.size()==0)
			return null;
		return result.get(0);
	}


	@Override
	public Long createServiceAllianceRequests(ServiceAllianceRequests request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceRequests.class));
		request.setId(id);

		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhServiceAllianceRequestsDao dao = new EhServiceAllianceRequestsDao(context.configuration());
        dao.insert(request);
		return id;
	}


	@Override
	public ServiceAllianceRequests findServiceAllianceRequests(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceRequestsDao dao = new EhServiceAllianceRequestsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceAllianceRequests.class);
	}


	@Override
	public List<ServiceAllianceRequests> listServiceAllianceRequests(
			CrossShardListingLocator locator, int pageSize) {
		List<ServiceAllianceRequests> requests = new ArrayList<ServiceAllianceRequests>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhServiceAllianceRequests.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhServiceAllianceRequestsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_REQUESTS);
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_SERVICE_ALLIANCE_REQUESTS.ID.gt(locator.getAnchor()));
            
            query.addLimit(pageSize - requests.size());
            
            query.fetch().map((r) -> {
            	
            	requests.add(ConvertHelper.convert(r, ServiceAllianceRequests.class));
                return null;
            });

            if (requests.size() >= pageSize) {
                locator.setAnchor(requests.get(requests.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return requests;
	}


	@Override
	public Long createSettleRequests(SettleRequests request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSettleRequests.class));
		request.setId(id);

		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhSettleRequestsDao dao = new EhSettleRequestsDao(context.configuration());
        dao.insert(request);
        return id;
		
	}


	@Override
	public SettleRequests findSettleRequests(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhSettleRequestsDao dao = new EhSettleRequestsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), SettleRequests.class);
	}


	@Override
	public List<SettleRequests> listSettleRequests(
			CrossShardListingLocator locator, int pageSize) {

		List<SettleRequests> requests = new ArrayList<SettleRequests>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhSettleRequests.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhSettleRequestsRecord> query = context.selectQuery(Tables.EH_SETTLE_REQUESTS);
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_SETTLE_REQUESTS.ID.gt(locator.getAnchor()));
            
            query.addLimit(pageSize - requests.size());
            
            query.fetch().map((r) -> {
            	
            	requests.add(ConvertHelper.convert(r, SettleRequests.class));
                return null;
            });

            if (requests.size() >= pageSize) {
                locator.setAnchor(requests.get(requests.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return requests;
	}


	@Override
	public Long createReservationRequests(ReservationRequests request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceReservationRequests.class));
		request.setId(id);

		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhServiceAllianceReservationRequestsDao dao = new EhServiceAllianceReservationRequestsDao(context.configuration());
        dao.insert(request);
        return id;
	}


	@Override
	public ReservationRequests findReservationRequests(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceReservationRequestsDao dao = new EhServiceAllianceReservationRequestsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ReservationRequests.class);
	}


	@Override
	public List<ReservationRequests> listReservationRequests(
			CrossShardListingLocator locator, int pageSize) {
		List<ReservationRequests> requests = new ArrayList<ReservationRequests>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhServiceAllianceReservationRequests.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhServiceAllianceReservationRequestsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS);
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.ID.gt(locator.getAnchor()));
            
            query.addLimit(pageSize - requests.size());
            
            query.fetch().map((r) -> {
            	
            	requests.add(ConvertHelper.convert(r, ReservationRequests.class));
                return null;
            });

            if (requests.size() >= pageSize) {
                locator.setAnchor(requests.get(requests.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return requests;
	}


	@Override
	public Long createApartmentRequests(ServiceAllianceApartmentRequests request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceApartmentRequests.class));
		request.setId(id);

		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhServiceAllianceApartmentRequestsDao dao = new EhServiceAllianceApartmentRequestsDao(context.configuration());
        dao.insert(request);
        return id;
	}


	@Override
	public ServiceAllianceApartmentRequests findApartmentRequests(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceApartmentRequestsDao dao = new EhServiceAllianceApartmentRequestsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceAllianceApartmentRequests.class);
	}


	@Override
	public List<ServiceAllianceApartmentRequests> listApartmentRequests(
			CrossShardListingLocator locator, int pageSize) {

		List<ServiceAllianceApartmentRequests> requests = new ArrayList<ServiceAllianceApartmentRequests>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhServiceAllianceApartmentRequests.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhServiceAllianceApartmentRequestsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS);
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.ID.gt(locator.getAnchor()));
            
            query.addLimit(pageSize - requests.size());
            
            query.fetch().map((r) -> {
            	
            	requests.add(ConvertHelper.convert(r, ServiceAllianceApartmentRequests.class));
                return null;
            });

            if (requests.size() >= pageSize) {
                locator.setAnchor(requests.get(requests.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return requests;
	}


	@Override
	public Long createInvestRequests(ServiceAllianceInvestRequests request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceInvestRequests.class));
		request.setId(id);

		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhServiceAllianceInvestRequestsDao dao = new EhServiceAllianceInvestRequestsDao(context.configuration());
		dao.insert(request);
		return id;
	}

	@Override
	public ServiceAllianceInvestRequests findInvestRequests(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceInvestRequestsDao dao = new EhServiceAllianceInvestRequestsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ServiceAllianceInvestRequests.class);
	}

	@Override
	public Long createGolfRequest(ServiceAllianceGolfRequest request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.
				getSequenceDomainFromTablePojo(EhServiceAllianceGolfRequests.class));
		request.setId(id);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		EhServiceAllianceGolfRequestsDao dao = new EhServiceAllianceGolfRequestsDao(context.configuration());
		dao.insert(request);
		return id;
	}

	@Override
	public ServiceAllianceGolfRequest findGolfRequest(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceGolfRequestsDao dao = new EhServiceAllianceGolfRequestsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ServiceAllianceGolfRequest.class);
	}

	@Override
	public Long createGymRequest(ServiceAllianceGymRequest request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.
				getSequenceDomainFromTablePojo(EhServiceAllianceGymRequests.class));
		request.setId(id);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		EhServiceAllianceGymRequestsDao dao = new EhServiceAllianceGymRequestsDao(context.configuration());
		dao.insert(request);
		return id;
	}

	@Override
	public ServiceAllianceGymRequest findGymRequest(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceGymRequestsDao dao = new EhServiceAllianceGymRequestsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ServiceAllianceGymRequest.class);
	}

	@Override
	public Long createServerRequest(ServiceAllianceServerRequest request) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.
				getSequenceDomainFromTablePojo(EhServiceAllianceServerRequests.class));
		request.setId(id);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		EhServiceAllianceServerRequestsDao dao = new EhServiceAllianceServerRequestsDao(context.configuration());
		dao.insert(request);
		return id;
	}

	@Override
	public ServiceAllianceServerRequest findServerRequest(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAllianceServerRequestsDao dao = new EhServiceAllianceServerRequestsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), ServiceAllianceServerRequest.class);
	}

	@Override
	public List<ServiceAllianceInvestRequests> listInvestRequests(CrossShardListingLocator locator, int pageSize) {
		List<ServiceAllianceInvestRequests> requests = new ArrayList<ServiceAllianceInvestRequests>();

		if (locator.getShardIterator() == null) {
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhServiceAllianceInvestRequests.class);
			ShardIterator shardIterator = new ShardIterator(accessSpec);
			locator.setShardIterator(shardIterator);
		}
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
			SelectQuery<EhServiceAllianceInvestRequestsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS);
			if(locator.getAnchor() != null)
				query.addConditions(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.ID.gt(locator.getAnchor()));

			query.addLimit(pageSize - requests.size());

			query.fetch().map((r) -> {

				requests.add(ConvertHelper.convert(r, ServiceAllianceInvestRequests.class));
				return null;
			});

			if (requests.size() >= pageSize) {
				locator.setAnchor(requests.get(requests.size() - 1).getId());
				return AfterAction.done;
			} else {
				locator.setAnchor(null);
			}
			return AfterAction.next;
		});

		return requests;
	}

	@Override
	public List<JumpModuleDTO> jumpModules(Integer namespaceId, String bizString) {
		List<JumpModuleDTO> modules = new ArrayList<>();

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceAllianceJumpModuleRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE);
		Condition condition = (Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.NAMESPACE_ID.eq(namespaceId).and(
				Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.SIGNAL.ne(SignalEnum.DELETE.getCode()))).
				or(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.SIGNAL.eq(SignalEnum.APPROVAL.getCode())
		);
		query.addConditions(condition);
		if (!StringUtils.isEmpty(bizString)) {
			query.addConditions(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.MODULE_URL.eq(bizString));
		}

		query.fetch().map((r) -> {
			modules.add(ConvertHelper.convert(r, JumpModuleDTO.class));
			return null;
		});

		return modules;
	}

	@Override
	public List<ServiceAllianceAttachment> listAttachments(
			CrossShardListingLocator locator, int count, String ownerType, Long ownerId) {
		List<ServiceAllianceAttachment> attachments = new ArrayList<ServiceAllianceAttachment>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhActivityAttachments.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhServiceAllianceAttachmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.ID.gt(locator.getAnchor()));

            query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.OWNER_ID.eq(ownerId));
            query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.ATTACHMENT_TYPE.eq(ServiceAllianceAttachmentType.FILE_ATTACHMENT.getCode()));

            query.addOrderBy(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.ID.asc());
            query.addLimit(count - attachments.size());

            query.fetch().map((r) -> {
                attachments.add(ConvertHelper.convert(r, ServiceAllianceAttachment.class));
                return null;
            });

            return AfterAction.next;
        });

        return attachments;
	}


	/**
	 * by dengs, 20170525，查询在idList中的服务联盟
	 */
	@Override
	public List<ServiceAlliances> listServiceAllianceSortOrders(List<Long> idList) {
		Long timestart = System.currentTimeMillis();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhServiceAlliancesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCES);
		query.addConditions(Tables.EH_SERVICE_ALLIANCES.ID.in(idList));
		LOGGER.debug("Query organization, sql={}, values ={}",query.getSQL(),query.getBindValues());
		List<ServiceAlliances>  serviceAllianceList = query.fetch().map(r->ConvertHelper.convert(r, ServiceAlliances.class));
		Long timeend = System.currentTimeMillis();
		LOGGER.debug("listServiceAllianceSortOrders , time = {}ms", timeend-timestart);
		return serviceAllianceList;
	}


	/**
	 * 更新defaultorder
	 */
	@Override
	public void updateOrderServiceAllianceDefaultOrder(List<ServiceAlliances> ServiceAllianceList) {
		List<Query> queryList = new ArrayList<Query>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		for (ServiceAlliances serviceAlliances : ServiceAllianceList) {
			Query query = context.update(Tables.EH_SERVICE_ALLIANCES)
					.set(Tables.EH_SERVICE_ALLIANCES.DEFAULT_ORDER, serviceAlliances.getDefaultOrder())
					.where(Tables.EH_SERVICE_ALLIANCES.ID.eq(serviceAlliances.getId()));
			queryList.add(query);
			LOGGER.debug("update serviceAlliance default order, sql = {}, values = {}",query.getSQL(),query.getBindValues());
		}
		Long timestart = System.currentTimeMillis();
		dbProvider.execute(status->{
			return context.batch(queryList).execute();
		});
		Long timeend = System.currentTimeMillis();
		LOGGER.debug("updateOrderServiceAllianceDefaultOrder , time = {}ms", timeend-timestart);
	}


	@Override
	public void updateServiceAlliancesDisplayFlag(Long id, Byte displayFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateConditionStep<EhServiceAlliancesRecord> updatesql = context.update(Tables.EH_SERVICE_ALLIANCES).set(Tables.EH_SERVICE_ALLIANCES.DISPLAY_FLAG, displayFlag).where(Tables.EH_SERVICE_ALLIANCES.ID.eq(id));
		LOGGER.debug("update showFlag, sql = {}, values = {}",updatesql.getSQL(),updatesql.getBindValues());
		updatesql.execute();
	}


	@Override
	public ServiceAllianceCategories findCategoryByEntryId(Integer namespaceId, Integer EntryId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceAllianceCategories.class));
        SelectQuery<EhServiceAllianceCategoriesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_CATEGORIES);
        if(null != namespaceId) 
        	query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        if(null != EntryId)
        	query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ENTRY_ID.eq(EntryId));
        
        return ConvertHelper.convert(query.fetchOne(), ServiceAllianceCategories.class);
	}


	@Override
	public List<Integer> listAscEntryIds(int namespaceId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceAllianceCategories.class));
        SelectQuery<EhServiceAllianceCategoriesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_CATEGORIES);
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.eq(0L));
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ENTRY_ID.isNotNull());
        query.addOrderBy(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ENTRY_ID.asc());
        
        return query.fetch().map(r->r.getEntryId());
	
	}
	
	@Override
	public void updateEntryIdNullByNamespaceId(Integer namespaceId) {
		dbProvider.getDslContext(AccessSpec.readWrite())
		.update(Tables.EH_SERVICE_ALLIANCE_CATEGORIES)
		.set(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ENTRY_ID,(Integer)null)
		.where(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
		.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.eq(0L))
		.execute();
	}
	
	@Override
	public List<ServiceAlliances> findOldFormServiceAlliance() {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhServiceAlliances.class));
        SelectQuery<EhServiceAlliancesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCES);
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.INTEGRAL_TAG1.eq(1L));
        query.addOrderBy(Tables.EH_SERVICE_ALLIANCES.ID.asc());
        
        return query.fetch().map(r->ConvertHelper.convert(r, ServiceAlliances.class));
	
	}

	/**
	 * 如果有时间时，表示是policydeclare类型
	 * 需要把defaultOrder组装成 yyyyMMddHHmmss(ms)
	 * 比如：20180615102559111
	 * 最后三位是毫秒
	 * @param sa
	 * @return
	 */
	private Long getDateDefaultOrder(ServiceAlliances sa) {
		long timeMillis = System.currentTimeMillis();
		long ms = timeMillis % 1000; // 获得毫秒
		long ms1 = ms/100;
		long ms2 = (ms % 100) / 10;
		long ms3 = (ms % 10);
		
		String timeHeadStr = DateUtil.dateToStr(sa.getEndTime(), "yyyyMMdd");
		String timeMidStr = DateUtil.dateToStr(new Timestamp(timeMillis), "HHmmss");
		return -Long.parseLong(timeHeadStr + timeMidStr + ms1 + ms2 + ms3);
	}


	@Override
	public List<ServiceAndTypeNameDTO> listServiceNames(Long type, Long ownerId, Long categoryId) {

		Condition condition = SA_TABLE.PARENT_ID.eq(type).and(SA_TABLE.PARENT_ID.ne(0L));
		if (null != ownerId) {
			condition = condition.and(SA_TABLE.OWNER_ID.eq(ownerId));
		}

		if (null != categoryId) {
			condition = condition.and(SA_TABLE.CATEGORY_ID.eq(categoryId));
		}

		condition = condition.and(SA_TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));

		return readOnlyContext()
		.select(SA_TABLE.ID, SA_TABLE.NAME, SA_TABLE.CATEGORY_ID, SA_TABLE.SERVICE_TYPE)
		.from(SA_TABLE)
		.where(condition)
		.fetch()
		.map(r->{
			ServiceAndTypeNameDTO dto = new ServiceAndTypeNameDTO();
			dto.setId(r.getValue(SA_TABLE.ID));
			dto.setName(r.getValue(SA_TABLE.NAME));
			dto.setServiceTypeId(r.getValue(SA_TABLE.CATEGORY_ID));
			dto.setServiceTypeName(r.getValue(SA_TABLE.SERVICE_TYPE));
			return dto;
		});
	}

	@Override
	public List<IdNameInfoDTO> listServiceTypeNames(Long type) {

		return readOnlyContext()
		.select(SA_TYPE_TABLE.ID, SA_TYPE_TABLE.NAME)
		.from(SA_TYPE_TABLE)
		.where(
				(SA_TYPE_TABLE.PARENT_ID.eq(type)
				.or(SA_TYPE_TABLE.ID.eq(type)))
				.and(SA_TYPE_TABLE.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode())))
		.fetch()
		.map(r->{
			IdNameInfoDTO dto = new IdNameInfoDTO();
			dto.setId(r.getValue(SA_TYPE_TABLE.ID));
			dto.setName(r.getValue(SA_TYPE_TABLE.NAME));
			return dto;
		});
	}

	private DSLContext readOnlyContext() {
		return dbProvider.getDslContext(AccessSpec.readOnly());
	}



	@Override
	public ServiceAllianceCategories findMainCategory(String ownerType, Long ownerId, Long type) {
		List<ServiceAllianceCategories> cags = listCategories(null, null, ownerType, ownerId,
				UserContext.getCurrentNamespaceId(), 0L, type, null,false);
		if (CollectionUtils.isEmpty(cags)) {
			return null;
		}
		return cags.get(0);
	}

	@Override
	public List<ServiceAllianceAttachment> listAttachments(String ownerType, Long ownerId, Byte attachmentType) {
		com.everhomes.server.schema.tables.EhServiceAllianceAttachments ATTACH = Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS;
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		 SelectQuery<EhServiceAllianceAttachmentsRecord> query  = context.selectQuery(ATTACH);
		 
		 if (!StringUtils.isEmpty(ownerType)) {
			 query.addConditions(ATTACH.OWNER_TYPE.eq(ownerType));
		 }
		 
		 if (null != ownerId) {
			 query.addConditions(ATTACH.OWNER_ID.eq(ownerId));
		 }

		 if (null != attachmentType) {
			 query.addConditions(ATTACH.ATTACHMENT_TYPE.eq(attachmentType));
		 }

		return query.fetchInto(ServiceAllianceAttachment.class);
	}


	@Override
	public void deleteProjectMainConfig(Long projectId, Long type) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_SERVICE_ALLIANCES)
				.where(
						Tables.EH_SERVICE_ALLIANCES.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode())
						.and(Tables.EH_SERVICE_ALLIANCES.OWNER_ID.eq(projectId))
						.and(Tables.EH_SERVICE_ALLIANCES.TYPE.eq(type))
						)
				.execute();
	}


	@Override
	public void deleteProjectCategories(Long projectId, Long type) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhServiceAllianceCategories CAG = Tables.EH_SERVICE_ALLIANCE_CATEGORIES;
		context.delete(CAG)
				.where(
						CAG.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode())
						.and(CAG.OWNER_ID.eq(projectId))
						.and(CAG.TYPE.eq(type))
						)
				.execute();
	}


	@Override
	public List<ServiceAllianceCategories> listChildCategories(Long parentId) {
		return listCategories(null, null, null, null, UserContext.getCurrentNamespaceId(), parentId, null, null, false);
	}



	@Override
	public Map<Long, Long> getServiceTypeOrders(List<Long> idList) {
		return readOnlyContext()
		.select(SA_TYPE_TABLE.ID, SA_TYPE_TABLE.DEFAULT_ORDER)
		.from(SA_TYPE_TABLE)
		.where(
				SA_TYPE_TABLE.ID.in(idList)
				.and(SA_TYPE_TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()))
				)
		.fetch()
		.intoMap(SA_TYPE_TABLE.ID, SA_TYPE_TABLE.DEFAULT_ORDER);
	}

	private DSLContext readWriteContext() {
		return dbProvider.getDslContext(AccessSpec.readWrite());
	}
	@Override
	public void updateServiceTypeOrders(Long id, Long order) {
		UpdateQuery<EhServiceAllianceCategoriesRecord> updateQuery = readWriteContext().updateQuery(SA_TYPE_TABLE);
		updateQuery.addValue(SA_TYPE_TABLE.DEFAULT_ORDER, order);
		updateQuery.addConditions(SA_TYPE_TABLE.ID.eq(id));
		updateQuery.addConditions(SA_TYPE_TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
		updateQuery.execute();
	}


	@Override
	public void updateMainCategorysByType(Long type, Byte enableComment, Byte enableProvider, String name) {
		UpdateQuery<EhServiceAllianceCategoriesRecord> updateQuery = readWriteContext().updateQuery(SA_TYPE_TABLE);
		updateQuery.addValue(SA_TYPE_TABLE.NAME, name);
		updateQuery.addValue(SA_TYPE_TABLE.PATH, name);
		updateQuery.addValue(SA_TYPE_TABLE.ENABLE_COMMENT, enableComment);
		updateQuery.addValue(SA_TYPE_TABLE.ENABLE_PROVIDER, enableProvider);
		updateQuery.addConditions(SA_TYPE_TABLE.TYPE.eq(type));
		updateQuery.addConditions(SA_TYPE_TABLE.PARENT_ID.eq(0L));
		updateQuery.addConditions(SA_TYPE_TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
		updateQuery.execute();
	}


	@Override
	public List<IdNameInfoDTO> listServiceTypeNames(String ownerType, Long ownerId, Long type) {
		return readOnlyContext()
		.select(SA_TYPE_TABLE.ID, SA_TYPE_TABLE.NAME, SA_TYPE_TABLE.PARENT_ID)
		.from(SA_TYPE_TABLE)
		.where(
				 SA_TYPE_TABLE.OWNER_ID.eq(ownerId)
				.and(SA_TYPE_TABLE.OWNER_TYPE.eq(ownerType))
				.and(SA_TYPE_TABLE.TYPE.eq(type))
				.and(SA_TYPE_TABLE.STATUS.eq(CategoryAdminStatus.ACTIVE.getCode())))
		.fetch()
		.map(r->{
			IdNameInfoDTO dto = new IdNameInfoDTO();
			dto.setId(r.getValue(SA_TYPE_TABLE.ID));
			dto.setName(r.getValue(SA_TYPE_TABLE.NAME));
			dto.setParentId(r.getValue(SA_TYPE_TABLE.PARENT_ID));
			return dto;
		});
	}

}
