--
-- eh_user_activities 数据处理
--
UPDATE `eh_terminal_app_version_actives` AS t, `eh_user_activities` AS u
SET t.`imei_number` = u.`uid`
WHERE t.`imei_number` = u.`imei_number` AND u.`uid` <> 0;

UPDATE `eh_terminal_app_version_cumulatives` AS t, `eh_user_activities` AS u
SET t.`imei_number` = u.uid
WHERE t.`imei_number` = u.`imei_number` AND u.uid <> 0;

DELETE FROM `eh_user_activities` WHERE `uid` = 0;

UPDATE `eh_user_activities` AS u SET u.`imei_number` = u.`uid`;