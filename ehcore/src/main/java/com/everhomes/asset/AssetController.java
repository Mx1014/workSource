
package com.everhomes.asset;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
import com.everhomes.rest.servicemoduleapp.CreateAnAppMappingCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForBannerResponse;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

/**
**************************************************************
*                                                            *
*   .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *
*    |                     ______                     |      *
*    |                  .-"      "-.                  |      *
*    |                 /            \                 |      *
*    |     _          |              |          _     |      *
*    |    ( \         |,  .-.  .-.  ,|         / )    |      *
*    |     > "=._     | )(__/  \__)( |     _.=" <     |      *
*    |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *
*    |           "=._"(_     ^^     _)"_.="           |      *
*    |               "=\__|IIIIII|__/="               |      *
*    |              _.="| \IIIIII/ |"=._              |      *
*    |    _     _.="_.="\          /"=._"=._     _    |      *
*    |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *
*    |    > _.="                            "=._ <    |      *
*    |   (_/                                    \_)   |      *
*    |                                                |      *
*    '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *
*                                                            *
*           LASCIATE OGNI SPERANZA, VOI CH'ENTRATE           *
**************************************************************
*/
@RestDoc(value = "Asset Controller", site = "core")
@RestController
@RequestMapping("/asset")
public class AssetController extends ControllerBase {

private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);
@Autowired
private AssetService assetService;
@Autowired
private PaymentService paymentService;

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

// this is for 展示所有收费项目   4
/**
 * <p>获取园区启用的收费项目列表</p>
 * <b>URL: /asset/listAllChargingItems</b>
 */
@RequestMapping("listAllChargingItems")
@RestReturn(value = ListChargingItemsDTO.class, collection = true)
public RestResponse listAllChargingItems(OwnerIdentityCommand cmd) {
    List<ListChargingItemsDTO> list = assetService.listChargingItems(cmd);
    RestResponse response = new RestResponse(list);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示所有收费项目   4
/**
 * <p>获取账单组启用的收费项目列表</p>
 * <b>URL: /asset/listChargingItems</b>
 */
@RequestMapping("listChargingItems")
@RestReturn(value = ListChargingItemsDTO.class, collection = true)
public RestResponse listChargingItems(OwnerIdentityCommand cmd) {
    List<ListChargingItemsDTO> list = assetService.listAvailableChargingItems(cmd);
    RestResponse response = new RestResponse(list);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

    // this is for 获取园区下的所有滞纳金标准   4
    /**
     * <p>获取园区下的所有滞纳金标准</p>
     * <b>URL: /asset/listLateFineStandards</b>
     */
    @RequestMapping("listLateFineStandards")
    @RestReturn(value = ListLateFineStandardsDTO.class, collection = true)
    public RestResponse listLateFineStandards(ListLateFineStandardsCommand cmd) {
        List<ListLateFineStandardsDTO> list = assetService.listLateFineStandards(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
    /**
     * <p>园区收费项权限配置</p>
     * <b>URL: /asset/configChargingItems</b>
     */
    @RequestMapping("configChargingItems")
    @RestReturn(value = String.class)
    public RestResponse configChargingItems(ConfigChargingItemsCommand cmd) {
        assetService.configChargingItems(cmd);
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }


// this is for 展示一个收费项目的客户可见的所有标准列表         4
/**
 * <p>展示一* <b>URL: /asset/listChargingStandards</b></p>
 * <p>个收费项目的园区下的所有标准列表</p>
 *
 * */

@RequestMapping("listChargingStandards")
@RestReturn(value = ListChargingStandardsDTO.class, collection = true)
public RestResponse listChargingStandards(ListChargingStandardsCommand cmd) {
    List<ListChargingStandardsDTO> list = assetService.listChargingStandards(cmd);
    RestResponse response = new RestResponse(list);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示一个收费项目的客户可见的所有标准列表         1
/**
 * <p>展示一个收费项目的园区下的的所有标准列表</p>
 * <b>URL: /asset/listOnlyChargingStandards</b>
 */
@RequestMapping("listOnlyChargingStandards")
@RestReturn(value = ListChargingStandardsResponse.class, collection = true)
public RestResponse listOnlyChargingStandards(ListChargingStandardsCommand cmd) {
    ListChargingStandardsResponse resp = assetService.listOnlyChargingStandards(cmd);
    RestResponse response = new RestResponse(resp);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 增加一个收费标准         4
/**
 * <p>增加一个收费标准</p>
 * <b>URL: /asset/createChargingStandard</b>
 */
@RequestMapping("createChargingStandard")
@RestReturn(value = String.class)
public RestResponse createChargingStandard(CreateChargingStandardCommand cmd) {
    assetService.createChargingStandard(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}


// this is for 修改一个收费标准         4
/**
 * <p>修改一个收费标准</p>
 * <b>URL: /asset/modifyChargingStandard</b>
 */
@RequestMapping("modifyChargingStandard")
@RestReturn(value = String.class)
public RestResponse modifyChargingStandard(ModifyChargingStandardCommand cmd) {
    assetService.modifyChargingStandard(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 查看一个收费标准详情       4
/**
 * <p>查看一个收费标准详情</p>
 * <b>URL: /asset/getChargingStandardDetail</b>
 */
@RequestMapping("getChargingStandardDetail")
@RestReturn(value = GetChargingStandardDTO.class)
public RestResponse getChargingStandardDetail(GetChargingStandardCommand cmd) {
    GetChargingStandardDTO dto = assetService.getChargingStandardDetail(cmd);
    RestResponse response = new RestResponse(dto);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 删除一个收费标准         4
/**
 * <p>删除一个收费标准</p>
 * <b>URL: /asset/deleteChargingStandard</b>
 */
@RequestMapping("deleteChargingStandard")
@RestReturn(value = DeleteChargingStandardDTO.class)
public RestResponse deleteChargingStandard(DeleteChargingStandardCommand cmd) {
    DeleteChargingStandardDTO dto = assetService.deleteChargingStandard(cmd);
    RestResponse response = new RestResponse(dto);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示所有可以插入的变量          4
/**
 * <p>展示所有可以插入的变量</p>
 * <b>URL: /asset/listAvailableVariables</b>
 */
@RequestMapping("listAvailableVariables")
@RestReturn(value = ListAvailableVariablesDTO.class, collection = true)
public RestResponse listAvailableVariables(ListAvailableVariablesCommand cmd) {
    List<ListAvailableVariablesDTO> dtos = assetService.listAvailableVariables(cmd);
    RestResponse response = new RestResponse(dtos);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示账单组列表          4
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

// this is for 创建一个账单组          4
/**
 * <p>创建一个账单组</p>
 * <b>URL: /asset/createBillGroup</b>
 */
@RequestMapping("createBillGroup")
@RestReturn(value = String.class)
public RestResponse createBillGroup(CreateBillGroupCommand cmd) {
    assetService.createBillGroup(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 修改一个账单组          4
/**
 * <p>修改一个账单组</p>
 * <b>URL: /asset/modifyBillGroup</b>
 */
@RequestMapping("modifyBillGroup")
@RestReturn(value = String.class)
public RestResponse modifyBillGroup(ModifyBillGroupCommand cmd) {
    assetService.modifyBillGroup(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 调整账单组的顺序     4
/**
 * <p>调整账单组的顺序</p>
 * <b>URL: /asset/adjustBillGroupOrder</b>
 */
@RequestMapping("adjustBillGroupOrder")
@RestReturn(value = String.class)
public RestResponse adjustBillGroupOrder(AdjustBillGroupOrderCommand cmd) {
    assetService.adjustBillGroupOrder(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

/**
 * <p>删除一个账单组</p>
 * <b>URL: /asset/deleteBillGroup</b>
 */
@RequestMapping("deleteBillGroup")
@RestReturn(value = DeleteBillGroupReponse.class)
public RestResponse deleteBillGroup(DeleteBillGroupCommand cmd) {
    DeleteBillGroupReponse res = assetService.deleteBillGroup(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示一个账单组的收费项目列表         4
/**
 * <p>展示一个账单组的收费项目列表</p>
 * <b>URL: /asset/listChargingStandardForBillGroup</b>
 */
@RequestMapping("listChargingStandardForBillGroup")
@RestReturn(value = ListChargingItemsForBillGroupResponse.class, collection = true)
public RestResponse listChargingItemsForBillGroup(BillGroupIdCommand cmd) {
    ListChargingItemsForBillGroupResponse list = assetService.listChargingItemsForBillGroup(cmd);
    RestResponse response = new RestResponse(list);
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
    ListChargingItemDetailForBillGroupDTO dto = assetService.listChargingItemDetailForBillGroup(cmd);
    RestResponse response = new RestResponse(dto);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 为一个账单组添加或修改一个收费项目，如果billGroupRuleId不为空则为修改，一个账单组不能重复收费项目id       4
/**
 * <p>为一个账单组添加或修改一个收费项目，如果billGroupRuleId不为空则为修改</p>
 * <b>URL: /asset/addOrModifyRuleForBillGroup</b>
 */
@RequestMapping("addOrModifyRuleForBillGroup")
@RestReturn(value = AddOrModifyRuleForBillGroupResponse.class)
public RestResponse addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd) {
//        AddOrModifyRuleForBillGroupResponse res = assetService.addOrModifyRuleForBillGroup(cmd);
//        RestResponse response = new RestResponse(res);
    assetService.addOrModifyRuleForBillGroup(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 删除一个账单组的收费项目         4
/**
 * <p>删除一个账单组的收费项目</p>
 * <b>URL: /asset/deleteChargingItemForBillGroup</b>
 */
@RequestMapping("deleteChargingItemForBillGroup")
@RestReturn(value = DeleteChargingItemForBillGroupResponse.class)
public RestResponse deleteChargingItemForBillGroup(BillGroupRuleIdCommand cmd) {
    RestResponse response = new RestResponse(assetService.deleteChargingItemForBillGroup(cmd));
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 按照账单组展示未出账单          4
/**
 * <p>按照账单组展示未出账单,先按照账期排序，再按照公司，最后按照账单组的defaultOrder</p>
 * <b>URL: /asset/listNotSettledBill</b>
 */
@RequestMapping("listNotSettledBill")
@RestReturn(value = ListBillsResponse.class)
public RestResponse listNotSettledBill(ListBillsCommand cmd) {
    ListBillsResponse res = assetService.listBills(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示未出账单的一项的详情         4
/**
 * <p>编辑账单，展示未出账单的一项的详情</p>
 * <b>URL: /asset/listNotSettledBillDetail</b>
 */
@RequestMapping("listNotSettledBillDetail")
@RestReturn(value = ListBillDetailResponse.class)
public RestResponse listNotSettledBillDetail(ListBillDetailCommandStr cmd) {
    ListBillDetailResponse res = assetService.listBillDetail(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

/**
 * <p>保存一个未出账单的修改</p>
 * <b>URL: /asset/modifyNotSettledBill</b>
 */
@RequestMapping("modifyNotSettledBill")
@RestReturn(value = String.class)
public RestResponse modifyNotSettledBill(ModifyNotSettledBillCommand cmd) {
    assetService.modifyNotSettledBill(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}
/**
 * <p>保存一个已出账单的修改</p>
 * <b>URL: /asset/modifySettledBill</b>
 */
@RequestMapping("modifySettledBill")
@RestReturn(value = String.class)
public RestResponse modifySettledBill(ModifySettledBillCommand cmd) {
    assetService.modifySettledBill(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 删除一个账单         2
/**
 * <p>删除一个账单</p>
 * <b>URL: /asset/deletBill</b>
 */
@RequestMapping("deletBill")
@RestReturn(value = String.class)
public RestResponse deletBill(BillIdCommand cmd) {
    String result = assetService.deleteBill(cmd);
    RestResponse response = new RestResponse();
    if(result.equals("OK")){
        response.setErrorDescription("OK");
    }else{
        response.setErrorDescription(result);
    }
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 删除一个账单的收费项     1
/**
 * <p>删除一个账单的收费项</p>
 * <b>URL: /asset/deletBillItem</b>
 */
@RequestMapping("deletBillItem")
@RestReturn(value = String.class)
public RestResponse deletBillItem(BillItemIdCommand cmd) {
    String result = assetService.deleteBillItem(cmd);
    RestResponse response = new RestResponse();
    if(result.equals("OK")){
        response.setErrorDescription("OK");
    }else{
        response.setErrorDescription(result);
    }
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 删除一个账单的减免项     2
/**
 * <p>删除一个未出账单的减免项</p>
 * <b>URL: /asset/deletExemptionItem</b>
 */
@RequestMapping("deletExemptionItem")
@RestReturn(value = String.class)
public RestResponse deletExemptionItem(ExemptionItemIdCommand cmd) {
    String result = assetService.deletExemptionItem(cmd);
    RestResponse response = new RestResponse();
    if(result.equals("OK")){
        response.setErrorDescription("OK");
    }else{
        response.setErrorDescription(result);
    }
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 新增账单的页面          4
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


/**
 * <p>保存一个新增账单</p>
 * <b>URL: /asset/createBill</b>
 */
@RequestMapping("createBill")
@RestReturn(value = ListBillsDTO.class)
public RestResponse createBill(CreateBillCommand cmd) {
    ListBillsDTO dto = assetService.createBill(cmd);
    RestResponse response = new RestResponse(dto);
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
    // unfinished, under plan
    RestResponse restResponse = new RestResponse();
    restResponse.setErrorDescription("OK");
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    return restResponse;
}



// this is for 按照账单组展示已出账单          4
/**
 * <p>按照账单组展示已出账单</p>
 * <b>URL: /asset/listSettledBill</b>
 */
@RequestMapping("listSettledBill")
@RestReturn(value = ListBillsResponse.class)
public RestResponse listSettledBill(ListBillsCommand cmd) {
    ListBillsResponse listBillsResponse = assetService.listBills(cmd);
    RestResponse response = new RestResponse(listBillsResponse);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 展示账单的收费项       4
/**
 * <p>展示已出账单的收费项</p>
 * <b>URL: /asset/listBillItems</b>
 */
@RequestMapping("listBillItems")
@RestReturn(value = ListBillItemsResponse.class)
public RestResponse listBillItems(ListBillItemsCommand cmd) {
    ListBillItemsResponse res = assetService.listBillItems(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 一键部分催缴           4
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

// this is for 更改缴费状态           4
/**
 * <p>更改账单的缴费状态</p>
 * <b>URL: /asset/modifyBillStatus</b>
 */
@RequestMapping("modifyBillStatus")
@RestReturn(value = String.class)
public RestResponse modifyBillStatus(BillIdCommand cmd) {
    assetService.modifyBillStatus(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 一键全部催缴       4
/**
 * <p>一键全部催缴</p>
 * <b>URL: /asset/oneKeyNotice</b>
 */
@RequestMapping("oneKeyNotice")
@RestReturn(value = String.class)
public RestResponse oneKeyNotice(OneKeyNoticeCommand cmd) {
    assetService.OneKeyNotice(cmd);
    RestResponse response = new RestResponse();
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 导出所有账单       4
/**
 * <p>导出筛选过的所有账单</p>
 * <b>URL: /asset/exportPaymentBills</b>
 */
@RequestMapping("exportPaymentBills")
public HttpServletResponse exportPaymentBills(ListBillsCommand cmd,HttpServletResponse response) {
    assetService.exportPaymentBills(cmd,response);
    RestResponse restResponse = new RestResponse();
    restResponse.setErrorDescription("OK");
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    return null;
}

// this is for 展示账单的减免项           4
/**
 * <p>展示账单的减免项</p>
 * <b>URL: /asset/listBillExemptionItems</b>
 */
@RequestMapping("listBillExemptionItems")
@RestReturn(value = ListSettledBillExemptionItemsResponse.class)
public RestResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
    ListSettledBillExemptionItemsResponse res = assetService.listBillExemptionItems(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 按照不同维度统计应收，已收，欠收的数额信息            4
/**
 * <p>按照不同维度统计应收，已收，欠收的数额信息</p>
 * <b>URL: /asset/billStatics</b>
 */
@RequestMapping("billStatics")
@RestReturn(value = BillStaticsDTO.class, collection = true)
public RestResponse billStatics(BillStaticsCommand cmd) {
    List<BillStaticsDTO> list = assetService.listBillStatics(cmd);
    RestResponse response = new RestResponse(list);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 显示一个用户的物业账单          4
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

// this is for 显示一个用户的物业账单          4
/**
 * <p>显示一个用户的物业账单</p>
 * <b>URL: /asset/showBillForClientV2</b>
 */
@RequestMapping("showBillForClientV2")
@RestReturn(value = ShowBillForClientV2DTO.class,collection = true)
public RestResponse showBillForClientV2(ShowBillForClientV2Command cmd) {
    if(cmd.getNamespaceId()!=UserContext.getCurrentNamespaceId()){
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
    }
    List<ShowBillForClientV2DTO> dtos = assetService.showBillForClientV2(cmd);
    RestResponse response = new RestResponse(dtos);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 显示一个用户的物业账单          4
/**
 * <p>显示一个用户的物业账单</p>
 * <b>URL: /asset/listAllBillsForClient</b>
 */
@RequestMapping("listAllBillsForClient")
@RestReturn(value = ListAllBillsForClientDTO.class,collection = true)
public RestResponse listAllBillsForClient(ListAllBillsForClientCommand cmd) {
    List<ListAllBillsForClientDTO> dtos = assetService.listAllBillsForClient(cmd);
    RestResponse response = new RestResponse(dtos);
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


//    /**
//     * <p>测试清单产生</p>
//     * <b>URL: /asset/xxd</b>
//     *
//     * 这个会自动生成一个错误的doctor！restresponse，因为我写的@RequestBody？下次测试下
//     */
//    @RequestMapping("doctor!")
//    @RestReturn(PaymentExpectanciesResponse.class)
//    public PaymentExpectanciesResponse hi(@RequestBody PaymentExpectanciesCommand cmd){
//        List<FeeRules> feesRules = cmd.getFeesRules();
//        for(int i = 0; i < feesRules.size(); i++) {
//            List<VariableIdAndValue> list = feesRules.get(i).getVariableIdAndValueList();
//            for(int j = 0; j < list.size(); j++){
//                Integer variableValue = (Integer)list.get(j).getVariableValue();
//                BigDecimal c = new BigDecimal(variableValue);
//                list.get(j).setVariableValue(c);
//            }
//        }
//        return assetService.paymentExpectancies(cmd);
//    }http://wangwt.lab.everhomes.com/evh/asset/listBillExpectanciesOnContract

/**
 * <p>展示预期的费用清单</p>
 * <b>URL: /asset/listBillExpectanciesOnContract</b>
 *
 */
@RequestMapping("listBillExpectanciesOnContract")
@RestReturn(PaymentExpectanciesResponse.class)
public RestResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd){
    PaymentExpectanciesResponse res = assetService.listBillExpectanciesOnContract(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorDescription("OK");
    response.setErrorCode(ErrorCodes.SUCCESS);
    return response;
}

// this is for 导出租金模板       4
/**
 * <p>导出租金模板</p>
 * <b>URL: /asset/exportRentalExcelTemplate</b>
 */
@RequestMapping("exportRentalExcelTemplate")
public HttpServletResponse exportRentalExcelTemplate(HttpServletResponse response) {
    assetService.exportRentalExcelTemplate(response);
    RestResponse restResponse = new RestResponse();
    restResponse.setErrorDescription("OK");
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    return null;
}

/**
 * <b>URL: /asset/findUserInfoForPayment</b>
 * <p>展示用户名称，合同，门牌楼栋和面积和</p>
 */
@RequestMapping("findUserInfoForPayment")
@RestReturn(value=FindUserInfoForPaymentResponse.class)
public RestResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
    FindUserInfoForPaymentResponse res = this.assetService.findUserInfoForPayment(cmd);
    RestResponse response = new RestResponse(res);
    response.setErrorCode(ErrorCodes.SUCCESS);
    response.setErrorDescription("OK");
    return response;
}
/**
 * <b>URL: /asset/getAreaAndAddressByContract</b>
 * <p>根据合同的id获取合同下的楼栋门牌和面积</p>
 */
@RequestMapping("getAreaAndAddressByContract")
@RestReturn(value=GetAreaAndAddressByContractDTO.class,collection=true)
public RestResponse getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
    GetAreaAndAddressByContractDTO dto = this.assetService.getAreaAndAddressByContract(cmd);
    RestResponse response = new RestResponse(dto);
    response.setErrorCode(ErrorCodes.SUCCESS);
    response.setErrorDescription("OK");
    return response;
}

/**
 * <b>URL: /asset/updateBillsToSettled</b>
 * <p>将未出账单转为已出账单</p>
 */
@RequestMapping("updateBillsToSettled")
@RestReturn(String.class)
public RestResponse updateBillsToSettled(UpdateBillsToSettled cmd) {
    this.assetService.updateBillsToSettled(cmd);
    RestResponse response = new RestResponse();
    response.setErrorCode(ErrorCodes.SUCCESS);
    response.setErrorDescription("OK");
    return response;
}
/**
 * <b>URL: /asset/payBills</b>
 * <p></p>
 */
@RequestMapping("payBills")
@RestReturn(PreOrderDTO.class)
public RestResponse placeAnAssetOrder(PlaceAnAssetOrderCommand cmd){
    PreOrderDTO response = assetService.placeAnAssetOrder(cmd);
    RestResponse restResponse = new RestResponse(response);
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    restResponse.setErrorDescription("OK");
    return restResponse;
}

/**
 * <b>URL: /asset/listPaymentBill</b>
 * <p>结算-账单明细</p>
 */
@RequestMapping(value = "listPaymentBill")
@RestReturn(ListPaymentBillResp.class)
public RestResponse listPaymentBill(ListPaymentBillCmd cmd, HttpServletRequest request) throws Exception {
    ListPaymentBillResp result = assetService.listPaymentBill(cmd);
    RestResponse response = new RestResponse(result);
    return response;
}

/**
 * <b>URL: /asset/listPaymentBillDetail</b>
 * <p>交易明细查看账单明细接口</p>
 */
@RequestMapping(value = "listPaymentBillDetail")
@RestReturn(PaymentOrderBillDTO.class)
public RestResponse listPaymentBillDetail(ListPaymentBillCmd cmd, HttpServletRequest request) throws Exception {
    ListPaymentBillResp listPaymentBillResp = assetService.listPaymentBill(cmd);
    PaymentOrderBillDTO result = new PaymentOrderBillDTO();
    if(listPaymentBillResp != null && listPaymentBillResp.getPaymentOrderBillDTOs() != null
            && listPaymentBillResp.getPaymentOrderBillDTOs().size() != 0 
            && listPaymentBillResp.getPaymentOrderBillDTOs().get(0) != null) {
        result = listPaymentBillResp.getPaymentOrderBillDTOs().get(0);
    }
    RestResponse response = new RestResponse(result);
    return response;
}

    /**
     * <b>URL: /asset/autoNoticeConfig</b>
     * <p>自动缴费配置</p>
     */
    @RequestMapping("autoNoticeConfig")
    @RestReturn(String.class)
    public RestResponse autoNoticeConfig(AutoNoticeConfigCommand cmd){
        assetService.autoNoticeConfig(cmd);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }


/**
 * <p>测试清单产生</p>
 * <b>URL: /asset/doctor</b>
 *
 * 这个会自动生成一个错误的doctor！restresponse，因为我写的@RequestBody？下次测试下
 */
@RequestMapping("doctor")
@RestReturn(String.class)
public String hi(@RequestBody PaymentExpectanciesCommand cmd){
//        List<FeeRules> feesRules = cmd.getFeesRules();
//        for(int i = 0; i < feesRules.size(); i++) {
//            List<VariableIdAndValue> list = feesRules.get(i).getVariableIdAndValueList();
//            for(int j = 0; j < list.size(); j++){
//                Integer variableValue = (Integer)list.get(j).getVariableValue();
//                BigDecimal c = new BigDecimal(variableValue);
//                list.get(j).setVariableValue(c);
//            }
//        }
    assetService.paymentExpectanciesCalculate(cmd);
    return "ROU ARE WA GA DEKI ROU KU ROU!";
}


/**
 * <b>URL: /asset/listAutoNoticeConfig</b>
 * <p>设置自动催缴</p>
 */
@RequestMapping("listAutoNoticeConfig")
@RestReturn(ListAutoNoticeConfigResponse.class)
public RestResponse listAutoNoticeConfig(ListAutoNoticeConfigCommand cmd){
    ListAutoNoticeConfigResponse response = assetService.listAutoNoticeConfig(cmd);
    RestResponse restResponse = new RestResponse(response);
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    restResponse.setErrorDescription("OK");
    return restResponse;
}

    /**
     * <b>URL: /asset/checkEnterpriseHasArrearage</b>
     * <p>检查企业是否有欠费的账单</p>
     */
    @RequestMapping("checkEnterpriseHasArrearage")
    @RestReturn(value = CheckEnterpriseHasArrearageResponse.class)
    public RestResponse checkEnterpriseHasArrearage(CheckEnterpriseHasArrearageCommand cmd){
        CheckEnterpriseHasArrearageResponse res = assetService.checkEnterpriseHasArrearage(cmd);
        RestResponse restResponse = new RestResponse(res);
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return restResponse;
    }

/**
 * <b>URL: /asset/functionDisableList</b>
 * <p>功能失效列表</p>
 */
@RequestMapping("functionDisableList")
@RestReturn(value = FunctionDisableListDto.class)
public RestResponse functionDisableList(FunctionDisableListCommand cmd){
    FunctionDisableListDto dto = assetService.functionDisableList(cmd);
    RestResponse restResponse = new RestResponse(dto);
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    restResponse.setErrorDescription("OK");
    return restResponse;
}


/**
 * <b>URL: /asset/batchImportBills</b>
 * <p>批量导入账单</p>
 */
@RequestMapping("batchImportBills")
@RestReturn(value = BatchImportBillsResponse.class)
public RestResponse batchImportBills(MultipartFile attachment,BatchImportBillsCommand cmd){
    BatchImportBillsResponse  res = assetService.batchImportBills(cmd, attachment);
    RestResponse restResponse = new RestResponse(res);
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    restResponse.setErrorDescription("OK");
    return restResponse;
}

/**
 * <b>URL: /asset/exportBillTemplates</b>
 * <p>导出账单的模板</p>
 */
@RequestMapping("exportBillTemplates")
public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response){
    assetService.exportBillTemplates(cmd, response);
}

//
/**
 * <b>URL: /asset/listBillRelatedTransac</b>
 * <p>列出账单所属的交易明细</p>
 */
@RequestMapping("listBillRelatedTransac")
public RestResponse listBillRelatedTransac(listBillRelatedTransacCommand cmd){
    ListPaymentBillResp listPaymentBillResp = assetService.listBillRelatedTransac(cmd);
    RestResponse restResponse = new RestResponse(listPaymentBillResp);
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    restResponse.setErrorDescription("OK");
    return restResponse;
}

/**
 * <b>URL: /asset/reCalBill</b>
 * <p>重新计算账单，不改变状态</p>
 */
@RequestMapping("reCalBill")
public RestResponse reCalBill(ReCalBillCommand cmd){
    assetService.reCalBill(cmd);
    RestResponse restResponse = new RestResponse();
    restResponse.setErrorCode(ErrorCodes.SUCCESS);
    restResponse.setErrorDescription("OK");
    return restResponse;
}

    /**
     * <b>URL: /asset/listUploadCertificates</b>
     * <p>线下缴费场景，显示付费凭证图片</p>
     */
    @RequestMapping("listUploadCertificates")
    @RestReturn(value=UploadCertificateInfoDTO.class)
    public RestResponse listUploadCertificates(ListUploadCertificatesCommand cmd){
    	UploadCertificateInfoDTO dto = assetService.listUploadCertificates(cmd);
        RestResponse restResponse = new RestResponse(dto);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    /**
     * <b>URL: /asset/noticeTrigger</b>
     * <p>启动自动催缴的定时任务</p>
     */
    @RequestMapping("noticeTrigger")
    public RestResponse noticeTrigger(NoticeTriggerCommand cmd){
        assetService.noticeTrigger(cmd.getNamespaceId());
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    
    /**
     * <b>URL: /asset/uploadCertificate</b>
     * <p>线下缴费场景，上传付费凭证图片</p>
     */
    @RequestMapping("uploadCertificate")
    @RestReturn(value=UploadCertificateInfoDTO.class)
    public RestResponse uploadCertificate(UploadCertificateCommand cmd){
    	UploadCertificateInfoDTO dto = assetService.uploadCertificate(cmd);
        RestResponse restResponse = new RestResponse(dto);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    /**
     * <b>URL: /asset/judgeAppShowPay</b>
     * <p>取出配置项，用于判断APP多账单组的缴费方式：全部缴费/部分缴费/单个缴费</p>
     */
    @RequestMapping("judgeAppShowPay")
    public RestResponse judgeAppShowPay(JudgeAppShowPayCommand cmd){
    	JudgeAppShowPayResponse judgeAppShowPayResponse = assetService.judgeAppShowPay(cmd);
        RestResponse restResponse = new RestResponse(judgeAppShowPayResponse);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    
    /**
     * <p>展示未缴费但是上传了缴费凭证的已出账单</p>
     * <b>URL: /asset/listSettledBillWithCertificate</b>
     */
    @RequestMapping("listSettledBillWithCertificate")
    @RestReturn(value = ListBillsResponse.class)
    public RestResponse listSettledBillWithCertificate(ListBillsCommand cmd) {
        ListBillsResponse listBillsResponse = assetService.listBills(cmd);
        RestResponse response = new RestResponse(listBillsResponse);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
    /**
     * <p>导出筛选过的所有交易明细</p>
     * <b>URL: /asset/exportOrders</b>
     */
    @RequestMapping("exportOrders")
    public HttpServletResponse exportOrders(ListPaymentBillCmd cmd,HttpServletResponse response) {
        assetService.exportOrders(cmd,response);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return null;
    }
    
    /**
     * <b>URL: /asset/listPayeeAccounts</b>
     * <p>列出当前项目下所有的收款方账户</p>
     */
    @RequestMapping("listPayeeAccounts")
    @RestReturn(value = ListBizPayeeAccountDTO.class, collection = true)
    public RestResponse listPayeeAccounts(ListPayeeAccountsCommand cmd){
    	List<ListBizPayeeAccountDTO> list = assetService.listPayeeAccounts(cmd);
        RestResponse restResponse = new RestResponse(list);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    
    /**
	 * <b>URL: /asset/payNotify</b>
	 * <p>支付模块回调接口，通知支付结果</p>
	 */
	@RequestMapping("payNotify")
	@RestReturn(value=String.class)
	@RequireAuthentication(false)
	public RestResponse payNotify(@Valid OrderPaymentNotificationCommand cmd) {
		assetService.payNotify(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <p>由于史晗不肯造张江高科的账单数据，所以只能手动造账单数据用于测试新支付</p>
	 * <b>URL: /asset/createTestZJGKBill</b>
	 */
	@RequestMapping("createTestZJGKBill")
	@RestReturn(value = ListBillsDTO.class)
	public RestResponse createTestZJGKBill(){
	    ListBillsDTO dto = assetService.createTestZJGKBill();
	    RestResponse response = new RestResponse(dto);
	    response.setErrorDescription("OK");
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    return response;
	}
	
	/**
	 * <b>URL: /asset/isProjectNavigateDefault</b>
	 * <p>项目导航区分全部、解耦 是否使用默认配置</p>
	 */
	@RequestMapping("isProjectNavigateDefault")
	@RestReturn(value = IsProjectNavigateDefaultResp.class)
	public RestResponse isProjectNavigateDefault(IsProjectNavigateDefaultCmd cmd){
		IsProjectNavigateDefaultResp dto = assetService.isProjectNavigateDefault(cmd);
	    RestResponse response = new RestResponse(dto);
	    response.setErrorDescription("OK");
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    return response;
	}
	
	/**
     * <b>URL: /asset/transferOrderPaymentType</b>
     * <p>以前的订单数据需要刷支付方式，做数据迁移</p>
     */
    @RequestMapping("transferOrderPaymentType")
    public RestResponse transferOrderPaymentType(){
        assetService.transferOrderPaymentType();
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    
    /*
     * <p>计算天企汇历史合同的租赁总额字段接口</p>
     * <b>URL: /asset/calculateRentForContract</b>
     */
    @RequestMapping("calculateRentForContract")
    public RestResponse calculateRentForContract(CalculateRentCommand cmd) {
        assetService.calculateRentForContract(cmd);
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    /**
     * to map an application to another. The relation is mutual, and for one of the two applications,
     * it can only have one mate from the same module to prevent a bigamy
     * @param cmd
     * @return OK
     */
    @RequestMapping("createAnAppMapping")
    @RestReturn(value=ListServiceModuleAppsForBannerResponse.class)
    public RestResponse createAnAppMapping(CreateAnAppMappingCommand cmd) {
        assetService.createAnAppMapping(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * forcely update a mapping relation to be allocated again
     * @param cmd
     * @return OK
     */
    @RequestMapping("updateAnAppMapping")
    @RestReturn(value=ListServiceModuleAppsForBannerResponse.class)
    public RestResponse updateAnAppMapping(UpdateAnAppMappingCommand cmd) {
        assetService.updateAnAppMapping(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /asset/isUserExistInAddress</b>
     * <p>用于创建账单时，判断个人用户是否关联了该账单所包含的楼栋门牌</p>
     */
    @RequestMapping("isUserExistInAddress")
    @RestReturn(value=IsUserExistInAddressResponse.class)
    public RestResponse isUserExistInAddress(IsUserExistInAddressCmd cmd){
    	IsUserExistInAddressResponse response = assetService.isUserExistInAddress(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
    
    /**
     * <p>对公转账：按照账单组展示已出账单</p>
     * <b>URL: /asset/listSettledBillForEnt</b>
     */
    @RequestMapping("listSettledBillForEnt")
    @RestReturn(value = ListBillsResponseForEnt.class)
    public RestResponse listSettledBillForEnt(ListBillsCommandForEnt cmd) {
    	ListBillsResponseForEnt listBillsResponseForEnt = assetService.listBillsForEnt(cmd);
        RestResponse response = new RestResponse(listBillsResponseForEnt);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
    
    /**
     * <p>对公转账：导出筛选过的所有账单</p>
     * <b>URL: /asset/exportSettledBillsForEnt</b>
     */
    @RequestMapping("exportSettledBillsForEnt")
    public HttpServletResponse exportSettledBillsForEnt(ListBillsCommandForEnt cmd,HttpServletResponse response) {
        assetService.exportSettledBillsForEnt(cmd,response);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return null;
    }
    
    /**
     * <p>对公转账：结算-账单明细</p>
     * <b>URL: /asset/listPaymentBillForEnt</b>
     */
    @RequestMapping(value = "listPaymentBillForEnt")
    @RestReturn(ListPaymentBillRespForEnt.class)
    public RestResponse listPaymentBillForEnt(ListPaymentBillCmdForEnt cmd, HttpServletRequest request) throws Exception {
        ListPaymentBillRespForEnt result = paymentService.listPaymentBillForEnt(cmd);
        RestResponse response = new RestResponse(result);
        return response;
    }
    
    /**
     * <p>对公转账：导出筛选过的所有交易明细</p>
     * <b>URL: /asset/exportOrdersForEnt</b>
     */
    @RequestMapping("exportOrdersForEnt")
    public HttpServletResponse exportOrdersForEnt(ListPaymentBillCmdForEnt cmd,HttpServletResponse response) {
        assetService.exportOrdersForEnt(cmd,response);
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return null;
    }
    
    /**
     * <p>对公转账：信息列表</p>
     * <b>URL: /asset/publicTransferBill</b>
     */
    @RequestMapping("publicTransferBill")
    @RestReturn(value = PublicTransferBillRespForEnt.class)
    public RestResponse publicTransferBill(PublicTransferBillCmdForEnt cmd) {
    	PublicTransferBillRespForEnt listPaymentBillResp = assetService.publicTransferBillForEnt(cmd);
        RestResponse restResponse = new RestResponse(listPaymentBillResp);
        restResponse.setErrorDescription("OK");
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        return restResponse;
    }
    
}

