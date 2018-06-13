-- Designer: wuhan
-- Description: ISSUE#25515: 薪酬V2.2（工资条发放管理；app支持工资条查看/确认）

CREATE TABLE `eh_salary_payslips` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INT ,
  `owner_type` VARCHAR(32)  COMMENT 'organization',
  `owner_id` BIGINT  COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT  COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `name` VARCHAR(1024) NOT NULL COMMENT '工资表名称',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_owner_period` (`owner_id`,`salary_period`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '工资条表';


CREATE TABLE `eh_salary_payslip_details` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `payslip_id` BIGINT NOT NULL COMMENT '父键;工资条id',
  `namespace_id` INT,
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `owner_type` VARCHAR(32)  COMMENT 'organization',
  `owner_id` BIGINT  COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT  COMMENT '属于哪一个总公司的',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `name` VARCHAR(512) NOT NULL COMMENT '姓名',
  `user_contact` VARCHAR(20) NOT NULL COMMENT '手机号',
  `payslip_content` TEXT  COMMENT '导入的工资条数据(key-value对的json字符串)',
  `viewed_flag` TINYINT COMMENT '已查看0-否 1-是',
  `status` TINYINT COMMENT '状态0-已发送 1-已撤回  2-已确认',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_payslip_id` (`payslip_id`),
  KEY `i_eh_organization_user` (`user_id`,`organization_id`),
  KEY `i_eh_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '工资条详情表';
-- 薪酬2.2 end

-- app配置 1.2.1 start  add by yanjun 201805241019
ALTER TABLE `eh_portal_versions` ADD COLUMN `preview_count`  int(11) NULL DEFAULT 0 COMMENT '预览版本发布次数';

-- app配置 1.2.1 end  add by yanjun


-- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;



-- 资源预约 订单资源表增加字段
ALTER TABLE `eh_rentalv2_resource_orders` ADD COLUMN `resource_number`  VARCHAR(64) NULL;

-- ISSUE#26184 门禁人脸识别 by liuyilin 201180524
-- 内网服务器表创建
CREATE TABLE `eh_aclink_servers` (
    `id` BIGINT(20) NOT NULL,
    `name` VARCHAR(32) DEFAULT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
    `ip_address` VARCHAR(128) DEFAULT NULL COMMENT 'IP地址',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`active_time` DATETIME DEFAULT NULL COMMENT '激活时间',
	`sync_time`  DATETIME DEFAULT NULL COMMENT '上次同步时间',
	`version` VARCHAR(8) DEFAULT NULL COMMENT '版本号',
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '组织id',
	`owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '组织类型',
	`aes_server_key` VARCHAR(64) COMMENT 'AES公钥',
	`status` TINYINT NOT NULL DEFAULT '0' COMMENT '激活状态0未激活1已激活2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`),
	UNIQUE `u_eh_aclink_servers_uuid`(`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '内网服务器表';

-- 内网ipad表创建
CREATE TABLE `eh_aclink_ipads` (
	`id` BIGINT(20) NOT NULL,
	`name` VARCHAR(32) DEFAULT NULL,
	`door_access_id` BIGINT(20) NOT NULL COMMENT '关联门禁id',
	`enter_status` TINYINT DEFAULT '0' COMMENT '进出标识 1进0出',
	`uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`server_id` BIGINT(20) COMMENT '服务器id',
	`active_time` DATETIME DEFAULT NULL COMMENT '激活时间',
	`status` TINYINT NOT NULL DEFAULT '0' COMMENT '激活状态0未激活1已激活2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`),
	UNIQUE `u_eh_aclink_ipads_uuid`(`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '内网ipad表';

-- 内网摄像头表创建
CREATE TABLE `eh_aclink_cameras` (
	`id` BIGINT(20) NOT NULL,
	`name` VARCHAR(32) DEFAULT NULL,
	`door_access_id` BIGINT NOT NULL COMMENT '关联门禁id',
	`enter_status` TINYINT DEFAULT '0' COMMENT '进出标识 1进0出',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`ip_address` VARCHAR(128) DEFAULT NULL COMMENT 'IP地址',
	`server_id` BIGINT(20) COMMENT '服务器id',
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态1正常2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	`key_code` VARCHAR(128) NOT NULL COMMENT '摄像头密钥' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '内网摄像头表';

-- 门禁与服务器多对一关联
ALTER TABLE `eh_door_access`
ADD `local_server_id` BIGINT(20) COMMENT '服务器id';

-- 人脸识别照片表创建
DROP TABLE IF EXISTS `eh_face_recognition_photos`;

CREATE TABLE `eh_face_recognition_photos` (
	`id` BIGINT(20) NOT NULL,
    `user_id` BIGINT(20) COMMENT '用户id(正式用户)',
	`auth_id` BIGINT(20) COMMENT '授权id(访客)',
	`user_type` TINYINT NOT NULL DEFAULT '0' COMMENT '照片关联的用户类型,0正式用户,1访客',
	`img_uri` VARCHAR(2048) NOT NULL COMMENT '照片uri',
	`img_url` VARCHAR(2048) NOT NULL COMMENT '照片url',
	`sync_time` DATETIME COMMENT '上次同步时间时间' ,
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态1正常2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '人脸识别照片表';

-- 门禁版本包url长度限制修改
ALTER TABLE eh_aclink_firmware MODIFY COLUMN `download_url` VARCHAR(1024);
ALTER TABLE eh_aclink_firmware MODIFY COLUMN `info_url` VARCHAR(1024);

-- issue28839允许修改门禁开门方式,增加字段表示是否支持二维码
ALTER TABLE `eh_door_access` ADD COLUMN `has_qr` TINYINT NOT NULL DEFAULT '1' COMMENT '门禁二维码能力0无1有';

-- End by: yilin Liu