package com.everhomes.asset.standard;

import java.util.List;

import com.everhomes.asset.PaymentFormula;
import com.everhomes.rest.asset.DeleteChargingStandardCommand;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsDTO;
import com.everhomes.rest.asset.ModifyChargingStandardCommand;
import com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards;

/**
 * @author created by ycx
 * @date 下午7:48:06
 */
public interface AssetStandardProvider {
	
	List<ListChargingStandardsDTO> listOnlyChargingStandards(ListChargingStandardsCommand cmd);
	
	List<PaymentFormula> getFormulas(Long id);
	
	EhPaymentChargingStandards findChargingStandardById(Long chargingStandardId);
	
	IsProjectNavigateDefaultResp isChargingStandardsForJudgeDefault(IsProjectNavigateDefaultCmd cmd);
	
	void modifyChargingStandard(ModifyChargingStandardCommand cmd, List<Long> allCommunity);
	
	void deleteChargingStandard(DeleteChargingStandardCommand cmd, List<Long> allCommunity);
	
	void decouplingHistoryStandard(Integer namespaceId, Long categoryId, Long chargingStandardId, List<Long> allCommunity);
	
	
}
