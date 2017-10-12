//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/12.
 */
/**
 *<ul>
 * <li>formulaType:公式类型,1:固定金额;2:普通公式;3:斜率跟着变量区间总体变化;4:斜率在不同变量区间取值不同;</li>
 * <li>formulaJson:公式的json</li>
 * <li>formula:公式</li>
 *</ul>
 */
public class CreateFormulaDTO {
    private Byte formulaType;
    private String formulaJson;
    private String formula;

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    public String getFormulaJson() {
        return formulaJson;
    }

    public void setFormulaJson(String formulaJson) {
        this.formulaJson = formulaJson;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
