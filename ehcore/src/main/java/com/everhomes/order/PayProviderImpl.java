//@formatter:off
package com.everhomes.order;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.pay.order.PaymentDTO;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPaymentOrderRecordsDao;
import com.everhomes.server.schema.tables.daos.EhPaymentUsersDao;
import com.everhomes.server.schema.tables.daos.EhPaymentWithdrawOrdersDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentOrderRecords;
import com.everhomes.server.schema.tables.pojos.EhPaymentUsers;
import com.everhomes.server.schema.tables.pojos.EhPaymentWithdrawOrders;
import com.everhomes.server.schema.tables.records.EhPaymentAccountsRecord;
import com.everhomes.server.schema.tables.records.EhPaymentOrderRecordsRecord;
import com.everhomes.server.schema.tables.records.EhPaymentTypesRecord;
import com.everhomes.server.schema.tables.records.EhPaymentUsersRecord;
import com.everhomes.server.schema.tables.records.EhPaymentWithdrawOrdersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
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
    public PaymentOrderRecord findOrderRecordById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentOrderRecordsRecord>  query = context.selectQuery(Tables.EH_PAYMENT_ORDER_RECORDS);
        query.addConditions(Tables.EH_PAYMENT_ORDER_RECORDS.ID.eq(id));
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
    public void createPaymentUser(PaymentUser paymentUser) {

        //下预付单时，BizOrderNum需要传PaymentOrderRecords表记录的id，此处先申请id，在返回值中使用BizOrderNum做为record的id
        if(paymentUser.getId() == null){
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentUsers.class));
            paymentUser.setId(id);
        }

        if(paymentUser.getCreateTime() == null){
            paymentUser.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentUsersDao dao = new EhPaymentUsersDao(context.configuration());
        dao.insert(paymentUser);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentUsers.class, null);
    }

    @Override
    public Long getNewPaymentUserId(){
        return sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentUsers.class));
    }

    @Override
    public List<PayMethodDTO> listPayMethods(Integer namespaceId, Integer paymentType, String orderType, String ownerType, Long ownerId, String resourceType, Long resourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentTypesRecord>  query = context.selectQuery(Tables.EH_PAYMENT_TYPES);

        if(namespaceId != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.NAMESPACE_ID.eq(namespaceId));
        }

        //1、可以指定支付方式，2、不指定支付方式则是所有非微信公众号支付，因为微信公众号支付必须指定支付方式
        if(paymentType != null){
            query.addConditions(Tables.EH_PAYMENT_TYPES.PAYMENT_TYPE.eq(paymentType));
        }else{
            query.addConditions(Tables.EH_PAYMENT_TYPES.PAYMENT_TYPE.ne(PaymentType.WECHAT_JS_PAY.getCode()));
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
            PayMethodDTO dto = ConvertHelper.convert(r, PayMethodDTO.class);
            PaymentParamsDTO paymentParamsDTO = (PaymentParamsDTO)StringHelper.fromJsonString(r.getPaymentparams(), PaymentParamsDTO.class);
            dto.setPaymentParams(paymentParamsDTO);
            payMethodDTOS.add(dto);
            return null;
        });

        return payMethodDTOS;
    }

    @Override
    public PaymentOrderRecord findOrderRecordByOrderNum(String bizOrderNum) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentOrderRecordsRecord>  query = context.selectQuery(Tables.EH_PAYMENT_ORDER_RECORDS);
        query.addConditions(Tables.EH_PAYMENT_ORDER_RECORDS.ORDER_NUM.eq(bizOrderNum));
        return query.fetchOneInto(PaymentOrderRecord.class);
    }
    
    @Override
    public void createPaymentWithdrawOrder(PaymentWithdrawOrder order) {
        if(order.getId() == null){
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentWithdrawOrders.class));
            order.setId(id);
        }

        if(order.getCreateTime() == null){
            order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentWithdrawOrdersDao dao = new EhPaymentWithdrawOrdersDao(context.configuration());
        dao.insert(order);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentWithdrawOrders.class, order.getId());
    }
    
    @Override
    public List<PaymentWithdrawOrder> listPaymentWithdrawOrders(String ownerType, Long ownerId, 
            Long pageAnchor, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentWithdrawOrdersRecord>  query = context.selectQuery(Tables.EH_PAYMENT_WITHDRAW_ORDERS);
        query.addConditions(Tables.EH_PAYMENT_WITHDRAW_ORDERS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PAYMENT_WITHDRAW_ORDERS.OWNER_ID.eq(ownerId));
        
        if(pageAnchor != null) {
            query.addConditions(Tables.EH_PAYMENT_WITHDRAW_ORDERS.ID.lt(pageAnchor));
        }
        query.addOrderBy(Tables.EH_PAYMENT_WITHDRAW_ORDERS.ID.desc());
        query.addLimit(pageSize);
        
        List<PaymentWithdrawOrder> orderList = new ArrayList<>();

        query.fetch().map(r -> {
            PaymentWithdrawOrder order = ConvertHelper.convert(r, PaymentWithdrawOrder.class);
            orderList.add(order);
            return null;
        });

        return orderList;
    }
    
    @Override
    public PaymentWithdrawOrder findPaymentWithdrawOrderByOrderNo(Long orderNo) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_PAYMENT_WITHDRAW_ORDERS)
                .where(Tables.EH_PAYMENT_WITHDRAW_ORDERS.ID.eq(orderNo));
        List<PaymentWithdrawOrder> list  = query.fetch().map((r)->{
            return ConvertHelper.convert(r, PaymentWithdrawOrder.class);
        });
        
        if(list !=null && list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public void updatePaymentWithdrawOrder(PaymentWithdrawOrder order) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentWithdrawOrdersDao dao = new EhPaymentWithdrawOrdersDao(context.configuration());
        dao.update(order);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPaymentWithdrawOrders.class, order.getId());
    }
}
