// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;

/**
 * <ul>自定义列表
 * <li>id: itemId</li>
 * <li>orderIndex: 自定义item位置</li>
 * <li>applyPolicy: 自定义item策略，参考{@link com.everhomes.launchpad.ApplyPolicy}</li>
 * </ul>
 */
public class Item {

	private Long    id;
	private Integer orderIndex;
	private Byte    applyPolicy;
    
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
