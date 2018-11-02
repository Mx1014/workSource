package com.everhomes.pmtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.pmtask.archibus.*;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.ARCHIBUS)
public class ArchibusPmTaskHandle extends DefaultPmTaskHandle implements ApplicationListener<ContextRefreshedEvent> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ArchibusPmTaskHandle.class);

    public static final String THIRDURL = "http://www.xboxad.com:8080/archibus/webServices/fmWork?wsdl";

    public static final String pk_crop = "GMFW";

    public static final Integer perg_size = 5;

    public static final String file_type = "1";

    private static FmWorkDataService service;
    public FmWorkDataService getService(){
        if(service == null){
            FmWorkDataServiceImplServiceLocator locator = new FmWorkDataServiceImplServiceLocator();
            locator.setFmWorkDataServiceImplPortEndpointAddress(THIRDURL);
            try {
                service = locator.getFmWorkDataServiceImplPort();
            } catch (ServiceException e) {
                LOGGER.error("archibus web service init fail",e);
                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_SERVICE_INIT_FAIL,"archibus web service init fail");
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
        FmWorkDataService service = getService();
        String pk = req.getParameter("projectId");
        if(!"resourcesType3".equals(req.getParameter("resourcesType"))){
            pk = req.getParameter("parentId");
        }
        String json;
        try {
            json = service.getResources(req.getParameter("projectId"),pk,req.getParameter("resourcesType"));
            LOGGER.debug(json);
        } catch (RemoteException e) {
            LOGGER.error("getResources fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"getResources fail,params={}",req.getParameterMap());
        }
        ArchibusListEntity<ArchibusResource> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusResource>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object getThirdCategories(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json;
        try {
            json = service.getEventServiceType(req.getParameter("project_id"),req.getParameter("record_type"));
            LOGGER.debug(json);
        } catch (RemoteException e) {
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"getEventServiceType fail,params={}",req.getParameterMap());
        }
        ArchibusListEntity<ArchibusCategory> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusCategory>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object getThirdProjects(HttpServletRequest req){
        try {
            FmWorkDataService service = getService();

//            String json = service.areaInfo();
//            ArchibusListEntity<ArchibusArea> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusArea>>(){});
//            if(!result.isSuccess()){
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
//            }
            Integer namespaceId = UserContext.getCurrentNamespaceId();
            String areaId = configProvider.getValue("pmtask.archibus.areaid-" + namespaceId,"ALL");
//            for(ArchibusArea area : result.getData()){
//                if(area.getArea_name().contains("国贸")){
//                    areaId = area.getPk_area();
//                }
//            }

//            String json1 = service.getProjectInfo(req.getParameter("areaId"));
            String json1 = service.getProjectInfo(areaId);
            ArchibusListEntity<ArchibusProject> result1 = JSONObject.parseObject(json1,new TypeReference<ArchibusListEntity<ArchibusProject>>(){});

            if(result1.isSuccess()){
                return result1.getData();
            }
        } catch (RemoteException e) {
            LOGGER.error("getProjectInfo fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"getProjectInfo fail,params={}",req.getParameterMap());
        }
        return null;
    }

    @Override
    public Object createThirdTask(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json;

        PmTaskArchibusUserMapping user = getUser(req.getParameter("user_id"));
        LOGGER.debug("用户Id：" + user.getArchibusUid());
        if(user == null){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_USER_NOT_FOUND,"archibus user not found");
        }

        String user_id = user.getArchibusUid();

        String request_source = req.getParameter("request_source");
        String project_id = req.getParameter("project_id");
        String service_id = req.getParameter("service_id");
        String record_type = req.getParameter("record_type");
        String remarks = req.getParameter("remarks");
        String contack = req.getParameter("contack");

        String telephone = req.getParameter("telephone");
        String location =req.getParameter("location");
        String order_date = req.getParameter("order_date");
        String order_time = req.getParameter("order_time");

        if("4".equals(record_type)){
            if(StringUtils.isBlank(order_date)){
                SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd ");
                order_date = dateFormat.format(new Date());
            }
            if(StringUtils.isBlank(order_time)){
                order_time = "FAST";
            }
        }
        try {
            json = service.submitEvent(pk_crop, request_source, user_id, project_id,
                            service_id, record_type, remarks, contack,
                            telephone, location, order_date, order_time);
            LOGGER.debug(json);
        } catch (RemoteException e) {
            LOGGER.error("getProjectInfo fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"getProjectInfo fail,params={}",req.getParameterMap());
        }
        ArchibusEntity<JSONObject> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<JSONObject>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object listThirdTasks(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json;
        try {
            PmTaskArchibusUserMapping user = getUser(req.getParameter("user_id"));
//            req.getParameter("order_type") Integer.valueOf(req.getParameter("page_num"))
            json = service.eventList(user.getArchibusUid(), req.getParameter("project_id"), "",
                    req.getParameter("record_type"), 0, perg_size);
            LOGGER.debug(json);
        } catch (RemoteException e) {
            LOGGER.error("eventList fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"eventList fail,params={}",req.getParameterMap());
        }
        ArchibusListEntity<ArchibusTask> result = JSONObject.parseObject(json,new TypeReference<ArchibusListEntity<ArchibusTask>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object getThirdTaskDetail(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json;
        try {
            json = service.eventDetails(req.getParameter("order_id"));
            LOGGER.debug(json);
        } catch (RemoteException e) {
            LOGGER.error("eventDetails fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"eventDetails fail,params={}",req.getParameterMap());
        }
        ArchibusEntity<ArchibusTaskDetail> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<ArchibusTaskDetail>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object createThirdEvaluation(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json;
        try {
            json = service.submitEventEvaluation(req.getParameter("order_id"),req.getParameter("remarks"),req.getParameter("level"),
                    req.getParameter("level2"),req.getParameter("level3"),req.getParameter("level4"),"","");
            LOGGER.debug(json);
        } catch (RemoteException e) {
            LOGGER.error("submitEventEvaluation fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"submitEventEvaluation fail,params={}",req.getParameterMap());
        }
        ArchibusEntity<JSONObject> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<JSONObject>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object getThirdEvaluation(HttpServletRequest req) {
        FmWorkDataService service = getService();
        String json;
        try {
            json = service.eventEvaluationDetails(req.getParameter("order_id"));
            LOGGER.debug(json);
        } catch (RemoteException e) {
            LOGGER.error("eventEvaluationDetails fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"eventEvaluationDetails fail,params={}",req.getParameterMap());
        }
        ArchibusEntity<ArchibusEvaluation> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<ArchibusEvaluation>>(){});
        if(!result.isSuccess()){
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
        }
        return result.getData();
    }

    @Override
    public Object submitThirdAttachment(HttpServletRequest req) {
        String orderId = req.getParameter("order_id");
        String filePathStr = req.getParameter("file_path");
        LOGGER.debug("imgages path" + filePathStr);
        List<JSONObject> filePaths = JSONObject.parseObject(filePathStr,new TypeReference<ArrayList<JSONObject>>(){});
        FmWorkDataService service = getService();
        String json;
        try {
            for(JSONObject filePath : filePaths){
                String filename = UUID.randomUUID().toString();
                json = service.submitEventFile(pk_crop,orderId,filePath.getString("contentUrl"),filename,"",file_type);
                LOGGER.debug(json);
                ArchibusEntity<JSONObject> result = JSONObject.parseObject(json,new TypeReference<ArchibusEntity<JSONObject>>(){});
                if(!result.isSuccess()){
                    LOGGER.error("submitEventEvaluation fail " + result.getMsg());
//                  throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"request fail response={}",json);
                }
            }
        } catch (RemoteException e) {
            LOGGER.error("submitEventEvaluation fail",e);
//            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"submitEventFile fail,params={}",req.getParameterMap());
        }

        return "OK";
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
        String json;
        try {
            json = service.userInfo(dateTime,userId);
        } catch (RemoteException e) {
            LOGGER.error("userInfo fail",e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,PmTaskErrorCode.ERROR_REQUEST_ARCHIBUS_FAIL,"userInfo fail");
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
}
