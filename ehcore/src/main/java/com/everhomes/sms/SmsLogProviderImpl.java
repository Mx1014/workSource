package com.everhomes.sms;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSmsLogsDao;
import com.everhomes.server.schema.tables.pojos.EhSmsLogs;
import com.everhomes.server.schema.tables.records.EhSmsLogsRecord;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */
@Component
public class SmsLogProviderImpl implements SmsLogProvider {

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createSmsLog(SmsLog smsLog) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSmsLogs.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhSmsLogsDao dao = new EhSmsLogsDao(context.configuration());
        smsLog.setId(id);
        smsLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dao.insert(smsLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSmsLogs.class, id);
    }

    /*@Override
    public List<SmsLog> listSmsLogs(Integer namespaceId, String mobile, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhSmsLogs.class));

        SelectJoinStep<Record> query = context.select(Tables.EH_SMS_LOGS.fields()).from(Tables.EH_SMS_LOGS);
        Condition condition = Tables.EH_SMS_LOGS.NAMESPACE_ID.eq(namespaceId);

        if(StringUtils.isNotBlank(mobile))
            condition = condition.and(Tables.EH_SMS_LOGS.MOBILE.eq(mobile));

        if(null != pageAnchor)
            condition = condition.and(Tables.EH_SMS_LOGS.ID.gt(pageAnchor));
        query.orderBy(Tables.EH_SMS_LOGS.ID.asc());
        if(null != pageSize)
            query.limit(pageSize);

        return query.where(condition).fetch().map(new DefaultRecordMapper(Tables.EH_SMS_LOGS.recordType(), SmsLog.class));
    }*/

    @Override
    public Map<String, SmsLog> findLastLogByMobile(Integer namespaceId, String mobile, String[] handlerNames) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhSmsLogs.class));
        com.everhomes.server.schema.tables.EhSmsLogs t = Tables.EH_SMS_LOGS;

        Table<EhSmsLogsRecord> subT = context.selectFrom(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.MOBILE.eq(mobile))
                .and(t.HANDLER.in(handlerNames))
                .orderBy(t.ID.desc()).asTable();

        return context.selectFrom(subT)
                .groupBy(subT.field(t.HANDLER))
                .fetchMap(t.HANDLER, SmsLog.class);
    }

    @Override
    public List<SmsLog> findSmsLog(String handler, String mobile, String smsId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhSmsLogs.class));
        SelectQuery<EhSmsLogsRecord> query = context.selectFrom(Tables.EH_SMS_LOGS).getQuery();

        ifNotNull(handler, () -> query.addConditions(Tables.EH_SMS_LOGS.HANDLER.eq(handler)));
        ifNotNull(mobile, () -> query.addConditions(Tables.EH_SMS_LOGS.MOBILE.eq(mobile)));
        ifNotNull(smsId, () -> query.addConditions(Tables.EH_SMS_LOGS.SMS_ID.eq(smsId)));

        return query.fetchInto(SmsLog.class);
    }

    @Override
    public void updateSmsLog(SmsLog smsLog) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhSmsLogsDao dao = new EhSmsLogsDao(context.configuration());
        dao.update(smsLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSmsLogs.class, smsLog.getId());
    }

    @Override
    public List<SmsLog> listSmsLogs(Integer namespaceId, String handler, String mobile, Byte status, int pageSize, ListingLocator locator) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.select().from(Tables.EH_SMS_LOGS).getQuery();

        ifNotNull(namespaceId, () -> query.addConditions(Tables.EH_SMS_LOGS.NAMESPACE_ID.eq(namespaceId)));
        ifNotNull(handler, () -> query.addConditions(Tables.EH_SMS_LOGS.HANDLER.eq(handler)));
        ifNotNull(mobile, () -> query.addConditions(Tables.EH_SMS_LOGS.MOBILE.eq(mobile)));
        ifNotNull(status, () -> query.addConditions(Tables.EH_SMS_LOGS.STATUS.eq(status)));
        ifNotNull(locator.getAnchor(), () -> query.addConditions(Tables.EH_SMS_LOGS.ID.le(locator.getAnchor())));

        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_SMS_LOGS.ID.desc());

        List<SmsLog> list = query.fetchInto(SmsLog.class);
        if (list != null && list.size() > pageSize) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list = list.subList(0, pageSize);
        }
        return list;
    }

    private void ifNotNull(Object condition, Callback callback) {
        if (condition instanceof String && condition.toString().trim().length() > 0) {
            callback.condition();
        } else if (condition != null) {
            callback.condition();
        }
    }

    private interface Callback {
        void condition();
    }
}
