package com.everhomes.archives;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.archives.ListArchivesContactsCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.organization.FilterOrganizationContactScopeType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

public class ArchivesContactsExportTaskHandler implements FileDownloadTaskHandler{

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ArchivesService archivesService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        Long organizationId = null;
        if(params.get("organizationId") != null)
            organizationId = (Long) params.get("organizationId");
        String keywords = null;
        if(params.get("keywords") != null)
            keywords = (String) params.get("keywords");
        Integer namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));

        ListArchivesContactsCommand cmd = new ListArchivesContactsCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setKeywords(keywords);
        cmd.setNamespaceId(namespaceId);

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = archivesService.getArchivesContactsOutputStream(cmd, taskId);
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
