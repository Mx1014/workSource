/**
 * WSWSSyncMyBayFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade;

public class WSWSSyncMyBayFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxyService {

    public WSWSSyncMyBayFacadeSrvProxyServiceLocator() {
    }


    public WSWSSyncMyBayFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSWSSyncMyBayFacadeSrvProxyServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSWSSyncMyBayFacade
    private java.lang.String WSWSSyncMyBayFacade_address = "http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade/已修改为可配置";

    public java.lang.String getWSWSSyncMyBayFacadeAddress() {
        return WSWSSyncMyBayFacade_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSWSSyncMyBayFacadeWSDDServiceName = "WSWSSyncMyBayFacade";

    public java.lang.String getWSWSSyncMyBayFacadeWSDDServiceName() {
        return WSWSSyncMyBayFacadeWSDDServiceName;
    }

    public void setWSWSSyncMyBayFacadeWSDDServiceName(java.lang.String name) {
        WSWSSyncMyBayFacadeWSDDServiceName = name;
    }

    public com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy getWSWSSyncMyBayFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSWSSyncMyBayFacade_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSWSSyncMyBayFacade(endpoint);
    }

    public com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy getWSWSSyncMyBayFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSoapBindingStub _stub = new com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSWSSyncMyBayFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSWSSyncMyBayFacadeEndpointAddress(java.lang.String address) {
        WSWSSyncMyBayFacade_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSoapBindingStub _stub = new com.everhomes.asset.szwwyjf.webservice.WSWSSyncMyBayFacade.WSWSSyncMyBayFacadeSoapBindingStub(new java.net.URL(WSWSSyncMyBayFacade_address), this);
                _stub.setPortName(getWSWSSyncMyBayFacadeWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSWSSyncMyBayFacade".equals(inputPortName)) {
            return getWSWSSyncMyBayFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade/经测试无关", "WSWSSyncMyBayFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade/经测试无关", "WSWSSyncMyBayFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSWSSyncMyBayFacade".equals(portName)) {
            setWSWSSyncMyBayFacadeEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
