// @formatter:off
package com.everhomes.organization;

import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserResetIdentifierVo;
import com.everhomes.user.ResetUserIdentifierEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * organization member 对于用户修改手机号的监听器，一旦用户修改了手机号，就会触发这个监听器
 * Created by xq.tian on 2017/6/28.
 */
@Component
public class OrganizationMemberResetIdentifierListener implements ApplicationListener<ResetUserIdentifierEvent> {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Async
    @Override
    public void onApplicationEvent(ResetUserIdentifierEvent event) {
        UserResetIdentifierVo vo = (UserResetIdentifierVo)event.getSource();
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(vo.getUid());
        if (members != null) {
            members.stream().filter(r ->
                r.getContactType() != null && r.getContactType() == IdentifierType.MOBILE.getCode()
            ).forEach(r -> {
                r.setContactToken(vo.getNewIdentifier());
                organizationProvider.updateOrganizationMember(r);
            });
        }
    }
}
