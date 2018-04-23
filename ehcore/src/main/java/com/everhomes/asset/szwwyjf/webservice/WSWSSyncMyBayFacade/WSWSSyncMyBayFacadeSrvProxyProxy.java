package com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade;

public class WSWSSyncMyBayFacadeSrvProxyProxy implements com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy {
  private String _endpoint = null;
  private com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy wSWSSyncMyBayFacadeSrvProxy = null;
  
  public WSWSSyncMyBayFacadeSrvProxyProxy() {
    _initWSWSSyncMyBayFacadeSrvProxyProxy();
  }
  
  public WSWSSyncMyBayFacadeSrvProxyProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSWSSyncMyBayFacadeSrvProxyProxy();
  }
  
  private void _initWSWSSyncMyBayFacadeSrvProxyProxy() {
    try {
      wSWSSyncMyBayFacadeSrvProxy = (new com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyServiceLocator()).getWSWSSyncMyBayFacade();
      if (wSWSSyncMyBayFacadeSrvProxy != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSWSSyncMyBayFacadeSrvProxy)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSWSSyncMyBayFacadeSrvProxy)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSWSSyncMyBayFacadeSrvProxy != null)
      ((javax.xml.rpc.Stub)wSWSSyncMyBayFacadeSrvProxy)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy getWSWSSyncMyBayFacadeSrvProxy() {
    if (wSWSSyncMyBayFacadeSrvProxy == null)
      _initWSWSSyncMyBayFacadeSrvProxyProxy();
    return wSWSSyncMyBayFacadeSrvProxy;
  }
  
  public java.lang.String sync_TenancyContractDetailed(java.lang.String params) throws java.rmi.RemoteException, com.everhomes.asset.szwwyjf.webservice.wssyncmybayfacade.client.WSInvokeException{
    if (wSWSSyncMyBayFacadeSrvProxy == null)
      _initWSWSSyncMyBayFacadeSrvProxyProxy();
    return wSWSSyncMyBayFacadeSrvProxy.sync_TenancyContractDetailed(params);
  }
  
  public java.lang.String sync_TenancyContractData(java.lang.String params) throws java.rmi.RemoteException, com.everhomes.asset.szwwyjf.webservice.wssyncmybayfacade.client.WSInvokeException{
    if (wSWSSyncMyBayFacadeSrvProxy == null)
      _initWSWSSyncMyBayFacadeSrvProxyProxy();
    return wSWSSyncMyBayFacadeSrvProxy.sync_TenancyContractData(params);
  }
  
  public java.lang.String sync_TenancyContractInfo(java.lang.String params) throws java.rmi.RemoteException, com.everhomes.asset.szwwyjf.webservice.wssyncmybayfacade.client.WSInvokeException{
    if (wSWSSyncMyBayFacadeSrvProxy == null)
      _initWSWSSyncMyBayFacadeSrvProxyProxy();
    return wSWSSyncMyBayFacadeSrvProxy.sync_TenancyContractInfo(params);
  }
  
  
}