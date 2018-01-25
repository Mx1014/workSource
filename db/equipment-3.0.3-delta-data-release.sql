--  标准数据增加周期类型及关系表状态start by jiarui 20180105
UPDATE eh_equipment_inspection_standards
SET repeat_type = (SELECT repeat_type FROM eh_repeat_settings WHERE id = eh_equipment_inspection_standards.repeat_setting_id)
WHERE STATUS =2  AND repeat_type =0;

-- 上版未置状态数据修改
UPDATE eh_equipment_inspection_equipment_standard_map
SET `status` = 0
WHERE review_status IN (0, 3 ,4) OR review_result = 2;
-- 上版bug数据修改

--  标准数据增加周期类型 及关系表状态 end by jiarui 20180105
-- 巡检任务状态统一 start by jiarui 20180105
UPDATE eh_equipment_inspection_tasks
SET `status` = 6
WHERE `status` = 4 AND review_result = 2;

UPDATE eh_equipment_inspection_tasks
SET `status` = 7
WHERE `status` = 4 AND review_result = 4;

UPDATE eh_equipment_inspection_task_logs
SET equipment_id = (SELECT  equipment_id FROM eh_equipment_inspection_tasks WHERE id = task_id);

-- 巡检任务状态统一 end by jiarui 20180105

-- 增加经纬度动态表单  jiarui   20180122
INSERT  INTO  `eh_var_fields` VALUES (10007, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10008, CONCAT('/',10008,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10107, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10007, CONCAT('/',10007,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10207, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10006, CONCAT('/',10006,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10307, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10005, CONCAT('/',10005,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10407, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10004, CONCAT('/',10004,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10507, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10003, CONCAT('/',10003,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10607, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10002, CONCAT('/',10002,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10707, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10001, CONCAT('/',10001,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');

INSERT  INTO  `eh_var_fields` VALUES (10926, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10000, CONCAT('/',10000,'/'), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"map\", \"length\": 32}');
-- 增加经纬度动态表单  jiarui  20180122