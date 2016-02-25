package com.everhomes.aclink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;


@Component
public class DoorAccessServiceImpl implements DoorAccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AesServerKeyProvider.class);
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    @Autowired
    DoorAuthProvider doorAuthProvider;
    
    @Autowired
    DoorCommandProvider doorCommandProvider;
    
    @Autowired
    AclinkMsgGenerator msgGenerator;
    
    @Autowired
    AclinkMessageSequence messageSequence;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private AclinkProvider aclinkProvider;
    
    @Autowired
    private AesServerKeyProvider aesServerKeyProvider;
    
    @Autowired
    private AclinkMessageSequence aclinkMessageSequence;
    
    @Autowired
    private AesUserKeyProvider aesUserKeyProvider;
    
    @Autowired
    private AesServerKeyService aesServerKeyService;
    
    @Autowired
    private OwnerDoorProvider ownerDoorProvider;
    
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
        processIncomeMessageResp(inputs);
        return generateMessages(doorId);
    }
    
    public void processIncomeMessageResp(List<DoorMessageResp> inputs) {
        for (DoorMessageResp resp : inputs) {
            //DoorMessage origin = bigCollectionProvider.getMapAccessor("", "");
        } 
    }
    
    public DoorMessage activatingDoorAccess(DoorAccessActivingCommand cmd) {        
        User user = UserContext.current().getUser();
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = new DoorAccess();
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType().getCode());
                doorAcc.setStatus(DoorAccessStatus.ACTIVING.getCode());
                //cmd.getHardwareId();
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType().getCode());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);    
                } catch(Exception ex) {
                    LOGGER.error("createOwnerDoor failed ", ex);
                    }
                
                Aclink aclink = new Aclink();
                aclink.setFirwareVer(cmd.getFirwareVer());
                aclink.setManufacturer(cmd.getRsaAclinkPub());
                aclink.setStatus(DoorAccessStatus.ACTIVING.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclinkProvider.createAclink(aclink);
                
                AesServerKey aesServerKey = new AesServerKey();
                aesServerKey.setCreateTimeMs(System.currentTimeMillis());
                aesServerKey.setDoorId(doorAcc.getId());
                aesServerKey.setSecret(AclinkUtils.generateAESKey());
                aesServerKey.setDeviceVer(AclinkDeviceVer.VER0.getCode());
                aesServerKeyService.createAesServerKey(aesServerKey);
                
                //create a command event
                DoorCommand cmd = new DoorCommand();
                cmd.setDoorId(doorAcc.getId());
                cmd.setOwnerId(cmd.getOwnerId());
                cmd.setCmdId(AclinkCommandType.CMD_ACTIVE.getCode());
                return doorAcc;
            }
        });
        
        //cmd.getRsaAclinkPub();
        //List<DoorMessage> msgs = generateMessages(doorAccess.getId());
        //return msgs.get(0);
        
        //Generate a single message
        return null;
    }
    
    //激活一个新锁
    public List<DoorMessage> activateDoorAccess(String hardwareId, String hardwareAddr) {
        //check exists message
        //lock doorAccess
        //update status to created
        
        //out of lock, create a template auth for current activing user.
        
        //generate some message for current door access
        
        
        User user = UserContext.current().getUser();
        
        
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
    
    //Scan the bluetooth, then valify the door key.
    public List<DoorAuth> listValidDoorAuthByDeviceId(ListValidDoorAuthByDeviceId cmd) {
        return null;
    }
    
    //list all valid auth for one users
    //TODO what if the aes_user_key?
    //version controller for all
    public List<DoorAuth> listValidDoorAuthByUserId(ListValidDoorAuthByUserIdCommand cmd) {
        //return doorAuthProvider.queryValidDoorAuthByUserId();
        return null;
    }
    
    public List<DoorMessage> generateMessages(Long doorId) {
        List<DoorMessage> msgs = msgGenerator.generateMessages(doorId);
        for(DoorMessage dm : msgs) {
            messageSequence.pendingMessage(dm);
        }
        
        return msgs;
    }
    
    @Override
    public void onDoorMessageTimeout(Long cmdId) {
        //message timeout here
    }
    
    //list all AesUserKeys for current login user
    public List<AesUserKey> listAesUserKeyByUser() {
        //TODO cache AesUserKey
        
        User user = UserContext.current().getUser();
        ListingLocator locator = new ListingLocator();
        List<DoorAuth> auths = uniqueAuths(doorAuthProvider.queryValidDoorAuthByUserId(locator, user.getId(), 60));
        
        //TODO when the key is invalid, MUST invalid it and generate a command.
        
        List<AesUserKey> aesUserKeys = new ArrayList<AesUserKey>();
        for(DoorAuth auth : auths) {
            AesUserKey aesUserKey = generateAesUserKey(user, auth);
            if(aesUserKey != null) {
                aesUserKeys.add(aesUserKey);    
                }
            }
        
        return aesUserKeys;
    }
    
    private List<DoorAuth> uniqueAuths(List<DoorAuth> auths) {
        Map<String, DoorAuth> map = new HashMap<String, DoorAuth>();
        for(int i = auths.size()-1; i >= 0; i--) {
            DoorAuth auth = auths.get(i);
            String key = "" + auth.getAuthType() + ":" + auth.getDoorId() + ":" + auth.getUserId();
            if(map.containsKey(key)) {
                continue;
            } else {
                map.put(key, auth);    
                }
        }
        
        List<DoorAuth> newAuths = new ArrayList<DoorAuth>(); 
        for(Map.Entry<String, DoorAuth> kset : map.entrySet()) {
            newAuths.add(kset.getValue());
        }
        
        return newAuths;
    }
    
    private AesUserKey generateAesUserKey(User user, DoorAuth auth) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
        if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVE.getCode())) {
            //Check if the door is active
            return null;
        }
        
        AesUserKey aesUserKey = this.dbProvider.execute(new TransactionCallback<AesUserKey>() {

            @Override
            public AesUserKey doInTransaction(TransactionStatus arg0) {
                
                //Find it if it's already created
                AesUserKey aesUserKey = aesUserKeyProvider.queryAesUserKeyByDoorId(auth.getDoorId(), auth.getUserId());
                if(aesUserKey == null) {
                    //create it
                    aesUserKey = new AesUserKey();
                    aesUserKey.setId(aesUserKeyProvider.prepareForAesUserKeyId());
                    aesUserKey.setUserId(user.getId());
                    aesUserKey.setDoorId(auth.getDoorId());
                    aesUserKey.setCreatorUid(user.getId());
                    aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    aesUserKey.setExpireTimeMs(auth.getValidEndMs());
                    
                    //BASE64 encoding
                    aesUserKey.setSecret(AclinkUtils.packAesUserKey("", auth.getUserId(), aesUserKey.getKeyId(), auth.getValidEndMs()));
                    
                    aesUserKeyProvider.createAesUserKey(aesUserKey);
                    }
                return aesUserKey;
            }
            
        });
        
        if(aesUserKey.getSecret().isEmpty()) {
            //TODO log here
            return null;
        }
        
        return aesUserKey;
    }
    
//    void syncLogToServer(List<DoorAccessLog> logs) {
//    }
}
