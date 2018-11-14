package com.everhomes.pmtask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.UserIdentifier;
import org.apache.commons.httpclient.HttpStatus;
import com.everhomes.configuration.ConfigurationProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.category.Category;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebService;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebServicePortType;
import com.everhomes.rest.pmtask.PmTaskAttachmentDTO;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TechparkSynchronizedAction implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TechparkSynchronizedAction.class);

	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
    private ContentServerService contentServerService;
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
	private ConfigurationProvider configProvider;

	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	private Long taskId;
	private Long targetId;
	private Long organizationId;
	
	public TechparkSynchronizedAction(final String json) {
		String[] ids = json.split(",");
		this.taskId = Long.valueOf(ids[0]);
		this.targetId = Long.valueOf(ids[1]);
		this.organizationId = Long.valueOf(ids[2]);

	}
	
	@Override
	public void run() {
		
		PmTask task = pmTaskProvider.findTaskById(taskId);
		
		UserContext userContext = UserContext.current();
		User user = userProvider.findUserById(task.getCreatorUid());
		userContext.setUser(user);
		userContext.setNamespaceId(task.getNamespaceId());
		userContext.setScheme("http");

		PmTaskCategory taskCategory = pmTaskProvider.findCategoryById(task.getTaskCategoryId());
		PmTaskCategory category = null;
		if(null != task.getCategoryId())
			category = pmTaskProvider.findCategoryById(task.getCategoryId());
		
		//查询图片
		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
		List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
			PmTaskAttachmentDTO attachmentDto = ConvertHelper.convert(r, PmTaskAttachmentDTO.class);
			
			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
	                EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			return attachmentDto;
		}).collect(Collectors.toList());					
		
		synchronizedData(task, attachmentDtos, taskCategory, category);
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
	
	public void synchronizedData(PmTask task, List<PmTaskAttachmentDTO> attachments, PmTaskCategory taskCategory, PmTaskCategory category) {
		JSONObject param = new JSONObject();
		String content = task.getContent();
		param.put("fileFlag", String.valueOf(null==task.getPriority()?1:task.getPriority()));
		param.put("fileTitle", task.getAddress() + "物业报修");
		
		Organization organization = organizationProvider.findOrganizationById(organizationId);

		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(task.getCreatorUid(), IdentifierType.MOBILE.getCode());

		param.put("submitUserPhone", userIdentifier.getIdentifierToken());

		OrganizationMember orgMember2 = organizationProvider.findOrganizationMemberByUIdAndOrgId(targetId, organizationId);

		param.put("acquiringDept", organization.getName());
		param.put("acquiringUser", orgMember2.getContactToken());
		param.put("serviceContent", content);
		param.put("backDate", dateSF.format(new Date()));
		
		JSONArray enclosure = new JSONArray();

		if(null != attachments) {
			for(PmTaskAttachmentDTO ad: attachments) {
				JSONObject attachment = new JSONObject();

				ContentServerResource resource = contentServerService.findResourceByUri(ad.getContentUri());
				String resourceName = resource.getResourceName();
				String fileSuffix = "jpg";
				if(StringUtils.isNotBlank(resourceName)) {
					int index = resourceName.lastIndexOf(".");
					if(index != -1)
						fileSuffix = resourceName.substring(index + 1, resourceName.length());
					attachment.put("fileName", resourceName);
				}else {
					attachment.put("fileName", "");
				}
				
				String contentUrl = getResourceUrlByUir(ad.getContentUri(), EntityType.USER.getCode(), task.getCreatorUid());
				
				attachment.put("fileSuffix", fileSuffix);
				
				String fileContent = null;
				try {
					fileContent = getImageStr(contentUrl);

				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null != fileContent)
					attachment.put("fileContent", fileContent);
				else
					continue;
				enclosure.add(attachment);
			}
		}
		
		param.put("enclosure", enclosure);

		if (LOGGER.isDebugEnabled())
        	LOGGER.debug("Synchronized pmTask data to techpark oa param={}", param.toJSONString());

		URL url = null;
		try {
			String value = configProvider.getValue("techpark.oa.url", "");
			url = new URL(value);
		} catch (MalformedURLException e) {
			LOGGER.error("Connect techpark oa failed", e);
		}
		WorkflowAppDraftWebService service = new WorkflowAppDraftWebService(url);
    	WorkflowAppDraftWebServicePortType port = service.getWorkflowAppDraftWebServiceHttpPort();
		String result = port.worflowAppDraft(param.toJSONString());

		if (LOGGER.isDebugEnabled())
        	LOGGER.debug("Synchronized pmTask data to techpark oa result={}", result);

	}

	public String getImageStr(String url) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            
            response = httpclient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();

			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					byte[] data = EntityUtils.toByteArray(entity);

					BASE64Encoder encoder = new BASE64Encoder();
					return encoder.encode(data);
				}
			}
        }catch (Exception e) {}
		finally {
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
    }
}
