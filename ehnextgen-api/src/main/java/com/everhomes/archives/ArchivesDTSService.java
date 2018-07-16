package com.everhomes.archives;

import com.everhomes.rest.archives.ExportArchivesEmployeesTemplateCommand;

import javax.servlet.http.HttpServletResponse;

public interface ArchivesDTSService {

    void exportArchivesEmployeesTemplate(ExportArchivesEmployeesTemplateCommand cmd, HttpServletResponse httpResponse);

}
