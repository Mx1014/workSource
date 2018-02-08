// @formatter:off
package com.everhomes.message;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.message.PushMessageToAdminAndBusinessContactsCommand;
import com.everhomes.rest.messaging.SearchMessageRecordCommand;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.search.MessageRecordSearcher;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MessageServiceImpl implements MessageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private SmsProvider smsProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private MessageProvider messageProvider;

	@Autowired
	private MessageRecordSearcher messageRecordSearcher;
	
	@Override
	public void pushMessageToAdminAndBusinessContacts(PushMessageToAdminAndBusinessContactsCommand cmd) {
		if (cmd.getCommunityId() == null 
				|| StringUtils.isBlank(cmd.getContent())
				|| (TrueOrFalseFlag.fromCode(cmd.getBusinessContactFlag()) != TrueOrFalseFlag.TRUE && TrueOrFalseFlag.fromCode(cmd.getAdminFlag()) != TrueOrFalseFlag.TRUE)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, cmd=" + cmd);
		}
		
		// 1.找到这个园区所有的公司
		List<OrganizationCommunityRequest> organizationCommunityRequestList = organizationProvider.listOrganizationCommunityRequests(cmd.getCommunityId());
		if (organizationCommunityRequestList == null || organizationCommunityRequestList.isEmpty()) {
			return;
		}
		// 因为这个表里面入驻的企业会有重复的，应该是数据错误，所以要去重
		Set<Long> organizationIdSet = new HashSet<>();
		for (OrganizationCommunityRequest organizationCommunityRequest : organizationCommunityRequestList) {
			organizationIdSet.add(organizationCommunityRequest.getMemberId());
		}
		
		// 2.根据参数找到以上公司的所有的业务联系人或管理员，注意去除重复号码
		Set<String> phoneSet = new HashSet<>();
		for (Long organizationId : organizationIdSet) {
			//如果需要业务联系人
			if (TrueOrFalseFlag.fromCode(cmd.getBusinessContactFlag()) == TrueOrFalseFlag.TRUE) {
				List<String> phoneList = organizationService.getBusinessContactPhone(organizationId);
				if (phoneList != null && !phoneList.isEmpty()) {
					phoneSet.addAll(phoneList);
				}
			}
			
			//如果需要管理员
			if (TrueOrFalseFlag.fromCode(cmd.getAdminFlag()) == TrueOrFalseFlag.TRUE) {
				List<String> phoneList = organizationService.getAdminPhone(organizationId);
				if (phoneList != null && !phoneList.isEmpty()) {
					phoneSet.addAll(phoneList);
				}
			}
		}
		
		if (phoneSet.isEmpty()) {
			return;
		}
		
		// 3.给这些号码发短信
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		Integer namespaceId = community.getNamespaceId();
		if (namespaceId == null) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String[] phoneNumbers = phoneSet.toArray(new String[phoneSet.size()]);
		String templateScope = SmsTemplateCode.SCOPE;
		int templateId = SmsTemplateCode.PUSH_MESSAGE_TO_BUSINESS_AND_ADMIN_CODE;
		String templateLocale = UserContext.current().getUser().getLocale();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("send message parameters are: namespaceId="+namespaceId+", phoneNumbers="+Arrays.toString(phoneNumbers)+", templateScope="+templateScope+", code="+templateId);
		}
		
		smsProvider.sendSms(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, null);

	}

	@Override
	public void persistMessage(List<MessageRecord> records) {
		this.messageProvider.createMessageRecords(records);
	}

	@Override
	public List<MessageRecord> searchMessageRecord(SearchMessageRecordCommand cmd) {
		return messageRecordSearcher.query(cmd);
	}

	@Override
	public void syncMessageRecord() {
		messageRecordSearcher.syncMessageRecordIndexs();
	}

}