// @formatter:off
package com.everhomes.announcement;

import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import com.everhomes.rest.announcement.DeleteAnnouncementCommand;
import com.everhomes.rest.announcement.GetAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementResponse;
import com.everhomes.rest.announcement.QueryAnnouncementCommand;

public interface AnnouncementService {
    AnnouncementDTO createAnnouncement(CreateAnnouncementCommand cmd);

    ListAnnouncementResponse queryAnnouncementByCategory(QueryAnnouncementCommand cmd);

    AnnouncementDTO getAnnouncement(GetAnnouncementCommand cmd);

    void deleteAnnouncement(DeleteAnnouncementCommand cmd);
}
