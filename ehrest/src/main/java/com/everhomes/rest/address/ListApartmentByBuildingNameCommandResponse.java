package com.everhomes.rest.address;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>apartmentLivingCount：存在用户的门牌数</li>
 * <li>apartmentList: apartment信息，参考{@link com.everhomes.rest.address.ApartmentDTO}</li>
 * </ul>
 */
public class ListApartmentByBuildingNameCommandResponse {
    private Integer apartmentLivingCount;

    @ItemType(ApartmentDTO.class)
    private List<ApartmentDTO> apartmentList;
    
    public ListApartmentByBuildingNameCommandResponse() {
    }

    public Integer getApartmentLivingCount() {
        return apartmentLivingCount;
    }

    public void setApartmentLivingCount(Integer apartmentLivingCount) {
        this.apartmentLivingCount = apartmentLivingCount;
    }

    public List<ApartmentDTO> getApartmentList() {
        return apartmentList;
    }

    public void setApartmentList(List<ApartmentDTO> apartmentList) {
        this.apartmentList = apartmentList;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
