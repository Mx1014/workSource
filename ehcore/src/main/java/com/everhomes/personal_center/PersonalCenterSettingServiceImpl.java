// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;

public class PersonalCenterSettingServiceImpl implements PersonalCenterService{

    @Autowired
    private UserProvider userProvider;

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
}
