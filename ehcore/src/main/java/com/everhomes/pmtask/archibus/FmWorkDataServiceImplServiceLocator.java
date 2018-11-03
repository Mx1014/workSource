/**
 * FmWorkDataServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.everhomes.pmtask.archibus;

public class FmWorkDataServiceImplServiceLocator extends org.apache.axis.client.Service implements com.everhomes.pmtask.archibus.FmWorkDataServiceImplService {

    public FmWorkDataServiceImplServiceLocator() {
    }


    public FmWorkDataServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FmWorkDataServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FmWorkDataServiceImplPort
    private java.lang.String FmWorkDataServiceImplPort_address = "http://www.xboxad.com:8080/archibus/webServices/fmWork";

    public java.lang.String getFmWorkDataServiceImplPortAddress() {
        return FmWorkDataServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FmWorkDataServiceImplPortWSDDServiceName = "FmWorkDataServiceImplPort";

    public java.lang.String getFmWorkDataServiceImplPortWSDDServiceName() {
        return FmWorkDataServiceImplPortWSDDServiceName;
    }

    public void setFmWorkDataServiceImplPortWSDDServiceName(java.lang.String name) {
        FmWorkDataServiceImplPortWSDDServiceName = name;
    }

    public com.everhomes.pmtask.archibus.FmWorkDataService getFmWorkDataServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FmWorkDataServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFmWorkDataServiceImplPort(endpoint);
    }

    public com.everhomes.pmtask.archibus.FmWorkDataService getFmWorkDataServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.everhomes.pmtask.archibus.FmWorkDataServiceImplServiceSoapBindingStub _stub = new com.everhomes.pmtask.archibus.FmWorkDataServiceImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getFmWorkDataServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFmWorkDataServiceImplPortEndpointAddress(java.lang.String address) {
        FmWorkDataServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.everhomes.pmtask.archibus.FmWorkDataService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.everhomes.pmtask.archibus.FmWorkDataServiceImplServiceSoapBindingStub _stub = new com.everhomes.pmtask.archibus.FmWorkDataServiceImplServiceSoapBindingStub(new java.net.URL(FmWorkDataServiceImplPort_address), this);
                _stub.setPortName(getFmWorkDataServiceImplPortWSDDServiceName());
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
        if ("FmWorkDataServiceImplPort".equals(inputPortName)) {
            return getFmWorkDataServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://inner.yjq.webservice.archibus.steward.com/", "FmWorkDataServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://inner.yjq.webservice.archibus.steward.com/", "FmWorkDataServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FmWorkDataServiceImplPort".equals(portName)) {
            setFmWorkDataServiceImplPortEndpointAddress(address);
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
