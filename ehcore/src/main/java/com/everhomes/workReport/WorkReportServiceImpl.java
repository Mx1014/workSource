package com.everhomes.workReport;

import com.alibaba.fastjson.JSON;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.comment.OwnerTokenDTO;
import com.everhomes.rest.comment.OwnerType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.ui.user.SceneContactDTO;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.workReport.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkReportServiceImpl implements WorkReportService {

    @Autowired
    private WorkReportProvider workReportProvider;

    @Autowired
    private WorkReportValProvider workReportValProvider;

    @Autowired
    private WorkReportMessageService workReportMessageService;

    @Autowired
    private WorkReportTimeService workReportTimeService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private GeneralFormService generalFormService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    private DateTimeFormatter reportFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String WORK_REPORT = "WORK_REPORT";

    private final String WORK_REPORT_VAL = "work_report_val";

    private final String WORK_REPORT_ICON = "work.report.icon";

    @Override
    public WorkReportDTO addWorkReport(AddWorkReportCommand cmd) {
        Long userId = UserContext.currentUserId();
        WorkReport report = new WorkReport();

        //  initialize the work report.
        report.setNamespaceId(cmd.getNamespaceId());
        report.setOwnerId(cmd.getOwnerId());
        report.setOwnerType(cmd.getOwnerType());
        report.setOrganizationId(cmd.getOrganizationId());
        report.setReportName(cmd.getReportName());
        report.setModuleId(cmd.getModuleId());
        report.setModuleType(cmd.getModuleType());
        report.setOperatorUserId(userId);
        report.setOperatorName(fixUpUserName(userId, cmd.getOwnerId()));
        report.setIconUri(configurationProvider.getValue(WORK_REPORT_ICON, ""));

        //  add it with the initial scope.
        dbProvider.execute((TransactionStatus status) -> {
            createWorkReport(report, cmd.getOrganizationId(), cmd.getNamespaceId());
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
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        //  find the report by id.
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        if (report == null)
            return null;
        LocalDateTime time = LocalDateTime.now();

        //  update it.
        if (cmd.getReportType() != null)
            report.setReportType(cmd.getReportType());
        report.setValiditySetting(JSON.toJSONString(cmd.getValiditySetting()));
        report.setReceiverMsgType(cmd.getRxMsgType());
        if (cmd.getRxMsgSetting() != null)
            report.setReceiverMsgSeeting(JSON.toJSONString(cmd.getRxMsgSetting()));
        report.setAuthorMsgType(cmd.getAuMsgType());
        if (cmd.getAuMsgSetting() != null)
            report.setAuthorMsgSeeting(JSON.toJSONString(cmd.getAuMsgSetting()));
        report.setFormOriginId(cmd.getFormOriginId());
        if (cmd.getFormOriginId() == 0)
            report.setStatus(WorkReportStatus.VALID.getCode());
        report.setFormVersion(cmd.getFormVersion());
        report.setOperatorUserId(userId);
        report.setOperatorName(fixUpUserName(userId, report.getOwnerId()));
        dbProvider.execute((TransactionStatus status) -> {
            workReportProvider.updateWorkReport(report);
            updateWorkReportScopeMap(report.getNamespaceId(), report.getId(), cmd.getScopes());
            return null;
        });

        if (WorkReportStatus.fromCode(report.getStatus()) == WorkReportStatus.RUNNING) {
            //  Enable another thread to update the data.
            ExecutorUtil.submit(() -> {
                UserContext.setCurrentNamespaceId(namespaceId);
                //  Make sure that the msg time could be chaned once at the same time.
                coordinationProvider.getNamedLock(CoordinationLocks.WORK_REPORT_AU_BASIC_MSG.getCode() + report.getId()).tryEnter(() -> {

                    Timestamp reportTime = workReportTimeService.getReportTime(report.getReportType(), time, cmd.getValiditySetting());
                    //  update the rxMsg
                    if (cmd.getRxMsgSetting() != null)
                        updateWorkReportValReceiverMsg(report, cmd.getRxMsgSetting(), reportTime);
                    //  update the auMsg
                    if (cmd.getAuMsgSetting() != null)
                        workReportMessageService.createWorkReportScopeMsg(report, cmd.getAuMsgSetting(), cmd.getValiditySetting(), reportTime);
                });
            });
        }

        //  return back
        WorkReportDTO dto = new WorkReportDTO();
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setReportAttribute(report.getReportAttribute());
        dto.setFormOriginId(report.getFormOriginId());
        dto.setFormVersion(report.getFormVersion());
        return dto;
    }

    private void updateWorkReportScopeMap(Integer namespaceId, Long reportId, List<WorkReportScopeMapDTO> scopes) {
        List<Long> detailIds = new ArrayList<>();
        List<Long> organizationIds = new ArrayList<>();

        if (scopes != null && scopes.size() > 0)
            for (WorkReportScopeMapDTO dto : scopes) {
                //  in order to record those ids.
                if (dto.getSourceType().equals(UniongroupTargetType.ORGANIZATION.getCode()))
                    organizationIds.add(dto.getSourceId());
                else if (dto.getSourceType().equals(UniongroupTargetType.MEMBERDETAIL.getCode()))
                    detailIds.add(dto.getSourceId());

                WorkReportScopeMap scopeMap = workReportProvider.getWorkReportScopeMapBySourceId(reportId, dto.getSourceId());
                if (scopeMap != null) {
                    scopeMap.setSourceDescription(dto.getSourceDescription());
                    scopeMap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    workReportProvider.updateWorkReportScopeMap(scopeMap);
                } else {
                    scopeMap = new WorkReportScopeMap();
                    scopeMap.setNamespaceId(UserContext.getCurrentNamespaceId());
                    scopeMap.setReportId(reportId);
                    scopeMap.setSourceType(dto.getSourceType());
                    scopeMap.setSourceId(dto.getSourceId());
                    scopeMap.setSourceDescription(dto.getSourceDescription());
                    workReportProvider.createWorkReportScopeMap(scopeMap);
                }
            }

        //  remove the extra scope.
        if (detailIds.size() == 0)
            detailIds.add(0L);
        workReportProvider.deleteOddWorkReportScope(namespaceId, reportId, UniongroupTargetType.MEMBERDETAIL.getCode(), detailIds);
        if (organizationIds.size() == 0)
            organizationIds.add(0L);
        workReportProvider.deleteOddWorkReportScope(namespaceId, reportId, UniongroupTargetType.ORGANIZATION.getCode(), organizationIds);
    }

    private void updateWorkReportValReceiverMsg(WorkReport report, ReportMsgSettingDTO msgSetting, Timestamp reportTime) {
        List<WorkReportValReceiverMsg> msgs = workReportValProvider.listReportValReceiverMsgByReportTime
                (report.getId(), workReportTimeService.toSqlDate(reportTime.getTime()));
        Timestamp reminderTime = Timestamp.valueOf(workReportTimeService.getSettingTime(report.getReportType(), reportTime.getTime(),
                msgSetting.getMsgTimeType(), msgSetting.getMsgTimeMark(), msgSetting.getMsgTime()));
        if (msgs.size() > 0)
            msgs.forEach(m -> {
                m.setReportName(report.getReportName());
                m.setReminderTime(reminderTime);
                workReportValProvider.updateWorkReportValReceiverMsg(m);
            });
    }

    @Override
    public WorkReportDTO getWorkReport(WorkReportIdCommand cmd) {
        WorkReport r = workReportProvider.getWorkReportById(cmd.getReportId());
        if (r == null || cmd.getNamespaceId().intValue() != r.getNamespaceId().intValue())
            return null;
        WorkReportDTO dto = ConvertHelper.convert(r, WorkReportDTO.class);
        dto.setReportId(r.getId());
        dto.setScopes(listWorkReportScopes(r.getId()));
        dto.setValiditySetting(JSON.parseObject(r.getValiditySetting(), ReportValiditySettingDTO.class));
        dto.setRxMsgType(r.getReceiverMsgType());
        dto.setRxMsgSetting(JSON.parseObject(r.getReceiverMsgSeeting(), ReportMsgSettingDTO.class));
        dto.setAuMsgType(r.getAuthorMsgType());
        dto.setAuMsgSetting(JSON.parseObject(r.getAuthorMsgSeeting(), ReportMsgSettingDTO.class));
        return dto;
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
            //  DTO(name, scopes).
            results.forEach(r -> {
                WorkReportDTO dto = ConvertHelper.convert(r, WorkReportDTO.class);
                dto.setReportId(r.getId());
                dto.setScopes(listWorkReportScopes(r.getId()));
                String updateTime = reportFormat.format(r.getUpdateTime().toLocalDateTime());
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
        Long userId = UserContext.currentUserId();
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        report.setReportName(cmd.getReportName());
        report.setOperatorUserId(userId);
        report.setOperatorName(fixUpUserName(userId, report.getOwnerId()));
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
        report.setStatus(WorkReportStatus.RUNNING.getCode());
        workReportProvider.updateWorkReport(report);
    }

    @Override
    public void disableWorkReport(WorkReportIdCommand cmd) {
        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        report.setStatus(WorkReportStatus.VALID.getCode());
        workReportProvider.updateWorkReport(report);
    }

    @Override
    public void disableWorkReportByFormOriginId(Long formOriginId, Long moduleId, String moduleType) {
        workReportProvider.disableWorkReportByFormOriginId(formOriginId, moduleId, moduleType);
    }

    private List<WorkReportScopeMapDTO> listWorkReportScopes(Long reportId) {
        List<WorkReportScopeMap> results = workReportProvider.listWorkReportScopesMap(reportId);
        if (results != null && results.size() > 0) {
            return results.stream().map(r -> ConvertHelper.convert(r, WorkReportScopeMapDTO.class)).collect(Collectors.toList());
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
                    if (template.getFormTemplateId() == 0)
                        createWorkReportByTemplate(template, null, cmd);
                    else {
                        CreateFormTemplatesCommand command = ConvertHelper.convert(cmd, CreateFormTemplatesCommand.class);
                        GeneralFormDTO form = generalFormService.createGeneralFormByTemplate(template.getFormTemplateId(), command);
                        createWorkReportByTemplate(template, form, cmd);
                    }
                }
                return null;
            });
        }
    }

    private void createWorkReportByTemplate(WorkReportTemplate template, GeneralFormDTO form, CreateWorkReportTemplatesCommand cmd) {
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
            report.setOperatorName(fixUpUserName(userId, cmd.getOwnerId()));
            report.setIconUri(template.getIconUri());
            if (form != null) {
                report.setFormOriginId(form.getFormOriginId());
                report.setFormVersion(form.getFormVersion());
            }
            workReportProvider.updateWorkReport(report);
        } else {
            //  otherwise, create the report.
            report = ConvertHelper.convert(template, WorkReport.class);
            report.setNamespaceId(namespaceId);
            report.setOwnerId(cmd.getOwnerId());
            report.setOwnerType(cmd.getOwnerType());
            report.setOrganizationId(cmd.getOrganizationId());
            report.setStatus(WorkReportStatus.RUNNING.getCode());
            report.setOperatorUserId(userId);
            report.setOperatorName(fixUpUserName(userId, cmd.getOwnerId()));
            report.setIconUri(template.getIconUri());
            if (form != null) {
                report.setFormOriginId(form.getFormOriginId());
                report.setFormVersion(form.getFormVersion());
            }
            report.setReportTemplateId(template.getId());
            createWorkReport(report, cmd.getOrganizationId(), namespaceId);
        }
    }

    @Override
    public ListWorkReportsResponse listActiveWorkReports(ListWorkReportsCommand cmd) {
        ListWorkReportsResponse response = new ListWorkReportsResponse();
        Long userId = UserContext.currentUserId();
        OrganizationMember member = getMemberDepartmentByUserId(userId, cmd.getOwnerId());
        if (member == null)
            member = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(userId, cmd.getOwnerId());
        List<WorkReportDTO> reports = new ArrayList<>();
        cmd.setPageSize(Integer.MAX_VALUE - 1);

        //  get all work reports.
        List<WorkReport> results = workReportProvider.listWorkReports(
                cmd.getPageAnchor(), cmd.getPageSize(), cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getModuleId(), WorkReportStatus.RUNNING.getCode());

        //  filter the result and return back to the user.
        if (results != null && results.size() > 0) {
            for (WorkReport result : results) {
                WorkReportDTO dto = new WorkReportDTO();
                dto.setReportName(result.getReportName());
                dto.setReportId(result.getId());
                dto.setReportType(result.getReportType());
                dto.setIconUri(result.getIconUri());
                dto.setIconUrl(contentServerService.parserUri(dto.getIconUri()));
                //  check the scope.
                if (checkTheScope(result.getId(), member))
                    reports.add(dto);
            }
        }

        response.setReports(reports);
        return response;
    }

    private boolean checkTheScope(Long reportId, OrganizationMember member) {
        //  1.check the user id list.
        //  2.check the user's department.
        if (member == null)
            return false;
        List<WorkReportScopeMapDTO> scopes = listWorkReportScopes(reportId);
        if (scopes == null || scopes.size() == 0)
            return false;
        List<Long> scopeUserIds = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.MEMBERDETAIL.getCode()))
                .map(WorkReportScopeMapDTO::getSourceId).collect(Collectors.toList());
        List<Long> scopeDepartmentIds = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.ORGANIZATION.getCode()))
                .map(WorkReportScopeMapDTO::getSourceId).collect(Collectors.toList());
        if (scopeUserIds.contains(member.getDetailId()))
            return true;
        for (Long departmentId : scopeDepartmentIds)
            if (member.getGroupPath().contains(String.valueOf(departmentId)))
                return true;
        return false;
    }

    @Override
    public String fixUpUserName(Long userId, Long ownerId) {
        List<OrganizationMember> members = organizationProvider.findOrganizationMembersByOrgIdAndUId(userId, ownerId);
        if (members != null && members.size() > 0)
            return members.get(0).getContactName();
        return "";
    }

    private OrganizationMember getMemberDepartmentByUserId(Long userId, Long ownerId) {
        return organizationProvider.findDepartmentMemberByTargetIdAndOrgId(userId, ownerId);
    }

    @Override
    public Long getUserDetailId(Long userId, Long ownerId) {
        List<OrganizationMember> members = organizationProvider.findOrganizationMembersByOrgIdAndUId(userId, ownerId);
        if (members != null && members.size() > 0)
            return members.get(0).getDetailId();
        return null;
    }

    @Override
    public String getUserAvatar(Long userId) {
        User user = userProvider.findUserById(userId);
        if (null != user) {
            // return contentServerService.parserUri(user.getAvatar());
            return user.getAvatar();
        }
        return "";
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
        WorkReportVal reportVal = new WorkReportVal();
        ReportValiditySettingDTO validity = JSON.parseObject(report.getValiditySetting(), ReportValiditySettingDTO.class);

        LocalDateTime startTime = workReportTimeService.getSettingTime(report.getReportType(), cmd.getReportTime(), validity.getStartType(),
                validity.getStartMark(), validity.getStartTime());        //  获取汇报有效起始时间
        LocalDateTime endTime = workReportTimeService.getSettingTime(report.getReportType(), cmd.getReportTime(), validity.getEndType(),
                validity.getEndMark(), validity.getEndTime());            //  获取汇报有效截止时间

        //  whether the post time is valid
        if (checkPostTime(LocalDateTime.now(), startTime, endTime))
            throw RuntimeErrorException.errorWith(WorkReportErrorCode.SCOPE, WorkReportErrorCode.ERROR_WRONG_POST_TIME,
                    "The post time is not in the valid range");

        reportVal.setNamespaceId(namespaceId);
        reportVal.setOwnerId(report.getOwnerId());
        reportVal.setOwnerType(report.getOwnerType());
        reportVal.setOrganizationId(cmd.getOrganizationId());
        reportVal.setModuleId(report.getModuleId());
        reportVal.setModuleType(report.getModuleType());
        reportVal.setStatus(WorkReportStatus.VALID.getCode());
        reportVal.setReportId(cmd.getReportId());
        reportVal.setReportTime(workReportTimeService.toSqlDate(cmd.getReportTime()));
        reportVal.setApplierUserId(user.getId());
        reportVal.setApplierName(fixUpUserName(user.getId(), cmd.getOrganizationId()));
        reportVal.setReportType(cmd.getReportType());
        reportVal.setReceiverAvatar(getUserAvatar(cmd.getReceiverIds().get(0)));
        reportVal.setApplierAvatar(getUserAvatar(user.getId()));

        //  set the content.
        PostGeneralFormValCommand formCommand = new PostGeneralFormValCommand();
        formCommand.setNamespaceId(namespaceId);
        formCommand.setOwnerId(reportVal.getOwnerId());
        formCommand.setOwnerType(reportVal.getOwnerType());
        formCommand.setSourceType(WORK_REPORT);
        formCommand.setCurrentOrganizationId(cmd.getOrganizationId());
        formCommand.setValues(cmd.getValues());

        dbProvider.execute((TransactionStatus status) -> {
            Timestamp reminderTime = null;
            if (ReportReceiverMsgType.fromCode(report.getReceiverMsgType()) == ReportReceiverMsgType.SUMMARY) {
                ReportMsgSettingDTO msg = JSON.parseObject(report.getReceiverMsgSeeting(), ReportMsgSettingDTO.class);
                reminderTime = Timestamp.valueOf(workReportTimeService.getSettingTime(report.getReportType(), cmd.getReportTime(), msg.getMsgTimeType(), msg.getMsgTimeMark(), msg.getMsgTime()));
            }

            //  create the val
            Long reportValId = workReportValProvider.createWorkReportVal(reportVal);
            formCommand.setSourceId(reportValId);
            //  create the content
            generalFormService.postGeneralForm(formCommand);
            for (Long receiverId : cmd.getReceiverIds()) {
                //  create the receiver.
                createWorkReportValReceiverMap(reportVal, receiverId);
                //  send message to the receiver.
                if (ReportReceiverMsgType.fromCode(report.getReceiverMsgType()) == ReportReceiverMsgType.IMMEDIATELY)
                    workReportMessageService.workReportPostMessage(report, reportVal, receiverId, user);
                else
                    createWorkReportValReceiverMsg(report, reportVal, reminderTime, receiverId);
            }
            return null;
        });
        //  return back.
        WorkReportValDTO dto = new WorkReportValDTO();
        dto.setReportId(report.getId());
        dto.setReportValId(reportVal.getId());
        return dto;
    }

    private boolean checkPostTime(LocalDateTime postTime, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isBefore(postTime) && endTime.isAfter(postTime))
            return false;
        if (startTime.isEqual(postTime))
            return false;
        if (endTime.isEqual(postTime))
            return false;
        return true;
    }

    private WorkReportValReceiverMap createWorkReportValReceiverMap(WorkReportVal reportVal, Long receiverId) {
        WorkReportValReceiverMap receiver = new WorkReportValReceiverMap();
        receiver.setNamespaceId(reportVal.getNamespaceId());
        receiver.setOrganizationId(reportVal.getOrganizationId());
        receiver.setReportValId(reportVal.getId());
        receiver.setReceiverUserId(receiverId);
        receiver.setReceiverName(fixUpUserName(receiverId, reportVal.getOrganizationId()));
        receiver.setReceiverAvatar(getUserAvatar(receiverId));
        receiver.setReadStatus(WorkReportReadStatus.UNREAD.getCode());
        if (receiverId.longValue() == reportVal.getApplierUserId().longValue())
            receiver.setReadStatus(WorkReportReadStatus.READ.getCode());
        workReportValProvider.createWorkReportValReceiverMap(receiver);
        return receiver;
    }

    private WorkReportValReceiverMsg createWorkReportValReceiverMsg(WorkReport report, WorkReportVal reportVal, Timestamp reminderTime, Long receiverId){
        WorkReportValReceiverMsg msg = new WorkReportValReceiverMsg();
        msg.setNamespaceId(reportVal.getNamespaceId());
        msg.setOrganizationId(reportVal.getOrganizationId());
        msg.setReportId(report.getId());
        msg.setReportName(report.getReportName());
        msg.setReportValId(reportVal.getId());
        msg.setReportType(report.getReportType());
        msg.setReportTime(reportVal.getReportTime());
        msg.setReminderTime(reminderTime);
        msg.setReceiverUserId(receiverId);
        workReportValProvider.createWorkReportValReceiverMsg(msg);
        return msg;
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
                //  3.delete the report receiver msg.
                workReportValProvider.deleteReportValReceiverMsgByValId(reportVal.getId());
                return null;
            });
        }
    }

    @Override
    public WorkReportValDTO updateWorkReportVal(PostWorkReportValCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        User user = UserContext.current().getUser();

        WorkReportVal reportVal = workReportValProvider.getValidWorkReportValById(cmd.getReportValId());
        if (reportVal == null)
            throw RuntimeErrorException.errorWith(WorkReportErrorCode.SCOPE, WorkReportErrorCode.ERROR_REPORT_VAL_NOT_FOUND,
                    "work report val has been deleted.");
        WorkReport report = workReportProvider.getWorkReportById(reportVal.getReportId());

        PostGeneralFormValCommand formCommand = new PostGeneralFormValCommand();
        formCommand.setNamespaceId(namespaceId);
        formCommand.setOwnerId(reportVal.getOwnerId());
        formCommand.setOwnerType(reportVal.getOwnerType());
        formCommand.setSourceId(reportVal.getId());
        formCommand.setSourceType(WORK_REPORT);
        formCommand.setCurrentOrganizationId(cmd.getOrganizationId());
        formCommand.setValues(cmd.getValues());

        dbProvider.execute((TransactionStatus status) -> {
            Timestamp reminderTime = null;
            if (ReportReceiverMsgType.fromCode(report.getReceiverMsgType()) == ReportReceiverMsgType.SUMMARY) {
                ReportMsgSettingDTO msg = JSON.parseObject(report.getReceiverMsgSeeting(), ReportMsgSettingDTO.class);
                reminderTime = Timestamp.valueOf(workReportTimeService.getSettingTime(report.getReportType(), cmd.getReportTime(), msg.getMsgTimeType(), msg.getMsgTimeMark(), msg.getMsgTime()));
            }
            //  1.update the reportVal's updateTime
            reportVal.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            workReportValProvider.updateWorkReportVal(reportVal);
            //  2.update form values.
            generalFormService.updateGeneralFormVal(formCommand);
            //  3.update receivers.
            workReportValProvider.deleteReportValReceiverByValId(reportVal.getId());
            workReportValProvider.deleteReportValReceiverMsgByValId(reportVal.getId());
            for (Long receiverId : cmd.getReceiverIds()) {
                //  create the receiver.
                createWorkReportValReceiverMap(reportVal, receiverId);
                /*  WorkReportValReceiverMap receiver = packageWorkReportValReceiverMap(namespaceId, reportVal.getId(), receiverId, user.getId(), cmd.getOrganizationId());
                  workReportValProvider.createWorkReportValReceiverMap(receiver); */
                //  send message to the receiver.
                workReportMessageService.workReportUpdateMessage(report, reportVal, receiverId, user);
                if (ReportReceiverMsgType.fromCode(report.getReceiverMsgType()) == ReportReceiverMsgType.SUMMARY)
                    createWorkReportValReceiverMsg(report, reportVal, reminderTime, receiverId);
            }
            return null;
        });

        //  return back.
        WorkReportValDTO dto = new WorkReportValDTO();
        dto.setReportId(reportVal.getReportId());
        dto.setReportValId(reportVal.getId());
        return dto;
    }


    @Override
    public WorkReportValDTO getWorkReportValItem(WorkReportValIdCommand cmd) {
        //  1.the reportValId is null means posting the report val.
        //  2.the reportValId is not null means updating the report val.
        WorkReportValDTO dto = new WorkReportValDTO();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        LocalDateTime currentTime = LocalDateTime.now();

        if (cmd.getReportValId() != null) {
            WorkReport report = workReportProvider.getRunningWorkReportById(cmd.getReportId());
            if (report == null)
                throw RuntimeErrorException.errorWith(WorkReportErrorCode.SCOPE, WorkReportErrorCode.ERROR_WORK_REPORT_ABNORMAL,
                        "work report status is abnormal.");

            List<GeneralFormFieldDTO> fields = new ArrayList<>();

            //  get the new form fields.
            GetTemplateBySourceIdCommand formCommand = new GetTemplateBySourceIdCommand();
            formCommand.setNamespaceId(namespaceId);
            formCommand.setOwnerType(report.getOwnerType());
            formCommand.setSourceId(report.getId());
            formCommand.setSourceType(WORK_REPORT);
            GeneralFormDTO form = generalFormService.getTemplateBySourceId(formCommand);

            //  get the field values which has been post by the user.
            GetGeneralFormValuesCommand valuesCommand = new GetGeneralFormValuesCommand();
            valuesCommand.setSourceId(cmd.getReportValId());
            valuesCommand.setSourceType(WORK_REPORT_VAL);
            List<PostApprovalFormItem> values = generalFormService.getGeneralFormValues(valuesCommand);

            //  process the value.
            for (GeneralFormFieldDTO field : form.getFormFields()) {
                for (PostApprovalFormItem value : values) {
                    if (field.getFieldName().equals(value.getFieldName())) {
                        field.setFieldValue(value.getFieldValue());
                        break;
                    }
                }
                fields.add(field);
            }

            //  get the reportVal and receivers.
            WorkReportVal reportVal = workReportValProvider.getWorkReportValById(cmd.getReportValId());
            List<SceneContactDTO> receivers = listWorkReportValReceivers(cmd.getReportValId());

            //  return back the reportVal.
            dto.setReportId(report.getId());
            dto.setReportValId(reportVal.getId());
            dto.setReportType(reportVal.getReportType());
            dto.setReportTime(new Timestamp(reportVal.getReportTime().getTime()));
            dto.setReportTimeText(workReportTimeService.displayReportTime(dto.getReportType(), dto.getReportTime().getTime()));
            dto.setValues(fields);
            dto.setReceivers(receivers);
            return dto;
        }

        WorkReport report = workReportProvider.getWorkReportById(cmd.getReportId());
        ReportValiditySettingDTO setting = JSON.parseObject(report.getValiditySetting(), ReportValiditySettingDTO.class);
        Timestamp reportTime = workReportTimeService.getReportTime(report.getReportType(), currentTime, setting);
        LocalDateTime startTime = workReportTimeService.getSettingTime(report.getReportType(), reportTime.getTime(), setting.getStartType(),
                setting.getStartMark(), setting.getStartTime());        //  获取汇报有效起始时间
        LocalDateTime endTime = workReportTimeService.getSettingTime(report.getReportType(), reportTime.getTime(), setting.getEndType(),
                setting.getEndMark(), setting.getEndTime());            //  获取汇报有效截止时间
        //  check whether return the white page
        if (checkPostTime(LocalDateTime.now(), startTime, endTime)) {
            String description = "已超过截止时间，无法提交。下期汇报将于" + workReportTimeService.formatTime(startTime) + "开始提交。";
            throw RuntimeErrorException.errorWith(WorkReportErrorCode.SCOPE, WorkReportErrorCode.ERROR_WRONG_POST_TIME_V2, description);
        }

        GetTemplateBySourceIdCommand formCommand = new GetTemplateBySourceIdCommand();
        formCommand.setNamespaceId(namespaceId);
        formCommand.setOwnerId(report.getOwnerId());
        formCommand.setOwnerType(report.getOwnerType());
        formCommand.setSourceId(report.getId());
        formCommand.setSourceType(WORK_REPORT);
        GeneralFormDTO form = generalFormService.getTemplateBySourceId(formCommand);

        dto.setReportId(report.getId());
        dto.setReportType(report.getReportType());
        dto.setValiditySetting(setting);
        dto.setReportTime(reportTime);
        dto.setReportTimeText(workReportTimeService.displayReportTime(dto.getReportType(), dto.getReportTime().getTime()));
        dto.setValidText(workReportTimeService.formatTime(endTime));
        dto.setValues(form.getFormFields());
        dto.setTitle(report.getReportName());
        return dto;
    }

    private List<SceneContactDTO> listWorkReportValReceivers(Long reportValId) {
        List<WorkReportValReceiverMap> results = workReportValProvider.listReportValReceiversByValId(reportValId);
        if (results != null && results.size() > 0) {
            return results.stream().map(r -> {
                SceneContactDTO dto = new SceneContactDTO();
                dto.setUserId(r.getReceiverUserId());
                dto.setContactName(r.getReceiverName());
                dto.setContactAvatar(contentServerService.parserUri(r.getReceiverAvatar()));
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ListWorkReportsValResponse listSubmittedWorkReportsVal(ListWorkReportsValCommand cmd) {
        ListWorkReportsValResponse response = new ListWorkReportsValResponse();
        List<WorkReportValDTO> reportVals = new ArrayList<>();
        Integer nextPageOffset = null;
        Long applierId = UserContext.currentUserId();

        //  set the condition.
        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1);
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);
        List<Long> applierIds = Collections.singletonList(applierId);

        //  calculate the pageOffset.
        Integer pageOffset = (cmd.getPageOffset() - 1) * cmd.getPageSize();

        List<WorkReportVal> results = workReportValProvider.listWorkReportValsByApplierIds(
                UserContext.getCurrentNamespaceId(), pageOffset, cmd.getPageSize(), cmd.getOwnerId(), cmd.getOwnerType(), applierIds);
        if (results != null && results.size() > 0) {
            //  pageOffset.
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageOffset = cmd.getPageOffset() + 1;
            }

            //  DTO(receiverNames).
            results.forEach(r -> {
                WorkReportValDTO dto = new WorkReportValDTO();
                WorkReport report = workReportProvider.getWorkReportById(r.getReportId());
                dto.setReportId(r.getReportId());
                dto.setReportValId(r.getId());
                dto.setReportType(r.getReportType());
                if (report != null)
                    dto.setTitle(report.getReportName());
                dto.setReportTime(new Timestamp(r.getReportTime().getTime()));
                dto.setReportTimeText(workReportTimeService.displayReportTime(dto.getReportType(), dto.getReportTime().getTime()));
                dto.setUpdateTime(r.getUpdateTime());
                List<SceneContactDTO> receivers = listWorkReportValReceivers(r.getId());
                dto.setReceivers(receivers);
                dto.setReceiverNames(convertReceiversToNames(receivers));
                dto.setIconUrl(contentServerService.parserUri(r.getReceiverAvatar()));
                reportVals.add(dto);
            });
        }
        response.setNextPageOffset(nextPageOffset);
        response.setReportVals(reportVals);
        return response;
    }

    private String convertReceiversToNames(List<SceneContactDTO> receivers) {
        StringBuilder names = new StringBuilder();
        if (receivers != null && receivers.size() > 0) {
            for (SceneContactDTO dto : receivers) {
                names.append(dto.getContactName()).append(",");
            }
            names = new StringBuilder(names.substring(0, names.length() - 1));
        }
        return names.toString();
    }

    @Override
    public ListWorkReportsValResponse listReceivedWorkReportsVal(ListWorkReportsValCommand cmd) {
        ListWorkReportsValResponse response = new ListWorkReportsValResponse();
        List<WorkReportValDTO> reportVals = new ArrayList<>();
        User user = UserContext.current().getUser();
        Integer nextPageOffset = null;

        //  set the condition.
        if (cmd.getPageOffset() == null)
            cmd.setPageOffset(1);
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);

        //  calculate the pageOffset.
        Integer pageOffset = (cmd.getPageOffset() - 1) * cmd.getPageSize();
        List<WorkReportVal> results = workReportValProvider.listWorkReportValsByReceiverId(user.getNamespaceId(), pageOffset, user.getId(), cmd);

        if (results != null && results.size() > 0) {
            //  pageOffset.
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageOffset = cmd.getPageOffset() + 1;
            }

            //  DTO(applierName, readStatus).
            results.forEach(r -> {
                WorkReportValDTO dto = new WorkReportValDTO();
                WorkReport report = workReportProvider.getWorkReportById(r.getReportId());
                dto.setReportId(r.getReportId());
                dto.setReportValId(r.getId());
                dto.setReportType(r.getReportType());
                if (report != null)
                    dto.setTitle(report.getReportName());
                dto.setReportTime(new Timestamp(r.getReportTime().getTime()));
                dto.setReportTimeText(workReportTimeService.displayReportTime(dto.getReportType(), dto.getReportTime().getTime()));
                dto.setReadStatus(r.getReadStatus());
                dto.setApplierName(r.getApplierName());
                dto.setUpdateTime(r.getUpdateTime());
                dto.setIconUrl(contentServerService.parserUri(r.getApplierAvatar()));
                reportVals.add(dto);
            });
        }
        response.setNextPageOffset(nextPageOffset);
        response.setReportVals(reportVals);
        return response;
    }

    @Override
    public Integer countUnReadWorkReportsVal(WorkReportOrgIdCommand cmd) {
        User user = UserContext.current().getUser();
        return workReportValProvider.countUnReadWorkReportsVal(cmd.getNamespaceId(), cmd.getOrganizationId(), user.getId());
    }

    @Override
    public void markWorkReportsValReading(WorkReportOrgIdCommand cmd) {
        User user = UserContext.current().getUser();
        workReportValProvider.markWorkReportsValReading(cmd.getNamespaceId(), cmd.getOrganizationId(), user.getId());
    }

    @Override
    public WorkReportValDTO getWorkReportValDetail(WorkReportValIdCommand cmd) {
        //  1.update the workReportVal's status.
        //  2.return back the result.
        WorkReportValDTO dto = new WorkReportValDTO();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long currentUserId = UserContext.currentUserId();


        WorkReportVal reportVal = workReportValProvider.getValidWorkReportValById(cmd.getReportValId());
        //  check the reportVal
        if (reportVal == null)
            throw RuntimeErrorException.errorWith(WorkReportErrorCode.SCOPE, WorkReportErrorCode.ERROR_REPORT_VAL_NOT_FOUND,
                    "work report val has been deleted.");

        WorkReport report = workReportProvider.getWorkReportById(reportVal.getReportId());

        /* update the status */
        WorkReportValReceiverMap receiverMap = workReportValProvider.getWorkReportValReceiverByReceiverId(
                namespaceId, reportVal.getId(), currentUserId);
        //  check the receiver except applier.
        if (reportVal.getApplierUserId().longValue() != currentUserId.longValue()) {
            if (receiverMap == null)
                throw RuntimeErrorException.errorWith(WorkReportErrorCode.SCOPE, WorkReportErrorCode.ERROR_NO_READ_PERMISSIONS,
                        "no read permissions.");
            receiverMap.setReadStatus(WorkReportReadStatus.READ.getCode());
            workReportValProvider.updateWorkReportValReceiverMap(receiverMap);
        }

        /* get the result */
        //  1) get receivers.
        List<SceneContactDTO> receivers = listWorkReportValReceivers(cmd.getReportValId());
        //  2) get the field values which has been post by the user.
        GetGeneralFormValuesCommand valuesCommand = new GetGeneralFormValuesCommand();
        valuesCommand.setSourceId(reportVal.getId());
        valuesCommand.setSourceType(WORK_REPORT_VAL);
        List<PostApprovalFormItem> values = generalFormService.getGeneralFormValues(valuesCommand);
        List<GeneralFormFieldDTO> fields = values.stream().map(r ->
                ConvertHelper.convert(r, GeneralFormFieldDTO.class)
        ).collect(Collectors.toList());

        //  3) convert the result
        dto.setReportValId(reportVal.getId());
        dto.setReportId(reportVal.getReportId());
        dto.setReportType(reportVal.getReportType());
        dto.setReportTime(new Timestamp(reportVal.getReportTime().getTime()));
        dto.setReportTimeText(workReportTimeService.displayReportTime(dto.getReportType(), dto.getReportTime().getTime()));
        dto.setTitle(report.getReportName());
        dto.setApplierUserId(reportVal.getApplierUserId());
        dto.setApplierName(reportVal.getApplierName());
        dto.setApplierDetailId(getUserDetailId(reportVal.getApplierUserId(), reportVal.getOwnerId()));
        dto.setApplierUserAvatar(contentServerService.parserUri(getUserAvatar(reportVal.getApplierUserId())));
        dto.setCreateTime(reportVal.getCreateTime());
        dto.setReceivers(receivers);
        dto.setUpdateTime(reportVal.getUpdateTime());
        dto.setValues(fields);

        //  4) get the comment
        dto.setOwnerToken(populateOwnerToken(reportVal.getId()));
        return dto;
    }

    private String populateOwnerToken(Long reportValId) {
        OwnerTokenDTO ownerTokenDto = new OwnerTokenDTO();
        ownerTokenDto.setId(reportValId);
        ownerTokenDto.setType(OwnerType.WORK_REPORT.getCode());
        return WebTokenGenerator.getInstance().toWebToken(ownerTokenDto);
    }


    @Override
    public void syncWorkReportReceiver() {
        List<WorkReportValReceiverMap> receivers = workReportValProvider.listWorkReportReceivers();
        for (WorkReportValReceiverMap r : receivers) {
            if (r.getOrganizationId() != 0)
                continue;
            WorkReportVal val = workReportValProvider.getWorkReportValById(r.getReportValId());
            r.setOrganizationId(val.getOrganizationId());
            workReportValProvider.updateWorkReportValReceiverMap(r);
        }
    }

    @Override
    public void updateWorkReportReceiverAvatar() {
        List<WorkReportValReceiverMap> receivers = workReportValProvider.listWorkReportReceivers();
        for (WorkReportValReceiverMap r : receivers) {
            if (r.getReceiverAvatar() == null)
                continue;
            User user = userProvider.findUserById(r.getReceiverUserId());
            if (user == null)
                continue;
            r.setReceiverAvatar(user.getAvatar());
            workReportValProvider.updateWorkReportValReceiverMap(r);
        }
    }

    @Override
    public void updateWorkReportValAvatar() {
        List<WorkReportVal> vals = workReportValProvider.listWorkReportVals();
        for (WorkReportVal v : vals) {
            User author = userProvider.findUserById(v.getApplierUserId());
            if (author != null)
                v.setApplierAvatar(author.getAvatar());
            List<WorkReportValReceiverMap> res = workReportValProvider.listReportValReceiversByValId(v.getId());
            if (res != null && res.size() > 0)
                v.setReceiverAvatar(res.get(0).getReceiverAvatar());
            workReportValProvider.updateWorkReportVal(v);
        }
    }
}
