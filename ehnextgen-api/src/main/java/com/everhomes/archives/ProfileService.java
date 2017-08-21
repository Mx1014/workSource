package com.everhomes.profile;

import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.profile.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ProfileService {

    ProfileContactDTO addProfileContact(AddProfileContactCommand cmd);

    void transferProfileContacts(TransferProfileContactsCommand cmd);

    void deleteProfileContacts(DeleteProfileContactsCommand cmd);

    void stickProfileContact(StickProfileContactCommand cmd);

    ListProfileContactsResponse listProfileContacts(ListProfileContactsCommand cmd);

    ImportFileTaskDTO importProfileContacts(
            MultipartFile mfile, Long userId, Integer namespaceId, ImportProfileContactsCommand cmd);

    void exportProfileContacts(ExportProfileContactsCommand cmd, HttpServletResponse httpResponse);

    void verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd);

    ListProfileEmployeesResponse listProfileEmployees(ListProfileEmployeesCommand cmd);

    ProfileEmployeeDTO addProfileEmployee(AddProfileEmployeeCommand cmd);

    ListProfileDismissEmployeesResponse listProfileDismissEmployees(ListProfileDismissEmployeesCommand cmd);

    void employProfileEmployees(EmployProfileEmployeesCommand cmd);

    void transferProfileEmployees(TransferProfileEmployeesCommand cmd);

    void dismissProfileEmployees(DismissProfileEmployeesCommand cmd);

    void addProfileField(AddProfileFieldCommand cmd);

    void addProfileFieldGroup(AddProfileFieldGroupCommand cmd);

    void updateProfileFieldOrder(UpdateProfileFieldOrderCommand cmd);

    GetProfileFieldResponse getProfileField(GetProfileFieldCommand cmd);
}
