// @formatter:off
package com.everhomes.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 家庭Id</li>
 * <li>name: 家庭名称</li>
 * <li>memberCount: 家庭成员数</li>
 * <li>address: 家庭所在地址详情</li>
 * <li>communityId: 小区Id</li>
 * <li>communityName: 小区名称</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id（如南山区的Id）</li>
 * <li>areaName: 区域名称</li>
 * <li>livingStatus: 地址状态, {@link com.everhomes.address.AddressAdminStatus}</li>
 * </ul>
 */
public class PropFamilyDTO {
    private Long id;
    private String name;
    private Long memberCount;
    private Long addressId;
    private String address;
    private Byte livingStatus;
 
    public PropFamilyDTO () {
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

  
    public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Byte getLivingStatus() {
		return livingStatus;
	}

	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
