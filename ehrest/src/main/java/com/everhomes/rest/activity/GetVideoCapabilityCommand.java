package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间</li>
 * <li>officialFlag: 是否为官方帖；参考{@link com.everhomes.rest.organization.OfficialFlag}</li>
 * </ul>
 * @author janson
 *
 */
public class GetVideoCapabilityCommand {
    private Integer namespaceId;
    private Byte officialFlag;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getOfficialFlag() {
        return officialFlag;
    }

    public void setOfficialFlag(Byte officialFlag) {
        this.officialFlag = officialFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
