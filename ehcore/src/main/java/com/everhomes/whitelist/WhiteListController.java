// @formatter:off
package com.everhomes.whitelist;


import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.whitelist.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestDoc(value="WhiteList controller", site="whiteList")
@RestController
@RequestMapping("/whitelist")
public class WhiteListController extends ControllerBase{

    /**
     * <b>URL: /whitelist/createWhiteList<b/>
     * <p>新增白名单中的手机号码</p>
     *
     */
    @RequestMapping("createWhiteList")
    @RestReturn(value = String.class)
    public RestResponse createWhiteList(CreateWhiteListCommand cmd) {

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /whitelist/deleteWhiteList</b>
     * <p>删除白名单中的手机号码</p>
     *
     *
     */
    @RequestMapping("deleteWhiteList")
    @RestReturn(value = String.class)
    public RestResponse deleteWhiteList(DeleteWhiteListCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /whitelist/updateWhiteList</b>
     * <p>修改白名单中的手机号码</p>
     */
    @RequestMapping("updateWhiteList")
    @RestReturn(value = WhiteListDTO.class)
    public RestResponse updateWhiteList(UpdateWhiteListCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /whitelist/getWhiteList</b>
     * <p>获取单条白名单中的手机号码</p>
     */
    @RequestMapping("getWhiteList")
    @RestReturn(value = WhiteListDTO.class)
    public RestResponse getWhiteList(GetWhiteListCommand cmd) {
        WhiteListDTO whiteListDTO = new WhiteListDTO();
        whiteListDTO.setId(1L);
        whiteListDTO.setNamespaceId(1);
        whiteListDTO.setPhoneNumber("123456789");

        RestResponse response = new RestResponse(whiteListDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /whitelist/listWhiteList</b>
     * <p>获取白名单列表</p>
     */
    @RequestMapping("listWhiteList")
    @RestReturn(value = ListWhiteListResponse.class)
    public RestResponse listWhiteList(ListWhiteListCommand cmd) {
        List<WhiteListDTO> list = new ArrayList<>();
        WhiteListDTO whiteListDTO = new WhiteListDTO();
        whiteListDTO.setId(1L);
        whiteListDTO.setNamespaceId(1);
        whiteListDTO.setPhoneNumber("123456789");
        WhiteListDTO whiteListDTO2 = new WhiteListDTO();
        whiteListDTO2.setId(1L);
        whiteListDTO2.setNamespaceId(1);
        whiteListDTO2.setPhoneNumber("123456789");
        list.add(whiteListDTO);
        list.add(whiteListDTO2);

        ListWhiteListResponse listWhiteListResponse = new ListWhiteListResponse();
        listWhiteListResponse.setNextAnchor(null);
        listWhiteListResponse.setWhiteListDTOList(list);

        RestResponse response = new RestResponse(listWhiteListResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
