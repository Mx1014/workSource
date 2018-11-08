
package com.everhomes.asset.bill;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProviderImpl;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.AssetVendor;
import com.everhomes.asset.AssetVendorHandler;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
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
import com.everhomes.rest.asset.bill.ListOpenBillsCommand;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.user.UserContext;

/**
 * @author created by ycx
 * @date 下午4:09:23
 */
@Component
public class AssetBillServiceImpl implements AssetBillService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetBillServiceImpl.class);
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private AssetBillProvider assetBillProvider;

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
	public ListBillsResponse listOpenBills(ListOpenBillsCommand cmd) {
    	LOGGER.info("AssetBillServiceImpl listOpenBills cmd={}", cmd.toString());
        ListBillsResponse response = new ListBillsResponse();
        AssetVendor assetVendor = assetService.checkAssetVendor(cmd.getNamespaceId(), 0);
        String vender = assetVendor.getVendorName();
        AssetVendorHandler handler = assetService.getAssetVendorHandler(vender);
        List<ListBillsDTO> list = handler.listOpenBills(cmd);
        response.setListBillsDTOS(list);
        return response;
	}
	
	
	
}
