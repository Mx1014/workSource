-- 深业能耗数据删除   add by xq.tian  2017/01/13
DELETE FROM `eh_energy_meter_categories` WHERE `status` = 0 AND `namespace_id` = 999992;

DELETE FROM `eh_energy_meter_formulas` WHERE `status` = 0 AND `namespace_id` = 999992;

UPDATE `eh_energy_meter_setting_logs` SET `formula_id` = 5 WHERE `setting_type` = 3 AND `namespace_id` = 999992;