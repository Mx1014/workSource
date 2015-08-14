// @formatter:off
// generated at 2015-08-14 13:59:48
package com.everhomes.openapi;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

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
