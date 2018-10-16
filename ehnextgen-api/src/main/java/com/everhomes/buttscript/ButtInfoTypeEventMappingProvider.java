package com.everhomes.buttscript;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface ButtInfoTypeEventMappingProvider {


    /**
     * 查询事件所对应的脚本信息,维度暂时按域空间分,一个事件不可能影响所有域空间.
     * @param eventName
     * @return
     */
    List<ButtInfoTypeEventMapping> findButtInfoTypeEventMapping(String eventName ,Integer namespaceId );

    /**
     *
     * @param infoType
     * @param namespaceId
     * @return
     */
    List<ButtInfoTypeEventMapping> findByInfoType(String infoType ,Integer namespaceId );

    ButtInfoTypeEventMapping getButtInfoTypeEventMappingById(Long id);

    ButtInfoTypeEventMapping crteateButtInfoTypeEventMapping(ButtInfoTypeEventMapping bo);

    void updateButtInfoTypeEventMapping(ButtInfoTypeEventMapping bo);

    void deleteButtInfoTypeEventMapping(ButtInfoTypeEventMapping bo);

    List<ButtInfoTypeEventMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);
}
