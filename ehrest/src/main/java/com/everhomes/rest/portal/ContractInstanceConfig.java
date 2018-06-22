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
	private Byte contractApplicationScene = 0;
	
	private String url;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
