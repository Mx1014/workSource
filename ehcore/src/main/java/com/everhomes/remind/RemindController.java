package com.everhomes.remind;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.remind.BatchSortRemindCategoryCommand;
import com.everhomes.rest.remind.BatchSortRemindCommand;
import com.everhomes.rest.remind.CreateOrUpdateRemindCategoryCommand;
import com.everhomes.rest.remind.CreateOrUpdateRemindCommand;
import com.everhomes.rest.remind.DeleteRemindCategoryCommand;
import com.everhomes.rest.remind.DeleteRemindCommand;
import com.everhomes.rest.remind.GetCurrentUserDetailIdCommand;
import com.everhomes.rest.remind.GetCurrentUserDetailIdResponse;
import com.everhomes.rest.remind.GetRemindCategoryColorsResponse;
import com.everhomes.rest.remind.GetRemindCategoryCommand;
import com.everhomes.rest.remind.GetRemindCommand;
import com.everhomes.rest.remind.GetRemindSettingsResponse;
import com.everhomes.rest.remind.ListRemindCategoriesCommand;
import com.everhomes.rest.remind.ListRemindCategoriesResponse;
import com.everhomes.rest.remind.ListRemindResponse;
import com.everhomes.rest.remind.ListSelfRemindCommand;
import com.everhomes.rest.remind.ListShareRemindCommand;
import com.everhomes.rest.remind.ListSharingPersonsCommand;
import com.everhomes.rest.remind.ListSharingPersonsResponse;
import com.everhomes.rest.remind.RemindCategoryDTO;
import com.everhomes.rest.remind.RemindDTO;
import com.everhomes.rest.remind.SubscribeShareRemindCommand;
import com.everhomes.rest.remind.UnSubscribeShareRemindCommand;
import com.everhomes.rest.remind.UpdateRemindStatusCommand;
import com.everhomes.rest.remind.UpdateRemindStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "Remind Controller", site = "core")
@RestController
@RequestMapping("/remind")
public class RemindController extends ControllerBase {

    @Autowired
    private RemindService remindService;

    /**
     * <b>URL: /remind/getRemindCategoryColors</b>
     * <p>获取分类支持的所有颜色</p>
     */
    @RequestMapping("getRemindCategoryColors")
    @RestReturn(GetRemindCategoryColorsResponse.class)
    public RestResponse getRemindCategoryColors() {
        GetRemindCategoryColorsResponse colorsResponse = remindService.getRemindCategoryColors();
        RestResponse response = new RestResponse(colorsResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/getRemindSettings</b>
     * <p>获取提醒的所有类型</p>
     */
    @RequestMapping("getRemindSettings")
    @RestReturn(GetRemindSettingsResponse.class)
    public RestResponse getRemindSettings() {
        GetRemindSettingsResponse remindTypesResponse = remindService.listRemindSettings();
        RestResponse response = new RestResponse(remindTypesResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/evictRemindSettingsCache</b>
     * <p>手动清除缓存数据</p>
     */
    @RequestMapping("evictRemindSettingsCache")
    @RestReturn(String.class)
    public RestResponse evictRemindSettingsCache() {
        remindService.evictRemindSettingsCache();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/createOrUpdateRemindCategory</b>
     * <p>添加或编辑日程分类</p>
     *
     * @return 返回创建或更新成功的分类ID
     */
    @RequestMapping("createOrUpdateRemindCategory")
    @RestReturn(Long.class)
    public RestResponse createOrUpdateRemindCategory(CreateOrUpdateRemindCategoryCommand cmd) {
        Long id = remindService.createOrUpdateRemindCategory(cmd);
        RestResponse response = new RestResponse(id);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/batchSortRemindCategories</b>
     * <p>日程分类拖拽排序,移动端缓存排序多次结果，定期发送请求给服务端</p>
     */
    @RequestMapping("batchSortRemindCategories")
    @RestReturn(String.class)
    public RestResponse batchSortRemindCategories(BatchSortRemindCategoryCommand cmd) {
        remindService.batchSortRemindCategories(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/deleteRemindCategory</b>
     * <p>删除日程分类</p>
     */
    @RequestMapping("deleteRemindCategory")
    @RestReturn(String.class)
    public RestResponse deleteRemindCategory(DeleteRemindCategoryCommand cmd) {
        remindService.deleteRemindCategory(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/getRemindCategoryDetail</b>
     * <p>获取日程分类详情</p>
     */
    @RequestMapping("getRemindCategoryDetail")
    @RestReturn(RemindCategoryDTO.class)
    public RestResponse getRemindCategoryDetail(GetRemindCategoryCommand cmd) {
        RemindCategoryDTO remindCategoryDTO = remindService.getRemindCategoryDetail(cmd);
        RestResponse response = new RestResponse(remindCategoryDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/listRemindCategories</b>
     * <p>获取日程分类列表</p>
     */
    @RequestMapping("listRemindCategories")
    @RestReturn(ListRemindCategoriesResponse.class)
    public RestResponse listRemindCategories(ListRemindCategoriesCommand cmd) {
        ListRemindCategoriesResponse categories = remindService.listRemindCategories(cmd);
        RestResponse response = new RestResponse(categories);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/listSharingPersons</b>
     * <p>获取分享日程的同事列表</p>
     */
    @RequestMapping("listSharingPersons")
    @RestReturn(ListSharingPersonsResponse.class)
    public RestResponse listSharingPersons(ListSharingPersonsCommand cmd) {
        RestResponse response = new RestResponse(remindService.listSharingPersons(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/createOrUpdateRemind</b>
     * <p>添加或编辑日程</p>
     *
     * @return 返回创建或更新成功的日程ID
     */
    @RequestMapping("createOrUpdateRemind")
    @RestReturn(Long.class)
    public RestResponse createOrUpdateRemind(CreateOrUpdateRemindCommand cmd) {
        Long id = remindService.createOrUpdateRemind(cmd);
        RestResponse response = new RestResponse(id);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/deleteRemind</b>
     * <p>删除日程</p>
     */
    @RequestMapping("deleteRemind")
    @RestReturn(String.class)
    public RestResponse deleteRemind(DeleteRemindCommand cmd) {
        remindService.deleteRemind(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/getRemindDetail</b>
     * <p>获取日程详情</p>
     */
    @RequestMapping("getRemindDetail")
    @RestReturn(RemindDTO.class)
    public RestResponse getRemindDetail(GetRemindCommand cmd) {
        RestResponse response = new RestResponse(remindService.getRemindDetail(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/batchSortReminds</b>
     * <p>日程拖拽排序,移动端缓存排序结果，定期发送请求给服务端</p>
     */
    @RequestMapping("batchSortReminds")
    @RestReturn(String.class)
    public RestResponse batchSortReminds(BatchSortRemindCommand cmd) {
        remindService.batchSortReminds(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/listSelfReminds</b>
     * <p>查询我的日程列表</p>
     */
    @RequestMapping("listSelfReminds")
    @RestReturn(ListRemindResponse.class)
    public RestResponse listSelfReminds(ListSelfRemindCommand cmd) {
        RestResponse response = new RestResponse(remindService.listSelfReminds(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/listShareReminds</b>
     * <p>查询同事共享的日程列表</p>
     */
    @RequestMapping("listShareReminds")
    @RestReturn(ListRemindResponse.class)
    public RestResponse listShareReminds(ListShareRemindCommand cmd) {
        RestResponse response = new RestResponse(remindService.listShareReminds(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/updateRemindStatus</b>
     * <p>修改日程状态</p>
     */
    @RequestMapping("updateRemindStatus")
    @RestReturn(UpdateRemindStatusResponse.class)
    public RestResponse updateRemindStatus(UpdateRemindStatusCommand cmd) {
        RestResponse response = new RestResponse(remindService.updateRemindStatus(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/subscribeShareRemind</b>
     * <p>关注共享人的日程</p>
     */
    @RequestMapping("subscribeShareRemind")
    @RestReturn(String.class)
    public RestResponse subscribeShareRemind(SubscribeShareRemindCommand cmd) {
        remindService.subscribeShareRemind(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/unSubscribeShareRemind</b>
     * <p>取消关注共享人的日程</p>
     */
    @RequestMapping("unSubscribeShareRemind")
    @RestReturn(String.class)
    public RestResponse unSubscribeShareRemind(UnSubscribeShareRemindCommand cmd) {
        remindService.unSubscribeShareRemind(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /remind/getCurrentUserContactSimpleInfo</b>
     * <p>获取用户档案id</p>
     */
    @RequestMapping("getCurrentUserContactSimpleInfo")
    @RestReturn(GetCurrentUserDetailIdResponse.class)
    public RestResponse getCurrentUserContactSimpleInfo(GetCurrentUserDetailIdCommand cmd) {
        RestResponse response = new RestResponse(remindService.getCurrentUserContactSimpleInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
