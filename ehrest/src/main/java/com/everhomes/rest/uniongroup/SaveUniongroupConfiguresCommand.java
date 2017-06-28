// @formatter:off
package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>参数:
 * <li>groupType: 组类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>groupId: 组id</li>
 * <li>targetId: 目标Id</li>
 * <li>targetType: 组类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</LI>
 * </ul>
 */
public class SaveUniongroupConfiguresCommand {

    private String groupType;

    private Long groupId;

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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
