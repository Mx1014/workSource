// @formatter:off
package com.everhomes.rest.organizationfile;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 名称</li>
 *     <li>size: 大小</li>
 *     <li>creatorName: 上传人名称</li>
 *     <li>contentUri: uri</li>
 *     <li>contentUrl: url</li>
 *     <li>createTime: 上传时间</li>
 *     <li>downloadCount: 下载次数</li>
 * </ul>
 */
public class OrganizationFileDTO {

    private Long id;
    private String name;
    private String size;
    private String creatorName;
    private String contentUri;
    private String contentUrl;
    private Long createTime;
    private Integer downloadCount;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreateTime() {
        return createTime;
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

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
