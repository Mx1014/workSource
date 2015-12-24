package com.everhomes.videoconf;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryConstants;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactEntry;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.VendorType;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.techpark.onlinePay.PayStatus;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import com.mysql.jdbc.StringUtils;

@Component
public class VideoConfServiceImpl implements VideoConfService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VideoConfServiceImpl.class);
	
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
	public void createVideoConfInvitation(CreateVideoConfInvitationCommand cmd) {

		User user = UserContext.current().getUser();
		String userName = user.getNickName();
		if(cmd.getChannel() == 0) {
			
		}
		
		if(cmd.getChannel() == 1) {

            String msgSubject = getMsgSubject(userName, cmd.getConfName());
            String confLink = getConfLink(cmd.getConfId());
            String confTime = getConfTime(cmd.getConfTime(), cmd.getDuration());
            String text = msgSubject + confLink + confTime;
			try {
				smsProvider.sendSms(cmd.getInvitee(), text, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(cmd.getChannel() == 2) {
			
		}

	}
	
	private Timestamp addMinutes(Long begin, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(begin));
		calendar.add(Calendar.MINUTE, minutes);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}

	private String getConfTime(Long confTime, int duration) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("begin", new Timestamp(confTime));
		map.put("end", addMinutes(confTime, duration));

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
	
	private String getConfLink(Integer confId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("confId", confId);

		String scope = VideoconfNotificationTemplateCode.SCOPE;
        int code = VideoconfNotificationTemplateCode.VIDEOCONF_ADDR_LINK;
        
        String confLink = localeTemplateService.getLocaleTemplateString(scope, code, "zh_CN", map, "");
        
        return confLink;
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
		
		rule.setAmount(cmd.getPackagePrice());
		rule.setMinPeriod(cmd.getMinimumMonths());
		
		if(AccountType.ACCOUNT_TYPE_SINGLE.getCode().equals(cmd.getAccountType())) {
			rule.setChannelType((byte) 0);
		}
		if(AccountType.ACCOUNT_TYPE_MULTIPLE.getCode().equals(cmd.getAccountType())) {
			rule.setChannelType((byte) 1);
		}
		
		//0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话
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
		if(cmd.getId() != null) {
			rule.setId(cmd.getId());
			vcProvider.updateConfAccountCategories(rule);
		} else {
			vcProvider.createConfAccountCategories(rule);
		}
		
	}

	@Override
	public ListVideoConfAccountRuleResponse listConfAccountCategories(
			ListRuleCommand cmd) {
		ListVideoConfAccountRuleResponse response = new ListVideoConfAccountRuleResponse();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer offset = cmd.getPageOffset() == null ? 0 : (cmd.getPageOffset() - 1 ) * pageSize;
		
		List<VideoConfAccountRuleDTO> rules = vcProvider.listConfAccountCategories(cmd.getChannelType(), cmd.getConfType(), offset, pageSize + 1).stream().map(r -> {
			
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
			ruleDto.setMinimumMonths(rule.getMinPeriod());
			ruleDto.setPackagePrice(rule.getAmount());
			if(rule.getChannelType() == 0) {
				ruleDto.setAccountType(AccountType.ACCOUNT_TYPE_SINGLE.getCode());
			}
			
			if(rule.getChannelType() == 1) {
				ruleDto.setAccountType(AccountType.ACCOUNT_TYPE_MULTIPLE.getCode());
			}
			
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
	public ListWarningContactorResponse listWarningContactor(ListRuleCommand cmd) {

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
		Integer offset = cmd.getPageOffset() == null ? 0 : (cmd.getPageOffset() - 1 ) * pageSize;
		
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
							r.setConfId(conf.getConfId());
					}
				}
				
				return toSourceAccounDto(r);
			}).collect(Collectors.toList());
			
			if(accounts != null && accounts.size() > pageSize) {
				accounts.remove(accounts.size() - 1);
				response.setNextPageOffset(cmd.getPageOffset() + 1);
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
		if(account.getOccupyAccountId() != null) {
			accountDto.setOccupyFlag((byte) 1);
			accountDto.setConfId(account.getConfId());
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
		
		return accountDto;
		
	}

	@Override
	public ListEnterpriseWithVideoConfAccountResponse listEnterpriseWithVideoConfAccount(
			ListEnterpriseWithVideoConfAccountCommand cmd) {
		
		ListEnterpriseWithVideoConfAccountResponse response = new ListEnterpriseWithVideoConfAccountResponse();
		
		CrossShardListingLocator locator=new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
	    int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
	    
	    List<ConfEnterprises> enterprises = vcProvider.listEnterpriseWithVideoConfAccount(cmd.getNamespaceId(), cmd.getStatus(), locator, pageSize+1);
	    Long nextPageAnchor = null;
		if(enterprises != null && enterprises.size() > pageSize) {
			enterprises.remove(enterprises.size() - 1);
			nextPageAnchor = enterprises.get(enterprises.size() -1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		
	    List<EnterpriseConfAccountDTO> enterpriseDto = enterprises.stream().map(r -> {

	    	EnterpriseConfAccountDTO dto = new EnterpriseConfAccountDTO();
	    	dto.setId(r.getId());
	    	dto.setEnterpriseId(r.getEnterpriseId());
	    	Enterprise enterprise = enterpriseProvider.findEnterpriseById(r.getEnterpriseId());
	    	dto.setEnterpriseName(enterprise.getName());
	    	dto.setEnterpriseDisplayName(enterprise.getDisplayName());
	    	dto.setEnterpriseContactor(r.getContactName());
	    	dto.setMobile(r.getContact());
	    	if(r.getActiveAccountAmount() > 0)
	    		dto.setUseStatus((byte) 0);
	    	if(r.getActiveAccountAmount() == 0 && r.getTrialAccountAmount() > 0) {
	    		dto.setUseStatus((byte) 1);
	    	}
	    	if(r.getActiveAccountAmount() == 0 && r.getTrialAccountAmount() == 0) {
	    		dto.setUseStatus((byte) 2);
	    	}
	    	
	    	dto.setStatus(r.getStatus());
	    	dto.setTotalAccount(r.getAccountAmount());
	    	dto.setValidAccount(r.getActiveAccountAmount());
	    	dto.setBuyChannel(r.getBuyChannel());
	    	return dto;
	    	
	    }).collect(Collectors.toList());
	    response.setEnterpriseConfAccounts(enterpriseDto);
	    
		return response;
	}


	@Override
	public void setEnterpriseLockStatus(EnterpriseLockStatusCommand cmd) {
		CrossShardListingLocator locator=new CrossShardListingLocator();
	    int pageSize = Integer.MAX_VALUE;
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		List<ConfAccounts> accounts = vcProvider.listConfAccountsByEnterpriseId(cmd.getEnterpriseId(), locator, pageSize);
		
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
		
		return response;
	}

	@Override
	public void updateVideoConfAccount(UpdateVideoConfAccountCommand cmd) {

		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		account.setExpiredDate(new Timestamp(cmd.getValidDate()));
		account.setAccountCategoryId(cmd.getAccountCategoryId());

		if(account.getStatus() != 2) {
			if(account.getExpiredDate().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
	        	account.setStatus((byte) 0);
	        }
	        else {
	        	account.setStatus((byte) 1);
	        }
		}
		
		vcProvider.updateConfAccounts(account);

	}

	@Override
	public void deleteVideoConfAccount(DeleteVideoConfAccountCommand cmd) {

		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		account.setDeleteUid(UserContext.current().getUser().getId());
		account.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		vcProvider.updateConfAccounts(account);
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
		
		ConfAccountCategories category = vcProvider.findAccountCategoriesById(order.getAccountCategoryId());
		if(category != null)
			dto.setAccountChannelType(category.getChannelType());
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
		
		List<ConfOrderAccountMap> order = vcProvider.findOrderAccountByOrderId(cmd.getOrderId(), locator, pageSize+1);
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
			EnterpriseContact contact = enterpriseContactProvider.queryContactByUserId(r.getEnterpriseId(), r.getOwnerId());
			if(contact != null) {
				accountDto.setDepartment(contact.getStringTag1());
				accountDto.setUserName(contact.getName());
				
				List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
				if(entry != null && entry.size() > 0) {
					accountDto.setMobile(entry.get(0).getEntryValue());
				}
			}
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
		ConfEnterprises confEnterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		confEnterprise.setContactName(cmd.getContactor());
		confEnterprise.setContact(cmd.getMobile());
		vcProvider.updateVideoconfEnterprise(confEnterprise);
		
		ConfOrders order = vcProvider.findOredrById(cmd.getId());
		
		//xiongying
		order.setAmount(cmd.getAmount());
//		order.setInvoiceReqFlag(cmd.getInvoiceFlag());
		order.setInvoiceIssueFlag(cmd.getMakeOutFlag());
		order.setOnlineFlag(cmd.getBuyChannel());
		vcProvider.updateConfOrders(order);

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

		return null;
	}

	@Override
	public ListEnterpriseVideoConfAccountResponse listVideoConfAccountByEnterpriseId(
			ListEnterpriseVideoConfAccountCommand cmd) {

		ListEnterpriseVideoConfAccountResponse response = new ListEnterpriseVideoConfAccountResponse();
		CrossShardListingLocator locator=new CrossShardListingLocator();
	    locator.setAnchor(cmd.getPageAnchor());
	    int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<ConfAccounts> accounts = vcProvider.listConfAccountsByEnterpriseId(cmd.getEnterpriseId(), locator, pageSize+1);
		
		Long nextPageAnchor = null;
		if(accounts != null && accounts.size() > pageSize) {
			accounts.remove(accounts.size() - 1);
			nextPageAnchor = accounts.get(accounts.size() -1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		
		List<ConfAccountDTO> confAccounts = accounts.stream().map(r -> {
			
			ConfAccountDTO dto = new ConfAccountDTO();
			
			dto.setId(r.getId());
			dto.setUserId(r.getOwnerId());
			dto.setValidDate(r.getExpiredDate());
			dto.setUpdateDate(r.getUpdateTime());
			if(r.getAccountType() == 1)
				dto.setUserType((byte) 0);
			else {
				if(vcProvider.countOrdersByAccountId(r.getId()) == 1)
					dto.setUserType((byte) 1);
				else {
					dto.setUserType((byte) 2);
				}
			}
			dto.setStatus(r.getStatus());
			if(new Timestamp(DateHelper.currentGMTTime().getTime()).after(r.getExpiredDate()))
				dto.setValidFlag((byte) 0);
			else {
				dto.setValidFlag((byte) 1);
			}
			ConfAccountCategories category = vcProvider.findAccountCategoriesById(r.getAccountCategoryId());
			if(category != null) {
				dto.setAccoutnType(category.getChannelType());
				dto.setConfType(category.getConfType());
			}
			
			Enterprise enterprise = enterpriseProvider.findEnterpriseById(r.getEnterpriseId());
			dto.setEnterpriseName(enterprise.getName());
			EnterpriseContact contact = enterpriseContactProvider.queryContactByUserId(r.getEnterpriseId(), r.getOwnerId());
			if(contact != null) {
				dto.setDepartment(contact.getStringTag1());
				dto.setUserName(contact.getName());
				List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(contact);
				if(entry != null && entry.size() >0) {
					dto.setMobile(entry.get(0).getEntryValue());
				}
			}
			return dto;
		}).collect(Collectors.toList());
		response.setConfAccounts(confAccounts);
		
		return response;
	}

	private Timestamp addMonth(Timestamp lastUpdate, int months) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastUpdate);
		calendar.add(Calendar.MONTH, months);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}
	
	@Override
	public void assignVideoConfAccount(
			AssignVideoConfAccountCommand cmd) {
		
		
		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		if(account == null)
			LOGGER.error("account is not exist!");

		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		if(now.before(addMonth(account.getOwnTime(), 3)))
			LOGGER.error("account has assigned in last 3 month!");
		account.setOwnerId(cmd.getUserId());
		account.setUpdateTime(now);
		account.setOwnTime(now);
		vcProvider.updateConfAccounts(account);

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
		vcProvider.createConfAccountHistories(history);
		
	}

//	@Override
//	public String applyVideoConfAccount(ApplyVideoConfAccountCommand cmd) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public ListUsersWithoutVideoConfPrivilegeResponse listUsersWithoutVideoConfPrivilege(
			ListUsersWithoutVideoConfPrivilegeCommand cmd) {
		ListUsersWithoutVideoConfPrivilegeResponse response = new ListUsersWithoutVideoConfPrivilegeResponse();
		List<Long> users = vcProvider.findUsersByEnterpriseId(cmd.getEnterpriseId());

		List<EnterpriseContact> ec = new ArrayList<EnterpriseContact>();
		List<EnterpriseContact> contact = enterpriseContactProvider.queryContactByEnterpriseId(cmd.getEnterpriseId());
		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		if(contact != null &contact.size() > 0) {
				for(EnterpriseContact con : contact) {
					if(!(users != null && users.contains(con.getUserId()))) {
						if(con.getUserId() > cmd.getPageAnchor())
							ec.add(con);
					}
						
				}
		}

		
		Long nextPageAnchor = null;
		if(ec != null && ec.size() > pageSize) {
			ec = ec.subList(0, pageSize);
			ec.remove(ec.size() - 1);
			nextPageAnchor = ec.get(ec.size() -1).getUserId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		
		List<EnterpriseUsersDTO> usersDto = ec.stream().map(r -> {
			EnterpriseUsersDTO user = new EnterpriseUsersDTO();
			user.setUserId(r.getUserId());
			user.setUserName(r.getName());
			user.setDepartment(r.getStringTag1());
			user.setContactId(r.getId());
			user.setEnterpriseId(r.getEnterpriseId());
			
			List<EnterpriseContactEntry> entry = enterpriseContactProvider.queryContactEntryByContactId(r);
			if(entry != null && entry.size() >0)
				user.setMobile(entry.get(0).getEntryValue());
			
			return user;
		}).collect(Collectors.toList());
		response.setEnterpriseUsers(usersDto);
		
		return response;
	}

	@Override
	public UserAccountDTO verifyVideoConfAccount(
			VerifyVideoConfAccountCommand cmd) {
		UserAccountDTO userAccount = new UserAccountDTO();
		ConfAccounts account = vcProvider.findAccountByUserId(cmd.getUserId());
		if(account != null) {
			userAccount.setAccountId(account.getId());
			userAccount.setStatus(account.getStatus());
			userAccount.setOccupyFlag(account.getAssignedFlag());
		}
		return userAccount;
	}

	@Override
	public void cancelVideoConf(CancelVideoConfCommand cmd) {

		ConfConferences conf = vcProvider.findConfConferencesByConfId(cmd.getConfId());
		
		if(conf != null && conf.getStatus() != 0) {
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
		
		String path = "http://api.confcloud.cn/openapi/confReservation";
		ConfAccounts account = vcProvider.findVideoconfAccountById(cmd.getAccountId());
		if(account != null) {
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
				    if("".equals(cmd.getConfName()))
				    	sPara.put("confName", "conference");
				    sPara.put("hostKey", cmd.getPassword());
				    sPara.put("startTime", sd);
				    sPara.put("duration", cmd.getDuration().toString());
				    if(cmd.getDuration() == 0)
				    	sPara.put("duration", "30");
				    sPara.put("optionJbh", "0");
				    NameValuePair[] param = generatNameValuePair(sPara);
				    HttpClient httpClient = new HttpClient();  
			          
			        HttpMethod method = postMethod(path, param);  
			          
			        httpClient.executeMethod(method);  
			          
			        String result = method.getResponseBodyAsString();  
			        System.out.println(result);  
					String json = strDecode(result);
		
					if(LOGGER.isDebugEnabled())
						LOGGER.error("startVideoConf,json="+json);
		
					ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
		
					if(LOGGER.isDebugEnabled())
						LOGGER.error("resultHolder="+resultHolder.isSuccess());
		
					if(resultHolder.getData() != null){
						Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
						
						response.setConfHostId(String.valueOf(data.get("userId")));
						response.setToken(String.valueOf(data.get("zoomToken")));
						response.setConfHostName(UserContext.current().getUser().getNickName());
						response.setMaxCount((Double.valueOf(String.valueOf(data.get("maxCount")))).intValue());
						response.setMeetingNo(String.valueOf(data.get("meetingNo")));
						
						Object obj = data.get("meetingNo");
						ConfConferences conf = new ConfConferences();
						conf.setConfId(Long.valueOf(String.valueOf(data.get("meetingNo"))));
						conf.setSubject(String.valueOf(data.get("meetingName")));
						conf.setStartTime(new Timestamp(timestamp));
						conf.setExpectDuration(cmd.getDuration());
						conf.setConfHostKey(String.valueOf(data.get("hostKey")));
						conf.setJoinPolicy(1);
						conf.setSourceAccountId(sourceAccount.getId());
						conf.setConfAccountId(cmd.getAccountId());
						conf.setCreatorUid(UserContext.current().getUser().getId());
						conf.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						conf.setConfHostId(String.valueOf(data.get("userId")));
//						conf.setConfHostName(confHostName);
						conf.setMaxCount((Double.valueOf(String.valueOf(data.get("maxCount")))).intValue());
						conf.setStatus((byte) 1);
						
						
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
					LOGGER.error("startVideoConf-error.sourceAccount="+sourceAccount.getAccountName()+".exception message="+e.getMessage());
				}
			} else {
				LOGGER.error("源账号不够");
			}
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

		
		UserIdentifier user = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(), cmd.getConfId());
		if(user != null) {
			ConfAccounts account = vcProvider.findAccountByUserId(user.getOwnerUid());
			if(account != null) {
				ConfConferences conf = vcProvider.findConfConferencesById(account.getAssignedConfId());
				if(conf != null) {
					response.setJoinUrl(conf.getJoinUrl());
					response.setCondId(conf.getConfId()+"");
					response.setPassword(conf.getConfHostKey());
				}
			}
		}
		
		if(StringUtils.isNullOrEmpty(response.getCondId())){
			Long confId = Long.valueOf(cmd.getConfId()); 
			ConfConferences conf = vcProvider.findConfConferencesByConfId(confId);
			if(conf != null) {
				response.setJoinUrl(conf.getJoinUrl());
				response.setCondId(conf.getConfId()+"");
				response.setPassword(conf.getConfHostKey());
			}
			
			else {
				LOGGER.error("conf id is wrong!");
			}
		}
		
		
		return response;
	}

	@Override
	public void reserveVideoConf(ReserveVideoConfCommand cmd) {
//		private java.lang.Byte     confHostName;
		
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
		
		ConfAccounts account = vcProvider.findAccountByUserId(user.getId());
		if(account != null) {
			reservation.setConfAccountId(account.getId());
			reservation.setEnterpriseId(account.getEnterpriseId());
		}
		
		if(cmd.getId() == null) {
			reservation.setCreatorUid(user.getId());
			reservation.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
	    
		ConfAccounts account = vcProvider.findAccountByUserId(user.getId());
		
		if(account != null) {
			List<ConfReservations> reservations = vcProvider.findReservationConfByAccountId(account.getId(), locator, pageSize+1);
			if(reservations != null && reservations.size() > 0) {
				
				Long nextPageAnchor = null;
				if(reservations != null && reservations.size() > pageSize) {
					reservations.remove(reservations.size() - 1);
					nextPageAnchor = reservations.get(reservations.size() -1).getId();
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
		List<Long> accountIds = cmd.getAccountIds();
		List<Long> userIds = cmd.getUserIds();
		if(userIds == null || userIds.isEmpty())
		if(userIds.size() > accountIds.size()) {
			LOGGER.error("user count is cannot larger than account count");
		}
		
//		accountIds.stream().map(accountId -> {
		for(Long accountId : accountIds) {	 
			if(userIds.size() > 0) {
				ConfAccounts account = vcProvider.findVideoconfAccountById(accountId);
				if(account == null)
					LOGGER.error("account is not exist!");
		
				Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
	
				account.setOwnerId(userIds.get(0));
				account.setUpdateTime(now);
				account.setOwnTime(now);
				vcProvider.updateConfAccounts(account);
				userIds.remove(0);
				
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
	public void createConfAccountOrder(CreateConfAccountOrderCommand cmd) {
		ConfOrders order = new ConfOrders();
		 
		order.setOwnerId(cmd.getEnterpriseId());
		order.setQuantity(cmd.getQuantity());
		order.setPeriod(cmd.getPeriod());
		order.setAmount(cmd.getAmount());
		order.setStatus(PayStatus.WAITING_FOR_PAY.getCode());
		order.setInvoiceReqFlag(cmd.getInvoiceFlag());
		order.setInvoiceIssueFlag(cmd.getMakeOutFlag());
//		order.setOnlineFlag(cmd.getBuyChannel());
		order.setAccountCategoryId(cmd.getAccountCategoryId());
		vcProvider.createConfOrders(order);
		
		if(order.getInvoiceReqFlag() == 1) {
			ConfInvoices invoice = ConvertHelper.convert(cmd.getInvoice(), ConfInvoices.class);
			vcProvider.createInvoice(invoice);
		}
		
		ConfEnterprises enterprise = vcProvider.findByEnterpriseId(cmd.getEnterpriseId());
		if(enterprise == null) {
			ConfEnterprises confEnterprise = new ConfEnterprises();
			confEnterprise.setEnterpriseId(cmd.getEnterpriseId());
			confEnterprise.setContactName(cmd.getContactor());
			confEnterprise.setContact(cmd.getMobile());
			confEnterprise.setStatus((byte) 1);
			
			User user = UserContext.current().getUser();
	    	Integer namespaceId = user.getNamespaceId();
	    	namespaceId = ( namespaceId == null ) ? 0 : namespaceId;
	    	confEnterprise.setNamespaceId(namespaceId);
			vcProvider.createConfEnterprises(confEnterprise);
		} else {
			enterprise.setContactName(cmd.getContactor());
			enterprise.setContact(cmd.getMobile());
			vcProvider.updateConfEnterprises(enterprise);
		}
			
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
		
		if(order.getStatus().byteValue() == PayStatus.WAITING_FOR_PAY.getCode()) {
			order.setOnlineFlag((byte) 1);
			this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode());
			
		}
		
		return order;
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
		order.setPayerId(UserContext.current().getUser().getId());
		order.setPaidTime(payTimeStamp);
		order.setStatus(paymentStatus);
		
		vcProvider.updateConfOrders(order);
		
		if(order.getStatus().byteValue() == PayStatus.PAID.getCode()) {
			for(int i = 0; i < order.getQuantity(); i++) {
				ConfAccounts account = new ConfAccounts();
				account.setStatus((byte) 1);
				account.setEnterpriseId(order.getOwnerId());
				account.setExpiredDate(addMonth(order.getPaidTime(), order.getPeriod()));
				account.setAccountCategoryId(order.getAccountCategoryId());
				vcProvider.createConfAccounts(account);
				
				ConfOrderAccountMap map = new ConfOrderAccountMap();
				map.setOrderId(order.getId());
				map.setEnterpriseId(order.getOwnerId());
				map.setConfAccountId(account.getId());
				vcProvider.createConfOrderAccountMap(map);

			}
			
			ConfEnterprises enterprise = vcProvider.findByEnterpriseId(order.getOwnerId());
			enterprise.setBuyChannel(order.getOnlineFlag());
			enterprise.setAccountAmount(enterprise.getAccountAmount()+order.getQuantity());
			enterprise.setActiveAccountAmount(enterprise.getActiveAccountAmount()+order.getQuantity());
			vcProvider.updateConfEnterprises(enterprise);
		}
		
	}
	
	private Long convertOrderNoToOrderId(String orderNo) {
		
		return Long.valueOf(orderNo);
	}

	@Override
	public void offlinePayBill(OfflinePayBillCommand cmd) {

		ConfOrders order = this.checkOrder(cmd.getOrderId());
		
		Long payTime = System.currentTimeMillis();
		Timestamp payTimeStamp = new Timestamp(payTime);
		this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode());
		
	}

}
