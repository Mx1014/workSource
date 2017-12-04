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
import com.everhomes.server.schema.tables.daos.EhLeaseProjectCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhLeaseProjectsDao;
import com.everhomes.server.schema.tables.daos.EhLeasePromotionCommunitiesDao;
import com.everhomes.server.schema.tables.pojos.EhLeaseBuildings;
import com.everhomes.server.schema.tables.pojos.EhLeaseProjectCommunities;
import com.everhomes.server.schema.tables.pojos.EhLeaseProjects;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotionCommunities;
import com.everhomes.server.schema.tables.records.EhLeaseBuildingsRecord;
import com.everhomes.server.schema.tables.records.EhLeaseProjectCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhLeaseProjectsRecord;
import com.everhomes.server.schema.tables.records.EhLeasePromotionCommunitiesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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
    public List<LeaseBuilding> listLeaseBuildings(Integer namespaceId, Long communityId, Long categoryId, Long pageAnchor,
                                                  Integer pageSize) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseBuildings.class));

        SelectQuery<EhLeaseBuildingsRecord> query = context.selectQuery(Tables.EH_LEASE_BUILDINGS);

        if (null != pageAnchor && pageAnchor != 0L) {
            query.addConditions(Tables.EH_LEASE_BUILDINGS.DEFAULT_ORDER.lt(pageAnchor));
        }

        if (null != namespaceId) {
            query.addConditions(Tables.EH_LEASE_BUILDINGS.NAMESPACE_ID.eq(namespaceId));
        }
        if (null != categoryId) {
            query.addConditions(Tables.EH_LEASE_BUILDINGS.CATEGORY_ID.eq(categoryId));
        }
        if (null != communityId) {
            query.addConditions(Tables.EH_LEASE_BUILDINGS.COMMUNITY_ID.eq(communityId));
        }
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

        return ConvertHelper.convert(query.fetchAny(), LeaseBuilding.class);
    }

    @Override
    public LeaseBuilding findLeaseBuildingByName(Long communityId, String name) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseBuildings.class));

        SelectQuery<EhLeaseBuildingsRecord> query = context.selectQuery(Tables.EH_LEASE_BUILDINGS);

        query.addConditions(Tables.EH_LEASE_BUILDINGS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_LEASE_BUILDINGS.NAME.eq(name));

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

    @Override
    public void createLeaseProject(LeaseProject leaseProject) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLeaseProjects.class));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeaseProjectsDao dao = new EhLeaseProjectsDao(context.configuration());
        leaseProject.setId(id);
        leaseProject.setCreateTime(new Timestamp(System.currentTimeMillis()));
        leaseProject.setCreatorUid(UserContext.currentUserId());
        dao.insert(leaseProject);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeaseProjects.class, null);

    }

    @Override
    public void createLeaseProjectCommunity(LeaseProjectCommunity leaseProjectCommunity) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLeaseProjectCommunities.class));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeaseProjectCommunitiesDao dao = new EhLeaseProjectCommunitiesDao(context.configuration());
        leaseProjectCommunity.setId(id);
        leaseProjectCommunity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        leaseProjectCommunity.setCreatorUid(UserContext.currentUserId());
        dao.insert(leaseProjectCommunity);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeaseProjectCommunities.class, null);

    }

    @Override
    public void deleteLeaseProjectCommunity(Long leaseProjectId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());


        DeleteQuery<EhLeaseProjectCommunitiesRecord> query =  context.deleteQuery(Tables.EH_LEASE_PROJECT_COMMUNITIES);
        query.addConditions(Tables.EH_LEASE_PROJECT_COMMUNITIES.LEASE_PROJECT_ID.eq(leaseProjectId));

        query.execute();
    }

    @Override
    public List<Long> listLeaseProjectCommunities(Long leaseProjectId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseProjectCommunities.class));

        SelectQuery<EhLeaseProjectCommunitiesRecord> query = context.selectQuery(Tables.EH_LEASE_PROJECT_COMMUNITIES);

        query.addConditions(Tables.EH_LEASE_PROJECT_COMMUNITIES.LEASE_PROJECT_ID.eq(leaseProjectId));

        return query.fetch(Tables.EH_LEASE_PROJECT_COMMUNITIES.COMMUNITY_ID);
    }

    @Override
    public void updateLeaseProject(LeaseProject leaseProject) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        EhLeaseProjectsDao dao = new EhLeaseProjectsDao(context.configuration());

        dao.update(leaseProject);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLeaseProjects.class, null);

    }

    @Override
    public LeaseProject findLeaseProjectByProjectId(Long projectId, Long categoryId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhLeaseProjectsRecord> query = context.selectQuery(Tables.EH_LEASE_PROJECTS);

        query.addConditions(Tables.EH_LEASE_PROJECTS.PROJECT_ID.eq(projectId));
        query.addConditions(Tables.EH_LEASE_PROJECTS.CATEGORY_ID.eq(categoryId));

        return ConvertHelper.convert(query.fetchOne(), LeaseProject.class);

    }
}
