ALTER TABLE eh_sync_data_tasks ADD COLUMN `view_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否被查看';
ALTER TABLE eh_customer_talents ADD COLUMN `member_id` BIGINT NOT NULL DEFAULT 0 COMMENT '通讯录表中的id';
