// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>communityId: 小区id</li>
 *     <li>orgId: orgId</li>
 * </ul>
 */
public class OPPushCards {

    private String style;

    private String title;

    private List<String> listCardJson;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getListCardJson() {
        return listCardJson;
    }

    public void setListCardJson(List<String> listCardJson) {
        this.listCardJson = listCardJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
