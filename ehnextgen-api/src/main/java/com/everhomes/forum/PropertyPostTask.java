package com.everhomes.forum;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Post represents post (both original and replied) to a forum
 * 
 * @author Kelven Yang
 *
 */
public class PropertyPostTask extends Post {
    private static final long serialVersionUID = 3902864739541467548L;

    private Long   taskId;
	private Long   communityId;
	private String entityType;
	private Long   entityId;
	private String targetType;
	private Long   targetId;
	private String taskType;
	private Byte   taskStatus;
        
    public PropertyPostTask() {
    }

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Byte getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Byte taskStatus) {
		this.taskStatus = taskStatus;
	}

	 public String toString() {
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            return mapper.writeValueAsString(this);
	        } catch (JsonProcessingException e) {
	            return super.toString();
	        }
	    }
   
}
