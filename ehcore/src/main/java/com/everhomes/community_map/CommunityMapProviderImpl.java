// @formatter:off
package com.everhomes.community_map;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.parking.*;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingOrderDeleteFlag;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.user.SearchTypesStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.SearchTypes;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

}
