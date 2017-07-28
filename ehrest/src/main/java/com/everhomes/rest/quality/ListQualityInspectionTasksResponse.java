package com.everhomes.rest.quality;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>tasks: 参考com.everhomes.rest.quality.QualityInspectionTaskDTO</li>
 *  <li>sampleName: 例行检查名称</li>
 *  <li>todayExecutedCount: 当日已执行任务数</li>
 *  <li>communityCount: 例行检查关联项目数</li>
 *  <li>startTime: 例行检查开始时间</li>
 *  <li>endTime: 例行检查结束时间</li>
 *  <li>pageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListQualityInspectionTasksResponse {
	
	@ItemType(QualityInspectionTaskDTO.class)
	private List<QualityInspectionTaskDTO> tasks;

	private Integer todayExecutedCount;

	private String sampleName;

	private Integer communityCount;

	private Timestamp startTime;

	private Timestamp endTime;
	
	private Long nextPageAnchor;

	public ListQualityInspectionTasksResponse() {
		
	}
	public ListQualityInspectionTasksResponse(Long nextPageAnchor, 
			List<QualityInspectionTaskDTO> tasks) {
        this.nextPageAnchor = nextPageAnchor;
        this.tasks = tasks;
    }

	public Integer getTodayExecutedCount() {
		return todayExecutedCount;
	}

	public void setTodayExecutedCount(Integer todayExecutedCount) {
		this.todayExecutedCount = todayExecutedCount;
	}

	public List<QualityInspectionTaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<QualityInspectionTaskDTO> tasks) {
		this.tasks = tasks;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public Integer getCommunityCount() {
		return communityCount;
	}

	public void setCommunityCount(Integer communityCount) {
		this.communityCount = communityCount;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
