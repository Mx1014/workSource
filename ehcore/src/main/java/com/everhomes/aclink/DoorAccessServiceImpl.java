package com.everhomes.aclink;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;


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
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    public static String Manufacturer = "zuolin001";
    
    @PostConstruct
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    //列出某园区下的所有锁门禁
    @Override
    public List<DoorAccessDTO> listDoorAccessByOwnerId(CrossShardListingLocator locator, Long ownerId, DoorAccessOwnerType ownerType, int count) {
        List<DoorAccess> dacs = doorAccessProvider.listDoorAccessByOwnerId(locator, ownerId, ownerType, count);
        List<DoorAccessDTO> dtos = new ArrayList<DoorAccessDTO>();
        for(DoorAccess da : dacs) {
            DoorAccessDTO dto = ConvertHelper.convert(da, DoorAccessDTO.class);
            User user = userProvider.findUserById(da.getCreatorUserId());
            String nickName = (user.getNickName() == null ? user.getNickName(): user.getAccountName());
            dto.setCreatorName(nickName);
            dtos.add(dto);
        }
        return dtos;
    }
    
    @Override
    public ListDoorAccessResponse searchDoorAccessByAdmin(QueryDoorAccessAdminCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAccessResponse resp = new ListDoorAccessResponse();
        
        List<DoorAccess> dacs = doorAccessProvider.queryDoorAccesss(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(cmd.getOwnerId()));
                query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(cmd.getOwnerType()));
                if(cmd.getSearch() != null) {
                    Condition c = Tables.EH_DOOR_ACCESS.NAME.like(cmd.getSearch() + "%").
                            or(Tables.EH_DOOR_ACCESS.HARDWARE_ID.like(cmd.getSearch() + "%"));
                    query.addConditions(c);
                }
//                if(cmd.getLinkStatus() != null) {
//                    
//                }
                
                if(cmd.getDoorType() != null) {
                    query.addConditions(Tables.EH_DOOR_ACCESS.DOOR_TYPE.eq(cmd.getDoorType()));
                }
                return query;
            }
            
        });
        
        List<DoorAccessDTO> dtos = new ArrayList<DoorAccessDTO>();
        for(DoorAccess da : dacs) {
            DoorAccessDTO dto = ConvertHelper.convert(da, DoorAccessDTO.class);
            User user = userProvider.findUserById(da.getCreatorUserId());
            String nickName = (user.getNickName() == null ? user.getNickName(): user.getAccountName());
            dto.setCreatorName(nickName);
            dtos.add(dto);
        }
        resp.setDoors(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        
        return resp;
    }
    
    @Override
    public ListDoorAccessResponse listDoorAccessByOwnerId(ListDoorAccessByOwnerIdCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        ListDoorAccessResponse resp = new ListDoorAccessResponse();
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        DoorAccessOwnerType typ = DoorAccessOwnerType.fromCode(cmd.getOwnerType()); 
        resp.setDoors(this.listDoorAccessByOwnerId(locator, cmd.getOwnerId(), typ, count));
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    @Override
    public DoorAuthDTO createDoorAuthForever(User approveUser, DoorAccess doorAcc, User user) {
        DoorAuth doorAuthResult = this.dbProvider.execute(new TransactionCallback<DoorAuth>() {
            @Override
            public DoorAuth doInTransaction(TransactionStatus arg0) {
                DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthForever(doorAcc.getId(), user.getId());
                if(doorAuth != null) {
                    return doorAuth;
                    }
                
                doorAuth = new DoorAuth();
                doorAuth.setDoorId(doorAcc.getId());
                doorAuth.setAuthType(DoorAuthType.FOREVER.getCode());
                doorAuth.setOwnerId(doorAcc.getOwnerId());
                doorAuth.setOwnerType(doorAcc.getOwnerType());
                doorAuth.setApproveUserId(approveUser.getId());
                doorAuth.setUserId(user.getId());
                doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
                doorAuthProvider.createDoorAuth(doorAuth);
                return doorAuth;
                }
            });
        return ConvertHelper.convert(doorAuthResult, DoorAuthDTO.class);
    }
    
    @Override
    public DoorAuthDTO createDoorAuth(CreateDoorAuthCommand cmd) {
        //TODO notify a message to client
        
        User approveUser = UserContext.current().getUser();
        DoorAccess doorAcc = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        User user = userProvider.findUserById(cmd.getUserId());
        
        DoorAuth doorAuth = ConvertHelper.convert(cmd, DoorAuth.class);
        
        if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
            return createDoorAuthForever(approveUser, doorAcc, user);
        } else {
            if(cmd.getValidEndMs() == null || cmd.getValidFromMs() == null) {
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "invalid param for DoorAuth");                
            }
            
            //TODO from APP ?
            doorAuth.setApproveUserId(approveUser.getId());
            doorAuth.setOwnerId(doorAcc.getOwnerId());
            doorAuth.setOwnerType(doorAcc.getOwnerType());
            doorAuthProvider.createDoorAuth(doorAuth);
            return ConvertHelper.convert(doorAuth, DoorAuthDTO.class);
        }
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
        
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
        if(doorAccess != null) {
            //TODO error code
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "DoorAccess exists");
        }
        
        doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
                if(doorAcc != null) {
                    return doorAcc;
                    }
                
                doorAcc = new DoorAccess();
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType().getCode());
                doorAcc.setStatus(DoorAccessStatus.ACTIVING.getCode());
                cmd.setHardwareId(cmd.getHardwareId());
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
                aclink.setManufacturer(Manufacturer);
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
                cmd.setOwnerType(cmd.getOwnerType());
                cmd.setCmdId(AclinkCommandType.CMD_ACTIVE.getCode());
                doorCommandProvider.createDoorCommand(cmd);
                return doorAcc;
            }
        });
        
        //cmd.getRsaAclinkPub();
        //List<DoorMessage> msgs = generateMessages(doorAccess.getId());
        //return msgs.get(0);
        
        //Generate a single message
        AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
        String message = AclinkUtils.packInitServerKey(cmd.getRsaAclinkPub(), aesServerKey.getSecret(), "", doorAccess.getName(),
                doorAccess.getCreateTime().getTime(), doorAccess.getUuid());
        
        DoorMessage doorMessage = new DoorMessage();
        doorMessage.setDoorId(doorAccess.getId());
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        AclinkMessage acMsg = new AclinkMessage();
        acMsg.setCmd(AclinkCommandType.INIT_SERVER_KEY.getCode());
        acMsg.setEncrypted(message);
        acMsg.setSecretVersion(aesServerKey.getDeviceVer());
        doorMessage.setBody(acMsg);
        
        return doorMessage;
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
