package com.everhomes.rest.archives;

public interface ArchivesServiceErrorCode {

    static final String SCOPE = "archives";

    int ERROR_NAME_ISEMPTY = 100001;    //  姓名不能为空
    int ERROR_NAME_TOOLONG = 100002;    //  姓名过长
    int ERROR_CONTACTTOKEN_ISEMPTY = 100004;    //  手机号不能为空

}
