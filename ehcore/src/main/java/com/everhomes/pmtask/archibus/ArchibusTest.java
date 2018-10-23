package com.everhomes.pmtask.archibus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.pmtask.ArchibusPmTaskHandle;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ArchibusTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ArchibusTest.class);

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

    public Object getThirdAddress(Map<String,String> req) {

        try {

            FmWorkDataService service = getService();
            String pk = req.get("projectId");
            if(!"resourcesType3".equals(req.get("resourcesType"))){
                pk = req.get("parentId");
            }
            LOGGER.debug(pk);
            String json = service.getResources(req.get("projectId"),pk,req.get("resourcesType"));
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

    public Object getThirdCategories(Map<String,String> req) {

        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.getEventServiceType(req.get("project_id"),req.get("record_type"));
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

    public Object getThirdProjects(Map<String,String> req){
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

            String json1 = service.getProjectInfo(areaId);
            ArchibusListEntity<ArchibusProject> result1 = JSONObject.parseObject(json1,new TypeReference<ArchibusListEntity<ArchibusProject>>(){});

            if(result1.isSuccess()){
                return result1.getData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object createThirdTask(Map<String,String> req) {
        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.submitEvent(pk_crop, req.get("request_source"), req.get("user_id"), req.get("project_id"),
                    req.get("service_id"), req.get("record_type"), req.get("remarks"), req.get("contack"),
                    req.get("telephone"), req.get("location"), req.get("order_date"), req.get("order_time"));
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

    public Object listThirdTasks(Map<String,String> req) {
        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.eventList(req.get("user_id"), req.get("project_id"), req.get("order_type"),
                    req.get("record_type"), Integer.valueOf(req.get("page_num")), perg_size);
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

    public Object getThirdTaskDetail(Map<String,String> req) {
        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.eventDetails(req.get("order_id"));
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

    private Object getUsers(){
        FmWorkDataService service = getService();
        String json = "";
        try {
            json = service.userInfo("","");
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

    public static void main(String[] args) {
        ArchibusTest bean = new ArchibusTest();
        Map<String,String> params = new HashMap<>();
//        bean.getUsers();
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
//        params.put("record_type","4");
//        bean.getThirdCategories(params);
//      下单
        params.put("request_source","taskSource4");
        params.put("user_id","000000201807220FERQP");
        params.put("project_id","YZ8600PEK01GMWYGMYJY");
//        params.put("service_id","0000002017110205VF4D");
//        params.put("record_type","4");
        params.put("service_id","64");
        params.put("record_type","1");
        params.put("remarks","报修");
        params.put("contack","tom");
        params.put("telephone","15010499864");
        params.put("location","深圳");
//        params.put("order_date","2018-11-17");
//        params.put("order_time","FAST");
        bean.createThirdTask(params);
//      查询列表
//        params.put("user_id","000000201807220FERQP");
//        params.put("project_id","YZ8600PEK01GMWYGMYJY");
//        params.put("order_type","1");
//        params.put("record_type","5");
//        params.put("page_num","1");
//        bean.listThirdTasks(params);
//      查询明细
//        params.put("order_id","000000201810180AUEUM");
//        bean.getThirdTaskDetail(params);

    }
}
