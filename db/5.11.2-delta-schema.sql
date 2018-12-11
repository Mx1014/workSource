-- AUTHOR: 吴寒
-- REMARK: 福利字段扩容
ALTER TABLE eh_welfare_coupons CHANGE `service_supply_name` `service_supply_name` VARCHAR(4096) COMMENT '适用地点';
ALTER TABLE eh_welfare_coupons CHANGE `service_range` `service_range` VARCHAR(4096) COMMENT '适用范围';
-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 到访类型修改
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `reason_type`  tinyint NULL DEFAULT null COMMENT '类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `visit_reason_code`  tinyint NULL DEFAULT null COMMENT '到访原因类型码';

-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 添加访客园区类型字段与园区Id
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `community_type`  tinyint NULL DEFAULT NULL COMMENT '园区类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `community_id`  bigint NULL DEFAULT NULL COMMENT '园区Id';


-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 第三方对接映射表
CREATE TABLE `eh_visitor_sys_third_mapping` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INT (11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
    `owner_type` VARCHAR (64) NOT NULL COMMENT 'community or organization',
    `owner_id` BIGINT (20) NOT NULL DEFAULT '0' COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
    `visitor_id` BIGINT NOT NULL COMMENT '主键',
    `third_type` VARCHAR (128) NULL COMMENT '关联类型',
    `third_value` VARCHAR (128) NULL COMMENT '关联值',
    `creator_uid` BIGINT (20) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `operator_uid` BIGINT (20) DEFAULT NULL,
    `operate_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '访客管理对接映射表';

-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 访客管理对接海康威视人员表
CREATE TABLE `eh_visitor_sys_hkws_user` (
`person_id` int NULL DEFAULT NULL COMMENT '人员ID',
`person_no` varchar(256) NULL DEFAULT NULL COMMENT '人员编号',
`person_name` varchar(256) NULL DEFAULT NULL COMMENT '姓名',
`gender` tinyint NULL DEFAULT NULL COMMENT '性别',
`certificate_type` int NULL DEFAULT NULL COMMENT '证件类型',
`certificate_no` varchar(256) NULL DEFAULT NULL COMMENT '证件号码',
`birthday` bigint NULL DEFAULT NULL COMMENT '出生日期',
`person_pinyin` varchar(256) NULL DEFAULT NULL COMMENT '姓名拼音',
`phone_no` varchar(256) NULL DEFAULT NULL COMMENT '联系电话',
`address` varchar(256) NULL DEFAULT NULL COMMENT '联系地址',
`photo` varchar(256) NULL DEFAULT NULL COMMENT '免冠照',
`english_name` varchar(256) NULL DEFAULT NULL COMMENT '英文名',
`email` varchar(256) NULL DEFAULT NULL COMMENT '邮箱',
`entry_date` bigint NULL DEFAULT NULL COMMENT '入职日期',
`leave_date` bigint NULL DEFAULT NULL COMMENT '离职日期',
`education` varchar(256) NULL DEFAULT NULL COMMENT '学历',
`nation` varchar(256) NULL DEFAULT NULL COMMENT '民族',
`dept_uuid` varchar(256) NULL DEFAULT NULL COMMENT '所属部门UUID',
`dept_no` varchar(256) NULL DEFAULT NULL COMMENT '所属部门编号',
`dept_name` varchar(256) NULL DEFAULT NULL COMMENT '所属部门名称',
`dept_path` varchar(256) NULL DEFAULT NULL COMMENT '所属部门路径',
`status` tinyint NULL DEFAULT NULL COMMENT '人员状态',
`identity_uuid` varchar(256) NULL DEFAULT NULL COMMENT '身份UUID',
`identity_name` varchar(256) NULL DEFAULT NULL COMMENT '身份名称',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
`remark` varchar(256) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理对接海康威视人员表';