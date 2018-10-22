package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.pmtask.archibus.*;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.util.RuntimeErrorException;
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

    public static final String pk_crop = "GMFW";

    public static final Integer perg_size = 1;

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
            ArchibusListEntity<ArchibusResource> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusResource>>(){});
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
        ArchibusListEntity<ArchibusCategory> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusCategory>>(){});
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
            ArchibusListEntity<ArchibusArea> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusArea>>(){});
            if(!result.isSuccess()){
    //                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
            }

            String areaId = "";
            for(ArchibusArea area : result.getData()){
                if(area.getArea_name().contains("国贸")){
                    areaId = area.getPk_area();
                }
            }

            String json1 = service.getProjectInfo(req.getParameter("areaId"));
            ArchibusListEntity<ArchibusProject> result1 = JSONObject.parseObject(json1,new TypeReference<ArchibusListEntity<ArchibusProject>>(){});

            if(result1.isSuccess()){
                return result1.getData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createThirdTask(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json = "";
        try {
            PmTaskArchibusUserMapping user = getUser(req.getParameter("user_id"));
            if(user == null){
                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_USER_NOT_FOUND,"archibus user not found");
            }
            service.submitEvent(pk_crop, req.getParameter("request_source"), user.getArchibusUid(), req.getParameter("project_id"),
                    req.getParameter("service_id"), req.getParameter("record_type"), req.getParameter("remarks"), req.getParameter("contack"),
                    req.getParameter("telephone"), req.getParameter("location"), req.getParameter("order_date"), req.getParameter("order_time"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        LOGGER.debug(json);
        ArchibusEntity<JSONObject> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<JSONObject>>(){});
        if(!result.isSuccess()){
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
        }
        return result.getData();
    }

    @Override
    public Object listThirdTasks(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json = "";
        try {
            getUser(req.getParameter("user_id"));
            json = service.eventList(req.getParameter("user_id"), req.getParameter("project_id"), req.getParameter("order_type"),
                    req.getParameter("record_type"), Integer.valueOf(req.getParameter("page_num")), perg_size);
            LOGGER.debug(json);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ArchibusListEntity<ArchibusTask> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusTask>>(){});
        if(!result.isSuccess()){
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
        }
        return result.getData();
    }

    @Override
    public Object getThirdTaskDetail(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.eventDetails(req.getParameter("order_id"));
            LOGGER.debug(json);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ArchibusEntity<ArchibusTaskDetail> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<ArchibusTaskDetail>>(){});
        if(!result.isSuccess()){
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.Error_)
        }
        return result.getData();
    }

    private PmTaskArchibusUserMapping getUser(String phone){
        PmTaskArchibusUserMapping user = pmTaskProvider.findArchibusUserbyPhone(phone);
        if(user == null){
            syncArchibusUser();
            user = pmTaskProvider.findArchibusUserbyPhone(phone);
        }
        return user;
    }

    private Object getThirdUsers(String dateTime,String userId){
        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.userInfo(dateTime,userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ArchibusListEntity<JSONObject> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<JSONObject>>(){});

        if(!result.isSuccess()){
            return null;
        }
        LOGGER.debug(json);
        return result.getData();
    }

    public void syncArchibusUser(){
        List<JSONObject> users = (List<JSONObject>) getThirdUsers("","");
        for (JSONObject user : users) {
            String archibusUid = user.getString("pk_user");
            PmTaskArchibusUserMapping localuser = pmTaskProvider.findArchibusUserbyArchibusId(archibusUid);
            if(localuser == null){
                PmTaskArchibusUserMapping newuser = new PmTaskArchibusUserMapping();
                newuser.setIdentifierToken(user.getString("phone"));
                newuser.setArchibusUid(archibusUid);
                pmTaskProvider.createArchibusUser(newuser);
            }
        }
    }

    public static void main(String[] args) {
        ArchibusPmTaskHandle bean = new ArchibusPmTaskHandle();
        Map<String,String> params = new HashMap<>();
        bean.getThirdUsers("","");
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
//      下单
//        params.put("request_source","taskSource7");
//        params.put("user_id","");
//        params.put("project_id","");
//        params.put("service_id","");
//        params.put("record_type","");
//        params.put("remarks","");
//        params.put("contack","");
//        params.put("telephone","");
//        params.put("location","");
//        params.put("order_date","");
//        params.put("order_time","");
//        bean.createThirdTask(params);
    }

}
