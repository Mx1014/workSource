package com.everhomes.contract.template;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.customer.CustomerService;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.GetEnterpriseCustomerCommand;
import com.everhomes.rest.investment.CustomerContactDTO;
import com.everhomes.rest.investment.CustomerContactType;
import com.everhomes.rest.investment.CustomerTrackerDTO;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "enterpriseCustomer")
public class EnterpriseCustomerContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseCustomerContractTemplate.class);
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private FieldService fieldService;
	
//	private EnterpriseCustomerDTO enterpriseCustomer;
//	
//	public EnterpriseCustomerDTO getEnterpriseCustomer() {
//		return enterpriseCustomer;
//	}
//
//	public void setEnterpriseCustomer(EnterpriseCustomerDTO enterpriseCustomer) {
//		this.enterpriseCustomer = enterpriseCustomer;
//	}

	/**
	 * 合法的例子：
	 * 合同编号：enterpriseCustomer.name
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
			return false;
		}
		if (!PropertyUtils.containsField(EnterpriseCustomerDTO.class, segments[1])) {
			return false;
		}
		return true;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		EnterpriseCustomerDTO enterpriseCustomer = null;
		Long customerId = contract.getCustomerId();
		if (customerId != null) {
			GetEnterpriseCustomerCommand cmd = new GetEnterpriseCustomerCommand();
			cmd.setId(customerId);
			enterpriseCustomer = customerService.getEnterpriseCustomer(cmd);
		}
		
		if (enterpriseCustomer != null) {
			String enterpriseCustomerInfoKey = segments[1];
			data = PropertyUtils.getProperty(enterpriseCustomer, enterpriseCustomerInfoKey);
			value = formatValue(segments,data,enterpriseCustomer);
		}
		
		return value;
	}

	@SuppressWarnings("unchecked")
	private String formatValue(String[] segments,Object data,EnterpriseCustomerDTO enterpriseCustomer){
		if (data == null && !"contacts".equals(segments[1]) && !"trackers".equals(segments[1])) {
			return "";
		}
		
		String value = "";
		switch (segments[1]) {
			case "contacts":
				if (segments.length >= 4) {
					if ("customer".equals(segments[2])) {
						if ("name".equals(segments[3])) {
							value = enterpriseCustomer.getContactName();
						}else if ("phoneNumber".equals(segments[3])) {
							value = enterpriseCustomer.getContactPhone();
						}
					}
				}
				break;
			case "trackers":
				if (segments.length >= 3) {
					if ("trackerName".equals(segments[2])) {
						value = enterpriseCustomer.getTrackingName();
					}else if ("trackerPhone".equals(segments[2])) {
						value = enterpriseCustomer.getTrackingPhone();
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
				value = getItemDisplayName(enterpriseCustomer,(Long) data);
				break;
			default:
				value = data.toString();
				break;
		}
		return value;
	}
	
	private String getItemDisplayName(EnterpriseCustomerDTO enterpriseCustomer,Long itemId){
		String itemDisplayName = "";
		if (itemId != null && enterpriseCustomer != null) {
			ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(enterpriseCustomer.getNamespaceId(),null, enterpriseCustomer.getCommunityId(), itemId);
		    if (scopeFieldItem != null) {
		    	itemDisplayName = scopeFieldItem.getItemDisplayName();
		    }
		}
		return itemDisplayName;
	}
	
}
