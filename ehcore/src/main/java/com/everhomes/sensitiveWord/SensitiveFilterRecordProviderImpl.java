// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSensitiveFilterRecordDao;
import com.everhomes.server.schema.tables.pojos.EhSensitiveFilterRecord;
import com.everhomes.server.schema.tables.records.EhSensitiveFilterRecordRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensitiveFilterRecordProviderImpl implements SensitiveFilterRecordProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSensitiveFilterRecord(SensitiveFilterRecord sensitiveFilterRecord) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhSensitiveFilterRecord.class));
        sensitiveFilterRecord.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhSensitiveFilterRecordDao dao = new EhSensitiveFilterRecordDao(context.configuration());
        dao.insert(sensitiveFilterRecord);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSensitiveFilterRecord.class, null);
    }

    @Override
    public List<SensitiveFilterRecord> listSensitiveFilterRecord(Integer namespaceId, Long communityId, Long pageAnchor, Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhSensitiveFilterRecord.class));
        SelectQuery<EhSensitiveFilterRecordRecord> query = context.selectQuery(Tables.EH_SENSITIVE_FILTER_RECORD);

        if (null != namespaceId) {
            query.addConditions(Tables.EH_SENSITIVE_FILTER_RECORD.NAMESPACE_ID.eq(namespaceId));
        }
        if (null != communityId) {
            query.addConditions(Tables.EH_SENSITIVE_FILTER_RECORD.COMMUNITY_ID.eq(communityId));
        }
        if (null != pageAnchor && 0L != pageAnchor) {
            query.addConditions(Tables.EH_SENSITIVE_FILTER_RECORD.ID.lt(pageAnchor));
        }
        if (null != pageSize) {
            query.addLimit(pageSize+1);
        }
        query.addOrderBy(Tables.EH_SENSITIVE_FILTER_RECORD.ID.desc());
        return query.fetch().map(r -> ConvertHelper.convert(r, SensitiveFilterRecord.class));
    }

    @Override
    public SensitiveFilterRecord getSensitiveFilterRecord(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhSensitiveFilterRecord.class));
        EhSensitiveFilterRecordDao dao = new EhSensitiveFilterRecordDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), SensitiveFilterRecord.class);
    }
}
