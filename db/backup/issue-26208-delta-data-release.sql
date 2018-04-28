-- Designer: zhiwei zhang
-- Description: ISSUE#26208 日程1.0


INSERT INTO eh_remind_settings(id,name,offset_day,fix_time,default_order,creator_uid,create_time)
SELECT r.id,r.name,r.offset_day,'09:00',r.default_order,0,NOW() FROM(
SELECT 1 AS id,'当天（09:00）' AS name,0 AS offset_day,1 AS default_order UNION ALL
SELECT 2 AS id,'提前1天（09:00）' AS name,1 AS offset_day,2 AS default_order UNION ALL
SELECT 3 AS id,'提前2天（09:00）' AS name,2 AS offset_day,3 AS default_order UNION ALL
SELECT 4 AS id,'提前3天（09:00）' AS name,3 AS offset_day,4 AS default_order UNION ALL
SELECT 5 AS id,'提前5天（09:00）' AS name,5 AS offset_day,5 AS default_order UNION ALL
SELECT 6 AS id,'提前1周（09:00）' AS name,7 AS offset_day,6 AS default_order
)r LEFT JOIN eh_remind_settings s ON r.id=s.id
WHERE s.id IS NULL;

INSERT INTO eh_configurations(name,value,namespace_id)
SELECT r.name,r.value,r.namespace_id FROM
(
SELECT 'remind.colour.list' AS name,'#B3FF3B30,#FFF58F3E,#FFF2C500,#FF4AD878,#FF00B9EF,#FF4285F4,#B3673AB7' AS value,0 AS namespace_id union all
SELECT 'remind.colour.share' AS name,'#FF6E6E74' AS value,0 AS namespace_id
)r LEFT JOIN eh_configurations c ON r.name=c.name
WHERE c.id IS NULL;


INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'calendarRemind' AS scope,10000 AS code,'zh_CN' AS locale,'分类名称已存在' AS text UNION ALL
SELECT 'calendarRemind' AS scope,10001 AS code,'zh_CN' AS locale,'此日程已被删除' AS text UNION ALL
SELECT 'calendarRemind' AS scope,10002 AS code,'zh_CN' AS locale,'此日程已取消共享' AS text UNION ALL
SELECT 'calendarRemind' AS scope,10003 AS code,'zh_CN' AS locale,'最后一个分类不能删除' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;



-- volgo 添加日程图标.
SET @item_id = (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
VALUES (@item_id := @item_id + 1, '1', '0', '0', '0', '/home', 'Bizs', '日程', '日程', 'cs://1/image/aW1hZ2UvTVRvNE5XWXdPVFl6Wm1SaVpqWmxaVFJpT0RkaE56WXpNR1EyTnpsa1ptSmpZZw', '1', '1', '73', '{"title":"日程"}', '11', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL, '0', NULL,NULL);


-- End by: zhiwei zhang
