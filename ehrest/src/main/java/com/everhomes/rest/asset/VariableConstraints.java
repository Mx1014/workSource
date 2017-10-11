//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/11.
 */
/**
 *<ul>
 * <li>variableId:变量id</li>
 * <li>contrants:变量的关系， 1：大于；2：大于等于；3：小于；4：小于等于</li>
 * <li>formula:公式</li>
 *</ul>
 */
public class VariableConstraints {
    private Long variableId;
    private Byte contrants;
    private String formula;

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public Byte getContrants() {
        return contrants;
    }

    public void setContrants(Byte contrants) {
        this.contrants = contrants;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
