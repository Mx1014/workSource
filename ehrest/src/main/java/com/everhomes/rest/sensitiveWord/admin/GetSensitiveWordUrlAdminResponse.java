// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 敏感词文本URL</li>
 *     <li>fileName: 敏感词文本名称</li>
 * </ul>
 */
public class GetSensitiveWordUrlAdminResponse {

    private String url;

    private String fileName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
