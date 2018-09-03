-- bu zheng 装修办理1.0
CREATE TABLE `eh_decoration_requests` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`community_id`  bigint(20) NOT NULL ,
`create_time`  datetime NULL  ,
`start_time`  datetime NULL  ,
`end_time`  datetime NULL  ,
`apply_uid`  bigint(20) NULL ,
`apply_name`  varchar(64) NULL ,
`apply_phone`  varchar(64) NULL ,
`apply_company`  varchar(255) NULL ,
`address`  varchar(255) NULL ,
`decorator_uid`  bigint(20) NULL ,
`decorator_name`  varchar(64) NULL ,
`decorator_phone`  varchar(64) NULL ,
`decorator_company_id`  bigint(20) NULL ,
`decorator_company`  varchar(255) NULL ,
`decorator_qrid`  varchar(255) NULL COMMENT '二维码id' ,
`status`  tinyint NULL ,
`cancel_flag`  tinyint NULL COMMENT '0未取消 1工作流取消 2后台取消' ,
`cancel_reason`  varchar(1024) NULL ,
`refound_amount`  DECIMAL(18,2) NULL COMMENT '退款金额' ,
`refound_comment`  varchar(1024) NULL COMMENT '退款备注',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_workers` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`request_id`  bigint(20) NOT NULL ,
`worker_type`  varchar(64) NULL ,
`uid`  bigint(20) NULL ,
`name`  varchar(64) NULL ,
`phone`  varchar(64) NULL ,
`image`  varchar(255) NULL ,
`qrid`  varchar(255) NULL COMMENT '二维码id' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_setting` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`community_id`  bigint(20) NOT NULL ,
`owner_type`  varchar(64) NOT NULL COMMENT '\'basic\' 基础设置 \'file\'装修资料 \'fee\'缴费 \'apply\'施工申请 \'complete\'竣工验收 \'refound\'押金退回' ,
`owner_id`  bigint(20) NULL COMMENT '当owner_type为apply 时 表示审批id' ,
`content`  text NULL ,
`address`  varchar(255) NULL COMMENT '收款地址或资料提交地址' ,
`longitude` DOUBLE,
`latitude` DOUBLE,
`phone`  varchar(64) NULL COMMENT '咨询电话' ,
`create_time`  datetime NULL  ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_atttachment` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`setting_id`  bigint(20) NOT NULL ,
`name`  varchar(64) NULL ,
`attachment_type`  varchar(64) NULL COMMENT '\'file\'文件 \'fee\'费用' ,
`size`  varchar(32) NULL ,
`file_uri`  varchar(255) NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_fee` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`request_id`  bigint(20) NOT NULL ,
`fee_name`  varchar(64) NULL ,
`fee_price`  varchar(64) NULL ,
`amount`  varchar(64) NULL ,
`total_price`  decimal(20,2) NULL ,
`create_time`  datetime NULL  ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_companies` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`organization_id`  bigint(20) NULL ,
`name`  varchar(64) NULL ,
`create_time`  datetime NULL  ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_company_chiefs` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`company_id`  bigint(20) NOT NULL COMMENT '装修公司的id',
`name`  varchar(64) NULL ,
`phone`  varchar(64) NULL ,
`uid`   bigint(20) NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_approval_vals` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`request_id`  bigint(20) NULL ,
`approval_id`  bigint(20) NULL ,
`approval_name`  varchar(64) NULL ,
`flow_case_id`  bigint(20) NULL ,
`form_origin_id`  bigint(20) NULL ,
`form_version`  bigint(20) NULL ,
`delete_flag`  tinyint NULL COMMENT '0未取消 1取消' ,
`create_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

