package com.everhomes.asset.third;

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
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.contract.ContractApplicationScene;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.user.UserContext;
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
	
	/**
	 * 物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单） 
	 */
	public ListBillsResponse listOpenBills(ListBillsCommand cmd) {
    	LOGGER.info("AssetBillServiceImpl listOpenBills sourceCmd={}", cmd.toString());
    	//写死中天的域空间ID
    	cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
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
        //组装账单费项明细
        for(ListBillsDTO dto : list) {
        	setBillInvalidParamNull(dto);//屏蔽账单无效参数
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
		dto.setDeleteFlag(null);
	}

}
