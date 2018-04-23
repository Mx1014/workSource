package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>communityControlList: 公司列表，参考{@link com.everhomes.rest.portal.ServiceModuleAppDTO}</li>
 * <li>orgControlList: 公司列表，参考{@link com.everhomes.rest.portal.ServiceModuleAppDTO}</li>
 * <li>unlimitControlList: ??列表，参考{@link com.everhomes.rest.portal.ServiceModuleAppDTO}</li>
 * </ul>
 */
public class TreeServiceModuleAppsResponse {
    @ItemType(ServiceModuleDTO.class)
    private List<ServiceModuleDTO> communityControlList;
    @ItemType(ServiceModuleDTO.class)
    private List<ServiceModuleDTO> orgControlList;
    @ItemType(ServiceModuleDTO.class)
    private List<ServiceModuleDTO> unlimitControlList;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ServiceModuleDTO> getCommunityControlList() {
        return communityControlList;
    }

    public void setCommunityControlList(List<ServiceModuleDTO> communityControlList) {
        this.communityControlList = communityControlList;
    }

    public List<ServiceModuleDTO> getOrgControlList() {
        return orgControlList;
    }

    public void setOrgControlList(List<ServiceModuleDTO> orgControlList) {
        this.orgControlList = orgControlList;
    }

    public List<ServiceModuleDTO> getUnlimitControlList() {
        return unlimitControlList;
    }

    public void setUnlimitControlList(List<ServiceModuleDTO> unlimitControlList) {
        this.unlimitControlList = unlimitControlList;
    }
}
