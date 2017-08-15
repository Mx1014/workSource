package com.everhomes.community_approve;

import com.everhomes.community_approve.CommunityApprove;
import com.everhomes.community_approve.CommunityApproveProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunityApproveDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityApprove;
import com.everhomes.server.schema.tables.records.EhCommunityApproveRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
@Component
public class CommunityApproveProviderImpl implements CommunityApproveProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createCommunityApprove(CommunityApprove obj) {
        return null;
    }

    @Override
    public void updateCommunityApprove(CommunityApprove obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApprove.class));
        EhCommunityApproveDao dao = new EhCommunityApproveDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteCommunityApprove(CommunityApprove obj) {

    }

    @Override
    public CommunityApprove getCommunityApproveById(Long id) {
        try{
            CommunityApprove [] result = new CommunityApprove[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApprove.class));
            result[0] = context.select().from(Tables.EH_COMMUNITY_APPROVE).
                    where(Tables.EH_COMMUNITY_APPROVE.ID.eq(id)).
                    fetchAny().map((r)->{
               return ConvertHelper.convert(r,CommunityApprove.class);
            });
            return result[0];
        } catch (Exception ex) {
        //fetchAny() maybe return null
        return null;
    }

    }

    @Override
    public List<CommunityApprove> queryCommunityApproves(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApprove.class));
        SelectQuery<EhCommunityApproveRecord> query = context.selectQuery(Tables.EH_COMMUNITY_APPROVE);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_COMMUNITY_APPROVE.ID.gt(locator.getAnchor()));
        }
        query.addLimit(count);
        List<CommunityApprove> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, CommunityApprove.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
}
