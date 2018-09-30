package com.everhomes.notice;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.notice.CancelEnterpriseNoticeCommand;
import com.everhomes.rest.notice.CreateEnterpriseNoticeCommand;
import com.everhomes.rest.notice.DeleteEnterpriseNoticeCommand;
import com.everhomes.rest.notice.EnterpriseNoticeDTO;
import com.everhomes.rest.notice.EnterpriseNoticePreviewDTO;
import com.everhomes.rest.notice.EnterpriseNoticeShowType;
import com.everhomes.rest.notice.EnterpriseNoticeStatus;
import com.everhomes.rest.notice.GetCurrentUserContactInfoCommand;
import com.everhomes.rest.notice.GetEnterpriseNoticeCommand;
import com.everhomes.rest.notice.GetSharedEnterpriseNoticeCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeAdminCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeAdminResponse;
import com.everhomes.rest.notice.ListEnterpriseNoticeCommand;
import com.everhomes.rest.notice.ListEnterpriseNoticeResponse;
import com.everhomes.rest.notice.StickyEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UnStickyEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UpdateEnterpriseNoticeCommand;
import com.everhomes.rest.notice.UserContactSimpleInfoDTO;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "EnterpriseNotice controller", site = "core")
@RestController
@RequestMapping("/enterpriseNotice")
public class EnterpriseNoticeController extends ControllerBase {

    @Autowired
    private EnterpriseNoticeService enterpriseNoticeService;

    /**
     * <b>URL : /enterpriseNotice/createEnterpriseNotice</b>
     * <p>创建公告</p>
     *
     * @param cmd ，请查看{@link CreateEnterpriseNoticeCommand}
     */
    @RequestMapping("createEnterpriseNotice")
    @RestReturn(value = EnterpriseNoticePreviewDTO.class)
    public RestResponse createEnterpriseNotice(CreateEnterpriseNoticeCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.createEnterpriseNotice(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/updateEnterpriseNotice</b>
     * <p>编辑公告</p>
     *
     * @param cmd，请查看{@link UpdateEnterpriseNoticeCommand}
     */
    @RequestMapping("updateEnterpriseNotice")
    @RestReturn(value = EnterpriseNoticePreviewDTO.class)
    public RestResponse updateEnterpriseNotice(UpdateEnterpriseNoticeCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.updateEnterpriseNotice(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/getEnterpriseNoticeDetail</b>
     * <p>获取公告详细信息</p>
     *
     * @param cmd，请查看{@link GetEnterpriseNoticeCommand}
     * @return {@link EnterpriseNoticeDTO}
     */
    @RequestMapping("getEnterpriseNoticeDetail")
    @RestReturn(value = EnterpriseNoticeDTO.class)
    public RestResponse getEnterpriseNoticeDetail(GetEnterpriseNoticeCommand cmd) {
        EnterpriseNoticeDTO enterpriseNoticeDTO = null;
        boolean isPreview = EnterpriseNoticeShowType.PREVIEW == EnterpriseNoticeShowType.fromCode(cmd.getShowType());
        if (isPreview || enterpriseNoticeService.isNoticeSendToCurrentUser(cmd.getOrganizationId(), cmd.getId())) {
            enterpriseNoticeDTO = enterpriseNoticeService.getEnterpriseNoticeDetailInfo(cmd.getId());
        }
        if (enterpriseNoticeDTO == null) {
            enterpriseNoticeDTO = new EnterpriseNoticeDTO();
            enterpriseNoticeDTO.setStatus(EnterpriseNoticeStatus.DELETED.getCode());
        }
        RestResponse response = new RestResponse(enterpriseNoticeDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/getEnterpriseNoticeDetail4Admin</b>
     * <p>获取公告详细信息</p>
     *
     * @param cmd，请查看{@link GetEnterpriseNoticeCommand}
     * @return {@link EnterpriseNoticeDTO}
     */
    @RequestMapping("getEnterpriseNoticeDetail4Admin")
    @RestReturn(value = EnterpriseNoticeDTO.class)
    public RestResponse getEnterpriseNoticeDetail4Admin(GetEnterpriseNoticeCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.getEnterpriseNoticeDetailInfo(cmd.getId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/deleteEnterpriseNotice</b>
     * <p>删除公告（标记为‘DELETED(0)’）</p>
     *
     * @param cmd，请查看{@link DeleteEnterpriseNoticeCommand}
     */
    @RequestMapping("deleteEnterpriseNotice")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterpriseNotice(DeleteEnterpriseNoticeCommand cmd) {
        enterpriseNoticeService.deleteEnterpriseNotice(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/cancelEnterpriseNotice</b>
     * <p>撤销公告（标记为‘INACTIVE(3)’）</p>
     *
     * @param cmd，请查看{@link CancelEnterpriseNoticeCommand}
     */
    @RequestMapping("cancelEnterpriseNotice")
    @RestReturn(value = String.class)
    public RestResponse cancelEnterpriseNotice(CancelEnterpriseNoticeCommand cmd) {
        enterpriseNoticeService.cancelEnterpriseNotice(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/stickyEnterpriseNotice</b>
     * <p>置顶公告</p>
     *
     * @param cmd，请查看{@link StickyEnterpriseNoticeCommand}
     */
    @RequestMapping("stickyEnterpriseNotice")
    @RestReturn(value = String.class)
    public RestResponse stickyEnterpriseNotice(StickyEnterpriseNoticeCommand cmd) {
        enterpriseNoticeService.stickyEnterpriseNotice(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/unStickyEnterpriseNotice</b>
     * <p>取消置顶公告</p>
     *
     * @param cmd，请查看{@link UnStickyEnterpriseNoticeCommand}
     */
    @RequestMapping("unStickyEnterpriseNotice")
    @RestReturn(value = String.class)
    public RestResponse unStickyEnterpriseNotice(UnStickyEnterpriseNoticeCommand cmd) {
        enterpriseNoticeService.unStickyEnterpriseNotice(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/listEnterpriseNoticeByNamespaceId</b>
     * <P>分页查询公告列表</P>
     *
     * @param cmd，请查看{@link ListEnterpriseNoticeAdminCommand}
     * @return {@link ListEnterpriseNoticeAdminResponse}
     */
    @RequestMapping("listEnterpriseNoticeByNamespaceId")
    @RestReturn(value = ListEnterpriseNoticeAdminResponse.class)
    public RestResponse listEnterpriseNoticeByNamespaceId(ListEnterpriseNoticeAdminCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.listEnterpriseNoticesByNamespaceId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL : /enterpriseNotice/listEnterpriseNoticeByUserId</b>
     * <P>分页查询公告列表</P>
     *
     * @param cmd，请查看{@link ListEnterpriseNoticeCommand}
     * @return {@link ListEnterpriseNoticeResponse}
     */
    @RequestMapping("listEnterpriseNoticeByUserId")
    @RestReturn(value = ListEnterpriseNoticeResponse.class)
    public RestResponse listEnterpriseNoticeByUserId(ListEnterpriseNoticeCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.listEnterpriseNoticesByUserId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /enterpriseNotice/getCurrentUserContactSimpleInfo</b>
     * <p>查询当前用户的姓名和手机号等简单信息</p>
     */
    @RequestMapping("getCurrentUserContactSimpleInfo")
    @RestReturn(value = UserContactSimpleInfoDTO.class)
    public RestResponse getCurrentUserContactSimpleInfo(GetCurrentUserContactInfoCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.getCurrentUserContactSimpleInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL : /enterpriseNotice/getSharedEnterpriseNoticeDetail</b>
     * <p>获取分享公告的详细信息</p>
     */
    @RequestMapping("getSharedEnterpriseNoticeDetail")
    @RestReturn(value = EnterpriseNoticeDTO.class)
    @RequireAuthentication(false)
    public RestResponse getSharedEnterpriseNoticeDetail(GetSharedEnterpriseNoticeCommand cmd) {
        RestResponse response = new RestResponse(enterpriseNoticeService.getSharedEnterpriseNoticeDetailInfo(cmd.getNoticeToken()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
