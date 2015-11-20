package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhYellowPageAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhYellowPagesDao;
import com.everhomes.server.schema.tables.pojos.EhYellowPageAttachments;
import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.server.schema.tables.records.EhYellowPageAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhYellowPagesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class YellowPageProviderImpl implements YellowPageProvider {
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
			int pageSize, String ownerType, Long ownerId, Long parentId) {
	        List<YellowPage> yellowPages = this.queryYellowPagesByOwnerId(locator, ownerId, pageSize, new ListingQueryBuilderCallback() {

	            @Override
	            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
	                    SelectQuery<? extends Record> query) {
	                query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_TYPE.eq(ownerType));
	                query.addConditions(Tables.EH_YELLOW_PAGES.OWNER_ID.eq(ownerId));
	                query.addConditions(Tables.EH_YELLOW_PAGES.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
	                if(null!=parentId)
	                	query.addConditions(Tables.EH_YELLOW_PAGES.PARENT_ID.eq(parentId));
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
}
