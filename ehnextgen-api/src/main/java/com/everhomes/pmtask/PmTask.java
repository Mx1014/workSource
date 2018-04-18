package com.everhomes.pmtask;


import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.util.StringHelper;

public class PmTask extends EhPmTasks {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

//	园区、服务类型聚合使用
	class OwnerCategory {
		private Long ownerId;
		private Long taskCategoryId;

		OwnerCategory(Long ownerId,Long taskCategoryId){
			this.ownerId = ownerId;
			this.taskCategoryId = taskCategoryId;
		}

		public Long getOwnerId() {
			return ownerId;
		}

		public void setOwnerId(Long ownerId) {
			this.ownerId = ownerId;
		}

		public Long getTaskCategoryId() {
			return taskCategoryId;
		}

		public void setTaskCategoryId(Long taskCategoryId) {
			this.taskCategoryId = taskCategoryId;
		}
	}
	public OwnerCategory getOwnerCategory(){
		return new OwnerCategory(this.getOwnerId(),this.getTaskCategoryId());
	}

	//	园区、服务状态聚合使用
	class OwnerStatus {
		private Long ownerId;
		private Byte status;

		public OwnerStatus(Long ownerId, Byte status) {
			this.ownerId = ownerId;
			this.status = status;
		}

		public Long getOwnerId() {
			return ownerId;
		}

		public void setOwnerId(Long ownerId) {
			this.ownerId = ownerId;
		}

		public Byte getStatus() {
			return status;
		}

		public void setStatus(Byte status) {
			this.status = status;
		}
	}

	public OwnerStatus getOwnerStatus(){
		return new OwnerStatus(this.getOwnerId(),this.getStatus());
	}

}
