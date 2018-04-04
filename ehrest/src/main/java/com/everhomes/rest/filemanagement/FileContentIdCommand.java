package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

public class FileContentIdCommand {

    private Long contentId;

    public FileContentIdCommand() {
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
