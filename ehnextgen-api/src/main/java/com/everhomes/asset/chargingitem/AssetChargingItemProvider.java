package com.everhomes.asset.chargingitem;

import java.util.List;

import com.everhomes.rest.asset.ConfigChargingItemsCommand;
import com.everhomes.rest.asset.CreateChargingItemCommand;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListChargingItemsDTO;

/**
 * @author created by ycx
 * @date 下午7:48:06
 */
public interface AssetChargingItemProvider {
	
	IsProjectNavigateDefaultResp isChargingItemsForJudgeDefault(IsProjectNavigateDefaultCmd cmd);
	
	void createChargingItem(CreateChargingItemCommand cmd, List<Long> communityIds);
	
	List<ListChargingItemsDTO> listChargingItems(String ownerType, Long ownerId, Long categoryId,Long orgId,Boolean allScope);
	
	void configChargingItems(ConfigChargingItemsCommand cmd, byte de_coupling, Boolean allScope);
	
}
