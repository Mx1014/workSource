package com.everhomes.workReport;

import com.everhomes.rest.workReport.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkReportServiceImpl implements WorkReportService{

    @Autowired
    private WorkReportProvider workReportProvider;

    @Override
    public WorkReportDTO addWorkReport(AddWorkReportCommand cmd) {
        WorkReport report = new WorkReport();
        //  initialize the work report.
        report.setNamespaceId(UserContext.getCurrentNamespaceId());
        report.setOwnerId(cmd.getOwnerId());
        report.setOwnerType(cmd.getOwnerType());
        report.setOrganizationId(cmd.getOrganizationId());
        report.setReportName(cmd.getReportName());
        report.setModuleId(cmd.getModuleId());
        report.setStatus(WorkReportStatus.VALID.getCode());
        report.setReportType(WorkReportType.DAY.getCode());
        report.setVisibleRangeId(cmd.getOrganizationId());
        //  add it.
        workReportProvider.createWorkReport(report);
        //  return back some information.
        WorkReportDTO dto = new WorkReportDTO();
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setReportAttribute(report.getReportAttribute());
        return dto;
    }

    @Override
    public void deleteWorkReport(WorkReportIdCommand cmd) {
        //  find the report by id.
        WorkReport report = workReportProvider.findWorkReport(cmd.getReportId());
        if(report != null){
            //  change the status.
            report.setStatus(WorkReportStatus.INVALID.getCode());
            workReportProvider.updateWorkReport(report);
        }
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
