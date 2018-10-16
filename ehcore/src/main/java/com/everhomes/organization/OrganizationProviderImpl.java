// @formatter:off
package com.everhomes.organization;

import com.everhomes.archives.ArchivesProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupMemberCaches;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pmsy.OrganizationMemberRecordMapper;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.NoticeMemberIdAndContact;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.organization.AuthFlag;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.rest.organization.FilterOrganizationContactScopeType;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.OperationType;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.organization.OrganizationBillingTransactionDTO;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.OrganizationCommunityRequestStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationJobPositionStatus;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.organization.OrganizationTaskStatus;
import com.everhomes.rest.organization.OrganizationTaskType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.UserOrganizationStatus;
import com.everhomes.rest.organization.VisibleFlag;
import com.everhomes.rest.organization.pm.OrganizationScopeCode;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.ui.user.ContactSignUpStatus;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUserOrganizations;
import com.everhomes.server.schema.tables.daos.EhCommunityOrganizationDetailDisplayDao;
import com.everhomes.server.schema.tables.daos.EhImportFileTasksDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationAddressMappingsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationAddressesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationAssignedScopesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillingAccountsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillingTransactionsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationBillsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationCommunityRequestsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationDetailsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationJobPositionMapsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationJobPositionsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMemberDetailsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMemberLogsDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationMembersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationOrdersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationOwnersDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationTasksDao;
import com.everhomes.server.schema.tables.daos.EhOrganizationsDao;
import com.everhomes.server.schema.tables.daos.EhUserOrganizationsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityOrganizationDetailDisplay;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhImportFileTasks;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAddressMappings;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAddresses;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAttachments;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBillingAccounts;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBillingTransactions;
import com.everhomes.server.schema.tables.pojos.EhOrganizationBills;
import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunities;
import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunityRequests;
import com.everhomes.server.schema.tables.pojos.EhOrganizationDetails;
import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositionMaps;
import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositions;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberDetails;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberLogs;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOrders;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwners;
import com.everhomes.server.schema.tables.pojos.EhOrganizationTasks;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.records.EhOrganizationAddressesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationAssignedScopesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationBillingAccountsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationCommunityRequestsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationDetailsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationJobPositionMapsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationJobPositionsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMemberDetailsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMemberLogsRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationMembersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOrdersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationTasksRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.everhomes.util.RecordHelper;
import com.everhomes.util.RuntimeErrorException;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrganizationProviderImpl implements OrganizationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ArchivesProvider archivesProvider;
    @Override
    public void createOrganization(Organization organization) {
        // eh_organizations表是global表，不能使用key table表的方式来获取id  modify by lqs 20160722
        // long id = shardingProvider.allocShardableContentId(EhOrganizations.class).second();
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizations.class));
        organization.setId(id);
        organization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organization.setPath(organization.getPath() + "/" + id);
        organization.setSetAdminFlag(TrueOrFalseFlag.FALSE.getCode());
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, id));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        dao.insert(organization);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizations.class, null);

    }

    @Override
    public Organization findOrganizationByOrganizationToken(String organizationToken) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ORGANIZATION_TOKEN.eq(organizationToken));
        return ConvertHelper.convert(query.fetchOne(), Organization.class);
    }

    @Override
    public void updateOrganization(Organization department) {
        assert (department.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        department.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(department);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
    }

    @Override
    public void updateUserOrganization(UserOrganizations userOrganization) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationsDao dao = new EhUserOrganizationsDao(context.configuration());
        userOrganization.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(userOrganization);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganizations.class, userOrganization.getId());
    }

    @Override
    public void updateOrganization(List<Long> ids, Byte status, Long uid, Timestamp now) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        int count = context.update(Tables.EH_ORGANIZATIONS).set(Tables.EH_ORGANIZATIONS.STATUS, status)
                .set(Tables.EH_ORGANIZATIONS.OPERATOR_UID, uid)
                .set(Tables.EH_ORGANIZATIONS.UPDATE_TIME, now)
                .where(Tables.EH_ORGANIZATIONS.ID.in(ids)).execute();

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, null);
    }


    @Override
    public void deleteOrganization(Organization department) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        dao.deleteById(department.getId());

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, department.getId());
    }

    @CacheEvict(value = "OrganizationById", key = "#id")
    @Override
    public void deleteOrganizationById(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, id);
    }

    /**
     * 根据组织id来查询eh_organizations表信息
     *
     * @param id
     * @return
     */
    //	@Cacheable(value="OrganizationById", key="#id")
    @Override
    public Organization findOrganizationById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Organization.class);
    }

    /**
     * 根据组织Id和手机号来查询Eh_organization_members表中的信息，来判断该用户是否已经加入到该公司
     *
     * @param organizationId
     * @param contactToken
     * @return
     */
    @Override
    public OrganizationMember findOrganizationMemberByContactTokenAndOrgId(Long organizationId, String contactToken) {
        //获取上下文
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        OrganizationMember organizationMember = context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactToken))
                .fetchAnyInto(OrganizationMember.class);
        return organizationMember;
    }

    @Override
    public List<Organization> listOrganizationsByIds(Set<Long> ids) {
        return listOrganizationsByIds(ids.toArray(new Long[ids.size()]));
    }

    @Override
    public List<Organization> listOrganizationsByIds(List<Long> ids) {
        return listOrganizationsByIds(ids.toArray(new Long[ids.size()]));
    }

    @Override
    public List<Organization> listOrganizationsByIds(Long... ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizationsDao dao = new EhOrganizationsDao(context.configuration());
        return dao.fetchById(ids).stream().map(o -> ConvertHelper.convert(o, Organization.class)).collect(Collectors.toList());
    }

    @Override
    public OrganizationMember findOrganizationPersonnelByPhone(Long id, String phone) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(id));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(phone));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });

        if (null != result && 0 != result.size()) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public OrganizationMemberDetails findOrganizationPersonnelByWorkEmail(Long orgId, String workEmail) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_DETAILS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(orgId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.WORK_EMAIL.eq(workEmail));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode()));
        return query.fetchAnyInto(OrganizationMemberDetails.class);
    }

    @Override
    public List<Organization> findOrganizationByCommunityId(Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addJoin(Tables.EH_ORGANIZATION_COMMUNITIES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));
        query.setDistinct(true);
        if (communityId != null)
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

        List<Organization> result = new ArrayList<Organization>();
        context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.PATH.eq(path))
                .fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });

        return result;
    }

    @Override
    public OrganizationMember findAnyOrganizationMemberByNamespaceIdAndUserId(Integer namespaceId, Long userId,
                                                                              String groupType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select()
                .from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId)).and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(groupType))
                .fetchAny();

        if (record != null) {
            return record.map(r -> ConvertHelper.convert(r, OrganizationMember.class));
        }
        return null;
    }

    @Override
    public List<Organization> listOrganizations(String organizationType, String name, Integer pageOffset, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        if (organizationType != null && !"".equals(organizationType)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType));
        }
        if (!StringUtils.isEmpty(name)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.eq(name));
        }

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
        query.addLimit(offset, pageSize);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });
        return result;
    }

    @Override
    public List<Organization> listOrganizations(String organizationType, Integer namespaceId, Long parentId, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        if (!StringUtils.isEmpty(organizationType)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType));
        }

        if (namespaceId != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        }

        if (null != parentId) {
            query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentId));
        }
        if (null != pageAnchor) {
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.gt(pageAnchor));
        }

        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.asc());
        if (null != pageSize) {
            query.addLimit(pageSize);
        }
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });
        return result;
    }


    @Override
    public List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String organizationType, CrossShardListingLocator locator, Integer pageSize) {
        return listEnterpriseByNamespaceIds(namespaceId, organizationType, null, locator, pageSize);
    }

    @Override
    public List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String keywords, String organizationType, CrossShardListingLocator locator, Integer pageSize) {
        return listEnterpriseByNamespaceIds(namespaceId, organizationType, null, keywords, locator, pageSize);
    }

    @Override
    public List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String organizationType,
                                                           Byte setAdminFlag, CrossShardListingLocator locator, int pageSize) {
        return listEnterpriseByNamespaceIds(namespaceId, organizationType, setAdminFlag, locator, pageSize);
    }

    @Override
    public List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String organizationType,
                                                           Byte setAdminFlag, String keywords, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        if (null != namespaceId) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        }
        if (!StringUtils.isEmpty(keywords)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like(keywords + "%"));
        }
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0l));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));
        if (!StringUtils.isEmpty(organizationType)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(organizationType));
        }
        if (setAdminFlag != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.SET_ADMIN_FLAG.eq(setAdminFlag));
        }

        if (null != locator.getAnchor()) {
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.lt(locator.getAnchor()));
        }
        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
        query.addLimit(pageSize);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });
        locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
    }

    @Caching(evict = {@CacheEvict(value = "listGroupMessageMembers", allEntries = true), @CacheEvict(value = "ListOrganizationMemberByPath", allEntries = true)})
    @Override
    public void createOrganizationMember(OrganizationMember organizationMember) {
        organizationMember.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationMember.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        if (null == VisibleFlag.fromCode(organizationMember.getVisibleFlag())) {
            organizationMember.setVisibleFlag(VisibleFlag.SHOW.getCode());
        }
        if (organizationMember.getNamespaceId() == null) {
            Integer namespaceId = UserContext.getCurrentNamespaceId(null);
            organizationMember.setNamespaceId(namespaceId);
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationMembers.class));
        organizationMember.setId(id);
        EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
        dao.insert(organizationMember);
        if (OrganizationMemberTargetType.fromCode(organizationMember.getTargetType()) == OrganizationMemberTargetType.USER) {
            DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationMembers.class, organizationMember.getId());
        }
    }

    @Caching(evict = {@CacheEvict(value = "listGroupMessageMembers", allEntries = true), @CacheEvict(value = "ListOrganizationMemberByPath", allEntries = true)})
    @Override
    public void updateOrganizationMember(OrganizationMember departmentMember) {
        assert (departmentMember.getId() == null);
        departmentMember.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
        dao.update(departmentMember);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
    }

    @Caching(evict = {@CacheEvict(value = "ListOrganizationMemberByPath", allEntries = true)})
    @Override
    public void updateOrganizationMemberByOrgPaths(String path, Byte status, Long uid, Timestamp now) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        int count = context.update(Tables.EH_ORGANIZATION_MEMBERS).set(Tables.EH_ORGANIZATION_MEMBERS.STATUS, status)
                .set(Tables.EH_ORGANIZATION_MEMBERS.OPERATOR_UID, uid)
                .set(Tables.EH_ORGANIZATION_MEMBERS.UPDATE_TIME, now)
                .where(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path)).execute();

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizations.class, null);
    }

    @Caching(evict = {@CacheEvict(value = "listGroupMessageMembers", allEntries = true), @CacheEvict(value = "ListOrganizationMemberByPath", allEntries = true)})
    @Override
    public void deleteOrganizationMemberById(Long id) {

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
    public List<OrganizationMember> listOrganizationMembersByIds(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.in(ids));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembers(Long orgId, Long memberUid, Long offset, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        if (orgId != null && orgId > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId));
        }
        if (memberUid != null && memberUid > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
        }
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.addLimit(offset.intValue(), pageSize);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }


    @Override
    public List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(Long organizationId, String memberGroup, String targetType) {
        return listOrganizationMembersByOrganizationIdAndMemberGroup(organizationId, memberGroup, targetType, null, -1, new ListingLocator());
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(Long organizationId, String memberGroup, String targetType, Integer pageSize, ListingLocator locator) {
        return listOrganizationMembersByOrganizationIdAndMemberGroup(organizationId, memberGroup, targetType, null, pageSize, locator);
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(String memberGroup, String targetType, Long targetId) {
        return listOrganizationMembersByOrganizationIdAndMemberGroup(null, memberGroup, targetType, targetId, -1, new ListingLocator());
    }


    @Override
    public List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(
            Long organizationId, String memberGroup, String targetType, Long targetId,
            int pageSize, ListingLocator locator) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);

        if (null != organizationId) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));

        }
        if (null != memberGroup) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.MEMBER_GROUP.eq(memberGroup));
        }
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));

        if (null != OrganizationMemberTargetType.fromCode(targetType)) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(targetType));
        }

        if (null != targetId) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(targetId));
        }

        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.le(locator.getAnchor()));
        }

        if (pageSize > 0) {
            query.addLimit(pageSize + 1);
        }
        //:todo 解决重复
        // updated by Janson 20171018 错误的公司成员会覆盖正确的公司成员 #17284
//		query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());

        List<OrganizationMember> list = query.fetchInto(OrganizationMember.class);
        if (list.size() > pageSize && pageSize > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembers(Long memberUid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);

        if (memberUid != null && memberUid > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
        }
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembers(Long orgId, List<Long> memberUids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
        if (memberUids != null) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.in(memberUids));
        }
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByPhone(String phone) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(phone));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByPhones(List<String> phones, Long departmentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_ID.eq(departmentId));
        if (null != phones && 0 != phones.size()) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.notIn(phones));
        }
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
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
    public void updateOrganizationCommunity(OrganizationCommunity departmentCommunity) {
        assert (departmentCommunity.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
        dao.update(departmentCommunity);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
    }


    @Override
    public void deleteOrganizationCommunity(OrganizationCommunity departmentCommunity) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
        dao.deleteById(departmentCommunity.getId());

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, departmentCommunity.getId());
    }


    @Override
    public void deleteOrganizationCommunityById(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationCommunitiesDao dao = new EhOrganizationCommunitiesDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, id);
    }

    @Override
    public void deleteOrganizationCommunityByOrgIds(List<Long> organizationIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ORGANIZATION_COMMUNITIES).where(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.in(organizationIds)).execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunities.class, null);
    }

    @Override
    public OrganizationCommunity findOrganizationCommunityByOrgIdAndCmmtyId(Long orgId, Long cmmtyId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES);
        Condition condition = Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId.longValue());
        condition = condition.and(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(cmmtyId.longValue()));
        final OrganizationCommunity[] result = new OrganizationCommunity[1];
        step.where(condition).fetch().map(r -> {
            result[0] = ConvertHelper.convert(r, OrganizationCommunity.class);
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
    public List<OrganizationCommunity> listOrganizationCommunities(Long organizationId, Integer pageOffset, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationCommunity> result = new ArrayList<OrganizationCommunity>();
        SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
        if (organizationId != null && organizationId > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
        }

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
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

        List<OrganizationCommunity> result = new ArrayList<OrganizationCommunity>();
        SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
        if (organizationId != null && organizationId > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
        }
        query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationCommunity.class));
            return null;
        });

        if (result == null || result.size() == 0) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("The community for the organization is not found, organizationId=" + organizationId);
                LOGGER.warn(query.getSQL());
            }
        }

        return result;
    }

    @Override
    public List<OrganizationCommunity> listOrganizationByCommunityId(Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationCommunity> result = new ArrayList<OrganizationCommunity>();
        SelectQuery<EhOrganizationCommunitiesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITIES);
        if (communityId != null && communityId > 0) {
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
        if (communityId != null && communityId > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
        }
        query.fetch().map((r) -> {
            if (r != null)
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
        if (organizationId != null && organizationId > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(organizationId));
        }
        query.fetch().map((r) -> {
            if (r != null)
                result[0] = ConvertHelper.convert(r, OrganizationCommunity.class);
            return null;
        });
        return result[0];
    }

    @Override
    public int countOrganizations(String type, String name) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_ORGANIZATIONS);
        Condition condition = null;
        if (type != null && !type.equals(""))
            condition = Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(type);
        if (!StringUtils.isEmpty(name)) {
            if (condition != null) {
                condition = condition.and(Tables.EH_ORGANIZATIONS.NAME.eq(name));
            } else {
                condition = Tables.EH_ORGANIZATIONS.NAME.eq(name);
            }
        }

        if (condition == null)
            return step.fetchOneInto(Integer.class);
        return step.where(condition).fetchOneInto(Integer.class);
    }

    @Override
    public int countOrganizationMembers(Long departmentId, Long memberUid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS);
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(departmentId);
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        if (memberUid != null && memberUid > 0)
            condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(memberUid));
        return step.where(condition).fetchOneInto(Integer.class);
    }

    @Override
    public int countOrganizationCommunitys(Long organizationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_ORGANIZATION_COMMUNITIES);
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

        if (record != null)
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
    public Organization findOrganizationByCommunityIdAndOrgType(Long communityId, String organizationType) {

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

        if (list != null && !list.isEmpty())
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
                .orderBy(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.desc(), Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.TARGET_ACCOUNT_ID.asc())
                .limit(pageSize).offset((int) offset)
                .fetch();

        if (records != null && !records.isEmpty()) {
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

        if (records != null && !records.isEmpty()) {
            records.stream().map(r -> {
                list.add(ConvertHelper.convert(r, CommunityPmOwner.class));
                return null;
            }).toArray();
        }

        return list;
    }

    @Cacheable(value = "findOrganizationTaskById", key = "#id", unless = "#result == null")
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

        if (records != null && !records.isEmpty()) {
            return ConvertHelper.convert(records.get(0), OrganizationBillingAccount.class);
        }

        return null;
    }

    @Override
    public void createOrganizationBillingAccount(OrganizationBillingAccount oAccount) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        InsertQuery<EhOrganizationBillingAccountsRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_BILLING_ACCOUNTS);
        query.setRecord(ConvertHelper.convert(oAccount, EhOrganizationBillingAccountsRecord.class));
        query.setReturning(Tables.EH_ORGANIZATION_BILLING_ACCOUNTS.ID);
        query.execute();

        oAccount.setId(query.getReturnedRecord().getId());
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationBillingAccounts.class, null);

    }

    @Override
    public void createOrganizationBillingTransaction(OrganizationBillingTransactions orgTx) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationBillingTransactionsDao dao = new EhOrganizationBillingTransactionsDao(context.configuration());
        dao.insert(ConvertHelper.convert(orgTx, EhOrganizationBillingTransactions.class));
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationBillingTransactions.class, null);
    }

    @Override
    public void updateOrganizationBillingAccount(OrganizationBillingAccount oAccount) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationBillingAccountsDao dao = new EhOrganizationBillingAccountsDao(context.configuration());
        dao.update(ConvertHelper.convert(oAccount, EhOrganizationBillingAccounts.class));
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationBillingAccounts.class, null);
    }


    @Override
    public List<Organization> listOrganizationByCondition(Condition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATIONS).where(condition).fetch();

        List<Organization> list = new ArrayList<Organization>();
        if (records != null && !records.isEmpty()) {
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

        if (records != null && !records.isEmpty()) {
            for (Record record : records) {
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
        if (records != null && !records.isEmpty())
            return ConvertHelper.convert(records.get(0), CommunityPmBill.class);
        return null;
    }


    @Override
    public List<OrganizationBillingTransactionDTO> listOrgBillTxByOrgId(Long orgId, int resultCode, Timestamp startTime, Timestamp endTime, String address, long offset, int pageSize) {

        List<OrganizationBillingTransactionDTO> list = new ArrayList<OrganizationBillingTransactionDTO>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS);
        query.addJoin(Tables.EH_ORGANIZATION_ORDERS, Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.ORDER_ID.eq(Tables.EH_ORGANIZATION_ORDERS.ID));
        query.addJoin(Tables.EH_ORGANIZATION_BILLS, Tables.EH_ORGANIZATION_BILLS.ID.eq(Tables.EH_ORGANIZATION_ORDERS.BILL_ID));

        query.addConditions(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.OWNER_ID.eq(orgId));
        query.addConditions(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.RESULT_CODE_ID.eq(resultCode));
        if (startTime != null && endTime != null)
            query.addConditions(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.greaterOrEqual(startTime)
                    .and(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.lessOrEqual(endTime)));
        if (address != null && !address.equals(""))
            query.addConditions(Tables.EH_ORGANIZATION_BILLS.ADDRESS.like("%" + address + "%"));


        query.addOrderBy(Tables.EH_ORGANIZATION_BILLING_TRANSACTIONS.CREATE_TIME.desc(), Tables.EH_ORGANIZATION_BILLS.ADDRESS.asc());
        query.addLimit((int) offset, pageSize);
        //System.out.println(query.getSQL());
        query.execute();

        Result<Record> records = query.getResult();
        if (records != null && !records.isEmpty()) {
            for (Record r : records) {
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
    public List<OrganizationOwner> listOrganizationOwnersByOrgIdAndAddressId(
            Long organizationId, Long addressId) {
        List<OrganizationOwner> list = new ArrayList<OrganizationOwner>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_OWNERS)
                .where(Tables.EH_ORGANIZATION_OWNERS.ORGANIZATION_ID.eq(organizationId)
                        .and(Tables.EH_ORGANIZATION_OWNERS.ADDRESS_ID.eq(addressId)))
                .fetch();
        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationOwner.class));
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

        if (records != null && !records.isEmpty()) {
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

        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, CommunityPmBill.class));
        }
        return list;
    }


    @Override
    public OrganizationCommunity findOrganizationCommunityByOrgId(Long orgId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_COMMUNITIES)
                .where(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId)).fetch();

        if (records != null && !records.isEmpty())
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

        if (records != null && !records.isEmpty()) {
            return ConvertHelper.convert(records.get(0), OrganizationTask.class);
        }
        return null;
    }

    @Caching(evict = {@CacheEvict(value = "findOrganizationTaskById", key = "#task.id")})
    public void updateOrganizationTask(OrganizationTask task) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationTasksDao dao = new EhOrganizationTasksDao(context.configuration());
        dao.update(task);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, OrganizationTask.class, task.getId());
    }

    @Override
    public void deleteOrganizationMember(OrganizationMember departmentMember) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
        dao.deleteById(departmentMember.getId());

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, departmentMember.getId());
    }

    @Override
    public List<OrganizationTask> listOrganizationTasksByOrgIdAndType(Long organizationId, String taskType, Byte taskStatus, int pageSize, long offset) {
        List<OrganizationTask> list = new ArrayList<OrganizationTask>();
        Condition condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.eq(organizationId);
        if (taskType != null && !taskType.isEmpty())
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));
        if (taskStatus != null)
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(taskStatus));

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASKS)
                .where(condition)
                .orderBy(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.desc())
                .limit(pageSize).offset((int) offset)
                .fetch();
        if (records != null && !records.isEmpty()) {
            for (Record r : records) {
                list.add(ConvertHelper.convert(r, OrganizationTask.class));
            }
        }
        return list;
    }

    @Override
    public List<OrganizationTask> listOrganizationTasksByTypeOrStatus(CrossShardListingLocator locator, List<Long> organizationIds, Long targetId, String taskType, Byte taskStatus, Byte visibleRegionType, Long visibleRegionId, int pageSize) {
        List<OrganizationTask> list = new ArrayList<OrganizationTask>();
        Condition condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.notEqual(-1l);
        if (null != organizationIds && 0 != organizationIds.size())
            condition = Tables.EH_ORGANIZATION_TASKS.ORGANIZATION_ID.in(organizationIds);
        if (!StringUtils.isEmpty(taskType))
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));
        if (taskStatus != null)
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(taskStatus));

        if (null != targetId) {
            Condition cond = Tables.EH_ORGANIZATION_TASKS.TARGET_ID.eq(targetId);
            if (OrganizationTaskType.EMERGENCY_HELP == OrganizationTaskType.fromCode(taskType)) {
                cond = cond.or(Tables.EH_ORGANIZATION_TASKS.TARGET_ID.eq(0l));
            }
            condition = condition.and(cond);
        }

        if (null != visibleRegionType && null != visibleRegionId) {
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.VISIBLE_REGION_TYPE.eq(visibleRegionType));
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.VISIBLE_REGION_ID.eq(visibleRegionId));
        }

        if (null != locator.getAnchor()) {
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
        }

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASKS)
                .where(condition)
                .orderBy(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.desc())
                .limit(pageSize + 1)
                .fetch();
        if (records != null && !records.isEmpty()) {
            for (Record r : records) {
                list.add(ConvertHelper.convert(r, OrganizationTask.class));
            }
        }

        locator.setAnchor(null);
        if (list.size() > pageSize) {
            list.remove(list.size() - 1);
            locator.setAnchor(list.get(list.size() - 1).getCreateTime().getTime());
        }

        return list;
    }

    /**
     * modify cause member_detail by lei lv 7.224
     **/
    @Override
    public OrganizationMember findOrganizationMemberByUIdAndOrgId(Long userId, Long organizationId) {

        Long enterpriseId = getTopOrganizationId(organizationId);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId).and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        if (organizationId != null && organizationId != 0L) {
            condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));
        }
        //added by wh 2016-10-13 把被拒绝的过滤掉
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();

        return backOrganizationMember(r, enterpriseId);
    }

    private OrganizationMember backOrganizationMember(Record record, Long enterpriseId) {
        if (record != null) {
            OrganizationMember organizationMember = ConvertHelper.convert(record, OrganizationMember.class);
            if (enterpriseId != null) {
                organizationMember.setEnterpriserId(enterpriseId);
            }
            return organizationMember;
        }
        return null;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public OrganizationMember findActiveOrganizationMemberByOrgIdAndUId(Long userId, Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));

        Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, OrganizationMember.class);
        return null;
    }

    @Override
    public List<OrganizationMember> findOrganizationMemberByOrgIdAndUIdWithoutAllStatus(Long organizationId, Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId))
                .orderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc())
                .fetchInto(OrganizationMember.class);
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> findOrganizationMembersByOrgIdAndUId(Long userId, Long organizationId) {

        List<OrganizationMember> list = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
//        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
//        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
//        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
//        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
//        Condition condition = t1.field("id").gt(0L);
//        condition = condition.and(t1.field("organization_id").eq(organizationId)).and(t1.field("target_id").eq(userId));
//        condition = condition.and(t1.field("status").ne(OrganizationMemberStatus.INACTIVE.getCode()))
//                .and(t1.field("status").ne(OrganizationMemberStatus.REJECT.getCode()));
//        List<OrganizationMember> records = step.where(condition).fetch().map(new OrganizationMemberRecordMapper());
//        if (records != null) {
//            records.stream().map(r -> {
//                result.add(ConvertHelper.convert(r, OrganizationMember.class));
//                return null;
//            }).collect(Collectors.toList());
//        }
//        return result;

        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetch();

        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationMember.class));
        }
        return list;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMembersByOrgId(Long orgId) {
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L);
        condition = condition.and(t1.field("organization_id").eq(orgId));
        condition = condition.and(t1.field("status").ne(OrganizationMemberStatus.INACTIVE.getCode()))
                .and(t1.field("status").ne(OrganizationMemberStatus.REJECT.getCode()));

        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").desc()).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        return result;

//        SendResult<Record> records = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId))
//                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()))
//                //added by wh 2016-10-13 把被拒绝的过滤掉
//                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode())).fetch();
//
//        if (records != null && !records.isEmpty()) {
//            for (Record r : records)
//                list.add(ConvertHelper.convert(r, OrganizationMember.class));
//        }
//        return list;
    }

    @Override
    public Integer getSignupCount(Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.ne(0L))
                .fetchOne()
                .value1();
    }

    @Override
    public Organization findOrganizationByName(String name) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Record r = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name)).fetchAny();
        if (r != null)
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
    public List<OrganizationOrder> listOrgOrdersByBillIdAndStatus(Long billId, Byte status) {
        List<OrganizationOrder> list = new ArrayList<OrganizationOrder>();

        Condition condition = null;
        if (billId != null)
            condition = Tables.EH_ORGANIZATION_ORDERS.BILL_ID.eq(billId);
        if (status != null)
            condition = condition.and(Tables.EH_ORGANIZATION_ORDERS.STATUS.eq(status));


        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhOrganizationOrdersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ORDERS);
        if (condition != null) {
            query.addConditions(condition);
        }
        query.fetch().map(r -> {
            list.add(ConvertHelper.convert(r, OrganizationOrder.class));
            return null;
        });

        return list;
    }


    @Override
    public Map<Long, BigDecimal> mapOrgOrdersByBillIdAndStatus(List<Long> billIds, byte organizationOrderStatus) {
        Map<Long, BigDecimal> map = new HashMap<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.select(Tables.EH_ORGANIZATION_ORDERS.BILL_ID, DSL.sum(Tables.EH_ORGANIZATION_ORDERS.AMOUNT)).from(Tables.EH_ORGANIZATION_ORDERS)
                .where(Tables.EH_ORGANIZATION_ORDERS.BILL_ID.in(billIds))
                .and(Tables.EH_ORGANIZATION_ORDERS.STATUS.eq(organizationOrderStatus))
                .groupBy(Tables.EH_ORGANIZATION_ORDERS.BILL_ID)
                .fetch().map(r -> {
            map.put(r.getValue(Tables.EH_ORGANIZATION_ORDERS.BILL_ID), r.getValue(DSL.sum(Tables.EH_ORGANIZATION_ORDERS.AMOUNT)));
            return null;
        });

        return map;
    }

    @Override
    public OrganizationOrder findOrganizationOrderById(Long orderId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_ORGANIZATION_ORDERS)
                .where(Tables.EH_ORGANIZATION_ORDERS.ID.eq(orderId))
                .fetchOne();
        if (record != null)
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

        if (records != null && !records.isEmpty()) {
            return ConvertHelper.convert(records.get(0), CommunityAddressMapping.class);
        }
        return null;
    }


    @Override
    public List<Organization> listOrganizationByName(String orgName, String orgType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<Organization> list = context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(orgType).and(Tables.EH_ORGANIZATIONS.NAME.eq(orgName)))
                .fetch().map(r -> {
                    return ConvertHelper.convert(r, Organization.class);
                });

        if (list == null || list.isEmpty())
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

        if (list == null || list.isEmpty())
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

        if (list == null || list.isEmpty())
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

        if (locator.getAnchor() != null)
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.gt(locator.getAnchor()));
        if (orgId != null) {
            Condition con = Tables.EH_ORGANIZATIONS.ID.eq(orgId);
            con = con.or(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(orgId));
            query.addConditions(con);
        }
        if (communityId != null) {
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID.eq(communityId));
        }

        query.addLimit(count);
        List<EhOrganizationsRecord> records = query.fetch().map(new EhOrganizationRecordMapper());
        List<Organization> organizations = records.stream().map((r) -> {
            return ConvertHelper.convert(r, Organization.class);
        }).collect(Collectors.toList());

        if (organizations.size() > 0) {
            locator.setAnchor(organizations.get(organizations.size() - 1).getId());
        }
        return organizations;
    }


    @Override
    public void deletePmBuildingByOrganizationId(Long organizationId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationAssignedScopesDao dao = new EhOrganizationAssignedScopesDao(context.configuration());
        List<EhOrganizationAssignedScopes> assignedScopes = dao.fetchByOrganizationId(organizationId);
        if (assignedScopes != null && !assignedScopes.isEmpty()) {
            assignedScopes.forEach(r -> deletePmBuildingById(r.getId()));
        }

    }


    @Override
    public List<OrganizationTask> listOrganizationTasksByOperatorUid(
            Long operatorUid, String taskType, int pageSize, long offset) {
        List<OrganizationTask> list = new ArrayList<OrganizationTask>();
        Condition condition = Tables.EH_ORGANIZATION_TASKS.OPERATOR_UID.eq(operatorUid);
        condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_STATUS.eq(OrganizationTaskStatus.UNPROCESSED.getCode()));
        if (taskType != null && !taskType.isEmpty())
            condition = condition.and(Tables.EH_ORGANIZATION_TASKS.TASK_TYPE.eq(taskType));

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASKS)
                .where(condition)
                .orderBy(Tables.EH_ORGANIZATION_TASKS.CREATE_TIME.desc())
                .limit(pageSize).offset((int) offset)
                .fetch();
        if (records != null && !records.isEmpty()) {
            for (Record r : records) {
                list.add(ConvertHelper.convert(r, OrganizationTask.class));
            }
        }
        return list;
    }


    @Override
    public List<Organization> listDepartments(String superiorPath,
                                              Integer pageOffset, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.PATH.like(superiorPath));
        query.addConditions((Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.DEPARTMENT.getCode())).or(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode())));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());
        query.addLimit(offset, pageSize);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });
        return result;
    }

    @Override
    public Organization findOrganizationByParentAndName(Long parentId, String name) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentId));
        query.addConditions(Tables.EH_ORGANIZATIONS.NAME.eq(name));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });

        if (0 == result.size()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public int countDepartments(String superiorPath) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_ORGANIZATIONS);
        Condition condition = Tables.EH_ORGANIZATIONS.PATH.like(superiorPath);

        return step.where(condition).fetchOneInto(Integer.class);
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listParentOrganizationMembers(String superiorPath, List<String> groupTypes, CrossShardListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        TableLike orgTable = Tables.EH_ORGANIZATIONS.as("orgTable");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        //内层查询条件
        Condition cond = orgTable.field("path").like(superiorPath + "/%")
                .or(orgTable.field("path").eq(superiorPath)).and(orgTable.field("status").eq(OrganizationStatus.ACTIVE.getCode()));
        if (null != groupTypes) {
            cond = cond.and(orgTable.field("group_type").in(groupTypes));
        }
        //外层查询条件
        Condition condition = t1.field("status").ne(OrganizationMemberStatus.INACTIVE.getCode())
                .and(t1.field("status").ne(OrganizationMemberStatus.REJECT.getCode()));
        condition = condition.and(t1.field("organization_id").in(
                context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
                        .where(cond)));
        if (null != locator.getAnchor())
            condition = condition.and(t1.field("id").gt(locator.getAnchor()));

        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").desc()).limit(pageSize).fetch().map(new OrganizationMemberRecordMapper());
        records.stream().map(r -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        }).collect(Collectors.toList());
        locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;

//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        Condition cond = Tables.EH_ORGANIZATIONS.PATH.like(superiorPath + "/%")
//                .or(Tables.EH_ORGANIZATIONS.PATH.eq(superiorPath)).and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
//        if (null != groupTypes) {
//            cond = cond.and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.in(groupTypes));
//        }
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
//        //added by wh 2016-10-13 把被拒绝的过滤掉
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
//        query.addConditions(
//                Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(
//                        context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
//                                .where(cond)));
//        if (null != locator.getAnchor())
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.lt(locator.getAnchor()));
//        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
//        query.addLimit(pageSize);
//        query.fetch().map((r) -> {
//            result.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//        locator.setAnchor(null);
//
//        if (result.size() >= pageSize) {
//            result.remove(result.size() - 1);
//            locator.setAnchor(result.get(result.size() - 1).getId());
//        }
//        return result;
    }

    /*@Override
    public List<OrganizationMember> listParentOrganizationMembersByName(String superiorPath, List<String> groupTypes, String userName) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        *//**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**//*
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        TableLike orgTable = Tables.EH_ORGANIZATIONS.as("orgTable");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        //内层查询条件
        Condition cond = orgTable.field("path").like(superiorPath + "/%")
                .or(orgTable.field("path").eq(superiorPath));
        if (null != groupTypes) {
            cond = cond.and(orgTable.field("group_type").in(groupTypes));
        }
        //外层查询条件
        Condition condition = t1.field("status").ne(OrganizationMemberStatus.INACTIVE.getCode())
                .and(t1.field("status").ne(OrganizationMemberStatus.REJECT.getCode()));

        //第二层查询条件
        Condition cond2 = t1.field("organization_id").in(
                context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
                        .where(cond));

        if (null != groupTypes && groupTypes.contains(OrganizationGroupType.DEPARTMENT.getCode()))
            cond2 = cond2.or(t1.field("group_id").in(
                    context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
                            .where(cond)));

        condition = condition.and(cond2);
        if (!StringUtils.isEmpty(userName))
            condition = condition.and(t2.field("contact_name").like("%" + userName + "%"));

        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").desc()).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && !result.isEmpty())
            return result;
        return null;
    }*/

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMembers(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);

        if (null != queryBuilderCallback)
            queryBuilderCallback.buildCondition(locator, query);
        if (null != locator && null != locator.getAnchor())
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.lt(locator.getAnchor()));


        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.addLimit(pageSize);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        if (null != locator)
            locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        convertMemberListAsDetailList(result);
        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationPersonnels(String keywords, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize) {
        return listOrganizationPersonnels(keywords, orgCommoand, contactSignedupStatus, visibleFlag, locator, pageSize, null);
    }

    @Override
    public void updateOrganizationDefaultOrder(Integer namespaceId, Long orgId, Integer order) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhOrganizationsRecord> query = context.updateQuery(Tables.EH_ORGANIZATIONS);
        query.addValue(Tables.EH_ORGANIZATIONS.ORDER, order);
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATIONS.ID.eq(orgId));
        query.execute();
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationPersonnels(Integer namespaceId, String keywords, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")).and(t1.field("target_id").eq(t2.field("target_id"))));
        Condition condition = t1.field("id").gt(0L).and(t1.field("namespace_id").eq(namespaceId));

        Condition cond = t1.field("organization_id").eq(orgCommoand.getId()).and(t1.field("status").eq(orgCommoand.getStatus()));

        if (!StringUtils.isEmpty(keywords)) {
            Condition cond1 = t2.field("contact_token").eq(keywords);
            cond1 = cond1.or(t2.field("contact_name").like("%" + keywords + "%"));
            cond = cond.and(cond1);
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            cond = cond.and(t2.field("target_id").ne(0L));
            cond = cond.and(t2.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != visibleFlag) {
            cond = cond.and(t1.field("visible_flag").eq(visibleFlag.getCode()));
        }

        condition = condition.and(cond);
        if (null != locator && null != locator.getAnchor()) {
            condition = condition.and(t1.field("id").lt(locator.getAnchor()));
        }

        List<OrganizationMember> records = step.where(condition).groupBy(t1.field("contact_token")).orderBy(t1.field("id").desc()).limit(pageSize).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (null != locator)
            locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationPersonnels(Integer namespaceId, String keywords, String identifierToken, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<>();
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        Condition condition = t1.field("id").gt(0L).and(t1.field("namespace_id").eq(namespaceId));

        Condition cond = t1.field("organization_id").eq(orgCommoand.getId()).and(t1.field("status").eq(orgCommoand.getStatus()));

        if (!StringUtils.isEmpty(keywords)) {
            Condition cond1 = t1.field("contact_name").like("%" + keywords + "%");
            cond = cond.and(cond1);
        }

        if (!StringUtils.isEmpty(identifierToken)) {
            Condition cond1 = t1.field("contact_token").eq(identifierToken);
            cond = cond.and(cond1);
        }
        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            cond = cond.and(t1.field("target_id").ne(0L));
            cond = cond.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != visibleFlag) {
            cond = cond.and(t1.field("visible_flag").eq(visibleFlag.getCode()));
        }

        condition = condition.and(cond);
        if (null != locator && null != locator.getAnchor()) {
            condition = condition.and(t1.field("id").lt(locator.getAnchor()));
        }

        result = context.select().from(t1).where(condition).groupBy(t1.field("contact_token")).orderBy(t1.field("id").desc()).limit(pageSize).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, OrganizationMember.class);
                });
        if (null != locator)
            locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
    }

    @Override
    public Integer countOrganizationPersonnels(Integer namespaceId, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep<Record1<Integer>> step = context.selectCount().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L).and(t1.field("namespace_id").eq(namespaceId));
        Condition cond = t1.field("organization_id").eq(orgCommoand.getId()).and(t1.field("status").eq(orgCommoand.getStatus()));
        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            cond = cond.and(t1.field("target_id").ne(0L));
            cond = cond.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }
        if (null != visibleFlag) {
            cond = cond.and(t1.field("visible_flag").eq(visibleFlag.getCode()));
        }
        condition = condition.and(cond);
        return step.where(condition).fetchOneInto(Integer.class);
    }

    @Override
    public List<OrganizationMember> listOrganizationPersonnels(String keywords, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize, ListOrganizationContactCommand listCommand) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize += 1;
        List<OrganizationMember> result = new ArrayList<>();
        /*modify by lei lv,增加了detail表，部分信息挪到detail表里去取*/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L).and(t1.field("namespace_id").eq(namespaceId));

        Condition cond = t1.field("organization_id").eq(orgCommoand.getId()).and(t1.field("status").eq(orgCommoand.getStatus()));

        if (!StringUtils.isEmpty(keywords)) {
            Condition cond1 = t2.field("contact_token").eq(keywords);
            cond1 = cond1.or(t2.field("contact_name").like("%" + keywords + "%"));
            cond = cond.and(cond1);
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            cond = cond.and(t1.field("target_id").ne(0L));
            cond = cond.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != visibleFlag) {
            cond = cond.and(t1.field("visible_flag").eq(visibleFlag.getCode()));
        }

        if (listCommand != null) {
            // 员工状态
            if (listCommand.getEmployeeStatus() != null) {
                cond = cond.and(t2.field("employee_status").eq(listCommand.getEmployeeStatus()));
            }

            // 合同主体
            if (listCommand.getContractPartyId() != null) {
                cond = cond.and(t2.field("contract_id").eq(listCommand.getContractPartyId()));
            }

            //工作地点
            if (listCommand.getWorkPlaceId() != null) {
                cond = cond.and(t2.field("work_place").eq(listCommand.getWorkPlaceId()));
            }

            //入职日期
            if (listCommand.getCheckInTimeStart() != null && listCommand.getCheckInTimeEnd() != null) {
                cond = cond.and(t2.field("check_in_time").between(listCommand.getCheckInTimeStart(), listCommand.getCheckInTimeEnd()));
            }

            //转正日期
            if (listCommand.getEmploymentTimeStart() != null && listCommand.getEmploymentTimeEnd() != null) {
                cond = cond.and(t2.field("employment_time").between(listCommand.getEmploymentTimeStart(), listCommand.getEmploymentTimeEnd()));
            }

            //合同结束日期
            if (listCommand.getContractEndTimeStart() != null && listCommand.getContractEndTimeStart() != null) {
                cond = cond.and(t2.field("contract_end_time").between(listCommand.getContractEndTimeStart(), listCommand.getContractEndTimeStart()));
            }
        }

        condition = condition.and(cond);
        if (null != locator && null != locator.getAnchor())
            condition.and(t1.field("id").lt(locator.getAnchor()));

        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").desc()).limit(pageSize).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (null != locator)
            locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Deprecated
    @Override
    public List<OrganizationMember> listOrganizationPersonnels(String keywords, List<Long> orgIds, Byte memberStatus, Byte contactSignedupStatus, CrossShardListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L);

        Condition cond = t1.field("status").eq(memberStatus);

        if (orgIds != null && orgIds.size() > 0) {
            cond = cond.and(t1.field("organization_id").in(orgIds));
        }
//        Condition cond = t1.field("organization_id").in(orgIds);
//        cond = cond.and(t1.field("status").eq(memberStatus));

        if (!StringUtils.isEmpty(keywords)) {
            Condition cond1 = t2.field("contact_token").eq(keywords);
            cond1 = cond1.or(t2.field("contact_name").like("%" + keywords + "%"));
            cond = cond.and(cond1);
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            cond = cond.and(t1.field("target_id").ne(0L));
            cond = cond.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        condition = condition.and(cond);
        if (null != locator.getAnchor())
            condition = condition.and(t1.field("id").lt(locator.getAnchor()));
        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").desc()).limit(pageSize).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        locator.setAnchor(null);
        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        Condition cond = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(orgIds);
//        cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(memberStatus));
//
//        if (!StringUtils.isEmpty(keywords)) {
//            Condition cond1 = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(keywords);
//            cond1 = cond1.or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + keywords + "%"));
//            cond = cond.and(cond1);
//        }
//
//        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
//            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.ne(0L));
//            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
//        }

//        query.addConditions(cond);
//        if (null != locator.getAnchor())
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.lt(locator.getAnchor()));
//        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
//        query.addLimit(pageSize);
//        query.fetch().map((r) -> {
//            result.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//        locator.setAnchor(null);
//
//        if (result.size() >= pageSize) {
//            result.remove(result.size() - 1);
//            locator.setAnchor(result.get(result.size() - 1).getId());
//        }
//        return result;
    }

    @Caching(evict = {@CacheEvict(value = "ListOrganizationMemberByPath", allEntries = true)})
    @Override
    public boolean updateOrganizationMemberByIds(List<Long> ids, Organization org) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        int count = context.update(Tables.EH_ORGANIZATION_MEMBERS)
                .set(Tables.EH_ORGANIZATION_MEMBERS.GROUP_ID, org.getId())
                .set(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH, org.getPath())
                .where(Tables.EH_ORGANIZATION_MEMBERS.ID.in(ids)).execute();
        if (count == 0)
            return false;
        return true;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public OrganizationMember findOrganizationMemberByOrgIdAndToken(
            String contactPhone, Long organizationId, String memberGroup) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId).and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactPhone));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        if (memberGroup != null) {
            //added by janson, filter by member_group
            condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.MEMBER_GROUP.eq(memberGroup));
        }
        Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, OrganizationMember.class);
        return null;
    }

    @Override
    public OrganizationMember findOrganizationMemberByOrgIdAndToken(
            String contactPhone, Long organizationId) {
        return findOrganizationMemberByOrgIdAndToken(contactPhone, organizationId, null);
    }

    @Override
    public OrganizationMember findMemberDepartmentByDetailId(Long detailId) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId)
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DEPARTMENT.getCode()).or(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode())));

        Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();

        if (r != null)
            return ConvertHelper.convert(r, OrganizationMember.class);
        return null;
    }

    @Override
    public List<OrganizationMember> findMemberJobPositionByDetailId(Long detailId) {
        List<OrganizationMember> results = new ArrayList<>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId)
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.JOB_POSITION.getCode()));
        query.addConditions(condition);
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });

        if (results.size() > 0)
            return results;
        return null;
    }

    @Override
    public OrganizationMember findMemberJobLevelByDetailId(Long detailId) {
        List<OrganizationMember> results = new ArrayList<>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId)
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.JOB_LEVEL.getCode()));
        query.addConditions(condition);
        Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchAny();

        if (r != null)
            return ConvertHelper.convert(r, OrganizationMember.class);
        return null;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMemberByTokens(String contactPhone, List<Long> organizationIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L).and(t2.field("contact_token").eq(contactPhone))
                .and(t1.field("organization_id").in(organizationIds))
                .and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));

        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").asc()).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        return result;
//
//        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactPhone);
//        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(organizationIds));
//        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        query.addConditions(condition);
//        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.asc());
//        query.fetch().map(r -> {
//            result.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//        return result;
    }


    @Override
    public List<Organization> listOrganizationByGroupTypes(String superiorPath, List<String> groupTypes) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.PATH.like(superiorPath));

        if (null != groupTypes && groupTypes.size() > 0)
            query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.in(groupTypes));

        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        query.addOrderBy(Tables.EH_ORGANIZATIONS.LEVEL.asc(), Tables.EH_ORGANIZATIONS.ID.desc());

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });

        return result;
    }

/*    @Override
    public List<Organization> listOrganizationByGroupType(Long parentId, OrganizationGroupType groupType) {
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(groupType.getCode());
        return this.listOrganizationByGroupTypes(parentId, groupTypes);
    }*/

    @Override
    public List<Organization> listOrganizationByGroupTypes(Long parentId, List<String> groupTypes) {
        return this.listOrganizationByGroupTypes(parentId, groupTypes, null);
    }

    @Override
    public List<Organization> listOrganizationByGroupTypes(Long parentId, List<String> groupTypes, String keyworks) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class));

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentId));

        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        if (null != groupTypes && groupTypes.size() > 0)
            query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.in(groupTypes));

        if (!StringUtils.isEmpty(keyworks)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like(keyworks + "%"));
        }

        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });

        return result;
    }

    @Override
    public List<Organization> listOrganizationByGroupTypes(Long parentId, List<String> groupTypes, String keyword, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class));

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.in(groupTypes));

        if (null != pageAnchor && pageAnchor != 0)
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.gt(pageAnchor));
        if (null != parentId)
            query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentId));
        if (!StringUtils.isEmpty(keyword)) {
            query.addJoin(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS, Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ORGANIZATION_ID));
            query.addJoin(Tables.EH_ORGANIZATION_JOB_POSITIONS, Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.eq(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.JOB_POSITION_ID));
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like("%" + keyword + "%").or(Tables.EH_ORGANIZATION_JOB_POSITIONS.NAME.like("%" + keyword + "%")));

        }
        if (null != pageSize)
            query.addLimit(pageSize);

        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.asc());
        if (!StringUtils.isEmpty(keyword)) {
            query.addGroupBy(Tables.EH_ORGANIZATIONS.ID);
            return query.fetch().map(new DefaultRecordMapper(Tables.EH_ORGANIZATIONS.recordType(), Organization.class));

        } else {
            query.fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, Organization.class));
                return null;
            });
        }
        return result;
    }

    @Override
    public List<Organization> listOrganizationByGroupTypesAndPath(String path, List<String> groupTypes, String keyword, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class));

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.in(groupTypes));

        if (null != pageAnchor && pageAnchor != 0)
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.gt(pageAnchor));
        if (null != path)
            query.addConditions(Tables.EH_ORGANIZATIONS.PATH.like(path));
        if (!StringUtils.isEmpty(keyword)) {
            query.addJoin(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS, Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ORGANIZATION_ID));
            query.addJoin(Tables.EH_ORGANIZATION_JOB_POSITIONS, Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.eq(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.JOB_POSITION_ID));
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like("%" + keyword + "%").or(Tables.EH_ORGANIZATION_JOB_POSITIONS.NAME.like("%" + keyword + "%")));

        }
        if (null != pageSize)
            query.addLimit(pageSize);

        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.asc());
        if (!StringUtils.isEmpty(keyword)) {
            query.addGroupBy(Tables.EH_ORGANIZATIONS.ID);
            return query.fetch().map(new DefaultRecordMapper(Tables.EH_ORGANIZATIONS.recordType(), Organization.class));

        } else {
            query.fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, Organization.class));
                return null;
            });
        }
        return result;
    }

    @Override
    public void createOrganizationDetail(OrganizationDetail organizationDetail) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationDetails.class));
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, organizationDetail.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationDetailsDao dao = new EhOrganizationDetailsDao(context.configuration());
        organizationDetail.setId(id);
        dao.insert(organizationDetail);
    }

    @Override
    public void updateOrganizationDetail(OrganizationDetail organizationDetail) {
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, organizationDetail.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationDetailsDao dao = new EhOrganizationDetailsDao(context.configuration());
        dao.update(organizationDetail);
    }

    @Override
    public OrganizationDetail findOrganizationDetailByOrganizationId(Long organizationId) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_DETAILS);
        query.addConditions(Tables.EH_ORGANIZATION_DETAILS.ORGANIZATION_ID.eq(organizationId));
        List<OrganizationDetail> organizationDetails = new ArrayList<OrganizationDetail>();
        query.fetch().map(r -> {
            organizationDetails.add(ConvertHelper.convert(r, OrganizationDetail.class));
            return null;
        });
        if (0 == organizationDetails.size()) {
            return null;
        }
        return organizationDetails.get(0);
    }


    @Override
    public void createOrganizationCommunityRequest(OrganizationCommunityRequest organizationCommunityRequest) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationCommunityRequests.class));
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, organizationCommunityRequest.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        organizationCommunityRequest.setId(id);
        organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationCommunityRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhOrganizationCommunityRequestsDao dao = new EhOrganizationCommunityRequestsDao(context.configuration());
        dao.insert(organizationCommunityRequest);
    }

    @Override
    public void updateOrganizationCommunityRequest(OrganizationCommunityRequest organizationCommunityRequest) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, organizationCommunityRequest.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationCommunityRequestsDao dao = new EhOrganizationCommunityRequestsDao(context.configuration());
        organizationCommunityRequest.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(organizationCommunityRequest);
    }

    @Override
    public OrganizationCommunityRequest getOrganizationRequest(Long organizationid) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.eq(OrganizationCommunityRequestStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.eq(organizationid));
        return ConvertHelper.convert(query.fetchAny(), OrganizationCommunityRequest.class);
    }

    @Override
    public void updateOrganizationCommunityRequestByOrgIds(List<Long> orgIds, Byte status, Long uid, Timestamp now) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        int count = context.update(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS).set(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS, status)
                .set(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.OPERATOR_UID, uid)
                .set(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.UPDATE_TIME, now)
                .where(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.in(orgIds)
                        .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode())))
                .execute();

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationCommunityRequests.class, null);
    }

    /*@Override
    public void deleteOrganizationCommunityRequestById(OrganizationCommunityRequest organizationCommunityRequest) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, organizationCommunityRequest.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        organizationCommunityRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhOrganizationCommunityRequestsDao dao = new EhOrganizationCommunityRequestsDao(context.configuration());
        dao.delete(organizationCommunityRequest);
    }*/

    /*@Override
    public OrganizationCommunityRequest getOrganizationCommunityRequestById(Long id) {
        OrganizationCommunityRequest[] result = new OrganizationCommunityRequest[1];

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    result[0] = context.select().from(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS)
                            .where(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.eq(id))
                            .fetchAny().map((r) -> {
                                return ConvertHelper.convert(r, OrganizationCommunityRequest.class);
                            });

                    if (result[0] != null) {
                        return false;
                    } else {
                        return true;
                    }
                });

        return result[0];
    }*/

    @Override
    public OrganizationCommunityRequest getOrganizationCommunityRequestByOrganizationId(Long organizationId) {
//    	OrganizationCommunityRequest[] result = new OrganizationCommunityRequest[1];
//
//        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null,
//            (DSLContext context, Object reducingContext) -> {
//                result[0] = context.select().from(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS)
//                    .where(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.eq(organizationId))
//                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()))
//                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.ne(OrganizationCommunityRequestStatus.INACTIVE.getCode()))
//                    .fetchAny().map((r) -> {
//                        return ConvertHelper.convert(r, OrganizationCommunityRequest.class);
//                    });
//
//                if (result[0] != null) {
//                    return false;
//                } else {
//                    return true;
//                }
//            });
//
//        return result[0];

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationCommunityRequestsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.eq(organizationId));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.eq(OrganizationCommunityRequestStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.desc());
        List<OrganizationCommunityRequest> request = new ArrayList<OrganizationCommunityRequest>();
        query.fetch().map(r -> {
            request.add(ConvertHelper.convert(r, OrganizationCommunityRequest.class));
            return null;
        });
        if (0 == request.size()) {
            return null;
        }
        return request.get(0);
    }

    @Override
    public List<OrganizationCommunityRequest> queryOrganizationCommunityRequestByCommunityId(ListingLocator locator, Long comunityId
            , int count, ListingQueryBuilderCallback queryBuilderCallback) {
        final List<OrganizationCommunityRequest> contacts = new ArrayList<OrganizationCommunityRequest>();
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, comunityId));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        count = count + 1;
        SelectQuery<EhOrganizationCommunityRequestsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.lt(locator.getAnchor()));
        }
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.ne(OrganizationCommunityRequestStatus.INACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(comunityId));
        query.addGroupBy(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID);  //也就mysql支持这种错误的语法
        query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.desc());
        query.addLimit(count - contacts.size());
        query.fetch().map((r) -> {
            contacts.add(ConvertHelper.convert(r, OrganizationCommunityRequest.class));
            return null;
        });
        locator.setAnchor(null);
        if (contacts.size() >= count) {
            contacts.remove(contacts.size() - 1);
            locator.setAnchor(contacts.get(contacts.size() - 1).getMemberId());
        }

        return contacts;
    }

    @Override
    public OrganizationCommunityRequest findOrganizationCommunityRequestByOrganizationId(Long communityId, Long organizationId) {
        ListingLocator locator = new ListingLocator();
        List<OrganizationCommunityRequest> enterprises = this.queryOrganizationCommunityRequestByCommunityId(locator, communityId, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(communityId));
                query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.eq(organizationId));
                query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
                query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.ne(OrganizationCommunityRequestStatus.INACTIVE.getCode()));
                return query;
            }

        });

        if (null != enterprises && enterprises.size() > 0) {
            return enterprises.get(0);
        }

        return null;
    }

    @Override
    public List<OrganizationCommunityRequest> queryOrganizationCommunityRequests(CrossShardListingLocator locator, int count,
                                                                                 ListingQueryBuilderCallback queryBuilderCallback) {
        final List<OrganizationCommunityRequest> contacts = new ArrayList<OrganizationCommunityRequest>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);

            locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhOrganizationCommunityRequestsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);

            if (queryBuilderCallback != null)
                queryBuilderCallback.buildCondition(locator, query);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.asc());
            query.addLimit(count - contacts.size());

            query.fetch().map((r) -> {
                contacts.add(ConvertHelper.convert(r, OrganizationCommunityRequest.class));
                return null;
            });

            if (contacts.size() >= count) {
                locator.setAnchor(contacts.get(contacts.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
                return AfterAction.next;
            }

        });
        return contacts;
    }


    @Override
    public void createOrganizationAttachment(OrganizationAttachment attachment) {

        assert (attachment.getOrganizationId() != null);

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, attachment.getCommunityId()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationAttachments.class));
        attachment.setId(id);

        EhOrganizationAttachmentsDao dao = new EhOrganizationAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, OrganizationAttachment.class, null);
    }

    @Override
    public void createOrganizationAddress(OrganizationAddress address) {

        assert (address.getOrganizationId() != null);

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, address.getCommunityId()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationAddresses.class));
        address.setId(id);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setUpdateTime(address.getCreateTime());
        EhOrganizationAddressesDao dao = new EhOrganizationAddressesDao(context.configuration());
        dao.insert(address);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationAddresses.class, null);
    }

    @Override
    public void createOrganizationAddressMapping(CommunityAddressMapping addressMapping) {

        assert (addressMapping.getOrganizationId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationAddressMappings.class));
        addressMapping.setId(id);

        EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
        dao.insert(addressMapping);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationAddressMappings.class, null);
    }

    @Override
    public void updateOrganizationAddressMapping(CommunityAddressMapping addressMapping) {

        assert (addressMapping.getOrganizationId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
        dao.update(addressMapping);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAddressMappings.class, null);
    }

    @Override
    public List<OrganizationAttachment> listOrganizationAttachments(long organizationId) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class, organizationId));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        return context.selectFrom(Tables.EH_ORGANIZATION_ATTACHMENTS)
                .where(Tables.EH_ORGANIZATION_ATTACHMENTS.ORGANIZATION_ID.eq(organizationId))
                .fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, OrganizationAttachment.class);
                });
    }


    @Override
    public Organization findOrganizationByAddressId(long addressId) {
        final Organization[] result = new Organization[1];

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), result,
        dbProvider.mapReduce(AccessSpec.readOnly(), result,
                (DSLContext context, Object reducingContext) -> {
                    List<Organization> list = context.select().from(Tables.EH_ORGANIZATIONS).leftOuterJoin(Tables.EH_ORGANIZATION_ADDRESSES)
                            .on(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID))
                            .where(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(addressId))
                            .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                            .fetch().map((r) -> {
                                return ConvertHelper.convert(r, Organization.class);
                            });

                    if (list != null && !list.isEmpty()) {
                        result[0] = list.get(0);
                        return false;
                    }

                    return true;
                });

        return result[0];
    }

    /*@Override
    public Boolean isExistInOrganizationAddresses(long organizationId,
                                                  long addressId) {
        List<Integer> addr = new ArrayList<Integer>();
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class, organizationId), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId));
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(addressId));
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.ne(EnterpriseAddressStatus.INACTIVE.getCode()));
                    List<EhOrganizationAddressesRecord> r = query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        return record;
                    });

                    if (r != null && !r.isEmpty()) {
                        addr.add(1);
                    }

                    return true;
                });


        return !addr.isEmpty();
    }*/


    @Override
    public void deleteOrganizationAttachmentsByOrganizationId(long organizationId) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class, organizationId), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhOrganizationAttachmentsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ATTACHMENTS);
                    query.addConditions(Tables.EH_ORGANIZATION_ATTACHMENTS.ORGANIZATION_ID.eq(organizationId));
                    query.fetch().map((EhOrganizationAttachmentsRecord record) -> {
                        deleteOrganizationeAttachmentsById(record.getId());
                        return null;
                    });

                    return true;
                });


    }

    @Override
    public void deleteOrganizationAddressByOrganizationId(long organizationId) {

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class, organizationId), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId));
                    query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        this.deleteOrganizationAddressById(record.getId());
                        return null;
                    });

                    return true;
                });


    }

    private void deleteOrganizationeAttachmentsById(long id) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationAttachmentsDao dao = new EhOrganizationAttachmentsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public List<OrganizationAddress> findOrganizationAddressByOrganizationId(
            Long organizationId) {

        List<OrganizationAddress> ea = new ArrayList<OrganizationAddress>();
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class, organizationId), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId));
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.ne(OrganizationAddressStatus.INACTIVE.getCode()));
                    query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        ea.add(ConvertHelper.convert(record, OrganizationAddress.class));
                        return null;
                    });

                    return true;
                });
        return ea;
    }

    @Override
    public List<OrganizationAddress> findOrganizationAddressByOrganizationIds(
            List<Long> organizationIds){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
        query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.in(organizationIds));
        query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.ne(OrganizationAddressStatus.INACTIVE.getCode()));
        List<OrganizationAddress> list = query.fetch().map(r -> ConvertHelper.convert(r, OrganizationAddress.class));

        return list;
    }


    @Override
    public void deleteOrganizationAddress(OrganizationAddress address) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, address.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhOrganizationAddressesDao dao = new EhOrganizationAddressesDao(context.configuration());
        dao.delete(address);
    }

    @Override
    public void deleteOrganizationAddressById(Long id) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, id));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationAddressesDao dao = new EhOrganizationAddressesDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public void updateOrganizationAddress(OrganizationAddress oa) {
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizations.class, oa.getCommunityId()));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//		oa.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhOrganizationAddressesDao dao = new EhOrganizationAddressesDao(context.configuration());
        oa.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(oa);

    }

    @Override
    public OrganizationAddress findOrganizationAddressByAddressId(Long addressId) {
        final OrganizationAddress[] result = new OrganizationAddress[1];

        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizationAddresses.class), result,
        this.dbProvider.mapReduce(AccessSpec.readOnly(), result,
                (DSLContext context, Object reducingContext) -> {
                    context.select().from(Tables.EH_ORGANIZATION_ADDRESSES)
                            .where(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(addressId).and(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.eq(OrganizationAddressStatus.ACTIVE.getCode())))
                            .fetch().map(r -> {
                        return result[0] = ConvertHelper.convert(r, OrganizationAddress.class);
                    });
                    return true;

                });

        return result[0];
    }

    @Override
    public List<OrganizationAddress> listOrganizationAddressByBuildingId(Long buildingId, Integer pageSize, CrossShardListingLocator locator) {
        List<OrganizationAddress> addresses = new ArrayList<OrganizationAddress>();
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    int size = pageSize + 1;
                    Condition cond = Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_ID.eq(buildingId);
                    cond = cond.and(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.ne(OrganizationAddressStatus.INACTIVE.getCode()));
                    if (null != locator.getAnchor()) {
                        cond = cond.and(Tables.EH_ORGANIZATION_ADDRESSES.ID.lt(locator.getAnchor()));
                    }
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(cond);
                    query.addGroupBy(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID);
                    query.addOrderBy(Tables.EH_ORGANIZATION_ADDRESSES.ID.desc());
                    query.addLimit(size);
                    query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        addresses.add(ConvertHelper.convert(record, OrganizationAddress.class));
                        return null;
                    });

                    return true;
                });

        locator.setAnchor(null);
        if (addresses.size() >= pageSize) {
            addresses.remove(addresses.size() - 1);
            locator.setAnchor(addresses.get(addresses.size() - 1).getId());
        }
        return addresses;
    }


    @Override
    public List<Long> listOrganizationIdByBuildingId(Long buildingId, byte setAdminFlag, int pageSize, CrossShardListingLocator locator) {
        List<Long> organizationIds = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null, (DSLContext context, Object reducingContext) -> {
            int size = pageSize + 1;
            organizationIds.addAll(context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
                    .where(Tables.EH_ORGANIZATIONS.SET_ADMIN_FLAG.eq(setAdminFlag))
                    .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
                    .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L))
                    .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                    .andExists(
                            context.select().from(Tables.EH_ORGANIZATION_ADDRESSES)
                                    .where(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID))
                                    .and(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_ID.eq(buildingId))
                                    .and(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.ne(OrganizationAddressStatus.INACTIVE.getCode()))
                    ).orderBy(Tables.EH_ORGANIZATIONS.ID.desc())
                    .limit(size)
                    .fetch().map(r -> r.getValue(Tables.EH_ORGANIZATIONS.ID))
            );
            return true;
        });

        locator.setAnchor(null);
        if (organizationIds.size() >= pageSize) {
            organizationIds.remove(organizationIds.size() - 1);
            locator.setAnchor(organizationIds.get(organizationIds.size() - 1));
        }
        return organizationIds;
    }

    @Override
    public List<Long> listOrganizationIdByCommunityId(Long communityId, byte setAdminFlag, int pageSize,
                                                      CrossShardListingLocator locator) {
        List<Long> organizationIds = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null, (DSLContext context, Object reducingContext) -> {
            int size = pageSize + 1;
            organizationIds.addAll(context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS)
                    .where(Tables.EH_ORGANIZATIONS.SET_ADMIN_FLAG.eq(setAdminFlag))
                    .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
                    .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L))
                    .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                    .andExists(
                            context.select().from(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS)
                                    .where(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID))
                                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.ne(OrganizationCommunityRequestStatus.INACTIVE.getCode()))
                                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()))
                                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(communityId))
                    ).orderBy(Tables.EH_ORGANIZATIONS.ID.desc())
                    .limit(size)
                    .fetch().map(r -> r.getValue(Tables.EH_ORGANIZATIONS.ID))
            );
            return true;
        });

        locator.setAnchor(null);
        if (organizationIds.size() >= pageSize) {
            organizationIds.remove(organizationIds.size() - 1);
            locator.setAnchor(organizationIds.get(organizationIds.size() - 1));
        }
        return organizationIds;
    }

    @Override
    public List<OrganizationAddress> listOrganizationAddressByBuildingName(String buildingName) {
        List<OrganizationAddress> addresses = new ArrayList<OrganizationAddress>();
        // eh_organizations不是key table，不能使用key table的方式操作 by lqs 20160722
        // dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizations.class), null,
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    Condition cond = Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_NAME.eq(buildingName);
                    cond = cond.and(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.eq(OrganizationAddressStatus.ACTIVE.getCode()));
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(cond);
                    query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        addresses.add(ConvertHelper.convert(record, OrganizationAddress.class));
                        return null;
                    });

                    return true;
                });
        return addresses;
    }

    @Override
    public Organization getOrganizationByGoupId(Long groupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Record r = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.GROUP_ID.eq(groupId)).fetchOne();
        if (r != null)
            return ConvertHelper.convert(r, Organization.class);
        return null;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByOrgIdAndMemberGroup(Long orgId, String memberGroup) {
        return listOrganizationMembersByOrgIdAndMemberGroup(orgId, memberGroup, null);
    }


    @Override
    public List<OrganizationMember> listOrganizationMembersByOrgIdAndMemberGroup(
            Long orgId, String memberGroup, Long userId) {
        List<OrganizationMember> list = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Condition cond = Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode());
        cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.MEMBER_GROUP.eq(memberGroup));
        if (null != orgId) {
            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId));
        }
        if (null != userId) {
            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
            cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId));
        }

        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(cond).fetch();

        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationMember.class));
        }
        return list;
    }


    @Override
    public List<OrganizationMember> getOrganizationMemberByOrgIds(List<Long> ids, Condition cond) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(ids));
        query.addConditions(cond);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }

    @Override
    public List<OrganizationMember> getOrganizationMemberByOrgIds(List<Long> ids, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(ids));
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(null, query);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        return result;
    }


    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMembersByUId(Long uId) {
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("target_id").eq(uId).and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        List<OrganizationMember> records = step.where(condition).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        return result;
//
//        SendResult<Record> records = context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
//                .where(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(uId))
//                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode())).fetch();
//
//        if (records != null && !records.isEmpty()) {
//            for (Record r : records)
//                list.add(ConvertHelper.convert(r, OrganizationMember.class));
//        }
//        return list;
    }

    @Override
    public List<OrganizationMember> listAllOrganizationMembersByUID(List<Long> uIds) {
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        Condition condition = t1.field("target_id").in(uIds);
        result = context.select().from(t1).where(condition).orderBy(t1.field("id").desc()).fetch()
                .map((r) -> {
                    return ConvertHelper.convert(r, OrganizationMember.class);
                });
        return result;
    }

    @Override
    public List<OrganizationTaskTarget> listOrganizationTaskTargetsByOwner(String ownerType, Long ownerId, String taskType) {
        List<OrganizationTaskTarget> list = new ArrayList<OrganizationTaskTarget>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Condition cond = Tables.EH_ORGANIZATION_TASK_TARGETS.OWNER_TYPE.eq(ownerType);
        cond = cond.and(Tables.EH_ORGANIZATION_TASK_TARGETS.OWNER_ID.eq(ownerId));
        cond = cond.and(Tables.EH_ORGANIZATION_TASK_TARGETS.TASK_TYPE.eq(taskType));
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_TASK_TARGETS)
                .where(cond).fetch();
        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationTaskTarget.class));
        }
        return list;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMembersTargetIdExist() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode())
                .and(t1.field("target_id").ne(0L))
                .and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        List<OrganizationMember> records = step.where(condition).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        return result;

//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.ne(0L));
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
//        query.fetch().map((r) -> {
//            result.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//        return result;
    }

    @Override
    public void createOrganizationOwner(OrganizationOwner owner) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        InsertQuery<EhOrganizationOwnersRecord> query = context.insertQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.setRecord(ConvertHelper.convert(owner, EhOrganizationOwnersRecord.class));
        query.setReturning(Tables.EH_ORGANIZATION_OWNERS.ID);
        query.execute();
        owner.setId(query.getReturnedRecord().getId());

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationTasks.class, null);
    }

    @Override
    public OrganizationOwner getOrganizationOwnerByTokenOraddressId(String contactToken, Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TYPE.eq(ContactType.MOBILE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ADDRESS_ID.eq(addressId));
        return ConvertHelper.convert(query.fetchAny(), OrganizationOwner.class);
    }

    @Override
    public void deleteOrganizationOwnerById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
        dao.deleteById(id);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwners.class, id);
    }

    @Override
    public void updateOrganizationOwner(OrganizationOwner organizationOwner) {
        assert (organizationOwner.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnersDao dao = new EhOrganizationOwnersDao(context.configuration());
        dao.update(organizationOwner);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationOwners.class, organizationOwner.getId());
    }

    @Override
    public List<OrganizationOwner> listOrganizationOwnerByCommunityId(Long communityId, ListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback) {
        Integer count = null == pageSize ? 0 : pageSize + 1;

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.like("%" + communityId + "%"));
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (null != locator.getAnchor())
            query.addConditions(Tables.EH_ORGANIZATION_OWNERS.ID.lt(locator.getAnchor()));

        query.addOrderBy(Tables.EH_ORGANIZATION_OWNERS.ID.desc());
        if (null != pageSize)
            query.addLimit(count);

        List<OrganizationOwner> owners = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OrganizationOwner.class);
        });

        if (null != pageSize) {
            if (owners.size() > pageSize) {
                owners = owners.subList(0, pageSize);
                locator.setAnchor(owners.get(pageSize - 1).getId());
            }
        }
        return owners;
    }

    @Override
    public List<OrganizationOwner> findOrganizationOwnerByTokenOrNamespaceId(String contactToken, Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TOKEN.eq(contactToken));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_TYPE.eq(ContactType.MOBILE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));

        List<OrganizationOwner> owners = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, OrganizationOwner.class);
        });
        return owners;
    }

    @Override
    public Organization findOrganizationByGroupId(Long groupId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_ID.eq(groupId));

        return ConvertHelper.convert(query.fetchAny(), Organization.class);

    }

    /**
     * List organization by name. by Janson
     */
    @Override
    public List<Organization> listOrganizationByName(ListingLocator locator, int count, Integer namespaceId, String name) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        if (namespaceId != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        }

        if (name != null && !"".equals(name)) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like("%" + name + "%"));
        }

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.ID.lt(locator.getAnchor()));
        }
        query.addLimit(count);
        query.addOrderBy(Tables.EH_ORGANIZATIONS.ID.desc());

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });

        if (result.size() >= count) {
            locator.setAnchor(result.get(result.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return result;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMemberByOrganizationIds(
            ListingLocator locator, int pageSize, Condition cond,
            List<Long> organizationIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L);
        condition = condition.and(t1.field("organization_id").in(organizationIds));
        if (null != locator.getAnchor()) {
            condition = condition.and(t1.field("create_time").lt(new Timestamp(locator.getAnchor())));
        }
        if (null != cond) {
            condition = condition.and(cond);
        }
        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("create_time").desc()).limit(pageSize + 1).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }

        locator.setAnchor(null);
        if (result.size() > pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getCreateTime().getTime());
        }

        return result;

//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(organizationIds));
//        if (null != locator.getAnchor()) {
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
//        }
//        if (null != cond) {
//            query.addConditions(cond);
//        }
//        query.addLimit(pageSize + 1);
//        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.CREATE_TIME.desc());
//        query.fetch().map(r -> {
//            results.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//
//        locator.setAnchor(null);
//        if (results.size() > pageSize) {
//            results.remove(results.size() - 1);
//            locator.setAnchor(results.get(results.size() - 1).getCreateTime().getTime());
//        }
//
//        return results;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    public List<OrganizationMember> listOrganizationMemberByContactTokens(List<String> contactTokens, Long organizationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t2.field("contact_token").in(contactTokens)
                .and(t1.field("organization_id").eq(organizationId))
                .and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        List<OrganizationMember> records = step.where(condition).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        return result;
//
//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.in(contactTokens));
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizationId));
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
//        query.fetch().map(r -> {
//            results.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//        return results;
    }

    @Override
    public Organization findOrganizationByNameAndNamespaceId(String name,
                                                             Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Record r = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()))
                .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode())).fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, Organization.class);
        return null;
    }

    @Override
    public Organization findOrganizationByName(String name, String groupType, Long parentId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Record r = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(groupType))
                .and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentId))
                .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()))
                .fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, Organization.class);
        return null;
    }

    @Override
    public List listOrganizationByName(String name, String groupType, Long parentId, Integer namespaceId) {
        return listOrganizationByName(name, groupType, parentId, namespaceId, null);
    }

    @Override
    public List listOrganizationByName(String name, String groupType, Long parentId, Integer namespaceId, Long enterpriseId) {
        List<Organization> result = new ArrayList<Organization>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like("%" + name + "%"));
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        if (parentId != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentId));
        }
        if (groupType != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(groupType));
        }
        if (enterpriseId != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.PATH.like("/" + enterpriseId + "%"));
        }

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });
        return result;
    }

    @Override
    public Organization findOrganizationByNameAndNamespaceIdForJindie(String name, Integer namespaceId,
                                                                      String namespaceToken, String namespaceType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ORGANIZATION_TYPE.eq(namespaceType))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ORGANIZATION_TOKEN.eq(namespaceToken))
                .fetchOne();

        if (record != null) {
            return ConvertHelper.convert(record, Organization.class);
        }

        //组织名字有重复的
        Result<Record> result = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .fetch();

        if (result != null && result.isNotEmpty()) {
            List<Organization> organizationList = result.map(r -> ConvertHelper.convert(r, Organization.class));
            if (organizationList.size() > 1) {
                for (Organization organization : organizationList) {
                    if (OrganizationStatus.fromCode(organization.getStatus()) == OrganizationStatus.ACTIVE
                            && (organization.getNamespaceOrganizationToken() == null || organization.getNamespaceOrganizationToken().equals(namespaceToken))) {
                        return organization;
                    }
                }
            } else if (organizationList.get(0).getNamespaceOrganizationToken() == null || organizationList.get(0).getNamespaceOrganizationToken().equals(namespaceToken)) {
                return organizationList.get(0);
            }
        }

        return null;
    }

    @Override
    public Organization findOrganizationByName(String name, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_ORGANIZATIONS.fields());
        query.addFrom(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.NAME.eq(name));
        //没有域名，重复率会高
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0l));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        List<Organization> list = query.fetchInto(Organization.class);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    // by Janson
    @Cacheable(value = "listGroupMessageMembers", key = "{#namespaceId, #groupId, #pageSize}", unless = "#result.getSize() == 0")
    @Override
    public GroupMemberCaches listGroupMessageMembers(Integer namespaceId, Long groupId, int pageSize) {
        List<GroupMember> members = new ArrayList<GroupMember>();
        Organization organization = findOrganizationByGroupId(groupId);
        if (null != organization) {
            List<OrganizationMember> organizationMember = listOrganizationMembersByOrgId(organization.getId());
            for (OrganizationMember member : organizationMember) {
                if (OrganizationMemberStatus.fromCode(member.getStatus()) == OrganizationMemberStatus.ACTIVE && OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER) {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setMemberId(member.getTargetId());
                    groupMember.setMemberType(EntityType.USER.getCode());
                    members.add(groupMember);
                }
            }
        }

        GroupMemberCaches caches = new GroupMemberCaches();
        caches.setMembers(members);
        caches.setTick(System.currentTimeMillis());

        return caches;
    }

    @Caching(evict = {@CacheEvict(value = "listGroupMessageMembers", key = "{#namespaceId, #groupId, #pageSize}")})
    @Override
    public void evictGroupMessageMembers(Integer namespaceId, Long groupId, int pageSize) {
    }

    @Override
    public List<Organization> listOrganizationByEmailDomainAndNamespace(Integer namesapceId, String emailDomain, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addJoin(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.eq(Tables.EH_ORGANIZATIONS.ID)
                        .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode())));
        query.setDistinct(true);
        if (communityId != null)
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.eq(OrganizationCommunityRequestStatus.ACTIVE.getCode()));
        if (emailDomain != null)
            query.addConditions(Tables.EH_ORGANIZATIONS.STRING_TAG1.eq(emailDomain));
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namesapceId));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        List<EhOrganizationsRecord> records = query.fetch().map(new EhOrganizationRecordMapper());
        List<Organization> organizations = records.stream().map((r) -> {
            return ConvertHelper.convert(r, Organization.class);
        }).collect(Collectors.toList());

        if (organizations == null || organizations.size() == 0)
            return null;
        return organizations;
    }

    @Override
    public List<OrganizationCommunityRequest> listOrganizationCommunityRequests(Long communityId) {
        List<OrganizationCommunityRequest> results = new ArrayList<OrganizationCommunityRequest>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationCommunityRequestsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.eq(OrganizationCommunityRequestStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(communityId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationCommunityRequest.class));
            return null;
        });
        return results;

    }

    @Override
    public List<OrganizationCommunityRequest> listOrganizationCommunityRequestsByOrganizationId(Long organizationId) {
        List<OrganizationCommunityRequest> results = new ArrayList<OrganizationCommunityRequest>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationCommunityRequestsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.eq(OrganizationCommunityRequestStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID.eq(organizationId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationCommunityRequest.class));
            return null;
        });
        return results;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public OrganizationMember getOrganizationMemberByContactToken(Integer currentNamespaceId, String email) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t2.field("contact_token").eq(email);
        condition = condition.and(t1.field("namespace_id").eq(currentNamespaceId));
        condition = condition.and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("organization_id").asc()).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }

        if (null == result || result.size() == 0)
            return null;
        return result.get(0);

//        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(email);
//        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(currentNamespaceId));
//        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        query.addConditions(condition);
//        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.asc());
//        query.fetch().map(r -> {
//            result.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//        if (null == result || result.size() == 0)
//            return null;
//        return result.get(0);
    }

    /*@Override
    public List<Community> listOrganizationCommunitiesByKeyword(Long orgId, String keyword) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Community> result = new ArrayList<Community>();
        SelectQuery<EhCommunitiesRecord> query = context.selectQuery(Tables.EH_COMMUNITIES);
        if (orgId != null && orgId > 0) {
            query.addJoin(Tables.EH_ORGANIZATION_COMMUNITIES, Tables.EH_COMMUNITIES.ID.eq(Tables.EH_ORGANIZATION_COMMUNITIES.COMMUNITY_ID));
            query.addConditions(Tables.EH_ORGANIZATION_COMMUNITIES.ORGANIZATION_ID.eq(orgId));
        }
        if (!StringUtils.isEmpty(keyword)) {
            query.addConditions(Tables.EH_COMMUNITIES.NAME.like("%" + keyword + "%")
                    .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like("%" + keyword + "%")));
        }

        query.addOrderBy(Tables.EH_ORGANIZATION_COMMUNITIES.ID.desc());
        result = query.fetch().map(new DefaultRecordMapper(Tables.EH_COMMUNITIES.recordType(), Community.class));

        if (result == null || result.size() == 0) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("The community for the organization is not found, organizationId=" + orgId);
                LOGGER.warn(query.getSQL());
            }
        }

        return result;
    }*/

    @Override
    public List<OrganizationJobPositionMap> listOrganizationJobPositionMaps(Long organizationId) {
        Long startTime = System.currentTimeMillis();
        List<OrganizationJobPositionMap> results = new ArrayList<OrganizationJobPositionMap>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationJobPositionMapsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS);
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ORGANIZATION_ID.eq(organizationId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationJobPositionMap.class));
            return null;
        });

        Long endTime = System.currentTimeMillis();
        LOGGER.debug("TrackUserRelatedCost:listOrganizationJobPositionMaps: elapse:{}", endTime - startTime);

        return results;
    }

    @Override
    public List<OrganizationJobPositionMap> listOrganizationJobPositionMapsByJobPositionId(Long jobPositionId) {
        List<OrganizationJobPositionMap> results = new ArrayList<OrganizationJobPositionMap>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationJobPositionMapsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS);
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.JOB_POSITION_ID.eq(jobPositionId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationJobPositionMap.class));
            return null;
        });
        return results;
    }

    @Override
    public OrganizationJobPositionMap getOrganizationJobPositionMapByOrgIdAndJobPostionId(Long organizationId, Long jobPostionId) {
        List<OrganizationJobPositionMap> results = new ArrayList<OrganizationJobPositionMap>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationJobPositionMapsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS);
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.JOB_POSITION_ID.eq(jobPostionId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationJobPositionMap.class));
            return null;
        });
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public void deleteOrganizationJobPositionMapById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationJobPositionMapsDao dao = new EhOrganizationJobPositionMapsDao(context.configuration());
        dao.deleteById(id);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationJobPositionMaps.class, id);
    }

    @Override
    public void createOrganizationMemberLog(OrganizationMemberLog orgLog) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationMemberLogs.class));
        orgLog.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMemberLogsDao dao = new EhOrganizationMemberLogsDao(context.configuration());
        dao.insert(orgLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationMemberLogs.class, null);
    }

    @Override
    public List<OrganizationMemberLog> listOrganizationMemberLogs(Long id) {
        List<OrganizationMemberLog> results = new ArrayList<OrganizationMemberLog>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberLogsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_LOGS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.USER_ID.eq(id));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationMemberLog.class));
            return null;
        });
        if (results.size() == 0)
            return null;
        return results;
    }

    @Override
    public List<OrganizationMemberLog> listOrganizationMemberLogs(List<Long> organizationIds, String userInfoKeyword, String orgNameKeyword, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberLogsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_LOGS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.ORGANIZATION_ID.in(organizationIds));
        if (userInfoKeyword != null) {
            String keyword = "%" + userInfoKeyword + "%";
            query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBER_LOGS.USER_ID));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_NAME.like(keyword)
                    .or(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_TOKEN.like(keyword))
                    .or(Tables.EH_USERS.NICK_NAME.like(keyword))
            );
        }
        if (orgNameKeyword != null) {
            String keyword = "%" + orgNameKeyword + "%";
            query.addJoin(Tables.EH_ORGANIZATIONS, JoinType.JOIN, Tables.EH_ORGANIZATION_MEMBER_LOGS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like(keyword));
        }
        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.le(locator.getAnchor()));
        }
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.desc());
        query.addLimit(pageSize + 1);

        List<OrganizationMemberLog> list = query.fetchInto(OrganizationMemberLog.class);
        if (list != null && list.size() > pageSize) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list = list.subList(0, pageSize);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public List<OrganizationMemberLog> listOrganizationMemberLogs(List<Long> organizationIds, String userInfoKeyword, String identifierToken, String keywords, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        //SelectQuery<EhOrganizationMemberLogsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_LOGS);
        //add by huangliangming 指定返回值的ID选LOG的,不指定它会随机选导致ID不唯一前端排序出错.
        com.everhomes.server.schema.tables.EhOrganizationMemberLogs log = Tables.EH_ORGANIZATION_MEMBER_LOGS;
        Field<?>[] logFields = log.fields();
        SelectQuery<Record> query = context.select(Arrays.asList(logFields)).getQuery();
        query.addFrom(log);

        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.ORGANIZATION_ID.in(organizationIds));
        if (userInfoKeyword != null) {
            String keyword = "%" + userInfoKeyword + "%";
            query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBER_LOGS.USER_ID));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_NAME.like(keyword)
                    .or(Tables.EH_USERS.NICK_NAME.like(keyword)));
        }
        if (identifierToken != null) {
            String keyword = "%" + identifierToken + "%";
            query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_ORGANIZATION_MEMBER_LOGS.USER_ID));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_TOKEN.like(keyword));
        }
        if (keywords != null) {
            String keyword = "%" + keywords + "%";
            query.addJoin(Tables.EH_ORGANIZATIONS, JoinType.JOIN, Tables.EH_ORGANIZATION_MEMBER_LOGS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like(keyword));
        }
        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.le(locator.getAnchor()));
        }
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.OPERATION_TYPE.eq(OperationType.JOIN.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.desc());
        query.addLimit(pageSize + 1);

       // List<OrganizationMemberLog> list = query.fetchInto(OrganizationMemberLog.class);
        //add by huangliangming 指定返回值的ID选LOG的,不指定它会随机选导致ID不唯一前端排序出错.
        List<OrganizationMemberLog> list = query.fetch().map(record -> {
        	Long memberLogId = record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID);
            OrganizationMemberLog memberLog = new OrganizationMemberLog();       
            memberLog.setId(memberLogId);
            memberLog.setContactDescription(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_DESCRIPTION));
            memberLog.setContactName(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_NAME));
            memberLog.setContactToken(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_TOKEN));
            memberLog.setContactType(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.CONTACT_TYPE));
            memberLog.setNamespaceId(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.NAMESPACE_ID));
            memberLog.setOperateTime(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.OPERATE_TIME));
            memberLog.setOperationType(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.OPERATION_TYPE));
            memberLog.setOperatorUid(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.OPERATOR_UID));
            memberLog.setOrganizationId(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.ORGANIZATION_ID));
            memberLog.setRequestType(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.REQUEST_TYPE));
            memberLog.setUserId(record.getValue(Tables.EH_ORGANIZATION_MEMBER_LOGS.USER_ID));
            return memberLog;
        });
        if (list != null && list.size() > pageSize) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list = list.subList(0, pageSize);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public List<OrganizationMemberLog> listOrganizationMemberLogs(Long userId, List<Long> organizationIds, Byte operationType) {
        List<OrganizationMemberLog> results = new ArrayList<OrganizationMemberLog>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberLogsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_LOGS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.ORGANIZATION_ID.in(organizationIds));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_LOGS.OPERATION_TYPE.eq(operationType));
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.desc());
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationMemberLog.class));
            return null;
        });
        if (results.size() == 0)
            return null;
        return results;
    }


    @Override
    public List<OrganizationMember> listOrganizationPersonnels(String userInfoKeyword,String identifierToken, String orgNameKeyword, List<Long> orgIds,
                                                               Byte memberStatus, Byte contactSignedupStatus, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<>();

        SelectQuery<Record> query = context.select(Tables.EH_ORGANIZATION_MEMBERS.fields()).from(Tables.EH_ORGANIZATION_MEMBERS).getQuery();

        query.addSelect(Tables.EH_ORGANIZATIONS.NAME);
        query.addJoin(Tables.EH_ORGANIZATIONS, JoinType.JOIN, Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));

        query.addSelect(Tables.EH_USERS.NICK_NAME);
        query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID));

        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(orgIds));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(memberStatus));

        if (!StringUtils.isEmpty(userInfoKeyword)) {
            String keyword = "%" + userInfoKeyword + "%";

            Condition cond = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like(keyword).or(Tables.EH_USERS.NICK_NAME.like(keyword));
            query.addConditions(cond);
        }

        if (!StringUtils.isEmpty(identifierToken)) {
            String keyword = "%" + identifierToken + "%";

            Condition cond = Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.like(keyword);
            query.addConditions(cond);
        }
        if (!StringUtils.isEmpty(orgNameKeyword)) {
            String keyword = "%" + orgNameKeyword + "%";
            query.addConditions(Tables.EH_ORGANIZATIONS.NAME.like(keyword));
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.ne(0L));
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != locator.getAnchor()) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.lt(locator.getAnchor()));
        }
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.addLimit(pageSize);
        LOGGER.debug("sql:" + query.getSQL());
        LOGGER.debug("sql parm:" + query.getParams());
        query.fetch().map((r) -> {
            OrganizationMember member = RecordHelper.convert(r, OrganizationMember.class);
            member.setOrganizationName(r.getValue(Tables.EH_ORGANIZATIONS.NAME));
            member.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
            result.add(member);
            return member;
        });
        locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
    }

    @Override
    public void createOrganizationJobPositionMap(OrganizationJobPositionMap organizationJobPositionMap) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationJobPositionMapsDao dao = new EhOrganizationJobPositionMapsDao(context.configuration());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationJobPositionMaps.class));
        organizationJobPositionMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationJobPositionMap.setId(id);
        dao.insert(organizationJobPositionMap);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationJobPositionMaps.class, null);
    }

    @Override
    public void createOrganizationJobPosition(OrganizationJobPosition organizationJobPosition) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationJobPositionsDao dao = new EhOrganizationJobPositionsDao(context.configuration());

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationJobPositions.class));
        organizationJobPosition.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        organizationJobPosition.setId(id);
        dao.insert(organizationJobPosition);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationJobPositions.class, null);
    }

    @Override
    public void updateOrganizationJobPosition(OrganizationJobPosition organizationJobPosition) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationJobPositionsDao dao = new EhOrganizationJobPositionsDao(context.configuration());

        organizationJobPosition.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(organizationJobPosition);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationJobPositions.class, null);
    }

    @Override
    public OrganizationJobPosition findOrganizationJobPositionById(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationJobPositions.class));
        EhOrganizationJobPositionsDao dao = new EhOrganizationJobPositionsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), OrganizationJobPosition.class);
    }

    @Override
    public OrganizationJobPosition findOrganizationJobPositionByName(String ownerType, Long ownerId, String name) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationJobPositions.class));
        SelectQuery<EhOrganizationJobPositionsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_JOB_POSITIONS);

        if (ownerType != null)
            query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.OWNER_TYPE.eq(ownerType));
        if (ownerId != null)
            query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.NAME.eq(name));
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.STATUS.eq(OrganizationJobPositionStatus.ACTIVE.getCode()));

        return ConvertHelper.convert(query.fetchOne(), OrganizationJobPosition.class);
    }

    @Override
    public List<OrganizationJobPosition> listOrganizationJobPositions(String ownerType, Long ownerId, String keyword,
                                                                      Long pageAnchor, Integer pageSize) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationJobPositions.class));
        SelectQuery<EhOrganizationJobPositionsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_JOB_POSITIONS);

        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.STATUS.eq(OrganizationJobPositionStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.OWNER_TYPE.eq(ownerType));
        if (!StringUtils.isEmpty(keyword))
            query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.NAME.like("%" + keyword + "%"));
        if (null != pageAnchor && pageAnchor != 0) {
            query.addConditions(Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.gt(pageAnchor));
        }
        if (null != pageSize)
            query.addLimit(pageSize);
        query.addOrderBy(Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.asc());
        return query.fetch().stream().map(r -> ConvertHelper.convert(r, OrganizationJobPosition.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationAddress findOrganizationAddress(Long organizationId, Long addressId, Long buildingId) {
        final OrganizationAddress[] result = new OrganizationAddress[1];

        this.dbProvider.mapReduce(AccessSpec.readOnly(), result,
                (DSLContext context, Object reducingContext) -> {
                    context.select().from(Tables.EH_ORGANIZATION_ADDRESSES)
                            .where(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(addressId))
                            .and(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId))
                            .and(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_ID.eq(buildingId))
                            .fetch().map(r -> {
                        return result[0] = ConvertHelper.convert(r, OrganizationAddress.class);
                    });
                    return true;

                });

        return result[0];
    }

    @Override
    public CommunityAddressMapping findOrganizationAddressMapping(Long organizationId, Long communityId,
                                                                  Long addressId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId)
                        .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId)))
                .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ADDRESS_ID.eq(addressId))
                .fetch();

        if (records != null && !records.isEmpty()) {
            return ConvertHelper.convert(records.get(0), CommunityAddressMapping.class);
        }
        return null;
    }

    @Override
    public List<Organization> listOrganizationByNamespaceType(Integer namespaceId, String namespaceType) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAMESPACE_ORGANIZATION_TYPE.eq(namespaceType))
                .and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .fetch()
                .map(r -> ConvertHelper.convert(r, Organization.class));
    }

    @Override
    public List<OrganizationAddress> listOrganizationAddressByOrganizationId(Long organizationId) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ORGANIZATION_ADDRESSES)
                .where(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.eq(OrganizationAddressStatus.ACTIVE.getCode()))
                .fetch()
                .map(r -> ConvertHelper.convert(r, OrganizationAddress.class));
    }

    @Override
    public List<CommunityAddressMapping> listOrganizationAddressMappingByOrganizationIdAndCommunityId(
            Long organizationId, Long communityId) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(organizationId)
                        .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId)))
                .fetch()
                .map(r -> ConvertHelper.convert(r, CommunityAddressMapping.class));
    }

    @Override
    public List<EnterpriseAddress> listEnterpriseAddressByOrganization(Long organizationId) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ENTERPRISE_ADDRESSES)
                .where(Tables.EH_ENTERPRISE_ADDRESSES.ENTERPRISE_ID.eq(organizationId))
                .fetch()
                .map(r -> ConvertHelper.convert(r, EnterpriseAddress.class));
    }

    @Override
    public OrganizationAddress findOrganizationAddressByOrganizationIdAndAddressId(Long organizationId,
                                                                                   Long addressId) {
        Record record = dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ORGANIZATION_ADDRESSES)
                .where(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(addressId))
                .fetchAny();

        if (record != null) {
            return ConvertHelper.convert(record, OrganizationAddress.class);
        }
        return null;
    }

    @Override
    public List<CommunityAddressMapping> listOrganizationAddressMappingByNamespaceType(Long superOrganizationId,
                                                                                       Long communityId, String namespaceType) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select().from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .where(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID.eq(superOrganizationId)
                        .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.COMMUNITY_ID.eq(communityId)))
                .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.NAMESPACE_TYPE.eq(namespaceType))
                .fetch()
                .map(r -> ConvertHelper.convert(r, CommunityAddressMapping.class));
    }

    @Override
    public void deleteOrganizationAddressMapping(CommunityAddressMapping organizationAddressMapping) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationAddressMappingsDao dao = new EhOrganizationAddressMappingsDao(context.configuration());
        dao.delete(organizationAddressMapping);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationAddressMappings.class, null);
    }

    //@Cacheable(value="ListOrganizationMemberByPath", key="#path+#groupTypes+#contactToken")
    @Override
    public List<OrganizationMember> listOrganizationMemberByPath(String path, List<String> groupTypes, String contactToken) {
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();

        //path一定不能为空 add by sfyan 20170428
        if (StringUtils.isEmpty(path)) {
            return result;
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
//        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
//        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
//        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
//        Condition condition = t1.field("group_path").like(path + "%");
//        if (null != groupTypes && groupTypes.size() > 0)
//            condition = condition.and(t1.field("group_type").in(groupTypes));
//
//        if (!StringUtils.isEmpty(contactToken)) {
//            condition = condition.and(t1.field("contact_token").eq(contactToken));
//        }
//
//        condition = condition.and(t1.field("status").ne(OrganizationMemberStatus.INACTIVE.getCode()))
//                .and(t1.field("status").ne(OrganizationMemberStatus.REJECT.getCode()));
//        List<OrganizationMember> records = step.where(condition).orderBy(t1.field("id").desc()).fetch().map(new OrganizationMemberRecordMapper());
//        if (records != null) {
//            records.stream().map(r -> {
//                result.add(ConvertHelper.convert(r, OrganizationMember.class));
//                return null;
//            }).collect(Collectors.toList());
//        }
//        return result;

        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path + "%"));
        if (null != groupTypes) {
            if (groupTypes.size() > 0) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
            }
        }
        if (!StringUtils.isEmpty(contactToken)) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactToken));
        }
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
//		query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });

        return result;
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listOrganizationMemberByPath(String keywords, String path, List<String> groupTypes, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize) {
        return listOrganizationMemberByPath(keywords, path, groupTypes, null, visibleFlag, locator, pageSize);

//        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path + "%"));
//        if (null != groupTypes && groupTypes.size() > 0)
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
//        if (!StringUtils.isEmpty(keywords)) {
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(keywords).or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like(keywords + "%")));
//        }
//
//        if (null != visibleFlag) {
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.VISIBLE_FLAG.eq(visibleFlag.getCode()));
//        }
//
//        if (null != locator.getAnchor()) {
//            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.lt(locator.getAnchor()));
//        }
//        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
//        query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
//        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
//        query.addLimit(pageSize + 1);
//        query.fetch().map((r) -> {
//            result.add(ConvertHelper.convert(r, OrganizationMember.class));
//            return null;
//        });
//
//        locator.setAnchor(null);
//        if (result.size() > pageSize) {
//            result.remove(result.size() - 1);
//            locator.setAnchor(result.get(result.size() - 1).getId());
//        }
//        return result;
    }

    @Override
    public List<OrganizationMember> listOrganizationMemberByPath(String keywords, String path, List<String> groupTypes, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("group_path").like(path + "%");
        if (null != groupTypes && groupTypes.size() > 0)
            condition = condition.and(t1.field("group_type").in(groupTypes));
        if (!StringUtils.isEmpty(keywords)) {
            condition = condition.and(t2.field("contact_token").eq(keywords).or(t2.field("contact_name").like(keywords + "%")));
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            condition = condition.and(t1.field("target_id").ne(0L));
            condition = condition.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != visibleFlag) {
            condition = condition.and(t1.field("visible_flag").eq(visibleFlag.getCode()));
        }

        if (null != locator.getAnchor()) {
            condition = condition.and(t1.field("id").lt(locator.getAnchor()));
        }
        condition = condition.and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        List<OrganizationMember> records = step.where(condition).groupBy(t2.field("contact_token")).orderBy(t1.field("id").desc()).limit(pageSize + 1).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, OrganizationMember.class));
                return null;
            }).collect(Collectors.toList());
        }
        locator.setAnchor(null);
        if (result.size() > pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }

        return result;
    }

    @Override
    public Integer countOrganizationMemberByPath(String keywords, String path, List<String> groupTypes, Byte contactSignedupStatus, VisibleFlag visibleFlag) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep<Record1<Integer>> step = context.select(DSL.countDistinct(t2.field("contact_token"))).from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("group_path").like(path + "%");
        if (null != groupTypes && groupTypes.size() > 0)
            condition = condition.and(t1.field("group_type").in(groupTypes));
        if (!StringUtils.isEmpty(keywords)) {
            condition = condition.and(t2.field("contact_token").eq(keywords).or(t2.field("contact_name").like(keywords + "%")));
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            condition = condition.and(t1.field("target_id").ne(0L));
            condition = condition.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != visibleFlag) {
            condition = condition.and(t1.field("visible_flag").eq(visibleFlag.getCode()));
        }

        condition = condition.and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        return step.where(condition).fetchOneInto(Integer.class);
    }

    /**
     * 金地取数据使用
     */
    @Override
    public List<Organization> listOrganizationByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
                                                                    Long pageAnchor, int pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATIONS.UPDATE_TIME.eq(new Timestamp(timestamp)))
                .and(Tables.EH_ORGANIZATIONS.ID.gt(pageAnchor))
                .orderBy(Tables.EH_ORGANIZATIONS.ID.asc())
                .limit(pageSize)
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return result.map(r -> ConvertHelper.convert(r, Organization.class));
        }
        return new ArrayList<Organization>();
    }

    /**
     * 金地取数据使用
     */
    @Override
    public List<Organization> listOrganizationByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATIONS.UPDATE_TIME.gt(new Timestamp(timestamp)))
                .orderBy(Tables.EH_ORGANIZATIONS.UPDATE_TIME.asc(), Tables.EH_ORGANIZATIONS.ID.asc())
                .limit(pageSize)
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return result.map(r -> ConvertHelper.convert(r, Organization.class));
        }
        return new ArrayList<Organization>();
    }

    /**
     * 金地抓取客房数据
     */
    @Override
    public List<CommunityAddressMapping> listCsthomerelByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
                                                                             Long pageAnchor, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.fields()).from(Tables.EH_ORGANIZATIONS)
                .join(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .on(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID))
                .where(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.UPDATE_TIME.eq(new Timestamp(timestamp)))
                .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.gt(pageAnchor))
                .orderBy(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.asc())
                .limit(pageSize)
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return result.map(r -> RecordHelper.convert(r, CommunityAddressMapping.class));
        }
        return new ArrayList<CommunityAddressMapping>();
    }

    /**
     * 金地抓取客房数据
     */
    @Override
    public List<CommunityAddressMapping> listCsthomerelByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.fields()).from(Tables.EH_ORGANIZATIONS)
                .join(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS)
                .on(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ORGANIZATION_ID))
                .where(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.UPDATE_TIME.gt(new Timestamp(timestamp)))
                .orderBy(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.UPDATE_TIME.asc(), Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.asc())
                .limit(pageSize)
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return result.map(r -> RecordHelper.convert(r, CommunityAddressMapping.class));
        }
        return new ArrayList<CommunityAddressMapping>();
    }

    @Override
    public OrganizationAddress findActiveOrganizationAddressByAddressId(Long addressId) {
        List<OrganizationAddress> orgAddr = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.in(addressId));
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.eq(OrganizationAddressStatus.ACTIVE.getCode()));
                    query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        orgAddr.add(ConvertHelper.convert(record, OrganizationAddress.class));
                        return null;
                    });

                    return true;
                });
        if (orgAddr.size() == 0) {
            return null;
        }

        return orgAddr.get(0);
    }

    @Override
    public void createImportFileTask(ImportFileTask importFileTask) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhImportFileTasks.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhImportFileTasks.class));
        EhImportFileTasksDao dao = new EhImportFileTasksDao(context.configuration());
        importFileTask.setId(id);
        importFileTask.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(importFileTask);
    }

    @Override
    public void updateImportFileTask(ImportFileTask importFileTask) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhImportFileTasks.class));
        EhImportFileTasksDao dao = new EhImportFileTasksDao(context.configuration());
        importFileTask.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        LOGGER.info("update executeTask importFileTask: {}", importFileTask);
        dao.update(importFileTask);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhImportFileTasks.class, importFileTask.getId());
    }

    @Override
    public ImportFileTask findImportFileTaskById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhImportFileTasksDao dao = new EhImportFileTasksDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ImportFileTask.class);
    }

    /**
     * modify cause member_detail by lei lv
     **/
    @Override
    public List<OrganizationMember> listUsersOfEnterprise(CrossShardListingLocator locator, int pageSize, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);

        if (null != queryBuilderCallback)
            queryBuilderCallback.buildCondition(locator, query);
        if (null != locator && null != locator.getAnchor())
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ID.gt(locator.getAnchor()));

        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID);
        query.addLimit(pageSize);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });
        if (null != locator)
            locator.setAnchor(null);

        if (result.size() >= pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getId());
        }
        return result;
    }

    @Override
    public Integer countUsersOfEnterprise(CrossShardListingLocator locator, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        if (null != queryBuilderCallback)
            queryBuilderCallback.buildCondition(locator, query);
        int totalRecords = query.fetchCount();
        return totalRecords;
    }


    /**
     * modify by lei lv,增加了detail表，部分信息挪到detail表里去取
     **/
    @Override
    public List<OrganizationMember> convertMemberListAsDetailList(List<OrganizationMember> old_list) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        for (OrganizationMember member : old_list) {
            if (member.getDetailId() == null) {
                continue;
            }
            OrganizationMemberDetails detail = findOrganizationMemberDetailsByDetailId(member.getDetailId());
            if (detail == null) {
                continue;
            }
            member.setContactToken(detail.getContactToken());
            member.setContactName(detail.getContactName());
            member.setContactType(detail.getContactType());
            member.setContactDescription(detail.getContactDescription());
            member.setEmployeeNo(detail.getEmployeeNo());
            member.setGender(detail.getGender());
            member.setEmployeeStatus(detail.getEmployeeStatus());
            member.setEmploymentTime(detail.getEmploymentTime());
            member.setCheckInTime(detail.getCheckInTime());
        }
        return old_list;
    }

    /* @Override
     public List<OrganizationMemberDetails> listOrganizationMembersV2(CrossShardListingLocator locator, Integer pageSize, Organization org, List<String> groupTypes, String keywords) {

         DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
         pageSize = pageSize + 1;
         List<OrganizationMemberDetails> result = new ArrayList<>();


         // 查询正常状态的员工
         Condition condition = Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode());
         condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));

         if (null != locator && null != locator.getAnchor())
             condition = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.lt(locator.getAnchor());

         // 若只对应部门字段的人员
 //         condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(org.getId()));
         // 若需要部门下的所有人员
         condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%"));

         // 添加关键字
         if (!StringUtils.isEmpty(keywords)) {
             condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(keywords).or(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME.like("%" + keywords + "%")));
         }

         // 按条件查找并得到结果
         context.select()
                 .from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                 .join(Tables.EH_ORGANIZATION_MEMBERS, JoinType.JOIN)
                 .on(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID))
                 .where(condition)
                 .groupBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN)
                 .orderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.desc())
                 .fetch().map(r -> {
             result.add(RecordHelper.convert(r, OrganizationMemberDetails.class));
             return null;
         });

         if (null != locator)
             locator.setAnchor(null);

         if (result.size() >= pageSize) {
             result.remove(result.size() - 1);
             locator.setAnchor(result.get(result.size() - 1).getId());
         }

         return result;
     }
 */
    @Override
    public List<OrganizationMemberDetails> findDetailInfoListByIdIn(List<Long> detailIds) {
        final List<OrganizationMemberDetails> response = new ArrayList<>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhOrganizationMemberDetails.class),
                null, (DSLContext context, Object reducingContext) -> {
                    Condition condition = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(detailIds);
                    List<OrganizationMemberDetails> list = context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                            .where(condition)
                            .fetchInto(OrganizationMemberDetails.class);
                    if (list != null)
                        response.addAll(list);
                    return true;
                });
        return response;
    }


    public Long createOrganizationMemberDetails(OrganizationMemberDetails organizationMemberDetails) {
        if (organizationMemberDetails.getNamespaceId() == null) {
            Integer namespaceId = UserContext.getCurrentNamespaceId(null);
            organizationMemberDetails.setNamespaceId(namespaceId);
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationMemberDetails.class));
        organizationMemberDetails.setId(id);
        EhOrganizationMemberDetailsDao dao = new EhOrganizationMemberDetailsDao(context.configuration());
        dao.insert(organizationMemberDetails);
        if (OrganizationMemberTargetType.fromCode(organizationMemberDetails.getTargetType()) == OrganizationMemberTargetType.USER) {
            DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationMemberDetails.class, organizationMemberDetails.getId());
        }
        return id;
    }

    @Override
    public OrganizationMemberDetails findOrganizationMemberDetailsByDetailId(Long detailId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizationMemberDetails.class));
        EhOrganizationMemberDetailsDao dao = new EhOrganizationMemberDetailsDao(context.configuration());
        EhOrganizationMemberDetails memberDetails = dao.findById(detailId);
        if (memberDetails == null)
            return null;
        return ConvertHelper.convert(memberDetails, OrganizationMemberDetails.class);
    }

    @Override
    public void updateOrganizationMemberDetails(OrganizationMemberDetails organizationMemberDetails, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMemberDetailsDao dao = new EhOrganizationMemberDetailsDao(context.configuration());
        dao.update(organizationMemberDetails);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, id);
    }

    @Override
    public void deleteOrganizationMemberDetails(OrganizationMemberDetails organizationMemberDetails) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMemberDetailsDao dao = new EhOrganizationMemberDetailsDao(context.configuration());
        dao.delete(organizationMemberDetails);
    }

    /**
     * 根据公司organization_id和电话contact_id判断是否创建或更新member_detail表
     **/
    @Override
    public Long createOrUpdateOrganizationMemberDetail(OrganizationMemberDetails organizationMemberDetails) {
        return createOrUpdateOrganizationMemberDetail(organizationMemberDetails, Boolean.FALSE);
    }

    /**
     * 根据公司organization_id和电话contact_id判断是否创建或更新member_detail表
     **/
    public Long createOrUpdateOrganizationMemberDetail(OrganizationMemberDetails organizationMemberDetails, Boolean needUpdate) {
        Long organization_id = organizationMemberDetails.getOrganizationId();
        String contactToken = organizationMemberDetails.getContactToken();
        OrganizationMemberDetails detail = findOrganizationMemberDetailsByOrganizationIdAndContactToken(organization_id, contactToken);
        Long new_detail_id = 0L;
        if (detail == null) {
            new_detail_id = createOrganizationMemberDetails(organizationMemberDetails);
        } else {
            if (organizationMemberDetails.getId() != 0L && !organizationMemberDetails.getId().equals(detail.getId())) {
                LOGGER.error("organizationMemberDetails is not matched, detailId={}, organization_id={}, contact_token={}", detail.getId(), organizationMemberDetails.getOrganizationId(), organizationMemberDetails.getContactToken());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "organizationMemberDetails is not matched");
            }
            if (needUpdate == Boolean.TRUE) {
                organizationMemberDetails.setId(detail.getId());
                updateOrganizationMemberDetails(organizationMemberDetails, organizationMemberDetails.getId());
                new_detail_id = detail.getId();
            }
        }
        return new_detail_id;
    }

    @Override
    public OrganizationMemberDetails findOrganizationMemberDetailsByOrganizationIdAndContactToken(Long organizationId, String contactToken) {
        Long enterpriseId = getTopOrganizationId(organizationId);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizationMemberDetails.class));
        SelectQuery<EhOrganizationMemberDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_DETAILS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN.eq(contactToken));
        Record record = query.fetchAny();
        LOGGER.debug("sql : " + query);
        if (record != null) {
            return ConvertHelper.convert(record, OrganizationMemberDetails.class);
        }
        return null;
    }

    @Override
    public Organization findUnderOrganizationByParentOrgId(Long parentOrgId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(parentOrgId));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode()));
        List<EhOrganizationsRecord> records = query.fetch();
        if (records != null && records.size() > 0) {
            return ConvertHelper.convert(records.get(0), Organization.class);
        }
        return null;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByOrgIdWithAllStatus(Long organizaitonId) {
        List<OrganizationMember> list = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> records = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(organizaitonId)).fetch();
        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationMember.class));
        }
        return list;
    }

    /**
     * add by janson
     *
     * @param organizationId
     * @param buildId
     * @return
     */
    @Override
    public List<OrganizationAddress> findOrganizationAddressByOrganizationIdAndBuildingId(Long organizationId, Long buildId) {

        List<OrganizationAddress> ea = new ArrayList<OrganizationAddress>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<EhOrganizationAddressesRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_ADDRESSES);
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId));
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.BUILDING_ID.eq(buildId));
                    query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.ne(OrganizationAddressStatus.INACTIVE.getCode()));
                    query.fetch().map((EhOrganizationAddressesRecord record) -> {
                        ea.add(ConvertHelper.convert(record, OrganizationAddress.class));
                        return null;
                    });

                    return true;
                });
        return ea;
    }

    @Override
    public List<UserOrganizations> listUserOrganizations(CrossShardListingLocator locator, int pageSize, ListingQueryBuilderCallback callback) {
        Integer size = pageSize + 1;
        List<UserOrganizations> result = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<Record> query = context.selectQuery();
                    query.addSelect(Tables.EH_USERS.ID, Tables.EH_USERS.NAMESPACE_USER_TYPE, Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID, Tables.EH_USER_ORGANIZATIONS.STATUS,
                            Tables.EH_USERS.NICK_NAME, Tables.EH_USERS.GENDER, Tables.EH_USERS.CREATE_TIME, Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID,
                            Tables.EH_USERS.EXECUTIVE_TAG, Tables.EH_USERS.POSITION_TAG, Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN);
                    query.addFrom(Tables.EH_USERS);
                    query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
                    query.addJoin(context.select().from(Tables.EH_USER_ORGANIZATIONS).where(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.ACTIVE.getCode()).or(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode()))).asTable(Tables.EH_USER_ORGANIZATIONS.getName()),
                            JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_ORGANIZATIONS.USER_ID));
                    query.addJoin(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID).and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode())));
                    //用于按企业名称查询
                    query.addJoin(Tables.EH_ORGANIZATIONS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));

                    //按照用户在企业的中的姓名查询
                    query.addJoin(Tables.EH_ORGANIZATION_MEMBERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(Tables.EH_USERS.ID), Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID));

                    if (null != callback) {
                        callback.buildCondition(locator, query);
                    }
                    if (null != locator && null != locator.getAnchor())
                        query.addConditions(Tables.EH_USERS.ID.lt(locator.getAnchor()));

                    query.addOrderBy(Tables.EH_USERS.ID.desc());
                    query.addLimit(size);
                    LOGGER.debug("query sql:{}", query.getSQL());
                    LOGGER.debug("query param:{}", query.getBindValues());
                    query.fetch().map((r) -> {
                        UserOrganizations userOrganizations = new UserOrganizations();
                        userOrganizations.setUserId(r.getValue(Tables.EH_USERS.ID));
                        userOrganizations.setOrganizationId(r.getValue(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID));
                        userOrganizations.setStatus(r.getValue(Tables.EH_USER_ORGANIZATIONS.STATUS));
                        userOrganizations.setNickName(r.getValue(Tables.EH_USERS.NICK_NAME));
                        userOrganizations.setGender(r.getValue(Tables.EH_USERS.GENDER));
                        userOrganizations.setRegisterTime(r.getValue(Tables.EH_USERS.CREATE_TIME));
                        userOrganizations.setCommunityId(r.getValue(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID));
                        userOrganizations.setExecutiveTag(r.getValue(Tables.EH_USERS.EXECUTIVE_TAG));
                        userOrganizations.setPosition(r.getValue(Tables.EH_USERS.POSITION_TAG));
                        userOrganizations.setPhoneNumber(r.getValue(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN));
                        userOrganizations.setNamespaceUserType(r.getValue(Tables.EH_USERS.NAMESPACE_USER_TYPE));
                        result.add(userOrganizations);
                        return null;
                    });
                    return true;
                });

        locator.setAnchor(null);
        if (result.size() > pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getUserId());
        }

        return result;
    }

    @Override
    public List<Long> listMemberDetailIdWithExclude(String keywords, Integer namespaceId, String big_path, List<String> small_path,
                                                    Timestamp checkinTimeStart, Timestamp checkinTimeEnd, Timestamp dissmissTimeStart, Timestamp dissmissTimeEnd,
                                                    CrossShardListingLocator locator, Integer pageSize, List<Long> notinDetails, List<Long> inDetails, List<String> groupTypes) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));

        Condition cond = t1.field("group_path").like(big_path + "%");

        cond = cond.and(t1.field("namespace_id").eq(namespaceId));
//		cond = cond.and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));

        if (small_path != null) {
            for (String p : small_path) {
                cond = cond.and(t1.field("group_path").notLike(p + "%"));
            }
        }
        if (!StringUtils.isEmpty(keywords)) {
            cond = cond.and(t2.field("contact_name").like("%" + keywords + "%"));
        }
        if (null != groupTypes) {
            cond = cond.and(t1.field("group_type").in(groupTypes));
        }
        if (null != notinDetails) {
            cond = cond.and(t2.field("id").notIn(notinDetails));
        }
        if (null != inDetails) {
            cond = cond.and(t2.field("id").in(inDetails));
        }
        //入职日期
        if (checkinTimeStart != null && checkinTimeEnd != null) {
            cond = cond.and(t2.field("check_in_time").gt(checkinTimeStart));
            cond = cond.and(t2.field("check_in_time").lt(checkinTimeEnd));
        }
        //离职日期
        if (dissmissTimeStart != null && dissmissTimeEnd != null) {
            cond = cond.and(t2.field("dismiss_time").gt(dissmissTimeStart));
            cond = cond.and(t2.field("dismiss_time").lt(dissmissTimeEnd));
        }
        if (null != locator && null != locator.getAnchor())
            cond = cond.and(t1.field("detail_id").lt(locator.getAnchor()));

        List<Long> result = step.where(cond).groupBy(t2.field("id")).orderBy(t2.field("id").desc()).limit(pageSize).fetch(t2.field("id"));
        LOGGER.debug("step " + step);
//		if (null != locator)
//			locator.setAnchor(null);

//		if (result != null & result.size() >= pageSize) {
//			result.remove(result.size() - 1);
//			locator.setAnchor(result.get(result.size() - 1));
//		}

        return result;

    }

    @Override
    public boolean checkIfLastOnNode(Integer namespaceId, Long organizationId, String contactToken, String path) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(contactToken));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.notEqual(OrganizationGroupType.ENTERPRISE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path + "%"));
        List<EhOrganizationMembersRecord> records = query.fetch();
        if (records != null) {
            if (records.size() > 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer countUserOrganization(Integer namespaceId, Long communityId, Byte userOrganizationStatus) {
        return countUserOrganization(namespaceId, communityId, userOrganizationStatus, null, null);
    }

    @Override
    public Integer countUserOrganization(Integer namespaceId, Long communityId, Byte userOrganizationStatus, String namespaceUserType, Byte gender) {
        List<Long> result = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<Record> query = context.selectQuery();
                    query.addSelect(Tables.EH_USERS.ID);
                    query.addFrom(Tables.EH_USERS);
                    query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
                    query.addJoin(Tables.EH_USER_ORGANIZATIONS,
                            JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_ORGANIZATIONS.USER_ID));
                    query.addJoin(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID).and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode())));
                    query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
                    if (null == userOrganizationStatus) {
                        query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.notIn(UserOrganizationStatus.REJECT.getCode(), UserOrganizationStatus.INACTIVE.getCode()));
                    } else {
                        query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(userOrganizationStatus));
                    }

                    query.addConditions(Tables.EH_USERS.STATUS.eq(UserStatus.ACTIVE.getCode()));
                    if (null != communityId) {
                        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(communityId));
                    }
                    if (!StringUtils.isEmpty(namespaceUserType)) {
                        query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(namespaceUserType));
                    }

                    if (gender != null) {
                        query.addConditions(Tables.EH_USERS.GENDER.eq(gender));
                    }
                    query.addGroupBy(Tables.EH_USERS.ID);
                    LOGGER.debug("query sql:{}", query.getSQL());
                    LOGGER.debug("query param:{}", query.getBindValues());
                    query.fetch().map((r) -> {
                        result.add(Long.valueOf(r.getValue(0).toString()));
                        return null;
                    });
                    return true;
                });
        return result.size();
    }

    @Override
    public Set<Long> listMemberDetailIdWithExclude(Integer namespaceId, String big_path, List<String> small_path) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DEPARTMENT.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        //added by wh 2016-10-13 把被拒绝的过滤掉
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        Condition cond = Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(big_path + "%");
        if (small_path != null) {
            for (String p : small_path) {
                cond = cond.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.notLike(p + "%"));
            }
        }
        query.addConditions(cond);
        List<EhOrganizationMembersRecord> records = query.fetch();
        Set<Long> result = new HashSet<>();
        if (records != null) {
            records.stream().map(r -> {
                result.add(r.getDetailId());
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && result.size() != 0) {
            return result;
        }
        return null;
    }

    @Override
    public List<OrganizationCommunityRequest> listOrganizationCommunityRequests(List<Long> communityIds) {
        List<OrganizationCommunityRequest> results = new ArrayList<OrganizationCommunityRequest>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationCommunityRequestsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS);
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_STATUS.eq(OrganizationCommunityRequestStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.in(communityIds));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, OrganizationCommunityRequest.class));
            return null;
        });
        return results;
    }

    @Override
    public Map<Long, String> listOrganizationsOfDetail(Integer namespaceId, Long detailId, String organizationGroupType) {
        Map<Long, String> orgMap = new HashMap<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATIONS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("organization_id").eq(t2.field("id")));
        Condition condition = t1.field("namespace_id").eq(namespaceId);
        condition = condition.and(t2.field("namespace_id").eq(namespaceId));
        condition = condition.and(t1.field("detail_id").eq(detailId));
        condition = condition.and(t2.field("group_type").eq(OrganizationGroupType.fromCode(organizationGroupType).getCode()));
        condition = condition.and(t1.field("status").ne(OrganizationMemberStatus.INACTIVE.getCode()))
                .and(t1.field("status").ne(OrganizationMemberStatus.REJECT.getCode()));
        Result result = step.where(condition).fetch();
        if (result != null) {
            result.map(r -> {
                orgMap.put(Long.valueOf(r.getValue(t1.field("organization_id")).toString()), r.getValue(t2.field("name")).toString());
                return null;
            });
        }
        if (orgMap.size() > 0) {
            return orgMap;
        }
        return null;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByDetailId(Long detailId, List<String> groupTypes) {
        List<OrganizationMember> list = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId));
        query = query.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));

        if (groupTypes != null && groupTypes.size() > 0) {
            query = query.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
        }
        List<Record> records = query.fetch();

        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationMember.class));
        }

        return list;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByDetailIdAndOrgId(Long detailId, Long orgId, List<String> groupTypes) {
        List<OrganizationMember> list = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId).and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId)));
        query = query.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));

        if (groupTypes != null && groupTypes.size() > 0) {
            query = query.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
        }
        List<Record> records = query.fetch();

        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationMember.class));
        }

        return list;
    }

    @Override
    public List<OrganizationMember> listOrganizationMembersByDetailIdAndPath(Long detailId, String path, List<String> groupTypes) {
        List<OrganizationMember> list = new ArrayList<OrganizationMember>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId).and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path + "%")));
        query = query.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));

        if (groupTypes != null && groupTypes.size() > 0) {
            query = query.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
        }
        List<Record> records = query.fetch();

        if (records != null && !records.isEmpty()) {
            for (Record r : records)
                list.add(ConvertHelper.convert(r, OrganizationMember.class));
        }

        return list;
    }

    private Long getTopOrganizationId(Long organizationId) {
        Organization organization = findOrganizationById(organizationId);
        if (organization != null) {
            if (organization.getParentId() == null)
                return organizationId;
            String path = organization.getPath();
            String[] ogs = path.split("/");
            return Long.valueOf(ogs[1]);
        }
        return null;
    }


    @Override
    public List<Organization> listOrganizationsByGroupType(String groupType, Long organizationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> step = context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(groupType));
        if (null != organizationId)
            step.and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(organizationId));
        return step.orderBy(Tables.EH_ORGANIZATIONS.ID.desc()).fetch().map(r -> ConvertHelper.convert(r, Organization.class));
    }

    @Override
    public Integer countOrganizationMemberDetailsByOrgId(Integer namespaceId, Long organizationId) {
        Organization org = this.findOrganizationById(organizationId);
        if (org == null) {
            LOGGER.error("org is not matched, orgId={},", organizationId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "org is not matched");
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%"));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));

        return context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).groupBy(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID).fetchCount();
    }

    @Override
    public Integer countOrganizationMemberDetails(Long orgId, Long departmentId){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
    	Condition condition = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(orgId); 
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode())); 
        if (departmentId != null) {
            Organization department = findOrganizationById(departmentId);

			List<String> groupTypes = new ArrayList<>();
			groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
			groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            List<Organization> subDeparts = listOrganizationByGroupTypesAndPath(department.getPath() + "%", groupTypes, null, null, Integer.MAX_VALUE - 1);
            List<Long> subDptIds = new ArrayList<>();
            subDeparts.forEach(r -> {
            	subDptIds.add(r.getId());
            });
            List<Long> workGroups = listOrganizationPersonnelDetailIdsByDepartmentIds(subDptIds);
            List<Long> dismissGroups = archivesProvider.listDismissEmployeeDetailIdsByDepartmentIds(subDptIds);
            Condition con1 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(0L);
            Condition con2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(0L);
            if (workGroups != null)
                con1 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(workGroups);
            if (dismissGroups != null)
                con2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(dismissGroups);
            condition = condition.and(con1.or(con2));
        }
        return context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS).where(condition).fetchCount();
    }
    @Override
    public OrganizationMemberDetails findOrganizationMemberDetailsByTargetId(Long targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMemberDetails> results = context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .where(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID.eq(targetId))
                .fetchInto(OrganizationMemberDetails.class);
        if (null == results || results.size() == 0)
            return null;
        return results.get(0);
    }

    @Override
    public OrganizationMemberDetails findOrganizationMemberDetailsByTargetIdAndOrgId(Long targetId, Long orgId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMemberDetails> results = context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .where(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID.eq(targetId))
                .and(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(orgId))
                .fetchInto(OrganizationMemberDetails.class);
        if (null == results || results.size() == 0)
            return null;
        return results.get(0);
    }

    public OrganizationMemberDetails findOrganizationMemberDetailsByTargetId(Long targetId, Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMemberDetails> results = context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .where(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID.eq(targetId)
                        .and(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(organizationId)))

                .fetchInto(OrganizationMemberDetails.class);
        if (null == results || results.size() == 0)
            return null;
        return results.get(0);
    }

    @Override
    public List<Organization> listHeadEnterprises() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> step = context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));
        step.and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L));
        return step.fetch().map(r -> ConvertHelper.convert(r, Organization.class));
    }

    @Override
    public List<Organization> listOrganizationsByGroupType(String groupType, Long organizationId, List<Long> orgIds,
                                                           String groupName, Long creatorUid, CrossShardListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> step = context.select().from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(groupType));
        if (null != orgIds) {
            step.and(Tables.EH_ORGANIZATIONS.ID.in(orgIds));
        }
        if (!StringUtils.isEmpty(groupName)) {
            step.and(Tables.EH_ORGANIZATIONS.NAME.like("%" + groupName + "%"));
        }
        if (null != creatorUid) {
            step.and(Tables.EH_ORGANIZATIONS.CREATOR_UID.eq(creatorUid));

        }
        if (null != organizationId)
            step.and(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(organizationId));
        if (null != locator) {
            step.and(Tables.EH_ORGANIZATIONS.ID.gt(locator.getAnchor()));
        }

        step.orderBy(Tables.EH_ORGANIZATIONS.ID.asc());
        if (null != pageSize) {
            step.limit(pageSize);
        }
        return step.fetch().map(r -> ConvertHelper.convert(r, Organization.class));
    }

    public List listOrganizationMembersGroupByToken() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<EhOrganizationMembers> list = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID.notEqual(0))
                .groupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN).having("count(*) > 1").fetch().map(r -> {
            list.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        return list;
    }

    @Override
    public List listOrganizationMemberByToken(String token) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<EhOrganizationMembers> list = new ArrayList<>();
        context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.eq(token))
                .fetch().map(r -> {
            list.add(ConvertHelper.convert(r, EhOrganizationMembers.class));
            return null;
        });
        return list;
    }

    @Override
    public String getOrganizationNameById(Long targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_ORGANIZATIONS.NAME)
                .from(Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATIONS.ID.eq(targetId))
                .fetchOne(Tables.EH_ORGANIZATIONS.NAME);
    }

    @Override
    public List<TargetDTO> findOrganizationIdByNameAndAddressId(String targetName, List<Long> ids) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<TargetDTO> list = new ArrayList<>();
        com.everhomes.server.schema.tables.EhOrganizations r = Tables.EH_ORGANIZATIONS.as("r");
        com.everhomes.server.schema.tables.EhOrganizationAddresses t = Tables.EH_ORGANIZATION_ADDRESSES.as("t");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(r.ID);
        query.addSelect(r.NAME);
        query.addSelect(r.ID);
        query.addFrom(r, t);
        query.addConditions(r.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
        query.addConditions(r.ID.eq(t.ORGANIZATION_ID));
        query.addConditions(t.STATUS.eq((byte) 2));
        if (targetName != null) {
            query.addConditions(r.NAME.eq(targetName));
        }
        if (ids.size() == 1) {
            query.addConditions(t.ADDRESS_ID.eq(ids.get(0)));
        }
        if (ids.size() > 1) {
            query.addConditions(t.ADDRESS_ID.in(ids));
        }
        query.fetch()
                .map(f -> {
                    TargetDTO dto = new TargetDTO();
                    dto.setTargetId(f.getValue(r.ID));
                    dto.setTargetName(f.getValue(r.NAME));
                    dto.setTargetType("eh_organization");
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public List listUserOrganizationByUserId(Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<UserOrganizations> list = new ArrayList<>();
        context.select().from(Tables.EH_USER_ORGANIZATIONS)
                .where(Tables.EH_USER_ORGANIZATIONS.USER_ID.eq(userId).and(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.ACTIVE.getCode())))
                .fetch().map(r -> {
            list.add(ConvertHelper.convert(r, UserOrganizations.class));
            return null;
        });
        return list;
    }

    @Override
    public UserOrganizations findActiveAndWaitUserOrganizationByUserIdAndOrgId(Long userId, Long orgId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_USER_ORGANIZATIONS)
                .where(Tables.EH_USER_ORGANIZATIONS.USER_ID.eq(userId)
                        .and(Tables.EH_USER_ORGANIZATIONS.STATUS.in(UserOrganizationStatus.ACTIVE.getCode(), UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode()))
                        .and(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(orgId)))
                .fetchAny();
        if (record != null)
            return ConvertHelper.convert(record, UserOrganizations.class);
        return null;
    }

    @Override
    public UserOrganizations findUserOrganizationById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_USER_ORGANIZATIONS)
                .where(Tables.EH_USER_ORGANIZATIONS.ID.eq(id))
                .fetchOne();
        if (record != null)
            return ConvertHelper.convert(record, UserOrganizations.class);
        return null;
    }

    @Override
    public List<Organization> findNamespaceUnifiedSocialCreditCode(String unifiedSocialCreditCode, Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizations.class));

        List<Organization> result = new ArrayList<Organization>();
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);

        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATIONS.UNIFIED_SOCIAL_CREDIT_CODE.eq(unifiedSocialCreditCode));

        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));

        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Organization.class));
            return null;
        });

        return result;
    }

    @Override
    public void deleteOrganizationPersonelByJobPositionIdsAndDetailIds(List<Long> jobPositionIds, List<Long> detailIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhOrganizationMembersRecord> delete = context.deleteQuery(Tables.EH_ORGANIZATION_MEMBERS);
        delete.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(jobPositionIds));
        delete.addConditions(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.in(detailIds));
        delete.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, null);
    }

    @Override
    public void deleteOrganizationMembersByGroupTypeWithDetailIds(Integer namespaceId, List<Long> detailIds, String groupType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhOrganizationMembersRecord> delete = context.deleteQuery(Tables.EH_ORGANIZATION_MEMBERS);
        delete.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(groupType));
        delete.addConditions(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.in(detailIds));
        delete.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOrganizationMembers.class, null);
    }


    @Override
    public List<Long> queryOrganizationPersonnelDetailIds(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID);
        query.addFrom(Tables.EH_ORGANIZATION_MEMBERS);
        query.addJoin(Tables.EH_ORGANIZATION_MEMBER_DETAILS, JoinType.LEFT_OUTER_JOIN, Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID));
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ID.gt(0L);

        Organization org = findOrganizationById(organizationId);
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%"));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
        // 不包括经理
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.notEqual(OrganizationGroupType.MANAGER.getCode()));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.isNotNull());
        query.addConditions(condition);
        query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
        return query.fetchInto(Long.class);
    }

    @Override
    public List<Long> queryOrganizationPersonnelTargetIds(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID);
        query.addFrom(Tables.EH_ORGANIZATION_MEMBERS);
        query.addJoin(Tables.EH_ORGANIZATION_MEMBER_DETAILS, JoinType.LEFT_OUTER_JOIN, Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID));
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ID.gt(0L);

        Organization org = findOrganizationById(organizationId);
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%"));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
        // 不包括经理
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.notEqual(OrganizationGroupType.MANAGER.getCode()));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.isNotNull());
        query.addConditions(condition);
        query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
        return query.fetchInto(Long.class);
    }

    @Override
    public List<OrganizationMember> listOrganizationPersonnelsWithDownStream(String keywords, Byte contactSignedupStatus, CrossShardListingLocator locator, Integer pageSize, ListOrganizationContactCommand listCommand, String filterScopeType, List<String> groupTypes) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        List<OrganizationMember> result = new ArrayList<>();
        /* modify by lei lv,增加了detail表，部分信息挪到detail表里去取 */
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select().from(t1).leftOuterJoin(t2).on(t1.field("detail_id").eq(t2.field("id")));
        Condition condition = t1.field("id").gt(0L);

        if (null != locator && null != locator.getAnchor())
            condition = condition.and(t1.field("detail_id").le(locator.getAnchor()));

        Organization org = findOrganizationById(listCommand.getOrganizationId());

        Condition cond;
        if (filterScopeType.equals(FilterOrganizationContactScopeType.CURRENT.getCode())) {
            // 当传入的是分公司节点时
            if (org.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode()) && org.getParentId() != 0) {
                // 获取公司下级的隐藏部门
                Organization under_org = findUnderOrganizationByParentOrgId(org.getId());
                if (under_org != null) {
                    cond = t1.field("organization_id").eq(under_org.getId());
                } else { //如果没有隐藏部门，直接返回空
                    return new ArrayList<>();
                }
            } else {
                cond = t1.field("organization_id").eq(org.getId());
            }
        } else {
            cond = t1.field("group_path").like(org.getPath() + "%");
        }

        cond = cond.and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));

        // 不包括经理
        cond = cond.and(t1.field("group_type").notEqual(OrganizationGroupType.MANAGER.getCode()));

        if (!StringUtils.isEmpty(keywords)) {
            Condition cond1 = t2.field("contact_token").eq(keywords);
            cond1 = cond1.or(t2.field("contact_name").like("%" + keywords + "%"));
            cond = cond.and(cond1);
        }

        if (contactSignedupStatus != null && contactSignedupStatus == ContactSignUpStatus.SIGNEDUP.getCode()) {
            cond = cond.and(t1.field("target_id").ne(0L));
            cond = cond.and(t1.field("target_type").eq(OrganizationMemberTargetType.USER.getCode()));
        }

        if (null != groupTypes && groupTypes.size() > 0) {
            cond = cond.and(t1.field("group_type").in(groupTypes));
        }

        if (listCommand != null) {
            // 员工状态
            if (listCommand.getEmployeeStatus() != null) {
                cond = cond.and(t2.field("employee_status").eq(listCommand.getEmployeeStatus()));
            }

            // 合同主体
            if (listCommand.getContractPartyId() != null) {
                cond = cond.and(t2.field("contract_party_id").eq(listCommand.getContractPartyId()));
            }

            //工作地点
            if (listCommand.getWorkPlaceId() != null) {
                cond = cond.and(t2.field("work_place").eq(listCommand.getWorkPlaceId()));
            }

            //入职日期
            if (listCommand.getCheckInTimeStart() != null && listCommand.getCheckInTimeEnd() != null) {
                cond = cond.and(t2.field("check_in_time").between(listCommand.getCheckInTimeStart(), listCommand.getCheckInTimeEnd()));
            }

            //转正日期
            if (listCommand.getEmploymentTimeStart() != null && listCommand.getEmploymentTimeEnd() != null) {
                cond = cond.and(t2.field("employment_time").between(listCommand.getEmploymentTimeStart(), listCommand.getEmploymentTimeEnd()));
            }

            //合同结束日期
            if (listCommand.getContractEndTimeStart() != null && listCommand.getContractEndTimeStart() != null) {
                cond = cond.and(t2.field("contract_end_time").between(listCommand.getContractEndTimeStart(), listCommand.getContractEndTimeEnd()));
            }

            if (listCommand.getExceptIds() != null) {
                cond = cond.and(t2.field("id").notIn(listCommand.getExceptIds()));
            }
        }

        condition = condition.and(cond);

        List<OrganizationMember> records = step.where(condition).groupBy(t1.field("contact_token")).orderBy(t1.field("detail_id").desc()).limit(pageSize).fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.forEach(r -> result.add(ConvertHelper.convert(r, OrganizationMember.class)));
        }
        if (null != locator)
            locator.setAnchor(null);

        if (result.size() >= pageSize) {
            locator.setAnchor(result.get(result.size() - 1).getDetailId());
            result.remove(result.size() - 1);
        }
        return result;
    }

    @Override
    public List<OrganizationMember> queryOrganizationPersonnelsWithDownStream(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback) {
        List<OrganizationMember> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBERS.as("t1");
        TableLike t2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t2");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(t1);
        query.addJoin(t2, JoinType.LEFT_OUTER_JOIN, t1.field("detail_id").eq(t2.field("id")));
        queryBuilderCallback.buildCondition(locator, query);

        Condition condition = t1.field("id").gt(0L);
        Organization org = findOrganizationById(organizationId);
        condition = condition.and(t1.field("group_path").like(org.getPath() + "%"));
        condition = condition.and(t1.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        // 不包括经理
        condition = condition.and(t1.field("group_type").notEqual(OrganizationGroupType.MANAGER.getCode()));
        condition = condition.and(t1.field("detail_id").isNotNull());
        query.addConditions(condition);
        query.addGroupBy(t1.field("contact_token"));
        LOGGER.debug("sql : " + query.toString());
        List<OrganizationMember> records = query.fetch().map(new OrganizationMemberRecordMapper());
        if (records != null) {
            records.forEach(r -> {
                results.add(ConvertHelper.convert(r, OrganizationMember.class));
            });
        }
        return results;
    }

    @Override
    public Integer queryOrganizationPersonnelCounts(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_ORGANIZATION_MEMBERS);
        query.addJoin(Tables.EH_ORGANIZATION_MEMBER_DETAILS, JoinType.LEFT_OUTER_JOIN, Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID));
        query.addSelect(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.ID.gt(0L);

        Organization org = findOrganizationById(organizationId);
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(org.getPath() + "%"));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
        // 不包括经理
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.notEqual(OrganizationGroupType.MANAGER.getCode()));
        condition = condition.and(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.isNotNull());
        query.addConditions(condition);
        query.addGroupBy(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN);
        return query.fetchCount();
    }


    @Override
    public List<OrganizationMember> listOrganizationMemberByPath(String path, List<String> groupTypes, List<String> tokens) {
        List<OrganizationMember> result = new ArrayList<OrganizationMember>();

        //path一定不能为空 add by sfyan 20170428
        if (StringUtils.isEmpty(path)) {
            return result;
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path + "%"));
        if (null != groupTypes) {
            if (groupTypes.size() > 0) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.in(groupTypes));
            }
        }

        if (null != tokens && tokens.size() > 0) {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN.in(tokens));
        }

        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()));
        query.addOrderBy(Tables.EH_ORGANIZATION_MEMBERS.ID.desc());
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationMember.class));
            return null;
        });

        return result;
    }

    @Override
    public void updateOrganizationMemberByDetailId(Long detailId, String contactToken, String contactName, Byte gender) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhOrganizationMembersRecord> query = context.updateQuery(Tables.EH_ORGANIZATION_MEMBERS);

        if (contactToken != null)
            query.addValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN, contactToken);
        if (contactName != null)
            query.addValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME, contactName);
        if (gender != null)
            query.addValue(Tables.EH_ORGANIZATION_MEMBERS.GENDER, gender);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId));
        query.execute();

    }


    @Override
    public List listLapseOrganizations(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.in(OrganizationStatus.INACTIVE.getCode(), OrganizationStatus.DELETED.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        List list = query.fetch();
        return list;
    }

    @Override
    public Integer updateOrganizationMembersToInactiveByPath(String path, Timestamp now) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationMembersDao dao = new EhOrganizationMembersDao(context.configuration());
        int count = context.update(Tables.EH_ORGANIZATION_MEMBERS).set(Tables.EH_ORGANIZATION_MEMBERS.STATUS, OrganizationMemberStatus.INACTIVE.getCode())
                .set(Tables.EH_ORGANIZATION_MEMBERS.UPDATE_TIME, now)
                .where(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(path)).execute();
        return count;
    }

    @Override
    public OrganizationMember findDepartmentMemberByTargetIdAndOrgId(Long userId, Long organizationId) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId)
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like("/" + organizationId + "%"))
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DEPARTMENT.getCode()).or(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode())));

        Record r = context.select().from(Tables.EH_ORGANIZATION_MEMBERS).where(condition).fetchOne();

        if (r != null)
            return ConvertHelper.convert(r, OrganizationMember.class);
        return null;
    }

    @Override
    public void createCommunityOrganizationDetailDisplay(CommunityOrganizationDetailDisplay detailDisplay) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCommunityOrganizationDetailDisplay.class));

        detailDisplay.setId(id);
        detailDisplay.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        detailDisplay.setOperatorUid(UserContext.currentUserId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityOrganizationDetailDisplay.class));
        EhCommunityOrganizationDetailDisplayDao dao = new EhCommunityOrganizationDetailDisplayDao(context.configuration());
        dao.insert(detailDisplay);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCommunityOrganizationDetailDisplay.class, id);
    }

    @Override
    public CommunityOrganizationDetailDisplay findOrganizationDetailFlag(Integer namespaceId, Long communityId) {

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_COMMUNITY_ORGANIZATION_DETAIL_DISPLAY)
                .where(Tables.EH_COMMUNITY_ORGANIZATION_DETAIL_DISPLAY.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_COMMUNITY_ORGANIZATION_DETAIL_DISPLAY.COMMUNITY_ID.eq(communityId))
                .fetchAnyInto(CommunityOrganizationDetailDisplay.class);
    }

    @Override
    public void updateCommunityOrganizationDetailDisplay(CommunityOrganizationDetailDisplay detailDisplay) {
        assert (detailDisplay.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityOrganizationDetailDisplay.class));
        EhCommunityOrganizationDetailDisplayDao dao = new EhCommunityOrganizationDetailDisplayDao(context.configuration());
        detailDisplay.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        detailDisplay.setOperatorUid(UserContext.currentUserId());
        dao.update(detailDisplay);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityOrganizationDetailDisplay.class, detailDisplay.getId());
    }

    @Override
    public List checkOrgExistInOrgOrPaths(Integer namespaceId, Long organizationId, List<Long> orgIds, List<String> orgPaths) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        // in
        Condition cond1 = null;
        if (orgIds != null && orgIds.size() > 0) {
            cond1 = Tables.EH_ORGANIZATIONS.ID.in(orgIds);
        }
        // like
        Condition cond2 = null;
        if (orgPaths != null && orgPaths.size() > 0) {
            for (String orgPath : orgPaths) {
                if (cond2 == null) {
                    cond2 = Tables.EH_ORGANIZATIONS.PATH.like(orgPath + "%");
                } else {
                    cond2 = cond2.or(Tables.EH_ORGANIZATIONS.PATH.like(orgPath + "%"));
                }
            }
        }
        if (cond1 != null && cond2 != null) {
            query.addConditions(cond1.or(cond2));
        } else if (cond1 != null) {
            query.addConditions(cond1);
        } else if (cond2 != null) {
            query.addConditions(cond2);
        }
        query.addConditions(Tables.EH_ORGANIZATIONS.ID.eq(organizationId));
        return query.fetch();
    }

    @Override
    public List<OrganizationMemberDetails> listOrganizationMemberDetails(Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMemberDetails> results = context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .where(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(ownerId))
                .fetchInto(OrganizationMemberDetails.class);
        if (null == results || results.size() == 0)
            return null;
        return results;
    }

    @Override
    public Integer queryOrganizationMemberDetailCounts(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_DETAILS);
        queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(organizationId));
        return query.fetchCount();
    }

    @Override
    public List<OrganizationMemberDetails> queryOrganizationMemberDetails(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_DETAILS);
        queryBuilderCallback.buildCondition(locator, query);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(organizationId));
        List<OrganizationMemberDetails> results = query.fetchInto(OrganizationMemberDetails.class);
        if (null == results || results.size() == 0)
            return null;
        return results;
    }

    @Override
    public List<Long> listOrganizationPersonnelDetailIdsByDepartmentIds(List<Long> subDptIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addSelect(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.in(subDptIds));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.isNotNull());
        List<Long> results = query.fetchInto(Long.class);
        if (null == results || results.size() == 0)
            return null;
        return results;
    }

    @Override
    public List<Organization> listPMOrganizations(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATIONS.ORGANIZATION_TYPE.eq(OrganizationType.PM.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.PARENT_ID.eq(0L));
        return query.fetch().map(record -> ConvertHelper.convert(record, Organization.class));
    }

    @Override
    public Organization findOrganizationByName(String groupType, String name, Long directlyEnterpriseId, Long groupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> step = context.select().from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.NAME.eq(name))
                .and(Tables.EH_ORGANIZATIONS.DIRECTLY_ENTERPRISE_ID.eq(directlyEnterpriseId))
                .and(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(groupType))
                .and(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
        if (null != groupId) {
            step = step.and(Tables.EH_ORGANIZATIONS.ID.ne(groupId));
        }

        Record r = step.fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, Organization.class);
        return null;
    }

    @Override
    public List<NoticeMemberIdAndContact> findActiveUidsByTargetTypeAndOrgId(Long orgId, String... targetTypes) {
        List<NoticeMemberIdAndContact> ret = new ArrayList<>();
        this.dbProvider.getDslContext(AccessSpec.readOnly())
                .select(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID, Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN)
                .from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.in(targetTypes))
                .and(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID.eq(orgId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))
                .fetch().forEach(r -> {
            NoticeMemberIdAndContact c = new NoticeMemberIdAndContact();
            c.setContactToken(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN));
            c.setTargetId(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID));
            ret.add(c);
        });
        return ret;
    }

    @Override
    public Integer countUserOrganization(Integer namespaceId, Long communityId) {
        return countUserOrganization(namespaceId, communityId, null, null, null);
    }


    @Override
    public void deleteAllOrganizationAddressById(Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ORGANIZATION_ADDRESSES)
                .where(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID.eq(organizationId))
                .execute();
    }

    @Override
    public Integer getUserOrgAmount(Long targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhOrganizationMembers.class));
        SelectConditionStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(targetId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_TYPE.eq("USER"));
        return step.fetchOneInto(Integer.class);
    }
    
	/**
	 * 通过项目ID 与 认证状态来查询项目下的用户(查询过程已利用分组去重UID)
	 * @param namespaceId
	 * @param communityIds
	 * @param authStatus
	 * @param locator
	 * @param pageSize
	 */
    @Override
	public List<UserOrganizations>  findUserByCommunityIDAndAuthStatus(Integer namespaceId , List<Long> communityIds , List<Integer> authStatus ,CrossShardListingLocator locator , int pageSize){
		
		List<UserOrganizations> users = listUserOrganizations(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_USERS.STATUS.eq(UserStatus.ACTIVE.getCode()));
				
				if(null != communityIds && communityIds.size() > 0){
					query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.in(communityIds));
				}

				if(null != authStatus && authStatus.size() > 0){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.in(authStatus));
				}
				
				query.addGroupBy(Tables.EH_USERS.ID);

				Condition cond = Tables.EH_USERS.ID.isNotNull();
				//
				 if(authStatus.size() == 1 && authStatus.contains(AuthFlag.PENDING_AUTHENTICATION.getCode())){
					cond = cond.and(" `eh_users`.`id` not in (select user_id from eh_user_organizations where status = " + UserOrganizationStatus.ACTIVE.getCode() + ")");
				}
				query.addHaving(cond);

				return query;
			}
		});
		return users;
	}

    @Override
    public OrganizationMember findMemberByType(Long userId, String groupPath, String type) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        OrganizationMember organizationMember = context.select().from(Tables.EH_ORGANIZATION_MEMBERS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.GROUP_PATH.like(groupPath+"%"))
                .and(Tables.EH_ORGANIZATION_MEMBERS.TARGET_ID.eq(userId))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq(type))
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.INACTIVE.getCode()))
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.ne(OrganizationMemberStatus.REJECT.getCode()))
                .fetchAnyInto(OrganizationMember.class);
        return organizationMember;
    }

    @Override
    public List<OrganizationMemberDetails> listOrganizationMemberDetails(Long ownerId, String userName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<OrganizationMemberDetails> results = context.select().from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .where(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ORGANIZATION_ID.eq(ownerId))
                .and(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME.like("%" + userName + "%"))
                .fetchInto(OrganizationMemberDetails.class);
        if (null == results || results.size() == 0)
            return null;
        return results;
    }
    
	
	/**
	 * 根据用户id获取用户的真实姓名和手机号 1.获取真实姓名。 2.没有的话获取昵称
	 * 
	 * @param userId
	 * @return
	 */
    @Override
	public TargetDTO findUserContactByUserId(Integer namespaceId, Long userId) {

		TargetDTO dto = null;
		OrganizationMember member = findAnyOrganizationMemberByNamespaceIdAndUserId(namespaceId,
				userId, OrganizationType.ENTERPRISE.getCode());
		if (null != member) {
			dto = new TargetDTO();
			dto.setTargetId(userId);
			dto.setTargetName(member.getContactName());
			dto.setUserIdentifier(member.getContactToken());
			return dto;
		}

		return userProvider.findUserTargetById(userId);
	}
}
