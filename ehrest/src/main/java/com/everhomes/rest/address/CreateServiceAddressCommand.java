// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>regionId: 区域id(城市或区县的id)</li>
 * <li>communityId: 小区id</li>
 * <li>address: 服务地址详情</li>
 * <li>contactType: 0- mobile, 1- email</li>
 * <li>contactToken: 电话号码或邮箱地址</li>
 * <li>contactName: 联系人名字</li>
 * </ul>
 */
public class CreateServiceAddressCommand {
    
//    @NotNull
//    private Long cityId;
//    
    @NotNull
//    private Long areaId;
    private Long regionId;
    
    private Long communityId;
    
    @NotNull
    private String address;
    
    private Byte contactType;
    
    private String contactToken;
    
    private String contactName;


    public CreateServiceAddressCommand() {
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
