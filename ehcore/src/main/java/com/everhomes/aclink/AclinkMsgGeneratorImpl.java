package com.everhomes.aclink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.aclink.AclinkMessage;
import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.rest.aclink.DoorMessageType;

@Component
public class AclinkMsgGeneratorImpl implements AclinkMsgGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkMsgGeneratorImpl.class);
    
    @Autowired
    DoorCommandProvider doorCommandProvider;
    
    @Autowired
    AesServerKeyProvider aesServerKeyProvider;
    
    @Autowired
    AesServerKeyService aesServerKeyService;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    @Autowired
    AesUserKeyProvider aesUserKeyProvider;
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    final String MESSAGE_SYNC = "dooraccess:%d:msgsync";
    
    private void genDefaultMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
        if(!cmd.getStatus().equals(DoorCommandStatus.SENDING.getCode())) {
            //Mark as sending
            cmd.setStatus(DoorCommandStatus.SENDING.getCode());
            doorCommandProvider.updateDoorCommand(cmd);
        }
        
        //Convert DoorCommand to DoorMessage
//        AesServerKey aesServerKey = aesServerKeyProvider.queryAesServerKeyByDoorId(cmd.getDoorId(), cmd.getServerKeyVer());
        AclinkMessage aclinkMessage = new AclinkMessage();
        aclinkMessage.setCmd(cmd.getCmdId());
        aclinkMessage.setEncrypted(cmd.getCmdBody());
        aclinkMessage.setSecretVersion(cmd.getAclinkKeyVer());
        DoorMessage doorMessage = new DoorMessage();
        doorMessage.setBody(aclinkMessage);
        doorMessage.setSeq(cmd.getId());
        doorMessage.setDoorId(cmd.getDoorId());
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        ctx.getDoorMessages().add(doorMessage);
    }
    
    private void genDeviceNameMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
        //Convert DoorCommand to DoorMessage
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        AesServerKey aesServerKey = aesServerKeyProvider.queryAesServerKeyByDoorId(cmd.getDoorId(), cmd.getServerKeyVer());
        if(doorAccess == null || aesServerKey == null) {
            return;
        }
        
        String body = AclinkUtils.packUpdateDeviceName(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
                , doorAccess.getAesIv(), doorAccess.getName());
        
        if(!cmd.getStatus().equals(DoorCommandStatus.SENDING.getCode())) {
            //Mark as sending
            cmd.setStatus(DoorCommandStatus.SENDING.getCode());
            doorCommandProvider.updateDoorCommand(cmd);
        }
        
        AclinkMessage aclinkMessage = new AclinkMessage();
        aclinkMessage.setCmd(cmd.getCmdId());
        aclinkMessage.setEncrypted(body);
        aclinkMessage.setSecretVersion(cmd.getAclinkKeyVer());
        DoorMessage doorMessage = new DoorMessage();
        doorMessage.setBody(aclinkMessage);
        doorMessage.setSeq(cmd.getId());
        doorMessage.setDoorId(cmd.getDoorId());
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        ctx.getDoorMessages().add(doorMessage);
    }
    
    private void genAddUndoMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
        if(!cmd.getStatus().equals(DoorCommandStatus.SENDING.getCode())) {
            cmd.setStatus(DoorCommandStatus.SENDING.getCode());
            doorCommandProvider.updateDoorCommand(cmd);
        }
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        AesServerKey aesServerKey = aesServerKeyProvider.queryAesServerKeyByDoorId(cmd.getDoorId(), cmd.getServerKeyVer());
        if(doorAccess == null || aesServerKey == null) {
            return;
        }
        
        Long aesUserKeyId = Long.valueOf(cmd.getCmdBody());
        if(aesUserKeyId == null) {
            return;
        }
        
        AesUserKey aesUserKey1 = aesUserKeyProvider.getAesUserKeyById(aesUserKeyId);
        if(aesUserKey1 == null) {
            return;    
        }
        
        String body = AclinkUtils.packAddUndoList(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
                , (int)(aesUserKey1.getExpireTimeMs().longValue()/1000), aesUserKey1.getKeyId().shortValue());
        
        AclinkMessage aclinkMessage = new AclinkMessage();
        aclinkMessage.setCmd(cmd.getCmdId());
        aclinkMessage.setEncrypted(body);
        aclinkMessage.setSecretVersion(cmd.getAclinkKeyVer());
        DoorMessage doorMessage = new DoorMessage();
        doorMessage.setBody(aclinkMessage);
        doorMessage.setSeq(cmd.getId());
        doorMessage.setDoorId(cmd.getDoorId());
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        ctx.getDoorMessages().add(doorMessage);
    }
    
    private void genMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
        AclinkCommandType cmdType = AclinkCommandType.fromCode(cmd.getCmdId());
        switch(cmdType) {
        case ADD_UNDO_LIST:
            genAddUndoMessage(ctx, cmd);
            break;
        case CMD_UPDATE_DEVNAME:
            genDeviceNameMessage(ctx, cmd);
            break;
        default:
           genDefaultMessage(ctx, cmd);     
        }
    }
    
    //从待处理的消息里，拿到所有的消息并发回给门禁
    @Override
    public List<DoorMessage> generateMessages(Long doorId) {
        ListingLocator locator = new ListingLocator();
        int count = 10;
        
        AclinkGeneratorContext ctx = new AclinkGeneratorContext();
        while(ctx.size() < 20) {
            //TODO validDoorCommandForUser?
            List<DoorCommand> cmds = doorCommandProvider.queryValidDoorCommands(locator, doorId, count);
            if(cmds == null || cmds.size() == 0) {
                break;
                }
            
            for(DoorCommand cmd : cmds) {
                ctx.putMessage(cmd);
                }
            
            //Check anchor
            if(locator.getAnchor() == null) {
                break;
                }
        }
        
        String key = String.format(MESSAGE_SYNC, doorId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object v = redisTemplate.opsForValue().get(key);
        if(v == null || v.toString().equals("0")) {
        	redisTemplate.opsForValue().set(key, "1", 3600, TimeUnit.SECONDS);
        	v = null;
        }
        
        List<DoorCommand> cmds = ctx.getOrderMessages();
        if(cmds.size() > 0 || v == null) {
            //fake time message, the sequence is 0
            AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorId);
            byte[] serverKey = Base64.decodeBase64(aesServerKey.getSecret());
            byte[] timeMessage = CmdUtil.updateTime(serverKey, aesServerKey.getDeviceVer().byteValue());
            DoorMessage doorMessage = new DoorMessage();
            doorMessage.setDoorId(doorId);
            doorMessage.setSeq(0l);
            doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
            AclinkMessage aclinkMessage = new AclinkMessage();
            aclinkMessage.setCmd(AclinkCommandType.CMD_UPDATE_TIME.getCode());
            aclinkMessage.setSecretVersion(aesServerKey.getDeviceVer());
            aclinkMessage.setEncrypted(Base64.encodeBase64String(timeMessage));
            doorMessage.setBody(aclinkMessage);
            ctx.getDoorMessages().add(doorMessage);
        }
        
        for(DoorCommand cmd: cmds) {
            genMessage(ctx, cmd);
        }
        
        List<DoorMessage> results = ctx.getDoorMessages();
        if(results.size() == 1 && v != null) {
            LOGGER.warn("some message loss because of something error!");
            return new ArrayList<DoorMessage>();
        }
        
        return results;
    }
    
    /**
     * Generate message one by one
     */
    @Override
    public AclinkWebSocketMessage generateWebSocketMessage(Long doorId) {
        ListingLocator locator = new ListingLocator();
        int count = 1;
        AclinkGeneratorContext ctx = new AclinkGeneratorContext();
        
        List<DoorCommand> cmds = doorCommandProvider.queryValidDoorCommands(locator, doorId, count);
        if(cmds == null || cmds.size() == 0) {
            return null;
            }
        
        ctx.putMessage(cmds.get(0));
        genMessage(ctx, cmds.get(0));
        
        List<DoorMessage> results = ctx.getDoorMessages();
        
        if(results == null || results.size() == 0) {
            return null;
            }
        
        AclinkWebSocketMessage smsg = new AclinkWebSocketMessage();
        smsg.setId(doorId);
        smsg.setPayload(results.get(0).getBody().getEncrypted());
        smsg.setSeq(results.get(0).getSeq());
        
        return smsg;
    }
    
    @Override
    public AclinkWebSocketMessage generateTimeMessage(Long doorId) {
        AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorId);
        byte[] serverKey = Base64.decodeBase64(aesServerKey.getSecret());
        byte[] timeMessage = CmdUtil.updateTime(serverKey, aesServerKey.getDeviceVer().byteValue());
        
        AclinkWebSocketMessage smsg = new AclinkWebSocketMessage();
        smsg.setId(doorId);
        smsg.setPayload(Base64.encodeBase64String(timeMessage));
        
        Random r = new Random();
        smsg.setSeq(new Long(r.nextInt(300)));
        
        return smsg;
    }
    
    @Override
    public void invalidSyncTimer(Long doorId) {
        String key = String.format(MESSAGE_SYNC, doorId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.opsForValue().set(key, "0", 3600, TimeUnit.SECONDS);
    }
}
