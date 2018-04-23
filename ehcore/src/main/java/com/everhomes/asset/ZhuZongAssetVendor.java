//@formatter:off
package com.everhomes.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.BillDTO;
import com.everhomes.rest.asset.BillIdAndType;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.BillItemIdCommand;
import com.everhomes.rest.asset.BillStaticsCommand;
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.ExemptionItemIdCommand;
import com.everhomes.rest.asset.ExportBillTemplatesCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentResponse;
import com.everhomes.rest.asset.GetAreaAndAddressByContractCommand;
import com.everhomes.rest.asset.GetAreaAndAddressByContractDTO;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListBillDetailCommand;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillExpectanciesOnContractCommand;
import com.everhomes.rest.asset.ListBillItemsResponse;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.PlaceAnAssetOrderCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.zhuzong.HouseDTO;
import com.everhomes.rest.asset.zhuzong.QueryHouseByPhoneNumberDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.user.UserIdentifierDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;


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
    @Autowired
    private ConfigurationProvider configurationProvider;
	@Autowired
    private RolePrivilegeService rolePrivilegeService;

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
	
	/**
	 * 根据手机号返回房产
	 * @param phoneNumbers
	 * @return
	 */
	private List<HouseDTO> queryHouseByPhoneNumber(List<String> phoneNumbers) {
		List<HouseDTO> houseDTOs = new ArrayList<HouseDTO>();
		try {
			String url = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_QUERYHOUSEBYPHONENUMBER_URL,""); 
			String AccountCode = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_ACCOUNTCODE,""); 
			for(int i = 0;i < phoneNumbers.size();i++) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("AccountCode", AccountCode);
				params.put("phone", phoneNumbers.get(i));
				String response = HttpUtils.post(url, params, 600, false);
				QueryHouseByPhoneNumberDTO queryHouseByPhoneNumberDTO = (QueryHouseByPhoneNumberDTO) StringHelper.fromJsonString(response, QueryHouseByPhoneNumberDTO.class);
				houseDTOs.addAll(queryHouseByPhoneNumberDTO.getResult());
			}
		} catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor queryHouseByPhoneNumber() {}", phoneNumbers, e);
		}
		return houseDTOs;
	}
	
	/**
	 * 个人直接获取手机号码，企业获取所有企业管理员的手机号码
	 * @param targetType
	 * @param targetId
	 * @param namespaceId
	 * @return
	 */
	private List<String> getPhoneNumber(String targetType, Long targetId, Integer namespaceId){
		List<String> phoneNumbers = new ArrayList<String>(); 
		try {
			if (targetType.equals(AssetTargetType.USER.getCode())) {
				UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(UserContext.currentUserId(), namespaceId);
				if(userIdentifier != null) {
					phoneNumbers.add(userIdentifier.getIdentifierToken());
				}
		    }else if(targetType.equals(AssetTargetType.ORGANIZATION.getCode())) {
		    	ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
				cmd.setOrganizationId(targetId);
				cmd.setActivationFlag((byte)1);
				cmd.setOwnerType("EhOrganizations");
				cmd.setOwnerId(null);
				//List<OrganizationContactDTO> lists = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);//获取超级管理员
	            //LOGGER.info("organization manager check for bill display, cmd = {}", cmd.toString());
	            List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd);//获取企业管理员
	            LOGGER.info("organization manager check for bill display, orgContactsDTOs are = "+ organizationContactDTOS.toString());
	            for(OrganizationContactDTO dto : organizationContactDTOS){
	            	UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(dto.getTargetId(), namespaceId);
	            	if(userIdentifier != null) {
	    				phoneNumbers.add(userIdentifier.getIdentifierToken());
	    			}
	            }
		    }
		} catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor getPhoneNumber() {}", targetType, targetId, namespaceId, e);
		}
		return phoneNumbers;
	}
	
	@Override
	public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
		try {
			//个人直接获取手机号码，企业获取所有企业管理员的手机号码
			List<String> phoneNumbers = getPhoneNumber(cmd.getTargetType(), cmd.getTargetId(), cmd.getNamespaceId());
			phoneNumbers.add("15650723221");//用于测试的手机号码，杨崇鑫
			List<HouseDTO> houseDTOs = queryHouseByPhoneNumber(phoneNumbers);
			
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor showBillForClientV2() cmd={}", cmd, e);
		}
		//return response;
		return null;
	}
	
	@Override
	public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
		try {
			
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor listAllBillsForClient() cmd={}", cmd, e);
		}
		//return response;
		return null;
	}
	
	@Override
	public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType,Long organizationId) {
		try {
			
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor getBillDetailForClient() cmd={}", ownerId, billId, targetType, organizationId, e);
		}
		//return response;
		return null;
	}
    
}
