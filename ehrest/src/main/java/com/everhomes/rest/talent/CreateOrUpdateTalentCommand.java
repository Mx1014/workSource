// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>ownerId: 所属id</li>
 * <li>organizationId: 管理公司id</li>
 * <li>id: id</li>
 * <li>name: 姓名</li>
 * <li>avatarUri: 头像</li>
 * <li>position: 职位</li>
 * <li>categoryId: 分类id</li>
 * <li>gender: 性别，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>experience: 经验年数</li>
 * <li>graduateSchool: 毕业院校</li>
 * <li>degree: 学历，参考{@link com.everhomes.rest.talent.TalentDegreeEnum}</li>
 * <li>phone: 联系电话</li>
 * <li>remark: 详细介绍</li>
 * </ul>
 */
public class CreateOrUpdateTalentCommand {
	@NotNull
	@Size(min=1)
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long organizationId;

	private Long id;
	@NotNull
	@Size(min=1)
	private String name;

	private String avatarUri;
	@NotNull
	@Size(min=1)
	private String position;
	@NotNull
	private Long categoryId;
	@NotNull
	private Byte gender;
	@NotNull
	private Integer experience;
	@NotNull
	@Size(min=1)
	private String graduateSchool;
	@NotNull
	private Byte degree;
	@NotNull
	@Size(min=1)
	private String phone;
	@NotNull
	@Size(min=1)
	private String remark;

	public CreateOrUpdateTalentCommand() {

	}

	public CreateOrUpdateTalentCommand(String ownerType, Long ownerId, Long organizationId, Long id, String name, String avatarUri, String position, Long categoryId, Byte gender, Integer experience, String graduateSchool, Byte degree, String phone, String remark) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.organizationId = organizationId;
		this.id = id;
		this.name = name;
		this.avatarUri = avatarUri;
		this.position = position;
		this.categoryId = categoryId;
		this.gender = gender;
		this.experience = experience;
		this.graduateSchool = graduateSchool;
		this.degree = degree;
		this.phone = phone;
		this.remark = remark;
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

	public String getAvatarUri() {
		return avatarUri;
	}

	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public Byte getDegree() {
		return degree;
	}

	public void setDegree(Byte degree) {
		this.degree = degree;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
