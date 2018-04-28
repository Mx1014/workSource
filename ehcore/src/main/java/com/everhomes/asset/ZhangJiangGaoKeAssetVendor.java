//@formatter:off
package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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


    @Override
    ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType, String targetType, Long targetId, String dateStr, String contractId, Long billGroupId) {
        if(isZuolinByBillGroupId(billGroupId)){
            return zuolinAssetVendorHandler.listBillDetailOnDateChange(billStatus, ownerId, ownerType, targetType, targetId, dateStr, contractId, billGroupId);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listBillDetailOnDateChange(billStatus, ownerId, ownerType, targetType, targetId, dateStr, contractId, billGroupId);
    }

    /**
     *
     *   只找租金的，找不到只显示用户名，不显示合同
     */
    @Override
    FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.findUserInfoForPayment(cmd);
    }

    /**
     *
     * 无法判断账单组，直接按照租金处理
     */
    @Override
    GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
        return zhangJiangGaoKeThirdPartyAssetVendor.getAreaAndAddressByContract(cmd);
    }

    @Override
    PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            return zuolinAssetVendorHandler.placeAnAssetOrder(cmd);
        }
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
     *   新ui界面，这里用左邻的吧，不用对接的
     */
    @Override
    List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
        return zuolinAssetVendorHandler.listAllBillsForClient(cmd);
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
        if(cmd.getStatus() != null && cmd.getStatus().byteValue() == (byte)0){
            //未缴纳账单查询
            return new ArrayList<>();
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listBills(currentNamespaceId, response, cmd);
    }

    @Override
    List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageOffSet, Integer pageSize, Long ownerId, ListBillItemsResponse response, Long billGroupid) {
        if(isZuolinByBillGroupId(billGroupid)){
            return zuolinAssetVendorHandler.listBillItems(targetType, billId, targetName, pageOffSet, pageSize, ownerId, response, billGroupid);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listBillItems(targetType, billId, targetName, pageOffSet, pageSize, ownerId, response, billGroupid);
    }

    @Override
    List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes, Long billGroupId) {
        if(isZuolinByBillGroupId(billGroupId)){
            return zuolinAssetVendorHandler.listNoticeInfoByBillId(billIdAndTypes, billGroupId);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listNoticeInfoByBillId(billIdAndTypes, billGroupId);
    }


    ////////////////////////////////////


    @Override
    ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId, Byte isOnlyOwedBill, String contractId, Integer namespaceId) {
        if(isZuolinByBillGroupId(billGroupId)){
            ListAllBillsForClientCommand cmdAllBill = new ListAllBillsForClientCommand();
            cmdAllBill.setNamespaceId(namespaceId);
            cmdAllBill.setOwnerId(ownerId);
            cmdAllBill.setOwnerType(ownerType);
            cmdAllBill.setTargetId(targetId);
            cmdAllBill.setTargetType(targetType);
            cmdAllBill.setIsOnlyOwedBill(isOnlyOwedBill);
            List<ListAllBillsForClientDTO> listAllBillsForClientDTOS = zuolinAssetVendorHandler.listAllBillsForClient(cmdAllBill);
            return v2BillDTO2v1BillDTO(listAllBillsForClientDTOS);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.showBillForClient(ownerId, ownerType, targetType, targetId, billGroupId, isOnlyOwedBill, contractId, namespaceId);
    }

    private ShowBillForClientDTO v2BillDTO2v1BillDTO(List<ListAllBillsForClientDTO> showBillForClientV2DTOS) {
        ShowBillForClientDTO ret = new ShowBillForClientDTO();
        List<BillDetailDTO> billDetailDTOList = new ArrayList<>();
        BigDecimal overAllAmountOwed = null;
        Integer billPeriodMonth = 0;
        for(ListAllBillsForClientDTO target : showBillForClientV2DTOS){
            BillDetailDTO dto = new BillDetailDTO();
            dto.setBillId(target.getBillId());
            dto.setAmountReceviable(new BigDecimal(target.getAmountReceivable()));
            BigDecimal owed = new BigDecimal(target.getAmountOwed());
            dto.setAmountOwed(owed);
            if(overAllAmountOwed == null){
                overAllAmountOwed = new BigDecimal("0");
            }
            overAllAmountOwed = overAllAmountOwed.add(owed);
            dto.setDateStrBegin(target.getDateStrBegin());
            dto.setDateStrEnd(target.getDateStrEnd());
            Calendar beginC = DateUtils.guessDateTimeFormatAndParse(target.getDateStrBegin());
            Calendar endC = DateUtils.guessDateTimeFormatAndParse(target.getDateStrEnd());
            if(beginC != null && endC != null){
                int period = endC.get(Calendar.MONTH) - beginC.get(Calendar.MONTH);
                billPeriodMonth += period;
            }
            dto.setDateStr(target.getDateStr());
            billDetailDTOList.add(dto);
        }
        ret.setBillDetailDTOList(billDetailDTOList);
        ret.setAmountOwed(overAllAmountOwed);
        ret.setBillPeriodMonths(billPeriodMonth);
        return ret;
    }

    @Override
    ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
        //billId为long，传递string即张江高科的情境下，走不到这一步
        return zuolinAssetVendorHandler.listBillDetail(cmd);
    }

    @Override
    void deleteBill(String billId, Long billGroupId) {
        if(isZuolinByBillGroupId(billGroupId)){
            zuolinAssetVendorHandler.deleteBill(billId, billGroupId);
            return;
        }
        zhangJiangGaoKeThirdPartyAssetVendor.deleteBill(billId, billGroupId);
    }

    @Override
    void deleteBillItem(BillItemIdCommand cmd) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            zuolinAssetVendorHandler.deleteBillItem(cmd);
            return;
        }
        zhangJiangGaoKeThirdPartyAssetVendor.deleteBillItem(cmd);
    }

    @Override
    void deletExemptionItem(ExemptionItemIdCommand cmd) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            zuolinAssetVendorHandler.deletExemptionItem(cmd);
            return;
        }
        super.deletExemptionItem(cmd);
    }

    @Override
    ShowCreateBillDTO showCreateBill(Long billGroupId) {
        if(isZuolinByBillGroupId(billGroupId)){
            return zuolinAssetVendorHandler.showCreateBill(billGroupId);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.showCreateBill(billGroupId);
    }

    @Override
    void modifyBillStatus(BillIdCommand cmd) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            zuolinAssetVendorHandler.modifyBillStatus(cmd);
            return;
        }
        zhangJiangGaoKeThirdPartyAssetVendor.modifyBillStatus(cmd);
    }

    @Override
    ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            return zuolinAssetVendorHandler.listBillExemptionItems(cmd);
        }
        return zhangJiangGaoKeThirdPartyAssetVendor.listBillExemptionItems(cmd);
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
            return;
        }
        zhangJiangGaoKeThirdPartyAssetVendor.exportBillTemplates(cmd, response);
    }

    @Override
    BatchImportBillsResponse batchImportBills(BatchImportBillsCommand cmd, MultipartFile file) {
        if(isZuolinByBillGroupId(cmd.getBillGroupId())){
            return zuolinAssetVendorHandler.batchImportBills(cmd, file);
        }
        return super.batchImportBills(cmd, file);
    }
}
