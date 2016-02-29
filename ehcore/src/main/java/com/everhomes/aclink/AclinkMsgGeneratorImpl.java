package com.everhomes.aclink;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.listing.ListingLocator;

@Component
public class AclinkMsgGeneratorImpl implements AclinkMsgGenerator {
    @Autowired
    DoorCommandProvider doorCommandProvider;
    
    private void genAddUndoMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
    }
    
    private void genMessage(AclinkGeneratorContext ctx, DoorCommand cmd) {
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
        }
        
        List<DoorCommand> cmds = ctx.getOrderMessages();
        for(DoorCommand cmd: cmds) {
            genMessage(ctx, cmd);
        }
        
        return ctx.getDoorMessages();
    }
}
