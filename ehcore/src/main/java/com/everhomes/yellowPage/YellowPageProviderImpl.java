package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.Category;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.rest.yellowPage.YellowPageType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseContactEntriesDao;
import com.everhomes.server.schema.tables.daos.EhServiceAllianceAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhServiceAllianceCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhServiceAlliancesDao;
import com.everhomes.server.schema.tables.daos.EhYellowPageAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhYellowPagesDao;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceAttachments;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.server.schema.tables.pojos.EhYellowPageAttachments;
import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.server.schema.tables.records.EhCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseContactEntriesRecord;
import com.everhomes.server.schema.tables.records.EhServiceAllianceAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhServiceAllianceCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhServiceAlliancesRecord;
import com.everhomes.server.schema.tables.records.EhYellowPageAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhYellowPagesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@Component
public class YellowPageProviderImpl implements YellowPageProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageProviderImpl.class);
	
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


	@Override
	public List<ServiceAlliances> queryServiceAlliance(
			CrossShardListingLocator locator, int pageSize, String ownerType,
			Long ownerId, Long parentId, Long categoryId, String keywords) {
		List<ServiceAlliances> saList = new ArrayList<ServiceAlliances>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhServiceAlliancesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCES);
 
        if (!StringUtils.isEmpty(ownerType) )
    		query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_TYPE.eq(ownerType));
        
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_ID.eq(ownerId));
        
        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_SERVICE_ALLIANCES.ID.gt(locator.getAnchor()));
            }
        
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
        
        if(!org.springframework.util.StringUtils.isEmpty(keywords)){
        	query.addConditions(Tables.EH_SERVICE_ALLIANCES.NAME.like("%" + keywords + "%"));
        }
        
        if(categoryId != null) {
        	query.addConditions(Tables.EH_SERVICE_ALLIANCES.CATEGORY_ID.eq(categoryId));
        }
        
        if(null!=parentId){
        	query.addConditions(Tables.EH_SERVICE_ALLIANCES.PARENT_ID.eq(parentId));
        } else {
    		query.addConditions(Tables.EH_SERVICE_ALLIANCES.PARENT_ID.ne(0L));
		}
        query.addLimit(pageSize);
       
        query.fetch().map((r) -> {
        	saList.add(ConvertHelper.convert(r, ServiceAlliances.class));
            return null;
        });
        
        if(saList != null && saList.size() > 0) {
            return saList;
        }
        return saList;
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
		 
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceCategoriesDao.class));
		category.setId(id);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		category.setCreatorUid(UserContext.current().getUser().getId());
	        
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


	@Override
	public void createServiceAlliances(ServiceAlliances sa) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAlliances.class));
        sa.setId(id);
        if(sa.getStatus() == null) {
            sa.setStatus(YellowPageStatus.ACTIVE.getCode());    
        }
        sa.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhServiceAlliancesDao dao = new EhServiceAlliancesDao(context.configuration());
        dao.insert(sa);
		
	}


	@Override
	public void updateServiceAlliances(ServiceAlliances sa) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAlliancesDao dao = new EhServiceAlliancesDao(context.configuration());
        dao.update(sa);
	}


	@Override
	public void createServiceAllianceAttachments(
			ServiceAllianceAttachment attachment) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
 		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceAttachments.class));
        
 		attachment.setId(id); 
 		attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
 		EhServiceAllianceAttachmentsDao dao = new EhServiceAllianceAttachmentsDao(context.configuration());
 		dao.insert(attachment);
	}


	@Override
	public void deleteServiceAllianceAttachmentsByOwnerId(Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		 
        SelectQuery<EhServiceAllianceAttachmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS);
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
	public ServiceAlliances queryServiceAllianceTopic(String ownerType,
			Long ownerId, Long type) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
        List<ServiceAlliances> saList = new ArrayList<ServiceAlliances>();
        
        SelectQuery<EhServiceAlliancesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCES);
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_ID.eq(ownerId));

    	if (!StringUtils.isEmpty(ownerType) )
    		query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.OWNER_ID.eq(ownerId));
        //topic
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.PARENT_ID.eq(0L));
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.TYPE.eq(type));
        query.addConditions(Tables.EH_SERVICE_ALLIANCES.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
        
        query.fetch().map((r) -> {
        	saList.add(ConvertHelper.convert(r, ServiceAlliances.class));
            return null;
        });
        
        
        if(saList != null && saList.size() > 0) {
            return saList.get(0);
        }
        return null;
	}


	@Override
	public ServiceAlliances findServiceAllianceById(Long id, String ownerType,
			Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhServiceAlliancesDao dao = new EhServiceAlliancesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ServiceAlliances.class);
	}


	@Override
	public void populateServiceAlliancesAttachment(ServiceAlliances sa) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhServiceAllianceAttachmentsRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS);
        query.addConditions(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.OWNER_ID.in(sa.getId()));
        query.fetch().map((EhServiceAllianceAttachmentsRecord record) -> {
        	 sa.getAttachments().add(ConvertHelper.convert(record, ServiceAllianceAttachment.class));
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
	public List<ServiceAllianceCategories> listChildCategories(
			Integer namespaceId, Long parentId, CategoryAdminStatus status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        List<ServiceAllianceCategories> result = new ArrayList<ServiceAllianceCategories>();
        
        SelectQuery<EhServiceAllianceCategoriesRecord> query = context.selectQuery(Tables.EH_SERVICE_ALLIANCE_CATEGORIES);
        Condition condition = null;
        
        if(parentId != null)
            condition = Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.eq(parentId.longValue());
        else
            condition = Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.isNull().or(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.PARENT_ID.eq(0L));
            
        if(status != null)
            condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.STATUS.eq(status.getCode()));

        condition = condition.and(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        if(condition != null) {
        	query.addConditions(condition);
        }
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query child categories, namespaceId=" + namespaceId + ", parentId=" + parentId + ", status=" + status);
        }

        query.fetch().map((EhServiceAllianceCategoriesRecord record) -> {
        	result.add(ConvertHelper.convert(record, ServiceAllianceCategories.class));
            return null;
        });
        
        return result;
	}
}
