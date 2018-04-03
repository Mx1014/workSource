//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/10/11.
 */
/**
 *<ul>
 * <li>formulaType:公式类型,1:固定金额;2:普通公式;3:斜面公式;4:梯度公式</li>
 * <li>formula:普通公式模式下新增的公式</li>
 * <li>stepValuePairs:区间和阶梯公式模式下的传值，参考{@link com.everhomes.rest.asset.VariableConstraints}</li>
 *</ul>
 */
public class CreateFormulaCommand {
    private Byte formulaType;
    @ItemType(VariableConstraints.class)
    private List<VariableConstraints> stepValuePairs;
    private Long chargingStandardId;
    private String formula;

    public Byte getFormulaType() {
        return formulaType;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }


    public List<VariableConstraints> getStepValuePairs() {
        return stepValuePairs;
    }

    public void setStepValuePairs(List<VariableConstraints> stepValuePairs) {
        this.stepValuePairs = stepValuePairs;
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }
}
