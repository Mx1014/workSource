package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.pmtask.archibus.*;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.util.RuntimeErrorException;
import com.sun.media.jfxmedia.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.ARCHIBUS)
public class ArchibusPmTaskHandle{

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ArchibusPmTaskHandle.class);

    public static final String THIRDURL = "http://www.xboxad.com:8080/archibus/webServices/fmWork?wsdl";
//    private CloseableHttpClient httpclient = null;
//
//    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
//    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
//    //@PostConstruct
//    public void init() {
//        httpclient = HttpClients.createDefault();
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        if(contextRefreshedEvent.getApplicationContext().getParent() == null) {
//            init();
//        }
//    }

//    @Override
    public Object getThirdAddress() {

        try {
            FmWorkDataServiceImplServiceLocator locator = new FmWorkDataServiceImplServiceLocator();
            locator.setFmWorkDataServiceImplPortEndpointAddress(THIRDURL);
            FmWorkDataService service = locator.getFmWorkDataServiceImplPort();

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

            LOGGER.debug(json);


        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        ArchibusPmTaskHandle bean = new ArchibusPmTaskHandle();
        bean.getThirdAddress();
    }

}
