// @formatter:off
package com.everhomes.rest.announcement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 附件ID</li>
 * <li>postId: 帖子或评论ID</li>
 * <li>contentType: 附件类型，{@link com.everhomes.rest.announcement.AnnouncementContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * <li>size: 附件大小</li>
 * <li>metadata: 附件其它属性（如音频/视频的时长、视频的分辨率等）</li>
 * </ul>
 */
public class AttachmentDTO {
    private Long id;
    
    private Long postId;
    
    private String contentType;
    
    private String contentUri;
    
    private String contentUrl;
    
    private Integer size;
    
    private String  metadata;
    
    public AttachmentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
