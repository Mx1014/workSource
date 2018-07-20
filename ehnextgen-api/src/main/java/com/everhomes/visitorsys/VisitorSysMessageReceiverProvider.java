// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysMessageReceiverProvider {

	void createVisitorSysMessageReceiver(VisitorSysMessageReceiver visitorSysMessageReceiver);

	void updateVisitorSysMessageReceiver(VisitorSysMessageReceiver visitorSysMessageReceiver);

	VisitorSysMessageReceiver findVisitorSysMessageReceiverById(Long id);

	List<VisitorSysMessageReceiver> listVisitorSysMessageReceiver();

}