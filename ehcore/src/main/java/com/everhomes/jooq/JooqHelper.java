// @formatter:off
package com.everhomes.jooq;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

/**
 * 
 * Provides JOOQ utility helper methods
 * 
 * @author Kelven Yang
 *
 */
public class JooqHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqHelper.class);
    
    @SuppressWarnings("rawtypes")
    public static Field[] toJooqFields(Table table, String... fieldNames) {
        if(fieldNames != null) {
            List<Field> fields = new ArrayList<>();
            
            for(String fieldName: fieldNames) {
                Field field = table.field(fieldName);
                if(field != null) {
                    fields.add(field);
                } else {
                    LOGGER.error("Unable to find field " + fieldName + " in table " + table.getName());
                }
            }
            
            if(fields.size() > 0)
                return fields.toArray(new Field[0]);
        }
        return null;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static SortField[] toJooqFields(Table table, Tuple<String, SortOrder>... fieldNames) {
        if(fieldNames != null) {
            List<SortField> fields = new ArrayList<>();
            
            for(Tuple<String, SortOrder> fieldName: fieldNames) {
                Field field = table.field(fieldName.first());
                if(field != null) {
                    if(fieldName.second() == SortOrder.ASC)
                        fields.add(field.asc());
                    else
                        fields.add(field.desc());
                } else {
                    LOGGER.error("Unable to find field " + fieldName.first() + " in table " + table.getName());
                }
            }
            
            if(fields.size() > 0)
                return fields.toArray(new SortField[0]);
        }
        
        return null;
    }
}
