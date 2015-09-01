package com.everhomes.business.admin;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.business.BusinessScope;
import com.everhomes.discover.ItemType;
import com.everhomes.launchpad.ItemScope;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 店铺ID</li>
 * <li>ItemScope: item可见范围列表， 参考{@link com.everhomes.launchpad.ItemScope}</li>
 * </ul>
 */

public class PromoteBusinessAdminCommand{
    @NotNull
    private Long id;
    @ItemType(ItemScope.class)
    private List<ItemScope> itemScopes;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemScope> getItemScopes() {
        return itemScopes;
    }

    public void setItemScopes(List<ItemScope> itemScopes) {
        this.itemScopes = itemScopes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
