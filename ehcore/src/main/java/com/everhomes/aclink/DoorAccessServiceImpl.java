package com.everhomes.aclink;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.user.User;


@Component
public class DoorAccessServiceImpl implements DoorAccessService {
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    //列出某园区下的所有锁门禁
    public List<DoorAccess> listDoorAccessByCommunityId(Long communityId, CrossShardListingLocator locator, int count) {
        return doorAccessProvider.listDoorAccessByCommunityId(communityId, locator, count);
    }
    
    //列出某企业下的所有门禁
    public List<DoorAccess> listDoorAccessByEnterpriseId(Long enterpriseId, CrossShardListingLocator locator, int count) {
        return doorAccessProvider.listDoorAccessByEnterpriseId(enterpriseId, locator, count);
    }
    
    //获取最新需要更新的数据，包括用户最新的钥匙DoorUserKey，以前锁与服务器交互的钥匙 DoorServerKey。同时可以对上次更新的消息进行确认。
    //urgent 为 true, 则拿最紧急的消息列表。更新到设备之后再尝试开门或其它事情。
    public List<DoorMessage> queryDoorMessageByDoorId(Boolean urgent, Long doorId, List<DoorMessageResp> inputs) {
        
        for (DoorMessageResp resp : inputs) {
            //DoorMessage origin = bigCollectionProvider.getMapAccessor("", "");
        }
        
        return null;
    }
    
    public DoorMessage activatingDoorAccess(DoorAccessActivingCommand cmd) {
        //bigCollectionProvider.setValue(arg0, arg1, arg2, TimeUnit.SECONDS);
        
        //Check auth
        //lock with doorAccess
        //create doorAccess, set status to creating
        //generate a server key, server key version. 
        //initial ackingSecretVersion expectSecretVersion
        
        //Out of lock, create a InitialServer message, add message seq to check message list
        //return door message
        return null;
    }
    
    //激活一个新锁
    public List<DoorMessage> activateDoorAccess(String hardwareId, String hardwareAddr) {
        //check exists message
        //lock doorAccess
        //update status to created
        
        //out of lock, create a template auth for current activing user.
        
        //generate some message for current door access
        
        return null;
        
    }
    
    //更新锁的详细信息
    public void updateDoorAccess(DoorAccess door){
        //only update some information.
    }
    
    //销毁一个门禁。要求管理员手动销毁，同时 reset 设备。
    public void destroyDoorAccess(DoorAccess door) {
        //lock
        //set status to deleted
    }
    
    //刷新并返回一个新的 DoorServerKey
    public AesServerKey refreshDoorServerKey() {
        return null;
    }
    
    //创建门禁的一个新的用户授权
    public void createDoorUserKey(User user, AesUserKey userKey) {
        
    }
    
    List<AesUserKey> listAesUserKeyByUserId(Long userId) {
        return null;
        
    }
    
    public void createDoorAuth(DoorAuth doorAuth) {
        
    }
    
    public void removeDoorAuth(DoorAuth doorAuth) {
        
    }
    
    public List<DoorInvite> listDoorAuths() {
        return null;
        
    }
    
//    void syncLogToServer(List<DoorAccessLog> logs) {
//    }
}
