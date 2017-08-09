package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.zjgk.ZjgkJsonEntity;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.pmtask.PmTaskAttachmentDTO;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/7/17.
 */
@Component(ZJGKHandoverTaskHandler.HANDOVER_VENDOR_PREFIX + HandoverTaskHandler.ZJGK)
public class ZJGKHandoverTaskHandler implements HandoverTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZJGKHandoverTaskHandler.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private CategoryProvider categoryProvider;

    @Autowired
    private PmTaskProvider pmTaskProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private FlowEventLogProvider flowEventLogProvider;


    @Override
    public void handoverTaskToTrd(PmTask task) {
        String url = configProvider.getValue("pmtask.zjgk.url", "");
        Map<String, String> params = generateParams(task);

        ZjgkJsonEntity entity = new ZjgkJsonEntity();


        String jsonStr = null;
        try {
            jsonStr = HttpUtils.post(url, params, 20, "UTF-8");
            //向第张江认证。
            entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity>(){});
        } catch (Exception e) {
            e.printStackTrace();
            entity.setErrorDescription("请求失败");
        }

    }


    private Map<String, String> generateParams(PmTask task){
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
        Category category = categoryProvider.findCategoryById(task.getTaskCategoryId());
        if(category != null) {
            params.put("taskCategory", category.getName());
        } else {
            params.put("taskCategory", "");
        }

        Organization org = organizationProvider.findOrganizationById(task.getOrganizationId());
        if(org != null) {
            params.put("organizationName", org.getName());
        } else {
            params.put("organizationName", "");
        }

        params.put("manager","1");
        String day = sdf.format(task.getCreateTime());
        params.put("createTime", day);
        params.put("taskContent",task.getContent());
        //查询图片
        List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
        String attachmentUrls = convertAttachmentUrl(attachments);
        params.put("taskAttachmentUrl", attachmentUrls);
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
            params.put("manager", "");
        }
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
