// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.sensitiveWord.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestDoc(value="SensitiveFilterRecord controller", site="SensitiveFilterRecord")
@RestController
@RequestMapping("/sensitiveFilterRecord")
public class SensitiveFilterRecordController extends ControllerBase{

    @Autowired
    private SensitiveFilterRecordService sensitiveFilterRecordService;

    /**
     * <b>URL: /sensitiveFilterRecord/getSensitiveFilterRecord</b>
     * <p>获取被过滤的日志内容</p>
     *
     */
    @RequestMapping("getSensitiveFilterRecord")
    @RestReturn(value = SensitiveFilterRecordAdminDTO.class)
    public RestResponse getSensitiveFilterRecord(GetSensitiveFilterRecordAdminCommand cmd) {
        SensitiveFilterRecordAdminDTO sensitiveFilterRecordAdminDTO = this.sensitiveFilterRecordService.getSensitiveFilterRecord(cmd);
        RestResponse response = new RestResponse(sensitiveFilterRecordAdminDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /sensitiveFilterRecord/listSensitiveFilterRecord</b>
     * <p>获取被过滤的日志内容列表</p>
     *
     */
    @RequestMapping("listSensitiveFilterRecord")
    @RestReturn(value = ListSensitiveFilterRecordAdminResponse.class)
    public RestResponse listSensitiveFilterRecord(ListSensitiveFilterRecordAdminCommand cmd) {
        ListSensitiveFilterRecordAdminResponse listSensitiveFilterRecordAdminResponse = this.sensitiveFilterRecordService.listSensitiveRecord(cmd);
        RestResponse response = new RestResponse(listSensitiveFilterRecordAdminResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /sensitiveFilterRecord/getSensitiveWordUrl</b>
     * <p>获取敏感词文本URL路径</p>
     *
     */
    @RequestMapping("getSensitiveWordUrl")
    @RestReturn(value = GetSensitiveWordUrlAdminResponse.class)
    public RestResponse getSensitiveWordUrl() {
        GetSensitiveWordUrlAdminResponse getSensitiveWordUrlAdminResponse = this.sensitiveFilterRecordService.getSensitiveWordUrl();
        RestResponse response = new RestResponse(getSensitiveWordUrlAdminResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
