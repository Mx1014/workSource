package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.app.AppProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.zjgk.ZjgkJsonEntity;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/7/17.
 */
@Component(ZJGKHandoverTaskHandler.HANDOVER_VENDOR_PREFIX + HandoverTaskHandler.ZJGK)
public class ZJGKHandoverTaskHandler implements HandoverTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZJGKHandoverTaskHandler.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String CREATE_TASK = "/openapi/createTask";

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private PmTaskProvider pmTaskProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private FlowEventLogProvider flowEventLogProvider;

    @Autowired
    private AppProvider appProvider;


    @Override
    public void handoverTaskToTrd(PmTask task, String content, List<String> attachments) {
        LOGGER.info("ZJGKHandoverTaskHandler handoverTaskToTrd:");
        String url = configProvider.getValue(task.getNamespaceId(), "shenzhou.host.url", "");
        Map<String, String> params = generateParams(task, content, attachments);
        String jsonStr = postToShenzhou(params, url+CREATE_TASK, null);
        ZjgkJsonEntity entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity>(){});

    }
    private String postToShenzhou(Map<String, String> params, String url, Map<String, String> headers) {
        String json = null;
        try {
            Long beforeRequest = System.currentTimeMillis();
            json = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, HTTP.UTF_8);
            Long afterRequest = System.currentTimeMillis();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("request shenzhou url: {}, params: {}, json: {}, total elapse: {}", url, params, json, afterRequest-beforeRequest);
            }
        } catch (Exception e) {
            LOGGER.error("sync from shenzhou request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_REMOTE_INVOKE_FAIL,
                    "sync from shenzhou request error.");
        }
        return json;
    }


    private Map<String, String> generateParams(PmTask task, String content, List<String> attachments){
        Map<String, String> params= new HashMap<String,String>();
        params.put("taskNum", task.getId().toString());
        Community community = communityProvider.findCommunityById(task.getOwnerId());
        if(community != null) {
            params.put("ownerName",community.getName());
        } else {
            params.put("ownerName","");
        }

        params.put("taskSource",task.getSourceType());
        //查询服务类型
        PmTaskCategory category = pmTaskProvider.findCategoryById(task.getTaskCategoryId());
        if(category != null) {
            params.put("taskCategory", category.getName());
        } else {
            params.put("taskCategory", "");
        }

//        Organization org = organizationProvider.findOrganizationById(task.getOrganizationId());
        OrganizationCommunity communityOrg = organizationProvider.findOrganizationProperty(task.getOwnerId());
        if(communityOrg != null) {
            Organization org = organizationProvider.findOrganizationById(communityOrg.getOrganizationId());
            if(org != null) {
                params.put("organizationName", org.getName());
            }
        } else {
            params.put("organizationName", "");
        }

        String day = sdf.format(task.getCreateTime());
        params.put("createTime", day);
        if(StringUtils.isBlank(content)) {
            params.put("taskName", task.getContent());
            params.put("taskContent",task.getContent());
        } else {
            params.put("taskName", content);
            params.put("taskContent",content);
        }
        if(task.getRemark() != null) {
            params.put("remark", task.getRemark());
        } else {
            params.put("remark", "");
        }

        if(attachments != null && attachments.size() > 0) {
            params.put("taskAttachmentUrl", StringHelper.toJsonString(attachments));
        } else {
            //查询图片
            List<PmTaskAttachment> taskAttachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
            String attachmentUrls = convertAttachmentUrl(taskAttachments);
            params.put("taskAttachmentUrl", attachmentUrls);
        }

        List<FlowEventLog> logs = flowEventLogProvider.findStepEventLogs(task.getFlowCaseId());
        if(logs != null && logs.size() > 0) {
            Collections.sort(logs, (a, b) ->{
                if(a.getCreateTime().after(b.getCreateTime())) {
                    return 1;
                } else {
                    return -1;
                }
            });

            FlowEventLog log = logs.get(0);
            params.put("manager", log.getFlowUserName());
        } else {
            params.put("manager","0");
        }

        String appKey = configProvider.getValue(task.getNamespaceId(), "shenzhoushuma.app.key", "");
        String secretKey = configProvider.getValue(task.getNamespaceId(), "shenzhoushuma.secret.key", "");
        params.put("appKey", appKey);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        Integer randomNum = (int) (Math.random()*1000);
        params.put("nonce",randomNum+"");
        params.put("crypto", "sssss");
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature",signature);
        return params;
    }

    private String convertAttachmentUrl(List<PmTaskAttachment> attachments) {
        StringBuilder sb = new StringBuilder();
        attachments.forEach(r -> {
            String contentUrl = getResourceUrlByUir(r.getContentUri(),
                    EntityType.USER.getCode(), r.getCreatorUid());
            sb.append(contentUrl);
        });

        return sb.toString();
    }

    private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }

        return url;
    }

}
