package com.everhomes.sms;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhYzxSmsLogsDao;
import com.everhomes.server.schema.tables.pojos.EhYzxSmsLogs;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Repository
public class YzxSmsLogProviderImpl implements YzxSmsLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createYzxSmsLog(YzxSmsLog log) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhYzxSmsLogs.class));
        log.setId(id);
        rwDao().insert(log);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhYzxSmsLogs.class, id);
    }

    @Override
    public void updateYzxSmsLog(YzxSmsLog log) {
        rwDao().update(log);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhYzxSmsLogs.class, log.getId());
    }

    @Override
    public YzxSmsLog findBySmsId(String smsId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_YZX_SMS_LOGS)
                .where(Tables.EH_YZX_SMS_LOGS.SMS_ID.eq(smsId))
                .fetchAnyInto(YzxSmsLog.class);
    }

    @Override
    public List<YzxSmsLog> listReportLogs(Integer namespaceId, String mobile, Byte status, Byte failure, ListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.select().from(Tables.EH_YZX_SMS_LOGS).getQuery();

        ifNotNull(namespaceId, () -> query.addConditions(Tables.EH_YZX_SMS_LOGS.NAMESPACE_ID.eq(namespaceId)));
        ifNotNull(mobile, () -> query.addConditions(Tables.EH_YZX_SMS_LOGS.MOBILE.eq(mobile)));
        ifNotNull(status, () -> query.addConditions(Tables.EH_YZX_SMS_LOGS.STATUS.eq(status)));
        ifNotNull(failure, () -> query.addConditions(Tables.EH_YZX_SMS_LOGS.FAILURE.eq(failure)));
        ifNotNull(locator.getAnchor(), () -> query.addConditions(Tables.EH_YZX_SMS_LOGS.ID.le(locator.getAnchor())));

        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_YZX_SMS_LOGS.ID.desc());

        List<YzxSmsLog> list = query.fetchInto(YzxSmsLog.class);
        if (list != null && list.size() > pageSize) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list = list.subList(0, pageSize);
        }
        return list;
    }

    private EhYzxSmsLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhYzxSmsLogsDao(context.configuration());
    }

    private void ifNotNull(Object condition, Callback callback) {
        if (condition != null) {
            callback.condition();
        }
    }

    private interface Callback {
        void condition();
    }
}
