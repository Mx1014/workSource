// @formatter:off
package com.everhomes.rest.community_form;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 项目与表单关系列表，请参考{@link com.everhomes.rest.community_form.CommunityGeneralFormDTO}</li>
 *     <li>nextPageOffset: 下页页码</li>
 * </ul>
 */
public class ListCommunityFormResponse {

    @ItemType(CommunityGeneralFormDTO.class)
    private List<CommunityGeneralFormDTO> dtos;
    private Integer nextPageOffset;

    public List<CommunityGeneralFormDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CommunityGeneralFormDTO> dtos) {
        this.dtos = dtos;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
