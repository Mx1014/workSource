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

    // wentian's controlls for payment module

    // this is for 展示所有收费项目   1
    /**
     * <p>获取客户可见的所有的收费项目列表</p>
     * <b>URL: /asset/listChargingItems</b>
     */
    @RequestMapping("listChargingItems")
    @RestReturn(value = ListChargingItemsDTO.class, collection = true)
    public RestResponse listChargingItems(OwnerIdentityCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示一个收费项目的客户可见的所有标准列表         1
    /**
     * <p>展示一个收费项目的客户可见的所有标准列表</p>
     * <b>URL: /asset/listChargingStandards</b>
     */
    @RequestMapping("listChargingStandards")
    @RestReturn(value = ListChargingStandardsDTO.class, collection = true)
    public RestResponse listChargingStandards(ListChargingStandardsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 增加一个收费标准         1
    /**
     * <p>增加一个收费标准</p>
     * <b>URL: /asset/createChargingStandard</b>
     */
    @RequestMapping("createChargingStandard")
    @RestReturn(value = String.class)
    public RestResponse createChargingStandard(CreateChargingStandardCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 修改一个收费标准         1
    /**
     * <p>修改一个收费标准</p>
     * <b>URL: /asset/modifyChargingStandard</b>
     */
    @RequestMapping("modifyChargingStandard")
    @RestReturn(value = String.class)
    public RestResponse modifyChargingStandard(ModifyChargingStandardCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 查看一个收费标准详情       1
    /**
     * <p>查看一个收费标准详情</p>
     * <b>URL: /asset/getChargingStandardDetail</b>
     */
    @RequestMapping("getChargingStandardDetail")
    @RestReturn(value = GetChargingStandardDTO.class)
    public RestResponse getChargingStandardDetail(GetChargingStandardCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 删除一个收费标准         1
    /**
     * <p>删除一个收费标准</p>
     * <b>URL: /asset/deleteChargingStandard</b>
     */
    @RequestMapping("deleteChargingStandard")
    @RestReturn(value = String.class)
    public RestResponse deleteChargingStandard(DeleteChargingStandardCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示所有可以插入的变量          1
    /**
     * <p>展示所有可以插入的变量</p>
     * <b>URL: /asset/listAvailableVariables</b>
     */
    @RequestMapping("listAvailableVariables")
    @RestReturn(value = ListAvailableVariablesDTO.class, collection = true)
    public RestResponse listAvailableVariables(ListAvailableVariablesCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示账单组列表          1
    /**
     * <p>展示账单组列表</p>
     * <b>URL: /asset/listBillGroups</b>
     */
    @RequestMapping("listBillGroups")
    @RestReturn(value = ListBillGroupsDTO.class, collection = true)
    public RestResponse listBillGroups(OwnerIdentityCommand cmd) {
        List<ListBillGroupsDTO> list = assetService.listBillGroups(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 创建一个账单组          1
    /**
     * <p>创建一个账单组</p>
     * <b>URL: /asset/createBillGroup</b>
     */
    @RequestMapping("createBillGroup")
    @RestReturn(value = String.class)
    public RestResponse createBillGroup(CreateBillGroupCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 修改一个账单组          1
    /**
     * <p>修改一个账单组</p>
     * <b>URL: /asset/modifyBillGroup</b>
     */
    @RequestMapping("modifyBillGroup")
    @RestReturn(value = String.class)
    public RestResponse modifyBillGroup(ModifyBillGroupCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 调整账单组的顺序     1
    /**
     * <p>调整账单组的顺序</p>
     * <b>URL: /asset/adjustBillGroupOrder</b>
     */
    @RequestMapping("adjustBillGroupOrder")
    @RestReturn(value = String.class)
    public RestResponse adjustBillGroupOrder(AdjustBillGroupOrderCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示一个账单组的收费项目列表         1
    /**
     * <p>展示一个账单组的收费项目列表</p>
     * <b>URL: /asset/listChargingStandardForBillGroup</b>
     */
    @RequestMapping("listChargingStandardForBillGroup")
    @RestReturn(value = ListChargingItemsForBillGroupDTO.class, collection = true)
    public RestResponse listChargingItemsForBillGroup(BillGroupIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示一个账单组的一个收费项目详情     1
    /**
     * <p>展示一个账单组的一个收费项目详情</p>
     * <b>URL: /asset/listChargingItemDetailForBillGroup</b>
     */
    @RequestMapping("listChargingItemDetailForBillGroup")
    @RestReturn(value = ListChargingItemDetailForBillGroupDTO.class)
    public RestResponse listChargingItemDetailForBillGroup(BillGroupRuleIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 为一个账单组添加或修改一个收费项目，如果billGroupRuleId不为空则为修改       1
    /**
     * <p>为一个账单组添加或修改一个收费项目，如果billGroupRuleId不为空则为修改</p>
     * <b>URL: /asset/addOrModifyRuleForBillGroup</b>
     */
    @RequestMapping("addOrModifyRuleForBillGroup")
    @RestReturn(value = String.class)
    public RestResponse addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 删除一个账单组的收费项目         1
    /**
     * <p>删除一个账单组的收费项目</p>
     * <b>URL: /asset/deleteChargingItemForBillGroup</b>
     */
    @RequestMapping("deleteChargingItemForBillGroup")
    @RestReturn(value = String.class)
    public RestResponse deleteChargingItemForBillGroup(BillGroupRuleIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 按照账单组展示未出账单          2
    /**
     * <p>按照账单组展示未出账单,先按照账期排序，再按照公司，最后按照账单组的defaultOrder</p>
     * <b>URL: /asset/listNotSettledBill</b>
     */
    @RequestMapping("listNotSettledBill")
    @RestReturn(value = ListNotSettledBillResponse.class)
    public RestResponse listNotSettledBill(ListNotSettledBillCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示未出账单的一项的详情         2
    /**
     * <p>编辑账单，展示未出账单的一项的详情</p>
     * <b>URL: /asset/listNotSettledBillDetail</b>
     */
    @RequestMapping("listNotSettledBillDetail")
    @RestReturn(value = ListNotSettledBillDetailResponse.class)
    public RestResponse listNotSettledBillDetail(ListNotSettledBillDetailCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 修改后保存一个未出账单          2
    /**
     * <p>保存一个未出账单的修改</p>
     * <b>URL: /asset/modifyNotSettledBill</b>
     */
    @RequestMapping("modifyNotSettledBill")
    @RestReturn(value = String.class)
    public RestResponse modifyNotSettledBill(ModifyNotSettledBillCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示一个未出账单的收费项详情       1
    /**
     * <p>展示一个未出账单的收费项详情</p>
     * <b>URL: /asset/listNotSettledBillItems</b>
     */
    @RequestMapping("listNotSettledBillItems")
    @RestReturn(value = ListNotSettledBillItemsResponse.class)
    public RestResponse listNotSettledBillItems(ListNotSettledBillItemsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示一个未出账单的减免项列表           2
    /**
     * <p>展示一个未出账单的减免项列表</p>
     * <b>URL: /asset/listNotSettledBillExemptionItems</b>
     */
    @RequestMapping("listNotSettledBillExemptionItems")
    @RestReturn(value = ListNotSettledBillExemptionItemsResponse.class)
    public RestResponse listNotSettledBillExemptionItems(ListNotSettledBillItemsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 删除一个未出账单         2
    /**
     * <p>删除一个未出账单</p>
     * <b>URL: /asset/deletUnSettledBill</b>
     */
    @RequestMapping("deletUnSettledBill")
    @RestReturn(value = String.class)
    public RestResponse deletUnSettledBill(DeletUnSettledBillCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 删除一个未出账单的收费项     1
    /**
     * <p>删除一个未出账单的收费项</p>
     * <b>URL: /asset/deletBillItem</b>
     */
    @RequestMapping("deletBillItem")
    @RestReturn(value = String.class)
    public RestResponse deletBillItem(BillItemIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 删除一个未出账单的减免项     2
    /**
     * <p>删除一个未出账单的减免项</p>
     * <b>URL: /asset/deletExemptionItem</b>
     */
    @RequestMapping("deletExemptionItem")
    @RestReturn(value = String.class)
    public RestResponse deletExemptionItem(ExemptionItemIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 新增账单的页面          2
    /**
     * <p>新增账单的页面</p>
     * <b>URL: /asset/showCreateBill</b>
     */
    @RequestMapping("showCreateBill")
    @RestReturn(value = ShowCreateBillDTO.class)
    public RestResponse showCreateBill(BillGroupIdCommand cmd) {
        ShowCreateBillDTO dto = assetService.showCreateBill(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 保存一个新增账单         2
    /**
     * <p>保存一个新增账单</p>
     * <b>URL: /asset/createBill</b>
     */
    @RequestMapping("createBill")
    @RestReturn(value = String.class)
    public RestResponse createBill(CreateBillCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 批量导入账单           2
    /**
     * <p>批量导入账单</p>
     * <b>URL: /asset/importBills</b>
     */
    @RequestMapping("importBills")
    @RestReturn(value = String.class)
    public RestResponse importBills(HttpServletResponse response) {
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return restResponse;
    }



    // this is for 按照账单组展示已出账单          2
    /**
     * <p>按照账单组展示已出账单</p>
     * <b>URL: /asset/listSettledBill</b>
     */
    @RequestMapping("listSettledBill")
    @RestReturn(value = ListSettledBillResponse.class)
    public RestResponse listSettledBill(ListSettledBillCommand cmd) {
        ListSettledBillResponse listSettledBillResponse = assetService.listSettledBill(cmd);
        RestResponse response = new RestResponse(listSettledBillResponse);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 展示已出账单的收费项       2
    /**
     * <p>展示已出账单的收费项</p>
     * <b>URL: /asset/listSettledBillItems</b>
     */
    @RequestMapping("listSettledBillItems")
    @RestReturn(value = ListSettledBillItemsResponse.class)
    public RestResponse listSettledBillItems(ListSettledBillItemsCommand cmd) {
        ListSettledBillItemsResponse res = assetService.listSettledBillItems(cmd);
        RestResponse response = new RestResponse(res);
        System.out.println(3434);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 一键部分催缴           2
    /**
     * <p>一键部分催缴</p>
     * <b>URL: /asset/selectedNotice</b>
     */
    @RequestMapping("selectedNotice")
    @RestReturn(value = String.class)
    public RestResponse selectedNotice(SelectedNoticeCommand cmd) {
        assetService.selectNotice(cmd);
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 更改缴费状态           2
    /**
     * <p>更改账单的缴费状态</p>
     * <b>URL: /asset/modifyBillStatus</b>
     */
    @RequestMapping("modifyBillStatus")
    @RestReturn(value = String.class)
    public RestResponse modifyBillStatus(BillIdCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 一键全部催缴       2
    /**
     * <p>一键全部催缴</p>
     * <b>URL: /asset/OneKeyNotice</b>
     */
    @RequestMapping("OneKeyNotice")
    @RestReturn(value = String.class)
    public RestResponse OneKeyNotice(OneKeyNoticeCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 导出所有账单       2
    /**
     * <p>导出筛选过的所有账单</p>
     * <b>URL: /asset/exportPaymentBills</b>
     */
    @RequestMapping("exportPaymentBills")
    public HttpServletResponse exportPaymentBills(OneKeyNoticeCommand cmd,HttpServletResponse response) {

        return response;
    }

    // this is for 展示已出账单的减免项           2
    /**
     * <p>展示已出账单的减免项</p>
     * <b>URL: /asset/listSettledBillExemptionItems</b>
     */
    @RequestMapping("listSettledBillExemptionItems")
    @RestReturn(value = ListSettledBillExemptionItemsResponse.class)
    public RestResponse listSettledBillExemptionItems(BillGroupAndTargetCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 按照不同维度统计应收，已收，欠收的数额信息            2
    /**
     * <p>按照不同维度统计应收，已收，欠收的数额信息</p>
     * <b>URL: /asset/billStatics</b>
     */
    @RequestMapping("billStatics")
    @RestReturn(value = BillStaticsDTO.class, collection = true)
    public RestResponse billStatics(BillStaticsCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    // this is for 显示一个用户的物业账单          2
    /**
     * <p>显示一个用户的物业账单</p>
     * <b>URL: /asset/showBillForClient</b>
     */
    @RequestMapping("showBillForClient")
    @RestReturn(value = ShowBillForClientDTO.class)
    public RestResponse showBillForClient(ClientIdentityCommand cmd) {
        ShowBillForClientDTO dto = assetService.showBillForClient(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    //this is for 查看缴费详情
    /**
     * <p>查看缴费详情</p>
     * <b>URL: /asset/getPaymentLog</b>
     */
//    public RestResponse getPaymentLog(getPaymentLog)

    // this is for 用户的账单详情          4
    /**
     * <p>用户的账单的收费细项</p>
     * <b>URL: /asset/showBillDetailForClient</b>
     */
    @RequestMapping("showBillDetailForClient")
    @RestReturn(value = ShowBillDetailForClientResponse.class, collection = true)
    public RestResponse showBillDetailForClient(BillIdCommand cmd) {
        ShowBillDetailForClientResponse res = assetService.getBillDetailForClient(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    //this is for app选择切换月份查看账单      4
    /**
     * <p>app选择切换月份查看账单</p>
     * <b>URL: /asset/listBillDetailOnDateChange</b>
     */
    @RequestMapping("listBillDetailOnDateChange")
    @RestReturn(value = ShowBillDetailForClientResponse.class, collection = true)
    public RestResponse listBillDetailOnDateChange(ListBillDetailOnDateChangeCommand cmd) {
        ShowBillDetailForClientResponse res = assetService.listBillDetailOnDateChange(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
