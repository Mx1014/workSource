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

-- 当item对应的layout是方形时给它一个默认的icon，现在用于搜索时的icon展现。以下的图片路径需要根据上线时实际的图片路径作修改。
SELECT 'cs://1/image/aW1hZ2UvTVRwaU5XWmtPREJoWVdGbU5UWTFOalkzTkdGbE5HUTNZbVprTm1WaU5tTmhNQQ' INTO @alias_icon_uri FROM DUAL;
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 0 AND item_group IN ('Bizs') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999980 AND item_group IN ('Gallery', 'Coupons') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999982 AND item_group IN ('Gallery') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999983 AND item_group IN ('Gallery1', 'Gallery', 'BizList') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999985 AND item_group IN ('libraryBanner', 'BizList') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999986 AND item_group IN ('aclinkAndpunchBanner', 'aclinkAndpunchBiz', 'serviceAllianceBanner', 'serviceAllianceBiz', 'communityBanner', 'communityBiz') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999990 AND item_group IN ('GovAgencies') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999991 AND item_group IN ('BizList') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999992 AND item_group IN ('Coupons') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999993 AND item_group IN ('Coupons') ; 
UPDATE eh_launch_pad_items a SET a.alias_icon_uri = @alias_icon_uri WHERE a.namespace_id = 999999 AND item_group IN ('MakerBanners', 'MakerSpaces') ; 