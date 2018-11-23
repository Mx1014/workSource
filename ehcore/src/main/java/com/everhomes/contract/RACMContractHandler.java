package com.everhomes.contract;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.customer.SyncDataTask;
import com.everhomes.entity.EntityType;
import com.everhomes.openapi.Contract;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.contract.SyncContractsFromThirdPartCommand;
import com.everhomes.rest.customer.SyncDataTaskType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;

@Component(ContractService.CONTRACT_PREFIX + "999929")
public class RACMContractHandler extends DefaultContractServiceImpl {

    private final static Logger LOGGER = LoggerFactory.getLogger(RACMContractHandler.class);


    @Override
    public String syncContractsFromThirdPart(SyncContractsFromThirdPartCommand cmd, Boolean authFlag) {
        if(authFlag) {
            checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_SYNC, cmd.getOrgId(), cmd.getCommunityId());
        }

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if(community == null) {
            return "0";
        }
        //这是一个简易的同步锁，在同步数据的时候我们会将获取到的数据存入数据库中，这时我们会将这些正在同步的数据的状态置为InActive,此时如果再次创建的话，上一次没有执行完同步之前数据库的正在执行的状态的数量不为1则返回状态‘有同步任务正在进行’
        int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.CONTRACT.getCode());
        if(syncCount > 0) {
            return "1";
        }
        String version;
        if(cmd.getAllSyncFlag() != null && cmd.getAllSyncFlag() == 1) {
            version = "1970-01-01";
        }else {
            Timestamp versionTime = contractProvider.findLastContractVersionByCommunity(cmd.getNamespaceId());
            if(versionTime != null){
                version = versionTime.toString();
            }else{
                version = "1970-01-01";
            }
        }
        ThirdPartContractHandler contractHandler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + cmd.getNamespaceId());
        if(contractHandler != null) {
            SyncDataTask task = new SyncDataTask();
            task.setOwnerType(EntityType.COMMUNITY.getCode());
            task.setOwnerId(community.getId());
            task.setType(SyncDataTaskType.CONTRACT.getCode());
            task.setCreatorUid(UserContext.currentUserId());
            task.setLockKey(CoordinationLocks.SYNC_CONTRACT.getCode() + cmd.getNamespaceId() + cmd.getCommunityId());
            SyncDataTask dataTask = syncDataTaskService.executeTask(() -> {
                SyncDataResponse response = new SyncDataResponse();
                
                ContractCategory contractCategory = contractProvider.findContractCategoryById(cmd.getCategoryId());
                Byte contractApplicationScene = 0;
                if (contractCategory != null && contractCategory.getContractApplicationScene() != null) {
                	contractApplicationScene = contractCategory.getContractApplicationScene();
				}
        		LOGGER.debug("RuiAn CM sync start , version is : {} , task is : {} , cmd is : {} , contractApplicationScene is : {} , communityId is : {}" , version, task, cmd, contractApplicationScene, community.getNamespaceCommunityToken());
                contractHandler.syncContractsFromThirdPart("1", version, community.getNamespaceCommunityToken(), task.getId(), cmd.getCategoryId(), contractApplicationScene);
                return response;
            }, task);

        }

        return "0";

    }

}
