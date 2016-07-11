// @formatter:off
package com.everhomes.entity;

import java.util.List;

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
    /**
     * Create a profile item
     * 
     * @param entityPojoClz owner entity Pojo class
     * @param entityId owner Id owner entity id
     * @param entityProfileItemPojoClz profile item Pojo class
     * @param item profile item
     */
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
     * Find profile item from item id
     * 
     */
    EntityProfileItem findProfileItemById(Class<?> entityPojoClz,  
            Class<?> entityProfileItemPojoClz, long itemId);
    
    /**
     * List all profile items of the entity. Assume the number of profile items of any entity
     * object is limited. therefore no pagination is needed
     * 
     * @param entityPojoClz
     * @param entityId
     * @return
     */
    List<EntityProfileItem> listEntityProfileItems(Class<?> entityPojoClz, long entityId,
        Class<?> itemPojoClz); 
}
