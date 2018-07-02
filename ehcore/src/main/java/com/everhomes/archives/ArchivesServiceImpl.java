package com.everhomes.archives;

import com.alibaba.fastjson.JSON;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.general_form.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.*;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class ArchivesServiceImpl implements ArchivesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesServiceImpl.class);

    private static final String ARCHIVES_FORM = "archives_form";

    private static final String ARCHIVES_OWNER_TYPE = "archives_owner_type";

    private static final String ARCHIVES_NOTIFICATION = "archives_notification";

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ArchivesProvider archivesProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private GeneralFormService generalFormService;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralFormValProvider generalFormValProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private TaskService taskService;

    @Override
    public ArchivesContactDTO addArchivesContact(AddArchivesContactCommand cmd) {

        //  校验权限 by lei.lv
        if (cmd.getDetailId() != null) {
            Long departmentId = organizationService.getDepartmentByDetailId(cmd.getDetailId());
            organizationService.checkOrganizationpPivilege(departmentId, PrivilegeConstants.CREATE_OR_MODIFY_PERSON);
        }
        ArchivesContactDTO dto = new ArchivesContactDTO();
        //  组织架构添加人员
        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setGender(cmd.getGender());
        addCommand.setContactToken(cmd.getContactToken());
        if (cmd.getDepartmentIds() != null)
            addCommand.setDepartmentIds(new ArrayList<>(cmd.getDepartmentIds()));
        if (cmd.getJobPositionIds() != null)
            addCommand.setJobPositionIds(new ArrayList<>(cmd.getJobPositionIds()));
        if (cmd.getJobLevelIds() != null)
            addCommand.setJobLevelIds(new ArrayList<>(cmd.getJobLevelIds()));
        addCommand.setVisibleFlag(cmd.getVisibleFlag());
        //  1.添加人员到组织架构
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  2.获得 detailId 然后处理其它信息
        Long detailId = null;
        if (memberDTO != null)
            detailId = memberDTO.getDetailId();
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (employee == null)
            return null;
        employee.setEnName(cmd.getContactEnName());
        employee.setRegionCode(cmd.getRegionCode());
        employee.setContactShortToken(cmd.getContactShortToken());
        employee.setWorkEmail(cmd.getWorkEmail());
        employee.setContractPartyId(cmd.getOrganizationId());

        dto.setDetailId(employee.getId());
        dto.setContactName(employee.getContactName());
        dto.setContactToken(employee.getContactToken());
        //  3-1.编辑则直接更新信息
        if (TrueOrFalseFlag.fromCode(cmd.getUpdateFlag()) == TrueOrFalseFlag.TRUE) {
            organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());
            return dto;
        }
        //  3-2.新增则初始化部分信息
        employee.setCheckInTime(ArchivesUtil.currentDate());
        employee.setCheckInTimeIndex(ArchivesUtil.getMonthAndDay(employee.getCheckInTime()));
        employee.setEmploymentTime(ArchivesUtil.currentDate());
        employee.setEmployeeType(EmployeeType.FULLTIME.getCode());
        employee.setEmployeeStatus(EmployeeStatus.ON_THE_JOB.getCode());
        organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());

        //  4.查询若存在于离职列表则删除
        deleteArchivesDismissEmployees(detailId, cmd.getOrganizationId());

        //  5.增加入职记录
        AddArchivesEmployeeCommand logCommand = ConvertHelper.convert(cmd, AddArchivesEmployeeCommand.class);
        logCommand.setCheckInTime(String.valueOf(employee.getCheckInTime()));
        logCommand.setEmploymentTime(logCommand.getCheckInTime());
        logCommand.setMonth(0);
        logCommand.setEmployeeType(EmployeeType.FULLTIME.getCode());
        logCommand.setEmployeeStatus(EmployeeStatus.ON_THE_JOB.getCode());
        addCheckInLogs(detailId, logCommand);
        return dto;
    }

    @Override
    public void transferArchivesContacts(TransferArchivesContactsCommand cmd) {
        //权限校验
        cmd.getDetailIds().forEach(detailId -> {
            Long departmentId = organizationService.getDepartmentByDetailId(detailId);
            organizationService.checkOrganizationpPivilege(departmentId, PrivilegeConstants.MODIFY_DEPARTMENT_JOB_POSITION);
        });
        dbProvider.execute((TransactionStatus status) -> {
            if (cmd.getDetailIds() != null) {
                TransferArchivesEmployeesCommand transferCommand = new TransferArchivesEmployeesCommand();
                transferCommand.setOrganizationId(cmd.getOrganizationId());
                transferCommand.setDetailIds(cmd.getDetailIds());
                transferCommand.setDepartmentIds(cmd.getDepartmentIds());
                transferCommand.setJobPositionIds(cmd.getJobPositionIds());
                transferCommand.setJobLevelIds(cmd.getJobLevelIds());
                //  调整部门、岗位、职级并同步到detail表
                organizationService.transferOrganizationPersonels(transferCommand);
            }
            return null;
        });
    }

    @Override
    public void deleteArchivesContacts(DeleteArchivesContactsCommand cmd) {
        //权限校验
        cmd.getDetailIds().forEach(detailId -> {
            Long departmentId = organizationService.getDepartmentByDetailId(detailId);
            organizationService.checkOrganizationpPivilege(departmentId, PrivilegeConstants.DELETE_PERSON);
        });

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            if (cmd.getDetailIds() != null) {
                //  1.置顶表删除
                archivesProvider.deleteArchivesStickyContactsByDetailIds(namespaceId, cmd.getDetailIds());
                //  2.人事删除
                DeleteArchivesEmployeesCommand command = ConvertHelper.convert(cmd, DeleteArchivesEmployeesCommand.class);
                deleteArchivesEmployees(command);
            }
            return null;
        });
    }

    //  置顶通讯录成员
    @Override
    public void stickArchivesContact(StickArchivesContactCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        //  状态码为 0 时删除
        if (cmd.getStick().equals("0")) {
            ArchivesStickyContacts result = archivesProvider.findArchivesStickyContactsByDetailIdAndOrganizationId(
                    namespaceId, cmd.getOrganizationId(), cmd.getDetailId());
            if (result != null)
                archivesProvider.deleteArchivesStickyContacts(result);
        }

        //  状态码为 1 时新增置顶
        if (cmd.getStick().equals("1")) {
            ArchivesStickyContacts result = archivesProvider.findArchivesStickyContactsByDetailIdAndOrganizationId(namespaceId, cmd.getOrganizationId(), cmd.getDetailId());
            if (result == null) {
                ArchivesStickyContacts contactsSticky = new ArchivesStickyContacts();
                contactsSticky.setNamespaceId(namespaceId);
                contactsSticky.setOrganizationId(cmd.getOrganizationId());
                contactsSticky.setDetailId(cmd.getDetailId());
                contactsSticky.setOperatorUid(userId);
                archivesProvider.createArchivesStickyContacts(contactsSticky);
            } else {
                archivesProvider.updateArchivesStickyContacts(result);
            }
        }
    }

    @Override
    public ListArchivesContactsResponse listArchivesContacts(ListArchivesContactsCommand cmd) {
        /*
          1.If the keywords is not null, just pass the key and get the corresponding employee back.
          2.If the keywords is null, then judged by the "pageAnchor"
          3.If the pageAnchor is null, we should get stick employees first.
          4.if the pageAnchor is not null, means we should get the next page of employees, so ignore those stick employees.
         */
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListArchivesContactsResponse response = new ListArchivesContactsResponse();
        final Integer stickCount = 20;  //  置顶数为20,表示一页最多显示20个置顶人员 at 11/06/2017
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);

        List<Long> detailIds = archivesProvider.listArchivesStickyContactsIds(namespaceId, cmd.getOrganizationId(), stickCount);    //  保存置顶人员
        if (!StringUtils.isEmpty(cmd.getKeywords()) || detailIds == null) {
            //  有查询的时候已经不需要置顶了，直接查询对应人员
            List<ArchivesContactDTO> contacts = new ArrayList<>(listArchivesContacts(cmd, response, null));
            response.setContacts(contacts);
        } else {
            if (StringUtils.isEmpty(cmd.getPageAnchor())) {
                List<ArchivesContactDTO> contacts = new ArrayList<>();
                //  读取置顶人员
                for (Long detailId : detailIds) {
                    ArchivesContactDTO stickDTO = getArchivesStickyContactInfo(detailId);
                    if (stickDTO != null)
                        contacts.add(stickDTO);
                }
                //  获取其余人员
                cmd.setPageSize(cmd.getPageSize() - detailIds.size());
                contacts.addAll(listArchivesContacts(cmd, response, detailIds));
                response.setContacts(contacts);
            } else {
                //  若已经读取了置顶的人则直接往下继续读
                List<ArchivesContactDTO> contacts = new ArrayList<>(listArchivesContacts(cmd, response, detailIds));
                response.setContacts(contacts);
            }
        }
        return response;
    }

    private ArchivesContactDTO getArchivesStickyContactInfo(Long detailId) {
        ArchivesContactDTO dto = new ArchivesContactDTO();
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);

        if (detail == null)
            return null;
        dto.setDetailId(detail.getId());
        dto.setOrganizationId(detail.getOrganizationId());
        dto.setContactName(detail.getContactName());
        dto.setContactToken(detail.getContactToken());
        dto.setWorkEmail(detail.getWorkEmail());
        dto.setTargetId(detail.getTargetId());
        dto.setTargetType(detail.getTargetType());
        dto.setRegionCode(detail.getRegionCode());
        dto.setContactShortToken(detail.getContactShortToken());
        dto.setContactEnName(detail.getEnName());

        //  查询部门
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        Organization directlyEnterprise = organizationProvider.findOrganizationById(detail.getOrganizationId());
        dto.setDepartments(organizationService.getOrganizationMemberGroups(groupTypes, dto.getContactToken(), directlyEnterprise.getPath()));

        //  查询职位
        dto.setJobPositions(organizationService.getOrganizationMemberGroups(OrganizationGroupType.JOB_POSITION, dto.getContactToken(), directlyEnterprise.getPath()));

        //	设置隐私保护值
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndToken(dto.getContactToken(), dto.getOrganizationId());
        dto.setVisibleFlag(member.getVisibleFlag());

        //  设置置顶
        dto.setStick("1");

        return dto;
    }

    private List<ArchivesContactDTO> listArchivesContacts(ListArchivesContactsCommand cmd, ListArchivesContactsResponse response, List<Long> detailIds) {
        List<ArchivesContactDTO> contacts = new ArrayList<>();
        ListOrganizationContactCommand orgCommand = new ListOrganizationContactCommand();
        orgCommand.setOrganizationId(cmd.getOrganizationId());
        orgCommand.setPageAnchor(cmd.getPageAnchor());
        orgCommand.setPageSize(cmd.getPageSize());
        orgCommand.setFilterScopeTypes(cmd.getFilterScopeTypes());
        if (detailIds != null && detailIds.size() > 0)
            orgCommand.setExceptIds(detailIds);
        if (!StringUtils.isEmpty(cmd.getKeywords()))
            orgCommand.setKeywords(cmd.getKeywords());
        if (cmd.getTargetTypes() != null)
            orgCommand.setTargetTypes(cmd.getTargetTypes());

        //modified by lei.lv
        orgCommand.setVisibleFlag(VisibleFlag.ALL.getCode());

        ListOrganizationMemberCommandResponse members = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);
        if (members != null && members.getMembers() != null) {
            members.getMembers().forEach(r -> {
                ArchivesContactDTO dto = new ArchivesContactDTO();
                dto.setDetailId(r.getDetailId());
                dto.setOrganizationId(r.getOrganizationId());
                dto.setTargetId(r.getTargetId());
                dto.setTargetType(r.getTargetType());
                dto.setContactName(r.getContactName());
                dto.setDepartments(r.getDepartments());
                dto.setJobPositions(r.getJobPositions());
                dto.setJobLevels(r.getJobLevels());
                dto.setGender(r.getGender());
                dto.setRegionCode(r.getRegionCode());
                dto.setContactToken(r.getContactToken());
                dto.setContactShortToken(r.getContactShortToken());
                dto.setContactEnName(r.getContactEnName());
                dto.setWorkEmail(r.getWorkEmail());
                dto.setVisibleFlag(r.getVisibleFlag());
                dto.setStick("0");
                contacts.add(dto);
            });
            response.setNextPageAnchor(members.getNextPageAnchor());
        }
        return contacts;
    }

    @Override
    public ImportFileTaskDTO importArchivesContacts(MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesContactsCommand cmd) {

        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(mfile);
        task.setOwnerType(ARCHIVES_OWNER_TYPE);
        task.setOwnerId(cmd.getOrganizationId());
        task.setType(ImportFileTaskType.PERSONNEL_ARCHIVES.getCode());
        task.setCreatorUid(userId);

        //  调用导入方法
        importFileService.executeTask(() -> {
            ImportFileResponse response = new ImportFileResponse();
            //  将 excel 的中的数据读取
            List<ImportArchivesContactsDTO> datas = handleImportArchivesContacts(resultList);
            String fileLog;
            if (datas.size() > 0) {
                //  校验标题，若不合格直接返回错误
                fileLog = checkArchivesContactsTitle(datas.get(0));
                if (!StringUtils.isEmpty(fileLog)) {
                    response.setFileLog(fileLog);
                    return response;
                }
                response.setTitle(datas.get(0));
                datas.remove(0);
            }

            //  开始导入，同时设置导入结果
            importArchivesContactsFiles(datas, response, cmd.getOrganizationId(), cmd.getDepartmentId());
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

    private List<ImportArchivesContactsDTO> handleImportArchivesContacts(List resultLists) {
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

    private void importArchivesContactsFiles(List<ImportArchivesContactsDTO> datas, ImportFileResponse response, Long organizationId, Long departmentId) {

        ImportFileResultLog<ImportArchivesContactsDTO> log;
        List<ImportFileResultLog<ImportArchivesContactsDTO>> errorDataLogs = new ArrayList<>();
        Long coverCount = 0L;
        for (ImportArchivesContactsDTO data : datas) {
            //  1.校验数据
            log = checkArchivesContactsDatas(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            //  2.导入数据库
            boolean flag = saveArchivesContactsData(data, organizationId, departmentId);
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
    private String checkArchivesContactsTitle(ImportArchivesContactsDTO title) {

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

    private ImportFileResultLog<ImportArchivesContactsDTO> checkArchivesContactsDatas(ImportArchivesContactsDTO data) {

        ImportFileResultLog<ImportArchivesContactsDTO> log = new ImportFileResultLog<>(ArchivesLocaleStringCode.SCOPE);

        //  姓名校验
        if (!checkArchivesContactName(log, data, data.getContactName()))
            return log;

        //  英文名校验
        if (!checkArchivesContactEnName(log, data, data.getContactEnName()))
            return log;

        //  手机号
        if (!checkArchivesContactToken(log, data, data.getContactToken()))
            return log;

        //  短号
        if (!checkArchivesContactShortToken(log, data, data.getContactShortToken()))
            return log;

        //  工作邮箱
        if (!checkArchivesWorkEmail(log, data, data.getWorkEmail()))
            return log;

        //  部门
        if (!checkArchivesDepartment(log, data, data.getDepartment()))
            return log;

        //  职务
        if (!checkArchivesJobPosition(log, data, data.getJobPosition()))
            return log;

        return null;
    }

    private boolean saveArchivesContactsData(ImportArchivesContactsDTO data, Long organizationId, Long departmentId) {
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
        addArchivesContact(addCommand);
        return flag;
    }

    @Override
    public void exportArchivesContacts(ExportArchivesContactsCommand cmd) {
        //  export with the file download center
        Map<String, Object> params = new HashMap<>();
        //  the value could be null if it is not exist
        params.put("organizationId", cmd.getOrganizationId());
        params.put("keywords", cmd.getKeywords());
        params.put("namespaceId", UserContext.getCurrentNamespaceId());
        String fileName = localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.CONTACT_LIST, "zh_CN", "userLists") + ".xlsx";
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ArchivesContactsExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }

    @Override
    public OutputStream getArchivesContactsExportStream(ListArchivesContactsCommand cmd, Long taskId) {
        //  get the output stream which will be used by the file download center
        OutputStream outputStream = null;
        cmd.setPageSize(Integer.MAX_VALUE - 1);
        cmd.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CHILD_ENTERPRISE.getCode()));
        ListArchivesContactsResponse response = listArchivesContacts(cmd);
        taskService.updateTaskProcess(taskId, 10);
        if (response.getContacts() != null && response.getContacts().size() > 0) {
            //  1.设置导出文件名与 sheet 名
            String fileName = localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.CONTACT_LIST, "zh_CN", "userLists");
            ExcelUtils excelUtils = new ExcelUtils(fileName, fileName);
            //  2.设置导出标题栏
            List<String> titleNames = new ArrayList<>(Arrays.asList("姓名", "性别", "手机", "短号", "工作邮箱", "部门", "岗位"));
            //  3.设置格式长度
            List<Integer> cellSizes = new ArrayList<>(Arrays.asList(20, 10, 20, 20, 30, 30, 20));
            //  4.设置导出变量名
            List<String> propertyNames = new ArrayList<>(Arrays.asList("contactName", "genderString", "contactToken",
                    "contactShortToken", "workEmail", "departmentString", "jobPositionString"));
            excelUtils.setNeedSequenceColumn(false);
            //  5.处理导出变量的值并导出
            List<ArchivesContactDTO> contacts = response.getContacts().stream().map(r -> {
                convertArchivesContactForExcel(r);
                return r;
            }).collect(Collectors.toList());
            taskService.updateTaskProcess(taskId, 60);
            outputStream = excelUtils.getOutputStream(propertyNames, titleNames, cellSizes, contacts);
            taskService.updateTaskProcess(taskId, 90);
        }
        return outputStream;
    }

    private void convertArchivesContactForExcel(ArchivesContactDTO dto) {
        //  转化以使用ExcelUtil进行导出
        //  性别转化
        dto.setGenderString(ArchivesUtil.resolveArchivesEnum(dto.getGender(), ArchivesParameter.GENDER));
        //  部门转化
        if (dto.getDepartments() != null && dto.getDepartments().size() > 0)
            dto.setDepartmentString(convertToOrgNames(dto.getDepartments().stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName))));
        //  岗位的导出
        if (dto.getJobPositions() != null && dto.getJobPositions().size() > 0)
            dto.setJobPositionString(convertToOrgNames(dto.getJobPositions().stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName))));
    }

    @Override
    public void verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd) {
        if (StringUtils.isEmpty(cmd.getPassword())) {
            LOGGER.error("Password is null ");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Invalid password");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        User user = UserContext.current().getUser();
        if (user == null) {
            LOGGER.error("Unable to find owner user,  namespaceId={}", namespaceId);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User does not exist");
        }

        if (UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE)
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED, "User account has not been activated yet");

        if (!EncryptionUtils.validateHashPassword(cmd.getPassword(), user.getSalt(), user.getPasswordHash())) {
            LOGGER.error("Password does not match for " + user.getIdentifierToken());
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Invalid password");
        }
    }

    @Override
    public ImportFileResponse<ImportArchivesContactsDTO> getImportContactsResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    @Override
    public void exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
        importFileService.exportImportFileFailResultXls(httpResponse, cmd.getTaskId());
    }

    /**
     * 获取员工在企业的真实名称
     */
    private String getEmployeeRealName(Long userId, Long organizationId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        if (userIdentifier == null)
            return "管理员";
        String contactToken = userIdentifier.getIdentifierToken();
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndToken(contactToken, organizationId);
        if (member == null)
            return "管理员" + contactToken;
        else
            return member.getContactName();
    }

    /**
     * 获取员工的部门
     */
    @Override
    public Map<Long, String> getEmployeeDepartment(Long detailId) {
        if (detailId != null) {
            OrganizationMember member = organizationProvider.findMemberDepartmentByDetailId(detailId);
            if (member != null) {
                return convertToOrganizationMap(Collections.singletonList(member));
            }
        }
        return null;
    }

    /**
     * 获取员工的职位
     */
    @Override
    public Map<Long, String> getEmployeeJobPosition(Long detailId) {
        if (detailId != null) {
            List<OrganizationMember> members = organizationProvider.findMemberJobPositionByDetailId(detailId);
            if (members != null) {
                return convertToOrganizationMap(members);
            }
        }
        return null;
    }

    /**
     * 获取员工的职级
     */
    @Override
    public Map<Long, String> getEmployeeJobLevel(Long detailId) {
        if (detailId != null) {
            OrganizationMember member = organizationProvider.findMemberJobLevelByDetailId(detailId);
            if (member != null) {
                return convertToOrganizationMap(Collections.singletonList(member));
            }
        }
        return null;
    }

    private Map<Long, String> convertToOrganizationMap(List<OrganizationMember> members) {
        Map<Long, String> map = new HashMap<>();
        for (OrganizationMember member : members) {
            Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());
            if (organization == null)
                return null;
            map.put(organization.getId(), organization.getName());
        }
        return map;
    }

    @Override
    public String convertToOrgNames(Map<Long, String> map) {
        StringBuilder names = new StringBuilder();
        if (map != null && map.size() > 0) {
            for (String value : map.values())
                names.append(value).append(",");
            //  remove the comma
            names = new StringBuilder(names.substring(0, names.length() - 1));
        }
        return names.toString();
    }

    @Override
    public List<Long> convertToOrgIds(Map<Long, String> map) {
        List<Long> ids = new ArrayList<>();
        if (map != null && map.size() > 0)
            ids.addAll(map.keySet());
        return ids;
    }

    private String getOrgNamesByIds(List<Long> orgIds) {
        StringBuilder names = new StringBuilder();
        if (orgIds != null && orgIds.size() > 0) {
            for (Long orgId : orgIds) {
                Organization org = organizationProvider.findOrganizationById(orgId);
                if (org != null)
                    names.append(org.getName()).append(",");
            }
            if (names.length() > 0)
                names = new StringBuilder(names.subSequence(0, names.length() - 1));
        }
        return names.toString();
    }

    @Override
    public ArchivesEmployeeDTO addArchivesEmployee(AddArchivesEmployeeCommand cmd) {

        ArchivesEmployeeDTO dto = new ArchivesEmployeeDTO();

        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setGender(cmd.getGender());
        if (cmd.getDepartmentIds() != null)
            addCommand.setDepartmentIds(new ArrayList<>(cmd.getDepartmentIds()));
        if (cmd.getJobPositionIds() != null)
            addCommand.setJobPositionIds(new ArrayList<>(cmd.getJobPositionIds()));
        if (cmd.getJobLevelIds() != null)
            addCommand.setJobLevelIds(new ArrayList<>(cmd.getJobLevelIds()));
        addCommand.setContactToken(cmd.getContactToken());
        addCommand.setVisibleFlag(VisibleFlag.SHOW.getCode());
        //  1.添加人员到组织架构
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  2.获得 detailId 然后处理其它信息
        Long detailId = null;
        if (memberDTO != null)
            detailId = memberDTO.getDetailId();
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (employee == null)
            return null;
        employee.setEmployeeType(cmd.getEmployeeType());
        employee.setEmployeeStatus(cmd.getEmployeeStatus());
        employee.setEmployeeNo(cmd.getEmployeeNo());
        employee.setEnName(cmd.getEnName());
        employee.setContactShortToken(cmd.getContactShortToken());
        employee.setWorkEmail(cmd.getWorkEmail());
        employee.setRegionCode(cmd.getRegionCode());
        if (cmd.getCheckInTime() != null)
            employee.setCheckInTime(ArchivesUtil.parseDate(cmd.getCheckInTime()));
        else
            employee.setCheckInTime(ArchivesUtil.currentDate());
        employee.setCheckInTimeIndex(ArchivesUtil.getMonthAndDay(employee.getCheckInTime()));
        if (cmd.getEmploymentTime() == null)
            employee.setEmploymentTime(ArchivesUtil.parseDate(cmd.getCheckInTime()));
        else {
            //  若转正日期不为当前时间则需做配置
            employee.setEmploymentTime(ArchivesUtil.parseDate(cmd.getEmploymentTime()));
            if (ArchivesUtil.parseDate(cmd.getEmploymentTime()).after(ArchivesUtil.parseDate(cmd.getCheckInTime()))) {
                EmployArchivesEmployeesCommand employConfigCommand = new EmployArchivesEmployeesCommand();
                employConfigCommand.setDetailIds(Collections.singletonList(employee.getId()));
                employConfigCommand.setOrganizationId(employee.getOrganizationId());
                employConfigCommand.setEmploymentTime(cmd.getEmploymentTime());
                employConfigCommand.setEmploymentEvaluation("");
                employArchivesEmployeesConfig(employConfigCommand);
            }
        }
        if (cmd.getContractPartyId() != null)
            employee.setContractPartyId(cmd.getContractPartyId());
        else
            employee.setContractPartyId(cmd.getOrganizationId());
        organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());

        //  3.查询若存在于离职列表则删除
        deleteArchivesDismissEmployees(detailId, cmd.getOrganizationId());

        //  4.增加入职记录
        addCheckInLogs(detailId, cmd);

        dto.setDetailId(employee.getId());
        dto.setContactName(employee.getContactName());
        dto.setContactToken(employee.getContactToken());
        return dto;
    }

    @Override
    public void updateArchivesEmployee(UpdateArchivesEmployeeCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            //  1.更新 detail 表信息
            OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
            employee = convertToEmployeeDetail(employee, cmd.getValues());
            organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());

            //  2.更新 member 表信息
            organizationService.updateOrganizationMemberInfoByDetailId(employee.getId(), employee.getContactToken(), employee.getContactName(), employee.getGender());

            //  3.更新自定义字段值，人事档案单独的表单值处理
            List<PostApprovalFormItem> dynamicItems = cmd.getValues().stream().filter(r -> !GeneralFormFieldAttribute.DEFAULT.getCode().equals(r.getFieldAttribute())).collect(Collectors.toList());
            addGeneralFormValuesForArchives(getRealFormOriginId(cmd.getFormOriginId()), employee, dynamicItems);

            return null;
        });
    }

    /**
     * 为人事档案单独做一个表单值的更新处理
     */
    private void addGeneralFormValuesForArchives(Long formOriginId, OrganizationMemberDetails employee, List<PostApprovalFormItem> dynamicItems) {
        if (null != dynamicItems) {
            GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(formOriginId);

            for (PostApprovalFormItem val : dynamicItems) {

                GeneralFormVal obj = generalFormValProvider.getGeneralFormValBySourceIdAndName(employee.getId(), GeneralFormSourceType.ARCHIVES_AUTH.getCode(), val.getFieldName());
                if (obj == null) {
                    obj = new GeneralFormVal();
                    //与表单信息一致
                    obj.setNamespaceId(form.getNamespaceId());
                    obj.setOrganizationId(form.getOrganizationId());
                    obj.setOwnerId(form.getOwnerId());
                    obj.setOwnerType(form.getOwnerType());
                    obj.setModuleId(form.getModuleId());
                    obj.setModuleType(form.getModuleType());
                    obj.setFormOriginId(form.getFormOriginId());
                    obj.setFormVersion(form.getFormVersion());

                    obj.setSourceType(GeneralFormSourceType.ARCHIVES_AUTH.getCode());
                    obj.setSourceId(employee.getId());
                    obj.setFieldName(val.getFieldName());
                    obj.setFieldType(val.getFieldType());
                    obj.setFieldValue(val.getFieldValue());
                    generalFormValProvider.createGeneralFormVal(obj);
                } else {
                    obj.setFieldValue(val.getFieldValue());
                    generalFormValProvider.updateGeneralFormVal(obj);
                }
            }
        }
    }

    @Override
    public void updateArchivesEmployeeAvatar(UpdateArchivesEmployeeCommand cmd) {
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        if (employee != null) {
            employee.setAvatar(cmd.getAvatar());
            organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());
        }
    }

    @Override
    public GetArchivesEmployeeResponse getArchivesEmployee(GetArchivesEmployeeCommand cmd) {

        GetArchivesEmployeeResponse response = new GetArchivesEmployeeResponse();

        //  1.获取表单所有字段
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  2.获取表单对应的值
        GetGeneralFormValuesCommand valueCommand =
                new GetGeneralFormValuesCommand(GeneralFormSourceType.ARCHIVES_AUTH.getCode(), cmd.getDetailId(), NormalFlag.NEED.getCode());
        List<PostApprovalFormItem> employeeDynamicVal = generalFormService.getGeneralFormValues(valueCommand);
        Map<String, String> employeeDynamicMaps = handleEmployeeDynamicVal(employeeDynamicVal);

        //  3.获取个人信息的值
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        Map<String, String> employeeDefaultMaps = handleEmployeeDefaultVal(employee);

        //  4.赋值

        //  4-1.处理部门.岗位.职级字段
        processEmployeeOrganization(employeeDefaultMaps, employee);
        for (GeneralFormFieldDTO dto : form.getFormFields()) {
            //  4-2.赋值给系统默认字段
            if (GeneralFormFieldAttribute.DEFAULT.getCode().equals(dto.getFieldAttribute())) {
                dto.setFieldValue(employeeDefaultMaps.get(dto.getFieldName()));
            }
            //  4-3.赋值给非系统默认字段
            else {
                dto.setFieldValue(employeeDynamicMaps.get(dto.getFieldName()));
            }
        }

        //  4-4.员工头像赋值
        response.setAvatar(contentServerService.parserUri(employee.getAvatar()));

        //  4-5.员工状态赋值
        response.setEmployeeCase(getArchivesEmployeeCase(employee));

        //  5.获取档案记录
        if (cmd.getIsExport() == null || cmd.getIsExport() != 1) {
            response.setLogs(listArchivesLogs(employee.getOrganizationId(), employee.getId()));
        }

        //  摒弃冗余字段
        //  由于业务的特殊性，此处的 formOriginId 由另外的接口去提供
        //  故屏蔽掉此处的返回以免造成误解
        form.setTemplateText(null);
        form.setFormOriginId(null);
        form.setFormVersion(null);
        response.setForm(form);
        return response;
    }

    private void processEmployeeOrganization(Map<String, String> valueMap, OrganizationMemberDetails employee) {
        //  process the department. jobPosition. jobLevel
        if (employee.getEmployeeStatus().equals(EmployeeStatus.DISMISSAL.getCode())) {
            ArchivesDismissEmployees dismissEmployee = archivesProvider.getArchivesDismissEmployeesByDetailId(employee.getId());
            if (dismissEmployee != null) {
                valueMap.put(ArchivesParameter.DEPARTMENT, dismissEmployee.getDepartment());
                valueMap.put(ArchivesParameter.JOB_POSITION, dismissEmployee.getJobPosition());
                valueMap.put(ArchivesParameter.JOB_LEVEL, dismissEmployee.getJobLevel());
            }
        } else {
            Map<Long, String> department = getEmployeeDepartment(employee.getId());
            Map<Long, String> jobPosition = getEmployeeJobPosition(employee.getId());
            Map<Long, String> jobLevel = getEmployeeJobLevel(employee.getId());

            valueMap.put(ArchivesParameter.DEPARTMENT, convertToOrgNames(department));
            valueMap.put(ArchivesParameter.JOB_POSITION, convertToOrgNames(jobPosition));
            valueMap.put(ArchivesParameter.JOB_LEVEL, convertToOrgNames(jobLevel));
        }
    }

    private String getArchivesEmployeeCase(OrganizationMemberDetails employee) {
        String employeeCase = "";
        Map<String, Object> map = new LinkedHashMap<>();
        switch (EmployeeStatus.fromCode(employee.getEmployeeStatus())) {
            case PROBATION:
                map.put("firstDate", employee.getCheckInTime() != null ? format.format(employee.getCheckInTime()) : "  无");
                map.put("nextDate", employee.getEmploymentTime() != null ? format.format(employee.getEmploymentTime()) : "  无");
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                        ArchivesLocaleTemplateCode.ARCHIVES_PROBATION_CASE, "zh_CN", map, "");
                break;
            case DISMISSAL:
                map.put("firstDate", employee.getCheckInTime() != null ? format.format(employee.getCheckInTime()) : "  无");
                map.put("nextDate", employee.getDismissTime() != null ? format.format(employee.getDismissTime()) : "  无");
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                        ArchivesLocaleTemplateCode.ARCHIVES_DISMISS_CASE, "zh_CN", map, "");
                break;
            default:
                map.put("firstDate", employee.getCheckInTime() != null ? format.format(employee.getCheckInTime()) : "  无");
                map.put("nextDate", employee.getContractEndTime() != null ? format.format(employee.getContractEndTime()) : "   无");
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                        ArchivesLocaleTemplateCode.ARCHIVES_ON_THE_JOB_CASE, "zh_CN", map, "");
                break;
        }
        return employeeCase;
    }

    @Override
    public ListArchivesEmployeesResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd) {

        ListArchivesEmployeesResponse response = new ListArchivesEmployeesResponse();

        ListOrganizationContactCommand orgCommand = new ListOrganizationContactCommand();
        orgCommand.setOrganizationId(cmd.getOrganizationId());
        orgCommand.setCheckInTimeStart(ArchivesUtil.parseDate(cmd.getCheckInTimeStart()));
        orgCommand.setCheckInTimeEnd(ArchivesUtil.parseDate(cmd.getCheckInTimeEnd()));
        orgCommand.setEmploymentTimeStart(ArchivesUtil.parseDate(cmd.getEmploymentTimeStart()));
        orgCommand.setEmploymentTimeEnd(ArchivesUtil.parseDate(cmd.getEmploymentTimeEnd()));
        orgCommand.setContractEndTimeStart(ArchivesUtil.parseDate(cmd.getContractTimeStart()));
        orgCommand.setContractEndTimeEnd(ArchivesUtil.parseDate(cmd.getContractTimeEnd()));
        orgCommand.setEmployeeStatus(cmd.getEmployeeStatus());
        orgCommand.setContractPartyId(cmd.getContractPartyId());
        orgCommand.setKeywords(cmd.getKeywords());
        if (cmd.getDepartmentId() != null) {
            orgCommand.setOrganizationId(cmd.getDepartmentId());
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
            orgCommand.setTargetTypes(groupTypes);
        }
        orgCommand.setPageAnchor(cmd.getPageAnchor());
        if (cmd.getPageSize() != null)
            orgCommand.setPageSize(cmd.getPageSize());
        else
            orgCommand.setPageSize(20);
        orgCommand.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CHILD_ENTERPRISE.getCode()));

        //modified by lei.lv
        orgCommand.setVisibleFlag(VisibleFlag.ALL.getCode());

        ListOrganizationMemberCommandResponse members = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);

        if (members.getMembers() != null && members.getMembers().size() > 0) {
            response.setArchivesEmployees(members.getMembers().stream().map(r -> {
                ArchivesEmployeeDTO dto = new ArchivesEmployeeDTO();
                dto.setDetailId(r.getDetailId());
                dto.setTargetId(r.getTargetId());
                dto.setTargetType(r.getTargetType());
                dto.setContactName(r.getContactName());
                dto.setEmployeeStatus(r.getEmployeeStatus());
                dto.setDepartments(r.getDepartments());
                dto.setContactToken(r.getContactToken());
                dto.setRegionCode(r.getRegionCode());
                dto.setWorkEmail(r.getWorkEmail());
                dto.setCheckInTime(r.getCheckInTime());
                dto.setEmploymentTime(r.getEmploymentTime());
                dto.setContractTime(r.getContractEndTime());
                dto.setEmployeeNo(r.getEmployeeNo());
                return dto;
            }).collect(Collectors.toList()));
        }
        response.setNextPageAnchor(members.getNextPageAnchor());
        return response;
    }

    /**
     * 增加入职记录
     */
    private void addCheckInLogs(Long detailId, AddArchivesEmployeeCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        AddArchivesLogCommand command = new AddArchivesLogCommand();
        command.setNamespaceId(namespaceId);
        command.setDetailId(detailId);
        command.setOrganizationId(cmd.getOrganizationId());
        command.setOperationType(ArchivesOperationType.CHECK_IN.getCode());
        command.setOperationTime(cmd.getCheckInTime());
        command.setStr1(getOrgNamesByIds(cmd.getDepartmentIds()));
        command.setStr2(ArchivesUtil.resolveArchivesEnum(cmd.getEmployeeStatus(), ArchivesParameter.EMPLOYEE_STATUS));
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("month", cmd.getMonth());
        command.setStr3(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                ArchivesLocaleTemplateCode.OPERATION_PROBATION_PERIOD, "zh_CN", map, ""));
        command.setOperatorUid(userId);
        command.setOperatorName(getEmployeeRealName(userId, cmd.getOrganizationId()));
        addArchivesLog(command);
    }

    /**
     * 增加转正记录
     */
    private void addEmployLogs(EmployArchivesEmployeesCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                AddArchivesLogCommand command = new AddArchivesLogCommand();
                command.setNamespaceId(namespaceId);
                command.setDetailId(detailId);
                command.setOrganizationId(cmd.getOrganizationId());
                command.setOperationType(ArchivesOperationType.EMPLOY.getCode());
                command.setOperationTime(cmd.getEmploymentTime());
                command.setStr1(cmd.getEmploymentEvaluation());
                command.setOperatorUid(userId);
                command.setOperatorName(getEmployeeRealName(userId, cmd.getOrganizationId()));
                addArchivesLog(command);
            }
        }
    }

    /**
     * 增加调整记录
     */
    private void addTransferLogs(TransferArchivesEmployeesCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                AddArchivesLogCommand command = new AddArchivesLogCommand();
                command.setNamespaceId(namespaceId);
                command.setDetailId(detailId);
                command.setOrganizationId(cmd.getOrganizationId());
                command.setOperationType(ArchivesOperationType.TRANSFER.getCode());
                command.setOperationTime(cmd.getEffectiveTime());
                Map<String, Object> map = new HashMap<>();
                if (cmd.getDepartmentIds() != null && cmd.getDepartmentIds().size() > 0) {
                    map.put("oldOrgNames", convertToOrgNames(getEmployeeDepartment(detailId)));
                    map.put("newOrgNames", getOrgNamesByIds(cmd.getDepartmentIds()));
                    command.setStr1(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                            ArchivesLocaleTemplateCode.OPERATION_ORG_CHANGE, "zh_CN", map, ""));
                }
                if (cmd.getJobPositionIds() != null && cmd.getJobPositionIds().size() > 0) {
                    map.put("oldOrgNames", convertToOrgNames(getEmployeeJobPosition(detailId)));
                    map.put("newOrgNames", getOrgNamesByIds(cmd.getJobPositionIds()));
                    command.setStr2(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                            ArchivesLocaleTemplateCode.OPERATION_ORG_CHANGE, "zh_CN", map, ""));
                }
                if (cmd.getJobLevelIds() != null && cmd.getJobLevelIds().size() > 0) {
                    map.put("oldOrgNames", convertToOrgNames(getEmployeeJobLevel(detailId)));
                    map.put("newOrgNames", getOrgNamesByIds(cmd.getJobLevelIds()));
                    command.setStr3(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                            ArchivesLocaleTemplateCode.OPERATION_ORG_CHANGE, "zh_CN", map, ""));
                }
                command.setStr4(ArchivesUtil.resolveArchivesEnum(cmd.getTransferType(), ArchivesParameter.TRANSFER_TYPE));
                command.setStr5(cmd.getTransferReason());
                command.setOperatorUid(userId);
                command.setOperatorName(getEmployeeRealName(userId, cmd.getOrganizationId()));
                addArchivesLog(command);
            }
        }
    }

    /**
     * 增加离职记录
     */
    private void addDismissLogs(DismissArchivesEmployeesCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                AddArchivesLogCommand command = new AddArchivesLogCommand();
                command.setNamespaceId(namespaceId);
                command.setDetailId(detailId);
                command.setOrganizationId(cmd.getOrganizationId());
                command.setOperationType(ArchivesOperationType.DISMISS.getCode());
                command.setOperationTime(cmd.getDismissTime());
                command.setStr1(ArchivesUtil.resolveArchivesEnum(cmd.getDismissType(), ArchivesParameter.DISMISS_TYPE));
                command.setStr2(ArchivesUtil.resolveArchivesEnum(cmd.getDismissReason(), ArchivesParameter.DISMISS_REASON));
                command.setStr3(cmd.getDismissRemark());
                command.setOperatorUid(userId);
                command.setOperatorName(getEmployeeRealName(userId, cmd.getOrganizationId()));
                command.setNamespaceId(namespaceId);
                command.setNamespaceId(namespaceId);
                addArchivesLog(command);
            }
        }
    }

    @Override
    public void addArchivesLog(AddArchivesLogCommand cmd) {
        ArchivesOperationalLog log = new ArchivesOperationalLog();
        log.setNamespaceId(cmd.getNamespaceId());
        log.setDetailId(cmd.getDetailId());
        log.setOrganizationId(cmd.getOrganizationId());
        log.setOperationType(cmd.getOperationType());
        log.setOperationTime(ArchivesUtil.parseDate(cmd.getOperationTime()));
        log.setStringTag1(cmd.getStr1());
        log.setStringTag2(cmd.getStr2());
        log.setStringTag3(cmd.getStr3());
        log.setStringTag4(cmd.getStr4());
        log.setStringTag5(cmd.getStr5());
        log.setStringTag6(cmd.getStr6());
        log.setOperatorUid(cmd.getOperatorUid());
        log.setOperatorName(cmd.getOperatorName());
        archivesProvider.createOperationalLog(log);
    }


    private List<ArchivesLogDTO> listArchivesLogs(Long organizationId, Long detailId) {
        List<ArchivesLogDTO> results = new ArrayList<>();
        List<ArchivesOperationalLog> logs = archivesProvider.listArchivesLogs(organizationId, detailId);
        if (logs.size() > 0) {
            results = logs.stream().map(r -> {
                ArchivesLogDTO dto = ConvertHelper.convert(r, ArchivesLogDTO.class);
                processArchivesLog(dto, r);
                return dto;
            }).collect(Collectors.toList());
        }
        return results;
    }

    private void processArchivesLog(ArchivesLogDTO dto, ArchivesOperationalLog log) {
        Map<String, String> map = new HashMap<>();
        switch (ArchivesOperationType.fromCode(log.getOperationType())) {
            case CHECK_IN:
                map.put(ArchivesParameter.DEPARTMENT, log.getStringTag1());
                map.put(ArchivesParameter.EMPLOYEE_STATUS, log.getStringTag2());
                map.put(ArchivesParameter.MONTH, log.getStringTag3());
                break;
            case EMPLOY:
                map.put(ArchivesParameter.EMPLOYMENT_REMARK, log.getStringTag1());
                break;
            case SELF_EMPLOY:
                map.put(ArchivesParameter.EMPLOYMENT_REASON, log.getStringTag1());
                break;
            case TRANSFER:
                if (log.getStringTag1() != null)
                    map.put(ArchivesParameter.DEPARTMENT, log.getStringTag1());
                if (log.getStringTag2() != null)
                    map.put(ArchivesParameter.JOB_POSITION, log.getStringTag2());
                if (log.getStringTag3() != null)
                    map.put(ArchivesParameter.JOB_LEVEL, log.getStringTag3());
                map.put(ArchivesParameter.TRANSFER_TYPE, log.getStringTag4());
                map.put(ArchivesParameter.TRANSFER_REMARK, log.getStringTag5());
                break;
            case DISMISS:
            case SELF_DISMISS:
                map.put(ArchivesParameter.DISMISS_TYPE, log.getStringTag1());
                map.put(ArchivesParameter.DISMISS_REASON, log.getStringTag2());
                map.put(ArchivesParameter.DISMISS_REMARK, log.getStringTag3());
                break;
        }
        dto.setLogs(map);
    }

    /********************    assistant function for Archives start    ********************/
    /**
     * 解析系统字段，便于存入档案表
     */
    private OrganizationMemberDetails convertToEmployeeDetail(OrganizationMemberDetails employee, List<PostApprovalFormItem> itemValues) {
        for (PostApprovalFormItem itemValue : itemValues) {
            if (!GeneralFormFieldAttribute.DEFAULT.getCode().equals(itemValue.getFieldAttribute()))
                continue;
            else {
                switch (itemValue.getFieldName()) {
                    case ArchivesParameter.BIRTHDAY:
                        employee.setBirthday(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        employee.setBirthdayIndex(ArchivesUtil.getMonthAndDay(employee.getBirthday()));
                        break;
                    case ArchivesParameter.CONTACT_NAME:
                        employee.setContactName(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.CONTACT_TOKEN:
                        employee.setContactToken(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.REGION_CODE:
                        employee.setRegionCode(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.EMPLOYEE_NO:
                        employee.setEmployeeNo(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.GENDER:
                        employee.setGender(ArchivesUtil.convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.GENDER));
                        break;
                    case ArchivesParameter.MARITAL_FLAG:
                        employee.setMaritalFlag(ArchivesUtil.convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.MARITAL_FLAG));
                        break;
                    case ArchivesParameter.POLITICAL_FLAG:
                        employee.setPoliticalFlag(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.NATIVE_PLACE:
                        employee.setNativePlace(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.EN_NAME:
                        employee.setEnName(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.REG_RESIDENCE:
                        employee.setRegResidence(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ID_NUMBER:
                        employee.setIdNumber(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.EMAIL:
                        employee.setEmail(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.WECHAT:
                        employee.setWechat(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.QQ:
                        employee.setQq(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.EMERGENCY_NAME:
                        employee.setEmergencyName(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.EMERGENCY_CONTACT:
                        employee.setEmergencyContact(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ADDRESS:
                        employee.setAddress(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.EMPLOYEE_TYPE:
                        employee.setEmployeeType(ArchivesUtil.convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.EMPLOYEE_TYPE));
                        break;
                    case ArchivesParameter.EMPLOYEE_STATUS:
                        employee.setEmployeeStatus(ArchivesUtil.convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.EMPLOYEE_STATUS));
                        break;
                    case ArchivesParameter.EMPLOYMENT_TIME:
                        employee.setEmploymentTime(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        break;
                    case ArchivesParameter.SALARY_CARD_NUMBER:
                        employee.setSalaryCardNumber(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.SOCIAL_SECURITY_NUMBER:
                        employee.setSocialSecurityNumber(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.PROVIDENT_FUND_NUMBER:
                        employee.setProvidentFundNumber(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.CHECK_IN_TIME:
                        employee.setCheckInTime(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        employee.setCheckInTimeIndex(ArchivesUtil.getMonthAndDay(employee.getCheckInTime()));
                        break;
                    case ArchivesParameter.PROCREATIVE:
                        employee.setProcreative(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ETHNICITY:
                        employee.setEthnicity(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ID_TYPE:
                        employee.setIdType(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ID_EXPIRY_DATE:
                        employee.setIdExpiryDate(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        break;
                    case ArchivesParameter.DEGREE:
                        employee.setDegree(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.GRADUATION_SCHOOL:
                        employee.setGraduationSchool(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.GRADUATION_TIME:
                        employee.setGraduationTime(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        break;
                    case ArchivesParameter.EMERGENCY_RELATIONSHIP:
                        employee.setEmergencyRelationship(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.CONTACT_SHORT_TOKEN:
                        employee.setContactShortToken(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.WORK_EMAIL:
                        employee.setWorkEmail(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.WORK_START_TIME:
                        employee.setWorkStartTime(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        break;
                    case ArchivesParameter.CONTRACT_START_TIME:
                        employee.setContractStartTime(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        break;
                    case ArchivesParameter.CONTRACT_END_TIME:
                        employee.setContractEndTime(ArchivesUtil.parseDate(itemValue.getFieldValue()));
                        break;
                    case ArchivesParameter.SALARY_CARD_BANK:
                        employee.setSalaryCardBank(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.REG_RESIDENCE_TYPE:
                        employee.setRegResidenceType(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ID_PHOTO:
                        employee.setIdPhoto(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.VISA_PHOTO:
                        employee.setVisaPhoto(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.LIFE_PHOTO:
                        employee.setLifePhoto(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.ENTRY_FORM:
                        employee.setEntryForm(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.GRADUATION_CERTIFICATE:
                        employee.setGraduationCertificate(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.DEGREE_CERTIFICATE:
                        employee.setDegreeCertificate(itemValue.getFieldValue());
                        break;
                    case ArchivesParameter.CONTRACT_CERTIFICATE:
                        employee.setContractCertificate(itemValue.getFieldValue());
                        break;
                }
            }
        }
        return employee;
    }

    /**
     * 系统字段赋值，利用 map 设置 key 来存取值
     */
    private Map<String, String> handleEmployeeDefaultVal(OrganizationMemberDetails employee) {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put(ArchivesParameter.CONTACT_NAME, employee.getContactName());
        valueMap.put(ArchivesParameter.EN_NAME, employee.getEnName());
        valueMap.put(ArchivesParameter.GENDER, ArchivesUtil.resolveArchivesEnum(employee.getGender(), ArchivesParameter.GENDER));
        valueMap.put(ArchivesParameter.MARITAL_FLAG, ArchivesUtil.resolveArchivesEnum(employee.getMaritalFlag(), ArchivesParameter.MARITAL_FLAG));
        valueMap.put(ArchivesParameter.ETHNICITY, employee.getEthnicity());
        valueMap.put(ArchivesParameter.POLITICAL_FLAG, employee.getPoliticalFlag());
        valueMap.put(ArchivesParameter.NATIVE_PLACE, employee.getNativePlace());
        valueMap.put(ArchivesParameter.ID_TYPE, employee.getIdType());
        valueMap.put(ArchivesParameter.ID_NUMBER, employee.getIdNumber());
        valueMap.put(ArchivesParameter.DEGREE, employee.getDegree());
        valueMap.put(ArchivesParameter.GRADUATION_SCHOOL, employee.getGraduationSchool());
        valueMap.put(ArchivesParameter.CONTACT_TOKEN, employee.getContactToken());
        valueMap.put(ArchivesParameter.EMAIL, employee.getEmail());
        valueMap.put(ArchivesParameter.WECHAT, employee.getWechat());
        valueMap.put(ArchivesParameter.QQ, employee.getQq());
        valueMap.put(ArchivesParameter.ADDRESS, employee.getAddress());
        valueMap.put(ArchivesParameter.EMERGENCY_NAME, employee.getEmergencyName());
        valueMap.put(ArchivesParameter.EMERGENCY_RELATIONSHIP, employee.getEmergencyRelationship());
        valueMap.put(ArchivesParameter.EMERGENCY_CONTACT, employee.getEmergencyContact());
        valueMap.put(ArchivesParameter.EMPLOYEE_TYPE, ArchivesUtil.resolveArchivesEnum(employee.getEmployeeType(), ArchivesParameter.EMPLOYEE_TYPE));
        valueMap.put(ArchivesParameter.EMPLOYEE_STATUS, ArchivesUtil.resolveArchivesEnum(employee.getEmployeeStatus(), ArchivesParameter.EMPLOYEE_STATUS));
        valueMap.put(ArchivesParameter.EMPLOYMENT_TIME, String.valueOf(employee.getEmploymentTime()));
        valueMap.put(ArchivesParameter.EMPLOYEE_NO, employee.getEmployeeNo());
        valueMap.put(ArchivesParameter.CONTACT_SHORT_TOKEN, employee.getContactShortToken());
        valueMap.put(ArchivesParameter.WORK_EMAIL, employee.getWorkEmail());
        valueMap.put(ArchivesParameter.CONTRACT_PARTY_ID, getOrgNamesByIds(Collections.singletonList(employee.getContractPartyId())));
        valueMap.put(ArchivesParameter.SALARY_CARD_NUMBER, employee.getSalaryCardNumber());
        valueMap.put(ArchivesParameter.SALARY_CARD_BANK, employee.getSalaryCardBank());
        valueMap.put(ArchivesParameter.SOCIAL_SECURITY_NUMBER, employee.getSocialSecurityNumber());
        valueMap.put(ArchivesParameter.PROVIDENT_FUND_NUMBER, employee.getProvidentFundNumber());
        valueMap.put(ArchivesParameter.REG_RESIDENCE_TYPE, employee.getRegResidenceType());
        valueMap.put(ArchivesParameter.REG_RESIDENCE, employee.getRegResidence());
        if (employee.getBirthday() != null)
            valueMap.put(ArchivesParameter.BIRTHDAY, String.valueOf(employee.getBirthday()));
        if (employee.getProcreative() != null)
            valueMap.put(ArchivesParameter.PROCREATIVE, employee.getProcreative());
        if (employee.getIdExpiryDate() != null)
            valueMap.put(ArchivesParameter.ID_EXPIRY_DATE, String.valueOf(employee.getIdExpiryDate()));
        if (employee.getGraduationTime() != null)
            valueMap.put(ArchivesParameter.GRADUATION_TIME, String.valueOf(employee.getGraduationTime()));
        if (employee.getCheckInTime() != null)
            valueMap.put(ArchivesParameter.CHECK_IN_TIME, String.valueOf(employee.getCheckInTime()));
        if (employee.getWorkStartTime() != null)
            valueMap.put(ArchivesParameter.WORK_START_TIME, String.valueOf(employee.getWorkStartTime()));
        if (employee.getContractStartTime() != null)
            valueMap.put(ArchivesParameter.CONTRACT_START_TIME, String.valueOf(employee.getContractStartTime()));
        if (employee.getContractEndTime() != null)
            valueMap.put(ArchivesParameter.CONTRACT_END_TIME, String.valueOf(employee.getContractEndTime()));
        return valueMap;
    }

    /**
     * 给用户自定义字段赋值，利用 map 设置 key 来存取值
     */
    private Map<String, String> handleEmployeeDynamicVal(List<PostApprovalFormItem> employeeDynamicVals) {
        Map<String, String> valueMap = new HashMap<>();
        if (employeeDynamicVals != null && employeeDynamicVals.size() > 0) {
            for (PostApprovalFormItem value : employeeDynamicVals) {
                valueMap.put(value.getFieldName(), value.getFieldValue());
            }
        }
        return valueMap;
    }
    /********************    assistant function for Archives end    ********************/

    /**
     * 员工转正(配置)
     */
    @Override
    public void employArchivesEmployeesConfig(EmployArchivesEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            //  判断日期决定是否立即执行
            if (!ArchivesUtil.parseDate(cmd.getEmploymentTime()).after(ArchivesUtil.currentDate())) {
                //  1-1.删除可能存在的配置
                archivesProvider.deleteLastConfiguration(namespaceId, cmd.getDetailIds(), ArchivesOperationType.EMPLOY.getCode());
                //  1-2.执行操作
                employArchivesEmployees(cmd);
            } else {
                for (Long detailId : cmd.getDetailIds()) {
                    //  2-1.更新档案中的转正时间
                    OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                    detail.setEmploymentTime(ArchivesUtil.parseDate(cmd.getEmploymentTime()));
                    organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());

                    //  2-2.添加定时配置
                    createArchivesOperation(
                            namespaceId,
                            cmd.getOrganizationId(),
                            detailId,
                            ArchivesOperationType.EMPLOY.getCode(),
                            ArchivesUtil.parseDate(cmd.getEmploymentTime()),
                            StringHelper.toJsonString(cmd));
                }
            }
            //  3.添加文档
            if (TrueOrFalseFlag.fromCode(cmd.getLogFlag()) != TrueOrFalseFlag.FALSE)
                addEmployLogs(cmd);
            return null;
        });
    }

    private void employArchivesEmployees(EmployArchivesEmployeesCommand cmd) {
        for (Long detailId : cmd.getDetailIds()) {
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            detail.setEmployeeStatus(EmployeeStatus.ON_THE_JOB.getCode());
            detail.setEmploymentTime(ArchivesUtil.parseDate(cmd.getEmploymentTime()));
            organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
        }
    }

    /**
     * 员工调动(配置)
     */
    @Override
    public void transferArchivesEmployeesConfig(TransferArchivesEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            //  先生成记录后执行用以保存前部门
            addTransferLogs(cmd);
            if (cmd.getEffectiveTime().equals(ArchivesUtil.currentDate().toString())) {
                archivesProvider.deleteLastConfiguration(namespaceId, cmd.getDetailIds(), ArchivesOperationType.TRANSFER.getCode());
                transferArchivesEmployees(cmd);
            } else {
                for (Long detailId : cmd.getDetailIds()) {
                    createArchivesOperation(
                            namespaceId,
                            cmd.getOrganizationId(),
                            detailId,
                            ArchivesOperationType.TRANSFER.getCode(),
                            ArchivesUtil.parseDate(cmd.getEffectiveTime()),
                            StringHelper.toJsonString(cmd));
                }
            }
            return null;
        });
    }

    private void transferArchivesEmployees(TransferArchivesEmployeesCommand cmd) {
        //  调整员工部门、岗位、职级
        organizationService.transferOrganizationPersonels(cmd);
    }

    /**
     * 员工离职(配置)
     */
    @Override
    public void dismissArchivesEmployeesConfig(DismissArchivesEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            if (!ArchivesUtil.parseDate(cmd.getDismissTime()).after(ArchivesUtil.currentDate())) {
                archivesProvider.deleteLastConfiguration(namespaceId, cmd.getDetailIds(), ArchivesOperationType.DISMISS.getCode());
                dismissArchivesEmployees(cmd);
            } else {
                for (Long detailId : cmd.getDetailIds()) {
                    createArchivesOperation(
                            namespaceId,
                            cmd.getOrganizationId(),
                            detailId,
                            ArchivesOperationType.DISMISS.getCode(),
                            ArchivesUtil.parseDate(cmd.getDismissTime()),
                            StringHelper.toJsonString(cmd));
                }
            }
            if (TrueOrFalseFlag.fromCode(cmd.getLogFlag()) != TrueOrFalseFlag.FALSE)
                addDismissLogs(cmd);
            return null;
        });
    }

    private void dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        for (Long detailId : cmd.getDetailIds()) {
            //  1.将员工添加到离职人员表
            OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            ArchivesDismissEmployees dismissEmployee = processDismissEmployee(employee, cmd);
            archivesProvider.createArchivesDismissEmployee(dismissEmployee);

            //  2.改为离职状态
            employee.setEmployeeStatus(EmployeeStatus.DISMISSAL.getCode());
            employee.setDismissTime(ArchivesUtil.parseDate(cmd.getDismissTime()));
            organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());

            //  3.删除置顶信息
            archivesProvider.deleteArchivesStickyContactsByDetailId(namespaceId, detailId);

            //  4.删除员工权限
            DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
            deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(employee.getOrganizationId());
            deleteOrganizationPersonnelByContactTokenCommand.setContactToken(employee.getContactToken());
            deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
            organizationService.deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);

        }
    }

    private ArchivesDismissEmployees processDismissEmployee(OrganizationMemberDetails employee, DismissArchivesEmployeesCommand cmd) {
        ArchivesDismissEmployees dismissEmployee = new ArchivesDismissEmployees();
        dismissEmployee.setDetailId(employee.getId());
        dismissEmployee.setNamespaceId(employee.getNamespaceId());
        dismissEmployee.setOrganizationId(employee.getOrganizationId());
        dismissEmployee.setContactName(employee.getContactName());
        dismissEmployee.setEmployeeStatus(employee.getEmployeeStatus());
        dismissEmployee.setCheckInTime(employee.getCheckInTime());
        dismissEmployee.setDismissTime(ArchivesUtil.parseDate(cmd.getDismissTime()));
        dismissEmployee.setDismissType(cmd.getDismissType());
        dismissEmployee.setDismissReason(cmd.getDismissReason());
        dismissEmployee.setDismissRemarks(cmd.getDismissRemark());
        dismissEmployee.setContractPartyId(employee.getContractPartyId());

        //  synchronize the department, job position, level info.
        Map<Long, String> department = getEmployeeDepartment(employee.getId());
        dismissEmployee.setDepartment(convertToOrgNames(department));
        List<Long> departmentId = convertToOrgIds(department);
        if (departmentId.size() > 0)
            dismissEmployee.setDepartmentId(departmentId.get(0));
        dismissEmployee.setJobPosition(convertToOrgNames(getEmployeeJobPosition(employee.getId())));
        dismissEmployee.setJobLevel(convertToOrgNames(getEmployeeJobLevel(employee.getId())));
        return dismissEmployee;
    }

    @Override
    public void deleteArchivesEmployees(DeleteArchivesEmployeesCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            for (Long detailId : cmd.getDetailIds()) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                //  1.删除员工权限
                DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
                deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(detail.getOrganizationId());
                deleteOrganizationPersonnelByContactTokenCommand.setContactToken(detail.getContactToken());
                deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
                organizationService.deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);

                //  2.删除员工档案
                organizationProvider.deleteOrganizationMemberDetails(detail);

                //  3.删除离职列表中对应的员工
                deleteArchivesDismissEmployees(detailId, detail.getOrganizationId());

            }
            return null;
        });
    }

    @Override
    public void deleteArchivesDismissEmployees(Long detailId, Long organizationId) {
        ArchivesDismissEmployees dismissEmployee = archivesProvider.getArchivesDismissEmployeesByDetailId(detailId);
        if (dismissEmployee != null)
            archivesProvider.deleteArchivesDismissEmployees(dismissEmployee);
    }

    private void createArchivesOperation(Integer namespaceId, Long organizationId, Long detailId,
                                         Byte operationType, Date date, String cmd) {
        //  1.若有上一次配置则先取消
        ArchivesOperationalConfiguration oldConfig = archivesProvider.findPendingConfigurationByDetailId(namespaceId, detailId, operationType);
        if (oldConfig != null) {
            oldConfig.setStatus(ArchivesOperationStatus.CANCEL.getCode());
            archivesProvider.updateOperationalConfiguration(oldConfig);
        }

        //  2.添加新配置
        ArchivesOperationalConfiguration newConfig = new ArchivesOperationalConfiguration();
        newConfig.setNamespaceId(namespaceId);
        newConfig.setOrganizationId(organizationId);
        newConfig.setDetailId(detailId);
        newConfig.setOperationType(operationType);
        newConfig.setOperationDate(date);
        newConfig.setAdditionalInfo(cmd);
        archivesProvider.createOperationalConfiguration(newConfig);
    }

    //  执行定时配置项
    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void executeArchivesConfiguration() {
        if (scheduleProvider.getRunningFlag() != RunningFlag.TRUE.getCode())
            return;
        List<ArchivesOperationalConfiguration> configurations = archivesProvider.listPendingConfigurations(ArchivesUtil.currentDate());
        if (configurations.size() == 0)
            return;
        coordinationProvider.getNamedLock(CoordinationLocks.ARCHIVES_CONFIGURATION.getCode()).tryEnter(() -> {
            for (ArchivesOperationalConfiguration configuration : configurations) {
                //  1.execute it
                resolveArchivesConfiguration(configuration);
                //  2.update its status
                configuration.setStatus(ArchivesOperationStatus.COMPLETE.getCode());
                archivesProvider.updateOperationalConfiguration(configuration);
            }

        });
    }

    private void resolveArchivesConfiguration(ArchivesOperationalConfiguration configuration) {
        switch (ArchivesOperationType.fromCode(configuration.getOperationType())) {
            case EMPLOY:
                EmployArchivesEmployeesCommand cmd1 = (EmployArchivesEmployeesCommand) StringHelper.fromJsonString(configuration.getAdditionalInfo(), EmployArchivesEmployeesCommand.class);
                cmd1.setDetailIds(Collections.singletonList(configuration.getDetailId()));
                employArchivesEmployees(cmd1);
                break;
            case TRANSFER:
                TransferArchivesEmployeesCommand cmd2 = (TransferArchivesEmployeesCommand) StringHelper.fromJsonString(configuration.getAdditionalInfo(), TransferArchivesEmployeesCommand.class);
                cmd2.setDetailIds(Collections.singletonList(configuration.getDetailId()));
                transferArchivesEmployees(cmd2);
                break;
            case DISMISS:
                DismissArchivesEmployeesCommand cmd3 = (DismissArchivesEmployeesCommand) StringHelper.fromJsonString(configuration.getAdditionalInfo(), DismissArchivesEmployeesCommand.class);
                cmd3.setDetailIds(Collections.singletonList(configuration.getDetailId()));
                dismissArchivesEmployees(cmd3);
                break;
        }
    }

    @Override
    public CheckOperationResponse checkArchivesOperation(CheckOperationCommand cmd) {
        CheckOperationResponse response = new CheckOperationResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<ArchivesOperationalConfiguration> results = archivesProvider.listPendingConfigurationsInDetailIds(namespaceId, cmd.getDetailIds(), cmd.getOperationType());
        if (results.size() == 0) {
            response.setFlag(TrueOrFalseFlag.FALSE.getCode());
            return response;
        } else {
            response.setFlag(TrueOrFalseFlag.TRUE.getCode());
            response.setResults(results.stream().map(r -> {
                ArchivesOperationalConfigurationDTO dto = ConvertHelper.convert(r, ArchivesOperationalConfigurationDTO.class);
                OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(dto.getDetailId());
                if (employee == null)
                    dto.setContactName("");
                else
                    dto.setContactName(employee.getContactName());
                return dto;
            }).collect(Collectors.toList()));
            return response;
        }
    }

    @Override
    public ArchivesOperationalConfigurationDTO getArchivesOperationByUserId(Long userId, Long organizationId, Byte operationType) {
        OrganizationMember employee = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(userId, organizationId);
        if (employee == null)
            return null;
        ArchivesOperationalConfiguration config = archivesProvider.findPendingConfigurationByDetailId(employee.getNamespaceId(), employee.getDetailId(), operationType);
        if (config != null) {
            ArchivesOperationalConfigurationDTO dto = ConvertHelper.convert(config, ArchivesOperationalConfigurationDTO.class);
            dto.setContactName(employee.getContactName());
            return dto;
        }
        return null;
    }

    @Override
    public ListDismissCategoriesResponse listArchivesDismissCategories() {
        ListDismissCategoriesResponse res = new ListDismissCategoriesResponse();
        List<String> results = new ArrayList<>();
        for (ArchivesDismissReason reason : ArchivesDismissReason.values()) {
            results.add(reason.getType());
        }
        res.setReasons(results);
        return res;
    }

    @Override
    public ListArchivesDismissEmployeesResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListArchivesDismissEmployeesResponse response = new ListArchivesDismissEmployeesResponse();

        Condition condition = listDismissEmployeesCondition(cmd);
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(1);
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);

        List<ArchivesDismissEmployees> results = archivesProvider.listArchivesDismissEmployees(cmd.getPageAnchor(), cmd.getPageSize() + 1, namespaceId, condition);

        if (results != null) {
            Integer nextPageOffset = null;
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageOffset = cmd.getPageAnchor() + 1;
            }
            response.setNextPageAnchor(nextPageOffset);
            response.setDismissEmployees(results.stream().map(r -> {
                ArchivesDismissEmployeeDTO dto = ConvertHelper.convert(r, ArchivesDismissEmployeeDTO.class);
                return dto;
            }).collect(Collectors.toList()));
            return response;
        }

        return response;
    }

    private Condition listDismissEmployeesCondition(ListArchivesDismissEmployeesCommand cmd) {
        Condition condition = Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.ORGANIZATION_ID.eq(cmd.getOrganizationId());

        //   离职日期判断
        if (cmd.getDismissTimeStart() != null && cmd.getDismissTimeEnd() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_TIME.ge(ArchivesUtil.parseDate(cmd.getDismissTimeStart())));
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_TIME.le(ArchivesUtil.parseDate(cmd.getDismissTimeEnd())));
        }

        //   入职日期判断
        if (cmd.getCheckInTimeStart() != null && cmd.getCheckInTimeEnd() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.CHECK_IN_TIME.ge(ArchivesUtil.parseDate(cmd.getCheckInTimeStart())));
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.CHECK_IN_TIME.le(ArchivesUtil.parseDate(cmd.getCheckInTimeEnd())));
        }

        //   合同主体判断
        if (cmd.getContractPartyId() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.CONTRACT_PARTY_ID.eq(cmd.getContractPartyId()));
        }

        //   离职类型
        if (cmd.getDismissType() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_TYPE.eq(cmd.getDismissType()));
        }

        //   离职原因
        if (cmd.getDismissReason() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_REASON.eq(cmd.getDismissReason()));
        }

        //   姓名搜索
        if (cmd.getContactName() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.CONTACT_NAME.like("%" + cmd.getContactName() + "%"));
        }
        return condition;
    }

    @Override
    public GeneralFormDTO updateArchivesForm(UpdateArchivesFormCommand cmd) {

        //  1.如果无 formOriginId 时则使用的是模板，此时在组织结构新增一份表单，同时业务表单组新增记录
        //  2.如果有 formOriginId 时则说明已经拥有了表单，此时在组织架构做修改，同时业务表单组同步记录

        if (cmd.getFormOriginId() == 0L) {
            //  1.先在组织架构增加表单
            CreateApprovalFormCommand createCommand = new CreateApprovalFormCommand();
            createCommand.setOwnerId(cmd.getOrganizationId());
            createCommand.setOwnerType(ARCHIVES_OWNER_TYPE);
            createCommand.setOrganizationId(cmd.getOrganizationId());
            createCommand.setFormName(ARCHIVES_FORM);
            createCommand.setFormFields(cmd.getFormFields());
            createCommand.setFormGroups(cmd.getFormGroups());
            GeneralFormDTO form = dbProvider.execute((TransactionStatus status) -> {
                GeneralFormDTO dto = generalFormService.createGeneralForm(createCommand);
                //  2.在业务表单组新增记录
                createArchivesForm(dto);
                return dto;
            });
            return form;
        } else {
            GeneralFormDTO form = dbProvider.execute((TransactionStatus status) -> {
                //  1.为人事档案单独做一个表单的更新处理
                GeneralFormDTO dto = updateGeneralFormForArchives(cmd);
                //  2.在业务表单同步记录
                ArchivesFroms archivesFroms = archivesProvider.findArchivesFormOriginId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
                archivesFroms.setFormOriginId(dto.getFormOriginId());
                archivesFroms.setFormVersion(dto.getFormVersion());
                archivesProvider.updateArchivesForm(archivesFroms);
                return dto;
            });
            return form;
        }
    }

    /**
     * 为人事档案单独做一个表单的更新处理
     */
    private GeneralFormDTO updateGeneralFormForArchives(UpdateArchivesFormCommand cmd) {
        //  1.更新表单的字段
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(cmd
                .getFormOriginId());
        if (null == form)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "form not found");
        form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
        generalFormProvider.updateGeneralForm(form);
        //  2.更新表单的字段组
        GeneralFormGroup group = generalFormProvider.findGeneralFormGroupByFormOriginId(form.getFormOriginId());
        if (group == null) {
            //  若为空说明之前的表单建立并未建字段组
            generalFormService.createGeneralFormGroup(form, cmd.getFormGroups());
        } else {
            //  不为空则说明之前的表单建立过字段组
            generalFormService.updateGeneralFormGroupByFormId(group, form, cmd.getFormGroups());
        }
        return ConvertHelper.convert(form, GeneralFormDTO.class);
    }

    /**
     * 业务部门新增表单记录，从而能够让业务获取到正确的表单 id
     */
    private void createArchivesForm(GeneralFormDTO form) {
        ArchivesFroms archivesFrom = new ArchivesFroms();
        archivesFrom.setNamespaceId(form.getNamespaceId());
        archivesFrom.setOrganizationId(form.getOrganizationId());
        archivesFrom.setFormOriginId(form.getFormOriginId());
        archivesFrom.setFormVersion(form.getFormVersion());
        archivesFrom.setStatus(form.getStatus());
        archivesProvider.createArchivesForm(archivesFrom);
    }

    @Override
    public GetArchivesFormResponse getArchivesForm(GetArchivesFormCommand cmd) {

        GetArchivesFormResponse response = new GetArchivesFormResponse();
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  摒弃冗余字段
        form.setTemplateText(null);
        //  由于业务的特殊性，此处的 formOriginId 由另外的接口去提供
        //  故屏蔽掉此处的返回以免造成误解
        form.setFormOriginId(null);
        form.setFormVersion(null);
        response.setForm(form);
        return response;
    }

    private Long getRealFormOriginId(Long id) {

        //  此处有两种情况
        //  1.调用模板表单(此时前端传参 formOriginId 为0)
        //  2.已经建立公司对应的表单(此时已有 formOriginId )

        Long formOriginId = id;
        if (id == 0L) {
            //  当没有表单 id 的时候则去获取模板表单的id
            String value = configurationProvider.getValue(ConfigConstants.ARCHIVES_FORM_ORIGIN_ID, "0");
            formOriginId = Long.valueOf(value);
        }
        return formOriginId;
    }

    @Override
    public ArchivesFromsDTO identifyArchivesForm(IdentifyArchivesFormCommand cmd) {
        ArchivesFroms form = archivesProvider.findArchivesFormOriginId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
        ArchivesFromsDTO dto = new ArchivesFromsDTO();
        if (form != null) {
            dto.setFormOriginId(form.getFormOriginId());
            dto.setFormVersion(form.getFormVersion());
        } else {
            dto.setFormOriginId(0L);
            dto.setFormVersion(0L);
        }
        return dto;
    }

    @Override
    public ImportFileTaskDTO importArchivesEmployees(MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesEmployeesCommand cmd) {

        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(mfile);
        task.setOwnerId(cmd.getOrganizationId());
        task.setType(ImportFileTaskType.PERSONNEL_ARCHIVES.getCode());
        task.setCreatorUid(userId);

        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  调用导入方法
        importFileService.executeTask(() -> {
            ImportFileResponse response = new ImportFileResponse();
            List<ImportArchivesEmployeesDTO> dataList = handleImportArchivesEmployees(resultList, form.getFormFields());
            String fileLog;
            if (dataList.size() > 0) {
                //  校验标题，若不合格直接返回错误
                fileLog = checkArchivesEmployeesTitle(dataList.get(0), form.getFormFields());
                if (!StringUtils.isEmpty(fileLog)) {
                    response.setFileLog(fileLog);
                    return response;
                }
                response.setTitle(convertListStringToMap(dataList.get(0)));
                dataList.remove(0);
            }

            //  开始导入，同时设置导入结果
            importArchivesEmployeesFiles(dataList, response, cmd.getFormOriginId(), cmd.getOrganizationId(), cmd.getDepartmentId(), form.getFormFields());
            //  返回结果
            return response;
        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportArchivesEmployeesDTO> handleImportArchivesEmployees(List resultLists, List fields) {
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

    private String checkArchivesEmployeesTitle(ImportArchivesEmployeesDTO title, List<GeneralFormFieldDTO> fields) {
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getFieldDisplayName().equals(title.getValues().get(i)))
                return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        return null;
    }

    private void importArchivesEmployeesFiles(
            List<ImportArchivesEmployeesDTO> datas, ImportFileResponse response, Long formOriginId, Long organizationId,
            Long departmentId, List<GeneralFormFieldDTO> formValues) {
        ImportFileResultLog<Map> log = new ImportFileResultLog<>(ArchivesLocaleStringCode.SCOPE);
        List<ImportFileResultLog<Map>> errorDataLogs = new ArrayList<>();
        Long coverCount = 0L;

        for (ImportArchivesEmployeesDTO data : datas) {
            List<PostApprovalFormItem> itemValues = new ArrayList<>();
            Map<String, Object> basicDataMap = new HashMap<>();
            boolean errorFlag = false;

            //  1.在校验的时候保存需要单独调用add的值,可以节省一次循环获取的时间
            for (int i = 0; i < formValues.size(); i++) {
                PostApprovalFormItem itemValue = ConvertHelper.convert(formValues.get(i), PostApprovalFormItem.class);
                itemValue.setFieldValue(data.getValues().get(i));
                //  2.校验导入数据
                log = checkArchivesEmployeesData(data, itemValue, departmentId, basicDataMap);
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
            ImportArchivesJudgments judge = saveArchivesEmployeesMember(organizationId, basicDataMap);
            //  5.导入详细信息
            if (judge.getDetailId() == null)
                continue;
            saveArchivesEmployeesDetail(formOriginId, organizationId, judge.getDetailId(), itemValues);
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

    private ImportFileResultLog<Map> checkArchivesEmployeesData(ImportArchivesEmployeesDTO data,
                                                                PostApprovalFormItem itemValue, Long departmentId, Map<String, Object> map) {
        ImportFileResultLog<Map> log = new ImportFileResultLog<>(ArchivesLocaleStringCode.SCOPE);

        //  姓名校验
        if (ArchivesParameter.CONTACT_NAME.equals(itemValue.getFieldName())) {
            if (!checkArchivesContactName(log, convertListStringToMap(data), itemValue.getFieldValue()))
                return log;
            else
                map.put(ArchivesParameter.CONTACT_NAME, itemValue.getFieldValue());
        }

        //  手机号校验
        if (ArchivesParameter.CONTACT_TOKEN.equals(itemValue.getFieldName())) {
            if (!checkArchivesContactToken(log, convertListStringToMap(data), itemValue.getFieldValue()))
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
            if (!checkArchivesCheckInTime(log, convertListStringToMap(data), itemValue.getFieldValue()))
                return log;
            else
                map.put(ArchivesParameter.CHECK_IN_TIME, itemValue.getFieldValue());
        }

        //  员工类型校验
        if (ArchivesParameter.EMPLOYEE_TYPE.equals(itemValue.getFieldName())) {
            if (!checkArchivesEmployeeType(log, convertListStringToMap(data), itemValue.getFieldValue()))
                return log;
        }

        //  在 check 阶段就把部门、岗位和职级的 id 找到
        if (ArchivesParameter.DEPARTMENT.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                map.put(ArchivesParameter.DEPARTMENT_IDS, departmentId);
            } else {
                if (!checkArchivesDepartment(log, convertListStringToMap(data), itemValue.getFieldValue()))
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

        return null;
    }

    //  为了能够成功的解析得转换成为 map
    private Map convertListStringToMap(ImportArchivesEmployeesDTO data) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < data.getValues().size(); i++) {
            map.put(String.valueOf(i), data.getValues().get(i));
        }
        return map;
    }

    private ImportArchivesJudgments saveArchivesEmployeesMember(
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
        ArchivesEmployeeDTO dto = addArchivesEmployee(addCommand);
        Long detailId = null;
        if (dto != null)
            detailId = dto.getDetailId();
        judge.setDetailId(detailId);
        return judge;
    }

    private void saveArchivesEmployeesDetail(Long formOriginId, Long organizationId, Long detailId, List<PostApprovalFormItem> itemValues) {
        UpdateArchivesEmployeeCommand updateCommand = new UpdateArchivesEmployeeCommand();
        updateCommand.setFormOriginId(formOriginId);
        updateCommand.setDetailId(detailId);
        updateCommand.setOrganizationId(organizationId);
        updateCommand.setValues(itemValues);
        updateArchivesEmployee(updateCommand);
    }

    @Override
    public void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd) {

        //  export with the file download center
        Map<String, Object> params = new HashMap<>();
        //  the value could be null if it is not exist
        params.put("organizationId", cmd.getOrganizationId());
        params.put("formOriginId", cmd.getFormOriginId());
        params.put("keywords", cmd.getKeywords());
        params.put("namespaceId", UserContext.getCurrentNamespaceId());
        params.put("userId", UserContext.current().getUser().getId());
        String fileName = localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.EMPLOYEE_LIST, "zh_CN", "EmployeeList") + ".xlsx";

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ArchivesEmployeesExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
    }

    @Override
    public OutputStream getArchivesEmployeesExportStream(ExportArchivesEmployeesCommand cmd, Long taskId) {
        //  此处的数据类型不好调用晓强哥的 ExcelUtil, 所以使用原始的导出方法
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  1.设置导出标题
        List<String> titles = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());
        taskService.updateTaskProcess(taskId, 10);

        //  2.设置导出变量值
        List<Long> detailIds = organizationService.ListDetailsByEnterpriseId(cmd.getOrganizationId());
        taskService.updateTaskProcess(taskId, 20);

        List<ExportArchivesEmployeesDTO> values = new ArrayList<>();
        for (Long detailId : detailIds) {
            ExportArchivesEmployeesDTO dto = new ExportArchivesEmployeesDTO();
            GetArchivesEmployeeCommand getCommand =
                    new GetArchivesEmployeeCommand(cmd.getFormOriginId(), cmd.getOrganizationId(), detailId, 1);
            GetArchivesEmployeeResponse response = getArchivesEmployee(getCommand);
            List<String> employeeValues = response.getForm().getFormFields().stream().map(GeneralFormFieldDTO::getFieldValue).collect(Collectors.toList());
            dto.setVals(employeeValues);
            values.add(dto);
        }
        taskService.updateTaskProcess(taskId, 70);

        XSSFWorkbook workbook = this.exportArchivesEmployeesFiles(titles, values);
        taskService.updateTaskProcess(taskId, 95);
        return writeExcel(workbook);
    }

    private XSSFWorkbook exportArchivesEmployeesFiles(List<String> titles, List<ExportArchivesEmployeesDTO> values) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.EMPLOYEE_LIST, "zh_CN", "EmployeeList"));
        //  导出标题
        Row titleNameRow = sheet.createRow(0);
        createArchivesEmployeesFilesTitle(workbook, titleNameRow, titles);
        for (int rowIndex = 1; rowIndex < values.size(); rowIndex++) {
            Row dataRow = sheet.createRow(rowIndex);
            createArchivesEmployeesFilesContent(workbook, dataRow, values.get(rowIndex - 1).getVals());
        }
        return workbook;
    }

    private void createArchivesEmployeesFilesTitle(XSSFWorkbook workbook, Row titleNameRow, List<String> list) {
        //  设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial Unicode MS");
        font.setBold(true);
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleStyle.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            Cell cell = titleNameRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(list.get(i));
        }
    }

    private void createArchivesEmployeesFilesContent(XSSFWorkbook workbook, Row dataRow, List<String> list) {
        //  设置样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial Unicode MS");
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
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
        }
        return out;
    }

    @Override
    public void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse) {
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);
        String fileName = localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.EMPLOYEE_IMPORT_MODULE, "zh_CN", "EmployeeImportModule");
        ExcelUtils excelUtils = new ExcelUtils(httpResponse, fileName, fileName);
        List<String> titleNames = form.getFormFields().stream().map(GeneralFormFieldDTO::getFieldDisplayName).collect(Collectors.toList());
        List<String> propertyNames = new ArrayList<>();
        List<Integer> titleSizes = new ArrayList<>();
        for (int i = 0; i < form.getFormFields().size(); i++) {
            titleSizes.add(20);
        }
        excelSettings(excelUtils, form);
        excelUtils.writeExcel(propertyNames, titleNames, titleSizes, propertyNames);
    }

    private void excelSettings(ExcelUtils excelUtils, GeneralFormDTO form) {
        List<Integer> mandatoryTitle = new ArrayList<>();
        for (int i = 0; i < form.getFormFields().size(); i++) {
            mandatoryTitle.add(checkMandatory(form.getFormFields().get(i).getFieldName()));
        }
        excelUtils.setNeedMandatoryTitle(true);
        excelUtils.setMandatoryTitle(mandatoryTitle);
        excelUtils.setTitleRemark(localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.EMPLOYEE_IMPORT_REMARK, "zh_CN", "EmployeeImportRemark"), (short) 18, (short) 4480);
        excelUtils.setNeedSequenceColumn(false);
        excelUtils.setNeedTitleRemark(true);
    }

    @Override
    public ImportFileResponse<ImportArchivesEmployeesDTO> getImportEmployeesResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    /********************    import function start    ********************/
    private Integer checkMandatory(String name) {
        if (ArchivesParameter.CONTACT_NAME.equals(name))
            return 1;
        else if (ArchivesParameter.CONTACT_TOKEN.equals(name))
            return 1;
        else if (ArchivesParameter.CHECK_IN_TIME.equals(name))
            return 1;
        else if (ArchivesParameter.EMPLOYEE_TYPE.equals(name))
            return 1;
        else if (ArchivesParameter.DEPARTMENT.equals(name))
            return 1;
        else
            return 0;
    }

    private boolean verifyPersonnelByPhone(Long organizationId, String contactToken) {
        VerifyPersonnelByPhoneCommand verifyCommand = new VerifyPersonnelByPhoneCommand();
        verifyCommand.setEnterpriseId(organizationId);
        verifyCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        verifyCommand.setPhone(contactToken);
        try {
            VerifyPersonnelByPhoneCommandResponse verifyRes = organizationService.verifyPersonnelByPhone(verifyCommand);
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
            return false;
        } else if (contactName.length() > 20) {
            LOGGER.warn("Contact name is too long. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name too long.");
            log.setCode(ArchivesLocaleStringCode.ERROR_NAME_TOO_LONG);
            return false;
        } else if (!Pattern.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_\\n]+$", contactName)) {
            LOGGER.warn("Contact name wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name wrong format.");
            log.setCode(ArchivesLocaleStringCode.ERROR_NAME_WRONG_FORMAT);
            return false;
        } else
            return true;
    }

    private <T> boolean checkArchivesContactEnName(ImportFileResultLog<T> log, T data, String contactEnName) {
        if (!StringUtils.isEmpty(contactEnName)) {
            if (!Pattern.matches("^[a-zA-Z0-9_\\-\\.]+$", contactEnName)) {
                LOGGER.warn("Contact EnName wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Contact EnName wrong format");
                log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_EN_NAME_WRONG_FORMAT);
                return false;
            }
            return true;
        } else
            return true;
    }

    private <T> boolean checkArchivesContactToken(ImportFileResultLog<T> log, T data, String contactToken) {
        if (StringUtils.isEmpty(contactToken)) {
            LOGGER.warn("Contact token is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token is empty");
            log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_TOKEN_IS_EMPTY);
            return false;
        } else if (!Pattern.matches("^1\\d{10}$", getRealContactToken(contactToken, ArchivesParameter.CONTACT_TOKEN))) {
            LOGGER.warn("Contact token wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token wrong format");
            log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_TOKEN_WRONG_FORMAT);
            return false;
        } else
            return true;
    }

    private <T> boolean checkArchivesDepartment(ImportFileResultLog<T> log, T data, String department) {
        if (!StringUtils.isEmpty(department)) {
            if (organizationService.getOrganizationNameByNameAndType(department, OrganizationGroupType.DEPARTMENT.getCode()) == null) {
                LOGGER.warn("Department not found. data = {}", data);
                log.setData(data);
                log.setErrorLog("Department not found");
                log.setCode(ArchivesLocaleStringCode.ERROR_DEPARTMENT_NOT_FOUND);
                return false;
            }
            return true;
        } else
            return true;
    }

    private <T> boolean checkArchivesJobPosition(ImportFileResultLog<T> log, T data, String jobPosition) {
        if (!StringUtils.isEmpty(jobPosition)) {
            if (organizationService.getOrganizationNameByNameAndType(jobPosition, OrganizationGroupType.JOB_POSITION.getCode()) == null) {
                LOGGER.warn("JobPosition not found. data = {}", data);
                log.setData(data);
                log.setErrorLog("JobPosition not found");
                log.setCode(ArchivesLocaleStringCode.ERROR_JOB_POSITION_NOT_FOUND);
                return false;
            }
            return true;
        } else
            return true;
    }

    private <T> boolean checkArchivesContactShortToken(ImportFileResultLog<T> log, T data, String contactShortToken) {
        if (!StringUtils.isEmpty(contactShortToken)) {
            if (!Pattern.matches("\\d+", contactShortToken)) {
                LOGGER.warn("Contact short token wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Contact short token wrong format");
                log.setCode(ArchivesLocaleStringCode.ERROR_CONTACT_SHORT_TOKEN_WRONG_FORMAT);
                return false;
            }
            return true;
        } else
            return true;
    }

    private <T> boolean checkArchivesWorkEmail(ImportFileResultLog<T> log, T data, String workEmail) {
        if (!StringUtils.isEmpty(workEmail)) {
            if (!Pattern.matches("^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", workEmail)) {
                LOGGER.warn("WorkEmail wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("WorkEmail wrong format");
                log.setCode(ArchivesLocaleStringCode.ERROR_WORK_EMAIL_WRONG_FORMAT);
                return false;
            }
            return true;
        } else
            return true;
    }

    private <T> boolean checkArchivesCheckInTime(ImportFileResultLog<T> log, T data, String checkInTime) {
        if (StringUtils.isEmpty(checkInTime)) {
            LOGGER.warn("Employee checkInTime is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Employee checkInTime is empty.");
            log.setCode(ArchivesLocaleStringCode.ERROR_CHECK_IN_TIME_IS_EMPTY);
            return false;
        } else {
            Date temp = ArchivesUtil.parseDate(checkInTime);
            if (temp == null) {
                LOGGER.warn("Employee date wrong format. data = {}", data);
                log.setData(data);
                log.setErrorLog("Employee date wrong format.");
                log.setCode(ArchivesLocaleStringCode.ERROR_DATE_WRONG_FORMAT);
                return false;
            } else
                return true;
        }
    }

    private <T> boolean checkArchivesEmployeeType(ImportFileResultLog<T> log, T data, String checkInTime) {
        if (StringUtils.isEmpty(checkInTime)) {
            LOGGER.warn("Employee employeeType is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Employee employeeType is empty.");
            log.setCode(ArchivesLocaleStringCode.ERROR_EMPLOYEE_TYPE_IS_EMPTY);
            return false;
        } else
            return true;
    }

    /********************    import function end    ********************/

    @Override
    public List<OrganizationMemberDetails> queryArchivesEmployees(ListingLocator locator, Long organizationId, Long departmentId, ListingQueryBuilderCallback queryBuilderCallback) {
        List<OrganizationMemberDetails> employees = organizationProvider.queryOrganizationMemberDetails(new ListingLocator(), organizationId, (locator1, query) -> {
            queryBuilderCallback.buildCondition(locator, query);
            if (departmentId != null) {
                Organization department = organizationProvider.findOrganizationById(departmentId);
                if (department.getGroupType().equals(OrganizationGroupType.ENTERPRISE.getCode())) {
                    // get the hidden department of the company which has the same name
                    Organization under_department = organizationProvider.findUnderOrganizationByParentOrgId(department.getId());
                    if (under_department != null)
                        department = under_department;
                }
                List<Long> workGroups = organizationProvider.listOrganizationPersonnelDetailIdsByDepartmentId(department.getId());
                List<Long> dismissGroups = archivesProvider.listDismissEmployeeDetailIdsByDepartmentId(department.getId());
                Condition con1 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(0L);
                Condition con2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(0L);
                if (workGroups != null)
                    con1 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(workGroups);
                if (dismissGroups != null)
                    con2 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.in(dismissGroups);
                query.addConditions(con1.or(con2));
            }
            return query;
        });

        //  get the department name.
        if (employees != null && employees.size() > 0) {
            for (OrganizationMemberDetails employee : employees) {
                if (employee.getEmployeeStatus().equals(EmployeeStatus.DISMISSAL.getCode())) {
                    ArchivesDismissEmployees dismissEmployee = archivesProvider.getArchivesDismissEmployeesByDetailId(employee.getId());
                    if (dismissEmployee != null)
                        employee.setDepartmentName(dismissEmployee.getDepartment());
                } else
                    employee.setDepartmentName(convertToOrgNames(getEmployeeDepartment(employee.getId())));
            }
            return employees;
        }
        return new ArrayList<>();
    }

    /********************    version2.6    ********************/

    @Override
    public void setArchivesNotification(ArchivesNotificationCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ArchivesNotifications originNotify = archivesProvider.findArchivesNotificationsByOrganizationId(namespaceId, cmd.getOrganizationId());
        if (originNotify == null) {
            ArchivesNotifications newNotify = new ArchivesNotifications();
            newNotify.setNamespaceId(namespaceId);
            newNotify.setOrganizationId(cmd.getOrganizationId());
            newNotify.setNotifyDay(cmd.getRemindDay());
            newNotify.setNotifyTime(cmd.getRemindTime());
            newNotify.setMailFlag(cmd.getMailFlag());
            newNotify.setMessageFlag(cmd.getMessageFlag());
            newNotify.setNotifyTarget(JSON.toJSONString(getNotificationTarget(cmd)));
            archivesProvider.createArchivesNotifications(newNotify);
        } else {
            originNotify.setNotifyDay(cmd.getRemindDay());
            originNotify.setNotifyTime(cmd.getRemindTime());
            originNotify.setMailFlag(cmd.getMailFlag());
            originNotify.setMessageFlag(cmd.getMessageFlag());
            originNotify.setNotifyTarget(JSON.toJSONString(getNotificationTarget(cmd)));
            archivesProvider.updateArchivesNotifications(originNotify);
        }
    }

    private List<ArchivesNotificationTarget> getNotificationTarget(ArchivesNotificationCommand cmd) {
        List<ArchivesNotificationTarget> results = new ArrayList<>();
        if (cmd.getDetailIds() == null || cmd.getDetailIds().size() == 0)
            return results;
        for (Long detailId : cmd.getDetailIds()) {
            ArchivesNotificationTarget target = new ArchivesNotificationTarget();
            OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            if (employee == null)
                continue;
            target.setDetailId(employee.getId());
            target.setUserId(employee.getTargetId());
            target.setContactName(employee.getContactName());
            target.setWorkEmail(employee.getWorkEmail());
            results.add(target);
        }
        return results;
    }

    @Override
    public void executeArchivesNotification(Integer day, Integer time, LocalDateTime nowDateTime) {
        List<ArchivesNotifications> results = archivesProvider.listArchivesNotifications(day, time);
        if (results == null) {
            LOGGER.info("No notifications");
            return;
        }
        for (ArchivesNotifications result : results)
            sendArchivesNotification(result, nowDateTime);
    }

    private void sendArchivesNotification(ArchivesNotifications notification, LocalDateTime dateTime) {
        Organization company = organizationProvider.findOrganizationById(notification.getOrganizationId());
        if (company == null) {
            throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_DEPARTMENT_NOT_FOUND,
                    "Company not found: notification=" + notification);

        }

        //  1.resolve targets
        List<ArchivesNotificationTarget> targets = JSON.parseArray(notification.getNotifyTarget(), ArchivesNotificationTarget.class);
        if (targets == null || targets.size() == 0) {
            throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_NO_TARGETS,
                    "No targets: notification=" + notification);

        }

        //  2.get employee's names
        Date firstOfWeek = Date.valueOf(dateTime.toLocalDate());
        Date lastOfWeek = ArchivesUtil.plusDate(firstOfWeek, 6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        List<String> weekScopes = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekScopes.add(formatter.format(ArchivesUtil.plusDate(firstOfWeek, i).toLocalDate()));
        }
        List<OrganizationMemberDetails> employees = organizationProvider.queryOrganizationMemberDetails(new ListingLocator(), company.getId(), (locator, query) -> {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode()));
            Condition con = Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYMENT_TIME.between(firstOfWeek, lastOfWeek);
            con = con.or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME_INDEX.in(weekScopes));
            con = con.or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.BIRTHDAY_INDEX.in(weekScopes));
            con = con.or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTRACT_END_TIME.between(firstOfWeek, lastOfWeek));
            con = con.or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID_EXPIRY_DATE.between(firstOfWeek, lastOfWeek));
            query.addConditions(con);
            return query;
        });
        if (employees == null) {
            LOGGER.info("Nothing needs to be sent.");
            return;
        }

        //  3.send notifications
        for (ArchivesNotificationTarget target : targets) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("contactName", target.getContactName());
            map.put("companyName", company.getName());
            //  3-1.get the notification body.
            StringBuilder body = new StringBuilder(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE, ArchivesLocaleTemplateCode.ARCHIVES_REMIND_BEGINNING, "zh_CN", map, ""));
            for (int n = 0; n < 7; n++)
                body.append(processNotificationBody(employees, company.getName(), ArchivesUtil.plusDate(firstOfWeek, n)));
            //  3-2.send it
            if (notification.getMailFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode())
                sendArchivesEmails(target.getWorkEmail(), body.toString());
            if (notification.getMessageFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode())
                sendArchivesMessages(target.getUserId(), body.toString());
        }
    }

    private String processNotificationBody(List<OrganizationMemberDetails> employees, String organizationName, Date date) {
        String body = "";
        StringBuilder employment = new StringBuilder();
        StringBuilder anniversary = new StringBuilder();
        StringBuilder birthday = new StringBuilder();
        StringBuilder contract = new StringBuilder();
        StringBuilder idExpiry = new StringBuilder();
//        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.toLocalDate() + "   " + date.toLocalDate().getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.SIMPLIFIED_CHINESE) + "\n";
        body += dateString;
        Map<String, Object> map;

        for (OrganizationMemberDetails employee : employees) {
            if (date.equals(employee.getEmploymentTime()))
                employment.append(employee.getContactName()).append("，");
            if (date.equals(employee.getContractEndTime()))
                contract.append(employee.getContactName()).append("，");
            if (date.equals(employee.getIdExpiryDate()))
                idExpiry.append(employee.getContactName()).append("，");
            if (ArchivesUtil.getMonthAndDay(date).equals(employee.getCheckInTimeIndex())) {
                map = new LinkedHashMap<>();
                map.put("contactName", employee.getContactName());
                map.put("companyName", organizationName);
                map.put("year", date.toLocalDate().getYear() - employee.getCheckInTime().toLocalDate().getYear());
                anniversary.append(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE, ArchivesLocaleTemplateCode.ARCHIVES_REMIND_ANNIVERSARY, "zh_CN", map, ""));
            }
            if (ArchivesUtil.getMonthAndDay(date).equals(employee.getBirthdayIndex())) {
                map = new LinkedHashMap<>();
                map.put("contactName", employee.getContactName());
                map.put("year", date.toLocalDate().getYear() - employee.getBirthday().toLocalDate().getYear());
                birthday.append(localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE, ArchivesLocaleTemplateCode.ARCHIVES_REMIND_BIRTH, "zh_CN", map, ""));
            }
        }
        if (!employment.toString().equals("")) {
            employment = new StringBuilder(employment.substring(0, employment.length() - 1));
            map = new LinkedHashMap<>();
            map.put("contactNames", employment.toString());
            body += localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE, ArchivesLocaleTemplateCode.ARCHIVES_REMIND_EMPLOYMENT, "zh_CN", map, "");
        }
        if (!contract.toString().equals("")) {
            contract = new StringBuilder(contract.substring(0, contract.length() - 1));
            map = new LinkedHashMap<>();
            map.put("contactNames", contract.toString());
            body += localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE, ArchivesLocaleTemplateCode.ARCHIVES_REMIND_CONTRACT, "zh_CN", map, "");
        }
        if (!idExpiry.toString().equals("")) {
            idExpiry = new StringBuilder(idExpiry.substring(0, idExpiry.length() - 1));
            map = new LinkedHashMap<>();
            map.put("contactNames", idExpiry.toString());
            body += localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE, ArchivesLocaleTemplateCode.ARCHIVES_REMIND_ID, "zh_CN", map, "");
        }
        if (!anniversary.toString().equals("")) {
            body += anniversary;
        }
        if (!birthday.toString().equals("")) {
            body += birthday;
        }
        body += "\n";

        return body;
    }


    private void sendArchivesEmails(String email, String body) {
        if (email == null)
            return;
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        handler.sendMail(UserContext.getCurrentNamespaceId(), null, email, "人事提醒", body, null);
    }

    private void sendArchivesMessages(Long userId, String body) {
        if (userId == 0)
            return;
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(body);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(userId)));
        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(userId),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    @Override
    public void syncArchivesConfigAndLogs() {
        List<ArchivesLogs> results = archivesProvider.listAllArchivesLogs();
        if (results.size() > 0) {
            for (ArchivesLogs result : results) {
                ArchivesOperationalLog log = ConvertHelper.convert(result, ArchivesOperationalLog.class);
                switch (ArchivesOperationType.fromCode(result.getOperationType())) {
                    case CHECK_IN:
                        break;
                    case EMPLOY:
                        log.setStringTag1(result.getOperationReason());
                        break;
                    case TRANSFER:
                        log.setStringTag1(result.getOperationRemark());
                        log.setStringTag4(ArchivesUtil.resolveArchivesEnum(result.getOperationCategory(), ArchivesParameter.TRANSFER_TYPE));
                        log.setStringTag5(result.getOperationReason());
                        break;
                    case DISMISS:
                        log.setStringTag1(ArchivesUtil.resolveArchivesEnum(result.getOperationCategory(), ArchivesParameter.DISMISS_TYPE));
                        log.setStringTag2(result.getOperationReason());
                        log.setStringTag3(result.getOperationRemark());
                        break;
                }
                archivesProvider.createOperationalLog(log);
            }
        }

        List<ArchivesConfigurations> results2 = archivesProvider.listAllPendingConfigs();
        if (results2.size() > 0) {
            for (ArchivesConfigurations result2 : results2) {
                createConfig(result2);
            }
        }
    }

    private void createConfig(ArchivesConfigurations r) {
        if (r.getOperationType().equals(ArchivesOperationType.EMPLOY.getCode())) {
            EmployArchivesEmployeesCommand cmd = (EmployArchivesEmployeesCommand) StringHelper.fromJsonString(r.getOperationInformation(), EmployArchivesEmployeesCommand.class);
            if (cmd.getDetailIds() == null || cmd.getDetailIds().size() == 0)
                return;
            for (Long detailId : cmd.getDetailIds()) {
                ArchivesOperationalConfiguration oldConfig = archivesProvider.findConfigurationByDetailId(r.getNamespaceId(), r.getOrganizationId(), r.getOperationType(), detailId);
                if (oldConfig != null) {
                    if (r.getOperationTime().before(oldConfig.getOperationDate()))
                        continue;
                    oldConfig.setOperationDate(r.getOperationTime());
                    oldConfig.setAdditionalInfo(r.getOperationInformation());
                    oldConfig.setCreateTime(r.getCreateTime());
                    oldConfig.setOperatorUid(r.getOperatorUid());
                    archivesProvider.updateOperationalConfiguration(oldConfig);
                } else {
                    ArchivesOperationalConfiguration newConfig = ConvertHelper.convert(r, ArchivesOperationalConfiguration.class);
                    newConfig.setDetailId(detailId);
                    newConfig.setOperationType(r.getOperationType());
                    newConfig.setOperationDate(r.getOperationTime());
                    newConfig.setStatus(ArchivesOperationStatus.PENDING.getCode());
                    newConfig.setAdditionalInfo(r.getOperationInformation());
                    archivesProvider.createOperationalConfiguration(newConfig);
                }
            }
        }
    }
}
