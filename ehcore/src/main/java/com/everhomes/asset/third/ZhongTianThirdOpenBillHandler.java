package com.everhomes.asset.third;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.PaymentBills;
import com.everhomes.asset.bill.AssetBillProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contract.ContractCategory;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.ChangeChargeStatusCommand;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.contract.ContractApplicationScene;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 2018年11月12日 下午8:02:38
 */
@Component(ThirdOpenBillHandler.THIRDOPENBILL_PREFIX + 999944)
public class ZhongTianThirdOpenBillHandler implements ThirdOpenBillHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZhongTianThirdOpenBillHandler.class);

	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired
	private PortalService portalService;
	
	@Autowired
	private ContractProvider contractProvider;
	
	@Autowired
	private AssetBillProvider assetBillProvider;
	
	/**
	 * 物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单） 
	 */
	public ListBillsResponse listOpenBills(ListBillsCommand cmd) {
    	LOGGER.info("AssetBillServiceImpl listOpenBills sourceCmd={}", cmd.toString());
    	cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
    	cmd.setOwnerType("community");
    	cmd.setOwnerId(cmd.getCommunityId());
    	//物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单），因为是同步账单，所以已出、未出都要同步
    	List<Byte> switchList = new ArrayList<Byte>();
    	switchList.add(new Byte("0"));
    	switchList.add(new Byte("1"));
    	cmd.setSwitchList(switchList);
    	//只传租赁账单
    	Long assetCategoryId = getRentalAssetCategoryId(cmd.getNamespaceId());
    	cmd.setCategoryId(assetCategoryId);
    	LOGGER.info("AssetBillServiceImpl listOpenBills convertCmd={}", cmd.toString());
        ListBillsResponse response = new ListBillsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        //每页大小(最大值为50)，每次请求获取的数据条数
        if(pageSize > 50) {
        	pageSize = 50;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillsDTO> list = assetProvider.listBills(cmd.getNamespaceId(), pageOffSet, pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor + pageSize.longValue());
            list.remove(list.size() - 1);
        }
        //组装账单费项明细
        for(ListBillsDTO dto : list) {
        	setBillInvalidParamNull(dto);//屏蔽账单无效参数
        	ListBillDetailResponse res = assetBillProvider.listOpenBillDetail(Long.valueOf(dto.getBillId()));
        	if(res != null && res.getBillGroupDTO() != null) {
        		List<BillItemDTO> billItemDTOList = res.getBillGroupDTO().getBillItemDTOList();
        		for(BillItemDTO billItemDTO : billItemDTOList) {
        			setItemInvalidParamNull(billItemDTO);//屏蔽费项无效参数
        		}
        		dto.setBillItemDTOList(billItemDTOList);
        	}
        }
        //每次同步都要打印下billId，方便对接过程中出现问题好进行定位
        printBillId(list);
        response.setListBillsDTOS(list);
        return response;
	}
	
	/**
	 * 每次同步都要打印下billId，方便对接过程中出现问题好进行定位
	 */
	public void printBillId(List<ListBillsDTO> list) {
		StringBuilder billIdStringBuilder = new StringBuilder();
        billIdStringBuilder.append("(");
        for(ListBillsDTO dto : list) {
        	billIdStringBuilder.append(dto.getBillId());
        	billIdStringBuilder.append(", ");
        }
        //去掉最后一个逗号
        if(billIdStringBuilder.length() >= 2) {
        	billIdStringBuilder = billIdStringBuilder.deleteCharAt(billIdStringBuilder.length() - 2);
        }
        billIdStringBuilder.append(")");
        LOGGER.info("AssetBillServiceImpl listOpenBills billIds={}", billIdStringBuilder);
	}
	
	/**
	 * 获取租赁缴费的categoryId
	 * 1、获取所有缴费的categoryId
	 * 2、根据缴费的categoryId找出对应合同的categoryId
	 * 3、根据合同的categoryId调用接口判断是否是租赁合同
	*/
	public Long getRentalAssetCategoryId(Integer namespaceId) {
		Long categoryId = null;
		ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
  		cmd.setNamespaceId(namespaceId);
  		cmd.setModuleId(PrivilegeConstants.ASSET_MODULE_ID);
  		//1、获取所有缴费的categoryId
  		ListServiceModuleAppsResponse response = portalService.listServiceModuleApps(cmd);
  		List<ServiceModuleAppDTO> serviceModuleApps = response.getServiceModuleApps();
  		for(ServiceModuleAppDTO serviceModuleAppDTO : serviceModuleApps) {
  			String instanceConfig = serviceModuleAppDTO.getInstanceConfig();
  			AssetInstanceConfigDTO assetInstanceConfigDTO = (AssetInstanceConfigDTO) StringHelper.fromJsonString(instanceConfig, AssetInstanceConfigDTO.class);
  			if(assetInstanceConfigDTO != null && assetInstanceConfigDTO.getCategoryId() != null) {
  				//2、根据缴费的categoryId找出对应合同的categoryId
  				Long moduleId = PrivilegeConstants.ASSET_MODULE_ID;
  				Long targetModuleId = PrivilegeConstants.CONTRACT_MODULE;
  				Long originId = assetInstanceConfigDTO.getCategoryId();
  				Long contractCategoryId = assetProvider.getOriginIdFromMappingApp(moduleId, originId, targetModuleId, namespaceId);
  				//3、根据合同的categoryId调用接口判断是否是租赁合同
  				ContractCategory contractCategory = contractProvider.findContractCategoryById(contractCategoryId);
  				if(contractCategory != null && contractCategory.getContractApplicationScene() != null) {
  					if(contractCategory.getContractApplicationScene() != ContractApplicationScene.PROPERTY.getCode()) {
  						return assetInstanceConfigDTO.getCategoryId();
  					}
  				}
  			}
  		}
    	return categoryId;
	}
	
	/**
	 * 屏蔽账单无效参数
	 */
	private void setBillInvalidParamNull(ListBillsDTO dto) {
		dto.setDateStr(null);
		dto.setBillGroupName(null);
		dto.setTargetName(null);
		dto.setTargetId(null);
		dto.setTargetType(null);
		dto.setNoticeTimes(null);
		dto.setOwnerId(null);
		dto.setOwnerType(null);
		dto.setSourceId(null);
		dto.setSourceType(null);
		dto.setSourceName(null);
		dto.setCanDelete(null);
		dto.setCanModify(null);
		dto.setIsReadOnly(null);
		dto.setNoticeTelList(null);
	}
	
	/**
	 * 屏蔽费项无效参数
	 */
	private void setItemInvalidParamNull(BillItemDTO dto) {
		dto.setDateStr(null);
		dto.setSourceId(null);
		dto.setSourceType(null);
		dto.setSourceName(null);
		dto.setCanDelete(null);
		dto.setCanModify(null);
		dto.setBillId(null);
		dto.setChargingItemsId(null);
		dto.setItemFineType(null);
		dto.setItemType(null);
	}
	
	/**
	 * EAS系统收到款项录入凭证，将收款状态回传至左邻
	 * 总体原则：不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付
	 */
	public ListBillsDTO changeChargeStatus(ChangeChargeStatusCommand cmd) {
		ListBillsDTO dto = new ListBillsDTO();
		if(cmd.getBillId() != null) {
			//如果账单已经在左邻支付，那么EAS调用左邻的收款回传接口是无效调用，已支付的数据以左邻为准
			PaymentBills bill = assetProvider.findBillById(cmd.getBillId());
	        if(bill != null && bill.getStatus().equals(AssetPaymentBillStatus.PAID.getCode()))  {
	            if(LOGGER.isInfoEnabled()) {
	                LOGGER.info("Bill orders have been paid, billId={}", cmd.getBillId());
	            }
	            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE, AssetErrorCodes.HAS_PAID_BILLS, "Bills have been paid");
	        }
	        //重新计算待收 = 应收 - 已收
	        BigDecimal amountOwed = bill.getAmountReceivable().subtract(cmd.getAmountReceived());
	        amountOwed = amountOwed.setScale(2, BigDecimal.ROUND_HALF_UP);
			assetBillProvider.changeChargeStatus(UserContext.getCurrentNamespaceId(), cmd.getBillId(), cmd.getAmountReceived(), amountOwed, cmd.getPaymentType());
			dto.setAmountReceivable(bill.getAmountReceivable());
			dto.setAmountReceived(cmd.getAmountReceived());
			dto.setAmountOwed(bill.getAmountOwed());
		}else {
			LOGGER.info("/openapi/asset/changeChargeStatus billId can not be null!");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "/openapi/asset/changeChargeStatus billId can not be null!");
		}
		return dto;
	}

}
