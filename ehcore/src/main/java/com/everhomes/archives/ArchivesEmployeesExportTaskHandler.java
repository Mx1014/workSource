package com.everhomes.archives;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.archives.ExportArchivesEmployeesCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.Map;

@Component
public class ArchivesEmployeesExportTaskHandler implements FileDownloadTaskHandler{

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ArchivesDTSService archivesDTSService;

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
        Integer namespaceId = null;
        if(params.get("namespaceId") != null)
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        Long userId = (Long) params.get("userId");

        //  set the basic data
        User user = new User();
        user.setNamespaceId(namespaceId);
        user.setId(userId);

        UserContext.setCurrentUser(user);
        UserContext.setCurrentNamespaceId(namespaceId);


        ExportArchivesEmployeesCommand cmd = new ExportArchivesEmployeesCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setKeywords(keywords);
        cmd.setNamespaceId(namespaceId);

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = archivesDTSService.getArchivesEmployeesExportStream(cmd, taskId);
        CsFileLocationDTO fileLocationDTO = fileDownloadTaskService.uploadToContenServer(fileName, outputStream, taskId);
        taskService.processUpdateTask(taskId, fileLocationDTO);
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
