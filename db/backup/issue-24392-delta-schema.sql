-- Designer: zhiwei zhang
-- Description: ISSUE#24392 固定资产管理V1.0（支持对内部各类固定资产进行日常维护）

CREATE TABLE `eh_fixed_asset_default_categories` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id` INTEGER NOT NULL DEFAULT 0 COMMENT '父级分类id',
  `path` VARCHAR(128) NOT NULL COMMENT '分类层级路径，如 /123/1234',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产默认分类表';


CREATE TABLE `eh_fixed_asset_categories` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations ',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id` INTEGER NOT NULL DEFAULT 0 COMMENT '父级分类id',
  `path` VARCHAR(128) NOT NULL COMMENT '分类层级路径，如 /123/1234',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 : IN_ACTIVE 1: ACTIVE',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_namespace_owner_id`(`namespace_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产分类表';


CREATE TABLE `eh_fixed_assets` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `item_no` VARCHAR(20) NOT NULL COMMENT '资产编号',
  `name` VARCHAR(64) NOT NULL COMMENT '资产名称',
  `fixed_asset_category_id` INTEGER NOT NULL COMMENT '资产分类  id of the table eh_fixed_asset_categories',
  `specification` VARCHAR(128) COMMENT '规格',
  `price` DECIMAL(14,2) COMMENT '单价',
  `buy_date` DATE COMMENT '购买日期 格式:yyyy-MM-dd',
  `vendor` VARCHAR(128) COMMENT '所属供应商',
  `add_from` TINYINT NOT NULL DEFAULT 0 COMMENT '来源 :0-其它,1-购入,2-自建,3-租赁,4-捐赠',
  `image_uri` VARCHAR(1024) COMMENT '图片uri',
  `barcode_uri` VARCHAR(1024) COMMENT '条形码uri',
  `other_info` VARCHAR(512) COMMENT '其它',
  `remark` VARCHAR(512) COMMENT '备注',
  `status` TINYINT NOT NULL COMMENT '状态:1-闲置,2-使用中,3-维修中,4-已出售,5-已报废,6-遗失',
  `location` VARCHAR(256) COMMENT '存放地点',
  `occupied_date` DATE COMMENT '领用时间',
  `occupied_department_id` BIGINT COMMENT '领用部门ID',
  `occupied_member_detail_id` BIGINT COMMENT '领用人 id of the table eh_organization_member_details',
  `occupied_member_name` VARCHAR(64) COMMENT '领用人姓名',
  `operator_name` VARCHAR(64) NOT NULL COMMENT '操作人姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT '删除操作人userId',
  `delete_time` DATETIME COMMENT '记录删除时间',
  PRIMARY KEY(`id`),
  KEY `i_eh_namespace_owner_id`(`namespace_id`,`owner_type`,`owner_id`),
  KEY `i_eh_fixed_asset_category_id`(`fixed_asset_category_id`),
  KEY `i_eh_create_time`(`create_time` DESC)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产表';


CREATE TABLE `eh_fixed_asset_operation_logs` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `fixed_asset_id` BIGINT NOT NULL COMMENT '资产ID',
  `operation_info` TEXT NOT NULL COMMENT '变更记录JSON格式记录',
  `operation_type` VARCHAR(16) NOT NULL COMMENT '新增、编辑或者删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_name` VARCHAR(45) NOT NULL COMMENT '操作人姓名 ',
  PRIMARY KEY(`id`),
  KEY `i_eh_namespace_asset_id`(`namespace_id`,`fixed_asset_id`),
  KEY `i_eh_create_time`(`create_time` DESC)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '固定资产信息变更记录表';


-- End by: zhiwei zhang
