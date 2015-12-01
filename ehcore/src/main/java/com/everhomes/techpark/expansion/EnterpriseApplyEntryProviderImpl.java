package com.everhomes.techpark.expansion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseOpRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.records.EhEnterpriseAddressesRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseDetailsRecord;
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
	
	public List<EnterpriseAddress> listBuildingEnterprisesByBuildingName(String buildingName, ListingLocator locator, int pageSize){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseAddressesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_ADDRESSES);
		query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.BUILDING_NAME.eq(buildingName));
		query.addGroupBy(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID);
		if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ENTERPRISE_ADDRESSES.ID.gt(locator.getAnchor()));
        }
		query.addLimit(pageSize);
		List<EnterpriseAddress> enterpriseAddresses = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, EnterpriseAddress.class);
		});
		
		if(enterpriseAddresses.size() >= pageSize) {
	            locator.setAnchor(enterpriseAddresses.get(enterpriseAddresses.size() - 1).getId());
	    }
		return enterpriseAddresses;
	}

	@Override
	public EnterpriseDetail getEnterpriseDetailById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		SelectQuery<EhEnterpriseDetailsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_DETAILS);
		query.addConditions(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID.eq(id));
		
		return ConvertHelper.convert(query.fetchOne(), EnterpriseDetail.class);
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
								attachments.add(ConvertHelper.convert(t, LeasePromotionAttachment.class));
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
