-- AUTHOR: 严军
-- REMARK: 组件表增加标题栏信息  20181001
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_flag`  tinyint(4) NULL COMMENT '0-none,1-left,2-center，reference  TitleFlag.java';
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_uri`  varchar(1024) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_style`  int(11) NULL COMMENT 'title style, reference TitleStyle.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `sub_title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_size`  tinyint(4) NULL COMMENT '0-small, 1-medium, 2-large    TitleSize.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_more_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes. reference trueOrFalseFlag.java' ;

-- AUTHOR: 严军
-- REMARK: 公司头像字段太短 128 -> 512
ALTER TABLE `eh_organization_details` MODIFY COLUMN `avatar`  varchar(512)  DEFAULT NULL ;

-- AUTHOR: djm
-- REMARK: 合同添加押金状态字段
ALTER TABLE eh_contracts ADD COLUMN `deposit_status`  tinyint(4) NULL COMMENT '押金状态, 1-未缴, 2-已缴' AFTER deposit;

-- AUTHOR: djm
-- REMARK: 对接门禁
CREATE TABLE `eh_asset_dooraccess_params` (
  `id` bigint(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT NULL,
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'owner_id',  
  `owner_type` VARCHAR(64) NOT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0:无效状态，2：激活状态',
  `freeze_days` bigint(5) NOT NULL DEFAULT '0'  COMMENT '欠费多少天冻结',
  `unfreeze_days` bigint(5) NOT NULL DEFAULT '0'  COMMENT '缴费多少天解冻门禁',
  `category_id` BIGINT COMMENT 'asset category id',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费对接门禁表';

-- AUTHOR: djm
-- REMARK: 对接门禁
CREATE TABLE `eh_asset_dooraccess_logs` (
  `id` bigint(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT NULL,
  `project_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '项目id', 
  `project_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '普通公司,企业的id,用户id',  
  `owner_type` VARCHAR(64) NOT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0:无效状态，2：激活状态',
  `dooraccess_status` tinyint(4) DEFAULT NULL  COMMENT '该项目下门禁的状态 1:发起禁用门禁 ',
  `category_id` BIGINT COMMENT 'asset category id',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费对接门禁表门禁记录表';

-- AUTHOR: 荣楠
-- REMARK: 组织架构4.6 增加了唯一标识账号给通讯录表
ALTER TABLE `eh_organization_member_details` ADD COLUMN `account` VARCHAR(32) COMMENT 'the unique symbol of the member' AFTER `target_id`;
-- end