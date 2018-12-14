package com.everhomes.contract.template;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.investment.InvitedCustomerService;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ChangeMethod;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.PeriodUnit;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.investment.CustomerContactDTO;
import com.everhomes.rest.investment.CustomerContactType;
import com.everhomes.rest.investment.InvitedCustomerDTO;
import com.everhomes.rest.investment.ViewInvestmentDetailCommand;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import com.mysql.fabric.xmlrpc.base.Array;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "investmentPromotion")
public class InvestmentPromotionContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentPromotionContractTemplate.class);
	
	@Autowired
	private InvitedCustomerService invitedCustomerService;
	
	@Autowired
	private FieldService fieldService;
	
//	private InvitedCustomerDTO invitedCustomer;
//	
//	public InvitedCustomerDTO getInvitedCustomer() {
//		return invitedCustomer;
//	}
//
//	public void setInvitedCustomer(InvitedCustomerDTO invitedCustomer) {
//		this.invitedCustomer = invitedCustomer;
//	}

	/**
	 * 合法的例子：
	 * 合同编号：investmentPromotion.name
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
			return false;
		}
		if (!PropertyUtils.containsField(InvitedCustomerDTO.class, segments[1])) {
			return false;
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		InvitedCustomerDTO invitedCustomer = null;
		Long customerId = contract.getCustomerId();
		if (customerId != null) {
			ViewInvestmentDetailCommand cmd = new ViewInvestmentDetailCommand();
			cmd.setId(customerId);
			cmd.setIsAdmin(true);//跳过权限校验
			invitedCustomer = invitedCustomerService.viewInvestmentDetail(cmd);
		}
		
		if (invitedCustomer != null) {
			String invitedCustomerInfoKey = segments[1];
			data = PropertyUtils.getProperty(invitedCustomer, invitedCustomerInfoKey);
			value = formatValue(segments,data,invitedCustomer);
		}
		
		return value;
	}

	private String formatValue(String[] segments,Object data,InvitedCustomerDTO invitedCustomer){
		if (data == null) {
			return "";
		}
		
		String value = "";
		switch (segments[1]) {
			case "contacts":
				if ("customer".equals(segments[2])) {
					List<CustomerContactDTO> customerContacts = invitedCustomer.getContacts()
																.stream()
																.filter(r->r.getContactType()==CustomerContactType.CUSTOMER_CONTACT.getCode())
																.collect(Collectors.toList());
					if (customerContacts != null && customerContacts.size()>0) {
						if ("name".equals(segments[3])) {
							value = customerContacts.get(0).getName();
						}else if ("phoneNumber".equals(segments[3])) {
							value = customerContacts.get(0).getPhoneNumber();
						}
					}											
				}else if ("channel".equals(segments[2])) {
					List<CustomerContactDTO> channelContacts = invitedCustomer.getContacts()
																.stream()
																.filter(r->r.getContactType()==CustomerContactType.CHANNEL_CONTACT.getCode())
																.collect(Collectors.toList());
					if (channelContacts != null && channelContacts.size()>0) {
						if ("name".equals(segments[3])) {
							value = channelContacts.get(0).getName();
						}else if ("phoneNumber".equals(segments[3])) {
							value = channelContacts.get(0).getPhoneNumber();
						}
					}	
				}
				break;
			case "trackers":
				if (invitedCustomer.getTrackers()!=null && invitedCustomer.getTrackers().size()>0) {
					if ("trackerName".equals(segments[2])) {
						value = invitedCustomer.getTrackers().get(0).getTrackerName();
					}else if ("trackerPhone".equals(segments[2])) {
						value = invitedCustomer.getTrackers().get(0).getTrackerPhone();
					}
				}
				break;
			case "categoryItemId":
			case "levelItemId":
			case "contactGenderItemId":
			case "corpProductCategoryItemId":
			case "propertyType":
			case "corpNatureItemId":
			case "corpPurposeItemId":
			case "corpQualificationItemId":
			case "corpIndustryItemId":
			case "sourceItemId":
			case "registrationTypeId":
			case "technicalFieldId":
			case "taxpayerTypeId":
			case "relationWillingId":
			case "highAndNewTechId":
			case "entrepreneurialCharacteristicsId":
			case "serialEntrepreneurId":
			case "buyOrLeaseItemId":
			case "financingDemandItemId":
			case "dropBox1ItemId":
			case "dropBox2ItemId":
			case "dropBox3ItemId":
			case "dropBox4ItemId":
			case "dropBox5ItemId":
			case "dropBox6ItemId":
			case "dropBox7ItemId":
			case "dropBox8ItemId":
			case "dropBox9ItemId":
			case "dropBox10ItemId":
				value = getItemDisplayName(invitedCustomer,(Long) data);
				break;
			case "expectedSignDate": 
				value = formatTimeStamp((Long) data);
				break;
			default:
				value = data.toString();
				break;
		}
		return value;
	}
	
	private String getItemDisplayName(InvitedCustomerDTO invitedCustomer,Long itemId){
		String itemDisplayName = "";
		if (itemId != null && invitedCustomer != null) {
			ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(invitedCustomer.getNamespaceId(),null, invitedCustomer.getCommunityId(), itemId);
		    if (scopeFieldItem != null) {
		    	itemDisplayName = scopeFieldItem.getItemDisplayName();
		    }
		}
		return itemDisplayName;
	}
	
	private String formatTimeStamp(Long timeStamp){
		if (timeStamp != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			return simpleDateFormat.format(new Date(timeStamp));
		}
		return "";
	}
	
}
