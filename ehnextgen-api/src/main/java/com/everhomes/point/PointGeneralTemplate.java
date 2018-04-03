package com.everhomes.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>messageTitle: messageTitle</li>
 *     <li>resetPointDesc: resetPointDesc</li>
 *     <li>resetPointCate: resetPointCate</li>
 *     <li>exportLogTitle: exportLogTitle</li>
 *     <li>exportLogFileName: exportLogFileName</li>
 * </ul>
 */
public class PointGeneralTemplate {

    private String messageTitle;
    private String resetPointDesc;
    private String resetPointCate;
    private String exportLogTitle;
    private String exportLogFileName;

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

    public String getExportLogTitle() {
        return exportLogTitle;
    }

    public void setExportLogTitle(String exportLogTitle) {
        this.exportLogTitle = exportLogTitle;
    }

    public String getExportLogFileName() {
        return exportLogFileName;
    }

    public void setExportLogFileName(String exportLogFileName) {
        this.exportLogFileName = exportLogFileName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
