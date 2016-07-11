package com.everhomes.contentserver;

import java.util.Map;

/**
 * the request message name:contentstorage.request.<AccessType>.<MessageType>
 * 
 * @author elians
 *
 */
public class ContentServerBase {
    // message type (lookup,uploaded,delete,unknown)
    // message format auth.<xx>
    private OperationMode messageType;

    // token string
    private String token;

    // md5 string
    private String md5;

    // include image audio video unknown
    private ResourceType objectType;

    private Integer totalSize;

    private String objectId;

    private transient AccessType accessType;

    private Map<String, String> meta;

    private Map<String, String> paramsMap;

    private String filename;
    
    private String url;

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public ResourceType getObjectType() {
        return objectType;
    }

    public void setObjectType(String type) {
        this.objectType = ResourceType.fromStringCode(type);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public OperationMode getMessageType() {
        return messageType;
    }

    public void setMessageType(OperationMode messageType) {
        this.messageType = messageType;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
