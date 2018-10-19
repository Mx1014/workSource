package com.everhomes.asset.statistic;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
/**
 * @author created by ycx
 * @date 下午3:52:18
 */
@Component
public class AssetStatisticProviderImpl implements AssetStatisticProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetStatisticProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
 
    private DSLContext getReadOnlyContext(){
        return this.dbProvider.getDslContext(AccessSpec.readOnly());
    }
    
    private DSLContext getReadWriteContext(){
        return this.dbProvider.getDslContext(AccessSpec.readWrite());
    }
    
    @SuppressWarnings("rawtypes")
	private Long getNextSequence(Class clz){
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(clz));
    }

	public void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr) {
		
		
		
	}
    
    
    
    
    
}