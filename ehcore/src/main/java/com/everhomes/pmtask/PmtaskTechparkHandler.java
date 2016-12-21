package com.everhomes.pmtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.category.Category;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.parking.ketuo.EncryptUtil;
import com.everhomes.rest.pmtask.AttachmentDescriptor;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

@Component("pmtaskTechparkHandler")
public class PmtaskTechparkHandler {

	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	
	private CloseableHttpClient httpclient = HttpClients.createDefault();
	private static final String RECHARGE = "/api/pay/CardRecharge";
	
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
		formContent.add(form);
		
		JSONArray enclosure = new JSONArray();
		for(AttachmentDescriptor a: attachments) {
			JSONObject attachment = new JSONObject();
			attachment.put("fileName", "1");
			attachment.put("fileSuffix", "");
			attachment.put("fileContent", "");
			enclosure.add(attachment);
		}
		
	}
	
	public String post(JSONObject param, String type) {
		HttpPost httpPost = new HttpPost("http://oa.ssipc.com.cn:8890/oa/" + type);
		StringBuilder result = new StringBuilder();
		
        String key = "F7A0B971B199FD2A52468575";
        String iv = "20161124";
        String user = "ktapi";
        String pwd = "0306C3";
        String data = null;
		try {
			data = EncryptUtil.getEncString(param, key, iv);
		} catch (Exception e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Synchronized pmtask data to techpark error.");
		}
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("data", data));
		CloseableHttpResponse response = null;
		InputStream instream = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
			httpPost.addHeader("user", user);
			httpPost.addHeader("pwd", pwd);
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(instream,"utf8"));
				String s;
            	
            	while((s = reader.readLine()) != null){
            		result.append(s);
				}
			}
		} catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Synchronized pmtask data to techpark error.");
		}finally {
            try {
            	instream.close();
				response.close();
			} catch (IOException e) {
			}
        }
		String json = result.toString();
		
		return json;
	}
}
