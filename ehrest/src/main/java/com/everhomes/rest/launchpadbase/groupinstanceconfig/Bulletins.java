package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>rowCount: 行数</li>
 *     <li>noticeCount: 公告条数</li>
 *     <li>style: 样式1-方形，2-圆形</li>
 *     <li>iconUri: iconUri</li>
 *     <li>iconUrl: iconUrl</li>
 *     <li>shadow: 是否有底部阴影 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>moduleId: moduleId</li>
 *     <li>appId: appId</li>
 * </ul>
 */
public class Bulletins {

    private String itemGroup;

    private Integer rowCount;

    private Integer noticeCount;

    private Byte style;

    private String iconUri;

    private String iconUrl;

    private Byte shadow;

    private Long moduleId;

    private Long appId;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(Integer noticeCount) {
        this.noticeCount = noticeCount;
    }

    public Byte getStyle() {
        return style;
    }

    public void setStyle(Byte style) {
        this.style = style;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Byte getShadow() {
        return shadow;
    }

    public void setShadow(Byte shadow) {
        this.shadow = shadow;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
