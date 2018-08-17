// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>allReadStatus: 是否全部阅读，请参考{@link com.everhomes.rest.filedownload.AllReadStatus}</li>
 * </ul>
 */
public class GetFileDownloadReadStatusResponse {

    private Byte allReadStatus;

    public Byte getAllReadStatus() {
        return allReadStatus;
    }

    public void setAllReadStatus(Byte allReadStatus) {
        this.allReadStatus = allReadStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
