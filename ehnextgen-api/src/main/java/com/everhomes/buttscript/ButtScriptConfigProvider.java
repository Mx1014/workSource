package com.everhomes.buttscript;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface ButtScriptConfigProvider {

    /**
     * 通过域空间查询配置信息
     * @param namespaceId
     * @return
     */
    List<ButtScriptConfig> findButtScriptConfigByNamespaceId(Integer namespaceId);


    List<ButtScriptConfig> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);
}
