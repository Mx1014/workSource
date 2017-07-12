package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId：机构ID</li>
 * <li>organizationName：机构名称</li>
 * <li>parentId：父机构ID</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>groupType：机构类别，需要首先用该值来判断是否是一个机构（其还有可能是部门、群组等），在该值为机构的情况下，OrganizationType才有意义，{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * <li>directlyEnterpriseId：如果本身是机构，则此值无效；如果本身是部门、群组（可能多层），则其层次结构上最近的一个直属公司</li>
 * <li>trees：子机构集合</li>
 * </ul>
 */
public class OrganizationTreeDTO {

	private Long organizationId;

	private Long directlyEnterpriseId;

	private String organizationName;

	private Long parentId;

	private String path;

	private String groupType;

	@ItemType(OrganizationTreeDTO.class)
	private List<OrganizationTreeDTO> trees;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getDirectlyEnterpriseId() {
		return directlyEnterpriseId;
	}

	public void setDirectlyEnterpriseId(Long directlyEnterpriseId) {
		this.directlyEnterpriseId = directlyEnterpriseId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public List<OrganizationTreeDTO> getTrees() {
		return trees;
	}

	public void setTrees(List<OrganizationTreeDTO> trees) {
		this.trees = trees;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
