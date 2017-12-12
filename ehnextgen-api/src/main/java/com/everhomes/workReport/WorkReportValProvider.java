package com.everhomes.workReport;

import java.util.List;

public interface WorkReportValProvider {

    Long createWorkReportVal(WorkReportVal val);

    void updateWorkReportVal(WorkReportVal val);

    WorkReportVal getWorkReportValById(Long id);

    List<WorkReportVal> listWorkReportValsByUserIds(Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, List<Long> applierIds);

    void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver);

    void deleteReportValReceiverByValId(Long valId);

    List<WorkReportValReceiverMap> listReportValReceiversByValId(Long reportValId);

}
