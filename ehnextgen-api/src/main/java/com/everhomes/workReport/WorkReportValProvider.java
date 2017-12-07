package com.everhomes.workReport;

public interface WorkReportValProvider {

    void createWorkReportVal(WorkReportVal val);

    void updateWorkReportVal(WorkReportVal val);

    WorkReportVal getWorkReportValById(Long id);
}
