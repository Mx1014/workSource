package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *<li>id：id</li>
 * <li>uri：文件uri</li>
 * <li>url：文件url</li>
 * <li>url：文件名</li>
 * </ul>
 */
public class RentalSiteFileDTO {
    private java.lang.Long   id;
    private java.lang.String uri;
    private java.lang.String url;
    private java.lang.String name;

    public java.lang.Long getId() {
        return id;
    }


    public void setId(java.lang.Long id) {
        this.id = id;
    }


    public java.lang.String getUri() {
        return uri;
    }


    public void setUri(java.lang.String uri) {
        this.uri = uri;
    }


    public java.lang.String getUrl() {
        return url;
    }


    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
