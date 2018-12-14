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

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "enterpriseCustomer")
public class EnterpriseCustomerContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseCustomerContractTemplate.class);
	
	@Autowired
	private CustomerService customerService;
	
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
			value = formatValue(segments,data);
		}
		
		return value;
	}

	@SuppressWarnings("unchecked")
	private String formatValue(String[] segments,Object data){
		if (data == null) {
			return "";
		}
		
		String value = "";
		switch (segments[1]) {
			case "contacts":
				if ("customer".equals(segments[2])) {
					List<CustomerContactDTO> customerContacts = ((List<CustomerContactDTO>) data)
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
					List<CustomerContactDTO> channelContacts = ((List<CustomerContactDTO>) data)
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
				List<CustomerTrackerDTO> trackers = (List<CustomerTrackerDTO>)data;
				if (trackers != null && trackers.size()>0) {
					if ("trackerName".equals(segments[2])) {
						value = trackers.get(0).getTrackerName();
					}else if ("trackerPhone".equals(segments[2])) {
						value = trackers.get(0).getTrackerPhone();
					}
				}
				break;
			default:
				value = data.toString();
				break;
		}
		return value;
	}
	
	private String formatTimeStamp(Long timeStamp){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(timeStamp));
	}
	
	private String formatTimeStamp(Timestamp timeStamp){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(timeStamp.getTime()));
	}
}
