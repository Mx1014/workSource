// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.enterprisepaymentauth.FrozenStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthFrozenRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthFrozenRequests;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthFrozenRequestsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterprisePaymentAuthFrozenRequestProviderImpl implements EnterprisePaymentAuthFrozenRequestProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createEnterprisePaymentAuthRequest(EnterprisePaymentAuthFrozenRequest enterprisePaymentAuthFrozenRequest) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthFrozenRequests.class));
        enterprisePaymentAuthFrozenRequest.setId(id);
        enterprisePaymentAuthFrozenRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        getReadWriteDao().insert(enterprisePaymentAuthFrozenRequest);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthFrozenRequests.class, null);
    }

    @Override
    public void updateEnterprisePaymentAuthRequest(EnterprisePaymentAuthFrozenRequest enterprisePaymentAuthFrozenRequest) {
        assert (enterprisePaymentAuthFrozenRequest.getId() != null);
        enterprisePaymentAuthFrozenRequest.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        getReadWriteDao().update(enterprisePaymentAuthFrozenRequest);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthFrozenRequests.class, enterprisePaymentAuthFrozenRequest.getId());
    }

    @Override
    public void deleteEnterprisePaymentAuthRequest(EnterprisePaymentAuthFrozenRequest enterprisePaymentAuthFrozenRequest) {
        getReadWriteDao().delete(enterprisePaymentAuthFrozenRequest);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthFrozenRequests.class, enterprisePaymentAuthFrozenRequest.getId());
    }

    @Override
    public EnterprisePaymentAuthFrozenRequest findEnterprisePaymentAuthRequestByOrderId(Integer namespaceId, Long organizationId, Long userId, Long paymentSceneAppId, Long orderId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterprisePaymentAuthFrozenRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS);
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.PAYMENT_SCENE_APP_ID.eq(paymentSceneAppId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.MERCHANT_ORDER_ID.eq(orderId));
        query.addLimit(1);

        EhEnterprisePaymentAuthFrozenRequestsRecord record = query.fetchOne();
        return ConvertHelper.convert(record, EnterprisePaymentAuthFrozenRequest.class);
    }

    @Override
    public List<EnterprisePaymentAuthFrozenRequest> findEnterprisePaymentAuthRequestsByFrozened(int count, Timestamp betweenFrom, Timestamp betweenTo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterprisePaymentAuthFrozenRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS);
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.STATUS.eq(FrozenStatus.FROZEN.getCode()));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_FROZEN_REQUESTS.CREATE_TIME.between(betweenFrom).and(betweenTo));
        query.addLimit(count);

        Result<EhEnterprisePaymentAuthFrozenRequestsRecord> result = query.fetch();
        if (result == null || result.size() == 0) {
            return new ArrayList<>();
        }
        return result.map(record -> {
            return ConvertHelper.convert(record, EnterprisePaymentAuthFrozenRequest.class);
        });
    }

    private EhEnterprisePaymentAuthFrozenRequestsDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhEnterprisePaymentAuthFrozenRequestsDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhEnterprisePaymentAuthFrozenRequestsDao getDao(DSLContext context) {
        return new EhEnterprisePaymentAuthFrozenRequestsDao(context.configuration());
    }

    private DSLContext getReadWriteContext() {
        return getContext(AccessSpec.readWrite());
    }

    private DSLContext getReadOnlyContext() {
        return getContext(AccessSpec.readOnly());
    }

    private DSLContext getContext(AccessSpec accessSpec) {
        return dbProvider.getDslContext(accessSpec);
    }
}
