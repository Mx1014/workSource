package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.FamilyUserDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationUserDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos 参考{@link AddressUserDTO}</li>
 * </ul>
 */
public class ListAddressUsersResponse {
    private List<AddressUserDTO> dtos;

    public List<AddressUserDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<AddressUserDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}