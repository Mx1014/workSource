// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>keyWords : 公司名称关键字,用于模糊匹配公司名称</li>
  *<li>ownerId : 园区Id，由左邻分配</li>
  *<li>ownerType : 填community</li>
  *<li>pageAnchor : 页码，默认为0</li>
  *<li>pageSize : 页大小，默认20</li>
  *</ul>
  */

public class OpenApiListOrganizationsCommand {
    private String keyWords;
    private String ownerType;
    private Long ownerId;
    private Long pageAnchor;
    private Integer pageSize;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
