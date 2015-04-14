// @formatter:off
package com.everhomes.address;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAddressesDao;
import com.everhomes.server.schema.tables.daos.EhCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhCommunityGeopointsDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityGeopoints;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AddressProviderImpl implements AddressProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequnceProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Override
    public void createAddress(Address address) {
        long id = shardingProvider.allocShardableContentId(EhAddresses.class).second();
        
        address.setId(id);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.insert(address);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAddresses.class, null);
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#address.id"),
            @CacheEvict(value="Apartment", key="{#address.communityId, #address.buildingName, #address.appartmentName}") })
    @Override
    public void updateAddress(Address address) {
        assert(address.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, address.getId()));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.update(address);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddresses.class, address.getId());
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#address.id"),
            @CacheEvict(value="Apartment", key="{#address.communityId, #address.buildingName, #address.appartmentName}") })
    @Override
    public void deleteAddress(Address address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, address.getId()));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.deleteById(address.getId());
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAddresses.class, address.getId());
    }

    @Override
    public void deleteAddressById(long id) {
        // work around limitation posted by Spring proxy based injection
        AddressProvider self = PlatformContext.getComponent(AddressProvider.class);
        Address address = self.findAddressById(id);
        if(address != null)
            self.deleteAddress(address);
    }

    @Cacheable(value="Address", key="#id")
    @Override
    public Address findAddressById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Address.class);
    }
    
    @Cacheable(value="Apartment", key="{#communityId, #buildingName, #apartmentName}")
    @Override
    public Address findApartmentAddress(long communityId, String buildingName, String apartmentName) {
        final Address[] result = new Address[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
               (DSLContext context, Object reducingContext) -> {

            result[0] = context.select().from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_ADDRESSES.BUILDING_NAME.eq(buildingName))
                .and(Tables.EH_ADDRESSES.APPARTMENT_NAME.eq(apartmentName))
                .fetchOne().map((r) -> {
                   return ConvertHelper.convert(r, Address.class); 
                });
            if(result[0] != null)
                return false;
            
            return true;
        });
        
        return result[0];
    }
}
