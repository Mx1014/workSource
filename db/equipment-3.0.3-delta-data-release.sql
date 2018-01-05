--  标准数据增加周期类型及关系表状态start by jiarui 20180105
UPDATE eh_equipment_inspection_standards
SET repeat_type = (SELECT repeat_type FROM eh_repeat_settings WHERE id = eh_equipment_inspection_standards.repeat_setting_id)
WHERE STATUS =2  AND repeat_type IS NULL;

-- 上版未置状态数据修改
UPDATE eh_equipment_inspection_equipment_standard_map
SET `status` = 0
WHERE review_status IN (0, 3) OR review_result = 2;
-- 上版bug数据修改

--  标准数据增加周期类型 及关系表状态 end by jiarui 20180105