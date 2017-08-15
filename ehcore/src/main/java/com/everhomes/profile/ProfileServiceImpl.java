package com.everhomes.profile;

import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.profile.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Autowired ProfileProvider profileProvider;

    @Override
    public AddProfileContactsResponse addProfileContacts(AddProfileContactsCommand cmd) {
        return null;
    }

    @Override
    public void transferProfileContacts(TransferProfileContactsCommand cmd) {

    }

    @Override
    public void deleteProfileContacts(DeleteProfileContactsCommand cmd) {

    }

    @Override
    public void stickProfileContacts(StickProfileContactsCommand cmd) {
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
}
