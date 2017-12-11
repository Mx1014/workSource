package com.everhomes.rest.version;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>downloadUrl: 下载链接 </li>
 * <li>infoUrl: 信息链接</li>
 * <li>upgradeDescription: 更新信息，换行用 \n 表示</li>
 * </ul>
 * @author janson
 *
 */
public class VersionUrlResponse {
    private String downloadUrl;
    private String infoUrl;
    private String upgradeDescription;
    
    public VersionUrlResponse() {
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public String getUpgradeDescription() {
        return upgradeDescription;
    }

    public void setUpgradeDescription(String upgradeDescription) {
        this.upgradeDescription = upgradeDescription;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
