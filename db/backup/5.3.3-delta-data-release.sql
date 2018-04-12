-- 广告管理 v1.4    add by xq.tian  2018/03/07
SET @eh_locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
    VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'banner', '10004', 'zh_CN', '跳转数据处理失败');

-- 增加扫码登录的域空间配置项
set @c_id = (select max(id) from eh_configurations);
-- 测试环境
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@c_id:= @c_id +1, 'scanForLogonServer', 'http://web-test.zuolin.com', NULL, '999971', NULL);
-- 正式环境
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@c_id:= @c_id +1, 'scanForLogonServer', 'http://web.zuolin.com', NULL, '999971', NULL);

-- 删除没用的 banner    add by xq.tian   2018/04/08
DELETE FROM eh_banners WHERE status = 0;

DROP PROCEDURE if exists delete_banner;
delimiter //
CREATE PROCEDURE `delete_banner` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE scene VARCHAR(128);
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select namespace_id, scene_type from eh_banners where status = 2 AND scene_type IS NOT NULL GROUP BY namespace_id;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO ns,scene;
                IF done THEN
                    LEAVE read_loop;
                END IF;

        DELETE FROM eh_banners WHERE namespace_id = ns AND scene_type <> scene;
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL delete_banner;
DROP PROCEDURE if exists create_admin;

-- 增加安邦的配置项 lei.lv
set @e_id = (select max(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@e_id := @e_id + 1, 'anbang.namespace.id', 999949, NULL, 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@e_id := @e_id + 1, 'anbang.oauth.url', 'http://139.196.255.176:8000', NULL, 999949, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@e_id := @e_id + 1, 'anbang.clientid', 'zuolin', NULL, 999949, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@e_id := @e_id + 1, 'anbang.clientsecret', 'enVvbGluMjAxODAxMDI=', NULL, 999949, NULL);

-- 工作流的字符模板     add by xq.tian  2018/04/09
SET @eh_locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
    VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '10010', 'zh_CN', '催办次数已达上限');

-- Mybay隐藏代发 by zheng
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`)
VALUES ( 'pmtask.hide.represent', '1', '代发隐藏', '999966');

-- 更新“企业账户”菜单图标 add by yanjun 201804101505
UPDATE eh_web_menus set icon_url = 'business_account' WHERE id = 52000000;

-- 更新错误码信息 add by yuanlei 201804101556
UPDATE eh_locale_strings SET TEXT = '该申请已经被处理' WHERE scope = 'organization' AND CODE=500005 AND locale='zh_CN';

/**
 * Designer:yilin Liu
 * Description:ISSUE#26184 门禁人脸识别
 * Created：2018-4-9
 */
-- 修复旧数据
UPDATE `eh_door_access` set `namespace_id` = 999981 where hardware_id like '%C0:8B:18:B3:BB:4B%';
 
-- 多公司管理
 
set @max_door_access_id = (select max(id) from eh_door_access);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `mac_copy`) VALUES (@max_door_access_id + 1, '999981', '4284c5f93a5c434099a3f9424a5d22e3A', '0', 'C0:8B:18:B3:BB:4B1', 'A2-05', NULL, '一期5号', NULL, '五星路707弄', '245814', '245814', NULL, NULL, NULL, 'WhXDz1msS5Yqttso7TS7vg==', '0', '1', '1040198', '0', '2017-04-21 18:04:03', '1', '1', '1', '0', 'C0:8B:18:B3:BB:4B');

set @max_aes_server_key_id = (select max(id) from eh_aes_server_key);
INSERT INTO `eh_aes_server_key` (`id`, `door_id`, `device_ver`, `secret_ver`, `secret`, `create_time_ms`) VALUES (@max_aes_server_key_id + 1, @max_door_access_id + 1, '0', '1', 'WhXDz1msS5Yqttso7TS7vg==', '1492769042835');

/**
* End by: yilin Liu
*/

