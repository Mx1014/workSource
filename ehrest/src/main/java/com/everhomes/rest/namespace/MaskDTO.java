package com.everhomes.rest.namespace;

import com.everhomes.util.StringHelper;

/**
 * <ul> 蒙版信息
 * <li>namespaceId: 域空间ID</li>
 * <li>resourceType: 域空间下所管理的小区类型，参考{@link com.everhomes.rest.namespace.NamespaceCommunityType}</li>
 * </ul>
 */
public class MaskDTO {
    private Long id;
    private String tips;
    private String iconName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
