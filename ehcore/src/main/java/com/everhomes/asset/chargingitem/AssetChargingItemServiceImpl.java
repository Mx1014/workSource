
package com.everhomes.asset.chargingitem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.ConfigChargingItemsCommand;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.asset.OwnerIdentityCommand;
import com.everhomes.sequence.SequenceProvider;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetChargingItemServiceImpl implements AssetChargingItemService {
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired
	protected LocaleStringService localeStringService;
	
	@Autowired
	private AssetChargingItemProvider assetChargingItemProvider;
	
	public void configChargingItems(ConfigChargingItemsCommand cmd) {
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
        	byte de_coupling = 0;
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
                IsProjectNavigateDefaultResp isProjectNavigateDefaultResp = assetChargingItemProvider.isChargingItemsForJudgeDefault(isProjectNavigateDefaultCmd);
                if(isProjectNavigateDefaultResp != null && isProjectNavigateDefaultResp.getDefaultStatus().equals(AssetProjectDefaultFlag.DEFAULT.getCode())) {
                	Boolean allScope = false;
                	assetChargingItemProvider.configChargingItems(cmd, de_coupling, allScope);
                }
            }
            //2、再创建全部配置
            Boolean allScope = true;
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            assetChargingItemProvider.configChargingItems(cmd, de_coupling, allScope);
        }else{
        	Boolean allScope = false;
        	byte de_coupling = 1;
        	assetChargingItemProvider.configChargingItems(cmd, de_coupling, allScope);
        }
    }
	
    public List<ListChargingItemsDTO> listAllChargingItems(OwnerIdentityCommand cmd) {
    	//标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            cmd.setAllScope(true);
        }else {
        	cmd.setAllScope(false);
        }
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        
        return assetChargingItemProvider.listChargingItems(cmd.getOwnerType(),cmd.getOwnerId(), cmd.getCategoryId(), cmd.getOrganizationId(), cmd.getAllScope());
    }
	
}