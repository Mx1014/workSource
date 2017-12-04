package com.everhomes.workReport;

import com.everhomes.rest.workReport.*;

public interface WorkReportService {

    WorkReportDTO addWorkReport(AddWorkReportCommand cmd);

    void deleteWorkReport(WorkReportIdCommand cmd);

    WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd);

    ListWorkReportsResponse listWorkReports(ListWorkReportsCommand cmd);

    WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd);

    void enableWorkReportName(WorkReportIdCommand cmd);

    void disableWorkReportName(WorkReportIdCommand cmd);

    void createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd);

    void postWorkReportVal(PostWorkReportValCommand cmd);

    void updateWorkReportVal(PostWorkReportValCommand cmd);

    ListWorkReportsValResponse listWorkReportsVal(ListWorkReportsValCommand cmd);

    WorkReportValDTO getWorkReportVal(WorkReportValIdCommand cmd);
}
