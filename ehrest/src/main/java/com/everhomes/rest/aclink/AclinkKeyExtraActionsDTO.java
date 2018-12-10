// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>extraActionTitle 扩展操作标题</li>
 * <li>extraActionType 扩展操作类型 {@link com.everhomes.rest.aclink.AclinkExtraActionsItemType}</li>
 * <li>extraActionContent 扩展操作内容</li>
 * </ul>
 * @author liqingyan
 *
 */
public class AclinkKeyExtraActionsDTO {

    private String extraActionTitle;
    private Byte extraActionType;
    private String extraActionContent;

    public String getExtraActionTitle() {
        return extraActionTitle;
    }

    public void setExtraActionTitle(String extraActionTitle) {
        this.extraActionTitle = extraActionTitle;
    }

    public Byte getExtraActionType() {
        return extraActionType;
    }

    public void setExtraActionType(Byte extraActionType) {
        this.extraActionType = extraActionType;
    }

    public String getExtraActionContent() {
        return extraActionContent;
    }

    public void setExtraActionContent(String extraActionContent) {
        this.extraActionContent = extraActionContent;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
