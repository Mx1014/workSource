// @formatter:off
package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>uri: 文件URI，用于存储</li>
 * <li>url: 文件URL，用于浏览器访问</li>
 * </ul>
 */
public class CsFileLocationDTO {
    private String uri;
    private String url;
    private Integer size;
    
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
