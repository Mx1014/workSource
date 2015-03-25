// @formatter:off
package com.everhomes.entity;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.everhomes.cache.CacheProvider;
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
@Component
public class EntityProfileProviderImpl implements EntityProfileProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityProfileProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private CacheProvider cacheProvider;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
            
            item.setOwnerEntityPojoClass(entityPojoClz);
            item.setItemPojoClass(entityProfileItemPojoClz);
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unexpected exception",e);
            throw new RuntimeException("Unexpected exception when constructing DAO instance for POJO " + entityProfileItemPojoClz);
        }
    }
    
    @Caching(evict = {@CacheEvict(value = "EntityProfile", key="{#item.itemPojoClass.simpleName, #item.id}"),
            @CacheEvict(value="ProfileList", key="{#item.ownerEntityPojoClass.simpleName, #item.ownerId}")})
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void updateProfileItem(EntityProfileItem item) {
        assert(item.getOwnerEntityPojoClass() != null);
        assert(item.getItemPojoClass() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(item.getOwnerEntityPojoClass(), 
             item.getOwnerId()));
        
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(item.getItemPojoClass());
        assert(meta != null);
        
        try {
            DAOImpl dao = (DAOImpl)meta.getDaoClass().newInstance();
            dao.setConfiguration(context.configuration());
            
            dao.update(ConvertHelper.convert(item, item.getItemPojoClass()));
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unexpected exception",e);
            throw new RuntimeException("Unexpected exception when constructing DAO instance for POJO " + item.getItemPojoClass());
        }
    }
    
    @Caching(evict = { @CacheEvict(value = "EntityProfile", key="{#item.itemPojoClass.simpleName, #item.id}"),
            @CacheEvict(value="ProfileList", key="{#item.ownerEntityPojoClass.simpleName, #item.ownerId}")})
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void deleteProfileItem(EntityProfileItem item) {
        assert(item.getOwnerEntityPojoClass() != null);
        assert(item.getItemPojoClass() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(item.getOwnerEntityPojoClass(), 
             item.getOwnerId()));
        
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(item.getItemPojoClass());
        assert(meta != null);
        
        try {
            DAOImpl dao = (DAOImpl)meta.getDaoClass().newInstance();
            dao.setConfiguration(context.configuration());
            dao.deleteById(item.getId());
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unexpected exception",e);
            throw new RuntimeException("Unexpected exception when constructing DAO instance for POJO " + item.getItemPojoClass());
        }
    }
    
    @Cacheable(value = "EntityProfile", key="{#entityProfileItemPojoClz.simpleName, #itemId}")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public EntityProfileItem findProfileItemById(Class<?> entityPojoClz,  
            Class<?> entityProfileItemPojoClz, long itemId) {
        
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(entityProfileItemPojoClz);
        assert(meta != null);
 
        Record blankRecord = meta.getBlankRecordObject();
        assert(blankRecord != null);
        
        final EntityProfileItem[] result = new EntityProfileItem[1];
                
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(entityPojoClz), result, 
            (DSLContext context, Object reducingContext) -> {
                EntityProfileItem item = context.select().from(meta.getTableName())
                .where(((TableField)blankRecord.field("id")).eq(itemId))
                .fetchOne().map(new EntityProfileItemRecordMapper(entityPojoClz, entityProfileItemPojoClz));
                
                if(item != null) {
                    result[0] = item;
                    return false;
                }
                
                return true;
            });
        
        return result[0];
    }

    @Cacheable(value = "ProfileList", key="{#entityPojoClz.simpleName, #entityId}")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<EntityProfileItem> listEntityProfileItems(Class<?> entityPojoClz, long entityId, 
            Class<?> itemPojoClz) {
        
        JooqMetaInfo meta = JooqDiscover.jooqMetaFromPojo(itemPojoClz);
        assert(meta != null);
 
        Record blankRecord = meta.getBlankRecordObject();
        assert(blankRecord != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(entityPojoClz, entityId));
        List<EntityProfileItem> items = context.select().from(meta.getTableName())
            .where(((TableField)blankRecord.field("owner_id")).eq(entityId))
            .fetch().map(new EntityProfileItemRecordMapper(entityPojoClz, itemPojoClz));
        
        return items;
    }
}
