package com.everhomes.archives;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.filedownload.TaskService;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.OrganizationService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.organization.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class ArchivesDTSServiceImpl implements ArchivesDTSService {
    /* Data Transformation Services */

    private static final String ARCHIVES_OWNER_TYPE = "archives_owner_type";

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesDTSServiceImpl.class);

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private ArchivesFormService archivesFormService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private TaskService taskService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public ImportFileTaskDTO importArchivesContacts(MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesContactsCommand cmd) {
        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(mfile);
        task.setOwnerType(ARCHIVES_OWNER_TYPE);
        task.setOwnerId(cmd.getOrganizationId());
        task.setType(ImportFileTaskType.PERSONNEL_ARCHIVES.getCode());
        task.setCreatorUid(userId);

        importFileService.executeTask(() -> {
            ImportFileResponse response = new ImportFileResponse();
            //  将 excel 的中的数据读取
            List<ImportArchivesContactsDTO> datas = handleContactsInfo(resultList);
            if (datas.size() > 0) {
                //  校验标题，若不合格直接返回错误
                String fileLog = checkContactsTitle(datas.get(0));
                if (!StringUtils.isEmpty(fileLog)) {
                    response.setFileLog(fileLog);
                    return response;
                }
                response.setTitle(datas.get(0));
                datas.remove(0);
            }
            //  开始导入，同时设置导入结果
            importContactsInfo(datas, response, cmd.getOrganizationId(), cmd.getDepartmentId());
            //  返回结果
            return response;
        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private ArrayList processorExcel(MultipartFile file) {
        try {
            return PropMrgOwnerHandler.processorExcel(file.getInputStream());
        } catch (IOException e) {
            LOGGER.error("Process excel error.", e);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Process excel error.");
        }
    }

    private List<ImportArchivesContactsDTO> handleContactsInfo(List resultLists) {
        List<ImportArchivesContactsDTO> datas = new ArrayList<>();
        for (int i = 1; i < resultLists.size(); i++) {
            RowResult r = (RowResult) resultLists.get(i);
            ImportArchivesContactsDTO data = new ImportArchivesContactsDTO();
            data.setContactName(r.getCells().get("A") != null ? r.getCells().get("A") : "");
            data.setContactEnName(r.getCells().get("B") != null ? r.getCells().get("B") : "");
            data.setGender(r.getCells().get("C") != null ? r.getCells().get("C") : "");
            data.setContactToken(r.getCells().get("D") != null ? r.getCells().get("D") : "");
            data.setContactShortToken(r.getCells().get("E") != null ? r.getCells().get("E") : "");
            data.setWorkEmail(r.getCells().get("F") != null ? r.getCells().get("F") : "");
            data.setDepartment(r.getCells().get("G") != null ? r.getCells().get("G") : "");
            data.setJobPosition(r.getCells().get("H") != null ? r.getCells().get("H") : "");
            datas.add(data);
        }
        return datas;
    }

    private void importContactsInfo(List<ImportArchivesContactsDTO> datas, ImportFileResponse response, Long organizationId, Long departmentId) {

        ImportFileResultLog<ImportArchivesContactsDTO> log;
        List<ImportFileResultLog<ImportArchivesContactsDTO>> errorDataLogs = new ArrayList<>();
        Long coverCount = 0L;
        for (ImportArchivesContactsDTO data : datas) {
            //  1.校验数据
            log = checkContactsInfo(data, organizationId);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            //  2.导入数据库
            boolean flag = saveContactsInfo(data, organizationId, departmentId);
            if (flag)
                coverCount++;
        }
        //  3.存储所有数据行数
        response.setTotalCount((long) datas.size());
        //  4.存储覆盖数据行数
        response.setCoverCount(coverCount);
        //  5.存储错误数据行数
        response.setFailCount((long) errorDataLogs.size());
        //  6.存储错误数据
        response.setLogs(errorDataLogs);
    }

    //  模板校验
    private String checkContactsTitle(ImportArchivesContactsDTO title) {

        List<String> module = new ArrayList<>(Arrays.asList("姓名", "英文名", "性别", "手机", "短号", "工作邮箱", "部门", "岗位"));
        //  存储字段来进行校验
        List<String> temp = new ArrayList<>();
        temp.add(title.getContactName());
        temp.add(title.getContactEnName());
        temp.add(title.getGender());
        temp.add(title.getContactToken());
        temp.add(title.getContactShortToken());
        temp.add(title.getWorkEmail());
        temp.add(title.getDepartment());
        temp.add(title.getJobPosition());

        for (int i = 0; i < module.size(); i++) {
            if (!module.get(i).equals(temp.get(i)))
                return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        return null;
    }

    private ImportFileResultLog<ImportArchivesContactsDTO> checkContactsInfo(ImportArchivesContactsDTO data, Long organizationId) {

        ImportFileResultLog<ImportArchivesContactsDTO> log = new ImportFileResultLog<>(ArchivesLocaleStringCode.SCOPE);

        //  姓名校验
        if (checkArchivesContactName(log, data, data.getContactName()))
            return log;

        //  英文名校验
        if (checkArchivesContactEnName(log, data, data.getContactEnName()))
            return log;

        //  手机号
        if (checkArchivesContactToken(log, data, data.getContactToken()))
            return log;

        //  短号
        if (checkArchivesContactShortToken(log, data, data.getContactShortToken()))
            return log;

        //  工作邮箱
        if (checkArchivesWorkEmail(log, data, data.getWorkEmail(), organizationId))
            return log;

        //  部门
        if (checkArchivesDepartment(log, data, data.getDepartment()))
            return log;

        //  职务
        if (checkArchivesJobPosition(log, data, data.getJobPosition()))
            return log;

        return null;
    }

    private boolean saveContactsInfo(ImportArchivesContactsDTO data, Long organizationId, Long departmentId) {
        AddArchivesContactCommand addCommand = new AddArchivesContactCommand();
        //  1.设置信息
        addCommand.setOrganizationId(organizationId);
        addCommand.setContactName(data.getContactName());
        addCommand.setContactEnName(data.getContactEnName());
        addCommand.setGender(ArchivesUtil.convertToArchivesEnum(data.getGender(), ArchivesParameter.GENDER));
        addCommand.setContactShortToken(data.getContactShortToken());
        addCommand.setRegionCode(getRealContactToken(data.getContactToken(), ArchivesParameter.REGION_CODE));
        addCommand.setContactToken(getRealContactToken(data.getContactToken(), ArchivesParameter.CONTACT_TOKEN));
        addCommand.setWorkEmail(data.getWorkEmail());
        if (StringUtils.isEmpty(data.getDepartment())) {
            List<Long> ids = new ArrayList<>();
            ids.add(departmentId);
            addCommand.setDepartmentIds(ids);
        } else {
            List<Long> ids = new ArrayList<>();
            ids.add(organizationService.getOrganizationNameByNameAndType(data.getDepartment(), OrganizationGroupType.DEPARTMENT.getCode()));
            addCommand.setDepartmentIds(ids);
        }
        if (!StringUtils.isEmpty(data.getJobPosition())) {
            List<Long> ids = new ArrayList<>();
            ids.add(organizationService.getOrganizationNameByNameAndType(data.getJobPosition(), OrganizationGroupType.JOB_POSITION.getCode()));
            addCommand.setJobPositionIds(ids);
        }
        addCommand.setVisibleFlag(VisibleFlag.SHOW.getCode());
        //  2.先校验是否已存在手机号，否则的话添加完后再校验，结果肯定是覆盖导入
        boolean flag = verifyPersonnelByPhone(organizationId, addCommand.getContactToken());
        //  3.添加人员
        archivesService.addArchivesContact(addCommand);
        return flag;
    }

    @Override
    public ImportFileResponse<ImportArchivesContactsDTO> getImportContactsResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public ImportFileTaskDTO importArchivesEmployees(MultipartFile mfile, ImportArchivesEmployeesCommand cmd) {

        Long userId = UserContext.currentUserId();
        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(mfile);
        task.setOwnerId(cmd.getOrganizationId());
        task.setOwnerType(ARCHIVES_OWNER_TYPE);
        task.setType(ImportFileTaskType.PERSONNEL_ARCHIVES.getCode());
        task.setCreatorUid(userId);

        //  获取对应的表单
        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());

        importFileService.executeTask(() -> {
            ImportFileResponse response = new ImportFileResponse();
            List<ImportArchivesEmployeesDTO> dataList = handleEmployeesInfo(resultList, form.getFormFields());
            String fileLog;
            if (dataList.size() > 0) {
                //  校验标题，若不合格直接返回错误
                fileLog = checkEmployeesTitle(dataList.get(0), form.getFormFields());
                if (!StringUtils.isEmpty(fileLog)) {
                    response.setFileLog(fileLog);
                    return response;
                }
                response.setTitle(convertListToMap(dataList.get(0)));
                dataList.remove(0);
            }

            //  开始导入，同时设置导入结果
            importEmployeesInfo(dataList, response, cmd.getOrganizationId(), cmd.getDepartmentId(), form.getFormFields());
            //  返回结果
            return response;
        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportArchivesEmployeesDTO> handleEmployeesInfo(List resultLists, List fields) {
        List<ImportArchivesEmployeesDTO> datas = new ArrayList<>();
        for (int i = 1; i < resultLists.size(); i++) {
            ImportArchivesEmployeesDTO data = new ImportArchivesEmployeesDTO();
            List<String> values = new ArrayList<>();
            for (int j = 0; j < fields.size(); j++) {
                RowResult r = (RowResult) resultLists.get(i);
                String value = r.getCells().get(ArchivesUtil.GetExcelLetter(j + 1)) != null ? r.getCells().get(ArchivesUtil.GetExcelLetter(j + 1)) : "";
                values.add(value);
            }
            data.setValues(values);
            datas.add(data);
        }
        return datas;
    }

    private String checkEmployeesTitle(ImportArchivesEmployeesDTO title, List<GeneralFormFieldDTO> fields) {
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getFieldDisplayName().equals(title.getValues().get(i)))
                return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        return null;
    }

    private void importEmployeesInfo(
            List<ImportArchivesEmployeesDTO> datas, ImportFileResponse response, Long organizationId,
            Long departmentId, List<GeneralFormFieldDTO> formValues) {
        ImportFileResultLog<Map> log;
        List<ImportFileResultLog<Map>> errorDataLogs = new ArrayList<>();
        Long coverCount = 0L;

        for (ImportArchivesEmployeesDTO data : datas) {
            List<PostApprovalFormItem> itemValues = new ArrayList<>();
            LinkedHashMap<String, Object> basicDataMap = new LinkedHashMap<>();
            boolean errorFlag = false;

            //  1.在校验的时候保存需要单独调用add的值,可以节省一次循环获取的时间
            for (int i = 0; i < formValues.size(); i++) {
                PostApprovalFormItem itemValue = ConvertHelper.convert(formValues.get(i), PostApprovalFormItem.class);
                itemValue.setFieldValue(data.getValues().get(i));
                //  2.校验导入数据
                log = checkEmployeesInfo(data, organizationId, itemValue, departmentId, basicDataMap);
                if (log != null) {
                    errorDataLogs.add(log);
                    errorFlag = true;
                    break;
                }
                itemValues.add(itemValue);
            }
            //  3.如果校验出错误则进行下一次循环
            if (errorFlag)
                continue;

            //  4.导入基础数据
            ImportArchivesJudgments judge = saveEmployeesBasicInfo(organizationId, basicDataMap);
            //  5.导入详细信息
            if (judge.getDetailId() == null)
                continue;
            saveEmployeesDetailInfo(organizationId, judge.getDetailId(), itemValues);
            //  6.记录重复数据
            if (judge.isDuplicateFlag())
                coverCount++;
        }
        //  7.存储所有数据行数
        response.setTotalCount((long) datas.size());
        //  8.存储覆盖数据行数
        response.setCoverCount(coverCount);
        //  9.存储错误数据行数
        response.setFailCount((long) errorDataLogs.size());
        //  10.存储错误数据
        response.setLogs(errorDataLogs);
    }

    private ImportFileResultLog<Map> checkEmployeesInfo(ImportArchivesEmployeesDTO data, Long organizationId,
                                                        PostApprovalFormItem itemValue, Long departmentId, Map<String, Object> map) {
        ImportFileResultLog<Map> log = new ImportFileResultLog<>(ArchivesLocaleStringCode.SCOPE);

        //  姓名校验
        if (ArchivesParameter.CONTACT_NAME.equals(itemValue.getFieldName())) {
            if (checkArchivesContactName(log, convertListToMap(data), itemValue.getFieldValue()))
                return log;
            else
                map.put(ArchivesParameter.CONTACT_NAME, itemValue.getFieldValue());
        }

        //  手机号校验
        if (ArchivesParameter.CONTACT_TOKEN.equals(itemValue.getFieldName())) {
            if (checkArchivesContactToken(log, convertListToMap(data), itemValue.getFieldValue()))
                return log;
            else
                map.put(ArchivesParameter.CONTACT_TOKEN, itemValue.getFieldValue());
        }

        //  性别转换
        if (ArchivesParameter.GENDER.equals(itemValue.getFieldName())) {
            map.put(ArchivesParameter.GENDER, ArchivesUtil.convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.GENDER));
        }

        //  入职时间校验
        if (ArchivesParameter.CHECK_IN_TIME.equals(itemValue.getFieldName())) {
            if (checkArchivesCheckInTime(log, convertListToMap(data), itemValue.getFieldValue()))
                return log;
            else
                map.put(ArchivesParameter.CHECK_IN_TIME, itemValue.getFieldValue());
        }

        //  员工类型校验
        if (ArchivesParameter.EMPLOYEE_TYPE.equals(itemValue.getFieldName())) {
            if (checkArchivesEmployeeType(log, convertListToMap(data), itemValue.getFieldValue()))
                return log;
        }

        //  在 check 阶段就把部门、岗位和职级的 id 找到
        if (ArchivesParameter.DEPARTMENT.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                map.put(ArchivesParameter.DEPARTMENT_IDS, departmentId);
            } else {
                if (checkArchivesDepartment(log, convertListToMap(data), itemValue.getFieldValue()))
                    return log;
                else
                    map.put(ArchivesParameter.DEPARTMENT_IDS, organizationService.getOrganizationNameByNameAndType(itemValue.getFieldValue(), OrganizationGroupType.DEPARTMENT.getCode()));
            }
        }

        if (ArchivesParameter.JOB_POSITION.equals(itemValue.getFieldName())) {
            if (!StringUtils.isEmpty(itemValue.getFieldValue()))
                map.put(ArchivesParameter.JOB_POSITION_IDS, organizationService.getOrganizationNameByNameAndType(itemValue.getFieldValue(), OrganizationGroupType.JOB_POSITION.getCode()));
        }

        if (ArchivesParameter.JOB_LEVEL.equals(itemValue.getFieldName())) {
            if (!StringUtils.isEmpty(itemValue.getFieldValue()))
                map.put(ArchivesParameter.JOB_LEVEL_IDS, organizationService.getOrganizationNameByNameAndType(itemValue.getFieldValue(), OrganizationGroupType.JOB_LEVEL.getCode()));
        }

        if(ArchivesParameter.WORK_EMAIL.equals(itemValue.getFieldName())){
            if (!StringUtils.isEmpty(itemValue.getFieldValue())){
                if(checkArchivesWorkEmail(log, convertListToMap(data), itemValue.getFieldValue(), organizationId))
                    return log;
            }
        }

        return null;
    }

    //  为了能够成功的解析得转换成为 map
    private Map convertListToMap(ImportArchivesEmployeesDTO data) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < data.getValues().size(); i++) {
            map.put(String.valueOf(i), data.getValues().get(i));
        }
        return map;
    }

    private ImportArchivesJudgments saveEmployeesBasicInfo(
            Long organizationId, Map<String, Object> basicDataMap) {
        ImportArchivesJudgments judge = new ImportArchivesJudgments();

        AddArchivesEmployeeCommand addCommand = new AddArchivesEmployeeCommand();
        addCommand.setOrganizationId(organizationId);
        addCommand.setContactName((String) basicDataMap.get(ArchivesParameter.CONTACT_NAME));
        addCommand.setGender((Byte) basicDataMap.get(ArchivesParameter.GENDER));

        //  1.部门、岗位、职级在 check 阶段获取 id 值
        List<Long> departmentIds = new ArrayList<>();
        departmentIds.add((Long) basicDataMap.get(ArchivesParameter.DEPARTMENT_IDS));
        addCommand.setDepartmentIds(departmentIds);
        List<Long> jobPositionIds = new ArrayList<>();
        jobPositionIds.add((Long) basicDataMap.get(ArchivesParameter.JOB_POSITION_IDS));
        addCommand.setJobPositionIds(jobPositionIds);
        List<Long> jobLevelIds = new ArrayList<>();
        jobLevelIds.add((Long) basicDataMap.get(ArchivesParameter.JOB_LEVEL_IDS));
        addCommand.setJobLevelIds(jobLevelIds);
        addCommand.setContactToken((String) basicDataMap.get(ArchivesParameter.CONTACT_TOKEN));
        if (basicDataMap.get(ArchivesParameter.CHECK_IN_TIME) != null)
            addCommand.setCheckInTime((String) basicDataMap.get(ArchivesParameter.CHECK_IN_TIME));

        //  2.先校验是否已存在手机号，否则的话添加完后再校验，结果肯定是覆盖导入
        judge.setDuplicateFlag(verifyPersonnelByPhone(organizationId, addCommand.getContactToken()));
        //  3.添加人员
        ArchivesEmployeeDTO dto = archivesService.addArchivesEmployee(addCommand);
        Long detailId = null;
        if (dto != null)
            detailId = dto.getDetailId();
        judge.setDetailId(detailId);
        return judge;
    }

    private void saveEmployeesDetailInfo(Long organizationId, Long detailId, List<PostApprovalFormItem> itemValues) {
        UpdateArchivesEmployeeCommand updateCommand = new UpdateArchivesEmployeeCommand();
        updateCommand.setDetailId(detailId);
        updateCommand.setOrganizationId(organizationId);
        updateCommand.setValues(itemValues);
        archivesService.updateArchivesEmployee(updateCommand);
    }

    @Override
    public ImportFileResponse<ImportArchivesEmployeesDTO> getImportEmployeesResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd) {
        //  export with the file download center
        Map<String, Object> params = new HashMap<>();
        //  the value could be null if it is not exist
        params.put("namespaceId", cmd.getNamespaceId());
        params.put("organizationId", cmd.getOrganizationId());
        params.put("keywords", cmd.getKeywords());
        params.put("userId", UserContext.current().getUser().getId());
        String fileName = ArchivesExcelLocaleString.E_FILENAME + "_" + formatter.format(LocalDate.now()) + ".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ArchivesEmployeesExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }

    @Override
    public OutputStream getArchivesEmployeesExportStream(ExportArchivesEmployeesCommand cmd, Long taskId) {

        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());
        //  title
        List<String> title = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());
        taskService.updateTaskProcess(taskId, 20);
        //  data
        List<Long> detailIds = organizationService.listDetailIdsByEnterpriseId(cmd.getOrganizationId());
        taskService.updateTaskProcess(taskId, 30);
        List<ExportArchivesEmployeesDTO> value = new ArrayList<>();
        for (Long detailId : detailIds) {
            ExportArchivesEmployeesDTO dto = new ExportArchivesEmployeesDTO();
            GetArchivesEmployeeCommand getCommand =
                    new GetArchivesEmployeeCommand(cmd.getOrganizationId(), detailId, 1);
            GetArchivesEmployeeResponse response = archivesService.getArchivesEmployee(getCommand);
            List<String> employeeValues = response.getForm().getFormFields().stream().map(GeneralFormFieldDTO::getFieldValue).collect(Collectors.toList());
            dto.setVals(employeeValues);
            value.add(dto);
        }
        taskService.updateTaskProcess(taskId, 75);

        //  write excel
        XSSFWorkbook workbook = exportArchivesEmployeesFiles(title, value);
        taskService.updateTaskProcess(taskId, 95);
        return writeExcel(workbook);
    }

    private ArchivesFormDTO getRealArchivesForm(Integer namespaceId, Long orgId) {
        ArchivesFormDTO form = archivesFormService.getArchivesFormByOrgId(namespaceId, orgId);
        if (form == null)
            form = archivesFormService.getArchivesDefaultForm();
        return form;
    }

    private XSSFWorkbook exportArchivesEmployeesFiles(List<String> title, List<ExportArchivesEmployeesDTO> value) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(ArchivesExcelLocaleString.E_FILENAME);
        sheet.createFreezePane(1,2,1,1);
        //  1.head
        Row headRow = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1));
        headRow.setHeight((short) (50 * 20));
        createExcelHead(workbook, headRow, ArchivesExcelLocaleString.E_HEAD + DateHelper.currentGMTTime());
        //  2.title
        Row titleRow = sheet.createRow(1);
        createExcelTitle(workbook, sheet, titleRow, title);
        //  3.data
        for (int rowIndex = 2; rowIndex < value.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex);
            createEmployeesFileData(workbook, dataRow, value.get(rowIndex - 2).getVals());
        }
        return workbook;
    }

    private void createEmployeesFileData(XSSFWorkbook workbook, Row dataRow, List<String> list) {
        //  设置样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("微软雅黑");
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = dataRow.createCell(i);
            cell.setCellStyle(contentStyle);
            cell.setCellValue(list.get(i));
        }
    }

    private ByteArrayOutputStream writeExcel(XSSFWorkbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
        return out;
    }

    @Override
    public void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse) {

        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());
        String fileName = ArchivesExcelLocaleString.T_FILENAME;
        String head = ArchivesExcelLocaleString.T_HEAD;
        List<String> title = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);
        sheet.createFreezePane(1,2,1,1);

        //  1.set the header
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1));
        Row headRow = sheet.createRow(0);
        headRow.setHeight((short) (150 * 20));
        createExcelHead(workbook, headRow, head);

        //  2.set the title
        Row titleRow = sheet.createRow(1);
        createExcelTitle(workbook, sheet, titleRow, title);

        writeHttpExcel(workbook, httpResponse, fileName);
    }

    private void createExcelHead(XSSFWorkbook workbook, Row headRow, String head) {
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // font.setBold(true);
        font.setFontName("微软雅黑");
        headStyle.setFont(font);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); //  find it in the IndexedColors
        headStyle.setWrapText(true);

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    private void createExcelTitle(XSSFWorkbook workbook, Sheet sheet, Row titleRow, List<String> title) {
        XSSFCellStyle commonStyle = commonTitleStyle(workbook);
        XSSFCellStyle mandatoryStyle = mandatoryTitleStyle(workbook);
        for (int i = 0; i < title.size(); i++) {
            Cell cell = titleRow.createCell(i);
            sheet.setColumnWidth( i,18 * 256);
            if (checkMandatory(title.get(i)))
                cell.setCellStyle(mandatoryStyle);
            else
                cell.setCellStyle(commonStyle);
            cell.setCellValue(title.get(i));
        }
    }

    private boolean checkMandatory(String text) {
        if (ArchivesExcelLocaleString.T_NAME.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_CONTACT_TOKEN.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_CHECK_IN_TIME.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_EMPLOYEE_TYPE.equals(text))
            return true;
        if (ArchivesExcelLocaleString.T_DEPARTMENT.equals(text))
            return true;
        return false;
    }

    private XSSFCellStyle commonTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private XSSFCellStyle mandatoryTitleStyle(XSSFWorkbook workbook){
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.RED.index);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private void writeHttpExcel(Workbook workbook, HttpServletResponse httpResponse, String fileName) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            DownloadUtil.download(out, httpResponse, fileName);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        }finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
    }

    private boolean verifyPersonnelByPhone(Long organizationId, String contactToken) {
        VerifyPersonnelByPhoneCommand verifyCommand = new VerifyPersonnelByPhoneCommand();
        verifyCommand.setEnterpriseId(organizationId);
        verifyCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        verifyCommand.setPhone(contactToken);
        try {
            organizationService.verifyPersonnelByPhone(verifyCommand);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private String getRealContactToken(String tokens, String type) {
        String token[] = tokens.split(" ");
        //  1.native users do not need to write region code
        if (token.length == 1) {
            if (type.equals(ArchivesParameter.CONTACT_TOKEN))
                return token[0];
            else
                return "86";
        } else {
            //  2.foreigners need to write region code
            token[0] = token[0].substring(1, token[0].length());
            if (type.equals(ArchivesParameter.CONTACT_TOKEN))
                return token[1];
            else
                return token[0];
        }
    }

    private <T> boolean checkArchivesContactName(ImportFileResultLog<T> log, T data, String contactName) {
        if (StringUtils.isEmpty(contactName)) {
            LOGGER.warn("Contact name is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name is empty.");
            log.setCode(ArchivesLocaleStringCode.ERROR_NAME_IS_EMPTY);
            return true;
        }
        if (contactName.length() > 20) {
            LOGGER.warn("Contact name is too long. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name too long.");
            log.setCode(ArchivesLocaleStringCode.ERROR_NAME_TOO_LONG);
            return true;
        }
        if (!Pattern.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_\\n]+$", contactName)) {
            LOGGER.warn("Contact name wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name wrong format.");
            log.setCode(ArchivesLocaleStringCode.ERROR_NAME_WRONG_FORMAT);
            return true;
        }
        return false;
    }

    private <T> boolean checkArchivesContactEnName(ImportFileResultLog<T> log, T data, String contactEnName) {
        if (!StringUtils.isEmpty(contactEnName)) {
            if (!Pattern.matches("^[a-zA-Z0-9_\\-.]+$", contactEnName)) {
                LOGGER.warn("Contact EnName wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Contact EnName wrong format");
                log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_EN_NAME_WRONG_FORMAT);
                return true;
            }
        }
        return false;
    }

    private <T> boolean checkArchivesContactToken(ImportFileResultLog<T> log, T data, String contactToken) {
        if (StringUtils.isEmpty(contactToken)) {
            LOGGER.warn("Contact token is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token is empty");
            log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_TOKEN_IS_EMPTY);
            return true;
        }
        if (!Pattern.matches("^1\\d{10}$", getRealContactToken(contactToken, ArchivesParameter.CONTACT_TOKEN))) {
            LOGGER.warn("Contact token wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token wrong format");
            log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_TOKEN_WRONG_FORMAT);
            return true;
        }
        return false;
    }

    private <T> boolean checkArchivesDepartment(ImportFileResultLog<T> log, T data, String department) {
        if (!StringUtils.isEmpty(department)) {
            if (organizationService.getOrganizationNameByNameAndType(department, OrganizationGroupType.DEPARTMENT.getCode()) == null) {
                LOGGER.warn("Department not found. data = {}", data);
                log.setData(data);
                log.setErrorLog("Department not found");
                log.setCode(ArchivesLocaleStringCode.ERROR_DEPARTMENT_NOT_FOUND);
                return true;
            }
        }
        return false;
    }

    private <T> boolean checkArchivesJobPosition(ImportFileResultLog<T> log, T data, String jobPosition) {
        if (!StringUtils.isEmpty(jobPosition)) {
            if (organizationService.getOrganizationNameByNameAndType(jobPosition, OrganizationGroupType.JOB_POSITION.getCode()) == null) {
                LOGGER.warn("JobPosition not found. data = {}", data);
                log.setData(data);
                log.setErrorLog("JobPosition not found");
                log.setCode(ArchivesLocaleStringCode.ERROR_JOB_POSITION_NOT_FOUND);
                return true;
            }
        }
        return false;
    }

    private <T> boolean checkArchivesContactShortToken(ImportFileResultLog<T> log, T data, String contactShortToken) {
        if (!StringUtils.isEmpty(contactShortToken)) {
            if (!Pattern.matches("\\d+", contactShortToken)) {
                LOGGER.warn("Contact short token wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Contact short token wrong format");
                log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_SHORT_TOKEN_WRONG_FORMAT);
                return true;
            }
        }
        return false;
    }

    private <T> boolean checkArchivesWorkEmail(ImportFileResultLog<T> log, T data, String workEmail, Long organizationId) {
        if (!StringUtils.isEmpty(workEmail)) {
            if (!Pattern.matches("^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", workEmail)) {
                LOGGER.warn("WorkEmail wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("WorkEmail wrong format");
                log.setCode(ArchivesLocaleStringCode.ERROR_WORK_EMAIL_WRONG_FORMAT);
                return true;
            }
            if (!organizationService.verifyPersonnelByWorkEmail(organizationId, null, workEmail)) {
                LOGGER.warn("Duplicate workEmail. data = {}", data);
                log.setData(data);
                log.setErrorLog("Duplicate workEmail");
                log.setCode(ArchivesLocaleStringCode.ERROR_DUPLICATE_WORK_EMAIL);
                return true;
            }
        }
        return false;
    }

    private <T> boolean checkArchivesCheckInTime(ImportFileResultLog<T> log, T data, String checkInTime) {
        if (StringUtils.isEmpty(checkInTime)) {
            LOGGER.warn("Employee checkInTime is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Employee checkInTime is empty.");
            log.setCode(ArchivesLocaleStringCode.ERROR_CHECK_IN_TIME_IS_EMPTY);
            return true;
        }
        java.sql.Date temp = ArchivesUtil.parseDate(checkInTime);
        if (temp == null) {
            LOGGER.warn("Employee date wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Employee date wrong format.");
            log.setCode(ArchivesLocaleStringCode.ERROR_DATE_WRONG_FORMAT);
            return true;
        } else
            return false;
    }

    private <T> boolean checkArchivesEmployeeType(ImportFileResultLog<T> log, T data, String checkInTime) {
        if (StringUtils.isEmpty(checkInTime)) {
            LOGGER.warn("Employee employeeType is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Employee employeeType is empty.");
            log.setCode(ArchivesLocaleStringCode.ERROR_EMPLOYEE_TYPE_IS_EMPTY);
            return true;
        }
        return false;
    }

    @Override
    public void exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
        importFileService.exportImportFileFailResultXls(httpResponse, cmd.getTaskId());
    }

}
