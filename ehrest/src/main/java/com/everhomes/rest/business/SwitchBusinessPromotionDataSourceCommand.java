// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>source: 数据源 biz：从电商那里获取数据， db: 从数据库获取数据</li>
 * </ul>
 */
public class SwitchBusinessPromotionDataSourceCommand {

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
