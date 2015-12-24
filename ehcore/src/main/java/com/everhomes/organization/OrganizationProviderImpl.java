// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pm.OrganizationScopeCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBusinessAssignedScopesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationAssignedScopesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillingAccountsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillingTransactionsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMembersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationOrdersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationTasksDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationsDao;
import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBillingAccounts;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBillingTransactions;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBills;
import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunities;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOrders;
import com.everhomes.server.schema.tables.pojos.EhOrganizationTasks;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.records.EhOrganizationAssignedScopesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationBillingAccountsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOrdersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationTasksRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
@Component
public class OrganizationProviderImpl implements OrganizationProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
    private ShardingProvider shardingProvider;

	@Override
	public void createOrganization(Organization department) {
		
		long id = shardingProvider.allocShardableContentId(EhOrganizations.class).second();
		department.setId(id);
		department.setPath(department.getPath() + "/" + id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, id));
		EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
		dao.insert(department);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizations.class, null); 

	}


	@Override
	public void updateOrganization(Organization department){
		assert(department.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
		dao.update(department);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
	}


	@Override
	public void deleteOrganization(Organization department){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
		dao.deleteById(department.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
	}


	@Override
	public void deleteOrganizationById(Long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, id);
	}


	@Override
	public Organization findOrganizationById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), Organization.class);
	}

	@Override
	public List<Organization> findOrganizationByCommunityId(Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
		query.addJoin(Tables.EH_ORGANIZATION_COMMUNITIES, JoinType.LEFT_OUTER_JOIN, 
				Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));
		query.setDistinct(true);
		if(communityId != null)
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));

		List<EhOrganizationsRecord> records = query.fetch().map(new EhOrganizationRecordMapper());
		List<Organization> organizations = records.stream().map((r) -> {
			return ConvertHelper.convert(r, Organization.class);
		}).collect(Collectors.toList());

		return organizations;
	}
	
    @Override
    public List<Organization> findOrganizationByPath(String path) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result  = new ArrayList<Organization>();
        context.select().from(Tables.EH_ORGANIZATIONS)
            .where(Tables.EH_ORGANIZATIONS.PATH.eq(path))
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, Organization.class));
                return null;
            });
        
        return result;
    }	

	@Override
	public List<Organization> listOrganizations(String organizationType,String name,Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<Organization> result  = new ArrayList<Organization>();
		SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
		if(organizationType != null && !"".equals(organizationType)) {
			query.addConditions(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType));
		}
		if(name != null && !"".equals(name)) {
			query.addConditions(Tables.EH_ORGANIZATIONS.NAME.eq(name));
		}

		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Organization.class));
			return null;
		});
		return result;
	}

	@Override
	public void createOrganizationMember(OrganizationMember departmentMember) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersRecord record = ConvertHelper.convert(departmentMember, EhOrganizationMembersRecord.class);
		InsertQuery<EhOrganizationMembersRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_MEMBERS);
		query.setRecord(record);
		query.setReturning(Tables.EH_ORGANIZATION_MEMBERS.ID);
		query.execute();

		departmentMember.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationMembers.class, null); 

	}


	@Override
	public void updateOrganizationMember(OrganizationMember departmentMember){
		assert(departmentMember.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.update(departmentMember);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
	}

	@Override
	public void deleteOrganizationMemberById(Long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, id);
	}


	@Override
	public OrganizationMember findOrganizationMemberById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), OrganizationMember.class);
	}

	@Override
	public List<OrganizationMember> listOrganizationMembers(Long orgId, Long memberUid,Long offset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<OrganizationMember> result  = new ArrayList<OrganizationMember>();
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		if(orgId != null && orgId > 0){
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId));
		}
		if(memberUid != null && memberUid > 0) {
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
		}
		
		query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
		query.addLimit(offset.intValue(), pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, OrganizationMember.class));
			return null;
		});
		return result;
	}
	

	@Override
	public List<OrganizationMember> listOrganizationMembers(Long memberUid) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<OrganizationMember> result  = new ArrayList<OrganizationMember>();
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		if(memberUid != null && memberUid > 0) {
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
		}
		query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, OrganizationMember.class));
			return null;
		});
		return result;
	}

	@Override
	public void createOrganizationCommunity(OrganizationCommunity departmentCommunity) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationCommunitiesRecord record = ConvertHelper.convert(departmentCommunity, EhOrganizationCommunitiesRecord.class);
		InsertQuery<EhOrganizationCommunitiesRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		query.setRecord(record);
		query.setReturning(Tables.EH_ORGANIZATION_COMMUNITIES.ID);
		query.execute();

		departmentCommunity.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationCommunities.class, null); 

	}


	@Override
	public void updateOrganizationCommunity(OrganizationCommunity departmentCommunity){
		assert(departmentCommunity.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
		dao.update(departmentCommunity);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
	}


	@Override
	public void deleteOrganizationCommunity(OrganizationCommunity departmentCommunity){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
		dao.deleteById(departmentCommunity.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
	}


	@Override
	public void deleteOrganizationCommunityById(Long id){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
		dao.deleteById(id);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, id);
	}

	@Override
	public OrganizationCommunity findOrganizationCommunityByOrgIdAndCmmtyId(Long orgId, Long cmmtyId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES);
		Condition condition = Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId.longValue());
		condition = condition.and(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(cmmtyId.longValue()));
		final OrganizationCommunity[] result = new OrganizationCommunity[1];
		step.where(condition).fetch().map(r -> {
			result[0] = ConvertHelper.convert(r,OrganizationCommunity.class);
			return null;
		});
		return result[0];
	}

	@Override
	public OrganizationCommunity findOrganizationCommunityById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), OrganizationCommunity.class);
	}

	@Override
	public List<OrganizationCommunity> listOrganizationCommunities(Long organizationId,Integer pageOffset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<OrganizationCommunity> result  = new ArrayList<OrganizationCommunity>();
		SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		if(organizationId != null && organizationId > 0){
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
		}

		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
			return null;
		});
		return result;
	}

	@Override
	public List<OrganizationCommunity> listOrganizationCommunities(Long organizationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<OrganizationCommunity> result  = new ArrayList<OrganizationCommunity>();
		SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		if(organizationId != null && organizationId > 0){
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
		}
		query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
			return null;
		});

		if(result == null || result.size() == 0) {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("The community for the organization is not found, organizationId=" + organizationId);
				LOGGER.warn(query.getSQL());
			}
		}

		return result;
	}

	@Override
	public List<OrganizationCommunity> listOrganizationByCommunityId(Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<OrganizationCommunity> result  = new ArrayList<OrganizationCommunity>();
		SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		if(communityId != null && communityId > 0){
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
		}
		query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
			return null;
		});
		return result;
	}

	@Override
	public OrganizationCommunity findOrganizationProperty(Long communityId) {
		final OrganizationCommunity[] result = new OrganizationCommunity[1];
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		if(communityId != null && communityId > 0){
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
		}
		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, OrganizationCommunity.class);
			return null;
		});
		return result[0];
	}

	@Override
	public OrganizationCommunity findOrganizationPropertyCommunity(Long organizationId) {
		final OrganizationCommunity[] result = new OrganizationCommunity[1];
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
		if(organizationId != null && organizationId > 0){
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
		}
		query.fetch().map((r) -> {
			if(r != null)
				result[0] = ConvertHelper.convert(r, OrganizationCommunity.class);
			return null;
		});
		return result[0];
	}

	@Override
	public int countOrganizations(String type,String name) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATIONS);
		Condition condition = null;
		if(type != null && !type.equals(""))
			condition = Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(type);
		if(!StringUtils.isEmpty(name)) {
		    if(condition != null) {
		        condition = condition.and(Tables.EH_ORGANIZATIONS.NAME.eq(name));
		    } else {
		        condition = Tables.EH_ORGANIZATIONS.NAME.eq(name);
		    }
		}
		
		if(condition == null)
			return step.fetchOneInto(Integer.class);
		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public int countOrganizationMembers(Long departmentId, Long memberUid) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS);
		Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(departmentId);
		if(memberUid != null && memberUid > 0)
			condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public int countOrganizationCommunitys(Long organizationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATION_COMMUNITIES);
		Condition condition = Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId);
		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public List<OrganizationCommunityDTO> findOrganizationCommunityByCommunityId(
			Long communityId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<OrganizationCommunityDTO> list = new ArrayList<OrganizationCommunityDTO>();

		context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES)
		.where(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId))
		.fetch().map(r -> {
			list.add(ConvertHelper.convert(r, OrganizationCommunityDTO.class));
			return null;
		});

		return list;
	}

	@Override
	public OrganizationDTO findOrganizationByIdAndOrgType(Long organizationId,
			String organizationType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		Record record = context.select().from(Tables.EH_ORGANIZATIONS)
				.where(Tables.EH_ORGANIZATIONS.ID.eq(organizationId).and(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType)))
				.fetchOne();

		if(record != null)
			return ConvertHelper.convert(record, OrganizationDTO.class);

		return null;
	}

	@Override
	public void createOrganizationTask(OrganizationTask task) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		InsertQuery<EhOrganizationTasksRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_TASKS);
		query.setRecord(ConvertHelper.convert(task, EhOrganizationTasksRecord.class));
		query.setReturning(Tables.EH_ORGANIZATION_TASKS.ID);
		query.execute();
		task.setId(query.getReturnedRecord().value1());

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationTasks.class, null); 
	}

	@Override
	public Organization findOrganizationByCommunityIdAndOrgType(Long communityId,String organizationType){

		List<Organization> list = new ArrayList<Organization>();

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		context.select().from(Tables.EH_ORGANIZATIONS)
		.join(Tables.EH_ORGANIZATION_COMMUNITIES).on(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID))
		.where(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId).and(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType)))
		.fetch().map(r -> {
			Organization org = new Organization();
			org.setAddressId(r.getValue(Tables.EH_ORGANIZATIONS.ADDRESS_ID));
			org.setDescription(r.getValue(Tables.EH_ORGANIZATIONS.DESCRIPTION));
			org.setId(r.getValue(Tables.EH_ORGANIZATIONS.ID));
			org.setLevel(r.getValue(Tables.EH_ORGANIZATIONS.LEVEL));
			org.setName(r.getValue(Tables.EH_ORGANIZATIONS.NAME));
			org.setOrganizationType(r.getValue(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE));
			org.setParentId(r.getValue(Tables.EH_ORGANIZATIONS.PARENT_ID));
			org.setPath(r.getValue(Tables.EH_ORGANIZATIONS.PATH));
			org.setStatus(r.getValue(Tables.EH_ORGANIZATIONS.STATUS));

			list.add(org);
			return null;
		});

		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public List<OrganizationBillingTransactions> listOrganizationBillingTransactions(
			Condition condition, long offset, int pageSize) {
		List<OrganizationBillingTransactions> list = new ArrayList<OrganizationBillingTransactions>();

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS)
				.where(condition)
				.orderBy(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.desc(),Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.TARGET_ACCOUNT_ID.asc())
				.limit(pageSize).offset((int)offset)
				.fetch();

		if(records != null && !records.isEmpty()){
			records.stream().map(r -> {
				list.add(ConvertHelper.convert(r, OrganizationBillingTransactions.class));
				return null;
			}).toArray();
		}

		return list;
	}

	@Override
	public List<CommunityPmOwner> listOrganizationOwnerByAddressIdAndOrgId(
			Long addressId, Long organizationId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_OWNERS)
				.where(Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_OWNERS.ADDRESS_ID.eq(addressId)))
				.fetch();

		List<CommunityPmOwner> list = new ArrayList<CommunityPmOwner>();

		if(records != null && !records.isEmpty()){
			records.stream().map(r -> {
				list.add(ConvertHelper.convert(r, CommunityPmOwner.class));
				return null;
			}).toArray();
		}

		return list;
	}

	@Cacheable(value="findOrganizationTaskById", key="#id", unless="#result == null")
	public OrganizationTask findOrganizationTaskById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		EhOrganizationTasksDao dao = new EhOrganizationTasksDao(context.configuration());
		EhOrganizationTasks task = dao.findById(id);

		return ConvertHelper.convert(task, OrganizationTask.class);
	}

	@Override
	public OrganizationBillingAccount findOrganizationBillingAccount(
			Long organizationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLING_ACCOUNTS)
				.where(Tables.EH_ORGANIZATION_BILLING_ACCOUNTS.OWNER_ID.eq(organizationId))
				.fetch();

		if(records != null && !records.isEmpty()){
			return ConvertHelper.convert(records.get(0), OrganizationBillingAccount.class);
		}

		return null;
	}

	@Override
	public void createOrganizationBillingAccount(OrganizationBillingAccount oAccount) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		InsertQuery<EhOrganizationBillingAccountsRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_BILLING_ACCOUNTS);
		query.setRecord(ConvertHelper.convert(oAccount,EhOrganizationBillingAccountsRecord.class));
		query.setReturning(Tables.EH_ORGANIZATION_BILLING_ACCOUNTS.ID);
		query.execute();

		oAccount.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationBillingAccounts.class, null);

	}

	@Override
	public void createOrganizationBillingTransaction(OrganizationBillingTransactions orgTx) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillingTransactionsDao dao = new EhOrganizationBillingTransactionsDao(context.configuration());
		dao.insert(ConvertHelper.convert(orgTx, EhOrganizationBillingTransactions.class));
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhOrganizationBillingTransactions.class, null);
	}

	@Override
	public void updateOrganizationBillingAccount(OrganizationBillingAccount oAccount) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationBillingAccountsDao dao = new EhOrganizationBillingAccountsDao(context.configuration());
		dao.update(ConvertHelper.convert(oAccount,EhOrganizationBillingAccounts.class));
		DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhOrganizationBillingAccounts.class, null);
	}


	@Override
	public List<Organization> listOrganizationByCondition(Condition condition) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATIONS).where(condition).fetch();

		List<Organization> list = new ArrayList<Organization>();
		if(records != null && !records.isEmpty()){
			records.stream().map(r -> {
				list.add(ConvertHelper.convert(r, Organization.class));
				return null;
			}).toArray();
		}

		return list;
	}


	@Override
	public List<CommunityPmOwner> listOrgOwnerByOrgIdAndAddressId(
			Long organizationId, Long addressId) {
		List<CommunityPmOwner> list = new ArrayList<CommunityPmOwner>();

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_OWNERS)
				.where(Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_OWNERS.ADDRESS_ID.eq(addressId)))
				.fetch();

		if(records != null && !records.isEmpty()){
			for(Record record : records){
				list.add(ConvertHelper.convert(record, CommunityPmOwner.class));
			}
		}
		return list;
	}


	@Override
	public CommunityPmBill findOranizationBillById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(Tables.EH_ORGANIZATION_BILLS.ID.eq(id))
				.fetch();
		if(records != null && !records.isEmpty())
			return ConvertHelper.convert(records.get(0), CommunityPmBill.class);
		return null;
	}


	@Override
	public List<OrganizationBillingTransactionDTO> listOrgBillTxByOrgId(Long orgId,int resultCode,Timestamp startTime, Timestamp endTime, String address, long offset, int pageSize) {

		List<OrganizationBillingTransactionDTO> list = new ArrayList<OrganizationBillingTransactionDTO>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		
		SelectQuery<Record> query = context.selectQuery();
		query.addFrom(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS);
		query.addJoin(Tables.EH_ORGANIZATION_ORDERS, Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.ORDER_ID.eq(Tables.EH_ORGANIZATION_ORDERS.ID));
		query.addJoin(Tables.EH_ORGANIZATION_BILLS, Tables.EH_ORGANIZATION_BILLS.ID.eq(Tables.EH_ORGANIZATION_ORDERS.BILL_ID));

		query.addConditions(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.OWNER_ID.eq(orgId));
		query.addConditions(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.RESULT_CODE_ID.eq(resultCode));
		if(startTime != null && endTime != null)
			query.addConditions(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.greaterOrEqual(startTime)
					.and(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.lessOrEqual(endTime)));
		if(address != null && !address.equals(""))
			query.addConditions(Tables.EH_ORGANIZATION_BILLS.ADDRESS.like("%"+address+"%"));
		

		query.addOrderBy(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.desc(),Tables.EH_ORGANIZATION_BILLS.ADDRESS.asc());
		query.addLimit((int)offset, pageSize);
		//System.out.println(query.getSQL());
		query.execute();

		Result<Record> records = query.getResult();
		if(records != null && !records.isEmpty()){
			for(Record r : records){
				OrganizationBillingTransactionDTO orgBillTxDto = new OrganizationBillingTransactionDTO();
				orgBillTxDto.setAddress(r.getValue(Tables.EH_ORGANIZATION_BILLS.ADDRESS));
				orgBillTxDto.setAddressId(r.getValue(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID));
				orgBillTxDto.setChargeAmount(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CHARGE_AMOUNT));
				orgBillTxDto.setCreateTime(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME).getTime());
				orgBillTxDto.setDescription(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.DESCRIPTION));
				orgBillTxDto.setId(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.ID));
				orgBillTxDto.setOrganizationId(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.OWNER_ID));
				orgBillTxDto.setPaidType(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.PAID_TYPE));
				orgBillTxDto.setTxType(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.TX_TYPE));
				orgBillTxDto.setVendor(r.getValue(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.VENDOR));
				list.add(orgBillTxDto);
			}
		}

		return list;
	}


	@Override
	public List<OrganizationOwners> listOrganizationOwnersByOrgIdAndAddressId(
			Long organizationId, Long addressId) {
		List<OrganizationOwners> list = new ArrayList<OrganizationOwners>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_OWNERS)
				.where(Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId)
						.and(Tables.EH_ORGANIZATION_OWNERS.ADDRESS_ID.eq(addressId)))
						.fetch();
		if(records != null && !records.isEmpty()){
			for(Record r : records)
				list.add(ConvertHelper.convert(r, OrganizationOwners.class));
		}
		return list;
	}


	@Override
	public CommunityAddressMapping findOrganizationAddressMappingByOrgIdAndAddress(
			Long organizationId, String address) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
				.where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId)
						.and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ADDRESS.eq(address))).fetch();

		if(records != null && !records.isEmpty()){
			return ConvertHelper.convert(records.get(0), CommunityAddressMapping.class);
		}
		return null;
	}


	@Override
	public void deleteOrganizationBillById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhOrganizationBillsDao dao = new EhOrganizationBillsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBills.class, id);
	}


	@Override
	public void updateOrganizationBill(CommunityPmBill communBill) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhOrganizationBillsDao dao = new EhOrganizationBillsDao(context.configuration());
		dao.update(ConvertHelper.convert(communBill, EhOrganizationBills.class));
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBills.class, null);
	}


	@Override
	public List<CommunityPmBill> listOrganizationBillsByAddressId(Long addreddId,
			long offset, int pageSize) {
		List<CommunityPmBill> list = new ArrayList<CommunityPmBill>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_BILLS)
				.where(Tables.EH_ORGANIZATION_BILLS.ENTITY_ID.eq(addreddId)).fetch();

		if(records != null && !records.isEmpty()){
			for(Record r : records)
				list.add(ConvertHelper.convert(r, CommunityPmBill.class));
		}
		return list;
	}


	@Override
	public OrganizationCommunity findOrganizationCommunityByOrgId(Long orgId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES)
				.where(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId)).fetch();

		if(records != null && !records.isEmpty())
			return ConvertHelper.convert(records.get(0), OrganizationCommunity.class);
		return null;
	}

	@Override
	public OrganizationTask findOrgTaskByOrgIdAndEntityId(Long orgId, Long entityId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASKS)
				.where(Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(orgId)
						.and(Tables.EH_ORGANIZATION_TASKS.APPLY_ENTITY_ID.eq(entityId)))
						.fetch();

		if(records != null && !records.isEmpty()){
			return ConvertHelper.convert(records.get(0), OrganizationTask.class);
		}
		return null;
	}

	@Caching(evict={@CacheEvict(value="findOrganizationTaskById", key="#task.id")})
	public void updateOrganizationTask(OrganizationTask task) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationTasksDao dao = new EhOrganizationTasksDao(context.configuration());
		dao.update(task);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, OrganizationTask.class, task.getId());
	}

	@Override
	public void deleteOrganizationMember(OrganizationMember departmentMember){

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
		dao.deleteById(departmentMember.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
	}

	@Override
	public List<OrganizationTask> listOrganizationTasksByOrgIdAndType(Long organizationId, String taskType, Byte taskStatus, int pageSize, long offset) {
		List<OrganizationTask> list = new ArrayList<OrganizationTask>();
		Condition condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(organizationId);
		if(taskType != null && !taskType.isEmpty())
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));
		if(taskStatus != null)
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(taskStatus));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASKS)
				.where(condition)
				.orderBy(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.desc())
				.limit(pageSize).offset((int)offset)
				.fetch();
		if(records != null && !records.isEmpty()){
			for(Record r : records){
				list.add(ConvertHelper.convert(r, OrganizationTask.class));
			}
		}
		return list;
	}

	@Override
	public OrganizationMember findOrganizationMemberByOrgIdAndUId(Long userId,
			Long organizationId) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId));
		Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();

		if(r != null)
			return ConvertHelper.convert(r, OrganizationMember.class);
		return null;
	}


	@Override
	public List<OrganizationMember> listOrganizationMembersByOrgId(Long orgId) {
		List<OrganizationMember> list = new ArrayList<OrganizationMember>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId)).fetch();

		if(records != null && !records.isEmpty()){
			for(Record r : records)
				list.add(ConvertHelper.convert(r, OrganizationMember.class));
		}
		return list;
	}


	@Override
	public Organization findOrganizationByName(String name) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record r = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name)).fetchOne();
		if(r != null)
			return ConvertHelper.convert(r, Organization.class);
		return null;
	}


	@Override
	public void createOrganizationOrder(OrganizationOrder order) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		InsertQuery<EhOrganizationOrdersRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_ORDERS);
		query.setRecord(ConvertHelper.convert(order, EhOrganizationOrdersRecord.class));
		query.setReturning(Tables.EH_ORGANIZATION_ORDERS.ID);
		query.execute();
		order.setId(query.getReturnedRecord().getId());

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationOrders.class, null);
	}


	@Override
	public List<OrganizationOrder> listOrgOrdersByBillIdAndStatus(Long billId,Byte status) {
		List<OrganizationOrder> list = new ArrayList<OrganizationOrder>();
		
		Condition condition = null;
		if(billId != null)
			condition = Tables.EH_ORGANIZATION_ORDERS.BILL_ID.eq(billId);
		if(status != null)
			condition = condition.and(Tables.EH_ORGANIZATION_ORDERS.STATUS.eq(status));
		
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		
		SelectQuery<EhOrganizationOrdersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ORDERS);
		if(condition != null){
			query.addConditions(condition);
		}
		query.fetch().map(r -> {
			list.add(ConvertHelper.convert(r, OrganizationOrder.class));
			return null;
		});

		return list;
	}


	@Override
	public OrganizationOrder findOrganizationOrderById(Long orderId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select().from(Tables.EH_ORGANIZATION_ORDERS)
				.where(Tables.EH_ORGANIZATION_ORDERS.ID.eq(orderId))
				.fetchOne();
		if(record != null)
			return ConvertHelper.convert(record, OrganizationOrder.class);
		return null;
	}


	@Override
	public void updateOrganizationOrder(OrganizationOrder order) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationOrdersDao dao = new EhOrganizationOrdersDao(context.configuration());
		dao.update(ConvertHelper.convert(order, EhOrganizationOrders.class));
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOrders.class, order.getId());
		
	}

	@Override
	public List<OrganizationOrder> listOrgOrdersByStatus(Byte status) {
		List<OrganizationOrder> list = new ArrayList<OrganizationOrder>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		
		context.select().from(Tables.EH_ORGANIZATION_ORDERS).where(Tables.EH_ORGANIZATION_ORDERS.STATUS.eq(status))
		.fetch().map(r -> {
			list.add(ConvertHelper.convert(r, OrganizationOrder.class));
			return null;
		});

		return list;
	}


	@Override
	public CommunityAddressMapping findOrganizationAddressMappingByAddressId(Long addressId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
				.where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId)).fetch();

		if(records != null && !records.isEmpty()){
			return ConvertHelper.convert(records.get(0), CommunityAddressMapping.class);
		}
		return null;
	}


	@Override
	public List<Organization> listOrganizationByName(String orgName,String orgType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Organization> list = context.select().from(Tables.EH_ORGANIZATIONS)
				.where(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(orgType).and(Tables.EH_ORGANIZATIONS.NAME.eq(orgName)))
				.fetch().map(r -> {
					return ConvertHelper.convert(r, Organization.class);
				});
				
		if(list == null || list.isEmpty())
			return null;
		return list;
	}


	@Override
	public void addPmBuilding(OrganizationAssignedScopes pmBuilding) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAssignedScopesRecord record = ConvertHelper.convert(pmBuilding, EhOrganizationAssignedScopesRecord.class);
		InsertQuery<EhOrganizationAssignedScopesRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES);
		query.setRecord(record);
		query.setReturning(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES.ID);
		query.execute();

		pmBuilding.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationAssignedScopes.class, null); 
	}


	@Override
	public void deletePmBuildingById(Long id) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAssignedScopesDao dao = new EhOrganizationAssignedScopesDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAssignedScopes.class, id);
		
	}


	@Override
	public List<OrganizationAssignedScopes> findPmBuildingId(Long orgId) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<OrganizationAssignedScopes> list = context.select().from(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES)
				.where(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES.SCOPE_CODE.eq(OrganizationScopeCode.BUILDING.getCode()).and(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES.ORGANIZATION_ID.eq(orgId)))
				.fetch().map(r -> {
					return ConvertHelper.convert(r, OrganizationAssignedScopes.class);
				});
				
		if(list == null || list.isEmpty())
			return null;
		return list;
	}


	@Override
	public List<OrganizationAssignedScopes> findUnassignedBuildingId(Long orgId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<OrganizationAssignedScopes> list = context.select().from(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES)
				.where(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES.SCOPE_CODE.eq(OrganizationScopeCode.BUILDING.getCode()).and(Tables.EH_ORGANIZATION_ASSIGNED_SCOPES.ORGANIZATION_ID.eq(orgId)))
				.fetch().map(r -> {
					return ConvertHelper.convert(r, OrganizationAssignedScopes.class);
				});
				
		if(list == null || list.isEmpty())
			return null;
		return list;
	}


	@Override
	public List<Organization> listPmManagements(ListingLocator locator,
			int count, Long orgId, Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
		query.addJoin(Tables.EH_ORGANIZATION_COMMUNITIES, JoinType.LEFT_OUTER_JOIN, 
				Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));
		query.setDistinct(true);
		
		if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.gt(locator.getAnchor()));
		if(orgId != null) {
			Condition con = Tables.EH_ORGANIZATIONS.ID.eq(orgId);
			con = con.or(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(orgId));
			query.addConditions(con);
		}
		if(communityId != null) {
			query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
		}

		query.addLimit(count);
		List<EhOrganizationsRecord> records = query.fetch().map(new EhOrganizationRecordMapper());
		List<Organization> organizations = records.stream().map((r) -> {
			return ConvertHelper.convert(r, Organization.class);
		}).collect(Collectors.toList());

		if(organizations.size() > 0) {
            locator.setAnchor(organizations.get(organizations.size() -1).getId());
        }
		return organizations;
	}


	@Override
	public void deletePmBuildingByOrganizationId(Long organizationId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOrganizationAssignedScopesDao dao = new EhOrganizationAssignedScopesDao(context.configuration()); 
        List<EhOrganizationAssignedScopes> assignedScopes = dao.fetchByOrganizationId(organizationId);
        if(assignedScopes != null && !assignedScopes.isEmpty()){
            assignedScopes.forEach(r -> deletePmBuildingById(r.getId()));
        }
		
	}


	@Override
	public List<OrganizationTask> listOrganizationTasksByOperatorUid(
			Long operatorUid, String taskType, int pageSize, long offset) {
		List<OrganizationTask> list = new ArrayList<OrganizationTask>();
		Condition condition = Tables.EH_ORGANIZATION_TASKS.OPERATOR_UID.eq(operatorUid);
		condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(OrganizationTaskStatus.UNPROCESSED.getCode()));
		if(taskType != null && !taskType.isEmpty())
			condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASKS)
				.where(condition)
				.orderBy(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.desc())
				.limit(pageSize).offset((int)offset)
				.fetch();
		if(records != null && !records.isEmpty()){
			for(Record r : records){
				list.add(ConvertHelper.convert(r, OrganizationTask.class));
			}
		}
		return list;
	}


	@Override
	public List<Organization> listDepartments(String superiorPath,
			Integer pageOffset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		List<Organization> result  = new ArrayList<Organization>();
		SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
		
		query.addConditions(Tables.EH_ORGANIZATIONS.PATH.like(superiorPath));
		
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1 ) * pageSize;
		query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, Organization.class));
			return null;
		});
		return result;
	}


	@Override
	public int countDepartments(String superiorPath) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>>  step = context.selectCount().from(Tables.EH_ORGANIZATIONS);
		Condition condition = Tables.EH_ORGANIZATIONS.PATH.like(superiorPath);

		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public List<OrganizationMember> listParentOrganizationMembers(String superiorPath, CrossShardListingLocator locator,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		pageSize = pageSize + 1;
		List<OrganizationMember> result  = new ArrayList<OrganizationMember>();
		SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
		query.addConditions(
				Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(
						context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
						.where(Tables.EH_ORGANIZATIONS.PATH.like(superiorPath + "/%")
								.or(Tables.EH_ORGANIZATIONS.PATH.eq(superiorPath)))));
		if(null != locator.getAnchor())
			query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.lt(locator.getAnchor()));
		query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
		query.addLimit(pageSize);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, OrganizationMember.class));
			return null;
		});
		
		if(result.size() >= pageSize){
			result.remove(result.size() - 1);
			locator.setAnchor(result.get(result.size() - 1).getId());
		}
		return result;
	}
	
	@Override
	public boolean updateOrganizationMemberByIds(List<Long> ids, Long deptId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		int count = context.update(Tables.EH_ORGANIZATION_MEMBERS)
		.set(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID, deptId)
		.where(Tables.EH_ORGANIZATION_MEMBERS.ID.in(ids)).execute();
		if(count == 0)
			return false;
		return true;
	}
	
	@Override
	public OrganizationMember findOrganizationMemberByOrgIdAndToken(
			String contactPhone, Long organizationId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactPhone));
		Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();
		if(r != null)
			return ConvertHelper.convert(r, OrganizationMember.class);
		return null;
	}
	
}
