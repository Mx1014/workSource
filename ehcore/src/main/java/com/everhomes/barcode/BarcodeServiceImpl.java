// @formatter:off
package com.everhomes.barcode;

import com.everhomes.BarCodeService.BarcodeModuleListener;
import com.everhomes.BarCodeService.BarcodeService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.barcode.BarcodeDTO;
import com.everhomes.rest.barcode.CheckBarcodeCommand;
import com.everhomes.rest.barcode.CheckForBizResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        //1、检查注册的模块
        if(listenerList != null){
            for(int i=0; i<listenerList.size(); i++){
                BarcodeDTO dto = listenerList.get(i).checkBarCode(cmd);
                if(dto != null){
                    return dto;
                }
            }
        }

        //2、检查手写的模块
        //2.1 电商
        BarcodeDTO dto = checkForBiz(cmd);
        if(dto != null){
            return dto;
        }

        return null;
    }

    private BarcodeDTO checkForBiz(CheckBarcodeCommand cmd){
        String host = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"stat.biz.server.url", "");
        String checkBarcodeApi =  this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"biz.zuolin.checkBarcode", "");

        Map<String, Object> para = new HashMap<>();
        para.put("body", cmd);
        try {
            LOGGER.info("checkForBiz host = {}, api = {}, param = {}", host, checkBarcodeApi, StringHelper.toJsonString(para));

            String result = HttpUtils.postJson(host + checkBarcodeApi, StringHelper.toJsonString(para), 2000);

            LOGGER.info("checkForBiz result = {}", result);

            if(result != null){
                CheckForBizResponse response = (CheckForBizResponse)StringHelper.fromJsonString(result, CheckForBizResponse.class);
                if(response != null &&  response.getBody() != null && response.getBody().getUrl() != null){
                    //因客户端要求，电商的链接在前面加上路由信息“zl://browser/i/”   add by yanjun 20170816
                    response.getBody().setUrl("zl://browser/i/" + response.getBody().getUrl());
                    return  response.getBody();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
