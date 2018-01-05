--  标准数据增加周期类型 start by jiarui 20180105
UPDATE eh_equipment_inspection_standards
SET repeat_type = (SELECT repeat_type FROM eh_repeat_settings WHERE id = eh_equipment_inspection_standards.repeat_setting_id)
WHERE STATUS =2  AND repeat_type IS NULL;
--  标准数据增加周期类型 end by jiarui 20180105