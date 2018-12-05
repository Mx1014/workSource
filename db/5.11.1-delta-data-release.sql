
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR:
-- REMARK:

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V7.5
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10022', 'zh_CN', '此账单不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10023', 'zh_CN', '该账单不是已出账单');

-- AUTHOR: 张智伟
-- REMARK: issue-42126
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'meetingMessage' AS scope,100008 AS code,'zh_CN' AS locale,'您已不是参会人' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100009 AS code,'zh_CN' AS locale,'您已不是会务人' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100010 AS code,'zh_CN' AS locale,'您已被指定为会务人' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;

-- AUTHOR:  谢旭双
-- REMARK: 修改会议预定消息内容
UPDATE eh_locale_strings SET text = REPLACE(text,'您','你') WHERE scope='meetingErrorCode';
UPDATE eh_locale_templates SET text = REPLACE(text,'您','你'),description=REPLACE(description,'您','你')  WHERE scope='meetingMessage';



-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END guangda -------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本

-- AUTHOR: 黄明波
-- REMARK: 上海金茂语音识别对接
SET @max_id := (select ifnull(max(id), 0) from eh_xfyun_match);
SET @vendor := 'ZUOLINIOS';
SET @service := 'zuolin';
SET @namespace_id := 0;
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wuyebaoxiu', '物业报修', 20100, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wuyekefu', '物业客服', 40300, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wuyejiaofei', '物业缴费', 20400, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'fangkeyuyue', '访客预约', 52100, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'tingchejiaofei', '停车缴费', 40800, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'pinzhihecha', '品质核查', 20600, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'shequhuodong', '社区活动', 10600, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'ziyuanyuyue', '资源预约', 40400, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodegongdan', '我的工单', NULL, 1, 'zl://workflow/tasks', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodedianpu', '我的店铺', NULL, 1, 'zl://browser/i?url=${home.url}/mobile/static/stay_tuned/index.html', 2, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodefabu', '我的发布', NULL, 1, 'zl://user-publish/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodeshoucang', '我的收藏', NULL, 1, 'zl://user-collection/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodebaoming', '我的报名', NULL, 1, 'zl://user-apply/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'sousuo', '搜索', NULL, 1, 'zl://search/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'saoyisao', '扫一扫', NULL, 1, 'zl://scan/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'xiaoxi', '消息', NULL, 1, 'zl://message/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodedizhi', '我的地址', NULL, 1, 'zl://address/index', NULL, NULL);

-- 添加测试token
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('xfyun.tpp.testToken', '341a5441a2ac8c2f', '讯飞测试token', 0, NULL, 0);



-- --------------------- SECTION END jinmao ------------------------------------------