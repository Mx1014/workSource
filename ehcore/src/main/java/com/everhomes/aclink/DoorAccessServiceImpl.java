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
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;


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
    private UserService userService;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    public static String Manufacturer = "zuolin001";
    
    private static long MAX_KEY_ID = 1024;
    
    @PostConstruct
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_ACLINK);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    private void sendMessageToUser(Long uid, Long doorId) {
        Map<String, String> meta = new HashMap<String, String>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.ACLINK_AUTH_CHANGED.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(new AclinkMessageMeta(uid, doorId)));
        sendMessageToUser(uid, "You have new auth", meta);
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
                
                query.addConditions(Tables.EH_DOOR_ACCESS.STATUS.ne(DoorAccessStatus.INVALID.getCode()));
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
    public AclinkUserResponse listAclinkUsers(ListAclinkUserCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<User> users = null;
        if(cmd.getKeyword() == null) {
            users = userProvider.listUserByKeyword("", cmd.getNamespaceId(), locator, pageSize);
        } else {
            users = userProvider.listUserByKeyword(cmd.getKeyword(), cmd.getNamespaceId(), locator, pageSize);
        }
        
        List<AclinkUserDTO> userDTOs = new ArrayList<AclinkUserDTO>();
        for(User u : users) {
            AclinkUserDTO dto = ConvertHelper.convert(u, AclinkUserDTO.class);
            dto.setPhone(u.getIdentifierToken());
            DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthForever(cmd.getDoorId(), dto.getId());
            if(doorAuth != null) {
                dto.setStatus(DoorAuthStatus.VALID.getCode());
                dto.setAuthId(doorAuth.getId());
            } else {
                dto.setStatus(DoorAuthStatus.INVALID.getCode());
                }
            userDTOs.add(dto);
        }
        
        AclinkUserResponse resp = new AclinkUserResponse();
        resp.setUsers(userDTOs);
        resp.setUsers(userDTOs);
        return resp;
    }
    
    @Override
    public DoorAuthDTO createDoorAuth(CreateDoorAuthCommand cmd) {
        //TODO notify a message to client
        
        if(cmd.getApproveUserId() == null) {
            User user = UserContext.current().getUser();
            cmd.setApproveUserId(user.getId());
        }
        
        DoorAccess doorAcc = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        UserInfo user = userService.getUserSnapshotInfoWithPhone(cmd.getUserId());
        
        DoorAuth doorAuth = ConvertHelper.convert(cmd, DoorAuth.class);
        DoorAuthDTO rlt = null;
        
        if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
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
                    doorAuth.setApproveUserId(cmd.getApproveUserId());
                    doorAuth.setUserId(user.getId());
                    doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
                    doorAuth.setOrganization(cmd.getOrganization());
                    doorAuth.setDescription(cmd.getDescription());
                    if(user.getPhones() != null && user.getPhones().size() > 0) {
                        doorAuth.setPhone(user.getPhones().get(0));    
                        }
                    
                    doorAuthProvider.createDoorAuth(doorAuth);
                    return doorAuth;
                    }
                });
            rlt = ConvertHelper.convert(doorAuthResult, DoorAuthDTO.class);
            rlt.setDoorName(doorAcc.getName());
        } else {
            if(cmd.getValidEndMs() == null || cmd.getValidFromMs() == null) {
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "invalid param for DoorAuth");                
            }
            
            doorAuth.setApproveUserId(cmd.getApproveUserId());
            doorAuth.setOwnerId(doorAcc.getOwnerId());
            doorAuth.setOwnerType(doorAcc.getOwnerType());
            doorAuth.setOrganization(cmd.getOrganization());
            doorAuth.setDescription(cmd.getDescription());
            doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
            
            if(user.getPhones() != null && user.getPhones().size() > 0) {
                doorAuth.setPhone(user.getPhones().get(0));    
                }
            if(user.getNickName() != null) {
                doorAuth.setNickname(user.getNickName());
            } else {
                doorAuth.setNickname(user.getAccountName());
            }
            
            doorAuthProvider.createDoorAuth(doorAuth);
            rlt = ConvertHelper.convert(doorAuth, DoorAuthDTO.class);
            rlt.setDoorName(doorAcc.getName());
            rlt.setHardwareId(doorAcc.getHardwareId());
        }
        
        sendMessageToUser(user.getId(), doorAcc.getId());
        
        return rlt;
    }
    
    @Override
    public DoorAuthDTO createDoorAuth(CreateDoorAuthByUser cmd2) {
        User user = UserContext.current().getUser();
        CreateDoorAuthCommand cmd = ConvertHelper.convert(cmd2, CreateDoorAuthCommand.class);
        cmd.setApproveUserId(user.getId());
        DoorAuth auth = doorAuthProvider.queryValidDoorAuthForever(cmd.getDoorId(), user.getId());
        if(auth == null) {
            //TODO auth failed
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Auth failed");   
        }
        
        cmd.setAuthType(DoorAuthType.TEMPERATE.getCode());
        return createDoorAuth(cmd);
    }
    
    @Override
    public Long deleteDoorAccess(Long doorAccessId) {
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = doorAccessProvider.getDoorAccessById(doorAccessId);
                if(doorAcc != null) {
                    doorAcc.setStatus(DoorAccessStatus.INVALID.getCode());
                    doorAccessProvider.updateDoorAccess(doorAcc);
                    
                    
                    
                    return doorAcc;
                }
                
                return null;
            }
        });
        
        if(doorAccess == null) {
            return null;
        }
        
        return doorAccess.getId();
    }
    
    @Override
    public Long deleteDoorAuth(Long doorAuthId) {
        this.dbProvider.execute(new TransactionCallback<DoorCommand>() {
            @Override
            public DoorCommand doInTransaction(TransactionStatus arg0) {
                DoorAuth doorAuth = doorAuthProvider.getDoorAuthById(doorAuthId);
                if(doorAuth != null) {
                    AesUserKey aesUserKey1 = aesUserKeyProvider.queryAesUserKeyByAuthId(doorAuth.getDoorId(), doorAuth.getId());
                    
                    AesUserKey aesUserKey2 = aesUserKeyProvider.queryAesUserKeyByDoorId(doorAuth.getDoorId(), doorAuth.getUserId(), doorAuthId);
                    
                    doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
                    doorAuthProvider.updateDoorAuth(doorAuth);
                    
                    if(aesUserKey1 == null || aesUserKey2 != null) {
                        return null;
                    }
                    
                    AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAuth.getDoorId());
                    if(aesServerKey == null) {
                        return null;
                    }
                    
                    if(aesUserKey1.getExpireTimeMs() < System.currentTimeMillis()) {
                    return null;
                    }
                    
                    //generate a DoorCommand, and will send to Device next time
                    DoorCommand cmd = new DoorCommand();
                    cmd.setUserId(doorAuth.getUserId());
                    cmd.setDoorId(doorAuth.getDoorId());
                    cmd.setOwnerId(cmd.getOwnerId());
                    cmd.setOwnerType(cmd.getOwnerType());
                    cmd.setCmdId(AclinkCommandType.ADD_UNDO_LIST.getCode());
                    cmd.setCmdType((byte)0);
                    cmd.setServerKeyVer(aesServerKey.getSecretVer());
                    cmd.setAclinkKeyVer(aesServerKey.getDeviceVer());
                    cmd.setStatus(DoorCommandStatus.CREATING.getCode());
                    cmd.setCmdBody(AclinkUtils.packAddUndoList(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
                            , (int)(aesUserKey1.getCreateTimeMs().longValue()/1000), aesUserKey1.getKeyId().shortValue()));
                    doorCommandProvider.createDoorCommand(cmd);
                    
                    return cmd;
                    }
                
                return null;
            }
        });
        
        return null;
    }
    
    //获取最新需要更新的数据，包括用户最新的钥匙DoorUserKey，以前锁与服务器交互的钥匙 DoorServerKey。同时可以对上次更新的消息进行确认。
    //urgent 为 true, 则拿最紧急的消息列表。更新到设备之后再尝试开门或其它事情。
    @Override
    public QueryDoorMessageResponse queryDoorMessageByDoorId(QueryDoorMessageCommand cmd) {
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
        if(doorAccess == null) {
            //TODO error code
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "DoorAccess not found");
        }
        
        processIncomeMessageResp(cmd.getInputs());
        QueryDoorMessageResponse resp = new QueryDoorMessageResponse();
        resp.setDoorId(doorAccess.getId());
        resp.setOutputs(generateMessages(doorAccess.getId()));
        return resp;
    }
    
    public void processIncomeMessageResp(List<DoorMessage> inputs) {
        if(inputs == null) {
            return;
        }
        
        for (DoorMessage resp : inputs) {
            aclinkMessageSequence.ackMessage(resp.getSeq());
            DoorCommand doorCommand = doorCommandProvider.getDoorCommandById(resp.getSeq());
            doorCommand.setStatus(DoorCommandStatus.RESPONSE.getCode());
            doorCommandProvider.updateDoorCommand(doorCommand);
            
            //doorCommand.getCmdId()
        } 
    }
    
    @Override
    public DoorMessage activatingDoorAccess(DoorAccessActivingCommand cmd) {        
        User user = UserContext.current().getUser();
        
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
        if(doorAccess != null && !doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())) {
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
                
                String aesKey = AclinkUtils.generateAESKey();
                String aesIv = AclinkUtils.generateAESIV(aesKey);
                
                doorAcc = new DoorAccess();
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType());
                doorAcc.setStatus(DoorAccessStatus.ACTIVING.getCode());
                doorAcc.setDoorType(DoorAccessType.ZLACLINK_WIFI.getCode());
                doorAcc.setHardwareId(cmd.getHardwareId());
                doorAcc.setName(cmd.getName());
                doorAcc.setDescription(cmd.getDescription());
                doorAcc.setAddress(cmd.getAddress());
                doorAcc.setCreatorUserId(user.getId());
                doorAcc.setAesIv(aesIv);
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);
                } catch(Exception ex) {
                    LOGGER.error("createOwnerDoor failed ", ex);
                    }
                
                Aclink aclink = new Aclink();
                aclink.setFirwareVer(cmd.getFirwareVer());
                aclink.setManufacturer(Manufacturer);
                //TODO set active as default
                aclink.setStatus(DoorAccessStatus.ACTIVE.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclink.setDeviceName("ZLTODO");
                aclinkProvider.createAclink(aclink);
                
                AesServerKey aesServerKey = new AesServerKey();
                aesServerKey.setCreateTimeMs(System.currentTimeMillis());
                aesServerKey.setDoorId(doorAcc.getId());
                aesServerKey.setSecret(aesKey);
                aesServerKey.setDeviceVer(AclinkDeviceVer.VER0.getCode());
                aesServerKeyService.createAesServerKey(aesServerKey);
                
                //create a command event
                DoorCommand cmd = new DoorCommand();
                cmd.setDoorId(doorAcc.getId());
                cmd.setOwnerId(cmd.getOwnerId());
                cmd.setOwnerType(cmd.getOwnerType());
                cmd.setCmdId(AclinkCommandType.INIT_SERVER_KEY.getCode());
                cmd.setCmdType((byte)0);//TODO use enum
                cmd.setServerKeyVer(1l);//Default for AesServerKey
                cmd.setAclinkKeyVer(AclinkDeviceVer.VER0.getCode());
                cmd.setStatus(DoorCommandStatus.SENDING.getCode());
                doorCommandProvider.createDoorCommand(cmd);
                return doorAcc;
            }
        });
        
        if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "DoorAccess is invalid");
        }
        
        //cmd.getRsaAclinkPub();
        //List<DoorMessage> msgs = generateMessages(doorAccess.getId());
        //return msgs.get(0);
        
        //Generate a single message
        AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
        String message = AclinkUtils.packInitServerKey(cmd.getRsaAclinkPub(), aesServerKey.getSecret(), doorAccess.getAesIv(), "ZLTODO",
                doorAccess.getCreateTime().getTime(), doorAccess.getUuid());
        
        DoorMessage doorMessage = new DoorMessage();
        doorMessage.setDoorId(doorAccess.getId());
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        AclinkMessage acMsg = new AclinkMessage();
        acMsg.setCmd(AclinkCommandType.INIT_SERVER_KEY.getCode());
        acMsg.setEncrypted(message);
        acMsg.setSecretVersion(aesServerKey.getDeviceVer());
        doorMessage.setBody(acMsg);
        
        //TODO pending messages
        
        return doorMessage;
    }
    
    //激活一个新锁
    @Override
    public QueryDoorMessageResponse activateDoorAccess(DoorAccessActivedCommand cmd) {
        User user = UserContext.current().getUser();
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(doorAccess != null && doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())
                && doorAccess.getCreatorUserId().equals(user.getId())
                && cmd.getHardwareId().equals(doorAccess.getHardwareId())) {
            
            DoorCommand dcmd = this.dbProvider.execute(new TransactionCallback<DoorCommand>() {
                @Override
                public DoorCommand doInTransaction(TransactionStatus arg0) {
                    doorAccess.setStatus(DoorAccessStatus.ACTIVE.getCode());
                    doorAccessProvider.updateDoorAccess(doorAccess);
                    DoorCommand doorCommand = doorCommandProvider.queryActiveDoorCommand(cmd.getDoorId());
                    doorCommand.setStatus(DoorCommandStatus.PROCESS.getCode());
                    doorCommandProvider.updateDoorCommand(doorCommand);
                    
                    //Create auth
                    DoorAuth doorAuth = new DoorAuth();
                    doorAuth.setApproveUserId(user.getId());
                    doorAuth.setAuthType(DoorAuthType.TEMPERATE.getCode());
                    doorAuth.setDoorId(cmd.getDoorId());
                    doorAuth.setDescription("auto generate");
                    doorAuth.setNickname(user.getNickName());
                    doorAuth.setOwnerId(doorAccess.getOwnerId());
                    doorAuth.setOwnerType(doorAccess.getOwnerType());
                    doorAuth.setValidFromMs(System.currentTimeMillis() -  60*1000);
                    doorAuth.setValidEndMs(System.currentTimeMillis()+ 10*60*1000);//TODO
                    doorAuth.setUserId(user.getId());
                    doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
                    UserIdentifier ui = userProvider.findIdentifierById(user.getId());
                    if(ui != null) {
                        doorAuth.setPhone(ui.getIdentifierToken());
                        }
                    doorAuthProvider.createDoorAuth(doorAuth);
                    
                    AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
                    
                    AesUserKey aesUserKey = new AesUserKey();
                    aesUserKey.setId(aesUserKeyProvider.prepareForAesUserKeyId());
                    aesUserKey.setKeyId(new Integer((int) (aesUserKey.getId().intValue() % MAX_KEY_ID)));
                    aesUserKey.setAuthId(doorAuth.getId());
                    aesUserKey.setUserId(doorAuth.getUserId());
                    aesUserKey.setCreatorUid(user.getId());
                    aesUserKey.setDoorId(doorAccess.getId());
                    if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
                        //7 Days
                        aesUserKey.setExpireTimeMs(System.currentTimeMillis() + 60*1000*24*7);
                    } else {
                        aesUserKey.setExpireTimeMs(doorAuth.getValidEndMs());    
                            }
                    aesUserKey.setStatus(AesUserKeyStatus.VALID.getCode());
                    aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    aesUserKey.setSecret(AclinkUtils.packAesUserKey(aesServerKey.getSecret(), doorAuth.getUserId(), aesUserKey.getKeyId(), doorAuth.getValidEndMs()));
                    aesUserKeyProvider.createAesUserKey(aesUserKey);
                    
                    //create device name command
                    doorCommand = new DoorCommand();
                    doorCommand.setDoorId(doorAccess.getId());
                    doorCommand.setOwnerId(doorAccess.getOwnerId());
                    doorCommand.setOwnerType(doorAccess.getOwnerType());
                    doorCommand.setCmdId(AclinkCommandType.CMD_UPDATE_DEVNAME.getCode());
                    doorCommand.setCmdType((byte)0);
                    doorCommand.setServerKeyVer(aesServerKey.getSecretVer());
                    doorCommand.setAclinkKeyVer(aesServerKey.getDeviceVer());
                    doorCommand.setStatus(DoorCommandStatus.CREATING.getCode());
                    
                    //Generate a message body for command
                    doorCommand.setCmdBody(AclinkUtils.packUpdateDeviceName(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
                            , doorAccess.getAesIv(), doorAccess.getName().substring(0, 6)));
                    
                    doorCommandProvider.createDoorCommand(doorCommand);
                    return doorCommand;
                }
            });
            
        } else {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "DoorAccess exists");
        }
        
        QueryDoorMessageResponse resp = new QueryDoorMessageResponse();
        resp.setDoorId(cmd.getDoorId());
        resp.setOutputs(generateMessages(cmd.getDoorId()));
        return resp;
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
        LOGGER.info("timeout for cmdId=", cmdId);
    }
    
    List<AesUserKey> listAesUserKeyByUser(User user) {
        //TODO cache AesUserKey
        
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
    
    @Override
    public  List<AesUserKey> listAesUserKeyByUserId(Long userId) {
        User user = userProvider.findUserById(userId);
        if(user != null) {
            return listAesUserKeyByUser(user);
        }
        
        return new ArrayList<AesUserKey>();
        
    }
    
    //list all AesUserKeys for current login user
    @Override
    public ListAesUserKeyByUserResponse listAesUserKeyByUser() {
        User user = UserContext.current().getUser();
        
        ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
        List<AesUserKey> aesUserKeys = this.listAesUserKeyByUser(user);
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(AesUserKey key : aesUserKeys) {
            AesUserKeyDTO dto = ConvertHelper.convert(key, AesUserKeyDTO.class);
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(dto.getDoorId());
            if(doorAccess != null) {
                dto.setHardwareId(doorAccess.getHardwareId());
                dtos.add(dto);    
            }
        }
        
        resp.setAesUserKeys(dtos);
        
        return resp;
    }
    
    @Override
    public ListAesUserKeyByUserResponse listAdminAesUserKeyByUser() {
        User user = UserContext.current().getUser();
        
        ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
        List<AesUserKey> aesUserKeys = aesUserKeyProvider.queryAdminAesUserKeyByUserId(user.getId(), 20);
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(AesUserKey key : aesUserKeys) {
            AesUserKeyDTO dto = ConvertHelper.convert(key, AesUserKeyDTO.class);
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(dto.getDoorId());
            if(doorAccess != null) {
                dto.setHardwareId(doorAccess.getHardwareId());
                dtos.add(dto);    
            }
        }
        
        resp.setAesUserKeys(dtos);
        
        return resp;
        
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
    
    private AesUserKey generateAesUserKey(User user, DoorAuth doorAuth) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(doorAuth.getDoorId());
        if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVE.getCode())) {
            //Check if the door is active
            return null;
        }
        
        AesUserKey aesUserKey = this.dbProvider.execute(new TransactionCallback<AesUserKey>() {

            @Override
            public AesUserKey doInTransaction(TransactionStatus arg0) {
                //Find it if it's already created
                AesUserKey aesUserKey = aesUserKeyProvider.queryAesUserKeyByDoorId(doorAuth.getDoorId(), doorAuth.getUserId());
                if(aesUserKey == null) {
                    AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
                    aesUserKey = new AesUserKey();
                    aesUserKey.setId(aesUserKeyProvider.prepareForAesUserKeyId());
                    aesUserKey.setKeyId(new Integer((int) (aesUserKey.getId().intValue() % MAX_KEY_ID)));
                    aesUserKey.setCreatorUid(user.getId());
                    aesUserKey.setUserId(doorAuth.getUserId());
                    aesUserKey.setDoorId(doorAccess.getId());
                    aesUserKey.setAuthId(doorAuth.getId());
                    if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
                        //7 Days
                        aesUserKey.setExpireTimeMs(System.currentTimeMillis() + 60*1000*24*7);
                        aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    } else {
                        aesUserKey.setExpireTimeMs(doorAuth.getValidEndMs());
                        aesUserKey.setKeyType(AesUserKeyType.TEMP.getCode());
                        }
                    
                    aesUserKey.setStatus(AesUserKeyStatus.VALID.getCode());
                    aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    aesUserKey.setSecret(AclinkUtils.packAesUserKey(aesServerKey.getSecret(), doorAuth.getUserId(), aesUserKey.getKeyId(), aesUserKey.getExpireTimeMs()));
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
    
    @Override
    public ListDoorAuthResponse queryDoorAuthByApproveId(ListDoorAuthCommand cmd) {
        User user = UserContext.current().getUser();
        
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        
        List<DoorAuth> auths = doorAuthProvider.queryDoorAuthByApproveId(locator, user.getId(), count);
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        for(DoorAuth auth : auths) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
            dto.setHardwareId(doorAccess.getHardwareId());
            dto.setDoorName(doorAccess.getName());
//            User u = userProvider.findUserById(auth.getUserId());
//            if(u != null) {
//                if(u.getNickName() != null) {
//                    dto.setUserNickName(u.getNickName());
//                } else {
//                    dto.setUserNickName(u.getAccountName());
//                }
//            }
            dtos.add(dto);
        }
        
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    @Override
    public ListDoorAuthResponse searchDoorAuth(SearchDoorAuthCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        
        List<DoorAuth> auths = doorAuthProvider.searchDoorAuthByAdmin(locator, cmd.getKeyword(), cmd.getStatus(), count);
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        for(DoorAuth auth : auths) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
            dto.setHardwareId(doorAccess.getHardwareId());
            dto.setDoorName(doorAccess.getName());
            User u = userProvider.findUserById(auth.getApproveUserId());
            if(u != null) {
                if(u.getNickName() != null) {
                    dto.setApproveUserName(u.getNickName());
                } else {
                    dto.setApproveUserName(u.getAccountName());
                }
            }
            dtos.add(dto);
        }
        
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;        
    }
    
//    void syncLogToServer(List<DoorAccessLog> logs) {
//    }
}
