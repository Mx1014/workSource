//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/12.
 */

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 * <li>formulaType:公式类型,1:固定金额;2:普通公式;3:斜率跟着变量区间总体变化;4:斜率在不同变量区间取值不同;</li>
 * <li>formulaIds:公式id的集合</li>
 *</ul>
 */
public class CreateFormulaDTO {
    private Byte formulaType;
    @ItemType(Long.class)
    private List<Long> formulaIds;

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    public List<Long> getFormulaIds() {
        return formulaIds;
    }

    public void setFormulaIds(List<Long> formulaIds) {
        this.formulaIds = formulaIds;
    }
}
