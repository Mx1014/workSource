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
 *</ul>
 */
public class ListAutoNoticeConfigResponse {
    @ItemType(NoticeConfig.class)
    private List<NoticeConfig> configs;

    public List<NoticeConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<NoticeConfig> configs) {
        this.configs = configs;
    }
}
