// @formatter:off
// generated at 2015-08-19 15:26:32
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListActionCategoriesRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListActionCategoriesRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
