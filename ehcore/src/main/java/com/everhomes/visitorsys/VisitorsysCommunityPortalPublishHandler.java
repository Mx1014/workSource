// @formatter:off
package com.everhomes.visitorsys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.module.ServiceModuleEntry;
import com.everhomes.module.ServiceModuleEntryProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.visitorsys.VisitorsysConstant;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/18 10:52
 * 园区访客
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + VisitorsysConstant.COMMUNITY_MODULE_ID)
public class VisitorsysCommunityPortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorsysCommunityPortalPublishHandler.class);

    @Autowired
    private PortalVersionProvider portalVersionProvider;
    @Autowired
    private ServiceModuleAppProvider serviceModuleAppProvider;
    @Autowired
    private ServiceModuleEntryProvider serviceModuleEntryProvider;

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
        PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
        List<ServiceModuleApp> serviceModuleApps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, releaseVersion == null ? null : releaseVersion.getId(), VisitorsysConstant.COMMUNITY_MODULE_ID);
        if(serviceModuleApps!=null && serviceModuleApps.size()>0){
            return String.format(instanceConfig,namespaceId,serviceModuleApps.get(serviceModuleApps.size()-1).getOriginId());
        }
        return String.format(instanceConfig,namespaceId);
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
        JSONObject jsonObject = JSON.parseObject(instanceConfig);
        if (!StringUtils.isEmpty(jsonObject.getString("url")) && cmd.getModuleEntryId() != null) {
            String url = jsonObject.getString("url");
            String newUrl = "";
            ServiceModuleEntry serviceModuleEntry = this.serviceModuleEntryProvider.findById(cmd.getModuleEntryId());
            if (serviceModuleEntry != null) {
                String[] urls = url.split("[?]");
//              去掉原来写死的sceneType
                StringBuffer sb = new StringBuffer();
                if(urls[1].indexOf("#/") > -1){
                    String[] subUrls = urls[1].split("#/");
                    if(subUrls.length > 1){
                        String[] params = subUrls[0].split("[&]");
                        for(String param : params){
                            if(param.indexOf("sceneType") == -1){
                                sb.append(param);
                                sb.append("&");
                            }
                        }
                        int idx = sb.lastIndexOf("&") + 1;
                        if(idx == sb.length()){
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        sb.append("#/");
                        sb.append(subUrls[1]);
                        urls[1] = sb.toString();
                    }else{
                        LOGGER.error("VisitorsysCommunityPortalPublishHandler,url is lack of suffix");
                    }
                }

                if (urls.length > 1) {
                    jsonObject.remove("url");
                    newUrl = urls[0] + "?sceneType=" + serviceModuleEntry.getSceneType() + "&" + urls[1];
                    jsonObject.put("url",newUrl);
                    return jsonObject.toJSONString();
                }
            }
        }
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return actionData;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig, HandlerGetCustomTagCommand cmd) {
        return null;
    }

    @Override
    public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
        return null;
    }

    public static void main(String[] args) {
//        String url = "ns=%s&appId=%s&ownerType=community&sceneType=1#/home#sign_suffix";
        String url = "sceneType=1&ns=%s&appId=%s&ownerType=community#/home#sign_suffix";
//        url = url.replaceAll("(&sceneType=[^#]*)", "");
        StringBuffer sb = new StringBuffer();
        if(url.indexOf("#/") > -1){
            String[] subUrls = url.split("#/");
            if(subUrls.length > 1){
                String[] params = subUrls[0].split("[&]");
                for(String param : params){
                    if(param.indexOf("sceneType") == -1){
                        sb.append(param);
                        sb.append("&");
                    }
                }
                int idx = sb.lastIndexOf("&") + 1;
                if(idx == sb.length()){
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("#/");
                sb.append(subUrls[1]);
            }else{
                LOGGER.error("VisitorsysCommunityPortalPublishHandler,url is lack of suffix");
            }
        }
        System.out.println(sb.toString());

    }
}