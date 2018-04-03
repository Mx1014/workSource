// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintRecordProvider {

	void createSiyinPrintRecord(SiyinPrintRecord siyinPrintRecord);

	void updateSiyinPrintRecord(SiyinPrintRecord siyinPrintRecord);

	SiyinPrintRecord findSiyinPrintRecordById(Long id);

	List<SiyinPrintRecord> listSiyinPrintRecord();

	SiyinPrintRecord findSiyinPrintRecordByJobId(String jobId);

	List<SiyinPrintRecord> listSiyinPrintRecordByOrderId(Long creatorUid, Long orderId, String ownerType, Long ownerId);

}