package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 分类ID，没有值时进行添加操作，有值时进行更新操作</li>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>name: 日程分类名称，必填</li>
 * <li>colour: 分类的颜色，RGB颜色值，如：#FF00B9EF，可选项</li>
 * <li>shareToMembers: 默认共享人的档案ID列表，可选项，参考{@link com.everhomes.rest.remind.ShareMemberDTO}</li>
 * </ul>
 */
public class CreateOrUpdateRemindCategoryCommand {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String name;
    private String colour;
    private List<ShareMemberDTO> shareToMembers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public List<ShareMemberDTO> getShareToMembers() {
        return shareToMembers;
    }

    public void setShareToMembers(List<ShareMemberDTO> shareToMembers) {
        this.shareToMembers = shareToMembers;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
