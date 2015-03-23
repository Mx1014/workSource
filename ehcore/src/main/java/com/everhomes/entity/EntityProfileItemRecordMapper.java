// @formatter:off
package com.everhomes.entity;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

/**
 * Map type-erased Record to EntityProfileItem object
 * 
 * @author Kelven Yang
 *
 */
public class EntityProfileItemRecordMapper implements RecordMapper<Record, EntityProfileItem> {
    private Class<?> entityPojoClass;
    private Class<?> itemPojoClass;
    
    public EntityProfileItemRecordMapper(Class<?> entityPojoClass, Class<?> itemPojoClass) {
        this.entityPojoClass = entityPojoClass;
        this.itemPojoClass = itemPojoClass;
    }
    
    @Override
    public EntityProfileItem map(Record r) {
        EntityProfileItem item = new EntityProfileItem();
        item.setOwnerEntityPojoClass(this.entityPojoClass);
        item.setItemPojoClass(this.itemPojoClass);
        
        item.setId(r.getValue((Field<Long>)r.field("id")));
        item.setAppId(r.getValue((Field<Long>)r.field("app_id")));
        item.setOwnerId(r.getValue((Field<Long>)r.field("owner_id")));
        item.setItemName(r.getValue((Field<String>)r.field("item_name")));
        item.setItemKind(r.getValue((Field<Byte>)r.field("item_kind")));
        item.setItemValue(r.getValue((Field<String>)r.field("item_value")));
        item.setTargetType(r.getValue((Field<String>)r.field("target_type")));
        item.setTargetId(r.getValue((Field<Long>)r.field("target_id")));
        item.setIntegralTag1(r.getValue((Field<Long>)r.field("integral_tag1")));
        item.setIntegralTag2(r.getValue((Field<Long>)r.field("integral_tag2")));
        item.setIntegralTag3(r.getValue((Field<Long>)r.field("integral_tag3")));
        item.setIntegralTag4(r.getValue((Field<Long>)r.field("integral_tag4")));
        item.setIntegralTag5(r.getValue((Field<Long>)r.field("integral_tag5")));
        item.setStringTag1(r.getValue((Field<String>)r.field("string_tag1")));
        item.setStringTag2(r.getValue((Field<String>)r.field("string_tag2")));
        item.setStringTag3(r.getValue((Field<String>)r.field("string_tag3")));
        item.setStringTag4(r.getValue((Field<String>)r.field("string_tag4")));
        item.setStringTag5(r.getValue((Field<String>)r.field("string_tag5")));
                           
        return item;
    }
}
