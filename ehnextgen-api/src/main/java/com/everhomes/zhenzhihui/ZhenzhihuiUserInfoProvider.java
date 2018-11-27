package com.everhomes.zhenzhihui;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ZhenzhihuiUserInfoProvider {

	Long createZhenzhihuiUserInfo(ZhenzhihuiUserInfo obj);

	void updateZhenzhihuiUserInfo(ZhenzhihuiUserInfo obj);

	void deleteZhenzhihuiUserInfo(ZhenzhihuiUserInfo obj);

	ZhenzhihuiUserInfo getZhenzhihuiUserInfoById(Long id);

	List<ZhenzhihuiUserInfo> queryZhenzhihuiUserInfos(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<ZhenzhihuiUserInfo> listZhenzhihuiUserInfosByUserId(Long userId);
}
