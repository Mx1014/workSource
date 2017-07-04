// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.rest.uniongroup.GetUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.ListUniongroupMemberDetailsCommand;
import com.everhomes.rest.uniongroup.ListUniongroupMemberDetailsWithConditionCommand;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;

import java.util.List;

public interface UniongroupService {

    /**
     * 保存一次组配置
     **/
    public void saveUniongroupConfigures(SaveUniongroupConfiguresCommand cmd);

    /**
     * 根据组Id获取配置项记录
     **/
    public List getConfiguresListByGroupId(GetUniongroupConfiguresCommand cmd);

    /**
     * 根据组Id获取组内人员记录
     **/
    public List listUniongroupMemberDetailsByGroupId(ListUniongroupMemberDetailsCommand cmd);

    /**
     * 删除一条配置记录
     **/
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure);

    /**根据条件查询记录**/
    public List listUniongroupMemberDetailsWithCondition(ListUniongroupMemberDetailsWithConditionCommand cmd);

}