-- AUTHOR: 杨崇鑫 20181114
-- REMARK: 物业缴费V7.5（中天-资管与财务EAS系统对接） ： 不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_paid` TINYINT COMMENT '不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付，0：没有任何支付，1：已在EAS支付';

-- AUTHOR: 黄鹏宇 2018-11-19
-- REMARK: 客户表单自定义
CREATE TABLE `eh_var_field_scope_filters` (
  `id` bigint NOT NULL,
  `namespace_id` int,
  `community_id` bigint COMMENT '被筛选的表单所在的园区id',
  `module_name` varchar(32) COMMENT '被筛选的表单的moduleName',
  `group_path` varchar(32) COMMENT '被筛选的表单的group',
  `field_id` bigint COMMENT '被筛选的表单id',
  `user_id` bigint COMMENT '被筛选的表单所属的用户id',
  `create_time` datetime,
  `create_uid` bigint,
  `status` tinyint,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '筛选显示的表单';

-- AUTHOR: chenhe 2018-11-20
-- REMARK: 圳智慧TICKET表
CREATE TABLE `eh_tickets` (
  `id` BIGINT NOT NULL,
  `user_id` BIGINT COMMENT 'token所属用户id',
  `ticket` VARCHAR(128) NOT NULL COMMENT 'token',
  `redirectCode` VARCHAR(16) COMMENT '指定跳转页面的代码',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '圳智慧TICKET表';