package com.everhomes.archives;

import java.util.List;

public interface ArchivesConfigurationService {

    void sendingMail(Integer hour, List<ArchivesNotifications> notifyLists);
}
