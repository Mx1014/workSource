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
    private List<ServiceModuleAppDTO> communityControlList;
    private List<ServiceModuleAppDTO> orgControlList;
    private List<ServiceModuleAppDTO> unlimitControlList;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ServiceModuleAppDTO> getCommunityControlList() {
        return communityControlList;
    }

    public void setCommunityControlList(List<ServiceModuleAppDTO> communityControlList) {
        this.communityControlList = communityControlList;
    }

    public List<ServiceModuleAppDTO> getOrgControlList() {
        return orgControlList;
    }

    public void setOrgControlList(List<ServiceModuleAppDTO> orgControlList) {
        this.orgControlList = orgControlList;
    }

    public List<ServiceModuleAppDTO> getUnlimitControlList() {
        return unlimitControlList;
    }

    public void setUnlimitControlList(List<ServiceModuleAppDTO> unlimitControlList) {
        this.unlimitControlList = unlimitControlList;
    }
}
