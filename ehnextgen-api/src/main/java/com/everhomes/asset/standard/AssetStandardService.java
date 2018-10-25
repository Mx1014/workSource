
package com.everhomes.asset.standard;

import java.util.List;

import com.everhomes.rest.asset.CreateChargingStandardCommand;
import com.everhomes.rest.asset.CreateFormulaCommand;
import com.everhomes.rest.asset.DeleteChargingStandardCommand;
import com.everhomes.rest.asset.DeleteChargingStandardDTO;
import com.everhomes.rest.asset.GetChargingStandardCommand;
import com.everhomes.rest.asset.GetChargingStandardDTO;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsResponse;
import com.everhomes.rest.asset.ModifyChargingStandardCommand;
import com.everhomes.server.schema.tables.pojos.EhPaymentFormula;

/**
 * @author created by ycx
 * @date 下午7:56:36
 */
public interface AssetStandardService {
	
	ListChargingStandardsResponse listOnlyChargingStandards(ListChargingStandardsCommand cmd);
	
	void createChargingStandard(CreateChargingStandardCommand cmd);
	
	List<EhPaymentFormula> createFormula(CreateFormulaCommand cmd);
	
	void modifyChargingStandard(ModifyChargingStandardCommand cmd);
	
	DeleteChargingStandardDTO deleteChargingStandard(DeleteChargingStandardCommand cmd);
	
	GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd);
	
}
