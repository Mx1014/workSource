// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListUserOrganizationCommand;
import com.everhomes.rest.personal_center.ListUserOrganizationResponse;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.personal_center.UpdateUserCompanyCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserInfoDTO;

public interface PersonalCenterService {
    void createUserEmail(CreateUserEmailCommand cmd);
    UserInfo updateShowCompanyFlag(UpdateShowCompanyCommand cmd);
    UserInfo updateUserCompany(UpdateUserCompanyCommand cmd);
    ListUserOrganizationResponse listUserOrganization(ListUserOrganizationCommand cmd);
    ListPersonalCenterSettingsResponse listPersonalCenterSettings(ListPersonalCenterSettingsCommand cmd);
}
