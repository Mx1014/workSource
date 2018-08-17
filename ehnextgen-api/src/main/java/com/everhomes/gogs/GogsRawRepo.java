package com.everhomes.gogs;

import com.everhomes.util.StringHelper;
import com.google.gson.annotations.SerializedName;

public class GogsRawRepo {

    private Long id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("private")
    private boolean isPrivate;
    private boolean fork;
    @SerializedName("home_url")
    private String htmlURL;
    @SerializedName("clone_url")
    private String cloneURL;
    @SerializedName("ssh_url")
    private String sshURL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public String getHtmlURL() {
        return htmlURL;
    }

    public void setHtmlURL(String htmlURL) {
        this.htmlURL = htmlURL;
    }

    public String getCloneURL() {
        return cloneURL;
    }

    public void setCloneURL(String cloneURL) {
        this.cloneURL = cloneURL;
    }

    public String getSshURL() {
        return sshURL;
    }

    public void setSshURL(String sshURL) {
        this.sshURL = sshURL;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
