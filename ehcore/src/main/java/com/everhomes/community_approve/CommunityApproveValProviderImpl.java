package com.everhomes.community_approve;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunityApproveValsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityApproveVals;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
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
    public Long createCommunityApproveVal(CommunityApproveVal obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCommunityApproveVals.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApproveVals.class));
        obj.setId(id);
        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhCommunityApproveValsDao dao = new EhCommunityApproveValsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateCommunityApproveVal(CommunityApproveVal obj) {

    }

    @Override
    public void deleteCommunityApproveVal(CommunityApproveVal obj) {

    }

    @Override
    public CommunityApproveVal getCommunityApproveValByFlowCaseId(Long id) {
        try {
            CommunityApproveVal [] result = new CommunityApproveVal[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityApproveVals.class));
            result[0] = context.select().from(Tables.EH_COMMUNITY_APPROVE_VALS).
                    where(Tables.EH_COMMUNITY_APPROVE_VALS.FLOW_CASE_ID.eq(id)).
                    fetchAny().map((r)->{
                        return ConvertHelper.convert(r,CommunityApproveVal.class);
                    });
        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public CommunityApproveVal getCommunityApproveValById(Long id) {
        return null;
    }

    @Override
    public List<CommunityApproveVal> queryCommunityApproves(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        return null;
    }
}
