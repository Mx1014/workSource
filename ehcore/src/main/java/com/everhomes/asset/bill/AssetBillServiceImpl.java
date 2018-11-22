
package com.everhomes.asset.bill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.PaymentBills;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.bill.BatchDeleteBillCommand;
import com.everhomes.rest.asset.bill.BatchDeleteBillDTO;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractCmd;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractDTO;
import com.everhomes.rest.asset.bill.BatchDeleteBillResponse;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillDTO;
import com.everhomes.rest.asset.bill.DeleteContractBillFlag;
import com.everhomes.rest.asset.bill.ListBatchDeleteBillFromContractResponse;
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
	
	@Autowired
	private AssetProvider assetProvider;

	/**
	 * 缴费V7.3(账单组规则定义)：批量删除“非已缴”账单接口
	 * 批量删除只支持删除只支持：手动新增 、 批量导入 、合同产生的账单
	 * 第三方或对接其他模块不管是已缴还是未缴都不允许删除
	 */
	public BatchDeleteBillResponse batchDeleteBill(BatchDeleteBillCommand cmd) {
		assetService.checkAssetPriviledgeForPropertyOrg(cmd.getOwnerId(),PrivilegeConstants.ASSET_MANAGEMENT_BATCH_DELETE_BILL,cmd.getOrganizationId());
		BatchDeleteBillResponse response = new BatchDeleteBillResponse();
		List<Long> billIdList = cmd.getBillIdList();
		Integer selectCount, deleteFail, deleteSuccess;
		if(billIdList != null) {
			selectCount = cmd.getBillIdList().size();
			response.setSelectCount(selectCount);//共选中条数
			//过滤出不能删除的账单
			List<PaymentBills> failDeleteBill = assetBillProvider.findCannotDeleteBill(billIdList);
			if(failDeleteBill != null) {
				deleteFail = failDeleteBill.size();
				List<BatchDeleteBillDTO> batchDeleteBillDTOList = new ArrayList<>();
				for(PaymentBills paymentBill : failDeleteBill) {
					String billGroupName = assetProvider.findBillGroupNameById(paymentBill.getBillGroupId());
					String deleteErrorMsg = "";
					if(!paymentBill.getSourceType().equals(AssetSourceType.ASSET_MODULE) 
							&& !paymentBill.getSourceType().equals(AssetSourceType.CONTRACT_MODULE) ) {
						deleteErrorMsg = "第三方账单，不允许删除";
					}else{
						deleteErrorMsg = "已缴账单，不允许删除";
					}
		            BatchDeleteBillDTO dto = new BatchDeleteBillDTO();
		            dto.setBillId(paymentBill.getId());
		            dto.setDateStrBegin(paymentBill.getDateStrBegin());
		            dto.setDateStrEnd(paymentBill.getDateStrEnd());
		            dto.setBillGroupName(billGroupName);
		            dto.setDeleteErrorMsg(deleteErrorMsg);
		            batchDeleteBillDTOList.add(dto);
		            billIdList.remove(paymentBill.getId());
				}
				response.setBatchDeleteBillDTOList(batchDeleteBillDTOList);
			}else {
				deleteFail = 0;
			}
			deleteSuccess = selectCount - deleteFail;
			response.setDeleteFail(deleteFail);
			response.setDeleteSuccess(deleteSuccess);
			assetBillProvider.batchDeleteBill(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), billIdList);
		}
        return response;
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
	

	
}
