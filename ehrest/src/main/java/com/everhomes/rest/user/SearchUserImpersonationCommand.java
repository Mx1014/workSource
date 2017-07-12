package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SearchUserImpersonationCommand {
    private Integer namespaceId;
    private String phone;
    private Byte imperOnly;
    private Long anchor;
    
    private Integer pageSize;


    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getImperOnly() {
        return imperOnly;
    }

    public void setImperOnly(Byte imperOnly) {
        this.imperOnly = imperOnly;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
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
