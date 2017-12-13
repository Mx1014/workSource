package com.everhomes.workReport;

import com.everhomes.rest.workReport.*;

public interface WorkReportService {

    WorkReportDTO addWorkReport(AddWorkReportCommand cmd);

    void deleteWorkReport(WorkReportIdCommand cmd);

    WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd);

    ListWorkReportsResponse listWorkReports(ListWorkReportsCommand cmd);

    WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd);

    void enableWorkReport(WorkReportIdCommand cmd);

    void disableWorkReport(WorkReportIdCommand cmd);

    VerifyWorkReportResponse verifyWorkReportTemplates(CreateWorkReportTemplatesCommand cmd);

    void createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd);

    ListWorkReportsResponse listActiveWorkReports(ListWorkReportsCommand cmd);

    WorkReportValDTO postWorkReportVal(PostWorkReportValCommand cmd);

    void deleteWorkReportVal(WorkReportValIdCommand cmd);

    WorkReportValDTO updateWorkReportVal(PostWorkReportValCommand cmd);

    WorkReportValDTO getWorkReportValItem(WorkReportValIdCommand cmd);

    ListWorkReportsValResponse listSubmittedWorkReportsVal(ListWorkReportsValCommand cmd);

    ListWorkReportsValResponse listReceivedWorkReportsVal(ListWorkReportsValCommand cmd);

    Integer countUnReadWorkReportsVal();

    void markWorkReportsValReading();

    WorkReportValDTO getWorkReportValDetail(WorkReportValIdCommand cmd);
}
