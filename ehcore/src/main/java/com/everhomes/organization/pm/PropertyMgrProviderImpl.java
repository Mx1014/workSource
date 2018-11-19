// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.address.AddressProperties;
import com.everhomes.community.Community;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.ContractTemplate;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationTask;
import com.everhomes.rest.contract.ContractTemplateStatus;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhContractTemplates;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class PropertyMgrProviderImpl implements PropertyMgrProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrProviderImpl.class);
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;


	// ??? How to set cache if there is more than one parameters? 
	//@Cacheable(value="Region", key="#regionId")

	@Override
	public List<CommunityPmMember> listUserCommunityPmMembers(Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmMember> result  = new ArrayList<CommunityPmMember>();
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS); 
		if(userId != null)
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId));
		query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmMember.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmMember> findPmMemberByTargetTypeAndId(String targetType, long targetId) {
		final List<CommunityPmMember> groups = new ArrayList<CommunityPmMember>();

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(targetType));
		query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(targetId));

		query.fetch().map((r) -> {
			groups.add(ConvertHelper.convert(r, CommunityPmMember.class));
			return null;
		});

		return groups;
	}

	@Override
	public List<CommunityPmMember> findPmMemberByCommunityAndTarget(long organizationId, String targetType, long targetId) {
		final List<CommunityPmMember> groups = new ArrayList<CommunityPmMember>();

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(targetType));
		query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(targetId));

		query.fetch().map((r) -> {
			groups.add(ConvertHelper.convert(r, CommunityPmMember.class));
			return null;
		});

		return groups;
	}

	@CacheEvict(value="CommunityPmMember", key="#communityPmMember.id")
	//@Cache(value = "CommunityPmMember", key="#communityPmMember.id")
	@Override
	public void createPropMember(CommunityPmMember communityPmMember) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.insert(communityPmMember);

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationMembers.class, null);
	}

	@CacheEvict(value="CommunityPmMember", key="#communityPmMember.id")
	@Override
	public void updatePropMember(CommunityPmMember communityPmMember){
		assert(communityPmMember.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.update(communityPmMember);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, communityPmMember.getId());
	}

	@CacheEvict(value="CommunityPmMember", key="#communityPmMember.id")
	@Override
	public void deletePropMember(CommunityPmMember communityPmMember){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.deleteById(communityPmMember.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, communityPmMember.getId());
	}

	//@CacheEvict(value="CommunityPmMember", key="#id")
	@CacheEvict(value="CommunityPmMember", key="#id")
	@Override
	public void deletePropMember(long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, id);
	}

	@Cacheable(value="CommunityPmMember", key="#id")
	@Override
	public CommunityPmMember findPropMemberById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityPmMember.class);
	}

	//@Cacheable(value = "CommunityPmMemberList", key="#communityId")
	@Override
	public List<CommunityPmMember> listCommunityPmMembers(Long organizationId, String contactToken,Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		List<CommunityPmMember> result  = new ArrayList<CommunityPmMember>();
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		if(organizationId != null)
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));

		if(contactToken != null && !"".equals(contactToken)) {
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactToken));
		}

		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.asc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmMember.class));
			return null;
		});
		return result;
	}

	//@Cacheable(value = "CommunityPmMemberList", key="#communityId")
	@Override
	public List<CommunityPmMember> listCommunityPmMembers(Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		List<CommunityPmMember> result  = new ArrayList<CommunityPmMember>();
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		if(communityId != null)
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(communityId));
		query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmMember.class));
			return null;
		});
		return result;
	}

	@Override
	public int countCommunityPmMembers(long orgId, String contactToken) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS);
		Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId);
		if(contactToken != null && !"".equals(contactToken)) {
			condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactToken));
		}

		return step.where(condition).fetchOneInto(Integer.class);
	}

	@CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id")
	//@Cacheable(value = "CommunityAddressMapping", key="#communityAddressMapping.id")
	@Override
	public void createPropAddressMapping(CommunityAddressMapping communityAddressMapping) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhOrganizationAddressMappingsRecord record = ConvertHelper.convert(communityAddressMapping, EhOrganizationAddressMappingsRecord.class);
		InsertQuery<EhOrganizationAddressMappingsRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		query.setRecord(record);
		query.setReturning(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID);
		query.execute();

		communityAddressMapping.setId(query.getReturnedRecord().getId());

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationAddressMappingsRecord.class, null);
	}


	@Caching(evict = { @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id"), 
			@CacheEvict(value="CommunityAddressMappingByAddressId", key="{#communityAddressMapping.communityId, #communityAddressMapping.addressId}"),
			@CacheEvict(value = "CommunityAddressMappingsList", key="#communityAddressMapping.communityId")})
	@Override
	public void updatePropAddressMapping(CommunityAddressMapping communityAddressMapping){
		assert(communityAddressMapping.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
		dao.update(communityAddressMapping);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAddressMappings.class, communityAddressMapping.getId());
	}

	@Caching(evict = { @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id"), 
			@CacheEvict(value="CommunityAddressMappingByAddressId", key="{#communityAddressMapping.communityId,#communityAddressMapping.addressId}"),
			@CacheEvict(value = "CommunityAddressMappingsList", key="#communityAddressMapping.communityId")})
	@Override
	public void deletePropAddressMapping(CommunityAddressMapping communityAddressMapping){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
		dao.deleteById(communityAddressMapping.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAddressMappings.class, communityAddressMapping.getId());
	}

	@Caching(evict = { @CacheEvict(value="CommunityAddressMapping", key="#communityAddressMapping.id"), 
			@CacheEvict(value="CommunityAddressMappingByAddressId", key="{#communityAddressMapping.communityId,#communityAddressMapping.addressId}"),
			@CacheEvict(value = "CommunityAddressMappingsList", key="#communityAddressMapping.communityId")})
	@Override
	public void deletePropAddressMapping(long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAddressMappings.class, id);
	}

	@Cacheable(value="CommunityAddressMapping", key="#id")
	@Override
	public CommunityAddressMapping findPropAddressMappingById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityAddressMapping.class);
	}

	@Cacheable(value="CommunityAddressMappingByAddressId", key="{#organizationId,#addressId}")
	@Override
	public CommunityAddressMapping findPropAddressMappingByAddressId(Long organizationId,Long addressId){
		final CommunityAddressMapping[] result = new CommunityAddressMapping[1];
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOrganizationAddressMappingsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		if(organizationId != null)
			query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId));
		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, CommunityAddressMapping.class);
			return null;
		});
		return result[0];
	}

	@Override
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long organizationId,Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityAddressMapping> result  = new ArrayList<CommunityAddressMapping>();
		SelectQuery<EhOrganizationAddressMappingsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		if(organizationId != null)
			query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId));
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.asc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
			return null;
		});
		return result;
	}

	@Override
	public int countCommunityAddressMappings(Long organizationId, Long communityId, Byte livingStatus) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record1<Integer>> query = context.selectCount().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).getQuery();
        if (organizationId != null)
            query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId));
        if (communityId != null)
            query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId));
        if(livingStatus != null)
            query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS.eq(livingStatus));
		return query.fetchOneInto(Integer.class);
	}

	@Cacheable(value = "CommunityAddressMappingsList", key="#communityId")
	@Override
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		List<CommunityAddressMapping> result  = new ArrayList<CommunityAddressMapping>();
		SelectQuery<EhOrganizationAddressMappingsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		if(communityId != null)
			query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(communityId));
		query.addOrderBy(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
			return null;
		});
		return result;
	}

	@Override
	public void createPropBill(CommunityPmBill communityPmBill) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhOrganizationBillsRecord record = ConvertHelper.convert(communityPmBill, EhOrganizationBillsRecord.class);
		InsertQuery<EhOrganizationBillsRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_BILLS);
		query.setRecord(record);
		query.setReturning(Tables.EH_ORGANIZATION_BILLS.ID);
		query.execute();

		communityPmBill.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationBills.class, null);
	}

	@CacheEvict(value = "CommunityPmBill", key="#id")
	@Override
	public void updatePropBill(CommunityPmBill communityPmBill){
		assert(communityPmBill.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillsDao dao = new EhOrganizationBillsDao(context.configuration());
		dao.update(communityPmBill);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBills.class, communityPmBill.getId());
	}

	@CacheEvict(value = "CommunityPmBill", key="#communityPmBill.id")
	@Override
	public void deletePropBill(CommunityPmBill communityPmBill){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillsDao dao = new EhOrganizationBillsDao(context.configuration());
		dao.deleteById(communityPmBill.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBills.class, communityPmBill.getId());
	}

	@CacheEvict(value = "CommunityPmBill", key="#id")
	@Override
	public void deletePropBill(long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillsDao dao = new EhOrganizationBillsDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBills.class, id);
	}

	@Cacheable(value = "CommunityPmBill", key="#id")
	@Override
	public CommunityPmBill findPropBillById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillsDao dao = new EhOrganizationBillsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityPmBill.class);
	}

	@Override
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		List<CommunityPmBill> result  = new ArrayList<CommunityPmBill>();
		SelectQuery<EhOrganizationBillsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_BILLS);
		if(communityId != null){
			query.addConditions(Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(communityId));
		}
		if(dateStr != null && !"".equals(dateStr)) {
			query.addConditions(Tables.EH_ORGANIZATION_BILLS.DATE_STR.eq(dateStr));
		}

		if(address != null && !"".equals(address)) {
			query.addConditions(Tables.EH_ORGANIZATION_BILLS.ADDRESS.eq(address));
		}

		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATION_BILLS.ID.asc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmBill.class));
			return null;
		});
		return result;
	}

	@Cacheable(value = "listCommunityPmBills")
	@Override
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		List<CommunityPmBill> result  = new ArrayList<CommunityPmBill>();
		SelectQuery<EhOrganizationBillsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_BILLS);
		if(communityId != null){
			query.addConditions(Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(communityId));
		}
		if(dateStr != null && !"".equals(dateStr)) {
			query.addConditions(Tables.EH_ORGANIZATION_BILLS.DATE_STR.eq(dateStr));
		}
		query.addOrderBy(Tables.EH_ORGANIZATION_BILLS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmBill.class));
			return null;
		});
		return result;
	}

	@Override
	public int countCommunityPmBills(long communityId, String dateStr, String address) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_BILLS);
		Condition condition = Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(communityId);
		if(address != null && !"".equals(address)) {
			condition = condition.and(Tables.EH_ORGANIZATION_BILLS.ADDRESS.eq(address));
		}

		if(dateStr != null && !"".equals(dateStr)) {
			condition = condition.and(Tables.EH_ORGANIZATION_BILLS.DATE_STR.eq(dateStr));
		}

		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public long createPropOwner(CommunityPmOwner communityPmOwner) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwners.class));
		communityPmOwner.setId(id);
		communityPmOwner.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		communityPmOwner.setCreatorUid(UserContext.current().getUser().getId());

		EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
		dao.insert(communityPmOwner);

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwners.class, null);
		return id;
	}

	@CacheEvict(value = "CommunityPmOwner", key="#communityPmOwner.id")
	@Override
	public void updatePropOwner(CommunityPmOwner communityPmOwner){
		assert(communityPmOwner.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
		dao.update(communityPmOwner);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwners.class, communityPmOwner.getId());
	}

	@CacheEvict(value = "CommunityPmOwner", key="#communityPmOwner.id")
	@Override
	public void deletePropOwner(CommunityPmOwner communityPmOwner){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
		dao.deleteById(communityPmOwner.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwners.class, communityPmOwner.getId());
	}

	@CacheEvict(value="CommunityPmOwner", key="#id")
	@Override
	public void deletePropOwner(long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwners.class, id);
	}

	// @Cacheable(value="CommunityPmOwner", key="#id")
	@Override
	public CommunityPmOwner findPropOwnerById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityPmOwner.class);
	}

	@Override
	public List<CommunityPmOwner> listCommunityPmOwners(Long organizationId, String address, String contactToken,Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
		if(organizationId != null)
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId));
		if(address != null && !"".equals(address)) {
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ADDRESS.eq(address));
		}

		if(contactToken != null && !"".equals(contactToken)) {
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken));
		}

		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.asc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmOwner> listCommunityPmOwners(Long organizationId, Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
		if(organizationId != null)
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId));
		if(addressId != null && !"".equals(addressId)) {
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ADDRESS_ID.eq(addressId));
		}
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmOwner> listCommunityPmOwnersWithLocator(CrossShardListingLocator locator, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result = new ArrayList<>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
		if (locator.getAnchor() != null && locator.getAnchor() != 0L) {
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ID.lt(locator.getAnchor()));
		}
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		query.addLimit(pageSize + 1);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		if(result.size()>pageSize){
			result.remove(result.size()-1);
			locator.setAnchor(result.get(result.size()-1).getId());
		}else {
			locator.setAnchor(null);
		}
		return result;
	}

	@Override
	public int countCommunityPmOwners(long organizationId, String address, String contactToken) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_OWNERS);
		Condition condition = Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId);
		if(address != null && !"".equals(address)) {
			condition = condition.and(Tables.EH_ORGANIZATION_OWNERS.ADDRESS.eq(address));
		}

		if(contactToken != null && !"".equals(contactToken)) {
			condition = condition.and(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken));
		}

		return step.where(condition).fetchOneInto(Integer.class);
	}

	@CacheEvict(value="CommunityPmTasks", key="#task.id")
	@Override
	public void createPmTask(CommunityPmTasks task) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationTasksRecord record = ConvertHelper.convert(task, EhOrganizationTasksRecord.class);
		InsertQuery<EhOrganizationTasksRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_TASKS);
		query.setRecord(record);
		query.setReturning(Tables.EH_ORGANIZATION_TASKS.ID);
		query.execute();

		task.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationTasks.class, null);
	}

	@Override
	public List<CommunityPmTasks> listCommunityPmTasks(Long communityId, Long entityId,String entityType,
			Long targetId, String targetType,String taskType, Byte status,Integer pageOffset,Integer pageSize) { 
		final List<CommunityPmTasks> groups = new ArrayList<CommunityPmTasks>();

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationTasksRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_TASKS);
		if(communityId != null && communityId > 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(communityId));
		if(entityId != null && entityId > 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_ID.eq(entityId));
		if(entityType != null && !entityType .equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_TYPE.eq(entityType));
		if(targetType != null && !targetType .equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TARGET_TYPE.eq(targetType));
		if(targetId != null && targetId > 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TARGET_ID.eq(targetId));
		if(taskType != null && !taskType .equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));
		if(status != null && status >= 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(status));
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATION_TASKS.ID.asc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			groups.add(ConvertHelper.convert(r, CommunityPmTasks.class));
			return null;
		});

		return groups;
	}

	@Override
	public List<CommunityPmTasks> findPmTaskEntityIdAndTargetId(Long communityId, Long entityId,String entityType,
			Long targetId, String targetType,String taskType, Byte status) {
		final List<CommunityPmTasks> groups = new ArrayList<CommunityPmTasks>();

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationTasksRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_TASKS);
		if(communityId != null && communityId > 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(communityId));
		if(entityId != null && entityId > 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_ID.eq(entityId));
		if(entityType != null && !entityType .equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_TYPE.eq(entityType));
		if(targetType != null && !targetType .equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TARGET_TYPE.eq(targetType));
		if(targetId != null && targetId > 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TARGET_ID.eq(targetId));
		if(taskType != null && !taskType .equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));
		if(status != null && status >= 0)
			query.addConditions(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(status));

		query.fetch().map((r) -> {
			groups.add(ConvertHelper.convert(r, CommunityPmTasks.class));
			return null;
		});

		return groups;
	}
	@Override
	public ListPropInvitedUserCommandResponse listInvitedUsers(Long organizationId,String contactToken, Long pageOffset, Long pageSize) {

		/*final List<PropInvitedUserDTO> results = new ArrayList<>();
		ListPropInvitedUserCommandResponse response = new ListPropInvitedUserCommandResponse();
		long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
		long size = pageSize;
		contactToken = contactToken == null ? "" : contactToken;
		String likeVal = contactToken + "%";
		Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
		if(offset > 0) {
			final List<Long> countsInShards = new ArrayList<>();
			this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, 
					(DSLContext context, Object reducingContext)-> {

						Long count = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS)
								.leftOuterJoin(Tables.EH_USERS)
								.on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID)
										.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
										.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode())))
										.where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
										.and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.like(likeVal)
												.or(Tables.EH_USERS.ACCOUNT_NAME.like(likeVal)))
												.fetchOne(0, Long.class);

						countsInShards.add(count);
						return true;
					});

			targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
		}
		if(targetShard.first() < 0){
			response.setMembers(results);
			return response;
		}

		final int[] currentShard = new int[1];
		currentShard[0] = 0;
		final Tuple<Integer, Long> fallingShard = targetShard;

		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), currentShard, 
				(DSLContext context, Object reducingContext)-> {
					int[] current = (int[])reducingContext;
					if(current[0] < fallingShard.first()) {
						current[0] += 1;
						return true;
					}

					long off = 0;
					if(current[0] == fallingShard.first())
						off = fallingShard.second();

					context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
					.leftOuterJoin(Tables.EH_USERS)
					.on(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.INVITOR_UID)
							.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
							.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode())))
							.where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
							.and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.like(likeVal)
									.or(Tables.EH_USERS.ACCOUNT_NAME.like(likeVal)))
									.limit((int)size).offset((int)off)
									.fetch().map((r) -> {
										//PropInvitedUserDTO user = ConvertHelper.convert(r, PropInvitedUserDTO.class);
										PropInvitedUserDTO user = new PropInvitedUserDTO();
										user.setUserId(r.getValue(Tables.EH_USERS.ID));
										user.setUserName(r.getValue(Tables.EH_USERS.ACCOUNT_NAME));
										user.setInviteType(r.getValue(Tables.EH_USERS.INVITE_TYPE));
										user.setRegisterTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
										user.setContactType(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TYPE));
										user.setContactToken(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN));
										user.setInvitorId(r.getValue(Tables.EH_USERS.INVITOR_UID));
										user.setInvitorName(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME));
										if(results.size() <= pageSize + 1)
											results.add(user);
										return null;
									});

					return true;
				});

		if(results.size() > pageSize) {
			results.remove(results.size() - 1);
			response.setMembers(results);
			return response;
		}
		return response;*/
		
		return null;

	}

	@Override
	public void createPropBillItem(CommunityPmBillItem communityPmBillItem) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillItemsDao dao = new EhOrganizationBillItemsDao(context.configuration());
		dao.insert(communityPmBillItem);

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationBillItems.class, null);
	}

	@Caching(evict = { @CacheEvict(value="CommunityPmBillItem", key="#communityPmBillItem.id"),
			@CacheEvict(value="CommunityPmBillItemsList", key="#communityPmBillItem.billId")})
	@Override
	public void updatePropBillItem(CommunityPmBillItem communityPmBillItem){
		assert(communityPmBillItem.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillItemsDao dao = new EhOrganizationBillItemsDao(context.configuration());
		dao.update(communityPmBillItem);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBillItems.class, communityPmBillItem.getId());
	}

	@Caching(evict = { @CacheEvict(value="CommunityPmBillItem", key="#communityPmBillItem.id"),
			@CacheEvict(value="CommunityPmBillItemsList", key="#communityPmBillItem.billId")})
	@Override
	public void deletePropBillItem(CommunityPmBillItem communityPmBillItem){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillItemsDao dao = new EhOrganizationBillItemsDao(context.configuration());
		dao.deleteById(communityPmBillItem.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBillItems.class, communityPmBillItem.getId());
	}

	@Caching(evict = { @CacheEvict(value="CommunityPmBillItem", key="#id"),
			@CacheEvict(value="CommunityPmBillItemsList", key="#billId")})
	@Override
	public void deletePropBillItem(long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillItemsDao dao = new EhOrganizationBillItemsDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBillItems.class, id);
	}

	@Cacheable(value = "CommunityPmBillItem", key="#id")
	@Override
	public CommunityPmBillItem findPropBillItemById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillItemsDao dao = new EhOrganizationBillItemsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityPmBillItem.class);
	}

	@Cacheable(value = "CommunityPmBillItemsList", key="#billId")
	@Override
	public List<CommunityPmBillItem> listCommunityPmBillItems(Long billId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		List<CommunityPmBillItem> result  = new ArrayList<CommunityPmBillItem>();
		SelectQuery<EhOrganizationBillItemsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_BILL_ITEMS);
		query.addConditions(Tables.EH_ORGANIZATION_BILL_ITEMS.BILL_ID.eq(billId));
		query.addOrderBy(Tables.EH_ORGANIZATION_BILL_ITEMS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmBillItem.class));
			return null;
		});
		return result;
	}

	@Override
	public List<String> listPropBillDateStr(Long communityId) {
		List<String> dateList = new ArrayList<String>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		context.selectDistinct(Tables.EH_ORGANIZATION_BILLS.DATE_STR).from(Tables.EH_ORGANIZATION_BILLS)
		.where(Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(communityId))
		.fetch().map((r) -> {
			dateList.add(r.getValue(Tables.EH_ORGANIZATION_BILLS.DATE_STR));
			return null;
		});

		return dateList;
	}

	@Override
	public CommunityPmTasks findPmTaskByEntityId(long communityId, long entityId, String entityType) {
		final CommunityPmTasks[] result = new CommunityPmTasks[1];

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationTasksRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_TASKS);
		query.addConditions(Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(communityId));
		query.addConditions(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_ID.eq(entityId));
		query.addConditions(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_TYPE.eq(entityType));

		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, CommunityPmTasks.class);
			return null;
		});

		return result[0];
	}

	@Override
	public CommunityPmTasks findPmTaskById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationTasksDao dao = new EhOrganizationTasksDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityPmTasks.class);
	}

	@Override
	public void updatePmTaskListStatus(List<CommunityPmTasks> tasks) {
		assert(tasks != null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationTasksDao dao = new EhOrganizationTasksDao(context.configuration());
		List<EhOrganizationTasks> pmTasks = new ArrayList<EhOrganizationTasks>();
		tasks.stream().map((r) ->{
			pmTasks.add(ConvertHelper.convert(r, EhOrganizationTasks.class));
			return null;
		});
		dao.update(pmTasks);
	}

	@CacheEvict(value="CommunityPmTasks", key="#task.id")
	@Override
	public void updatePmTask(CommunityPmTasks task) {
		assert(task != null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationTasksDao dao = new EhOrganizationTasksDao(context.configuration());
		dao.update(task);

		DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhOrganizationContacts.class, task.getId());
	}


	@Override
	public void createPropContact(CommunityPmContact communityPmContact) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhOrganizationContactsDao dao = new EhOrganizationContactsDao(context.configuration());
		dao.insert(communityPmContact);

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationContacts.class, null);
	}

	@CacheEvict(value = "CommunityPmContact", key="#communityPmContact.id")
	@Override
	public void updatePropContact(CommunityPmContact communityPmContact){
		assert(communityPmContact.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationContactsDao dao = new EhOrganizationContactsDao(context.configuration());
		dao.update(ConvertHelper.convert(communityPmContact, EhOrganizationContacts.class));

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationContacts.class, communityPmContact.getId());
	}

	@CacheEvict(value = "CommunityPmContact", key="#communityPmContact.id")
	@Override
	public void deletePropContact(CommunityPmContact communityPmContact){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationContactsDao dao = new EhOrganizationContactsDao(context.configuration());
		dao.deleteById(communityPmContact.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationContacts.class, communityPmContact.getId());
	}

	@CacheEvict(value="CommunityPmContact", key="#id")
	@Override
	public void deletePropContact(long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationContactsDao dao = new EhOrganizationContactsDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationContacts.class, id);
	}

	@Cacheable(value="CommunityPmContact", key="#id")
	@Override
	public CommunityPmContact findPropContactById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationContactsDao dao = new EhOrganizationContactsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), CommunityPmContact.class);
	}


	@Override
	public List<CommunityPmContact> listCommunityPmContacts(Long orgId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmContact> result  = new ArrayList<CommunityPmContact>();
		SelectQuery<EhOrganizationContactsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_CONTACTS);
		if(orgId != null)
			query.addConditions(Tables.EH_ORGANIZATION_CONTACTS.ORGANIZATION_ID.eq(orgId));

		query.addOrderBy(Tables.EH_ORGANIZATION_CONTACTS.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmContact.class));
			return null;
		});
		return result;
	}

	@Override
	public int countCommunityPmTasks(Long organizationId, Long entityId,String entityType, Long targetId, String targetType,
			String taskType, Byte status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_TASKS);
		Condition condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(organizationId);
		if(entityId != null && entityId > 0)
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_ID.eq(entityId));
		if(entityType != null && !entityType .equals(""))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_TYPE.eq(entityType));
		if(targetType != null && !targetType .equals(""))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TARGET_TYPE.eq(targetType));
		if(targetId != null && targetId > 0)
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TARGET_ID.eq(targetId));
		if(taskType != null && !taskType .equals(""))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));
		if(status != null && status >= 0)
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(status));
		return step.where(condition).fetchOneInto(Integer.class);
	}
	@Override
	public int countCommunityPmTasks(Long organizationId,String taskType,Byte status,String startTime,String endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_TASKS);
		Condition condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(organizationId);
		if(!StringUtils.isEmpty(taskType))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));

		if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.between(Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)));
		if(status != null && status >= 0)
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(status));
		return step.where(condition).fetchOneInto(Integer.class);
	}
	
	@Override
	public List<OrganizationTask> communityPmTaskLists(Long organizationId,Long communityId,String taskType,Byte status,String startTime,String endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhOrganizationTasksRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_TASKS);
		Condition condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(organizationId);
		if(!StringUtils.isEmpty(taskType))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));

		if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime))
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.between(Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)));
		if(status != null && status >= 0)
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(status));
		
		if(null != communityId){
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.VISIBLE_REGION_ID.eq(communityId));
		}
		
		query.addConditions(condition);
		
		List<OrganizationTask> tasks = query.fetch().map(r -> {
			return ConvertHelper.convert(r, OrganizationTask.class);
		});
		
		return tasks;
	}

	@Override
	public CommunityPmBill findNewestBillByAddressId(Long addressId) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.eq(addressId);
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(condition)
				.orderBy(Tables.EH_ORGANIZATION_BILLS.END_DATE.desc())
				.fetch();

		if(records != null && !records.isEmpty())
			return ConvertHelper.convert(records.get(0),CommunityPmBill.class);
		return null;
	}

	@Override
	public Map<Long, CommunityPmBill> mapNewestBillByAddressIds(List<Long> addressIds) {
		Map<Long, CommunityPmBill> map = new HashMap<>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		context.select().from(Tables.EH_ORGANIZATION_BILLS)
			.where(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.in(addressIds))
			.orderBy(Tables.EH_ORGANIZATION_BILLS.END_DATE.asc())
			.fetch().map(r->{
				CommunityPmBill bill = ConvertHelper.convert(r, CommunityPmBill.class);
				map.put(bill.getEntityId(), bill);
				return null;
			});
		
		return map;
	}

	@Override
	public CommunityPmBill findPmBillByAddressAndDate(Long addressId,java.sql.Date startDate,java.sql.Date endDate) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.eq(addressId)
						.and(Tables.EH_ORGANIZATION_BILLS.START_DATE.greaterOrEqual(startDate))
						.and(Tables.EH_ORGANIZATION_BILLS.END_DATE.lessOrEqual(endDate)))
						.fetch();

		if(records != null && !records.isEmpty())
			return ConvertHelper.convert(records.get(0),CommunityPmBill.class);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommunityPmBill> listNewestPmBillsByOrgId(Long organizationId,String address) {
		List<CommunityPmBill> list = new ArrayList<CommunityPmBill>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		org.jooq.Table<Record2<Long, Date>> table2 = context.select(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.as("t2One"),Tables.EH_ORGANIZATION_BILLS.END_DATE.max().as("t2Two"))
				.from(Tables.EH_ORGANIZATION_BILLS)
				.groupBy(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID).asTable("t2");

		Condition condition = Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(organizationId);

		if(address != null && !address.isEmpty())
			condition = condition.and(Tables.EH_ORGANIZATION_BILLS.ADDRESS.like("%" + address + "%"));

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.join(table2)
				.on(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.equal((Field<Long>) table2.field("t2One"))
						.and(Tables.EH_ORGANIZATION_BILLS.END_DATE.equal((Field<Date>) table2.field("t2Two"))))
						.where(condition)
						.fetch();

		if(records != null && !records.isEmpty()){
			records.stream().map(r -> {
				CommunityPmBill bill = new CommunityPmBill();
				bill.setAddress(r.getValue(Tables.EH_ORGANIZATION_BILLS.ADDRESS));
				bill.setCreateTime(r.getValue(Tables.EH_ORGANIZATION_BILLS.CREATE_TIME));
				bill.setCreatorUid(r.getValue(Tables.EH_ORGANIZATION_BILLS.CREATOR_UID));
				bill.setDateStr(r.getValue(Tables.EH_ORGANIZATION_BILLS.DATE_STR));
				bill.setDescription(r.getValue(Tables.EH_ORGANIZATION_BILLS.DESCRIPTION));
				bill.setDueAmount(r.getValue(Tables.EH_ORGANIZATION_BILLS.DUE_AMOUNT));
				bill.setEndDate(r.getValue(Tables.EH_ORGANIZATION_BILLS.END_DATE));
				bill.setEntityId(r.getValue(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID));
				bill.setEntityType(r.getValue(Tables.EH_ORGANIZATION_BILLS.ENTITY_TYPE));
				bill.setId(r.getValue(Tables.EH_ORGANIZATION_BILLS.ID));
				bill.setName(r.getValue(Tables.EH_ORGANIZATION_BILLS.NAME));
				bill.setNotifyCount(r.getValue(Tables.EH_ORGANIZATION_BILLS.NOTIFY_COUNT));
				bill.setNotifyTime(r.getValue(Tables.EH_ORGANIZATION_BILLS.NOTIFY_TIME));
				bill.setOrganizationId(r.getValue(Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID));
				bill.setOweAmount(r.getValue(Tables.EH_ORGANIZATION_BILLS.OWE_AMOUNT));
				bill.setPayDate(r.getValue(Tables.EH_ORGANIZATION_BILLS.PAY_DATE));
				bill.setStartDate(r.getValue(Tables.EH_ORGANIZATION_BILLS.START_DATE));

				list.add(bill);
				return null;
			}).toArray();
		}

		return list;
	}

	@Override
	public BigDecimal countPmYearIncomeByOrganizationId(Long organizationId,Integer resultCodeId) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
		Timestamp firstDateOfYear = new Timestamp(cal.getTime().getTime());

		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
		Timestamp lastDateOfYear = new Timestamp(cal.getTime().getTime());

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.OWNER_ID.eq(organizationId)
				.and(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.greaterOrEqual(firstDateOfYear))
				.and(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.lessOrEqual(lastDateOfYear));
		if(resultCodeId != null)
			condition = condition.and(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.RESULT_CODE_ID.eq(resultCodeId));

		Record1<BigDecimal> record = context.select(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CHARGE_AMOUNT.sum())
				.from(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS)
				.where(condition)
				.fetchOne();

		if(record != null && record.value1() != null)
			return record.value1();
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal countFamilyPmBillDueAmountInYear(Long orgId, Long addressId) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		java.sql.Date endDateInYear = new java.sql.Date(cal.getTime().getTime());
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		java.sql.Date startDateInYear = new java.sql.Date(cal.getTime().getTime());

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record1<BigDecimal>> records = context.select(Tables.EH_ORGANIZATION_BILLS.DUE_AMOUNT.sum()).from(Tables.EH_ORGANIZATION_BILLS)
				.where(Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(orgId)
						.and(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.eq(addressId))
						.and(Tables.EH_ORGANIZATION_BILLS.START_DATE.greaterOrEqual(startDateInYear))
						.and(Tables.EH_ORGANIZATION_BILLS.END_DATE.lessOrEqual(endDateInYear)))
						.fetch();

		if(records != null && !records.isEmpty() && records.get(0).value1() != null)
			return records.get(0).value1();
		return BigDecimal.ZERO;
	}

	@Override
	public CommunityPmBill findFamilyFirstPmBillInYear(Long orgId, Long addressId) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		java.sql.Date endDateInYear = new java.sql.Date(cal.getTime().getTime());
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		java.sql.Date startDateInYear = new java.sql.Date(cal.getTime().getTime());

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(orgId)
						.and(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.eq(addressId))
						.and(Tables.EH_ORGANIZATION_BILLS.START_DATE.greaterOrEqual(startDateInYear))
						.and(Tables.EH_ORGANIZATION_BILLS.END_DATE.lessOrEqual(endDateInYear)))
						.orderBy(Tables.EH_ORGANIZATION_BILLS.END_DATE.asc())
						.fetch();

		if(records != null && !records.isEmpty())
			return ConvertHelper.convert(records.get(0),CommunityPmBill.class);
		return null;
	}

	@Override
	public CommunityPmBill findPmBillByAddressIdAndTime(Long addressId,
			Date startDate, Date endDate) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.eq(addressId)
						.and(Tables.EH_ORGANIZATION_BILLS.START_DATE.greaterOrEqual(startDate)).and(Tables.EH_ORGANIZATION_BILLS.END_DATE.lessOrEqual(endDate)))
						.fetch();

		if(records != null && !records.isEmpty()){
			return ConvertHelper.convert(records.get(0),CommunityPmBill.class);
		}
		return null;
	}

	@Override
	public OrganizationCommunity findPmCommunityByOrgId(Long orgId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES)
				.where(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId))
				.fetch();

		if(records != null && !records.isEmpty()){
			return ConvertHelper.convert(records.get(0), OrganizationCommunity.class);
		}
		return null;
	}

	@Override
	public CommunityAddressMapping findAddressMappingByAddressId(Long addressId) {
		final CommunityAddressMapping[] result = new CommunityAddressMapping[1];
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationAddressMappingsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId));
		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, CommunityAddressMapping.class);
			return null;
		});
		return result[0];
	}

	@Override
	public Map<Long, CommunityAddressMapping> mapAddressMappingByAddressIds(List<Long> addressIds) {
		Map<Long, CommunityAddressMapping> map = new HashMap<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		context.select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
			.where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.in(addressIds))
			.fetch().map(r->{
				CommunityAddressMapping mapping = ConvertHelper.convert(r, CommunityAddressMapping.class);
				map.put(mapping.getAddressId(), mapping);
				return null;
			});
		
		return map;
	}

	@Override
	public Map<Long, CommunityAddressMapping> mapAddressMappingByAddressIds(List<Long> addressIds, Byte livingStatus) {
		Map<Long, CommunityAddressMapping> map = new HashMap<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
        query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.in(addressIds));
        if(livingStatus != null){
            query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS.eq(livingStatus));
        }
        query
				.fetch().map(r->{
			CommunityAddressMapping mapping = ConvertHelper.convert(r, CommunityAddressMapping.class);
			map.put(mapping.getAddressId(), mapping);
			return null;
		});

		return map;
	}

	@Override
	public List<CommunityAddressMapping> listCommunityAddressMappingByAddressIds(List<Long> addressIds) {
		List<CommunityAddressMapping> result = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		context.select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
				.where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.in(addressIds))
				.fetch().map(r->{
			result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityAddressMapping> listAddressMappingsByOrgId(Long orgId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityAddressMapping> result  = new ArrayList<CommunityAddressMapping>();
		SelectQuery<EhOrganizationAddressMappingsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		if(orgId != null)
			query.addConditions(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(orgId));
		query.addOrderBy(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityAddressMapping.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmBill> listPmBillsByOrgId(Long orgId,String address,Date startDate, Date endDate, long offset, int pageSize) {
		List<CommunityPmBill> list = new ArrayList<CommunityPmBill>();

		Condition condition = Tables.EH_ORGANIZATION_BILLS.ORGANIZATION_ID.eq(orgId);
		if(!(address == null || address.isEmpty())){
			condition = condition.and(Tables.EH_ORGANIZATION_BILLS.ADDRESS.like("%"+address+"%"));
		}
		if(startDate != null && endDate != null){
			condition = condition.and(Tables.EH_ORGANIZATION_BILLS.START_DATE.greaterOrEqual(startDate)
					.and(Tables.EH_ORGANIZATION_BILLS.END_DATE.lessOrEqual(endDate)));
		}

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(condition)
				.orderBy(Tables.EH_ORGANIZATION_BILLS.CREATE_TIME.desc(),Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.asc())
				.limit(pageSize).offset((int)offset)
				.fetch();

		if(records != null && !records.isEmpty()){
			records.stream().map(r -> {
				list.add(ConvertHelper.convert(r, CommunityPmBill.class));
				return null;
			}).toArray();
		}
		return list;
	}

	@Override
	public void updateOrganizationAddressMapping(CommunityAddressMapping mapping) {
		assert(mapping.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
		dao.update(mapping);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAddressMappings.class, mapping.getId());

	}

	@Override
	public List<CommunityPmBillItem> listOrganizationBillItemsByBillId(Long billId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmBillItem> result  = new ArrayList<CommunityPmBillItem>();
		SelectQuery<EhOrganizationBillItemsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_BILL_ITEMS);
		query.addConditions(Tables.EH_ORGANIZATION_BILL_ITEMS.BILL_ID.eq(billId));
		query.addOrderBy(Tables.EH_ORGANIZATION_BILL_ITEMS.ID.asc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmBillItem.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmOwner> listCommunityPmOwners(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
		if(ids != null && ids.size() > 0)
			query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ID.in(ids));

        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmOwner> listCommunityPmOwnersByToken(
            Integer namespaceId, Long communityId, String contactToken) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken)
				.or(Tables.EH_ORGANIZATION_OWNERS.CONTACT_EXTRA_TELS.like("%" + contactToken + "%")));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"));
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("listCommunityPmOwnersByToken, sql=" + query.getSQL());
			LOGGER.debug("listCommunityPmOwnersByToken, bindValues=" + query.getBindValues());
		}
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		return result;
	}

	@Override
	public List<CommunityPmOwner> listCommunityPmOwnersByToken(
			Integer namespaceId, String contactToken) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		return result;
	}

    @Override
    public long createOrganizationOwnerBehavior(OrganizationOwnerBehavior behavior) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerBehaviors.class));
		behavior.setId(id);
		behavior.setCreateTime(new Timestamp(System.currentTimeMillis()));
		behavior.setUpdateTime(behavior.getCreateTime());
		behavior.setUpdateUid(UserContext.current().getUser().getId());
		EhOrganizationOwnerBehaviorsDao dao = new EhOrganizationOwnerBehaviorsDao(context.configuration());
		dao.insert(behavior);
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwnerBehaviorsDao.class, id);
		return id;
    }

	@Override
	public OrganizationOwnerBehavior findOrganizationOwnerBehaviorByOwnerAndAddressId(Long id, Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return  context.selectFrom(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
				.where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.OWNER_ID.eq(id))
				.and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ADDRESS_ID.eq(addressId))
				.fetchAnyInto(OrganizationOwnerBehavior.class);
	}

	@Override
	public OrganizationOwnerType findOrganizationOwnerTypeById(Long orgOwnerTypeId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_TYPE)
                .where(Tables.EH_ORGANIZATION_OWNER_TYPE.ID.eq(orgOwnerTypeId))
                .fetchOneInto(OrganizationOwnerType.class);
	}

    @Override
    public List<OrganizationOwnerAddress> listOrganizationOwnerAddressByOwnerId(Integer namespaceId, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select()
                .from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(ownerId))
                .fetchInto(OrganizationOwnerAddress.class);
    }

    @Override
    public long createOrganizationOwnerAttachment(OrganizationOwnerAttachment attachment) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerAttachments.class));
		attachment.setId(id);
		attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		attachment.setCreatorUid(UserContext.current().getUser().getId());
		EhOrganizationOwnerAttachmentsDao dao = new EhOrganizationOwnerAttachmentsDao(context.configuration());
		dao.insert(attachment);
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwnerAttachments.class, id);
		return id;
    }

    @Override
    public List<OrganizationOwnerAttachment> listOrganizationOwnerAttachments(Integer namespaceId, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.OWNER_ID.eq(ownerId))
                .fetchInto(OrganizationOwnerAttachment.class);
    }

    @Override
    public OrganizationOwnerAttachment findOrganizationOwnerAttachment(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.eq(id))
                .fetchOneInto(OrganizationOwnerAttachment.class);
    }

    @Override
    public void deleteOrganizationOwnerAttachment(OrganizationOwnerAttachment attachment) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerAttachmentsDao dao = new EhOrganizationOwnerAttachmentsDao(context.configuration());
        dao.deleteById(attachment.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerAttachments.class, attachment.getId());
    }

    @Override
    public OrganizationOwnerBehavior findOrganizationOwnerBehaviorById(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ID.eq(id))
                .fetchOneInto(OrganizationOwnerBehavior.class);
    }

    @Override
    public void deleteOrganizationOwnerBehavior(OrganizationOwnerBehavior behavior) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerBehaviorsDao dao = new EhOrganizationOwnerBehaviorsDao(context.configuration());
        behavior.setStatus(OrganizationOwnerBehaviorStatus.DELETE.getCode());// 设置为删除状态
        dao.update(behavior);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerBehaviors.class, behavior.getId());
    }

    @Override
    public List<OrganizationOwnerBehavior> listOrganizationOwnerBehaviors(Integer namespaceId, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
                .where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.STATUS.eq(OrganizationOwnerBehaviorStatus.NORMAL.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.OWNER_ID.eq(ownerId))
                .fetchInto(OrganizationOwnerBehavior.class);
    }

	@Override
	public List<OrganizationOwnerBehavior> listApartmentOrganizationOwnerBehaviors(Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select().from(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS)
				.where(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ADDRESS_ID.eq(addressId))
				.and(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.STATUS.eq(OrganizationOwnerBehaviorStatus.NORMAL.getCode()))
				.fetchInto(OrganizationOwnerBehavior.class);
	}

	@Override
    public long createOrganizationOwnerAddress(OrganizationOwnerAddress ownerAddress) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerAddress.class));
        ownerAddress.setId(id);
        EhOrganizationOwnerAddressDao dao = new EhOrganizationOwnerAddressDao(context.configuration());
        dao.insert(ownerAddress);
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwnerAddress.class, id);
        return id;
    }

    @Override
    public OrganizationOwnerAddress findOrganizationOwnerAddressByOwnerAndAddress(Integer namespaceId, Long ownerId, Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(ownerId))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(addressId))
                .fetchAnyInto(OrganizationOwnerAddress.class);
    }

    @Override
    public void updateOrganizationOwnerAddress(OrganizationOwnerAddress ownerAddress) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerAddressDao dao = new EhOrganizationOwnerAddressDao(context.configuration());
        dao.update(ownerAddress);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerAddress.class, ownerAddress.getId());
    }

    @Override
    public void deleteOrganizationOwnerAddress(OrganizationOwnerAddress ownerAddress) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationOwnerAddressDao dao = new EhOrganizationOwnerAddressDao(context.configuration());
		dao.delete(ownerAddress);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerAddress.class, ownerAddress.getId());
    }

	@Override
	public int deleteOrganizationOwnerAddressByOwnerId(Integer namespaceId, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		int rows = context.delete(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
						.where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
						.and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(ownerId))
						.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerAddress.class, null);
		return rows;
	}

    @Override
    public List<OrganizationOwnerDTO> listOrganizationOwnersByAddressId(Integer namespaceId,
																		Long addressId,
																		RecordMapper<Record, OrganizationOwnerDTO> mapper) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<OrganizationOwnerDTO> dtoList = context.select(
				Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS,
				Tables.EH_ORGANIZATION_OWNER_ADDRESS.AUTH_TYPE,
				Tables.EH_ORGANIZATION_OWNERS.ORG_OWNER_TYPE_ID,
				Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME,
				Tables.EH_ORGANIZATION_OWNERS.ID.as("ownerId")
		)
				.from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
				.join(Tables.EH_ORGANIZATION_OWNERS)
				.on(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(Tables.EH_ORGANIZATION_OWNERS.ID))
				.where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
				.and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(addressId))
                // 供在门牌管理中显示已在App中注册的用户(在organizationOwner里表现为已认证状态)
                // .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.AUTH_TYPE.eq(OrganizationOwnerAddressAuthType.ACTIVE.getCode()))
				.fetch().map(mapper);
		return dtoList;
    }

	@Override
	public Map<Long, Integer> mapOrganizationOwnerCountByAddressIds(Integer namespaceId, List<Long> addressIds) {
		Map<Long, Integer> map = new HashMap<>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		context.select(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID, DSL.count())
			.from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
			.join(Tables.EH_ORGANIZATION_OWNERS)
			.on(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(Tables.EH_ORGANIZATION_OWNERS.ID))
			.where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
            .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
			.and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.in(addressIds))
			.groupBy(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID)
			.fetch().map(r->{
				map.put(r.getValue(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID), r.getValue(DSL.count()));
				return null;
			});
		
		return map;
	}

	@Override
	public OrganizationOwnerType findOrganizationOwnerTypeByDisplayName(String orgOwnerTypeName) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select().from(Tables.EH_ORGANIZATION_OWNER_TYPE)
				.where(Tables.EH_ORGANIZATION_OWNER_TYPE.DISPLAY_NAME.eq(orgOwnerTypeName))
				.fetchOneInto(OrganizationOwnerType.class);
	}

    @Override
    public List<CommunityPmOwner> listCommunityPmOwnersByCommunity(Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"))
                .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
                .fetchInto(CommunityPmOwner.class);
    }

    @Override
    public long createOrganizationOwnerCar(OrganizationOwnerCar car) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerCars.class));
        car.setId(id);
        car.setUpdateUid(UserContext.current().getUser().getId());
        car.setCreateTime(new Timestamp(System.currentTimeMillis()));
        car.setUpdateTime(car.getCreateTime());
        EhOrganizationOwnerCarsDao dao = new EhOrganizationOwnerCarsDao(context.configuration());
        dao.insert(car);
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwnerCars.class, id);
        return id;
    }

    @Override
    public List<OrganizationOwnerCar> findOrganizationOwnerCarByCommunityIdAndPlateNumber(Integer namespaceId, Long communityId, String plateNumber) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationOwnerCarsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNER_CARS);
        if (namespaceId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNER_CARS.NAMESPACE_ID.eq(namespaceId));
        }
        if (communityId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNER_CARS.COMMUNITY_ID.eq(communityId));
        }
        if (plateNumber != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNER_CARS.PLATE_NUMBER.eq(plateNumber));
        }
        query.addOrderBy(Tables.EH_ORGANIZATION_OWNER_CARS.CREATE_TIME);
        query.addConditions(Tables.EH_ORGANIZATION_OWNER_CARS.STATUS.eq(OrganizationOwnerCarStatus.NORMAL.getCode()));
        return query.fetchInto(OrganizationOwnerCar.class);
    }

    @Override
    public List<OrganizationOwnerCar> listOrganizationOwnerCarsByIds(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationOwnerCarsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNER_CARS);
        if(ids != null && ids.size() > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNER_CARS.ID.in(ids));
        }
        query.addConditions(Tables.EH_ORGANIZATION_OWNER_CARS.STATUS.eq(OrganizationOwnerCarStatus.NORMAL.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_OWNER_CARS.ID.desc());
        return query.fetchInto(OrganizationOwnerCar.class);
    }

    @Override
    public List<OrganizationOwnerType> listOrganizationOwnerType() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_TYPE).fetchInto(OrganizationOwnerType.class);
    }

    @Override
    public OrganizationOwnerCar findOrganizationOwnerCar(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_CARS)
                .where(Tables.EH_ORGANIZATION_OWNER_CARS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.ID.eq(id))
                .fetchAnyInto(OrganizationOwnerCar.class);
    }

    @Override
    public void updateOrganizationOwnerCar(OrganizationOwnerCar car) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerCarsDao dao = new EhOrganizationOwnerCarsDao(context.configuration());
        dao.update(car);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerCars.class, car.getId());
    }

    @Override
    public List<CommunityPmOwner> listOrganizationOwners(Integer namespaceId, Long communityId, Long orgOwnerTypeId, String keyword, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        if (namespaceId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
        }
        if (communityId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"));
        }
        if (orgOwnerTypeId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ORG_OWNER_TYPE_ID.eq(orgOwnerTypeId));
        }
        if (StringUtils.isEmpty(keyword)) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.like("%"+keyword+"%")
            		.or(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.like("%"+keyword+"%")));
        }
        if (pageAnchor != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ID.ge(pageAnchor));
        }
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
        if(null != pageSize)
        	query.addLimit(pageSize);
        return query.fetchInto(CommunityPmOwner.class);
    }

    @Override
    public long createOrganizationOwnerCarAttachment(OrganizationOwnerCarAttachment attachment) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerCarAttachments.class));
        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        attachment.setCreatorUid(UserContext.current().getUser().getId());
        EhOrganizationOwnerCarAttachmentsDao dao = new EhOrganizationOwnerCarAttachmentsDao(context.configuration());
        dao.insert(attachment);
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwnerCarAttachments.class, id);
        return id;
    }

    @Override
    public List<OrganizationOwnerCarAttachment> listOrganizationOwnerCarAttachment(Integer namespaceId, Long carId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.OWNER_ID.eq(carId))
                .fetchInto(OrganizationOwnerCarAttachment.class);
    }

    @Override
    public OrganizationOwnerCarAttachment findOrganizationOwnerCarAttachment(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.eq(id))
                .fetchOneInto(OrganizationOwnerCarAttachment.class);
    }

    @Override
    public void deleteOrganizationOwnerCarAttachment(OrganizationOwnerCarAttachment attachment) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerCarAttachmentsDao dao = new EhOrganizationOwnerCarAttachmentsDao(context.configuration());
        dao.delete(attachment);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwnerCarAttachments.class, attachment.getId());
    }

    @Override
    public long createOrganizationOwnerOwnerCar(OrganizationOwnerOwnerCar ownerOwnerCar) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerOwnerCar.class));
        ownerOwnerCar.setId(id);
        EhOrganizationOwnerOwnerCarDao dao = new EhOrganizationOwnerOwnerCarDao(context.configuration());
        dao.insert(ownerOwnerCar);
        DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwnerOwnerCar.class, id);
        return id;
    }

    @Override
    public <R> List<R> listOrganizationOwnersByCar(Integer namespaceId, Long carId, RecordMapper<Record, R> mapper) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(
                Tables.EH_ORGANIZATION_OWNERS.ID.as("ownerId"),
                Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.as("ownerOwnerCarId"),
                Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME,
                Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN,
                Tables.EH_ORGANIZATION_OWNERS.ORG_OWNER_TYPE_ID,
                Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.PRIMARY_FLAG
        )
                .from(Tables.EH_ORGANIZATION_OWNERS)
                .join(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .on(Tables.EH_ORGANIZATION_OWNERS.ID.eq(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID))
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(carId))
                .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
                .orderBy(Tables.EH_ORGANIZATION_OWNERS.ORG_OWNER_TYPE_ID.asc())
                .fetch(mapper);
    }

    @Override
    public int deleteOrganizationOwnerOwnerCarByOwnerId(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return context.delete(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID.eq(id))
                .execute();
    }

    @Override
    public int deleteOrganizationOwnerOwnerCarByCarId(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return context.delete(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(id))
                .execute();
    }

    @Override
    public int deleteOrganizationOwnerCarAttachmentByCarId(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return context.delete(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.OWNER_ID.eq(id))
                .execute();
    }

    @Override
    public void deleteOrganizationOwnerOwnerCarByOwnerIdAndCarId(Integer namespaceId, Long ownerId, Long carId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(carId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID.eq(ownerId))
                .execute();
    }

    @Override
    public OrganizationOwnerOwnerCar findOrganizationOwnerOwnerCarByOwnerIdAndCarId(Integer namespaceId, Long ownerId, Long carId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(carId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID.eq(ownerId))
                .fetchAnyInto(OrganizationOwnerOwnerCar.class);
    }

    @Override
    public void updateOrganizationOwnerOwnerCar(OrganizationOwnerOwnerCar ownerOwnerCar) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerOwnerCarDao dao = new EhOrganizationOwnerOwnerCarDao(context.configuration());
        dao.update(ownerOwnerCar);
        DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhOrganizationOwnerOwnerCar.class, ownerOwnerCar.getId());
    }

    @Override
    public OrganizationOwnerOwnerCar findOrganizationOwnerOwnerCarById(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.eq(id))
                .fetchOneInto(OrganizationOwnerOwnerCar.class);
    }

    @Override
    public OrganizationOwnerOwnerCar findOrganizationOwnerCarPrimaryUser(Integer namespaceId, Long carId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(carId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.PRIMARY_FLAG.eq(OrganizationOwnerOwnerCarPrimaryFlag.PRIMARY.getCode()))
                .fetchAnyInto(OrganizationOwnerOwnerCar.class);
    }

    @Override
    public List<OrganizationOwnerCar> listOrganizationOwnerCarByOwnerId(Integer namespaceId, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_ORGANIZATION_OWNER_CARS.fields())
                .from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR)
                .join(Tables.EH_ORGANIZATION_OWNER_CARS)
                .on(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.CAR_ID.eq(Tables.EH_ORGANIZATION_OWNER_CARS.ID))
                .where(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ORGANIZATION_OWNER_ID.eq(ownerId))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.STATUS.eq(OrganizationOwnerCarStatus.NORMAL.getCode()))
                .fetchInto(OrganizationOwnerCar.class);
    }

    @Override
    public List<OrganizationOwnerCar> listOrganizationOwnerCarsByCommunity(Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select()
                .from(Tables.EH_ORGANIZATION_OWNER_CARS)
                .where(Tables.EH_ORGANIZATION_OWNER_CARS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ORGANIZATION_OWNER_CARS.STATUS.eq(OrganizationOwnerCarStatus.NORMAL.getCode()))
                .fetchInto(OrganizationOwnerCar.class);
    }

    @Override
    public List<ListOrganizationOwnerStatisticDTO> listOrganizationOwnerStatisticByGender(
            Long communityId, Byte livingStatus, List<Long> orgOwnerTypeIds, RecordMapper<Record, ListOrganizationOwnerStatisticDTO> mapper) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        SelectQuery<Record1<Long>> subQuery = context.select(
                Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID
        )
                .from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .getQuery();

        if (livingStatus != null) {
            subQuery.addConditions(Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS.eq(livingStatus));
        }
        subQuery.addGroupBy(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID);

        SelectQuery<Record2<Byte, Integer>> query = context.select(
                Tables.EH_ORGANIZATION_OWNERS.GENDER,
                DSL.count().as("count")
        )
                .from(subQuery)
                .join(Tables.EH_ORGANIZATION_OWNERS)
                .on(subQuery.field(DSL.fieldByName(Long.class, "organization_owner_id")).eq(Tables.EH_ORGANIZATION_OWNERS.ID))
                .getQuery();

        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
        if (communityId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"));
        }
        if (orgOwnerTypeIds != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ORG_OWNER_TYPE_ID.in(orgOwnerTypeIds));
        }
        query.addGroupBy(Tables.EH_ORGANIZATION_OWNERS.GENDER);
        return query.fetch(mapper);
    }

    @Override
    public List<ListOrganizationOwnerStatisticDTO> listOrganizationOwnerStatisticByAge(Long communityId, Byte livingStatus, List<Long> orgOwnerTypeIds, RecordMapper<Record, ListOrganizationOwnerStatisticDTO> mapper) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        SelectQuery<Record1<Long>> subQuery = context.select(
                Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID
        )
                .from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .getQuery();

        Field<Integer> age = DSL.year(DSL.currentDate()).sub(DSL.year(Tables.EH_ORGANIZATION_OWNERS.BIRTHDAY));
        Field<String> ageGroups = DSL.decode()
                .when(age.between(0, 10), "0-10")
                .when(age.between(11, 20), "11-20")
                .when(age.between(21, 30), "21-30")
                .when(age.between(31, 40), "31-40")
                .when(age.between(41, 50), "41-50")
                .when(age.between(51, 60), "51-60")
                .when(age.between(61, 70), "61-70")
                .when(age.between(71, 80), "71-80")
                .when(age.between(81, 90), "81-90")
                .when(age.between(91, 100), "91-100")
                .otherwise("未知").as("ageGroups");

        if (livingStatus != null) {
            subQuery.addConditions(Tables.EH_ORGANIZATION_OWNER_ADDRESS.LIVING_STATUS.eq(livingStatus));
        }
        subQuery.addGroupBy(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID);

        SelectQuery<Record3<Byte, String, Integer>> query = context.select(
                Tables.EH_ORGANIZATION_OWNERS.GENDER,
                ageGroups,
                DSL.count()
        )
                .from(subQuery)
                .join(Tables.EH_ORGANIZATION_OWNERS)
                .on(subQuery.field(DSL.fieldByName(Long.class, "organization_owner_id")).eq(Tables.EH_ORGANIZATION_OWNERS.ID))
                .getQuery();

        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
        if (communityId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"));
        }
        if (orgOwnerTypeIds != null) {
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ORG_OWNER_TYPE_ID.in(orgOwnerTypeIds));
        }
        query.addGroupBy(Tables.EH_ORGANIZATION_OWNERS.GENDER);
        query.addGroupBy(ageGroups);
        return query.fetch(mapper);
    }

    @Override
    public List<OrganizationOwnerAddress> listOrganizationOwnerAddressByAddressId(Integer namespaceId, Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(addressId))
                .fetchInto(OrganizationOwnerAddress.class);
    }

    @Override
    public CommunityPmOwner findOrganizationOwnerByCommunityIdAndContactToken(Integer namespaceId, Long communityId, String contactToken) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<CommunityPmOwner> list = context.select().from(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"))
                .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
                .and(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken))
                .fetchInto(CommunityPmOwner.class);
        if(list != null && list.size()>0){
        	return list.get(0);
        }
        return null ;
    }

    @Override
	public OrganizationOwner findOrganizationOwnerById(Long organizationOwnerId) {
    	  DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
          return context.select().from(Tables.EH_ORGANIZATION_OWNERS)
                  .where(Tables.EH_ORGANIZATION_OWNERS.ID.eq(organizationOwnerId))
                  .and(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()))
                  .fetchOneInto(OrganizationOwner.class);
	}

	@Override
    public List<EhParkingCardCategories> listParkingCardCategories() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhParkingCardCategoriesDao dao = new EhParkingCardCategoriesDao(context.configuration());
        return dao.findAll();
    }

    @Override
    public List<OrganizationOwnerAddress> listOrganizationOwnerAddressByAddressIds(Integer namespaceId, List<Long> addressIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
                .where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.in(addressIds))
                .fetchInto(OrganizationOwnerAddress.class);
    }

	@Override
	public List<OrganizationOwnerAddress> listOrganizationOwnerAuthAddressByAddressId(Integer namespaceId, Long addressId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select().from(Tables.EH_ORGANIZATION_OWNER_ADDRESS)
				.where(Tables.EH_ORGANIZATION_OWNER_ADDRESS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ADDRESS_ID.eq(addressId))
				.and(Tables.EH_ORGANIZATION_OWNER_ADDRESS.AUTH_TYPE.eq(OrganizationOwnerAddressAuthType.ACTIVE.getCode()))
				.fetchInto(OrganizationOwnerAddress.class);
	}

	@Override
	public void insertResourceReservation(PmResourceReservation resourceReservation) {
		EhPmResoucreReservationsDao dao = new EhPmResoucreReservationsDao(this.dbProvider.getDslContext(AccessSpec.readWrite()).configuration());
		dao.insert(resourceReservation);
	}

	@Override
	public List<PmResourceReservation> listReservationsByAddresses(List<Long> ids) {
		return this.dbProvider.getDslContext(AccessSpec.readOnly()).selectFrom(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID.in(ids))
				.and(Tables.EH_PM_RESOUCRE_RESERVATIONS.STATUS.notEqual(ReservationStatus.DELTED.getCode()))
				.fetchInto(PmResourceReservation.class);
	}

	@Override
	public boolean isInvolvedWithReservation(Long addressId) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<Long> ids = dslContext.select(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID)
				.from(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID.eq(addressId))
				.and(Tables.EH_PM_RESOUCRE_RESERVATIONS.STATUS.eq(ReservationStatus.ACTIVE.getCode()))
				.fetch(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID);
		if(ids.size() < 1) return false;
		return true;
	}

	@Override
	public List<PmResourceReservation> findReservationByAddress(Long addressId, ReservationStatus status) {
		return this.dbProvider.getDslContext(AccessSpec.readOnly()).selectFrom(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID.eq(addressId))
				.and(Tables.EH_PM_RESOUCRE_RESERVATIONS.STATUS.eq(status.getCode()))
				.fetchInto(PmResourceReservation.class);
	}

	@Override
	public void updateReservation(Long reservationId, Timestamp startTime, Timestamp endTime, Long enterpriseCustomerId,Long addressId) {
		this.dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.set(Tables.EH_PM_RESOUCRE_RESERVATIONS.ENTERPRISE_CUSTOMER_ID ,enterpriseCustomerId)
				.set(Tables.EH_PM_RESOUCRE_RESERVATIONS.START_TIME, startTime)
				.set(Tables.EH_PM_RESOUCRE_RESERVATIONS.END_TIME, endTime)
				.set(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID, addressId)
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID.eq(reservationId))
				.execute();
	}

	@Override
	public Long changeReservationStatus(Long reservationId, Byte status) {
	    LOGGER.info("sdhfsdhfjsdjfsj:   sdfs");
		this.dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.set(Tables.EH_PM_RESOUCRE_RESERVATIONS.STATUS, status)
				.set(Tables.EH_PM_RESOUCRE_RESERVATIONS.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID.eq(reservationId))
				.execute();
		return	this.dbProvider.getDslContext(AccessSpec.readWrite()).select(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID)
				.from(Tables.EH_PM_RESOUCRE_RESERVATIONS)
				.where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID.eq(reservationId))
                .fetchOne(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID);
	}

	@Override
	public List<ReservationInfo> listRunningReservations() {
	    List<ReservationInfo> infos = new ArrayList<>();
	    this.dbProvider.getDslContext(AccessSpec.readOnly())
				.select(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID, Tables.EH_PM_RESOUCRE_RESERVATIONS.END_TIME, Tables.EH_PM_RESOUCRE_RESERVATIONS.ID)
				.from(Tables.EH_PM_RESOUCRE_RESERVATIONS)
                .where(Tables.EH_PM_RESOUCRE_RESERVATIONS.STATUS.eq(ReservationStatus.ACTIVE.getCode()))
				.fetch()
				.forEach(r -> {
					ReservationInfo info = new ReservationInfo();
					info.setAddressId(r.getValue(Tables.EH_PM_RESOUCRE_RESERVATIONS.ADDRESS_ID));
					info.setEndTime(r.getValue(Tables.EH_PM_RESOUCRE_RESERVATIONS.END_TIME));
					info.setReservationId(r.getValue(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID));
					infos.add(info);
				});
		return infos;
	}

    @Override
    public Byte getReservationPreviousLivingStatusById(Long reservationId) {
        return this.dbProvider.getDslContext(AccessSpec.readOnly())
                .select(Tables.EH_PM_RESOUCRE_RESERVATIONS.PREVIOUS_LIVING_STATUS)
                .from(Tables.EH_PM_RESOUCRE_RESERVATIONS)
                .where(Tables.EH_PM_RESOUCRE_RESERVATIONS.ID.eq(reservationId))
                .fetchOne(Tables.EH_PM_RESOUCRE_RESERVATIONS.PREVIOUS_LIVING_STATUS);
    }

    @Override
    public ParkingCardCategory findParkingCardCategory(Byte cardType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_PARKING_CARD_CATEGORIES)
                .where(Tables.EH_PARKING_CARD_CATEGORIES.CARD_TYPE.eq(cardType))
                .fetchOneInto(ParkingCardCategory.class);
    }

    @Override
    public int deleteOrganizationOwnerAttachmentByOwnerId(Integer namespaceId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return context.delete(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.OWNER_ID.eq(id))
                .execute();
    }

	@Override

	public List<CommunityPmOwner> listCommunityPmOwnersByTel(Integer namespaceId, Long communityId, String tel) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<CommunityPmOwner> result  = new ArrayList<CommunityPmOwner>();
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(OrganizationOwnerStatus.NORMAL.getCode()));
        if (!StringUtils.isEmpty(tel)) {
        	query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_EXTRA_TELS.like("%"+tel+"%"));
		}
        if (communityId != null) {
        	query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%"+communityId+"%"));
		}
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("listCommunityPmOwnersByTel, sql = {}" , query.getSQL());
			LOGGER.debug("listCommunityPmOwnersByTel, bindValues = {}" , query.getBindValues());
		}
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
		return result;
	}

	public PmResourceReservation findReservationById(Long reservationId) {
		EhPmResoucreReservationsDao dao = new EhPmResoucreReservationsDao(this.dbProvider.getDslContext(AccessSpec.readWrite()).configuration());
		return ConvertHelper.convert(dao.findById(reservationId), PmResourceReservation.class);
	}

	@Override
	public void updateReservation(PmResourceReservation oldReservation) {
		EhPmResoucreReservationsDao dao = new EhPmResoucreReservationsDao(this.dbProvider.getDslContext(AccessSpec.readWrite()).configuration());
		dao.update(oldReservation);

	}

	//一房一价
	@Override
	public void createAuthorizePrice(AddressProperties addressProperties) {
		long id = this.dbProvider.allocPojoRecordId(EhAddressProperties.class);
		addressProperties.setId(id);
		addressProperties.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		addressProperties.setOperatorTime(addressProperties.getCreateTime());
		addressProperties.setStatus(ContractTemplateStatus.ACTIVE.getCode()); //有效的状态
		addressProperties.setCreatorUid(UserContext.currentUserId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddressProperties.class, id));
        EhAddressPropertiesDao dao = new EhAddressPropertiesDao(context.configuration());
        dao.insert(addressProperties);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddressProperties.class, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<AddressProperties> listAuthorizePrices(Integer namespaceId, Long buildingId, Long communityId, Long pageAnchor, Integer pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddressProperties.class));
		SelectJoinStep<Record> query = context.select(Tables.EH_ADDRESS_PROPERTIES.fields()).from(Tables.EH_ADDRESS_PROPERTIES);
		Condition cond = Tables.EH_ADDRESS_PROPERTIES.NAMESPACE_ID.eq(namespaceId);
		cond = cond.and(Tables.EH_ADDRESS_PROPERTIES.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));
		cond = cond.and(Tables.EH_ADDRESS_PROPERTIES.BUILDING_ID.eq(buildingId));
		cond = cond.and(Tables.EH_ADDRESS_PROPERTIES.COMMUNITY_ID.eq(communityId));
		
		if(null != pageAnchor && pageAnchor != 0){
			cond = cond.and(Tables.EH_ADDRESS_PROPERTIES.ID.gt(pageAnchor));
		}
		query.orderBy(Tables.EH_ADDRESS_PROPERTIES.CREATE_TIME.desc());
		
		if(null != pageSize)
			query.limit(pageSize);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("listCommunityPmOwnersByTel, sql = {}" , query.getSQL());
			LOGGER.debug("listCommunityPmOwnersByTel, bindValues = {}" , query.getBindValues());
		}
		
		List<AddressProperties> result = query.where(cond).fetch().
				map(new DefaultRecordMapper(Tables.EH_ADDRESS_PROPERTIES.recordType(), AddressProperties.class));

		return result;
	}

	@Override
	public AddressProperties findAddressPropertiesById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhAddressProperties.class, id));
		EhAddressPropertiesDao dao = new EhAddressPropertiesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), AddressProperties.class);
	}

	@Override
	public void updateAuthorizePrice(AddressProperties addressProperties) {
		assert(addressProperties.getId() != null);
		addressProperties.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		addressProperties.setOperatorUid(UserContext.currentUserId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddressProperties.class, addressProperties.getId()));
        EhAddressPropertiesDao dao = new EhAddressPropertiesDao(context.configuration());
        dao.update(addressProperties);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddressProperties.class, addressProperties.getId());
	}

	@Override
	public void deleteAuthorizePrice(AddressProperties addressProperties) {
		assert(addressProperties.getId() != null);
		addressProperties.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		addressProperties.setOperatorUid(UserContext.currentUserId());
		addressProperties.setStatus(ContractTemplateStatus.INACTIVE.getCode());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddressProperties.class, addressProperties.getId()));
        EhAddressPropertiesDao dao = new EhAddressPropertiesDao(context.configuration());
        dao.update(addressProperties);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddressProperties.class, addressProperties.getId());
	}

	@Override
	public AddressProperties findAddressPropertiesByApartmentId(Community community, Long buildingId, Long addressId) {
		
		final AddressProperties[] result = new AddressProperties[1];

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhAddressPropertiesRecord> query = context.selectQuery(Tables.EH_ADDRESS_PROPERTIES);
		query.addConditions(Tables.EH_ADDRESS_PROPERTIES.NAMESPACE_ID.eq(community.getNamespaceId()));
		query.addConditions(Tables.EH_ADDRESS_PROPERTIES.BUILDING_ID.eq(buildingId));
		query.addConditions(Tables.EH_ADDRESS_PROPERTIES.ADDRESS_ID.eq(addressId));
		query.addConditions(Tables.EH_ADDRESS_PROPERTIES.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));

		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, AddressProperties.class);
			return null;
		});

		return result[0];
	}

	@Override
	public CommunityPmOwner findOrganizationOwnerByContactToken(String contactToken, Integer namespaceId){
		final CommunityPmOwner[] result = new CommunityPmOwner[1];

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq((byte)1));

		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, CommunityPmOwner.class);
			return null;
		});

		return result[0];
	}

	@Override
	public CommunityPmOwner findOrganizationOwnerByContactExtraTels(String contactToken, Integer namespaceId){
		final CommunityPmOwner[] result = new CommunityPmOwner[1];

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_EXTRA_TELS.like(contactToken));
		query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));

		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, CommunityPmOwner.class);
			return null;
		});

		return result[0];
	}


}
