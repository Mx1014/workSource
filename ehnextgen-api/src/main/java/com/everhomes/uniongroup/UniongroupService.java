// @formatter:off
package com.everhomes.uniongroup;

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
    public List getConfiguresListByGroupId(Long groupId);

    /**
     * 删除一条配置记录
     **/
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure);

}