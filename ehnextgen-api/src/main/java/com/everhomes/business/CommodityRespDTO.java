package com.everhomes.business;

import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
import com.everhomes.util.StringHelper;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq.tian on 2017/8/21.
 */
public class CommodityRespDTO {

    private String version;
    private Integer errorCode;
    @SerializedName("response")
    private List<ModulePromotionEntityDTO> response = new ArrayList<>();

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<ModulePromotionEntityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ModulePromotionEntityDTO> response) {
        this.response = response;
    }
}
