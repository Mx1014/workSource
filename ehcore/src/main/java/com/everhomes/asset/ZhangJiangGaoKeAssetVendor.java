//@formatter:off
package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.PreOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/4/16.
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZJGK")
public class ZhangJiangGaoKeAssetVendor extends AssetVendorHandler{
    private static final String ZUJIN = "租金";
//    private static final String SHUI_DIAN = "水电费";
    @Autowired
    private ZhangJiangGaoKeThirdPartyAssetVendor zhangJiangGaoKeThirdPartyAssetVendor;
    @Autowired
    private ZuolinAssetVendorHandler zuolinAssetVendorHandler;
    @Autowired
    private AssetProvider assetProvider;

    private boolean isZuolinByBillGroupId(Long billGroupId){
        if(billGroupId == null) return true;
        String billGroupNameById = assetProvider.findBillGroupNameById(billGroupId);
        if(billGroupNameById == null){
            return true;
        }
        if(billGroupNameById.equals(ZUJIN)){
            return false;
        }
        return true;
    }
    private boolean isZuolinByBillId(String billId){
        try{
            long l = Long.parseLong(billId);
            return assetProvider.checkBillExistById(l);
        }catch (NumberFormatException e){
            return false;
        }
    }
    @Override
    ListBillsDTO createBill(CreateBillCommand cmd) {
        Long billGroupId = cmd.getBillGroupDTO().getBillGroupId();
        if(isZuolinByBillGroupId(billGroupId)){
            return zuolinAssetVendorHandler.createBill(cmd);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.createBill(cmd);
    }

    @Override
    ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType) {
        if(isZuolinByBillId(billId)){
            return zuolinAssetVendorHandler.getBillDetailForClient(ownerId, billId, targetType);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.getBillDetailForClient(ownerId, billId, targetType);
    }

    /**
     *
     * 此功能只有张江高科对接的, 无法切换, todo ask frontend is it good to add billGroupId as parameter?
     */
    @Override
    ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType, String targetType, Long targetId, String dateStr, String contractId) {
        return zhangJiangGaoKeThirdPartyAssetVendor.listBillDetailOnDateChange(billStatus, ownerId, ownerType, targetType, targetId, dateStr, contractId);
    }

    /**
     *
     * todo 无法判断账单组，需要新的参数     ask frontend is it good to add billGroupId as parameter?
     */
    @Override
    FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.findUserInfoForPayment(cmd);
    }

    /**
     *
     * todo 无法判断账单组，需要新的参数     ask frontend is it good to add billGroupId as parameter?
     */
    @Override
    GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.getAreaAndAddressByContract(cmd);
    }
    /**
     *
     * todo 无法判断账单组，需要新的参数     ask frontend is it good to add billGroupId as parameter?
     */
    @Override
    PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.placeAnAssetOrder(cmd);
    }
    /**
     *
     *  张江高科不用基于tab卡的ui
     */
    @Override
    List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.showBillForClientV2(cmd);
    }
    /**
     *
     * todo 无法判断账单组，需要新的参数     ask frontend is it good to add billGroupId as parameter?
     */
    @Override
    List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.listAllBillsForClient(cmd);
    }
    /**
     *
     * 熊颖写的老的缴费代码，接口已经不用了
     */
    @Override
    ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId, Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
        return zuolinAssetVendorHandler.listSimpleAssetBills(ownerId, ownerType, targetId, targetType, organizationId, addressId, tenant, status, startTime, endTime, pageAnchor, pageSize);
    }
    /**
     *
     * 熊颖写的老的缴费代码，接口已经不用了
     */
    @Override
    AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId) {
        return zuolinAssetVendorHandler.findAssetBill(id, ownerId, ownerType, targetId, targetType, templateVersion, organizationId, dateStr, tenantId, tenantType, addressId);
    }
    /**
     *
     * 熊颖写的老的缴费代码，接口已经不用了
     */
    @Override
    AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId) {
        return zuolinAssetVendorHandler.getAssetBillStat(tenantType, tenantId, addressId);
    }

    @Override
    List<ListBillsDTO> listBills(Integer currentNamespaceId, ListBillsResponse response, ListBillsCommand cmd) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            return zuolinAssetVendorHandler.listBills(currentNamespaceId, response, cmd);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listBills(currentNamespaceId, response, cmd);
    }

    @Override
    List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageOffSet, Integer pageSize, Long ownerId, ListBillItemsResponse response) {
        if(isZuolinByBillId(billId)){
            return zuolinAssetVendorHandler.listBillItems(targetType, billId, targetName, pageOffSet, pageSize, ownerId, response);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listBillItems(targetType, billId, targetName, pageOffSet, pageSize, ownerId, response);
    }

    @Override
    List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes) {
        if(billIdAndTypes == null || billIdAndTypes.size() < 1){
            return new ArrayList<>();
        }
        if(isZuolinByBillId(billIdAndTypes.get(0).getBillId())){
            return zuolinAssetVendorHandler.listNoticeInfoByBillId(billIdAndTypes);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listNoticeInfoByBillId(billIdAndTypes);
    }


    ////////////////////////////////////


    @Override
    ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId, Byte isOnlyOwedBill, String contractId, Integer namespaceId) {
        if(isZuolinByBillGroupId(billGroupId)){
            ShowBillForClientV2Command cmd = new ShowBillForClientV2Command();
            cmd.setTargetType(targetType);
            cmd.setTargetId(targetId);
            cmd.setNamespaceId(namespaceId);
            //todo
            zuolinAssetVendorHandler.showBillForClientV2()
            return zuolinAssetVendorHandler.showBillForClient(ownerId, ownerType, targetType, targetId, billGroupId, isOnlyOwedBill, contractId);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.showBillForClient(ownerId, ownerType, targetType, targetId, billGroupId, isOnlyOwedBill, contractId);
    }

    @Override
    ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
        //billId为long，传递string即张江高科的情境下，走不到这一步
        return zuolinAssetVendorHandler.listBillDetail(cmd);
    }

    @Override
    void deleteBill(String l) {
        if(isZuolinByBillId(l)){
            zuolinAssetVendorHandler.deleteBill(l);
        }
        zhangJiangGaoKeThirdPartyAssetVendor.deleteBill(l);
    }

    @Override
    void deleteBillItem(BillItemIdCommand cmd) {
        if(isZuolinByBillItemId(cmd.getBillItemId())){
            zuolinAssetVendorHandler.deleteBillItem(cmd);
        }
        zhangJiangGaoKeThirdPartyAssetVendor.deleteBillItem(cmd);
    }

    @Override
    void deletExemptionItem(ExemptionItemIdCommand cmd) {
        // todo can I get the billGroupId info instead of guessing
        super.deletExemptionItem(cmd);
    }

    @Override
    ShowCreateBillDTO showCreateBill(Long billGroupId) {
        if(isZuolinByBillGroupId(billGroupId)){
            return zuolinAssetVendorHandler.showCreateBill(billGroupId);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.showCreateBill(billGroupId);
    }

    // todo 这里可以改为billGroupId应该
    @Override
    void modifyBillStatus(BillIdCommand cmd) {
        if(isZuolinByBillId(cmd.getBillId())){
            zuolinAssetVendorHandler.modifyBillStatus(cmd);
        }
        zhangJiangGaoKeThirdPartyAssetVendor.modifyBillStatus(cmd);
    }

    @Override
    ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
        //todo 改为billGroupid
        return super.listBillExemptionItems(cmd);
    }

    @Override
    List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        // without data from zjgk, just display the data from zuolin local
        return zuolinAssetVendorHandler.listBillStatics(cmd);
    }

    @Override
    PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd) {
        // just try to get the bill data, return empty list with zhangjianggaoke contractId
        return zuolinAssetVendorHandler.listBillExpectanciesOnContract(cmd);
    }

    @Override
    void exportRentalExcelTemplate(HttpServletResponse response) {
        // api not working now?
        zuolinAssetVendorHandler.exportRentalExcelTemplate(response);
    }

    @Override
    void updateBillsToSettled(UpdateBillsToSettled cmd) {
        // 对接的合同本地有存，contractId唯一，这里可以直接查
        zuolinAssetVendorHandler.updateBillsToSettled(cmd);
    }

    @Override
    void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            zuolinAssetVendorHandler.exportBillTemplates(cmd, response);
        }
        zhangJiangGaoKeThirdPartyAssetVendor.exportBillTemplates(cmd, response);
    }

    @Override
    BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            zuolinAssetVendorHandler.batchImportBills(cmd, file);
        }
        return super.batchImportBills(cmd, file);
    }
}
