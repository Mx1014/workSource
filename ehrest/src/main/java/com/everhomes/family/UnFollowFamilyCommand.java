// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.family.BaseCommand}</li>
 * </ul>
 */
public class UnFollowFamilyCommand extends BaseCommand{

    public UnFollowFamilyCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
