package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 *
 * <ul>
 *     <li>id: 附件id</li>
 *     <li>addressId: 门牌id</li>
 *     <li>name: 附件名</li>
 *     <li>fileSize: 附件大小(单位为byte)</li>
 *     <li>contentType: 内容类型，参考{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>contentUri: 内容uri</li>
 *     <li>contentUrl: 内容url</li>
 *     <li>downloadCount: 下载次数</li>
 *     <li>creatorUid: 创建者id</li>
 *     <li>creatorName: 创建者姓名</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 * Created by ying.xiong on 2017/11/28.
 */
public class ApartmentAttachmentDTO {
    private Long id;

    private Long addressId;

    private String name;

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
