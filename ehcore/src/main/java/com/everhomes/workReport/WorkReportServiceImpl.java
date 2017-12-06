package com.everhomes.workReport;

import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.workReport.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkReportServiceImpl implements WorkReportService {

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public WorkReportDTO addWorkReport(AddWorkReportCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        WorkReport report = new WorkReport();

        //  initialize the work report.
        report.setNamespaceId(namespaceId);
        report.setOwnerId(cmd.getOwnerId());
        report.setOwnerType(cmd.getOwnerType());
        report.setOrganizationId(cmd.getOrganizationId());
        report.setReportName(cmd.getReportName());
        report.setModuleId(cmd.getModuleId());
        report.setStatus(WorkReportStatus.VALID.getCode());
        report.setReportType(WorkReportType.DAY.getCode());
        //  add it with the initial scope.
        dbProvider.execute((TransactionStatus status) -> {
            workReportProvider.createWorkReport(report);
            Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
            if (org != null) {
                WorkReportScopeMap scopeMap = new WorkReportScopeMap();
                scopeMap.setNamespaceId(namespaceId);
                scopeMap.setSourceType(WorkReportScopeType.DEPARTMENT.getCode());
                scopeMap.setSourceId(org.getId());
                scopeMap.setSourceDescription(org.getName());
                workReportProvider.createWorkReportScopeMap(scopeMap);
            }
            return null;
        });

        //  return back some information.
        WorkReportDTO dto = new WorkReportDTO();
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setReportAttribute(report.getReportAttribute());
        return dto;
    }

    @Override
    public void deleteWorkReport(WorkReportIdCommand cmd) {
        WorkReport report = workReportProvider.findWorkReport(cmd.getReportId());
        if (report != null) {
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
            report.setReportType(cmd.getReportType());
            report.setFormOriginId(cmd.getFormOriginId());
            report.setFormVersion(cmd.getFormVersion());
            dbProvider.execute((TransactionStatus status) -> {
                workReportProvider.updateWorkReport(report);
                updateWorkReportScopeMap(report.getId(), cmd.getScopes());
                return null;
            });

            //  return back
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

    private void updateWorkReportScopeMap(Long reportId, List<WorkReportScopeMapDTO> scopes){
        if(scopes == null)
            return;
        List<Long> sourceIds = new ArrayList<>();
        for(WorkReportScopeMapDTO dto : scopes){
            //  in order to record those ids.
            sourceIds.add(dto.getSourceId());
            WorkReportScopeMap scopeMap = workReportProvider.findWorkReportScopeMapBySourceId(reportId, dto.getSourceId());
            if(scopeMap !=null) {
                scopeMap.setSourceDescription(dto.getSourceDescription());
                scopeMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                workReportProvider.updateWorkReportScopeMap(scopeMap);
            }else {
                workReportProvider.createWorkReportScopeMap(scopeMap);
            }
        }
        //  remove the extra scope.
        workReportProvider.deleteWorkReportScopeMapNotInIds(reportId, sourceIds);
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
                cmd.getModuleId(), null);
        //  convert to the result.
        if (results != null && results.size() > 0) {
            //  pageAnchor.
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }
            //  DTO.
            results.forEach(r -> {
                WorkReportDTO dto = ConvertHelper.convert(r, WorkReportDTO.class);
                dto.setReportId(r.getId());
                dto.setScopes(listWorkReportScopeMap(r.getId()));
                reports.add(dto);
            });
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setReports(reports);
        return response;
    }

    @Override
    public WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd) {
        WorkReport report = workReportProvider.findWorkReport(cmd.getReportId());
        report.setReportName(cmd.getReportName());
        workReportProvider.updateWorkReport(report);

        WorkReportDTO dto = new WorkReportDTO();
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setReportAttribute(report.getReportAttribute());
        return dto;
    }

    @Override
    public void enableWorkReport(WorkReportIdCommand cmd) {
        WorkReport report = workReportProvider.findWorkReport(cmd.getReportId());
        report.setReportType(WorkReportStatus.RUNNING.getCode());
        workReportProvider.updateWorkReport(report);
    }

    @Override
    public void disableWorkReport(WorkReportIdCommand cmd) {
        WorkReport report = workReportProvider.findWorkReport(cmd.getReportId());
        report.setReportType(WorkReportStatus.VALID.getCode());
        workReportProvider.updateWorkReport(report);
    }

    public List<WorkReportScopeMapDTO> listWorkReportScopeMap(Long reportId){
        List<WorkReportScopeMap> results = workReportProvider.listWorkReportScopeMap(reportId);
        if(results !=null && results.size()>0){
            return results.stream().map(r ->{
                WorkReportScopeMapDTO dto = ConvertHelper.convert(r, WorkReportScopeMapDTO.class);
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public VerifyWorkReportResponse verifyWorkReportTemplates(CreateWorkReportTemplatesCommand cmd) {
        return null;
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
