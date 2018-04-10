//@formatter:off
package com.everhomes.asset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.szywyjf.SZYQuery;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy;
import com.everhomes.asset.szywyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator;
import com.everhomes.asset.zjgkVOs.*;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.order.PayService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.BillDetailDTO;

import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;

import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import org.springframework.context.ApplicationContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by chongxin yang on 2018/04/08.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"SZY")
public class ShenZhenWanAssetVendor implements AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShenZhenWanAssetVendor.class);
    
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
	public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType) {
		JSONObject jsonObject = new JSONObject();
		//企业客户，按名称查询，个人按电话号码查询
		String cusName = null;
		String type = null;
		if (targetType.equals(AssetTargetType.USER.getCode())) {
	    	Long targetId = UserContext.currentUserId();
	    	Integer namespaceId = UserContext.getCurrentNamespaceId();
	    	cusName = userProvider.findUserIdentifiersOfUser(targetId, namespaceId).getIdentifierToken();
	    	//0：企业客户，1：个人（判断）
	    	type = "1";
	    }else if(targetType.equals(AssetTargetType.ORGANIZATION.getCode())) {
	    	Long orgId = UserContext.currentUserId();
	    	cusName = organizationProvider.findOrganizationById(orgId).getName();
	    	//0：企业客户，1：个人（判断）
	    	type = "0";
	    }
		jsonObject.put("cusName", cusName);
		//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
		jsonObject.put("startDate", "1900-01-01");
		jsonObject.put("endDate", "2900-01-01");
		jsonObject.put("type", type);
		//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
		jsonObject.put("state", "2");
		jsonObject.put("fid", billId);
		//通过WebServices接口查询数据
		SZYQuery szyQuery = new SZYQuery();
		ShowBillDetailForClientResponse response = szyQuery.getBillDetailForClient(jsonObject.toString());
		return response;
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
	public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
		JSONObject jsonObject = new JSONObject();
		//企业客户，按名称查询，个人按电话号码查询
		String cusName = null;
		String type = null;
		if (cmd.getTargetType().equals(AssetTargetType.USER.getCode())) {
	    	cmd.setTargetId(UserContext.currentUserId());
	    	cusName = userProvider.findUserIdentifiersOfUser(cmd.getTargetId(), cmd.getNamespaceId()).getIdentifierToken();
	    	//0：企业客户，1：个人（判断）
	    	type = "1";
	    }else if(cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
	    	Long orgId = cmd.getTargetId();
	    	cusName = organizationProvider.findOrganizationById(orgId).getName();
	    	//0：企业客户，1：个人（判断）
	    	type = "0";
	    }
		jsonObject.put("cusName", cusName);
		//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
		jsonObject.put("startDate", "1900-01-01");
		jsonObject.put("endDate", "2900-01-01");
		jsonObject.put("type", type);
		//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
		jsonObject.put("state", "0");
		//通过WebServices接口查询数据
		SZYQuery szyQuery = new SZYQuery();
		List<ShowBillForClientV2DTO> response = szyQuery.showBillForClientV2(jsonObject.toString());
		return response;
	}
	
	@Override
	public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
		JSONObject jsonObject = new JSONObject();
		//企业客户，按名称查询，个人按电话号码查询
		String cusName = null;
		String type = null;
		if (cmd.getTargetType().equals(AssetTargetType.USER.getCode())) {
	    	cmd.setTargetId(UserContext.currentUserId());
	    	cusName = userProvider.findUserIdentifiersOfUser(cmd.getTargetId(), cmd.getNamespaceId()).getIdentifierToken();
	    	//0：企业客户，1：个人（判断）
	    	type = "1";
	    }else if(cmd.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
	    	Long orgId = cmd.getTargetId();
	    	cusName = organizationProvider.findOrganizationById(orgId).getName();
	    	//0：企业客户，1：个人（判断）
	    	type = "0";
	    }
		jsonObject.put("cusName", cusName);
		//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
		jsonObject.put("startDate", "1900-01-01");
		jsonObject.put("endDate", "2900-01-01");
		jsonObject.put("type", type);
		//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
		jsonObject.put("state", "0");
		//通过WebServices接口查询数据
		SZYQuery szyQuery = new SZYQuery();
		List<ListAllBillsForClientDTO> response = szyQuery.listAllBillsForClient(jsonObject.toString(), Byte.valueOf("0"));
		//由于对接查询全部没有字段用于区分是“待支付”还是“已支付”，所以第一次查询所有未缴，第二次查询所有已缴，再作相加
		jsonObject.put("state", "1");
		response.addAll(szyQuery.listAllBillsForClient(jsonObject.toString(), Byte.valueOf("1")));
		return response;
	}
    
}
