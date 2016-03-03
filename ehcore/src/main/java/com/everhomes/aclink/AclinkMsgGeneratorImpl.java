package com.everhomes.aclink;

import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.listing.ListingLocator;

@Component
public class AclinkMsgGeneratorImpl implements AclinkMsgGenerator {
    @Autowired
    DoorCommandProvider doorCommandProvider;
    
    @Autowired
    AesServerKeyProvider aesServerKeyProvider;
    
    @Autowired
    AesServerKeyService aesServerKeyService;
    
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
    
    private void genAddUndoMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
    }
    
    private void genMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
        AclinkCommandType cmdType = AclinkCommandType.fromCode(cmd.getCmdId());
        switch(cmdType) {
        case ADD_UNDO_LIST:
            genAddUndoMessage(ctx, cmd);
        default:
           genDefaultMessage(ctx, cmd);     
        }
    }
    
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
        
        List<DoorCommand> cmds = ctx.getOrderMessages();
        if(cmds.size() > 0) {
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
        
        return ctx.getDoorMessages();
    }
}
