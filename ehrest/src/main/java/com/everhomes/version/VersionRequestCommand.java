package com.everhomes.version;

import javax.validation.constraints.NotNull;

public class VersionRequestCommand {

    @NotNull
    private String realm;
    
    @NotNull
    private VersionDTO currentVersion;
    
    public VersionRequestCommand() {
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public VersionDTO getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(VersionDTO currentVersion) {
        this.currentVersion = currentVersion;
    }
}
