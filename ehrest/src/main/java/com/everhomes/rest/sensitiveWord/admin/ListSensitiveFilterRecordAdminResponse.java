// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>dtos: 敏感词过滤日志列表{@link com.everhomes.rest.sensitiveWord.admin.SensitiveFilterRecordAdminDTO}</li>
 * </ul>
 */
public class ListSensitiveFilterRecordAdminResponse {

    private Long nextPageAnchor;

    @ItemType(SensitiveFilterRecordAdminDTO.class)
    private List<SensitiveFilterRecordAdminDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SensitiveFilterRecordAdminDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<SensitiveFilterRecordAdminDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
