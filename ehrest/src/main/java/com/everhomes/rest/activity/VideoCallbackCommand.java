// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

public class VideoCallbackCommand {

    private String appid;

    private String lid;

    private String module;

    private String from;

    private Integer state;

    private String fid;

    private String size;

    private String dura;

    private String msg;

    public VideoCallbackCommand() {
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDura() {
        return dura;
    }

    public void setDura(String dura) {
        this.dura = dura;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
