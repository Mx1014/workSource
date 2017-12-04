package com.everhomes.workReport;

import com.everhomes.rest.workReport.*;
import org.springframework.stereotype.Component;

@Component
public class WorkReportServiceImpl implements WorkReportService{
    @Override
    public WorkReportDTO addWorkReport(AddWorkReportCommand cmd) {
        return null;
    }

    @Override
    public void deleteWorkReport(WorkReportIdCommand cmd) {

    }

    @Override
    public WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd) {
        return null;
    }

    @Override
    public ListWorkReportsResponse listWorkReports(ListWorkReportsCommand cmd) {
        return null;
    }

    @Override
    public WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd) {
        return null;
    }

    @Override
    public void enableWorkReportName(WorkReportIdCommand cmd) {

    }

    @Override
    public void disableWorkReportName(WorkReportIdCommand cmd) {

    }

    @Override
    public void createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd) {

    }

    @Override
    public void postWorkReportVal(PostWorkReportValCommand cmd) {

    }

    @Override
    public void updateWorkReportVal(PostWorkReportValCommand cmd) {

    }

    @Override
    public ListWorkReportsValResponse listWorkReportsVal(ListWorkReportsValCommand cmd) {
        return null;
    }

    @Override
    public WorkReportValDTO getWorkReportVal(WorkReportValIdCommand cmd) {
        return null;
    }
}
