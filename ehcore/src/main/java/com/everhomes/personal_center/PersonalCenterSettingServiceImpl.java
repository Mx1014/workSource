// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListUserOrganizationCommand;
import com.everhomes.rest.personal_center.ListUserOrganizationResponse;
import com.everhomes.rest.personal_center.PersonalCenterSettingDTO;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.personal_center.UpdateUserCompanyCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalCenterSettingServiceImpl implements PersonalCenterService{

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PersonalCenterSettingProvider personalCenterSettingProvider;
    @Override
    public void createUserEmail(CreateUserEmailCommand cmd) {
        Long userId = UserContext.currentUserId();
        UserIdentifier userIdentifier = new UserIdentifier();
        userIdentifier.setOwnerUid(userId);
        userIdentifier.setIdentifierToken(cmd.getEmail());
        userIdentifier.setIdentifierType(IdentifierType.EMAIL.getCode());
        userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
        userIdentifier.setNamespaceId(UserContext.getCurrentNamespaceId());
        userIdentifier.setCreateTime(new Timestamp(new Date().getTime()));
        userIdentifier.setNotifyTime(new Timestamp(new Date().getTime()));
        this.userProvider.createIdentifier(userIdentifier);
    }

    @Override
    public UserInfo updateShowCompanyFlag(UpdateShowCompanyCommand cmd) {
        User user = UserContext.current().getUser();
        user.setShowCompanyFlag(cmd.getShowCompanyFlag());
        userProvider.updateUser(user);
        return userService.getUserInfo(user.getId());
    }

    @Override
    public UserInfo updateUserCompany(UpdateUserCompanyCommand cmd) {
        User user = UserContext.current().getUser();
        user.setCompany(cmd.getCompanyName());
        userProvider.updateUser(user);
        return userService.getUserInfo(user.getId());
    }

    @Override
    public ListUserOrganizationResponse listUserOrganization(ListUserOrganizationCommand cmd) {
        ListUserOrganizationResponse response = new ListUserOrganizationResponse();
        List<OrganizationDTO> dtoList = this.organizationService.listUserRelateOrganizations(cmd.getNamespaceId(),UserContext.currentUserId(), OrganizationGroupType.ENTERPRISE);
        response.setDtos(dtoList);
        return response;
    }

    @Override
    public ListActivePersonalCenterSettingsResponse listActivePersonalCenterSettings(ListActivePersonalCenterSettingsCommand cmd) {
        ListActivePersonalCenterSettingsResponse response = new ListActivePersonalCenterSettingsResponse();
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryActivePersonalCenterSettings(cmd.getNamespaceId());
        if (CollectionUtils.isEmpty(list)) {
            list = this.personalCenterSettingProvider.queryDefaultPersonalCenterSettings();
        }
        List<PersonalCenterSettingDTO> dtoList = new ArrayList<>();
        list.stream().forEach((r) -> {
             dtoList.add(ConvertHelper.convert(r,PersonalCenterSettingDTO.class));
        });
        return response;
    }

    @Override
    public ListPersonalCenterSettingsResponse listPersonalCenterSettingsByNamespaceIdAndVersion(ListPersonalCenterSettingsCommand cmd) {
        ListPersonalCenterSettingsResponse response = new ListPersonalCenterSettingsResponse();
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryPersonalCenterSettingsByNamespaceIdAndVersion(cmd.getNamespaceId(), cmd.getVersion());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<PersonalCenterSettingDTO> dtoList = new ArrayList<>();
        list.stream().forEach((r) -> {
            dtoList.add(ConvertHelper.convert(r,PersonalCenterSettingDTO.class));
        });
        return response;
    }
}
