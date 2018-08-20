// @formatter:off
package com.everhomes.announcement;

import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;

public interface AnnouncementService {
    AnnouncementDTO createAnnouncement(CreateAnnouncementCommand cmd);
}
