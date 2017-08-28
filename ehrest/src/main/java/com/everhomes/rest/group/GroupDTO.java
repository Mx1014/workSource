// @formatter:off
package com.everhomes.rest.group;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: group ID</li>
 * <li>uuid: group对应的UUID</li>
 * <li>owningForumId: 拥有的论坛ID</li>
 * <li>name: group名称</li>
 * <li>avatar: group头像URI</li>
 * <li>avatarUrl: group头像URL</li>
 * <li>description: group描述</li>
 * <li>creatorUid: group创建者ID</li>
 * <li>createTime: group创建时间</li>
 * <li>privateFlag: group公有、私有标记，0-公有、1-私有</li>
 * <li>joinPolicy: 加入策略，参考{@link com.everhomes.rest.group.GroupJoinPolicy}</li>
 * <li>memberCount: group成员数</li>
 * <li>tag: group标签</li>
 * <li>categoryId: group类别ID</li>
 * <li>categoryName: group类别名称</li>
 * <li>memberOf: 是否是group成员，1-是(成员状态为待审核时也置为1，也就是服务器有记录则为1，还需要根据<code>memberStatus</code>来判断是否是正常成员)、0-否</li>
 * <li>memberStatus: group成员状态，{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 * <li>memberNickName: group成员在group内的昵称，是group成员时字段才有效</li>
 * <li>memberRole: group成员角色，用于判断是否为管理员，参考{@link com.everhomes.rest.acl.RoleConstants}</li>
 * <li>phonePrivateFlag: group成员是否显示手机号标记，{@link com.everhomes.rest.group.GroupMemberPhonePrivacy}</li>
 * <li>muteNotificationFlag: group成员是否免打扰标记，{@link com.everhomes.rest.group.GroupMemberMuteNotificationFlag}</li>
 * <li>memberGroupPrivileges: group成员的权限列表，是group成员时字段才有效，参考{@link com.everhomes.rest.acl.PrivilegeConstants}</li>
 * <li>memberForumPrivileges: group成员的论坛权限列表，是group成员时字段才有效，参考{@link com.everhomes.rest.acl.PrivilegeConstants}</li>
 * <li>discriminator: group标识，参考{@link com.everhomes.rest.group.GroupDiscriminator}</li>
 * <li>approvalStatus: 审核的状态，针对需要验证的才有此标记，参考{@link com.everhomes.rest.group.ApprovalStatus}</li>
 * <li>operatorName: 操作人</li>
 * <li>joinTime: 加入时间</li>
 * <li>shareUrl: 分享链接</li>
 * <li>scanJoinUrl:扫描入群页面链接</li>
 * <li>scanDownloadUrl:扫描下载页面链接</li>
 * <li>alias: group别名，获取群列表时如果name为空的时候会返回一个别名：用户1、用户2、用户3、用户4、用户5</li>
 * <li>isNameEmptyBefore: 原来的name字段是否为空，0-非空，1-空, {@link com.everhomes.rest.group.GroupNameEmptyFlag}</li>
 * <li>ogrId: orgId公司Id</li>
 * </ul>
 */
public class GroupDTO {
    private Long id;
    private String uuid;
    private Long owningForumId;
    private String name;
    private String avatar;
    private String avatarUrl;
    private String description;
    private Long creatorUid;
    private Timestamp createTime;
    private Byte privateFlag;
    private Integer joinPolicy;
    private Long memberCount;
    private String tag;
    private Long categoryId;
    private String categoryName;
    private Byte memberOf;
    private Byte memberStatus;
    private String memberNickName;
    private Long memberRole;
    private Byte phonePrivateFlag;
    private Byte muteNotificationFlag;
    private Byte postFlag;
    private String creatorName;
    private String creatorFamilyName;
    private String operatorName;
    private Timestamp joinTime;
    private String shareUrl;
    private String scanJoinUrl;
    private String scanDownloadUrl;
    private String alias;
    private Byte isNameEmptyBefore;
    private Long orgId;
    
    @ItemType(Long.class)
    private List<Long> memberGroupPrivileges;
    
    @ItemType(Long.class)
    private List<Long> memberForumPrivileges;
    
    private Timestamp updateTime;
    
    private String discriminator;
    
    private Byte approvalStatus;
    
    public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Timestamp getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Timestamp joinTime) {
		this.joinTime = joinTime;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorFamilyName() {
		return creatorFamilyName;
	}

	public void setCreatorFamilyName(String creatorFamilyName) {
		this.creatorFamilyName = creatorFamilyName;
	}

	public GroupDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    public Integer getJoinPolicy() {
        return joinPolicy;
    }

    public void setJoinPolicy(Integer joinPolicy) {
        this.joinPolicy = joinPolicy;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Byte getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(Byte memberOf) {
        this.memberOf = memberOf;
    }

    public Byte getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Byte memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public List<Long> getMemberGroupPrivileges() {
        return memberGroupPrivileges;
    }

    public void setMemberGroupPrivileges(List<Long> memberGroupPrivileges) {
        this.memberGroupPrivileges = memberGroupPrivileges;
    }

    public List<Long> getMemberForumPrivileges() {
        return memberForumPrivileges;
    }

    public void setMemberForumPrivileges(List<Long> memberForumPrivileges) {
        this.memberForumPrivileges = memberForumPrivileges;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getOwningForumId() {
        return owningForumId;
    }

    public void setOwningForumId(Long owningForumId) {
        this.owningForumId = owningForumId;
    }

    public Long getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Long memberRole) {
        this.memberRole = memberRole;
    }

    public Byte getPhonePrivateFlag() {
        return phonePrivateFlag;
    }

    public void setPhonePrivateFlag(Byte phonePrivateFlag) {
        this.phonePrivateFlag = phonePrivateFlag;
    }

    public Byte getMuteNotificationFlag() {
        return muteNotificationFlag;
    }

    public void setMuteNotificationFlag(Byte muteNotificationFlag) {
        this.muteNotificationFlag = muteNotificationFlag;
    }

    public Byte getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Byte postFlag) {
        this.postFlag = postFlag;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getScanJoinUrl() {
        return scanJoinUrl;
    }

    public void setScanJoinUrl(String scanJoinUrl) {
        this.scanJoinUrl = scanJoinUrl;
    }

    public String getScanDownloadUrl() {
        return scanDownloadUrl;
    }

    public void setScanDownloadUrl(String scanDownloadUrl) {
        this.scanDownloadUrl = scanDownloadUrl;
    }

    public Byte getIsNameEmptyBefore() {
        return isNameEmptyBefore;
    }

    public void setIsNameEmptyBefore(Byte isNameEmptyBefore) {
        this.isNameEmptyBefore = isNameEmptyBefore;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
