//package com.everhomes.rest.messaging;
//
//import com.everhomes.util.StringHelper;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class  BlockingEventStored {
//
//    private static final Map stored = new ConcurrentHashMap();
//
//    public static synchronized Integer incr(String key){
//        Integer value = Integer.valueOf(stored.get(key + ".calledTimes").toString());
//        value += 1;
//        stored.put(key + ".calledTimes",value);
//        return value;
//    }
//
//    public static synchronized Object get(String key){
//        return stored.get(key);
//    }
//
//    public static synchronized void set(String key, Object value){
//            if(value != null){
//            stored.put(key,value);
//        }
//    }
//
//    public static synchronized void remove(String key){
//        stored.remove(key);
//    }
//
//    @Override
//    public String toString() {
//        return StringHelper.toJsonString(this);
//    }
//}
