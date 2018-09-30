// @formatter:off
package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

import java.io.File;

/**
 * <ul>
 * <li>upload:上传文件</li>
 * <li>uploadName:上传文件名称</li>
 * <li>uploadType:上传文件扩展名</li>
 * <li>contentType: 内容类型：file-文件, folder-文件夹 参考{@link com.everhomes.rest.filemanagement.FileContentType}</li>
 * <li>contentName: 内容名称</li>
 * <li>contentSuffix: 后缀名称(文件夹忽略)</li>
 * <li>contentSize: 内容大小(文件夹忽略)</li>
 * <li>contentUri: 内容uri(文件夹忽略)</li>
 * </ul>
 */
public class UploadBluetoothCommand {

    private Long catalogId;

    private Long parentId;

    private String contentType;

    private String contentName;

    private String contentSuffix;

    private Integer contentSize;

    private String contentUri;

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentSuffix() {
        return contentSuffix;
    }

    public void setContentSuffix(String contentSuffix) {
        this.contentSuffix = contentSuffix;
    }

    public Integer getContentSize() {
        return contentSize;
    }

    public void setContentSize(Integer contentSize) {
        this.contentSize = contentSize;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
