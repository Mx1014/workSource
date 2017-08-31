package com.everhomes.archives;

import com.everhomes.rest.archives.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ArchivesService {

    ArchivesContactDTO addArchivesContact(AddArchivesContactCommand cmd);

    void transferArchivesContacts(TransferArchivesContactsCommand cmd);

    void deleteArchivesContacts(DeleteArchivesContactsCommand cmd);

    void stickArchivesContact(StickArchivesContactCommand cmd);

    ListArchivesContactsResponse listArchivesContacts(ListArchivesContactsCommand cmd);

    ImportFileTaskDTO importArchivesContacts(
            MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesContactsCommand cmd);

    void exportArchivesContacts(ExportArchivesContactsCommand cmd, HttpServletResponse httpResponse);

    void verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd);

    ListArchivesEmployeesResponse listArchivesEmployees(ListArchivesEmployeesCommand cmd);

    ArchivesEmployeeDTO addArchivesEmployee(AddArchivesEmployeeCommand cmd);

        GetArchivesEmployeeResponse getArchivesEmployee( GetArchivesEmployeeCommand cmd);

    ListArchivesDismissEmployeesResponse listArchivesDismissEmployees(ListArchivesDismissEmployeesCommand cmd);

    void employArchivesEmployees(EmployArchivesEmployeesCommand cmd);

    void transferArchivesEmployees(TransferArchivesEmployeesCommand cmd);

    void dismissArchivesEmployees(DismissArchivesEmployeesCommand cmd);

    void updateArchivesForm(UpdateArchivesFormCommand cmd);

    GetArchivesFormResponse getArchivesForm(GetArchivesFormCommand cmd);

    Long identifyArchivesForm(IdentifyArchivesFormCommand cmd);

    ImportFileTaskDTO importArchivesEmployees(MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesEmployeesCommand cmd);

    void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd, HttpServletResponse httpResponse);

    void remindArchivesEmployee(RemindArchivesEmployeeCommand cmd);
}
