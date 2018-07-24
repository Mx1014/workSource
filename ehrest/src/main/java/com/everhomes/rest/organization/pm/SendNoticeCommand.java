// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>communityId: 小区id</li>
 *     <li>buildingNames: 小区楼栋号列表</li>
 *     <li>buildingIds: buildingIds</li>
 *     <li>addressIds: 地址id列表</li>
 *     <li>mobilePhones: mobilePhones</li>
 *     <li>message: 消息内容</li>
 *     <li>messageBodyType: messageBodyType</li>
 *     <li>imgUri: imgUri</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>sendMode: 发送模式 {@link com.everhomes.rest.organization.pm.SendNoticeMode}</li>
 * </ul>
 */
public class SendNoticeCommand {

    private Long communityId;

    @ItemType(String.class)
    private List<String> buildingNames;

    @ItemType(Long.class)
    private List<Long> buildingIds;

    @ItemType(Long.class)
    private List<Long> addressIds;

    @ItemType(String.class)
    private List<String> mobilePhones;

    private String message;

    private String messageBodyType;

    private String imgUri;

    private Integer namespaceId;

    private String sendMode;
    
    //add by huanglm 20180711
    @ItemType(Long.class) 
    private List<Long> communityIds  ;
    
    /**
     * 用于后台保存记录ID
     */
    private Long logId ;

    public SendNoticeCommand() {
    }


    public Long getCommunityId() {
        return communityId;
    }


    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<String> getBuildingNames() {
        return buildingNames;

    }


    public void setBuildingNames(List<String> buildingNames) {
        this.buildingNames = buildingNames;
    }


    public List<Long> getAddressIds() {
        return addressIds;
    }


    public void setAddressIds(List<Long> addressIds) {
        this.addressIds = addressIds;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public List<String> getMobilePhones() {
        return mobilePhones;
    }


    public void setMobilePhones(List<String> mobilePhones) {
        this.mobilePhones = mobilePhones;
    }


    public List<Long> getBuildingIds() {
        return buildingIds;
    }


    public void setBuildingIds(List<Long> buildingIds) {
        this.buildingIds = buildingIds;
    }


    public String getMessageBodyType() {
        return messageBodyType;
    }


    public void setMessageBodyType(String messageBodyType) {
        this.messageBodyType = messageBodyType;
    }


    public String getImgUri() {
        return imgUri;
    }


    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getSendMode() {
        return sendMode;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


	public List<Long> getCommunityIds() {
		return communityIds;
	}


	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}


	public Long getLogId() {
		return logId;
	}


	public void setLogId(Long logId) {
		this.logId = logId;
	}
    
    
}
