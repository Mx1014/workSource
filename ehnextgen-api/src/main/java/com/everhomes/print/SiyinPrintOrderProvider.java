// @formatter:off
package com.everhomes.print;

import com.everhomes.rest.order.ListBizPayeeAccountDTO;

import java.sql.Timestamp;
import java.util.List;

public interface SiyinPrintOrderProvider {

	void createSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder);

	void updateSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder);

	SiyinPrintOrder findSiyinPrintOrderById(Long id);

	List<SiyinPrintOrder> listSiyinPrintOrder(Timestamp startTime, Timestamp endTime, List<Object> ownerTypeList,
			List<Object> ownerIdList);
	
	List<SiyinPrintOrder> listSiyinPrintUnpaidOrderByUserId(Long userId, String ownerType, Long ownerId);
	
	List<SiyinPrintOrder> listSiyinPrintOrderByUserId(Long userId, Integer pageSize, Long pageAnchor, String ownerType, Long ownerId);

	void updateSiyinPrintOrderLockFlag(Long id, byte lockFlag);

	SiyinPrintOrder findSiyinPrintOrderByOrderNo(Long orderNo);
	
	SiyinPrintOrder findSiyinPrintOrderByGeneralOrderId(String GeneralOrderId);

	SiyinPrintOrder findUnpaidUnlockedOrderByUserId(Long id, Byte jobType, String ownerType, Long ownerId, String printerName);

	List<SiyinPrintOrder> listSiyinPrintOrderByOwners(List<Object> ownerTypeList,
			List<Object> ownerIdList, Timestamp startTime,
			Timestamp endTime, Byte jobType, Byte orderStatus, String keywords, Long pageAnchor, Integer pageSize, Byte payMode, String paymentType);

    ListBizPayeeAccountDTO createPersonalPayUserIfAbsent(String s, String sNamespaceId, String userIdentifier, Object o, Object o1, Object o2);

	SiyinPrintOrder findSiyinPrintOrderByGeneralBillId(String generalBillId);
}