
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

-- 初始化数据，add by tt, 20170703
set @id = 0;
insert into eh_rentalv2_price_rules 
(
    `id`,
    `owner_type`,
    `owner_id`,
    `rental_type`,
    `workday_price`,
    `weekend_price`,
    `org_member_workday_price`,
    `org_member_weekend_price`,
    `approving_user_workday_price`,
    `approving_user_weekend_price`, 
    `discount_type`,
    `full_price`,
    `cut_price`,
    `discount_ratio`,
    `cell_begin_id`,
    `cell_end_id`,
    `creator_uid`,
    `create_time`
)
select @id:=@id+1,
         'default',
         t.id,
         t.rental_type,
         t.workday_price,
         t.weekend_price,
         t.org_member_workday_price,
         t.org_member_weekend_price,
         t.approving_user_workday_price,
         t.approving_user_weekend_price,
         null,
         null,
         null,
         null,
         0,
         0,
         0,
         now()
from eh_rentalv2_default_rules t
where t.rental_type is not null
union all
select @id:=@id+1,
         'resource',
         t2.id,
         t2.rental_type,
         t2.workday_price,
         t2.weekend_price,
         t2.org_member_workday_price,
         t2.org_member_weekend_price,
         t2.approving_user_workday_price,
         t2.approving_user_weekend_price,
         t2.discount_type,
         t2.full_price,
         t2.cut_price,
         t2.discount_ratio,
         t2.cell_begin_id,
         t2.cell_end_id,
         0,
         now()
from eh_rentalv2_resources t2
where t2.rental_type is not null;


