// @formatter:off
package com.everhomes.announcement;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementController.class);

    @Autowired
    private AnnouncementService announcementService;

    /**
     * <b>URL: /announcement/newAnnouncement</b>
     * <p>发布公告</p>
     */
    @RequestMapping("newAnnouncement")
    @RestReturn(value = AnnouncementDTO.class)
    public RestResponse newOrgTopic(@Valid CreateAnnouncementCommand cmd) {
        AnnouncementDTO announcementDTO = this.announcementService.createAnnouncement(cmd);
        RestResponse response = new RestResponse(announcementDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
