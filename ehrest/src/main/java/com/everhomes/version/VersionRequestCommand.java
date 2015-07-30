package com.everhomes.version;

import javax.validation.constraints.NotNull;

public class VersionRequestCommand {

    @NotNull
    private String realm;
    
    @NotNull
    private VersionDTO currentVersion;
    
    private String locale;
    
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
