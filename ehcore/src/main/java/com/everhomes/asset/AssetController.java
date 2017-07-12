package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.pmkexing.PmKeXingBillService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestDoc(value = "Asset Controller", site = "core")
@RestController
@RequestMapping("/asset")
public class AssetController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);
    @Autowired
    private AssetService assetService;

//    根据用户查关联模板字段列表（必填字段最前，关联表中最新version的字段按default_order和id排序）
    /**
     * <b>URL: /asset/listAssetBillTemplate</b>
     * <p>查用户的资产账单模板字段列表</p>
     * @return 资产账单模板字段列表
     */
    @RequestMapping("listAssetBillTemplate")
    @RestReturn(value=AssetBillTemplateFieldDTO.class, collection = true)
    public RestResponse listAssetBillTemplate(@Valid ListAssetBillTemplateCommand cmd) {
        List<AssetBillTemplateFieldDTO> dtos = this.assetService.listAssetBillTemplate(cmd);

        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取公司物业账单统计信息</p>
     * <b>URL: /asset/getAssetBillStat</b>
     */
    @RequestMapping("getAssetBillStat")
    @RestReturn(AssetBillStatDTO.class)
    public RestResponse getAssetBillStat(GetAssetBillStatCommand cmd) {
        RestResponse response = new RestResponse(assetService.getAssetBillStat(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //    搜索账单列表 list：owner_type owner_id address_id account_period status 企业名（园区场景）或家庭任一成员姓名（小区场景） 都关联到门牌 这里用search取得门牌addressId
    /**
     * <b>URL: /asset/listSimpleAssetBills</b>
     * <p>搜索账单列表</p>
     */
    @RequestMapping("listSimpleAssetBills")
    @RestReturn(value=ListSimpleAssetBillsResponse.class)
    public RestResponse listSimpleAssetBills(@Valid ListSimpleAssetBillsCommand cmd) {
        ListSimpleAssetBillsResponse resp = this.assetService.listSimpleAssetBills(cmd);

        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //            导出账单
    /**
     * <b>URL: /asset/exportAssetBills</b>
     * <p>导出账单</p>
     */
    @RequestMapping("exportAssetBills")
    public HttpServletResponse exportAssetBills(@Valid ListSimpleAssetBillsCommand cmd,HttpServletResponse response) {

        HttpServletResponse commandResponse = assetService.exportAssetBills(cmd, response);

        return commandResponse;
    }

    //    批量上传账单（与模板字段按字段展示名对应）
    /**
     * <b>URL: /asset/importAssetBills</b>
     * <p>批量上传账单（与用户当前设置的模板字段按字段展示名对应）</p>
     */
    @RequestMapping("importAssetBills")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importAssetBills(@Valid ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if(null == files || null == files[0]){
            LOGGER.error("files is null。userId="+userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        ImportDataResponse importDataResponse = this.assetService.importAssetBills(cmd, files[0], userId);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    新增账单
    /**
	 * <b>URL: /asset/creatAssetBill</b>
	 * <p>新增账单</p>
	 */
	@RequestMapping("creatAssetBill")
	@RestReturn(value = AssetBillTemplateValueDTO.class)
	public RestResponse creatAssetBill(@Valid CreatAssetBillCommand cmd) {

        AssetBillTemplateValueDTO bill = assetService.creatAssetBill(cmd);

		RestResponse response = new RestResponse(bill);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
//            查看账单
    /**
     * <b>URL: /asset/findAssetBill</b>
     * <p>查看账单</p>
     */
    @RequestMapping("findAssetBill")
    @RestReturn(value = AssetBillTemplateValueDTO.class)
    public RestResponse findAssetBill(@Valid FindAssetBillCommand cmd) {

        AssetBillTemplateValueDTO bill = assetService.findAssetBill(cmd);

        RestResponse response = new RestResponse(bill);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//    编辑账单
    /**
     * <b>URL: /asset/updateAssetBill</b>
     * <p>编辑账单</p>
     */
    @RequestMapping("updateAssetBill")
    @RestReturn(value = AssetBillTemplateValueDTO.class)
    public RestResponse updateAssetBill(@Valid UpdateAssetBillCommand cmd) {

        AssetBillTemplateValueDTO bill = assetService.updateAssetBill(cmd);

        RestResponse response = new RestResponse(bill);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//    一键催缴
    /**
     * <b>URL: /asset/notifyUnpaidBillsContact</b>
     * <p>一键催缴</p>
     */
    @RequestMapping("notifyUnpaidBillsContact")
    @RestReturn(value = String.class)
    public RestResponse notifyUnpaidBillsContact(@Valid NotifyUnpaidBillsContactCommand cmd) {

        assetService.notifyUnpaidBillsContact(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//            批量设置已缴
//    批量设置待缴
    /**
     * <b>URL: /asset/setBillsPaid</b>
     * <p>批量设置已缴</p>
     */
    @RequestMapping("setBillsPaid")
    @RestReturn(value = String.class)
    public RestResponse setBillsPaid(@Valid BillIdListCommand cmd) {

        assetService.setBillsStatus(cmd, AssetBillStatus.PAID);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/setBillsUnpaid</b>
     * <p>批量设置待缴</p>
     */
    @RequestMapping("setBillsUnpaid")
    @RestReturn(value = String.class)
    public RestResponse setBillsUnpaid(@Valid BillIdListCommand cmd) {

        assetService.setBillsStatus(cmd, AssetBillStatus.UNPAID);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//            删除账单
    /**
     * <b>URL: /asset/deleteBill</b>
     * <p>删除账单</p>
     */
    @RequestMapping("deleteBill")
    @RestReturn(value = String.class)
    public RestResponse deleteBill(@Valid DeleteBillCommand cmd) {

        assetService.deleteBill(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//    设置用户模板显示顺序
    //    设置用户模板
    /**
     * <b>URL: /asset/updateAssetBillTemplate</b>
     * <p>设置用户的资产账单模板</p>
     * @return 资产账单模板字段列表
     */
    @RequestMapping("updateAssetBillTemplate")
    @RestReturn(value=AssetBillTemplateFieldDTO.class, collection = true)
    public RestResponse updateAssetBillTemplate(@Valid UpdateAssetBillTemplateCommand cmd) {
        List<AssetBillTemplateFieldDTO> dtos = this.assetService.updateAssetBillTemplate(cmd);

        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/checkTokenRegister</b>
     * <p>检查手机号是否是注册用户</p>
     * @return
     */
    @RequestMapping("checkTokenRegister")
    @RestReturn(value=Boolean.class)
    public RestResponse checkTokenRegister(@Valid CheckTokenRegisterCommand cmd) {

        RestResponse response = new RestResponse(this.assetService.checkTokenRegister(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /asset/notifyTimes</b>
     * <p>本月已催缴次数</p>
     * @return
     */
    @RequestMapping("notifyTimes")
    @RestReturn(value=NotifyTimesResponse.class)
    public RestResponse notifyTimes(@Valid ImportOwnerCommand cmd) {

        RestResponse response = new RestResponse(this.assetService.notifyTimes(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取用户为管理员的公司列表</p>
     * <b>URL: /asset/listOrganizationsByPmAdmin</b>
     */
    @RequestMapping("listOrganizationsByPmAdmin")
    @RestReturn(value = ListOrganizationsByPmAdminDTO.class, collection = true)
    public RestResponse listOrganizationsByPmAdmin() {

        RestResponse response = new RestResponse(assetService.listOrganizationsByPmAdmin());
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
//    脚本配置设置用户模板
}
