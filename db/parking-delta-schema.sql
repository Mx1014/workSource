
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





CREATE TABLE `eh_rentalv2_order_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'default_rule, resource_rule',
  `owner_id` bigint(20) DEFAULT NULL,
  `handle_type` tinyint(4) DEFAULT NULL COMMENT '1: 退款, 2: 加收',
  `duration_type` tinyint(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` varchar(20) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` double DEFAULT NULL COMMENT '时长',
  `factor` double DEFAULT NULL COMMENT '价格系数',

  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





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
drop column rental_step,
drop column day_open_time,
drop column day_close_time,
drop column time_step,
drop column cancel_time,
drop column cancel_flag;

ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `resource_type` varchar(64) NOT NULL COMMENT '资源类型',
ADD COLUMN `holiday_open_flag` tinyint(4) DEFAULT NULL COMMENT '节假日是否开放预约: 1-是, 0-否',
ADD COLUMN `holiday_type` tinyint(4) DEFAULT NULL COMMENT '1-普通双休, 0-同步中国节假日',
ADD COLUMN `refund_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',
ADD COLUMN `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full';

ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `resource_type` varchar(64) NOT NULL COMMENT '资源类型';



CREATE TABLE `eh_rentalv2_default_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type: community, organization',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'community id or organization id',
  `resource_type_id` bigint(20) DEFAULT NULL COMMENT 'resource type id',

  `need_pay` tinyint(4) DEFAULT NULL COMMENT '是否需要支付: 1-是, 0-否',

  `begin_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',

  `multi_time_interval` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个时段: 1-是, 0-否',
  `rental_start_time_flag` tinyint(4) DEFAULT '0' COMMENT '最多提前多少时间预定标志: 1-限制, 0-不限制',
  `rental_end_time_flag` tinyint(4) DEFAULT '0' COMMENT '最少提前多少时间预定标志: 1-限制, 0-不限制',
  `rental_start_time` bigint(20) DEFAULT NULL COMMENT '最多提前多少时间预定',
  `rental_end_time` bigint(20) DEFAULT NULL COMMENT '最少提前多少时间预定',


  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,

  `open_weekday` varchar(7) DEFAULT NULL COMMENT '7位二进制，0000000每一位表示星期7123456',
  `multi_unit` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个场所: 1-是, 0-否',
  `auto_assign` tinyint(4) DEFAULT NULL COMMENT '是否动态分配: 1-是, 0-否',
  `resource_counts` double DEFAULT NULL COMMENT '可预约个数',

  `refund_flag` tinyint(4) DEFAULT NULL COMMENT '是否支持退款: 1-是, 0-否',
  `refund_ratio` int(11) DEFAULT NULL COMMENT '退款比例',

  --`rental_type` tinyint(4) DEFAULT NULL COMMENT '0-as hour:min, 1-as half day, 2-as day, 3-支持晚上的半天',

  --`pay_start_time` bigint(20) DEFAULT NULL,
  --`pay_end_time` bigint(20) DEFAULT NULL,
  --`payment_ratio` int(11) DEFAULT NULL COMMENT 'payment ratio',

  --`contact_num` varchar(20) DEFAULT NULL COMMENT 'phone number',

  --`overtime_time` bigint(20) DEFAULT NULL COMMENT '超期时间',
  --已经没有用到`exclusive_flag` tinyint(4) DEFAULT NULL COMMENT '是否为独占资源: 0-否, 1-是',
  --`unit` double DEFAULT NULL COMMENT '1-整租, 0.5-可半个租',

  --`rental_step` int(11) DEFAULT NULL COMMENT 'how many time_step must be rental every time',


  --`day_open_time` double DEFAULT NULL,
  --`day_close_time` double DEFAULT NULL,
  --`time_step` double DEFAULT NULL COMMENT '步长，每个单元格是多少小时（半小时是0.5）',

  --`workday_price` decimal(10,2) DEFAULT NULL COMMENT '工作日价格',
  --`weekend_price` decimal(10,2) DEFAULT NULL COMMENT '周末价格',
  --`org_member_workday_price` decimal(10,2) DEFAULT NULL COMMENT '企业内部工作日价格',
  --`org_member_weekend_price` decimal(10,2) DEFAULT NULL COMMENT '企业内部节假日价格',
  --`approving_user_workday_price` decimal(10,2) DEFAULT NULL COMMENT '外部客户工作日价格',
  --`approving_user_weekend_price` decimal(10,2) DEFAULT NULL COMMENT '外部客户节假日价格',
  --`cancel_time` bigint(20) DEFAULT NULL COMMENT '至少提前取消时间',
  --`cancel_flag` tinyint(4) DEFAULT NULL COMMENT '是否允许取消: 1-是, 0-否',


  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;







CREATE TABLE `eh_rentalv2_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `order_no` varchar(20) NOT NULL COMMENT '订单编号',
  `rental_resource_id` bigint(20) NOT NULL COMMENT 'id',
  `rental_uid` bigint(20) DEFAULT NULL COMMENT 'rental user id',
  `rental_date` date DEFAULT NULL COMMENT '使用日期',
  `start_time` datetime DEFAULT NULL COMMENT '使用开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '使用结束时间',
  `rental_count` double DEFAULT NULL COMMENT '预约数',
  `pay_total_money` decimal(10,2) DEFAULT NULL COMMENT '总价',
  `resource_total_money` decimal(10,2) DEFAULT NULL,
  `reserve_money` decimal(10,2) DEFAULT NULL,
  `reserve_time` datetime DEFAULT NULL COMMENT 'reserve time',
  `pay_start_time` datetime DEFAULT NULL,
  `pay_end_time` datetime DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  `cancel_time` datetime DEFAULT NULL,
  `paid_money` decimal(10,2) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0:wait for reserve 1:paid reserve 2:paid all money reserve success 3:wait for final payment 4:unlock reserve fail',
  `visible_flag` tinyint(4) DEFAULT NULL COMMENT '0:visible 1:unvisible',
  `invoice_flag` tinyint(4) DEFAULT NULL COMMENT '0:want invocie 1 no need',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '预约人',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `resource_name` varchar(255) DEFAULT NULL COMMENT '名称',
  `use_detail` varchar(255) DEFAULT NULL COMMENT '使用时间',
  `vendor_type` varchar(255) DEFAULT NULL COMMENT '支付方式,10001-支付宝，10002-微信 --多次支付怎木办，估计产品都没想清楚',
  `resource_type_id` bigint(20) DEFAULT NULL COMMENT 'resource type id',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属公司的ID',
  `spec` varchar(255) DEFAULT NULL COMMENT '规格',
  `address` varchar(192) DEFAULT NULL COMMENT '地址',
  `longitude` double DEFAULT NULL COMMENT '地址经度',
  `latitude` double DEFAULT NULL COMMENT '地址纬度',
  `contact_phonenum` varchar(20) DEFAULT NULL COMMENT '咨询电话',
  `introduction` text COMMENT '详情',
  `notice` text,
  `community_id` bigint(20) DEFAULT NULL COMMENT '资源所属园区的ID',
  `namespace_id` int(11) DEFAULT NULL COMMENT '域空间',
  `refund_flag` tinyint(4) DEFAULT NULL COMMENT '是否支持退款 1是 0否',
  `refund_ratio` int(11) DEFAULT NULL COMMENT '退款比例',
  `cancel_flag` tinyint(4) DEFAULT NULL COMMENT '是否允许取消 1是 0否',
  `reminder_time` datetime DEFAULT NULL COMMENT '消息提醒时间',
  `reminder_end_time` datetime DEFAULT NULL,
  `auth_start_time` datetime DEFAULT NULL,
  `auth_end_time` datetime DEFAULT NULL,
  `door_auth_id` bigint(20) DEFAULT NULL,
  `package_name` varchar(45) DEFAULT NULL,
  `pay_mode` tinyint(4) DEFAULT '0' COMMENT 'pay mode :0-online pay 1-offline',
  `offline_cashier_address` varchar(200) DEFAULT NULL,
  `offline_payee_uid` bigint(20) DEFAULT NULL,
  `flow_case_id` bigint(20) DEFAULT NULL COMMENT 'id of the flow_case',
  `requestor_organization_id` bigint(20) DEFAULT NULL COMMENT 'id of the requestor organization',
  `paid_version` tinyint(4) DEFAULT NULL,
  `resource_type2` tinyint(4) DEFAULT NULL,
  `rental_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_rentalv2_resources` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
  `parent_id` bigint(20) DEFAULT NULL,
  `resource_name` varchar(127) DEFAULT NULL COMMENT '名称：',
  `resource_type2` tinyint(4) DEFAULT NULL,
  `building_name` varchar(128) DEFAULT NULL,
  `building_id` bigint(20) DEFAULT NULL,
  `address` varchar(192) DEFAULT NULL COMMENT '地址',
  `address_id` bigint(20) DEFAULT NULL,
  `spec` varchar(255) DEFAULT NULL COMMENT '规格',
  `own_company_name` varchar(60) DEFAULT NULL,
  `contact_name` varchar(40) DEFAULT NULL,
  `contact_phonenum` varchar(20) DEFAULT NULL COMMENT '咨询电话',
  `contact_phonenum2` varchar(20) DEFAULT NULL,
  `contact_phonenum3` varchar(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `introduction` text COMMENT '详情',
  `notice` text,
  `charge_uid` bigint(20) DEFAULT NULL COMMENT '负责人id',
  `cover_uri` varchar(1024) DEFAULT NULL COMMENT '封面图uri',
  `time_step` double DEFAULT NULL COMMENT '按小时预约：最小单元格是多少小时，浮点型',
  `exclusive_flag` tinyint(4) DEFAULT NULL COMMENT '是否为独占资源0否 1 是',
  `auto_assign` tinyint(4) DEFAULT NULL COMMENT '是否动态分配 1是 0否',
  `multi_unit` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个时段 1是 0否',
  `cancel_flag` tinyint(4) DEFAULT NULL COMMENT '是否允许取消 1是 0否',
  `need_pay` tinyint(4) DEFAULT NULL COMMENT '是否需要支付 1是 0否',
  `resource_type_id` bigint(20) DEFAULT NULL COMMENT 'resource type id',
  `cancel_time` bigint(20) DEFAULT NULL COMMENT '至少提前取消时间',
  `rental_start_time` bigint(20) DEFAULT NULL COMMENT '最多提前多少时间预定',
  `rental_end_time` bigint(20) DEFAULT NULL COMMENT '最少提前多少时间预定',
  `refund_flag` tinyint(4) DEFAULT NULL COMMENT '是否支持退款 1是 0否',
  `refund_ratio` int(11) DEFAULT NULL COMMENT '退款比例',
  `longitude` double DEFAULT NULL COMMENT '地址经度',
  `latitude` double DEFAULT NULL COMMENT '地址纬度',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属公司的ID',
  `day_begin_time` time DEFAULT NULL COMMENT '对于按小时预定的每天开始时间',
  `day_end_time` time DEFAULT NULL COMMENT '对于按小时预定的每天结束时间',
  `community_id` bigint(20) DEFAULT NULL COMMENT '所属的社区ID（和可见范围的不一样）',
  `resource_counts` double DEFAULT NULL COMMENT '可预约个数',
  `unit` double DEFAULT '1' COMMENT '1-整租, 0.5-可半个租',
  `begin_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `day_open_time` double DEFAULT NULL,
  `day_close_time` double DEFAULT NULL,
  `open_weekday` varchar(7) DEFAULT NULL COMMENT '7位二进制，0000000每一位表示星期7123456',
  `avg_price_str` varchar(1024) DEFAULT NULL COMMENT '平均价格计算好的字符串',
  `confirmation_prompt` varchar(200) DEFAULT NULL,
  `offline_cashier_address` varchar(200) DEFAULT NULL,
  `offline_payee_uid` bigint(20) DEFAULT NULL,
  `rental_start_time_flag` tinyint(4) DEFAULT '0' COMMENT '至少提前预约时间标志: 1-限制, 0-不限制',
  `rental_end_time_flag` tinyint(4) DEFAULT '0' COMMENT '最多提前预约时间标志: 1-限制, 0-不限制',
  `default_order` bigint(20) NOT NULL DEFAULT '0' COMMENT 'order',
  `aclink_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





























