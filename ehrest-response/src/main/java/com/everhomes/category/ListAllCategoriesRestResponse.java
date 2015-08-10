// @formatter:off
// generated at 2015-08-10 11:20:28
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListAllCategoriesRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListAllCategoriesRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
