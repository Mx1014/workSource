package com.everhomes.archives;

import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.profile.ProfileContactsSticky;
import com.everhomes.profile.ProfileDismissEmployees;
import com.everhomes.profile.ProfileProvider;
import com.everhomes.profile.ProfileService;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.profile.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileProvider profileProvider;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationProvider organizationProvider;

    @Override
    public ProfileContactDTO addProfileContact(AddProfileContactCommand cmd) {

        ProfileContactDTO dto = new ProfileContactDTO();
        //  TODO: visibleFlag 的判断

        //  组织架构添加人员
        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setGender(cmd.getGender());
        addCommand.setContactToken(cmd.getContactToken());
        addCommand.setDepartmentIds(cmd.getDepartmentIds());
        addCommand.setJobPositionIds(cmd.getJobPositionIds());
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  获得 detailId 然后处理其它信息
        Long detailId = null;
        if (memberDTO != null)
            detailId = memberDTO.getDetailId();
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if(memberDetail != null){
            memberDetail.setEnName(cmd.getContactEnName());
            //  TODO: areaCode 区号与 contactShortToken 短号,部门与职位的转化
            memberDetail.setEmail(cmd.getEmail());
            organizationProvider.updateOrganizationMemberDetails(memberDetail,memberDetail.getId());
            dto.setDetailId(detailId);
            dto.setContactName(memberDetail.getContactName());
            dto.setContactToken(memberDetail.getContactToken());
            dto.setEmail(memberDetail.getEmail());
        }

        //  TODO: 添加档案记录

        return dto;
    }

    @Override
    public void transferProfileContacts(TransferProfileContactsCommand cmd) {
        if(cmd.getDetailIds() != null){
            //  TODO: 根据提供的方法获取部门名称
            for(Long detailId : cmd.getDetailIds()){
                // TODO: 循环添加档案记录
            }
        }
    }

    @Override
    public void deleteProfileContacts(DeleteProfileContactsCommand cmd) {
        if(cmd.getDetailIds() != null){
            for(Long detailId : cmd.getDetailIds()){
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
    public void stickProfileContact(StickProfileContactCommand cmd) {
        User user = UserContext.current().getUser();

        //  状态码为 0 时删除
        if (cmd.getStick().equals("0")) {
            ProfileContactsSticky result = profileProvider.findProfileContactsStickyByDetailIdAndOrganizationId(
                    user.getNamespaceId(), cmd.getOrganizationId(), cmd.getDetailId());
            if (result != null)
                profileProvider.deleteProfileContactsSticky(result);
        }

        //  状态码为 1 时新增置顶
        if (cmd.getStick().equals("1")) {
            ProfileContactsSticky result = profileProvider.findProfileContactsStickyByDetailIdAndOrganizationId(user.getNamespaceId(), cmd.getOrganizationId(), cmd.getDetailId());
            if (result == null) {
                ProfileContactsSticky contactsSticky = new ProfileContactsSticky();
                contactsSticky.setNamespaceId(user.getNamespaceId());
                contactsSticky.setOrganizationId(cmd.getOrganizationId());
                contactsSticky.setDetailId(cmd.getDetailId());
                contactsSticky.setOperatorUid(user.getId());
                profileProvider.createProfileContactsSticky(contactsSticky);
            } else {
                profileProvider.updateProfileContactsSticky(result);
            }
        }
    }

    @Override
    public ListProfileContactsResponse listProfileContacts(ListProfileContactsCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        //  没有查询时显示主体
        if (StringUtils.isEmpty(cmd.getKeywords())) {
            //  1.首先从置顶的表读取置顶人员
            List<Long> detailIds = profileProvider.listProfileContactsStickyIds(namespaceId,cmd.getOrganizationId());
            //  TODO: 2.从组织架构读取对应人员，确定置顶个数
            //  TODO: 3.获取其余人员

        } else {
            //  查询则显示特定人员

        }
        return null;
    }

    @Override
    public ImportFileTaskDTO importProfileContacts(MultipartFile mfile, Long userId, Integer namespaceId, ImportProfileContactsCommand cmd) {
        return null;
    }

    @Override
    public void exportProfileContacts(ExportProfileContactsCommand cmd, HttpServletResponse httpResponse) {

    }

    @Override
    public void verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd) {
        //  TODO: 校验密码
        //   校验有误时抛出异常
/*        if(member != null){
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_PHONE_ALREADY_EXIST,
                    "phone number already exists.");
                    }*/
    }

    @Override
    public ListProfileEmployeesResponse listProfileEmployees(ListProfileEmployeesCommand cmd) {

        return null;
    }

    @Override
    public ProfileEmployeeDTO addProfileEmployee(AddProfileEmployeeCommand cmd) {

        //  1.组织架构添加人员
        AddOrganizationPersonnelCommand addCommand = new AddOrganizationPersonnelCommand();
        addCommand.setOrganizationId(cmd.getOrganizationId());
        addCommand.setContactName(cmd.getContactName());
        addCommand.setCheckInTime(cmd.getCheckInTime());
        addCommand.setEmployeeType(cmd.getEmployeeType());
        addCommand.setEmployeeStatus(cmd.getEmployeeStatus());
        addCommand.setEmploymentTime(cmd.getEmploymentTime());
//        addCommand.setGender(cmd.getGender());
        addCommand.setContactToken(cmd.getContactToken());
//        addCommand.setDepartmentIds(cmd.getDepartmentId());
//        addCommand.setJobPositionIds(cmd.getJobPositionId());
        if(cmd.getEmployeeNo() != null)
            addCommand.setEmployeeNo(cmd.getEmployeeNo());
        OrganizationMemberDTO memberDTO = organizationService.addOrganizationPersonnel(addCommand);

        //  2.修改其余的信息
        OrganizationMemberDetails result = organizationProvider.findOrganizationMemberDetailsByDetailId(memberDTO.getDetailId());

        return null;
    }

    @Override
    public ListProfileDismissEmployeesResponse listProfileDismissEmployees(ListProfileDismissEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListProfileDismissEmployeesResponse response = new ListProfileDismissEmployeesResponse();

        Condition condition = listDismissEmployeesCondition(cmd);

        List<ProfileDismissEmployees> results = profileProvider.listProfileDismissEmployees(cmd.getPageOffset(), cmd.getPageSize()+1, namespaceId, condition);

        if (results != null) {
            response.setDismissEmployees(results.stream().map(r -> {
                ProfileDismissEmployeeDTO dto = ConvertHelper.convert(r, ProfileDismissEmployeeDTO.class);
                return dto;
            }).collect(Collectors.toList()));
            Integer nextPageOffset = null;
            if (results.size() > cmd.getPageSize()) {
                nextPageOffset = cmd.getPageOffset() +1;
            }
            response.setNextPageOffset(nextPageOffset);
            return response;
        }

        return null;
    }

    private Condition listDismissEmployeesCondition(ListProfileDismissEmployeesCommand cmd){
        Condition condition = Tables.EH_PROFILE_DISMISS_EMPLOYEES.ORGANIZATION_ID.eq(cmd.getOrganizationId());

        //   离职日期判断
        if(cmd.getDismissTimeStart()!=null && cmd.getDismissTimeEnd() != null) {
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.DISMISS_TIME.ge(cmd.getDismissTimeStart()));
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.DISMISS_TIME.le(cmd.getDismissTimeEnd()));
        }

        //   入职日期判断
        if(cmd.getCheckInTimeStart()!=null && cmd.getCheckInTimeEnd() != null) {
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.CHECK_IN_TIME.ge(cmd.getCheckInTimeStart()));
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.CHECK_IN_TIME.le(cmd.getCheckInTimeEnd()));
        }

        //   离职类型
        if(cmd.getDismissType()!=null) {
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.DISMISS_TYPE.eq(cmd.getDismissType()));
        }

        //   离职原因
        if(cmd.getDismissReason()!=null) {
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.DISMISS_REASON.eq(cmd.getDismissReason()));
        }

        //   姓名搜索
        if(cmd.getContactName() != null){
            condition = condition.and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.CONTACT_NAME.like("%" + cmd.getContactName()+ "%"));
        }
        return condition;
    }

    @Override
    public void employProfileEmployees(EmployProfileEmployeesCommand cmd) {

    }

    @Override
    public void transferProfileEmployees(TransferProfileEmployeesCommand cmd) {

    }

    @Override
    public void dismissProfileEmployees(DismissProfileEmployeesCommand cmd) {

    }

    @Override
    public void addProfileField(AddProfileFieldCommand cmd) {

    }

    @Override
    public void addProfileFieldGroup(AddProfileFieldGroupCommand cmd) {

    }

    @Override
    public void updateProfileFieldOrder(UpdateProfileFieldOrderCommand cmd) {

    }

    @Override
    public GetProfileFieldResponse getProfileField(GetProfileFieldCommand cmd) {
        return null;
    }

}
