package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListConfCategoryResponse {

	private int enterpriseVaildAccounts;
	
	@ItemType(ConfCategoryDTO.class)
	private List<ConfCategoryDTO> categories;

	public int getEnterpriseVaildAccounts() {
		return enterpriseVaildAccounts;
	}

	public void setEnterpriseVaildAccounts(int enterpriseVaildAccounts) {
		this.enterpriseVaildAccounts = enterpriseVaildAccounts;
	}

	public List<ConfCategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<ConfCategoryDTO> categories) {
		this.categories = categories;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }	
	
}
