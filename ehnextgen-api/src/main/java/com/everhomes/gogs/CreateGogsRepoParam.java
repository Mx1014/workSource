package com.everhomes.gogs;

import com.everhomes.util.StringHelper;
import com.google.gson.annotations.SerializedName;

public class CreateGogsRepoParam {

    private String name;
    private String description;
    @SerializedName("private")
    private boolean privateFlag;
    private String readme;
    @SerializedName("auto_init")
    private boolean autoInit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(boolean privateFlag) {
        this.privateFlag = privateFlag;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    public boolean isAutoInit() {
        return autoInit;
    }

    public void setAutoInit(boolean autoInit) {
        this.autoInit = autoInit;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
