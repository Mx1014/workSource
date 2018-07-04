// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;


/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>phoneNumber: 白名单手机号码[选填]</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 每一页的数量</li>
 * </ul>
 */
public class ListWhiteListCommand {

    @NotNull
    private Integer namespaceId;

    private String phoneNumber;

    private Long pageAnchor;

    private Integer pageSize;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
