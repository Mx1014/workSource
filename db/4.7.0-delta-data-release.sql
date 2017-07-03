
-- merge from forum-2.0 start by yanjun 20170703
-- 增加话题、投票类型的热门标签  add by yanjun 20170613
SET @eh_hot_tags_id = (SELECT MAX(id) FROM eh_hot_tags);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','二手和租售','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','免费物品','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','失物招领','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','topic','紧急通知','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','创意','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','人气','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','节日','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','兴趣','1','0',NOW(),'1',NULL,NULL);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`) VALUES(@eh_hot_tags_id := @eh_hot_tags_id + 1,'0','poll','讨论','1','0',NOW(),'1',NULL,NULL);

-- 修改搜索的页面图标的显示顺序，话题第一、投票第二 add by yanjun 20170627
UPDATE eh_search_types SET `order`  = 1 WHERE content_type = 'topic';
UPDATE eh_search_types SET `order`  = 2 WHERE content_type = 'activity';
-- merge from forum-2.0 end by yanjun 20170703