// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 园区和用户的相关信息 参考{@link CommunityInfoDTO}</li>
 * </ul>
 */
public class ListAllCommunitiesResponse {
    private List<CommunityInfoDTO> dtos;

    public List<CommunityInfoDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CommunityInfoDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
