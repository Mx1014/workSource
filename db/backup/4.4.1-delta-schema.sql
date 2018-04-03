-- 活动表标识该活动是否为全天活动 2017-04-05 16:32 add by yanjun
ALTER TABLE `eh_activities` ADD COLUMN `all_day_flag` TINYINT DEFAULT '0' NULL COMMENT 'whether it is an all day activity, 0 not, 1 yes';
   
   
-- 设备表增加字段是否需要拍照 add by xiongying20170406
ALTER TABLE `eh_equipment_inspection_equipments` ADD COLUMN `picture_flag` tinyint(4) DEFAULT '1' NULL COMMENT 'whether need to take a picture while report equipment task, 0 not, 1 yes';