//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>notifyThirdSignDTOList: 账单ID+错误描述列表，左邻对这些账单置一个特殊error标记，以此标记判断这些账单数据下一次同步不再传输</li>
 */
public class NotifyThirdSignCommand {
	
	private List<NotifyThirdSignDTO> notifyThirdSignDTOList;

	public List<NotifyThirdSignDTO> getNotifyThirdSignDTOList() {
		return notifyThirdSignDTOList;
	}

	public void setNotifyThirdSignDTOList(List<NotifyThirdSignDTO> notifyThirdSignDTOList) {
		this.notifyThirdSignDTOList = notifyThirdSignDTOList;
	}
	
}
