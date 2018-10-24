package com.everhomes.rentalv2;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalv2OrderRecordsDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2PayAccountsDao;
import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderRecords;
import com.everhomes.server.schema.tables.pojos.EhRentalv2PayAccounts;
import com.everhomes.server.schema.tables.records.EhRentalv2OrderRecordsRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2PayAccountsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class Rentalv2AccountProviderImpl implements  Rentalv2AccountProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<Rentalv2PayAccount> listPayAccounts(Integer namespaceId, Long communityId, String resourceType, Long resourceTypeId,String sourceType, Long sourceId,
                                                    ListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_PAY_ACCOUNTS);
        Condition condition = Tables.EH_RENTALV2_PAY_ACCOUNTS.COMMUNITY_ID.eq(communityId);
        if (null != namespaceId)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.NAMESPACE_ID.eq(namespaceId));
        if (!StringUtils.isBlank(resourceType))
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.RESOURCE_TYPE.eq(resourceType));
        if (null != resourceTypeId)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.RESOURCE_TYPE_ID.eq(resourceTypeId));
        if(!StringUtils.isBlank(sourceType))
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_TYPE.eq(sourceType));
        if (null != sourceId)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_ID.eq(sourceId));
        if (null != locator)
            condition = condition.and(Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.lt(locator.getAnchor()));
        step.where(condition);
        if (pageSize != null)
            step.limit(pageSize);

        return step.orderBy(Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.desc()).fetch().map(r->ConvertHelper.convert(r,Rentalv2PayAccount.class));
    }

    @Override
    public void deletePayAccount(Long id,Long communityId, String sourceType, Long sourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhRentalv2PayAccountsRecord> step = context.delete(Tables.EH_RENTALV2_PAY_ACCOUNTS);
        Condition condition ;
        if (id != null)
            condition = Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.eq(id);
        else{
            condition = Tables.EH_RENTALV2_PAY_ACCOUNTS.COMMUNITY_ID.eq(communityId).and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_TYPE.eq(sourceType))
                    .and(Tables.EH_RENTALV2_PAY_ACCOUNTS.SOURCE_ID.eq(sourceId));
        }
        step.where(condition).execute();
    }

    @Override
    public void createPayAccount(Rentalv2PayAccount account) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhRentalv2PayAccounts.class));
        account.setId(id);
        account.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRentalv2PayAccountsRecord record = ConvertHelper.convert(account,
                EhRentalv2PayAccountsRecord.class);
        InsertQuery<EhRentalv2PayAccountsRecord> query = context
                .insertQuery(Tables.EH_RENTALV2_PAY_ACCOUNTS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2PayAccounts.class,
                null);
    }

    @Override
    public Rentalv2PayAccount getAccountById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_RENTALV2_PAY_ACCOUNTS).where(Tables.EH_RENTALV2_PAY_ACCOUNTS.ID.eq(id)).fetchOne();
        if (record == null)
            return null;
        return ConvertHelper.convert(record,Rentalv2PayAccount.class);
    }

    @Override
    public void updatePayAccount(Rentalv2PayAccount account) {
        assert (account.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRentalv2PayAccountsDao dao = new EhRentalv2PayAccountsDao(context.configuration());
        dao.update(account);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2PayAccounts.class,
                account.getId());
    }

    @Override
    public void createOrderRecord(Rentalv2OrderRecord record) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhRentalv2OrderRecords.class));
        record.setId(id);
        record.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRentalv2OrderRecordsRecord record1 = ConvertHelper.convert(record,
                EhRentalv2OrderRecordsRecord.class);
        InsertQuery<EhRentalv2OrderRecordsRecord> query = context
                .insertQuery(Tables.EH_RENTALV2_ORDER_RECORDS);
        query.setRecord(record1);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2OrderRecords.class,
                null);
    }

    @Override
    public void updateOrderRecord(Rentalv2OrderRecord record) {
        assert (record.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhRentalv2OrderRecordsDao dao = new EhRentalv2OrderRecordsDao(context.configuration());
        dao.update(record);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2OrderRecords.class,
                record.getId());
    }

    @Override
    public Rentalv2OrderRecord getOrderRecordByOrderNo(Long orderNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_RENTALV2_ORDER_RECORDS).where(Tables.EH_RENTALV2_ORDER_RECORDS.ORDER_NO.eq(orderNo)).fetchOne();
        if (record == null)
            return null;
        return ConvertHelper.convert(record,Rentalv2OrderRecord.class);
    }

    @Override
    public Rentalv2OrderRecord getOrderRecordByBizOrderNo(String bizOrderNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_RENTALV2_ORDER_RECORDS).where(Tables.EH_RENTALV2_ORDER_RECORDS.BIZ_ORDER_NUM.eq(bizOrderNo)).fetchOne();
        if (record == null)
            return null;
        return ConvertHelper.convert(record,Rentalv2OrderRecord.class);
    }

    @Override
    public Rentalv2OrderRecord getOrderRecordByMerchantOrderId(Long merchantOrderId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = context.select().from(Tables.EH_RENTALV2_ORDER_RECORDS).where(Tables.EH_RENTALV2_ORDER_RECORDS.MERCHANT_ORDER_ID.eq(merchantOrderId)).fetchOne();
        if (record == null)
            return null;
        return ConvertHelper.convert(record,Rentalv2OrderRecord.class);
    }

    @Override
    public void deleteOrderRecordByOrderNo(Long orderNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteWhereStep<EhRentalv2OrderRecordsRecord> step = context
                .delete(Tables.EH_RENTALV2_ORDER_RECORDS);
        Condition condition = Tables.EH_RENTALV2_ORDER_RECORDS.ORDER_NO
                .equal(orderNo);
        step.where(condition);
        step.execute();
    }
}
