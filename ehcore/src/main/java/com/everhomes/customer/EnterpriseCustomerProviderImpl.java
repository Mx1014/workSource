package com.everhomes.customer;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.quality.QualityInspectionSampleCommunityMap;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.CustomerProjectStatisticsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void createEnterpriseCustomer(EnterpriseCustomer customer) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseCustomers.class));
        customer.setId(id);
        customer.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        customer.setCreatorUid(UserContext.current().getUser().getId());
        customer.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createEnterpriseCustomer: " + customer);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, id));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        dao.insert(customer);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseCustomers.class, null);
    }

    @Override
    public void updateEnterpriseCustomer(EnterpriseCustomer customer) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, customer.getId()));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());

        customer.setOperatorUid(UserContext.current().getUser().getId());
        customer.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(customer);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
    }

    @Override
    public void deleteEnterpriseCustomer(EnterpriseCustomer customer) {
        assert(customer.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseCustomers.class, customer.getId()));
        EhEnterpriseCustomersDao dao = new EhEnterpriseCustomersDao(context.configuration());
        dao.delete(customer);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseCustomers.class, customer.getId());
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
    public List<EnterpriseCustomer> listEnterpriseCustomersByIds(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);
        query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.ID.in(ids));

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
    public void createCustomerTalent(CustomerTalent talent) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCustomerTalents.class));
        talent.setId(id);
        talent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        talent.setCreatorUid(UserContext.current().getUser().getId());
        talent.setStatus(CommonStatus.ACTIVE.getCode());

        LOGGER.info("createCustomerTalent: " + talent);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTalents.class, id));
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
    public void updateCustomerTalent(CustomerTalent talent) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerTalents.class, talent.getId()));
        EhCustomerTalentsDao dao = new EhCustomerTalentsDao(context.configuration());

        talent.setOperatorUid(UserContext.current().getUser().getId());
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
        project.setStatus(CommonStatus.ACTIVE.getCode());

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

        LOGGER.info("createCustomerEconomicIndicator: " + economicIndicator);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicators.class, id));
        EhCustomerEconomicIndicatorsDao dao = new EhCustomerEconomicIndicatorsDao(context.configuration());
        dao.insert(economicIndicator);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCustomerEconomicIndicators.class, null);
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
        query.addConditions(Tables.EH_CUSTOMER_APPLY_PROJECTS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

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
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerEconomicIndicators.class, economicIndicator.getId()));
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
}
