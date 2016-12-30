package com.everhomes.pmtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.category.Category;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebService;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebServicePortType;
import com.everhomes.rest.pmtask.AttachmentDescriptor;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

import sun.misc.BASE64Encoder;

@Component("pmtaskTechparkHandler")
public class PmtaskTechparkHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(PmtaskTechparkHandler.class);

	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
    private ContentServerService contentServerService;

	WorkflowAppDraftWebService service = new WorkflowAppDraftWebService();
	WorkflowAppDraftWebServicePortType port = service.getWorkflowAppDraftWebServiceHttpPort();
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	public void synchronizedData(PmTask task, List<AttachmentDescriptor> attachments, Category taskCategory, Category category) {
		JSONObject param = new JSONObject();
		param.put("fileFlag", "1");
		param.put("fileTitle", "");
		
		JSONArray headContent = new JSONArray();
		JSONObject head1 = new JSONObject();
		JSONObject head2 = new JSONObject();
		if(null == task.getOrganizationId() || task.getOrganizationId() ==0 ){
			Organization organization = organizationProvider.findOrganizationById(task.getAddressOrgId());
			OrganizationMember orgMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getCreatorUid(), task.getAddressOrgId());
			if(null != orgMember) {
				head1.put("userName", orgMember.getContactName());
				head1.put("userId", orgMember.getTargetId());
				head1.put("phone", orgMember.getContactToken());
				head1.put("company", organization.getName());
				
				head2.put("userName", orgMember.getContactName());
				head2.put("userId", orgMember.getTargetId());
				head2.put("phone", orgMember.getContactToken());
				head2.put("company", organization.getName());
				param.put("submitUserId", orgMember.getContactToken());
			}else {
				
			}
		}else {
			Organization organization = organizationProvider.findOrganizationById(task.getOrganizationId());
			OrganizationMember orgMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getCreatorUid(), task.getOrganizationId());
			if(null != orgMember) {
				head1.put("userName", orgMember.getContactName());
				head1.put("userId", orgMember.getTargetId());
				head1.put("phone", orgMember.getContactToken());
				head1.put("company", organization.getName());
				
				head2.put("userName", task.getRequestorName());
				head2.put("userId", "");
				head2.put("phone", task.getRequestorPhone());
				head2.put("company", "");
				param.put("submitUserId", orgMember.getContactToken());
			}
		}
		
		headContent.add(head1);
		headContent.add(head2);
		
		JSONArray formContent = new JSONArray();
		JSONObject form = new JSONObject();
		if(null != task.getAddressId()) {
			Address address = addressProvider.findAddressById(task.getAddressId());
			if(null != address)
				form.put("chooseStoried", address.getBuildingName());
			else
				form.put("chooseStoried", "");
		}
		
		form.put("serviceType", taskCategory.getName());
		form.put("serviceClassify", null != category?category.getName():"");
		form.put("serviceContent", task.getContent());
		form.put("fileType", "1");
		form.put("taskUrgencyLevel", "1");
		form.put("liaisonContent", "");
		form.put("backDate", dateSF.format(new Date()));
		formContent.add(form);
		
		JSONArray enclosure = new JSONArray();
		if(null != attachments) {
			for(AttachmentDescriptor ad: attachments) {
				JSONObject attachment = new JSONObject();
				ContentServerResource resource = contentServerService.findResourceByUri(ad.getContentUri());
				String resourceName = resource.getResourceName();
				String fileSuffix = "";
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
				
				InputStream in = get(contentUrl);
				if(null != in)
					attachment.put("fileContent", getImageStr(in));
				else
					continue;
				enclosure.add(attachment);
			}
		}
		
		param.put("headContent", headContent);
		param.put("formContent", formContent);
		param.put("enclosure", enclosure);
		
        LOGGER.debug("Synchronized pmtask data to techpark oa param={}", param.toJSONString());

		String result = port.worflowAppDraft(param.toJSONString());
		
        LOGGER.debug("Synchronized pmtask data to techpark oa result={}", result);

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
	
	public String getImageStr(InputStream inputStream) {
	    byte[] data = null;
	    try {
	        data = new byte[inputStream.available()];
	        inputStream.read(data);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // 加密
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(data);
	}
	
	public InputStream get(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            
            response = httpclient.execute(httpGet);
            
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
            	InputStream instream = entity.getContent();
            	return instream;
			}

        }catch (Exception e) {
        	
		} finally {
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
