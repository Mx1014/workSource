-- 广告管理 v1.4 加字段    add by xq.tian  2018/03/07
ALTER TABLE eh_banners ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
ALTER TABLE eh_banners ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';

ALTER TABLE eh_banners MODIFY COLUMN scene_type VARCHAR(32) DEFAULT NULL;
ALTER TABLE eh_banners MODIFY COLUMN apply_policy TINYINT DEFAULT NULL;