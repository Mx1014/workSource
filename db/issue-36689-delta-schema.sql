-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR:
-- REMARK:
ALTER TABLE `eh_news` ADD COLUMN `create_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0-后台创建 1-第三方调用接口' ;

-- AUTHOR: 杨崇鑫  20180811
-- REMARK: 物业缴费V6.7 统一订单
CREATE TABLE `eh_payment_bill_orders`(
  `id` BIGINT NOT NULL,
  `namespace_id` INT(10),
  `bill_id` VARCHAR(255),
  `order_number` varchar(255) COMMENT '业务订单编号，如：WUF00000000000004926',
  `payment_order_id` BIGINT COMMENT '支付系统订单ID',
  `general_order_id` BIGINT COMMENT '统一订单ID',
  `amount` DECIMAL(10,2),
  `payment_status` INT DEFAULT 0 COMMENT '支付状态，0-待支付、1-支付成功、2-支付中、5-支付失败',
  `payment_order_type` INTEGER NOT NULL DEFAULT 0 COMMENT '支付系统中的订单类型：1-RECHARGE(充值)、2-WITHDRAW(提现)、3-PURCHACE(支付), 4-REFUND(退款)',
  `payment_type` INT NOT NULL DEFAULT 0 COMMENT '支付类型，由支付系统定义（参考通联），如0-未选择支付方式、1-WECHAT_APPPAY(微信APP支付)、8-ALI_SCAN_PAY(阿里扫码支付)、9-WECHAT_JS_PAY(微信公众号支付)、21-WECHAT_JS_ORG_PAY(微信公众号集团支付)',
  `payment_time` DATETIME COMMENT '支付时间（缴费时间）',
  `payment_channel` INTEGER DEFAULT 0 COMMENT '支付渠道: 0-未知、1-微信、2-支付宝、3-现金',
  `uid` BIGINT COMMENT '缴费人',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- AUTHOR: 梁燕龙 20180815
-- REMARK: 活动报名 统一订单
ALTER TABLE `eh_activity_roster` MODIFY COLUMN `order_no` VARCHAR(64);
ALTER TABLE `eh_activity_roster` MODIFY COLUMN `refund_order_no` VARCHAR(64);

-- AUTHOR: 郑思挺 20180816
-- REMARK: 停车 保存biz_order_no
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `biz_order_no`  varchar(64) NULL AFTER `order_no`;

-- AUTHOR: 马世亨 20180817
-- REMARK: 物业报修 保存统一订单系统id
ALTER TABLE `eh_pm_task_orders` ADD COLUMN `general_order_id` varchar(64) NULL COMMENT '统一订单系统订单id';

-- AUTHOR: huangmingbo 0820
-- REMARK: 云打印 快递 保存统一订单系统id
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `general_order_id` VARCHAR(64) NULL COMMENT '统一订单系统订单编号' ;
ALTER TABLE `eh_express_orders` ADD COLUMN `general_order_id` VARCHAR(64) NULL COMMENT '统一订单系统订单编号' ;

-- AUTHOR:梁燕龙 20180904
-- REMARK:报名表增加表单ID字段
ALTER TABLE `eh_activity_roster` ADD COLUMN `form_id` BIGINT COMMENT '表单ID';
-- AUTHOR:梁燕龙 20180904
-- REMARK:表单与项目关联表
CREATE TABLE `eh_community_general_form`(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `community_id` BIGINT COMMENT '项目ID',
  `form_origin_id` BIGINT COMMENT '表单formOriginID',
  `type` VARCHAR(32) COMMENT '类型',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '表单与项目关系表';

-- AUTHOR: 梁燕龙 20180830
-- REMARK: 增加字段判断认证的来源
ALTER TABLE `eh_organization_members` ADD COLUMN `source_type` TINYINT COMMENT '认证来源';

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心v3.0
-- 用户表增加showCompanyFlag字段
ALTER TABLE `eh_users` ADD COLUMN `show_company_flag` TINYINT COMMENT '是否展示公司名称';

-- AUTHOR: 梁燕龙
-- REMARK: 个人中心v3.0
-- 个人中心配置表
CREATE TABLE `eh_personal_center_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `name` VARCHAR(32) COMMENT '展示名称',
  `function_name` VARCHAR(32) COMMENT '功能名称',
  `region` TINYINT NOT NULL DEFAULT 0 COMMENT '个人中心展示区域',
  `group_type` TINYINT COMMENT '展示区域分组',
  `sort_num` INTEGER NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `showable` TINYINT COMMENT '是否展示',
  `editable` TINYINT COMMENT '是否可编辑',
  `type` INTEGER NOT NULL COMMENT '功能所属类型',
  `icon_uri` VARCHAR(1024) COMMENT '图标URI',
  `version` INTEGER NOT NULL DEFAULT 0 COMMENT '版本号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态',
  `create_uid` BIGINT COMMENT '创建人ID',
  `create_time` DATETIME COMMENT '创建时间',
  `update_uid` BIGINT COMMENT '修改人ID',
  `update_time` DATETIME COMMENT '修改时间',
  `link_url` VARCHAR(1024) COMMENT '跳转链接',
  PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '个人中心配置表';
-- --------------------- SECTION END ---------------------------------------------------------
