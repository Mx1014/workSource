package com.everhomes.search;

public class SearchUtils {
    //public static final String TOPICINDEXNAME = "everhomesv3";
    public static final String TOPICINDEXTYPE = "topic";
    
    //public static final String GROUPINDEXNAME = "everhomesv3";
    public static final String GROUPINDEXTYPE = "group";
    
    //public static final String COMMUNITYINDEXNAME = "everhomesv3";
    public static final String COMMUNITYINDEXTYPE = "community";
    
    //public static final String ENTERPRISEINDEXNAME = "everhomesv3";
    public static final String ENTERPRISEINDEXTYPE = "enterprise";
    
    public static final String CONFACCOUNTINDEXTYPE = "confaccount";
    
    public static final String ENTERPRISECONTACTINDEXTYPE = "enterprisecontact";
    
    public static final String CONFENTERPRISEINDEXTYPE = "confenterprise";
    
    public static final String CONFORDERINDEXTYPE = "conforder";
    
    public static final String NEWS = "news";
    
    public static Long getLongField(Object o) {
        Long v = -1l;
        String s = "";
        try {
        	if(null == o){
        		return v;
        	}
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
