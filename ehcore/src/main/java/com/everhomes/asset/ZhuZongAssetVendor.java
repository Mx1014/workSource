//@formatter:off
package com.everhomes.asset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.BillDTO;
import com.everhomes.rest.asset.BillForClientV2;
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
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.PlaceAnAssetOrderCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.listBillRelatedTransacCommand;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.zhuzong.CostByHouseListDTO;
import com.everhomes.rest.asset.zhuzong.CostDTO;
import com.everhomes.rest.asset.zhuzong.CostDetailDTO;
import com.everhomes.rest.asset.zhuzong.HouseDTO;
import com.everhomes.rest.asset.zhuzong.QueryCostByHouseListDTO;
import com.everhomes.rest.asset.zhuzong.QueryCostDetailByIDDTO;
import com.everhomes.rest.asset.zhuzong.QueryHouseByPhoneNumberDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;


/**
 * Created by chongxin yang on 2018/04/19.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZZ")
public class ZhuZongAssetVendor extends AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuZongAssetVendor.class);
    
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
	@Autowired
    private RolePrivilegeService rolePrivilegeService;


	
	/**
	 * 根据手机号返回房产
	 */
	private List<HouseDTO> queryHouseByPhoneNumber(List<String> phoneNumbers) {
		List<HouseDTO> houseDTOs = new ArrayList<HouseDTO>();
		try {
			String url = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_QUERYHOUSEBYPHONENUMBER_URL,""); 
			String AccountCode = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_ACCOUNTCODE,""); 
			if(phoneNumbers != null) {
				for(int i = 0;i < phoneNumbers.size();i++) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("AccountCode", AccountCode);
					params.put("phone", phoneNumbers.get(i));
					String response = HttpUtils.post(url, params, 600, false);
					try {
						QueryHouseByPhoneNumberDTO queryHouseByPhoneNumberDTO = 
								(QueryHouseByPhoneNumberDTO) StringHelper.fromJsonString(response, QueryHouseByPhoneNumberDTO.class);
						houseDTOs.addAll(queryHouseByPhoneNumberDTO.getResult());
					} catch (Exception e) {
						LOGGER.error("ZhuZongAssetVendor queryHouseByPhoneNumber() {}", phoneNumbers.get(i), e);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor queryHouseByPhoneNumber() {}", e);
		}
		return houseDTOs;
	}
	
	/**
	 * 根据房屋查询费用
	 * onlyws 是	0：全部费用；1：未收；2：已收
	 */
	private List<CostDTO> queryCostByHouseList(String houseid, String clientid, String onlyws) {
		List<CostDTO> costDTOs = new ArrayList<CostDTO>();
		try {
			String url = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_QUERYCOSTBYHOUSELIST_URL,""); 
			String AccountCode = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_ACCOUNTCODE,""); 
			Map<String, String> params = new HashMap<String, String>();
			params.put("AccountCode", AccountCode);
			params.put("houseid", houseid);
			params.put("clientid", clientid);
			params.put("onlyws", onlyws);
			params.put("pageOprator", "next");
			params.put("currenpage", "0");//从首页开始查询
			String response = HttpUtils.post(url, params, 600, false);
			QueryCostByHouseListDTO queryCostByHouseListDTO = 
					(QueryCostByHouseListDTO) StringHelper.fromJsonString(response, QueryCostByHouseListDTO.class);
			CostByHouseListDTO costByHouseListDTO = queryCostByHouseListDTO.getResult();
			costDTOs = costByHouseListDTO.getDatas();
			Integer totalpage = costByHouseListDTO.getTotalpage();//获取总页数
			for(int currentpage = 1;currentpage < totalpage;currentpage++) {
				params.put("currenpage", new Integer(currentpage).toString());
				response = HttpUtils.post(url, params, 600, false);
				queryCostByHouseListDTO = 
						(QueryCostByHouseListDTO) StringHelper.fromJsonString(response, QueryCostByHouseListDTO.class);
				costByHouseListDTO = queryCostByHouseListDTO.getResult();
				costDTOs.addAll(costByHouseListDTO.getDatas());
			}
			//把账单按照账期开始日期进行倒序排序
			if(costDTOs != null) {
				Collections.sort(costDTOs, new Comparator<CostDTO>() {
					public int compare(CostDTO c1, CostDTO c2) {
						if(c1.getBegintime().compareTo(c2.getBegintime()) > 0) {
							return -1;
						}
						if(c1.getBegintime().compareTo(c2.getBegintime()) < 0) {
							return 1;
						}
						return 0;
					}
				});
			}
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor queryCostByHouseList() {}", houseid, clientid, onlyws, e);
		}
		return costDTOs;
	}
	
	/**
	 * 查询费用明细
	 */
	private List<CostDetailDTO> queryCostDetailByID(String feeid) {
		List<CostDetailDTO> costDetailDTOs = new ArrayList<CostDetailDTO>();
		try {
			String url = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_QUERYCOSTDETAILBYID_URL,""); 
			String AccountCode = configurationProvider.getValue(999955, ConfigConstants.ASSET_ZHUZONG_ACCOUNTCODE,""); 
			Map<String, String> params = new HashMap<String, String>();
			params.put("AccountCode", AccountCode);
			params.put("feeid", feeid);
			String response = HttpUtils.post(url, params, 600, false);
			QueryCostDetailByIDDTO queryCostDetailByIDDTO = 
					(QueryCostDetailByIDDTO) StringHelper.fromJsonString(response, QueryCostDetailByIDDTO.class);
			costDetailDTOs = queryCostDetailByIDDTO.getResult();
		} catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor queryCostDetailByID() {}", feeid, e);
		}
		return costDetailDTOs;
	}
	
	/**
	 * 组装ListAllBillsForClientDTO
	 */
	private ListAllBillsForClientDTO assembleListAllBillsForClientDTO(CostDTO costDTO, Byte chargeStatus) {
		ListAllBillsForClientDTO listAllBillsForClientDTO = new ListAllBillsForClientDTO();
		listAllBillsForClientDTO.setBillGroupName(costDTO.getFeename());
		listAllBillsForClientDTO.setAmountOwed(costDTO.getWs_amount() != null ? costDTO.getWs_amount().toString() : "");//待缴金额
		listAllBillsForClientDTO.setAmountReceivable(costDTO.getAmount() != null ? costDTO.getAmount().toString() : "");//应缴金额
		listAllBillsForClientDTO.setDateStrBegin(costDTO.getBegintime() != null ? costDTO.getBegintime().toString() : "");
		listAllBillsForClientDTO.setDateStrEnd(costDTO.getEndtime() != null ? costDTO.getEndtime().toString() : "");
		String billDuration = costDTO.getBegintime() + "~" + costDTO.getEndtime();
		listAllBillsForClientDTO.setDateStr(billDuration);
		listAllBillsForClientDTO.setChargeStatus(chargeStatus);//chargeStatus：0：未付款；1：已付款
		listAllBillsForClientDTO.setBillId(costDTO.getFeeid());
		return listAllBillsForClientDTO;
	}
	
	/**
	 * 个人直接获取手机号码，企业获取所有企业管理员的手机号码
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
		List<ShowBillForClientV2DTO> response = new ArrayList<ShowBillForClientV2DTO>();
		try {
			//个人直接获取手机号码，企业获取所有企业管理员的手机号码
			List<String> phoneNumbers = getPhoneNumber(cmd.getTargetType(), cmd.getTargetId(), cmd.getNamespaceId());
			//phoneNumbers.add("13810281614");//杨崇鑫测试
			//根据手机号返回房产
			List<HouseDTO> houseDTOs = queryHouseByPhoneNumber(phoneNumbers);
			String houseid = "",clientid = "";
			if(houseDTOs != null) {
				for(int i = 0 ;i < houseDTOs.size();i++){
					houseid += houseDTOs.get(i).getPk_house() + ",";//房屋ID如果是多个以英文逗号隔开
					clientid += houseDTOs.get(i).getPk_client() + ",";//业户ID如果是多个以英文逗号隔开
				}
			}
			String onlyws = "1";//首页只查询未缴费:0：全部费用；1：未收；2：已收
			//根据房屋查询费用
			List<CostDTO> costDTOs = queryCostByHouseList(houseid, clientid, onlyws);
			if(costDTOs != null) {
				ShowBillForClientV2DTO showBillForClientV2DTO = new ShowBillForClientV2DTO();
				showBillForClientV2DTO.setBillGroupName("待缴账单");
				showBillForClientV2DTO.setContractId("      ");
				showBillForClientV2DTO.setContractNum("       ");
				BigDecimal overAllAmountOwed = BigDecimal.ZERO;//待缴金额总计
				String addressStr = "";//包含的地址
				List<BillForClientV2> bills = new ArrayList<BillForClientV2>();
				for(int i = 0;i < costDTOs.size();i++) {
					CostDTO costDTO = costDTOs.get(i);
					BillForClientV2 bill = new BillForClientV2();
					bill.setBillId(costDTO.getFeeid());
					bill.setAmountOwed(costDTO.getWs_amount() != null ? costDTO.getWs_amount().toString() : "");//待缴金额
					bill.setAmountReceivable(costDTO.getAmount() != null ? costDTO.getAmount().toString() : "");//应缴金额
					String billDuration = costDTO.getBegintime() + "~" + costDTO.getEndtime();
					bill.setBillDuration(billDuration);//账期
					bills.add(bill);
					//账单组包含的所有地址（去除重复地址）
					if(!addressStr.contains(costDTO.getHousename())){
						addressStr += costDTO.getHousename() + "," ;//包含的地址
					}
					overAllAmountOwed = overAllAmountOwed.add(costDTO.getWs_amount());//待缴金额总计
				}
				showBillForClientV2DTO.setBills(bills);
				showBillForClientV2DTO.setOverAllAmountOwed(String.valueOf(overAllAmountOwed));
				addressStr = addressStr.substring(0, addressStr.length() - 1);
				showBillForClientV2DTO.setAddressStr(addressStr);
				response.add(showBillForClientV2DTO);
			}
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor showBillForClientV2() cmd={}", cmd, e);
		}
		return response;
	}
	
	@Override
	public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
		List<ListAllBillsForClientDTO> response = new ArrayList<ListAllBillsForClientDTO>();
		try {
			//个人直接获取手机号码，企业获取所有企业管理员的手机号码
			List<String> phoneNumbers = getPhoneNumber(cmd.getTargetType(), cmd.getTargetId(), cmd.getNamespaceId());
			//phoneNumbers.add("13810281614");//杨崇鑫测试
			//根据手机号返回房产
			List<HouseDTO> houseDTOs = queryHouseByPhoneNumber(phoneNumbers);
			String houseid = "",clientid = "";
			if(houseDTOs != null) {
				for(int i = 0 ;i < houseDTOs.size();i++){
					houseid += houseDTOs.get(i).getPk_house() + ",";//房屋ID如果是多个以英文逗号隔开
					clientid += houseDTOs.get(i).getPk_client() + ",";//业户ID如果是多个以英文逗号隔开
				}
			}
			//由于对接查询全部没有字段用于区分是“待支付”还是“已支付”，所以第一次查询所有未缴，第二次查询所有已缴，再作相加
			String onlyws = "1";//0：全部费用；1：未收；2：已收
			//根据房屋查询费用
			List<CostDTO> costDTOs = queryCostByHouseList(houseid, clientid, onlyws);
			if(costDTOs != null) {
				for(int i = 0;i < costDTOs.size();i++) {
					CostDTO costDTO = costDTOs.get(i);
					//chargeStatus：0：未付款；1：已付款
					ListAllBillsForClientDTO listAllBillsForClientDTO = assembleListAllBillsForClientDTO(costDTO, Byte.valueOf("0"));
					response.add(listAllBillsForClientDTO);
				}
			}
			onlyws = "2";//0：全部费用；1：未收；2：已收
			//根据房屋查询费用
			costDTOs = queryCostByHouseList(houseid, clientid, onlyws);
			if(costDTOs != null) {
				for(int i = 0;i < costDTOs.size();i++) {
					CostDTO costDTO = costDTOs.get(i);
					//chargeStatus：0：未付款；1：已付款
					ListAllBillsForClientDTO listAllBillsForClientDTO = assembleListAllBillsForClientDTO(costDTO, Byte.valueOf("1"));
					response.add(listAllBillsForClientDTO);
				}
			}
			//把账单按照账期开始日期进行倒序排序
			if(response != null) {
				Collections.sort(response, new Comparator<ListAllBillsForClientDTO>() {
					public int compare(ListAllBillsForClientDTO c1, ListAllBillsForClientDTO c2) {
						if(c1.getDateStrBegin().compareTo(c2.getDateStrBegin()) > 0) {
							return -1;
						}
						if(c1.getDateStrBegin().compareTo(c2.getDateStrBegin()) < 0) {
							return 1;
						}
						return 0;
					}
				});
			}
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor listAllBillsForClient() cmd={}", cmd, e);
		}
		return response;
	}
	
	@Override
	public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId, String targetType,Long organizationId) {
		ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
		try {
			List<CostDetailDTO> costDetailDTOs = queryCostDetailByID(billId);
			if(costDetailDTOs != null) {
				CostDetailDTO costDetailDTO = costDetailDTOs.get(0);//住总ELive通过账单ID只会返回一个对应的收费细项
				String dateStr = costDetailDTO.getBeginTime() + "~" + costDetailDTO.getEndTime();//账期
				List<ShowBillDetailForClientDTO> showBillDetailForClientDTOList = new ArrayList<ShowBillDetailForClientDTO>();
				ShowBillDetailForClientDTO showBillDetailForClientDTO = new ShowBillDetailForClientDTO();
				showBillDetailForClientDTO.setBillItemName(costDetailDTO.getFeename());
				showBillDetailForClientDTO.setAmountOwed(costDetailDTO.getWs_amount());//待缴金额
				showBillDetailForClientDTO.setAddressName(costDetailDTO.getHouseName());
				showBillDetailForClientDTO.setAmountReceivable(costDetailDTO.getAmount());//应收金额
				showBillDetailForClientDTO.setDateStrBegin(costDetailDTO.getBeginTime());
				showBillDetailForClientDTO.setDateStrEnd(costDetailDTO.getEndTime());
				showBillDetailForClientDTO.setDateStr(dateStr);
				showBillDetailForClientDTOList.add(showBillDetailForClientDTO);
				response.setShowBillDetailForClientDTOList(showBillDetailForClientDTOList);
				response.setDatestr(dateStr);
				response.setAmountOwed(costDetailDTO.getWs_amount());
			}
		}catch (Exception e) {
			LOGGER.error("ZhuZongAssetVendor getBillDetailForClient() cmd={}", ownerId, billId, targetType, organizationId, e);
		}
		return response;
	}
	
    
}
