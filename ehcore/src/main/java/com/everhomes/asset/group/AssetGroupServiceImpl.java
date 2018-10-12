
package com.everhomes.asset.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetPaymentConstants;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AddOrModifyRuleForBillGroupCommand;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.CreateBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupReponse;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ModifyBillGroupCommand;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetGroupServiceImpl implements AssetGroupService {
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired 
	private AssetGroupProvider assetGroupProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	protected LocaleStringService localeStringService;

	public List<ListBillGroupsDTO> listBillGroups(OwnerIdentityCommand cmd) {
    	//标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            cmd.setAllScope(true);
        }else {
        	cmd.setAllScope(false);
        }
        if(cmd.getOwnerType() == null) {
        	cmd.setOwnerType("community");
        }
         // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getModuleId() != null && cmd.getModuleId().longValue() != ServiceModuleConstants.ASSET_MODULE){
            // 转换
             Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l, cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
             //issue-36480 【物业缴费6.5】合同应用没有关联物业缴费应用，签合同的时候添加计价条款，账单组显示了内容
             if(assetCategoryId != null) {
            	 cmd.setCategoryId(assetCategoryId);
             }else {
            	 return null;
             }
         }
        return assetGroupProvider.listBillGroups(cmd.getOwnerId(),cmd.getOwnerType(), cmd.getCategoryId(), cmd.getOrganizationId(), cmd.getAllScope());
    }

    public void createBillGroup(CreateBillGroupCommand cmd) {
        byte deCouplingFlag = 1;
        Long brotherGroupId = null;
        //创造账单组不涉及到安全问题，所以只需要看同步与否
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            deCouplingFlag = 0;
            brotherGroupId = getBrotherGroupId();
            //1、先同步下去
            List<Long> allCommunity = assetService.getAllCommunity(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), false);
            for(int i =0; i < allCommunity.size(); i ++){
                Long communityId = allCommunity.get(i);
                cmd.setOwnerId(communityId);
                //全部配置要同步到具体项目的时候，首先要判断一下该项目是否解耦了，如果解耦了，则不需要
            	IsProjectNavigateDefaultCmd isProjectNavigateDefaultCmd = new IsProjectNavigateDefaultCmd();
                isProjectNavigateDefaultCmd.setOwnerId(communityId);
                isProjectNavigateDefaultCmd.setOwnerType("community");
                isProjectNavigateDefaultCmd.setNamespaceId(cmd.getNamespaceId());
                isProjectNavigateDefaultCmd.setCategoryId(cmd.getCategoryId());
                isProjectNavigateDefaultCmd.setOrganizationId(cmd.getOrganizationId());//标准版新增的管理公司ID
                IsProjectNavigateDefaultResp isProjectNavigateDefaultResp = assetGroupProvider.isBillGroupsForJudgeDefault(isProjectNavigateDefaultCmd);
                if(isProjectNavigateDefaultResp != null && isProjectNavigateDefaultResp.getDefaultStatus().equals(AssetProjectDefaultFlag.DEFAULT.getCode())) {
                	Boolean allScope = false;
                	assetGroupProvider.createBillGroup(cmd, deCouplingFlag, brotherGroupId, null, allScope);
                }
            }
            //2、再创建全部配置
            Boolean allScope = true;
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            assetGroupProvider.createBillGroup(cmd, deCouplingFlag, null, brotherGroupId, allScope);
            //全部项目修改billGroup的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
            //根据billGroupId查询出所有关联的项目（包括已经授权出去的项目）
            List<ListBillGroupsDTO> listBillGroupsDTOs = assetGroupProvider.listBillGroups(cmd.getOwnerId(),cmd.getOwnerType(), cmd.getCategoryId(), cmd.getOrganizationId(), allScope);
            for(ListBillGroupsDTO dto : listBillGroupsDTOs) {
            	Long billGroupId = dto.getBillGroupId();
            	assetGroupProvider.decouplingHistoryBillGroup(cmd.getNamespaceId(), cmd.getCategoryId(), billGroupId, allCommunity);
            }
        }else{
        	Boolean allScope = false;
        	assetGroupProvider.createBillGroup(cmd, deCouplingFlag, brotherGroupId, null, allScope);
        }
    }
    
    private Long getBrotherGroupId() {
    	long nextGroupId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_GROUPS.getClass()));
    	return nextGroupId;
    }

    public void modifyBillGroup(ModifyBillGroupCommand cmd) {
    	List<Long> allCommunity = assetService.getAllCommunity(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), true);
    	//标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            //全部的情况,修改billGroup的以及bro为billGroup的
            cmd.setAllScope(true);
            assetGroupProvider.modifyBillGroup(cmd, allCommunity);
        }else {
        	//具体项目修改billGroup的，此园区的所有group解耦
        	cmd.setAllScope(false);
        	assetGroupProvider.modifyBillGroup(cmd, allCommunity);
        }
    }

    /**
     * 如果已经关联合同（不论状态）或者新增账单被使用，则不能修改或删除
     */
    public DeleteBillGroupReponse deleteBillGroup(DeleteBillGroupCommand cmd) {
    	List<Long> allCommunity = assetService.getAllCommunity(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), true);
    	//1、物业缴费V6.6统一账单：如果该账单组被其他模块应用选中了，则不允许删除
        boolean workFlag = assetProvider.checkIsUsedByGeneralBill(cmd.getBillGroupId(), null);
        if(workFlag) {
        	DeleteBillGroupReponse response = new DeleteBillGroupReponse();
        	response.setFailCause(AssetPaymentConstants.DELTE_GROUP_UNSAFE);
            return response;
        }
        //2、缴费模块原来的判断是否可以删除逻辑
        if(cmd.getOwnerId() == null || cmd.getOwnerId() ==-1) {
        	cmd.setAllScope(true);
        }else {
        	cmd.setAllScope(false);
        }
        return assetGroupProvider.deleteBillGroupAndRules(cmd, allCommunity);
    }
    
    public List<ListBillGroupsDTO> listBillGroupsForEnt(OwnerIdentityCommand cmd) {
		if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
        }
        return assetGroupProvider.listBillGroups(cmd.getOwnerId(),cmd.getOwnerType(), null, cmd.getOrganizationId(), false);//对公转账不区分多入口，所以categoryId为null
	}
	
    public void addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd) {
        byte deCouplingFlag = 1;
        Long brotherRuleId = null;
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            deCouplingFlag = 0;
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            brotherRuleId = assetGroupProvider.addOrModifyRuleForBillGroup(cmd,brotherRuleId,deCouplingFlag);
            List<Long> allCommunity = assetService.getAllCommunity(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), false);
            for(int i = 0; i < allCommunity.size(); i ++){
                cmd.setOwnerId(allCommunity.get(i));
                boolean coupled = checkCoupledForGroupRule(cmd.getOwnerId(),cmd.getOwnerType());
                if(coupled){
                	assetGroupProvider.addOrModifyRuleForBillGroup(cmd,brotherRuleId,deCouplingFlag);
                }
            }
        }else if(cmd.getOwnerId() != null && cmd.getOwnerId() != -1){
            //添加+解耦，判断safe+修改+解耦group和rule
        	assetGroupProvider.addOrModifyRuleForBillGroup(cmd,brotherRuleId,deCouplingFlag);
        }
    }

    private boolean checkCoupledForGroupRule(Long ownerId, String ownerType) {
        List<EhPaymentBillGroupsRules> list = assetProvider.getBillGroupRuleByCommunityWithBro(ownerId,ownerType,false);
        if(list.size() > 0){
            //没有bro，不耦合或者为空
            List<EhPaymentBillGroupsRules> list2 = assetProvider.getBillGroupRuleByCommunity(ownerId,ownerType);
            if(list2.size() == 0){
                return true;
            }
            return false;
        }
        //有bro，肯定耦合
        return true;
    }
	
}
