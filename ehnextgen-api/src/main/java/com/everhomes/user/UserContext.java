// @formatter:off
package com.everhomes.user;

import com.everhomes.app.App;
import com.everhomes.domain.Domain;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.launchpadbase.AppContext;

public class UserContext {
    private static ThreadLocal<UserContext> s_userContexts = new ThreadLocal<UserContext>();
    
    private User user;
    private UserLogin login;
    private App callerApp;
    private Integer namespaceId;
    private String version;
    private String versionRealm;
    private String scheme;
    private Domain domain;
    //预览版本标志
    private Long previewPortalVersionId;

    private AppContext appContext;

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
    
    public App getCallerApp() {
        return callerApp;
    }
    
    public void setCallerApp(App app) {
        this.callerApp = app;
    }
    
    public UserLogin getLogin() {
        return this.login;
    }
    
    public void setLogin(UserLogin login) {
        this.login = login;
    }

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static Integer getCurrentNamespaceId() {
	    return getCurrentNamespaceId(null);
	}
	
	public static void setCurrentNamespaceId(Integer namespaceId) {
		current().setNamespaceId(namespaceId);
	}
	
	public static void setCurrentUser(User user) {
		current().setUser(user);
	}

	public static Long currentUserId() {
        User user = current().getUser();
        return user != null ? user.getId() : null;
    }

	public static Integer getCurrentNamespaceId(Integer namespaceId){
		UserContext context = s_userContexts.get();
		
		if(context == null) {
			return null == namespaceId ? Namespace.DEFAULT_NAMESPACE : namespaceId;
        }
		
		//added by janson 如果预先设置了域空间，则优先用 context 里面的域空间
		if(null == namespaceId || Namespace.DEFAULT_NAMESPACE == namespaceId){
			namespaceId = context.namespaceId;
		}
		
		if(context.getUser() == null) {
			return null == namespaceId ? Namespace.DEFAULT_NAMESPACE : namespaceId;
		}
		
		if(null == namespaceId || Namespace.DEFAULT_NAMESPACE == namespaceId){
			namespaceId = context.getUser().getNamespaceId();
		}
		
		if(null == namespaceId){
			namespaceId = Namespace.DEFAULT_NAMESPACE;
		}
		
		return namespaceId;
		
	}

    public String getVersionRealm() {
        return versionRealm;
    }

    public void setVersionRealm(String versionRealm) {
        this.versionRealm = versionRealm;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Long getPreviewPortalVersionId() {
        return previewPortalVersionId;
    }

    public void setPreviewPortalVersionId(Long previewPortalVersionId) {
        this.previewPortalVersionId = previewPortalVersionId;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }
}
