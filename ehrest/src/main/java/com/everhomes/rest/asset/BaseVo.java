//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class BaseVo<T> {

    private String sysid;
    private String sid;
    private String sidv;
    private String sign;
    private String token;
    private String encry;
    private String format;
    private String session;
    private String rc;
    private String ri;
    private T body;


    public String getSysid() {
        return sysid;
    }
    public void setSysid(String sysid) {
        this.sysid = sysid;
    }
    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getSidv() {
        return sidv;
    }
    public void setSidv(String sidv) {
        this.sidv = sidv;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getEncry() {
        return encry;
    }
    public void setEncry(String encry) {
        this.encry = encry;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getSession() {
        return session;
    }
    public void setSession(String session) {
        this.session = session;
    }
    public String getRc() {
        return rc;
    }
    public void setRc(String rc) {
        this.rc = rc;
    }
    public String getRi() {
        return ri;
    }
    public void setRi(String ri) {
        this.ri = ri;
    }
    public T getBody() {
        return body;
    }
    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "BaseVo [sysid=" + sysid + ", sid=" + sid + ", sidv=" + sidv
                + ", sign=" + sign + ", token=" + token + ", encry=" + encry
                + ", format=" + format + ", session=" + session + ", rc=" + rc
                + ", ri=" + ri + ", body=" + body + "]";
    }


}