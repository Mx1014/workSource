package com.everhomes.parking.jinyi;

import com.everhomes.util.StringHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JinyiRequestParam {
    private String methodname;
    private String appid;
    private String timestamp;
    private String version;
    private String sign;
    private String postdata;

    public JinyiRequestParam() {
        this.version = "1.0";

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = localDateTime.format(dtf);
        this.timestamp = "timestamp";
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPostdata() {
        return postdata;
    }

    public void setPostdata(String postdata) {
        this.postdata = postdata;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
