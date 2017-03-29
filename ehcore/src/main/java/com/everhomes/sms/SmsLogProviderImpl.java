package com.everhomes.sms;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSmsLogsDao;
import com.everhomes.server.schema.tables.pojos.EhSmsLogs;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

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
    public void createSmsLog(SmsLog smsLog){
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhSmsLogs.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhSmsLogsDao dao = new EhSmsLogsDao(context.configuration());
        smsLog.setId(id);
        smsLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dao.insert(smsLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSmsLogs.class, null);
    }

    @Override
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
    }
}
