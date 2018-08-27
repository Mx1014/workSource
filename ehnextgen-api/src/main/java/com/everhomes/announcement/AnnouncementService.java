// @formatter:off
package com.everhomes.announcement;

import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CancelLikeAnnouncementCommand;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import com.everhomes.rest.announcement.DeleteAnnouncementCommand;
import com.everhomes.rest.announcement.GetAnnouncementCommand;
import com.everhomes.rest.announcement.LikeAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementResponse;
import com.everhomes.rest.announcement.QueryAnnouncementCommand;

public interface AnnouncementService {
    AnnouncementDTO createAnnouncement(CreateAnnouncementCommand cmd);

    AnnouncementDTO getAnnouncement(GetAnnouncementCommand cmd);

    void deleteAnnouncement(DeleteAnnouncementCommand cmd);

    ListAnnouncementResponse listAnnouncement(ListAnnouncementCommand cmd);

    void likeAnnouncement(LikeAnnouncementCommand cmd);

    void cancelLikeAnnouncement(CancelLikeAnnouncementCommand cmd);
}
