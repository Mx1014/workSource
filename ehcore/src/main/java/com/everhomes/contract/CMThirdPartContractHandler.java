package com.everhomes.contract;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.openapi.ZjSyncdataBackup;
import com.everhomes.openapi.ZjSyncdataBackupProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.contract.CMDataObject;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.contract.EbeiContract;
import com.everhomes.rest.customer.EbeiJsonEntity;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.openapi.shenzhou.SyncFlag;
import com.everhomes.util.*;
import com.everhomes.util.xml.XMLToJSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by pengyu.huang on 2018/08/21.
 */
@Component(ThirdPartContractHandler.CONTRACT_PREFIX + "999929")
public class CMThirdPartContractHandler implements ThirdPartContractHandler{

    private final static Logger LOGGER = LoggerFactory.getLogger(EbeiThirdPartContractHandler.class);

    private static final String PAGE_SIZE = "20";
    private static final String SUCCESS_CODE = "0";
    private static final Integer NAMESPACE_ID = 999983;

    private static final String DispContract = "/DispContract";


    DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Override
    public void syncContractsFromThirdPart(String pageOffset, String date, String communityIdentifier, Long taskId, Long categoryId, Byte contractApplicationScene){
        Map<String, String> params= new HashMap<>();
        Map<String, String> codeString= new HashMap<>();

        /*if(communityIdentifier == null) {
            communityIdentifier = "";
        }
        params.put("projectId", communityIdentifier);
        if(pageOffset == null || "".equals(pageOffset)) {
            pageOffset = "1";
        }
        params.put("currentPage", pageOffset);
        params.put("pageSize", PAGE_SIZE);
*/
        if(date == null || "".equals(date)) {
            date = "1970-01-01";
        }
        params.put("Date", date);
        params.put("currentpage", pageOffset);

        String enterprises = null;
        String url = configurationProvider.getValue("RuiAnCM.sync.url", "");
//        String url = "http://183.62.222.87:5902/sf";
        try {
            String codeStr = JSONObject.toJSONString(params);
            codeString.put("CodeStr", codeStr);
            enterprises = HttpUtils.get(url+DispContract, codeString, 600, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("sync customer from RuiAnCM error: {}", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "sync customer from ebei error");
        }


        enterprises = enterprises.substring(enterprises.indexOf(">{")+1, enterprises.indexOf("</string>"));

        Map result = JSONObject.parseObject(enterprises);
        CMSyncObject cmSyncObject =
                (CMSyncObject) StringHelper.fromJsonString(enterprises, CMSyncObject.class);

        syncData(cmSyncObject, DataType.CONTRACT.getCode(), communityIdentifier);

        if(SUCCESS_CODE.equals(cmSyncObject.getErrorCode())) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String nowStr = sdf.format(now);
            if(date.compareTo(nowStr) < 0) {
                date = getNextDay(date, sdf);
                syncContractsFromThirdPart("1", date, communityIdentifier, taskId, categoryId, contractApplicationScene);
            }else{
            	//同步CM数据到物业账单表
            	
            	
            }

        }


        if(SUCCESS_CODE.equals(cmSyncObject.getErrorCode())) {
            List<CMDataObject> dtos = cmSyncObject.getData();
            if(dtos != null && dtos.size() > 0) {
                syncData(cmSyncObject, DataType.CONTRACT.getCode(), communityIdentifier);

                //数据有下一页则继续请求
                if(!cmSyncObject.getTotalpage().equals(cmSyncObject.getCurrentpage())) {
                    syncContractsFromThirdPart(String.valueOf(Integer.valueOf(cmSyncObject.getCurrentpage()) + 1), date, communityIdentifier, taskId, categoryId, contractApplicationScene);
                }
            }

            //如果到最后一页了，则开始更新到我们数据库中
            if(cmSyncObject.getTotalpage().equals(cmSyncObject.getCurrentpage())) {
                //syncDataToDb(DataType.CONTRACT.getCode(), communityIdentifier, taskId, categoryId, contractApplicationScene);
            }
        }


        /*EbeiJsonEntity<List<EbeiContract>> entity = JSONObject.parseObject(enterprises, new TypeReference<EbeiJsonEntity<List<EbeiContract>>>(){});

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

    public static void main(String[] args) {
        System.out.println(String.valueOf(Integer.valueOf("1") + 1));
    }


    private String getNextDay(String date, SimpleDateFormat sdf){
        try {
            Date oldDate = sdf.parse(date);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(oldDate);
            calendar.add(Calendar.DATE, 1);
            oldDate = calendar.getTime();
            date = sdf.format(oldDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void syncData(CMSyncObject cmObj, Byte dataType, String communityIdentifier) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("ebei syncData: dataType: {}, communityIdentifier: {}", dataType, communityIdentifier);
        }

        ZjSyncdataBackup backup = new ZjSyncdataBackup();
        backup.setNamespaceId(NAMESPACE_ID);
        backup.setDataType(dataType);

        backup.setData(StringHelper.toJsonString(cmObj.getData()));
        backup.setStatus(CommonStatus.ACTIVE.getCode());
        backup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        backup.setCreatorUid(1L);
        backup.setUpdateTime(backup.getCreateTime());
        backup.setAllFlag(SyncFlag.PART.getCode());
        backup.setUpdateCommunity(communityIdentifier);

        zjSyncdataBackupProvider.createZjSyncdataBackup(backup);
    }
}
