package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

public class YzbVideoDeviceChangeCommand {
    //livestop / livestart
    private String optcode;
    private String devid;
    private String yzb_user_id;
    private String vid;
    private String app_nonce;
    
    public String getOptcode() {
        return optcode;
    }
    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }
    public String getDevid() {
        return devid;
    }
    public void setDevid(String devid) {
        this.devid = devid;
    }
    public String getYzb_user_id() {
        return yzb_user_id;
    }
    public void setYzb_user_id(String yzb_user_id) {
        this.yzb_user_id = yzb_user_id;
    }
    public String getVid() {
        return vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public String getApp_nonce() {
        return app_nonce;
    }
    public void setApp_nonce(String app_nonce) {
        this.app_nonce = app_nonce;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
