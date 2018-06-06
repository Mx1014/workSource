// @formatter:off
package com.everhomes.rest.sensitiveWord;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 上传敏感词库的url</li>
 * </ul>
 */
public class InitSensitiveWordTrieCommand {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
