// @formatter:off
package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ignoreParameters: 忽略参数列表</li>
 * </ul>
 */
public class ContentCacheConfigDTO {

    private List<String> ignoreParameters;

    public List<String> getIgnoreParameters() {
        return ignoreParameters;
    }

    public void setIgnoreParameters(List<String> ignoreParameters) {
        this.ignoreParameters = ignoreParameters;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
