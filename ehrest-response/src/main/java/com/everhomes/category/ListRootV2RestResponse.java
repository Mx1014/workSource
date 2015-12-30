// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.category;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.RestResponseBase;

import java.util.List;

public class ListRootV2RestResponse extends RestResponseBase {

    private List<CategoryDTO> response;

    public ListRootV2RestResponse () {
    }

    public List<CategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CategoryDTO> response) {
        this.response = response;
    }
}
