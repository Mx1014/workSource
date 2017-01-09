package com.everhomes.general_approval;

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
import com.everhomes.server.schema.tables.daos.EhGeneralApprovalsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovals;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class GeneralApprovalProviderImpl implements GeneralApprovalProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createGeneralApproval(GeneralApproval obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralApprovals.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));
        obj.setId(id);
        prepareObj(obj);
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateGeneralApproval(GeneralApproval obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteGeneralApproval(GeneralApproval obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public GeneralApproval getGeneralApprovalById(Long id) {
        try {
        GeneralApproval[] result = new GeneralApproval[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));

        result[0] = context.select().from(Tables.EH_GENERAL_APPROVALS)
            .where(Tables.EH_GENERAL_APPROVALS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, GeneralApproval.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<GeneralApproval> queryGeneralApprovals(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));

        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_GENERAL_APPROVALS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<GeneralApproval> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, GeneralApproval.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(GeneralApproval obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        obj.setUpdateTime(new Timestamp(l2));
    }
}
