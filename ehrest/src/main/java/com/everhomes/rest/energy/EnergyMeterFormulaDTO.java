package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 公式id</li>
 *     <li>name: 公式名称</li>
 *     <li>expression: 公式</li>
 *     <li>formulaType: 公式类型 {@link com.everhomes.rest.energy.EnergyFormulaType}</li>
 *     <li>communityId: 公式所属项目id，域空间直属的则为0或无</li>
 *     <li>communityName: 所属（应用）项目列表</li>
 * </ul>
 */
public class EnergyMeterFormulaDTO {

    private Long id;
    private String name;
    private String expression;
    private Byte formulaType;
    private Long communityId;

    @ItemType(String.class)
    private List<String> communityName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<String> getCommunityName() {
        return communityName;
    }

    public void setCommunityName(List<String> communityName) {
        this.communityName = communityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
