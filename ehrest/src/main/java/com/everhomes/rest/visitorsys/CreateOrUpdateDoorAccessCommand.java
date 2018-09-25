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
 *  <li>defaultAuthDurationType : 默认访客授权有效期种类,0 天数，1 小时数</li>
 *  <li>defaultAuthDuration : 默认访客授权有效期</li>
 *  <li>defaultEnableAuthCount : 默认访客授权次数开关 0 关 1 开</li>
 *  <li>defaultAuthCount : 默认访客授权次数</li>
 *  <li>defaultDoorAccessFlag : 默认门禁组 0 非默认 1 默认</li>
 * </ul>
 */

public class CreateOrUpdateDoorAccessCommand extends VisitorSysDoorAccessDTO {


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
