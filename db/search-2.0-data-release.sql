-- 增加应用类型和商家类型
SELECT MAX(id) INTO @id FROM eh_search_types;
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'0','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'1000000','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999992','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999991','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999989','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999990','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999993','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999999','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999988','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999987','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999986','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999985','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999983','','0','应用','launchpaditem','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'0','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'1000000','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999992','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999991','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999989','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999990','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999993','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999999','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999988','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999987','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999986','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999985','','0','商家','shop','1',NULL,NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES(@id:=@id+1,'999983','','0','商家','shop','1',NULL,NULL);

-- 搜索电商商家API
SELECT MAX(id) INTO @id FROM eh_configurations;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'biz.search.shops.api','/zl-ec/rest/openapi/shop/listByKeyword','搜索电商商家API','0',NULL);