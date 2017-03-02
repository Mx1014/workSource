
package com.everhomes.pmtask.webservice;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

@WebServiceClient(name = "WorkflowAppDraftWebService", targetNamespace = "http://interfaces.workflow.modules.firstsoft.cn")
public class WorkflowAppDraftWebService
    extends Service
{

    public WorkflowAppDraftWebService(URL url) {
        super(url, new QName("http://interfaces.workflow.modules.firstsoft.cn", "WorkflowAppDraftWebService"));
    }

    /**
     * 
     * @return
     *     returns WorkflowAppDraftWebServicePortType
     */
    @WebEndpoint(name = "WorkflowAppDraftWebServiceHttpPort")
    public WorkflowAppDraftWebServicePortType getWorkflowAppDraftWebServiceHttpPort() {
        return super.getPort(new QName("http://interfaces.workflow.modules.firstsoft.cn", "WorkflowAppDraftWebServiceHttpPort"), WorkflowAppDraftWebServicePortType.class);
    }

}
