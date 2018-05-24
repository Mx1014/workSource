//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/9.
 */

public class AppTemplate {
    private Long appTemplateId;
    private String appTemplateStr;

    public AppTemplate(Long appTemplateId, String appTemplateStr) {
        this.appTemplateId = appTemplateId;
        this.appTemplateStr = appTemplateStr;
    }

    public Long getAppTemplateId() {
        return appTemplateId;
    }

    public void setAppTemplateId(Long appTemplateId) {
        this.appTemplateId = appTemplateId;
    }

    public String getAppTemplateStr() {
        return appTemplateStr;
    }

    public void setAppTemplateStr(String appTemplateStr) {
        this.appTemplateStr = appTemplateStr;
    }
}
