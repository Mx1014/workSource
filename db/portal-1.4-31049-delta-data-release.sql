
-- 重建服务广场模板
DELETE from eh_portal_layout_templates;
INSERT INTO `eh_portal_layout_templates` (`id`, `namespace_id`, `label`, `template_json`, `show_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `type`) VALUES ('1', '0', '首页', '{\"layoutName\":\"ServiceMarketLayout\",\"location\":\"/home\",\"groups\":[{\"label\":\"海报\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Banners\",\"style\":\"Default\",\"defaultOrder\":1},{\"label\":\"公告\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Bulletins\",\"style\":\"Default\",\"defaultOrder\":2,\"description\":\"\"},{\"label\":\"容器\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Navigator\",\"style\":\"Gallery\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"\"},\"defaultOrder\":3,\"description\":\"\"}]}', NULL, '2', '2017-09-15 18:53:16', '2017-09-15 18:53:16', '1', '1', NULL, '1');
INSERT INTO `eh_portal_layout_templates` (`id`, `namespace_id`, `label`, `template_json`, `show_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `type`) VALUES ('2', '0', '自定义门户', '{\"groups\":[{\"label\":\"容器\", \"separatorFlag\":\"0\",\"separatorHeight\":\"0\",\"widget\":\"Navigator\",\"style\":\"Gallery\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"\"},\"defaultOrder\":1,\"description\":\"\"}]}', NULL, '2', '2017-09-15 18:53:16', '2017-09-15 18:53:16', '1', '1', NULL, '2');
INSERT INTO `eh_portal_layout_templates` (`id`, `namespace_id`, `label`, `template_json`, `show_uri`, `status`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `description`, `type`) VALUES ('3', '0', '分页签门户', '{\"groups\":[{\"label\":\"分页签\", \"separatorFlag\":\"0\", \"separatorHeight\":\"0\",\"widget\":\"Tab\",\"style\":\"1\",\"defaultOrder\":1}]}', NULL, '2', '2017-09-15 18:53:16', '2017-09-15 18:53:16', '1', '1', NULL, '3');

-- 首页（激活）
UPDATE eh_portal_layouts SET type = 1, index_flag = 1 WHERE location = '/home' AND `name` = 'ServiceMarketLayout';
-- 自定义门户（激活）
UPDATE eh_portal_layouts SET type = 2, index_flag = 1 WHERE location = '/secondhome' AND `name` = 'SecondServiceMarketLayout';
-- 分页签门户（激活）
UPDATE eh_portal_layouts SET type = 3, index_flag = 1 WHERE location = '/association' AND `name` = 'AssociationLayout';

-- 分页签门户（未激活）
UPDATE eh_portal_layouts a SET type = 3, index_flag = 0 WHERE type IS NULL AND EXISTS ( SELECT * from  eh_portal_item_groups b WHERE a.id = b.layout_id and b.widget = 'Tab');

-- 自定义门户（未激活）
UPDATE eh_portal_layouts SET type = 2, index_flag = 0 WHERE type IS NULL;

