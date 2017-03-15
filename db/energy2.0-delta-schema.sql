--
-- 梯度价格表
--
CREATE TABLE `eh_energy_meter_price_formulas` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name`         VARCHAR(255) COMMENT 'formula name',
  `explain`      VARCHAR(255) COMMENT 'explain formula',
  `expression`   VARCHAR(1024) COMMENT 'expression json',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
);

expression: {[range:"(0,100]", price:"1.00"],[range:"(100,150)", price:"1.50"],[range:"[150, )", price:"2.50"]}