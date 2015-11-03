// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListInterestCategoriesRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListInterestCategoriesRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
