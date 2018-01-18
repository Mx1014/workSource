//@formatter:off
package com.everhomes.dynamicExcel;

/**
 * Created by Wentian Wang on 2018/1/18.
 */

public class DynamicColumnDTO {
    /**
     * @param value 给定行号和列号的数据，已经转为了string
     */
    private String value;
    /**
     * @param headerDisplay 对应的标题名称
     */
    private String headerDisplay;
    /**
     * @param columnNum 列号
     */
    private Integer columnNum;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeaderDisplay() {
        return headerDisplay;
    }

    public void setHeaderDisplay(String headerDisplay) {
        this.headerDisplay = headerDisplay;
    }

    public Integer getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(Integer columnNum) {
        this.columnNum = columnNum;
    }
}
