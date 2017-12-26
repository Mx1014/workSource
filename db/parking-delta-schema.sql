
CREATE TABLE `eh_parking_spaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `space_no` varchar(64) NOT NULL DEFAULT '',
  `space_address` varchar(64) NOT NULL DEFAULT '',
  `lock_id` varchar(128) DEFAULT NULL,
  `lock_status` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_space_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `space_no` varchar(64) NOT NULL,
  `lock_id` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(64) DEFAULT NULL,
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_enterprise_name` varchar(128) DEFAULT NULL,
  `operate_type` tinyint(4) NOT NULL COMMENT '1: up, 2: down',
  `user_type` tinyint(4) NOT NULL COMMENT '1: booking person, 2: plate owner',
  `operate_uid` bigint(20) NOT NULL DEFAULT '0',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `menu_type` tinyint(4) DEFAULT 1 COMMENT '1: 通用 2:公司会议室',
ADD COLUMN `identify` varchar(64) DEFAULT NULL COMMENT '类型标识';











/**
 * <ul>
 * 查询默认规则
 * <li>exclusiveFlag: 是否是独占资源</li>
 * <li>unit: 1整租，0.5可半租</li>
 * <li>autoAssign: 是否需要自动分配资源</li>
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>needPay: 是否需要支付</li>
 * <li>multiTimeInterval: 是否允许预约多个时段</li>
 * <li>attachments: 预约需要提交的信息</li>
 * <li>rentalType: 预约类型，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>rentalEndTime: 至少提前预约时间</li>
 * <li>rentalStartTime: 最多提前预约时间</li>
 * <li>timeStep: 最短可预约时长</li>
 * <li>timeIntervals: 开放时段</li>
 * <li>dayOpenTime: 每天的开放时间</li>
 * <li>dayCloseTime: 每天的关闭时间</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>openWeekday: 开放日期，从周日到周六是0123456，开放哪天就在数组传哪天</li>
 * <li>closeDates: 关闭日期</li>
 * <li>workdayPrice: 园区客户工作日价格</li>
 * <li>weekendPrice: 园区客户节假日价格</li>
 * <li>siteCounts: 可预约个数</li>
 * <li>siteNumbers: 用户填写的可预约个数个场所编号{@link com.everhomes.rest.rentalv2.admin.SiteNumberDTO}}</li>
 * <li>cancelTime: 至少提前取消时间</li>
 * <li>refundFlag: 是否允许退款</li>
 * <li>refundRatio: 退款比例</li>
 * <li>discountType: 状态，0不打折1满钱减钱优惠 2满天减钱 3 比例 参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满多少钱</li>
 * <li>cutPrice: 减多少钱</li>
 * <li>discountRatio: 折扣比例</li>
 * <li>rentalEndTimeFlag: 至少提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalStartTimeFlag: 最多提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>orgMemberWorkdayPrice: 企业内部工作日价格</li>
 * <li>orgMemberWeekendPrice: 企业内部节假日价格</li>
 * <li>approvingUserWorkdayPrice: 外部客户工作日价格</li>
 * <li>approvingUserWeekendPrice: 外部客户节假日价格</li>
 * <li>halfDayTimeIntervals: 半天时间设置 {@link com.everhomes.rest.rentalv2.admin.TimeIntervalDTO}</li>
 * <li>rentalTypes: 时间单元类型列表 {@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceRules: 价格策略列表 {@link com.everhomes.rest.rentalv2.admin.PriceRuleDTO}</li>
 * <li>pricePackages: 套餐价格表{@link com.everhomes.rest.rentalv2.admin.PricePackageDTO}</li>
 * </ul>
 */


ALTER TABLE `eh_rentalv2_default_rules`
drop column pay_start_time,
drop column pay_end_time,
drop column payment_ratio,
drop column contact_num,
drop column overtime_time,
drop column unit,
drop column multi_unit,
drop column rental_step,
drop column resource_counts,
drop column day_open_time,
drop column day_close_time,
drop column time_step,
drop column cancel_time,
drop column cancel_flag;































