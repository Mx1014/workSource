package com.everhomes.varField;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.dynamicExcel.DynamicImportResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
     * <b>URL: /varField/listSystemFields</b>
     * <p>获取系统模块字段</p>
     * @return {@link SystemFieldDTO}
     */
    @RequestMapping("listSystemFields")
    @RestReturn(value=SystemFieldDTO.class, collection = true)
    public RestResponse listSystemFields(@Valid ListSystemFieldCommand cmd) {
        List<SystemFieldDTO> fields = fieldService.listSystemFields(cmd);
        RestResponse res = new RestResponse(fields);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/listSystemFieldGroups</b>
     * <p>获取系统模块字段组</p>
     * @return {@link SystemFieldGroupDTO}
     */
    @RequestMapping("listSystemFieldGroups")
    @RestReturn(value=SystemFieldGroupDTO.class, collection = true)
    public RestResponse listSystemFieldGroups(@Valid ListSystemFieldGroupCommand cmd) {
        List<SystemFieldGroupDTO> groups = fieldService.listSystemFieldGroups(cmd);
        RestResponse res = new RestResponse(groups);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/listSystemFieldItems</b>
     * <p>获取系统模块字段选择项</p>
     * @return {@link SystemFieldItemDTO}
     */
    @RequestMapping("listSystemFieldItems")
    @RestReturn(value=SystemFieldItemDTO.class, collection = true)
    public RestResponse listSystemFieldItems(@Valid ListSystemFieldItemCommand cmd) {
        List<SystemFieldItemDTO> items = fieldService.listSystemFieldItems(cmd);
        RestResponse res = new RestResponse(items);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/updateFields</b>
     * <p>更新域空间or项目模块字段</p>
     * @return {@link String}
     */
    @RequestMapping("updateFields")
    @RestReturn(value=String.class)
    public RestResponse updateFields(@Valid UpdateFieldsCommand cmd) {
    	fieldService.updateFields(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/listFields(增加default)</b>
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
        fieldService.updateFieldGroups(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/listFieldGroups(增加default)</b>
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
        fieldService.updateFieldItems(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    /**
     * <b>URL: /varField/listFieldItems</b>
     * <p>获取域空间模块字段选择项(增加default)</p>
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
     * <b>URL: /varField/listScopeFieldItems</b>
     * <p>获取域空间模块字段选择项</p>
     * @return {@link FieldItemDTO}
     */
    @RequestMapping("listScopeFieldItems")
    @RestReturn(value=FieldItemDTO.class, collection = true)
    public RestResponse listScopeFieldItems(@Valid ListScopeFieldItemCommand cmd) {
        List<FieldItemDTO> items = fieldService.listScopeFieldItems(cmd);
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

    // 数据导入导出 重构
    /**
     * <p>模板的导出</p>
     * <b>URL: /varField/exportExcelTemplate</b>
     */
//    @RequireAuthentication(false)
    @RequestMapping("exportExcelTemplate")
    public void exportExcelTemplateRe(@Valid ListFieldGroupCommand cmd,HttpServletResponse response){
        fieldService.exportDynamicExcelTemplate(cmd,response);

    }

    /**
     * <p>excel数据的导出</p>
     * <b>URL: /varField/exportFieldsExcel</b>
     */
//    @RequireAuthentication(false)
    @RequestMapping("exportFieldsExcel")
    public void exportFieldsExcelRe(@Valid ExportFieldsExcelCommand cmd, HttpServletResponse response){
        fieldService.exportDynamicExcel(cmd,response);
    }

    /**
     * <p>excel数据导入</p>
     * <b>URL: /varField/importFieldsExcel</b>
     */
//    @RequireAuthentication(false)
    @RequestMapping("importFieldsExcel")
    @RestReturn(DynamicImportResponse.class)
    public RestResponse importFieldsExcelRe(@Valid ImportFieldExcelCommand cmd, MultipartFile file){
        DynamicImportResponse response = fieldService.importDynamicExcel(cmd,file);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <p>fieldFilter的保存</p>
     * <b>URL: /varField/saveFieldScopeFilter</b>
     */
    @RequestMapping("saveFieldScopeFilter")
    @RestReturn(String.class)
    public RestResponse saveFieldScopeFilter(SaveFieldScopeFilterCommand cmd){
        fieldService.saveFieldScopeFilter(cmd);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }


    /**
     * <p>fieldFilter的查询</p>
     * <b>URL: /varField/listFieldScopeFilter</b>
     */
    @RequestMapping("listFieldScopeFilter")
    @RestReturn(value = FieldDTO.class, collection = true)
    public RestResponse listFieldScopeFilter(ListFieldScopeFilterCommand cmd){
        List<FieldDTO> list = fieldService.listFieldScopeFilter(cmd);
        RestResponse restResponse = new RestResponse(list);
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

}
