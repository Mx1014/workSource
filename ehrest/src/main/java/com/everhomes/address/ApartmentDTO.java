// @formatter:off
package com.everhomes.address;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>addressId: 门牌号</li>
 * <li>familyId: 家庭ID，如果为0，则表示地址不存在家庭</li>
 * <li>apartmentName: 门牌号</li>
 * <li>address: 楼栋门牌地址</li>
  * <li>livingStatus: 地址入住状态 ，参考{@link com.everhomes.address.AddressLivingStatus}</li>
 * </ul>
 */
public class ApartmentDTO {
    private Long addressId;
    private Long familyId;
    private String apartmentName;
    private String address;
    private Byte livingStatus;
    
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
}
