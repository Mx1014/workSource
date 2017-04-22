package com.everhomes.organization;


import com.everhomes.rest.common.ImportFileResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by sfyan on 2017/4/21.
 */
public interface ImportFileService {

    ImportFileTask executeTask(ExecuteImportTaskCallback callback, ImportFileTask task);

    ImportFileResponse getImportFileResult(Long taskId);

    void exportImportFileFialResult(HttpServletResponse httpResponse, Long taskId);

}
