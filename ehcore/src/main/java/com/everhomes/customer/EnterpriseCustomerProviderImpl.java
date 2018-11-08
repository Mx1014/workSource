package com.everhomes.customer;

import com.everhomes.acl.AuthorizationRelation;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseAttachment;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.CustomerAnnualStatisticDTO;
import com.everhomes.rest.customer.CustomerErrorCode;
import com.everhomes.rest.customer.CustomerProjectStatisticsDTO;
import com.everhomes.rest.customer.CustomerTrackingTemplateCode;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EasySearchEnterpriseCustomersDTO;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.ListCustomerTrackingPlansByDateCommand;
import com.everhomes.rest.customer.ListNearbyEnterpriseCustomersCommand;
import com.everhomes.rest.customer.TrackingPlanNotifyStatus;
import com.everhomes.rest.customer.TrackingPlanReadStatus;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.openapi.techpark.AllFlag;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.pmNotify.PmNotifyConfigurationStatus;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCustomerAccountsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerApplyProjectsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerCertificatesDao;
import com.everhomes.server.schema.tables.daos.EhCustomerCommercialsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerConfigutationsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerDepartureInfosDao;
import com.everhomes.server.schema.tables.daos.EhCustomerEconomicIndicatorStatisticsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerEconomicIndicatorsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerEntryInfosDao;
import com.everhomes.server.schema.tables.daos.EhCustomerEventsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerInvestmentsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerPatentsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerPotentialDatasDao;
import com.everhomes.server.schema.tables.daos.EhCustomerTalentsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerTaxesDao;
import com.everhomes.server.schema.tables.daos.EhCustomerTrackingPlansDao;
import com.everhomes.server.schema.tables.daos.EhCustomerTrackingsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerTrademarksDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCustomerAdminsDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCustomerAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCustomersDao;
import com.everhomes.server.schema.tables.daos.EhTrackingNotifyLogsDao;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhAuthorizationRelationsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerAccountsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerApplyProjectsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerCertificatesRecord;
import com.everhomes.server.schema.tables.records.EhCustomerCommercialsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerDepartureInfosRecord;
import com.everhomes.server.schema.tables.records.EhCustomerEconomicIndicatorStatisticsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerEconomicIndicatorsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerEntryInfosRecord;
import com.everhomes.server.schema.tables.records.EhCustomerEventsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerInvestmentsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerPatentsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerPotentialDatasRecord;
import com.everhomes.server.schema.tables.records.EhCustomerTalentsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerTaxesRecord;
import com.everhomes.server.schema.tables.records.EhCustomerTrackingPlansRecord;
import com.everhomes.server.schema.tables.records.EhCustomerTrackingsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerTrademarksRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseCustomerAdminsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseCustomersRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.FieldParams;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectOffsetStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/11.
 */
@Component
public class EnterpriseCustomerProviderImpl implements EnterpriseCustomerProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseCustomerProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
	private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private FieldService fieldService;
    
    @Autowired
    private FieldProvider fieldProvider;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Long createEnterpriseCustomer(EnterpriseCustomer customer) {
        LOGGER.info("create customer: {}", StringHelper.toJsonString(customer));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCustomers.class));
        customer.setId(id);
        customer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        customer.setStatus(CommonStatus.ACTIVE.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, id));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        dao.insert(customer);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseCustomers.class, null);
        return id;
    }

    @Override
    public void createEnterpriseCustomers(Collection<EhEnterpriseCustomers> customers) {
        LOGGER.info("create customers: {}", StringHelper.toJsonString(customers));
        customers.forEach(customer ->{
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCustomers.class));
            customer.setId(id);
            customer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            customer.setStatus(CommonStatus.ACTIVE.getCode());
        });


        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, id));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        dao.insert(customers);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseCustomers.class, null);
    }


	@Override
    public Long updateEnterpriseCustomer(EnterpriseCustomer customer) {
        LOGGER.debug("updateEnterpriseCustomer customer: {}",
                StringHelper.toJsonString(customer));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(customer);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
        return customer.getId();
    }

    @Override
    public void updateEnterpriseCustomers(List<EhEnterpriseCustomers> customers) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
/*        customers.forEach(customer -> {
            LOGGER.debug("updateEnterpriseCustomer customer: {}",
                    StringHelper.toJsonString(customer));

            customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        });*/

        dao.update(customers);
        //DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, null);
    }

    @Override
    public void deleteEnterpriseCustomer(EnterpriseCustomer customer) {
        assert(customer.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        dao.delete(customer);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
    }

    @Override
    public EnterpriseCustomer findByNamespaceToken(String namespaceType, String namespaceCustomerToken) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_CUSTOMER_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_CUSTOMER_TOKEN.eq(namespaceCustomerToken));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        if(result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_CUSTOMER_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndName(Integer namespaceId, Long communityId, String name) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        if(null != communityId){
            query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        }
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAME.eq(name));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceType(Integer namespaceId, String namespaceType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_CUSTOMER_TYPE.eq(namespaceType));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }


    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndName(Integer namespaceId, String name) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAME.eq(name));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceIdAndNumber(Integer namespaceId, Long communityId, String number) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CUSTOMER_NUMBER.eq(number));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }


    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByNamespaceId(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public EnterpriseCustomer findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EnterpriseCustomer.class);
    }

    @Override
    public EnterpriseCustomer findByOrganizationId(Long organizationId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        if(result.size() == 0) {
            return null;
        }
        return result.get(0);
    }


    @Override
    public EnterpriseCustomer findByOrganizationIdAndCommunityId(Long organizationId, Long communityId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        if(result.size() == 0) {
            return null;
        }
        return result.get(0);
    }


    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomers(CrossShardListingLocator locator, Integer pageSize) {
        List<EnterpriseCustomer> customers = new ArrayList<>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhEnterpriseCustomers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ID.lt(locator.getAnchor()));
            }

            query.addOrderBy(Tables.EH_ENTERPRISE_CUSTOMERS.ID.desc());
            query.addLimit(pageSize - customers.size());

            query.fetch().map((r) -> {
                customers.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
                return null;
            });

            if (customers.size() >= pageSize) {
                locator.setAnchor(customers.get(customers.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return customers;
    }

    @Override
    public Map<Long, EnterpriseCustomer> listEnterpriseCustomersByIds(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ID.in(ids));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        Map<Long, EnterpriseCustomer> result = new HashMap<>();
        query.fetch().map((r) -> {
            result.put(r.getId(),ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomers(Set<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ID.in(customerIds));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public Map<Long, Long> listEnterpriseCustomerSourceByCommunityId(Long communityId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listEnterpriseCustomerSourceByCommunityId, sql=" + query.getSQL());
            LOGGER.debug("listEnterpriseCustomerSourceByCommunityId, bindValues=" + query.getBindValues());
        }

        Map<Long, Long> result = new HashMap<>();
        query.fetch().map((r) -> {
            if(result.get(r.getSourceItemId()) == null) {
                result.put(r.getSourceItemId(), 1L);
            } else {
                result.put(r.getSourceItemId(), result.get(r.getSourceItemId()) + 1);
            }
            return null;
        });

        return result;
    }

    @Override
    public Map<Long, Long> listEnterpriseCustomerIndustryByCommunityId(Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listEnterpriseCustomerIndustryByCommunityId, sql=" + query.getSQL());
            LOGGER.debug("listEnterpriseCustomerIndustryByCommunityId, bindValues=" + query.getBindValues());
        }

        Map<Long, Long> result = new HashMap<>();
        query.fetch().map((r) -> {
            if(result.get(r.getCorpIndustryItemId()) == null) {
                result.put(r.getCorpIndustryItemId(), 1L);
            } else {
                result.put(r.getCorpIndustryItemId(), result.get(r.getCorpIndustryItemId()) + 1);
            }
            return null;
        });

        return result;
    }

    @Override
    public List<EnterpriseCustomer> listEnterpriseCustomerByCommunity(Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });

        return result;
    }

    @Override
    public void createCustomerAccount(CustomerAccount account) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerAccounts.class));
        account.setId(id);
        account.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        account.setCreateUid(UserContext.current().getUser().getId());
        account.setStatus(CommonStatus.ACTIVE.getCode());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerAccounts.class, id));
        EhCustomerAccountsDao dao = new EhCustomerAccountsDao(context.configuration());
        dao.insert(account);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerAccounts.class, null);
    }

    @Override
    public void createCustomerTax(CustomerTax tax) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerTaxes.class));
        tax.setId(id);
        tax.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        tax.setCreateUid(UserContext.current().getUser().getId());
        tax.setStatus(CommonStatus.ACTIVE.getCode());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTaxes.class, id));
        EhCustomerTaxesDao dao = new EhCustomerTaxesDao(context.configuration());
        dao.insert(tax);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerTaxes.class, null);
    }

    @Override
    public void deleteCustomerAccount(CustomerAccount account) {
        assert(account.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerAccounts.class, account.getId()));
        EhCustomerAccountsDao dao = new EhCustomerAccountsDao(context.configuration());
        dao.delete(account);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerAccounts.class, account.getId());
    }

    @Override
    public void deleteCustomerTax(CustomerTax tax) {
        assert(tax.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTaxes.class, tax.getId()));
        EhCustomerTaxesDao dao = new EhCustomerTaxesDao(context.configuration());
        dao.delete(tax);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTaxes.class, tax.getId());
    }

    @Override
    public CustomerAccount findCustomerAccountById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerAccountsDao dao = new EhCustomerAccountsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerAccount.class);
    }

    @Override
    public CustomerTax findCustomerTaxById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerTaxesDao dao = new EhCustomerTaxesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerTax.class);
    }

    @Override
    public List<CustomerAccount> listCustomerAccountsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerAccountsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ACCOUNTS);
        query.addConditions(Tables.EH_CUSTOMER_ACCOUNTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_ACCOUNTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerAccount> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerAccount.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerTax> listCustomerTaxesByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTaxesRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TAXES);
        query.addConditions(Tables.EH_CUSTOMER_TAXES.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TAXES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerTax> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTax.class));
            return null;
        });

        return result;
    }

    @Override
    public void updateCustomerAccount(CustomerAccount account) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerAccounts.class, account.getId()));
        EhCustomerAccountsDao dao = new EhCustomerAccountsDao(context.configuration());

        account.setOperatorUid(UserContext.current().getUser().getId());
        account.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(account);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerAccounts.class, account.getId());
    }

    @Override
    public void updateCustomerTax(CustomerTax tax) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTaxes.class, tax.getId()));
        EhCustomerTaxesDao dao = new EhCustomerTaxesDao(context.configuration());

        tax.setOperatorUid(UserContext.current().getUser().getId());
        tax.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(tax);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTaxes.class, tax.getId());
    }

    @Override
    public void createCustomerTalent(CustomerTalent talent) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerTalents.class));
        talent.setId(id);
        talent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        talent.setCreatorUid(UserContext.currentUserId());
        talent.setStatus(CommonStatus.ACTIVE.getCode());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerTalentsDao dao = new EhCustomerTalentsDao(context.configuration());
        dao.insert(talent);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerTalents.class, null);
    }

    @Override
    public void deleteCustomerTalent(CustomerTalent talent) {
        assert(talent.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTalents.class, talent.getId()));
        EhCustomerTalentsDao dao = new EhCustomerTalentsDao(context.configuration());
        dao.delete(talent);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTalents.class, talent.getId());
    }

    @Override
    public CustomerTalent findCustomerTalentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerTalentsDao dao = new EhCustomerTalentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerTalent.class);
    }

    @Override
    public CustomerTalent findCustomerTalentByPhone(String phone, Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTalentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TALENTS);
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.PHONE.eq(phone));
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerTalent> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTalent.class));
            return null;
        });

        if(result != null && result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    @Override
    public void updateCustomerTalent(CustomerTalent talent) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTalents.class, talent.getId()));
        EhCustomerTalentsDao dao = new EhCustomerTalentsDao(context.configuration());

        talent.setOperatorUid(UserContext.currentUserId());
        talent.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(talent);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTalents.class, talent.getId());
    }

    @Override
    public List<CustomerTalent> listCustomerTalentsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTalentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TALENTS);
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerTalent> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTalent.class));
            return null;
        });

        return result;
    }

    @Override
    public Map<Long, Long> listCustomerTalentCountByCustomerIds(List<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTalentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TALENTS);
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.CUSTOMER_ID.in(customerIds));
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listGroupCustomerTalentsByCustomerIds, sql=" + query.getSQL());
            LOGGER.debug("listGroupCustomerTalentsByCustomerIds, bindValues=" + query.getBindValues());
        }

        Map<Long, Long> result = new HashMap<>();
        query.fetch().map((r) -> {
            if(result.get(r.getIndividualEvaluationItemId()) == null) {
                result.put(r.getIndividualEvaluationItemId(), 1L);
            } else {
                result.put(r.getIndividualEvaluationItemId(), result.get(r.getIndividualEvaluationItemId()) + 1);
            }
            return null;
        });

        return result;
    }

    @Override
    public void createCustomerApplyProject(CustomerApplyProject project) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerApplyProjects.class));
        project.setId(id);
        project.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        project.setCreateUid(UserContext.current().getUser().getId());
//        project.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerApplyProject: " + project);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerApplyProjects.class, id));
        EhCustomerApplyProjectsDao dao = new EhCustomerApplyProjectsDao(context.configuration());
        dao.insert(project);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerApplyProjects.class, null);
    }

    @Override
    public void createCustomerCommercial(CustomerCommercial commercial) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerCommercials.class));
        commercial.setId(id);
        commercial.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        commercial.setCreateUid(UserContext.current().getUser().getId());
        commercial.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerCommercial: " + commercial);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCommercials.class, id));
        EhCustomerCommercialsDao dao = new EhCustomerCommercialsDao(context.configuration());
        dao.insert(commercial);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerCommercials.class, null);
    }

    @Override
    public void createCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEconomicIndicators.class));
        economicIndicator.setId(id);
        economicIndicator.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        economicIndicator.setCreateUid(UserContext.current().getUser().getId());
        economicIndicator.setStatus(CommonStatus.ACTIVE.getCode());


        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicators.class));
        EhCustomerEconomicIndicatorsDao dao = new EhCustomerEconomicIndicatorsDao(context.configuration());
        dao.insert(economicIndicator);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEconomicIndicators.class, id);
    }

    @Override
    public void createCustomerEconomicIndicatorStatistic(CustomerEconomicIndicatorStatistic statistic) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEconomicIndicatorStatistics.class));
        statistic.setId(id);
        statistic.setStatus(CommonStatus.ACTIVE.getCode());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicatorStatistics.class));
        EhCustomerEconomicIndicatorStatisticsDao dao = new EhCustomerEconomicIndicatorStatisticsDao(context.configuration());
        dao.insert(statistic);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEconomicIndicatorStatistics.class, id);
    }

    @Override
    public void updateCustomerEconomicIndicatorStatistic(CustomerEconomicIndicatorStatistic statistic) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicatorStatistics.class));
        EhCustomerEconomicIndicatorStatisticsDao dao = new EhCustomerEconomicIndicatorStatisticsDao(context.configuration());

        dao.update(statistic);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerEconomicIndicatorStatistics.class, statistic.getId());
    }

    @Override
    public void deleteCustomerEconomicIndicatorStatistic(CustomerEconomicIndicatorStatistic statistic) {
        assert(statistic.getId() != null);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicatorStatistics.class, statistic.getId()));
        EhCustomerEconomicIndicatorStatisticsDao dao = new EhCustomerEconomicIndicatorStatisticsDao(context.configuration());
        dao.delete(statistic);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerEconomicIndicatorStatistics.class, statistic.getId());

    }

    @Override
    public CustomerEconomicIndicatorStatistic listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(Long customerId, Timestamp time) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEconomicIndicatorStatisticsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS);
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.START_TIME.le(time));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.END_TIME.ge(time));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEconomicIndicatorStatistic> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEconomicIndicatorStatistic.class));
            return null;
        });
        if(result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public void createCustomerDepartureInfo(CustomerDepartureInfo departureInfo) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerDepartureInfos.class));
        departureInfo.setId(id);
        departureInfo.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        departureInfo.setCreateUid(UserContext.current().getUser().getId());
        departureInfo.setStatus(CommonStatus.ACTIVE.getCode());


        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerDepartureInfos.class));
        EhCustomerDepartureInfosDao dao = new EhCustomerDepartureInfosDao(context.configuration());
        dao.insert(departureInfo);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerDepartureInfos.class, id);
    }

    @Override
    public void createCustomerEntryInfo(CustomerEntryInfo entryInfo) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEntryInfos.class));
        entryInfo.setId(id);
        entryInfo.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        entryInfo.setCreateUid(UserContext.current().getUser().getId());
        entryInfo.setStatus(CommonStatus.ACTIVE.getCode());


        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEntryInfos.class));
        EhCustomerEntryInfosDao dao = new EhCustomerEntryInfosDao(context.configuration());
        dao.insert(entryInfo);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEntryInfos.class, id);
    }

    @Override
    public void deleteCustomerDepartureInfo(CustomerDepartureInfo departureInfo) {
        assert(departureInfo.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerDepartureInfos.class, departureInfo.getId()));
        EhCustomerDepartureInfosDao dao = new EhCustomerDepartureInfosDao(context.configuration());
        dao.delete(departureInfo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerDepartureInfos.class, departureInfo.getId());
    }

    @Override
    public void deleteCustomerEntryInfo(CustomerEntryInfo entryInfo) {
        assert(entryInfo.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEntryInfos.class, entryInfo.getId()));
        EhCustomerEntryInfosDao dao = new EhCustomerEntryInfosDao(context.configuration());
        dao.delete(entryInfo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerEntryInfos.class, entryInfo.getId());
    }

    @Override
    public CustomerDepartureInfo findCustomerDepartureInfoById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerDepartureInfosDao dao = new EhCustomerDepartureInfosDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerDepartureInfo.class);
    }

    @Override
    public CustomerEntryInfo findCustomerEntryInfoById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerEntryInfosDao dao = new EhCustomerEntryInfosDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerEntryInfo.class);
    }

    @Override
    public CustomerEntryInfo findCustomerEntryInfoByAddressId(Long customerId, Byte customerType, Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEntryInfosRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ENTRY_INFOS);
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.eq(addressId));
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_TYPE.eq(customerType));
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEntryInfo> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEntryInfo.class));
            return null;
        });

        if(result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    @Override
    public List<CustomerDepartureInfo> listCustomerDepartureInfos(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerDepartureInfosRecord> query = context.selectQuery(Tables.EH_CUSTOMER_DEPARTURE_INFOS);
        query.addConditions(Tables.EH_CUSTOMER_DEPARTURE_INFOS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_DEPARTURE_INFOS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerDepartureInfo> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerDepartureInfo.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerEntryInfo> listCustomerEntryInfos(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEntryInfosRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ENTRY_INFOS);
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEntryInfo> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEntryInfo.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerEntryInfo> listAddressEntryInfos(Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEntryInfosRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ENTRY_INFOS);
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.eq(addressId));
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEntryInfo> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEntryInfo.class));
            return null;
        });

        return result;
    }

    @Override
    public void updateCustomerDepartureInfo(CustomerDepartureInfo departureInfo) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerDepartureInfos.class));
        EhCustomerDepartureInfosDao dao = new EhCustomerDepartureInfosDao(context.configuration());

        dao.update(departureInfo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerDepartureInfos.class, departureInfo.getId());
    }

    @Override
    public void updateCustomerEntryInfo(CustomerEntryInfo entryInfo) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEntryInfos.class));
        EhCustomerEntryInfosDao dao = new EhCustomerEntryInfosDao(context.configuration());

        dao.update(entryInfo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerEntryInfos.class, entryInfo.getId());
    }

    @Override
    public void createCustomerInvestment(CustomerInvestment investment) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerInvestments.class));
        investment.setId(id);
        investment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        investment.setCreateUid(UserContext.current().getUser().getId());
        investment.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerInvestment: " + investment);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerInvestments.class, id));
        EhCustomerInvestmentsDao dao = new EhCustomerInvestmentsDao(context.configuration());
        dao.insert(investment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerInvestments.class, null);
    }

    @Override
    public void createCustomerPatent(CustomerPatent patent) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerPatents.class));
        patent.setId(id);
        patent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        patent.setCreateUid(UserContext.current().getUser().getId());
        patent.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerPatent: " + patent);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerPatents.class, id));
        EhCustomerPatentsDao dao = new EhCustomerPatentsDao(context.configuration());
        dao.insert(patent);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerPatents.class, null);
    }

    @Override
    public void createCustomerTrademark(CustomerTrademark trademark) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerTrademarks.class));
        trademark.setId(id);
        trademark.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        trademark.setCreateUid(UserContext.current().getUser().getId());
        trademark.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerTrademark: " + trademark);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrademarks.class, id));
        EhCustomerTrademarksDao dao = new EhCustomerTrademarksDao(context.configuration());
        dao.insert(trademark);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerTrademarks.class, null);
    }

    @Override
    public void deleteCustomerApplyProject(CustomerApplyProject project) {
        assert(project.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerApplyProjects.class, project.getId()));
        EhCustomerApplyProjectsDao dao = new EhCustomerApplyProjectsDao(context.configuration());
        dao.delete(project);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerApplyProjects.class, project.getId());

    }

    @Override
    public void deleteCustomerCommercial(CustomerCommercial commercial) {
        assert(commercial.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCommercials.class, commercial.getId()));
        EhCustomerCommercialsDao dao = new EhCustomerCommercialsDao(context.configuration());
        dao.delete(commercial);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerCommercials.class, commercial.getId());

    }

    @Override
    public void deleteCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator) {
        assert(economicIndicator.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicators.class, economicIndicator.getId()));
        EhCustomerEconomicIndicatorsDao dao = new EhCustomerEconomicIndicatorsDao(context.configuration());
        dao.delete(economicIndicator);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerEconomicIndicators.class, economicIndicator.getId());

    }

    @Override
    public void deleteCustomerInvestment(CustomerInvestment investment) {
        assert(investment.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerInvestments.class, investment.getId()));
        EhCustomerInvestmentsDao dao = new EhCustomerInvestmentsDao(context.configuration());
        dao.delete(investment);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerInvestments.class, investment.getId());

    }

    @Override
    public void deleteCustomerPatent(CustomerPatent patent) {
        assert(patent.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerPatents.class, patent.getId()));
        EhCustomerPatentsDao dao = new EhCustomerPatentsDao(context.configuration());
        dao.delete(patent);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerPatents.class, patent.getId());

    }

    @Override
    public void deleteCustomerTrademark(CustomerTrademark trademark) {
        assert(trademark.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrademarks.class, trademark.getId()));
        EhCustomerTrademarksDao dao = new EhCustomerTrademarksDao(context.configuration());
        dao.delete(trademark);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrademarks.class, trademark.getId());

    }

    @Override
    public CustomerApplyProject findCustomerApplyProjectById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerApplyProjectsDao dao = new EhCustomerApplyProjectsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerApplyProject.class);
    }

    @Override
    public CustomerCommercial findCustomerCommercialById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerCommercialsDao dao = new EhCustomerCommercialsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerCommercial.class);
    }

    @Override
    public CustomerEconomicIndicator findCustomerEconomicIndicatorById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerEconomicIndicatorsDao dao = new EhCustomerEconomicIndicatorsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerEconomicIndicator.class);
    }

    @Override
    public CustomerInvestment findCustomerInvestmentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerInvestmentsDao dao = new EhCustomerInvestmentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerInvestment.class);
    }

    @Override
    public CustomerPatent findCustomerPatentById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerPatentsDao dao = new EhCustomerPatentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerPatent.class);
    }

    @Override
    public CustomerTrademark findCustomerTrademarkById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerTrademarksDao dao = new EhCustomerTrademarksDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerTrademark.class);
    }

    @Override
    public List<CustomerApplyProject> listCustomerApplyProjectsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerApplyProjectsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_APPLY_PROJECTS);
        query.addConditions(Tables.EH_CUSTOMER_APPLY_PROJECTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_APPLY_PROJECTS.STATUS.ne(CommonStatus.INACTIVE.getCode()));

        List<CustomerApplyProject> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerApplyProject.class));
            return null;
        });

        return result;
    }

    @Override
    public Map<Long, CustomerProjectStatisticsDTO> listCustomerApplyProjectsByCustomerIds(List<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerApplyProjectsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_APPLY_PROJECTS);
        query.addConditions(Tables.EH_CUSTOMER_APPLY_PROJECTS.CUSTOMER_ID.in(customerIds));
        query.addConditions(Tables.EH_CUSTOMER_APPLY_PROJECTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        Map<Long, CustomerProjectStatisticsDTO> result = new HashMap<>();
        query.fetch().map((r) -> {
            if(r != null && r.getProjectSource() != null) {
                String[] ids = r.getProjectSource().split(",");
                for(String id : ids) {
                    BigDecimal projectAmount = r.getProjectAmount() == null ? BigDecimal.ZERO : r.getProjectAmount();
                    if(result.get(Long.valueOf(id)) == null) {
                        CustomerProjectStatisticsDTO dto = new CustomerProjectStatisticsDTO();
                        dto.setProjectAmount(projectAmount);
                        dto.setProjectCount(1L);
                        dto.setProjectSourceItemId(Long.valueOf(id));
                        result.put(Long.valueOf(id), dto);
                    } else {
                        CustomerProjectStatisticsDTO dto = result.get(Long.valueOf(id));
                        BigDecimal oldAmount = dto.getProjectAmount() == null ? BigDecimal.ZERO : dto.getProjectAmount();
                        dto.setProjectAmount(oldAmount.add(projectAmount));
                        dto.setProjectCount(dto.getProjectCount() + 1);
                        result.put(Long.valueOf(id), dto);
                    }
                }
            }
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerCommercial> listCustomerCommercialsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerCommercialsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_COMMERCIALS);
        query.addConditions(Tables.EH_CUSTOMER_COMMERCIALS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_COMMERCIALS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerCommercial> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerCommercial.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEconomicIndicatorsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS);
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEconomicIndicator> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEconomicIndicator.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerId(Long customerId, Timestamp startTime, Timestamp endTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEconomicIndicatorsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS);
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.MONTH.ge(startTime));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.MONTH.le(endTime));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEconomicIndicator> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEconomicIndicator.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerEconomicIndicator> listCustomerEconomicIndicatorsByCustomerIds(List<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEconomicIndicatorsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS);
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.CUSTOMER_ID.in(customerIds));
        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerEconomicIndicator> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEconomicIndicator.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerAnnualStatisticDTO> listCustomerAnnualStatistics(Long communityId, Timestamp now, CrossShardListingLocator locator, Integer pageSize,
               BigDecimal turnoverMinimum, BigDecimal turnoverMaximum, BigDecimal taxPaymentMinimum, BigDecimal taxPaymentMaximum) {
        Integer size = pageSize + 1;
        List<CustomerAnnualStatisticDTO> result = new ArrayList<>();
        dbProvider.mapReduce(AccessSpec.readOnly(), null,
                (DSLContext context, Object reducingContext) -> {
                    SelectQuery<Record> query = context.selectQuery();
                    query.addSelect(Tables.EH_ENTERPRISE_CUSTOMERS.ID,Tables.EH_ENTERPRISE_CUSTOMERS.NAME,
                            Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TURNOVER,
                            Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TAX_PAYMENT);
                    query.addFrom(Tables.EH_ENTERPRISE_CUSTOMERS);
                    query.addJoin(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS, JoinType.LEFT_OUTER_JOIN,
                            Tables.EH_ENTERPRISE_CUSTOMERS.ID.eq(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.CUSTOMER_ID));

                    query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
                    query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
                    query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.START_TIME.le(now));
                    query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.END_TIME.ge(now));

                    if(turnoverMinimum != null) {
                        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TURNOVER.ge(turnoverMinimum));
                    }
                    if(turnoverMaximum != null) {
                        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TURNOVER.le(turnoverMaximum));
                    }
                    if(taxPaymentMinimum != null) {
                        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TAX_PAYMENT.ge(taxPaymentMinimum));
                    }
                    if(taxPaymentMaximum != null) {
                        query.addConditions(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TAX_PAYMENT.le(taxPaymentMaximum));
                    }
                    if (null != locator && null != locator.getAnchor())
                        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ID.lt(locator.getAnchor()));

                    query.addOrderBy(Tables.EH_ENTERPRISE_CUSTOMERS.ID.desc());
                    query.addLimit(size);
                    LOGGER.debug("query sql:{}", query.getSQL());
                    LOGGER.debug("query param:{}", query.getBindValues());
                    query.fetch().map((r) -> {
                        CustomerAnnualStatisticDTO statisticDTO = new CustomerAnnualStatisticDTO();
                        statisticDTO.setEnterpriseCustomerId(r.getValue(Tables.EH_ENTERPRISE_CUSTOMERS.ID));
                        statisticDTO.setEnterpriseCustomerName(r.getValue(Tables.EH_ENTERPRISE_CUSTOMERS.NAME));
                        statisticDTO.setTurnover(r.getValue(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TURNOVER));
                        statisticDTO.setTaxPayment(r.getValue(Tables.EH_CUSTOMER_ECONOMIC_INDICATOR_STATISTICS.TAX_PAYMENT));
                        result.add(statisticDTO);
                        return null;
                    });
                    return true;
                });

        locator.setAnchor(null);
        if (result.size() > pageSize) {
            result.remove(result.size() - 1);
            locator.setAnchor(result.get(result.size() - 1).getEnterpriseCustomerId());
        }

        return result;
    }

    @Override
    public List<CustomerInvestment> listCustomerInvestmentsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerInvestmentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_INVESTMENTS);
        query.addConditions(Tables.EH_CUSTOMER_INVESTMENTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_INVESTMENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerInvestment> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerInvestment.class));
            return null;
        });

        return result;
    }

    @Override
    public List<CustomerPatent> listCustomerPatentsByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerPatentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_PATENTS);
        query.addConditions(Tables.EH_CUSTOMER_PATENTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_PATENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerPatent> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerPatent.class));
            return null;
        });

        return result;
    }

    @Override
    public Map<Long, Long> listCustomerPatentsByCustomerIds(List<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerPatentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_PATENTS);
        query.addConditions(Tables.EH_CUSTOMER_PATENTS.CUSTOMER_ID.in(customerIds));
        query.addConditions(Tables.EH_CUSTOMER_PATENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        Map<Long, Long> result = new HashMap<>();
        query.fetch().map((r) -> {
            if(result.get(r.getPatentStatusItemId()) == null) {
                result.put(r.getPatentStatusItemId(), 1L);
            } else {
                result.put(r.getPatentStatusItemId(), result.get(r.getPatentStatusItemId()) + 1);
            }
            return null;
        });
        return result;
    }

    @Override
    public List<CustomerTrademark> listCustomerTrademarksByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTrademarksRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRADEMARKS);
        query.addConditions(Tables.EH_CUSTOMER_TRADEMARKS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TRADEMARKS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerTrademark> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTrademark.class));
            return null;
        });

        return result;
    }

    @Override
    public Long countTrademarksByCustomerIds(List<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectCount().from(Tables.EH_CUSTOMER_TRADEMARKS)
                .where(Tables.EH_CUSTOMER_TRADEMARKS.CUSTOMER_ID.in(customerIds))
                .and(Tables.EH_CUSTOMER_TRADEMARKS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchAnyInto(Long.class);
    }

    @Override
    public Long countCertificatesByCustomerIds(List<Long> customerIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectCount().from(Tables.EH_CUSTOMER_CERTIFICATES)
                .where(Tables.EH_CUSTOMER_CERTIFICATES.CUSTOMER_ID.in(customerIds))
                .and(Tables.EH_CUSTOMER_CERTIFICATES.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchAnyInto(Long.class);
    }

    @Override
    public void updateCustomerApplyProject(CustomerApplyProject project) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerApplyProjects.class, project.getId()));
        EhCustomerApplyProjectsDao dao = new EhCustomerApplyProjectsDao(context.configuration());

        project.setOperatorUid(UserContext.current().getUser().getId());
        project.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(project);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerApplyProjects.class, project.getId());
    }

    @Override
    public void updateCustomerCommercial(CustomerCommercial commercial) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCommercials.class, commercial.getId()));
        EhCustomerCommercialsDao dao = new EhCustomerCommercialsDao(context.configuration());

        commercial.setOperatorUid(UserContext.current().getUser().getId());
        commercial.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(commercial);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerCommercials.class, commercial.getId());
    }

    @Override
    public void updateCustomerEconomicIndicator(CustomerEconomicIndicator economicIndicator) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicators.class));
        EhCustomerEconomicIndicatorsDao dao = new EhCustomerEconomicIndicatorsDao(context.configuration());

        economicIndicator.setOperatorUid(UserContext.current().getUser().getId());
        economicIndicator.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(economicIndicator);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerEconomicIndicators.class, economicIndicator.getId());
    }

    @Override
    public void updateCustomerInvestment(CustomerInvestment investment) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerInvestments.class, investment.getId()));
        EhCustomerInvestmentsDao dao = new EhCustomerInvestmentsDao(context.configuration());

        investment.setOperatorUid(UserContext.current().getUser().getId());
        investment.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(investment);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerInvestments.class, investment.getId());
    }

    @Override
    public void updateCustomerPatent(CustomerPatent patent) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerPatents.class, patent.getId()));
        EhCustomerPatentsDao dao = new EhCustomerPatentsDao(context.configuration());

        patent.setOperatorUid(UserContext.current().getUser().getId());
        patent.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(patent);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerPatents.class, patent.getId());
    }

    @Override
    public void updateCustomerTrademark(CustomerTrademark trademark) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrademarks.class, trademark.getId()));
        EhCustomerTrademarksDao dao = new EhCustomerTrademarksDao(context.configuration());

        trademark.setOperatorUid(UserContext.current().getUser().getId());
        trademark.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(trademark);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrademarks.class, trademark.getId());
    }

    @Override
    public void createCustomerCertificate(CustomerCertificate certificate) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerCertificates.class));
        certificate.setId(id);
        certificate.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        certificate.setCreateUid(UserContext.current().getUser().getId());
        certificate.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerCertificate: " + certificate);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCertificates.class, id));
        EhCustomerCertificatesDao dao = new EhCustomerCertificatesDao(context.configuration());
        dao.insert(certificate);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerCertificates.class, null);
    }

    @Override
    public void deleteCustomerCertificate(CustomerCertificate certificate) {
        assert(certificate.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCertificates.class, certificate.getId()));
        EhCustomerCertificatesDao dao = new EhCustomerCertificatesDao(context.configuration());
        dao.delete(certificate);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerCertificates.class, certificate.getId());
    }

    @Override
    public CustomerCertificate findCustomerCertificateById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerCertificatesDao dao = new EhCustomerCertificatesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerCertificate.class);
    }

    @Override
    public List<CustomerCertificate> listCustomerCertificatesByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerCertificatesRecord> query = context.selectQuery(Tables.EH_CUSTOMER_CERTIFICATES);
        query.addConditions(Tables.EH_CUSTOMER_CERTIFICATES.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_CERTIFICATES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerCertificate> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerCertificate.class));
            return null;
        });

        return result;
    }

    @Override
    public void updateCustomerCertificate(CustomerCertificate certificate) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCertificates.class, certificate.getId()));
        EhCustomerCertificatesDao dao = new EhCustomerCertificatesDao(context.configuration());

        certificate.setOperatorUid(UserContext.current().getUser().getId());
        certificate.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(certificate);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerCertificates.class, certificate.getId());
    }

	@Override
	public void createCustomerTracking(CustomerTracking tracking) {
	    long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerTrackings.class));
	    tracking.setId(id);
	    tracking.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	    tracking.setCreatorUid(UserContext.current().getUser().getId());
	    tracking.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerTracking: " + tracking);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackings.class, id));
        EhCustomerTrackingsDao dao = new EhCustomerTrackingsDao(context.configuration());
        dao.insert(tracking);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerTrackings.class, null);
	}

	@Override
	public CustomerTracking findCustomerTrackingById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerTrackingsDao dao = new EhCustomerTrackingsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerTracking.class);
	}

	@Override
	public void deleteCustomerTracking(CustomerTracking tracking) {
		assert(tracking.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackings.class, tracking.getId()));
        context.update(Tables.EH_CUSTOMER_TRACKINGS)
        	   .set(Tables.EH_CUSTOMER_TRACKINGS.STATUS,CommonStatus.INACTIVE.getCode())
        	   .set(Tables.EH_CUSTOMER_TRACKINGS.DELETE_UID,UserContext.current().getUser().getId())
        	   .set(Tables.EH_CUSTOMER_TRACKINGS.DELETE_TIME , new Timestamp(DateHelper.currentGMTTime().getTime()))
        	   .where(Tables.EH_CUSTOMER_TRACKINGS.ID.eq(tracking.getId()))
        	   .execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrackings.class, tracking.getId());
	}

	@Override
	public void updateCustomerTracking(CustomerTracking tracking) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackings.class, tracking.getId()));
        EhCustomerTrackingsDao dao = new EhCustomerTrackingsDao(context.configuration());

        tracking.setUpdateUid(UserContext.current().getUser().getId());
        tracking.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(tracking);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrackings.class, tracking.getId());
	}

	@Override
	public List<CustomerTracking> listCustomerTrackingsByCustomerId(Long customerId,Byte customerSource) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTrackingsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRACKINGS);
        query.addConditions(Tables.EH_CUSTOMER_TRACKINGS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TRACKINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_CUSTOMER_TRACKINGS.CUSTOMER_SOURCE.eq(customerSource));
//        query.addConditions(Tables.EH_CUSTOMER_TRACKINGS.TRACKING_UID.eq(UserContext.currentUserId()));
        query.addOrderBy(Tables.EH_CUSTOMER_TRACKINGS.TRACKING_TIME.desc());
        List<CustomerTracking> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTracking.class));
            return null;
        });
        return result;
	}

	@Override
	public void createCustomerTrackingPlan(CustomerTrackingPlan plan) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerTrackingPlans.class));
		plan.setId(id);
		plan.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		plan.setCreatorUid(UserContext.current().getUser().getId());
		plan.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerTrackingPlan: " + plan);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackingPlans.class, id));
        EhCustomerTrackingPlansDao dao = new EhCustomerTrackingPlansDao(context.configuration());
        dao.insert(plan);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerTrackingPlans.class, null);
	}

	@Override
	public CustomerTrackingPlan findCustomerTrackingPlanById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerTrackingPlansDao dao = new EhCustomerTrackingPlansDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerTrackingPlan.class);
	}

	@Override
	public void deleteCustomerTrackingPlan(CustomerTrackingPlan plan) {
		assert(plan.getId() != null);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackingPlans.class, plan.getId()));
        context.update(Tables.EH_CUSTOMER_TRACKING_PLANS)
        	   .set(Tables.EH_CUSTOMER_TRACKING_PLANS.STATUS,CommonStatus.INACTIVE.getCode())
        	   .set(Tables.EH_CUSTOMER_TRACKING_PLANS.DELETE_UID,UserContext.current().getUser().getId())
        	   .set(Tables.EH_CUSTOMER_TRACKING_PLANS.DELETE_TIME , new Timestamp(DateHelper.currentGMTTime().getTime()))
        	   .where(Tables.EH_CUSTOMER_TRACKING_PLANS.ID.eq(plan.getId()))
        	   .execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrackingPlans.class, plan.getId());
	}

	@Override
	public void updateCustomerTrackingPlan(CustomerTrackingPlan plan) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackingPlans.class, plan.getId()));
        EhCustomerTrackingPlansDao dao = new EhCustomerTrackingPlansDao(context.configuration());

        plan.setUpdateUid(UserContext.current().getUser().getId());
        plan.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(plan);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrackingPlans.class, plan.getId());
	}

	@Override
	public List<CustomerTrackingPlan> listCustomerTrackingPlans(Long customerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTrackingPlansRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRACKING_PLANS);
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CREATOR_UID.eq(UserContext.currentUserId()));
        
        List<CustomerTrackingPlan> result = new ArrayList<CustomerTrackingPlan>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTrackingPlan.class));
            return null;
        });
        return result;
	}


    @Override
    public void saveCustomerEvent(int i, EnterpriseCustomer customer, EnterpriseCustomer exist,Byte deviceType, String moduleName) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEvents.class));
        CustomerEvent event = new CustomerEvent();
        event.setId(id);
        event.setNamespaceId(UserContext.getCurrentNamespaceId());
        event.setCustomerType(CustomerType.ENTERPRISE.getCode());
        event.setCustomerId(customer.getId());
        event.setCustomerName(customer.getName());
        event.setContactName(customer.getContactName());
        event.setDeviceType(deviceType);
        String content = null;
        switch(i){
            case 1 :
                content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.ADD , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
                break;
            case 2 :
                content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.DELETE , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
                break;
            case 3 :
                content = compareEnterpriseCustomer(customer,exist, moduleName);
                break;
            default :break;
        }
        if(StringUtils.isNotEmpty(content)){
            event.setContent(content);
            event.setCreatorUid(UserContext.currentUserId());
            event.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEvents.class, id));
            EhCustomerEventsDao dao = new EhCustomerEventsDao(context.configuration());
            LOGGER.info("saveCustomerEventWithInsert: " + event);
            dao.insert(event);
            DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEvents.class, null);
        }
    }

    @Override
    public void saveCustomerEvents(int i, List<EhEnterpriseCustomers> customers, Byte deviceType) {
        List<EhCustomerEvents> events = new ArrayList<>();
        customers.forEach(customer -> {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEvents.class));
            CustomerEvent event = new CustomerEvent();
            event.setId(id);
            event.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setCustomerType(CustomerType.ENTERPRISE.getCode());
            event.setCustomerId(customer.getId());
            event.setCustomerName(customer.getName());
            event.setContactName(customer.getContactName());
            event.setDeviceType(deviceType);
            String content = null;
            switch(i){
                case 1 :
                    content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.ADD , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
                    break;
                case 2 :
                    content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.DELETE , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
                    break;
                case 3 :
                    content = compareEnterpriseCustomer((ConvertHelper.convert(customer, EnterpriseCustomer.class)),null, null);
                    break;
                default :break;
            }
            if(StringUtils.isNotEmpty(content)){
                event.setContent(content);
                event.setCreatorUid(UserContext.currentUserId());
                event.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                events.add(event);
            }
        });
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEvents.class));
        EhCustomerEventsDao dao = new EhCustomerEventsDao(context.configuration());
        LOGGER.info("saveCustomerEventWithInsert: " + events);
        dao.insert(events);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEvents.class, null);
    }


	@Override
	public void saveCustomerEvent(int i, EnterpriseCustomer customer, EnterpriseCustomer exist,Byte deviceType) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEvents.class));
		CustomerEvent event = new CustomerEvent(); 
		event.setId(id);
		event.setNamespaceId(UserContext.getCurrentNamespaceId());
		event.setCustomerType(CustomerType.ENTERPRISE.getCode());
		event.setCustomerId(customer.getId());
		event.setCustomerName(customer.getName());
		event.setContactName(customer.getContactName());
        event.setDeviceType(deviceType);
        String content = null;
		switch(i){
		case 1 : 
			content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.ADD , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
			break;
		case 2 :
			content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.DELETE , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
			break;
		case 3 :
			content = compareEnterpriseCustomer(customer,exist, null);
			break;
		default :break;
		}
		if(StringUtils.isNotEmpty(content)){
			event.setContent(content);
			event.setCreatorUid(UserContext.currentUserId());
			event.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEvents.class, id));
	        EhCustomerEventsDao dao = new EhCustomerEventsDao(context.configuration());
	        LOGGER.info("saveCustomerEventWithInsert: " + event);
	        dao.insert(event);
	        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEvents.class, null);
		}
	}


	@Override
	public List<CustomerEvent> listCustomerEvents(Long customerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEventsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_EVENTS);
        query.addConditions(Tables.EH_CUSTOMER_EVENTS.CUSTOMER_ID.eq(customerId));
        query.addOrderBy(Tables.EH_CUSTOMER_EVENTS.ID.desc());
        List<CustomerEvent> result = new ArrayList<CustomerEvent>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerEvent.class));
            return null;
        });
        return result;
	}
	
	private String compareEnterpriseCustomer(EnterpriseCustomer customer, EnterpriseCustomer exist, String moduleName) {
		//查出模板配置的参数
		ListFieldCommand command = new ListFieldCommand();
		command.setNamespaceId(customer.getNamespaceId());

        if(StringUtils.isNotBlank(moduleName)){
            command.setModuleName(moduleName);
        }else{
            command.setModuleName(CustomerTrackingTemplateCode.MODULE_NAME_INVITED);
        }
		command.setGroupPath(CustomerTrackingTemplateCode.GROUP_PATH);
		command.setCommunityId(customer.getCommunityId());
		List<FieldDTO> fields = fieldService.listFields(command);
		String  getPrefix = "get";
		StringBuffer buffer = new StringBuffer();
		if(null != fields && fields.size() > 0){
			for(FieldDTO field : fields){
				String getter = getPrefix + StringUtils.capitalize(field.getFieldName());
				Method methodNew = ReflectionUtils.findMethod(customer.getClass(), getter);
				Method methodOld = ReflectionUtils.findMethod(exist.getClass(), getter);
                String fieldType = field.getFieldType();
				Object objNew = null;
				Object objOld = null;
				try {
					if(null != methodNew){
						objNew = methodNew.invoke(customer);
						objOld = methodOld.invoke(exist);
					}
				} catch (Exception e) {
					throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRACKING_NOT_EXIST,
		                    "reflect exception");
				}
				if(null != objNew || null != objOld){
					if(!(objNew == null ? "" : objNew).equals((objOld == null ? "" : objOld))){
						String  content = "";
						String  newData = objNew == null ? "空" : objNew.toString();
						String  oldData = objOld == null ? "空" : objOld.toString();
                        LOGGER.debug("compareEnterpriseCustomer FieldName: {}; newData: {}; oldData: {}",
                                field.getFieldName(), newData, oldData);
						if(field.getFieldName().lastIndexOf("ItemId") > -1){
							ScopeFieldItem levelItemNew = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(),(objNew == null ? -1l : Long.parseLong(objNew.toString())));
					        if(levelItemNew != null) {
					        	newData = levelItemNew.getItemDisplayName();
					        }
					        ScopeFieldItem levelItemOld = fieldProvider.findScopeFieldItemByFieldItemId(exist.getNamespaceId(),customer.getOwnerId(),customer.getCommunityId(), (objOld == null ? -1l : Long.parseLong(objOld.toString())));
					        if(levelItemOld != null) {
					        	oldData = levelItemOld.getItemDisplayName();
					        }
						}

                        FieldParams params = (FieldParams) StringHelper.fromJsonString(field.getFieldParam(), FieldParams.class);
                        if((params.getFieldParamType().equals("select") || params.getFieldParamType().equals("customizationSelect")) && field.getFieldName().lastIndexOf("Id") > -1){
                            ScopeFieldItem levelItemNew = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getOwnerId(), customer.getCommunityId(),(objNew == null ? -1l : Long.parseLong(objNew.toString())));
                            if(levelItemNew != null) {
                                newData = levelItemNew.getItemDisplayName();
                            }
                            ScopeFieldItem levelItemOld = fieldProvider.findScopeFieldItemByFieldItemId(exist.getNamespaceId(),customer.getOwnerId(),customer.getCommunityId(), (objOld == null ? -1l : Long.parseLong(objOld.toString())));
                            if(levelItemOld != null) {
                                oldData = levelItemOld.getItemDisplayName();
                            }
                        }

						if("propertyType".equals(field.getFieldName())){
							ScopeFieldItem levelItemNew = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getOwnerId(),customer.getCommunityId(),(objNew == null ? -1l : Long.parseLong(objNew.toString())));
					        if(levelItemNew != null) {
					        	newData = levelItemNew.getItemDisplayName();
					        }
					        ScopeFieldItem levelItemOld = fieldProvider.findScopeFieldItemByFieldItemId(exist.getNamespaceId(),customer.getOwnerId(),customer.getCommunityId(), (objOld == null ? -1l : Long.parseLong(objOld.toString())));
					        if(levelItemOld != null) {
					        	oldData = levelItemOld.getItemDisplayName();
					        }
						}
                        if("trackingUid".equals(field.getFieldName())) {
                            if (oldData == null || oldData.equals("")|| oldData.equals("空")) {
                                oldData = "空";
                            } else {
                                oldData = exist.getTrackingName();
                            }

                            if (newData == null || newData.equals("")||newData.equals("空")) {
                                newData = "空";
                            } else {
                                newData = customer.getTrackingName();
                            }
                        }
                        if (fieldType.equals("Date")) {
                            if (objOld != null) {
                                Timestamp old = (Timestamp) objOld;
                                oldData = old.toLocalDateTime().format(formatter);
                            }
                            if (objNew != null) {
                                Timestamp newTime = (Timestamp) objNew;
                                newData = newTime.toLocalDateTime().format(formatter);
                            }
                        }
                        // remove rich text html lable
                        if("corp_description".equals(field.getFieldName())){
                             String regEx_html = "<[^>]+>";
                            Pattern pattern = Pattern.compile(regEx_html);
                            Matcher oldDataMatcher = pattern.matcher(oldData);
                            Matcher newDataMatcher = pattern.matcher(newData);
                            if(oldDataMatcher.find()){
                                oldData = oldDataMatcher.replaceAll("");
                            }
                            if (newDataMatcher.find()) {
                                newData = newDataMatcher.replaceAll("");
                            }

                        }
                        if (fieldType.equals("Double")) {
                            DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                            if (objNew != null && objNew != "空"){
                                objNew = decimalFormat.format(objNew);
                                newData = objNew.toString();
                            }
                            if (objNew != null && objNew != "空"){
                                objOld = decimalFormat.format(objOld);
                                oldData = objOld.toString();
                            }
                        }
                        Map<String,Object> map = new HashMap<>();
						map.put("display", field.getFieldDisplayName());
						map.put("oldData", oldData);
						map.put("newData", newData);
						content = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, CustomerTrackingTemplateCode.UPDATE , UserContext.current().getUser().getLocale(), map, "");
						if(field.getFieldParam().contains("image")){
                            content = content.split(":")[0];
                        }
						buffer.append(content);
						buffer.append(";");
					}
				}
			}
		}
		return buffer.toString().length() > 0 ? buffer.toString().substring(0,buffer.toString().length() -1) : buffer.toString();
	}


	@Override
	public void allotEnterpriseCustomer(EnterpriseCustomer customer) {
		assert(customer.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, customer.getId()));
        context.update(Tables.EH_ENTERPRISE_CUSTOMERS)
        	   .set(Tables.EH_ENTERPRISE_CUSTOMERS.TRACKING_UID, customer.getTrackingUid())
        	   .set(Tables.EH_ENTERPRISE_CUSTOMERS.TRACKING_NAME,customer.getTrackingName())
        	   .where(Tables.EH_ENTERPRISE_CUSTOMERS.ID.eq(customer.getId()))
        	   .execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
	}


	@Override
	public void giveUpEnterpriseCustomer(EnterpriseCustomer customer) {
		assert(customer.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, customer.getId()));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        dao.update(customer);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
	}


	@Override
	public List<EnterpriseCustomerDTO> findEnterpriseCustomersByDistance(ListNearbyEnterpriseCustomersCommand cmd , ListingLocator locator , int pageSize) {
		List<EnterpriseCustomerDTO> list = new ArrayList<EnterpriseCustomerDTO>();
	        String geoHashStr = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude()).substring(0, 6);
	        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhEnterpriseCustomers.class), null,
	                (DSLContext context, Object reducingContext)-> {
	                    String likeVal = geoHashStr + "%";
	                    Condition cond = Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommunityAdminStatus.ACTIVE.getCode());
	                    cond = cond.and(Tables.EH_ENTERPRISE_CUSTOMERS.GEOHASH.like(likeVal));
	                    cond = cond.and(Tables.EH_ENTERPRISE_CUSTOMERS.ID.ge(locator.getAnchor()));
	                    SelectOffsetStep<Record> query = context.select().from(Tables.EH_ENTERPRISE_CUSTOMERS)
	    	        		    .where(cond).limit(pageSize);
	                    if(LOGGER.isDebugEnabled()) {
		                    LOGGER.debug("Query enterpriseCustomer nearby, sql=" + query.getSQL());
		                    LOGGER.debug("Query enterpriseCustomer nearby, bindValues=" + query.getBindValues());
		                }
	                    query.fetch().map((r) -> {
	                    EnterpriseCustomerDTO customerDTO = ConvertHelper.convert(r, EnterpriseCustomerDTO.class);
	                    list.add(customerDTO);
	                    	return null;
	                    });

	                return true;
	                
	            });
	        return list;
	}


	@Override
	public boolean updateTrackingPlanNotify(Long recordId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackingPlans.class));

        int effect = context.update(Tables.EH_CUSTOMER_TRACKING_PLANS)
                .set(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_STATUS, TrackingPlanNotifyStatus.ALREADY_SENDED.getCode())
                .where(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_STATUS.eq(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode())
                .and(Tables.EH_CUSTOMER_TRACKING_PLANS.ID.eq(recordId)))
                .execute();

        if(effect > 0) {
            return true;
        }

        return false;
	}


	@Override
	public List<CustomerTrackingPlan> listWaitNotifyTrackingPlans(Timestamp queryStartTime, Timestamp queryEndTime) {
		List<CustomerTrackingPlan> plans = new ArrayList<>();
		List<CustomerTrackingPlan> futurePlan =  dbProvider.getDslContext(AccessSpec.readOnly())
				.select()
				.from(Tables.EH_CUSTOMER_TRACKING_PLANS)
				.where(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_STATUS.eq(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode()))
				.and(Tables.EH_CUSTOMER_TRACKING_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_TIME.gt(queryStartTime))
				.and(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_TIME.le(queryEndTime))
				.fetch()
				.map(r->ConvertHelper.convert(r, CustomerTrackingPlan.class));
		List<CustomerTrackingPlan> passPlan =  dbProvider.getDslContext(AccessSpec.readOnly())
				.select()
				.from(Tables.EH_CUSTOMER_TRACKING_PLANS)
				.where(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_STATUS.eq(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode()))
				.and(Tables.EH_CUSTOMER_TRACKING_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
				.and(Tables.EH_CUSTOMER_TRACKING_PLANS.NOTIFY_TIME.le(queryStartTime))
				.fetch()
				.map(r->ConvertHelper.convert(r, CustomerTrackingPlan.class));
		plans.addAll(futurePlan);
		plans.addAll(passPlan);
		return plans;
	}


	@Override
	public void createTrackingNotifyLog(TrackingNotifyLog log) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhTrackingNotifyLogs.class));

        log.setId(id);
        log.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        LOGGER.info("createTrackingNotifyLog: " + log);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhTrackingNotifyLogsDao dao = new EhTrackingNotifyLogsDao(context.configuration());
        dao.insert(log);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerTrackingPlans.class, null);
	}


	@Override
	public void updateTrackingPlanReadStatus(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTrackingPlans.class, id));
		context.update(Tables.EH_CUSTOMER_TRACKING_PLANS)
				.set(Tables.EH_CUSTOMER_TRACKING_PLANS.READ_STATUS , TrackingPlanReadStatus.READED.getCode())
				.where(Tables.EH_CUSTOMER_TRACKING_PLANS.ID.eq(id))
				.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCustomerTrackingPlans.class, id);
	}


	@Override
	public List<CustomerTrackingPlan> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd , Timestamp todayFirst) {
		List<CustomerTrackingPlan> result = new ArrayList<CustomerTrackingPlan>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTrackingPlansRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRACKING_PLANS);
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CREATOR_UID.eq(UserContext.currentUserId()));
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.TRACKING_TIME.ge(todayFirst));
        if(StringUtils.isNotEmpty(cmd.getCustomerName())){
			String customerName = "%" + cmd.getCustomerName() + "%";
			query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CUSTOMER_NAME.like(customerName));
		}
        if(null != cmd.getCustomerId()){
        	query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CUSTOMER_ID.eq(cmd.getCustomerId()));
        }
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTrackingPlan.class));
            return null;
        });
        return result;
	}


	@Override
	public List<CustomerTrackingPlan> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd,Long todayFirst) {
		List<CustomerTrackingPlan> result = new ArrayList<CustomerTrackingPlan>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTrackingPlansRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRACKING_PLANS);
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CREATOR_UID.eq(UserContext.currentUserId()));
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.TRACKING_TIME.lt(new Timestamp(todayFirst)));
        Calendar calc =Calendar.getInstance(); 
        calc.setTime(new Date(todayFirst));
        calc.add(Calendar.DAY_OF_MONTH, -30);
        query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.TRACKING_TIME.gt(new Timestamp(calc.getTime().getTime())));
        if(StringUtils.isNotEmpty(cmd.getCustomerName())){
			String customerName = "%" + cmd.getCustomerName() + "%";
			query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CUSTOMER_NAME.like(customerName));
		}
        if(null != cmd.getCustomerId()){
        	query.addConditions(Tables.EH_CUSTOMER_TRACKING_PLANS.CUSTOMER_ID.eq(cmd.getCustomerId()));
        }
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTrackingPlan.class));
            return null;
        });
        return result;
	}


	@Override
	public void updateCustomerLastTrackingTime(EnterpriseCustomer customer) {
		assert(customer.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, customer.getId()));
        context.update(Tables.EH_ENTERPRISE_CUSTOMERS)
        	   .set(Tables.EH_ENTERPRISE_CUSTOMERS.LAST_TRACKING_TIME, customer.getLastTrackingTime())
        	   .where(Tables.EH_ENTERPRISE_CUSTOMERS.ID.eq(customer.getId()))
        	   .execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
	}

    @Override
    public String findLastEnterpriseCustomerVersionByCommunity(Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_ENTERPRISE_CUSTOMERS.VERSION.desc());
        query.addLimit(1);
        List<EnterpriseCustomer> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, EnterpriseCustomer.class));
            return null;
        });
        if(result == null || result.size() == 0) {
            return null;
        }

        return result.get(0).getVersion();
    }

    @Override
    public List<AuthorizationRelation> listAuthorizationRelations(String ownerType, Long ownerId, Long moduleId, Long appId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhAuthorizationRelationsRecord> query = context.selectQuery(Tables.EH_AUTHORIZATION_RELATIONS);
        query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.MODULE_ID.eq(moduleId));
        if (appId != null) {
            query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.APP_ID.eq(appId));
        }
        query.addConditions(Tables.EH_AUTHORIZATION_RELATIONS.ALL_PROJECT_FLAG.eq(AllFlag.ALL.getCode())
                .or(Tables.EH_AUTHORIZATION_RELATIONS.PROJECT_JSON.like("%"+communityId+"%")));
        return query.fetchInto(AuthorizationRelation.class);
    }

    @Override
    public void updateEnterpriseBannerUri(Long customerId, List<AttachmentDescriptor> banners) {
        deleteCustomerBannerUriByCustomerId(customerId);
        if (banners != null && banners.size() > 0) {
            banners.forEach((b) -> createCustomerBannerUri(customerId, b));
        }
    }

    private void createCustomerBannerUri(Long customerId, AttachmentDescriptor banner) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCustomerAttachments.class));
        CustomerAttachements attachement = new CustomerAttachements();
        attachement = ConvertHelper.convert(banner, CustomerAttachements.class);
        attachement.setId(id);
        attachement.setCustomerId(customerId);
        attachement.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        attachement.setCreatorUid(UserContext.currentUserId());
        EhEnterpriseCustomerAttachmentsDao dao = new EhEnterpriseCustomerAttachmentsDao(context.configuration());
        dao.insert(attachement);
    }

    private void deleteCustomerBannerUriByCustomerId(Long customerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ENTERPRISE_CUSTOMER_ATTACHMENTS)
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ATTACHMENTS.CUSTOMER_ID.eq(customerId))
                .execute();
    }

    @Override
    public void createEnterpriseCustomerAdminRecord(Long customerId, String contactName,String contactType, String contactToken,Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCustomerAdmins.class));
        CustomerAdminRecord record = new CustomerAdminRecord();
        record.setContactName(contactName);
        record.setContactToken(contactToken);
        record.setCustomerId(customerId);
        record.setContactType(contactType);
        record.setId(id);
        record.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        record.setCreatorUid(UserContext.currentUserId());
        record.setNamespaceId(namespaceId);
        record.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhEnterpriseCustomerAdminsDao dao = new EhEnterpriseCustomerAdminsDao(context.configuration());
        dao.insert(record);
    }

    @Override
    public void deleteEnterpriseCustomerAdminRecord(Long customerId, String contactToken) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS)
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CONTACT_TOKEN.eq(contactToken).and(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CUSTOMER_ID.eq(customerId)))
                .execute();
    }

    @Override
    public void updateEnterpriseCustomerAdminRecord(String contacToken,Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        List<CustomerAdminRecord> records = context.selectFrom(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS)
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CONTACT_TOKEN.eq(contacToken))
                .and(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.NAMESPACE_ID.eq(namespaceId))
                .fetchInto(CustomerAdminRecord.class);
        if (records != null && records.size() > 0) {
            for (CustomerAdminRecord record : records) {
                record.setContactType(OrganizationMemberTargetType.USER.getCode());
                EhEnterpriseCustomerAdminsDao dao = new EhEnterpriseCustomerAdminsDao(context.configuration());
                dao.update(record);
            }
        }
    }

    @Override
    public List<CustomerAdminRecord> listEnterpriseCustomerAdminRecords(Long customerId, String contactType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomerAdminsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CUSTOMER_ID.eq(customerId));
        if (contactType != null) {
            query.addConditions(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CONTACT_TYPE.eq(contactType));
        }
        return query.fetchInto(CustomerAdminRecord.class);
    }

    @Override
    public List<EnterpriseAttachment> listEnterpriseCustomerPostUri(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENTERPRISE_CUSTOMER_ATTACHMENTS)
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ATTACHMENTS.CUSTOMER_ID.eq(customerId))
                .fetchInto(EnterpriseAttachment.class);
    }

    @Override
    public List<Organization> listNoSyncOrganizations(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        List<Long> organizationIds = context.selectDistinct(Tables.EH_ENTERPRISE_CUSTOMERS.ORGANIZATION_ID)
//                .from(Tables.EH_ENTERPRISE_CUSTOMERS)
//                .fetchInto(Long.class);
//        List<Long> namespaceId = context.selectDistinct(Tables.EH_LAUNCH_PAD_ITEMS.NAMESPACE_ID)
//                .from(Tables.EH_LAUNCH_PAD_ITEMS)
//                .where(Tables.EH_LAUNCH_PAD_ITEMS.ACTION_TYPE.eq(ActionType.PARKENTERPRISE.getCode()))
//                .fetchInto(Long.class);
        SelectQuery<EhOrganizationsRecord> query = context.selectQuery(Tables.EH_ORGANIZATIONS);
//        query.addConditions(Tables.EH_ORGANIZATIONS.ID.notIn(organizationIds));
        query.addConditions(Tables.EH_ORGANIZATIONS.GROUP_TYPE.eq(OrganizationGroupType.ENTERPRISE.getCode()));
        query.addConditions(Tables.EH_ORGANIZATIONS.STATUS.eq(OrganizationStatus.ACTIVE.getCode()));
//        query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.in(namespaceId));
        //大师说暂时可以用item来区分使用园区企业应用的namespaceId
        if (namespaceId != null) {
            query.addConditions(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
        }
        return query.fetchInto(Organization.class);
    }

    @Override
    public void deleteAllCustomerEntryInfo(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_CUSTOMER_ENTRY_INFOS)
                .where(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_ID.eq(customerId))
                .execute();
    }

    @Override
    public void deleteAllEnterpriseCustomerAdminRecord(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS)
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CUSTOMER_ID.eq(id))
                .execute();
    }

    @Override
    public void deleteCustomerEntryInfoByCustomerIdAndAddressId(Long customerId, Long addressId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_CUSTOMER_ENTRY_INFOS)
                .where(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_ID.eq(customerId))
                .and(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.eq(addressId))
                .execute();
    }

    @Override
    public void deleteCustomerEntryInfoByCustomerIdAndAddressIds(Long customerId, List<Long> addressIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_CUSTOMER_ENTRY_INFOS)
                .where(Tables.EH_CUSTOMER_ENTRY_INFOS.CUSTOMER_ID.eq(customerId))
                .and(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.in(addressIds))
                .execute();
    }

    @Override
    public void updateEnterpriseCustomerAdminRecordByCustomerId(Long customerId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS)
                .set(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CONTACT_TYPE, OrganizationMemberTargetType.USER.getCode())
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CUSTOMER_ID.eq(customerId))
                .and(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.NAMESPACE_ID.eq(namespaceId))
                .execute();
    }

    @Override

    public String getEnterpriseCustomerNameById(Long enterpriseCustomerId) {
        return this.dbProvider.getDslContext(AccessSpec.readOnly()).select(Tables.EH_ENTERPRISE_CUSTOMERS.NAME)
                .from(Tables.EH_ENTERPRISE_CUSTOMERS).where(Tables.EH_ENTERPRISE_CUSTOMERS.ID.eq(enterpriseCustomerId))
                .fetchOne(Tables.EH_ENTERPRISE_CUSTOMERS.NAME);
    }

    @Override
    public List<EasySearchEnterpriseCustomersDTO> listEnterpriseCustomerNameAndId(List<Long> ids) {
        List<EasySearchEnterpriseCustomersDTO> ret = new ArrayList<>();
        this.dbProvider.getDslContext(AccessSpec.readOnly())
                .select(Tables.EH_ENTERPRISE_CUSTOMERS.NAME, Tables.EH_ENTERPRISE_CUSTOMERS.ID)
                .from(Tables.EH_ENTERPRISE_CUSTOMERS)
                .where(Tables.EH_ENTERPRISE_CUSTOMERS.ID.in(ids))
                .fetch()
                .forEach(r -> {
                    EasySearchEnterpriseCustomersDTO dto = new EasySearchEnterpriseCustomersDTO();
                    dto.setId(r.getValue(Tables.EH_ENTERPRISE_CUSTOMERS.ID));
                    dto.setName(r.getValue(Tables.EH_ENTERPRISE_CUSTOMERS.NAME));
                    ret.add(dto);
                });
        return ret;
    }

    @Override
    public List<EasySearchEnterpriseCustomersDTO> listCommunityEnterpriseCustomers(Long communityId, Integer namespaceId) {
        List<EasySearchEnterpriseCustomersDTO> dtos = new ArrayList<>();
        this.dbProvider.getDslContext(AccessSpec.readOnly())
                .select()
                .from(Tables.EH_ENTERPRISE_CUSTOMERS)
                .where(Tables.EH_ENTERPRISE_CUSTOMERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ENTERPRISE_CUSTOMERS.STATUS.notEqual(CommonStatus.INACTIVE.getCode()))
                .fetch()
                .forEach(r -> {
                    EasySearchEnterpriseCustomersDTO dto = new EasySearchEnterpriseCustomersDTO();
                    dto.setName(r.getValue(Tables.EH_ENTERPRISE_CUSTOMERS.NAME));
                    dto.setId(r.getValue(Tables.EH_ENTERPRISE_CUSTOMERS.ID));
                    dtos.add(dto);
                });
        return dtos;
    }

    public void deleteCustomerEntryInfoByBuildingId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_CUSTOMER_ENTRY_INFOS)
                .set(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS,CommonStatus.INACTIVE.getCode())
                .where(Tables.EH_CUSTOMER_ENTRY_INFOS.BUILDING_ID.eq(id))
                .execute();

    }

    @Override
    public void deleteCustomerEntryInfoByAddessId(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_CUSTOMER_ENTRY_INFOS)
                .set(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS,CommonStatus.INACTIVE.getCode())
                .where(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.eq(id))
                .execute();
    }

    @Override
    public void createCustomerAttachements(CustomerAttachment attachment) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerAttachments.class));
        attachment.setId(id);
        attachment.setNamespaceId(UserContext.getCurrentNamespaceId());
        attachment.setStatus(CommonStatus.ACTIVE.getCode());
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        attachment.setCreatorUid(UserContext.currentUserId());
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerAttachmentsDao dao = new EhCustomerAttachmentsDao(context.configuration());
        dao.insert(attachment);
    }

    @Override
    public void deleteAllCustomerAttachements(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_CUSTOMER_ATTACHMENTS)
                .set(Tables.EH_CUSTOMER_ATTACHMENTS.STATUS, CommonStatus.INACTIVE.getCode())
                .where(Tables.EH_CUSTOMER_ATTACHMENTS.CUSTOMER_ID.eq(customerId))
                .execute();
    }

    @Override
    public List<CustomerAttachment> listCustomerAttachments(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return  context.selectFrom(Tables.EH_CUSTOMER_ATTACHMENTS)
                .where(Tables.EH_CUSTOMER_ATTACHMENTS.CUSTOMER_ID.eq(id))
                .and(Tables.EH_CUSTOMER_ATTACHMENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchInto(CustomerAttachment.class);
    }

    @Override
    public CustomerConfiguration getSyncCustomerConfiguration(Integer namespaceId,byte code) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_CUSTOMER_CONFIGUTATIONS)
                .where(Tables.EH_CUSTOMER_CONFIGUTATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_CUSTOMER_CONFIGUTATIONS.SCOPE_ID.eq((long) code))
                .and(Tables.EH_CUSTOMER_CONFIGUTATIONS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchAnyInto(CustomerConfiguration.class);
    }

    @Override
    public void createPotentialCustomer(CustomerPotentialData data) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerPotentialDatas.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerPotentialDatasDao dao = new EhCustomerPotentialDatasDao(context.configuration());
        data.setId(id);
        data.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        data.setStatus(CommonStatus.ACTIVE.getCode());
        dao.insert(data);
    }

    @Override
    public CustomerPotentialData findPotentialCustomerByName(String text) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_CUSTOMER_POTENTIAL_DATAS)
                .where(Tables.EH_CUSTOMER_POTENTIAL_DATAS.NAME.eq(text))
                .fetchAnyInto(CustomerPotentialData.class);
    }

    @Override
    public void deletePotentialCustomer(Long enterpriseId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerPotentialDatasDao datasDao = new EhCustomerPotentialDatasDao(context.configuration());
        CustomerPotentialData data = ConvertHelper.convert(datasDao.findById(enterpriseId), CustomerPotentialData.class);
        if(data!=null){
            data.setStatus(CommonStatus.INACTIVE.getCode());
            data.setDeleteUid(UserContext.currentUserId());
            data.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            datasDao.update(data);
        }
    }

    @Override
    public List<CustomerPotentialData> listPotentialCustomers(Integer namespaceId, Long sourceId, String sourceType,String name,Long pageAnchor,Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerPotentialDatasRecord> query = context.selectQuery(Tables.EH_CUSTOMER_POTENTIAL_DATAS);
        query.addConditions(Tables.EH_CUSTOMER_POTENTIAL_DATAS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_CUSTOMER_POTENTIAL_DATAS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        if (pageAnchor != null && pageAnchor != 0) {
            query.addConditions(Tables.EH_CUSTOMER_POTENTIAL_DATAS.ID.lt(pageAnchor));
        }
        if (sourceId != null) {
            query.addConditions(Tables.EH_CUSTOMER_POTENTIAL_DATAS.SOURCE_ID.eq(sourceId));
        }
        if (StringUtils.isNotBlank(sourceType)) {
            query.addConditions(Tables.EH_CUSTOMER_POTENTIAL_DATAS.SOURCE_TYPE.eq(sourceType));
        }
        if (StringUtils.isNotBlank(name)) {
            query.addConditions(Tables.EH_CUSTOMER_POTENTIAL_DATAS.NAME.like("%" + name + "%"));
        }
        query.addOrderBy(Tables.EH_CUSTOMER_POTENTIAL_DATAS.ID.desc());
        query.addLimit(pageSize);
        return query.fetchInto(CustomerPotentialData.class);
    }

    @Override
    public void updatePotentialTalentsToCustomer(Long customerId, Long sourceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_CUSTOMER_TALENTS)
                .set(Tables.EH_CUSTOMER_TALENTS.CUSTOMER_ID, customerId)
                .set(Tables.EH_CUSTOMER_TALENTS.STATUS,CommonStatus.ACTIVE.getCode())
                .where(Tables.EH_CUSTOMER_TALENTS.ORIGIN_SOURCE_ID.eq(sourceId))
                .execute();
    }


    @Override
    public void deleteCustomerConfiguration(Integer namespaceId,String sourceType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_CUSTOMER_CONFIGUTATIONS)
                .where(Tables.EH_CUSTOMER_CONFIGUTATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_CUSTOMER_CONFIGUTATIONS.SCOPE_TYPE.eq(sourceType))
                .execute();
    }

    @Override
    public void createCustomerConfiguration(CustomerConfiguration customerConfiguration) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerConfigutations.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerConfigutationsDao dao = new EhCustomerConfigutationsDao(context.configuration());
        customerConfiguration.setId(id);
        customerConfiguration.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        customerConfiguration.setStatus(CommonStatus.ACTIVE.getCode());
        customerConfiguration.setCreatorUid(UserContext.currentUserId());
        dao.insert(customerConfiguration);
    }

    @Override
    public List<CustomerConfiguration> listSyncPotentialCustomer(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_CUSTOMER_CONFIGUTATIONS)
                .where(Tables.EH_CUSTOMER_CONFIGUTATIONS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_CUSTOMER_CONFIGUTATIONS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchInto(CustomerConfiguration.class);
    }

    @Override
    public List<CustomerTalent> listPotentialTalentBySourceId(Long sourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTalentsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TALENTS);
//        query.addConditions(Tables.EH_CUSTOMER_TALENTS.CUSTOMER_ID.eq(0L));
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.ORIGIN_SOURCE_ID.eq(sourceId));
        query.addConditions(Tables.EH_CUSTOMER_TALENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<CustomerTalent> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CustomerTalent.class));
            return null;
        });

        return result;
    }

    @Override
    public void updatePotentialCustomer(Long sourceId, String name, Long userId, Integer currentNamespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_CUSTOMER_POTENTIAL_DATAS)
                .set(Tables.EH_CUSTOMER_POTENTIAL_DATAS.NAME, name)
                .set(Tables.EH_CUSTOMER_POTENTIAL_DATAS.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
                .set(Tables.EH_CUSTOMER_POTENTIAL_DATAS.OPERATE_UID, userId)
                .where(Tables.EH_CUSTOMER_POTENTIAL_DATAS.NAMESPACE_ID.eq(currentNamespaceId))
                .and(Tables.EH_CUSTOMER_POTENTIAL_DATAS.ID.eq(sourceId))
                .execute();
    }


    @Override
    public void updatePotentialCustomer(CustomerPotentialData latestPotentialCustomer) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerPotentialDatasDao datasDao = new EhCustomerPotentialDatasDao(context.configuration());
        datasDao.update(latestPotentialCustomer);
    }

    @Override
    public void updateCustomerTalentRegisterStatus(String contactToken) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_CUSTOMER_TALENTS)
                .set(Tables.EH_CUSTOMER_TALENTS.REGISTER_STATUS, CommonStatus.ACTIVE.getCode())
                .where(Tables.EH_CUSTOMER_TALENTS.PHONE.eq(contactToken))
                .execute();
    }

    @Override
    public CustomerTalent findPotentialTalentBySourceId(Long sourceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_CUSTOMER_TALENTS)
                .where(Tables.EH_CUSTOMER_TALENTS.ORIGIN_SOURCE_ID.eq(sourceId))
                .and(Tables.EH_CUSTOMER_TALENTS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .fetchAnyInto(CustomerTalent.class);
    }

    @Override
    public void createCustomerEvent(CustomerEvent event) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerEvents.class));
        event.setId(id);
        EhCustomerEventsDao dao = new EhCustomerEventsDao(context.configuration());
        event.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(event);
    }

    @Override
    public CustomerTalent findPotentialCustomerById(Long sourceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhCustomerPotentialDatasDao dao = new EhCustomerPotentialDatasDao(context.configuration());
        return ConvertHelper.convert(dao.findById(sourceId), CustomerTalent.class);
    }

    @Override
    public List<CustomerAdminRecord> listEnterpriseCustomerAdminRecordsByToken(Long id, String adminToken) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
       return  context.selectFrom(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS)
                .where(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CONTACT_TOKEN.eq(adminToken))
                .and(Tables.EH_ENTERPRISE_CUSTOMER_ADMINS.CUSTOMER_ID.eq(id))
                .fetchInto(CustomerAdminRecord.class);
    }


	@Override
	public List<CustomerEntryInfo> findActiveCustomerEntryInfoByAddressId(Long addressId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerEntryInfosRecord> query = context.selectQuery(Tables.EH_CUSTOMER_ENTRY_INFOS);
        
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.ADDRESS_ID.eq(addressId));
        query.addConditions(Tables.EH_CUSTOMER_ENTRY_INFOS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        return query.fetchInto(CustomerEntryInfo.class);
    }

    @Override
    public void updateCustomerAptitudeFlag(Long id, Long approvalStatus){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        EhEnterpriseCustomers eepc = dao.findById(id);
        eepc.setAptitudeFlagItemId(approvalStatus);
        dao.update(eepc);

    }

    @Override
    public Timestamp getCustomerMaxTrackingTime(Long customerId, Byte customerSource) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(DSL.max(Tables.EH_CUSTOMER_TRACKINGS.TRACKING_TIME))
                .from(Tables.EH_CUSTOMER_TRACKINGS)
                .where(Tables.EH_CUSTOMER_TRACKINGS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                .and(Tables.EH_CUSTOMER_TRACKINGS.CUSTOMER_SOURCE.eq(customerSource))
                .fetchAnyInto(Timestamp.class);
    }

    @Override
    public  List<CreateOrganizationAdminCommand> getOrganizationAdmin(Long nextPageAnchor){
        List<CreateOrganizationAdminCommand> dtoList = new ArrayList<>();
        if(nextPageAnchor == null){
            nextPageAnchor = 0l;
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.select(
                Tables.EH_ORGANIZATION_MEMBERS.ID,
                Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME,
                Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN,
                Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID,
                Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID)
                .from(Tables.EH_ORGANIZATION_MEMBERS,Tables.EH_ORGANIZATIONS)
                .where(Tables.EH_ORGANIZATION_MEMBERS.MEMBER_GROUP.eq("manager"))
                .and(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq((byte)3))
                .and(Tables.EH_ORGANIZATION_MEMBERS.GROUP_TYPE.eq("ENTERPRISE"))
                .and(Tables.EH_ORGANIZATIONS.STATUS.eq((byte)2))
                .and(Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID))
                .and(Tables.EH_ORGANIZATION_MEMBERS.ID.ge(nextPageAnchor))
                .orderBy(Tables.EH_ORGANIZATION_MEMBERS.ID)
                .limit(101)
                .fetch().map(r->{
                    CreateOrganizationAdminCommand dto = new CreateOrganizationAdminCommand();
                    dto.setContactName(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_NAME));
                    dto.setOrganizationId(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.ORGANIZATION_ID));
                    dto.setContactToken(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.CONTACT_TOKEN));
                    dto.setNamespaceId(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.NAMESPACE_ID));
                    dto.setOwnerId(r.getValue(Tables.EH_ORGANIZATION_MEMBERS.ID));
                    dtoList.add(dto);
                    return null;
                });
        return dtoList;
    }
}
