package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>contactName: 成员姓名</li>
 * <li>contactEnName: 成员英文名</li>
 * <li>gender: 成员性别</li>
 * <li>areaCode: 区号</li>
 * <li>contactToken: 联系号码</li>
 * <li>contactShortToken: 手机短号</li>
 * <li>email: 成员邮箱</li>
 * <li>departmentIds: 选择的部门</li>
 * <li>jobPositionIds: 选择的职位</li>
 * <li>visibleFlag: 隐私设置: 0-显示, 1-隐藏</li>
 * </ul>
 */
public class AddProfileContactCommand {
    private String contactName;

    private String contactEnName;

    private Byte gender;

    private String areaCode;

    private String contactToken;

    private String contactShortToken;

    private String email;

    @ItemType(Long.class)
    private List<Long> departmentIds;

    @ItemType(Long.class)
    private List<Long> jobPositionIds;

    private Byte visibleFlag;

    public AddProfileContactCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
