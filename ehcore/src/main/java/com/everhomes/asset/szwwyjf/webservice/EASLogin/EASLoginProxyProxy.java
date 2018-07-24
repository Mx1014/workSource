package com.everhomes.asset.szwwyjf.webservice.EASLogin;

public class EASLoginProxyProxy implements com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxy {
  private String _endpoint = null;
  private com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxy eASLoginProxy = null;
  
  public EASLoginProxyProxy() {
    _initEASLoginProxyProxy();
  }
  
  public EASLoginProxyProxy(String endpoint) {
    _endpoint = endpoint;
    _initEASLoginProxyProxy();
  }
  
  private void _initEASLoginProxyProxy() {
    try {
      eASLoginProxy = (new com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxyServiceLocator()).getEASLogin();
      if (eASLoginProxy != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)eASLoginProxy)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)eASLoginProxy)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (eASLoginProxy != null)
      ((javax.xml.rpc.Stub)eASLoginProxy)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.everhomes.asset.szwwyjf.webservice.EASLogin.EASLoginProxy getEASLoginProxy() {
    if (eASLoginProxy == null)
      _initEASLoginProxyProxy();
    return eASLoginProxy;
  }
  
  public com.everhomes.asset.szwwyjf.webservice.client.WSContext login(java.lang.String userName, java.lang.String password, java.lang.String slnName, java.lang.String dcName, java.lang.String language, int dbType) throws java.rmi.RemoteException{
    if (eASLoginProxy == null)
      _initEASLoginProxyProxy();
    return eASLoginProxy.login(userName, password, slnName, dcName, language, dbType);
  }
  
  public com.everhomes.asset.szwwyjf.webservice.client.WSContext login(java.lang.String userName, java.lang.String password, java.lang.String slnName, java.lang.String dcName, java.lang.String language, int dbType, java.lang.String authPattern) throws java.rmi.RemoteException{
    if (eASLoginProxy == null)
      _initEASLoginProxyProxy();
    return eASLoginProxy.login(userName, password, slnName, dcName, language, dbType, authPattern);
  }
  
  public com.everhomes.asset.szwwyjf.webservice.client.WSContext login(java.lang.String userName, java.lang.String password, java.lang.String slnName, java.lang.String dcName, java.lang.String language, int dbType, java.lang.String authPattern, int isEncodePwd) throws java.rmi.RemoteException{
    if (eASLoginProxy == null)
      _initEASLoginProxyProxy();
    return eASLoginProxy.login(userName, password, slnName, dcName, language, dbType, authPattern, isEncodePwd);
  }
  
  public boolean logout(java.lang.String userName, java.lang.String slnName, java.lang.String dcName, java.lang.String language) throws java.rmi.RemoteException{
    if (eASLoginProxy == null)
      _initEASLoginProxyProxy();
    return eASLoginProxy.logout(userName, slnName, dcName, language);
  }
  
  
}