// @formatter:off
package com.everhomes.entity;

/**
 * General entity profile management
 * 
 * @author Kelven Yang
 *
 */
public interface EntityProfileProvider {
    void createProfileItem(Class<?> entityPojoClz, long entityId, 
            Class<?> entityProfileItemPojoClz, EntityProfileItem item);
}
