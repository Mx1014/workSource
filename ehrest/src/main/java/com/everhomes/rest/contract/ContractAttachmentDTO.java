package com.everhomes.rest.contract;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 合同附件id</li>
 *     <li>name: 附件名</li>
 *     <li>fileSize: 附件大小(单位为byte)</li>
 *     <li>contentType: 内容类型，参考{@link com.everhomes.rest.forum.PostContentType}</li>
 *     <li>contentUri: 内容uri</li>
 *     <li>contentUrl: 内容url</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class ContractAttachmentDTO {

    private Long id;

    private String name;

    private Integer fileSize;

    private String contentType;

    private String contentUri;

    private String contentUrl;

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

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

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
}
