// @formatter:off
package com.everhomes.rest.launchpad;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>自定义列表
 * <li>id: itemId</li>
 * <li>orderIndex: 自定义item位置</li>
 * <li>applyPolicy: 自定义item策略，参考{@link com.everhomes.rest.launchpad.ApplyPolicy}</li>
 * <li>displayFlag: 自定义item是否显示，参考{@link com.everhomes.rest.launchpad.ItemDisplayFlag}</li>
 * </ul>
 */
public class Item {
    @NotNull
	private Long    id;
	private Integer orderIndex;
	@NotNull
	private Byte    applyPolicy;
	@NotNull
	private Byte    displayFlag;
    
	public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Byte getApplyPolicy() {
        return applyPolicy;
    }
    
    public void setApplyPolicy(Byte applyPolicy) {
        this.applyPolicy = applyPolicy;
    }
    
    public Byte getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Byte displayFlag) {
        this.displayFlag = displayFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
