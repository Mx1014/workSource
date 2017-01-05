package com.everhomes.pmtask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebService;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebServicePortType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.pmtask.PmTaskAttachmentDTO;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TechparkSynchronizedData implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TechparkSynchronizedData.class);
	
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
    private ContentServerService contentServerService;
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
	private CategoryProvider categoryProvider;
	@Autowired
    private BigCollectionProvider bigCollectionProvider;
	
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	private Long taskId;
	
	public TechparkSynchronizedData(final String taskId) {
		this.taskId = Long.valueOf(taskId);
	}
	
	@Override
	public void run() {
		User user = UserContext.current().getUser();
		PmTask task = pmTaskProvider.findTaskById(taskId);
		Category taskCategory = categoryProvider.findCategoryById(task.getTaskCategoryId());
		Category category = null;
		if(null != task.getCategoryId())
			category = categoryProvider.findCategoryById(task.getCategoryId());
		
		//查询图片
//		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
//		List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
//			PmTaskAttachmentDTO attachmentDto = ConvertHelper.convert(r, PmTaskAttachmentDTO.class);
//			
//			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
//	                EntityType.USER.getCode(), r.getCreatorUid());
//			attachmentDto.setContentUrl(contentUrl);
//			return attachmentDto;
//		}).collect(Collectors.toList());						
		
		synchronizedData(task, /*attachmentDtos,*/ taskCategory, category);
	}

	public void synchronizedData(PmTask task, /*List<PmTaskAttachmentDTO> attachments,*/ Category taskCategory, Category category) {
		JSONObject param = new JSONObject();
		String content = task.getContent();
		param.put("fileFlag", "1");
		param.put("fileTitle", content.length()<=5?content:content.substring(0, 5)+"...");
		
		JSONArray headContent = new JSONArray();
		JSONObject head1 = new JSONObject();
		if(null == task.getOrganizationId() || task.getOrganizationId() ==0 ){
			Organization organization = organizationProvider.findOrganizationById(task.getAddressOrgId());
			OrganizationMember orgMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getCreatorUid(), task.getAddressOrgId());
			if(null != orgMember) {
				head1.put("userName", orgMember.getContactName());
				head1.put("userId", orgMember.getTargetId());
				head1.put("phone", orgMember.getContactToken());
				head1.put("company", organization.getName());
				
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
				
				param.put("submitUserId", orgMember.getContactToken());
			}
		}
		
		headContent.add(head1);
		
		JSONArray formContent = new JSONArray();
		JSONObject form = new JSONObject();
		if(null != task.getAddressId()) {
			Address address = addressProvider.findAddressById(task.getAddressId());
			if(null != address)
				form.put("chooseStoried", address.getBuildingName());
			else
				form.put("chooseStoried", "");
		}else{
			form.put("chooseStoried", "");
		}
		
		form.put("serviceType", taskCategory.getName());
		form.put("serviceClassify", null != category?category.getName():"");
		form.put("serviceContent", content);
		form.put("fileType", "物业维修申请流程");
		form.put("taskUrgencyLevel", "普通");
		form.put("liaisonContent", "");
		form.put("backDate", dateSF.format(new Date()));
		formContent.add(form);
		
		JSONArray enclosure = new JSONArray();
		
		String key = "TechparkSynchronizedData-pmtask" + task.getId();
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String attachmentJson = valueOperations.get(key);
        List<AttachmentDescriptor> attachments = JSONObject.parseArray(attachmentJson, AttachmentDescriptor.class);
        LOGGER.error("Delete TechparkSynchronizedData key, key={}", key);
        redisTemplate.delete(key);
		
		if(null != attachments) {
			for(AttachmentDescriptor ad: attachments) {
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
				
//				String contentUrl = getResourceUrlByUir(ad.getContentUri(), EntityType.USER.getCode(), task.getCreatorUid());
				
				attachment.put("fileSuffix", fileSuffix);
				
				String fileContent = null;
				try {
					fileContent = getImageStr(ad.getContentUrl());
//					String fileContent1 = getURLImage(contentUrl);
//					
//					System.out.println(fileContent.equals(fileContent1));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(null != fileContent)
					attachment.put("fileContent", fileContent);
				else
					continue;
				enclosure.add(attachment);
			}
		}
		
		param.put("headContent", headContent);
		param.put("formContent", formContent);
		param.put("enclosure", enclosure);
		
        LOGGER.debug("Synchronized pmtask data to techpark oa param={}", param.toJSONString());

        WorkflowAppDraftWebService service = new WorkflowAppDraftWebService();
    	WorkflowAppDraftWebServicePortType port = service.getWorkflowAppDraftWebServiceHttpPort();
		String result = port.worflowAppDraft(param.toJSONString());
		
        LOGGER.debug("Synchronized pmtask data to techpark oa result={}", result);

	}
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

	
	public String getURLImage(String imageUrl) throws Exception {
		if(null == imageUrl)
			return null;
        //new一个URL对象  
        URL url = new URL(imageUrl);  
        //打开链接  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        //设置请求方式为"GET"  
        conn.setRequestMethod("GET");  
        //超时响应时间为5秒  
//        conn.setConnectTimeout(5 * 1000);  
        //通过输入流获取图片数据  
        InputStream inStream = conn.getInputStream();
        System.out.println(inStream.available());
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
        byte[] data = readInputStream(inStream);  
        BASE64Encoder encode = new BASE64Encoder();  
        String s = encode.encode(data);  
        return s;  
    }
	
	private byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  

        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    } 
	
	public String getImageStr(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            
            response = httpclient.execute(httpGet);
            
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
            	InputStream instream = entity.getContent();
            	System.out.println(instream.available());
            	
            	ByteArrayOutputStream outStream = new ByteArrayOutputStream();  

            	byte[] data = null;
                byte[] buffer = new byte[1024];  
                int len = 0;  
        			while( (len=instream.read(buffer)) != -1 ){  
        			    outStream.write(buffer, 0, len);  
        			}
        		
                
                data = outStream.toByteArray();  
        	    
        	    // 加密
        	    BASE64Encoder encoder = new BASE64Encoder();
        	    return encoder.encode(data);
            	
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
