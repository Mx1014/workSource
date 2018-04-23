//@formatter:off
package com.everhomes.asset;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.szwwyjf.SZWQuery;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.OrganizationProvider;

import com.everhomes.rest.asset.*;

import com.everhomes.rest.order.PreOrderDTO;

import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;


/**
 * Created by chongxin yang on 2018/04/19.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZZ")
public class ZhuZongAssetVendor implements AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuZongAssetVendor.class);
    
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizationProvider organizationProvider;

	@Override
	public ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId,
			String targetType, Long organizationId, Long addressId, String tenant, Byte status, Long startTime,
			Long endTime, Long pageAnchor, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId,
			String targetType, Long templateVersion, Long organizationId, String dateStr, Long tenantId,
			String tenantType, Long addressId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ListBillsDTO> listBills(String contractNum, Integer currentNamespaceId, Long ownerId, String ownerType,
			String buildingName, String apartmentName, Long addressId, String billGroupName, Long billGroupId,
			Byte billStatus, String dateStrBegin, String dateStrEnd, Long pageAnchor, Integer pageSize,
			String targetName, Byte status, String targetType, ListBillsResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BillDTO> listBillItems(String targetType, String billId, String targetName, Integer pageOffSet,
			Integer pageSize, Long ownerId, ListBillItemsResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId,
			Long billGroupId, Byte isOnlyOwedBill, String contractId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType,
			String targetType, Long targetId, String dateStr, String contractId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deleteBill(String l) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteBillItem(BillItemIdCommand cmd) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deletExemptionItem(ExemptionItemIdCommand cmd) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ShowCreateBillDTO showCreateBill(Long billGroupId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ListBillsDTO createBill(CreateBillCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void modifyBillStatus(BillIdCommand cmd) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void exportRentalExcelTemplate(HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateBillsToSettled(UpdateBillsToSettled cmd) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
		try {
			
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor showBillForClientV2() cmd={}",cmd,e);
		}
		//return response;
		return null;
	}
	
	@Override
	public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
		try {
			
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor listAllBillsForClient() cmd={}",cmd,e);
		}
		//return response;
		return null;
	}
	
	@Override
	public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType,Long organizationId) {
		try {
			
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor getBillDetailForClient() cmd={}",ownerId,billId,targetType,organizationId,e);
		}
		//return response;
		return null;
	}
    
}
