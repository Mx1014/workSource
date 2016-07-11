// @formatter:off
package com.everhomes.listing;

import com.everhomes.sharding.ShardIterator;

public class CrossShardListingLocator extends ListingLocator {
    private static final long serialVersionUID = -4727465945254146605L;
    
    private ShardIterator shardIterator;
    
    public CrossShardListingLocator() {
    }
    
    public CrossShardListingLocator(long entityId) {
        super(entityId);
    }

    public ShardIterator getShardIterator() {
        return shardIterator;
    }
    
    public void setShardIterator(ShardIterator shardIterator) {
        this.shardIterator = shardIterator;
    }
}
