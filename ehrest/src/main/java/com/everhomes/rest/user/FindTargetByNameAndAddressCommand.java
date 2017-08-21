//@formatter:off
package com.everhomes.rest.user;

/**
 * Created by Wentian Wang on 2017/8/19.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>targetName:客户名称</li>
 * <li>buildingName:楼栋名称</li>
 * <li>apartmentName:门牌名称</li>
 * <li>communityId:园区id</li>
 * <li>tel:个人用户电话</li>
 *</ul>
 */
public class FindTargetByNameAndAddressCommand {
    private String targetName;
    private String buildingName;
    private String apartmentName;
    private Long communityId;
    private String tel;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
