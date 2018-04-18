// @formatter:off
package com.everhomes.parking.elivejieshun;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/11 15:19
 */
public class EliveJieShunLogonReponse<ATTR,DATA> {
    private Integer resultCode;
    private String message;
    private String token;
    private String seqId;
    private String serviceId;
    private ATTR attributes;
    private DATA dataItems;

    public boolean isSuccess(){
        return resultCode!=null && resultCode==0;
    }

    public boolean isErrorToken(){
        return resultCode!=null && resultCode==6;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public ATTR getAttributes() {
        return attributes;
    }

    public void setAttributes(ATTR attributes) {
        this.attributes = attributes;
    }

    public DATA getDataItems() {
        return dataItems;
    }

    public void setDataItems(DATA dataItems) {
        this.dataItems = dataItems;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
