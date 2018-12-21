
-- AUTHOR: 梁燕龙 20181219
-- REMARK: 动态主页签
ALTER TABLE eh_portal_navigation_bars MODIFY config_json VARCHAR(1024);
ALTER TABLE eh_portal_navigation_bars ADD COLUMN `top_bar_style` TINYINT COMMENT '主页签顶栏样式，1：透明渐变，2：不透明形变，3：不透明固定';
ALTER TABLE eh_launch_pad_indexs ADD COLUMN `preview_version_id` BIGINT COMMENT '预览版本号';