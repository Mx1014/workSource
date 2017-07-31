package com.everhomes.community_approve;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;

import com.everhomes.server.schema.tables.daos.EhCommunityApproveRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityApproveRequests;
import com.everhomes.server.schema.tables.records.EhCommunityApproveRequestsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
@Component
public class CommunityApproveValProviderImpl implements CommunityApproveValProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createCommunityApproveVal(CommunityApproveRequests obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCommunityApproveRequests.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApproveRequests.class));
        obj.setId(id);
        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhCommunityApproveRequestsDao dao = new EhCommunityApproveRequestsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateCommunityApproveVal(CommunityApproveRequests obj) {

    }

    @Override
    public void deleteCommunityApproveVal(CommunityApproveRequests obj) {

    }

    @Override
    public CommunityApproveRequests getCommunityApproveValByFlowCaseId(Long id) {
        try {
            CommunityApproveRequests[] result = new CommunityApproveRequests[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApproveRequests.class));
            result[0] = context.select().from(Tables.EH_COMMUNITY_APPROVE_REQUESTS).
                    where(Tables.EH_COMMUNITY_APPROVE_REQUESTS.FLOW_CASE_ID.eq(id)).
                    fetchAny().map((r)->{
                        return ConvertHelper.convert(r,CommunityApproveRequests.class);
                    });
        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public CommunityApproveRequests getCommunityApproveValById(Long id) {
        return null;
    }

    @Override
    public List<CommunityApproveRequests> queryCommunityApproves(ListingLocator locator, int count,
                                                                 ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApproveRequests.class));
        SelectQuery<EhCommunityApproveRequestsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_APPROVE_REQUESTS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_COMMUNITY_APPROVE_REQUESTS.ID.gt(locator.getAnchor()));
        }
        query.addLimit(count);
        List<CommunityApproveRequests> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, CommunityApproveRequests.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
}
