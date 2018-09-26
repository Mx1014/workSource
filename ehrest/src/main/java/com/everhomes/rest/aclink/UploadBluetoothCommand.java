// @formatter:off
package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

import java.io.File;

/**
 * <ul>
 * <li>upload:上传文件</li>
 * <li>uploadName:上传文件名称</li>
 * <li>uploadType:上传文件扩展名</li>
 * </ul>
 */
public class UploadBluetoothCommand {
    private File upload;
    private String uploadName;
    private String uploadType;

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
