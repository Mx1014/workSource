// @formatter:off
// generated at 2015-10-15 10:18:58
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListBusinessSubCategoriesRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListBusinessSubCategoriesRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
