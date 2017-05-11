// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>ownerId: 所属id</li>
 * <li>organizationId: 管理公司id</li>
 * <li>categoryId: 分类id</li>
 * <li>gender: 性别，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>experience: 经验，参考{@link com.everhomes.rest.talent.TalentExperienceConditionEnum}</li>
 * <li>degree: 学历，参考{@link com.everhomes.rest.talent.TalentDegreeConditionEnum}</li>
 * <li>keyword: 关键词</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListTalentCommand {

	private String ownerType;

	private Long ownerId;

	private Long organizationId;

	private Long categoryId;

	private Byte gender;

	private Byte experience;

	private Byte degree;

	private String keyword;

	private Long pageAnchor;

	private Integer pageSize;

	public ListTalentCommand() {

	}

	public ListTalentCommand(String ownerType, Long ownerId, Long organizationId, Long categoryId, Byte gender, Byte experience, Byte degree, String keyword, Long pageAnchor, Integer pageSize) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.organizationId = organizationId;
		this.categoryId = categoryId;
		this.gender = gender;
		this.experience = experience;
		this.degree = degree;
		this.keyword = keyword;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public Byte getExperience() {
		return experience;
	}

	public void setExperience(Byte experience) {
		this.experience = experience;
	}

	public Byte getDegree() {
		return degree;
	}

	public void setDegree(Byte degree) {
		this.degree = degree;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
