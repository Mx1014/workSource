package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为APPOPEN_APP时启动第三方应用
 * <li>pkg: 应用包名/li>
 * <li>download: 应用下载链接/li>
 * </ul>
 */
public class ApplaunchAppActionData implements Serializable{

    private static final long serialVersionUID = -5364366676212368720L;
    //{"iosEmbedded_json":{"appPackage":"mqq:open","download":"www.xx.com"},"androidEmbedded_json":{"appPackage":"mqq:open","download":"www.xx.com"}}
    private String pkg;
    private String download;
    
    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
