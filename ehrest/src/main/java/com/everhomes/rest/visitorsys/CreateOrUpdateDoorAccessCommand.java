package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id : 主键</li>
 *  <li>namespaceId : 域空间Id</li>
 *  <li>ownerType : 所属类型</li>
 *  <li>ownerId : 所属Id</li>
 *  <li>doorAccessId : 门禁Id</li>
 *  <li>doorAccessName : 门禁名称</li>
 *  <li>authRuleType : 授权规则种类，0 时长，1 次数</li>
 *  <li>maxDuration : 访客授权最长有效期</li>
 *  <li>maxCount : 访客授权最大次数</li>
 *  <li>defaultDoorAccessFlag : 默认门禁组 0 非默认 1 默认</li>
 * </ul>
 */

public class CreateOrUpdateDoorAccessCommand extends VisitorSysDoorAccessDTO {


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
