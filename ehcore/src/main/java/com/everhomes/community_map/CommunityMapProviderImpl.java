// @formatter:off
package com.everhomes.community_map;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.community_map.CommunityMapShopStatus;

import com.everhomes.rest.user.SearchTypesStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommunityMapProviderImpl implements CommunityMapProvider {
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private DbProvider dbProvider;

    @Override
    public List<CommunityMapSearchType> listCommunityMapSearchTypesByNamespaceId(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityMapSearchTypesRecord> query = context.selectQuery(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES);
        query.addConditions(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.NAMESPACE_ID.eq(namespaceId));

        query.addConditions(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.STATUS.eq(SearchTypesStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.ORDER.asc());
        List<CommunityMapSearchType> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CommunityMapSearchType.class));
            return null;
        });

        return result;
    }

    @Override
    public CommunityMapInfo findCommunityMapInfo(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityMapInfosRecord> query = context.selectQuery(Tables.EH_COMMUNITY_MAP_INFOS);
        query.addConditions(Tables.EH_COMMUNITY_MAP_INFOS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_COMMUNITY_MAP_INFOS.VERSION.desc());
        query.addLimit(1);
        return query.fetchOne().map((r) -> ConvertHelper.convert(r, CommunityMapInfo.class));

    }

    @Override
    public List<CommunityBuildingGeo> listCommunityBuildingGeos(Long BuildingId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCommunityBuildingGeosRecord> query = context.selectQuery(Tables.EH_COMMUNITY_BUILDING_GEOS);
        query.addConditions(Tables.EH_COMMUNITY_BUILDING_GEOS.BUILDING_ID.eq(BuildingId));


        List<CommunityBuildingGeo> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, CommunityBuildingGeo.class));
            return null;
        });

        return result;

    }

    @Override
    public void createCommunityMapShop(CommunityMapShopDetail communityMapShopDetail) {

        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCommunityMapShops.class));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityMapShopsDao dao = new EhCommunityMapShopsDao(context.configuration());

        communityMapShopDetail.setId(id);
        communityMapShopDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        communityMapShopDetail.setCreatorUid(UserContext.currentUserId());
        communityMapShopDetail.setStatus(CommunityMapShopStatus.ACTIVE.getCode());

        dao.insert(communityMapShopDetail);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhCommunityMapShops.class, null);

    }

    @Override
    public void updateCommunityMapShop(CommunityMapShopDetail communityMapShopDetail) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityMapShopsDao dao = new EhCommunityMapShopsDao(context.configuration());

        communityMapShopDetail.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        communityMapShopDetail.setUpdateUid(UserContext.currentUserId());

        dao.update(communityMapShopDetail);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhCommunityMapShops.class, null);

    }

    @Override
    public CommunityMapShopDetail getCommunityMapShopDetailById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhCommunityMapShopsDao dao = new EhCommunityMapShopsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), CommunityMapShopDetail.class);
    }

    @Override
    public List<CommunityMapShopDetail> searchCommunityMapShops(Integer namespaceId, String ownerType, Long ownerId, Long buildingId,
                                                                String keyword, Long pageAnchor, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhCommunityMapShopsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_MAP_SHOPS);
        query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.STATUS.eq(CommunityMapShopStatus.ACTIVE.getCode()));

        if (null != ownerId) {
            query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.OWNER_TYPE.eq(ownerType));
            query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.OWNER_ID.eq(ownerId));
        }
        if (null != buildingId) {
            query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.BUILDING_ID.eq(buildingId));
        }
        if (StringUtils.isNotBlank(keyword)) {
            query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.SHOP_NAME.like("%" + keyword + "%"));
        }
        if (null != pageAnchor && pageAnchor != 0L) {
            query.addConditions(Tables.EH_COMMUNITY_MAP_SHOPS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        }
        query.addOrderBy(Tables.EH_COMMUNITY_MAP_SHOPS.CREATE_TIME.desc());
        if (null != pageSize) {
            query.addLimit(pageSize);
        }

        return query.fetch().map(r -> ConvertHelper.convert(r, CommunityMapShopDetail.class));
    }
}
