// @formatter:off
package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>downloadName:上传文件名称</li>
 * </ul>
 */
public class DownloadBluetoothCommand {
    private String downloadName;

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
