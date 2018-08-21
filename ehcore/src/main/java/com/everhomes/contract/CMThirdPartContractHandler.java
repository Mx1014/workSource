package com.everhomes.contract;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.contract.EbeiContract;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengyu.huang on 2018/08/21.
 */
@Component(ThirdPartContractHandler.CONTRACT_PREFIX + "999929")
public class CMThirdPartContractHandler implements ThirdPartContractHandler{

    private final static Logger LOGGER = LoggerFactory.getLogger(EbeiThirdPartContractHandler.class);

    private static final String PAGE_SIZE = "20";
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer NAMESPACE_ID = 999983;

    private static final String DispContract = "/DispContract";


    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void syncContractsFromThirdPart(String pageOffset, String date, String communityIdentifier, Long taskId, Long categoryId, Byte contractApplicationScene) {
        Map<String, String> params= new HashMap<String,String>();
        if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("projectId", communityIdentifier);
        if(pageOffset == null || "".equals(pageOffset)) {
            pageOffset = "1";
        }
        params.put("currentPage", pageOffset);
        params.put("pageSize", PAGE_SIZE);

        if(date == null || "".equals(date)) {
            date = "1970-01-01";
        }
        params.put("date", date);

        String enterprises = null;
        String url = configurationProvider.getValue("RuiAnCM.sync.url", "");
//        String url = "http://183.62.222.87:5902/sf";
        try {
            enterprises = HttpUtils.get(url+DispContract, params, 600, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("sync customer from RuiAnCM error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync customer from ebei error");
        }
/*
        EbeiJsonEntity<List<EbeiContract>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiContract>>>(){});

        if(SUCCESS_CODE.equals(entity.getResponseCode())) {
            List<EbeiContract> dtos = entity.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(entity, DataType.CONTRACT.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if(entity.getHasNextPag() == 1) {
                    syncContractsFromThirdPart(String.valueOf(entity.getCurrentPage()+1), version, communityIdentifier, taskId, categoryId, contractApplicationScene);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(entity.getHasNextPag() == 0) {
                syncDataToDb(DataType.CONTRACT.getCode(), communityIdentifier, taskId, categoryId, contractApplicationScene);
            }
        }*/

    }
}
