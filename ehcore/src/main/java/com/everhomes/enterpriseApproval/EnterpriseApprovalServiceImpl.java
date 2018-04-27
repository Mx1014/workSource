package com.everhomes.enterpriseApproval;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.flow.*;
import com.everhomes.general_approval.GeneralApprovalFlowCase;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.general_approval.GeneralApprovalServiceImpl;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.*;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.flow.*;
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
    private GeneralApprovalFlowModuleListener generalApprovalFlowModuleListener;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ConfigurationProvider configurationProvider;

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
            results = details.stream().map(r -> {
                EnterpriseApprovalRecordDTO dto = convertEnterpriseApprovalRecordDTO(r);
                return dto;
            }).collect(Collectors.toList());
        }
        res.setRecords(results);
        res.setNextPageAnchor(locator.getAnchor());
        return res;
    }

    @Override
    public EnterpriseApprovalRecordDTO convertEnterpriseApprovalRecordDTO(FlowCase r) {
        GeneralApprovalFlowCase flowCase = ConvertHelper.convert(r, GeneralApprovalFlowCase.class);
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
    public List<EnterpriseApprovalGroupDTO> listAvailableApprovalGroups() {
        return null;
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
            String formLogs = "";
            for (int i = 4; i < entityLists.size(); i++) {
                formLogs += entityLists.get(i).getKey() + " : " + entityLists.get(i).getValue() + "\n";
            }
            Cell formCell = dataRow.createCell(4);
            formCell.setCellStyle(wrapStyle);
            formCell.setCellValue(formLogs);
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
            String operateLogs = "";
            for (int i = 0; i < operateLogLists.size(); i++) {
                operateLogs += operateLogLists.get(i).getFlowUserName() + operateLogLists.get(i).getLogContent() + "\n";
            }
            Cell logCell = dataRow.createCell(6);
            logCell.setCellStyle(wrapStyle);
            logCell.setCellValue(operateLogs);
        }

        //  5. the current operator
        FlowCaseProcessorsProcessor processorRes = flowService.getCurrentProcessors(data.getFlowCaseId(), true);
        if (processorRes.getProcessorsInfoList() != null && processorRes.getProcessorsInfoList().size() > 0) {
            String processors = "";
            for (int i = 0; i < processorRes.getProcessorsInfoList().size(); i++) {
                processors += processorRes.getProcessorsInfoList().get(i).getNickName() + ",";
            }
            if (!"".equals(processors))
                processors = processors.substring(0, processors.length() - 1);
            dataRow.createCell(7).setCellValue(processors);
        }

        //  6. the current supervisor
        FlowCase flowCase = flowService.getFlowCaseById(data.getFlowCaseId());
        List<UserInfo> supervisorLists = flowService.getSupervisor(flowCase);
        if (supervisorLists != null && supervisorLists.size() > 0) {
            String supervisors = "";
            for (int i = 0; i < supervisorLists.size(); i++) {
                supervisors += supervisorLists.get(i).getNickName() + ",";
            }
            if (!"".equals(supervisors))
                supervisors = supervisors.substring(0, supervisors.length() - 1);
            dataRow.createCell(8).setCellValue(supervisors);
        }
    }

    public List<FlowCaseEntity> getApprovalDetails(Long flowCaseId) {
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        return generalApprovalFlowModuleListener.onFlowCaseDetailRender(flowCase, null);
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
        return null;
    }

    @Override
    public ListEnterpriseApprovalsResponse listAvailableEnterpriseApprovals(ListEnterpriseApprovalsCommand cmd) {
        return null;
    }
}
