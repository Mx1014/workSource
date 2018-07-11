-- Designer: zhiwei zhang
-- Description: ISSUE#28363 会议管理V1.0

CREATE TABLE `eh_meeting_rooms` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
    `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations，表示会议室归属的企业',
    `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID  会议室归属的分公司',
    `name` VARCHAR(128) NOT NULL COMMENT '会议室名称',
    `seat_count` INTEGER NOT NULL COMMENT '可容纳座位数',
    `description` VARCHAR(512) COMMENT '描述',
    `open_begin_time` TIME NOT NULL COMMENT '会议室可预订的起始时间',
    `open_end_time` TIME NOT NULL COMMENT '会议室可预订的结束时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:  0: DELETED 删除  1:CLOSED 不可用  2 : OPENING 可用',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    `operator_name` VARCHAR(64) NULL COMMENT '操作人姓名',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `u_eh_namespace_owner_name` (`namespace_id` , `organization_id`, `owner_type` , `owner_id` , `name`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议室资源管理表';


CREATE TABLE `eh_meeting_reservations` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
    `subject` VARCHAR(256)  COMMENT '会议主题',
    `content` VARCHAR(1024) COMMENT '会议详细内容',
    `meeting_room_name` VARCHAR(128) NULL COMMENT '会议室名称，预约会议室后保存会议室名称，后期该值不随着会议室编辑而改变',
    `meeting_room_seat_count` INTEGER NULL COMMENT '可容纳座位数，预约会议室后保存会议室名称，后期该值不随着会议室编辑而改变',
    `meeting_room_id` BIGINT NULL COMMENT '会议室id,id of eh_meeting_rooms',
    `meeting_sponsor_user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '会议发起人的user_id',
    `meeting_sponsor_detail_id` BIGINT NOT NULL COMMENT '会议发起人的detail_id',
    `meeting_sponsor_name` VARCHAR(64) NOT NULL COMMENT '会议发起人的姓名',
    `meeting_recorder_user_id` BIGINT NULL COMMENT '会议纪要人user_id',
    `meeting_recorder_detail_id` BIGINT NULL COMMENT '会议纪要人detail_id',
    `meeting_recorder_name` VARCHAR(64) NULL COMMENT '会议纪要人的姓名',
    `invitation_user_count` INT COMMENT '会议受邀人数',
    `meeting_date` DATE NOT NULL COMMENT '会议预定日期',
    `expect_begin_time` DATETIME NOT NULL COMMENT '预计开始时间（预订会议室的时间）',
    `expect_end_time` DATETIME NOT NULL COMMENT '预计结束时间（预订会议室的时间）',
    `lock_begin_time` DATETIME NOT NULL COMMENT '实际锁定开始时间',
    `lock_end_time` DATETIME NOT NULL COMMENT '实际锁定结束时间',
    `act_begin_time` DATETIME NULL COMMENT '实际开始时间，只有用户操作了开始会议才有值',
    `act_end_time` DATETIME NULL COMMENT '实际结束时间，只有用户操作了结束会议才有值',
    `system_message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启系统消息通知：0-关闭消息通知 1-开启消息通知',
    `email_message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知：0-关闭邮件通知 1-开启邮件通知',
    `act_remind_time` DATETIME NULL COMMENT '实际发出提醒的时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0:DELETED 删除，1:时间锁定， 2:CANCELED 取消,3:NORMAL 正常状态',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    PRIMARY KEY (`id`),
    INDEX `i_eh_namespace_organization_id` (`namespace_id`,`organization_id`),
    INDEX `i_eh_meeting_date`(`meeting_date`),
    INDEX `i_eh_meeting_room_id` (`meeting_room_id`),
    INDEX `i_eh_meeting_sponsor_detail_id` (`meeting_sponsor_detail_id`),
    INDEX `i_eh_meeting_recorder_detail_id` (`meeting_recorder_detail_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议室预约表';


CREATE TABLE `eh_meeting_invitations` (
    `id` BIGINT NOT NULL,
    `meeting_reservation_id` BIGINT NOT NULL COMMENT '会议预约eh_meeting_reservations的id',
    `source_type` VARCHAR(45) NOT NULL COMMENT '机构或者个人：ORGANIZATION OR MEMBER_DETAIL',
    `source_id` BIGINT NOT NULL COMMENT '机构id或员工detail_id',
    `source_name` VARCHAR(64) NOT NULL COMMENT '机构名称或者员工的姓名',
    `role_type` VARCHAR(16) NOT NULL COMMENT '参会人或抄送人',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    PRIMARY KEY (`id`),
    INDEX `i_eh_meeting_reservation_id` (`meeting_reservation_id`),
    INDEX `i_eh_source_id`(`source_type`,`source_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议邀请清单，即参会人和抄送人清单';


CREATE TABLE `eh_meeting_records` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `meeting_reservation_id` BIGINT NOT NULL COMMENT '会议预约ID，id of eh_meeting_reservations',
    `meeting_subject` VARCHAR(256)  COMMENT '会议主题，冗余字段，用于纪要列表展示主题名称',
    `content` TEXT COMMENT '会议纪要详细内容',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    `operator_name` VARCHAR(64) NULL COMMENT '操作人姓名',
    PRIMARY KEY (`id`),
    INDEX `i_eh_meeting_reservation_id` (`meeting_reservation_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议纪要表';


-- End by: zhiwei zhang

-- Designer: wuhan
-- Description:  考勤4.2


-- 考勤月报表
CREATE TABLE `eh_punch_month_reports` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `owner_type` VARCHAR (128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `process` INT COMMENT '进度百分比',
  `error_info` VARCHAR (200) COMMENT '错误信息(如果归档错误)',
  `status` TINYINT COMMENT '状态:0-创建更新中 1-创建完成 2-已归档',
  `creator_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL,
  `punch_member_number` INT COMMENT '考勤人数',
  `filer_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `file_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `creator_Name` VARCHAR (128) DEFAULT NULL COMMENT '创建者姓名',
  `filer_Name` VARCHAR (128) DEFAULT NULL COMMENT '归档者姓名',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '考勤月报表' ;

ALTER TABLE `eh_punch_statistics` CHANGE status_list status_list TEXT COMMENT '校正后状态列表(月初到月末)';
-- ALTER TABLE `eh_punch_statistics` ADD COLUMN month_report_id BIGINT COMMENT 'eh_punch_month_reports id';
-- ALTER TABLE `eh_punch_statistics` ADD INDEX i_eh_report_id(`month_report_id`)  ;
-- end by wuhan

-- Designer: liuyilin
-- Description:   issue-31813门禁2.9.7客户端支持自定义门禁授权方式 20180615
ALTER TABLE `eh_door_access` ADD `max_duration` INTEGER COMMENT '有效时间最大值(天)';
ALTER TABLE `eh_door_access` ADD `max_count` INTEGER COMMENT '按次授权最大次数';
ALTER TABLE `eh_door_access` ADD `defualt_invalid_duration` INTEGER COMMENT '按次授权默认有效期(天)';
ALTER TABLE `eh_door_access` ADD `enable_duration` TINYINT DEFAULT '1' COMMENT '是否支持按有效期开门';
-- issue-31813 END