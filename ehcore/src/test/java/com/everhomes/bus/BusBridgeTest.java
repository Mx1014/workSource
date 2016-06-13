package com.everhomes.bus;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.msgbox.MessageLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.app.AppConstants;

public class BusBridgeTest  extends CoreServerTestCase {

    @Autowired
    private LocalBusProvider localBusProvider;
    
    @Autowired
    private MessageBoxProvider messageBoxProvider;
    
    //@Ignore @Test
    public void testBridgeForwarding() {
        this.localBusProvider.publish(null, "global.bus.message", "Hello, world");
        
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    
    @Test
    public void testMessageBox() {
        String testPrefix = "testMessageBox6:janson";
        for(int i = 0; i < 12; i++) {
            Message m = new Message();
            m.setContent("testMessageBox:" + i);
            m.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
            m.setAppId(AppConstants.APPID_PUSH);
            long msgId = messageBoxProvider.putMessage(testPrefix, m);
            System.out.println("msgId: " + msgId);
        }
        
        MessageLocator l = new MessageLocator(testPrefix);
        //l.setAnchor(0l);
        List<Message> msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());

        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());
        
        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + msgInBox.get(msgInBox.size()-1).getContent() + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());
        
        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        System.out.println("Got content: " + " anchor: " + l.getAnchor() + " size: " + msgInBox.size());
        
        l = new MessageLocator(testPrefix);
        //l.setAnchor(0l);
        msgInBox = messageBoxProvider.getPastToRecentMessages(l, 5, true);
        assertTrue(msgInBox.size() == 0);
    }
}
