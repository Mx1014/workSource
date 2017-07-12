// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：group名称</li>
 * <li>description：group描述</li>
 * <li>avatar：group头像ID，图片上传到ContentServer得到的ID</li>
 * <li>visibilityScope：group可见性类型，参考{@link com.everhomes.rest.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId：根据group可见性类型对应的ID（如小区ID、城市ID等）</li>
 * <li>privateFlag：公私有标志，兴趣圈为公有、私有邻居圈为私有，参考{@link com.everhomes.rest.group.GroupPrivacy}</li>
 * <li>categoryId：group类型ID</li>
 * <li>tag：标签，用于搜索</li>
 * <li>postFlag：是否只有管理员可以发帖，参考{@link com.everhomes.rest.group.GroupPostFlag}</li>
 * <li>explicitRegionDescriptorsJson：暂不使用</li>
 * <li>visibleRegionType：用户创建圈时所在的范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}，添加于3.1.0版本 20151104</li>
 * <li>visibleRegionId：用户创建圈时所在的范围ID，如园区/小区ID、片区ID（即机构ID），添加于3.1.0版本 20151104</li>
 * <li>joinPolicy: 加入策略，参考{@link com.everhomes.rest.group.GroupJoinPolicy}</li>
 * </ul>
 */
public class CreateGroupCommand {
    @NotNull
    private String name;
    private String description;
    private String avatar;
    private Byte visibilityScope;
    private Long visibilityScopeId;
    private Byte privateFlag;
    private Long categoryId;
    private String tag;
    private Byte postFlag;
    private Byte visibleRegionType;
    private Long visibleRegionId;

    // json of List<RegionDescriptor>
    private String explicitRegionDescriptorsJson;
    private Integer namespaceId;
    private Integer joinPolicy;
    
    public CreateGroupCommand() {
    }

    public Integer getJoinPolicy() {
		return joinPolicy;
	}

	public void setJoinPolicy(Integer joinPolicy) {
		this.joinPolicy = joinPolicy;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Byte getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(Byte privateFlag) {
        this.privateFlag = privateFlag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Byte getVisibilityScope() {
        return visibilityScope;
    }

    public void setVisibilityScope(Byte visibilityScope) {
        this.visibilityScope = visibilityScope;
    }

    public Long getVisibilityScopeId() {
        return visibilityScopeId;
    }

    public void setVisibilityScopeId(Long visibilityScopeId) {
        this.visibilityScopeId = visibilityScopeId;
    }

    public Byte getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Byte postFlag) {
        this.postFlag = postFlag;
    }

    public String getExplicitRegionDescriptorsJson() {
        return explicitRegionDescriptorsJson;
    }

    public void setExplicitRegionDescriptorsJson(String explicitRegionDescriptorsJson) {
        this.explicitRegionDescriptorsJson = explicitRegionDescriptorsJson;
    }

    public Byte getVisibleRegionType() {
        return visibleRegionType;
    }

    public void setVisibleRegionType(Byte visibleRegionType) {
        this.visibleRegionType = visibleRegionType;
    }

    public Long getVisibleRegionId() {
        return visibleRegionId;
    }

    public void setVisibleRegionId(Long visibleRegionId) {
        this.visibleRegionId = visibleRegionId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
