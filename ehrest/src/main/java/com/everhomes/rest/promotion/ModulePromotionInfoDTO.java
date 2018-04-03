//@formatter:off
package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>infoType: 信息流类型{@link com.everhomes.rest.promotion.ModulePromotionInfoType}</li>
 *     <li>content: 显示文字</li>
 *     <li>iconUrl: iconUrl</li>
 * </ul>
 */
public class ModulePromotionInfoDTO {

    private Byte infoType;
    private String iconUrl;
    private String content;

    public ModulePromotionInfoDTO(Byte infoType, String iconUrl, String content) {
        this.infoType = infoType;
        this.iconUrl = iconUrl;
        this.content = content;
    }

    public ModulePromotionInfoDTO() { }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getInfoType() {
        return infoType;
    }

    public void setInfoType(Byte infoType) {
        this.infoType = infoType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
