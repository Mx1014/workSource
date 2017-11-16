package com.everhomes.rest.portal;

import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <ul>返回值:
 * <li>List<ServiceModuleAppDTO>: 模块应用列表，参考{@link com.everhomes.rest.portal.ServiceModuleAppDTO}</li>
 * </ul>
 */
public class TreeServiceModuleAppsResponse {
    private List<ServiceModuleDTO> communityControlList;
    private List<ServiceModuleDTO> orgControlList;
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
