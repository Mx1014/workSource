package com.everhomes.rentalv2;

public class RentalNotificationTemplateCode {
	public static final String SCOPE = "rental.notification";
	 public static final int RENTAL_BEGIN_NOTIFY = 1; //预定快开始的推送
	 public static final int RENTAL_ADMIN_NOTIFY = 2; //添加预定给管理员推送
	 public static final int RENTAL_APPLY_SUCCESS_CODE = 3; // 申请成功短信：预定成功给管理员发推送','您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系${offlinePayeeName}（${offlinePayeeContact}）上门收费，2. 到${offlineCashierAddress}付款；感谢您的使用。
	 public static final int RENTAL_APPLY_FAILURE_CODE = 4; // 申请失败短信：【正在会】您申请预约的{使用时间}的{资源名称}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。
	 public static final int RENTAL_PAY_SUCCESS_CODE = 5; // 支付成功短信：【正中会】您已完成支付，成功预约{使用时间}的{资源名称}，请按照预约的时段使用资源，感谢您的使用。
	 public static final int APPROVE_RENTAL_APPLY_SUCCESS_CODE = 6; // 审批线上支付模式，申请成功短信：【正中会】您申请预约的{使用时间}的{资源名称}已通过审批，为确保您成功预约，请尽快到APP完成在线支付，感谢您的使用。
//	  public static final  int RENTAL_TIMEOUT_AND_CANCEL_CODE = 32; // 超时自动取消短信：【正中会】您申请预约的{使用时间}的{资源名称}由于超时未支付，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。
	 public static final int RENTAL_CANCEL_CODE = 7; // 取消短信：【正中会】您申请预约的{使用时间}的{资源名称}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。
	 public static final int RENTAL_REMIND_CODE = 8; // 资催办-正中会:【正中发】客户（{申请人姓名}{申请人联系电话}）提交资源预约的线下支付申请，预约{资源名称}，使用时间：{使用时间}，订单金额{订单金额}，请尽快联系客户完成支付

	public static final int RENTAL_PROCESSOR_SUCCESS_CODE = 9; // 正中会-预成功:【正在会】客户{客户姓名}（{客户联系方式}）完成支付，成功预约{使用时间}的{资源名称}，请提前做好相关准备工作。

	 public static final String locale = "zh_CN";
	 
	 public static final String FLOW_SCOPE = "rental.flow";
	 public static final int RENTAL_FLOW_CONTENT = 1; //工作流content
	 public static final int RENTAL_FLOW_OFFLINE_INFO = 2; //工作流 线下支付的info
	 
 
	 
	 
}
