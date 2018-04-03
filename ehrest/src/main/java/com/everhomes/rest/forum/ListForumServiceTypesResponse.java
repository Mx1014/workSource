// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 服务类型 {@link ForumServiceTypeDTO}</li>
 * </ul>
 */
public class ListForumServiceTypesResponse {

    @ItemType(ForumServiceTypeDTO.class)
    private List<ForumServiceTypeDTO> dtos;

    public List<ForumServiceTypeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ForumServiceTypeDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
