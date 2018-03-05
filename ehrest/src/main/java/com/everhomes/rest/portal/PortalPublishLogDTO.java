package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>id: 发布日志id</li>
 *     <li>namespaceId: 域空间</li>
 *     <li>contentType: contentType</li>
 *     <li>contentData: contentData</li>
 *     <li>versionId: 版本号</li>
 *     <li>process: 进度</li>
 *     <li>status: 状态，0: 正在发布 1: 发布失败 2: 发布成功</li>
 * </ul>
 */
public class PortalPublishLogDTO {

    private Long id;
    private Integer namespaceId;
    private String contentType;
    private String contentData;
    private Long versionId;
    private Integer process;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentData() {
        return contentData;
    }

    public void setContentData(String contentData) {
        this.contentData = contentData;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }
}
