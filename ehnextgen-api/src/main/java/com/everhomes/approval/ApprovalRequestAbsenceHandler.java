package com.everhomes.approval;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.rest.approval.AbsenceBasicDescription;
import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalTypeTemplateCode;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.TimeRange;
import com.everhomes.rest.approval.TrueOrFalseFlag;

/**
 * 
 * <ul>
 * 用于审批的处理请假的handler
 * </ul>
 */
@Component(ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + ApprovalTypeTemplateCode.ABSENCE)
public class ApprovalRequestAbsenceHandler extends ApprovalRequestDefaultHandler {

	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		ApprovalBasicInfoOfRequestDTO approvalBasicInfo = super.processApprovalBasicInfoOfRequest(approvalRequest);
		
		AbsenceBasicDescription description = new AbsenceBasicDescription();
		
		//1. 获取类别名称
		ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
		if (approvalCategory != null) {
			description.setCategoryName(approvalCategory.getCategoryName());
		}
		
		//2. 获取时间
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = approvalService.listTimeRangeByRequestId(approvalRequest.getId());
			timeRangeList.forEach(t->{
				description.setTimeTotal(calculateTimeTotal(description.getTimeTotal(), t.getActualResult()));
			});
			description.setTimeRangeList(timeRangeList);
		}
		
		approvalBasicInfo.setDescriptionJson(JSON.toJSONString(description));
		return approvalBasicInfo;
	}

	
	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = super.processBriefApprovalRequest(approvalRequest);
		String timeTotal = null;
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			List<TimeRange> timeRangeList = briefApprovalRequestDTO.getTimeRangeList();
			for (TimeRange timeRange : timeRangeList) {
				timeTotal = calculateTimeTotal(timeTotal, timeRange.getActualResult());
			}
		}
		briefApprovalRequestDTO.setDescription(timeTotal);
		briefApprovalRequestDTO.setReason(JSONObject.parseObject(approvalRequest.getContentJson()).getString("reason"));
		
		return briefApprovalRequestDTO;
	}

	private String calculateTimeTotal(String timeTotal, String actualResult) {
		//表中按1.25.33这样存储，每一位分别代表天、小时、分钟，统计时需要每个位分别相加，且小时满24不用进一，分钟满60需要进一，如果某一位是0也必须存储，也就是说结果中必须包含两个小数点
		if (StringUtils.isBlank(timeTotal)) {
			return actualResult;
		}
		
		String[] times = timeTotal.split(".");
		String[] actuals = actualResult.split(".");
		
		int days = Integer.parseInt(times[0]) + Integer.parseInt(actuals[0]);
		int hours = Integer.parseInt(times[1]) + Integer.parseInt(actuals[1]);
		int minutes = Integer.parseInt(times[2]) + Integer.parseInt(actuals[2]);
		
		hours = hours + minutes / 60;
		minutes = minutes % 60;
		
		return days + "." + hours + "." + minutes;
	}
	
}
