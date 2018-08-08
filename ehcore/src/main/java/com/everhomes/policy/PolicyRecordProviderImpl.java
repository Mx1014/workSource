package com.everhomes.policy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPolicyRecords;
import com.everhomes.server.schema.tables.daos.EhPolicyRecordsDao;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PolicyRecordProviderImpl implements PolicyRecordProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public PolicyRecord createPolicyRecord(PolicyRecord policyRecord) {
        if(policyRecord.getId() == null) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPolicyRecords.class));
            policyRecord.setId(id);
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyRecords.class));
        EhPolicyRecordsDao dao = new EhPolicyRecordsDao(context.configuration());
        policyRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(policyRecord);
        return policyRecord;
    }

    @Override
    public PolicyRecord searchPolicyRecordById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyRecords.class));
        EhPolicyRecordsDao dao = new EhPolicyRecordsDao(context.configuration());
        EhPolicyRecords result = dao.findById(id);
        return ConvertHelper.convert(result, PolicyRecord.class);
    }

    @Override
    public List<PolicyRecord> searchPolicyRecords(Integer namespaceId, String ownerType, List<Long> ownerIds, Long beginDate, Long endDate, Long category, String keyword, Long pageAnchor, Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.pojos.EhPolicyRecords.class));
        SelectQuery query = context.selectQuery(Tables.EH_POLICY_RECORDS);
        if(null != namespaceId)
            query.addConditions(Tables.EH_POLICY_RECORDS.NAMESPACE_ID.eq(namespaceId));
        if(StringUtils.isNotEmpty(ownerType))
            query.addConditions(Tables.EH_POLICY_RECORDS.OWNER_TYPE.eq(ownerType));
        if(ownerIds.size() > 0)
            query.addConditions(Tables.EH_POLICY_RECORDS.OWNER_ID.in(ownerIds));
        if(null != beginDate)
            query.addConditions(Tables.EH_POLICY_RECORDS.CREATE_TIME.gt(new Timestamp(beginDate)));
        if(null != endDate)
            query.addConditions(Tables.EH_POLICY_RECORDS.CREATE_TIME.lt(new Timestamp(endDate)));
        if(null != category)
            query.addConditions(Tables.EH_POLICY_RECORDS.CATEGORY_ID.eq(category));
        if(null != keyword){
            query.addConditions(Tables.EH_POLICY_RECORDS.CREATOR_NAME.like("%" + keyword + "%").or(Tables.EH_POLICY_RECORDS.CREATOR_PHONE.like("%" + keyword + "%").or(Tables.EH_POLICY_RECORDS.CREATOR_ORG_NAME.like("%" + keyword + "%"))));
        }
        if(null != pageAnchor)
            query.addConditions(Tables.EH_POLICY_RECORDS.ID.lt(pageAnchor));
        query.addOrderBy(Tables.EH_POLICY_RECORDS.ID.desc());
        query.addLimit(pageSize);
        return query.fetch().map(r->ConvertHelper.convert(r,PolicyRecord.class));

    }
}
