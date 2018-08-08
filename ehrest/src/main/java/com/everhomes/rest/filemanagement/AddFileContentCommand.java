package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * <li>parentId: 文件夹id(若在文件夹内新增文件或文件夹时传递，在目录中新建不用传递该参数)</li>
 * <li>contentType: 内容类型：file-文件, folder-文件夹 参考{@link com.everhomes.rest.filemanagement.FileContentType}</li>
 * <li>contentName: 内容名称 如果包含"/"则为其找到文件夹下存放,比如/A/B.JPG 会把B.JPG放在所在文件夹下的A文件夹下</li>
 * <li>contentSuffix: 后缀名称(文件夹忽略)</li>
 * <li>contentSize: 内容大小(文件夹忽略)</li>
 * <li>contentUri: 内容uri(文件夹忽略)</li>
 * </ul>
 */
public class AddFileContentCommand {

    private Long catalogId;

    private Long parentId;

    private String contentType;

    private String contentName;

    private String contentSuffix;

    private Integer contentSize;

    private String contentUri;

    public AddFileContentCommand() {
    }

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
