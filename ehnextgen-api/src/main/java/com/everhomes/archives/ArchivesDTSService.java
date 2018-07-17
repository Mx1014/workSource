package com.everhomes.archives;

import com.everhomes.rest.archives.ExportArchivesEmployeesCommand;
import com.everhomes.rest.archives.ExportArchivesEmployeesTemplateCommand;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public interface ArchivesDTSService {

    void exportArchivesEmployees(ExportArchivesEmployeesCommand cmd);

    OutputStream getArchivesEmployeesExportStream(ExportArchivesEmployeesCommand cmd, Long taskId);

    void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse);


}
