//@formatter:off
package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
/**
 *<ul>
 * <li>id:请示类型id</li>
 * <li>name:请示类型名称</li>
 *</ul>
 */
public class ListRequisitionTypesDTO {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
