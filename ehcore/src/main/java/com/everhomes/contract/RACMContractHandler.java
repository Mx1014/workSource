package com.everhomes.contract;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.customer.SyncDataTask;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.contract.SyncContractsFromThirdPartCommand;
import com.everhomes.rest.customer.SyncCustomersCommand;
import com.everhomes.rest.customer.SyncDataTaskType;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component(ContractService.CONTRACT_PREFIX + "999929")
public class RACMContractHandler extends DefaultContractServiceImpl {

    @Override
    public String syncContractsFromThirdPart(SyncContractsFromThirdPartCommand cmd, Boolean authFlag) {
        if(authFlag) {
            checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_SYNC, cmd.getOrgId(), cmd.getCommunityId());
        }

        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if(community == null) {
            return "0";
        }
        int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.CONTRACT.getCode());
        if(syncCount > 0) {
            return "1";
        }
        String version;
        if(cmd.getAllSyncFlag() != null && cmd.getAllSyncFlag() == 1) {
            version = "2018-08-01";
        }else {
            version = new Timestamp(DateHelper.currentGMTTime().getTime()).toString();
            //version = contractProvider.findLastContractVersionByCommunity(cmd.getNamespaceId(), community.getId());
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
                contractHandler.syncContractsFromThirdPart("1", version, community.getNamespaceCommunityToken(), task.getId(), cmd.getCategoryId(), cmd.getContractApplicationScene());
                return response;
            }, task);

        }

        return "0";

    }

}
