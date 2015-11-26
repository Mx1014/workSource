package com.everhomes.techpark.expansion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseOpRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.records.EhEnterpriseOpRequestsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class EnterpriseApplyEntryProviderImpl implements
		EnterpriseApplyEntryProvider {
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<EnterpriseDetail> listEnterpriseDetails(Long communityId,String buildingName,
			int offset, int pageSize) {
		List<EnterpriseDetail> enterpriseDetails = new ArrayList<EnterpriseDetail>();
		
		 this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
	               (DSLContext context, Object reducingContext) -> {
	            Condition cond = Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId);
	           	if(!StringUtils.isEmpty(buildingName)){
	           		cond = cond.and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName));
	            }
	            context.select().from(Tables.EH_ENTERPRISE_ADDRESSES)
	            	.leftOuterJoin(Tables.EH_ADDRESSES)
	            	.on(Tables.EH_ENTERPRISE_ADDRESSES.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID))
	            	.leftOuterJoin(Tables.EH_GROUPS)
	            	.on(Tables.EH_GROUPS.ID.eq(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID))
	            	.leftOuterJoin(Tables.EH_ENTERPRISE_DETAILS)
	            	.on(Tables.EH_GROUPS.ID.eq(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID))
	                .where(cond)
	                .groupBy(Tables.EH_GROUPS.ID)
	                .limit(offset, pageSize)
	                .fetch().map((r) -> {
	                	EnterpriseDetail enterpriseDetail = ConvertHelper.convert(r, EnterpriseDetail.class);
	                	enterpriseDetail.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
	                	enterpriseDetail.setId(r.getValue(Tables.EH_GROUPS.ID));
	                	enterpriseDetail.setEnterpriseName(r.getValue(Tables.EH_GROUPS.NAME));
	                	String description =  r.getValue(Tables.EH_ENTERPRISE_DETAILS.DESCRIPTION);
	                	enterpriseDetail.setDescription(StringUtils.isEmpty(description) ? r.getValue(Tables.EH_GROUPS.DESCRIPTION) : description);
	                	String contact =  r.getValue(Tables.EH_ENTERPRISE_DETAILS.CONTACT);
	                	enterpriseDetail.setContact(StringUtils.isEmpty(contact) ? r.getValue(Tables.EH_GROUPS.STRING_TAG1) : contact);
	                	String address = r.getValue(Tables.EH_ENTERPRISE_DETAILS.ADDRESS);
	                	enterpriseDetail.setDescription(StringUtils.isEmpty(address) ? r.getValue(Tables.EH_GROUPS.STRING_TAG2) : address);
	                	
	                	enterpriseDetails.add(enterpriseDetail);
	        			return null;
	        		});
	            return true;
	        });
		 
		
		 
		return enterpriseDetails;
	}

	@Override
	public EnterpriseDetail getEnterpriseDetailById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record r= context.select().from(Tables.EH_GROUPS).leftOuterJoin(Tables.EH_ENTERPRISE_DETAILS)
		.on(Tables.EH_GROUPS.ID.eq(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID))
		.where(Tables.EH_GROUPS.ID.eq(id)).fetchOne();
		EnterpriseDetail enterpriseDetail = ConvertHelper.convert(r, EnterpriseDetail.class);
		enterpriseDetail.setId(r.getValue(Tables.EH_GROUPS.ID));
    	enterpriseDetail.setEnterpriseName(r.getValue(Tables.EH_GROUPS.NAME));
		String description =  r.getValue(Tables.EH_ENTERPRISE_DETAILS.DESCRIPTION);
    	enterpriseDetail.setDescription(StringUtils.isEmpty(description) ? r.getValue(Tables.EH_GROUPS.DESCRIPTION) : description);
    	String contact =  r.getValue(Tables.EH_ENTERPRISE_DETAILS.CONTACT);
    	enterpriseDetail.setContact(StringUtils.isEmpty(contact) ? r.getValue(Tables.EH_GROUPS.STRING_TAG1) : contact);
    	String address = r.getValue(Tables.EH_ENTERPRISE_DETAILS.ADDRESS);
    	enterpriseDetail.setDescription(StringUtils.isEmpty(address) ? r.getValue(Tables.EH_GROUPS.STRING_TAG2) : address);
		return enterpriseDetail;
	}

	@Override
	public List<EnterpriseOpRequest> listApplyEntrys(Long communityId,
			int offset, int pageSize) {
		List<EnterpriseOpRequest> enterpriseOpRequests = new ArrayList<EnterpriseOpRequest>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseOpRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.COMMUNITY_ID.eq(communityId));
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			enterpriseOpRequests.add(ConvertHelper.convert(r, EnterpriseOpRequest.class));
			return null;
		});
		return enterpriseOpRequests;
	}

	@Override
	public boolean createApplyEntry(EnterpriseOpRequest enterpriseOpRequest) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhEnterpriseOpRequests.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EnterpriseOpRequest.class));
		enterpriseOpRequest.setId(id);
		enterpriseOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		EhEnterpriseOpRequestsDao dap = new EhEnterpriseOpRequestsDao(context.configuration());
		dap.insert(enterpriseOpRequest);
		return true;
	}

	@Override
	public EnterpriseOpRequest getEnterpriseOpRequestByBuildIdAndUserId(
			Long id, Long userId) {
		List<EnterpriseOpRequest> enterpriseOpRequests = new ArrayList<EnterpriseOpRequest>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseOpRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.APPLY_USER_ID.eq(userId));
		if(null != id)
			query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.eq(id));
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.STATUS.eq(ApplyEntryStatus.RESIDED_IN.getCode()));
		
		query.fetch().map((r) -> {
			enterpriseOpRequests.add(ConvertHelper.convert(r, EnterpriseOpRequest.class));
			return null;
		});
		
		if(0 == enterpriseOpRequests.size()){
			return null;
		}
		return enterpriseOpRequests.get(0);
	}
	
	@Override
	public List<LeasePromotion> listLeasePromotions(Long communityId,
			int offset, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<LeasePromotion> leasePromotions = new ArrayList<LeasePromotion>();
		context.select().from(Tables.EH_LEASE_PROMOTIONS)
						.where(Tables.EH_LEASE_PROMOTIONS.COMMUNITY_ID.eq(communityId))
						.limit(offset, pageSize)
						.fetch().map((r) -> {
							List<LeasePromotionAttachment> attachments = new ArrayList<LeasePromotionAttachment>();
							context.select().from(Tables.EH_LEASE_PROMOTION_ATTACHMENTS)
							.where(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.LEASE_ID.eq(r.getValue(Tables.EH_LEASE_PROMOTIONS.ID)))
							.fetch().map((t) -> {
								attachments.add(ConvertHelper.convert(r, LeasePromotionAttachment.class));
								return null;
							});
							
							LeasePromotion leasePromotion = ConvertHelper.convert(r, LeasePromotion.class);
							leasePromotion.setAttachments(attachments);
							leasePromotions.add(leasePromotion);
							return null;
						});
		return leasePromotions;
	}

}
