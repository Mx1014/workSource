// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.rest.sensitiveWord.admin.GetSensitiveFilterRecordAdminCommand;
import com.everhomes.rest.sensitiveWord.admin.ListSensitiveFilterRecordAdminCommand;
import com.everhomes.rest.sensitiveWord.admin.ListSensitiveFilterRecordAdminResponse;
import com.everhomes.rest.sensitiveWord.admin.SensitiveFilterRecordAdminDTO;

public interface SensitiveFilterRecordService {

    ListSensitiveFilterRecordAdminResponse listSensitiveRecord(ListSensitiveFilterRecordAdminCommand cmd);
    SensitiveFilterRecordAdminDTO getSensitiveFilterRecord(GetSensitiveFilterRecordAdminCommand cmd);
}
