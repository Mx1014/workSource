package com.everhomes.rest.version;

import com.everhomes.util.StringHelper;

/**
 * <ul> 获取更新信息
 * <li>targetVersion: 目标版本</li>
 * <li>forceFlag: 是否强制更新 </li>
 * </ul>
 * @author janson
 *
 */
public class UpgradeInfoResponse {
    private VersionDTO targetVersion;
    private Byte forceFlag;
    
    public UpgradeInfoResponse() {
    }

    public VersionDTO getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(VersionDTO targetVersion) {
        this.targetVersion = targetVersion;
    }

    public Byte getForceFlag() {
        return forceFlag;
    }

    public void setForceFlag(Byte forceFlag) {
        this.forceFlag = forceFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
