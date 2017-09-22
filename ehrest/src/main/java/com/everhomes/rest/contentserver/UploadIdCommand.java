package com.everhomes.rest.contentserver;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 上传控件的会话 ID，用于处理后续的一系列动作
 * <li>uploadId: 上传会话 ID</li>
 * </ul>
 * @author janson
 *
 */
public class UploadIdCommand {
    @NotNull
    String uploadId;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
