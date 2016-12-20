package com.everhomes.rest.openapi.techpark;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class CustomerRental {
    private String name;
    private String number;
    private String contact;
    private String contactPhone;
    @ItemType(CustomerContract.class)
    private List<CustomerContract> contracts;
    private Boolean dealed;
    
    public Boolean getDealed() {
		return dealed;
	}

	public void setDealed(Boolean dealed) {
		this.dealed = dealed;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<CustomerContract> getContracts() {
        return contracts;
    }

    public void setContracts(List<CustomerContract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
