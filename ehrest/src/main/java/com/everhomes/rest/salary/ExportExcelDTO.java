package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>excelName: 表名</li>
 * <li>exportToken: 导出token</li>
 * <li>url: 下载链接</li>
 * </ul>
 */
public class ExportExcelDTO {

    private String excelName;
    private String exportToken;
    private String url;
    public ExportExcelDTO() {
    }

    public ExportExcelDTO(String excelName, String exportToken,String url) {
        this.excelName = excelName;
        this.exportToken = exportToken;
        this.url = url;
    }

    public ExportExcelDTO(String excelName, String exportToken) {
        this.excelName = excelName;
        this.exportToken = exportToken;
    }

    @Override

    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getExportToken() {
        return exportToken;
    }

    public void setExportToken(String exportToken) {
        this.exportToken = exportToken;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
