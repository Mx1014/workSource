package com.everhomes.varField;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by ying.xiong on 2017/7/31.
 */

@RestController
@RequestMapping("/varField")
public class FieldController extends ControllerBase {

    @Autowired
    private FieldService fieldService;

    /**
     * <b>URL: /varField/updateFields</b>
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
     * <b>URL: /varField/listFields</b>
     * <p>获取域空间模块字段</p>
     * @return {@link FieldDTO}
     */
    @RequestMapping("listFields")
    @RestReturn(value=FieldDTO.class, collection = true)
    public RestResponse listFields(@Valid ListFieldCommand cmd) {
        List<FieldDTO> fields = fieldService.listFields(cmd);
        RestResponse res = new RestResponse(fields);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/updateFieldGroups</b>
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
     * <b>URL: /varField/listFieldGroups</b>
     * <p>获取域空间模块字段组</p>
     * @return {@link FieldGroupDTO}
     */
    @RequestMapping("listFieldGroups")
    @RestReturn(value=FieldGroupDTO.class, collection = true)
    public RestResponse listFieldGroups(@Valid ListFieldGroupCommand cmd) {
        List<FieldGroupDTO> groups = fieldService.listFieldGroups(cmd);
        RestResponse res = new RestResponse(groups);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/updateFieldItems</b>
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
     * <b>URL: /varField/listFieldItems</b>
     * <p>获取域空间模块字段选择项</p>
     * @return {@link FieldItemDTO}
     */
    @RequestMapping("listFieldItems")
    @RestReturn(value=FieldItemDTO.class, collection = true)
    public RestResponse listFieldItems(@Valid ListFieldItemCommand cmd) {
        List<FieldItemDTO> items = fieldService.listFieldItems(cmd);
        RestResponse res = new RestResponse(items);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/listFieldStatistics</b>
     * <p>列出特定字段统计信息</p>
     */
    @RequestMapping("listFieldStatistics")
    @RestReturn(value = FieldStatisticDTO.class)
    public RestResponse listFieldStatistics(@Valid ListFieldStatisticsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>模板导出</p>
     * <b>URL: /varField/exportExcelTemplate</b>
     */
    @RequestMapping("exportExcelTemplate")
    public void exportExcelTemplate(@Valid ListFieldGroupCommand cmd,HttpServletResponse response){
        fieldService.exportExcelTemplate(cmd,response);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(200);
    }

    /**
     * <p>excel数据导出</p>
     * <b>URL: /varField/exportFieldsExcel</b>
     */
    @RequestMapping("exportFieldsExcel")
    public void exportFieldsExcel(@Valid ExportFieldsExcelCommand cmd, HttpServletResponse response){
        fieldService.exportFieldsExcel(cmd,response);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(200);
    }
}
