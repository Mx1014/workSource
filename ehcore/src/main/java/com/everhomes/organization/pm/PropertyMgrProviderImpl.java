// @formatter:off
package com.everhomes.organization.pm;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationTask;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.pm.ListPropInvitedUserCommandResponse;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOrganizationAddressMappingsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillItemsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationContactsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMembersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationOwnersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationTasksDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAddressMappings;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBillItems;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBills;
import com.everhomes.server.schema.tables.pojos.EhOrganizationContacts;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwners;
import com.everhomes.server.schema.tables.pojos.EhOrganizationTasks;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhOrganizationAddressMappingsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationBillItemsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationBillsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationContactsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationTasksRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.Tuple;


@Component
public class PropertyMgrProviderImpl implements PropertyMgrProvider {

	@Autowired
	private DbProvider dbProvider;


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
	public int countCommunityAddressMappings(long organizationId,Byte livingStatus) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS);
		Condition condition = Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId);
		if(livingStatus != null) {
			condition = condition.and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.LIVING_STATUS.eq(livingStatus));
		}
		return step.where(condition).fetchOneInto(Integer.class);
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
	public void createPropOwner(CommunityPmOwner communityPmOwner) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
		dao.insert(communityPmOwner);

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationOwners.class, null);
	}

	@CacheEvict(value = "CommunityPmOwner", key="communityPmOwner.#id")
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

	@Cacheable(value="CommunityPmOwner", key="#id")
	@Override
	public CommunityPmOwner findPropOwnerById(long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
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
		query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, CommunityPmOwner.class));
			return null;
		});
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
}
