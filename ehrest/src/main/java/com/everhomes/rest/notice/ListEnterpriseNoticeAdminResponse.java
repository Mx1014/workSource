package com.everhomes.rest.notice;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <p>所有的公告列表</p>
 * <ul>
 * <li>pageSize : 每页获取的最大记录数，用于前端计算总页数</li>
 * <li>totalCount : 总记录数，用于前端计算总页数</li>
 * <li>dtos : 结果集,参考{@link EnterpriseNoticeDTO}</li>
 * </ul>
 */
public class ListEnterpriseNoticeAdminResponse {
    Integer pageSize;
    Integer totalCount;
    @ItemType(value = EnterpriseNoticeDTO.class)
    private List<EnterpriseNoticeDTO> dtos;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
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
