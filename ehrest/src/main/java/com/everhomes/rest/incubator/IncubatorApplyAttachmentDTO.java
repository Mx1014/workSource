package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 活动附件id</li>
 *     <li>incubatorApplyId: 入孵申请Id</li>
 *     <li>name: 附件名</li>
 *     <li>type: 附件类型，1-营业执照，2-计划书 参考{@link IncubatorApplyAttachmentType}</li>
 *     <li>fileSize: 附件大小(单位为byte)</li>
 *     <li>contentType: 内容类型，参考{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>contentUri: 内容uri</li>
 *     <li>contentUrl: 内容url</li>
 *     <li>downloadCount: 下载次数</li>
 *     <li>creatorUid: 创建者id</li>
 *     <li>creatorName: 创建者姓名</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 */
public class IncubatorApplyAttachmentDTO {

    private Long id;

    private Long incubatorApplyId;

    private String name;

    private Byte type;

    private Integer fileSize;

    private String contentType;

    private String contentUri;

    private String contentUrl;

    private int downloadCount;

    private Long creatorUid;

    private String creatorName;

    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncubatorApplyId() {
        return incubatorApplyId;
    }

    public void setIncubatorApplyId(Long incubatorApplyId) {
        this.incubatorApplyId = incubatorApplyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
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

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
