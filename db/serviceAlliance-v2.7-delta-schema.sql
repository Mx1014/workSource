-- 增加可见范围字段 by st.zheng
ALTER TABLE`eh_service_alliances`  ADD COLUMN `range` VARCHAR(512) NULL DEFAULT NULL AFTER `owner_id`;