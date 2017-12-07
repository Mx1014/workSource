package com.everhomes.workReport;

public interface WorkReportValProvider {

    Long createWorkReportVal(WorkReportVal val);

    void updateWorkReportVal(WorkReportVal val);

    WorkReportVal getWorkReportValById(Long id);

    void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver);
}
