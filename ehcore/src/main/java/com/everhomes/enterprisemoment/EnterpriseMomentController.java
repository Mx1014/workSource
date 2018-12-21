// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprisemoment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enterprisemoment")
public class EnterpriseMomentController extends ControllerBase {
    @Autowired
    private EnterpriseMomentService enterpriseMomentService;


    /**
     * <b>URL: /enterprisemoment/checkAdmin</b>
     * <p>查询当前用户是否为该公司的管理员(目前是企业管理员)</p>
     */
    @RequestMapping("checkAdmin")
    @RestReturn(value = CheckAdminResponse.class)
    public RestResponse checkAdmin(CheckAdminCommand cmd) {
        CheckAdminResponse res = enterpriseMomentService.checkAdmin(cmd);
        RestResponse response = new RestResponse(res);
        return response;
    }

    /**
     * <p>获取动态列表</p>
     * <b>URL: /enterprisemoment/listMoments</b>
     */
    @RequestMapping("listMoments")
    @RestReturn(ListMomentsResponse.class)
    public RestResponse listMoments(ListMomentsCommand cmd) {
        return new RestResponse(enterpriseMomentService.listMoments(cmd));
    }

    /**
     * <p>获取动态详情</p>
     * <b>URL: /enterprisemoment/getMomentDetail</b>
     */
    @RequestMapping("getMomentDetail")
    @RestReturn(MomentDTO.class)
    public RestResponse getMomentDetail(GetMomentDetailCommand cmd) {
        return new RestResponse(enterpriseMomentService.getMomentDetail(cmd));
    }

    /**
     * <p>发布动态</p>
     * <b>URL: /enterprisemoment/createMoment</b>
     */
    @RequestMapping("createMoment")
    @RestReturn(MomentDTO.class)
    public RestResponse createMoment(CreateMomentCommand cmd) {
        return new RestResponse(enterpriseMomentService.createMoment(cmd));
    }

    /**
     * <p>删除动态</p>
     * <b>URL: /enterprisemoment/deleteMoment</b>
     */
    @RequestMapping("deleteMoment")
    @RestReturn(String.class)
    public RestResponse deleteMoment(DeleteMomentCommand cmd) {
        enterpriseMomentService.deleteMoment(cmd);
        return new RestResponse();
    }

    /**
     * <p>获取消息列表</p>
     * <b>URL: /enterprisemoment/listMomentMessages</b>
     */
    @RequestMapping("listMomentMessages")
    @RestReturn(ListMomentMessagesResponse.class)
    public RestResponse listMomentMessages(ListMomentMessagesCommand cmd) {
        return new RestResponse(enterpriseMomentService.listMomentMessages(cmd));
    }

    /**
     * <p>查询tag列表</p>
     * <b>URL: /enterprisemoment/listTags</b>
     */
    @RequestMapping("listTags")
    @RestReturn(ListTagsResponse.class)
    public RestResponse listTags(ListTagsCommand cmd) {
        return new RestResponse(enterpriseMomentService.listTags(cmd));
    }

    /**
     * <p>编辑tag列表(添加、删除、更新)</p>
     * <b>URL: /enterprisemoment/editTags</b>
     */
    @RequestMapping("editTags")
    @RestReturn(ListTagsResponse.class)
    public RestResponse editTags(EditTagsCommand cmd){
        return new RestResponse(enterpriseMomentService.editTags(cmd));
    }

    /**
     * <p>对动态点赞</p>
     * <b>URL: /enterprisemoment/likeMoment</b>
     */
    @RequestMapping("likeMoment")
    @RestReturn(String.class)
    public RestResponse likeMoment(LikeMomentCommand cmd) {
        enterpriseMomentService.likeMoment(cmd);
        return new RestResponse();
    }

    /**
     * <p>对动态取消点赞</p>
     * <b>URL: /enterprisemoment/unlikeMoment</b>
     */
    @RequestMapping("unlikeMoment")
    @RestReturn(String.class)
    public RestResponse unlikeMoment(UnlikeMomentCommand cmd) {
        enterpriseMomentService.unlikeMoment(cmd);
        return new RestResponse();
    }

    /**
     * <p>获取动态的点赞列表</p>
     * <b>URL: /enterprisemoment/listMomentFavourites</b>
     */
    @RequestMapping("listMomentFavourites")
    @RestReturn(ListMomentFavouritesResponse.class)
    public RestResponse listMomentFavourites(ListMomentFavouritesCommand cmd) {
        return new RestResponse(enterpriseMomentService.listMomentFavourites(cmd));
    }

    /**
     * <p>获取头部背景图</p>
     * <b>URL: /enterprisemoment/getBanner</b>
     */
    @RequestMapping("getBanner")
    @RestReturn(GetBannerResponse.class)
    public RestResponse getBanner(GetBannerCommand cmd) {
        return new RestResponse(enterpriseMomentService.getBanner(cmd));
    }
}