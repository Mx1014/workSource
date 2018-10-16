package com.everhomes.rest.filemanagement;

public interface FileManagementErrorCode {

    String SCOPE = "FILE_MANAGEMENT";

    int ERROR_NAME_ALREADY_EXISTS = 10001; //  名称已存在

    int ERROR_SUFFIX_NULL = 10002;  //  后缀名为空

    int ERROR_FILE_CATALOG_NOT_FOUND = 10003; //  目录未找到

    int ERROR_FILE_CONTENT_NOT_FOUND = 10004; //  文件或文件夹未找到

    int ERROR_NAME_GENERATE_FAILED = 10005; //  自动名称设置失败

    int ERROR_CANNOT_MOVE = 10006; //  移动不合法:比如移动到自己下级目录
    int ERROR_LOOP = 10007; //  循环太多次疑似死循环
}
