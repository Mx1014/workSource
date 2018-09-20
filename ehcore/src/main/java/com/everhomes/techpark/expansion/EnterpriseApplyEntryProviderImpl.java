package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSONArray;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseOpRequestsDao;
import com.everhomes.server.schema.tables.daos.EhLeaseFormRequestsDao;
import com.everhomes.server.schema.tables.daos.EhLeasePromotionAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhLeasePromotionsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.pojos.EhLeaseFormRequests;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotionAttachments;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.*;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class EnterpriseApplyEntryProviderImpl implements EnterpriseApplyEntryProvider {
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public EnterpriseDetail getEnterpriseDetailById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		
		SelectQuery<EhEnterpriseDetailsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_DETAILS);
		query.addConditions(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID.eq(id));
		
		return ConvertHelper.convert(query.fetchOne(), EnterpriseDetail.class);
	}

	@Override
	public List<EnterpriseOpRequest> listApplyEntrys(EnterpriseOpRequest request,
			ListingLocator locator, Integer pageSize) {
		return listApplyEntrys(request, locator, pageSize, null);
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhEnterpriseOpRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.APPLY_USER_ID.eq(userId));
		if(null != id)
			query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.eq(id));
		//TODO:select status eq success
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
	public List<LeasePromotion> listLeasePromotions(LeasePromotion leasePromotion, ListingLocator locator, int pageSize) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());


		SelectJoinStep query = context.select(Tables.EH_LEASE_PROMOTIONS.fields()).from(Tables.EH_LEASE_PROMOTIONS);
		query.leftOuterJoin(Tables.EH_LEASE_PROMOTION_COMMUNITIES).on(Tables.EH_LEASE_PROMOTION_COMMUNITIES.LEASE_PROMOTION_ID
				.eq(Tables.EH_LEASE_PROMOTIONS.ID));

		Condition cond = Tables.EH_LEASE_PROMOTIONS.NAMESPACE_ID.eq(leasePromotion.getNamespaceId());
		cond = cond.and(Tables.EH_LEASE_PROMOTIONS.CATEGORY_ID.eq(leasePromotion.getCategoryId()));
		cond = cond.and(Tables.EH_LEASE_PROMOTIONS.STATUS.ne(LeasePromotionStatus.INACTIVE.getCode()));

		if(!StringUtils.isEmpty(leasePromotion.getSceneCommunityId())){

			cond = cond.and(Tables.EH_LEASE_PROMOTION_COMMUNITIES.COMMUNITY_ID.eq(leasePromotion.getSceneCommunityId()));
		}
		if(!StringUtils.isEmpty(leasePromotion.getCommunityId())){

			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.COMMUNITY_ID.eq(leasePromotion.getCommunityId()));
		}
		if(!StringUtils.isEmpty(leasePromotion.getRentType())){
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.RENT_TYPE.eq(leasePromotion.getRentType()));
		}
		if(!StringUtils.isEmpty(leasePromotion.getStatus())){
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.STATUS.eq(leasePromotion.getStatus()));
		}
		if(!StringUtils.isEmpty(leasePromotion.getBuildingId())){
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.BUILDING_ID.eq(leasePromotion.getBuildingId()));
		}
		if (!StringUtils.isEmpty(leasePromotion.getIssuerType())) {
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.ISSUER_TYPE.eq(leasePromotion.getIssuerType()));
		}
		if (null != leasePromotion.getStartRentArea()) {
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.RENT_AREAS.cast(BigDecimal.class).ge(leasePromotion.getStartRentArea()));
		}
		if (null != leasePromotion.getEndRentArea()) {
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.RENT_AREAS.cast(BigDecimal.class).le(leasePromotion.getEndRentArea()));
		}
		if (null != leasePromotion.getStartRentAmount()) {
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.RENT_AMOUNT.ge(leasePromotion.getStartRentAmount()));
		}
		if (null != leasePromotion.getEndRentAmount()) {
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.RENT_AMOUNT.le(leasePromotion.getEndRentAmount()));
		}
		if (null != leasePromotion.getCreateUid()) {
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.CREATE_UID.eq(leasePromotion.getCreateUid()));
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.ISSUER_TYPE.eq(LeaseIssuerType.NORMAL_USER.getCode()));
		}
		if (null != leasePromotion.getHouseResourceType()){
			Condition cond2 = null;
			List<String> houseResourceTypes = parseHouseResourceTypes(leasePromotion.getHouseResourceType());
			for (String s : houseResourceTypes)
				if (cond2 == null)
					cond2 = Tables.EH_LEASE_PROMOTIONS.HOUSE_RESOURCE_TYPE.like("%"+s+"%");
				else
					cond2 = cond2.and(Tables.EH_LEASE_PROMOTIONS.HOUSE_RESOURCE_TYPE.like("%"+s+"%"));
			if (cond2!=null)
				cond = cond.and(cond2);
		}

		if(null != locator.getAnchor()){
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.DEFAULT_ORDER.lt(locator.getAnchor()));
		}

		List<LeasePromotion> leasePromotions = query
				.where(cond)
				.groupBy(Tables.EH_LEASE_PROMOTIONS.ID)
				.orderBy(Tables.EH_LEASE_PROMOTIONS.DEFAULT_ORDER.desc())
				.limit(pageSize)
				.fetchInto(LeasePromotion.class);
		
		if(leasePromotions.size() >= pageSize) {
			locator.setAnchor(leasePromotions.get(leasePromotions.size() - 1).getDefaultOrder());
		}else {
			locator.setAnchor(null);
		}
		return leasePromotions;
	}

	private List<String> parseHouseResourceTypes(String jsonArray){
		JSONArray array = JSONArray.parseArray(jsonArray);
		List<String> result = new ArrayList<>();
		for (Object obj:array)
			result.add((String)obj);
		return result;
	}

	@Override
	public List<LeasePromotionAttachment> findAttachmentsByOwnerTypeAndOwnerId(String ownerType, Long ownerId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		
		List<LeasePromotionAttachment> attachments = context.select().from(Tables.EH_LEASE_PROMOTION_ATTACHMENTS)
		.where(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.OWNER_ID.eq(ownerId)
				.and(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.OWNER_TYPE.eq(ownerType)))
		.fetch().map((t) -> {
			return ConvertHelper.convert(t, LeasePromotionAttachment.class);
		});
		return attachments;
	}


	@Override
	public void createLeasePromotion(LeasePromotion leasePromotion) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhLeasePromotions.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeasePromotions.class));
		leasePromotion.setId(id);
		Long now = System.currentTimeMillis();
		leasePromotion.setCreateTime(new Timestamp(now));
		leasePromotion.setDefaultOrder(now);
		leasePromotion.setCreateUid(UserContext.currentUserId());
		leasePromotion.setStatus(LeasePromotionStatus.RENTING.getCode());

		EhLeasePromotionsDao dao = new EhLeasePromotionsDao(context.configuration());
		dao.insert(leasePromotion);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeasePromotions.class, null);
	}
	
	@Override
	public LeasePromotion getLeasePromotionById(Long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhLeasePromotionsDao dao = new EhLeasePromotionsDao(context.configuration());
		LeasePromotion leasePromotion = ConvertHelper.convert(dao.findById(id), LeasePromotion.class);

		return leasePromotion;
	}
	
	@Override
	public EnterpriseOpRequest getApplyEntryById(Long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnterpriseOpRequestsDao dao = new EhEnterpriseOpRequestsDao(context.configuration());
		EnterpriseOpRequest enterpriseOpRequest = ConvertHelper.convert(dao.findById(id), EnterpriseOpRequest.class);
		return enterpriseOpRequest;
	}
	
	@Override
	public boolean updateLeasePromotion(LeasePromotion leasePromotion){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhLeasePromotionsDao dao = new EhLeasePromotionsDao(context.configuration());
		leasePromotion.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		leasePromotion.setUpdateUid(UserContext.currentUserId());
		dao.update(leasePromotion);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLeasePromotions.class, null);
		return true;
	}
	
	@Override
	public boolean deleteLeasePromotionAttachment(String ownerTYpe, Long ownerId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		DeleteQuery<EhLeasePromotionAttachmentsRecord> deleteQuery = context.deleteQuery(Tables.EH_LEASE_PROMOTION_ATTACHMENTS);
		deleteQuery.addConditions(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.OWNER_TYPE.eq(ownerTYpe));
		deleteQuery.addConditions(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.OWNER_ID.eq(ownerId));
		deleteQuery.execute();
		return true;
	}
	
	@Override
	public boolean addPromotionAttachment(LeasePromotionAttachment attachment){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhLeasePromotionAttachments.class));
		attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		attachment.setId(id);
		EhLeasePromotionAttachmentsDao dao = new EhLeasePromotionAttachmentsDao(context.configuration());
		dao.insert(attachment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeasePromotionAttachments.class, null);
		return true;
	}
	
	@Override
	public boolean updateLeasePromotionStatus(long id, byte status){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhLeasePromotionsRecord> r = context.updateQuery(Tables.EH_LEASE_PROMOTIONS);
		r.addValue(Tables.EH_LEASE_PROMOTIONS.STATUS, status);
		r.addConditions(Tables.EH_LEASE_PROMOTIONS.ID.eq(id));
		r.execute();
		return true;
	}
	
	@Override
	public boolean updateApplyEntryStatus(long id, byte status){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterpriseOpRequestsRecord> r = context.updateQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		r.addValue(Tables.EH_ENTERPRISE_OP_REQUESTS.STATUS, status);
		r.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.eq(id));
		r.execute();
		return true;
	}

	@Override
	public void updateApplyEntry(EnterpriseOpRequest request){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnterpriseOpRequestsDao dao = new EhEnterpriseOpRequestsDao(context.configuration());
		dao.update(request);
	}
	
	@Override
	public boolean deleteLeasePromotion(long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhLeasePromotionsDao dao = new EhLeasePromotionsDao(context.configuration());
		dao.deleteById(id);
		return true;
	}

	@Override
	public void deleteLeasePromotionByUidAndIssuerType(long id, String issuerType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		DeleteQuery query = context.deleteQuery(Tables.EH_LEASE_PROMOTIONS);
		query.addConditions(Tables.EH_LEASE_PROMOTIONS.CREATE_UID.eq(id));
		query.addConditions(Tables.EH_LEASE_PROMOTIONS.ISSUER_TYPE.eq(issuerType));

		query.execute();
	}

	@Override
	public List<LeasePromotion> listLeasePromotionsByUidAndIssuerType(long id, String issuerType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectQuery query = context.selectQuery(Tables.EH_LEASE_PROMOTIONS);
		query.addConditions(Tables.EH_LEASE_PROMOTIONS.CREATE_UID.eq(id));
		query.addConditions(Tables.EH_LEASE_PROMOTIONS.ISSUER_TYPE.eq(issuerType));

		return query.fetch().map(r -> ConvertHelper.convert(r, LeasePromotion.class));
	}

	@Override
	public boolean deleteApplyEntry(long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnterpriseOpRequestsDao dao = new EhEnterpriseOpRequestsDao(context.configuration());
		dao.deleteById(id);
		return true;
	}

	@Override
	public LeasePromotion getLeasePromotionByToken(Integer namespaceId, Long communityId, String namespaceType, String namespaceToken) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Record record = context.select().from(Tables.EH_LEASE_PROMOTIONS).join(Tables.EH_LEASE_PROMOTION_COMMUNITIES)
						.on(Tables.EH_LEASE_PROMOTION_COMMUNITIES.LEASE_PROMOTION_ID.eq(Tables.EH_LEASE_PROMOTIONS.ID))
						.where(Tables.EH_LEASE_PROMOTIONS.NAMESPACE_ID.eq(namespaceId))
						.and(Tables.EH_LEASE_PROMOTION_COMMUNITIES.COMMUNITY_ID.eq(communityId))
						.and(Tables.EH_LEASE_PROMOTIONS.NAMESPACE_TYPE.eq(namespaceType))
						.and(Tables.EH_LEASE_PROMOTIONS.NAMESPACE_TOKEN.eq(namespaceToken))
						.fetchOne();
		
		if (record != null) {
			return ConvertHelper.convert(record, LeasePromotion.class);
		}
		
		return null;
	}

	@Override
	public List<EnterpriseOpRequest> listApplyEntrys(EnterpriseOpRequest request,
													 ListingLocator locator, Integer pageSize, List<Long> idList) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		Condition cond = Tables.EH_ENTERPRISE_OP_REQUESTS.NAMESPACE_ID.eq(request.getNamespaceId());
		cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.CATEGORY_ID.eq(request.getCategoryId()));

		SelectQuery<EhEnterpriseOpRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);

		if(!StringUtils.isEmpty(request.getCommunityId())){
			cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.COMMUNITY_ID.eq(request.getCommunityId()));
		}

		if (null != request.getLeaseIssuerId()) {
			cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_TYPE.eq(ApplyEntrySourceType.FOR_RENT.getCode()));

			query.addJoin(Tables.EH_LEASE_PROMOTIONS, Tables.EH_LEASE_PROMOTIONS.ID.eq(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_ID));
			cond = cond.and(Tables.EH_LEASE_PROMOTIONS.CREATE_UID.eq(request.getLeaseIssuerId()));
		}

		if(!StringUtils.isEmpty(request.getIssuerType())){
			cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.ISSUER_TYPE.eq(request.getIssuerType()));
		}

		if(!StringUtils.isEmpty(request.getStatus())){
			cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.STATUS.eq(request.getStatus()));
		}

		if(!StringUtils.isEmpty(request.getSourceType())){
			if (request.getSourceType().equals(ApplyEntrySourceType.LEASE_PROJECT.getCode())) {
				cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_TYPE.eq(ApplyEntrySourceType.LEASE_PROJECT.getCode())
					.or(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_TYPE.eq(ApplyEntrySourceType.BUILDING.getCode())));
			}else {
				cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_TYPE.eq(request.getSourceType()));
			}
		}

		if(null != idList && idList.size()>0){
			cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.in(idList));
		}

		if(null != locator.getAnchor()){
			cond = cond.and(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.le(locator.getAnchor()));
		}

		query.addConditions(cond);
		query.addOrderBy(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.desc());

		if (null != pageSize) {
			pageSize = pageSize + 1;
			query.addLimit(pageSize);
		}

		List<EnterpriseOpRequest> enterpriseOpRequests = query.fetch().map(new DefaultRecordMapper(Tables.EH_ENTERPRISE_OP_REQUESTS.recordType(), EnterpriseOpRequest.class));
		int listSize = enterpriseOpRequests.size();
		if (null != pageSize && listSize >= pageSize) {
			locator.setAnchor(enterpriseOpRequests.get(listSize - 1).getId());
			enterpriseOpRequests.remove(listSize - 1);
		} else {
			locator.setAnchor(null);
		}
		return enterpriseOpRequests;
	}

	@Override
	public void deleteApplyEntrysByLeasePromotionIds(List<Long> idList) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		DeleteQuery query = context.deleteQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);

		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.ISSUER_TYPE.eq(LeaseIssuerType.NORMAL_USER.getCode()));
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_TYPE.eq(ApplyEntrySourceType.FOR_RENT.getCode()));
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.in(idList));

		query.execute();
	}

	@Override
	public void createLeaseRequestForm(LeaseFormRequest leaseFormRequest) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhLeaseFormRequests.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseFormRequests.class));
		leaseFormRequest.setId(id);
		leaseFormRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhLeaseFormRequestsDao dap = new EhLeaseFormRequestsDao(context.configuration());
		dap.insert(leaseFormRequest);

	}

	@Override
	public void updateLeaseRequestForm(LeaseFormRequest leaseFormRequest) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseFormRequests.class));

		EhLeaseFormRequestsDao dap = new EhLeaseFormRequestsDao(context.configuration());
		dap.update(leaseFormRequest);

	}

	@Override
	public void deleteLeaseRequestForm(LeaseFormRequest leaseFormRequest) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseFormRequests.class));

		EhLeaseFormRequestsDao dap = new EhLeaseFormRequestsDao(context.configuration());
		dap.delete(leaseFormRequest);

	}

	@Override
	public LeaseFormRequest findLeaseRequestFormById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseFormRequests.class));

		EhLeaseFormRequestsDao dao = new EhLeaseFormRequestsDao(context.configuration());

		return ConvertHelper.convert(dao.findById(id), LeaseFormRequest.class);

	}

	@Override
	public LeaseFormRequest findLeaseRequestForm(Integer namespaceId, Long ownerId, String ownerType, String sourceType,
												 Long categoryId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseFormRequests.class));

		SelectQuery query = context.selectQuery(Tables.EH_LEASE_FORM_REQUESTS);
		query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.CATEGORY_ID.eq(categoryId));

		if (null != ownerId) {
			query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.OWNER_ID.eq(ownerId));
		}
		if (!StringUtils.isEmpty(ownerType)) {
			query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.OWNER_TYPE.eq(ownerType));
		}
		query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.SOURCE_TYPE.eq(sourceType));

		return ConvertHelper.convert(query.fetchAny(), LeaseFormRequest.class);

	}
	
	@Override
	public List<LeaseFormRequest> listLeaseRequestForm(Integer namespaceId, Long ownerId, String ownerType) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseFormRequests.class));

		SelectQuery<EhLeaseFormRequestsRecord> query = context.selectQuery(Tables.EH_LEASE_FORM_REQUESTS);
		query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_LEASE_FORM_REQUESTS.OWNER_TYPE.eq(ownerType));

		query.addOrderBy(Tables.EH_LEASE_FORM_REQUESTS.ID.asc());
		return query.fetch().map(r->ConvertHelper.convert(r, LeaseFormRequest.class));
	}

	@Override
	public void updateApplyEntryTransformFlag(Long applyEntryId, byte transformFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseOpRequests.class));
		UpdateQuery<EhEnterpriseOpRequestsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.eq(applyEntryId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_OP_REQUESTS.TRANSFORM_FLAG, transformFlag);
		updateQuery.execute();	
	}
}
