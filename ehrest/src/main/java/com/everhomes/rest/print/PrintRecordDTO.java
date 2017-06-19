// @formatter:off
package com.everhomes.rest.print;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 
 * <ul>
 * <li>id : 订单id</li>
 * <li>jobType : 订单状态，全部则为空, 参考 {@link com.everhomes.rest.print.PrintJobTypeType}</li>
 * <li>creatorUid : 发起人id</li>
 * <li>nickName : 发起人名称</li>
 * <li>creatorPhone : 发起人电话</li>
 * <li>createTime : 发起时间</li>
 * <li>orderTotalFee : 金额</li>
 * <li>orderStatus : 订单状态，全部则为空, 参考 {@link com.everhomes.rest.print.PrintOrderStatusType}</li>
 * <li>printDocumentName : 打印文档名称</li>
 * <li>detail : 详情</li>
 * <li>orderNo : 订单编号</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PrintRecordDTO {
	private Long id;
	private Byte jobType;
	private Long creatorUid;
	private String nickName;
	private String creatorPhone;
	private Timestamp createTime;
	private BigDecimal orderTotalFee;
	private Byte orderStatus;
	private String printDocumentName;
	private String detail;
	private Long orderNo;

}
