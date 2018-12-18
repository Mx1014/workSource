// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>apps: 工作台app配置，请参考{@link com.everhomes.rest.launchpad.WorkPlatformAppDTO}</li>
 *     <li>organizationId: 企业ID</li>
 * </ul>
 */
public class SaveWorkPlatformAppCommand {

    @ItemType(WorkPlatformAppDTO.class)
    private List<WorkPlatformAppDTO> apps;

    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<WorkPlatformAppDTO> getApps() {
        return apps;
    }

    public void setApps(List<WorkPlatformAppDTO> apps) {
        this.apps = apps;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
