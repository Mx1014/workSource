//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/10.
 */
/**
 *<ul>
 * <li>configs:催缴的设置列表,参考{@link com.everhomes.rest.asset.NoticeConfig}</li>
 * <li>msgTemplates:List<MsgTemplate>, 参考{@link MsgTemplate}</li>
 * <li>appTemplates:List<AppTemplate>, 参考{@link AppTemplate}</li>
 *</ul>
 */
public class ListAutoNoticeConfigResponse {
    @ItemType(NoticeConfig.class)
    private List<NoticeConfig> configs;
    private List<MsgTemplate> msgTemplates;
    private List<AppTemplate> appTemplates;

    public List<AppTemplate> getAppTemplates() {
        return appTemplates;
    }

    public void setAppTemplates(List<AppTemplate> appTemplates) {
        this.appTemplates = appTemplates;
    }

    public List<MsgTemplate> getMsgTemplates() {

        return msgTemplates;
    }

    public void setMsgTemplates(List<MsgTemplate> msgTemplates) {
        this.msgTemplates = msgTemplates;
    }

    public List<NoticeConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<NoticeConfig> configs) {
        this.configs = configs;
    }
}
