--
-- 车辆放行申请人员与处理人员
--
-- DROP TABLE IF EXISTS `eh_parking_clearance_operators`;
CREATE TABLE `eh_parking_clearance_operators` (
  `id`              BIGINT  NOT NULL,
  `namespace_id`    INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `community_id`    BIGINT,
  `parking_lot_id`  BIGINT COMMENT 'eh_parking_lots id',
  `operator_type`   VARCHAR(32) COMMENT 'applicant, handler',
  `operator_id`     BIGINT,
  `status`          TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`     BIGINT,
  `create_time`     DATETIME,
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4;

--
-- 车辆放行记录
--
-- DROP TABLE IF EXISTS `eh_parking_clearance_logs`;
CREATE TABLE `eh_parking_clearance_logs` (
  `id`             BIGINT  NOT NULL,
  `namespace_id`   INTEGER NOT NULL DEFAULT 0,
  `community_id`   BIGINT,
  `parking_lot_id` BIGINT COMMENT 'eh_parking_lots id',
  `applicant_id`   BIGINT COMMENT 'applicant id',
  `operator_id`    BIGINT COMMENT 'operator id',
  `plate_number`   VARCHAR(32) COMMENT 'plate number',
  `apply_time`     DATETIME COMMENT 'apply time',
  `clearance_time` DATETIME COMMENT 'The time the vehicle passed',
  `remarks`        VARCHAR(1024) COMMENT 'remarks',
  `status`         TINYINT COMMENT '0: inactive, 1: processing, 2: completed, 3: cancelled, 4: pending',
  `creator_uid`    BIGINT,
  `create_time`    DATETIME,
  `update_uid`     BIGINT,
  `update_time`    DATETIME,
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4;