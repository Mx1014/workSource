package com.everhomes.user;

public class UserContext {
    private static ThreadLocal<UserContext> s_userContexts = new ThreadLocal<UserContext>();
    
    private User user;
    
    public UserContext() {
    }
    
    public static UserContext current() {
        UserContext context = s_userContexts.get();
        if(context == null) {
            context = new UserContext();
            s_userContexts.set(context);
        }
        return context;
    }
    
    public static void clear() {
        s_userContexts.set(null);
    }
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
