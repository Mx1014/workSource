package com.everhomes.archives;

import java.util.List;

public interface ArchivesConfigurationService {

    void sendingMailJob(Integer hour, List<ArchivesNotifications> notifyLists);
}
