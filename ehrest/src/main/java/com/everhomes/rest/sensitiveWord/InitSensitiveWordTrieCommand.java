// @formatter:off
package com.everhomes.rest.sensitiveWord;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 上传敏感词库的url</li>
 *     <li>fileName: 上传的文件名称</li>
 * </ul>
 */
public class InitSensitiveWordTrieCommand {

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
