package com.everhomes.archives;

import com.alibaba.fastjson.JSON;
import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.archives.ListArchivesContactsCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ArchivesContactsExportTaskHandler implements FileDownloadTaskHandler {

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
        List<String> filterScopeTypes = null;
        if (params.get("filterScopeTypes") != null)
            filterScopeTypes = JSON.parseArray((String) params.get("filterScopeTypes"), String.class);
        List<String> targetTypes = null;
        if (params.get("targetTypes") != null)
            targetTypes = JSON.parseArray((String) params.get("targetTypes"), String.class);
        Integer namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        UserContext.setCurrentNamespaceId(namespaceId);

        ListArchivesContactsCommand cmd = new ListArchivesContactsCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setKeywords(keywords);
        cmd.setFilterScopeTypes(filterScopeTypes);
        cmd.setTargetTypes(targetTypes);
        cmd.setNamespaceId(namespaceId);
        cmd.setPageSize(Integer.MAX_VALUE - 1);

        String fileName = (String) params.get("name");
        Long taskId = (Long) params.get("taskId");
        OutputStream outputStream = archivesDTSService.getArchivesContactsExportStream(cmd, taskId);
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
