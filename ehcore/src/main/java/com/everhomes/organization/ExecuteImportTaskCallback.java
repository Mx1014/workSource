package com.everhomes.organization;

import com.everhomes.rest.common.ImportFileResponse;

/**
 * Created by zl on 2017/4/21.
 */
public interface ExecuteImportTaskCallback<T> {

    ImportFileResponse<T> importFile();

}
