package com.everhomes.message;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhMessageRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhMessageRecords;
import com.everhomes.server.schema.tables.records.EhMessageRecordsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageProviderImpl implements MessageProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createMessageRecord(MessageRecord messageRecord) {
        messageRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMessageRecords.class));
        messageRecord.setId(id);
        EhMessageRecordsDao dao = new EhMessageRecordsDao(context.configuration());
        dao.insert(messageRecord);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMessageRecords.class, messageRecord.getId());
    }

    @Override
    public void createMessageRecords(List<MessageRecord> messageRecords) {
        List<EhMessageRecords> records = new ArrayList<>();
        messageRecords.forEach(r->{
            r.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMessageRecords.class));
            r.setId(id);
            records.add(ConvertHelper.convert(r, EhMessageRecords.class));
        });
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhMessageRecordsDao dao = new EhMessageRecordsDao(context.configuration());
        dao.insert(records);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhMessageRecords.class, null);
    }

    @Override
    public void updateMessageRecord(MessageRecord messageRecord) {
        assert (messageRecord.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhMessageRecordsDao dao = new EhMessageRecordsDao(context.configuration());
        dao.update(messageRecord);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMessageRecords.class, messageRecord.getId());
    }

    @Override
    public void deleteMessageRecordById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhMessageRecordsDao dao = new EhMessageRecordsDao(context.configuration());
        dao.deleteById(id);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhMessageRecords.class, id);
    }

    @Override
    public List<MessageRecord> listMessageRecords(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhMessageRecordsRecord> query = context.selectQuery(Tables.EH_MESSAGE_RECORDS);
        query.addConditions(Tables.EH_MESSAGE_RECORDS.NAMESPACE_ID.eq(namespaceId));

        return query.fetch().map(r->{
           return ConvertHelper.convert(r,MessageRecord.class);
        });
    }

    @Override
    public Long getMaxMessageIndexId() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Long index_id = context.select(Tables.EH_MESSAGE_RECORDS.INDEX_ID.max()).from(Tables.EH_MESSAGE_RECORDS).fetchOne().value1();
        return index_id != null ? index_id : 0;
    }
}
