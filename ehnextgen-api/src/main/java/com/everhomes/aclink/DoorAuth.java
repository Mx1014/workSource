package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import com.everhomes.util.StringHelper;


public class DoorAuth extends EhDoorAuth {

    /**
     * 
     */
    private static final long serialVersionUID = 8693100396412528058L;

    //创建人姓名 add by liqingyan

    private  String userName;

    public String getUserName;

    public String getLinglingId(String linglingId) {
        return AclinkAuthCustomField.AUTH_LINGLING_ID.getStringValue(this);
    }
    
    public void setLinglingId(String linglingId) {
        AclinkAuthCustomField.AUTH_LINGLING_ID.setStringValue(this, linglingId);
    }
    
    public Long getLinglingDoorId() {
        return AclinkAuthCustomField.AUTH_LINGLING_DOOR_ID.getIntegralValue(this);
    }
    
    public void setLinglingDoorId(Long doorId) {
        AclinkAuthCustomField.AUTH_LINGLING_DOOR_ID.setIntegralValue(this, doorId);
    }
    
    public Long getKeyValidTime() {
        return AclinkAuthCustomField.AUTH_LINGLING_VALID.getIntegralValue(this);
    }
    
    public void setKeyValidTime(Long validTime) {
        AclinkAuthCustomField.AUTH_LINGLING_VALID.setIntegralValue(this, validTime);
    }
    
    public Long getCurrStorey() {
        return AclinkAuthCustomField.AUTH_LINGLING_STOREY.getIntegralValue(this);
    }
    
    public void setCurrStorey(Long storey) {
        AclinkAuthCustomField.AUTH_LINGLING_STOREY.setIntegralValue(this, storey);
    }
    
    public String getLinglingSDKKey() {
        return AclinkAuthCustomField.AUTH_LINGLING_SDK_KEY.getStringValue(this);
    }
    
    public void setLinglingSDKKey(String key) {
        AclinkAuthCustomField.AUTH_LINGLING_SDK_KEY.setStringValue(this, key);
    }
    
    public String getLinglingUuid() {
        return AclinkAuthCustomField.AUTH_LINGLING_UUID.getStringValue(this);
    }
    
    public void setLinglingUuid(String uuid) {
        AclinkAuthCustomField.AUTH_LINGLING_UUID.setStringValue(this, uuid);
    }
    
    public String getApplyUserName() {
        return AclinkAuthCustomField.AUTH_APPLY_USERNAME.getStringValue(this);
    }
    
    public void setApplyUserName(String uname) {
        AclinkAuthCustomField.AUTH_APPLY_USERNAME.setStringValue(this, uname);
    }
    
    public String getVisitorEvent() {
        return AclinkAuthCustomField.AUTH_VISITOR_EVENT.getStringValue(this);
    }
    
    public void setVisitorEvent(String eventName) {
        AclinkAuthCustomField.AUTH_VISITOR_EVENT.setStringValue(this, eventName);
    }
    
    public String getQrKey() {
        return AclinkAuthCustomField.AUTH_QR_KEY.getStringValue(this);
    }
    
    public void setQrKey(String key) {
        AclinkAuthCustomField.AUTH_QR_KEY.setStringValue(this, key);
    }
    
    public String getAuthMethod() {
        return AclinkAuthCustomField.AUTH_METHOD.getStringValue(this);
    }
    
    public void setAuthMethod(String authMethod) {
        AclinkAuthCustomField.AUTH_METHOD.setStringValue(this, authMethod);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
