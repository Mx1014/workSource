-- 仓库权限 by wentian
ALTER TABLE `eh_warehouse_materials` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouses` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_stocks` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_stock_logs` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_request_materials` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
ALTER TABLE `eh_warehouse_requests` ADD COLUMN `community_id` BIGINT DEFAULT 0 COMMENT '园区id';
-- endl