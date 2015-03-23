// @formatter:off
package com.everhomes.entity;

import org.jooq.DSLContext;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqDiscover;
import com.everhomes.jooq.JooqMetaInfo;
import com.everhomes.util.ConvertHelper;

/**
 * General entity profile management
 * 
 * @author Kelven Yang
 *
 */
public class EntityProfileProviderImpl implements EntityProfileProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityProfileProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    public void createProfileItem(Class<?> entityPojoClz, long entityId, 
            Class<?> entityProfileItemPojoClz, EntityProfileItem item) {
        
        // use owner entity to determine DB partition
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(entityPojoClz, entityId));
        
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(entityProfileItemPojoClz);
        assert(meta != null);
        
        try {
            DAOImpl dao = (DAOImpl)meta.getDaoClass().newInstance();
            dao.setConfiguration(context.configuration());
            dao.insert(ConvertHelper.convert(item, entityProfileItemPojoClz));
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unexpected exception",e);
            throw new RuntimeException("Unexpected exception when constructing DAO instance for POJO " + entityProfileItemPojoClz.getName());
        }
    }
}
