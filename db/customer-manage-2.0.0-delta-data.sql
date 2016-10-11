--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
VALUES ('1', '0', NULL, NULL, '1', '临时卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
VALUES ('2', '0', NULL, NULL, '2', '月临时', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
VALUES ('3', '0', NULL, NULL, '3', '充值卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
VALUES ('4', '0', NULL, NULL, '4', '固定卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
VALUES ('5', '0', NULL, NULL, '5', '免费卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
VALUES ('6', '0', NULL, NULL, '6', '其它', '1', NULL, NULL);

--
-- 客户资料分类     add by xq.tian 2016/10/13
--
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`, `create_time`, `update_time`, `update_uid`)
VALUES ('7', '0', 'none', '无', '1', NULL, NULL, NULL);
