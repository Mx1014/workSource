package com.everhomes.buttscript;

import com.everhomes.configurations.Configurations;

public interface ButtScriptPublishInfoProvider {


    void getButtScriptPublishInfoById(Integer id);

    /**
     * 创建配置项信息
     * @param bo	Configurations
     */
    void crteateButtScriptPublishInfo(Configurations bo);

    /**
     * 修改配置项信息，主键不能为空
     * @param bo	Configurations
     */
    void updateButtScriptPublishInfo(Configurations bo);


    void deleteButtScriptPublishInfo(Configurations bo);


}
