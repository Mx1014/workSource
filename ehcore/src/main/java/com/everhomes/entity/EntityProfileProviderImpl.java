// @formatter:off
package com.everhomes.entity;

import java.lang.reflect.Method;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                .fetchOne().map((Record r)-> {
                    EntityProfileItem val = new EntityProfileItem();
                    val.setOwnerEntityPojoClass(entityPojoClz);
                    val.setItemPojoClass(entityProfileItemPojoClz);
                    
                    val.setId(r.getValue((Field<Long>)r.field("id")));
                    val.setAppId(r.getValue((Field<Long>)r.field("app_id")));
                    val.setOwnerId(r.getValue((Field<Long>)r.field("owner_id")));
                    val.setItemName(r.getValue((Field<String>)r.field("item_name")));
                    val.setItemKind(r.getValue((Field<Byte>)r.field("item_kind")));
                    val.setItemValue(r.getValue((Field<String>)r.field("item_value")));
                    val.setTargetType(r.getValue((Field<String>)r.field("target_type")));
                    val.setTargetId(r.getValue((Field<Long>)r.field("target_id")));
                    val.setIntegralTag1(r.getValue((Field<Long>)r.field("integral_tag1")));
                    val.setIntegralTag2(r.getValue((Field<Long>)r.field("integral_tag2")));
                    val.setIntegralTag3(r.getValue((Field<Long>)r.field("integral_tag3")));
                    val.setIntegralTag4(r.getValue((Field<Long>)r.field("integral_tag4")));
                    val.setIntegralTag5(r.getValue((Field<Long>)r.field("integral_tag5")));
                    val.setStringTag1(r.getValue((Field<String>)r.field("string_tag1")));
                    val.setStringTag2(r.getValue((Field<String>)r.field("string_tag2")));
                    val.setStringTag3(r.getValue((Field<String>)r.field("string_tag3")));
                    val.setStringTag4(r.getValue((Field<String>)r.field("string_tag4")));
                    val.setStringTag5(r.getValue((Field<String>)r.field("string_tag5")));
                                       
                    return val;
                });
                
                if(item != null) {
                    result[0] = item;
                    return false;
                }
                
                return true;
            });
        
        return result[0];
    }
}
