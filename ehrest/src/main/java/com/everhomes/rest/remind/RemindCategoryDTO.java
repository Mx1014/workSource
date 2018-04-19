package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 分类ID</li>
 * <li>name: 分类名称</li>
 * <li>colour: 分类颜色,无色是空值</li>
 * <li>defaultOrder: 排序值</li>
 * <li>shareToMembers: 默认共享人的档案ID列表，可选项，参考{@link com.everhomes.rest.remind.ShareMemberDTO}</li>
 * <li>shareShortDisplay: 分享人显示概要，如YH等3人</li>
 * </ul>
 */
public class RemindCategoryDTO {
    private Long id;
    private String name;
    private String colour;
    private Integer defaultOrder;
    private List<ShareMemberDTO> shareToMembers;
    private String shareShortDisplay;

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

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public List<ShareMemberDTO> getShareToMembers() {
        return shareToMembers;
    }

    public void setShareToMembers(List<ShareMemberDTO> shareToMembers) {
        this.shareToMembers = shareToMembers;
    }

    public String getShareShortDisplay() {
        return shareShortDisplay;
    }

    public void setShareShortDisplay(String shareShortDisplay) {
        this.shareShortDisplay = shareShortDisplay;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
