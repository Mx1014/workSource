// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.rest.personal_center.CreatePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.CreatePersonalSettingCommand;
import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListUserOrganizationCommand;
import com.everhomes.rest.personal_center.ListUserOrganizationResponse;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.personal_center.UpdateUserCompanyCommand;
import com.everhomes.rest.user.UserInfo;

public interface PersonalCenterService {
    void createUserEmail(CreateUserEmailCommand cmd);
    UserInfo updateShowCompanyFlag(UpdateShowCompanyCommand cmd);
    UserInfo updateUserCompany(UpdateUserCompanyCommand cmd);
    ListUserOrganizationResponse listUserOrganization(ListUserOrganizationCommand cmd);
    ListActivePersonalCenterSettingsResponse listActivePersonalCenterSettings(ListActivePersonalCenterSettingsCommand cmd);
    ListPersonalCenterSettingsResponse listPersonalCenterSettingsByNamespaceIdAndVersion(ListPersonalCenterSettingsCommand cmd);
    CreatePersonalCenterSettingsResponse createPersonalCenterSettings(CreatePersonalSettingCommand cmd);
}
