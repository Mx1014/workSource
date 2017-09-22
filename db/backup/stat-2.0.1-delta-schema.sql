--
-- 运营统计新增场景和组件字段  add by xq.tian  2017/09/01
--
ALTER TABLE eh_stat_event_portal_statistics ADD COLUMN `scene_type` VARCHAR(64) COMMENT 'default, pm_admin, park_tourist...';
ALTER TABLE eh_stat_event_portal_statistics ADD COLUMN `widget` VARCHAR(64) COMMENT 'Navigator, Banner, OPPush...';