// @formatter:off
package com.everhomes.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.AclProvider;
import com.everhomes.activity.ActivityCheckinCommand;
import com.everhomes.activity.ActivityDTO;
import com.everhomes.activity.ActivityListCommand;
import com.everhomes.activity.ActivityListResponse;
import com.everhomes.activity.ActivitySignupCommand;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryDTO;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;

@RestController
@RequestMapping("/activity")
public class ActivityController extends ControllerBase {

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private AclProvider aclProvider;
    
//    @RequestMapping("post")
//    @RestReturn(value=ActivityDTO.class)
//    public RestResponse signup(@Valid ActivityPostCommand cmd) {
//        ActivityDTO result = activityService.createPost(cmd);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        response.setResponseObject(result);
//        return response;
//    }
    /**
     * 报名
     * @return {@link ActivityDTO}
     */
    @RequestMapping("signup")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse signup(@Valid ActivitySignupCommand cmd) {
        ActivityDTO result = activityService.signup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 取消报名
     * @return {@link ActivityDTO}
     */
    @RequestMapping("cancelSignup")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse cacnelSignup(@Valid ActivityCancelSignupCommand cmd) {
        ActivityDTO result = activityService.cancelSignup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 签到
     * @return {@link ActivityDTO}
     */
    @RequestMapping("checkin")
    @RestReturn(value=ActivityDTO.class)
    public RestResponse checkin(@Valid ActivityCheckinCommand cmd) {
        ActivityDTO result = activityService.checkin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(result);
        return response;
    }
    
    /**
     * 查询活动详情
     * @return {@link ActivityListResponse}
     */
    @RequestMapping("findActivityDetails")
    @RestReturn(value=ActivityListResponse.class)
    public RestResponse list(@Valid ActivityListCommand cmd) {
        ActivityListResponse activities = activityService.findActivityDetails(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        response.setResponseObject(activities);
        return response;
    }
    
    /**
     * 确认
     * @return {@link ActivityDTO}
     */
    @RequestMapping("confirm")
    @RestReturn(ActivityDTO.class)
    public RestResponse confirm(@Valid ActivityConfirmCommand cmd){
        ActivityDTO confirm = activityService.confirm(cmd);
        return new RestResponse(confirm);
    }
    
    /**
     * 拒绝
     * @return {@link String}
     */
    @RequestMapping("reject")
    @RestReturn(String.class)
    public RestResponse reject(@Valid ActivityRejectCommand cmd){
        activityService.rejectPost(cmd);
        return new RestResponse("OK");
    }
    
    /**
     * 查询活动category
     * @return {@link ListActivityCategories}
     */
    @RequestMapping("listActivityCategories")
    @RestReturn(ListActivityCategories.class)
    public RestResponse listActivityCategories(){
        List<Category> result = activityService.listActivityCategories();
        if(CollectionUtils.isEmpty(result))
            return new RestResponse(new ListActivityCategories());
        ListActivityCategories categories=new ListActivityCategories();
        categories.setActivityCategories(result.stream().map(r->ConvertHelper.convert(r, CategoryDTO.class)).collect(Collectors.toList()));
        return new RestResponse(categories);
    }
}
