package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowSubjectsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowSubjects;
import com.everhomes.server.schema.tables.records.EhFlowSubjectsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class FlowSubjectProviderImpl implements FlowSubjectProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowSubject(FlowSubject obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowSubjects.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowSubjects.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowSubjectsDao dao = new EhFlowSubjectsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlowSubject(FlowSubject obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowSubjects.class));
        EhFlowSubjectsDao dao = new EhFlowSubjectsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowSubject(FlowSubject obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowSubjects.class));
        EhFlowSubjectsDao dao = new EhFlowSubjectsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowSubject getFlowSubjectById(Long id) {
        try {
        FlowSubject[] result = new FlowSubject[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowSubjects.class));

        result[0] = context.select().from(Tables.EH_FLOW_SUBJECTS)
            .where(Tables.EH_FLOW_SUBJECTS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowSubject.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowSubject> queryFlowSubjects(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowSubjects.class));

        SelectQuery<EhFlowSubjectsRecord> query = context.selectQuery(Tables.EH_FLOW_SUBJECTS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_SUBJECTS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<FlowSubject> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowSubject.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowSubject obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
