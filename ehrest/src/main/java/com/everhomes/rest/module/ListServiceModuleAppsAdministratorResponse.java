package com.everhomes.rest.module;

import com.everhomes.rest.acl.ServiceModuleAppsAuthorizationsDto;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 管理员对象</;i>
 * <li>pageAnchor:分页锚点</li>
 * <li>pageSize: 页数</li>
 * </ul>
 */
public class ListServiceModuleAppsAdministratorResponse {
    private Long nextAnchor;

    private Integer pageSize;

    private List<ServiceModuleAppsAuthorizationsDto> dtos;

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<ServiceModuleAppsAuthorizationsDto> getDtos() {
        return dtos;
    }

    public void setDtos(List<ServiceModuleAppsAuthorizationsDto> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
