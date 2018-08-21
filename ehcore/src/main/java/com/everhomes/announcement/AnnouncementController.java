// @formatter:off
package com.everhomes.announcement;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import com.everhomes.rest.announcement.DeleteAnnouncementCommand;
import com.everhomes.rest.announcement.GetAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementResponse;
import com.everhomes.rest.announcement.QueryAnnouncementCommand;
import com.everhomes.util.RequireAuthentication;
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

    /**
     * <b>URL: /announcement/queryAnnouncementByCategory</b>
     * <p>查询公告列表</p>
     */
    @RequestMapping("queryAnnouncementByCategory")
    @RestReturn(value = ListAnnouncementResponse.class)
    @RequireAuthentication(false)
    public RestResponse queryAnnouncementByCategory(QueryAnnouncementCommand cmd) {
        ListAnnouncementResponse cmdResponse = announcementService.queryAnnouncementByCategory(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /announcement/getAnnouncement</b>
     * <p>获取公告内容</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getAnnouncement")
    @RestReturn(value=AnnouncementDTO.class)
    public RestResponse getAnnouncement(GetAnnouncementCommand cmd) {
        AnnouncementDTO announcementDTO = this.announcementService.getAnnouncement(cmd);
        RestResponse response = new RestResponse(announcementDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /announcement/deleteAnnouncement</b>
     * <p>删除公告</p>
     * @return 删除结果
     */
    @RequestMapping("deleteAnnouncement")
    @RestReturn(value=String.class)
    public RestResponse deleteAnnouncement(DeleteAnnouncementCommand cmd) {
        this.announcementService.deleteAnnouncement(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
