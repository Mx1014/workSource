// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entryId: 入口ID</li>
 *     <li>entryCategory: 入口分类</li>
 *     <li>entryName: 入口名称</li>
 *     <li>entryIconUri: 入口图标uri</li>
 *     <li>entryIconUrl: 入口图标url</li>
 * </ul>
 */
public class AppEntryDTO {
    private Long entryId;

    private Byte entryCategory;

    private String entryName;

    private String entryIconUri;

    private String entryIconUrl;

    public String getEntryIconUrl() {
        return entryIconUrl;
    }

    public void setEntryIconUrl(String entryIconUrl) {
        this.entryIconUrl = entryIconUrl;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Byte getEntryCategory() {
        return entryCategory;
    }

    public void setEntryCategory(Byte entryCategory) {
        this.entryCategory = entryCategory;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryIconUri() {
        return entryIconUri;
    }

    public void setEntryIconUri(String entryIconUri) {
        this.entryIconUri = entryIconUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
