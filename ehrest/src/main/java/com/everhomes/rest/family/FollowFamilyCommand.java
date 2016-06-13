// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>aliasName: 关注家庭的昵称（可选）</li>
 * </ul>
 */
public class FollowFamilyCommand extends BaseCommand{
    private String aliasName;

    public FollowFamilyCommand() {
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
