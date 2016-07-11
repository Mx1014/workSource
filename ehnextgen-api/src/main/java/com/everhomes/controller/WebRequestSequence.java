// @formatter:off
package com.everhomes.controller;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicLong;

import com.everhomes.util.NetHelper;

public class WebRequestSequence {
    private static ThreadLocal<WebRequestSequence> s_sequences = new ThreadLocal<WebRequestSequence>();

    private static AtomicLong s_nextId = new AtomicLong(System.currentTimeMillis());
    
    private static String prefix = "";
    
    private String requestSequence = "";
    
    static {
        InetAddress addr = NetHelper.getLocalInetAddress();
        if(addr != null) {
            byte[] address = addr.getAddress();
            int lastByte = ((int)address[address.length - 1] & 0xff);
            prefix = String.valueOf(lastByte) + "-";
        }
    }
    
    private WebRequestSequence() {
    }
    
    public static WebRequestSequence current() {
        WebRequestSequence seq = s_sequences.get();
        if(seq == null) {
            seq = new WebRequestSequence();
            s_sequences.set(seq);
        }
        return seq;
    }
    
    public String getRequestSequence() {
        return this.requestSequence;
    }
    
    public void setupRequestSequence() {
        long id = s_nextId.addAndGet(1);
        requestSequence = prefix + String.valueOf(id);
    }
    
    public void setRequenceSequence(String requestSequence) {
        this.requestSequence = requestSequence;
    }
}
