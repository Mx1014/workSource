package com.everhomes.workReport;

import com.everhomes.rest.workReport.WorkReportDTO;

public interface WorkReportProvider {

    void createWorkReport(WorkReport report);

    void updateWorkReport(WorkReport report);

    WorkReport findWorkReport(Long id);
}
