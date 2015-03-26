// @formatter:off
package com.everhomes.jooq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jooq.tools.StringUtils;

/**
 * JooqDiscover relies heavily on JOOQ code generation. This is a hacking way under this
 * extreme case, if JOOQ code generation has broken all the assumptions made in below
 * implementation code, we either need to update or create our own meta management
 * 
 * @author Kelven Yang
 *
 */
public class JooqDiscover {
    private static Map<Class<?>, JooqMetaInfo> s_metaMap = new HashMap<Class<?>, JooqMetaInfo>();

    public static JooqMetaInfo jooqMetaFromPojo(Class<?> entityPojoClz) {
        synchronized(s_metaMap) {
            JooqMetaInfo meta = s_metaMap.get(entityPojoClz);
            if(meta == null) {
                meta = buildJooqMeta(entityPojoClz);
                s_metaMap.put(entityPojoClz, meta);
            }
            
            return meta;
        }
    }
    
    private static JooqMetaInfo buildJooqMeta(Class<?> entityPojoClz) {
        JooqMetaInfo meta = new JooqMetaInfo();
        
        String fullClzName = entityPojoClz.getCanonicalName();
        String[] tokens = fullClzName.split("\\.");
        
        // discover record class
        String[] recordClzTokens = new String[tokens.length];
        System.arraycopy(tokens, 0, recordClzTokens, 0, tokens.length - 2);
        recordClzTokens[tokens.length - 2] = "records";
        recordClzTokens[tokens.length - 1] =  entityPojoClz.getSimpleName() + "Record";
        
        try {
            String recordClzName = StringUtils.join(recordClzTokens, ".");
            Class<?> recordClz = Class.forName(recordClzName);
            
            try {
                Object instance = recordClz.newInstance();
                Method method = recordClz.getMethod("getTable");
                
                @SuppressWarnings("rawtypes")
                org.jooq.impl.TableImpl table = (org.jooq.impl.TableImpl)method.invoke(instance);
                
                meta.setRecordClass(recordClz);
                meta.setTableImpl(table);
                meta.setTableName(table.getName());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException 
                    | SecurityException | IllegalArgumentException | InvocationTargetException e) {
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find corresponding record class from POJO class " + entityPojoClz.getName());
        }
        
        // discover DAO class
        String[] daoClzTokens = new String[tokens.length];
        System.arraycopy(tokens, 0, daoClzTokens, 0, tokens.length - 2);
        daoClzTokens[tokens.length - 2] = "daos";
        daoClzTokens[tokens.length - 1] =  entityPojoClz.getSimpleName() + "Dao";
        
        try {
            String daoClzName = StringUtils.join(daoClzTokens, ".");
            Class<?> daoClz = Class.forName(daoClzName);
            meta.setDaoClass(daoClz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find corresponding DAO class from POJO class " + entityPojoClz.getName());
        }
        
        return meta;
    }
}
