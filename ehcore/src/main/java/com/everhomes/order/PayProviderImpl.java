//@formatter:off
package com.everhomes.order;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPaymentOrderRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentOrderRecords;
import com.everhomes.server.schema.tables.records.EhPaymentAccountsRecord;
import com.everhomes.server.schema.tables.records.EhPaymentOrderRecordsRecord;
import com.everhomes.server.schema.tables.records.EhPaymentTypesRecord;
import com.everhomes.server.schema.tables.records.EhPaymentUsersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PayProviderImpl implements PayProvider {
    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public PaymentOrderRecord findOrderRecordByOrder(String orderType, Long orderId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentOrderRecordsRecord>  query = context.selectQuery(Tables.EH_PAYMENT_ORDER_RECORDS);
        query.addConditions(Tables.EH_PAYMENT_ORDER_RECORDS.ORDER_ID.eq(orderId));
        query.addConditions(Tables.EH_PAYMENT_ORDER_RECORDS.ORDER_TYPE.eq(orderType));
        return query.fetchOneInto(PaymentOrderRecord.class);
    }

    @Override
    public PaymentUser findPaymentUserByOwner(String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentUsersRecord>  query = context.selectQuery(Tables.EH_PAYMENT_USERS);
        query.addConditions(Tables.EH_PAYMENT_USERS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PAYMENT_USERS.OWNER_ID.eq(ownerId));
        return query.fetchOneInto(PaymentUser.class);
    }

    @Override
    public PaymentAccount findPaymentAccountBySystemId(Integer systemId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentAccountsRecord>  query = context.selectQuery(Tables.EH_PAYMENT_ACCOUNTS);
        if(systemId != null){
            query.addConditions(Tables.EH_PAYMENT_ACCOUNTS.SYSTEM_ID.eq(systemId));
        }
        return query.fetchOneInto(PaymentAccount.class);
    }

    @Override
    public void createPaymentOrderRecord(PaymentOrderRecord orderRecord) {

        //下预付单时，BizOrderNum需要传PaymentOrderRecords表记录的id，此处先申请id，在返回值中使用BizOrderNum做为record的id
        if(orderRecord.getId() == null){
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentOrderRecords.class));
            orderRecord.setId(id);
        }

        if(orderRecord.getCreateTime() == null){
            orderRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentOrderRecordsDao dao = new EhPaymentOrderRecordsDao(context.configuration());
        dao.insert(orderRecord);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentOrderRecords.class, null);
    }

    @Override
    public Long getNewPaymentOrderRecordId(){
        return sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentOrderRecords.class));
    }

    @Override
    public List<PayMethodDTO> listPayMethods(Integer namespaceId, String orderType, String ownerType, Long ownerId, String resourceType, Long resourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentTypesRecord>  query = context.selectQuery(Tables.EH_PAYMENT_TYPES);

        if(namespaceId != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.NAMESPACE_ID.eq(namespaceId));
        }
        if(orderType != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.ORDER_TYPE.eq(orderType));
        }
        if(ownerType != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.OWNER_TYPE.eq(ownerType));
        }
        if(ownerId != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.OWNER_ID.eq(ownerId));
        }
        if(resourceType != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.RESOURCE_TYPE.eq(resourceType));
        }
        if(resourceId != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.RESOURCE_ID.eq(resourceId));
        }
        List<PayMethodDTO> payMethodDTOS = new ArrayList<>();

        query.fetch().map(r -> {
            payMethodDTOS.add(ConvertHelper.convert(r, PayMethodDTO.class));
            return null;
        });

        return payMethodDTOS;
    }
}
