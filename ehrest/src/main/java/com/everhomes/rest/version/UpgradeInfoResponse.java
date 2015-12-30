package com.everhomes.rest.version;

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
}
