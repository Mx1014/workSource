-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: xq.tian 20181116
-- REMARK: 替换最新的 contentserver 二进制 #40547 contentserver/release/server/contentserver




-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: xq.tian  20181116
-- REMARK: 报错提示模板
INSERT INTO eh_locale_strings (scope, code, locale, text) VALUES ('flow', '10013', 'zh_CN', '任务状态已经改变，请刷新重试');

-- AUTHOR: 黄明波  20181121
-- REMARK: 添加服务联盟短信模板
SET @module_id = 40500;  -- 模块id
SET @sms_code = 87;      -- sms code, 对应于`com.everhomes.rest.sms.SmsTemplateCode`中的 code
SET @description = '模板1';
SET @display_name = '【app名称】$发起人姓名$（$发起人手机号$）提交了$服务名称$申请，请及时处理';
SET @namespace_id = 0; -- 域空间id, 如果为0, 则相当于配置给所有域空间, 不为0, 则只给特定的域空间配置
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@locale_templates_id := @locale_templates_id + 1), CONCAT('flow:', @module_id), @sms_code, 'zh_CN', @description, @display_name, @namespace_id);


SET @module_id = 40500;  -- 模块id
SET @sms_code = 88;      -- sms code, 对应于`com.everhomes.rest.sms.SmsTemplateCode`中的 code
SET @description = '模板2';
SET @display_name = '【app名称】你提交的$服务名称$申请正在处理，可在app“我”-“我的申请”中查看处理进度';
SET @namespace_id = 0; -- 域空间id, 如果为0, 则相当于配置给所有域空间, 不为0, 则只给特定的域空间配置
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@locale_templates_id := @locale_templates_id + 1), CONCAT('flow:', @module_id), @sms_code, 'zh_CN', @description, @display_name, @namespace_id);


INSERT INTO eh_locale_templates (scope, code, locale, description, text, namespace_id) VALUES ( 'sms.default', '87', 'zh_CN', '服务申请推送', '${applierName}（${applierPhone}）提交了${serviceName}申请，请及时处理', '0');    
INSERT INTO eh_locale_templates (scope, code, locale, description, text, namespace_id) VALUES ( 'sms.default', '88', 'zh_CN', '服务申请提醒', '你提交的${serviceName}申请正在处理，可在app“我”-“我的申请”中查看处理进度', '0');

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
-- ENV: shanghaijinmao
-- DESCRIPTION: 此SECTION只在上海金茂-999925执行的脚本
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.host', 'http://10.1.10.37:80', '接口地址', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.appkey', '880076901009202', 'appkey', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.secretKey', '880076901009202', 'secretKey', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.parkUuid', '06dfa3ed3a5a4309bd087fd2625ea00e', '停车场标识', 999925, NULL, 1);




set @max_lots_id := (select ifnull(max(id),0)  from eh_parking_lots);
set @namespace_id := 11;
set @community_id := 240111044332061474;
set @parking_name := '上海金茂停车场';
set @parking_vendor := 'HKWS_SHJINMAO';
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`) 

VALUES ((@max_lots_id := @max_lots_id + 1), 'community', @community_id,  @parking_name,  @parking_vendor, '', 2, 1, now(), @namespace_id, '{"expiredRechargeFlag":0,"monthlyDiscountFlag":0,"tempFeeDiscountFlag":0}', '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0}', right(@max_lots_id, 3), @max_lots_id, NULL, '["tempfee"]');



-- --------------------- SECTION END shanghaijinmao ------------------------------------------