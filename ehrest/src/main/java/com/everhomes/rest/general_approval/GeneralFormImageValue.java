package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


 /* @author copy from PostApprovalFormImageValue
  */
public class GeneralFormImageValue {

    @ItemType(String.class)
    private List<String> uris;

    @ItemType(String.class)
    private List<String> urls;

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
