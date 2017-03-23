
-- 增加全部页面item排序
ALTER TABLE eh_launch_pad_items ADD COLUMN  `more_order` INT NOT NULL DEFAULT 0;
-- 按场景分类
ALTER TABLE eh_item_service_categries ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';

