// @formatter:off
package com.everhomes.relocation;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.relocation.RelocationCommonStatus;
import com.everhomes.rest.relocation.RelocationRequestStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class RelocationProviderImpl implements RelocationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelocationProviderImpl.class);


	@Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public RelocationRequest findRelocationRequestById(Long id) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhRelocationRequests.class));
		EhRelocationRequestsDao dao = new EhRelocationRequestsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), RelocationRequest.class);
    }
    
    @Override
    public void createRelocationRequest(RelocationRequest request){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRelocationRequests.class));
		request.setId(id);
    	request.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	request.setCreatorUid(UserContext.currentUserId());
		request.setRequestorUid(UserContext.currentUserId());
		request.setStatus(RelocationRequestStatus.PROCESSING.getCode());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRelocationRequestsDao dao = new EhRelocationRequestsDao(context.configuration());
		dao.insert(request);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRelocationRequests.class, null);
    	
    }

	@Override
	public void updateRelocationRequest(RelocationRequest request){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRelocationRequestsDao dao = new EhRelocationRequestsDao(context.configuration());
		dao.update(request);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRelocationRequests.class, null);

	}

    @Override
    public List<RelocationRequest> searchRelocationRequests(Integer namespaceId, String ownerType, Long ownerId,
		String keyword, Long startDate, Long endDate, Byte status, Long orgId, Long pageAnchor, Integer pageSize) {
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
        SelectQuery<EhRelocationRequestsRecord> query = context.selectQuery(Tables.EH_RELOCATION_REQUESTS);
        
        query.addConditions(Tables.EH_RELOCATION_REQUESTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_RELOCATION_REQUESTS.FLOW_CASE_ID.gt(0l));//抛弃创建工作流失败的
        if (null != pageAnchor && pageAnchor != 0) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		}
		if (null != ownerId) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.OWNER_ID.eq(ownerId));
		}
		if (StringUtils.isNotBlank(ownerType)) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.OWNER_TYPE.eq(ownerType));
		}
		if (null != orgId) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.REQUESTOR_ENTERPRISE_ID.eq(orgId));
		}
        if(StringUtils.isNotBlank(keyword)) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.REQUESTOR_ENTERPRISE_NAME.like("%" + keyword + "%")
					.or(Tables.EH_RELOCATION_REQUESTS.REQUESTOR_NAME.like("%" + keyword + "%"))
					.or(Tables.EH_RELOCATION_REQUESTS.CONTACT_PHONE.like("%" + keyword + "%"))
					.or(Tables.EH_RELOCATION_REQUESTS.REQUEST_NO.like("%" + keyword + "%")));
		}
        if(null != startDate) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.gt(new Timestamp(startDate)));
		}
        if(null != endDate) {
			query.addConditions(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.lt(new Timestamp(endDate)));
		}
        if (null != status) {
            query.addConditions(Tables.EH_RELOCATION_REQUESTS.STATUS.eq(status));
        }

        query.addOrderBy(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.desc());
		query.addOrderBy(Tables.EH_RELOCATION_REQUESTS.STATUS.desc());
        if(null != pageSize) {
			query.addLimit(pageSize);
		}

    	return query.fetch().map(r -> ConvertHelper.convert(r, RelocationRequest.class));
    }

	@Override
	public List<RelocationStatistics> queryRelocationStatistics(Integer namespaceId, String ownerType, Long ownerId, Long startDate, Long endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhParkingRechargeOrders.class));
		SelectJoinStep<Record2<Integer, Byte>> step = context.select(DSL.count().as("count"), Tables.EH_RELOCATION_REQUESTS.STATUS)
				.from(Tables.EH_RELOCATION_REQUESTS);
		Condition condition = Tables.EH_RELOCATION_REQUESTS.NAMESPACE_ID.eq(namespaceId);
		if (StringHelper.hasContent(ownerType) && null != ownerId)
			condition = condition.and(Tables.EH_RELOCATION_REQUESTS.OWNER_TYPE.eq(ownerType))
					.and(Tables.EH_RELOCATION_REQUESTS.OWNER_ID.eq(ownerId));
		if (null != startDate)
			condition = condition.and(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.gt(new Timestamp(startDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_RELOCATION_REQUESTS.CREATE_TIME.lt(new Timestamp(endDate)));
		return step.where(condition).groupBy(Tables.EH_RELOCATION_REQUESTS.STATUS)
				.fetch().map(r -> {
					RelocationStatistics statistics = new RelocationStatistics();
					statistics.setCount(r.value1());
					statistics.setStatus(r.value2());
					return statistics;
				});
	}

	@Override
	public void createRelocationRequestItem(RelocationRequestItem item){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRelocationRequestItems.class));
		item.setId(id);
		item.setCreateTime(new Timestamp(System.currentTimeMillis()));
		item.setCreatorUid(UserContext.currentUserId());
		item.setStatus(RelocationCommonStatus.ACTIVE.getCode());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRelocationRequestItemsDao dao = new EhRelocationRequestItemsDao(context.configuration());
		dao.insert(item);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRelocationRequestItems.class, null);

	}

	@Override
	public void updateRelocationRequestItemsStatus(Integer namespaceId, Long requestId, Byte status){


		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		UpdateSetMoreStep<EhRelocationRequestItemsRecord> updateQuery = context.update(Tables.EH_RELOCATION_REQUEST_ITEMS).set(Tables.EH_RELOCATION_REQUEST_ITEMS.STATUS, status);

		updateQuery.where(Tables.EH_RELOCATION_REQUEST_ITEMS.NAMESPACE_ID.eq(namespaceId)
			.and(Tables.EH_RELOCATION_REQUEST_ITEMS.REQUEST_ID.eq(requestId))).execute();

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRelocationRequestItems.class, null);

	}

	@Override
	public List<RelocationRequestItem> listRelocationRequestItems(Long requestId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(RelocationRequestItem.class));
		SelectQuery<EhRelocationRequestItemsRecord> query = context.selectQuery(Tables.EH_RELOCATION_REQUEST_ITEMS);

		query.addConditions(Tables.EH_RELOCATION_REQUEST_ITEMS.REQUEST_ID.eq(requestId));

		query.addConditions(Tables.EH_RELOCATION_REQUEST_ITEMS.STATUS.eq(RelocationCommonStatus.ACTIVE.getCode()));

		return query.fetch().map(r -> ConvertHelper.convert(r, RelocationRequestItem.class));
	}

	@Override
	public void createRelocationRequestAttachment(RelocationRequestAttachment attachment){

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRelocationRequestAttachments.class));
		attachment.setId(id);
		attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		attachment.setCreatorUid(UserContext.currentUserId());
		attachment.setStatus(RelocationCommonStatus.ACTIVE.getCode());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRelocationRequestAttachmentsDao dao = new EhRelocationRequestAttachmentsDao(context.configuration());
		dao.insert(attachment);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRelocationRequestAttachments.class, null);

	}

	@Override
	public List<RelocationRequestAttachment> listRelocationRequestAttachments(String ownerType, Long ownerId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhRelocationRequestAttachments.class));
		SelectQuery<EhRelocationRequestAttachmentsRecord> query = context.selectQuery(Tables.EH_RELOCATION_REQUEST_ATTACHMENTS);

		query.addConditions(Tables.EH_RELOCATION_REQUEST_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_RELOCATION_REQUEST_ATTACHMENTS.OWNER_ID.eq(ownerId));

		query.addConditions(Tables.EH_RELOCATION_REQUEST_ATTACHMENTS.STATUS.eq(RelocationCommonStatus.ACTIVE.getCode()));

		return query.fetch().map(r -> ConvertHelper.convert(r, RelocationRequestAttachment.class));
	}
}
