package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/6/25 16 :40
 */

public class CustomerAttachmentDTO {
    private Long id;
    private String name;
    private Long customerId;
    private String contentType;
    private String contentUri;
    private String contentUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
