package com.everhomes.archives;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.organization.*;
import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class ArchivesServiceImpl implements ArchivesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesServiceImpl.class);

    private static final String ARCHIVES = "archives_information";

    @Autowired
    ArchivesProvider archivesProvider;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationProvider organizationProvider;

    @Autowired
    ImportFileService importFileService;

    @Autowired
    GeneralFormService generalFormService;

    @Autowired
    ConfigurationProvider configurationProvider;

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
        addCommand.setVisibleFlag(cmd.getVisibleFlag());
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  获得 detailId 然后处理其它信息
        Long detailId = null;
        if (memberDTO != null)
            detailId = memberDTO.getDetailId();
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (memberDetail != null) {
            memberDetail.setEnName(cmd.getContactEnName());
            memberDetail.setRegionCode(cmd.getRegionCode());
            memberDetail.setContactShortToken(cmd.getContactShortToken());
            memberDetail.setDepartment(getDepartmentName(cmd.getDepartmentIds()));
            //  TODO:职位有可能修改
            memberDetail.setJobPosition(cmd.getJobPosition());
            memberDetail.setWorkEmail(cmd.getWorkEmail());
            organizationProvider.updateOrganizationMemberDetails(memberDetail, memberDetail.getId());
            dto.setDetailId(detailId);
            dto.setContactName(memberDetail.getContactName());
            dto.setContactToken(memberDetail.getContactToken());
        }

        //  TODO: 添加档案记录

        return dto;
    }

    //  获取部门名称
    private String getDepartmentName(List<Long> ids) {
        String departmentName = "";
        for (Long id : ids) {
            Organization org = organizationProvider.findOrganizationById(id);
            if (org != null) {
                departmentName += org.getName() + ",";
            }
        }
        departmentName = departmentName.substring(0, departmentName.length() - 1);
        return departmentName;
    }

    @Override
    public void transferArchivesContacts(TransferArchivesContactsCommand cmd) {
        if (cmd.getDetailIds() != null) {
            TransferArchivesEmployeesCommand transferCommand = new TransferArchivesEmployeesCommand();
            transferCommand.setOrganizationId(cmd.getOrganizationId());
            transferCommand.setDetailIds(cmd.getDetailIds());
            transferCommand.setDepartmentIds(cmd.getDepartmentIds());
            organizationService.transferOrganizationPersonels(transferCommand);

            //  同步部门名称
            for (Long detailId : cmd.getDetailIds()) {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                detail.setDepartment(getDepartmentName(cmd.getDepartmentIds()));
                organizationProvider.updateOrganizationMemberDetails(detail,detail.getId());
            }
            // TODO: 循环添加档案记录
        }
    }

    @Override
    public void deleteArchivesContacts(DeleteArchivesContactsCommand cmd) {
        if (cmd.getDetailIds() != null) {
            for (Long detailId : cmd.getDetailIds()) {
                //  组织架构删除
                OrganizationMemberDetails detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
                deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(cmd.getOrganizationId());
                deleteOrganizationPersonnelByContactTokenCommand.setContactToken(detail.getContactToken());
                deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
                organizationService.deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);
                // TODO: 循环添加档案记录
            }
        }
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
     * @return
     */
    @Override
    public ListArchivesContactsResponse listArchivesContacts(ListArchivesContactsCommand cmd) {

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListArchivesContactsResponse response = new ListArchivesContactsResponse();
        final Integer stickCount = 10;  //  置顶数为10,表示一页最多显示10个置顶人员
        if (cmd.getPageSize() != null)
            cmd.setPageSize(cmd.getPageSize());
        else
            cmd.setPageSize(20);

        List<Long> detailIds = archivesProvider.listArchivesStickyContactsIds(namespaceId, cmd.getOrganizationId(), stickCount);    //  保存置顶人员
        if (!StringUtils.isEmpty(cmd.getKeywords())) {
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
        //  TODO: 职位的获取待确定后在修改
        dto.setJobPositions(detail.getJobPosition());

        //  查询部门
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        Organization directlyEnterprise = organizationProvider.findOrganizationById(detail.getOrganizationId());
        dto.setDepartments(organizationService.getOrganizationMemberGroups(groupTypes, dto.getContactToken(), directlyEnterprise.getPath()));

/*        //  查询职位
        List<OrganizationDTO> positions = organizationService.getOrganizationMemberGroups(OrganizationGroupType.JOB_POSITION, dto.getContactToken(), directlyEnterprise.getPath());
        String jobPositions = "";
        if(positions!=null){
            for(OrganizationDTO : positions)
        }*/

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
        if (detailIds!=null && detailIds.size() >0)
            orgCommand.setExceptIds(detailIds);
        if (!StringUtils.isEmpty(cmd.getKeywords()))
            orgCommand.setKeywords(cmd.getKeywords());
        ListOrganizationMemberCommandResponse members = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);
        members.getMembers().forEach(r -> {
            ArchivesContactDTO dto = new ArchivesContactDTO();
            dto.setDetailId(r.getDetailId());
            dto.setOrganizationId(r.getOrganizationId());
            dto.setTargetId(r.getTargetId());
            dto.setTargetType(r.getTargetType());
            dto.setContactName(r.getContactName());
            dto.setDepartments(r.getDepartments());
            dto.setGender(r.getGender());
            dto.setRegionCode(r.getRegionCode());
            dto.setContactToken(r.getContactToken());
            dto.setWorkEmail(r.getWorkEmail());
            //  TODO:组织架构list接口多返回邮箱
//                dto.setEmail(r.getEmail);
            dto.setStick("0");
            contacts.add(dto);
        });
        response.setNextPageAnchor(members.getNextPageAnchor());
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
        task.setOwnerType("organization");
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

        ImportFileResultLog<ImportArchivesContactsDTO> log = new ImportFileResultLog<>(ArchivesServiceErrorCode.SCOPE);
        List<ImportFileResultLog<ImportArchivesContactsDTO>> errorDataLogs = new ArrayList<>();
        Long coverCount = 0L;
        for (ImportArchivesContactsDTO data : datas) {
            //  1.校验数据
            log = checkArchivesContactsdatas(data);
            if (log != null) {
                errorDataLogs.add(log);
                continue;
            }
            //  2.导入数据库
            boolean flag = saveArchivesContactsdatas(data, organizationId, departmentId);
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

    private ImportFileResultLog<ImportArchivesContactsDTO> checkArchivesContactsdatas(ImportArchivesContactsDTO data) {

        ImportFileResultLog<ImportArchivesContactsDTO> log = new ImportFileResultLog<>(ArchivesServiceErrorCode.SCOPE);

        //  姓名
        if (!StringUtils.isEmpty(data.getContactName())) {
            LOGGER.warn("Contact name is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name is empty.");
            log.setCode(ArchivesServiceErrorCode.ERROR_NAME_ISEMPTY);
            return log;
        } else if (data.getContactEnName().length() > 20) {
            LOGGER.warn("Contact name is too long. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact name too long.");
            log.setCode(ArchivesServiceErrorCode.ERROR_NAME_TOOLONG);
            return log;
        }

        //  TODO:英文名校验


        if (StringUtils.isEmpty(data.getContactToken())) {
            LOGGER.warn("Contact token is empty. data = {}", data);
            log.setData(data);
            log.setErrorLog("Contact token is empty");
            log.setCode(ArchivesServiceErrorCode.ERROR_CONTACTTOKEN_ISEMPTY);
            return log;
        }
        return null;
    }

    private boolean saveArchivesContactsdatas(ImportArchivesContactsDTO data, Long organizationId, Long departmentId) {
        AddArchivesContactCommand addCommand = new AddArchivesContactCommand();
        addCommand.setOrganizationId(organizationId);
        addCommand.setContactName(data.getContactName());
        addCommand.setContactEnName(data.getContactEnName());
        //  性别
        Byte gender;
        if (data.getGender().trim().equals("男")) {
            gender = 1;
        } else {
            gender = 2;
        }
        addCommand.setGender(gender);
        addCommand.setContactShortToken(data.getContactShortToken());
        addCommand.setRegionCode(getRealContactToken(data.getContactToken(), "regionCode"));
        addCommand.setContactToken(getRealContactToken(data.getContactToken(), "contactToken"));
        addCommand.setWorkEmail(data.getWorkEmail());
        if (StringUtils.isEmpty(data.getDepartment())) {
            addCommand.setDepartmentIds(Arrays.asList(departmentId));
//            addArchivesContact()
        }
        //  TODO:部门、岗位中文查询
        addCommand.setVisibleFlag(VisibleFlag.SHOW.getCode());
        //  TODO:手机号存在的话则累积数目+1
        VerifyPersonnelByPhoneCommand verifyCommand = new VerifyPersonnelByPhoneCommand();
        verifyCommand.setEnterpriseId(organizationId);
        verifyCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        verifyCommand.setPhone(addCommand.getContactToken());
        VerifyPersonnelByPhoneCommandResponse verifyRes = organizationService.verifyPersonnelByPhone(verifyCommand);

        if (null != verifyRes && null != verifyRes.getDto()) {
            addArchivesContact(addCommand);
            return true;
        } else
            return false;
    }

    private String getRealContactToken(String tokens, String type) {
        String token[] = tokens.split(" ");
        token[0] = token[0].substring(1, token[0].length());
        if (type.equals("contactToken"))
            return token[1];
        else
            return token[0];
    }

    @Override
    public void exportArchivesContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse) {
        ListArchivesContactsCommand listCommand = new ListArchivesContactsCommand();
        listCommand.setOrganizationId(cmd.getOrganizationId());
        listCommand.setKeywords(cmd.getKeywords());
        listCommand.setPageSize(10000);
        ListArchivesContactsResponse response = listArchivesContacts(listCommand);
        if(response.getContacts() !=null && response.getContacts().size()>0){
            List<ArchivesContactDTO> contacts = response.getContacts().stream().map(r ->{
                ArchivesContactDTO dto = convertArchivesContactForExcel(r);
                return dto;
            }).collect(Collectors.toList());
            String fileName = "通讯录成员列表";
            ExcelUtils excelUtils = new ExcelUtils(httpResponse,fileName,"通讯录成员列表");
            List<String> propertyNames = new ArrayList<String>(Arrays.asList("contactName", "genderString", "contactToken",
                    "contactShortToken", "workEmail", "departmentString", "jobPositionString"));
            List<String> titleNames = new ArrayList<String>(Arrays.asList("姓名", "性别", "手机", "短号", "工作邮箱", "部门", "职务"));
            List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 10, 20, 20, 20, 20, 20));
            excelUtils.setNeedSequenceColumn(false);
            excelUtils.writeExcel(propertyNames,titleNames,titleSizes,contacts);
        }
    }

    private ArchivesContactDTO convertArchivesContactForExcel(ArchivesContactDTO dto) {

        if (dto.getGender().equals(UserGender.MALE.getCode()))
            dto.setGenderString("男");
        else
            dto.setGenderString("女");

        if (dto.getDepartments()!=null && dto.getDepartments().size()>0){
            String departmentString ="";
            for(OrganizationDTO depDTO: dto.getDepartments()){
                departmentString += depDTO.getName() + ",";
            }
            departmentString = departmentString.substring(0,departmentString.length()-1);
            dto.setDepartmentString(departmentString);
        }
        //  TODO:岗位的导出

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
    public ArchivesEmployeeDTO addArchivesEmployee(AddArchivesEmployeeCommand cmd) {

        ArchivesEmployeeDTO dto = new ArchivesEmployeeDTO();

        //  1.组织架构添加人员
        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setGender(cmd.getGender());
        addCommand.setCheckInTime(cmd.getCheckInTime());
        addCommand.setEmployeeType(cmd.getEmployeeType());
        addCommand.setEmployeeStatus(cmd.getEmployeeStatus());
        addCommand.setEmploymentTime(cmd.getEmploymentTime());
        addCommand.setDepartmentIds(Arrays.asList(cmd.getDepartmentId()));
        addCommand.setContactToken(cmd.getContactToken());
        if (cmd.getEmployeeNo() != null)
            addCommand.setEmployeeNo(cmd.getEmployeeNo());
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  2.获得 detailId 然后处理其它信息
        Long detailId = null;
        if (memberDTO != null)
            detailId = memberDTO.getDetailId();
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (memberDetail != null) {
            memberDetail.setEnName(cmd.getEnName());
            //  TODO:职位有可能修改
            memberDetail.setJobPosition(cmd.getJobPosition());
            memberDetail.setDepartment(getDepartmentName(Arrays.asList(cmd.getDepartmentId())));
            memberDetail.setContactShortToken(cmd.getContactShortToken());
            memberDetail.setWorkEmail(cmd.getWorkEmail());
            memberDetail.setWorkPlaceId(cmd.getWorkingPlaceId());
            memberDetail.setContractPartyId(cmd.getContractId());
            memberDetail.setRegionCode(cmd.getRegionCode());
            organizationProvider.updateOrganizationMemberDetails(memberDetail, memberDetail.getId());
            dto.setDetailId(detailId);
            dto.setContactName(memberDetail.getContactName());
            dto.setContactToken(memberDetail.getContactToken());
        }
        return dto;
    }

    @Override
    public void updateArchivesEmployee(UpdateArchivesEmployeeCommand cmd){

        addGeneralFormValuesCommand formCommand = new addGeneralFormValuesCommand();
        formCommand.setGeneralFormId(getRealFormOriginId(cmd.getFormOriginId()));
        formCommand.setSourceId(cmd.getDetailId());
        formCommand.setSourceType(GeneralFormSourceType.ARCHIVES_ATUH.getCode());
        formCommand.setValues(cmd.getValues());
        generalFormService.addGeneralFormValues(formCommand);
    }

    @Override
    public GetArchivesEmployeeResponse getArchivesEmployee(GetArchivesEmployeeCommand cmd) {

        GetArchivesEmployeeResponse response = new GetArchivesEmployeeResponse();

        //  1.获取表单所有字段
        GeneralFormIdCommand formCommand = new GeneralFormIdCommand(getRealFormOriginId(cmd.getFormOriginId()));
        GeneralFormDTO form = generalFormService.getGeneralForm(formCommand);

        //  2.获取表单对应的值
        GetGeneralFormValuesCommand valueCommand =
                new GetGeneralFormValuesCommand(GeneralFormSourceType.ARCHIVES_ATUH.getCode(),cmd.getDetailId(),NormalFlag.NEED.getCode());
        List<PostApprovalFormItem> employeeDynamicVal = generalFormService.getGeneralFormValues(valueCommand);

        //  3.获取个人信息
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        Map<String, String> employeeStaticVal = handleEmployeeStaticVal(employee);

        //  4.赋值
        for(GeneralFormFieldDTO dto : form.getFormFields()){
            //  4-1.赋值给系统默认字段
            if (dto.getFieldAttribute().equals(GeneralFormFieldAttributeType.DEFAULT.getCode())) {
                dto.setFieldValue(employeeStaticVal.get(dto.getFieldName()));
            }
            //  TODO: 4-2.赋值给非系统默认字段
            else {
               //
            }
        }

        return response;
    }


    //  利用 map 设置 key 来存取值
    private Map<String, String> handleEmployeeStaticVal(OrganizationMemberDetails employee) {
        Map<String, String> valueMap = new HashMap<>();
/*        Field[] fields = employee.getClass().getDeclaredFields();
        for(int i=0; i<fields.length; i++){
            valueMap.put(fields[i].getName(),fields[i].get(employee));
        }*/
        valueMap.put("contactName", employee.getContactName());
        valueMap.put("enName", employee.getEnName());
        //  TODO:性别处理,婚姻状况
//        valueMap.put("gender",employee.getGender());
        if(employee.getBirthday() != null)
            valueMap.put("birthday", String.valueOf(employee.getBirthday()));
//        valueMap.put("enName",employee.getMaritalFlag());
        if(employee.getProcreative() != null)
            valueMap.put("procreative", String.valueOf(employee.getProcreative()));
        valueMap.put("ethnicity", employee.getEthnicity());
        valueMap.put("politicalFlag", employee.getPoliticalFlag());
        valueMap.put("nativePlace", employee.getNativePlace());
        valueMap.put("idType", employee.getIdType());
        valueMap.put("idNumber", employee.getIdNumber());
        if(employee.getIdExpirtDate() != null)
            valueMap.put("idExpirtDate", String.valueOf(employee.getIdExpirtDate()));
        valueMap.put("degree", employee.getDegree());
        valueMap.put("graduationSchool", employee.getGraduationSchool());
        if(employee.getGraduationTime() != null)
            valueMap.put("graduationTime", String.valueOf(employee.getGraduationTime()));
        valueMap.put("contactToken", employee.getContactToken());
        valueMap.put("email", employee.getEmail());
        valueMap.put("wechat", employee.getWechat());
        valueMap.put("qq", employee.getQq());
        valueMap.put("address", employee.getAddress());
        valueMap.put("emergencyName", employee.getEmergencyName());
        valueMap.put("emergencyRelationship", employee.getEmergencyRelationship());
        valueMap.put("emergencyContact", employee.getEmergencyContact());
        if(employee.getCheckInTime() != null)
            valueMap.put("checkInTime", String.valueOf(employee.getCheckInTime()));
//        valueMap.put("employeeType", employee.getEmployeeType());
//        valueMap.put("employeeStatus", employee.getEmployeeStatus());
        valueMap.put("employmentTime", String.valueOf(employee.getEmploymentTime()));
        //  TODO:部门的同步，岗位的修改
//        valueMap.put("department", employee.getEnName());
//        valueMap.put("jobPosition", employee.getJobPosition());
        valueMap.put("employeeNo", employee.getEmployeeNo());
        valueMap.put("contactShortToken", employee.getContactShortToken());
        valueMap.put("workEmail", employee.getWorkEmail());
        //  TODO:工作地点，合同主体的转化
//        valueMap.put("workPlaceId", String.valueOf(employee.getWorkPlaceId()));
//        valueMap.put("contractPartyId", employee.getEnName());
        if(employee.getWorkStartTime() != null)
            valueMap.put("workStartTime", String.valueOf(employee.getWorkStartTime()));
        if(employee.getContractStartTime() != null)
            valueMap.put("contractStartTime", String.valueOf(employee.getContractStartTime()));
        if(employee.getContractEndTime() != null)
            valueMap.put("contractEndTime", String.valueOf(employee.getContractEndTime()));

        valueMap.put("salaryCardNumber", employee.getSalaryCardNumber());
        valueMap.put("salaryCardBank", employee.getSalaryCardBank());
        valueMap.put("socialSecurityNumber", employee.getSocialSecurityNumber());
        valueMap.put("providentFundNumber", employee.getProvidentFundNumber());
        valueMap.put("regResidenceType", employee.getRegResidenceType());
        valueMap.put("regResidence", employee.getRegResidence());
        valueMap.put("idPhoto", employee.getIdPhoto());
        valueMap.put("visaPhoto", employee.getVisaPhoto());
        valueMap.put("lifePhoto", employee.getLifePhoto());
        valueMap.put("entryForm", employee.getEntryForm());
        valueMap.put("graduationCertificate", employee.getGraduationCertificate());
        valueMap.put("degreeCertificate", employee.getDegreeCertificate());
        valueMap.put("contractCertificate", employee.getContractCertificate());
        return valueMap;
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
        orgCommand.setContractEndTimeStart(cmd.getCheckInTimeStart());
        orgCommand.setContractEndTimeEnd(cmd.getContractTimeEnd());
        orgCommand.setEmployeeStatus(cmd.getEmployeeStatus());
        orgCommand.setContractPartyId(cmd.getContractPartyId());
        //  TODO:查询文字的确定
        orgCommand.setKeywords(cmd.getContactName());
        if (cmd.getDepartmentId() != null)
            orgCommand.setOrganizationId(cmd.getDepartmentId());
        orgCommand.setWorkPlaceId(cmd.getWorkingPlaceId());

        orgCommand.setPageAnchor(cmd.getPageAnchor());
        if (cmd.getPageSize() != null)
            orgCommand.setPageSize(cmd.getPageSize());
        else
            orgCommand.setPageSize(20);
        ListOrganizationMemberCommandResponse members = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);

        response.setArchivesEmployees(members.getMembers().stream().map(r -> {
            ArchivesEmployeeDTO dto = new ArchivesEmployeeDTO();
            dto.setDetailId(r.getDetailId());
            dto.setTargetId(r.getTargetId());
            dto.setTargetType(r.getTargetType());
            dto.setContactName(r.getContactName());
            dto.setEmployeeStatus(r.getEmployeeStatus());
            //  TODO: 岗位的确定
            dto.setDepartments(r.getDepartments());
            //  TODO: 区号的添加,工作邮箱的读取,合同时间的读取
            dto.setContactToken(r.getContactToken());
//            dto.setWorkEmail(r.getEmail());
            dto.setEmploymentTime(r.getEmploymentTime());
//            dto.setContractTime(r.getcontr);
            return dto;
        }).collect(Collectors.toList()));
        response.setNextPageAnchor(members.getNextPageAnchor());
        return response;
    }

    @Override
    public ListArchivesDismissEmployeesResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListArchivesDismissEmployeesResponse response = new ListArchivesDismissEmployeesResponse();

        Condition condition = listDismissEmployeesCondition(cmd);

        List<ArchivesDismissEmployees> results = archivesProvider.listArchivesDismissEmployees(cmd.getPageOffset(), cmd.getPageSize() + 1, namespaceId, condition);

        if (results != null) {
            response.setDismissEmployees(results.stream().map(r -> {
                ArchivesDismissEmployeeDTO dto = ConvertHelper.convert(r, ArchivesDismissEmployeeDTO.class);
                return dto;
            }).collect(Collectors.toList()));
            Integer nextPageOffset = null;
            if (results.size() > cmd.getPageSize()) {
                nextPageOffset = cmd.getPageOffset() + 1;
            }
            response.setNextPageOffset(nextPageOffset);
            return response;
        }

        return null;
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
    public void employArchivesEmployees(EmployArchivesEmployeesCommand cmd) {

    }

    @Override
    public void transferArchivesEmployees(TransferArchivesEmployeesCommand cmd) {

    }

    @Override
    public void dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd) {

    }

    @Override
    public GeneralFormDTO updateArchivesForm(UpdateArchivesFormCommand cmd) {

        //  1.如果无 formOriginId 时则使用的是模板，此时在组织结构新增一份表单，同时业务表单组新增记录
        //  2.如果有 formOriginId 时则说明已经拥有了表单，此时在组织架构做修改，同时业务表单组同步记录

        if (cmd.getFormOriginId() == 0L) {
            //  新增时，在组织架构增加表单
            CreateApprovalFormCommand createCommand = new CreateApprovalFormCommand();
            createCommand.setOwnerId(cmd.getOrganizationId());
            createCommand.setOwnerType("organization");
            createCommand.setOrganizationId(cmd.getOrganizationId());
            createCommand.setFormName(ARCHIVES);
            createCommand.setFormFields(cmd.getFormFields());
            createCommand.setFormGroups(cmd.getFormGroups());
            GeneralFormDTO form = generalFormService.createGeneralForm(createCommand);

            //  在业务表单组新增记录
            createArchivesForm(form);
            return form;
        } else {
            //  修改时，在组织架构修改表单
            UpdateApprovalFormCommand updateCommand = new UpdateApprovalFormCommand();
            updateCommand.setFormOriginId(cmd.getFormOriginId());
            updateCommand.setOwnerId(cmd.getOrganizationId());
            updateCommand.setOwnerType("organization");
            updateCommand.setOrganizationId(cmd.getOrganizationId());
            updateCommand.setFormFields(cmd.getFormFields());
            updateCommand.setFormGroups(cmd.getFormGroups());
            GeneralFormDTO form = generalFormService.updateGeneralForm(updateCommand);

            //  在业务表单同步记录
            ArchivesFroms archivesFroms = archivesProvider.findArchivesFormOriginId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
            archivesFroms.setFormOriginId(form.getFormOriginId());
            archivesFroms.setFormVersion(form.getFormVersion());
            archivesProvider.updateArchivesForm(archivesFroms);

            return form;
        }
    }

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
            String value = configurationProvider.getValue("archives.form.origin.id", "");
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
        return null;
    }

    @Override
    public void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd, HttpServletResponse httpResponse) {

    }

    @Override
    public void remindArchivesEmployee(RemindArchivesEmployeeCommand cmd) {

    }


}
