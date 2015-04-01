// @formatter:off
package com.everhomes.address;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class AddressServiceImpl implements AddressService, LocalBusSubscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private LocalBus localBus;
 
    @PostConstruct
    public void setup() {
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhCommunities.class, null), this);
        localBus.subscribe(DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhCommunities.class, null), this);
    }
    
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        // TODO monitor change notifications for cache invalidation
        return Action.none;
    }
    
    @Override
    public Tuple<Integer, List<CommunityDTO>> listNearbyCommunities(ListNearbyCommunityCommand cmd) {
        final List<CommunityDTO> results = new ArrayList<>();

        if(cmd.getCityId() == null && cmd.getLatigtue() == null && cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, need at least one valid value in one of these parameters: latitude, longitude, cityId");
        
        if(cmd.getLatigtue() != null && cmd.getLongitude() == null || 
                cmd.getLatigtue() == null && cmd.getLongitude() != null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");

        // TODO, return all communities only to test our REST response for now
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
            (DSLContext context, Object reducingContext)-> {
            
            context.select().from(Tables.EH_COMMUNITIES).fetch().map((r) -> {
                CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
                results.add(community);
                
                return null;
            });
                
            return true;
        });
        
        
        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }

    /**
     *  TODO: Pagination across partitions needs to be pre-fetched and cached
     */
    @Override
    public Tuple<Integer, List<CommunityDTO>> listCommunitiesByKeyword(ListCommunityByKeywordCommand cmd) {
        //
        // TODO, should be integrating with keyword search server
        //
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid keyword parameter");
            
        final List<CommunityDTO> results = new ArrayList<>();
        final long pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
        long offset = PaginationHelper.offsetFromPageOffset(cmd.getPageOffset(), pageSize);
        Tuple<Integer, Long> targetShard = new Tuple<>(0, offset);
        if(offset > 0) {
            final List<Long> countsInShards = new ArrayList<>();
            this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        
                    Long count = context.selectCount().from(Tables.EH_COMMUNITIES)
                            .where(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeyword() + "%"))
                            .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeyword() + "%"))
                            .fetchOne(0, Long.class);
                    
                    countsInShards.add(count);
                    return true;
                });
            
            targetShard = PaginationHelper.offsetFallsAt(countsInShards, offset);
        }
        if(targetShard.first() < 0)
            return new Tuple<>(ErrorCodes.SUCCESS, results);

        final int[] currentShard = new int[1];
        currentShard[0] = 0;
        final Tuple<Integer, Long> fallingShard = targetShard;
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), currentShard, 
            (DSLContext context, Object reducingContext)-> {
                int[] current = (int[])reducingContext;
                if(current[0] < fallingShard.first()) {
                    current[0] += 1;
                    return true;
                }
                
                long off = 0;
                if(current[0] == fallingShard.first())
                    off = fallingShard.second();
                
                context.select().from(Tables.EH_COMMUNITIES)
                    .where(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeyword() + "%"))
                    .or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeyword() + "%"))
                    .limit((int)pageSize).offset((int)off)
                    .fetch().map((r) -> {
                        CommunityDTO community = ConvertHelper.convert(r, CommunityDTO.class);
                        if(results.size() <= pageSize + 1)
                            results.add(community);
                        return null;
                });
                
            return true;
        });
        
        if(results.size() > pageSize) {
            results.remove(results.size() - 1);
            return new Tuple<>(ErrorCodes.SUCCESS_MORE_DATA, results);
        }
        
        return new Tuple<>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<BuildingDTO>> listBuildingsByKeyword(ListBuildingByKeywordCommand cmd) {
        if(cmd.getCommunitId() == null || cmd.getKeyword() == null || cmd.getKeyword().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId or keyword parameter");
        
        List<BuildingDTO> results = new ArrayList<BuildingDTO>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    String likeVal = cmd.getKeyword() + "%";
                    context.selectDistinct(Tables.EH_ADDRESSES.BUILDING_NAME, Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME)
                        .from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunitId())
                        .and(Tables.EH_ADDRESSES.BUILDING_NAME.like(likeVal)
                                .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.like(likeVal))))        
                        .fetch().map((r) -> {
                            BuildingDTO building = new BuildingDTO();
                            building.setBuildingName(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME));
                            building.setBuildingAliasName(r.getValue(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME));
                            results.add(building);
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<BuildingDTO>>(ErrorCodes.SUCCESS, results);
    }

    @Override
    public Tuple<Integer, List<String>> listAppartmentsByKeyword(ListAppartmentByKeywordCommand cmd) {
        if(cmd.getCommunitId() == null || cmd.getKeyword() == null || cmd.getKeyword().isEmpty() ||
             cmd.getBuildingName() == null || cmd.getBuildingName().isEmpty())
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid communityId, buildingName or keyword parameter");
        
        List<String> results = new ArrayList<>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    
                    context.selectDistinct(Tables.EH_ADDRESSES.BUILDING_NAME, Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME)
                        .from(Tables.EH_ADDRESSES)
                        .where(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(cmd.getCommunitId())
                        .and(Tables.EH_ADDRESSES.BUILDING_NAME.equal(cmd.getBuildingName())
                                .or(Tables.EH_ADDRESSES.BUILDING_ALIAS_NAME.equal(cmd.getBuildingName()))))
                        .and(Tables.EH_ADDRESSES.APPARTMENT_NAME.like(cmd.getKeyword() + "%"))
                        .fetch().map((r) -> {
                            results.add(r.getValue(Tables.EH_ADDRESSES.APPARTMENT_NAME));
                            return null;
                        });
                    
                return true;
            });
        
        return new Tuple<Integer, List<String>>(ErrorCodes.SUCCESS, results);
    }
}
