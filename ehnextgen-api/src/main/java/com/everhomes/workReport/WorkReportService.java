package com.everhomes.workReport;

import com.everhomes.rest.workReport.*;

public interface WorkReportService {

    WorkReportDTO addWorkReport(AddWorkReportCommand cmd);

    void deleteWorkReport(WorkReportIdCommand cmd);

    WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd);

    ListWorkReportsResponse listWorkReports(ListWorkReportsCommand cmd);

    WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd);

    void createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd);

    void postWorkReportVal(PostWorkReportValCommand cmd);

    void updateWorkReportVal(PostWorkReportValCommand cmd);

    void listWorkReportsVal(ListWorkReportsValCommand cmd);
}
