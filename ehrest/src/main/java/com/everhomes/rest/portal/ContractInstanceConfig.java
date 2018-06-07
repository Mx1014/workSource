// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>categoryId: 合同分类id</li>
 * <li>contractApplicationScene: 0 租赁合同场景 1 物业合同场景 2 综合合同场景</li>
 * </ul>
 */
public class ContractInstanceConfig {

	private Long categoryId;
	private Byte contractApplicationScene;

	public Byte getContractApplicationScene() {
		return contractApplicationScene;
	}

	public void setContractApplicationScene(Byte contractApplicationScene) {
		this.contractApplicationScene = contractApplicationScene;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
