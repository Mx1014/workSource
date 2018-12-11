-- AUTHOR: djm 20181206
-- REMARK: 合同套打开关
ALTER TABLE eh_contract_categories ADD COLUMN print_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同套打(0 关闭，任何合同状态都能打印 1 打开 只有审批通过，正常合同、即将到期合同才能进行此操作)打开关';
ALTER TABLE eh_contract_categories ADD COLUMN editor_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同文档编辑开关';

-- AUTHOR: tangcen 2018年12月10日
-- REMARK: 合同文档表
CREATE TABLE `eh_contract_documents` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `community_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL COMMENT '合同多入口id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同文档关联的合同id',
  `contract_template_id` bigint(20) DEFAULT NULL COMMENT '关联的合同模板id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '上一版合同文档的id',
  `name` varchar(255) DEFAULT NULL COMMENT '合同文档名称',
  `content_type` varchar(32) DEFAULT NULL COMMENT '文本存储方式（gogs）',
  `content` varchar(128) DEFAULT NULL COMMENT '文本内容（在gogs存储时，存的是文本的commit id）',
  `version` int(11) DEFAULT '0' COMMENT '合同文档的版本号',
  `status` tinyint(4) DEFAULT NULL COMMENT '合同文档的状态-0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_uid` bigint(20) NOT NULL,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同文档表';

-- AUTHOR: tangcen 2018年12月10日
-- REMARK: 在合同表中添加用于记录合同文档的字段
ALTER TABLE `eh_contracts` ADD COLUMN `document_id` bigint NULL COMMENT '当前生效的合同文档id';

-- AUTHOR: tangcen 2018年12月11日
-- REMARK: 在合同模板表中添加用于记录合同模板初始化参数的字段
ALTER TABLE `eh_contract_templates` ADD COLUMN `init_params`  varchar(1024) NULL COMMENT '合同模板初始化参数（计价条款、关联资产等的数目），前端会解析这个json';


