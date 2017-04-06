-- 增加一个“活动时间：”中文字符串 2017-03-31 19:17 add by yanjun
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('activity','11','zh_CN','活动时间：');

-- 处理设备-标准关联关系脏数据 add by xiongying20170406
update eh_equipment_inspection_equipment_standard_map set review_status = 4 where target_id in(select id from eh_equipment_inspection_equipments where status = 0);


