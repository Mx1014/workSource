package com.everhomes.workReport;

import org.jooq.Record;
import org.jooq.RecordMapper;

import java.util.List;

public interface WorkReportValProvider {

    Long createWorkReportVal(WorkReportVal val);

    void updateWorkReportVal(WorkReportVal val);

    WorkReportVal getWorkReportValById(Long id);

    List<WorkReportVal> listWorkReportValsByApplierIds(Integer namespaceId, Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, List<Long> applierIds);

    List<WorkReportVal> listWorkReportValsByReceiverId(Integer namespaceId, Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, Long receiverId, Byte readStatus);

    void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver);

    void deleteReportValReceiverByValId(Long valId);

    List<WorkReportValReceiverMap> listReportValReceiversByValId(Long reportValId);

    Integer countUnReadWorkReportsVal(Integer namespaceId, Long receiverId);

    void markWorkReportsValReading(Integer namespaceId, Long receiverId);
}
