// @formatter:off
package com.everhomes.rest.address;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId: 门牌号</li>
 * <li>familyId: 家庭ID，如果为0，则表示地址不存在家庭</li>
 * <li>apartmentName: 门牌号</li>
 * <li>address: 楼栋门牌地址</li>
  * <li>livingStatus: 地址入住状态 ，参考{@link com.everhomes.rest.address.AddressLivingStatus}</li>
 * </ul>
 */
public class ApartmentDTO implements Comparable<ApartmentDTO> {
    private Long addressId;
    private Long familyId;
    private String apartmentName;
    private String businessApartmentName;
    private String address;
    private Byte livingStatus;
    private Double areaSize;
    private Double rentArea;
    private Double freeArea;
    private Double chargeArea;
    private String enterpriseName;
    private String apartmentFloor;
    private Byte isLived;
    private String orientation;
	private String namespaceAddressType;
	private String namespaceAddressToken;

    public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getNamespaceAddressType() {
		return namespaceAddressType;
	}

	public void setNamespaceAddressType(String namespaceAddressType) {
		this.namespaceAddressType = namespaceAddressType;
	}

	public String getNamespaceAddressToken() {
		return namespaceAddressToken;
	}

	public void setNamespaceAddressToken(String namespaceAddressToken) {
		this.namespaceAddressToken = namespaceAddressToken;
	}

	public Double getRentArea() {
		return rentArea;
	}

	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}

	public Double getFreeArea() {
		return freeArea;
	}

	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public String getBusinessApartmentName() {
		return businessApartmentName;
	}

	public void setBusinessApartmentName(String businessApartmentName) {
		this.businessApartmentName = businessApartmentName;
	}

	public ApartmentDTO() {
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

    public java.lang.String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(java.lang.String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    
    
    
    public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

    public Byte getIsLived() {
        return isLived;
    }

    public void setIsLived(Byte isLived) {
        this.isLived = isLived;
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof ApartmentDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	@Override
	public int compareTo(ApartmentDTO o) {
		int f1 = parseInt(getApartmentFloor());
		int f2 = parseInt(o.getApartmentFloor());
		if (f1 == f2) {
			return getApartmentName().compareTo(o.getApartmentName());
		}
		return f1 - f2;
	}
	
	private int parseInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			return 0;
		}
	}
}
