package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 门牌名</li>
 *     <li>buildingName: 楼栋名</li>
 *     <li>livingStatus: 门牌状态 参考{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>orientation: 朝向</li>
 *     <li>relatedArrangementBeginDate: 关联的拆分合并计划的生效日期</li>
 *     <li>communityName: 所在园区名</li>
 * </ul>
 * Created by ying.xiong on 2017/8/18.
 */
public class ApartmentAbstractDTO {

    private Long id;

    private String name;

    private String buildingName;

    private Byte livingStatus;

    private Double chargeArea;

    private String orientation;
    
    private Long relatedArrangementBeginDate;
    
    private Byte isFutureApartment;

    private String communityName;

    private Long communityId;
    
    public Byte getIsFutureApartment() {
		return isFutureApartment;
	}

	public void setIsFutureApartment(Byte isFutureApartment) {
		this.isFutureApartment = isFutureApartment;
	}

	public Long getRelatedArrangementBeginDate() {
		return relatedArrangementBeginDate;
	}

	public void setRelatedArrangementBeginDate(Long relatedArrangementBeginDate) {
		this.relatedArrangementBeginDate = relatedArrangementBeginDate;
	}

	public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Double getChargeArea() {
        return chargeArea;
    }

    public void setChargeArea(Double chargeArea) {
        this.chargeArea = chargeArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
