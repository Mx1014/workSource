package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>excelName: 表名</li>
 * <li>exportToken: 导出token</li>
 * </ul>
 */
public class exportExcelDTO {

    private String excelName;
    private String exportToken;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
