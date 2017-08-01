package com.everhomes.field;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.field.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by ying.xiong on 2017/7/31.
 */

@RestController
@RequestMapping("/field")
public class FieldController extends ControllerBase {


    /**
     * <b>URL: /field/updateFields</b>
     * <p>更新域空间模块字段</p>
     * @return {@link String}
     */
    @RequestMapping("updateFields")
    @RestReturn(value=String.class)
    public RestResponse updateFields(@Valid UpdateFieldsCommand cmd) {
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /field/listFields</b>
     * <p>获取域空间模块字段</p>
     * @return {@link FieldDTO}
     */
    @RequestMapping("listFields")
    @RestReturn(value=FieldDTO.class, collection = true)
    public RestResponse listFields(@Valid ListFieldCommand cmd) {
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /field/updateFieldGroups</b>
     * <p>更新域空间模块字段组</p>
     * @return {@link String}
     */
    @RequestMapping("updateFieldGroups")
    @RestReturn(value=String.class)
    public RestResponse updateFieldGroups(@Valid UpdateFieldGroupsCommand cmd) {
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /field/listFieldGroups</b>
     * <p>获取域空间模块字段组</p>
     * @return {@link FieldGroupDTO}
     */
    @RequestMapping("listFieldGroups")
    @RestReturn(value=FieldGroupDTO.class, collection = true)
    public RestResponse listFieldGroups(@Valid ListFieldGroupCommand cmd) {
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /field/updateFieldItems</b>
     * <p>更新域空间模块字段选择项</p>
     * @return {@link String}
     */
    @RequestMapping("updateFieldItems")
    @RestReturn(value=String.class)
    public RestResponse updateFieldItems(@Valid UpdateFieldItemsCommand cmd) {
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /field/listFieldItems</b>
     * <p>获取域空间模块字段选择项</p>
     * @return {@link FieldItemDTO}
     */
    @RequestMapping("listFieldItems")
    @RestReturn(value=FieldItemDTO.class, collection = true)
    public RestResponse listFieldItems(@Valid ListFieldItemCommand cmd) {
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
}
