// @formatter:off
package com.everhomes.address;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
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
     *  Pagination used in this method has a big performance impact on partitioned database
     *  This method will be used for gathering performance data 
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
}
