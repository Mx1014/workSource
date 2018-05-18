// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>contentId: 内容Id。此处为String，因为可能某些业务的内容id是String，例如新闻快讯</li>
 *     <li>properties: 需要展现的一些字段，需要和客户端预定字段和顺序，推荐按照显示的从上到下、从左到右显示放置properties</li>
 * </ul>
 */
public class OPPushCard {

    private String contentId;

    private List<Object> properties;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public List<Object> getProperties() {
        return properties;
    }

    public void setProperties(List<Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
