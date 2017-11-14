package com.everhomes.archives;

import java.util.List;

public interface ArchivesConfigurationService {

    void sendingMail(List<ArchivesNotifications> notifyLists);
}
