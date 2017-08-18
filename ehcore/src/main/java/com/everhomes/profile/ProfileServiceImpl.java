package com.everhomes.profile;

import com.everhomes.rest.organization.ImportFileTaskDTO;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired ProfileProvider profileProvider;

    @Override
    public ProfileContactDTO addProfileContact(AddProfileContactCommand cmd) {
        return null;
    }

    @Override
    public void transferProfileContacts(TransferProfileContactsCommand cmd) {

    }

    @Override
    public void deleteProfileContacts(DeleteProfileContactsCommand cmd) {

    }

    @Override
    public void stickProfileContact(StickProfileContactCommand cmd) {
        User user = UserContext.current().getUser();

        //  状态码为 0 时删除
        if(cmd.getStick().equals("0")){
            ProfileContactsSticky result = profileProvider.findProfileContactsStickyByDetailIdAndOrganizationId(
                    user.getNamespaceId(),cmd.getOrganizationId(),cmd.getDetailId());
            if(result != null)
                profileProvider.deleteProfileContactsSticky(result);
        }

        //  状态码为 1 时新增置顶
        if(cmd.getStick().equals("1")){
            ProfileContactsSticky contactsSticky = new ProfileContactsSticky();
            contactsSticky.setNamespaceId(user.getNamespaceId());
            contactsSticky.setOrganizationId(cmd.getOrganizationId());
            contactsSticky.setDetailId(cmd.getDetailId());
            contactsSticky.setOperatorUid(user.getId());
            profileProvider.createProfileContactsSticky(contactsSticky);
        }
    }

    @Override
    public ListProfileContactsResponse listProfileContacts(ListProfileContactsCommand cmd) {
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
        return null;
    }

    @Override
    public ListProfileDismissEmployeesResponse listProfileDismissEmployees(ListProfileDismissEmployeesCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ListProfileDismissEmployeesResponse response = new ListProfileDismissEmployeesResponse();

        Condition condition = listDismissEmployeesCondition(cmd);

        List<ProfileDismissEmployees> results = profileProvider.listProfileDismissEmployees(cmd.getPageAnchor(), cmd.getPageSize()+1, namespaceId, condition);

        if (results != null) {
            response.setDismissEmployees(results.stream().map(r -> {
                ProfileDismissEmployeeDTO dto = ConvertHelper.convert(r, ProfileDismissEmployeeDTO.class);
                return dto;
            }).collect(Collectors.toList()));
            Long nextPageAnchor = null;
            if (results.size() > cmd.getPageSize()) {
                results.remove(results.size() - 1);
                nextPageAnchor = results.get(results.size() - 1).getId();
            }
            response.setNextPageAnchor(nextPageAnchor);
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
        if(cmd.getDismissType()!=null) {
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
