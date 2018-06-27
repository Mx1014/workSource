// @formatter:off
package com.everhomes.sensitiveWord;

import java.util.List;

public interface SensitiveFilterRecordProvider {

    void createSensitiveFilterRecord(SensitiveFilterRecord sensitiveFilterRecord);

    List<SensitiveFilterRecord> listSensitiveFilterRecord(Integer namespaceId,Long communityId, Long pageAnchor, Integer pageSize);

    SensitiveFilterRecord getSensitiveFilterRecord(Long id);
}
