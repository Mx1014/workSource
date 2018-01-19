package com.everhomes.rest.filemanagement;

public interface FileManagementErrorCode {

    String SCOPE = "FILE_MANAGEMENT";

    int ERROR_NAME_ALREADY_EXISTS = 10001; //  名称已存在

    int ERROR_SUFFIX_NULL = 10002;  //  后缀名为空
}
