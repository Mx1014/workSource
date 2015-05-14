package com.everhomes.search;

public class SearchUtils {
    public static final String TOPICINDEXNAME = "everhomesv3";
    public static final String TOPICINDEXTYPE = "topic";
    
    public static final String GROUPINDEXNAME = "everhomesv3";
    public static final String GROUPINDEXTYPE = "group";
    
    public static final String COMMUNITYINDEXNAME = "everhomesv3";
    public static final String COMMUNITYINDEXTYPE = "community";
    
    public static Long getLongField(Object o) {
        Long v = -1l;
        String s = "";
        try {
            v = (Long)o;
            return v;
        }
        catch(Exception e) {
            s = e.getMessage();
        }
    
        try {
            v = new Long((Integer)o);
            return v;
        }
        catch(Exception e) {
            s += e.getMessage();
        }
        
        throw new ClassCastException(s);
    }
}
