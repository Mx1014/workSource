package com.everhomes.techpark.expansion;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.techpark.expansion.LeaseBulidingStatus;
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
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        query.addConditions(Tables.EH_LEASE_BUILDINGS.STATUS.eq(LeaseBulidingStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_LEASE_BUILDINGS.DEFAULT_ORDER.desc());

        if (null != pageSize) {
            query.addLimit(pageSize);
        }

        return query.fetch().map(r -> ConvertHelper.convert(r, LeaseBuilding.class));
    }

    @Override
    public Boolean verifyBuildingName(Integer namespaceId, Long communityId, String buildingName) {

        final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readWrite(), null,
                (DSLContext context, Object reducingContext)-> {

                    SelectJoinStep<Record1<Integer>> query = context.selectCount()
                            .from(Tables.EH_LEASE_BUILDINGS);

                    Condition condition = Tables.EH_LEASE_BUILDINGS.COMMUNITY_ID.eq(communityId);
                    condition = condition.and(Tables.EH_LEASE_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
                    condition = condition.and(Tables.EH_LEASE_BUILDINGS.NAME.eq(buildingName));

                    count[0] = query.where(condition).fetchOneInto(Integer.class);
                    return true;
                });
        if(count[0] > 0) {
            return false;
        }
        return true;
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
    public void createLeaseBuildings(List<LeaseBuilding> leaseBuildings) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeaseBuildingsDao dao = new EhLeaseBuildingsDao(context.configuration());

        dao.insert(leaseBuildings.stream().map(r -> ConvertHelper.convert(r, EhLeaseBuildings.class)).collect(Collectors.toList()));
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
    public LeaseBuilding findLeaseBuildingByBuildingId(Long buildingId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseBuildings.class));

        SelectQuery<EhLeaseBuildingsRecord> query = context.selectQuery(Tables.EH_LEASE_BUILDINGS);

        query.addConditions(Tables.EH_LEASE_BUILDINGS.BUILDING_ID.eq(buildingId));

        return ConvertHelper.convert(query.fetchOne(), LeaseBuilding.class);
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
