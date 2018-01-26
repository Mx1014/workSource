//@formatter:off
package com.everhomes.contractDebug;

import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.ImportFieldsExcelResponse;
import com.everhomes.rest.field.ExportFieldsExcelCommand;
import com.everhomes.rest.varField.ImportFieldExcelCommand;
import com.everhomes.rest.varField.ListFieldGroupCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Wentian Wang on 2018/1/11.
 */
//@RestController
//@RequestMapping("/varField")
public class DebugController {
//    @Autowired
//    private DebugFieldService fieldService;
    // 数据导入导出
    /**
     * <p>模板的导出</p>
     * <b>URL: /varField/moduleOut</b>
//     */
//    @RequireAuthentication(false)
//    @RequestMapping("moduleOut")
//    public void exportExcelTemplate(@Valid ListFieldGroupCommand cmd, HttpServletResponse response){
//        fieldService.exportExcelTemplate(cmd,response);
//
//    }
//
//    /**
//     * <p>excel数据的导出</p>
//     * <b>URL: /varField/dataOut</b>
//     */
//    @RequireAuthentication(false)
//    @RequestMapping("dataOut")
//    public void exportFieldsExcel(@Valid ExportFieldsExcelCommand cmd, HttpServletResponse response){
//        fieldService.exportFieldsExcel(cmd,response);
//    }
//
//    /**
//     * <p>excel数据导入</p>
//     * <b>URL: /varField/dataIn</b>
//     */
//    @RequireAuthentication(false)
//    @RequestMapping("dataIn")
//    @RestReturn(ImportFieldsExcelResponse.class)
//    public RestResponse importFieldsExcel(@Valid ImportFieldExcelCommand cmd, MultipartFile file){
//
//        ImportFieldsExcelResponse response = fieldService.importFieldsExcel(cmd,file);
//
//        RestResponse restResponse = new RestResponse(response);
//        restResponse.setErrorCode(200);
//        restResponse.setErrorDescription("OK");
//        return restResponse;
//    }
}
