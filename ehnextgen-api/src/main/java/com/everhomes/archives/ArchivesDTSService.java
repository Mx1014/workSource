package com.everhomes.archives;

import com.everhomes.rest.archives.*;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.GetImportFileResultCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public interface ArchivesDTSService {

    ImportFileTaskDTO importArchivesContacts(
            MultipartFile mfile, Long userId, Integer namespaceId, ImportArchivesContactsCommand cmd);

    ImportFileResponse<ImportArchivesContactsDTO> getImportContactsResult(GetImportFileResultCommand cmd);

    void exportArchivesContacts(ListArchivesContactsCommand cmd);

    OutputStream getArchivesContactsExportStream(ListArchivesContactsCommand cmd, Long taskId);

    void verifyPersonnelByPassword(VerifyPersonnelByPasswordCommand cmd);

    ImportFileTaskDTO importArchivesEmployees(MultipartFile mfile, ImportArchivesEmployeesCommand cmd);

    ImportFileResponse<ImportArchivesEmployeesDTO> getImportEmployeesResult(GetImportFileResultCommand cmd);

    void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd);

    OutputStream getArchivesEmployeesExportStream(ExportArchivesEmployeesCommand cmd, Long taskId);

    void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse);

    void exportImportFileFailResults(GetImportFileResultCommand cmd, HttpServletResponse httpResponse);

}
