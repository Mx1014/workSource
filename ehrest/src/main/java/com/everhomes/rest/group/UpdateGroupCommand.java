// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>name：group名称</li>
 * <li>description：group描述</li>
 * <li>avatar：group头像URI，图片上传到ContentServer得到的ID</li>
 * <li>visibilityScope：group可见性类型，参考{@link com.everhomes.rest.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId：根据group可见性类型对应的ID（如小区ID、城市ID等）</li>
 * <li>categoryId：group类型ID</li>
 * <li>tag：标签，用于搜索</li>
 * <li>joinPolicy: 加入策略，参考{@link com.everhomes.rest.group.GroupJoinPolicy}</li>
 * </ul>
 */
public class UpdateGroupCommand {
    @NotNull
    private Long groupId;
    
    private String name;
    private String description;
    private String avatar;
    
    private Byte visibilityScope;
    private Long visibilityScopeId;
    
    private Long categoryId;
    
    private String tag;
    private Integer joinPolicy;

    public Integer getJoinPolicy() {
		return joinPolicy;
	}

	public void setJoinPolicy(Integer joinPolicy) {
		this.joinPolicy = joinPolicy;
	}

	public UpdateGroupCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
