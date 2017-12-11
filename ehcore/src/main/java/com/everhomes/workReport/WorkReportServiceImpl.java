package com.everhomes.workReport;

import com.everhomes.db.DbProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.ui.user.SceneContactDTO;
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

    private final String WORK_REPORT = "WORK_REPORT";

    private final String WORK_REPORT_VAL = "work_report_val";

    @Override
    public WorkReportDTO addWorkReport(AddWorkReportCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();
        WorkReport report = new WorkReport();

        //  initialize the work report.
        report.setNamespaceId(namespaceId);
        report.setOwnerId(cmd.getOwnerId());
        report.setOwnerType(cmd.getOwnerType());
        report.setOrganizationId(cmd.getOrganizationId());
        report.setReportName(cmd.getReportName());
        report.setModuleId(cmd.getModuleId());
        /*report.setStatus(WorkReportStatus.VALID.getCode());
        report.setReportAttribute(WorkReportAttribute.CUSTOMIZE.getCode());
        report.setDeleteFlag(AttitudeFlag.YES.getCode());
        report.setModifyFlag(AttitudeFlag.YES.getCode());*/
        report.setOperatorUserId(userId);
        report.setOperatorName(fixUpUserName(report.getOrganizationId(), userId));

        //  add it with the initial scope.
        dbProvider.execute((TransactionStatus status) -> {
            createWorkReport(report, cmd.getOrganizationId(), namespaceId);
            return null;
        });

        //  return back some information.
        WorkReportDTO dto = new WorkReportDTO();
        dto.setReportName(report.getReportName());
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
        Long userId = UserContext.currentUserId();
        //  find the report by id.
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        if (report != null) {
            //  update it.
            if(cmd.getReportType() != null)
                report.setReportType(cmd.getReportType());
            report.setFormOriginId(cmd.getFormOriginId());
            report.setFormVersion(cmd.getFormVersion());
            report.setOperatorUserId(userId);
            report.setOperatorName(fixUpUserName(report.getOrganizationId(), userId));
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

    private void updateWorkReportScopeMap(Long reportId, List<WorkReportScopeMapDTO> scopes) {
        if (scopes == null)
            return;
        List<Long> sourceIds = new ArrayList<>();
        for (WorkReportScopeMapDTO dto : scopes) {
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
                dto.setScopes(listWorkReportScopes(r.getId()));
                String updateTime = reportFormat.format(r.getUpdateTime());
                dto.setUpdateInfo(updateTime + " " + r.getOperatorName());
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

    public List<WorkReportScopeMapDTO> listWorkReportScopes(Long reportId) {
        List<WorkReportScopeMap> results = workReportProvider.listWorkReportScopesMap(reportId);
        if (results != null && results.size() > 0) {
            return results.stream().map(r -> {
                WorkReportScopeMapDTO dto = ConvertHelper.convert(r, WorkReportScopeMapDTO.class);
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

    private void createWorkReportByTemplate(WorkReportTemplate template, Long formOriginId, CreateWorkReportTemplatesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();
        WorkReport report = workReportProvider.getWorkReportByTemplateId(UserContext.getCurrentNamespaceId(),
                template.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), template.getId());
        //  update the report if it is already existing.
        if (report != null) {
            report.setStatus(WorkReportStatus.RUNNING.getCode());
            report.setReportName(template.getReportName());
            report.setReportType(template.getReportType());
            report.setOperatorUserId(userId);
            report.setOperatorName(fixUpUserName(report.getOrganizationId(), userId));
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
            report.setOperatorUserId(userId);
            report.setOperatorName(fixUpUserName(report.getOrganizationId(), userId));
            if (formOriginId != null)
                report.setFormOriginId(formOriginId);
            report.setReportTemplateId(template.getId());
            createWorkReport(report, cmd.getOrganizationId(), namespaceId);
        }
    }

    @Override
    public ListWorkReportsResponse listActiveWorkReports(ListWorkReportsCommand cmd) {
        ListWorkReportsResponse response = new ListWorkReportsResponse();
        Long userId = UserContext.currentUserId();
        List<WorkReportDTO> reports = new ArrayList<>();
        cmd.setPageSize(10000000);

        //  get all work reports.
        List<WorkReport> results = workReportProvider.listWorkReports(
                cmd.getPageAnchor(), cmd.getPageSize(), cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getModuleId(), WorkReportStatus.RUNNING.getCode());

        //  filter the result and return back to the user.
        if (results != null && results.size() > 0) {
            results.forEach(r -> {
                WorkReportDTO dto = new WorkReportDTO();
                dto.setReportName(r.getReportName());
                dto.setReportId(r.getId());
                //  check the scope.
                if(checkTheScope(r.getId(), userId))
                    reports.add(dto);
            });
        }

        response.setReports(reports);
        return response;
    }

    private boolean checkTheScope(Long reportId, Long userId) {
        //  1.check the user id list.
        //  2.check the user's department.
        List<WorkReportScopeMapDTO> scopes = listWorkReportScopes(reportId);
        OrganizationMember user = getUserDepPath(userId);
        List<Long> scopeUserIds = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.MEMBERDETAIL.getCode()))
                .map(p2 -> p2.getSourceId()).collect(Collectors.toList());
        List<Long> scopeDepartmentIds = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.ORGANIZATION.getCode()))
                .map(p2 -> p2.getSourceId()).collect(Collectors.toList());
        if (scopeUserIds.contains(userId))
            return true;
        if (user != null)
            for (Long departmentId : scopeDepartmentIds)
                if (user.getGroupPath().contains(String.valueOf(departmentId)))
                    return true;
        return false;
    }

    public String fixUpUserName(Long organizationId, Long userId) {
        OrganizationMember om = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
        if (om != null && om.getContactName() != null && !om.getContactName().isEmpty())
            return om.getContactName();
        return "";
    }

    public OrganizationMember getUserDepPath(Long userId) {
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(userId).stream().filter(r ->
                r.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())
        ).collect(Collectors.toList());
        if (members != null && members.size() > 0)
            return members.get(0);
        return null;
    }

    @Override
    public WorkReportValDTO postWorkReportVal(PostWorkReportValCommand cmd) {
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
        val.setStatus(WorkReportStatus.VALID.getCode());
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

        Long reportValId = dbProvider.execute((TransactionStatus status) -> {
            Long valId = workReportValProvider.createWorkReportVal(val);
            formCommand.setSourceId(valId);
            generalFormService.postGeneralForm(formCommand);
            for (Long receiverId : cmd.getReceiverIds()) {
                WorkReportValReceiverMap receiver = new WorkReportValReceiverMap();
                receiver.setNamespaceId(namespaceId);
                receiver.setReportValId(valId);
                receiver.setReceiverUserId(receiverId);
                receiver.setReceiverName(fixUpUserName(cmd.getOrganizationId(), receiverId));
                receiver.setReceiverAvatar(user.getAvatar());
                receiver.setReadStatus(WorkReportReadStatus.UNREAD.getCode());
                workReportValProvider.createWorkReportValReceiverMap(receiver);
            }
            return valId;
        });

        //  return back;
        WorkReportValDTO dto = new WorkReportValDTO();
        dto.setReportId(report.getId());
        dto.setReportValId(reportValId);
        return dto;
    }

    @Override
    public void deleteWorkReportVal(WorkReportValIdCommand cmd) {
        WorkReportVal reportVal = workReportValProvider.getWorkReportValById(cmd.getReportValId());
        if (reportVal != null) {
            reportVal.setStatus(WorkReportStatus.INVALID.getCode());
            dbProvider.execute((TransactionStatus status) -> {
                //  1.delete the report value.
                workReportValProvider.updateWorkReportVal(reportVal);
                //  2.delete the report receivers.
                workReportValProvider.deleteReportValReceiverByValId(reportVal.getId());
                return null;
            });
        }
    }

    @Override
    public WorkReportValDTO updateWorkReportVal(PostWorkReportValCommand cmd) {
        return null;
    }

    @Override
    public WorkReportValDTO getWorkReportValItem(WorkReportValIdCommand cmd) {
        //  1.the reportValId is null means posting the report val.
        //  2.the reportValId is not null means updating the report val.
        WorkReportValDTO dto = new WorkReportValDTO();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();

        if (cmd.getReportValId() != null) {
            WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
            List<GeneralFormFieldDTO> fields = new ArrayList<>();

            //  get the new form fields.
            GetTemplateBySourceIdCommand formCommand = new GetTemplateBySourceIdCommand();
            formCommand.setNamespaceId(namespaceId);
            formCommand.setOwnerId(report.getOwnerId());
            formCommand.setOwnerType(report.getOwnerType());
            formCommand.setSourceId(report.getId());
            formCommand.setSourceType(WORK_REPORT);
            GeneralFormDTO form = generalFormService.getTemplateBySourceId(formCommand);

            //  get the field values which has been post by the user.
            GetGeneralFormValuesCommand valuesCommand = new GetGeneralFormValuesCommand();
            valuesCommand.setSourceId(userId);
            valuesCommand.setSourceType(WORK_REPORT_VAL);
            List<PostApprovalFormItem> values = generalFormService.getGeneralFormValues(valuesCommand);

            //  process the value.
            for(GeneralFormFieldDTO field: form.getFormFields()){
                for(PostApprovalFormItem value : values){
                    if(field.getFieldName().equals(value.getFieldName())) {
                        field.setFieldValue(value.getFieldValue());
                        continue;
                    }
                    fields.add(field);
                }
            }

            //  get the reportVal and receivers.
            WorkReportVal reportVal = workReportValProvider.getWorkReportValById(cmd.getReportValId());
            List<SceneContactDTO> receivers = listWorkReportValReceivers(cmd.getReportValId());

            //  return back the reportVal.
            dto.setReportId(report.getId());
            dto.setReportType(reportVal.getReportType());
            dto.setReportTime(reportVal.getReportTime());
            dto.setValues(fields);
            dto.setReceivers(receivers);
            return dto;
        } else {
            WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
            GetTemplateBySourceIdCommand formCommand = new GetTemplateBySourceIdCommand();
            formCommand.setNamespaceId(namespaceId);
            formCommand.setOwnerId(report.getOwnerId());
            formCommand.setOwnerType(report.getOwnerType());
            formCommand.setSourceId(report.getId());
            formCommand.setSourceType(WORK_REPORT);
            GeneralFormDTO form = generalFormService.getTemplateBySourceId(formCommand);

            dto.setReportId(report.getId());
            dto.setReportType(report.getReportType());
            dto.setValues(form.getFormFields());
            dto.setTitle(report.getReportName());
            return dto;
        }
    }

    public List<SceneContactDTO> listWorkReportValReceivers (Long reportValId) {
        List<WorkReportValReceiverMap> results = workReportValProvider.listReportValReceiversByValId(reportValId);
        if(results !=null && results.size()>0){
            return results.stream().map(r ->{
                SceneContactDTO dto = new SceneContactDTO();
                dto.setUserId(r.getReceiverUserId());
                dto.setContactName(r.getReceiverName());
                dto.setContactAvatar(r.getReceiverAvatar());
                return dto;
            }).collect(Collectors.toList());
        }
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
    public void MarkWorkReportsValReading() {

    }

    @Override
    public WorkReportValDTO getWorkReportValDetail(WorkReportValIdCommand cmd) {
        return null;
    }
}
