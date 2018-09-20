// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>dtos: 家庭列表 {@link FamilyDTO}</li>
 * </ul>
 */
public class ListUserFamilyByCommunityIdResponse {

    private List<FamilyDTO> dtos;

    public List<FamilyDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FamilyDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
