
-- 增加联系人职位 add by xiongying 20170914
ALTER TABLE eh_enterprise_customers ADD COLUMN contact_position VARCHAR(64);


 
 -- 打卡3.0

ALTER TABLE `eh_punch_rules` ADD COLUMN `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次';
ALTER TABLE `eh_punch_rules` ADD COLUMN `punch_organization_id` BIGINT;
ALTER TABLE `eh_punch_rules` ADD COLUMN `china_holiday_flag` TINYINT COMMENT '同步法定节假日0- no  ; 1- yes ';
ALTER TABLE `eh_punch_rules` ADD COLUMN `status` TINYINT DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效';  

ALTER TABLE `eh_punch_holidays` ADD COLUMN `exchange_from_date` DATE DEFAULT NULL COMMENT '特殊上班日:上原本哪天的班次';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN status_list VARCHAR(120) COMMENT '多次打卡的状态用/分隔 example: 1 ; 1/13 ; 13/3/4 ';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN punch_count INT COMMENT '打卡次数';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `punch_organization_id` BIGINT;
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `time_rule_name` VARCHAR(64) COMMENT '排班规则名称';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `time_rule_id` BIGINT COMMENT '排班规则id';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `approval_status_list` VARCHAR(120) COMMENT '1-未审批 0-审批正常 例如:0/1;1/1/0/1';

ALTER TABLE `eh_punch_logs` ADD COLUMN `punch_type` TINYINT DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡'; 
ALTER TABLE `eh_punch_logs` ADD COLUMN `punch_interval_no` INT DEFAULT '1' COMMENT '第几次排班的打卡'; 
ALTER TABLE `eh_punch_logs` ADD COLUMN `rule_time` BIGINT COMMENT '规则设置的该次打卡时间';  
ALTER TABLE `eh_punch_logs` ADD COLUMN `status` TINYINT COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡';  

ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `punch_interval_no` INT DEFAULT '1' COMMENT '第几次排班的打卡'; 

ALTER TABLE `eh_punch_exception_approvals` ADD COLUMN `approval_status_list` VARCHAR(120) COMMENT '1-未审批 0-审批正常 例如:0/1;1/1/0/1';

ALTER TABLE `eh_punch_statistics` ADD COLUMN `punch_org_name` VARCHAR(64) COMMENT '所属规则-考勤组';
ALTER TABLE `eh_punch_statistics` ADD COLUMN `detail_id` BIGINT COMMENT '用户detailId';

ALTER TABLE `eh_punch_time_rules` ADD COLUMN `rule_type` TINYINT DEFAULT '1' COMMENT '0- 排班制 ; 1- 固定班次'; 
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `hommization_type` TINYINT DEFAULT '0' COMMENT '人性化设置:0-无 1-弹性 2晚到晚走'; 
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `flex_time_long` BIGINT COMMENT '弹性时间 ';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `begin_punch_time` BIGINT COMMENT '上班多久之前可以打卡';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `end_punch_time` BIGINT COMMENT '下班多久之后可以打卡';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `punch_organization_id` BIGINT  COMMENT 'fk:eh_punch_workday_rules id';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `status` TINYINT  DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效';  
  

-- 固定时间制:特殊日期
CREATE TABLE `eh_punch_special_days` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128)  COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT  COMMENT 'owner resource(user/organization) id',
  `punch_organization_id` BIGINT  COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_time_rules id ', 
  `status` TINYINT  COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `rule_date` DATE  COMMENT 'date',
  `description` TEXT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 多时间段的打卡时段表
CREATE TABLE `eh_punch_time_intervals` (
  `id` BIGINT NOT NULL DEFAULT '0' COMMENT 'id',
  `owner_type` VARCHAR(128)  COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT  COMMENT 'owner resource(user/organization) id',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT '2' COMMENT 'how many times should punch everyday :2/4/6', 
  `punch_organization_id` BIGINT  COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_time_rules id  ', 
  `arrive_time_long` BIGINT  COMMENT ' arrive',
  `leave_time_long` BIGINT  COMMENT 'leave',
  `description` TEXT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;




-- 薪酬批次可用的选项 基础数据
CREATE TABLE `eh_salary_default_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用',
  `default_flag` TINYINT COMMENT '是否是缺省参数:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0文本类;1数值类',
  `category_id` BIGINT COMMENT '标签(统计分类) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:基础,应发,应收,合计',
  `name` VARCHAR(32),  
  `editable_flag` TINYINT COMMENT '是否可编辑(对文本类):0-否   1-是',
  `template_name` VARCHAR(32) COMMENT '',
  `default_order` INT,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;



-- 薪酬批次可用的选项的标签类型 基础数据
CREATE TABLE `eh_salary_entity_categories` (
  `id` BIGINT,
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用', 
  `category_name` VARCHAR(64)  COMMENT 'name of category',
  `status` TINYINT ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次包含的选项
CREATE TABLE `eh_salary_group_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `group_id` BIGINT COMMENT '标签(统计分类) 薪酬组表pk',
  `origin_entity_id` BIGINT,
  `type` TINYINT COMMENT '字段类型:0文本类;1数值类',
  `category_id` BIGINT COMMENT '标签(统计分类) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:基础,应发,应收,合计',
  `name` VARCHAR(32),  
  `editable_flag` TINYINT COMMENT '是否可编辑(对文本类):0-否   1-是',
  `template_name` VARCHAR(32) COMMENT '模板名称',
  `number_type` TINYINT COMMENT '数值类型:0-普通数值 1-计算公式',
  `default_value` TEXT COMMENT'默认值/默认数值/计算公式',
  `need_check` TINYINT COMMENT'是否需要核算:0-否 1-是',
  `default_order` INT,
  `visible_flag` TINYINT DEFAULT 1 COMMENT'是否展示到工资条:0-否 1-是',
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 某个人的薪酬设定
CREATE TABLE `eh_salary_employee_origin_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `group_id` BIGINT COMMENT '标签(统计分类) organization group表pk', 
  `user_id` BIGINT ,
  `user_detail_id` BIGINT ,
  `member_id` BIGINT ,
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(32),  
  `origin_entity_id` BIGINT,
  `salary_value` TEXT , 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次每期的数据
CREATE TABLE `eh_salary_groups` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_period` VARCHAR(32) COMMENT 'example:201705',
  `organization_group_id` BIGINT COMMENT '标签(统计分类) organization group表pk',  
  `group_name` VARCHAR(64) COMMENT '批次名',
  `send_time` DATETIME COMMENT '预发送时间:不预发送则为null',  
  `email_content` TEXT COMMENT '工资条发送邮件内容',
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-未核算 1-已核算  3-定时发放等待中 4-已发放',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 薪酬批次每个人的每期数据
CREATE TABLE `eh_salary_employees` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_period` VARCHAR(32) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `user_detail_id` BIGINT ,
  `member_id` BIGINT ,
  `creator_uid` BIGINT COMMENT'人员id',
  `organization_group_id` BIGINT COMMENT '标签(统计分类) organization group表pk',  
  `salary_group_id` BIGINT COMMENT '标签(统计分类) salary group表pk',   
  `email_content` TEXT COMMENT '工资条发送邮件内容',
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-未核算 1-已核算  3-定时发放等待中 4-已发放',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬薪酬人员每期的实际值
CREATE TABLE `eh_salary_employee_period_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_employee_id` BIGINT COMMENT '标签(统计分类) salary_employee表pk', 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(32),  
  `origin_entity_id` BIGINT,
  `salary_value` TEXT , 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- DROP TABLE IF EXISTS `eh_uniongroup_configures`;
CREATE TABLE `eh_uniongroup_configures` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT(20) DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT(20) NOT NULL COMMENT 'id of group',
  `current_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, organization or memberDetail',
  `current_type` VARCHAR(32) COMMENT 'organziation,memberDetail',
  `current_name` VARCHAR(32) COMMENT 'name',
  `operator_uid` BIGINT(20),
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--  DROP TABLE IF EXISTS `eh_uniongroup_member_details`;
CREATE TABLE `eh_uniongroup_member_details` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT(20) NOT NULL COMMENT 'id of group',
  `detail_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, only memberDetail',
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id' ,
  `contact_name` VARCHAR(64) COMMENT 'the name of the member',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `update_time` DATETIME,
  `operator_uid` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_organizations ADD COLUMN `email_content` TEXT COMMENT '工资条发送邮件内容';

-- DROP TABLE IF EXISTS `eh_organization_members_test`;
CREATE TABLE `eh_organization_members_test` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL,
  `target_type` VARCHAR(32) COMMENT 'untrack, user',
  `target_id` BIGINT NOT NULL COMMENT 'target user id if target_type is a user',
  `member_group` VARCHAR(32) COMMENT 'pm group the member belongs to',
  `contact_name` VARCHAR(64),
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `contact_description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: confirming, 2: active',
  `group_id` BIGINT DEFAULT 0 COMMENT 'refer to the organization id',
  `employee_no` VARCHAR(128),
  `avatar` VARCHAR(128),
  `group_path` VARCHAR(128) COMMENT 'refer to the organization path',
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `update_time` DATETIME,
  `create_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `namespace_id` INTEGER DEFAULT 0,
  `visible_flag` TINYINT DEFAULT 0 COMMENT '0 show 1 hide',
  `group_type` VARCHAR(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `creator_uid` BIGINT,
  `operator_uid` BIGINT,
  `detail_id` BIGINT COMMENT 'id for detail records',
  PRIMARY KEY (`id`),
  KEY `fk_eh_orgm_owner` (`organization_id`),
  KEY `i_eh_corg_group` (`member_group`),
  KEY `i_target_id` (`target_id`),
  KEY `i_contact_token` (`contact_token`)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4;







-- 增加可见范围字段 by st.zheng
ALTER TABLE`eh_service_alliances`  ADD COLUMN `range` VARCHAR(512) NULL DEFAULT NULL AFTER `owner_id`;
-- 增加标志位 by st.zheng
ALTER TABLE `eh_service_alliance_jump_module`
ADD COLUMN `signal` TINYINT(4) NULL DEFAULT '1' COMMENT '标志 0:删除 1:普通 2:审批' AFTER `parent_id`;

-- merge from msg-2.1
-- 更改群聊名称可为空  edit by yanjun 20170724
ALTER TABLE eh_groups MODIFY `name` VARCHAR(128) DEFAULT NULL;

