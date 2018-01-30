package com.everhomes.rest.share;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>shareType: 分享类型 {@link com.everhomes.rest.share.ShareType}</li>
 *     <li>shareData: 分享的元数据，每种分享类型自己定义,json字符串</li>
 * </ul>
 */
public class ShareCommand {

    private String shareType;
    private String shareData;

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareData() {
        return shareData;
    }

    public void setShareData(String shareData) {
        this.shareData = shareData;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
