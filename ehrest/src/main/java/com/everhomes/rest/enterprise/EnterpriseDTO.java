package com.everhomes.rest.enterprise;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.address.AddressDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul> 公司信息
 * <li>id: 公司ID</li>
 * <li>name: 公司名称</li>
 * <li>displayName: 公司显示名称</li>
 * <li>avatarUri: 公司LOGO URI</li>
 * <li>avatarUrl: 公司LOGO URL</li>
 * <li>description: 公司详细描述</li>
 * <li>contactCount: 通讯录人数</li>
 * <li>contactCount: 通讯录人数</li>
 * <li>userId: 公司通讯录成员对应的用户ID，要在公司认证过的用户才有</li>
 * <li>tag: 标签</li>
 * <li>contactOf: 是否是公司通讯录成员，1-是(成员状态为待认证时也置为1，也就是服务器有记录则为1，还需要根据<code>contactStatus</code>来判断是否是认证成员)、0-否</li>
 * <li>contactStatus: 公司通讯录成员状态，{@link com.everhomes.rest.organization.OrganizationMemberStatus}</li>
 * <li>contactNickName: 公司通讯录成员在公司内的昵称，是公司通讯录成员时字段才有效</li>
 * <li>contactRole: 公司通讯录成员角色，用于判断是否为管理员，参考{@link com.everhomes.rest.acl.RoleConstants}</li>
 * <li>creatorUid: 公司实体创建者ID</li>
 * <li>creatorName: 公司实体创建者名称</li>
 * <li>contactGroupPrivileges: 公司通讯录成员的权限列表，参考{@link com.everhomes.rest.acl.PrivilegeConstants}</li>
 * <li>contactForumPrivileges: 公司通讯录成员的论坛权限列表，参考{@link com.everhomes.rest.acl.PrivilegeConstants}</li>
 * <li>updateTime: 公司实体更新时间</li>
 * <li>createTime: 公司实体创建时间</li>
 * <li>address: 公司所在地址详情</li>
 * <li>communityId: 园区Id</li>
 * <li>communityName: 园区名称</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id（如南山区的Id）</li>
 * <li>areaName: 区域名称</li>
 * <li>contactsPhone: 公司联系电话</li>
 * <li>contactor: 公司联系人</li>
 * <li>entries: 公司联系人手机号</li>
 * <li>enterpriseAddress: 公司地址</li>
 * <li>enterpriseCheckinDate: 公司入驻时间</li>
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>defaultForumId: 默认论坛ID，每个园区都有一个自己的默认论坛用于放园区整体的帖子（如公告）</li>
 * <li>feedbackForumId: 意见论坛ID，每个园区都有一个自己的意见反馈论坛用于放园区意见反馈帖子</li>
 * <li>postUri: 标题图uri</li>
 * <li>postUrl: 标题图url</li>
 * <li>emailDomain: 邮箱域名</li>
 * <li>adminMembers: 管理员列表，参考{@link com.everhomes.rest.organization.OrganizationContactDTO}</li>
 * <li>appDtos: 授权应用，参考{@link ServiceModuleAppDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class EnterpriseDTO {
    private Long id;
    private String name;
    private String displayName;
    private String avatarUri;
    private String avatarUrl;
    private String description;
    private Long contactCount;
    private Long userId;
    private Long owningForumId;
    private String tag;
    private Byte contactOf;
    private Byte contactStatus;
    private String contactNickName;
    private Long contactRole;
    private Long creatorUid;
    private String creatorName;
    
    private Long communityId;
    private String communityName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    
    @ItemType(Long.class)
    private List<Long> contactGroupPrivileges;
    
    @ItemType(Long.class)
    private List<Long> contactForumPrivileges;
    
    private Timestamp updateTime;
    private Timestamp createTime;
    
    private String contactsPhone;

    private String contactor;
    
    private String entries; 
    
    private String enterpriseCheckinDate;
    
    private String enterpriseAddress;
    private String postUri;
    private String postUrl;
    
    private Byte communityType;
    private Long defaultForumId;
    private Long feedbackForumId;
    private List<ServiceModuleAppDTO> appDtos;

    private String emailDomain;

    @ItemType(OrganizationMemberDTO.class)
    private List<OrganizationContactDTO> adminMembers;

    public List<OrganizationContactDTO> getAdminMembers() {
        return adminMembers;
    }

    public void setAdminMembers(List<OrganizationContactDTO> adminMembers) {
        this.adminMembers = adminMembers;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public String getPostUri() {
		return postUri;
	}

	public void setPostUri(String postUri) {
		this.postUri = postUri;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public String getEnterpriseCheckinDate() {
		return enterpriseCheckinDate;
	}

	public void setEnterpriseCheckinDate(String enterpriseCheckinDate) {
		this.enterpriseCheckinDate = enterpriseCheckinDate;
	}

	public String getEnterpriseAddress() {
		return enterpriseAddress;
	}

	public void setEnterpriseAddress(String enterpriseAddress) {
		this.enterpriseAddress = enterpriseAddress;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getEntries() {
		return entries;
	}

	public void setEntries(String entries) {
		this.entries = entries;
	}

	//TODO address info ?
    @ItemType(value = AddressDTO.class)
    private List<AddressDTO> address;
    
    @ItemType(value = EnterpriseAttachmentDTO.class)
    private List<EnterpriseAttachmentDTO> attachments = new ArrayList<EnterpriseAttachmentDTO>();

    public List<EnterpriseAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EnterpriseAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<AddressDTO> getAddress() {
		return address;
	}

	public void setAddress(List<AddressDTO> address) {
		this.address = address;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getContactCount() {
        return contactCount;
    }

    public void setContactCount(Long contactCount) {
        this.contactCount = contactCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOwningForumId() {
        return owningForumId;
    }

    public void setOwningForumId(Long owningForumId) {
        this.owningForumId = owningForumId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Byte getContactOf() {
        return contactOf;
    }

    public void setContactOf(Byte contactOf) {
        this.contactOf = contactOf;
    }

    public Byte getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(Byte contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getContactNickName() {
        return contactNickName;
    }

    public void setContactNickName(String contactNickName) {
        this.contactNickName = contactNickName;
    }

    public Long getContactRole() {
        return contactRole;
    }

    public void setContactRole(Long contactRole) {
        this.contactRole = contactRole;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<Long> getContactGroupPrivileges() {
        return contactGroupPrivileges;
    }

    public void setContactGroupPrivileges(List<Long> contactGroupPrivileges) {
        this.contactGroupPrivileges = contactGroupPrivileges;
    }

    public List<Long> getContactForumPrivileges() {
        return contactForumPrivileges;
    }

    public void setContactForumPrivileges(List<Long> contactForumPrivileges) {
        this.contactForumPrivileges = contactForumPrivileges;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public Long getDefaultForumId() {
        return defaultForumId;
    }

    public void setDefaultForumId(Long defaultForumId) {
        this.defaultForumId = defaultForumId;
    }

    public Long getFeedbackForumId() {
        return feedbackForumId;
    }

    public void setFeedbackForumId(Long feedbackForumId) {
        this.feedbackForumId = feedbackForumId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ServiceModuleAppDTO> getAppDtos() {
        return appDtos;
    }

    public void setAppDtos(List<ServiceModuleAppDTO> appDtos) {
        this.appDtos = appDtos;
    }
}
