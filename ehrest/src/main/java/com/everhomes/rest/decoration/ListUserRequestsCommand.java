package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>namespaceId</li>
 * <li>conmmunityId</li>
 * <li>phone</li>
 * <li>pageAnchor</li>
 * <li>pageSize</li>
 * </ul>
 */
public class ListUserRequestsCommand {

    private Long namespaceId;
    private Long conmmunityId;
    private String phone;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getConmmunityId() {
        return conmmunityId;
    }

    public void setConmmunityId(Long conmmunityId) {
        this.conmmunityId = conmmunityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
