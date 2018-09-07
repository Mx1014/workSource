-- add by st.zheng 增加一卡通账号设置
ALTER TABLE `eh_payment_card_recharge_orders` ADD COLUMN `biz_order_no` VARCHAR(128) AFTER `order_no`;

CREATE TABLE `eh_payment_card_accounts` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NULL ,
`owner_type`  varchar(64) NULL ,
`owner_id`  bigint(20) NULL ,
`account_id`  bigint(20) NULL ,
`craete_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_payment_card_issuer_communities`
ADD COLUMN `hotline`  varchar(255) NULL AFTER `issuer_id`;

ALTER TABLE `eh_payment_cards`
ADD COLUMN `update_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `create_time`;

