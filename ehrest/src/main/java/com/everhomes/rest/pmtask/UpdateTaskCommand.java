package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>categoryId: 分类ID</li>
 * <li>priority: 客户反映</li>
 * <li>reserveTime: 预约时间</li>
 * <li>sourceType: 报事来源</li>
 * <li>taskId: 任务ID</li>
 * </ul>
 */
public class UpdateTaskCommand {
	private String ownerType;
    private Long ownerId;
	private Long taskId;
	private Long categoryId;
	private Byte priority;
	private String sourceType;
	private Long reserveTime;
}
