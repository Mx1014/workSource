
package com.everhomes.asset.group;

import java.util.List;

import com.everhomes.rest.asset.AddOrModifyRuleForBillGroupCommand;
import com.everhomes.rest.asset.CreateBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupReponse;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ModifyBillGroupCommand;
import com.everhomes.rest.asset.OwnerIdentityCommand;

/**
 * @author created by ycx
 * @date 下午7:56:36
 */
public interface AssetGroupService {
	
	List<ListBillGroupsDTO> listBillGroups(OwnerIdentityCommand cmd);
	
	void createBillGroup(CreateBillGroupCommand cmd);

	void modifyBillGroup(ModifyBillGroupCommand cmd);
	
	DeleteBillGroupReponse deleteBillGroup(DeleteBillGroupCommand cmd);
	
	List<ListBillGroupsDTO> listBillGroupsForEnt(OwnerIdentityCommand cmd);
	
	void addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd);
	
	
	
}
