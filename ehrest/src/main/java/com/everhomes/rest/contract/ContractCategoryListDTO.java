package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>categoryId: 合同应用id</li>
 *     <li>name: 合同应用名称</li>
 *     <li>contractApplicationScene: 合同应用适用场景</li>
 * </ul>
 * Created by djm on 2018/8/30.
 */
public class ContractCategoryListDTO {

	private Integer namespaceId;
	private Long communityId;
	private Long categoryId;
	private String name;
	private Byte contractApplicationScene;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getContractApplicationScene() {
		return contractApplicationScene;
	}

	public void setContractApplicationScene(Byte contractApplicationScene) {
		this.contractApplicationScene = contractApplicationScene;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
