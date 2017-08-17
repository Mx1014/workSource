package com.everhomes.customer;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.quality.QualityInspectionSampleCommunityMap;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCustomersDao;
import com.everhomes.server.schema.tables.records.EhEnterpriseCustomersRecord;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
}
