// @formatter:off
package com.everhomes.rest.appwhitelist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>link: 第三方应用链接</li>
 *     <li>name: 第三方名称</li>
 * </ul>
 */
public class AppWhiteListDTO {

    private String link;

    private String name;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
