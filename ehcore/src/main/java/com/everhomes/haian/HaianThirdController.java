// @formatter:off
package com.everhomes.haian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.haian.EncryptForInsurobotCommand;
import com.everhomes.rest.haian.EncryptForInsurobotDTO;

@RestDoc(value="Haian controller", site="haian")
@RestController
@RequestMapping("/haian")
public class HaianThirdController extends ControllerBase {
    
    @Autowired
    private HaianThirdService haianThirdService;
    
    /**
     * <b>URL: /haian/encryptForInsurobot</b>
     * <p>海岸 险萝卜 服务 加密</p>
     */
    @RequestMapping("encryptForInsurobot")
    @RestReturn(value=EncryptForInsurobotDTO.class)
    public RestResponse encryptForInsurobot(EncryptForInsurobotCommand cmd) {
        
    	EncryptForInsurobotDTO parkingLotList = haianThirdService.encryptForInsurobot(cmd);
        RestResponse response = new RestResponse(parkingLotList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
