package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contentId: 内容id</li>
 * <li>contentName: 内容名称</li>
 * </ul>
 */
public class UpdateFileContentNameCommand {

    private Long contentId;

    private String contentName;

    private String contentSuffix;

    public UpdateFileContentNameCommand() {
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
