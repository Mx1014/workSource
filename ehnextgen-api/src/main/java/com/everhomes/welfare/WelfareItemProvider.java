// @formatter:off
package com.everhomes.welfare;

import java.util.List;

public interface WelfareItemProvider {

	void createWelfareItem(WelfareItem welfareItem);

	void updateWelfareItem(WelfareItem welfareItem);

	WelfareItem findWelfareItemById(Long id);

	List<WelfareItem> listWelfareItem();

}