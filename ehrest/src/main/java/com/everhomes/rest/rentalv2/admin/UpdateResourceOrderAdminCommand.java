package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 资源id</li>
 * <li>defaultOrderId: 交换顺序id
 * </li>
 * </ul>
 */
public class UpdateResourceOrderAdminCommand {
    private Long id;
    private Long defaultOrderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefaultOrderId() {
        return defaultOrderId;
    }

    public void setDefaultOrderId(Long defaultOrderId) {
        this.defaultOrderId = defaultOrderId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
