package com.everhomes.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>messageTitle: messageTitle</li>
 *     <li>resetPointDesc: resetPointDesc</li>
 *     <li>resetPointCate: resetPointCate</li>
 * </ul>
 */
public class PointGeneralTemplate {

    private String messageTitle;
    private String resetPointDesc;
    private String resetPointCate;

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getResetPointDesc() {
        return resetPointDesc;
    }

    public void setResetPointDesc(String resetPointDesc) {
        this.resetPointDesc = resetPointDesc;
    }

    public String getResetPointCate() {
        return resetPointCate;
    }

    public void setResetPointCate(String resetPointCate) {
        this.resetPointCate = resetPointCate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
