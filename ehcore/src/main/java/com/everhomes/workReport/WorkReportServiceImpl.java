package com.everhomes.workReport;

import com.everhomes.db.DbProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.CreateFormTemplatesCommand;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.workReport.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkReportServiceImpl implements WorkReportService {

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private WorkReportValProvider workReportValProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private GeneralFormService generalFormService;

    private SimpleDateFormat reportFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static String WORK_REPORT = "WORK_REPORT";

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
            createWorkReport(report, cmd.getOrganizationId(), namespaceId);
            return null;
        });

        //  return back some information.
        WorkReportDTO dto = new WorkReportDTO();
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setReportAttribute(report.getReportAttribute());
        return dto;
    }

    private void createWorkReport(WorkReport report, Long organizationId, Integer namespaceId) {
        Long reportId = workReportProvider.createWorkReport(report);
        Organization org = organizationProvider.findOrganizationById(organizationId);
        if (org != null) {
            WorkReportScopeMap scopeMap = new WorkReportScopeMap();
            scopeMap.setReportId(reportId);
            scopeMap.setNamespaceId(namespaceId);
            scopeMap.setSourceType(UniongroupTargetType.ORGANIZATION.getCode());
            scopeMap.setSourceId(org.getId());
            scopeMap.setSourceDescription(org.getName());
            workReportProvider.createWorkReportScopeMap(scopeMap);
        }
    }

    @Override
    public void deleteWorkReport(WorkReportIdCommand cmd) {
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        if (report != null) {
            report.setStatus(WorkReportStatus.INVALID.getCode());
            workReportProvider.updateWorkReport(report);
        }
    }

    @Override
    public WorkReportDTO updateWorkReport(UpdateWorkReportCommand cmd) {
        //  find the report by id.
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
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

    private void updateWorkReportScopeMap(Long reportId, List<WorkReportValScopeMapDTO> scopes) {
        if (scopes == null)
            return;
        List<Long> sourceIds = new ArrayList<>();
        for (WorkReportValScopeMapDTO dto : scopes) {
            //  in order to record those ids.
            sourceIds.add(dto.getSourceId());
            WorkReportScopeMap scopeMap = workReportProvider.getWorkReportScopeMapBySourceId(reportId, dto.getSourceId());
            if (scopeMap != null) {
                scopeMap.setSourceDescription(dto.getSourceDescription());
                scopeMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                workReportProvider.updateWorkReportScopeMap(scopeMap);
            } else {
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
                String updateTime = reportFormat.format(r.getUpdateTime());
                //todo:修改人
                dto.setUpdateInfo(updateTime + " " );
                reports.add(dto);
            });
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setReports(reports);
        return response;
    }

    @Override
    public WorkReportDTO updateWorkReportName(UpdateWorkReportNameCommand cmd) {
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
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
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        report.setReportType(WorkReportStatus.RUNNING.getCode());
        workReportProvider.updateWorkReport(report);
    }

    @Override
    public void disableWorkReport(WorkReportIdCommand cmd) {
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        report.setReportType(WorkReportStatus.VALID.getCode());
        workReportProvider.updateWorkReport(report);
    }

    public List<WorkReportValScopeMapDTO> listWorkReportScopeMap(Long reportId) {
        List<WorkReportScopeMap> results = workReportProvider.listWorkReportScopeMap(reportId);
        if (results != null && results.size() > 0) {
            return results.stream().map(r -> {
                WorkReportValScopeMapDTO dto = ConvertHelper.convert(r, WorkReportValScopeMapDTO.class);
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public VerifyWorkReportResponse verifyWorkReportTemplates(CreateWorkReportTemplatesCommand cmd) {
        VerifyWorkReportResponse response = new VerifyWorkReportResponse();
        response.setResult(TrueOrFalseFlag.TRUE.getCode());
        List<WorkReportTemplate> templates = workReportProvider.listWorkReportTemplates(cmd.getModuleId());
        for (WorkReportTemplate template : templates) {
            WorkReport report = workReportProvider.getWorkReportByTemplateId(UserContext.getCurrentNamespaceId(),
                    template.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), template.getId());
            if (report == null) {
                response.setResult(TrueOrFalseFlag.FALSE.getCode());
                break;
            }
        }
        return response;
    }

    @Override
    public void createWorkReportTemplates(CreateWorkReportTemplatesCommand cmd) {
        //  1.get templates by moduleId and then check the formTemplateId.
        //  2.create the work report if the formTemplateId is 0 immediately.
        //  3.Otherwise create the form before creating work reports.
        List<WorkReportTemplate> templates = workReportProvider.listWorkReportTemplates(cmd.getModuleId());
        if (templates != null && templates.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                for (WorkReportTemplate template : templates) {
                    if (template.getFormTemplateId().longValue() == 0)
                        createWorkReportByTemplate(template, null, cmd);
                    else {
                        CreateFormTemplatesCommand command = ConvertHelper.convert(cmd, CreateFormTemplatesCommand.class);
                        Long formOriginId = generalFormService.createGeneralFormByTemplate(template.getFormTemplateId(), command);
                        createWorkReportByTemplate(template, formOriginId, cmd);
                    }
                }
                return null;
            });
        }
    }

    @Override
    public ListWorkReportsResponse listActiveWorkReports(ListWorkReportsCommand cmd) {
        return null;
    }

    private void createWorkReportByTemplate(WorkReportTemplate template, Long formOriginId, CreateWorkReportTemplatesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        WorkReport report = workReportProvider.getWorkReportByTemplateId(UserContext.getCurrentNamespaceId(),
                template.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), template.getId());
        //  update the report if it is already existing.
        if (report != null) {
            report.setStatus(WorkReportStatus.RUNNING.getCode());
            report.setReportName(template.getReportName());
            report.setReportType(template.getReportType());
            if (formOriginId != null)
                report.setFormOriginId(formOriginId);
            workReportProvider.updateWorkReport(report);
        } else {
            //  otherwise, create the report
            report = ConvertHelper.convert(template, WorkReport.class);
            report.setNamespaceId(namespaceId);
            report.setOwnerId(cmd.getOwnerId());
            report.setOwnerType(cmd.getOwnerType());
            report.setOrganizationId(cmd.getOrganizationId());
            report.setStatus(WorkReportStatus.RUNNING.getCode());
            if (formOriginId != null)
                report.setFormOriginId(formOriginId);
            report.setReportTemplateId(template.getId());
            createWorkReport(report, cmd.getOrganizationId(), namespaceId);
        }
    }

    public String fixUpUserName(Long organizationId, Long userId) {
        OrganizationMember om = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
        if (om != null && om.getContactName() != null && !om.getContactName().isEmpty())
            return om.getContactName();
        return "";
    }

    @Override
    public void postWorkReportVal(PostWorkReportValCommand cmd) {
        //  There are three steps to finish this function.
        //  1.create the report val
        //  2.create the report form val
        //  3.create the report receivers
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        User user = UserContext.current().getUser();
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        WorkReportVal val = new WorkReportVal();

        val.setNamespaceId(namespaceId);
        val.setOwnerId(report.getOwnerId());
        val.setOwnerType(report.getOwnerType());
        val.setOrganizationId(cmd.getOrganizationId());
        val.setModuleId(report.getModuleId());
        val.setModuleType(report.getModuleType());
        //  set the content.
/*        switch (WorkReportType.fromCode(cmd.getReportType())){
            case DAY:

        }*/
        val.setReportId(cmd.getReportId());
        val.setReportTime(cmd.getReportTime());
        val.setApplierUserId(user.getId());
        val.setApplierName(fixUpUserName(cmd.getOrganizationId(), user.getId()));
        val.setReportType(cmd.getReportType());

        PostGeneralFormCommand formCommand = new PostGeneralFormCommand();
        formCommand.setNamespaceId(namespaceId);
        formCommand.setOwnerId(val.getOwnerId());
        formCommand.setOwnerType(val.getOwnerType());
        formCommand.setSourceType(WORK_REPORT);
        formCommand.setCurrentOrganizationId(cmd.getOrganizationId());
        formCommand.setValues(cmd.getValues());

        dbProvider.execute((TransactionStatus status) ->{
            Long reportValId = workReportValProvider.createWorkReportVal(val);
            formCommand.setSourceId(reportValId);
            generalFormService.postGeneralForm(formCommand);
            for(Long receiverId : cmd.getReceiverIds()){
                WorkReportValReceiverMap receiver = new WorkReportValReceiverMap();
                receiver.setNamespaceId(namespaceId);
                receiver.setReportValId(reportValId);
                receiver.setReceiverUserId(receiverId);
                receiver.setReceiverName(fixUpUserName(cmd.getOrganizationId(),receiverId));
                workReportValProvider.createWorkReportValReceiverMap(receiver);
            }
            return null;
        });
    }

    @Override
    public void deleteWorkReportVal(WorkReportValIdCommand cmd) {

    }

    @Override
    public void updateWorkReportVal(PostWorkReportValCommand cmd) {

    }

    @Override
    public WorkReportValDTO getWorkReportValItem(WorkReportValIdCommand cmd) {
        return null;
    }

    @Override
    public ListWorkReportsValResponse listSubmittedWorkReportsVal(ListWorkReportsValCommand cmd) {
        return null;
    }

    @Override
    public ListWorkReportsValResponse listReceivedWorkReportsVal(ListWorkReportsValCommand cmd) {
        return null;
    }

    @Override
    public Integer countUnReadWorkReportsVal() {
        return null;
    }

    @Override
    public WorkReportValDTO getWorkReportValDetail(WorkReportValIdCommand cmd) {
        return null;
    }
}
