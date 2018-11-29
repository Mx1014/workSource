package com.everhomes.asset.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
/**
 * @author created by ycx
 * @date 下午4:06:16
 */
@Component
public class AssetItemProviderImpl implements AssetItemProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetItemProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
 
    
    
    

}