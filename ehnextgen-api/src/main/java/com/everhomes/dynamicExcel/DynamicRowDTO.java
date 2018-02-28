//@formatter:off
package com.everhomes.dynamicExcel;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/18.
 */

public class DynamicRowDTO {
    /**
     * @param values 该行下的列数据
     */
    private List<DynamicColumnDTO> columns;
    /**
     * @param rowNum 行号
     */
    private Integer rowNum;


    public List<DynamicColumnDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<DynamicColumnDTO> columns) {
        this.columns = columns;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
}
