
package com.everhomes.asset.bill;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.contract.ContractCategory;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.recommend.DateTimeUtils;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.BatchDeleteBillCommand;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractCmd;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractDTO;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillDTO;
import com.everhomes.rest.asset.bill.DeleteContractBillFlag;
import com.everhomes.rest.asset.bill.ListBatchDeleteBillFromContractResponse;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.bill.ListCheckContractIsProduceBillResponse;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.contract.ContractApplicationScene;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.DateUtils;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午4:09:23
 */
@Component
public class AssetBillServiceImpl implements AssetBillService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetBillServiceImpl.class);
	
	@Autowired
	private AssetBillProvider assetBillProvider;
	
	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired
	private PortalService portalService;
	
	@Autowired
	private ContractProvider contractProvider;

	//缴费V7.3(账单组规则定义)：批量删除“非已缴”账单接口
	public String batchDeleteBill(BatchDeleteBillCommand cmd) {
		String result = "OK";
        assetBillProvider.batchDeleteBill(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getBillIdList());
        return result;
	}

	//校验合同是否产生已缴账单接口（合同模块调用）
	public ListCheckContractIsProduceBillResponse checkContractIsProduceBill(CheckContractIsProduceBillCmd cmd) {
		ListCheckContractIsProduceBillResponse response = new ListCheckContractIsProduceBillResponse();
		List<CheckContractIsProduceBillDTO> list = new ArrayList<CheckContractIsProduceBillDTO>();
		Integer namespaceId = cmd.getNamespaceId();
	    String ownerType = cmd.getOwnerType();
	    Long ownerId = cmd.getOwnerId();
	    List<Long> contractIdList = cmd.getContractIdList();
	    for(Long contractId : contractIdList) {
	    	CheckContractIsProduceBillDTO dto = new CheckContractIsProduceBillDTO();
	    	Byte paymentStatus = assetBillProvider.checkContractIsProduceBill(namespaceId, ownerType, ownerId, contractId);
	    	dto.setContractId(contractId);
	    	dto.setPaymentStatus(paymentStatus);
	    	list.add(dto);
	    }
		response.setList(list);
		return response;
	}

	public ListBatchDeleteBillFromContractResponse batchDeleteBillFromContract(BatchDeleteBillFromContractCmd cmd) {
		ListBatchDeleteBillFromContractResponse response = new ListBatchDeleteBillFromContractResponse();
		List<BatchDeleteBillFromContractDTO> list = new ArrayList<BatchDeleteBillFromContractDTO>();
		Integer namespaceId = cmd.getNamespaceId();
	    String ownerType = cmd.getOwnerType();
	    Long ownerId = cmd.getOwnerId();
	    List<Long> contractIdList = cmd.getContractIdList();
	    for(Long contractId : contractIdList) {
	    	BatchDeleteBillFromContractDTO dto = new BatchDeleteBillFromContractDTO();
	    	dto.setContractId(contractId);
	    	//校验合同是否产生已缴账单接口
	    	Byte paymentStatus = assetBillProvider.checkContractIsProduceBill(namespaceId, ownerType, ownerId, contractId);
	    	dto.setPaymentStatus(paymentStatus);
	    	if(paymentStatus.equals(AssetPaymentBillStatus.PAID.getCode())) {
	    		//如果已经产生已缴账单，那么不允许合同删除相关账单，需走合同变更
	    		dto.setDeleteSuccess(DeleteContractBillFlag.FAIL.getCode());
	    	}else {
	    		dto.setDeleteSuccess(DeleteContractBillFlag.SUCCESS.getCode());
	    		//删除合同产生的相关账单、费项明细数据
	    		assetBillProvider.deleteBillFromContract(namespaceId, ownerType, ownerId, contractId);
	    	}
	    	list.add(dto);
	    }
		response.setList(list);
		return response;
	}

	/**
	 * 物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单） 
	 */
	public ListBillsResponse listOpenBills(ListBillsCommand cmd) {
    	LOGGER.info("AssetBillServiceImpl listOpenBills sourceCmd={}", cmd.toString());
    	//写死中天的域空间ID
    	cmd.setNamespaceId(999944);
    	cmd.setOwnerType("community");
    	cmd.setOwnerId(cmd.getCommunityId());
    	cmd.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
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
        //每页大小(最大值为1000)，每次请求获取的数据条数
        if(pageSize > 1000) {
        	pageSize = 1000;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillsDTO> list = assetProvider.listBills(cmd.getNamespaceId(), pageOffSet, pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor + pageSize.longValue());
            list.remove(list.size() - 1);
        }
        //每次同步都要打印下billId，方便对接过程中出现问题好进行定位
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
        
        response.setListBillsDTOS(list);
        return response;
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
	
}
