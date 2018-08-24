package com.everhomes.zhenzhihui;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ZhenzhihuiEnterpriseInfoProvider {

	Long createZhenzhihuiEnterpriseInfo(ZhenzhihuiEnterpriseInfo obj);

	void updateZhenzhihuiEnterpriseInfo(ZhenzhihuiEnterpriseInfo obj);

	void deleteZhenzhihuiEnterpriseInfo(ZhenzhihuiEnterpriseInfo obj);

	ZhenzhihuiEnterpriseInfo getZhenzhihuiEnterpriseInfoById(Long id);

	List<ZhenzhihuiEnterpriseInfo> queryZhenzhihuiEnterpriseInfos(
			ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<ZhenzhihuiEnterpriseInfo> listZhenzhihuiEnterpriseInfoByUserId(Long userId);

}
