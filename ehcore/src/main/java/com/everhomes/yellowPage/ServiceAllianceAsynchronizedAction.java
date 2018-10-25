// @formatter:off
package com.everhomes.yellowPage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;

import com.everhomes.rest.asset.TargetDTO;

import com.everhomes.rest.general_approval.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.file.FileUtils;
import com.everhomes.util.pdf.PdfUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceAllianceAsynchronizedAction implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceAsynchronizedAction.class);

	@Autowired
	private YellowPageProvider yellowPageProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	ServiceAllianceProviderProvider serviceAllianceProvidProvider;

	@Autowired
	GeneralApprovalProvider generalApprovalProvider;

	@Autowired
	GeneralApprovalValProvider generalApprovalValProvider;

	@Autowired
	GeneralFormProvider generalFormProvider;
	
	@Autowired
	ContentServerService contentServerService;
	

	private Integer namespaceId;
	private Long serviceAllianceId;
	private Long userId;
	private Long organizationId;
	private String formItemsInfo;

	private String jsonContents;
	
	
	private static List<String> DEFUALT_FIELDS = new ArrayList<String>();
	
	private void setDefaultFields() {
	    for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
	        DEFUALT_FIELDS.add(value.getCode());
	    }
	}
	
	public ServiceAllianceAsynchronizedAction(final String jsonContents) {
		// 赋值限制
		setDefaultFields();

		this.jsonContents = jsonContents;
		if (StringUtils.isBlank(jsonContents)) {
			return;
		}
		
		JSONObject json = JSONObject.parseObject(jsonContents);

		this.namespaceId = json.getInteger("namespaceId");
		this.serviceAllianceId = json.getLong("serviceAllianceId");
		this.userId = json.getLong("userId");
		this.organizationId = json.getLong("organizationId");
		this.formItemsInfo = json.getString("formItemsInfo");
	}

	private boolean checkParam() {
		// 公司id可以为空
		if (null == namespaceId || null == serviceAllianceId || null == userId) {
			return false;
		}

		return true;
	}

	@Override
	public void run() {

		LOGGER.info("mail send, param:" + this.jsonContents);

		if (!checkParam()) {
			LOGGER.error("mail param not valid");
			return;
		}

		// 获取服务
		ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(serviceAllianceId, null, null);
		if (null == sa) {
			LOGGER.error("mail send fail since sa not found");
			return;
		}

		sendMailMsg(sa);
	}

	/**
	 * @Function: ServiceAllianceFlowModuleListener.java
	 * @Description: 发送邮件通知
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年6月25日 下午2:00:29
	 *
	 */
	private void sendMailMsg(ServiceAlliances sa) {

		String serviceName = sa.getName(); 

		List<ServiceAllianceNotifyTargets> targets = yellowPageProvider.listNotifyTargets(namespaceId,
				ContactType.EMAIL.getCode(), sa.getParentId(), new CrossShardListingLocator(), Integer.MAX_VALUE);
		if (CollectionUtils.isEmpty(targets) && StringUtils.isBlank(sa.getEmail())) {
			return;
		}
		
		List<File> files = new ArrayList<>(10);
		GeneralForm form = null;
		List<PostApprovalFormItem> values = null;
		PostGeneralFormValCommand cmd = JSONObject.parseObject(this.formItemsInfo, PostGeneralFormValCommand.class);
		if (null != cmd) {
			form = this.generalFormProvider.getActiveGeneralFormByOriginId(sa.getFormId());
			values = cmd.getValues();
		}

		
		List<GeneralFormFieldDTO> fieldDTOs = null;
		if (null != form) {
			fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		}

		// 生成邮件body 和 附件
		String title = serviceName + "的申请";
		String body = buildMailBody(serviceName, values, fieldDTOs, files);

		// 生成pdf文件
		buildMailPdf(title + ".pdf", title, body, files);

		// 逐个进行发送
		List<String> totalMails = new ArrayList<>(10);
		for (ServiceAllianceNotifyTargets target : targets) {
			totalMails.add(target.getContactToken());
		}
		
		// 服务记录中的邮箱也要推送
		if (!StringUtils.isBlank(sa.getEmail())) {
			totalMails.add(sa.getEmail());
		}
		
		YellowPageUtils.sendMultiMails(namespaceId, totalMails, title, body, files);
	}

	private void buildMailPdf(String pdfFileName, String title, String htmlText, List<File> files) {
		
		// 过滤图片的样式，使得pdf上的图片为大图
		String pdfBody = pdfBodyFilter(htmlText);
		if (StringUtils.isBlank(pdfBody)) {
			return;
		}

		File file = PdfUtils.changeHtmlText2Pdf(pdfFileName, title, pdfBody);
		if (null != file) {
			files.add(file);
		}

	}

	private String pdfBodyFilter(String htmlText) {
		//过滤 img{height: 200px;width: 200px;}
		Pattern pattern = Pattern.compile("img\\s*\\{.*?\\}{1}");
		Matcher matcher = pattern.matcher(htmlText);
		return matcher.replaceAll("");
	}

	private String buildMailBody(String serviceName, List<PostApprovalFormItem> applyFormVals,
			List<GeneralFormFieldDTO> fieldDTOs, List<File> files) {

		StringBuilder mailHtmlBody = new StringBuilder();

		Map<String, Object> notifyMap = new HashMap<>(10); // 储存邮件正文的参数

		if (!CollectionUtils.isEmpty(applyFormVals)) {
			for (PostApprovalFormItem val : applyFormVals) {

				String fieldName = val.getFieldName();
				String fieldJsonStr = val.getFieldValue();
				GeneralFormFieldDTO fieldInfoDto = getFieldDTO(fieldName, fieldDTOs);
				if (null == fieldInfoDto) {
					continue;
				}

				// 固定字段处理
				if (DEFUALT_FIELDS.contains(fieldName)) {
					buildDefaultField(fieldName, fieldJsonStr, notifyMap);
					continue;
				}

				buildSingleValItem(val.getFieldType(), fieldJsonStr, fieldInfoDto.getFieldDisplayName(), mailHtmlBody,
						files);
			}
		}

		// 获取html固定信息
		buildFixeNotiFy(serviceName, this.userId, this.organizationId, notifyMap);

		// 获取html其他信息
		notifyMap.put("title", serviceName + "的申请");
		notifyMap.put("note", mailHtmlBody.toString());

		return buildMailContent(notifyMap);
	}

	/**
	 * @Description: 创建固定字段 预定人，手机号，公司名称，服务名称。
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @param flowCase
	 * @param category
	 * @param sa
	 * @date: 2018年6月27日 下午4:56:48
	 *
	 */
	private void buildFixeNotiFy(String serviceName, Long applyUserId, Long applierOrganizationId,
			Map<String, Object> notifyMap) {
		/*
		 * 发件人：<APP名称>
		 * 
		 * 邮件主题：{机构名称}的申请单
		 * 
		 * 邮件内容：
		 * 
		 * 预订人：XX
		 * 
		 * 手机号：XX
		 * 
		 * 公司名称：XX
		 * 
		 * 服务名称：{服务机构名称}
		 * 
		 * 自定义字段（单行文本）：xxx
		 * 
		 * 自定义字段（多行文本）：xxxxxxxxx
		 * 
		 * xxxxxxxxxxxxxxxxxxxxxxxxxxxx
		 * 
		 * 自定义字段（图片）：
		 *
		 */

		// 基本信息包括
		/*
		 * ${creatorName} ${creatorMobile} ${creatorOrganization} serviceOrgName
		 */
		// 获取电话和姓名
		TargetDTO userDto = userProvider.findUserTargetById(applyUserId);
		if (null != userDto) {
			notifyMap.put("creatorName", userDto.getTargetName());
			if (!StringUtils.isBlank(userDto.getUserIdentifier())) {
				notifyMap.put("creatorMobile", userDto.getUserIdentifier());
			}
		}

		// 获取公司名称 这里不做处理了。直接使用表单提供的值，见buildDefaultField
//		String organizationName = organizationProvider.getOrganizationNameById(applierOrganizationId);
//		if (!StringUtils.isBlank(organizationName)) {
//			notifyMap.put("creatorOrganization", organizationName);
//		}

		// 获取服务机构名称
		notifyMap.put("serviceOrgName", serviceName);
	}

	/**
	 * @Description: 根据单行表单值，构建需要的信息
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年6月27日 下午3:25:58
	 *
	 */
	private void buildSingleValItem(String fieldType, String fieldJsonStr, String displayName,
			StringBuilder mailHtmlBody, List<File> files) {

		// 根据每项值类型做各自的处理
		GeneralFormFieldType valType = GeneralFormFieldType.fromCode(fieldType);
		if (valType == GeneralFormFieldType.INTEGER_TEXT || valType == GeneralFormFieldType.MULTI_LINE_TEXT
				|| valType == GeneralFormFieldType.SINGLE_LINE_TEXT || valType == GeneralFormFieldType.DROP_BOX
				|| valType == GeneralFormFieldType.DATE || valType == GeneralFormFieldType.NUMBER_TEXT) {

			buildTextValItem(fieldJsonStr, displayName, mailHtmlBody);

		} else if (valType == GeneralFormFieldType.IMAGE) {

			buildImageValItem(fieldJsonStr, displayName, mailHtmlBody, files);

		} else if (valType == GeneralFormFieldType.SUBFORM) {

			buildSubFormValItem(fieldJsonStr, displayName, mailHtmlBody, files);

		} else if (valType == GeneralFormFieldType.FILE) {

			buildFileValItem(fieldJsonStr, displayName, mailHtmlBody, files);

		} else {
			buildOtherValItem(fieldJsonStr, displayName, mailHtmlBody);
		}
	}

	private void buildOtherValItem(String fieldJsonStr, String displayName, StringBuilder mailHtmlBody) {
		mailHtmlBody.append("<p>").append(displayName).append(": ").append(fieldJsonStr).append("</p>");
	}

	private void buildFileValItem(String fieldJsonStr, String displayName, StringBuilder mailHtmlBody,
			List<File> files) {
		// 获取值
		PostApprovalFormFileValue fileItem = getApprovalFileVal(fieldJsonStr);
		List<String> urls = fileItem.getUrls();
		List<PostApprovalFormFileDTO> fileDtos = fileItem.getFiles();

		// 正文显示
		mailHtmlBody.append("<p>").append(displayName);
		if (CollectionUtils.isEmpty(urls) || CollectionUtils.isEmpty(fileDtos)) {
			// 添加内容
			mailHtmlBody.append("(附件): 无").append("</p>");
			return;
		}
		mailHtmlBody.append("(附件): 见邮件附件").append("</p>");

		// 由fileDtos获取永久url
		for (int i = 0; i < fileDtos.size(); i++) {
			PostApprovalFormFileDTO dto = fileDtos.get(i);

			File file = null;
			String url = null;
			try {
				url = contentServerService.parseSharedUri(dto.getUri());
				file = FileUtils.getFileFormUrl(url, getFileOrImgName(displayName, i+1, dto.getFileName()));
			} catch (Exception e) {
				LOGGER.error("getFileFormUrl err trace: " + e.getStackTrace() + " msg:" + e.getMessage() + " uri:"
						+ dto.getUri() + " url:" + url);
			}

			if (null != file) {
				files.add(file);
			}
		}
	}
	
	private String getFileOrImgName(String fieldDisplayName, int index, String originalFileName){
		String originalName = StringUtils.isBlank(originalFileName) ? "" : originalFileName;
		return fieldDisplayName +"-"+index+"_"+originalName;
	}

	private void buildSubFormValItem(String fieldJsonStr, String displayName, StringBuilder mailHtmlBody,
			List<File> files) {

		// 添加内容
		PostApprovalFormSubformValue subForms = JSON.parseObject(fieldJsonStr, PostApprovalFormSubformValue.class);
		List<PostApprovalFormSubformItemValue> forms = subForms.getForms();
		if (CollectionUtils.isEmpty(forms)) {
			return;
		}

		int formCnt = forms.size();
		for (int i = 0; i < formCnt; i++) {
			PostApprovalFormSubformItemValue form = forms.get(i);
			// 表单名称
			mailHtmlBody.append("<p>").append(displayName);
			if (formCnt > 1) {
				mailHtmlBody.append(i + 1);
			}
			mailHtmlBody.append(": ").append("</p>");

			// 表单子项
			for (PostApprovalFormItem item : form.getValues()) {
				buildSingleValItem(item.getFieldType(), item.getFieldValue(), item.getFieldName(), mailHtmlBody, files);
			}
		}

	}

	private void buildImageValItem(String fieldJsonStr, String displayName, StringBuilder mailHtmlBody,
			List<File> files) {

		List<String> urlLists = getApprovalImgVal(fieldJsonStr);
		if (0 == urlLists.size()) {
			mailHtmlBody.append("<p>").append(displayName).append(":无</p>");
			return;
		}
		
		mailHtmlBody.append("<p>").append(displayName).append(": </p>");
		for (int i = 0; i < urlLists.size(); i++) {
			String url = urlLists.get(i);
			mailHtmlBody.append("<img style=\"margin-right:8px;\" src=\"");
			mailHtmlBody.append(url);
			mailHtmlBody.append("\" />"); // 必须已/结尾，否则转pdf会失败

			// 生成图片附件
			File file = null;
			try {
				file = FileUtils.getFileFormUrl(url, getFileOrImgName(displayName, i+1, "pic.png")); //默认都是png图片
			} catch (Exception e) {
				LOGGER.error(
						"getFileFormUrl err trace: " + e.getStackTrace() + " msg:" + e.getMessage() + " url:" + url);
			}

			if (null != file) {
				files.add(file);
			}
		}
	}

	private void buildTextValItem(String fieldJsonStr, String displayName, StringBuilder mailHtmlBody) {
		mailHtmlBody.append("<p>").append(displayName + ": " + getApprovalTextVal(fieldJsonStr)).append("</p>");
	}

	/**
	 * @Function: ServiceAllianceFlowModuleListener.java
	 * @Description: 该函数的功能描述
	 *
	 * @version: v1.0.0
	 * @author: 黄明波
	 * @date: 2018年6月27日 下午1:54:23
	 *
	 */
	private void buildDefaultField(String fieldName, String fieldJsonStr, Map<String, Object> notifyMap) {

		String textValue = getApprovalTextVal(fieldJsonStr);
		if (StringUtils.isBlank(textValue)) {
			return;
		}

		if (GeneralFormDataSourceType.USER_NAME.getCode().equals(fieldName) && null == notifyMap.get("creatorName")) {
			notifyMap.put("creatorName", textValue);
			return;
		}

		if (GeneralFormDataSourceType.USER_PHONE.getCode().equals(fieldName)
				&& null == notifyMap.get("creatorMobile")) {
			notifyMap.put("creatorMobile", textValue);
			return;
		}

		if (GeneralFormDataSourceType.USER_COMPANY.getCode().equals(fieldName)
				&& null == notifyMap.get("creatorOrganization")) {
			notifyMap.put("creatorOrganization", textValue);
			return;
		}
	}

	private String getApprovalTextVal(String fieldJsonStr) {
		 String textValue = JSON.parseObject(fieldJsonStr, PostApprovalFormTextValue.class).getText();
		 
		 return StringUtils.isBlank(textValue) ? "无" : textValue;
	}

	private List<String> getApprovalImgVal(String fieldJsonStr) {

		List<String> urlLists = new ArrayList<String>();
		
		PostApprovalFormImageValue images = JSONObject.parseObject(fieldJsonStr, PostApprovalFormImageValue.class);
		for (String uri : images.getUris()) {
			String url = contentServerService.parseSharedUri(uri);
			if (null != url) {
				urlLists.add(url);
			}
		}
		return urlLists;
	}

	private PostApprovalFormFileValue getApprovalFileVal(String fieldJsonStr) {
		return JSONObject.parseObject(fieldJsonStr, PostApprovalFormFileValue.class);
	}

	private String buildMailContent(Map<String, Object> notifyMap) {

		String notifyTextForOrg = localeTemplateService.getLocaleTemplateString(
				ServiceAllianceRequestNotificationTemplateCode.SCOPE,
				ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_ORG_ADMIN_IN_HTML, "zh_CN", notifyMap, "");

		return notifyTextForOrg;
	}
	
	private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
		
		if (CollectionUtils.isEmpty(fieldDTOs)) {
			return null;
		}
		
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        
        return null;
    }
	
	
	public static void main(String[] args) {
		
		List<String> tests = Arrays.asList("bobo");

	    for (String num : tests) {
	    	System.out.println(num);
		}
	}
}
