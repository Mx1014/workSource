// @formatter:off
package com.everhomes.parking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserProvider;

@Component
public class ParkingProviderImpl implements ParkingProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingProviderImpl.class);
    
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CoordinationProvider coordinator;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
 }
