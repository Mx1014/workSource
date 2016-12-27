// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>公司已经和机构合并(201604)，故在概念上它们是一个；公司分为物业公司、政府相关的公司（工作站、公安等），以及普通公司；
 * <li>namespaceId：域空间id</li>
 * <li>namespaceName：域空间名称</li>
 * <li>id：主键id</li>
 * <li>parentId：父机构id。没有填0</li>
 * <li>parentName：父机构名称</li>
 * <li>name：名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>level：当前层级。没有填0</li>
 * <li>addressId：地址</li>
 * <li>OrganizationType：组织类型，仅用来区分机构是物业、工作站、公安、普通公司等，注意其与groupType意义的区别：参考{@link com.everhomes.rest.organization.OrganizationType}</li>
 * <li>status：状态：参考{@link com.everhomes.rest.organization.OrganizationStatus}</li>
 * <li>memberStatus：成员状态：参考{@link com.everhomes.rest.organization.OrganizationMemberStatus}</li>
 * <li>description：组织描述</li>
 * <li>address：机构地址</li>
 * <li>groupType：机构类别，需要首先用该值来判断是否是一个机构（其还有可能是部门、群组等），在该值为机构的情况下，OrganizationType才有意义，{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * <li>directlyEnterpriseId：如果本身是机构，则此值无效；如果本身是部门、群组（可能多层），则其层次结构上最近的一个直属机构</li>
 * <li>avatarUri：机构头像URI</li>
 * <li>avatarUrl：机构头像URL</li>
 * <li>contact：机构联系电话</li>
 * <li>displayName：机构显示名称</li>
 * <li>communityId：机构办公所在的小区id</li>
 * <li>communityName：小区名称</li>
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>defaultForumId: 默认论坛ID，每个园区都有一个自己的默认论坛用于放园区整体的帖子（如公告）</li>
 * <li>feedbackForumId: 意见论坛ID，每个园区都有一个自己的意见反馈论坛用于放园区意见反馈帖子</li>
 * <li>groupId：机构内部私有圈</li>
 * <li>showFlag：控制是否在导航菜单中，{@link com.everhomes.rest.organization.OrganizationNaviFlag}</li>
 * <li>childrens：子集参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>roles：机构/部门/群组拥有的角色</li>
 * <li>enterpriseContactor: 企业联系人</li>
 * <li>mobile: 手机号</li>
 * <li>doorplateAddress：门牌地址</li>
 * <li>managers：经理列表 {@link com.everhomes.rest.organization.OrganizationManagerDTO}</li>
 * <li>emailDomain: 邮箱域名 </li>
 * </ul>
 */
public class OrganizationDTO {
	private Integer namespaceId;
	private String namespaceName;
	private Long    id;
    private Long    parentId;
    private String parentName;
	private String  name;
	private String  path;
	private Integer level;
	private Long addressId;
	private String OrganizationType;
	private Byte    status;
	private Byte memberStatus;
	private String description;
	private String address;
	private String groupType;
	private Long directlyEnterpriseId;
	
    private String avatarUri;
    private String avatarUrl;
    private String contact;

    private String displayName;
	
	//expand 
	private Long communityId;
	private String communityName;    
    private Byte communityType;
    private Long defaultForumId;
    private Long feedbackForumId;
	
	private Long groupId;
	
	private Byte showFlag;
	
	@ItemType(OrganizationDTO.class)
	private List<OrganizationDTO> childrens;
	
	@ItemType(RoleDTO.class)
	private List<RoleDTO> roles;
	
	private String enterpriseContactor;
	
	private String mobile;

	@ItemType(OrganizationManagerDTO.class)
	private List<OrganizationManagerDTO> managers;

	private String pathName;

    private String emailDomain;
	
	public String getEnterpriseContactor() {
		return enterpriseContactor;
	}

	public void setEnterpriseContactor(String enterpriseContactor) {
		this.enterpriseContactor = enterpriseContactor;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNamespaceName() {
		return namespaceName;
	}

	public void setNamespaceName(String namespaceName) {
		this.namespaceName = namespaceName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OrganizationDTO() {
    }
	
	public java.lang.Long getParentId() {
		return parentId;
	}

	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getPath() {
		return path;
	}

	public void setPath(java.lang.String path) {
		this.path = path;
	}

	public java.lang.Integer getLevel() {
		return level;
	}

	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getOrganizationType() {
		return OrganizationType;
	}

	public void setOrganizationType(String organizationType) {
		OrganizationType = organizationType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Byte getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Byte memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<OrganizationDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<OrganizationDTO> childrens) {
		this.childrens = childrens;
	}
	
	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getDirectlyEnterpriseId() {
		return directlyEnterpriseId;
	}

	public void setDirectlyEnterpriseId(Long directlyEnterpriseId) {
		this.directlyEnterpriseId = directlyEnterpriseId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	public Byte getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Byte showFlag) {
		this.showFlag = showFlag;
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

	public List<OrganizationManagerDTO> getManagers() {
		return managers;
	}

	public void setManagers(List<OrganizationManagerDTO> managers) {
		this.managers = managers;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}
}
