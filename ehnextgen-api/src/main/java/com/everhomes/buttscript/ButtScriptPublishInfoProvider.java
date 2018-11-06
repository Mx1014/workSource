package com.everhomes.buttscript;

import com.everhomes.configurations.Configurations;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface ButtScriptPublishInfoProvider {


    /**
     * 通过主键查询信息
     * @param id
     * @return
     */
    ButtScriptPublishInfo getButtScriptPublishInfoById(Long id);

    /**
     * 新增
     * @param bo
     */
    void crteateButtScriptPublishInfo(ButtScriptPublishInfo bo);

    /**
     * 更新
     * @param bo
     */
    void updateButtScriptPublishInfo(ButtScriptPublishInfo bo);


    /**
     * 删除
     * @param bo
     */
    void deleteButtScriptPublishInfo(ButtScriptPublishInfo bo);


    /**
     * 通过域空间及类型查询发布信息
     * @param namespaceId
     * @param infoType
     * @return
     */
    ButtScriptPublishInfo getButtScriptPublishInfo(Integer namespaceId , String infoType);


    List<ButtScriptPublishInfo> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);
}
