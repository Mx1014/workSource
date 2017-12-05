package com.everhomes.workReport;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.workReport.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkReportServiceImpl implements WorkReportService {

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

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
        if (report != null) {
            //  change the status.
            report.setStatus(WorkReportStatus.INVALID.getCode());
            workReportProvider.updateWorkReport(report);
        }
    }

    @Override
    public WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd) {
        //  find the report by id.
        WorkReport report = workReportProvider.findWorkReport(cmd.getReportId());
        if (report != null) {
            //  update it.
            report.setVisibleRangeId(cmd.getVisibleRangeId());
            report.setReportType(cmd.getReportType());
            report.setFormOriginId(cmd.getFormOriginId());
            report.setFormVersion(cmd.getFormVersion());
            workReportProvider.updateWorkReport(report);
            WorkReportDTO dto = new WorkReportDTO();
            dto.setReportName(report.getReportName());
            dto.setReportType(report.getReportType());
            dto.setReportAttribute(report.getReportAttribute());
            dto.setFormOriginId(report.getFormOriginId());
            dto.setFormVersion(report.getFormVersion());
            return dto;
        }
        return null;
    }

    @Override
    public ListWorkReportsResponse listWorkReports(ListWorkReportsCommand cmd) {
        ListWorkReportsResponse response = new ListWorkReportsResponse();
        List<WorkReportDTO> reports = new ArrayList<>();
        Long nextPageAnchor = null;
        //  set the conditions.
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);
        List<WorkReport> results = workReportProvider.listWorkReports(
                cmd.getPageAnchor(), cmd.getPageSize(), cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getModuleId(), cmd.getReportStatus());
        //  convert to the result.
        if (results != null && results.size() > 0) {
            //  pageAnchor.
            if(results.size() > cmd.getPageSize()){
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }
            //  DTO.
            results.forEach(r -> {
                WorkReportDTO dto = ConvertHelper.convert(r, WorkReportDTO.class);
                dto.setReportId(r.getId());
                Organization org = organizationProvider.findOrganizationById(r.getVisibleRangeId());
                if (org != null)
                    dto.setVisibleRange(ConvertHelper.convert(org, OrganizationDTO.class));
                reports.add(dto);
            });
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setReports(reports);
        return response;
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
