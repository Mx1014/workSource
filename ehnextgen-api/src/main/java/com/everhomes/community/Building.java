package com.everhomes.community;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.util.StringHelper;

public class Building extends EhBuildings{

	private static final long serialVersionUID = -5800525047417779384L;
	
	private List<BuildingAttachment> attachments = new ArrayList<BuildingAttachment>();

	private String managerNickName;
    
    private String managerAvatar;
    
    private String managerAvatarUrl;

    private String operateNickName;
    
    private String operateAvatar;
    
    private String operateAvatarUrl;

    private String creatorNickName;
    
    private String creatorAvatar;
    
    private String creatorAvatarUrl;
    
    private String posterUrl;

	public List<BuildingAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<BuildingAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getManagerNickName() {
		return managerNickName;
	}

	public void setManagerNickName(String managerNickName) {
		this.managerNickName = managerNickName;
	}

	public String getManagerAvatar() {
		return managerAvatar;
	}

	public void setManagerAvatar(String managerAvatar) {
		this.managerAvatar = managerAvatar;
	}

	public String getManagerAvatarUrl() {
		return managerAvatarUrl;
	}

	public void setManagerAvatarUrl(String managerAvatarUrl) {
		this.managerAvatarUrl = managerAvatarUrl;
	}

	public String getOperateNickName() {
		return operateNickName;
	}

	public void setOperateNickName(String operateNickName) {
		this.operateNickName = operateNickName;
	}

	public String getOperateAvatar() {
		return operateAvatar;
	}

	public void setOperateAvatar(String operateAvatar) {
		this.operateAvatar = operateAvatar;
	}

	public String getOperateAvatarUrl() {
		return operateAvatarUrl;
	}

	public void setOperateAvatarUrl(String operateAvatarUrl) {
		this.operateAvatarUrl = operateAvatarUrl;
	}

	public String getCreatorNickName() {
		return creatorNickName;
	}

	public void setCreatorNickName(String creatorNickName) {
		this.creatorNickName = creatorNickName;
	}

	public String getCreatorAvatar() {
		return creatorAvatar;
	}

	public void setCreatorAvatar(String creatorAvatar) {
		this.creatorAvatar = creatorAvatar;
	}

	public String getCreatorAvatarUrl() {
		return creatorAvatarUrl;
	}

	public void setCreatorAvatarUrl(String creatorAvatarUrl) {
		this.creatorAvatarUrl = creatorAvatarUrl;
	}

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
