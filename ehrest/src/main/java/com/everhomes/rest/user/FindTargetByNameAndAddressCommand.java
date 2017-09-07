//@formatter:off
package com.everhomes.rest.user;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>targetName:客户名称</li>
 * <li>targetType:客户类型,eh_user个人;eh_organization企业</li>
 * <li>buildingName:楼栋名称</li>
 * <li>apartmentName:门牌名称</li>
 * <li>ownerId:园区id</li>
 * <li>ownerType:类型，园区为community</li>
 * <li>tel:个人用户电话</li>
 * <li>contractNum:合同编号</li>
 *</ul>
 */
public class FindTargetByNameAndAddressCommand {
    private String targetName;
    private String targetType;
    private String buildingName;
    private String apartmentName;
    private Long ownerId;
    private String ownerType;
    private String tel;
    private String contractNum;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public FindTargetByNameAndAddressCommand() {
    }

    public String getTargetName() {

        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
