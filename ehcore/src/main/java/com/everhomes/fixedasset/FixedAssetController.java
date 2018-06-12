package com.everhomes.fixedasset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.fixedasset.BatchDeleteFixedAssetCommand;
import com.everhomes.rest.fixedasset.BatchUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.CreateFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.CreateOrUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.DeleteFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.DeleteFixedAssetCommand;
import com.everhomes.rest.fixedasset.FixedAssetCategoryDTO;
import com.everhomes.rest.fixedasset.FixedAssetDTO;
import com.everhomes.rest.fixedasset.FixedAssetStatisticsDTO;
import com.everhomes.rest.fixedasset.GetFixedAssetCommand;
import com.everhomes.rest.fixedasset.GetFixedAssetDictionaryResponse;
import com.everhomes.rest.fixedasset.GetFixedAssetOperationLogsCommand;
import com.everhomes.rest.fixedasset.GetFixedAssetOperationLogsResponse;
import com.everhomes.rest.fixedasset.GetImportFixedAssetResultCommand;
import com.everhomes.rest.fixedasset.ImportFixedAssetsCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetResponse;
import com.everhomes.rest.fixedasset.UpdateFixedAssetCategoryCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestDoc(value = "FixedAsset controller", site = "core")
@RestController
@RequestMapping("/fixedAsset")
public class FixedAssetController extends ControllerBase {

    @Autowired
    private FixedAssetService fixedAssetService;

    /**
     * <p>获取资产字典列表(状态列表和来源等枚举信息)</p>
     * <b>URL: /fixedAsset/listFixedAssetDictionaries</b>
     */
    @RequestMapping("listFixedAssetDictionaries")
    @RestReturn(GetFixedAssetDictionaryResponse.class)
    public RestResponse listFixedAssetDictionaries() {
        RestResponse response = new RestResponse(this.fixedAssetService.listFixedAssetDictionaries());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>创建资产分类</p>
     * <b>URL: /fixedAsset/createFixedAssetCategory</b>
     *
     * @return 返回新创建的资产分类ID
     */
    @RequestMapping("createFixedAssetCategory")
    @RestReturn(Integer.class)
    public RestResponse createFixedAssetCategory(CreateFixedAssetCategoryCommand cmd) {
        Integer fixedAssetCategoryId = this.fixedAssetService.createFixedAssetCategory(cmd);
        RestResponse response = new RestResponse(fixedAssetCategoryId);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>更新资产分类名称</p>
     * <b>URL: /fixedAsset/updateFixedAssetCategory</b>
     */
    @RequestMapping("updateFixedAssetCategory")
    @RestReturn(String.class)
    public RestResponse updateFixedAssetCategory(UpdateFixedAssetCategoryCommand cmd) {
        this.fixedAssetService.updateFixedAssetCategory(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>删除资产分类名称</p>
     * <b>URL: /fixedAsset/deleteFixedAssetCategory</b>
     */
    @RequestMapping("deleteFixedAssetCategory")
    @RestReturn(String.class)
    public RestResponse deleteFixedAssetCategory(DeleteFixedAssetCategoryCommand cmd) {
        this.fixedAssetService.deleteFixedAssetCategory(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询资产分类名称列表</p>
     * <b>URL: /fixedAsset/listFixedAssetCategories</b>
     */
    @RequestMapping("listFixedAssetCategories")
    @RestReturn(value = FixedAssetCategoryDTO.class, collection = true)
    public RestResponse listFixedAssetCategories(ListFixedAssetCategoryCommand cmd) {
        List<FixedAssetCategoryDTO> result = this.fixedAssetService.findFixedAssetCategories(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>创建或更新资产</p>
     * <b>URL: /fixedAsset/createOrUpdateFixedAsset</b>
     * @return 返回资产ID
     */
    @RequestMapping("createOrUpdateFixedAsset")
    @RestReturn(Long.class)
    public RestResponse createOrUpdateFixedAsset(CreateOrUpdateFixedAssetCommand cmd) {
        Long id = this.fixedAssetService.createOrUpdateFixedAsset(cmd);
        RestResponse response = new RestResponse(id);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取资产详情</p>
     * <b>URL: /fixedAsset/getFixedAssetDetail</b>
     */
    @RequestMapping("getFixedAssetDetail")
    @RestReturn(FixedAssetDTO.class)
    public RestResponse getFixedAssetDetail(GetFixedAssetCommand cmd) {
        RestResponse response = new RestResponse(this.fixedAssetService.getFixedAssetDetail(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>删除资产</p>
     * <b>URL: /fixedAsset/deleteFixedAsset</b>
     */
    @RequestMapping("deleteFixedAsset")
    @RestReturn(String.class)
    public RestResponse deleteFixedAsset(DeleteFixedAssetCommand cmd) {
        this.fixedAssetService.deleteFixedAsset(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>批量编辑资产状态</p>
     * <b>URL: /fixedAsset/batchUpdateFixedAsset</b>
     */
    @RequestMapping("batchUpdateFixedAsset")
    @RestReturn(Integer.class)
    public RestResponse batchUpdateFixedAsset(BatchUpdateFixedAssetCommand cmd) {
        Integer successCount = this.fixedAssetService.batchUpdateFixedAsset(cmd);
        RestResponse response = new RestResponse(successCount);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>批量删除资产</p>
     * <b>URL: /fixedAsset/batchDeleteFixedAsset</b>
     *
     * @return 返回成功删除的记录数
     */
    @RequestMapping("batchDeleteFixedAsset")
    @RestReturn(Integer.class)
    public RestResponse batchDeleteFixedAsset(BatchDeleteFixedAssetCommand cmd) {
        Integer successCount = this.fixedAssetService.batchDeleteFixedAsset(cmd);
        RestResponse response = new RestResponse(successCount);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询资产列表</p>
     * <b>URL: /fixedAsset/listFixedAssets</b>
     */
    @RequestMapping("listFixedAssets")
    @RestReturn(ListFixedAssetResponse.class)
    public RestResponse listFixedAssets(ListFixedAssetCommand cmd) {
        RestResponse response = new RestResponse(this.fixedAssetService.listFixedAssets(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询资产列表合计信息</p>
     * <b>URL: /fixedAsset/getFixedAssetsStatistic</b>
     */
    @RequestMapping("getFixedAssetsStatistic")
    @RestReturn(FixedAssetStatisticsDTO.class)
    public RestResponse getFixedAssetsStatistic(ListFixedAssetCommand cmd) {
        RestResponse response = new RestResponse(this.fixedAssetService.getFixedAssetsStatistic(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询资产编辑历史记录</p>
     * <b>URL: /fixedAsset/getFixedAssetOperationLogs</b>
     */
    @RequestMapping("getFixedAssetOperationLogs")
    @RestReturn(value = GetFixedAssetOperationLogsResponse.class, collection = true)
    public RestResponse getFixedAssetOperationLogs(GetFixedAssetOperationLogsCommand cmd) {
        RestResponse response = new RestResponse(this.fixedAssetService.findFixedAssetOperationLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>资产导入</p>
     * <b>URL: /fixedAsset/importFixedAssets</b>
     */
    @RequestMapping("importFixedAssets")
    @RestReturn(ImportFileTaskDTO.class)
    public RestResponse importFixedAssets(@RequestParam(value = "attachment") MultipartFile[] files, ImportFixedAssetsCommand cmd) {
        ImportFileTaskDTO dto = this.fixedAssetService.importFixedAssets(cmd, files[0]);
        return new RestResponse(dto);
    }

    /**
     * <p>导出资产列表</p>
     * <b>URL: /fixedAsset/exportFixedAssets</b>
     */
    @RequestMapping("exportFixedAssets")
    public RestResponse exportFixedAssets(ListFixedAssetCommand cmd, HttpServletResponse response) {
        this.fixedAssetService.exportFixedAssets(cmd, response);
        return new RestResponse();
    }

    /**
     * <b>URL: /fixedAsset/getImportFixedAssetsResult</b>
     * <p>查询资产列表导入结果</p>
     */
    @RequestMapping("getImportFixedAssetsResult")
    @RestReturn(value = ImportFileResponse.class)
    public RestResponse getImportFixedAssetsResult(GetImportFixedAssetResultCommand cmd) {
        RestResponse response = new RestResponse(fixedAssetService.getImportFixedAssetsResult(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fixedAsset/exportImportFileFailResults</b>
     * <p>下载资产列表导入失败结果</p>
     */
    @RequestMapping("exportImportFileFailResults")
    @RestReturn(value = String.class)
    public RestResponse exportImportFileFailResults(GetImportFixedAssetResultCommand cmd, HttpServletResponse httpResponse) {
        fixedAssetService.exportImportFileFailResults(cmd, httpResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
