// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>id: 通用岗位id</li>
 * <li>name: 名称</li>
 * <li>discription: 描述</li>
 * </ul>
 */
public class UpdateOrganizationJobPositionCommand {

    private Long id;

    private String name;

    private String discription;


    public UpdateOrganizationJobPositionCommand() {
    }

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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
