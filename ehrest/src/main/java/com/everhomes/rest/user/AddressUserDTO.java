package com.everhomes.rest.user;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.CommunityInfoDTO;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationSiteDTO;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>id: 公司id或者家庭id</li>
 *     <li>type: 类型，0-家庭、1-公司，参考{@link AddressUserType}</li>
 *     <li>name: 显示名称</li>
 *     <li>aliasName: 别名</li>
 *     <li>avatarUri: 头像Uri</li>
 *     <li>avatarUrl: 显示头像URL</li>
 *     <li>fullPinyin: 拼音全拼</li>
 *     <li>capitalPinyin: capitalPinyin</li>
 *     <li>status: status  {@link GroupMemberStatus}</li>
 *     <li>workPlatformFlag: 是否开启工作台标志，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>managerFlag: 是否为公司管理员，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>addressSiteDtos: 公司办公点或者家庭对应的信息参考{@link AddressSiteDTO}</li>
 *     <li>communityInfoDtos: 所在园区列表 {@link com.everhomes.rest.community.CommunityInfoDTO}</li>
 * </ul>
 */
public class AddressUserDTO {
    private Long id;
    private Byte type;
    private String name;
    private String aliasName;
    private String avatarUri;
    private String avatarUrl;
    private String fullPinyin;
    private String capitalPinyin;
    private Byte status;
    private Byte workPlatformFlag;

    //地址列表需要的默认是否是这个公司的管理员字段
    private Byte managerFlag;

    private List<AddressSiteDTO> addressSiteDtos;
    private List<CommunityInfoDTO> communityInfoDtos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFullPinyin() {
        return fullPinyin;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getCapitalPinyin() {
        return capitalPinyin;
    }

    public void setCapitalPinyin(String capitalPinyin) {
        this.capitalPinyin = capitalPinyin;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getWorkPlatformFlag() {
        return workPlatformFlag;
    }

    public void setWorkPlatformFlag(Byte workPlatformFlag) {
        this.workPlatformFlag = workPlatformFlag;
    }

    public List<AddressSiteDTO> getAddressSiteDtos() {
        return addressSiteDtos;
    }

    public void setAddressSiteDtos(List<AddressSiteDTO> addressSiteDtos) {
        this.addressSiteDtos = addressSiteDtos;
    }

    public Byte getManagerFlag() {
        return managerFlag;
    }

    public void setManagerFlag(Byte managerFlag) {
        this.managerFlag = managerFlag;
    }

    public List<CommunityInfoDTO> getCommunityInfoDtos() {
        return communityInfoDtos;
    }

    public void setCommunityInfoDtos(List<CommunityInfoDTO> communityInfoDtos) {
        this.communityInfoDtos = communityInfoDtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
