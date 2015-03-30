// @formatter:off
package com.everhomes.address;

import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhAddressClaimsDao;
import com.everhomes.server.schema.tables.daos.EhAddressesDao;
import com.everhomes.server.schema.tables.daos.EhCommunitiesDao;
import com.everhomes.server.schema.tables.pojos.EhAddressClaims;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AddressProviderImpl implements AddressProvider {
    
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
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#address.id") } )
    @Override
    public void updateAddress(Address address) {
        assert(address.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, address.getId()));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.update(address);
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#address.id") } )
    @Override
    public void deleteAddress(Address address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, address.getId()));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.deleteById(address.getId());
    }

    @Caching(evict = { @CacheEvict(value="Address", key="#id") } )
    @Override
    public void deleteAddressById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        dao.deleteById(id);
    }

    @Cacheable(value="Address", key="#id")
    @Override
    public Address findAddressById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAddresses.class, id));
        EhAddressesDao dao = new EhAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Address.class);
    }

    @Override
    public void createAddressClaim(AddressClaim claim) {
        assert(claim.getAddressId() != null);
        
        long id = this.sequnceProvider.getNextSequence(
            NameMapper.getSequenceDomainFromTablePojo(EhAddressClaims.class));
        claim.setId(id);
        
        DSLContext context = this.dbProvider.getDslContext(
            AccessSpec.readWriteWith(EhAddresses.class, claim.getAddressId()));
        EhAddressClaimsDao dao = new EhAddressClaimsDao(context.configuration());
        
        claim.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(claim);
    }

    @Caching(evict = { @CacheEvict(value="AddressClaim", key="#claim.id") } )
    @Override
    public void updateAddressClaim(AddressClaim claim) {
        assert(claim.getId() != null);
        assert(claim.getAddressId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
                AccessSpec.readWriteWith(EhAddresses.class, claim.getAddressId()));
        EhAddressClaimsDao dao = new EhAddressClaimsDao(context.configuration());
        dao.update(claim);
    }

    @Caching(evict = { @CacheEvict(value="AddressClaim", key="#claim.id") } )
    @Override
    public void deleteAddressClaim(AddressClaim claim) {
        assert(claim.getId() != null);
        assert(claim.getAddressId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
                AccessSpec.readWriteWith(EhAddresses.class, claim.getAddressId()));
        EhAddressClaimsDao dao = new EhAddressClaimsDao(context.configuration());
        dao.deleteById(claim.getId());
    }

    /**
     * Avoid using this method, Spring proxy mode cause caching to be skipped for methods calls
     * within the same class
     */
    @Caching(evict = { @CacheEvict(value="AddressClaim", key="#id") } )
    @Override
    public void deleteAddressClaimById(long id) {
        AddressClaim claim = findAddressClaimById(id);
        if(claim != null)
            deleteAddressClaim(claim);
    }

    @Cacheable(value="AddressClaim", key="#id")
    @Override
    public AddressClaim findAddressClaimById(long id) {
        final AddressClaim[] result = new AddressClaim[1];

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), result, 
            (DSLContext context, Object reducingContext) -> {
                EhAddressClaimsDao dao = new EhAddressClaimsDao(context.configuration());
                result[0] = ConvertHelper.convert(dao.findById(id), AddressClaim.class);
            
                if(result[0] != null) {
                    return false;
                }
                
                return true;
            });
        
        return result[0];
    }

    @Override
    public void createCommunity(Community community) {
        long id = shardingProvider.allocShardableContentId(EhCommunities.class).second();
        
        community.setId(id);
        community.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, id));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.insert(community);
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#community.id") } )
    @Override
    public void updateCommunity(Community community) {
        assert(community.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, community.getId()));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.update(community);
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#community.id") } )
    @Override
    public void deleteCommunity(Community community) {
        assert(community.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, community.getId()));
        EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
        dao.deleteById(community.getId());
    }

    @Caching(evict = { @CacheEvict(value="Community", key="#id") } )
    @Override
    public void deleteCommunityById(long id) {
        Community community = findCommunityById(id);
        if(community != null)
            deleteCommunity(community);
    }

    @Cacheable(value="Community", key="#id")
    @Override
    public Community findCommunityById(long id) {
        final Community[] result = new Community[1];

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), result, 
            (DSLContext context, Object reducingContext) -> {
                EhCommunitiesDao dao = new EhCommunitiesDao(context.configuration());
                result[0] = ConvertHelper.convert(dao.findById(id), Community.class);
            
                if(result[0] != null) {
                    return false;
                }
                
                return true;
            });
        
        return result[0];
    }
}
