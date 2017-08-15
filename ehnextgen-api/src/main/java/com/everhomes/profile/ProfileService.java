package com.everhomes.profile;

import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.profile.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ProfileService {

    AddProfileContactsResponse addProfileContacts(AddProfileContactsCommand cmd);

    void transferProfileContacts(TransferProfileContactsCommand cmd);

    void deleteProfileContacts(DeleteProfileContactsCommand cmd);

    void stickProfileContacts(StickProfileContactsCommand cmd);

    ListProfileContactsResponse listProfileContacts(ListProfileContactsCommand cmd);

    ImportFileTaskDTO importProfileContacts(
            MultipartFile mfile, Long userId, Integer namespaceId, ImportProfileContactsCommand cmd);

    void exportProfileContacts(ExportProfileContactsCommand cmd, HttpServletResponse httpResponse);
}
