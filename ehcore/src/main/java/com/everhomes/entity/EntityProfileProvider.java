// @formatter:off
package com.everhomes.entity;

/**
 * General entity profile management
 * 
 * Please be aware that in most of the interface method, item object of EntityProfileItem requires 
 * certain meta fields to be filled properly. EntityProfileProvider usually fill them when it hand out
 * EntityProfileItem objects to its caller, for example, in findProfileItemById() etc.
 * 
 * @author Kelven Yang
 *
 */
public interface EntityProfileProvider {
    void createProfileItem(Class<?> entityPojoClz, long entityId, 
            Class<?> entityProfileItemPojoClz, EntityProfileItem item);
    
    /**
     * Update profile item.  
     * 
     * @param item profile item to be updated
     */
    void updateProfileItem(EntityProfileItem item);
    
    /**
     * Delete profile item. 
     * 
     * @param item profile item to be deleted
     */
    void deleteProfileItem(EntityProfileItem item);

    /**
     * Find profile item by id
     * 
     */
    EntityProfileItem findProfileItemById(Class<?> entityPojoClz,  
            Class<?> entityProfileItemPojoClz, long itemId);
}
