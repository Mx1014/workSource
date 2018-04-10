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