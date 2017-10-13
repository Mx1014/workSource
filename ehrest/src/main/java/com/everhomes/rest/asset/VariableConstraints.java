//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/11.
 */
/**
 *<ul>
 * <li>variableIdentifier:变量标识</li>
 * <li>startNum:区间的最左边值</li>
 * <li>startConstraint:和左边临界值的关系， 1：大于；2：大于等于；3：小于；4：小于等于</li>
 * <li>endNum:区间的最右边值</li>
 * <li>endConstraint:和右边临界值的关系， 1：大于；2：大于等于；3：小于；4：小于等于</li>
 * <li>variableLimit:变量的条件临街值</li>
 * <li>formula:公式</li>
 *</ul>
 */
public class VariableConstraints {
    private String variableIdentifier;
    private String startNum;
    private Byte startConstraint;
    private String endNum;
    private Byte endConstraint;
    private String formula;

    public String getVariableIdentifier() {
        return variableIdentifier;
    }

    public void setVariableIdentifier(String variableIdentifier) {
        this.variableIdentifier = variableIdentifier;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public Byte getStartConstraint() {
        return startConstraint;
    }

    public void setStartConstraint(Byte startConstraint) {
        this.startConstraint = startConstraint;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }

    public Byte getEndConstraint() {
        return endConstraint;
    }

    public void setEndConstraint(Byte endConstraint) {
        this.endConstraint = endConstraint;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
