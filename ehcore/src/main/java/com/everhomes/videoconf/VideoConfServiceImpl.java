package com.everhomes.videoconf;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.everhomes.scheduler.RunningFlag;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.onlinePay.PayStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.videoconf.AddSourceVideoConfAccountCommand;
import com.everhomes.rest.videoconf.AssignVideoConfAccountCommand;
import com.everhomes.rest.videoconf.BizConfDTO;
import com.everhomes.rest.videoconf.BizConfHolder;
import com.everhomes.rest.videoconf.BizConfStatus;
import com.everhomes.rest.videoconf.CancelVideoConfCommand;
import com.everhomes.rest.videoconf.CheckVideoConfTrialAccountCommand;
import com.everhomes.rest.videoconf.CheckVideoConfTrialAccountResponse;
import com.everhomes.rest.videoconf.ConfAccountOrderDTO;
import com.everhomes.rest.videoconf.ConfAccountStatus;
import com.everhomes.rest.videoconf.ConfCapacity;
import com.everhomes.rest.videoconf.ConfCategoryDTO;
import com.everhomes.rest.videoconf.ConfEnterprisesBuyChannel;
import com.everhomes.rest.videoconf.ConfOrderAccountDTO;
import com.everhomes.rest.videoconf.ConfOrderDTO;
import com.everhomes.rest.videoconf.ConfRecordDTO;
import com.everhomes.rest.videoconf.ConfReservationsDTO;
import com.everhomes.rest.videoconf.ConfServiceErrorCode;
import com.everhomes.rest.videoconf.ConfType;
import com.everhomes.rest.videoconf.CountAccountOrdersAndMonths;
import com.everhomes.rest.videoconf.CreateAccountOwnerCommand;
import com.everhomes.rest.videoconf.CreateConfAccountOrderCommand;
import com.everhomes.rest.videoconf.CreateConfAccountOrderOnlineCommand;
import com.everhomes.rest.videoconf.CreateInvoiceCommand;
import com.everhomes.rest.videoconf.CreateVideoConfInvitationCommand;
import com.everhomes.rest.videoconf.DeleteConfEnterpriseCommand;
import com.everhomes.rest.videoconf.DeleteReservationConfCommand;
import com.everhomes.rest.videoconf.DeleteSourceVideoConfAccountCommand;
import com.everhomes.rest.videoconf.DeleteVideoConfAccountCommand;
import com.everhomes.rest.videoconf.DeleteWarningContactorCommand;
import com.everhomes.rest.videoconf.EnterpriseLockStatusCommand;
import com.everhomes.rest.videoconf.GetBizConfHolder;
import com.everhomes.rest.videoconf.GetVideoConfHelpUrlResponse;
import com.everhomes.rest.videoconf.GetVideoConfTrialAccountCommand;
import com.everhomes.rest.videoconf.TrialFlag;
import com.everhomes.rest.videoconf.UpdateConfAccountPeriodCommand;
import com.everhomes.rest.videoconf.ExtendedSourceAccountPeriodCommand;
import com.everhomes.rest.videoconf.ExtendedVideoConfAccountPeriodCommand;
import com.everhomes.rest.videoconf.GetNamespaceIdListCommand;
import com.everhomes.rest.videoconf.GetNamespaceListResponse;
import com.everhomes.rest.videoconf.InvoiceDTO;
import com.everhomes.rest.videoconf.JoinVideoConfCommand;
import com.everhomes.rest.videoconf.JoinVideoConfResponse;
import com.everhomes.rest.videoconf.ListConfCategoryCommand;
import com.everhomes.rest.videoconf.ListConfCategoryResponse;
import com.everhomes.rest.videoconf.ListConfOrderAccountResponse;
import com.everhomes.rest.videoconf.ListInvoiceByOrderIdCommand;
import com.everhomes.rest.videoconf.ListOrderByAccountCommand;
import com.everhomes.rest.videoconf.ListOrderByAccountResponse;
import com.everhomes.rest.videoconf.ListReservationConfCommand;
import com.everhomes.rest.videoconf.ListReservationConfResponse;
import com.everhomes.rest.videoconf.ListConfAccountSaleRuleCommand;
import com.everhomes.rest.videoconf.ListSourceVideoConfAccountCommand;
import com.everhomes.rest.videoconf.ListSourceVideoConfAccountResponse;
import com.everhomes.rest.videoconf.ListUnassignAccountsByOrderCommand;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountByOrderIdCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountConfRecordCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountConfRecordResponse;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderResponse;
import com.everhomes.rest.videoconf.ListVideoConfAccountRuleResponse;
import com.everhomes.rest.videoconf.ListWarningContactorResponse;
import com.everhomes.rest.videoconf.OfflinePayBillCommand;
import com.everhomes.rest.videoconf.OrderBriefDTO;
import com.everhomes.rest.videoconf.ReserveVideoConfCommand;
import com.everhomes.rest.videoconf.SetWarningContactorCommand;
import com.everhomes.rest.videoconf.SourceVideoConfAccountDTO;
import com.everhomes.rest.videoconf.SourceVideoConfAccountStatistics;
import com.everhomes.rest.videoconf.StartVideoConfCommand;
import com.everhomes.rest.videoconf.StartVideoConfResponse;
import com.everhomes.rest.videoconf.TaxpayerType;
import com.everhomes.rest.videoconf.UnassignAccountResponse;
import com.everhomes.rest.videoconf.UpdateAccountOrderCommand;
import com.everhomes.rest.videoconf.UpdateConfAccountCategoriesCommand;
import com.everhomes.rest.videoconf.UpdateContactorCommand;
import com.everhomes.rest.videoconf.UpdateInvoiceCommand;
import com.everhomes.rest.videoconf.UpdateVideoConfAccountCommand;
import com.everhomes.rest.videoconf.UserAccountDTO;
import com.everhomes.rest.videoconf.VatType;
import com.everhomes.rest.videoconf.VerifyPurchaseAuthorityCommand;
import com.everhomes.rest.videoconf.VerifyPurchaseAuthorityResponse;
import com.everhomes.rest.videoconf.VerifyVideoConfAccountCommand;
import com.everhomes.rest.videoconf.VideoConfAccountRuleDTO;
import com.everhomes.rest.videoconf.VideoConfInvitationResponse;
import com.everhomes.rest.videoconf.VideoconfNotificationTemplateCode;
import com.everhomes.rest.videoconf.WarningContactorDTO;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ConfAccountSearcher;
import com.everhomes.search.ConfEnterpriseSearcher;
import com.everhomes.search.ConfOrderSearcher;
import com.everhomes.search.UserWithoutConfAccountSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import com.mysql.jdbc.StringUtils;


@Component
public class VideoConfServiceImpl implements VideoConfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VideoConfServiceImpl.class);

	private final String BIZCONFPATH = "http://api.bizvideo.cn/openapi";


	@Autowired
	private ScheduleProvider scheduleProvider;

	@Autowired
	private VideoConfProvider vcProvider;

	@Autowired
	private CategoryProvider categoryProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private SmsProvider smsProvider;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private EnterpriseProvider enterpriseProvider;

	@Autowired
	private EnterpriseContactProvider enterpriseContactProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private ConfAccountSearcher confAccountSearcher;

	@Autowired
	private UserWithoutConfAccountSearcher userWithoutConfAccountSearcher;

	@Autowired
	private ConfEnterpriseSearcher confEnterpriseSearcher;

	@Autowired
	private ConfOrderSearcher confOrderSearcher;

	@Autowired
	private NamespaceProvider nsProvider;

	@Autowired
	private AppProvider appProvider;

	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private OrganizationProvider organizationProvider;


	@Override
	public List<String> getConfCapacity() {

		Long parentId = CategoryConstants.CATEGORY_ID_CONF_CAPACITY;

		List<String> confCapacity = getCategoryName(parentId);

		return confCapacity;
	}

	@Override
	public List<String> getAccountType() {

		Long parentId = CategoryConstants.CATEGORY_ID_ACCOUNT_TYPE;

		List<String> accountType =getCategoryName(parentId);

		return accountType;
	}

	@Override
	public List<String> getConfType() {

		Long parentId = CategoryConstants.CATEGORY_ID_CONF_TYPE;

		List<String> confType = getCategoryName(parentId);

		return confType;
	}

	private List<String> getCategoryName(Long parentId) {
		User user = UserContext.current().getUser();
		Integer namespaceId = user.getNamespaceId();

		namespaceId = ( namespaceId == null ) ? 0 : namespaceId;

		Tuple[] orderBy = new Tuple[1];
		orderBy[0] = new Tuple<String, SortOrder>("default_order", SortOrder.ASC);
		List<Category> result = categoryProvider.listChildCategories(namespaceId, parentId, CategoryAdminStatus.ACTIVE,orderBy);
		if(CollectionUtils.isEmpty(result))
			return null;

		List<String> categoryName = result.stream().map(r -> {
			String name = r.getName();
			return name;
		}).collect(Collectors.toList());

		return categoryName;
	}
	@Override
	public VideoConfInvitationResponse createVideoConfInvitation(CreateVideoConfInvitationCommand cmd) {
		VideoConfInvitationResponse response = new VideoConfInvitationResponse();

		User user = UserContext.current().getUser();
		String userName = user.getNickName();

		ConfReservations reserveConf = vcProvider.findReservationConfById(cmd.getReserveConfId());
		if(reserveConf == null) {

		}
		String msgSubject = getMsgSubject(userName, reserveConf.getSubject());
		String confTime = getConfTime(reserveConf.getStartTime(), reserveConf.getDuration(), reserveConf.getTimeZone());
		String msgDescribtion = getMsgDescribtion(reserveConf.getCreatorPhone());

		String subject = utf8Togb2312(msgSubject);
		String body = utf8Togb2312(confTime + msgDescribtion);

		response.setSubject(subject);
		response.setBody(body);
		return response;

	}

	private String utf8Togb2312(String str){

		StringBuffer sb = new StringBuffer();

		for ( int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
				case '+' :
					sb.append( ' ' );
					break ;
				case '%' :
					try {
						sb.append(( char )Integer.parseInt (
								str.substring(i+1,i+3),16));
					}
					catch (NumberFormatException e) {
						throw new IllegalArgumentException();
					}
					i += 2;
					break ;
				default :
					sb.append(c);
					break ;
			}
		}
		String result = sb.toString();
		String res= null ;
		try {
			byte [] inputBytes = result.getBytes( "8859_1" );
			res= new String(inputBytes, "UTF-8" );
		}
		catch (Exception e){}
		return res;
	}

	private Timestamp addMinutes(Timestamp begin, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		calendar.add(Calendar.MINUTE, minutes);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		return time;
	}

	private String getConfTime(Timestamp confTime, int duration, String zone) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin", confTime);
		map.put("end", addMinutes(confTime, duration));
		map.put("zone", zone);

		String scope = VideoconfNotificationTemplateCode.SCOPE;
		int code = VideoconfNotificationTemplateCode.VIDEOCONF_CONFTIME;

		String subject = localeTemplateService.getLocaleTemplateString(scope, code, "zh_CN", map, "");

		return subject;
	}

	private String getMsgSubject(String userName, String confName) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("confName", confName);

		String scope = VideoconfNotificationTemplateCode.SCOPE;
		int code = VideoconfNotificationTemplateCode.VIDEOCONF_MSG_SUBJECT;

		String subject = localeTemplateService.getLocaleTemplateString(scope, code, "zh_CN", map, "");

		return subject;
	}

	private String getMsgDescribtion(String mobile) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);

		String scope = VideoconfNotificationTemplateCode.SCOPE;
		int code = VideoconfNotificationTemplateCode.VIDEOCONF_CONFDESCRIBTION;

		String subject = localeTemplateService.getLocaleTemplateString(scope, code, "zh_CN", map, "");

		return subject;
	}

	@Override
	public void createInvoice(CreateInvoiceCommand cmd) {

		ConfInvoices invoice = new ConfInvoices();

		invoice.setOrderId(cmd.getOrderId());
		invoice.setTaxpayerType(cmd.getTaxpayerType());
		invoice.setExpenseType(cmd.getExpenseType());
		invoice.setAddress(cmd.getAddress());
		invoice.setZipCode(cmd.getZipCode());
		invoice.setConsignee(cmd.getConsignee());
		invoice.setContact(cmd.getContact());
		invoice.setContractFlag(cmd.getContractFlag());

		if(cmd.getTaxpayerType() == TaxpayerType.COMPANY.getCode()) {
			invoice.setVatType(cmd.getVatType());
			invoice.setCompanyName(cmd.getCompanyName());

			if(cmd.getVatType() == VatType.GENERAL_TAXPAYER.getCode()) {
				invoice.setVatAddress(cmd.getTaxRegAddress());
				invoice.setVatCode(cmd.getTaxRegCertificateNum());
				invoice.setVatPhone(cmd.getTaxRegPhone());
				invoice.setVatBankName(cmd.getBankName());
				invoice.setVatBankAccount(cmd.getBankAccount());
			}
		}

		vcProvider.createInvoice(invoice);

	}

	@Override
	public void updateConfAccountCategories(UpdateConfAccountCategoriesCommand cmd) {

		ConfAccountCategories rule = new ConfAccountCategories();
		rule.setSingleAccountPrice(cmd.getSingleAccountPrice());
		rule.setMultipleAccountPrice(cmd.getMultipleAccountPrice());
		rule.setMinPeriod(cmd.getMinPeriod());
		rule.setDisplayFlag(cmd.getDisplayFlag());

		rule.setMultipleAccountThreshold(cmd.getMultipleAccountThreshold());;

		//0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话
		if(ConfCapacity.CONF_CAPACITY_25.getCode().equals(cmd.getConfCapacity())) {

			if(ConfType.CONF_TYPE_VIDEO_ONLY.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 0);
			}
			if(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 1);
			}
		}
		if(ConfCapacity.CONF_CAPACITY_100.getCode().equals(cmd.getConfCapacity())) {

			if(ConfType.CONF_TYPE_VIDEO_ONLY.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 2);
			}
			if(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 3);
			}
		}
		if(ConfCapacity.CONF_CAPACITY_6.getCode().equals(cmd.getConfCapacity())) {

			if(ConfType.CONF_TYPE_VIDEO_ONLY.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 4);
			}
		}
		if(ConfCapacity.CONF_CAPACITY_50.getCode().equals(cmd.getConfCapacity())) {

			if(ConfType.CONF_TYPE_VIDEO_ONLY.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 5);
			}
			if(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode().equals(cmd.getConfType())) {
				rule.setConfType((byte) 6);
			}
		}
		if(cmd.getId() != null) {
			rule.setId(cmd.getId());
			vcProvider.updateConfAccountCategories(rule);
		} else {
			vcProvider.createConfAccountCategories(rule);
		}

	}

	@Override
	public ListVideoConfAccountRuleResponse listConfAccountCategories(
			ListConfAccountSaleRuleCommand cmd) {
		ListVideoConfAccountRuleResponse response = new ListVideoConfAccountRuleResponse();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageOffset() == null ? 0 : (cmd.getPageOffset() - 1 ) * pageSize;

		List<VideoConfAccountRuleDTO> rules = vcProvider.listConfAccountCategories(cmd.getConfType(),
				cmd.getIsOnline(), offset, pageSize + 1).stream().map(r -> {

			return toRuleDto(r);
		}).collect(Collectors.toList());

		if(rules != null && rules.size() > pageSize) {
			rules.remove(rules.size() - 1);
			response.setNextPageOffset(cmd.getPageOffset() + 1);
		}

		response.setRules(rules);

		return response;
	}

	private VideoConfAccountRuleDTO toRuleDto(ConfAccountCategories rule) {

		VideoConfAccountRuleDTO ruleDto = new VideoConfAccountRuleDTO();

		if(rule != null) {
			ruleDto.setId(rule.getId());
			ruleDto.setMinPeriod(rule.getMinPeriod());
			ruleDto.setMultipleAccountThreshold(rule.getMultipleAccountThreshold());
			ruleDto.setMultipleAccountPrice(rule.getMultipleAccountPrice());
			ruleDto.setSingleAccountPrice(rule.getSingleAccountPrice());

			if(rule.getConfType() == 0) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_25.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
			}

			if(rule.getConfType() == 1) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_25.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
			}

			if(rule.getConfType() == 2) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_100.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
			}

			if(rule.getConfType() == 3) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_100.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
			}

			if(rule.getConfType() == 4) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_6.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
			}

			if(rule.getConfType() == 5) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_50.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
			}

			if(rule.getConfType() == 6) {
				ruleDto.setConfCapacity(ConfCapacity.CONF_CAPACITY_50.getCode());
				ruleDto.setConfType(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
			}

		}

		return ruleDto;
	}

	@Override
	public void setWarningContactor(SetWarningContactorCommand cmd) {

		WarningContacts contactor = new WarningContacts();
		contactor.setContactor(cmd.getContactor());
		contactor.setMobile(cmd.getMobile());
		contactor.setEmail(cmd.getEmail());

		if(cmd.getId() != null) {
			contactor.setId(cmd.getId());
			vcProvider.updateWarningContacts(contactor);
		} else {
			vcProvider.createWarningContacts(contactor);
		}

	}

	@Override
	public void deleteWarningContactor(DeleteWarningContactorCommand cmd) {

		vcProvider.deleteWarningContacts(cmd.getWarningContactorId());
	}

	@Override
	public ListWarningContactorResponse listWarningContactor(ListConfAccountSaleRuleCommand cmd) {

		ListWarningContactorResponse response = new ListWarningContactorResponse();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageOffset() == null ? 0 : (cmd.getPageOffset() - 1 ) * pageSize;

		List<WarningContactorDTO> contactors = vcProvider.listWarningContacts(offset, pageSize + 1).stream().map(r -> {

			return ConvertHelper.convert(r, WarningContactorDTO.class);
		}).collect(Collectors.toList());

		if(contactors != null && contactors.size() > pageSize) {
			contactors.remove(contactors.size() - 1);
			response.setNextPageOffset(cmd.getPageOffset() + 1);
		}

		response.setContactors(contactors);

		return response;
	}

	@Override
	public void addSourceVideoConfAccount(AddSourceVideoConfAccountCommand cmd) {

		ConfSourceAccounts account = new ConfSourceAccounts();
		account.setAccountCategoryId(cmd.getAccountCategory());
		account.setAccountName(cmd.getSourceAccount());
		account.setPassword(cmd.getPassword());
		account.setExpiredDate(new Timestamp(cmd.getValidDate()));

		vcProvider.createSourceVideoConfAccount(account);
	}

	@Override
	public void extendedSourceAccountPeriod(
			ExtendedSourceAccountPeriodCommand cmd) {

		ConfSourceAccounts account = vcProvider.findSourceAccountById(cmd.getSourceAccountId());
		account.setExpiredDate(new Timestamp(cmd.getValidDate()));

		vcProvider.extendedSourceAccountPeriod(account);
	}

	@Override
	public ListSourceVideoConfAccountResponse listSourceVideoConfAccount(
			ListSourceVideoConfAccountCommand cmd) {
		ListSourceVideoConfAccountResponse response = new ListSourceVideoConfAccountResponse();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageOffset() == null ? 0 : cmd.getPageOffset() * pageSize;

		List<ConfSourceAccounts> sourceAccounts = vcProvider.listSourceAccount(cmd.getSourceAccount(), null, offset, pageSize + 1);

		if(sourceAccounts != null && sourceAccounts.size() >= 0){
			List<SourceVideoConfAccountDTO> accounts = sourceAccounts.stream().map(r -> {
				ConfAccounts account = vcProvider.findAccountByAssignedSourceId(r.getId());
				if(account != null) {
					r.setOccupyAccountId(account.getId());
					if(account.getId() != null) {
						r.setOccupyFlag((byte) 1);
						ConfConferences conf = vcProvider.findConfConferencesById(account.getAssignedConfId());
						if(conf != null)
							r.setConfId(conf.getMeetingNo());
					}
				}

				return toSourceAccounDto(r);
			}).collect(Collectors.toList());

			if(accounts != null && accounts.size() > pageSize) {
				accounts.remove(accounts.size() - 1);
				Integer pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset()+1;
				response.setNextPageOffset(pageOffset);
			}

			response.setSourceAccounts(accounts);
		}
		return response;
	}

	private SourceVideoConfAccountDTO toSourceAccounDto(ConfSourceAccounts account) {

		SourceVideoConfAccountDTO accountDto = new SourceVideoConfAccountDTO();

		accountDto.setId(account.getId());
		accountDto.setSourceAccount(account.getAccountName());
		accountDto.setPassword(account.getPassword());
		accountDto.setStatus(account.getStatus());
		accountDto.setValidDate(account.getExpiredDate());

		accountDto.setOccupyAccountId(account.getOccupyAccountId());
		if(account.getOccupyAccountId() != null && account.getOccupyFlag() == 1) {
			accountDto.setOccupyFlag((byte) 1);
			accountDto.setConfId(account.getConfId());
			ConfAccounts occupyAccount = vcProvider.findVideoconfAccountById(account.getOccupyAccountId());
			if(occupyAccount != null && occupyAccount.getOwnerId() != null && occupyAccount.getOwnerId() != 0) {
				UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(occupyAccount.getOwnerId(), IdentifierType.MOBILE.getCode());
				if(identifier != null)
					accountDto.setOccupyIdentifierToken(identifier.getIdentifierToken());
			}
		} else {
			accountDto.setOccupyFlag((byte) 0);
		}

		ConfAccountCategories category = vcProvider.findAccountCategoriesById(account.getAccountCategoryId());
		if(category.getConfType() == 0) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_25.getCode()+ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
		}

		if(category.getConfType() == 1) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_25.getCode()+ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
		}

		if(category.getConfType() == 2) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_100.getCode()+ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
		}

		if(category.getConfType() == 3) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_100.getCode()+ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
		}

		if(category.getConfType() == 4) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_6.getCode()+ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
		}

		if(category.getConfType() == 5) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_50.getCode()+ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
		}

		if(category.getConfType() == 6) {
			accountDto.setConfType(ConfCapacity.CONF_CAPACITY_50.getCode()+ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
		}

		return accountDto;

	}

//	@Override
//	public ListEnterpriseWithVideoConfAccountResponse listEnterpriseWithVideoConfAccount(
//			ListEnterpriseWithVideoConfAccountCommand cmd) {
//
//		ListEnterpriseWithVideoConfAccountResponse response = new ListEnterpriseWithVideoConfAccountResponse();
//
//		CrossShardListingLocator locator=new CrossShardListingLocator();
//	    locator.setAnchor(cmd.getPageAnchor());
//	    int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//
//	    List<ConfEnterprises> enterprises = vcProvider.listEnterpriseWithVideoConfAccount(null, cmd.getStatus(), locator, pageSize+1);
//	    Long nextPageAnchor = null;
//		if(enterprises != null && enterprises.size() > pageSize) {
//			enterprises.remove(enterprises.size() - 1);
//			nextPageAnchor = enterprises.get(enterprises.size() -1).getId();
//		}
//		response.setNextPageAnchor(nextPageAnchor);
//
//		List<EnterpriseConfAccountDTO> enterpriseDto = enterprises.stream().map(r -> {
//
//	    	EnterpriseConfAccountDTO dto = new EnterpriseConfAccountDTO();
//	    	dto.setId(r.getId());
//	    	dto.setEnterpriseId(r.getEnterpriseId());
//	    	Enterprise enterprise = enterpriseProvider.findEnterpriseById(r.getEnterpriseId());
//	    	if(!StringUtils.isNullOrEmpty(cmd.getKeyword())) {
//	    		if(!cmd.getKeyword().equals(enterprise.getName()) && !cmd.getKeyword().equals(enterprise.getDisplayName())
//	    				&& !cmd.getKeyword().equals(enterprise.getId().toString()))
//	    			return null;
//	    	}
//	    	dto.setEnterpriseName(enterprise.getName());
//	    	dto.setEnterpriseDisplayName(enterprise.getDisplayName());
//	    	dto.setEnterpriseContactor(r.getContactName());
//	    	dto.setMobile(r.getContact());
//	    	if(r.getActiveAccountAmount() > 0)
//	    		dto.setUseStatus((byte) 0);
//	    	if(r.getActiveAccountAmount() == 0 && r.getTrialAccountAmount() > 0) {
//	    		dto.setUseStatus((byte) 1);
//	    	}
//	    	if(r.getActiveAccountAmount() == 0 && r.getTrialAccountAmount() == 0) {
//	    		dto.setUseStatus((byte) 2);
//	    	}
//
//	    	dto.setStatus(r.getStatus());
//	    	dto.setTotalAccount(r.getAccountAmount());
//	    	dto.setValidAccount(r.getActiveAccountAmount());
//	    	dto.setBuyChannel(r.getBuyChannel());
//	    	return dto;
//
//	    }).filter(t->t!=null).collect(Collectors.toList());
//	    response.setEnterpriseConfAccounts(enterpriseDto);
//
//		return response;
//	}


	@Override
	public void setEnterpriseLockStatus(EnterpriseLockStatusCommand cmd) {
		CrossShardListingLocator locator=new CrossShardListingLocator();
		int pageSize = Integer.MAX_VALUE;
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		List<ConfAccounts> accounts = vcProvider.listConfAccountsByEnterpriseId(cmd.getEnterpriseId(), null, locator, pageSize);

		if(cmd.getLockStatus() == 2) {
			enterprise.setStatus((byte) 2);
			if(accounts != null && accounts.size() > 0) {
				for(ConfAccounts account : accounts) {
					account.setStatus((byte) 2);
					vcProvider.updateConfAccounts(account);
				}
			}
		}

		if(cmd.getLockStatus() == 1) {
			enterprise.setStatus((byte) 0);
			if(accounts != null && accounts.size() > 0) {
				for(ConfAccounts account : accounts) {
					if(account.getExpiredDate().before(new Timestamp(DateHelper.currentGMTTime().getTime()))){
						account.setStatus((byte) 0);
					} else {
						account.setStatus((byte) 1);
						enterprise.setStatus((byte) 1);
					}
					vcProvider.updateConfAccounts(account);
				}

			}

		}

		vcProvider.updateVideoconfEnterprise(enterprise);
	}

//	@Override
//	public ListVideoConfAccountResponse listVideoConfAccount(
//			ListEnterpriseWithVideoConfAccountCommand cmd) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public ListVideoConfAccountConfRecordResponse listVideoConfAccountConfRecord(
			ListVideoConfAccountConfRecordCommand cmd) {

		ListVideoConfAccountConfRecordResponse response = new ListVideoConfAccountConfRecordResponse();

		int countConf = vcProvider.countConfByAccount(cmd.getAccountId());
		response.setConfCount(countConf);

		Long confTimeCount = vcProvider.listConfTimeByAccount(cmd.getAccountId());
		response.setConfTimeCount(confTimeCount);

		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<ConfConferences> conferences = vcProvider.listConfbyAccount(cmd.getAccountId(), locator, pageSize+1);

		if(conferences != null && conferences.size() > 0) {

			Long nextPageAnchor = null;
			if(conferences != null && conferences.size() > pageSize) {
				nextPageAnchor = conferences.get(conferences.size() -1).getId();
				conferences.remove(conferences.size() - 1);
			}
			response.setNextPageAnchor(nextPageAnchor);

			List<ConfRecordDTO> confRecords = conferences.stream().map(r -> {
				ConfRecordDTO confRecord = new ConfRecordDTO();
				confRecord.setConfId(r.getMeetingNo());
				confRecord.setConfDate(r.getStartTime());
				confRecord.setConfTime(r.getRealDuration());
				confRecord.setPeople(r.getMaxCount());

				return confRecord;
			}).collect(Collectors.toList());

			response.setConfRecords(confRecords);
		}

		return response;
	}


	@Override
	public void updateVideoConfAccount(UpdateVideoConfAccountCommand cmd) {

		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		account.setExpiredDate(new Timestamp(cmd.getValidDate()));

//		ConfAccountCategories category = vcProvider.findAccountCategoriesById(account.getAccountCategoryId());
		List<ConfAccountCategories> rules = vcProvider.listConfAccountCategories(cmd.getConfType(), null, 0, Integer.MAX_VALUE);
		if(rules != null && rules.size() > 0)
			account.setAccountCategoryId(rules.get(0).getId());

		if(account.getStatus() != 2) {
			if(account.getExpiredDate().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
				account.setStatus((byte) 0);
			}
			else {
				account.setStatus((byte) 1);
			}
		}
		account.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		vcProvider.updateConfAccounts(account);
		confAccountSearcher.feedDoc(account);

	}

	@Override
	public void deleteVideoConfAccount(DeleteVideoConfAccountCommand cmd) {

		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		account.setStatus((byte) 0);
		account.setDeleteUid(UserContext.current().getUser().getId());
		account.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		vcProvider.updateConfAccounts(account);
		confAccountSearcher.feedDoc(account);

		ConfEnterprises confEnterprise = vcProvider.findByEnterpriseId(account.getEnterpriseId());
		confEnterprise.setActiveAccountAmount(confEnterprise.getActiveAccountAmount() - 1);
		confEnterprise.setAccountAmount(confEnterprise.getAccountAmount() - 1);
		if(account.getAccountType() == 1) {
			confEnterprise.setTrialAccountAmount(confEnterprise.getTrialAccountAmount() - 1);
		}

		vcProvider.updateConfEnterprises(confEnterprise);
		confEnterpriseSearcher.feedDoc(confEnterprise);
	}

	@Override
	public void extendedVideoConfAccountPeriod(
			ExtendedVideoConfAccountPeriodCommand cmd) {
		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		account.setExpiredDate(new Timestamp(cmd.getValidDate()));

		if(account.getStatus() != 2) {
			if(account.getExpiredDate().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
				account.setStatus((byte) 0);
			}
			else {
				account.setStatus((byte) 1);
			}
		}

		vcProvider.updateConfAccounts(account);
		confAccountSearcher.feedDoc(account);

		ConfEnterprises confEnterprise = vcProvider.findByEnterpriseId(account.getEnterpriseId());
		int activeCount = vcProvider.countAccountsByEnterprise(account.getEnterpriseId(), null);
		int trialCount = vcProvider.countAccountsByEnterprise(account.getEnterpriseId(), (byte) 1);
		confEnterprise.setActiveAccountAmount(activeCount);
		confEnterprise.setTrialAccountAmount(trialCount);

		vcProvider.updateConfEnterprises(confEnterprise);
		confEnterpriseSearcher.feedDoc(confEnterprise);

	}

	@Override
	public ListVideoConfAccountOrderResponse listConfOrder(
			ListVideoConfAccountOrderCommand cmd) {

		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListVideoConfAccountOrderResponse response = new ListVideoConfAccountOrderResponse();

		List<ConfOrders> orders = vcProvider.findOrdersByEnterpriseId(cmd.getEnterpriseId(), locator, pageSize+1);
		if(orders != null) {

			Long nextPageAnchor = null;
			if(orders != null && orders.size() > pageSize) {
				orders.remove(orders.size() - 1);
				nextPageAnchor = orders.get(orders.size() -1).getId();
			}

			response.setNextPageAnchor(nextPageAnchor);

			List<ConfOrderDTO> confOrders = orders.stream().map(r -> {

				ConfOrderDTO dto = toConfOrderDTO(r);
				return dto;
			}).collect(Collectors.toList());

			response.setConfOrders(confOrders);
		}

		return response;
	}

	private ConfOrderDTO toConfOrderDTO(ConfOrders order) {

		ConfOrderDTO dto = new ConfOrderDTO();
		dto.setId(order.getId());
		dto.setEnterpriseId(order.getOwnerId());
		Enterprise enterprise = enterpriseProvider.findEnterpriseById(order.getOwnerId());
		if(enterprise != null)
			dto.setEnterpriseName(enterprise.getName());

		ConfEnterprises enterpriseContact = vcProvider.findByEnterpriseId(order.getOwnerId());
		if(enterpriseContact != null) {
			dto.setContactor(enterpriseContact.getContactName());
			dto.setMobile(enterpriseContact.getContact());
		}

//		ConfAccountCategories category = vcProvider.findAccountCategoriesById(order.getAccountCategoryId());
		dto.setCreateTime(order.getCreateTime());
		dto.setQuantity(order.getQuantity());
		dto.setPeriod(order.getPeriod());
		dto.setAmount(order.getAmount());
		int assignedAccount = vcProvider.countOrderAccounts(order.getId(), (byte) 1);
		dto.setAssignedQuantity(assignedAccount);
		dto.setAccountCategoryId(order.getAccountCategoryId());
		dto.setInvoiceFlag(order.getInvoiceReqFlag());
		dto.setMakeOutFlag(order.getInvoiceIssueFlag());
		dto.setBuyChannel(order.getOnlineFlag());
		return dto;
	}

//	@Override
//	public ConfOrderDTO listVideoConfAccountOrderInfoByOrderId(
//			ListVideoConfAccountOrderInfoByOrderIdCommand cmd) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public ListConfOrderAccountResponse listVideoConfAccountByOrderId(
			ListVideoConfAccountByOrderIdCommand cmd) {

		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<ConfOrderAccountMap> order = vcProvider.findOrderAccountByOrderId(cmd.getOrderId(), locator, pageSize+1,(byte)1);
		List<ConfAccounts> accounts = order.stream().map(r -> {
			ConfAccounts account = vcProvider.findVideoconfAccountById(r.getConfAccountId());
			return account;
		}).collect(Collectors.toList());
		Long nextPageAnchor = null;
		if(accounts != null && accounts.size() > pageSize) {
			accounts.remove(accounts.size() - 1);
			nextPageAnchor = accounts.get(accounts.size() -1).getId();
		}
		ListConfOrderAccountResponse response = new ListConfOrderAccountResponse();
		response.setNextPageAnchor(nextPageAnchor);

		List<ConfOrderAccountDTO> orderAccounts = accounts.stream().map(r -> {
			ConfOrderAccountDTO accountDto = new ConfOrderAccountDTO();
			accountDto.setId(r.getId());

			accountDto.setUserId(r.getOwnerId());
			Organization org = organizationProvider.findOrganizationById(r.getEnterpriseId());
			if(org != null) {
				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(r.getOwnerId(), org.getId());
				if (member != null) {
					accountDto.setUserName(member.getContactName());
					accountDto.setMobile(member.getContactToken());
					Organization dept = organizationProvider.findOrganizationById(member.getGroupId());
					if (dept != null) {
						accountDto.setDepartment(dept.getName());
					}
				}
			}
//			EnterpriseContact contact = enterpriseContactProvider.queryContactByUserId(r.getEnterpriseId(), r.getOwnerId());
//			if(contact != null) {
//				accountDto.setDepartment(contact.getStringTag1());
//				accountDto.setUserName(contact.getName());
//
//				List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
//				if(entry != null && entry.size() > 0) {
//					accountDto.setMobile(entry.get(0).getEntryValue());
//				}
//			}
			ConfAccountCategories category = vcProvider.findAccountCategoriesById(r.getAccountCategoryId());
			accountDto.setConfType(category.getConfType());
			return accountDto;
		}).collect(Collectors.toList());

		response.setOrderAccounts(orderAccounts);
		return response;
	}

	@Override
	public InvoiceDTO listInvoiceByOrderId(ListInvoiceByOrderIdCommand cmd) {

		InvoiceDTO invoice = vcProvider.getInvoiceByOrderId(cmd.getOrderId());
		return invoice;
	}

	@Override
	public void updateVideoConfAccountOrderInfo(UpdateAccountOrderCommand cmd) {
//		ConfEnterprises confEnterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
//		confEnterprise.setContactName(cmd.getContactor());
//		confEnterprise.setContact(cmd.getMobile());
//		vcProvider.updateVideoconfEnterprise(confEnterprise);
//		confEnterpriseSearcher.feedDoc(confEnterprise);

		ConfOrders order = vcProvider.findOredrById(cmd.getId());
		order.setBuyerContact(cmd.getMobile());
		order.setBuyerName(cmd.getContactor());
		order.setAmount(cmd.getAmount());
//		order.setInvoiceReqFlag(cmd.getInvoiceFlag());
		order.setInvoiceIssueFlag(cmd.getMakeOutFlag());
		order.setOnlineFlag(cmd.getBuyChannel());

		vcProvider.updateConfOrders(order);
		confOrderSearcher.feedDoc(order);

	}

//	@Override
//	public List<VideoConfAccountStatisticsDTO> getVideoConfAccountStatistics(
//			GetVideoConfAccountStatisticsCommand cmd) {
//
//		List<VideoConfAccountStatisticsDTO> statisticsDto = new ArrayList<VideoConfAccountStatisticsDTO>();
//		VideoConfAccountStatisticsDTO statistics0 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics1 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics2 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics3 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics4 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics5 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics6 = new VideoConfAccountStatisticsDTO();
//		VideoConfAccountStatisticsDTO statistics7 = new VideoConfAccountStatisticsDTO();
//
//		statistics0.setConfType("25方/仅视频");
//		Long confAccounts0 = vcProvider.countVideoconfAccountByConfType((byte) 0);
//		Long validAccounts0 = vcProvider.countValidAccountByConfType((byte) 0);
//		statistics0.setConfAccounts(confAccounts0);
//		statistics0.setValidConfAccount(validAccounts0);
//		if(cmd.getStartTime() != null && cmd.getEndTime() != null) {
//
//		}
//		statisticsDto.add(statistics0);
//
//		statistics1.setConfType("25方/支持电话");
//		Long confAccounts1 = vcProvider.countVideoconfAccountByConfType((byte) 1);
//		Long validAccounts1 = vcProvider.countValidAccountByConfType((byte) 1);
//		statistics1.setConfAccounts(confAccounts1);
//		statistics1.setValidConfAccount(validAccounts1);
//		if(cmd.getStartTime() == null) {
//
//		}
//		statisticsDto.add(statistics1);
//
//
//		statistics2.setConfType("100方/仅视频");
//		Long confAccounts2 = vcProvider.countVideoconfAccountByConfType((byte) 2);
//		Long validAccounts2 = vcProvider.countValidAccountByConfType((byte) 2);
//		statistics2.setConfAccounts(confAccounts2);
//		statistics2.setValidConfAccount(validAccounts2);
//		if(cmd.getStartTime() != null && cmd.getEndTime() != null) {
//
//		}
//		statisticsDto.add(statistics2);
//
//		statistics3.setConfType("100方/支持电话");
//		Long confAccounts3 = vcProvider.countVideoconfAccountByConfType((byte) 3);
//		Long validAccounts3 = vcProvider.countValidAccountByConfType((byte) 3);
//		statistics0.setConfAccounts(confAccounts3);
//		statistics0.setValidConfAccount(validAccounts3);
//		if(cmd.getStartTime() != null && cmd.getEndTime() != null) {
//
//		}
//		statisticsDto.add(statistics3);
//
//		statistics4.setConfType("25方");
//		statistics4.setConfAccounts(statistics0.getConfAccounts()+statistics1.getConfAccounts());
//		statistics4.setValidConfAccount(statistics0.getValidConfAccount()+statistics1.getValidConfAccount());
//		statistics4.setNewConfAccount(statistics0.getNewConfAccount()+statistics1.getNewConfAccount());
//		statisticsDto.add(statistics4);
//
//		statistics5.setConfType("100方");
//		statistics5.setConfAccounts(statistics2.getConfAccounts()+statistics3.getConfAccounts());
//		statistics5.setValidConfAccount(statistics2.getValidConfAccount()+statistics3.getValidConfAccount());
//		statistics5.setNewConfAccount(statistics2.getNewConfAccount()+statistics3.getNewConfAccount());
//		statisticsDto.add(statistics5);
//
//		statistics6.setConfType("仅视频");
//		statistics6.setConfAccounts(statistics0.getConfAccounts()+statistics2.getConfAccounts());
//		statistics6.setValidConfAccount(statistics0.getValidConfAccount()+statistics2.getValidConfAccount());
//		statistics6.setNewConfAccount(statistics0.getNewConfAccount()+statistics2.getNewConfAccount());
//		statisticsDto.add(statistics6);
//
//		statistics7.setConfType("支持电话");
//		statistics7.setConfAccounts(statistics3.getConfAccounts()+statistics1.getConfAccounts());
//		statistics7.setValidConfAccount(statistics3.getValidConfAccount()+statistics1.getValidConfAccount());
//		statistics7.setNewConfAccount(statistics3.getNewConfAccount()+statistics1.getNewConfAccount());
//		statisticsDto.add(statistics7);
//
//		return statisticsDto;
//	}

	@Override
	public List<SourceVideoConfAccountStatistics> getSourceVideoConfAccountStatistics() {

		List<SourceVideoConfAccountStatistics> statistics = new ArrayList<SourceVideoConfAccountStatistics>();

		for(Byte i = 0; i < 16; i++){
			SourceVideoConfAccountStatistics monitor = new SourceVideoConfAccountStatistics();
			monitor.setMonitoringPoints(i);
			monitor.setWarningLine(getEarlyWarningLine(i));
			monitor.setRatio(getRadio(i));
			statistics.add(monitor);
		}
		return statistics;
	}

//	@Override
//	public ListEnterpriseVideoConfAccountResponse listVideoConfAccountByEnterpriseId(
//			ListEnterpriseVideoConfAccountCommand cmd) {
//
//		ListEnterpriseVideoConfAccountResponse response = new ListEnterpriseVideoConfAccountResponse();
//		CrossShardListingLocator locator=new CrossShardListingLocator();
//	    locator.setAnchor(cmd.getPageAnchor());
//	    int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//		List<ConfAccounts> accounts = vcProvider.listConfAccountsByEnterpriseId(cmd.getEnterpriseId(), cmd.getStatus(), locator, pageSize+1);
//
//		Long nextPageAnchor = null;
//		if(accounts != null && accounts.size() > pageSize) {
//			accounts.remove(accounts.size() - 1);
//			nextPageAnchor = accounts.get(accounts.size() -1).getId();
//		}
//		response.setNextPageAnchor(nextPageAnchor);
//
//		List<ConfAccountDTO> confAccounts = accounts.stream().map(r -> {
//
//			ConfAccountDTO dto = new ConfAccountDTO();
//
//			dto.setId(r.getId());
//			dto.setUserId(r.getOwnerId());
//			dto.setValidDate(r.getExpiredDate());
//			dto.setUpdateDate(r.getUpdateTime());
//			if(r.getAccountType() == 1)
//				dto.setUserType((byte) 0);
//			else {
//				if(vcProvider.countOrdersByAccountId(r.getId()) == 1)
//					dto.setUserType((byte) 1);
//				else {
//					dto.setUserType((byte) 2);
//				}
//			}
//			dto.setStatus(r.getStatus());
//			if(new Timestamp(DateHelper.currentGMTTime().getTime()).after(r.getExpiredDate()))
//				dto.setValidFlag((byte) 0);
//			else {
//				dto.setValidFlag((byte) 1);
//			}
//			ConfAccountCategories category = vcProvider.findAccountCategoriesById(r.getAccountCategoryId());
//			if(category != null) {
//				dto.setAccountType(category.getChannelType());
//				dto.setConfType(category.getConfType());
//			}
//
//			Enterprise enterprise = enterpriseProvider.findEnterpriseById(r.getEnterpriseId());
//			dto.setEnterpriseName(enterprise.getName());
//			EnterpriseContact contact = enterpriseContactProvider.queryContactByUserId(r.getEnterpriseId(), r.getOwnerId());
//			if(contact != null) {
//
//				dto.setDepartment(contact.getStringTag1());
//				dto.setUserName(contact.getName());
//				List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
//				if(entry != null && entry.size() >0) {
//					dto.setMobile(entry.get(0).getEntryValue());
//				}
//			}
//			if(!StringUtils.isNullOrEmpty(cmd.getKeyword())) {
//	    		if(!cmd.getKeyword().equals(dto.getUserName()) && !cmd.getKeyword().equals(dto.getDepartment())
//	    				&& !cmd.getKeyword().equals(dto.getMobile()))
//	    			return null;
//	    	}
//			return dto;
//		}).filter(r->r!=null).collect(Collectors.toList());
//		response.setConfAccounts(confAccounts);
//
//		return response;
//	}

	private Timestamp addMonth(Timestamp lastUpdate, int months) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastUpdate);
		calendar.add(Calendar.MONTH, months);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());

		return newPeriod;
	}

	private Timestamp addDay(Timestamp lastUpdate, int days) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastUpdate);
		calendar.add(Calendar.DATE, days);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());

		return newPeriod;
	}

	@Override
	public void assignVideoConfAccount(
			AssignVideoConfAccountCommand cmd) {

		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();
		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		if(account == null) {
			LOGGER.error("account is not exist!");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ACCOUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ACCOUNT),
							UserContext.current().getUser().getLocale(),"account is not exist."));
		}

		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		if(now.before(addDay(account.getOwnTime(), 7))) {
			LOGGER.error("account has assigned in last 7 day!");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ASSIGN,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ASSIGN),
							UserContext.current().getUser().getLocale(),"account has assigned in last 7 days!"));
		}
		account.setOwnerId(cmd.getUserId());
		account.setUpdateTime(now);
		account.setOwnTime(now);
		vcProvider.updateConfAccounts(account);
		confAccountSearcher.feedDoc(account);

		ConfAccountHistories history = new ConfAccountHistories();
		history.setEnterpriseId(account.getEnterpriseId());
		history.setExpiredDate(account.getExpiredDate());
		history.setStatus(account.getStatus());
		history.setAccountCategoryId(account.getAccountCategoryId());
		history.setAccountType(account.getAccountType());
		history.setOwnerId(account.getOwnerId());
		history.setOwnTime(now);
		history.setCreatorUid(account.getCreatorUid());
		history.setCreateTime(account.getCreateTime());
		history.setOperatorUid(UserContext.current().getUser().getId());
		history.setOperationType("assign account");
		history.setOperateTime(now);
		history.setProcessDetails("");
		history.setNamespaceId(namespaceId);
		vcProvider.createConfAccountHistories(history);

	}

//	@Override
//	public String applyVideoConfAccount(ApplyVideoConfAccountCommand cmd) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public ListUsersWithoutVideoConfPrivilegeResponse listUsersWithoutVideoConfPrivilege(
//			ListUsersWithoutVideoConfPrivilegeCommand cmd) {
//		ListUsersWithoutVideoConfPrivilegeResponse response = new ListUsersWithoutVideoConfPrivilegeResponse();
//		List<Long> users = vcProvider.findUsersByEnterpriseId(cmd.getEnterpriseId());
//
//		List<EnterpriseContact> ec = new ArrayList<EnterpriseContact>();
//		List<EnterpriseContact> contact = enterpriseContactProvider.queryContactByEnterpriseId(cmd.getEnterpriseId(), cmd.getKeyword());
//		if(cmd.getPageAnchor() == null)
//			cmd.setPageAnchor(0L);
//		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
//
//		if(contact != null &contact.size() > 0) {
//				for(EnterpriseContact con : contact) {
//					if(!(users != null && users.contains(con.getUserId()))) {
//						if(con.getUserId() > cmd.getPageAnchor())
//							ec.add(con);
//					}
//
//				}
//		}
//
//
//		Long nextPageAnchor = null;
//		if(ec != null && ec.size() > pageSize) {
//			ec = ec.subList(0, pageSize);
//			ec.remove(ec.size() - 1);
//			nextPageAnchor = ec.get(ec.size() -1).getUserId();
//		}
//		response.setNextPageAnchor(nextPageAnchor);
//
//		List<EnterpriseUsersDTO> usersDto = ec.stream().map(r -> {
//			EnterpriseUsersDTO user = new EnterpriseUsersDTO();
//			user.setUserId(r.getUserId());
//			user.setUserName(r.getName());
//			user.setDepartment(r.getStringTag1());
//			user.setContactId(r.getId());
//			user.setEnterpriseId(r.getEnterpriseId());
//
//			List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(r);
//			if(entry != null && entry.size() >0)
//				user.setMobile(entry.get(0).getEntryValue());
//
//			return user;
//		}).collect(Collectors.toList());
//		response.setEnterpriseUsers(usersDto);
//
//		return response;
//	}

	@Override
	public UserAccountDTO verifyVideoConfAccount(
			VerifyVideoConfAccountCommand cmd) {
		User user = UserContext.current().getUser();
		UserAccountDTO userAccount = new UserAccountDTO();
		ConfAccounts account = new ConfAccounts();
		if(cmd.getEnterpriseId() != null && cmd.getEnterpriseId() != 0) {
			account = vcProvider.findAccountByUserIdAndEnterpriseId(user.getId(), cmd.getEnterpriseId());
		} else {
			account = vcProvider.findAccountByUserId(user.getId());
		}

		boolean privilege = rolePrivilegeService.checkAdministrators(cmd.getEnterpriseId());
		userAccount.setPurchaseAuthority(privilege);

		if(account != null) {
			userAccount.setAccountId(account.getId());
			userAccount.setStatus(account.getStatus());
			userAccount.setOccupyFlag(account.getAssignedFlag());

			if(userAccount.getOccupyFlag() == 1) {
				ConfConferences conf = vcProvider.findConfConferencesById(account.getAssignedConfId());
				if(conf != null) {
					userAccount.setConfId(conf.getMeetingNo());
				} else {
					userAccount.setOccupyFlag((byte) 0);

					account.setAssignedConfId(0L);
					account.setAssignedFlag((byte) 0);
					account.setAssignedSourceId(0L);
					account.setAssignedTime(null);
					vcProvider.updateConfAccounts(account);
				}
			}
		} else {
			userAccount.setAccountId(0L);
			userAccount.setStatus((byte) 0);
			userAccount.setOccupyFlag((byte) 2);
		}
		return userAccount;
	}

	@Scheduled(cron="0 0 2 * * ? ")
	@Override
	public void invalidConf() {
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			LOGGER.info("update invalid conference which longer than 24 hours.");
			Timestamp oneDayBefore = addHours(new Timestamp(DateHelper.currentGMTTime().getTime()), -24);
			List<ConfAccounts> occupiedAccounts = vcProvider.listOccupiedConfAccounts(oneDayBefore);
			if(occupiedAccounts != null && occupiedAccounts.size() > 0) {
				for(ConfAccounts account : occupiedAccounts) {
					if(account.getAssignedConfId() != null && account.getAssignedSourceId() != null) {
						CancelVideoConfCommand cancelCmd = new CancelVideoConfCommand();
						ConfConferences conf = vcProvider.findConfConferencesById(account.getAssignedConfId());
						if(conf != null)
							cancelCmd.setConfId(conf.getMeetingNo());
						cancelVideoConf(cancelCmd);
					}
				}
			}
		}
	}

	private Timestamp addHours(Timestamp begin, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());

		return time;
	}


	private int getConfStatus(String sourceAccountName, Long conferenceId) {
		String path = BIZCONFPATH + "/getConf";

		Long timestamp = DateHelper.currentGMTTime().getTime();
		String tokenString = sourceAccountName + "|" + configurationProvider.getValue(ConfigConstants.VIDEOCONF_SECRET_KEY, "0") + "|" + timestamp;
		String token = DigestUtils.md5Hex(tokenString);


		Map<String, String> sPara = new HashMap<String, String>() ;
		sPara.put("loginName", sourceAccountName);
		sPara.put("timeStamp", timestamp.toString());
		sPara.put("token", token);
		sPara.put("confId", conferenceId + "");

		NameValuePair[] param = generatNameValuePair(sPara);
		if(LOGGER.isDebugEnabled())
			LOGGER.info("getConfStatus, sourceAccountName=" + sourceAccountName + ", conferenceId="+ conferenceId + ", restUrl="+path+", param="+sPara);
		try {
			HttpClient httpClient = new HttpClient();
			HttpMethod method;
			method = postMethod(path, param);
			httpClient.executeMethod(method);

			String result = method.getResponseBodyAsString();
			System.out.println(result);
			String json = strDecode(result);
			if(LOGGER.isDebugEnabled())
				LOGGER.error("getConfStatus,json="+json);

			GetBizConfHolder resultHolder = GsonUtil.fromJson(json, GetBizConfHolder.class);

			if(resultHolder.getStatus() == 100){
				//confStatus: 0: 预约成功; 2: 正在召开; 3: 已结束; 9: 取消的会议
				BizConfStatus status = resultHolder.getData();
				return status.getConfStatus();
			}


		} catch (IOException e) {
			LOGGER.error("getConfStatus-error.sourceAccount="+sourceAccountName, e);
		}

		return -1;
	}

	@Override
	public void cancelVideoConf(CancelVideoConfCommand cmd) {

		ConfConferences conf = vcProvider.findConfConferencesByConfId(cmd.getConfId());
		if(conf == null) {
			LOGGER.info("cancelVideoConf, cmd=" + cmd + ", conf is not exist!");
			return ;
		}
		ConfSourceAccounts source = vcProvider.findSourceAccountById(conf.getSourceAccountId());
		if(source == null) {
			LOGGER.info("cancelVideoConf, cmd=" + cmd + ", source account is not exist!");
			return ;
		}
		//有几分钟的延迟且没有实际时间长度返回，先不轮询
//		boolean flag=true;
//		while(flag){
//			int status = getConfStatus(source.getAccountName(), conf.getConferenceId());
//			if(3 == status) {
//				flag = false;
//			} else {
//				//睡眠5秒钟
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}

		if(conf != null && conf.getStatus() != null && conf.getStatus() != 0) {
			conf.setStatus((byte) 0);
			conf.setEndTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			int minutes = (int) ((conf.getEndTime().getTime()-conf.getStartTime().getTime())/60000);
			conf.setRealDuration(minutes);

			vcProvider.updateConfConferences(conf);

			ConfAccounts account = vcProvider.findVideoconfAccountById(conf.getConfAccountId());
			if(account != null) {
				account.setAssignedConfId(0L);
				account.setAssignedFlag((byte) 0);
				account.setAssignedSourceId(0L);
				account.setAssignedTime(null);

				vcProvider.updateConfAccounts(account);
			}
		}
	}

	private static String strDecode(String json){
		String newJson="";
		try {
			newJson=URLDecoder.decode(json,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newJson;
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public StartVideoConfResponse startVideoConf(StartVideoConfCommand cmd) {
		StartVideoConfResponse response = new StartVideoConfResponse();

		int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();

		String path = BIZCONFPATH + "/confReservation";
		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		if(account != null && account.getStatus() == 1) {
			if(account.getAssignedSourceId() != null && account.getAssignedSourceId() != 0) {
				account.setAssignedConfId(0L);
				account.setAssignedFlag((byte) 0);
				account.setAssignedSourceId(0L);
				account.setAssignedTime(null);

				vcProvider.updateConfAccounts(account);
			}
			ConfAccountCategories category = vcProvider.findAccountCategoriesById(account.getAccountCategoryId());
			List<Long> accountCategories = vcProvider.findAccountCategoriesByConfType(category.getConfType());
			ConfSourceAccounts sourceAccount = vcProvider.findSpareAccount(accountCategories);
			if(sourceAccount != null) {

				String accountName = sourceAccount.getAccountName();
				Long timestamp = DateHelper.currentGMTTime().getTime();
				String tokenString = accountName + "|" + configurationProvider.getValue(ConfigConstants.VIDEOCONF_SECRET_KEY, "0") + "|" + timestamp;
				String token = DigestUtils.md5Hex(tokenString);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sd = sdf.format(timestamp);

				if(LOGGER.isDebugEnabled())
					LOGGER.info("startVideoConf-restUrl"+path);

				try {
					Map<String, String> sPara = new HashMap<String, String>() ;
					sPara.put("loginName", accountName);
					sPara.put("timeStamp", timestamp.toString());
					sPara.put("token", token);
					sPara.put("confName", cmd.getConfName());
					if(cmd.getConfName() == null || cmd.getConfName().trim().length() == 0)
						sPara.put("confName", "conference");
					sPara.put("hostKey", cmd.getPassword());
					sPara.put("startTime", sd);
					if(cmd.getDuration() == null || cmd.getDuration() == 0)
						sPara.put("duration", "30");
					else {
						sPara.put("duration", cmd.getDuration().toString());
					}
					sPara.put("optionJbh", "0");
					NameValuePair[] param = generatNameValuePair(sPara);
					if(LOGGER.isDebugEnabled())
						LOGGER.info("startVideoConf, namespaceId=" + namespaceId + ", cmd=" + cmd + ", restUrl="+path+", param="+sPara);

					HttpClient httpClient = new HttpClient();

					HttpMethod method = postMethod(path, param);

					httpClient.executeMethod(method);

					String result = method.getResponseBodyAsString();
					System.out.println(result);
					String json = strDecode(result);

					if(LOGGER.isDebugEnabled())
						LOGGER.error("startVideoConf,json="+json);

					BizConfHolder resultHolder = GsonUtil.fromJson(json, BizConfHolder.class);


					if(resultHolder.getStatus() == 100){
//						Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
						BizConfDTO data = resultHolder.getData();

//						response.setConfHostId(String.valueOf(data.get("userId")));
//						response.setToken(String.valueOf(data.get("token")));
						response.setConfHostName(UserContext.current().getUser().getNickName());
//						response.setMaxCount((Double.valueOf(String.valueOf(data.get("maxCount")))).intValue());
//						response.setMeetingNo(String.valueOf(data.get("meetingNo")));

						response.setMaxCount(data.getMaxCount());
						response.setMeetingNo(data.getMeetingNo());
						response.setConfHostId(data.getUserId());
						response.setToken(data.getToken());
						ConfConferences conf = new ConfConferences();

						String conferenceId = data.getId();
//						int index = conferenceId.indexOf(".");
//						conferenceId = conferenceId.substring(0, index);
						conf.setConferenceId(Long.valueOf(conferenceId));
						conf.setMeetingNo(Long.valueOf(data.getMeetingNo()));
						conf.setSubject(data.getMeetingName());
						conf.setStartTime(new Timestamp(timestamp));
						conf.setExpectDuration(cmd.getDuration());
						conf.setConfHostKey(data.getHostKey());
						conf.setJoinPolicy(1);
						conf.setSourceAccountId(sourceAccount.getId());
						conf.setConfAccountId(cmd.getAccountId());
						conf.setCreatorUid(UserContext.current().getUser().getId());
						conf.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						conf.setConfHostId(data.getUserId());
//						conf.setConfHostName(confHostName);
						conf.setMaxCount(data.getMaxCount());
						String joinUrl = "cfcloud://www.confcloud.cn/join?confno={" + conf.getMeetingNo() + "}";
						conf.setJoinUrl(joinUrl);
						conf.setStatus((byte) 1);
						conf.setNamespaceId(namespaceId);

						vcProvider.createConfConferences(conf);

						ConfAccounts confAccount = vcProvider.findVideoconfAccountById(conf.getConfAccountId());
						confAccount.setAssignedConfId(conf.getId());
						confAccount.setAssignedFlag((byte)1);
						confAccount.setAssignedSourceId(sourceAccount.getId());
						confAccount.setAssignedTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

						vcProvider.updateConfAccounts(confAccount);

					}else {
						LOGGER.error("startVideoConf error,json="+json);
					}

				} catch (Exception e) {
					LOGGER.error("startVideoConf-error.sourceAccount="+sourceAccount.getAccountName(), e);
				}
			} else {
				LOGGER.error("源账号不够");
			}
		}
		else {
			LOGGER.error("account "+cmd.getAccountId()+ "is invaild");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ACCOUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ACCOUNT),
							UserContext.current().getUser().getLocale(),"account is invaild."));
		}
		return response;
	}
	private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}

		return nameValuePair;
	}

	private static HttpMethod postMethod(String url, NameValuePair[] param) throws IOException{
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
		post.setRequestBody(param);
		post.releaseConnection();
		return post;
	}
	@Override
	public JoinVideoConfResponse joinVideoConf(JoinVideoConfCommand cmd) {
		JoinVideoConfResponse response = new JoinVideoConfResponse();

		if(cmd.getConfId().length() == 11){
			UserIdentifier user = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(), cmd.getConfId());
			if(LOGGER.isDebugEnabled())
				LOGGER.error("joinVideoConf, cmd="+cmd+",user="+user);
			if(user != null) {
				ConfAccounts account = new ConfAccounts();
				if(cmd.getEnterpriseId() != null && cmd.getEnterpriseId() != 0) {
					account = vcProvider.findAccountByUserIdAndEnterpriseId(user.getOwnerUid(), cmd.getEnterpriseId());
				} else {
					account = vcProvider.findAccountByUserId(user.getOwnerUid());
				}
				if(LOGGER.isDebugEnabled())
					LOGGER.error("joinVideoConf, account="+account);
				if(account != null) {
					ConfConferences conf = vcProvider.findConfConferencesById(account.getAssignedConfId());
					if(LOGGER.isDebugEnabled())
						LOGGER.error("joinVideoConf, conf="+conf);
					if(conf != null) {
						response.setJoinUrl(conf.getJoinUrl());
						response.setCondId(conf.getMeetingNo()+"");
						response.setPassword(conf.getConfHostKey());
					} else {
						LOGGER.error("conference has not been held yet!");
						throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.CONF_NOT_OPEN,
								localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
										String.valueOf(ConfServiceErrorCode.CONF_NOT_OPEN),
										UserContext.current().getUser().getLocale(),"conference has not been held yet!"));
					}
				}
			}
		}

		else if(cmd.getConfId().length() == 10){
			Long confId = Long.valueOf(cmd.getConfId());
			ConfConferences conf = vcProvider.findConfConferencesByConfId(confId);
			if(LOGGER.isDebugEnabled())
				LOGGER.error("joinVideoConf, cmd="+cmd+",conf="+conf);
			if(conf != null) {
				response.setJoinUrl(conf.getJoinUrl());
				response.setCondId(conf.getMeetingNo()+"");
				response.setPassword(conf.getConfHostKey());
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("meetingNo", cmd.getConfId());

				String scope = VideoconfNotificationTemplateCode.SCOPE;
				int code = VideoconfNotificationTemplateCode.VIDEOCONF_JOINURL_TEMPLATE;

				String joinUrl = localeTemplateService.getLocaleTemplateString(scope, code, "zh_CN", map, "");
				response.setJoinUrl(joinUrl);
				//没有的话原样返回会议号 by xiongying20170406
				response.setCondId(cmd.getConfId());
			}

//			else {
//				LOGGER.error("conf id is wrong!");
//				throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_CONF_ID,
//						localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
//								String.valueOf(ConfServiceErrorCode.ERROR_INVALID_CONF_ID),
//								UserContext.current().getUser().getLocale(),"conf id is wrong!"));
//			}
		}

		else {
			LOGGER.error("conf id length is 10 or 11!");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_CONF_ID,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_CONF_ID),
							UserContext.current().getUser().getLocale(),"conf id length is 10 or 11!"));
		}

		return response;
	}

	@Override
	public void reserveVideoConf(ReserveVideoConfCommand cmd) {
		LOGGER.info("reserveVideoConf cmd = " + cmd);
		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();

		User user = UserContext.current().getUser();
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
		List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
				.map((r) -> { return r.getIdentifierToken(); })
				.collect(Collectors.toList());

		int minutes = (int) ((cmd.getEndTime()-cmd.getStartTime())/60000);
		ConfReservations reservation = new ConfReservations();

		reservation.setSubject(cmd.getSubject());
		reservation.setTimeZone(cmd.getTimeZone());
		reservation.setStartTime(new Timestamp(cmd.getStartTime()));
		reservation.setDuration(minutes);
		reservation.setDescription(cmd.getDescription());
		reservation.setConfHostKey(cmd.getHostKey());
		reservation.setStatus((byte) 1);

		if(phones != null && phones.size() > 0)
			reservation.setCreatorPhone(phones.get(0));

		ConfAccounts account = new ConfAccounts();
		if(cmd.getEnterpriseId() != null && cmd.getEnterpriseId() != 0) {
			account = vcProvider.findAccountByUserIdAndEnterpriseId(user.getId(), cmd.getEnterpriseId());
		} else {
			account = vcProvider.findAccountByUserId(user.getId());
		}
		LOGGER.info("reserveVideoConf account = " + account + ", current user = " + user.getId());
		if(account == null) {
			LOGGER.error("account is null");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ACCOUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ACCOUNT),
							UserContext.current().getUser().getLocale(),"account is invaild."));

		}
		reservation.setConfAccountId(account.getId());
		reservation.setEnterpriseId(account.getEnterpriseId());

		if(cmd.getId() == null) {
			reservation.setCreatorUid(user.getId());
			reservation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			reservation.setNamespaceId(namespaceId);
			vcProvider.createReserveVideoConf(reservation);

		}else {
			reservation.setId(cmd.getId());
			vcProvider.updateReserveVideoConf(reservation);
		}

	}

	@Override
	public void deleteReservationConf(DeleteReservationConfCommand cmd) {

		ConfReservations reservation = vcProvider.findReservationConfById(cmd.getId());
		reservation.setStatus((byte) 0);
		vcProvider.updateReserveVideoConf(reservation);
	}

	@Override
	public ListReservationConfResponse listReservationConf(ListReservationConfCommand cmd) {

		ListReservationConfResponse response = new ListReservationConfResponse();

		User user = UserContext.current().getUser();
		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ConfAccounts account = new ConfAccounts();
		if(cmd.getEnterpriseId() != null && cmd.getEnterpriseId() != 0) {
			account = vcProvider.findAccountByUserIdAndEnterpriseId(user.getId(), cmd.getEnterpriseId());
		} else {
			account = vcProvider.findAccountByUserId(user.getId());
		}

		if(account != null) {
			List<ConfReservations> reservations = vcProvider.findReservationConfByAccountId(account.getId(), locator, pageSize+1);
			if(reservations != null && reservations.size() > 0) {

				Long nextPageAnchor = null;
				if(reservations != null && reservations.size() > pageSize) {
					reservations.remove(reservations.size() - 1);
					nextPageAnchor = reservations.get(reservations.size() - 1).getCreateTime().getTime();
				}
				response.setNextPageAnchor(nextPageAnchor);

				List<ConfReservationsDTO> reserveConfs = reservations.stream().map(r -> {
					ConfReservationsDTO reserveConf = new ConfReservationsDTO();

					reserveConf.setId(r.getId());
					reserveConf.setSubject(r.getSubject());
					reserveConf.setTimeZone(r.getTimeZone());
					reserveConf.setStartTime(r.getStartTime());
					reserveConf.setDuration(r.getDuration());
					reserveConf.setDescription(r.getDescription());
					reserveConf.setConfHostKey(r.getConfHostKey());
					return reserveConf;
				}).collect(Collectors.toList());

				response.setReserveConf(reserveConfs);
			}

		}

		return response;
	}

	@Override
	public Set<Long> listOrdersWithUnassignAccount(
			ListUsersWithoutVideoConfPrivilegeCommand cmd) {

		Set<Long> orders = vcProvider.listOrdersWithUnassignAccount(cmd.getEnterpriseId());
		return orders;
	}

	@Override
	public UnassignAccountResponse listUnassignAccountsByOrder(
			ListUnassignAccountsByOrderCommand cmd) {
		UnassignAccountResponse response = new UnassignAccountResponse();

		int count = vcProvider.countOrderAccounts(cmd.getOrderId(), null);
		int unassignAccount = vcProvider.countOrderAccounts(cmd.getOrderId(), (byte) 0);
		response.setAccountsCount(count);
		response.setUnassignAccountsCount(unassignAccount);

		List<Long> accountIds = vcProvider.listUnassignAccountIds(cmd.getOrderId());
		response.setAccountIds(accountIds);

		if(accountIds != null && accountIds.size() > 0) {
			ConfAccounts account = vcProvider.findVideoconfAccountById(accountIds.get(0));
			if(account != null) {
				response.setExpiredDate(account.getExpiredDate());
				ConfAccountCategories category = vcProvider.findAccountCategoriesById(account.getAccountCategoryId());
				if(category != null)
					response.setConfType(category.getConfType());
			}

		}

		return response;
	}

	@Override
	public void createAccountOwner(CreateAccountOwnerCommand cmd) {
		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();
		List<Long> accountIds = cmd.getAccountIds();
		List<Long> userIds = cmd.getUserIds();
		if(userIds == null || userIds.isEmpty()){
			LOGGER.error("user count is null");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_USER_COUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_USER_COUNT),
							UserContext.current().getUser().getLocale(),"user count is null."));
		}
		if(accountIds == null || accountIds.isEmpty()){
			LOGGER.error("account count is null");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ACCOUNT_COUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ACCOUNT_COUNT),
							UserContext.current().getUser().getLocale(),"account count is null."));
		}
		if(userIds.size() > accountIds.size()) {
			LOGGER.error("user count is cannot larger than account count");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_USER_ACCOUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_USER_ACCOUNT),
							UserContext.current().getUser().getLocale(),"user count is cannot larger than account count."));
		}

//		accountIds.stream().map(accountId -> {
		for(Long accountId : accountIds) {
			if(userIds.size() > 0) {
				ConfAccounts account = vcProvider.findVideoconfAccountById(accountId);
				if(account == null) {
					LOGGER.error("account is not exist!");
					throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ACCOUNT,
							localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
									String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ACCOUNT),
									UserContext.current().getUser().getLocale(),"account is invaild."));
				}

				Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

				account.setOwnerId(userIds.get(0));
				account.setUpdateTime(now);
				account.setOwnTime(now);
				vcProvider.updateConfAccounts(account);
				confAccountSearcher.feedDoc(account);
				userIds.remove(0);

				List<ConfOrderAccountMap> maps = vcProvider.findOrderAccountByAccountId(accountId);
				if(maps != null && maps.size() > 0) {
					for(ConfOrderAccountMap map : maps) {
						map.setAssigedFlag((byte) 1);
						vcProvider.updateConfOrderAccountMap(map);
					}
				}

				ConfAccountHistories history = new ConfAccountHistories();
				history.setEnterpriseId(account.getEnterpriseId());
				history.setExpiredDate(account.getExpiredDate());
				history.setStatus(account.getStatus());
				history.setAccountCategoryId(account.getAccountCategoryId());
				history.setAccountType(account.getAccountType());
				history.setOwnerId(account.getOwnerId());
				history.setOwnTime(now);
				history.setCreatorUid(account.getCreatorUid());
				history.setCreateTime(account.getCreateTime());
				history.setOperatorUid(UserContext.current().getUser().getId());
				history.setOperationType("assign account");
				history.setOperateTime(now);
				history.setProcessDetails("");
				history.setNamespaceId(namespaceId);
				vcProvider.createConfAccountHistories(history);
			}
		}
//		return null;
//		});

	}

	@Override
	public void updateContactor(UpdateContactorCommand cmd) {
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		enterprise.setContactName(cmd.getContactorName());
		enterprise.setContact(cmd.getContactor());

		vcProvider.updateVideoconfEnterprise(enterprise);

	}

	@Override
	public ListOrderByAccountResponse listOrderByAccount(
			ListOrderByAccountCommand cmd) {
		CountAccountOrdersAndMonths counts = vcProvider.countAccountOrderInfo(cmd.getAccountId());
		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListOrderByAccountResponse response = new ListOrderByAccountResponse();
		response.setCounts(counts);
		List<OrderBriefDTO> orders = vcProvider.findOrdersByAccountId(cmd.getAccountId(), locator, pageSize+1);
		if(orders != null && orders.size() > 0) {

			Long nextPageAnchor = null;
			if(orders != null && orders.size() > pageSize) {
				orders.remove(orders.size() - 1);
				nextPageAnchor = orders.get(orders.size() -1).getId();
			}

			response.setNextPageAnchor(nextPageAnchor);

			response.setOrderBriefs(orders);
		}

		return response;
	}

	@Override
	public Long createConfAccountOrder(CreateConfAccountOrderCommand cmd) {
		ConfOrders order = new ConfOrders();

		order.setOwnerId(cmd.getEnterpriseId());
		order.setQuantity(cmd.getQuantity());
		order.setPeriod(cmd.getPeriod());
		if(cmd.getExpiredDate() != null)
			order.setExpiredDate(new Timestamp(cmd.getExpiredDate()));
		order.setAmount(cmd.getAmount());
		order.setStatus(PayStatus.WAITING_FOR_PAY.getCode());
		order.setInvoiceReqFlag(cmd.getInvoiceFlag());
		order.setInvoiceIssueFlag(cmd.getMakeOutFlag());
		order.setOnlineFlag(cmd.getBuyChannel());

		order.setAccountCategoryId(cmd.getAccountCategoryId());

		order.setBuyerName(cmd.getContactor());
		order.setBuyerContact(cmd.getMobile());
		vcProvider.createConfOrders(order);

		confOrderSearcher.feedDoc(order);

		if(order.getInvoiceReqFlag() != null && order.getInvoiceReqFlag() == 1) {
			ConfInvoices invoice = ConvertHelper.convert(cmd.getInvoice(), ConfInvoices.class);
			if(invoice == null) {
				invoice = new ConfInvoices();
			}
			invoice.setOrderId(order.getId());
			vcProvider.createInvoice(invoice);
		}

		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		if(enterprise == null) {
			ConfEnterprises confEnterprise = new ConfEnterprises();
			confEnterprise.setEnterpriseId(cmd.getEnterpriseId());
			confEnterprise.setContactName(cmd.getContactor());
			confEnterprise.setContact(cmd.getMobile());
			confEnterprise.setStatus((byte) 1);
			confEnterprise.setActiveAccountAmount(0);
			confEnterprise.setTrialAccountAmount(0);


			Enterprise enter = enterpriseProvider.findEnterpriseById(cmd.getEnterpriseId());
			if(enter != null)
				confEnterprise.setNamespaceId(enter.getNamespaceId());
			vcProvider.createConfEnterprises(confEnterprise);

			confEnterpriseSearcher.feedDoc(confEnterprise);
		}
//		} else {
//			enterprise.setContactName(cmd.getContactor());
//			enterprise.setContact(cmd.getMobile());
//			vcProvider.updateConfEnterprises(enterprise);
//		}

		if(order.getOnlineFlag() == 0) {
			OfflinePayBillCommand command = new OfflinePayBillCommand();
			command.setOrderId(order.getId());
			offlinePayBill(command);
		}

		return order.getId();

	}

	@Override
	public void confPaymentCallBack(OnlinePayBillCommand cmd) {
		//fail
		if(cmd.getPayStatus().toLowerCase().equals("fail"))
			this.onlinePayBillFail(cmd);
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
			this.onlinePayBillSuccess(cmd);

	}

	private ConfOrders onlinePayBillFail(OnlinePayBillCommand cmd) {

		if(LOGGER.isDebugEnabled())
			LOGGER.error("confPayBillFail");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());

		ConfOrders order = this.checkOrder(orderId);

		Date cunnentTime = new Date();
		Timestamp currentTimestamp = new Timestamp(cunnentTime.getTime());
		order.setOnlineFlag((byte) 1);
		this.updateOrderStatus(order, currentTimestamp, PayStatus.INACTIVE.getCode());

		return order;
	}

	private ConfOrders onlinePayBillSuccess(OnlinePayBillCommand cmd) {

		if(LOGGER.isDebugEnabled())
			LOGGER.error("confPayBillSuccess");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		this.checkVendorTypeIsNull(cmd.getVendorType());
		this.checkPayAmountIsNull(cmd.getPayAmount());

		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
		ConfOrders order = this.checkOrder(orderId);

		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);

		this.checkVendorTypeFormat(cmd.getVendorType());
		this.checkPayAmount(order.getAmount(), new BigDecimal(cmd.getPayAmount()));

		if(order.getStatus().byteValue() == PayStatus.WAITING_FOR_PAY.getCode()) {
			order.setOnlineFlag((byte) 1);
			this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode());

		}

		return order;
	}

	private void checkPayAmount(BigDecimal orderAmount, BigDecimal payAmount) {

		if (0 != orderAmount.compareTo(payAmount)) {
			LOGGER.error("Order amount is not equal to payAmount, orderAmount={}, payAmount={}", orderAmount, payAmount);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Order amount is not equal to payAmount.");
		}

	}

	private void checkPayAmountIsNull(String payAmount) {

		if(payAmount == null || payAmount.trim().equals("")){
			LOGGER.error("payAmount is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"payAmount or is null or empty.");
		}

	}

	private void checkVendorTypeIsNull(String vendorType) {

		if(vendorType == null || vendorType.trim().equals("")){
			LOGGER.error("vendorType is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendorType is null or empty.");
		}

	}

	private void checkOrderNoIsNull(String orderNo) {

		if(orderNo == null || orderNo.trim().equals("")){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}

	}

	private void checkVendorTypeFormat(String vendorType) {
		if(VendorType.fromCode(vendorType) == null){
			LOGGER.error("vendor type is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"vendor type is wrong.");
		}
	}

	private ConfOrders checkOrder(Long orderId) {

		ConfOrders order = vcProvider.findOredrById(orderId);

		if(order == null){
			LOGGER.error("the order not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}

	private void updateOrderStatus(ConfOrders order, Timestamp payTimeStamp, byte paymentStatus) {
//		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();
		User user = UserContext.current().getUser();
		order.setPayerId(user.getId());
		order.setPaidTime(payTimeStamp);
		order.setStatus(paymentStatus);

		vcProvider.updateConfOrders(order);
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(order.getOwnerId());
		int namespaceId = enterprise.getNamespaceId();
		if(order.getStatus().byteValue() == PayStatus.PAID.getCode()
				&& order.getAccountCategoryId() != null && order.getAccountCategoryId() != 0) {
			List<ConfAccounts> accounts = new ArrayList<ConfAccounts>();

			//6方的为试用账号
			ConfAccountCategories category =  vcProvider.findAccountCategoriesById(order.getAccountCategoryId());

			for(int i = 0; i < order.getQuantity(); i++) {

				ConfAccounts account = new ConfAccounts();
				account.setStatus((byte) 1);
				account.setEnterpriseId(order.getOwnerId());
				if(order.getPeriod() == 0) {
					account.setExpiredDate(order.getExpiredDate());
				} else {
					account.setExpiredDate(addMonth(order.getPaidTime(), order.getPeriod()));
				}

				account.setAccountCategoryId(order.getAccountCategoryId());
				if(null != category && null != category.getConfType() && category.getConfType() == 4) {
					//对于新增测试账号:如果之前该用户之前有有效账号,则不分配;没有有效账号,则给他分配
					ConfAccounts activeAccounts = vcProvider.findAccountByUserIdAndEnterpriseIdAndStatus(UserContext.current().getUser().getId(), order.getOwnerId(), ConfAccountStatus.ACTIVE.getCode());
					if(null == activeAccounts)
						account.setOwnerId(UserContext.current().getUser().getId());
					account.setAccountType((byte) 1);
				} else {
					account.setAccountType((byte) 2);
				}

				account.setNamespaceId(namespaceId);
				vcProvider.createConfAccounts(account);

				accounts.add(account);

				ConfOrderAccountMap map = new ConfOrderAccountMap();
				map.setOrderId(order.getId());
				map.setEnterpriseId(order.getOwnerId());
				map.setConfAccountId(account.getId());
				map.setConfAccountNamespaceId(namespaceId);
				vcProvider.createConfOrderAccountMap(map);

			}

			confAccountSearcher.bulkUpdate(accounts);

			enterprise.setBuyChannel(order.getOnlineFlag());
			enterprise.setAccountAmount(enterprise.getAccountAmount()+order.getQuantity());
			enterprise.setActiveAccountAmount(enterprise.getActiveAccountAmount()+order.getQuantity());
			if(null != category && null != category.getConfType() && category.getConfType() == 4) {
				enterprise.setTrialAccountAmount(enterprise.getTrialAccountAmount()+order.getQuantity());
			}
			enterprise.setStatus((byte) 1);
			vcProvider.updateConfEnterprises(enterprise);
			confEnterpriseSearcher.feedDoc(enterprise);

			sendConfInvoiceEmail(order.getEmail());

		} else if(order.getStatus().byteValue() == PayStatus.PAID.getCode()
				&& (order.getAccountCategoryId() == null || order.getAccountCategoryId() == 0)) {
			CrossShardListingLocator locator = new CrossShardListingLocator();
			List<ConfOrderAccountMap> maps = vcProvider.findOrderAccountByOrderId(order.getId(), locator, Integer.MAX_VALUE,(byte)1);
			if(maps != null && maps.size() > 0) {
				Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
				int toActive = 0;
				int toTrail = 0;
				for(ConfOrderAccountMap map : maps) {

					ConfAccounts account = vcProvider.findVideoconfAccountById(map.getConfAccountId());
					//6方的为试用账号
					ConfAccountCategories category =  vcProvider.findAccountCategoriesById(account.getAccountCategoryId());

					//0-inactive 1-active 2-locked
					if(account.getStatus() == 0) {
						account.setExpiredDate(addMonth(now, order.getPeriod()));
						account.setStatus((byte) 1);
						toActive++;

						if(null != category && null != category.getConfType() && category.getConfType() == 4) {
							toTrail++;
							account.setAccountType((byte) 1);
						} else {
							account.setAccountType((byte) 2);
						}
					}
					else {
						account.setExpiredDate(addMonth(account.getExpiredDate(), order.getPeriod()));

						if(null != category && null != category.getConfType() && category.getConfType() == 4) {
							account.setAccountType((byte) 1);
						} else {
							account.setAccountType((byte) 2);
						}
					}

					vcProvider.updateConfAccounts(account);

					confAccountSearcher.feedDoc(account);
				}

				if(null == enterprise.getBuyChannel()) {
					enterprise.setBuyChannel(order.getOnlineFlag());
				} else {
					if(enterprise.getBuyChannel().equals(order.getOnlineFlag())) {
						enterprise.setBuyChannel(order.getOnlineFlag());
					} else {
						enterprise.setBuyChannel(ConfEnterprisesBuyChannel.BOTH.getCode());
					}
				}

				enterprise.setActiveAccountAmount(enterprise.getActiveAccountAmount()+toActive);
				enterprise.setTrialAccountAmount(enterprise.getTrialAccountAmount()+toTrail);
				enterprise.setStatus((byte) 1);
				vcProvider.updateConfEnterprises(enterprise);
				confEnterpriseSearcher.feedDoc(enterprise);

				sendConfInvoiceEmail(order.getEmail());

			}
		}

	}

	private void sendConfInvoiceEmail(String emailAddress) {
		if(!StringUtils.isNullOrEmpty(emailAddress)) {
			String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
			MailHandler handler = PlatformContext.getComponent(handlerName);
			String subject = localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
					String.valueOf(ConfServiceErrorCode.CONF_INVOICE_SUBJECT),
					UserContext.current().getUser().getLocale(),"zuolin video conference invoce");

			String body = localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
					String.valueOf(ConfServiceErrorCode.CONF_INVOICE_BODY),
					UserContext.current().getUser().getLocale(),"zuolin video conference invoce");
			handler.sendMail(Namespace.DEFAULT_NAMESPACE, null,emailAddress, subject, body, null);
		}
	}

	private Long convertOrderNoToOrderId(String orderNo) {

		return Long.valueOf(orderNo);
	}

	@Override
	public void offlinePayBill(OfflinePayBillCommand cmd) {

		LOGGER.info("offlinePayBill.");
		ConfOrders order = this.checkOrder(cmd.getOrderId());

		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);
		this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode());


	}

	@Override
	public Double getEarlyWarningLine(Byte warningLineType) {
		Double warningLine = 0.0000;
		if(warningLineType == 0) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_SOURCEACCOUNT_RADIO_WARNING_LINE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 1) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_SOURCEACCOUNT_OCCUPANCY_WARNING_LINE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 2) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_25VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 3) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_25PHONE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 4) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_100VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 5) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_100PHONE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 6) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_25VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 7) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_25PHONE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 8) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_100VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 9) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_100PHONE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 10) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_6VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 11) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_50VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 12) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_RADIO_WARNING_LINE_50PHONE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 13) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_6VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 14) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_50VIDEO, "0.0000");
			warningLine = Double.valueOf(line);
		}

		if(warningLineType == 15) {
			String line = configurationProvider.getValue(ConfigConstants.VIDEOCONF_ACCOUNT_OCCUPANCY_WARNING_LINE_50PHONE, "0.0000");
			warningLine = Double.valueOf(line);
		}

		return warningLine;
	}

	private Double getActiveAccountRadio(Byte confType) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(4);

		List<Long> accountCategoryIds = vcProvider.findAccountCategoriesByConfType(confType);
		int countActiveAccounts = vcProvider.countActiveAccounts(accountCategoryIds);
		int countActiveSourceAccounts = vcProvider.countActiveSourceAccounts(accountCategoryIds);
		if(countActiveSourceAccounts == 0)
			return 0.0000;
		String radio = numberFormat.format((float)countActiveAccounts/(float)countActiveSourceAccounts);
		return Double.valueOf(radio);
	}

	private Double getOccupiedAccountRadio(Byte confType) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(4);

		List<Long> accountCategoryIds = vcProvider.findAccountCategoriesByConfType(confType);
		int countOccupiedAccounts = vcProvider.countOccupiedAccounts(accountCategoryIds);
		int countActiveSourceAccounts = vcProvider.countActiveSourceAccounts(accountCategoryIds);
		if(countActiveSourceAccounts == 0)
			return 0.0000;
		String radio = numberFormat.format((float)countOccupiedAccounts/(float)countActiveSourceAccounts);
		return Double.valueOf(radio);
	}

	private Double getRadio(Byte monitorType) {

		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(4);
		if(monitorType == 0) {
			return getActiveAccountRadio(null);
		}

		if(monitorType == 1) {
			return getOccupiedAccountRadio(null);
		}

		if(monitorType == 2) {
			return getActiveAccountRadio((byte) 0);
		}

		if(monitorType == 3) {
			return getActiveAccountRadio((byte) 1);
		}

		if(monitorType == 4) {
			return getActiveAccountRadio((byte) 2);
		}

		if(monitorType == 5) {
			return getActiveAccountRadio((byte) 3);
		}

		if(monitorType == 6) {
			return getOccupiedAccountRadio((byte) 0);
		}

		if(monitorType == 7) {
			return getOccupiedAccountRadio((byte) 1);
		}

		if(monitorType == 8) {
			return getOccupiedAccountRadio((byte) 2);
		}

		if(monitorType == 9) {
			return getOccupiedAccountRadio((byte) 3);
		}

		if(monitorType == 10) {
			return getActiveAccountRadio((byte) 4);
		}

		if(monitorType == 11) {
			return getActiveAccountRadio((byte) 5);
		}

		if(monitorType == 12) {
			return getActiveAccountRadio((byte) 6);
		}

		if(monitorType == 13) {
			return getOccupiedAccountRadio((byte) 4);
		}

		if(monitorType == 14) {
			return getOccupiedAccountRadio((byte) 5);
		}

		if(monitorType == 15) {
			return getOccupiedAccountRadio((byte) 6);
		}

		return 0.0000;

	}

	@Scheduled(cron="0 0 2 * * ? ")
	@Override
	public void invalidAccount() {

		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			LOGGER.info("update invalid appliers.");
			vcProvider.updateInvaildAccount();
			vcProvider.updateEnterpriseAccounts();
		}
	}

	@Override
	public List<GetNamespaceListResponse> getRegisterNamespaceIdList(
			GetNamespaceIdListCommand cmd) {

		List<GetNamespaceListResponse> namespaceIdList = new ArrayList<GetNamespaceListResponse>();
		List<UserIdentifier> userList = userProvider.findClaimedIdentifiersByToken(cmd.getUserIdentifier());
		if(userList != null && userList.size() > 0) {
			for(UserIdentifier user : userList) {
				GetNamespaceListResponse namespace = new GetNamespaceListResponse();
				namespace.setNamespaceId(user.getNamespaceId());
				if(user.getNamespaceId() == 0) {
					namespace.setName(localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ZUOLIN_NAMESPACE_NAME),
							UserContext.current().getUser().getLocale(),"ZUOLIN"));
				} else {
					Namespace ns = nsProvider.findNamespaceById(user.getNamespaceId());
					if(ns != null)
						namespace.setName(ns.getName());
				}
				namespaceIdList.add(namespace);
			}
		}

		return namespaceIdList;
	}

	@Override
	public List<GetNamespaceListResponse> getConferenceNamespaceIdList(
			GetNamespaceIdListCommand cmd) {

		List<GetNamespaceListResponse> namespaceIdList = new ArrayList<GetNamespaceListResponse>();
		List<UserIdentifier> userList = userProvider.findClaimedIdentifiersByToken(cmd.getUserIdentifier());
		if(userList != null && userList.size() > 0) {
			for(UserIdentifier user : userList) {
				List<ConfAccounts> accounts = vcProvider.findAccountsByUserId(user.getOwnerUid());
				if(LOGGER.isDebugEnabled())
					LOGGER.error("getConferenceNamespaceIdList, accounts="+accounts);
				if(accounts != null && accounts.size() > 0) {
					for(ConfAccounts account : accounts) {
						if(account != null) {
							ConfConferences conf = vcProvider.findConfConferencesById(account.getAssignedConfId());
							if(LOGGER.isDebugEnabled())
								LOGGER.error("getConferenceNamespaceIdList, conf="+conf);
							if(conf != null) {
								GetNamespaceListResponse namespace = new GetNamespaceListResponse();
								namespace.setEnterpriseId(account.getEnterpriseId());
								Organization org = organizationProvider.findOrganizationById(account.getEnterpriseId());
								if(org != null) {
									namespace.setEnterpriseName(org.getName());
								}
								namespace.setNamespaceId(user.getNamespaceId());
								if(user.getNamespaceId() == 0) {
									namespace.setName(localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
											String.valueOf(ConfServiceErrorCode.ZUOLIN_NAMESPACE_NAME),
											UserContext.current().getUser().getLocale(),"ZUOLIN"));
								} else {
									Namespace ns = nsProvider.findNamespaceById(user.getNamespaceId());
									if(ns != null)
										namespace.setName(ns.getName());
								}
								namespaceIdList.add(namespace);
							}
						}
					}
				}

			}
		}
		return namespaceIdList;
	}

	@Override
	public void deleteSourceVideoConfAccount(
			DeleteSourceVideoConfAccountCommand cmd) {
		vcProvider.deleteSourceVideoConfAccount(cmd.getSourceAccountId());
	}

	@Override
	public InvoiceDTO updateInvoice(UpdateInvoiceCommand cmd) {
		InvoiceDTO dto = vcProvider.getInvoiceByOrderId(cmd.getOrderId());
		if(dto == null) {
			ConfInvoices invoice = new ConfInvoices();
			invoice.setOrderId(cmd.getOrderId());
			vcProvider.createInvoice(invoice);

			dto = vcProvider.getInvoiceByOrderId(cmd.getOrderId());
		}
		dto.setTaxpayerType(cmd.getTaxpayerType());
		dto.setVatType(cmd.getVatType());
		dto.setExpenseType(cmd.getExpenseType());
		dto.setCompanyName(cmd.getCompanyName());
		dto.setVatCode(cmd.getVatCode());
		dto.setVatAddress(cmd.getVatAddress());
		dto.setVatPhone(cmd.getVatPhone());
		dto.setVatBankname(cmd.getVatBankname());
		dto.setVatBankaccount(cmd.getVatBankaccount());
		dto.setAddress(cmd.getAddress());
		dto.setZipCode(cmd.getZipCode());
		dto.setConsignee(cmd.getConsignee());
		dto.setContact(cmd.getContact());
		dto.setContractFlag(cmd.getContractFlag());

		ConfInvoices invoice = ConvertHelper.convert(dto, ConfInvoices.class);
		vcProvider.updateInvoice(invoice);

		ConfOrders order = vcProvider.findOredrById(cmd.getOrderId());
		order.setInvoiceReqFlag((byte) 1);
		order.setInvoiceIssueFlag((byte) 1);
		vcProvider.updateConfOrders(order);

		return dto;
	}

	@Override
	public ListConfCategoryResponse listConfCategory(ListConfCategoryCommand cmd) {
		List<ConfAccountCategories> categories = vcProvider.listConfAccountCategories(null, (byte) 1, 0, Integer.MAX_VALUE);
		ListConfCategoryResponse response = new ListConfCategoryResponse();

		List<ConfCategoryDTO> categoryDtos = new ArrayList<ConfCategoryDTO>();
		for(ConfAccountCategories category : categories) {
			//0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话
			ConfCategoryDTO dto = new ConfCategoryDTO();
			dto.setSingleAccountPrice(category.getSingleAccountPrice());
			dto.setMultipleAccountThreshold(category.getMultipleAccountThreshold());
			dto.setMultipleAccountPrice(category.getMultipleAccountPrice());
			dto.setMinPeriod(category.getMinPeriod());

			if(category.getConfType() == 0 || category.getConfType() == 1) {
				dto.setConfCapacity((byte) 0);
			}
			if(category.getConfType() == 2 || category.getConfType() == 3) {
				dto.setConfCapacity((byte) 1);
			}
			if(category.getConfType() == 4) {
				dto.setConfCapacity((byte) 2);
			}
			if(category.getConfType() == 5 || category.getConfType() == 6) {
				dto.setConfCapacity((byte) 3);
			}

			categoryDtos.add(dto);
		}

		response.setCategories(categoryDtos);

		int enterpriseVaildAccounts = vcProvider.countAccountsByEnterprise(cmd.getEnterpriseId(), null);
		response.setEnterpriseVaildAccounts(enterpriseVaildAccounts);

		return response;
	}

	@Override
	public ConfAccountOrderDTO updateConfAccountPeriod(UpdateConfAccountPeriodCommand cmd) {

		int quantity = cmd.getAccountIds().size();
		CreateConfAccountOrderCommand order = new CreateConfAccountOrderCommand();
		order.setEnterpriseId(cmd.getEnterpriseId());
		order.setEnterpriseName(cmd.getEnterpriseName());
		order.setContactor(cmd.getContactor());
		order.setMobile(cmd.getMobile());
		order.setQuantity(quantity);
		order.setPeriod(cmd.getMonths());
		order.setAmount(cmd.getAmount());
		order.setInvoiceFlag(cmd.getInvoiceFlag());
		order.setBuyChannel(cmd.getBuyChannel());
		order.setAccountCategoryId(0L);
		order.setMakeOutFlag((byte) 0);
		Long orderId = createConfAccountOrder(order);
		ConfOrders confOrder = vcProvider.findOredrById(orderId);
		confOrder.setEmail(cmd.getMailAddress());
		vcProvider.updateConfOrders(confOrder);
		confOrderSearcher.feedDoc(confOrder);

		ConfAccountOrderDTO dto = new ConfAccountOrderDTO();
		dto.setBillId(orderId);
		dto.setAmount(order.getAmount().doubleValue());
		dto.setName(cmd.getContactor() + " order");
		dto.setDescription(cmd.getContactor() + " extend " + quantity + " accounts " + cmd.getMonths() + " months for " + cmd.getEnterpriseName());
		dto.setOrderType("videoConf");
		this.setSignatureParam(dto);


		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		int namespaceId = enterprise.getNamespaceId();
		for(Long accountId : cmd.getAccountIds()) {

			ConfOrderAccountMap map = new ConfOrderAccountMap();
			map.setOrderId(orderId);
			map.setEnterpriseId(cmd.getEnterpriseId());
			map.setConfAccountId(accountId);
			map.setConfAccountNamespaceId(namespaceId);
			vcProvider.createConfOrderAccountMap(map);
		}

		return dto;
	}

	@Override
	public ConfAccountOrderDTO createConfAccountOrderOnline(
			CreateConfAccountOrderOnlineCommand cmd) {
		//0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话
		//账号类型 0-25方 1-100方 2-6方 3-50方
		Byte confType = null;
		if(cmd.getConfCapacity() == 0 && cmd.getConfType() == 0) {
			confType = 0;
		}
		if(cmd.getConfCapacity() == 0 && cmd.getConfType() == 1) {
			confType = 1;
		}
		if(cmd.getConfCapacity() == 1 && cmd.getConfType() == 0) {
			confType = 2;
		}
		if(cmd.getConfCapacity() == 1 && cmd.getConfType() == 1) {
			confType = 3;
		}
		if(cmd.getConfCapacity() == 2 && cmd.getConfType() == 0) {
			confType = 4;
		}
		if(cmd.getConfCapacity() == 3 && cmd.getConfType() == 0) {
			confType = 5;
		}
		if(cmd.getConfCapacity() == 3 && cmd.getConfType() == 1) {
			confType = 6;
		}
		List<ConfAccountCategories> categories = vcProvider.listConfAccountCategories(confType, cmd.getBuyChannel(), 0, Integer.MAX_VALUE);
		CreateConfAccountOrderCommand order = new CreateConfAccountOrderCommand();
		order.setEnterpriseId(cmd.getEnterpriseId());
		order.setEnterpriseName(cmd.getEnterpriseName());
		order.setContactor(cmd.getContactor());
		order.setMobile(cmd.getMobile());
		order.setQuantity(cmd.getQuantity());
		order.setPeriod(cmd.getPeriod());
		order.setAmount(cmd.getAmount());
		order.setInvoiceFlag(cmd.getInvoiceReqFlag());
		order.setBuyChannel(cmd.getBuyChannel());
		if(categories != null && categories.size() > 0)
			order.setAccountCategoryId(categories.get(0).getId());
		order.setMakeOutFlag((byte) 0);
		order.setInvoice(new InvoiceDTO());

		Long orderId = createConfAccountOrder(order);
		ConfOrders confOrder = vcProvider.findOredrById(orderId);
		confOrder.setEmail(cmd.getMailAddress());
		vcProvider.updateConfOrders(confOrder);

		ConfAccountOrderDTO dto = new ConfAccountOrderDTO();
		dto.setBillId(orderId);
		dto.setAmount(order.getAmount().doubleValue());
		dto.setName(cmd.getContactor() + " order");
		dto.setDescription(cmd.getContactor() + " buy " + cmd.getQuantity() + " accounts " + cmd.getPeriod() + " months for " + cmd.getEnterpriseName());
		dto.setOrderType("videoConf");
		this.setSignatureParam(dto);
		return dto;

	}

	private void setSignatureParam(ConfAccountOrderDTO dto) {
		String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);

		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",appKey);
		map.put("timestamp",timestamp+"");
		map.put("randomNum",randomNum+"");
		map.put("amount",dto.getAmount().doubleValue()+"");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		dto.setAppKey(appKey);
		dto.setRandomNum(randomNum);
		dto.setSignature(URLEncoder.encode(signature));
		dto.setTimestamp(timestamp);
	}

	@Override
	public VerifyPurchaseAuthorityResponse verifyPurchaseAuthority(
			VerifyPurchaseAuthorityCommand cmd) {

		VerifyPurchaseAuthorityResponse response = new VerifyPurchaseAuthorityResponse();
		int enterpriseVaildAccounts = vcProvider.countAccountsByEnterprise(cmd.getEnterpriseId(), null);
		int enterpriseAccounts = vcProvider.countEnterpriseAccounts(cmd.getEnterpriseId());
		boolean isAllTrial = vcProvider.allTrialEnterpriseAccounts(cmd.getEnterpriseId());
		response.setEnterpriseActiveAccountCount(enterpriseVaildAccounts);
		response.setEnterpriseAccountCount(enterpriseAccounts);
		response.setAllTrial(isAllTrial);

		boolean privilege = rolePrivilegeService.checkAdministrators(cmd.getEnterpriseId());

		response.setPurchaseAuthority(privilege);
		return response;
	}

	@Override
	public void deleteConfEnterprise(DeleteConfEnterpriseCommand cmd) {
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		if(null == enterprise || enterprise.getStatus() == 0) {
			LOGGER.error("enterprise is not exist!");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.ERROR_INVALID_ENTERPRISE,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.ERROR_INVALID_ENTERPRISE),
							UserContext.current().getUser().getLocale(),"enterprise is not exist."));
		}

		if(enterprise.getActiveAccountAmount() != 0) {
			LOGGER.error("enterpriseid = " + cmd.getEnterpriseId() + "Have active account! cannot inactive!");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.CONF_ENTERPRISE_HAS_ACTIVE_ACCOUNT,
					localeStringService.getLocalizedString(String.valueOf(ConfServiceErrorCode.SCOPE),
							String.valueOf(ConfServiceErrorCode.CONF_ENTERPRISE_HAS_ACTIVE_ACCOUNT),
							UserContext.current().getUser().getLocale(), "has active account"));
		}

		enterprise.setStatus((byte) 0);
		vcProvider.updateConfEnterprises(enterprise);
		confEnterpriseSearcher.feedDoc(enterprise);
	}

	@Override
	public CheckVideoConfTrialAccountResponse checkVideoConfTrialAccount(
			CheckVideoConfTrialAccountCommand cmd) {
		CheckVideoConfTrialAccountResponse response = new CheckVideoConfTrialAccountResponse();
		response.setTrialFlag(TrialFlag.OK.getCode());
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		if(enterprise != null && enterprise.getTrialAccountAmount() >0) {
			response.setTrialFlag(TrialFlag.REJECT.getCode());
		}
		return response;
	}

	@Override
	public void getVideoTrialConfAccount(GetVideoConfTrialAccountCommand cmd) {
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		ConfAccounts activeAccounts = vcProvider.findAccountByUserIdAndEnterpriseIdAndStatus(UserContext.current().getUser().getId(), cmd.getEnterpriseId(), ConfAccountStatus.ACTIVE.getCode());

		List<Long> categories = vcProvider.findAccountCategoriesByConfType((byte) 4);
		if (null == categories || categories.size() == 0){
			//没有初始化categories表,没有添加6方账号
			LOGGER.error("didnt found data in database : sql [ select * from eh_conf_account_categories where  conf_type=4;]");
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.CONF_CATEGORY_NOT_FOUND,  "HAVE NO CATEGORY");
		}
		if(enterprise != null && enterprise.getTrialAccountAmount() >0) {
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.CONF_CAN_NOT_TRIAL_MORE,  "you can only have one chance to trial");
		}
		//创建一个试用账号
		CreateConfAccountOrderCommand cmd2 = new CreateConfAccountOrderCommand();
		cmd2.setEnterpriseId(cmd.getEnterpriseId());
		cmd2.setAmount(new BigDecimal(0));
		cmd2.setBuyChannel((byte) 0);
		//到期日期手动设置为15天
		Calendar expiredDate = Calendar.getInstance();
		expiredDate.add(Calendar.DAY_OF_MONTH, 15);
		cmd2.setExpiredDate(expiredDate.getTimeInMillis());
		cmd2.setAccountCategoryId(categories.get(0));
		cmd2.setQuantity(1);
		cmd2.setPeriod(0);
		cmd2.setInvoiceFlag((byte) 0);
		cmd2.setMakeOutFlag((byte) 0);
		Long orderId = createConfAccountOrder(cmd2);
		//之前是否有活跃账号
		if(activeAccounts != null ) {
			throw RuntimeErrorException.errorWith(ConfServiceErrorCode.SCOPE, ConfServiceErrorCode.CONF_ENTERPRISE_HAS_ACTIVE_ACCOUNT,  "has active account");
		}
	}



	@Scheduled(cron = "0 20 11 * * ?")
	private void scheduledExpirationReminder(){
		//提前三天提醒试用的

		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			List<Long> categories = vcProvider.findAccountCategoriesByConfType((byte) 4);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			List<ConfOrders> orders = vcProvider.findConfOrdersByCategoriesAndDate(categories,calendar);
			for(ConfOrders order : orders){
				LOGGER.debug("send trial remainder to order "+ order);
				sendExpirationRemi1derPhoneMsg(order,calendar,SmsTemplateCode.VIDEO_TRIAL_EXPIRATION_REMINDER);
			}
			//提前7天提醒正式的
			categories = vcProvider.findAccountCategoriesByNotInConfType((byte) 4);
			calendar.add(Calendar.DAY_OF_MONTH, 4);
			orders = vcProvider.findConfOrdersByCategoriesAndDate(categories,calendar);
			for(ConfOrders order : orders){
				LOGGER.debug("send  remainder to order "+ order);
				sendExpirationRemi1derPhoneMsg(order,calendar,SmsTemplateCode.VIDEO_EXPIRATION_REMINDER);
			}
		}
	}

	private void sendExpirationRemi1derPhoneMsg(ConfOrders order ,Calendar calendar , int templateId) {
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		String phoneNum = findContactNumber(order);
		if(null == phoneNum)
			return;
		CrossShardListingLocator locator=new CrossShardListingLocator();
		int pageSize =Integer.MAX_VALUE;
		List<ConfOrderAccountMap> orderMap = vcProvider.findOrderAccountByOrderId(order.getId(), locator, pageSize,null);
		if(orderMap == null || orderMap.size() == 0){
			LOGGER.error("order cannot found order map "+ order );
			return;
		}
		StringBuilder accountName = new StringBuilder();

		orderMap.stream().map(r -> {
			processAccountName(accountName,r,phoneNum);
			return null;
		}).collect(Collectors.toList());
		List<Tuple<String, Object>> variables = smsProvider.toTupleList("accountName", accountName);
		smsProvider.addToTupleList(variables, "date", dateSF.format(calendar.getTime()));
		String templateLocale = RentalNotificationTemplateCode.locale;

		smsProvider.sendSms(orderMap.get(0).getConfAccountNamespaceId(), phoneNum, SmsTemplateCode.SCOPE, templateId, templateLocale, variables);

	}
	@Override
	public void testSendPhoneMsg(String phoneNum,int templateId,int namespaceId){
		scheduledExpirationReminder();
		List<Tuple<String, Object>> variables = smsProvider.toTupleList("accountName", "账号1");
		smsProvider.addToTupleList(variables, "date", "2017年5月22日");
		String templateLocale = RentalNotificationTemplateCode.locale;
		smsProvider.sendSms(namespaceId, phoneNum, SmsTemplateCode.SCOPE, templateId, templateLocale, variables);
	}
	private void processAccountName(StringBuilder accountName, ConfOrderAccountMap r,String phoneNum) {
		ConfAccounts account = vcProvider.findVideoconfAccountById(r.getConfAccountId());
		if(!accountName.equals(""))
			accountName.append("，");

		if(null != account.getOwnerId()){

			UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(account.getOwnerId(), IdentifierType.MOBILE.getCode()) ;
			if(null != userIdentifier)
				accountName.append(userIdentifier.getIdentifierToken());
		}else{
			accountName.append(phoneNum);
		}

	}

	private String findContactNumber(ConfOrders order) {
		if(null != order.getBuyerContact())
			return order.getBuyerContact();

		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
		if(null != userIdentifier)
			return userIdentifier.getIdentifierToken();
		return null;
	}

	@Override
	public GetVideoConfHelpUrlResponse getVideoConfHelpUrl() {
		String url = configurationProvider.getValue("video.help", "");
		GetVideoConfHelpUrlResponse response = new GetVideoConfHelpUrlResponse();
		response.setHelpUrl(url);
		return response;
	}


}
