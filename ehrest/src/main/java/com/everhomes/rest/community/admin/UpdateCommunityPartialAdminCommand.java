// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>communityId: 被更新的小区Id</li>
 *     <li>name: 项目名称</li>
 * </ul>
 */
public class UpdateCommunityPartialAdminCommand {
    @NotNull
    private Long communityId;

    private String name;

    private Byte status;

    public UpdateCommunityPartialAdminCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
