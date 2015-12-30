// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 家庭Id</li>
 * <li>name: 家庭名称</li>
 * <li>memberCount: 家庭成员数</li>
 * <li>address: 家庭所在地址详情</li>
 * <li>addressId: 地址信息</li>
 * <li>livingStatus: 地址状态, 详见{@link com.everhomes.rest.organization.pm.PmMemberStatus}</li>
 * <li>owed: 是否欠费 , 详见{@link com.everhomes.rest.organization.pm.OwedType}</li>
 * </ul>
 */
public class PropFamilyDTO {
    private Long id;
    private String name;
    private Long memberCount;
    private Long addressId;
    private String address;
    private Byte livingStatus;
    private String enterpriseName;
    private Double areaSize;
    
    private Byte owed;
    
    public PropFamilyDTO () {
    }
    
    public Byte getOwed() {
		return owed;
	}

	public void setOwed(Byte owed) {
		this.owed = owed;
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
	
	

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
