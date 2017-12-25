package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>categoryId: categoryId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>displayName: displayName</li>
 *     <li>description: description</li>
 *     <li>arithmeticType: arithmeticType</li>
 *     <li>points: points</li>
 *     <li>limitType: limitType</li>
 *     <li>limitData: limitData</li>
 *     <li>bindingRuleId: bindingRuleId</li>
 *     <li>displayFlag: displayFlag</li>
 *     <li>status: status</li>
 *     <li>mappings: 积分指南和积分规则的映射 {@link com.everhomes.rest.point.CreatePointRuleToEventMappingCommand}</li>
 * </ul>
 */
public class CreatePointRuleCommand {

    private Long categoryId;
    private Long moduleId;
    private String displayName;
    private String description;
    private Byte arithmeticType;
    private Long points;
    private Byte limitType;
    private String limitData;
    private Long bindingRuleId;
    private Byte displayFlag;
    private Byte status;

    @ItemType(CreatePointRuleToEventMappingCommand.class)
    private List<CreatePointRuleToEventMappingCommand> mappings;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getArithmeticType() {
        return arithmeticType;
    }

    public void setArithmeticType(Byte arithmeticType) {
        this.arithmeticType = arithmeticType;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Byte getLimitType() {
        return limitType;
    }

    public void setLimitType(Byte limitType) {
        this.limitType = limitType;
    }

    public String getLimitData() {
        return limitData;
    }

    public void setLimitData(String limitData) {
        this.limitData = limitData;
    }

    public Long getBindingRuleId() {
        return bindingRuleId;
    }

    public void setBindingRuleId(Long bindingRuleId) {
        this.bindingRuleId = bindingRuleId;
    }

    public Byte getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Byte displayFlag) {
        this.displayFlag = displayFlag;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<CreatePointRuleToEventMappingCommand> getMappings() {
        return mappings;
    }

    public void setMappings(List<CreatePointRuleToEventMappingCommand> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
