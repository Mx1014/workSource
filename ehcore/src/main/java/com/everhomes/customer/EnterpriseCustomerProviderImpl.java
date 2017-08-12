package com.everhomes.customer;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.quality.QualityInspectionSampleCommunityMap;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.EhEnterpriseCustomersDao;
import com.everhomes.server.schema.tables.records.EhEnterpriseCustomersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/11.
 */
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
}
