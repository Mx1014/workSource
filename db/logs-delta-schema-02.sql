-- 活动表标识该活动是否为全天活动 2017-04-05 16:32 add by yanjun
ALTER TABLE `eh_activities` ADD COLUMN `all_day_flag` TINYINT DEFAULT '0' NULL COMMENT 'whether it is an all day activity, 0 not, 1 yes';
   
   
-- 设备表增加字段是否需要拍照 add by xiongying20170406
ALTER TABLE `eh_equipment_inspection_equipments` ADD COLUMN `picture_flag` tinyint(4) DEFAULT '1' NULL COMMENT 'whether need to take a picture while report equipment task, 0 not, 1 yes';

-- 机构表里增加字段 记录操作人  2017-04-20 add by sfyan
ALTER TABLE `eh_organizations` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `operator_uid` BIGINT;

-- 机构人员表里增加字段 记录操作人  2017-04-20 add by sfyan
ALTER TABLE `eh_organization_members` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `operator_uid` BIGINT;

-- 导入文件任务  2017-04-20 add by sfyan
CREATE TABLE `eh_import_file_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '',
  `status` TINYINT NOT NULL,
  `result` TEXT,
  `creator_uid` BIGINT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 增加一个icon别名。现在用于搜索结果页面，原有icon_uri有圆形、方形等，展现风格不一致。应对这样的场景增加alias_icon_uri，存储圆形默认图片。搜索功能模块当它不为空时用它替换icon_uri add by yanjun 20170420
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `alias_icon_uri` VARCHAR(1024) NULL COMMENT '原有icon_uri有圆形、方形等，展现风格不一致。应对这样的场景增加alias_icon_uri，存储圆形默认图片。' ;

-- 增加一个排序字段，用于客户端显示顺序 add by yanjun 20170427 search-2.0
ALTER TABLE `eh_search_types` ADD COLUMN `order` TINYINT(4) NULL;