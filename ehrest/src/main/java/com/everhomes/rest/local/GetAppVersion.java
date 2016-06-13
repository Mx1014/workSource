package com.everhomes.rest.local;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>downloadLink:下载链接</li>
 *<li>versionCode:版本号</li>
 *<li>versionName:版本名字</li>
 *<li>versionDescLink:版本release log</li>
 *<li>operation:是否需要强制升级</li>
 *<li>mktDataVersion:市场版本号</li>
 *<li>userLocRptFreq:用户位置信息上报频率，单位：秒</li>
 *<li>userContactRptFreq:用户通信录上报频率，单位：天</li>
 *<li>userRptConfigVersion:用户上报信息配置数据版本</li>
 */
public class GetAppVersion {
    private String downloadLink;
    private String versionCode;
    private String versionName;
    private String versionDescLink;
    private Integer operation;
    private Integer mktDataVersion;
    private Integer userLocRptFreq;
    private Integer userContactRptFreq;
    private Integer userRptConfigVersion;

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDescLink() {
        return versionDescLink;
    }

    public void setVersionDescLink(String versionDescLink) {
        this.versionDescLink = versionDescLink;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Integer getMktDataVersion() {
        return mktDataVersion;
    }

    public void setMktDataVersion(Integer mktDataVersion) {
        this.mktDataVersion = mktDataVersion;
    }

    public Integer getUserLocRptFreq() {
        return userLocRptFreq;
    }

    public void setUserLocRptFreq(Integer userLocRptFreq) {
        this.userLocRptFreq = userLocRptFreq;
    }

    public Integer getUserContactRptFreq() {
        return userContactRptFreq;
    }

    public void setUserContactRptFreq(Integer userContactRptFreq) {
        this.userContactRptFreq = userContactRptFreq;
    }

    public Integer getUserRptConfigVersion() {
        return userRptConfigVersion;
    }

    public void setUserRptConfigVersion(Integer userRptConfigVersion) {
        this.userRptConfigVersion = userRptConfigVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
