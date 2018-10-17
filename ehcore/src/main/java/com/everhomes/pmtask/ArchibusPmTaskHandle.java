package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.pmtask.archibus.*;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.util.RuntimeErrorException;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.ARCHIBUS)
public class ArchibusPmTaskHandle extends DefaultPmTaskHandle implements ApplicationListener<ContextRefreshedEvent> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ArchibusPmTaskHandle.class);

    public static final String THIRDURL = "http://www.xboxad.com:8080/archibus/webServices/fmWork?wsdl";

    private static FmWorkDataService service;
    public FmWorkDataService getService(){

        if(service == null){
            FmWorkDataServiceImplServiceLocator locator = new FmWorkDataServiceImplServiceLocator();
            locator.setFmWorkDataServiceImplPortEndpointAddress(THIRDURL);

            try {
                    service = locator.getFmWorkDataServiceImplPort();
            } catch (ServiceException e) {
                e.printStackTrace();
            }

        }

        return service;
    }

    private CloseableHttpClient httpclient = null;

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void init() {
        httpclient = HttpClients.createDefault();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null) {
            init();
        }
    }

    @Override
    public Object getThirdAddress(HttpServletRequest req) {

        try {

            FmWorkDataService service = getService();
            String pk = req.getParameter("projectId");
            if(!"resourcesType3".equals(req.getParameter("resourcesType"))){
                pk = req.getParameter("parentId");
            }
            LOGGER.debug(pk);
            String json = service.getResources(req.getParameter("projectId"),pk,req.getParameter("resourcesType"));
            ArchibusEntity<ArchibusResource> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<ArchibusResource>>(){});
            if(!result.isSuccess()){
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
            }

            LOGGER.debug(json);

            return  result.getData();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object getThirdCategories(HttpServletRequest req) {

        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.getEventServiceType(req.getParameter("project_id"),req.getParameter("record_type"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ArchibusEntity<ArchibusCategory> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<ArchibusCategory>>(){});
        if(!result.isSuccess()){
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
        }

        LOGGER.debug(json);

        return  result.getData();

    }

    @Override
    public Object getThirdProjects(HttpServletRequest req){
        try {
            FmWorkDataService service = getService();

            String json = service.areaInfo();
            ArchibusEntity<ArchibusArea> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<ArchibusArea>>(){});
            if(!result.isSuccess()){
    //                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
            }

            String areaId = "";
            for(ArchibusArea area : result.getData()){
                if(area.getArea_name().contains("国贸")){
                    areaId = area.getPk_area();
                }
            }

            String json1 = service.getProjectInfo(areaId);
            ArchibusEntity<ArchibusProject> result1 = JSONObject.parseObject(json1,new TypeReference<ArchibusEntity<ArchibusProject>>(){});

            if(result1.isSuccess()){
                return result1.getData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        ArchibusPmTaskHandle bean = new ArchibusPmTaskHandle();
        Map<String,String> params = new HashMap<>();
//        楼栋
//        params.put("projectId","YZ8600PEK01GMWYGMYJY");
//        params.put("parentId","YZ8600PEK01GMWYGMYJY");
//        params.put("resourcesType","resourcesType3");
//        楼层
//        params.put("projectId","YZ8600PEK01GMWYGMYJY");
//        params.put("parentId","YZ8600PEK01GMWYGMYJY001002SX");
//        params.put("resourcesType","resourcesType5");
//        房间
//        params.put("projectId","YZ8600PEK01GMWYGMYJY");
//        params.put("parentId","YZ8600PEK01GMWYGMYJY001002SX002");
//        params.put("resourcesType","resourcesType6");
//        bean.getThirdAddress(params);
//        查类型
//        params.put("project_id","YZ8600PEK01GMWYGMYJY");
//        params.put("record_type","1");
//        bean.getThirdCategories(params);
    }

}
