// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.rest.sensitiveWord.admin.*;

public interface SensitiveFilterRecordService {

    ListSensitiveFilterRecordAdminResponse listSensitiveRecord(ListSensitiveFilterRecordAdminCommand cmd);
    SensitiveFilterRecordAdminDTO getSensitiveFilterRecord(GetSensitiveFilterRecordAdminCommand cmd);
    GetSensitiveWordUrlAdminResponse getSensitiveWordUrl();
}
