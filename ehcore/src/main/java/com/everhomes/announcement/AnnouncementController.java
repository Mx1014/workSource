// @formatter:off
package com.everhomes.announcement;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CancelLikeAnnouncementCommand;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import com.everhomes.rest.announcement.DeleteAnnouncementCommand;
import com.everhomes.rest.announcement.GetAnnouncementCommand;
import com.everhomes.rest.announcement.LikeAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementCommand;
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
    @XssExclude
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

    /**
     * <b>URL: /announcement/listAnnouncement</b>
     * <p>查询公告</p>
     */
    @RequestMapping("listAnnouncement")
    @RestReturn(value=ListAnnouncementResponse.class)
    @RequireAuthentication(false)
    public RestResponse listAnnouncement(ListAnnouncementCommand cmd) {
        ListAnnouncementResponse res = announcementService.listAnnouncement(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /announcement/likeAnnouncement</b>
     * <p>对公告点赞</p>
     * @return 点赞的结果
     */
    @RequestMapping("likeAnnouncement")
    @RestReturn(value=String.class)
    public RestResponse likeAnnouncement(LikeAnnouncementCommand cmd) {
        this.announcementService.likeAnnouncement(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /announcement/cancelLikeAnnouncement</b>
     * <p>对公告取消赞</p>
     * @return 取消赞的结果
     */
    @RequestMapping("cancelLikeAnnouncement")
    @RestReturn(value=String.class)
    public RestResponse cancelLikeAnnouncement(CancelLikeAnnouncementCommand cmd) {
        this.announcementService.cancelLikeAnnouncement(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
