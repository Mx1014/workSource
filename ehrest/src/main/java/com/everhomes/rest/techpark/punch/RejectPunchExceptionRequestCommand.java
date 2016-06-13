package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
* <ul>驳回打卡异常申报
* <li>id：申报id</li>
* <li>processDetail：处理详情(只在驳回申请时必填项)</li>
* </ul>
*/
public class RejectPunchExceptionRequestCommand {

    private Long id;
    private String processDetail;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProcessDetail() {
		return processDetail;
	}
	public void setProcessDetail(String processDetail) {
		this.processDetail = processDetail;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 }
