// @formatter:off
package com.everhomes.sensitiveWord;

import java.util.List;

public interface SensitiveFilterRecordProvider {

    List<SensitiveFilterRecord> listSensitiveFilterRecord(Long namespaceId, Long pageAnchor, Integer pageSize);

    SensitiveFilterRecord getSensitiveFilterRecord(Long id);
}
