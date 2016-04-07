// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.category.CategoryDTO;

public class ListBizCategoriesRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListBizCategoriesRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
