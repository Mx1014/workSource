// @formatter:off
package com.everhomes.barcode;

import com.everhomes.BarCodeService.BarcodeModuleListener;
import com.everhomes.BarCodeService.BarcodeService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.rest.barcode.BarcodeDTO;
import com.everhomes.rest.barcode.CheckBarcodeCommand;
import com.everhomes.rest.oauth2client.OAuth2ClientApiResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BarcodeServiceImpl implements BarcodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeServiceImpl.class);

    @Autowired(required = false)
    List<BarcodeModuleListener> listenerList;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public BarcodeDTO checkBarcode(CheckBarcodeCommand cmd) {
        BarcodeDTO dto = null;
        //1、检查注册的模块
        if(listenerList != null){
            for(int i=0; i<listenerList.size(); i++){
                dto = listenerList.get(i).checkBarCode(cmd);
                if(dto != null){
                    return dto;
                }
            }
        }

        //2、检查手写的模块
        //2.1 电商
        dto = checkForBiz(cmd);
        if(dto != null){
            return dto;
        }



        return dto;
    }

    private BarcodeDTO checkForBiz(CheckBarcodeCommand cmd){
        String host = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"stat.biz.server.url", "");
        String checkBarcodeApi =  this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"biz.zuolin.checkBarcode", "");

        Map<String, Object> para = new HashMap<>();
        para.put("body", cmd);
        String result = null;
        try {
            result = HttpUtils.postJson(host + checkBarcodeApi, StringHelper.toJsonString(para), 2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        HttpResponseEntity responseEntity = RestCallTemplate.url(host + checkBarcodeApi)
////                .var("barcode", cmd.getBarcode())
//                .body(StringHelper.toJsonString(para))
//                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//                .respType(String.class)
//                .post();
//
//
//        if(responseEntity.getBody() != null){
////            return responseEntity.getBody();
//        }
        LOGGER.info("result = {}", result);
        return null;
    }
}
