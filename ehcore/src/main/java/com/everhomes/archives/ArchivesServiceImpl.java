package com.everhomes.archives;

import com.alibaba.fastjson.JSON;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
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
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.discovery.zen.membership.MembershipAction;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.io.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class ArchivesServiceImpl implements ArchivesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchivesServiceImpl.class);

    private static final String ARCHIVES_NOTIFICATION = "archives_notification";

    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ArchivesProvider archivesProvider;

    @Autowired
    private ArchivesFormService archivesFormService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;

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

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Override
    public ArchivesContactDTO addArchivesContact(AddArchivesContactCommand cmd) { 
    	if(cmd.getUpdateDetailId()!=null || cmd.getDetailId() != null){
    		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByDetailIdAndOrgId(cmd.getDetailId(),cmd.getOrganizationId(),
    				Arrays.asList(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode(),OrganizationGroupType.DEPARTMENT.getCode(),OrganizationGroupType.GROUP.getCode()));
    		if(CollectionUtils.isNotEmpty(members)){
    			//当编辑后的部门和现在的部门不一样,取消置顶
    			if(!members.get(0).getOrganizationId().equals(cmd.getDepartmentIds().get(0))){
    				ArchivesStickyContacts result = archivesProvider.findArchivesStickyContactsByDetailIdAndOrganizationId(
    	                    cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getDetailId());
    	            if (result != null)
    	                archivesProvider.deleteArchivesStickyContacts(result);
    			}
    		}
    	}
        //  校验权限 by lei.lv  update by huanglm
        /*if (cmd.getDetailId() != null) {
            Long departmentId = organizationService.getDepartmentByDetailId(cmd.getDetailId());
            organizationService.checkOrganizationPrivilege(departmentId, PrivilegeConstants.CREATE_OR_MODIFY_PERSON);
        }*/
    	cmd.setAccount(cmd.getAccount() == null? null : cmd.getAccount().trim());
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
        addCommand.setNamespaceId(cmd.getNamespaceId());
        addCommand.setOperateType(cmd.getOperateType());
        //  1.进行校验(例如: 邮箱)
        if (!StringUtils.isEmpty(cmd.getWorkEmail()))
            if (!organizationService.verifyPersonnelByWorkEmail(cmd.getOrganizationId(), cmd.getUpdateDetailId(), cmd.getWorkEmail()))
                throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_DUPLICATE_WORK_EMAIL,
                        "Duplicate work email");
        if(!StringUtils.isEmpty(cmd.getAccount()))
            if (!organizationService.verifyPersonnelByAccount(cmd.getUpdateDetailId(), cmd.getAccount().trim()))
                throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_DUPLICATE_ACCOUNT,
                        "Duplicate account");

        //  2.添加人员到组织架构
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  3.获得 detailId 然后处理其它信息
        Long detailId = null;
        if (memberDTO != null)
            detailId = memberDTO.getDetailId();
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (employee == null)
            return null;
        
        if(employee.getAccount() == null)
            employee.setAccount(cmd.getAccount());
        employee.setEnName(cmd.getContactEnName());
        employee.setRegionCode(cmd.getRegionCode());
        employee.setContactShortToken(cmd.getContactShortToken());
        employee.setWorkEmail(cmd.getWorkEmail());
        employee.setContractPartyId(cmd.getOrganizationId());
        //员工认证时，没有填写邮箱地址，使用申请加入公司时使用的邮箱。add by yanlong.liang
        //如果影响到了组织架构，请删除
        if (StringUtils.isEmpty(cmd.getWorkEmail())) {
            OrganizationMember organizationMember = this.organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(memberDTO.getTargetId(),memberDTO.getOrganizationId());
            if (organizationMember != null) {
                employee.setWorkEmail(organizationMember.getStringTag3());
            }
        }
        dto.setDetailId(employee.getId());
        dto.setContactName(employee.getContactName());
        dto.setContactToken(employee.getContactToken());
        //  4-1.编辑则直接更新信息
        if (cmd.getDetailId() != null) {
            organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());
            return dto;
        }
        //  4-2.新增则初始化部分信息
        employee.setCheckInTime(ArchivesUtil.currentDate());
        employee.setCheckInTimeIndex(ArchivesUtil.getMonthAndDay(employee.getCheckInTime()));
        employee.setEmploymentTime(ArchivesUtil.currentDate());
        employee.setEmployeeType(EmployeeType.FULLTIME.getCode());
        employee.setEmployeeStatus(EmployeeStatus.ON_THE_JOB.getCode());
        organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());

        //  5.查询若存在于离职列表则删除
        deleteArchivesDismissEmployees(detailId, cmd.getOrganizationId());

        //  6.增加入职记录
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
            organizationService.checkOrganizationPrivilege(departmentId, PrivilegeConstants.MODIFY_DEPARTMENT_JOB_POSITION);
        });
        dbProvider.execute((TransactionStatus status) -> {
            if (cmd.getDetailIds() != null) {
                TransferArchivesEmployeesCommand tCommand = new TransferArchivesEmployeesCommand();
                tCommand.setNamespaceId(cmd.getNamespaceId());
                tCommand.setOrganizationId(cmd.getOrganizationId());
                tCommand.setDetailIds(cmd.getDetailIds());
                tCommand.setDepartmentIds(cmd.getDepartmentIds());
                tCommand.setJobPositionIds(cmd.getJobPositionIds());
                tCommand.setJobLevelIds(cmd.getJobLevelIds());
                //  调整部门、岗位、职级并同步到detail表
                organizationService.transferOrganizationPersonels(tCommand);
            }
            return null;
        });
    }

    @Override
    public void deleteArchivesContacts(DeleteArchivesContactsCommand cmd) {
        //权限校验
        cmd.getDetailIds().forEach(detailId -> {
            Long departmentId = organizationService.getDepartmentByDetailId(detailId);
            organizationService.checkOrganizationPrivilege(departmentId, PrivilegeConstants.DELETE_PERSON);
        });

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        dbProvider.execute((TransactionStatus status) -> {
            if (cmd.getDetailIds() != null) {
                //  1.人事离职
                DismissArchivesEmployeesCommand command = new DismissArchivesEmployeesCommand();
                command.setDetailIds(cmd.getDetailIds());
                command.setOrganizationId(cmd.getOrganizationId());
                command.setDismissType(ArchivesDismissType.OTHER.getCode());
                command.setDismissReason(ArchivesDismissReason.OTHER.getCode());
                command.setDismissRemark(localeStringService.getLocalizedString(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.CONTACT_DELETE, "zh_CN", "Delete by the admin"));
                command.setDismissTime(String.valueOf(ArchivesUtil.currentDate()));
                dismissArchivesEmployeesConfig(command);

                /*由人事删除改为上面的人事离职
                DeleteArchivesEmployeesCommand command = ConvertHelper.convert(cmd, DeleteArchivesEmployeesCommand.class);
                deleteArchivesEmployees(command);*/

                //  2.置顶表删除
                archivesProvider.deleteArchivesStickyContactsByDetailIds(namespaceId, cmd.getDetailIds());
            }
            return null;
        });
    }

    //  置顶通讯录成员
    @Override
    public void stickArchivesContact(StickArchivesContactCommand cmd) {
        Long userId = UserContext.currentUserId();
        Integer namespaceId = cmd.getNamespaceId();

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
        // 1.If the keywords is not null, just pass the key and get the corresponding employee back.
        // 2.If the keywords is null, then judged by the "pageAnchor"
        // 3.If the pageAnchor is null, we should get stick employees first.
        // 4.if the pageAnchor is not null, means we should get the next page of employees, so ignore those stick employees.
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListArchivesContactsResponse response = new ListArchivesContactsResponse();
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);
        final Integer stickCount = cmd.getPageSize();  //  由于页码大小支持多种模式，故置顶数从页码取 at 01/08/2018

        //  保存置顶人员
        List<Long> detailIds = archivesProvider.listArchivesStickyContactsIds(namespaceId, cmd.getOrganizationId(), stickCount);
        if (!StringUtils.isEmpty(cmd.getKeywords()) || detailIds == null) {
            //  有查询的时候已经不需要置顶了，直接查询对应人员
            response.setContacts(listArchivesContacts(cmd, response, null));
        } else {
            if (StringUtils.isEmpty(cmd.getPageAnchor())) {
                List<ArchivesContactDTO> contacts = new ArrayList<>();
                //  先获取置顶人员信息
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
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);

        ArchivesContactDTO dto = getArchivesStickContactInfo(detail);

        return dto;
    }

	private ArchivesContactDTO getArchivesStickContactInfo(OrganizationMemberDetails detail) {
		ArchivesContactDTO dto = new ArchivesContactDTO();
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
        dto.setAccount(detail.getAccount());
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
	
	@Override
    public ArchivesContactDTO getArchivesContact(GetArchivesContactCommand cmd){
    	OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(cmd.getUserId(), cmd.getOrganizationId());
    	return getArchivesStickContactInfo(detail);
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

        ListOrganizationMemberCommandResponse res;
        //  1.置顶个数超过页总数
        if (orgCommand.getPageSize() == 0) {
            orgCommand.setPageSize(1);
            res = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);
            response.setNextPageAnchor(res.getMembers().get(0).getDetailId());
        } else {
            //  2.置顶个数未超过页总数
            res = organizationService.listOrganizationPersonnelsWithDownStream(orgCommand);
            if (res.getMembers() == null)
                return contacts;
            res.getMembers().forEach(r -> {
                ArchivesContactDTO dto = new ArchivesContactDTO();
                dto.setDetailId(r.getDetailId());
                dto.setOrganizationId(r.getOrganizationId());
                dto.setTargetId(r.getTargetId());
                dto.setTargetType(r.getTargetType());
                dto.setAccount(r.getAccount());
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
            response.setNextPageAnchor(res.getNextPageAnchor());
        }
        return contacts;
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

    private void checkTargetPrivilege(Long detailId, Long organizationId) {
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (employee == null || employee.getTargetId() == 0)
            return;
        if (rolePrivilegeService.checkIsSystemOrAppAdmin(organizationId, employee.getTargetId()))
            throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_DELETE_ADMIN,
                    "No rights to remove the admin.");
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

        //  1.进行校验(例如: 邮箱)
        if (!StringUtils.isEmpty(cmd.getWorkEmail()))
            if (!organizationService.verifyPersonnelByWorkEmail(cmd.getOrganizationId(), 0L, cmd.getWorkEmail()))
                throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_DUPLICATE_WORK_EMAIL,
                        "Duplicate work email");

        //  2.添加人员到组织架构
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  3-1.获得 detailId 处理人事档案信息
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
        else
            employee.setEmploymentTime(ArchivesUtil.parseDate(cmd.getEmploymentTime()));
        if (cmd.getContractPartyId() != null)
            employee.setContractPartyId(cmd.getContractPartyId());
        else
            employee.setContractPartyId(cmd.getOrganizationId());
        organizationProvider.updateOrganizationMemberDetails(employee, employee.getId());
        //  3-2.增加入职记录
        addCheckInLogs(detailId, cmd);
        //  3-3.配置转正
        EmployArchivesEmployeesCommand command = new EmployArchivesEmployeesCommand();
        command.setDetailIds(Collections.singletonList(employee.getId()));
        command.setOrganizationId(employee.getOrganizationId());
        command.setEmploymentTime(format.format(employee.getEmploymentTime().toLocalDate()));
        command.setEmploymentEvaluation("");
        employArchivesEmployeesConfig(command);
        //  3-4.查询若存在于离职列表则删除
        deleteArchivesDismissEmployees(detailId, cmd.getOrganizationId());

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
            List<PostApprovalFormItem> dynamicItems = cmd.getValues().stream().filter(
                    r -> GeneralFormFieldAttribute.fromCode(r.getFieldAttribute()) != GeneralFormFieldAttribute.DEFAULT).collect(Collectors.toList());
            archivesFormService.addArchivesDynamicValues(employee, dynamicItems);
            return null;
        });
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
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        if(employee == null)
            return response;

        //  1.获取表单所有字段
        ArchivesFormDTO form = getRealArchivesForm(cmd.getNamespaceId(), cmd.getOrganizationId());

        //  2.获取表单对应的值
        List<GeneralFormFieldDTO> dynamicVals = archivesFormService.getArchivesDynamicValues(form.getId(), cmd.getDetailId());
        Map<String, String> dynamicMaps = handleEmployeeDynamicVal(dynamicVals);

        //  3.获取个人信息的值
        Map<String, String> staticMaps = handleEmployeeDefaultVal(employee);

        //  4.赋值
        //  4-1.处理部门.岗位.职级字段
        processEmployeeOrganization(staticMaps, employee);
        for (GeneralFormFieldDTO dto : form.getFormFields()) {
            //  4-2.赋值给系统默认字段
            if (GeneralFormFieldAttribute.DEFAULT.getCode().equals(dto.getFieldAttribute())) {
                dto.setFieldValue(staticMaps.get(dto.getFieldName()));
            }
            //  4-3.赋值给非系统默认字段
            else {
                dto.setFieldValue(dynamicMaps.get(dto.getFieldName()));
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
        String employeeCase;
        Map<String, Object> map = new LinkedHashMap<>();
        switch (EmployeeStatus.fromCode(employee.getEmployeeStatus())) {
            case PROBATION:
                map.put("firstDate", employee.getCheckInTime() != null ? format.format(employee.getCheckInTime().toLocalDate()) : "  无");
                map.put("nextDate", employee.getEmploymentTime() != null ? format.format(employee.getEmploymentTime().toLocalDate()) : "  无");
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                        ArchivesLocaleTemplateCode.ARCHIVES_PROBATION_CASE, "zh_CN", map, "");
                break;
            case DISMISSAL:
                map.put("firstDate", employee.getCheckInTime() != null ? format.format(employee.getCheckInTime().toLocalDate()) : "  无");
                map.put("nextDate", employee.getDismissTime() != null ? format.format(employee.getDismissTime().toLocalDate()) : "  无");
                employeeCase = localeTemplateService.getLocaleTemplateString(ArchivesLocaleTemplateCode.SCOPE,
                        ArchivesLocaleTemplateCode.ARCHIVES_DISMISS_CASE, "zh_CN", map, "");
                break;
            default:
                map.put("firstDate", employee.getCheckInTime() != null ? format.format(employee.getCheckInTime().toLocalDate()) : "  无");
                map.put("nextDate", employee.getContractEndTime() != null ? format.format(employee.getContractEndTime().toLocalDate()) : "   无");
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

    /* *******************    assistant function for Archives start    ******************* */
    /**
     * 解析系统字段，便于存入档案表
     */
    private OrganizationMemberDetails convertToEmployeeDetail(OrganizationMemberDetails employee, List<PostApprovalFormItem> itemValues) {
        for (PostApprovalFormItem itemValue : itemValues) {
            if (GeneralFormFieldAttribute.DEFAULT.getCode().equals(itemValue.getFieldAttribute())) {
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
                        if (!organizationService.verifyPersonnelByWorkEmail(employee.getOrganizationId(), employee.getId(), itemValue.getFieldValue()))
                            throw RuntimeErrorException.errorWith(ArchivesLocaleStringCode.SCOPE, ArchivesLocaleStringCode.ERROR_DUPLICATE_WORK_EMAIL,
                                    "Duplicate work email");
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
        valueMap.put(ArchivesParameter.ID_PHOTO, employee.getIdPhoto());
        valueMap.put(ArchivesParameter.VISA_PHOTO, employee.getVisaPhoto());
        valueMap.put(ArchivesParameter.LIFE_PHOTO, employee.getLifePhoto());
        valueMap.put(ArchivesParameter.ENTRY_FORM, employee.getEntryForm());
        valueMap.put(ArchivesParameter.GRADUATION_CERTIFICATE, employee.getGraduationCertificate());
        valueMap.put(ArchivesParameter.DEGREE_CERTIFICATE, employee.getDegreeCertificate());
        valueMap.put(ArchivesParameter.CONTRACT_CERTIFICATE, employee.getContractCertificate());
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
    private Map<String, String> handleEmployeeDynamicVal(List<GeneralFormFieldDTO> dynamicVals) {
        Map<String, String> valueMap = new HashMap<>();
        if (dynamicVals != null && dynamicVals.size() > 0) {
            for (GeneralFormFieldDTO val : dynamicVals) {
                valueMap.put(val.getFieldName(), val.getFieldValue());
            }
        }
        return valueMap;
    }
    /* *******************    assistant function for Archives end    ******************* */

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
            //  3.添加记录
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
            for (Long detailId : cmd.getDetailIds()) {
                //  1.校验管理员
                checkTargetPrivilege(detailId, cmd.getOrganizationId());
                //  2.按照操作时间执行分情况执行下一步
                if (!ArchivesUtil.parseDate(cmd.getDismissTime()).after(ArchivesUtil.currentDate())) {
                    dismissArchivesEmployee(detailId, namespaceId, cmd);
                } else {
                    createArchivesOperation(namespaceId, cmd.getOrganizationId(), detailId, ArchivesOperationType.DISMISS.getCode(),
                            ArchivesUtil.parseDate(cmd.getDismissTime()), StringHelper.toJsonString(cmd));
                }
            }
            if (!ArchivesUtil.parseDate(cmd.getDismissTime()).after(ArchivesUtil.currentDate())) {
                archivesProvider.deleteLastConfiguration(namespaceId, cmd.getDetailIds(), ArchivesOperationType.DISMISS.getCode());
            }
            if (TrueOrFalseFlag.fromCode(cmd.getLogFlag()) != TrueOrFalseFlag.FALSE) {
                addDismissLogs(cmd);
            }
            return null;
        });
    }

    private void dismissArchivesEmployee(Long detailId, Integer namespaceId, DismissArchivesEmployeesCommand cmd){
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
                //  1.校验管理员
                checkTargetPrivilege(detailId, cmd.getOrganizationId());
                //  2.删除员工权限
                DeleteOrganizationPersonnelByContactTokenCommand deleteOrganizationPersonnelByContactTokenCommand = new DeleteOrganizationPersonnelByContactTokenCommand();
                deleteOrganizationPersonnelByContactTokenCommand.setOrganizationId(detail.getOrganizationId());
                deleteOrganizationPersonnelByContactTokenCommand.setContactToken(detail.getContactToken());
                deleteOrganizationPersonnelByContactTokenCommand.setScopeType(DeleteOrganizationContactScopeType.ALL_NOTE.getCode());
                organizationService.deleteOrganizationPersonnelByContactToken(deleteOrganizationPersonnelByContactTokenCommand);
                //  3.删除员工档案
                organizationProvider.deleteOrganizationMemberDetails(detail);
                //  4.删除离职列表中对应的员工
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
                dismissArchivesEmployee(configuration.getDetailId(), configuration.getNamespaceId(), cmd3);
/*                cmd3.setDetailIds(Collections.singletonList(configuration.getDetailId()));
                dismissArchivesEmployees(cmd3);*/
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
            response.setDismissEmployees(results.stream().map(r -> ConvertHelper.convert(r, ArchivesDismissEmployeeDTO.class)).collect(Collectors.toList()));
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



    private ArchivesFormDTO getRealArchivesForm(Integer namespaceId, Long orgId) {
        ArchivesFormDTO form = archivesFormService.getArchivesFormByOrgId(namespaceId, orgId);
        if(form == null)
            form = archivesFormService.getArchivesDefaultForm();
        return form;
    }

/*    @Override
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
    }*/

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
        Integer namespaceId = cmd.getNamespaceId();
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
                Integer count = date.toLocalDate().getYear() - employee.getCheckInTime().toLocalDate().getYear();
                if(count == 0)
                    continue;
                map = new LinkedHashMap<>();
                map.put("contactName", employee.getContactName());
                map.put("companyName", organizationName);
                map.put("year", count);
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
    public ArchivesNotificationDTO getArchivesNotification(ArchivesNotificationCommand cmd){
        Integer namespaceId = cmd.getNamespaceId();
        ArchivesNotifications notification = archivesProvider.findArchivesNotificationsByOrganizationId(namespaceId, cmd.getOrganizationId());
        if (notification == null) {
            return new ArchivesNotificationDTO();
        }
        ArchivesNotificationDTO res = ConvertHelper.convert(notification, ArchivesNotificationDTO.class);
        res.setTargets(JSON.parseArray(notification.getNotifyTarget(), ArchivesNotificationTarget.class));
        return res;
    }

    @Override
    public void makeArchivesCheckInTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        List<OrganizationMemberDetails> details = archivesProvider.listDetailsWithoutCheckInTime();
        if(details == null)
            return;
        int i = 0;
        for(OrganizationMemberDetails detail : details){
            OrganizationMember member = archivesProvider.findOrganizationMemberWithoutStatusByDetailId(detail.getId());
            if(member != null){
                detail.setCheckInTime(new Date(member.getCreateTime().getTime()));
                detail.setCheckInTimeIndex(formatter.format(detail.getCheckInTime().toLocalDate()));
                organizationProvider.updateOrganizationMemberDetails(detail, detail.getId());
                LOGGER.info("the [" + i++ + "] times to make the archives check in time") ;
            }
        }
    }

    @Override
    public void cleanRedundantArchivesDetails() {
        List<Long> detailIds = archivesProvider.listDismissalDetailIds();
        if(detailIds.size() == 0)
            detailIds = Collections.singletonList(0L);
        List<OrganizationMemberDetails> details = archivesProvider.listDetailsWithoutDismissalStatus(detailIds);
        if (details == null)
            return;
        int i = 0;
        for (OrganizationMemberDetails detail : details) {
            OrganizationMember member = archivesProvider.findOrganizationMemberWithStatusByDetailId(detail.getId());
            if (member == null){
                organizationProvider.deleteOrganizationMemberDetails(detail);
                archivesProvider.deleteArchivesStickyContactsByDetailId(detail.getNamespaceId(), detail.getId());
                LOGGER.info("the [" + i++ + "] times to make the delete the detail") ;
            }
        }
    }
}
