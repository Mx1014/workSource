package com.everhomes.enterpriseApproval;

import com.everhomes.archives.ArchivesService;
import com.everhomes.archives.ArchivesUtil;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.flow.FlowAutoStepTransferDTO;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseDetail;
import com.everhomes.flow.FlowCaseProcessorsResolver;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowSubject;
import com.everhomes.flow.FlowSubjectProvider;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalScopeMap;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.archives.ArchivesOperationalConfigurationDTO;
import com.everhomes.rest.archives.ArchivesParameter;
import com.everhomes.rest.enterpriseApproval.ApprovalFilterType;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdCommand;
import com.everhomes.rest.enterpriseApproval.ApprovalFlowIdsCommand;
import com.everhomes.rest.enterpriseApproval.CreateApprovalTemplatesCommand;
import com.everhomes.rest.enterpriseApproval.CreateEnterpriseApprovalCommand;
import com.everhomes.rest.enterpriseApproval.DeliverApprovalFlowCommand;
import com.everhomes.rest.enterpriseApproval.DeliverApprovalFlowsCommand;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalErrorCode;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalGroupAttribute;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalGroupDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalIdCommand;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalRecordDTO;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalTemplateCode;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsCommand;
import com.everhomes.rest.enterpriseApproval.ListApprovalFlowRecordsResponse;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsCommand;
import com.everhomes.rest.enterpriseApproval.ListEnterpriseApprovalsResponse;
import com.everhomes.rest.enterpriseApproval.UpdateEnterpriseApprovalCommand;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesCommand;
import com.everhomes.rest.enterpriseApproval.VerifyApprovalTemplatesResponse;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.UpdateFlowFormCommand;
import com.everhomes.rest.general_approval.CreateFormTemplatesCommand;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EnterpriseApprovalServiceImpl implements EnterpriseApprovalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalServiceImpl.class);

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private EnterpriseApprovalProvider enterpriseApprovalProvider;

    @Autowired
    private EnterpriseApprovalFlowModuleListener enterpriseApprovalFlowModuleListener;

    @Autowired
    private GeneralFormService generalFormService;

    @Autowired
    private GeneralApprovalService generalApprovalService;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private TaskService taskService;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private FlowEventLogProvider flowEventLogProvider;
    @Autowired
    private FlowSubjectProvider flowSubjectProvider;

    private DateTimeFormatter archivesFormatter = DateTimeFormatter.ofPattern("MM月dd日");

    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {
        ListApprovalFlowRecordsResponse res = new ListApprovalFlowRecordsResponse();
        List<EnterpriseApprovalRecordDTO> results = new ArrayList<>();
        ListingLocator locator = new ListingLocator();
        Integer namespaceId = cmd.getNamespaceId();
        SearchFlowCaseCommand command = new SearchFlowCaseCommand();
        command.setStartTime(cmd.getStartTime());
        command.setEndTime(cmd.getEndTime());
        command.setOrganizationId(cmd.getOrganizationId());
        command.setModuleId(cmd.getModuleId());
        command.setFlowCaseSearchType(FlowCaseSearchType.ADMIN.getCode());
        command.setNamespaceId(cmd.getNamespaceId());
        //  审批状态
        if (cmd.getApprovalStatus() != null)
            command.setFlowCaseStatus(cmd.getApprovalStatus());
        if (cmd.getPageAnchor() != null)
            command.setPageAnchor(cmd.getPageAnchor());

        int count = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        command.setPageSize(count);

        List<FlowCaseDetail> details = flowCaseProvider.findAdminFlowCases(locator, count, command, (locator1, query) -> {
            //  审批类型
            if (cmd.getFilter() != null) {
                if (ApprovalFilterType.APPROVAL.getCode().equals(cmd.getFilter().getSourceType()))
                    query.addConditions(Tables.EH_FLOW_CASES.REFER_ID.eq(cmd.getFilter().getSourceId()));
                else
                    query.addConditions(Tables.EH_FLOW_CASES.REFER_ID.in(enterpriseApprovalProvider.listGeneralApprovalIdsByGroupId(namespaceId, cmd.getModuleId(), cmd.getOrganizationId(), "EhOrganizations", cmd.getFilter().getSourceId())));
            }
            //  申请人姓名
            if (cmd.getCreatorName() != null)
                query.addConditions(Tables.EH_FLOW_CASES.APPLIER_NAME.eq(cmd.getCreatorName()));
            //  审批编号
            if (cmd.getApprovalNo() != null)
                query.addConditions(EnterpriseApprovalFlowCaseCustomField.APPROVAL_NO.getField().eq(cmd.getApprovalNo()));
            //  申请人部门
            if (cmd.getCreatorDepartmentId() != null)
                query.addConditions(EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.getField().eq(cmd.getCreatorDepartmentId()));
            //  activeFlag
            if (TrueOrFalseFlag.fromCode(cmd.getActiveFlag()) == TrueOrFalseFlag.TRUE)
                query.addConditions(Tables.EH_FLOW_CASES.STATUS.notIn(FlowCaseStatus.ABSORTED.getCode(), FlowCaseStatus.FINISHED.getCode()));
            return query;
        });

        if (details != null && details.size() > 0) {
            results = details.stream().map(this::convertEnterpriseApprovalRecordDTO).collect(Collectors.toList());
        }
        res.setRecords(results);
        res.setNextPageAnchor(locator.getAnchor());
        return res;
    }

    @Override
    public EnterpriseApprovalRecordDTO convertEnterpriseApprovalRecordDTO(FlowCase r) {
        EnterpriseApprovalFlowCase flowCase = ConvertHelper.convert(r, EnterpriseApprovalFlowCase.class);
        EnterpriseApprovalRecordDTO dto = new EnterpriseApprovalRecordDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        dto.setId(r.getId());
        dto.setNamespaceId(r.getNamespaceId());
        dto.setModuleId(r.getModuleId());
        dto.setCreatorName(r.getApplierName());
        dto.setCreatorDepartment(flowCase.getCreatorDepartment());
        dto.setCreatorDepartmentId(flowCase.getCreatorDepartmentId());
        dto.setCreatorMobile(r.getApplierPhone());
        dto.setCreateTime(formatter.format(r.getCreateTime().toLocalDateTime()));
        dto.setApprovalType(r.getTitle());
        dto.setApprovalNo(flowCase.getApprovalNo());
        dto.setApprovalStatus(r.getStatus());
        dto.setFlowCaseId(flowCase.getId());
        dto.setApprovalId(r.getReferId());
        dto.setCurrentLane(r.getCurrentLane());
        return dto;
    }

    @Override
    public List<EnterpriseApprovalGroupDTO> listEnterpriseApprovalGroups() {
        List<EnterpriseApprovalGroupDTO> results = new ArrayList<>();
        List<EnterpriseApprovalGroup> groups = enterpriseApprovalProvider.listEnterpriseApprovalGroups();
        if (groups == null || groups.size() == 0)
            return results;
        results = groups.stream()
                .map(r -> ConvertHelper.convert(r, EnterpriseApprovalGroupDTO.class))
                .collect(Collectors.toList());
        return results;
    }

    @Override
    public List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups() {
        List<EnterpriseApprovalGroupDTO> results = listEnterpriseApprovalGroups();
        if (results != null && results.size() > 0)
            results = results.stream()
                    .filter(r -> r.getGroupAttribute().equals(EnterpriseApprovalGroupAttribute.CUSTOMIZE.getCode()))
                    .collect(Collectors.toList());
        return results;
    }

    @Override
    public void exportApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {
        //  export with te file download center
        Map<String, Object> params = new HashMap<>();

        //  the value could be null if it is not exist.
        params.put("organizationId", cmd.getOrganizationId());
        params.put("moduleId", cmd.getModuleId());
        params.put("startTime", cmd.getStartTime());
        params.put("endTime", cmd.getEndTime());
        params.put("approvalStatus", cmd.getApprovalStatus());
        params.put("filter", StringHelper.toJsonString(cmd.getFilter()));
        params.put("creatorDepartmentId", cmd.getCreatorDepartmentId());
        params.put("creatorName", cmd.getCreatorName());
        params.put("approvalNo", cmd.getApprovalNo());
        params.put("namespaceId", UserContext.getCurrentNamespaceId());
        String fileName = String.format("审批记录_%s.xlsx", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), EnterpriseApprovalExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());

    }

    @Override
    public OutputStream getEnterpriseApprovalOutputStream(ListApprovalFlowRecordsCommand cmd, Long taskId) {
        ListApprovalFlowRecordsResponse response = listApprovalFlowRecords(cmd);
        taskService.updateTaskProcess(taskId, 10);
        //  1. Set the main title of the sheet
        String mainTitle = "审批记录";
        //  2. Set the subtitle of the sheet
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String subTitle = "申请时间:" + formatter.format(new Timestamp(cmd.getStartTime()).toLocalDateTime()) + " ~ " + formatter.format(new Timestamp(cmd.getEndTime()).toLocalDateTime());
        //  3. Set the title of the approval lists
        List<String> titles = Arrays.asList("审批编号", "审批类型", "申请时间", "申请人", "申请人部门", "表单内容",
                "审批状态", "审批记录", "当前审批人", "督办人");
        //  4. Start to write the excel
        LOGGER.debug("generalApproval++++++++++++++++++++++++++++++: {}", response.getRecords().size());
        XSSFWorkbook workbook = createApprovalRecordsBook(mainTitle, subTitle, titles, response.getRecords(), taskId);
        return writeExcel(workbook);
    }

    private ByteArrayOutputStream writeExcel(XSSFWorkbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }
        return out;
    }

    private XSSFWorkbook createApprovalRecordsBook(
            String mainTitle, String subTitle, List<String> titles, List<EnterpriseApprovalRecordDTO> data, Long taskId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("审批记录");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
        //  1. Write titles
        createGeneralApprovalRecordsFileTitle(workbook, sheet, mainTitle, subTitle, titles);
        taskService.updateTaskProcess(taskId, 30);
        //  2. Write data
        if (data != null && data.size() > 0) {
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row dataRow = sheet.createRow(rowIndex + 3);
                createGeneralApprovalRecordsFileData(workbook, dataRow, data.get(rowIndex));
            }
            taskService.updateTaskProcess(taskId, 95);
        }
        return workbook;
    }

    private void createGeneralApprovalRecordsFileTitle(XSSFWorkbook workbook, Sheet sheet, String mainTitle, String subTitle, List<String> list) {

        //  1-1.Set the style(center)
        Row mainTitleRow = sheet.createRow(0);
        XSSFCellStyle mainTitleStyle = workbook.createCellStyle();
        mainTitleStyle.setAlignment(HorizontalAlignment.CENTER);

        //  1-2.Set the value of the main title
        Cell mainTitleCell = mainTitleRow.createCell(0);
        mainTitleCell.setCellStyle(mainTitleStyle);
        mainTitleCell.setCellValue(mainTitle);

        //  2-1.Set the style(center)
        Row subTitleRow = sheet.createRow(1);
        XSSFCellStyle subTitleStyle = workbook.createCellStyle();
        subTitleStyle.setAlignment(HorizontalAlignment.CENTER);

        //  2-2.Set the value of the subtitle
        Cell subTitleCell = subTitleRow.createCell(0);
        subTitleCell.setCellStyle(subTitleStyle);
        subTitleCell.setCellValue(subTitle);

        //  3.Set the title of the approval lists
        Row titleRow = sheet.createRow(2);
        for (int i = 0; i < list.size(); i++) {
            sheet.setColumnWidth(i, 18 * 256);
            if (i == 5 || i == 7)
                sheet.setColumnWidth(i, 30 * 256);
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(list.get(i));
        }
    }

    private void createGeneralApprovalRecordsFileData(XSSFWorkbook workbook, Row dataRow, EnterpriseApprovalRecordDTO data) {

        XSSFCellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);

        //  1. basic data from flowCases
        Cell approvalNoCell = dataRow.createCell(0);
        approvalNoCell.setCellValue(data.getApprovalNo() != null ? data.getApprovalNo().toString() : "");
        dataRow.createCell(1).setCellValue(data.getApprovalType());
        dataRow.createCell(2).setCellValue(data.getCreateTime());
        dataRow.createCell(3).setCellValue(data.getCreatorName());
        dataRow.createCell(4).setCellValue(data.getCreatorDepartment());

        //  2. data from form
        List<FlowCaseEntity> entityLists = getApprovalDetails(data.getFlowCaseId());
        if (!CollectionUtils.isEmpty(entityLists)) {
            StringBuilder formLogs = new StringBuilder();
            for (int i = 0; i < entityLists.size(); i++) {
                formLogs.append(entityLists.get(i).getKey()).append(" : ").append(entityLists.get(i).getValue()).append("\n");
            }
            Cell formCell = dataRow.createCell(5);
            formCell.setCellStyle(wrapStyle);
            formCell.setCellValue(formLogs.toString());
        }

        //  3. approval status
        if (FlowCaseStatus.PROCESS.getCode().equals(data.getApprovalStatus()))
            dataRow.createCell(6).setCellValue("处理中");
        else if (FlowCaseStatus.FINISHED.getCode().equals(data.getApprovalStatus()))
            dataRow.createCell(6).setCellValue("已完成");
        else
            dataRow.createCell(6).setCellValue("已取消");

        //  4. the operator logs of the approval
        List<FlowEventLog> operateLogLists = searchFlowOperateLogs(data.getFlowCaseId());
        if (operateLogLists != null && operateLogLists.size() > 0) {
            StringBuilder operateLogs = new StringBuilder();
            for (FlowEventLog operateLog : operateLogLists) {
                if (StringUtils.hasText(operateLog.getFlowUserName())) {
                    operateLogs.append(operateLog.getFlowUserName()).append(" ");
                }
                operateLogs.append(operateLog.getLogContent()).append("\n");
            }
            Cell logCell = dataRow.createCell(7);
            logCell.setCellStyle(wrapStyle);
            logCell.setCellValue(operateLogs.toString());
        }

        //  5. the current operator
        FlowCaseProcessorsResolver processorRes = flowService.getCurrentProcessors(data.getFlowCaseId(), true);
        if (processorRes.getProcessorsInfoList() != null && processorRes.getProcessorsInfoList().size() > 0) {
            StringBuilder processors = new StringBuilder();
            for (int i = 0; i < processorRes.getProcessorsInfoList().size(); i++) {
                processors.append(processorRes.getProcessorsInfoList().get(i).getNickName()).append(",");
            }
            if (!"".equals(processors.toString()))
                processors = new StringBuilder(processors.substring(0, processors.length() - 1));
            dataRow.createCell(8).setCellValue(processors.toString());
        }

        //  6. the current supervisor
        FlowCase flowCase = flowService.getFlowCaseById(data.getFlowCaseId());
        List<UserInfo> supervisorLists = flowService.getSupervisor(flowCase);
        if (supervisorLists != null && supervisorLists.size() > 0) {
            StringBuilder supervisors = new StringBuilder();
            for (UserInfo supervisor : supervisorLists) {
                supervisors.append(supervisor.getNickName()).append(",");
            }
            if (!"".equals(supervisors.toString()))
                supervisors = new StringBuilder(supervisors.substring(0, supervisors.length() - 1));
            dataRow.createCell(9).setCellValue(supervisors.toString());
        }
    }

    private List<FlowCaseEntity> getApprovalDetails(Long flowCaseId) {
        return enterpriseApprovalFlowModuleListener.getApprovalDetails(flowCaseId);
    }

    private List<FlowEventLog> searchFlowOperateLogs(Long flowCaseId) {
        List<FlowEventLog> logs = flowEventLogProvider.queryFlowEventLogs(new ListingLocator(), Integer.MAX_VALUE - 1, ((locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCaseId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_CONTENT.isNotNull());
            query.addOrderBy(Tables.EH_FLOW_EVENT_LOGS.CREATE_TIME.asc());
            return query;
        }));
        if (CollectionUtils.isEmpty(logs)) {
            return new ArrayList<>();
        }
        Set<Long> subjectIds = new HashSet<>();
        for (FlowEventLog log : logs) {
            if (log.getSubjectId() != null && log.getSubjectId() > 0) {
                subjectIds.add(log.getSubjectId());
            }
        }
        if(CollectionUtils.isEmpty(subjectIds)){
            return logs;
        }
        List<FlowSubject> subjects = flowSubjectProvider.queryFlowSubjects(new ListingLocator(), Integer.MAX_VALUE - 1, ((locator, query) -> {
            query.addConditions(Tables.EH_FLOW_SUBJECTS.ID.in(subjectIds));
            return query;
        }));
        if(CollectionUtils.isEmpty(subjects)){
            return logs;
        }
        Map<Long, String> subjectMap = new HashMap<>();
        for (FlowSubject subject : subjects) {
            subjectMap.put(subject.getId(), subject.getContent());
        }
        Set<Long> setedList = new HashSet<>();
        for (FlowEventLog log : logs) {
            if (log.getSubjectId() != null && log.getSubjectId() > 0 && subjectMap.containsKey(log.getSubjectId()) && setedList.add(log.getSubjectId())) {
                log.setLogContent(log.getLogContent() + "\n" + subjectMap.get(log.getSubjectId()));
            }
        }
        return logs;
    }

    @Override
    public ListApprovalFlowRecordsResponse listActiveApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {
        cmd.setActiveFlag(TrueOrFalseFlag.TRUE.getCode());
        return listApprovalFlowRecords(cmd);
    }

    @Override
    public void stopApprovalFlows(ApprovalFlowIdsCommand cmd) {
        if (cmd.getFlowCaseIds() == null || cmd.getFlowCaseIds().size() == 0)
            return;
        Long userId = UserContext.currentUserId();
        for (Long flowCaseId : cmd.getFlowCaseIds()) {
            FlowCase flowCase = flowService.getFlowCaseById(flowCaseId);
            if (flowCase == null)
                continue;
            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            stepDTO.setFlowCaseId(flowCase.getId());
            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            stepDTO.setStepCount(flowCase.getStepCount());
            stepDTO.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
            stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
            stepDTO.setOperatorId(userId);
            flowService.processAutoStep(stepDTO);
        }
    }

    @Override
    public void deleteApprovalFlow(ApprovalFlowIdCommand cmd) {
        FlowCase flowCase = flowService.getFlowCaseById(cmd.getFlowCaseId());
        if (flowCase == null || TrueOrFalseFlag.TRUE == TrueOrFalseFlag.fromCode(flowCase.getDeleteFlag())) {
            return;
        }
        flowCase.setDeleteFlag(TrueOrFalseFlag.TRUE.getCode());
        flowCaseProvider.updateFlowCase(flowCase);

        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(flowCase.getReferId());
        if (ga == null) {
            return;
        }

        GeneralApprovalAttribute attribute = GeneralApprovalAttribute.fromCode(ga.getApprovalAttribute());
        if (GeneralApprovalAttribute.CUSTOMIZE == attribute || GeneralApprovalAttribute.DEFAULT == attribute) {
            // 自定义类型没有业务数据需要删除
            return;
        }
        EnterpriseApprovalHandler handler = EnterpriseApprovalHandlerUtils.getEnterpriseApprovalHandler(ga);
        if (handler != null) {
            handler.onFlowCaseDeleted(flowCase);
        }
    }

    @Override
    public List<OrganizationMemberDTO> listApprovalProcessors(ApprovalFlowIdCommand cmd) {
        List<OrganizationMemberDTO> results = new ArrayList<>();
        FlowCaseProcessorsResolver processorRes = flowService.getCurrentProcessors(cmd.getFlowCaseId(), true);
        if (processorRes.getProcessorsInfoList() != null && processorRes.getProcessorsInfoList().size() > 0) {
            results = processorRes.getProcessorsInfoList().stream().map(r -> {
                OrganizationMemberDTO dto = new OrganizationMemberDTO();
                dto.setTargetId(r.getId());
                dto.setContactName(r.getNickName());
                return dto;
            }).collect(Collectors.toList());
        }
        return results;
    }

    @Override
    public void deliverApprovalFlow(DeliverApprovalFlowCommand cmd) {
        if (cmd.getInnerIds() == null || cmd.getInnerIds().size() == 0 || cmd.getOuterIds() == null || cmd.getOuterIds().size() == 0)
            return;
        FlowCase flowCase = flowService.getFlowCaseById(cmd.getFlowCaseId());
        if (flowCase == null)
            return;
        FlowAutoStepTransferDTO stepDTO = new FlowAutoStepTransferDTO();
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
        stepDTO.setStepCount(flowCase.getStepCount());
        stepDTO.setAutoStepType(FlowStepType.TRANSFER_STEP.getCode());
        stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
        List<FlowEntitySel> transferIn = cmd.getInnerIds().stream().map(r -> new FlowEntitySel(r, FlowEntityType.FLOW_USER.getCode())).collect(Collectors.toList());
        List<FlowEntitySel> transferOut = cmd.getOuterIds().stream().map(r -> new FlowEntitySel(r, FlowEntityType.FLOW_USER.getCode())).collect(Collectors.toList());
        stepDTO.setTransferIn(transferIn);
        stepDTO.setTransferOut(transferOut);
        flowService.processAutoStep(stepDTO);
    }

    @Override
    public void deliverApprovalFlows(DeliverApprovalFlowsCommand cmd) {
        if (cmd.getInnerIds() == null || cmd.getInnerIds().size() == 0)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_NO_OUTERS, "no outers.");
        List<FlowEntitySel> transferIn = cmd.getInnerIds().stream().map(r -> new FlowEntitySel(r, FlowEntityType.FLOW_USER.getCode())).collect(Collectors.toList());
        for (Long flowCaseId : cmd.getFlowCaseIds()) {
            FlowCase flowCase = flowService.getFlowCaseById(flowCaseId);
            if (flowCase == null) {
                LOGGER.error("Can not find the flowCase which should be transferred");
                continue;
            }
            FlowAutoStepTransferDTO stepDTO = new FlowAutoStepTransferDTO();
            stepDTO.setFlowCaseId(flowCase.getId());
            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            stepDTO.setStepCount(flowCase.getStepCount());
            stepDTO.setAutoStepType(FlowStepType.TRANSFER_STEP.getCode());
            stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
            List<OrganizationMemberDTO> processors = listApprovalProcessors(new ApprovalFlowIdCommand(flowCaseId));
            if (processors == null || processors.size() == 0) {
                LOGGER.error("Can not find old processors");
                continue;
            }
            stepDTO.setTransferIn(transferIn);
            stepDTO.setTransferOut(processors.stream().map(r -> new FlowEntitySel(r.getTargetId(), FlowEntityType.FLOW_USER.getCode())).collect(Collectors.toList()));
            flowService.processAutoStep(stepDTO);
        }
    }

    //  Whether the approval template has already been existed, check it.
    @Override
    public VerifyApprovalTemplatesResponse verifyApprovalTemplates(VerifyApprovalTemplatesCommand cmd) {
        VerifyApprovalTemplatesResponse response = new VerifyApprovalTemplatesResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        response.setResult(TrueOrFalseFlag.FALSE.getCode());
        List<EnterpriseApprovalTemplate> templates = enterpriseApprovalProvider.listEnterpriseApprovalTemplateByModuleId(cmd.getModuleId(), true);
        if (templates.size() == 0)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_APPROVAL_TEMPLATE_NOT_EXIST, "" +
                    "Approval templates not exist. Please check it.");
        Integer counts = enterpriseApprovalProvider.countGeneralApprovalInTemplateIds(namespaceId, cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), templates.stream().map(EnterpriseApprovalTemplate::getId).collect(Collectors.toList()));
        if (counts != templates.size())
            response.setResult(TrueOrFalseFlag.TRUE.getCode());
        return response;
    }

    //  Create enterprise approval templates.
    @Override
    public void createApprovalTemplates(CreateApprovalTemplatesCommand cmd) {
        List<EnterpriseApprovalTemplate> templates = enterpriseApprovalProvider.listEnterpriseApprovalTemplateByModuleId(cmd.getModuleId(), false);
        //  1.判断审批模板中是否有对应的表单模板
        //  2.没有则直接创建审批
        //  3.有则先创建关联表单的工作流
        if (templates != null && templates.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                for (EnterpriseApprovalTemplate template : templates) {
                    if (template.getFormTemplateId() == 0) {
                        //  Create Approvals directly.
                        createGeneralApprovalByTemplate(template, null, cmd);
                    } else {
                        //  Create the form before creating the approval.
                        GeneralFormDTO form = generalFormService.createGeneralFormByTemplate(template.getFormTemplateId(), ConvertHelper.convert(cmd, CreateFormTemplatesCommand.class));
                        //  Then, create the approval.
                        createGeneralApprovalByTemplate(template, form, cmd);
                    }
                }
                return null;
            });
        }
    }

    private void createGeneralApprovalByTemplate(EnterpriseApprovalTemplate template, GeneralFormDTO form, CreateApprovalTemplatesCommand cmd) {
        //  check whether the template has been existed by the template id
        GeneralApproval ga = enterpriseApprovalProvider.getGeneralApprovalByTemplateId(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), template.getId());
        //  1. just update it when it exist
        if (ga != null) {
            ga = processApprovalTemplate(ga, template, form, cmd);
            generalApprovalProvider.updateGeneralApproval(ga);
        } else {
            //  2-1. create it if not exist
            ga = ConvertHelper.convert(template, GeneralApproval.class);
            ga = processApprovalTemplate(ga, template, form, cmd);
            generalApprovalProvider.createGeneralApproval(ga);
            //  2-2. create the flow.
            createGeneralApprovalFlow(ga, form);
        }

        //  3. initialize the approval scope map
        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
        if (org != null) {
            GeneralApprovalScopeMapDTO dto = new GeneralApprovalScopeMapDTO();
            dto.setSourceId(cmd.getOwnerId());
            dto.setSourceType(UniongroupTargetType.ORGANIZATION.getCode());
            dto.setSourceDescription(org.getName());
            updateGeneralApprovalScope(ga.getNamespaceId(), ga.getId(), Collections.singletonList(dto));
        }
    }

    private GeneralApproval processApprovalTemplate(GeneralApproval ga, EnterpriseApprovalTemplate template, GeneralFormDTO form, CreateApprovalTemplatesCommand cmd) {
        Long userId = UserContext.currentUserId();
        ga.setNamespaceId(UserContext.getCurrentNamespaceId());
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        ga.setOwnerId(cmd.getOwnerId());
        ga.setOwnerType(cmd.getOwnerType());
        ga.setOrganizationId(cmd.getOrganizationId());
        ga.setProjectId(cmd.getProjectId());
        ga.setProjectType(cmd.getProjectType());
        //  Group initialize
        EnterpriseApprovalAdditionFieldDTO fieldDTO = new EnterpriseApprovalAdditionFieldDTO();
        fieldDTO.setApprovalGroupId(template.getGroupId());
        BeanUtils.copyProperties(fieldDTO, ga);
        //  Template id insert
        ga.setApprovalTemplateId(template.getId());
        if (form != null) {
            ga.setFormOriginId(form.getFormOriginId());
            ga.setFormVersion(form.getFormVersion());
        }
        ga.setSupportType(cmd.getSupportType());
        ga.setIconUri(template.getIconUri());
        ga.setOperatorUid(userId);
        ga.setOperatorName(getUserRealName(userId, ga.getOrganizationId()));
        return ga;
    }

    private void createGeneralApprovalFlow(GeneralApproval ga, GeneralFormDTO form) {
        //  1.create the flow
        CreateFlowCommand flowCmd = new CreateFlowCommand();
        flowCmd.setNamespaceId(ga.getNamespaceId());
        flowCmd.setFlowName(ga.getApprovalName());
        flowCmd.setOrgId(ga.getOrganizationId());
        flowCmd.setModuleId(ga.getModuleId());
        flowCmd.setOwnerId(ga.getId());
        flowCmd.setOwnerType("GENERAL_APPROVAL");
        FlowDTO flowDTO = flowService.createFlow(flowCmd);
        //  2.correlate the form
        UpdateFlowFormCommand flowFormCmd = new UpdateFlowFormCommand();
        flowFormCmd.setEntityId(flowDTO.getId());
        flowFormCmd.setEntityType(FlowEntityType.FLOW.getCode());
        flowFormCmd.setFormOriginId(form.getFormOriginId());
        flowFormCmd.setFormVersion(form.getFormVersion());
        flowService.createFlowForm(flowFormCmd);
    }

    private String getUserRealName(Long userId, Long ownerId) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, ownerId);
        if (member != null)
            return member.getContactName();
        return null;
    }

    @Override
    public EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        EnterpriseApprovalGroup group = enterpriseApprovalProvider.findEnterpriseApprovalGroup(cmd.getApprovalGroupId());
        if (group == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_APPROVAL_GROUP_NOT_EXIST,
                    "The approval group not exist.");
        GeneralApproval oldApproval = enterpriseApprovalProvider.findEnterpriseApprovalByName(namespaceId, cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getApprovalName(), cmd.getApprovalGroupId());
        if (oldApproval != null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_DUPLICATE_NAME,
                    "Duplicate approval name.");

        GeneralApproval ga = ConvertHelper.convert(cmd, GeneralApproval.class);
        EnterpriseApprovalAdditionFieldDTO fieldDTO = new EnterpriseApprovalAdditionFieldDTO();
        fieldDTO.setApprovalGroupId(cmd.getApprovalGroupId());
        BeanUtils.copyProperties(fieldDTO, ga);
        ga.setNamespaceId(namespaceId);
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        ga.setOperatorUid(userId);
        ga.setOperatorName(getUserRealName(userId, ga.getOwnerId()));
        //  set a default icon
        if (group.getApprovalIcon() != null)
            ga.setIconUri(group.getApprovalIcon());
        dbProvider.execute((TransactionStatus status) -> {
            //  create it into the database
            Long approvalId = generalApprovalProvider.createGeneralApproval(ga);
            //  update the scope
            updateGeneralApprovalScope(ga.getNamespaceId(), approvalId, cmd.getScopes());
            return null;
        });

        EnterpriseApprovalDTO dto = new EnterpriseApprovalDTO();
        dto.setId(ga.getId());
        dto.setApprovalName(ga.getApprovalName());
        return dto;
    }

    @Override
    public void deleteEnterpriseApproval(EnterpriseApprovalIdCommand cmd) {
        // change the status
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        ga.setStatus(GeneralApprovalStatus.DELETED.getCode());
        dbProvider.execute((TransactionStatus status) -> {
            //  1.delete the approval
            updateGeneralApproval(ga);
            //  2.delete the scope
            generalApprovalProvider.deleteApprovalScopeMapByApprovalId(cmd.getApprovalId());
            return null;
        });
    }

    @Override
    public EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd) {
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        if (ga == null)
            return null;
        EnterpriseApprovalGroup group = enterpriseApprovalProvider.findEnterpriseApprovalGroup(cmd.getApprovalGroupId());
        if (group == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_APPROVAL_GROUP_NOT_EXIST,
                    "The approval group not exist.");
        //  duplicate name checked
        GeneralApproval oldApproval = enterpriseApprovalProvider.findEnterpriseApprovalByName(ga.getNamespaceId(), ga.getModuleId(), ga.getOwnerId(), ga.getOwnerType(), cmd.getApprovalName(), cmd.getApprovalGroupId());
        if (oldApproval != null)
            if (oldApproval.getId().longValue() != ga.getId().longValue())
                throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_DUPLICATE_NAME,
                        "Duplicate approval name.");
        EnterpriseApprovalAdditionFieldDTO fieldDTO = new EnterpriseApprovalAdditionFieldDTO();
        fieldDTO.setApprovalGroupId(cmd.getApprovalGroupId());
        BeanUtils.copyProperties(fieldDTO, ga);
        ga.setApprovalName(cmd.getApprovalName());
        ga.setApprovalRemark(cmd.getApprovalRemark());
        if (group.getApprovalIcon() != null)
            ga.setIconUri(group.getApprovalIcon());

        dbProvider.execute((TransactionStatus status) -> {
            //  1.update the approval
            updateGeneralApproval(ga);
            //  2.update the scope
            updateGeneralApprovalScope(ga.getNamespaceId(), ga.getId(), cmd.getScopes());
            return null;
        });

        EnterpriseApprovalDTO dto = new EnterpriseApprovalDTO();
        dto.setId(ga.getId());
        dto.setApprovalName(ga.getApprovalName());
        return dto;
    }

    private void updateGeneralApproval(GeneralApproval ga) {
        Long userId = UserContext.currentUserId();
        ga.setOperatorUid(userId);
        ga.setOperatorName(getUserRealName(userId, ga.getOrganizationId()));
        generalApprovalProvider.updateGeneralApproval(ga);
    }

    @Override
    public void updateGeneralApprovalScope(Integer namespaceId, Long approvalId, List<GeneralApprovalScopeMapDTO> dtos) {
        //  1.set the temporary array to save ids
        List<Long> detailIds = new ArrayList<>();
        List<Long> organizationIds = new ArrayList<>();

        if (dtos == null || dtos.size() == 0)
            return;

        for (GeneralApprovalScopeMapDTO dto : dtos) {
            GeneralApprovalScopeMap scope = generalApprovalProvider.findGeneralApprovalScopeMap(namespaceId, approvalId,
                    dto.getSourceId(), dto.getSourceType());

            //  2.save ids under the sourceType condition
            if (dto.getSourceType().equals(UniongroupTargetType.ORGANIZATION.getCode()))
                organizationIds.add(dto.getSourceId());
            else if (dto.getSourceType().equals(UniongroupTargetType.MEMBERDETAIL.getCode()))
                detailIds.add(dto.getSourceId());

            //  3.create or update the scope
            if (scope != null) {
                scope.setSourceDescription(dto.getSourceDescription());
                generalApprovalProvider.updateGeneralApprovalScopeMap(scope);
            } else {
                scope = new GeneralApprovalScopeMap();
                scope.setApprovalId(approvalId);
                scope.setNamespaceId(namespaceId);
                scope.setSourceId(dto.getSourceId());
                scope.setSourceType(dto.getSourceType());
                scope.setSourceDescription(dto.getSourceDescription());
                generalApprovalProvider.createGeneralApprovalScopeMap(scope);
            }
        }

        //  4.delete the scope which is not in the array
        if (detailIds.size() == 0)
            detailIds.add(0L);
        generalApprovalProvider.deleteOddGeneralApprovalScope(namespaceId, approvalId, UniongroupTargetType.MEMBERDETAIL.getCode(), detailIds);

        if (organizationIds.size() == 0)
            organizationIds.add(0L);
        generalApprovalProvider.deleteOddGeneralApprovalScope(namespaceId, approvalId, UniongroupTargetType.ORGANIZATION.getCode(), organizationIds);
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd) {
        ListEnterpriseApprovalsResponse res = new ListEnterpriseApprovalsResponse();
        List<EnterpriseApprovalGroupDTO> groups = listEnterpriseApprovalGroups();
        if (groups == null || groups.size() == 0)
            return res;
        List<GeneralApproval> results = queryEnterpriseApprovals(cmd);
        if (results == null || results.size() == 0)
            return res;
        List<EnterpriseApprovalDTO> approvals = results.stream().map(r -> {
            EnterpriseApproval approval = ConvertHelper.convert(r, EnterpriseApproval.class);
            EnterpriseApprovalDTO dto = new EnterpriseApprovalDTO();
            dto.setId(r.getId());
            dto.setApprovalName(r.getApprovalName());
            dto.setApprovalGroupId(approval.getApprovalGroupId());
            return dto;
        }).collect(Collectors.toList());
        for (EnterpriseApprovalGroupDTO group : groups) {
            group.setApprovals(getCorrespondingApproval(group, approvals));
        }
        res.setGroups(groups);
        return res;
    }

    @Override
    public void enableEnterpriseApproval(EnterpriseApprovalIdCommand cmd) {
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        Flow flow = flowService.getEnabledFlow(ga.getNamespaceId(), ga.getModuleId(), ga.getModuleType(), ga.getId(),
                FlowOwnerType.GENERAL_APPROVAL.getCode());
        if (flow == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_DISABLE_APPROVAL_FLOW,
                    "The approval flow is off !");
        ga.setStatus(GeneralApprovalStatus.RUNNING.getCode());
        updateGeneralApproval(ga);
    }

    @Override
    public void disableEnterpriseApproval(EnterpriseApprovalIdCommand cmd) {
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        updateGeneralApproval(ga);
    }

    //  find approvals, In order to be reused by other function
    private List<GeneralApproval> queryEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        return generalApprovalProvider.queryGeneralApprovals(new ListingLocator(), Integer.MAX_VALUE - 1, (locator, query) -> {
            query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd.getOwnerId()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd.getOwnerType()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(cmd.getModuleId()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(cmd.getModuleType()));
            if (null != cmd.getStatus())
                query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(cmd.getStatus()));
            return query;
        });
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        ListEnterpriseApprovalsResponse res = new ListEnterpriseApprovalsResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<EnterpriseApprovalGroupDTO> groups = listEnterpriseApprovalGroups();
        if (groups == null || groups.size() == 0)
            return res;
        List<GeneralApproval> results = queryEnterpriseApprovals(cmd);
        if (results == null || results.size() == 0)
            return res;
        //  convert the result to the EnterpriseApprovalDTO
        List<EnterpriseApprovalDTO> approvals = results.stream().map(r -> processEnterpriseApproval(namespaceId, r)).collect(Collectors.toList());
        for (EnterpriseApprovalGroupDTO group : groups) {
            group.setApprovals(getCorrespondingApproval(group, approvals));
        }
        res.setGroups(groups);
        return res;
    }

    @Override
    public GeneralFormReminderDTO checkArchivesApproval(Long userId, Long organizationId, Long approvalId, Byte operationType) {
        GeneralFormReminderDTO dto = new GeneralFormReminderDTO();

        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(approvalId);
        List<FlowCaseDetail> details = listActiveFlowCasesByApprovalId(userId, organizationId, approvalId);
        if (details != null && details.size() > 0) {
            dto.setFlag(TrueOrFalseFlag.TRUE.getCode());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("approvalName", ga.getApprovalName());
            dto.setTitle(localeTemplateService.getLocaleTemplateString(EnterpriseApprovalTemplateCode.SCOPE,
                    EnterpriseApprovalTemplateCode.APPROVAL_TITLE, "zh_CN", map, "Remind!"));
            dto.setContent(localeTemplateService.getLocaleTemplateString(EnterpriseApprovalTemplateCode.SCOPE,
                    EnterpriseApprovalTemplateCode.APPROVAL_CONTENT, "zh_CN", map, "Content!"));
            return dto;
        }

        ArchivesOperationalConfigurationDTO archives = archivesService.getArchivesOperationByUserId(userId, organizationId, operationType);
        if (archives != null) {
            dto.setFlag(TrueOrFalseFlag.TRUE.getCode());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("operationType", ArchivesUtil.resolveArchivesEnum(archives.getOperationType(),
                    ArchivesParameter.OPERATION_TYPE));
            dto.setTitle(localeTemplateService.getLocaleTemplateString(EnterpriseApprovalTemplateCode.SCOPE,
                    EnterpriseApprovalTemplateCode.ARCHIVES_TITLE, "zh_CN", map, "Remind!"));
            map.put("operationTime", archivesFormatter.format(archives.getOperationDate().toLocalDate()));
            dto.setContent(localeTemplateService.getLocaleTemplateString(EnterpriseApprovalTemplateCode.SCOPE,
                    EnterpriseApprovalTemplateCode.ARCHIVES_CONTENT, "zh_CN", map, "Content!"));
            return dto;
        }

        dto.setFlag(TrueOrFalseFlag.FALSE.getCode());
        return dto;
    }

    private EnterpriseApprovalDTO processEnterpriseApproval(Integer namespaceId, GeneralApproval r) {
        EnterpriseApproval ea = ConvertHelper.convert(r, EnterpriseApproval.class);
        EnterpriseApprovalDTO dto = ConvertHelper.convert(ea, EnterpriseApprovalDTO.class);
        dto.setScopes(generalApprovalService.listGeneralApprovalScopes(namespaceId, dto.getId()));
        //  approval icon
        if (dto.getIconUri() != null)
            dto.setIconUrl(contentServerService.parserUri(dto.getIconUri()));
        return dto;
    }

    //  get the corresponding approval by the group id
    private List<EnterpriseApprovalDTO> getCorrespondingApproval(EnterpriseApprovalGroupDTO group, List<EnterpriseApprovalDTO> list) {
        return list.stream().filter(r -> group.getId().equals(r.getApprovalGroupId())).collect(Collectors.toList());
    }

    @Override
    public ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        ListEnterpriseApprovalsResponse res = new ListEnterpriseApprovalsResponse();
        Long userId = UserContext.currentUserId();
        cmd.setStatus(GeneralApprovalStatus.RUNNING.getCode());
        if (null == cmd.getModuleType())
            cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
        if (null == cmd.getModuleId())
            cmd.setModuleId(EnterpriseApprovalController.MODULE_ID);
        List<EnterpriseApprovalGroupDTO> groups = listEnterpriseApprovals(cmd).getGroups();
        if (groups == null || groups.size() == 0)
            return res;

        //  get the user's info
        OrganizationMember member = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(userId, cmd.getOwnerId());
        if (member == null)
            member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, cmd.getOwnerId());
        //  check the approval by scope and filter it
        for (int i = groups.size() - 1; i >= 0; i--) {
            EnterpriseApprovalGroupDTO group = groups.get(i);
            filterTheApprovalByScope(group, member);
            if (group.getApprovals() == null || group.getApprovals().size() == 0)
                //  remove the group which approval list size is zero
                groups.remove(group);
        }
        res.setGroups(groups);
        return res;
    }

    //  filter function, take the address of the variable 'group' to assign a new value
    private void filterTheApprovalByScope(EnterpriseApprovalGroupDTO group, OrganizationMember member) {
        List<EnterpriseApprovalDTO> results = new ArrayList<>();
        if (group.getApprovals() == null || group.getApprovals().size() == 0)
            return;
        for (EnterpriseApprovalDTO approval : group.getApprovals()) {
            if (generalApprovalService.checkTheApprovalScope(approval.getScopes(), member))
                results.add(approval);
        }
        group.setApprovals(results);
    }

    @Override
    public List<FlowCaseDetail> listActiveFlowCasesByApprovalId(Long userId, Long ownerId, Long approvalId) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        SearchFlowCaseCommand command = new SearchFlowCaseCommand();
        command.setOrganizationId(ownerId);
        command.setFlowCaseSearchType(FlowCaseSearchType.ADMIN.getCode());
        command.setNamespaceId(namespaceId);
        command.setModuleId(EnterpriseApprovalController.MODULE_ID);
        command.setUserId(userId);
        List<FlowCaseDetail> details = flowCaseProvider.findAdminFlowCases(new ListingLocator(), Integer.MAX_VALUE - 1, command, (locator1, query) -> {
            query.addConditions(Tables.EH_FLOW_CASES.REFER_ID.eq(approvalId));
            query.addConditions(Tables.EH_FLOW_CASES.REFER_TYPE.eq("approval"));
            query.addConditions(Tables.EH_FLOW_CASES.STATUS.notIn(FlowCaseStatus.ABSORTED.getCode(), FlowCaseStatus.FINISHED.getCode()));
            return query;
        });

        if (details != null && details.size() > 0)
            return details;
        return null;
    }
}
