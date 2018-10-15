package com.everhomes.asset.group;

import java.util.List;

import com.everhomes.asset.PaymentBillGroup;
import com.everhomes.rest.asset.AddOrModifyRuleForBillGroupCommand;
import com.everhomes.rest.asset.CreateBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupReponse;
import com.everhomes.rest.asset.DeleteChargingItemForBillGroupResponse;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ModifyBillGroupCommand;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules;

/**
 * @author created by ycx
 * @date 下午7:48:06
 */
public interface AssetGroupProvider {
	
	List<ListBillGroupsDTO> listBillGroups(Long ownerId, String ownerType, Long categoryId,Long orgId,Boolean allScope);

	Long createBillGroup(CreateBillGroupCommand cmd, byte deCouplingFlag, Long brotherGroupId, Long nextGroupId, Boolean allScope);

    void modifyBillGroup(ModifyBillGroupCommand cmd, List<Long> allCommunity);
	
    PaymentBillGroup getBillGroupById(Long billGroupId);
    
    String getbillGroupNameById(Long billGroupId);
    
    DeleteBillGroupReponse deleteBillGroupAndRules(DeleteBillGroupCommand cmd, List<Long> allCommunity);
    
    boolean isInWorkGroupRule(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule);
    
    Long addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd,Long brotherRuleId,byte deCouplingFlag);
    
    boolean checkBillsByBillGroupId(Long billGroupId);
    
    DeleteChargingItemForBillGroupResponse deleteBillGroupRuleById(Long billGroupRuleId,byte deCouplingFlag);
    
    EhPaymentBillGroupsRules findBillGroupRuleById(Long billGroupRuleId);
    
    IsProjectNavigateDefaultResp isBillGroupsForJudgeDefault(IsProjectNavigateDefaultCmd cmd);
    
    void decouplingHistoryBillGroup(Integer namespaceId, Long categoryId, Long billGroupId, List<Long> allCommunity);
}
