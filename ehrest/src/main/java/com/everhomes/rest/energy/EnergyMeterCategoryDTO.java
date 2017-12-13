package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 分类id</li>
 *     <li>name: 分类名称</li>
 *     <li>deleteFlag: 是否是默认分类, 不允许删除 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>communityId: 公式所属项目id，域空间直属的则为0或无</li>
 *     <li>communityName: 所属（应用）项目列表</li>
 * </ul>
 */
public class EnergyMeterCategoryDTO {

    private Long id;
    private String name;
    private Byte deleteFlag;
    private Long communityId;

    @ItemType(String.class)
    private List<String> communityName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<String> getCommunityName() {
        return communityName;
    }

    public void setCommunityName(List<String> communityName) {
        this.communityName = communityName;
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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
