
package com.everhomes.asset.chargingitem;

import java.util.List;

import com.everhomes.rest.asset.AddOrModifyRuleForBillGroupCommand;
import com.everhomes.rest.asset.ConfigChargingItemsCommand;
import com.everhomes.rest.asset.CreateBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupReponse;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.ModifyBillGroupCommand;
import com.everhomes.rest.asset.OwnerIdentityCommand;

/**
 * @author created by ycx
 * @date 下午7:56:36
 */
public interface AssetChargingItemService {
	
	void configChargingItems(ConfigChargingItemsCommand cmd);
	
	List<ListChargingItemsDTO> listAllChargingItems(OwnerIdentityCommand cmd);
	
	
	
}
