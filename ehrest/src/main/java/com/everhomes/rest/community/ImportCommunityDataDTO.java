// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 小区名称</li>
 * <li>communityNumber: 园区编号</li>
 * <li>aliasName: 小区别名</li>
 * <li>cityName: 城市名称</li>
 * <li>areaName: 区域名称</li>
 * <li>address: 小区地址</li>
 * <li>areaSize: 面积</li>
 * <li>rentArea: 出租面积</li>
 * <li>provinceName: 省市名称</li>
 * 
 * <li>id: 小区Id</li>
 * <li>uuid: 小区uuid，作为唯一标识</li>
 * <li>cityId: 城市Id</li>
 * <li>areaId: 区域Id</li>
 * <li>areaName: 区域名称</li>
 * <li>zipcode: 邮政编码</li>
 * <li>description: 简略描述</li>
 * <li>detailDescription: 详细描述</li>
 * <li>aptCount: 公寓数</li>
 * <li>creatorUid: 创建者Id</li>
 * <li>status: 小区状态，参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteTime: 删除时间</li>
 * <li>requestStatus: 小区收集状态，参考{@link com.everhomes.rest.community.RequestStatus}</li>
 * <li>geoPointList: 小区经纬度列表，参考{@link com.everhomes.rest.community.CommunityGeoPointDTO}</li>
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>defaultForumId: 默认论坛ID，每个园区都有一个自己的默认论坛用于放园区整体的帖子（如公告）</li>
 * <li>feedbackForumId: 意见论坛ID，每个园区都有一个自己的意见反馈论坛用于放园区意见反馈帖子</li>
 * <li>areaSize: 面积</li>
 * <li>sharedArea: 公摊面积</li>
 * <li>chargeArea: 收费面积</li>
 * <li>buildArea: 建筑面积</li>
 * <li>updateTime: 更新时间</li>
 * <li>categoryId: 分类id</li>
 * <li>categoryName: 分类名称</li>
 * <li>communityUserCount : 统计人数</li>
 * <li>firstLatterOfName: 名字首字母</li>
 * </ul>
 */

public class ImportCommunityDataDTO {
	private String name;
	private String communityNumber;
	private String aliasName;
	private String provinceName;
	private String cityName;
	private String areaName;
	private String address;
	private String areaSize;
	private String rentArea;
	private Long cityId;
    private Long areaId;

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCommunityNumber() {
		return communityNumber;
	}

	public void setCommunityNumber(String communityNumber) {
		this.communityNumber = communityNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(String areaSize) {
		this.areaSize = areaSize;
	}

	public String getRentArea() {
		return rentArea;
	}

	public void setRentArea(String rentArea) {
		this.rentArea = rentArea;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
