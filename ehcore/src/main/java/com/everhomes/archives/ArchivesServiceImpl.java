package com.everhomes.archives;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormGroup;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.*;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
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
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class ArchivesServiceImpl implements ArchivesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesServiceImpl.class);

    private static final String ARCHIVES_FORM = "archives_form";

    private static final String ARCHIVE_OWNER_TYPE = "archives_owner_type";

    private static final String ARCHIVES_FORM_ORIGIN_ID = "archives.form.origin.id";

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
    private ConfigurationProvider configurationProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private ArchivesConfigurationService archivesConfigurationService;

    @Override
    public ArchivesContactDTO addArchivesContact(AddArchivesContactCommand cmd) {

        ArchivesContactDTO dto = new ArchivesContactDTO();
        //  组织架构添加人员
        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setGender(cmd.getGender());
        addCommand.setContactToken(cmd.getContactToken());
        addCommand.setDepartmentIds(cmd.getDepartmentIds());
        addCommand.setJobPositionIds(cmd.getJobPositionIds());
        addCommand.setJobLevelIds(cmd.getJobLevelIds());
        addCommand.setVisibleFlag(cmd.getVisibleFlag());
        dbProvider.execute((TransactionStatus status) -> {
            //  1.添加人员到组织架构
            OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

            //  2.获得 detailId 然后处理其它信息
            Long detailId = null;
            if (memberDTO != null)
                detailId = memberDTO.getDetailId();
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            if (memberDetail != null) {
                memberDetail.setEnName(cmd.getContactEnName());
                memberDetail.setRegionCode(cmd.getRegionCode());
                memberDetail.setContactShortToken(cmd.getContactShortToken());
                memberDetail.setCheckInTime(ArchivesUtil.currentDate());
                memberDetail.setEmploymentTime(ArchivesUtil.currentDate());
                memberDetail.setEmployeeType(EmployeeType.FULLTIME.getCode());
                memberDetail.setEmployeeStatus(EmployeeStatus.ON_THE_JOB.getCode());
                memberDetail.setWorkEmail(cmd.getWorkEmail());
                organizationProvider.updateOrganizationMemberDetails(memberDetail, memberDetail.getId());
                dto.setDetailId(detailId);
                dto.setContactName(memberDetail.getContactName());
                dto.setContactToken(memberDetail.getContactToken());
            }

            //  3.查询若存在于离职列表则删除
            deleteArchivesDismissEmployees(detailId, cmd.getOrganizationId());

            //  4.增加入职记录
            checkInArchivesEmployeesLogs(cmd.getOrganizationId(), detailId, ArchivesUtil.currentDate());
            return null;
        });
        return dto;
    }

    @Override
    public void transferArchivesContacts(TransferArchivesContactsCommand cmd) {
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

                //  添加档案记录
                TransferArchivesEmployeesCommand logCommand = ConvertHelper.convert(cmd, TransferArchivesEmployeesCommand.class);
                logCommand.setEffectiveTime(String.valueOf(ArchivesUtil.currentDate()));
                logCommand.setTransferType(ArchivesTransferType.OTHER.getCode());
                transferArchivesEmployeesLogs(logCommand);
                transferArchivesEmployeesLogs(logCommand);
            }
            return null;
        });
    }

    @Override
    public void deleteArchivesContacts(DeleteArchivesContactsCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            if (cmd.getDetailIds() != null) {
                for (Long detailId : cmd.getDetailIds()) {
                    //  1.组织架构删除
                    OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                    DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
                    deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(cmd.getOrganizationId());
                    deleteOrganizationPersonnelByContactTokenCommand.setContactToken(detail.getContactToken());
                    deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
                    organizationService.deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);

                    //  2.置顶表删除
                    ArchivesStickyContacts stickyContact = archivesProvider.findArchivesStickyContactsByDetailIdAndOrganizationId(namespaceId, cmd.getOrganizationId(), detailId);
                    if (stickyContact != null)
                        archivesProvider.deleteArchivesStickyContacts(stickyContact);
                }
                //  3.添加档案记录
                DismissArchivesEmployeesCommand dismissCommand = new DismissArchivesEmployeesCommand();
                dismissCommand.setDetailIds(cmd.getDetailIds());
                dismissCommand.setOrganizationId(cmd.getOrganizationId());
                dismissCommand.setDismissTime(String.valueOf(ArchivesUtil.currentDate()));
                dismissCommand.setDismissType(ArchivesDismissType.OTHER.getCode());
                dismissCommand.setDismissReason(ArchivesDismissReason.OTHER.getCode());
                dismissCommand.setDismissRemark("通讯录删除");
                dismissArchivesEmployeesLogs(dismissCommand);
            }
            return null;
        });
    }

    //  通讯录成员置顶接口
    @Override
    public void stickArchivesContact(StickArchivesContactCommand cmd) {
        User user = UserContext.current().getUser();

        //  状态码为 0 时删除
        if (cmd.getStick().equals("0")) {
            ArchivesStickyContacts result = archivesProvider.findArchivesStickyContactsByDetailIdAndOrganizationId(
                    user.getNamespaceId(), cmd.getOrganizationId(), cmd.getDetailId());
            if (result != null)
                archivesProvider.deleteArchivesStickyContacts(result);
        }

        //  状态码为 1 时新增置顶
        if (cmd.getStick().equals("1")) {
            ArchivesStickyContacts result = archivesProvider.findArchivesStickyContactsByDetailIdAndOrganizationId(user.getNamespaceId(), cmd.getOrganizationId(), cmd.getDetailId());
            if (result == null) {
                ArchivesStickyContacts contactsSticky = new ArchivesStickyContacts();
                contactsSticky.setNamespaceId(user.getNamespaceId());
                contactsSticky.setOrganizationId(cmd.getOrganizationId());
                contactsSticky.setDetailId(cmd.getDetailId());
                contactsSticky.setOperatorUid(user.getId());
                archivesProvider.createArchivesStickyContacts(contactsSticky);
            } else {
                archivesProvider.updateArchivesStickyContacts(result);
            }
        }
    }

    /**
     * 1.If the keywords is not null, just pass the key and get the corresponding employee back.
     * 2.If the keywords is null, then judged by the "pageAnchor"
     * 3.If the pageAnchor is null, we should get stick employees first.
     * 4.if the pageAnchor is not null, means we should get the next page of employees, so ignore those stick employees.
     *
     * @return
     */
    @Override
    public ListArchivesContactsResponse listArchivesContacts(ListArchivesContactsCommand cmd) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListArchivesContactsResponse response = new ListArchivesContactsResponse();
        final Integer stickCount = 20;  //  置顶数为20,表示一页最多显示20个置顶人员 at 11/06/2017
        if (cmd.getPageSize() != null)
            cmd.setPageSize(cmd.getPageSize());
        else
            cmd.setPageSize(20);

        List<Long> detailIds = archivesProvider.listArchivesStickyContactsIds(namespaceId, cmd.getOrganizationId(), stickCount);    //  保存置顶人员
        if (!StringUtils.isEmpty(cmd.getKeywords()) || detailIds == null) {
            //  有查询的时候已经不需要置顶了，直接查询对应人员
            List<ArchivesContactDTO> contacts = new ArrayList<>();
            contacts.addAll(listArchivesContacts(cmd, response, null));
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
                List<ArchivesContactDTO> contacts = new ArrayList<>();
                contacts.addAll(listArchivesContacts(cmd, response, detailIds));
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

    //  数字转换(1-A,2-B...)
    private static String GetExcelLetter(int n) {
        String s = "";
        while (n > 0) {
            int m = n % 26;
            if (m == 0)
                m = 26;
            s = (char) (m + 64) + s;
            n = (n - m) / 26;
        }
        return s;
    }

    @Override
    public ImportFileTaskDTO importArchivesContacts(MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesContactsCommand cmd) {

        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(mfile);
        task.setOwnerType(ARCHIVE_OWNER_TYPE);
        task.setOwnerId(cmd.getOrganizationId());
        task.setType(ImportFileTaskType.PERSONNEL_ARCHIVES.getCode());
        task.setCreatorUid(userId);

        //  调用导入方法
        importFileService.executeTask(new ExecuteImportTaskCallback() {
            @Override
            public ImportFileResponse importFile() {
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
            }
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
            if (null != r.getCells().get("A"))
                data.setContactName(r.getCells().get("A"));
            if (null != r.getCells().get("B"))
                data.setContactEnName(r.getCells().get("B"));
            if (null != r.getCells().get("C"))
                data.setGender(r.getCells().get("C"));
            if (null != r.getCells().get("D"))
                data.setContactToken(r.getCells().get("D"));
            if (null != r.getCells().get("E"))
                data.setContactShortToken(r.getCells().get("E"));
            if (null != r.getCells().get("F"))
                data.setWorkEmail(r.getCells().get("F"));
            if (null != r.getCells().get("G"))
                data.setDepartment(r.getCells().get("G"));
            if (null != r.getCells().get("H"))
                data.setJobPosition(r.getCells().get("H"));
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

        //  TODO:是否从数据库读取模板
        List<String> module = new ArrayList<>(Arrays.asList("姓名", "英文名", "性别", "手机", "短号", "工作邮箱", "部门", "职务"));
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
            if (module.get(i).equals(temp.get(i)))
                continue;
            else {
                return ImportFileErrorType.TITLE_ERROE.getCode();
            }
        }
        return null;
    }



    private ImportFileResultLog<ImportArchivesContactsDTO> checkArchivesContactsDatas(ImportArchivesContactsDTO data) {

        ImportFileResultLog<ImportArchivesContactsDTO> log = new ImportFileResultLog<>(ArchivesServiceErrorCode.SCOPE);

        //  姓名校验
        if(!checkArchivesContactName(log, data, data.getContactName()))
            return log;

        //  TODO:英文名校验

        //  手机号
        if(!checkArchivesContactToken(log, data, data.getContactToken()))
            return log;

        //  部门
        if(!checkArchivesDepartment(log, data, data.getDepartment()))
            return log;

        //  职务
        if(!checkArchivesJobPosition(log, data, data.getJobPosition()))
            return log;

        return null;
    }

    private boolean saveArchivesContactsData(ImportArchivesContactsDTO data, Long organizationId, Long departmentId) {
        AddArchivesContactCommand addCommand = new AddArchivesContactCommand();
        addCommand.setOrganizationId(organizationId);
        addCommand.setContactName(data.getContactName());
        addCommand.setContactEnName(data.getContactEnName());
        addCommand.setGender(convertToArchivesEnum(data.getGender(), ArchivesParameter.GENDER));
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
        addArchivesContact(addCommand);
        return verifyPersonnelByPhone(organizationId, addCommand.getContactToken());
    }

    @Override
    public void exportArchivesContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse) {
        ListArchivesContactsCommand listCommand = new ListArchivesContactsCommand();
        listCommand.setOrganizationId(cmd.getOrganizationId());
        listCommand.setKeywords(cmd.getKeywords());
        listCommand.setPageSize(10000);
        listCommand.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CHILD_ENTERPRISE.getCode()));
        ListArchivesContactsResponse response = listArchivesContacts(listCommand);
        if (response.getContacts() != null && response.getContacts().size() > 0) {
            //  1.设置导出文件名与 sheet 名
            ExcelUtils excelUtils = new ExcelUtils(httpResponse, "通讯录成员列表", "通讯录成员列表");
            //  2.设置导出标题栏
            List<String> titleNames = new ArrayList<String>(Arrays.asList("姓名", "性别", "手机", "短号", "工作邮箱", "部门", "职务"));
            //  3.设置格式长度
            List<Integer> cellSizes = new ArrayList<Integer>(Arrays.asList(20, 10, 20, 20, 30, 30, 20));
            //  4.设置导出变量名
            List<String> propertyNames = new ArrayList<String>(Arrays.asList("contactName", "genderString", "contactToken",
                    "contactShortToken", "workEmail", "departmentString", "jobPositionString"));
            excelUtils.setNeedSequenceColumn(false);
            //  5.处理导出变量的值并导出
            List<ArchivesContactDTO> contacts = response.getContacts().stream().map(r -> {
                ArchivesContactDTO dto = convertArchivesContactForExcel(r);
                return r;
            }).collect(Collectors.toList());
            excelUtils.writeExcel(propertyNames, titleNames, cellSizes, contacts);
        }
    }

    private ArchivesContactDTO convertArchivesContactForExcel(ArchivesContactDTO dto) {

        //  性别转化
        dto.setGenderString(convertToArchivesInfo(dto.getGender(), ArchivesParameter.GENDER));

        //  部门转化
        dto.setDepartmentString(convertToArchivesInfo(dto.getDepartments(), ArchivesParameter.DEPARTMENT));

        //  岗位的导出
        dto.setJobPositionString(convertToArchivesInfo(dto.getJobPositions(), ArchivesParameter.DEPARTMENT));

        return dto;
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
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED, "User acount has not been activated yet");

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


    /*
     * 增加入职记录
     */
    private void checkInArchivesEmployeesLogs(Long organizationId, Long detailId, Date checkInTime) {
        Long userId = UserContext.currentUserId();
        ArchivesLogs log = new ArchivesLogs();
        log.setDetailId(detailId);
        log.setOrganizationId(organizationId);
        log.setOperationType(ArchivesOperationType.CHECK_IN.getCode());
        log.setOperationTime(checkInTime);
        log.setOperatorUid(userId);
        log.setOperatorName(getArchivesContactName(userId, organizationId));
        archivesProvider.createArchivesLogs(log);
    }

    /*
     * 增加转正记录
     */
    private void employArchivesEmployeesLogs(EmployArchivesEmployeesCommand cmd) {
        Long userId = UserContext.currentUserId();
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                ArchivesLogs log = new ArchivesLogs();
                log.setDetailId(detailId);
                log.setOrganizationId(cmd.getOrganizationId());
                log.setOperationType(ArchivesOperationType.EMPLOY.getCode());
                log.setOperationTime(cmd.getEmploymentTime());
                log.setOperationReason(cmd.getEmploymentEvaluation());
                log.setOperatorUid(userId);
                log.setOperatorName(getArchivesContactName(userId, cmd.getOrganizationId()));
                archivesProvider.createArchivesLogs(log);
            }
        }
    }

    /*
     * 增加调整记录
     */
    private void transferArchivesEmployeesLogs(TransferArchivesEmployeesCommand cmd) {
        Long userId = UserContext.currentUserId();
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                ArchivesLogs log = new ArchivesLogs();
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("new", convertToArchivesInfo(cmd.getDepartmentIds(), ArchivesParameter.DEPARTMENT_IDS));
                String remark = localeTemplateService.getLocaleTemplateString(ArchivesTemplateCode.SCOPE, ArchivesTemplateCode.ARCHIVES_DEPARTMENT_CHANGE, "zh_CN", map, "");
                log.setDetailId(detailId);
                log.setOrganizationId(cmd.getOrganizationId());
                log.setOperationType(ArchivesOperationType.TRANSFER.getCode());
                log.setOperationTime(cmd.getEffectiveTime());
                log.setOperationCategory(cmd.getTransferType());
                log.setOperationReason(cmd.getTransferReason());
                log.setOperationRemark(remark);
                log.setOperatorUid(userId);
                log.setOperatorName(getArchivesContactName(userId, cmd.getOrganizationId()));
                archivesProvider.createArchivesLogs(log);
            }
        }
    }

    /*
     * 增加离职记录
     */
    private void dismissArchivesEmployeesLogs(DismissArchivesEmployeesCommand cmd) {
        Long userId = UserContext.currentUserId();
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                ArchivesLogs log = new ArchivesLogs();
                log.setDetailId(detailId);
                log.setOrganizationId(cmd.getOrganizationId());
                log.setOperationType(ArchivesOperationType.DISMISS.getCode());
                log.setOperationTime(cmd.getDismissTime());
                log.setOperationCategory(cmd.getDismissType());
                log.setOperationReason(convertToArchivesInfo(cmd.getDismissReason(), "dismissReason"));
                log.setOperationRemark(cmd.getDismissRemark());
                log.setOperatorUid(userId);
                log.setOperatorName(getArchivesContactName(userId, cmd.getOrganizationId()));
                archivesProvider.createArchivesLogs(log);
            }
        }
    }

    private String getArchivesContactName(Long userId, Long organizationId) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        String contactToken = userIdentifier.getIdentifierToken();
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndToken(contactToken, organizationId);
        if (member == null)
            return "管理员" + contactToken;
        else
            return member.getContactName();
    }

    @Override
    public ArchivesEmployeeDTO addArchivesEmployee(AddArchivesEmployeeCommand cmd) {

        ArchivesEmployeeDTO dto = new ArchivesEmployeeDTO();

        //  1.组织架构添加人员
        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setGender(cmd.getGender());
        addCommand.setDepartmentIds(cmd.getDepartmentIds());
        addCommand.setJobPositionIds(cmd.getJobPositionIds());
        addCommand.setJobLevelIds(cmd.getJobLevelIds());
        addCommand.setContactToken(cmd.getContactToken());
        dbProvider.execute((TransactionStatus status) -> {

            OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

            //  2.获得 detailId 然后处理其它信息
            Long detailId = null;
            if (memberDTO != null)
                detailId = memberDTO.getDetailId();
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            if (memberDetail != null) {

                memberDetail.setCheckInTime(cmd.getCheckInTime());
                memberDetail.setEmployeeType(cmd.getEmployeeType());
                memberDetail.setEmployeeStatus(cmd.getEmployeeStatus());
                if (cmd.getEmploymentTime() == null)
                    memberDetail.setEmploymentTime(cmd.getCheckInTime());
                else
                    memberDetail.setEmploymentTime(cmd.getEmploymentTime());
                memberDetail.setEmployeeNo(cmd.getEmployeeNo());
                memberDetail.setEnName(cmd.getEnName());
                memberDetail.setContactShortToken(cmd.getContactShortToken());
                memberDetail.setWorkEmail(cmd.getWorkEmail());
                memberDetail.setContractPartyId(cmd.getContractPartyId());
                memberDetail.setRegionCode(cmd.getRegionCode());
                organizationProvider.updateOrganizationMemberDetails(memberDetail, memberDetail.getId());
                dto.setDetailId(detailId);
                dto.setContactName(memberDetail.getContactName());
                dto.setContactToken(memberDetail.getContactToken());
            }

            //  3.查询若存在于离职列表则删除
            deleteArchivesDismissEmployees(detailId, cmd.getOrganizationId());

            //  4.增加入职记录
            checkInArchivesEmployeesLogs(cmd.getOrganizationId(), detailId, cmd.getCheckInTime());
            return null;
        });
        return dto;
    }

    /*
     * 获取部门名称
     */
/*    private String getDepartmentName(List<Long> ids) {

    }*/

    @Override
    public void updateArchivesEmployee(UpdateArchivesEmployeeCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            //  1.更新 detail 表信息
            OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
            employee = convertToEmployeeDetail(employee, cmd.getValues());
            organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());

            //  2.更新 member 表信息
            organizationService.updateOrganizationMemberInfoByDetailId(employee.getId(), employee.getContactToken(), employee.getContactName(), employee.getGender());

            //  3.更新自定义字段值
            List<PostApprovalFormItem> dynamicItems = cmd.getValues().stream().filter(r -> {
                return !GeneralFormFieldAttribute.DEFAULT.getCode().equals(r.getFieldAttribute());
            }).map(r -> {
                return r;
            }).collect(Collectors.toList());
            addGeneralFormValuesCommand formCommand = new addGeneralFormValuesCommand();
            formCommand.setGeneralFormId(getRealFormOriginId(cmd.getFormOriginId()));
            formCommand.setSourceId(employee.getId());
            formCommand.setSourceType(GeneralFormSourceType.ARCHIVES_AUTH.getCode());
            formCommand.setValues(dynamicItems);
            generalFormService.addGeneralFormValues(formCommand);

            return null;
        });
    }

    @Override
    public GetArchivesEmployeeResponse getArchivesEmployee(GetArchivesEmployeeCommand cmd) {

        GetArchivesEmployeeResponse response = new GetArchivesEmployeeResponse();
        String employeeCase = "";

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
        for (GeneralFormFieldDTO dto : form.getFormFields()) {
            //  4-1.赋值给系统默认字段
            if (GeneralFormFieldAttribute.DEFAULT.getCode().equals(dto.getFieldAttribute())) {
                dto.setFieldValue(employeeDefaultMaps.get(dto.getFieldName()));
            }
            //  4-2.赋值给非系统默认字段
            else {
                dto.setFieldValue(employeeDynamicMaps.get(dto.getFieldName()));
            }
        }

        //  5.获取档案记录
        List<ArchivesLogs> logs = archivesProvider.listArchivesLogs(cmd.getOrganizationId(), cmd.getDetailId());
        if (logs != null && logs.size() > 0)
            response.setLogs(logs.stream().map(r -> {
                ArchivesLogDTO dto = ConvertHelper.convert(r, ArchivesLogDTO.class);
                return dto;
            }).collect(Collectors.toList()));

        //  6.拼接员工状态
        if (cmd.getDismiss() != null) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("firstDate", format.format(employee.getCheckInTime()));
            if (employee.getDismissTime() != null)
                map.put("nextDate", format.format(employee.getDismissTime()));
            else
                map.put("nextDate", "   无");
            employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesTemplateCode.SCOPE, ArchivesTemplateCode.ARCHIVES_DISMISS_CASE, "zh_CN", map, "");

        } else {
            if (employee.getEmployeeStatus().equals(EmployeeStatus.ON_THE_JOB.getCode())) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("firstDate", format.format(employee.getCheckInTime()));
                if (employee.getContractEndTime() != null)
                    map.put("nextDate", format.format(employee.getContractEndTime()));
                else
                    map.put("nextDate", "   无");
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesTemplateCode.SCOPE, ArchivesTemplateCode.ARCHIVES_ON_THE_JOB_CASE, "zh_CN", map, "");
            } else {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("firstDate", format.format(employee.getCheckInTime()));
                map.put("nextDate", format.format(employee.getEmploymentTime()));
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesTemplateCode.SCOPE, ArchivesTemplateCode.ARCHIVES_PROBATION_CASE, "zh_CN", map, "");
            }
        }

        //  7.设置部门、岗位、职级ids
        if (employee.getDepartmentIds() != null)
            response.setDepartmentIds(JSONObject.parseArray(employee.getDepartmentIds(), Long.class));
        if (employee.getJobPositionIds() != null)
            response.setJobPositionIds(JSONObject.parseArray(employee.getJobPositionIds(), Long.class));
        if (employee.getJobLevelIds() != null)
            response.setJobLevelIds(JSONObject.parseArray(employee.getJobLevelIds(), Long.class));

        //  摒弃冗余字段
        //  由于业务的特殊性，此处的 formOriginId 由另外的接口去提供
        //  故屏蔽掉此处的返回以免造成误解
        form.setTemplateText(null);
        form.setFormOriginId(null);
        form.setFormVersion(null);
        response.setForm(form);
        response.setEmployeeCase(employeeCase);
        return response;
    }

    @Override
    public ListArchivesEmployeesResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd) {

        ListArchivesEmployeesResponse response = new ListArchivesEmployeesResponse();

        ListOrganizationContactCommand orgCommand = new ListOrganizationContactCommand();
        orgCommand.setOrganizationId(cmd.getOrganizationId());
        orgCommand.setCheckInTimeStart(cmd.getCheckInTimeStart());
        orgCommand.setCheckInTimeEnd(cmd.getCheckInTimeEnd());
        orgCommand.setEmploymentTimeStart(cmd.getEmploymentTimeStart());
        orgCommand.setEmploymentTimeEnd(cmd.getEmploymentTimeEnd());
        orgCommand.setContractEndTimeStart(cmd.getContractTimeStart());
        orgCommand.setContractEndTimeEnd(cmd.getContractTimeEnd());
        orgCommand.setEmployeeStatus(cmd.getEmployeeStatus());
        orgCommand.setContractPartyId(cmd.getContractPartyId());
        orgCommand.setKeywords(cmd.getKeywords());
        if (cmd.getDepartmentId() != null)
            orgCommand.setOrganizationId(cmd.getDepartmentId());
        orgCommand.setPageAnchor(cmd.getPageAnchor());
        if (cmd.getPageSize() != null)
            orgCommand.setPageSize(cmd.getPageSize());
        else
            orgCommand.setPageSize(20);
        orgCommand.setFilterScopeTypes(Collections.singletonList(FilterOrganizationContactScopeType.CHILD_ENTERPRISE.getCode()));
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

    /********************    assistant function start    ********************/
    /*
     * 对前端传来的值进行分析并给对应的变量赋值
     */
    private OrganizationMemberDetails convertToEmployeeDetail(OrganizationMemberDetails employee, List<PostApprovalFormItem> itemValues) {
        for (PostApprovalFormItem itemValue : itemValues) {
            if (!GeneralFormFieldAttribute.DEFAULT.getCode().equals(itemValue.getFieldAttribute()))
                continue;
            else {
                switch (itemValue.getFieldName()) {
                    case ArchivesParameter.BIRTHDAY:
                        employee.setBirthday(ArchivesUtil.parseDate(itemValue.getFieldValue()));
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
                        employee.setGender(convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.GENDER));
                        break;
                    case ArchivesParameter.MARITAL_FLAG:
                        employee.setMaritalFlag(convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.MARITAL_FLAG));
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
                        employee.setEmployeeType(convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.EMPLOYEE_TYPE));
                        break;
                    case ArchivesParameter.EMPLOYEE_STATUS:
                        employee.setEmployeeStatus(convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.EMPLOYEE_STATUS));
                        break;
                    case ArchivesParameter.EMPLOYMEN_TTIME:
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
                        break;
                    case ArchivesParameter.PROCREATIVE:
                        employee.setProcreative(ArchivesUtil.parseDate(itemValue.getFieldValue()));
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
                    //  TODO:部门是否为另外的处理
                    /*        if(cmd.getDepartment() !=null)
                                   employee.setDepartment(cmd.getDepartment());
                              if(cmd.getJobPosition() !=null)
                                   employee.setJobPosition(cmd.getJobPosition());
                              if(cmd.getReportTarget() !=null)
                                   employee.setReportTarget(cmd.getReportTarget());*/
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
     * 给系统字段赋值，利用 map 设置 key 来存取值
     */
    private Map<String, String> handleEmployeeDefaultVal(OrganizationMemberDetails employee) {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put(ArchivesParameter.CONTACT_NAME, employee.getContactName());
        valueMap.put(ArchivesParameter.EN_NAME, employee.getEnName());
        valueMap.put(ArchivesParameter.GENDER, convertToArchivesInfo(employee.getGender(), ArchivesParameter.GENDER));
        if (employee.getBirthday() != null)
            valueMap.put(ArchivesParameter.BIRTHDAY, String.valueOf(employee.getBirthday()));
        valueMap.put(ArchivesParameter.MARITAL_FLAG, convertToArchivesInfo(employee.getMaritalFlag(), ArchivesParameter.MARITAL_FLAG));
        if (employee.getProcreative() != null)
            valueMap.put(ArchivesParameter.PROCREATIVE, String.valueOf(employee.getProcreative()));
        valueMap.put(ArchivesParameter.ETHNICITY, employee.getEthnicity());
        valueMap.put(ArchivesParameter.POLITICAL_FLAG, employee.getPoliticalFlag());
        valueMap.put(ArchivesParameter.NATIVE_PLACE, employee.getNativePlace());
        valueMap.put(ArchivesParameter.ID_TYPE, employee.getIdType());
        valueMap.put(ArchivesParameter.ID_NUMBER, employee.getIdNumber());
        if (employee.getIdExpiryDate() != null)
            valueMap.put(ArchivesParameter.ID_EXPIRY_DATE, String.valueOf(employee.getIdExpiryDate()));
        valueMap.put(ArchivesParameter.DEGREE, employee.getDegree());
        valueMap.put(ArchivesParameter.GRADUATION_SCHOOL, employee.getGraduationSchool());
        if (employee.getGraduationTime() != null)
            valueMap.put(ArchivesParameter.GRADUATION_TIME, String.valueOf(employee.getGraduationTime()));
        valueMap.put(ArchivesParameter.CONTACT_TOKEN, employee.getContactToken());
        valueMap.put(ArchivesParameter.EMAIL, employee.getEmail());
        valueMap.put(ArchivesParameter.WECHAT, employee.getWechat());
        valueMap.put(ArchivesParameter.QQ, employee.getQq());
        valueMap.put(ArchivesParameter.ADDRESS, employee.getAddress());
        valueMap.put(ArchivesParameter.EMERGENCY_NAME, employee.getEmergencyName());
        valueMap.put(ArchivesParameter.EMERGENCY_RELATIONSHIP, employee.getEmergencyRelationship());
        valueMap.put(ArchivesParameter.EMERGENCY_CONTACT, employee.getEmergencyContact());
        if (employee.getCheckInTime() != null)
            valueMap.put(ArchivesParameter.CHECK_IN_TIME, String.valueOf(employee.getCheckInTime()));
        valueMap.put(ArchivesParameter.EMPLOYEE_TYPE, convertToArchivesInfo(employee.getEmployeeType(), ArchivesParameter.EMPLOYEE_TYPE));
        valueMap.put(ArchivesParameter.EMPLOYEE_STATUS, convertToArchivesInfo(employee.getEmployeeStatus(), ArchivesParameter.EMPLOYEE_STATUS));
        valueMap.put(ArchivesParameter.EMPLOYMEN_TTIME, String.valueOf(employee.getEmploymentTime()));
        valueMap.put(ArchivesParameter.DEPARTMENT, employee.getDepartment());
        valueMap.put(ArchivesParameter.JOB_POSITION, employee.getJobPosition());
        valueMap.put(ArchivesParameter.JOB_LEVEL, employee.getJobLevel());
        valueMap.put(ArchivesParameter.EMPLOYEE_NO, employee.getEmployeeNo());
        valueMap.put(ArchivesParameter.CONTACT_SHORT_TOKEN, employee.getContactShortToken());
        valueMap.put(ArchivesParameter.WORK_EMAIL, employee.getWorkEmail());
        valueMap.put(ArchivesParameter.CONTRACT_PARTY_ID, convertToArchivesInfo(employee.getContractPartyId(), ArchivesParameter.CONTRACT_PARTY_ID));
        if (employee.getWorkStartTime() != null)
            valueMap.put(ArchivesParameter.WORK_START_TIME, String.valueOf(employee.getWorkStartTime()));
        if (employee.getContractStartTime() != null)
            valueMap.put(ArchivesParameter.CONTRACT_START_TIME, String.valueOf(employee.getContractStartTime()));
        if (employee.getContractEndTime() != null)
            valueMap.put(ArchivesParameter.CONTRACT_END_TIME, String.valueOf(employee.getContractEndTime()));
        valueMap.put(ArchivesParameter.SALARY_CARD_NUMBER, employee.getSalaryCardNumber());
        valueMap.put(ArchivesParameter.SALARY_CARD_BANK, employee.getSalaryCardBank());
        valueMap.put(ArchivesParameter.SOCIAL_SECURITY_NUMBER, employee.getSocialSecurityNumber());
        valueMap.put(ArchivesParameter.PROVIDENT_FUND_NUMBER, employee.getProvidentFundNumber());
        valueMap.put(ArchivesParameter.REG_RESIDENCE_TYPE, employee.getRegResidenceType());
        valueMap.put(ArchivesParameter.REG_RESIDENCE, employee.getRegResidence());
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

    /**
     * 将文字转化为枚举值
     */
    private Byte convertToArchivesEnum(String str, String type) {
        if (type.equals(ArchivesParameter.GENDER)) {
            if ("男".equals(str))
                return 1;
            else if ("女".equals(str))
                return 2;
        }

        if (type.equals(ArchivesParameter.MARITAL_FLAG)) {
            if ("已婚".equals(str))
                return 1;
            else if ("未婚".equals(str))
                return 2;
            else if ("离异".equals(str))
                return 3;
            else
                return 0;
        }

        if (type.equals(ArchivesParameter.EMPLOYEE_TYPE)) {
            if ("全职".equals(str))
                return 0;
            else if ("兼职".equals(str))
                return 1;
            else if ("实习".equals(str))
                return 2;
            else if ("劳动派遣".equals(str))
                return 3;
        }

        if (type.equals(ArchivesParameter.EMPLOYEE_STATUS)) {
            if ("试用".equals(str))
                return 0;
            else if ("在职".equals(str))
                return 1;
            else if ("实习".equals(str))
                return 2;
            else
                return 1;
        }

        return null;
    }

    /**
     * 将特定标记符处理为文字信息
     */
    private String convertToArchivesInfo(Object obj, String type) {
        if (type.equals(ArchivesParameter.GENDER)) {
            Byte gender = (Byte) obj;
            if (StringUtils.isEmpty(gender))
                return "";
            else if (gender.equals(UserGender.MALE.getCode()))
                return "男";
            else if (gender.equals(UserGender.FEMALE.getCode()))
                return "女";
            else
                return "";
        }

        if (type.equals(ArchivesParameter.MARITAL_FLAG)) {
            Byte maritalFlag = (Byte) obj;
            if (StringUtils.isEmpty(maritalFlag))
                return "";
            else if (maritalFlag.equals(MaritalFlag.UNDISCLOSURED.getCode()))
                return "保密";
            else if (maritalFlag.equals(MaritalFlag.MARRIED.getCode()))
                return "已婚";
            else if (maritalFlag.equals(MaritalFlag.UNMARRIED.getCode()))
                return "未婚";
            else if (maritalFlag.equals(MaritalFlag.DIVORCE.getCode()))
                return "离异";
        }

        if (type.equals(ArchivesParameter.EMPLOYEE_TYPE)) {
            Byte employeeType = (Byte) obj;
            if (StringUtils.isEmpty(employeeType))
                return "";
            else if (employeeType.equals(EmployeeType.FULLTIME.getCode()))
                return "全职";
            else if (employeeType.equals(EmployeeType.PARTTIME.getCode()))
                return "兼职";
            else if (employeeType.equals(EmployeeType.INTERSHIP.getCode()))
                return "实习";
            else if (employeeType.equals(EmployeeType.LABORDISPATCH.getCode()))
                return "劳动派遣";
        }

        if (type.equals(ArchivesParameter.EMPLOYEE_STATUS)) {
            Byte employeeStatus = (Byte) obj;
            if (StringUtils.isEmpty(employeeStatus))
                return "";
            else if (employeeStatus.equals(EmployeeStatus.PROBATION.getCode()))
                return "试用";
            else if (employeeStatus.equals(EmployeeStatus.ON_THE_JOB.getCode()))
                return "在职";
            else if (employeeStatus.equals(EmployeeStatus.INTERSHIP.getCode()))
                return "实习";
        }

        if (type.equals(ArchivesParameter.DEPARTMENT)) {
            List<OrganizationDTO> departments = (List<OrganizationDTO>) obj;
            if (departments != null && departments.size() > 0) {
                String departmentString = "";
                for (OrganizationDTO depDTO : departments) {
                    departmentString += depDTO.getName() + ",";
                }
                departmentString = departmentString.substring(0, departmentString.length() - 1);
                return departmentString;
            }
        }

        if (type.equals(ArchivesParameter.DEPARTMENT_IDS)) {
            List<Long> ids = (List<Long>) obj;
            String departmentName = "";
            if (ids != null && ids.size() > 0) {
                for (Long id : ids) {
                    Organization org = organizationProvider.findOrganizationById(id);
                    if (org != null) {
                        departmentName += org.getName() + ",";
                    }
                }
                departmentName = departmentName.substring(0, departmentName.length() - 1);
                return departmentName;
            }
        }

        if (type.equals(ArchivesParameter.CONTRACT_PARTY_ID)) {
            if (obj != null) {
                Long id = (Long) obj;
                Organization org = organizationProvider.findOrganizationById(id);
                if (org != null) {
                    return org.getName();
                }
            }
        }

        if (type.equals(ArchivesParameter.DISMISS_REASON)) {
            Byte dismissReason = (Byte) obj;
            if (dismissReason.equals(ArchivesDismissReason.SALARY.getCode()))
                return "薪资";
            else if (dismissReason.equals(ArchivesDismissReason.CULTURE.getCode()))
                return "文化";
            else if (dismissReason.equals(ArchivesDismissReason.BALANCE.getCode()))
                return "生活平衡";
            else if (dismissReason.equals(ArchivesDismissReason.PERSONAL_REASON.getCode()))
                return "个人原因";
            else if (dismissReason.equals(ArchivesDismissReason.CAREER_DEVELOPMENT.getCode()))
                return "职业发展";
            else if (dismissReason.equals(ArchivesDismissReason.FIRE.getCode()))
                return "不胜任";
            else if (dismissReason.equals(ArchivesDismissReason.ADJUSTMENT.getCode()))
                return "编制调整";
            else if (dismissReason.equals(ArchivesDismissReason.BREAK_RULE.getCode()))
                return "违纪";
            else if (dismissReason.equals(ArchivesDismissReason.OTHER.getCode()))
                return "其他";
        }

        return "";
    }

    /********************    assistant function end    ********************/

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
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_TIME.ge(cmd.getDismissTimeStart()));
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_TIME.le(cmd.getDismissTimeEnd()));
        }

        //   入职日期判断
        if (cmd.getCheckInTimeStart() != null && cmd.getCheckInTimeEnd() != null) {
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.CHECK_IN_TIME.ge(cmd.getCheckInTimeStart()));
            condition = condition.and(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.CHECK_IN_TIME.le(cmd.getCheckInTimeEnd()));
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

    //  执行定时配置项
    @Override
    public void executeArchivesConfiguration() {
        List<ArchivesConfigurations> configurations = archivesProvider.listArchivesConfigurations(ArchivesUtil.currentDate());
        if (configurations != null && configurations.size() > 0) {
            for (ArchivesConfigurations configuration : configurations) {
                if (configuration.getOperationType().equals(ArchivesOperationType.EMPLOY.getCode())) {
                    EmployArchivesEmployeesCommand cmd = (EmployArchivesEmployeesCommand) StringHelper.fromJsonString(configuration.getOperationInformation(), EmployArchivesEmployeesCommand.class);
                    employArchivesEmployees(cmd);
                } else if (configuration.getOperationType().equals(ArchivesOperationType.TRANSFER.getCode())) {
                    TransferArchivesEmployeesCommand cmd = (TransferArchivesEmployeesCommand) StringHelper.fromJsonString(configuration.getOperationInformation(), TransferArchivesEmployeesCommand.class);
                    transferArchivesEmployees(cmd);
                } else if (configuration.getOperationType().equals(ArchivesOperationType.DISMISS.getCode())) {
                    DismissArchivesEmployeesCommand cmd = (DismissArchivesEmployeesCommand) StringHelper.fromJsonString(configuration.getOperationInformation(), DismissArchivesEmployeesCommand.class);
                    dismissArchivesEmployees(cmd);
                }
            }
        }
    }

    @Override
    public void employArchivesEmployeesConfig(EmployArchivesEmployeesCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            //  1.更新员工转正时间
            for (Long detailId : cmd.getDetailIds()) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                detail.setEmploymentTime(cmd.getEmploymentTime());
                organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
            }
            //  2.若为当前日期则立即执行
            if (cmd.getEmploymentTime().toString().equals(ArchivesUtil.currentDate().toString()))
                employArchivesEmployees(cmd);
            else {
                //  3.若为其它时间则增加转正配置
                ArchivesConfigurations configuration = new ArchivesConfigurations();
                configuration.setOrganizationId(cmd.getOrganizationId());
                configuration.setOperationType(ArchivesOperationType.EMPLOY.getCode());
                configuration.setOperationTime(cmd.getEmploymentTime());
                configuration.setOperationInformation(StringHelper.toJsonString(cmd));
                archivesProvider.createArchivesConfigurations(configuration);

            }
            //  4.更新人员变动记录
            employArchivesEmployeesLogs(cmd);
            return null;
        });
    }

    /**
     * 员工转正
     */
    @Override
    public void employArchivesEmployees(EmployArchivesEmployeesCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            for (Long detailId : cmd.getDetailIds()) {
                //  1.更新员工状态
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                detail.setEmployeeStatus(EmployeeStatus.ON_THE_JOB.getCode());
                detail.setEmploymentTime(cmd.getEmploymentTime());
                organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
            }
            return null;
        });
    }

    @Override
    public void transferArchivesEmployeesConfig(TransferArchivesEmployeesCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            //  1.若为当天则立即执行
            if (cmd.getEffectiveTime().toString().equals(ArchivesUtil.currentDate().toString()))
                transferArchivesEmployees(cmd);
                //  2.若为其它时间则添加调整配置
            else {
                ArchivesConfigurations configuration = new ArchivesConfigurations();
                configuration.setOrganizationId(cmd.getOrganizationId());
                configuration.setOperationType(ArchivesOperationType.TRANSFER.getCode());
                configuration.setOperationTime(cmd.getEffectiveTime());
                configuration.setOperationInformation(StringHelper.toJsonString(cmd));
                archivesProvider.createArchivesConfigurations(configuration);
            }
            //  3.更新人员调整记录
            transferArchivesEmployeesLogs(cmd);
            return null;
        });
    }


    /**
     * 员工部门调整
     */
    public void transferArchivesEmployees(TransferArchivesEmployeesCommand cmd) {
        dbProvider.execute((TransactionStatus status) -> {
            //  调整员工部门、岗位、职级并同步到 detail 表
            organizationService.transferOrganizationPersonels(cmd);
            return null;
        });
    }

    @Override
    public void dismissArchivesEmployeesConfig(DismissArchivesEmployeesCommand cmd) {

        dbProvider.execute((TransactionStatus status) -> {
            //  1.更新员工离职时间
            for (Long detailId : cmd.getDetailIds()) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                detail.setDismissTime(cmd.getDismissTime());
                organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
            }
            //  2.若为当天则立即执行
            if (cmd.getDismissTime().toString().equals(ArchivesUtil.currentDate().toString()))
                dismissArchivesEmployees(cmd);
                //  3.若为其它时间则添加离职配置
            else {
                ArchivesConfigurations configuration = new ArchivesConfigurations();
                configuration.setOrganizationId(cmd.getOrganizationId());
                configuration.setOperationType(ArchivesOperationType.DISMISS.getCode());
                configuration.setOperationTime(cmd.getDismissTime());
                configuration.setOperationInformation(StringHelper.toJsonString(cmd));
                archivesProvider.createArchivesConfigurations(configuration);
            }
            //  3.添加人员离职记录
            dismissArchivesEmployeesLogs(cmd);
            return null;
        });
    }

    /**
     * 员工离职
     */
    public void dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd) {
        //  添加事物
        dbProvider.execute((TransactionStatus status) -> {
            for (Long detailId : cmd.getDetailIds()) {
                //  1.将员工添加到离职人员表
                OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                ArchivesDismissEmployees dismissEmployee = new ArchivesDismissEmployees();
                dismissEmployee.setDetailId(employee.getId());
                dismissEmployee.setNamespaceId(employee.getNamespaceId());
                dismissEmployee.setOrganizationId(employee.getOrganizationId());
                dismissEmployee.setContactName(employee.getContactName());
                dismissEmployee.setEmployeeStatus(employee.getEmployeeStatus());
                dismissEmployee.setDepartment(employee.getDepartment());
                dismissEmployee.setCheckInTime(employee.getCheckInTime());
                dismissEmployee.setDismissTime(cmd.getDismissTime());
                dismissEmployee.setDismissType(cmd.getDismissType());
                dismissEmployee.setDismissReason(cmd.getDismissReason());
                dismissEmployee.setDismissRemarks(cmd.getDismissRemark());
                dismissEmployee.setContractPartyId(employee.getContractPartyId());
                archivesProvider.createArchivesDismissEmployee(dismissEmployee);

                //  2.删除员工权限
                DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
                deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(employee.getOrganizationId());
                deleteOrganizationPersonnelByContactTokenCommand.setContactToken(employee.getContactToken());
                deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
                organizationService.deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);
            }
            return null;
        });
    }

    /**
     * 删除离职表中的员工
     */
    @Override
    public void deleteArchivesDismissEmployees(Long detailId, Long organizationId) {
        ArchivesDismissEmployees dismissEmployee = archivesProvider.getArchivesDismissEmployeesByDetailId(organizationId, detailId);
        if (dismissEmployee != null)
            archivesProvider.deleteArchivesDismissEmployees(dismissEmployee);
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
            }
            return null;
        });
    }

    @Override
    public GeneralFormDTO updateArchivesForm(UpdateArchivesFormCommand cmd) {

        //  1.如果无 formOriginId 时则使用的是模板，此时在组织结构新增一份表单，同时业务表单组新增记录
        //  2.如果有 formOriginId 时则说明已经拥有了表单，此时在组织架构做修改，同时业务表单组同步记录

        if (cmd.getFormOriginId() == 0L) {
            //  1.先在组织架构增加表单
            CreateApprovalFormCommand createCommand = new CreateApprovalFormCommand();
            createCommand.setOwnerId(cmd.getOrganizationId());
            createCommand.setOwnerType(ARCHIVE_OWNER_TYPE);
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
            String value = configurationProvider.getValue(ARCHIVES_FORM_ORIGIN_ID, "");
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
        importFileService.executeTask(new ExecuteImportTaskCallback() {
            @Override
            public ImportFileResponse importFile() {
                ImportFileResponse response = new ImportFileResponse();
                List<ImportArchivesEmployeesDTO> datas = handleImportArchivesEmployees(resultList, form.getFormFields());
                String fileLog;
                if (datas.size() > 0) {
                    //  校验标题，若不合格直接返回错误
                    fileLog = checkArchivesEmployeesTitle(datas.get(0), form.getFormFields());
                    if (!StringUtils.isEmpty(fileLog)) {
                        response.setFileLog(fileLog);
                        return response;
                    }
                    response.setTitle(datas.get(0));
                    datas.remove(0);
                }

                //  开始导入，同时设置导入结果
                importArchivesEmployeesFiles(datas, response, cmd.getFormOriginId(), cmd.getOrganizationId(), cmd.getDepartmentId(), form.getFormFields());
                //  返回结果
                return response;
            }
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
                String value = r.getCells().get(GetExcelLetter(j + 1));
                if (value == null)
                    value = "";
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
                return ImportFileErrorType.TITLE_ERROE.getCode();
            else
                continue;
        }
        return null;
    }

    private void importArchivesEmployeesFiles(
            List<ImportArchivesEmployeesDTO> datas, ImportFileResponse response, Long formOriginId, Long organizationId,
            Long departmentId, List<GeneralFormFieldDTO> formValues) {
        ImportFileResultLog<Map> log = new ImportFileResultLog<>(ArchivesServiceErrorCode.SCOPE);
        List<ImportFileResultLog<Map>> errorDataLogs = new ArrayList<>();
        Long coverCount = 0L;

        for (ImportArchivesEmployeesDTO data : datas) {
            List<PostApprovalFormItem> itemValues = new ArrayList<>();
            String contactName = "", contactToken = "";
            Long realDepartmentId = 0L, realJobPositionId = 0L, realJobLevelId = 0L, detailId = null;
            Byte gender = null;
            boolean flag = false;


            //  1.在校验的时候保存需要单独调用add的值,可以节省一次循环获取的时间
            for (int i = 0; i < formValues.size(); i++) {
                PostApprovalFormItem itemValue = ConvertHelper.convert(formValues.get(i), PostApprovalFormItem.class);
                itemValue.setFieldValue(data.getValues().get(i));
                //  2.校验导入数据
                log = checkArchivesEmployeesDatas(data, itemValue, departmentId, contactName, gender,
                        contactToken, realDepartmentId, realJobPositionId, realJobLevelId);
                if (log != null) {
                    errorDataLogs.add(log);
                    break;
                }
                itemValues.add(itemValue);
            }
            //  3.导入基础数据
            detailId = saveArchivesEmployeesMember(organizationId, contactName, gender, realDepartmentId,
                    realJobPositionId, realJobLevelId, contactToken, flag);
            //  4.导入详细信息
            if (detailId == null)
                continue;
            saveArchivesEmployeesDetail(formOriginId, detailId, organizationId, itemValues);
            //  5.记录重复数据
            if (flag)
                coverCount++;
        }
        //  6.存储所有数据行数
        response.setTotalCount((long) datas.size());
        //  7.存储覆盖数据行数
        response.setCoverCount(coverCount);
        //  8.存储错误数据行数
        response.setFailCount((long) errorDataLogs.size());
        //  9.存储错误数据
        response.setLogs(errorDataLogs);
    }

    private ImportFileResultLog<Map> checkArchivesEmployeesDatas(
            ImportArchivesEmployeesDTO data, PostApprovalFormItem itemValue, Long departmentId, String contactName,
            Byte gender, String contactToken, Long realDepartmentId, Long realJobPositionId, Long realJobLevelId) {
        ImportFileResultLog<Map> log = new ImportFileResultLog<>(ArchivesServiceErrorCode.SCOPE);

        if (ArchivesParameter.CONTACT_NAME.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                LOGGER.warn("Employee name is empty. data = {}", data);
                log.setData(convertListStringToMap(data));
                log.setErrorLog("Employee name is empty.");
                log.setCode(ArchivesServiceErrorCode.ERROR_NAME_ISEMPTY);
                return log;
            } else {
                if (itemValue.getFieldValue().length() > 40) {
                    LOGGER.warn("Employee name is too long. data = {}", data);
                    log.setData(convertListStringToMap(data));
                    log.setErrorLog("Employee name is too long.");
                    log.setCode(ArchivesServiceErrorCode.ERROR_NAME_TOOLONG);
                    return log;
                } else
                    contactName = itemValue.getFieldValue();
            }
        }

        if (ArchivesParameter.CONTACT_TOKEN.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                LOGGER.warn("Employee token is empty. data = {}", data);
                log.setData(convertListStringToMap(data));
                log.setErrorLog("Employee token is empty.");
                log.setCode(ArchivesServiceErrorCode.ERROR_CONTACT_TOKEN_ISEMPTY);
                return log;
            } else {
                if (itemValue.getFieldValue().length() != 11) {
                    LOGGER.warn("Employee token wrong format. data = {}", data);
                    log.setData(convertListStringToMap(data));
                    log.setErrorLog("Employee token wrong format.");
                    log.setCode(ArchivesServiceErrorCode.ERROR_CONTACT_TOKEN_WRONGFORMAT);
                } else {
                    contactToken = itemValue.getFieldValue();
                }
            }
        }

        if (ArchivesParameter.GENDER.equals(itemValue.getFieldName())) {
            gender = convertToArchivesEnum(itemValue.getFieldValue(), ArchivesParameter.GENDER);
        }

        if (ArchivesParameter.CHECK_IN_TIME.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                LOGGER.warn("Employee checkInTime is empty. data = {}", data);
                log.setData(convertListStringToMap(data));
                log.setErrorLog("Employee checkInTime is empty.");
                log.setCode(ArchivesServiceErrorCode.ERROR_CHECK_IN_TIME_ISEMPTY);
                return log;
            }
        }

        if (ArchivesParameter.EMPLOYEE_TYPE.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                LOGGER.warn("Employee employeeType is empty. data = {}", data);
                log.setData(convertListStringToMap(data));
                log.setErrorLog("Employee employeeType is empty.");
                log.setCode(ArchivesServiceErrorCode.ERROR_EMPLOYEE_TYPE_ISEMPTY);
                return log;
            }
        }

        //  在 check 阶段就把部门、岗位和职级的 id 找到
        if (ArchivesParameter.DEPARTMENT.equals(itemValue.getFieldName())) {
            if (StringUtils.isEmpty(itemValue.getFieldValue())) {
                realDepartmentId = departmentId;
            } else {
                realDepartmentId = organizationService.getOrganizationNameByNameAndType(itemValue.getFieldValue(), OrganizationGroupType.DEPARTMENT.getCode());
            }
        }

        if (ArchivesParameter.JOB_POSITION.equals(itemValue.getFieldName())) {
            if (!StringUtils.isEmpty(itemValue.getFieldValue()))
                realJobPositionId = organizationService.getOrganizationNameByNameAndType(itemValue.getFieldValue(), OrganizationGroupType.JOB_POSITION.getCode());
        }

        if (ArchivesParameter.JOB_LEVEL.equals(itemValue.getFieldName())) {
            if (!StringUtils.isEmpty(itemValue.getFieldValue()))
                realJobLevelId = organizationService.getOrganizationNameByNameAndType(itemValue.getFieldValue(), OrganizationGroupType.JOB_LEVEL.getCode());
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

    private Long saveArchivesEmployeesMember(
            Long organizationId, String contactName, Byte gender, Long departmentId, Long jobPositionId,
            Long jobLevelId, String contactToken, boolean flag) {
        AddArchivesEmployeeCommand addCommand = new AddArchivesEmployeeCommand();
        addCommand.setOrganizationId(organizationId);
        addCommand.setContactName(contactName);
        addCommand.setGender(gender);

        //  部门、岗位、职级在 check 阶段获取 id 值
        List<Long> departmentIds = new ArrayList<>();
        departmentIds.add(departmentId);
        addCommand.setDepartmentIds(departmentIds);
        List<Long> jobPositionIds = new ArrayList<>();
        jobPositionIds.add(jobPositionId);
        addCommand.setJobPositionIds(jobPositionIds);
        List<Long> jobLevelIds = new ArrayList<>();
        jobLevelIds.add(jobLevelId);
        addCommand.setJobLevelIds(jobLevelIds);
        addCommand.setContactToken(contactToken);

        ArchivesEmployeeDTO dto = addArchivesEmployee(addCommand);
        flag = verifyPersonnelByPhone(organizationId, addCommand.getContactToken());
        Long detailId = null;
        if (dto != null)
            detailId = dto.getDetailId();
        return detailId;
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
    public void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd, HttpServletResponse httpResponse) {

        //  此处的数据类型不好调用晓强哥的 ExcelUtil, 所以使用原始的导出方法
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  1.设置导出标题
        List<String> titles = form.getFormFields().stream().map(r -> {
            return r.getFieldDisplayName();
        }).collect(Collectors.toList());

        //  2.设置导出变量值
        List<Long> detailIds = organizationService.ListDetailsByEnterpriseId(cmd.getOrganizationId());

        List<ExportArchivesEmployeesDTO> values = new ArrayList<>();
        for (Long detailId : detailIds) {
            ExportArchivesEmployeesDTO dto = new ExportArchivesEmployeesDTO();
            GetArchivesEmployeeCommand getCommand = new GetArchivesEmployeeCommand(cmd.getFormOriginId(), cmd.getOrganizationId(), detailId);
            GetArchivesEmployeeResponse response = getArchivesEmployee(getCommand);
            List<String> employeeValues = response.getForm().getFormFields().stream().map(r -> {
                return r.getFieldValue();
            }).collect(Collectors.toList());
            dto.setVals(employeeValues);
            values.add(dto);
        }
        XSSFWorkbook workbook = this.exportArchivesEmployeesFiles(titles, values);
        writeExcel(workbook, httpResponse);
    }

    private XSSFWorkbook exportArchivesEmployeesFiles(List<String> titles, List<ExportArchivesEmployeesDTO> values) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("人员档案列表");
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

    private void writeExcel(XSSFWorkbook workbook, HttpServletResponse httpResponse) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            workbook.write(out);
            String fileName = "人员档案列表.xlsx";
            httpResponse.setContentType("application/msexcel");
            httpResponse.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
            //response.addHeader("Content-Length", "" + out.);
            OutputStream excelStream = new BufferedOutputStream(httpResponse.getOutputStream());
            httpResponse.setContentType("application/msexcel");
            excelStream.write(out.toByteArray());
            excelStream.flush();
            excelStream.close();
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally {
            try {
                workbook.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }

    @Override
    public void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse) {
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand();
        formCommand.setFormOriginId(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);
        ExcelUtils excelUtils = new ExcelUtils(httpResponse, "人员档案导入模板", "人员档案导入模板");
        List<String> titleNames = form.getFormFields().stream().map(r -> {
            String name = r.getFieldDisplayName();
            return name;
        }).collect(Collectors.toList());
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
        excelUtils.setTitleRemark("填写须知：\n" +
                "    1、请不要对员工信息类别进行增加、删除或修改，以免无法识别员工信息；\n" +
                "    2、Excel中红色字段为必填字段,黑色字段为选填字段\n" +
                "    3、请不要包含公式，以免错误识别员工信息；\n" +
                "    4、多次导入时，若系统中已存在相同手机号码的员工，将以导入的信息为准；\n" +
                "    5、部门：上下级部门间用‘/'隔开，且从最上级部门开始，例如\"左邻/深圳研发中心/研发部\"；部门若为空，则自动将成员添加到选择的目录下；\n" +
                "    6、手机：支持国内、国际手机号（国内手机号直接输入手机号即可；国际手机号必须包含加号以及国家地区码，格式示例：“+85259****24”）；\n" +
                "    7、合同公司：合同公司若为空，将默认使用公司全称\n" +
                "    8、若要删除某行信息，请右键行号，选择删除", (short) 18, (short) 4480);
        excelUtils.setNeedSequenceColumn(false);
        excelUtils.setNeedTitleRemark(true);
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

    @Override
    public ImportFileResponse<ImportArchivesEmployeesDTO> getImportEmployeesResult(GetImportFileResultCommand cmd) {
        return importFileService.getImportFileResult(cmd.getTaskId());
    }

    private boolean verifyPersonnelByPhone(Long organizationId, String contactToken) {
        //  TODO:手机号存在的话则累积数目+1
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
            log.setCode(ArchivesServiceErrorCode.ERROR_NAME_ISEMPTY);
            return false;
        } else if (contactName.length() > 20) {
            LOGGER.warn("Contact name is too long. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name too long.");
            log.setCode(ArchivesServiceErrorCode.ERROR_NAME_TOOLONG);
            return false;
        }else if(Pattern.matches("/^[\\u4E00-\\u9FA5A-Za-z0-9_]+$/", contactName)){
            LOGGER.warn("Contact name wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name wrong format.");
            log.setCode(ArchivesServiceErrorCode.ERROR_NAME_WRONGFORMAT);
            return false;
        }else
            return true;
    }

    private <T> boolean checkArchivesContactToken(ImportFileResultLog<T> log, T data, String contactToken) {
        if (StringUtils.isEmpty(contactToken)) {
            LOGGER.warn("Contact token is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token is empty");
            log.setCode(ArchivesServiceErrorCode.ERROR_CONTACT_TOKEN_ISEMPTY);
            return false;
        } else if (Pattern.matches("/^1\\d{10}$/",getRealContactToken(contactToken,ArchivesParameter.CONTACT_TOKEN))) {
            LOGGER.warn("Contact token wrong format. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token wrong format");
            log.setCode(ArchivesServiceErrorCode.ERROR_CONTACT_TOKEN_WRONGFORMAT);
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
                log.setCode(ArchivesServiceErrorCode.ERROR_DEPARTMENT_NOT_FOUND);
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
                log.setCode(ArchivesServiceErrorCode.ERROR_JOB_POSITION_NOT_FOUND);
                return false;
            }
            return true;
        } else
            return true;
    }

    /********************    import function end    ********************/

    @Override
    public void remindArchivesEmployee(RemindArchivesEmployeeCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ArchivesNotifications originNotify = archivesProvider.findArchivesNotificationsByOrganizationId(namespaceId, cmd.getOrganizationId());
        if (originNotify == null) {
            ArchivesNotifications newNotify = new ArchivesNotifications();
            newNotify.setNamespaceId(namespaceId);
            newNotify.setOrganizationId(cmd.getOrganizationId());
            newNotify.setNotifyDay(cmd.getRemindDay());
            newNotify.setNotifyHour(cmd.getRemindTime());
            newNotify.setNotifyEmails(JSON.toJSONString(cmd.getRemindEmails()));
            archivesProvider.createArchivesNotifications(newNotify);
        } else {
            originNotify.setNotifyEmails(JSON.toJSONString(cmd.getRemindEmails()));
            originNotify.setNotifyDay(cmd.getRemindDay());
            originNotify.setNotifyHour(cmd.getRemindTime());
            archivesProvider.updateArchivesNotifications(originNotify);
        }
    }

    //    @Scheduled(cron = "0 0 * * * ?")
    public void executeArchivesNotification() {
        Calendar c = Calendar.getInstance();
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        List<ArchivesNotifications> results = archivesProvider.listArchivesNotificationsByWeek(weekDay);
        if (results != null && results.size() > 0) {
            //  按照时间归类，来启动对应时间点的定时器
            Map<Integer, List<ArchivesNotifications>> notifyMap = results.stream().collect(Collectors.groupingBy
                    (ArchivesNotifications::getNotifyHour));
            for (Integer key : notifyMap.keySet()) {
                archivesConfigurationService.sendingMail(notifyMap.get(key));
            }
        }
    }
}
