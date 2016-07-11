package com.everhomes.quality;


import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTasks;
import com.everhomes.util.StringHelper;

public class QualityInspectionTasks extends EhQualityInspectionTasks{

	private static final long serialVersionUID = 896277642113935133L;
	
	private String categoryName;
	
	private Byte taskFlag;
	
	private QualityInspectionTaskRecords record;
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Byte getTaskFlag() {
		return taskFlag;
	}

	public void setTaskFlag(Byte taskFlag) {
		this.taskFlag = taskFlag;
	}

	public QualityInspectionTaskRecords getRecord() {
		return record;
	}

	public void setRecord(QualityInspectionTaskRecords record) {
		this.record = record;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
