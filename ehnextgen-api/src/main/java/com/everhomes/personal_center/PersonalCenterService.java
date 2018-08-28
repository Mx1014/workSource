// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.user.UserInfoDTO;

public interface PersonalCenterService {
    void createUserEmail(CreateUserEmailCommand cmd);
    UserInfoDTO updateShowCompanyFlag(UpdateShowCompanyCommand cmd);
}
