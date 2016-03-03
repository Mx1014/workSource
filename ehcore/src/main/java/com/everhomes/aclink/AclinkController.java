package com.everhomes.aclink;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="Aclink controller", site="core")
@RestController
@RequestMapping("/aclink")
public class AclinkController extends ControllerBase {
    @Autowired
    private DoorAccessService doorAccessService;
    
    @Autowired
    private AesServerKeyProvider aesServerKeyProvider;
    
    /**
     * <b>URL: /aclink/activing</b>
     * <p>激活门禁</p>
     * @return 激活门禁消息
     */
    @RequestMapping("activing")
    @RestReturn(value=DoorMessage.class)
    public RestResponse create(@Valid DoorAccessActivingCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.activatingDoorAccess(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/active</b>
     * <p>激活门禁</p>
     * @return 激活门禁消息
     */
    @RequestMapping("active")
    @RestReturn(value=QueryDoorMessageResponse.class)
    public RestResponse active(@Valid DoorAccessActivedCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.activateDoorAccess(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/queryMessages</b>
     * <p>激活门禁</p>
     * @return 激活门禁消息
     */
    @RequestMapping("queryMessages")
    @RestReturn(value=QueryDoorMessageResponse.class)
    public RestResponse queryMessages(@Valid QueryDoorMessageCommand cmd) {
        RestResponse response = new RestResponse(doorAccessService.queryDoorMessageByDoorId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * <b>URL: /aclink/listAesUserKey</b>
     * <p>获取用户授权信息</p>
     * @return
     */
    @RequestMapping("listAesUserKey")
    @RestReturn(value=ListAesUserKeyByUserResponse.class)
    public RestResponse listAesUserKey() {
        ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
        List<AesUserKey> aesUserKeys = doorAccessService.listAesUserKeyByUser();
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(AesUserKey key : aesUserKeys) {
            AesUserKeyDTO dto = ConvertHelper.convert(key, AesUserKeyDTO.class);
            dtos.add(dto);
        }
        
        resp.setAesUserKeys(dtos);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
}
