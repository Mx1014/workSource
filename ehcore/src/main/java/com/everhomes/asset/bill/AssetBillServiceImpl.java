
package com.everhomes.asset.bill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.bill.BatchDeleteBillCommand;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillDTO;
import com.everhomes.rest.asset.bill.ListCheckContractIsProduceBillResponse;

/**
 * @author created by ycx
 * @date 下午4:09:23
 */
@Component
public class AssetBillServiceImpl implements AssetBillService {
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private AssetBillProvider assetBillProvider;

	//缴费V7.3(账单组规则定义)：批量删除“非已缴”账单接口
	public String batchDeleteBill(BatchDeleteBillCommand cmd) {
		//assetService.checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_DELETE,cmd.getOrganizationId());
		String result = "OK";
        assetBillProvider.batchDeleteBill(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getBillIdList());
        return result;
	}

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
	

	
}
