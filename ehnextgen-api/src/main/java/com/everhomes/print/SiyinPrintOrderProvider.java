// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

public interface SiyinPrintOrderProvider {

	void createSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder);

	void updateSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder);

	SiyinPrintOrder findSiyinPrintOrderById(Long id);

	List<SiyinPrintOrder> listSiyinPrintOrder(Timestamp startTime, Timestamp endTime, List<String> ownerTypeList,
			List<Long> ownerIdList);
	
	List<SiyinPrintOrder> listSiyinPrintUnpaidOrderByUserId(Long userId, String ownerType, Long ownerId);
	
	List<SiyinPrintOrder> listSiyinPrintOrderByUserId(Long userId, Integer pageSize, Long pageAnchor, String ownerType, Long ownerId);

	void updateSiyinPrintOrderLockFlag(Long id, byte lockFlag);

	SiyinPrintOrder findSiyinPrintOrderByOrderNo(Long orderNo);

	SiyinPrintOrder findUnpaidUnlockedOrderByUserId(Long id, Byte jobType, String ownerType, Long ownerId);

}