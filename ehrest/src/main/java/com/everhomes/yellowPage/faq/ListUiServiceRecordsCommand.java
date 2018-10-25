package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.AllianceCommonCommand;

/**
 * <ul>
 * <li>namespaceId: namespaceId</li>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型</li>
 * <li>status: 2-处理中 空或其他-全部</li>
 * </ul>
 **/

public class ListUiServiceRecordsCommand extends AllianceCommonCommand {
	private Byte status;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
