// @formatter:off
package com.everhomes.user;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class MessageDataTest extends TestCase {

    @Test
    public void testSerialization() {
        MessageData msg = new MessageData();
        msg.setAppId(1L);
        msg.setBody("Test body");
        msg.setChannels(new MessageChannel("user", "1"), new MessageChannel("user", "2"));
        
        Map<String, String> meta = new HashMap<String, String>();
        meta.put("k1", "v1");
        meta.put("k2", "v2");
        msg.setMeta(meta);
        
        String json = msg.toJson();
        System.out.println("json: " + json);
        
        MessageData msg2 = MessageData.fromJson(json);
        
        Assert.assertTrue(msg2.getChannels() != null);
        Assert.assertTrue(msg2.getMeta() != null);
        Assert.assertTrue(msg2.getMeta().size() == 2);
        Assert.assertTrue(msg2.getMeta().get("k1").equals("v1"));
        Assert.assertTrue(msg2.getMeta().get("k2").equals("v2"));
    }
}
