package com.everhomes.workReport;

import com.everhomes.rest.workReport.*;

public interface WorkReportService {

    WorkReportDTO addWorkReport(AddWorkReportCommand cmd);

    void deleteWorkReport(WorkReportIdCommand cmd);

    WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd);

    ListWorkReportsResponse listWorkReports(ListWorkReportsCommand cmd);

    WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd);

    void createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd);

    void postWorkReport(PostWorkReportCommand cmd);

    void updatePostWorkReport(PostWorkReportCommand cmd);
}
