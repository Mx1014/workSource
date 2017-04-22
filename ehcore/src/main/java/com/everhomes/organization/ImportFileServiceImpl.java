package com.everhomes.organization;


import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sfyan on 2017/4/21.
 */
public class ImportFileServiceImpl implements ImportFileService{

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Override
    public ImportFileTask executeTask(ExecuteImportTaskCallback callback, ImportFileTask task){
        task.setStatus(ImportFileTaskStatus.CREATED.getCode());
        organizationProvider.createImportFileTask(task);

        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                task.setStatus(ImportFileTaskStatus.EXECUTING.getCode());
                organizationProvider.updateImportFileTask(task);
                ImportFileResponse response = callback.importFile();
                task.setStatus(ImportFileTaskStatus.FINISH.getCode());
                task.setResult(StringHelper.toJsonString(response));
                organizationProvider.updateImportFileTask(task);
            }
        });
        return task;
    }

    @Override
    public ImportFileResponse getImportFileResult(Long taskId) {

        User user = UserContext.current().getUser();

        ImportFileResponse response = new ImportFileResponse();

        ImportFileTask task = organizationProvider.findImportFileTaskById(taskId);

        if(ImportFileTaskStatus.FINISH == ImportFileTaskStatus.fromCode(task.getStatus())){
            response =  (ImportFileResponse)StringHelper.fromJsonString(task.getResult(), ImportFileResponse.class);
        }

        List<ImportFileResultLog> logs =  response.getLogs();
        Map<String, String> map = new HashMap<>();
        for (ImportFileResultLog log: logs) {
            log.setErrorDescription(localeTemplateService.getLocaleTemplateString(log.getScope(), log.getCode(), user.getLocale(), map, ""));
        }
        response.setImportStatus(task.getStatus());

        return response;
    }
}
