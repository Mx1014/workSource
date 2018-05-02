package com.everhomes.enterpriseApproval;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.flow.*;
import com.everhomes.general_approval.*;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.*;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EnterpriseApprovalServiceImpl implements EnterpriseApprovalService{

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalServiceImpl.class);

    @Autowired
    private EnterpriseApprovalProvider enterpriseApprovalProvider;

    @Autowired
    private GeneralApprovalFlowModuleListener generalApprovalFlowModuleListener;

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
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowRecords(ListApprovalFlowRecordsCommand cmd) {
        ListApprovalFlowRecordsResponse res = new ListApprovalFlowRecordsResponse();
        List<EnterpriseApprovalRecordDTO> results = new ArrayList<>();
        ListingLocator locator = new ListingLocator();
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
            if (cmd.getFilter() != null){
                if(ApprovalFilterType.APPROVAL.getCode().equals(cmd.getFilter()))
                    query.addConditions(Tables.EH_FLOW_CASES.REFER_ID.eq(cmd.getFilter().getSourceId()));
                else
                    //todo:获取审批组id
                    query.addConditions(Tables.EH_FLOW_CASES.REFER_ID.in(1L,2L));
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
        return dto;
    }

    @Override
    public ListApprovalFlowRecordsResponse listApprovalFlowMonitors(ListApprovalFlowRecordsCommand cmd) {
        return null;
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
        if (results != null && results.size() == 0)
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
        cmd.setPageAnchor(null);
        cmd.setPageSize(Integer.MAX_VALUE - 1);
        ListApprovalFlowRecordsResponse response = listApprovalFlowRecords(cmd);
        taskService.updateTaskProcess(taskId, 10);
        //  1. Set the main title of the sheet
        String mainTitle = "审批记录";
        //  2. Set the subtitle of the sheet
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String subTitle = "申请时间:" + formatter.format(new Timestamp(cmd.getStartTime()).toLocalDateTime()) + " ~ " + formatter.format(new Timestamp(cmd.getEndTime()).toLocalDateTime());
        //  3. Set the title of the approval lists
        List<String> titles = Arrays.asList("审批编号", "提交时间", "申请人", "申请人部门", "表单内容",
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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
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

        //  1.Set the style(center)
        Row mainTitleRow = sheet.createRow(0);
        XSSFCellStyle mainTitleStyle = workbook.createCellStyle();
        mainTitleStyle.setAlignment(HorizontalAlignment.CENTER);

        //  1.Set the value of the main title
        Cell mainTitleCell = mainTitleRow.createCell(0);
        mainTitleCell.setCellStyle(mainTitleStyle);
        mainTitleCell.setCellValue(mainTitle);

        //  2.Set the style(center)
        Row subTitleRow = sheet.createRow(1);
        XSSFCellStyle subTitleStyle = workbook.createCellStyle();
        subTitleStyle.setAlignment(HorizontalAlignment.CENTER);

        //  2.Set the value of the subtitle
        Cell subTitleCell = subTitleRow.createCell(0);
        subTitleCell.setCellStyle(subTitleStyle);
        subTitleCell.setCellValue(subTitle);

        //  3.Set the title of the approval lists
        Row titleRow = sheet.createRow(2);
        for (int i = 0; i < list.size(); i++) {
            sheet.setColumnWidth(i, 17 * 256);
            if (i == 4 || i == 6)
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
        dataRow.createCell(1).setCellValue(data.getCreateTime());
        dataRow.createCell(2).setCellValue(data.getCreatorName());
        dataRow.createCell(3).setCellValue(data.getCreatorDepartment());

        //  2. data from form
        List<FlowCaseEntity> entityLists = getApprovalDetails(data.getFlowCaseId());
        if (entityLists != null && entityLists.size() > 4) {
            StringBuilder formLogs = new StringBuilder();
            for (int i = 4; i < entityLists.size(); i++) {
                formLogs.append(entityLists.get(i).getKey()).append(" : ").append(entityLists.get(i).getValue()).append("\n");
            }
            Cell formCell = dataRow.createCell(4);
            formCell.setCellStyle(wrapStyle);
            formCell.setCellValue(formLogs.toString());
        }

        //  3. approval status
        if (FlowCaseStatus.PROCESS.getCode().equals(data.getApprovalStatus()))
            dataRow.createCell(5).setCellValue("处理中");
        else if (FlowCaseStatus.FINISHED.getCode().equals(data.getApprovalStatus()))
            dataRow.createCell(5).setCellValue("已完成");
        else
            dataRow.createCell(5).setCellValue("已取消");

        //  4. the operator logs of the approval
        SearchFlowOperateLogsCommand logsCommand = new SearchFlowOperateLogsCommand();
        logsCommand.setFlowCaseId(data.getFlowCaseId());
        logsCommand.setPageSize(100000);
        logsCommand.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
        List<FlowOperateLogDTO> operateLogLists = flowService.searchFlowOperateLogs(logsCommand).getLogs();
        if (operateLogLists != null && operateLogLists.size() > 0) {
            StringBuilder operateLogs = new StringBuilder();
            for (FlowOperateLogDTO operateLog : operateLogLists) {
                operateLogs.append(operateLog.getFlowUserName()).append(operateLog.getLogContent()).append("\n");
            }
            Cell logCell = dataRow.createCell(6);
            logCell.setCellStyle(wrapStyle);
            logCell.setCellValue(operateLogs.toString());
        }

        //  5. the current operator
        FlowCaseProcessorsProcessor processorRes = flowService.getCurrentProcessors(data.getFlowCaseId(), true);
        if (processorRes.getProcessorsInfoList() != null && processorRes.getProcessorsInfoList().size() > 0) {
            StringBuilder processors = new StringBuilder();
            for (int i = 0; i < processorRes.getProcessorsInfoList().size(); i++) {
                processors.append(processorRes.getProcessorsInfoList().get(i).getNickName()).append(",");
            }
            if (!"".equals(processors.toString()))
                processors = new StringBuilder(processors.substring(0, processors.length() - 1));
            dataRow.createCell(7).setCellValue(processors.toString());
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
            dataRow.createCell(8).setCellValue(supervisors.toString());
        }
    }

    public List<FlowCaseEntity> getApprovalDetails(Long flowCaseId) {
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        return generalApprovalFlowModuleListener.onFlowCaseDetailRender(flowCase, null);
    }

    //  判断是否需要创建模板
    @Override
    public VerifyApprovalTemplatesResponse verifyApprovalTemplates(VerifyApprovalTemplatesCommand cmd) {
        VerifyApprovalTemplatesResponse response = new VerifyApprovalTemplatesResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        response.setResult(TrueOrFalseFlag.TRUE.getCode());
        List<EnterpriseApprovalTemplate> templates = enterpriseApprovalProvider.listEnterpriseApprovalTemplateByModuleId(cmd.getModuleId());
        if (templates == null || templates.size() == 0)
            LOGGER.error("Approval templates not exist. Please check it.");
        GeneralApproval ga = enterpriseApprovalProvider.getGeneralApprovalByTemplateId(namespaceId, cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), templates.get(0).getId());
        if (ga == null)
            response.setResult(TrueOrFalseFlag.FALSE.getCode());
        return response;
    }

    //  创建审批模板的接口
    @Override
    public void createApprovalTemplates(CreateApprovalTemplatesCommand cmd) {
        List<EnterpriseApprovalTemplate> templates = enterpriseApprovalProvider.listEnterpriseApprovalTemplateByModuleId(cmd.getModuleId());
        //  1.判断审批模板中是否有对应的表单模板
        //  2.没有则直接创建审批
        //  3.有则先创建表单拿去表单 id,在创建审批与生成的 id 关联
        if (templates != null || templates.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                for (EnterpriseApprovalTemplate template : templates) {
                    if (template.getFormTemplateId() == 0) {
                        //  Create Approvals directly.
                        createGeneralApprovalByTemplate(template, null, cmd);
                    } else {
                        //  Create Forms before creating approvals.
                        Long formOriginId = generalFormService.createGeneralFormByTemplate(template.getFormTemplateId(), ConvertHelper.convert(cmd, CreateFormTemplatesCommand.class));
                        //  Then, start to create approvals.
                        createGeneralApprovalByTemplate(template, formOriginId, cmd);
                    }
                }
                return null;
            });
        }
    }

    private void createGeneralApprovalByTemplate(EnterpriseApprovalTemplate approval, Long formOriginId, CreateApprovalTemplatesCommand cmd) {
        GeneralApproval ga = enterpriseApprovalProvider.getGeneralApprovalByTemplateId(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), approval.getId());
        if (ga != null) {
            ga = convertApprovalFromTemplate(ga, approval, formOriginId, cmd);
            generalApprovalProvider.updateGeneralApproval(ga);
        } else {
            ga = ConvertHelper.convert(approval, GeneralApproval.class);
            ga = convertApprovalFromTemplate(ga, approval, formOriginId, cmd);
            generalApprovalProvider.createGeneralApproval(ga);
        }

        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
        if (org != null) {
            GeneralApprovalScopeMapDTO dto = new GeneralApprovalScopeMapDTO();
            dto.setSourceId(cmd.getOwnerId());
            dto.setSourceType(UniongroupTargetType.ORGANIZATION.getCode());
            dto.setSourceDescription(org.getName());
            generalApprovalService.updateGeneralApprovalScope(ga.getNamespaceId(), ga.getId(), Arrays.asList(dto));
        }
    }

    private GeneralApproval convertApprovalFromTemplate(GeneralApproval ga, EnterpriseApprovalTemplate approval, Long formOriginId, CreateApprovalTemplatesCommand cmd) {
        Long userId = UserContext.currentUserId();
        ga.setNamespaceId(UserContext.getCurrentNamespaceId());
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        ga.setOwnerId(cmd.getOwnerId());
        ga.setOwnerType(cmd.getOwnerType());
        ga.setOrganizationId(cmd.getOrganizationId());
        ga.setProjectId(cmd.getProjectId());
        ga.setProjectType(cmd.getProjectType());
        ga.setApprovalTemplateId(approval.getId());
        if (formOriginId != null)
            ga.setFormOriginId(formOriginId);
        ga.setSupportType(cmd.getSupportType());
        ga.setOperatorUid(userId);
        ga.setOperatorName(getUserRealName(userId, ga.getOrganizationId()));
        return ga;
    }

    private String getUserRealName(Long userId, Long ownerId) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, ownerId);
        if (member != null)
            return member.getContactName();
        //  若没有真实姓名则返回空
        return null;
    }

    @Override
    public EnterpriseApprovalDTO createEnterpriseApproval(CreateEnterpriseApprovalCommand cmd) {
        return null;
    }

    @Override
    public EnterpriseApprovalDTO updateEnterpriseApproval(UpdateEnterpriseApprovalCommand cmd) {
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovalTypes(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        ListEnterpriseApprovalsResponse res = new ListEnterpriseApprovalsResponse();
        List<EnterpriseApprovalGroupDTO> groups = listEnterpriseApprovalGroups();
        if (groups == null || groups.size() == 0)
            return res;
        List<GeneralApproval> results = generalApprovalProvider.queryGeneralApprovals(new ListingLocator(), Integer.MAX_VALUE - 1, (locator, query) -> {
            //  1-(2)normal operation (nan.rong at 10/16/2017)
            query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd.getOwnerId()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd.getOwnerType()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(cmd.getModuleId()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(cmd.getModuleType()));
            if (null != cmd.getStatus())
                query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(cmd.getStatus()));
            return query;
        });
        if(results ==null || results.size() == 0)
            return res;
        List<EnterpriseApprovalDTO> approvals = results.stream().map(r -> processEnterpriseApproval(r)).collect(Collectors.toList());
        for(EnterpriseApprovalGroupDTO group : groups){
            group.setApprovals(getCorrespondingApproval(group, approvals));
        }
                /*ListGeneralApprovalCommand command = ConvertHelper.convert(cmd, ListGeneralApprovalCommand.class);
        ListGeneralApprovalResponse response = generalApprovalService.listGeneralApproval(command);
        if (response.getDtos() == null || response.getDtos().size() == 0)
            return res;
        */
        return null;
    }

    private EnterpriseApprovalDTO processEnterpriseApproval(GeneralApproval r){
        EnterpriseApproval ea = ConvertHelper.convert(r, EnterpriseApproval.class);
        EnterpriseApprovalDTO dto = ConvertHelper.convert(ea, EnterpriseApprovalDTO.class);
        //  approval icon
        if (dto.getIconUri() != null)
            dto.setIconUrl(contentServerService.parserUri(dto.getIconUri()));
        return dto;
    }

    private List<EnterpriseApprovalDTO> getCorrespondingApproval(EnterpriseApprovalGroupDTO group, List<EnterpriseApprovalDTO> list){
        list = list.stream().filter(r ->group.getId().equals(r.getApprovalGroupId())).collect(Collectors.toList());
        return list;
    }


    @Override
    public ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }
}
