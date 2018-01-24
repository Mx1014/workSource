package com.everhomes.rentalv2;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.TableField;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum Rentalv2CustomField {
    /*------------------------vip 车位--------------------- */
    SPACE_NO("string_tag1", "stringTag1", Tables.EH_RENTALV2_ORDERS.STRING_TAG1),
    PLATE_NUMBER("string_tag2", "stringTag2", Tables.EH_RENTALV2_ORDERS.STRING_TAG2);

    private String fieldName;
    private String propertyName;
    private TableField<?, ?> field;

    private Rentalv2CustomField(String fieldName, String propertyName, TableField<?, ?> field) {
        this.fieldName = fieldName;
        this.propertyName = propertyName;
        this.field = field;
    }
    
    public String getFieldName() {
        return this.fieldName;
    }
    
    public String getPropertyName() {
        return this.propertyName;
    }
    
    @SuppressWarnings("unchecked")
    public <R extends Record, T> TableField<R, T> getField() {
        return (TableField<R, T>)field;
    }
    
    public void setIntegralValue(Object pojo, Long value) {
        String setter = StringHelper.setterNameFromProperty(this.getPropertyName());
        
        Method method;
        try {
            method = pojo.getClass().getMethod(setter, Long.class);
            assert(method != null);
            method.invoke(pojo, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to set property value of pojo object");
        }
    }
    
    public Long getIntegralValue(Object pojo) {
        String getter = StringHelper.getterNameFromProperty(this.getPropertyName());
        
        Method method;
        try {
            method = pojo.getClass().getMethod(getter);
            assert(method != null);
            return (Long)method.invoke(pojo);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to get property value of pojo object");
        }
    }
    
    public void setStringValue(Object pojo, String value) {
        String setter = StringHelper.setterNameFromProperty(this.getPropertyName());
        
        Method method;
        try {
            method = pojo.getClass().getMethod(setter, String.class);
            assert(method != null);
            method.invoke(pojo, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to set property value of pojo object");
        }
    }
    
    public String getStringValue(Object pojo) {
        String getter = StringHelper.getterNameFromProperty(this.getPropertyName());
        
        Method method;
        try {
            method = pojo.getClass().getMethod(getter);
            assert(method != null);
            return (String)method.invoke(pojo);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to get property value of pojo object");
        }
    }
}
