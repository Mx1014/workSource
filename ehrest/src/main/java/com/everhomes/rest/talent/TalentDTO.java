// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>name: 姓名</li>
 * <li>avatarUrl: 头像url</li>
 * <li>avatarUri: 头像uri</li>
 * <li>phone: 电话</li>
 * <li>gender: 性别，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>position: 职位</li>
 * <li>categoryId: 分类id</li>
 * <li>categoryName: 分类名称</li>
 * <li>experience: 经验</li>
 * <li>graduateSchool: 毕业院校</li>
 * <li>degree: 学历，参考{@link com.everhomes.rest.talent.TalentDegreeEnum}</li>
 * <li>remark: 详情</li>
 * <li>enabled: 是否打开，1是0否，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class TalentDTO {
	private Long id;
	private String name;
	private String avatarUrl;
	private String avatarUri;
	private String phone;
	private Byte gender;
	private String position;
	private Long categoryId;
	private String categoryName;
	private Integer experience;
	private String graduateSchool;
	private Byte degree;
	private String remark;
	private Byte enabled;

	public String getAvatarUri() {
		return avatarUri;
	}

	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
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

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getEnabled() {
		return enabled;
	}

	public void setEnabled(Byte enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
