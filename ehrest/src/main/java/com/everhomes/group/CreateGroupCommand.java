// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：group名称</li>
 * <li>description：group描述</li>
 * <li>avatar：group头像ID，图片上传到ContentServer得到的ID</li>
 * <li>visibilityScope：group可见性类型，参考{@link com.everhomes.visibility.VisibilityScope}</li>
 * <li>visibilityScopeId：根据group可见性类型对应的ID（如小区ID、城市ID等）</li>
 * <li>privateFlag：公私有标志，公有group可查询、私有group不可查询</li>
 * <li>categoryId：group类型ID</li>
 * <li>tag：标签，用于搜索</li>
 * <li>explicitRegionDescriptorsJson：暂不使用</li>
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

    // json of List<RegionDescriptor>
    private String explicitRegionDescriptorsJson;
    
    public CreateGroupCommand() {
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

    public String getExplicitRegionDescriptorsJson() {
        return explicitRegionDescriptorsJson;
    }

    public void setExplicitRegionDescriptorsJson(String explicitRegionDescriptorsJson) {
        this.explicitRegionDescriptorsJson = explicitRegionDescriptorsJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
