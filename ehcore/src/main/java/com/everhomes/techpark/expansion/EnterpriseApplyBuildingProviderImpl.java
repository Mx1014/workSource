package com.everhomes.techpark.expansion;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhLeaseBuildingsDao;
import com.everhomes.server.schema.tables.daos.EhLeasePromotionCommunitiesDao;
import com.everhomes.server.schema.tables.pojos.EhLeaseBuildings;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotionCommunities;
import com.everhomes.server.schema.tables.records.EhLeaseBuildingsRecord;
import com.everhomes.server.schema.tables.records.EhLeasePromotionCommunitiesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author sw on 2017/8/3.
 */
@Component
public class EnterpriseApplyBuildingProviderImpl implements EnterpriseApplyBuildingProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<LeaseBuilding> listLeaseBuildings(Integer namespaceId, Long communityId, Long pageAnchor, Integer pageSize) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseBuildings.class));

        SelectQuery<EhLeaseBuildingsRecord> query = context.selectQuery(Tables.EH_LEASE_BUILDINGS);

        if (null != pageAnchor && pageAnchor != 0L) {
            query.addConditions(Tables.EH_LEASE_BUILDINGS.ID.gt(pageAnchor));
        }

        query.addConditions(Tables.EH_LEASE_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_LEASE_BUILDINGS.COMMUNITY_ID.eq(communityId));

        query.addOrderBy(Tables.EH_LEASE_BUILDINGS.ID.asc());

        if (null != pageSize) {
            query.addLimit(pageSize);
        }

        return query.fetch().map(r -> ConvertHelper.convert(r, LeaseBuilding.class));
    }

    @Override
    public void createLeaseBuilding(LeaseBuilding leaseBuilding) {

        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhLeaseBuildings.class));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeaseBuildingsDao dao = new EhLeaseBuildingsDao(context.configuration());

        leaseBuilding.setId(id);
        leaseBuilding.setCreateTime(new Timestamp(System.currentTimeMillis()));
        leaseBuilding.setCreatorUid(UserContext.currentUserId());
        leaseBuilding.setDefaultOrder(id);
        dao.insert(leaseBuilding);
    }

    @Override
    public void updateLeaseBuilding(LeaseBuilding leaseBuilding) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeaseBuildingsDao dao = new EhLeaseBuildingsDao(context.configuration());

        leaseBuilding.setOperateTime(new Timestamp(System.currentTimeMillis()));
        leaseBuilding.setOperatorUid(UserContext.currentUserId());
        dao.update(leaseBuilding);

    }

    @Override
    public LeaseBuilding findLeaseBuildingById(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseBuildings.class));

        EhLeaseBuildingsDao dao = new EhLeaseBuildingsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), LeaseBuilding.class);
    }

    @Override
    public void createLeasePromotionCommunity(LeasePromotionCommunity leasePromotionCommunity) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLeasePromotionCommunities.class));
        leasePromotionCommunity.setId(id);
        leasePromotionCommunity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        leasePromotionCommunity.setCreatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeasePromotionCommunitiesDao dao = new EhLeasePromotionCommunitiesDao(context.configuration());
        dao.insert(leasePromotionCommunity);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeasePromotionCommunities.class, null);
    }

    @Override
    public List<Long> listLeasePromotionCommunities(Long id) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhLeasePromotionCommunitiesRecord> query = context.selectQuery(Tables.EH_LEASE_PROMOTION_COMMUNITIES);
        query.addConditions(Tables.EH_LEASE_PROMOTION_COMMUNITIES.LEASE_PROMOTION_ID.eq(id));

        return query.fetch(Tables.EH_LEASE_PROMOTION_COMMUNITIES.COMMUNITY_ID);
    }

    @Override
    public void deleteLeasePromotionCommunity(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        DeleteQuery query = context.deleteQuery(Tables.EH_LEASE_PROMOTION_COMMUNITIES);
        query.addConditions(Tables.EH_LEASE_PROMOTION_COMMUNITIES.LEASE_PROMOTION_ID.eq(id));

        query.execute();

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLeasePromotionCommunities.class, null);

    }
}
