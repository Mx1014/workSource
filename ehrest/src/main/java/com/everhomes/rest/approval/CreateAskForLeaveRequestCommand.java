// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.rest.news.AttachmentDescriptor;

/**
 * 
 * <ul>参数：
 * <li>: </li>
 * </ul>
 */
public class CreateAskForLeaveRequestCommand {
	private String sceneToken;
	private Long categoryId;
	private String reason;
	private List<TimeRange> timeRangeList;
	private List<AttachmentDescriptor> attachmentList;
}
