// @formatter:off
package com.everhomes.welfare;

import java.util.List;

public interface WelfareReceiverProvider {

	void createWelfareReceiver(WelfareReceiver welfareReceiver);

	void updateWelfareReceiver(WelfareReceiver welfareReceiver);

	WelfareReceiver findWelfareReceiverById(Long id);

	List<WelfareReceiver> listWelfareReceiver();

	List<WelfareReceiver> listWelfareReceiver(Long welfareId);

	void deleteWelfareReceivers(Long welfareId);

	WelfareReceiver findWelfareReceiverByUser(Long welfareId, Long userId);
}