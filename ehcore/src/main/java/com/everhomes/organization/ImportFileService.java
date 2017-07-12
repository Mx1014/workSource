package com.everhomes.organization;


import com.everhomes.rest.common.ImportFileResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by sfyan on 2017/4/21.
 */
public interface ImportFileService {

    ImportFileTask executeTask(ExecuteImportTaskCallback callback, ImportFileTask task);

    ImportFileResponse getImportFileResult(Long taskId);

    void exportImportFileFailResultXls(HttpServletResponse httpResponse, Long taskId, Map<String, String> overrideTitleMap);

    void exportImportFileFailResultXls(HttpServletResponse httpResponse, Long taskId);

}
