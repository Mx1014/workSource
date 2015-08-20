// @formatter:off
// generated at 2015-08-20 18:05:54
package com.everhomes.category;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.category.CategoryDTO;

public class ListChildrenRestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListChildrenRestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
