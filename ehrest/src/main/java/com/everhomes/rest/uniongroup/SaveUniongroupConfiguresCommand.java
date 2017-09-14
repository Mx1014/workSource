// @formatter:off
package com.everhomes.rest.uniongroup;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>参数:
 * <li>groupType: 组类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>groupId: 组id，既是organizationId</li>
 * <li>enterpriseId: 公司Id</li>
 * <li>versionCode: 版本号</li>
 * <li>targetId: 目标Id</li>
 * <li>targetType: 组类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</LI>
 * </ul>
 */
public class SaveUniongroupConfiguresCommand {

    private String groupType;

    private Long groupId;

    private Long enterpriseId;

    private Integer versionCode;

    @ItemType(UniongroupTarget.class)
    private List<UniongroupTarget> targets;

    public SaveUniongroupConfiguresCommand() {

    }

    public SaveUniongroupConfiguresCommand(String groupType, Long groupId, List<UniongroupTarget> targets) {
        super();
        this.groupType = groupType;
        this.groupId = groupId;
        this.targets = targets;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<UniongroupTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<UniongroupTarget> targets) {
        this.targets = targets;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }
}
