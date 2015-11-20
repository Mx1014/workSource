// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListInterestCategoriesV2RestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListInterestCategoriesV2RestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
