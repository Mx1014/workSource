-- 打印设置表 , add by dengs, 20170615
-- DROP TABLE IF EXISTS `eh_siyin_print_settings`;
CREATE TABLE `eh_siyin_print_settings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `setting_type` TINYINT COMMENT '1(scan/print/copy),2(course/hotline)',
  `job_type` TINYINT COMMENT 'job type, PRINT(1),COPY(2),SCAN(3)',
  `paper_size` TINYINT COMMENT 'paper size, A3(3),A4(4),A5(5),A6(6)',
  `black_white_price` DECIMAL(10,2) COMMENT 'black white price',
  `color_price` DECIMAL(10,2) COMMENT 'color price',
  `hotline` VARCHAR(32) NULL COMMENT 'contact number',
  `print_course` TEXT COMMENT 'print course',
  `scan_copy_course` TEXT COMMENT 'scan or copy course',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 打印机表 , add by dengs, 20170615
-- DROP TABLE IF EXISTS `eh_siyin_print_printers`;
CREATE TABLE `eh_siyin_print_printers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `reader_name` VARCHAR(128) COMMENT 'printer reader name',
  `module_port` VARCHAR(16) COMMENT 'port of the mfpModuleManager interface return',
  `login_context` VARCHAR(128) COMMENT 'siyin login url location',
  `trademark` VARCHAR(128) COMMENT 'trade mark',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 打印订单表 , add by dengs, 20170615
-- DROP TABLE IF EXISTS `eh_siyin_print_orders`;
CREATE TABLE `eh_siyin_print_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `order_no` BIGINT COMMENT 'order number',
  `job_type` TINYINT COMMENT 'siyin returned,PRINT(1),COPY(2),SCAN(3)',
  `print_document_name` VARCHAR(256) COMMENT 'print document name',
  `detail` TEXT COMMENT 'print/copy/scan details',
  `email` VARCHAR(128) COMMENT '',
  `order_total_fee` DECIMAL(10,2) COMMENT 'order price',
  `order_type` VARCHAR(128) COMMENT 'order type: print(1)',
  `order_body` VARCHAR(128) COMMENT 'order body: print(1)',
  `order_subject` VARCHAR(256) COMMENT 'print order',
  `order_status` TINYINT COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `lock_flag` TINYINT COMMENT 'lock the order, and can not merge order 0(unlocked),1(locked)',
  `paid_type` VARCHAR(32) COMMENT '10001:alipay,10002:weixin',
  `paid_time` DATETIME,
  `nick_name` VARCHAR(128) COMMENT 'creator nick name',
  `creator_company` TEXT COMMENT 'creator companys',
  `creator_phone` VARCHAR(128) COMMENT 'create phone',
  `creator_uid` BIGINT COMMENT 'creator/initiator id',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 打印记录表 , add by dengs, 20170615
-- DROP TABLE IF EXISTS `eh_siyin_print_records`;
CREATE TABLE `eh_siyin_print_records` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) COMMENT 'community',
  `owner_id` BIGINT,
  `order_id` BIGINT COMMENT '',
  `job_id` VARCHAR(128) COMMENT 'siyin returned, uniqueness flag',
  `job_status` VARCHAR(128) COMMENT 'siyin returned, siyin job status',
  `group_name` VARCHAR(128) COMMENT 'siyin returned, user group name',
  `user_display_name` VARCHAR(128) COMMENT 'siyin returned, user display name',
  `client_ip` VARCHAR(128) COMMENT 'siyin returned, documents sended source commputer ip',
  `client_name` VARCHAR(128) COMMENT 'siyin returned, documents sended source commputer name',
  `client_mac` VARCHAR(128) COMMENT 'siyin returned, documents sended source commputer mac',
  `driver_name` VARCHAR(128) COMMENT 'siyin returned, driver name',
  `job_type` TINYINT COMMENT 'siyin returned,PRINT(1),COPY(2),SCAN(3)',
  `start_time` VARCHAR(128) COMMENT 'siyin returned, job start time',
  `end_time` VARCHAR(128) COMMENT 'siyin returned, job end time',
  `document_name` VARCHAR(512) COMMENT 'siyin returned, print document name',
  `printer_name` VARCHAR(256) COMMENT 'siyin returned, printer name',
  `paper_size` TINYINT COMMENT 'siyin returned, paper size, A3(3),A4(4),A5(5),A6(6)',
  `duplex` TINYINT COMMENT 'siyin returned, 1,one surface,2:double surface',
  `copy_count` INT COMMENT 'siyin returned, copy count',
  `surface_count` INT COMMENT 'siyin returned, surface count',
  `color_surface_count` INT COMMENT '',
  `mono_surface_count` INT COMMENT '',
  `page_count` INT COMMENT '',
  `color_page_count` INT COMMENT '',
  `mono_page_count` INT COMMENT '',
  `status` TINYINT COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT COMMENT 'creator/initiator id',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 打印用户邮箱表,可以考虑加到eh_users或者eh_user_identifiers表，但是数据太多，连表查询效率低 , add by dengs, 20170615
-- DROP TABLE IF EXISTS `eh_siyin_print_emails`;
CREATE TABLE `eh_siyin_print_emails` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `user_id` BIGINT,
  `email` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

