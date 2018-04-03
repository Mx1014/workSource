// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.forum.ForumCategoryDTO}</li>
 * </ul>
 */
public class ListForumCategoryResponse {

    @ItemType(ForumCategoryDTO.class)
    private List<ForumCategoryDTO> dtos;

    public List<ForumCategoryDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ForumCategoryDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
