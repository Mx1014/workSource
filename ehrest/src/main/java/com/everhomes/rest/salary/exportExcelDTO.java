package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>excelName: 表名</li>
 * <li>exportToken: 导出token</li>
 * </ul>
 */
public class ExportExcelDTO {

    private String excelName;
    private String exportToken;
    public ExportExcelDTO() {

    }

    public ExportExcelDTO(String excelName, String exportToken) {
        this.excelName = excelName;
        this.exportToken = exportToken;
    }
    @Override

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
