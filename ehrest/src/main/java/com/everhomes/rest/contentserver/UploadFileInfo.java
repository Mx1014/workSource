package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>uri: 文件链接地址</li>
 * <li>url: 文件的下载链接</li>
 * <li>fileName: 文件名字</li>
 * <li>size: 文件大小</li>
 * </ul>
 * @author janson
 *
 */
public class UploadFileInfo {
    private String uri;
    private String url;
    private String fileName;
    private Long size;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
