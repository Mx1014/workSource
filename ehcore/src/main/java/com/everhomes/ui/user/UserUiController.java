// @formatter:off
package com.everhomes.ui.user;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.ui.user.GetUserOpPromotionCommand;
import com.everhomes.rest.ui.user.GetUserRelatedAddressCommand;
import com.everhomes.rest.ui.user.GetUserRelatedAddressResponse;
import com.everhomes.rest.ui.user.ListContactBySceneRespose;
import com.everhomes.rest.ui.user.ListContactsBySceneCommand;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.user.ListUserOpPromotionsRespose;
import com.everhomes.rest.user.OpPromotionDTO;
import com.everhomes.rest.user.SyncActivityCommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中，这类API属于比较高层的API，专门服务于界面</li>
 * </ul>
 */
@RestDoc(value="UserUi controller", site="userui")
@RestController
@RequestMapping("/ui/user")
public class UserUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    /**
     * <b>URL: /ui/user/listUserRelatedScenes</b>
     * <p>列出用户当前域空间下的相关场景。</p>
     * <p>必须在请求的Header中提供域空间。</p>
     */
    @RequestMapping("listUserRelatedScenes")
    @RestReturn(value=SceneDTO.class, collection=true)
    public RestResponse listUserRelatedScenes() {
        List<SceneDTO> sceneDtoList = null;
        
        RestResponse response = new RestResponse(sceneDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 

    /**
     * <p>根据指定的场景查询通讯录列表。</p>
     * <p>对于左邻版，普通用户查询的是邻居好友列表；对于园区版，查询的是公司通讯录列表；而对于物业管理员场景，查询的是同事列表。</p> 
     * <p>url:/ui/user/listContactsByScene</p>
     */
    @RequestMapping(value = "listContactsByScene")
    @RestReturn(value = ListContactBySceneRespose.class)
    public RestResponse listContactsByScene(@Valid ListContactsBySceneCommand cmd) throws Exception {
//        Tuple<Long, List<ContactDTO>> result = userActivityService.listUserContacts(cmd.getAnchor());
//        ListContactRespose rsp = new ListContactRespose(result.second(), result.first());
//        return new RestResponse(rsp);
        return null;
    }
    
    /**
     * <b>URL: /ui/user/getUserRelatedAddresses</b>
     * <p>根据用户域空间场景获取用户相关的地址列表</p>
     */
    @RequestMapping("getUserRelatedAddresses")
    @RestReturn(value=GetUserRelatedAddressResponse.class)
    public RestResponse getUserRelatedAddresses(GetUserRelatedAddressCommand cmd) {
        //List<UserServiceAddressDTO> result = this.userActivityService.getUserRelateServiceAddress();
        GetUserRelatedAddressResponse cmdResponse = null;
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/user/getUserOpPromotionsByScene</b>
     * <p>获取用户相关的运营活动，打开APP时调用一次该接口，里面包含用户当前可享受的运营活动。
     *    对于一次性的活动，如果还在有效期内，用户不管是否已经查看，此接口都有数据返回，客户端需要记录是否看过来决定是否还显示该活动。</p>
     */
    @RequestMapping(value = "getUserOpPromotionsByScene")
    @RestReturn(ListUserOpPromotionsRespose.class)
    public RestResponse getUserOpPromotionsByScene(GetUserOpPromotionCommand cmd) throws Exception {
        ListUserOpPromotionsRespose cmdResponse = null;
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
