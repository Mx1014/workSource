package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>dtos: 公司和个人的相关信息 参考{@link OrganizationUserDTO}</li>
 * </ul>
 */
public class ListOrganizationsByUserResponse {

    private List<OrganizationUserDTO> dtos;

    public List<OrganizationUserDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<OrganizationUserDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
