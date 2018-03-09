package com.everhomes.rest.notice;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <p>我的公告列表</p>
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 * <li>dtos : 结果集,参考{@link EnterpriseNoticeDTO}</li>
 * </ul>
 */
public class ListEnterpriseNoticeResponse {
    private Integer nextPageOffset;
    @ItemType(value = EnterpriseNoticeDTO.class)
    private List<EnterpriseNoticeDTO> dtos;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<EnterpriseNoticeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<EnterpriseNoticeDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
