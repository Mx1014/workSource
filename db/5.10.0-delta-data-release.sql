-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR:xq.tian 20181016
-- REMARK:网关及注册中心部署.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539677844

-- AUTHOR:梁燕龙 20181016
-- REMARK:统一用户上线操作.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539678631

-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
-- REMARK：备份eh_payment_variables表
-- select * from eh_payment_variables;



-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境


-- AUTHOR:黄明波
-- REMARK:服务联盟数据迁移，待续
-- 迁移 start
-- 迁移1.调整ca表的ownerType和ownerId
update eh_service_alliance_categories ca, eh_service_alliances sa 
set ca.owner_type = sa.owner_type, ca.owner_id = sa.owner_id, ca.`type` = ca.id 
,ca.enable_provider = ifnull(sa.integral_tag3, 0) , ca.enable_comment = ifnull(sa.enable_comment, 0)
where ca.parent_id = 0 and sa.`type` = ca.id;


-- 迁移2.调整ca表子类的ownerType ownerId, type
update eh_service_alliance_categories  cag1,  eh_service_alliance_categories  cag2 
set cag1.owner_type = cag2.owner_type, cag1.owner_id = cag2.owner_id, cag1.`type` = cag2.`type` 
where cag1.parent_id = cag2.id;


-- 迁移3.更新ca表skip_rule
update eh_service_alliance_categories ca, eh_service_alliance_skip_rule sr 
set ca.skip_type = 1, ca.delete_uid = -100 where ca.id = sr.service_alliance_category_id and sr.id is not null and ca.namespace_id = sr.namespace_id;


-- 迁移4.tag表填充ownerType ownerId
update eh_alliance_tag tag, eh_service_alliances sa 
set tag.owner_type = sa.owner_type, tag.owner_id = sa.owner_id 
where tag.type = sa.type and sa.parent_id = 0 and tag.type <> 0 ;

-- 迁移5.jumpType应用跳转时，设置为3 
update eh_service_alliances 
set  integral_tag1 = 3
where module_url not like 'zl://approva%' and  module_url not like 'zl://form%' and  module_url is not null and integral_tag1 = 2;


-- 迁移6.添加基础数据
DELIMITER $$  -- 开始符

CREATE PROCEDURE alliance_transfer_add_base_ca(

) -- 声明存储过程

READS SQL DATA
SQL SECURITY INVOKER

BEGIN

DECLARE  no_more_record INT DEFAULT 0;
DECLARE  pName varchar(64);
DECLARE pNamespaceId INT;
DECLARE pType BIGINT(20);

DECLARE  cur_record CURSOR FOR   SELECT  name,  namespace_id, `type` from eh_service_alliance_categories;  -- 首先这里对游标进行定义
 DECLARE  CONTINUE HANDLER FOR NOT FOUND  SET  no_more_record = 1; -- 这个是个条件处理,针对NOT FOUND的条件,当没有记录时赋值为1
 
 OPEN  cur_record; -- 接着使用OPEN打开游标
 FETCH  cur_record INTO pName, pNamespaceId, pType; -- 把第一行数据写入变量中,游标也随之指向了记录的第一行
 
 
 SET @max_id = (select max(id) from eh_service_alliance_categories);
 
 WHILE no_more_record != 1 DO
 INSERT  INTO eh_service_alliance_categories(id, name, namespace_id, parent_id, owner_type, owner_id,creator_uid,`status`, `type`)
 VALUES  (@max_id:=@max_id+1, pName, pNamespaceId, 0, 'organaization', -1, 3, 2, pType );
 FETCH  cur_record INTO pName, pNamespaceId, pType;
 
 END WHILE;
 CLOSE  cur_record;  -- 用完后记得用CLOSE把资源释放掉

END

$$

DELIMITER ; -- 结束符

call alliance_transfer_add_base_ca();

DROP PROCEDURE IF EXISTS alliance_transfer_add_base_ca;


-- 迁移7.添加服务与类型的关联到match表
DELIMITER $$  -- 开始符

CREATE PROCEDURE alliance_transfer_add_match(

) -- 声明存储过程

READS SQL DATA
SQL SECURITY INVOKER

BEGIN

DECLARE  no_more_record INT DEFAULT 0;
DECLARE  pServiceId BIGINT(20);
DECLARE  pCategoryId BIGINT(20);
DECLARE  pNamespaceId BIGINT(20);
DECLARE  pOwnerType VARCHAR(50);
DECLARE  pOwnerId BIGINT(20);
DECLARE  pType BIGINT(20);
DECLARE  pCategoryName VARCHAR(64);

-- 首先这里对游标进行定义
DECLARE  cur_record CURSOR FOR  
SELECT  sa.id, sa.category_id, ca.name, ca.namespace_id,  sa.owner_type, sa.owner_id, ca.`type` 
from eh_service_alliances sa, eh_service_alliance_categories ca 
where sa.category_id = ca.id and sa.category_id is not null and sa.parent_id <> 0; 

-- 这个是个条件处理,针对NOT FOUND的条件,当没有记录时赋值为1
DECLARE  CONTINUE HANDLER FOR NOT FOUND  SET  no_more_record = 1; 
 
 OPEN  cur_record; -- 接着使用OPEN打开游标
 FETCH  cur_record INTO pServiceId, pCategoryId, pCategoryName,  pNamespaceId, pOwnerType, pOwnerId, pType; -- 把第一行数据写入变量中,游标也随之指向了记录的第一行
 
 SET @max_id = (select ifnull(max(id),0) from eh_alliance_service_category_match);
 
 WHILE no_more_record != 1 DO
 
 INSERT  INTO eh_alliance_service_category_match(id, namespace_id, owner_type, owner_id, `type`, service_id, category_id, category_name,create_time, create_uid)
 VALUES  (@max_id:=@max_id+1, pNamespaceId, pOwnerType, pOwnerId, pType, pServiceId, pCategoryId, pCategoryName, now(), 3 );
 FETCH  cur_record INTO pServiceId, pCategoryId, pCategoryName,  pNamespaceId, pOwnerType, pOwnerId, pType;
 
 END WHILE;
 CLOSE  cur_record;  -- 用完后记得用CLOSE把资源释放掉

END

$$

DELIMITER ; -- 结束符

call alliance_transfer_add_match(); -- 执行

DROP PROCEDURE IF EXISTS alliance_transfer_add_match;  --删除该存储过程
-- 迁移 end


-- AUTHOR:黄明波
-- REMARK:云打印账号迁移
update eh_siyin_print_business_payee_accounts ac set ac.merchant_id = ac.payee_id ;



-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
delete from eh_payment_variables;
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (1, NULL, NULL, '单价', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'dj');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (2, NULL, 1, '面积', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'mj');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (3, NULL, 6, '固定金额', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'gdje');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (4, NULL, 5, '用量', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'yl');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (5, NULL, 6, '欠费', 0, '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (7, NULL, NULL, '比例系数', 0, '2018-05-04 21:34:48', NULL, '2018-05-04 21:34:48', 'blxs');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (8, NULL, NULL, '折扣', 0, '2018-05-23 02:09:38', NULL, '2018-05-23 02:09:38', 'zk');


-- AUTHOR:杨崇鑫 20181015
-- REMARK:补充缴费模块“应用开小差”的错误码
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10012', 'zh_CN', '第三方授权异常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10013', 'zh_CN', '收费项标准公式不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10014', 'zh_CN', '收费项标准类型错误');
	
-- 更新 layout
SET @versionCode = '201810110200';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @newsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10800 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\","title":"园区运营","titleFlag":1,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\","title":"企业办公","titleFlag":1,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\",\"style\":\"Shape\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\","title":"容器","titleFlag":0,"titleStyle":101,"titleSize":2,"titleMoreFlag":0,\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\","title":"商品精选","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\","title":"活动","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\","title":"论坛","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"},{\"defaultOrder\":8,\"groupName\":\"园区快讯\","title":"园区快讯","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10800,\"appId\":', @newsAppId, ',\"actionType\":48,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"NewsListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;




-- 企业访客 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (52100, 52200);
-- 更新应用信息
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id in (52100, 52200);

-- 默认的微信消息模板Id
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wx.default.template.id', 'JnTt-ce69Wlie-o8nv4Jhl3CKA0pXaageIsr4aJiWCk', '默认的微信消息模板Id', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wx.default.template.url', 'http://www.zuolin.com/', '默认的微信消息模板url', '0', NULL, '1');


-- AUTHOR: 荣楠
-- REMARK: 组织架构4.6
SET @locale_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100015', 'zh_CN', '账号重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100016', 'zh_CN', '账号长度不对或格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100017', 'zh_CN', '账号一经设定，无法修改');


-- AUTHOR: 严军
-- REMARK: 客户端处理方式
update eh_service_modules set client_handler_type = 2 WHERE id in (41700, 20100,40730,41200);


-- AUTHOR: 严军
-- REMARK: 云打印设置为园区应用
UPDATE eh_service_modules set app_type = 1 WHERE id = 41400;
UPDATE eh_service_module_apps a set app_type = 1 WHERE module_id = 41400;

UPDATE eh_service_modules set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' WHERE id = 41400;
UPDATE eh_service_module_apps set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' WHERE module_id = 41400;

-- AUTHOR: 严军
-- REMARK: 工位预定客户端处理方式设置为内部链接
update eh_service_modules set client_handler_type = 2 WHERE id in (40200);


-- AUTHOR: 严军
-- REMARK: 开放“应用入口”菜单
DELETE FROM eh_web_menus WHERE id = 15010000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15010000', '基础数据', '15000000', NULL, NULL, '1', '2', '/15000000/15010000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL);
DELETE FROM eh_web_menus WHERE id = 15025000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15010000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15010000/15025000', 'zuolin', '30', NULL, '3', 'system', 'module', NULL);

-- AUTHOR: 严军
-- REMARK: 设置默认的应用分类
UPDATE eh_service_modules set app_type = 1 WHERE app_type is NULL;
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);

update eh_service_modules set client_handler_type = 2 WHERE id = 43000;

-- AUTHOR: xq.tian
-- REMARK: 用户名或密码错误提示 add by xq.tian  2018/10/11
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'user', '100020', 'zh_CN', '用户名或密码错误');

-- AUTHOR: 缪洲 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10034', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10035', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10036', 'zh_CN', '订单状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10037', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10038', 'zh_CN', '工作流未开启');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10039', 'zh_CN', '对象不存在');

-- AUTHOR: 马世亨 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10020','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10021','zh_CN','查询失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10022','zh_CN','文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10023','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10024','zh_CN','第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10025','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1414','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1415','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1416','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1417','zh_CN','二维码下载失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1418','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1419','zh_CN','文件导出失败');

-- by st.zheng
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'relocation', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '507', 'zh_CN', '参数缺失', '参数缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '508', 'zh_CN', '资源或资源规则缺失', '资源或资源规则缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '509', 'zh_CN', '找不到订单或订单状态错误', '找不到订单或订单状态错误', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '510', 'zh_CN', '下单失败', '下单失败', '0');
ALTER TABLE `eh_rentalv2_site_resources`
MODIFY COLUMN `name`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `type`;


-- AUTHOR: 黄明波 20181008
-- REMARK: issue-38650 增加error消息模板
-- yellowPage
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10012', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10013', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10014', 'zh_CN', '跳转链接格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10015', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10016', 'zh_CN', '获取电商模块失败');


-- express
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10003', 'zh_CN', 'URL加密失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10004', 'zh_CN', '请求失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10005', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10006', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10007', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10008', 'zh_CN', '获取公司失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10009', 'zh_CN', '用户鉴权失败，请重新登录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10010', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10011', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10012', 'zh_CN', '支付鉴权失败');

-- news
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10017', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10018', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10019', 'zh_CN', '无效的快讯类型id');

-- print

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10000', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10001', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10002', 'zh_CN', '邮箱地址格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10003', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10004', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10005', 'zh_CN', '获取打印任务失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10006', 'zh_CN', '订单不存在或已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10007', 'zh_CN', '打印机解锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10008', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10009', 'zh_CN', '扫码失败，请重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10010', 'zh_CN', '有未支付订单，请支付后重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10011', 'zh_CN', '订单已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10012', 'zh_CN', '锁定订单失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10013', 'zh_CN', '文件导出失败');


-- 马世亨 2018-10-10
-- 访客管理1.3 合并访客应用
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}' where id = 41800;
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-appointment/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}' where id = 52100;
delete from eh_service_modules where id in (42100,52200);
-- end

-- 马世亨 2018-10-10
-- 访客管理1.3 企业访客权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52110', '预约管理', '52100', '/100/50000/52100/52110', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52120', '访客管理', '52100', '/100/50000/52100/52120', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52130', '设备管理', '52100', '/100/50000/52100/52130', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52140', '移动端管理', '52100', '/100/50000/52100/52140', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);


set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52100', '0', '5210052100', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052110, '0', '企业访客 预约管理权限', '企业访客 预约管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52110', '0', 5210052110, '预约管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052120, '0', '企业访客 访客管理权限', '企业访客 访客管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52120', '0', 5210052120, '访客管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052130, '0', '企业访客 设备管理权限', '企业访客 设备管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52130', '0', 5210052130, '设备管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052140, '0', '企业访客 移动端管理权限', '企业访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52140', '0', 5210052140, '移动端管理权限', '0', now());


-- AUTHOR: 唐岑2018年10月17日20:32:09
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','101','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','102','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','103','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','104','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','105','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','106','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','107','zh_CN','消息内容为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','108','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','109','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','110','zh_CN','上传文件为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','111','zh_CN','解析文件失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','112','zh_CN','账单数据重复');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','113','zh_CN','服务器内部错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','114','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','115','zh_CN','该用户未欠费，不能向其发送催缴短信');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','116','zh_CN','支付方式不支持');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','117','zh_CN','订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','118','zh_CN','账单无效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','119','zh_CN','用户权限不足');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','120','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','121','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','122','zh_CN','excel数据格式不正确');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','123','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','124','zh_CN','创建预约计划失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','125','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','126','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','127','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','128','zh_CN','导出文件失败');

-- end

-- AUTHOR: xq.tian 2018-10-19
-- REMARK: 驳回按钮的默认跟踪
UPDATE eh_locale_strings SET text='任务已被 ${text_tracker_curr_operator_name} 驳回' WHERE scope='flow' AND code='20005';


-- AUTHOR: 严军 2018-10-21
-- REMARK: issue-38924 修改菜单
-- 一级菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('25000000', '资产管理系统', '0', NULL, NULL, '1', '2', '/25000000', 'zuolin', '23', NULL, '1', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('26000000', '物业服务系统', '0', NULL, NULL, '1', '2', '/26000000', 'zuolin', '26', NULL, '1', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('27000000', '统计分析', '0', NULL, NULL, '1', '2', '/27000000', 'zuolin', '60', NULL, '1', 'system', 'classify', NULL, '1');
UPDATE eh_web_menus set `name` = '园区运营系统' WHERE id = 16000000;
UPDATE eh_web_menus set `name` = '企业办公系统' WHERE id = 23000000;
-- 资产管理系统
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 10 WHERE id = 16010000;
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 20 WHERE id = 16210000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/25000000/') WHERE parent_id in (16010000, 16210000) OR id in (16010000, 16210000);
-- 物业服务系统
UPDATE eh_web_menus set parent_id = 26000000, sort_num = 10, `name` = '物业服务' WHERE id = 16050000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/26000000/') WHERE parent_id = 16050000 or id = 16050000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 16050400;
-- 园区运营系统
UPDATE eh_web_menus SET `status` = 2, parent_id = 16400000, path = '/16000000/16400000/16020500' WHERE id = 16020500;
UPDATE eh_web_menus SET `name` = '收款账户管理' WHERE id = 16070000;
-- 统计分析
UPDATE eh_web_menus set parent_id = 27000000, sort_num = 10, `name` = '统计分析' WHERE id = 17000000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/27000000/') WHERE parent_id = 17000000 or id = 17000000;
-- 企业办公系统
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23020000', '协同办公', '23000000', NULL, NULL, '1', '2', '/23000000/23020000', 'zuolin', '10', NULL, '2', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23030000', '人力资源', '23000000', NULL, NULL, '1', '0', '/23000000/23030000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23040000', '支付管理', '23000000', NULL, NULL, '1', '2', '/23000000/23040000', 'zuolin', '50', NULL, '2', 'system', 'classify', NULL, '1');
UPDATE eh_web_menus SET parent_id = 23040000, path = '/23000000/23040000/78000001' WHERE id = 78000001;
UPDATE eh_web_menus SET parent_id = 23040000, path = '/23000000/23040000/79100000' WHERE id = 79100000;

-- AUTHOR:黄鹏宇 2018年10月22日
-- REMARK:将计划任务中的拜访时间改为计划时间
update eh_var_fields set display_name = '计划时间' where display_name='拜访时间' and group_id = 20;
update eh_var_field_scopes set field_display_name = '计划时间' where field_display_name='拜访时间' and group_id = 20;


-- AUTHOR:黄鹏宇 2018年10月22日
-- REMARK:更改module表中的client_handler_type类型为外部链接
update eh_service_modules set client_handler_type = 2 where id = 25000;
update eh_service_modules set client_handler_type = 2 where id = 150020;

-- 资产管理系统
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 25 WHERE id = 20000000;
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 5 WHERE id = 21000000;
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 90 WHERE id = 22000000;
UPDATE eh_web_menus SET path = replace(path, '/11000000/', '/15000000/') WHERE parent_id in (20000000, 21000000, 22000000) OR id in (20000000, 21000000, 22000000);
UPDATE eh_web_menus SET `status` = 0 WHERE id = 11000000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 23020000;
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 30 WHERE id = 16050000;
UPDATE eh_web_menus SET path = replace(path, '/26000000/', '/25000000/') WHERE parent_id = 16050000 OR id = 16050000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 26000000;
UPDATE eh_web_menus SET `name` = '资管物业业务' WHERE id = 25000000;
UPDATE eh_web_menus set `name` = '园区运营业务' WHERE id = 16000000;
UPDATE eh_web_menus set `name` = '企业办公业务' WHERE id = 23000000;
UPDATE eh_web_menus set `name` = '统计分析业务' WHERE id = 27000000;



-- AUTHOR: 严军 2018-10-21
-- REMARK: issue-null 增加模块路由
update eh_service_modules set host = 'bulletin'  where id = 	10300;
update eh_service_modules set host = 'activity'  where id = 	10600;
update eh_service_modules set host = 'post'  where id = 	10100;
update eh_service_modules set host = 'group'  where id = 	10750;
update eh_service_modules set host = 'group'  where id = 	10760;
update eh_service_modules set host = 'approval'  where id = 	52000;
update eh_service_modules set host = 'work-report'  where id = 	54000;
update eh_service_modules set host = 'file-management'  where id = 	55000;
update eh_service_modules set host = 'remind'  where id = 	59100;
update eh_service_modules set host = 'meeting-reservation'  where id = 	53000;
update eh_service_modules set host = 'video-conference'  where id = 	50700;
update eh_service_modules set host = 'enterprise-bulletin'  where id = 	57000;
update eh_service_modules set host = 'enterprise-contact'  where id = 	50100;
update eh_service_modules set host = 'attendance'  where id = 	50600;
update eh_service_modules set host = 'salary'  where id = 	51400;
update eh_service_modules set host = 'station'  where id = 	40200;
update eh_service_modules set host = 'news-feed'  where id = 	10800;
update eh_service_modules set host = 'questionnaire'  where id = 	41700;
update eh_service_modules set host = 'hot-line'  where id = 	40300;
update eh_service_modules set host = 'property-repair'  where id = 	20100;
update eh_service_modules set host = 'resource-reservation'  where id = 	40400;
update eh_service_modules set host = 'visitor'  where id = 	41800;
update eh_service_modules set host = 'parking'  where id = 	40800;
update eh_service_modules set host = 'vehicle-release'  where id = 	20900;
update eh_service_modules set host = 'cloud-print'  where id = 	41400;
update eh_service_modules set host = 'item-release'  where id = 	49200;
update eh_service_modules set host = 'decoration'  where id = 	22000;
update eh_service_modules set host = 'service-alliance'  where id = 	40500;
update eh_service_modules set host = 'wifi'  where id = 	41100;
update eh_service_modules set host = 'park-enterprises'  where id = 	33000;
update eh_service_modules set host = 'park-settle'  where id = 	40100;
update eh_service_modules set host = 'property-payment'  where id = 	20400;
update eh_service_modules set host = 'property-inspection'  where id = 	20800;
update eh_service_modules set host = 'quality'  where id = 	20600;
update eh_service_modules set host = 'energy-management'  where id = 	49100;
update eh_service_modules set host = 'customer-management'  where id = 	21100;

update eh_service_modules set host = 'access-control'  where id = 	41000;
update eh_service_modules set client_handler_type = 2  where id = 	40700;
update eh_service_modules set client_handler_type = 2  where id = 	10800;

-- AUTHOR: st.zheng
-- REMARK: 增加商户管理模块及菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('210000', '商户管理', '170000', '/200/170000/210000', '1', '3', '2', '110', '2018-03-19 17:52:57', NULL, NULL, '2018-03-19 17:53:11', '0', '0', '0', '0', 'community_control', '1', '1', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79820000', '商户管理', '16400000', NULL, 'business-management', '1', '2', '/16000000/16400000/79820000', 'zuolin', '120', '210000', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79830000', '商户管理', '56000000', NULL, 'business-management', '1', '2', '/40000040/56000000/79830000', 'park', '120', '210000', '3', 'system', 'module', '2');

-- AUTHOR: st.zheng
-- REMARK: 资源预订3.7.1


-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加企业支付授权页面
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79800000, '企业支付授权', 16300000, NULL, 'payment-privileges', 1, 2, '/16000000/16300000/79800000', 'zuolin', 8, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79810000, '企业支付授权', 55000000, NULL, 'payment-privileges', 1, 2, '/40000040/55000000/79810000', 'park', 2, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`) VALUES (200000, '企业支付授权', 140000, '/200/140000', 1, 3, 2, 10, '2018-09-26 16:51:46', '{}', 13, '2018-09-26 16:51:46', 0, 0, '0', NULL, 'community_control', 1, 1, 'module', NULL, 0, NULL, NULL);

-- AUTHOR: liangqishi
-- REMARK: 把原来统一订单的配置项前缀gorder改为prmt 20181006
UPDATE `eh_configurations` SET `value`=REPLACE(`value`, 'gorder', 'prmt') WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.connect_url' WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.app_key' WHERE `name`='gorder.server.app_key';
UPDATE `eh_configurations` SET `name`='prmt.server.app_secret' WHERE `name`='gorder.server.app_secret';
UPDATE `eh_configurations` SET `name`='prmt.default.personal_bind_phone' WHERE `name`='gorder.default.personal_bind_phone';
UPDATE `eh_configurations` SET `name`='prmt.system_id' WHERE `name`='gorder.system_id';

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加未支付推送与短信模板
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (3421, 'sms.default', 83, 'zh_CN', '未支付短信', '您有一笔云打印的订单未支付，请到云打印-打印记录中进行支付。', 0);

-- AUTHOR: 缪洲 201801011
-- REMARK: issue-34780 删除打印设置规则
DELETE FROM `eh_service_modules` WHERE parent_id = 41400 AND id = 41430;
DELETE FROM `eh_acl_privileges` WHERE id = 4140041430;
DELETE FROM `eh_service_module_privileges` WHERE privilege_id = 4140041430;

-- AUTHOR: 梁燕龙 20181026
-- REMARK: 行业协会路由修改
UPDATE eh_service_modules SET instance_config = '{"isGuild":1}' WHERE id = 10760;
UPDATE eh_service_module_apps SET instance_config = '{"isGuild":1}' WHERE module_id = 10760;
 
-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.create','1','zh_CN','打卡发送消息','${createType}: ${punchTime}','0');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','1','zh_CN','自动打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','2','zh_CN','人脸识别打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','3','zh_CN','门禁打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','4','zh_CN','其他第三方打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','1','zh_CN','天');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','2','zh_CN','次');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('remind.msg','1','zh_CN','日程提醒');

INSERT INTO eh_locale_strings(scope,CODE,locale,TEXT) VALUE( 'PunchStatusStatisticsItemName', 11, 'zh_CN', '外出');

-- AUTHOR: 吴寒
-- REMARK: 日程提醒给共享人发消息
SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','6','zh_CN','创建给被共享人发消息','${trackContractName}共享了日程“${planDescription}” ','0');

-- AUTHOR: 马世亨
-- REMARK: 邀请者查看邀请详情路由修改
UPDATE `eh_configurations` SET `value`='%s/visitor-appointment/build/index.html?detailId=%s&ns=%s#/appointment-detail#sign_suffix' WHERE `name`='visitorsys.inviter.route';

-- AUTHER：李清岩 20181019
-- REMARK: 新增公共门禁权限子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41011, '门禁授权', '40000', '/200/40000/41010/41011', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41012, '门禁日志', '40000', '/200/40000/41010/41012', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41013, '数据统计', '40000', '/200/40000/41010/41013', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41014, '移动端管理', '40000', '/200/40000/41010/41014', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- REMARK: 新增企业门禁权限子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41021, '门禁授权', '310000', '/100/310000/41020/4102', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41022, '门禁日志', '310000', '/100/310000/41020/41022', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41023, '数据统计', '310000', '/100/310000/41020/41023', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41024, '移动端管理', '310000', '/100/310000/41020/41024', '1', '4', '2', 0, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增门禁权限项
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041010, 0, '公共门禁 全部权限', '公共门禁 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041011, 0, '公共门禁 门禁授权', '公共门禁 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041012, 0, '公共门禁 门禁日志', '公共门禁 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041013, 0, '公共门禁 数据统计', '公共门禁 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4101041014, 0, '公共门禁 移动端管理', '公共门禁 移动端管理权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041020, 0, '企业门禁 全部权限', '企业门禁 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041021, 0, '企业门禁 门禁授权', '企业门禁 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041022, 0, '企业门禁 门禁日志', '企业门禁 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041023, 0, '企业门禁 数据统计', '企业门禁 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4102041024, 0, '企业门禁 移动端管理', '企业门禁 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 模块权限关联 SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41010', '0', 4101041010, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41011', '0', 4101041011, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41012', '0', 4101041012, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41013', '0', 4101041013, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41014', '0', 4101041014, '移动端管理权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41020', '0', 4102041020, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41021', '0', 4102041021, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41022', '0', 4102041022, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41023', '0', 4102041023, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41024', '0', 4102041024, '移动端管理权限', '0', NOW());
-- AUTHER：李清岩 20181019
-- 新增两个门禁模块：公共门禁beta，企业门禁beta
INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 16030610, '公共门禁beta', 16030000, NULL, 'public-access', 1, 2, '/16000000/16030000/16030610', 'zuolin', 40, 41110, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 16030620, '公共门禁beta', 45000000, NULL, 'public-access', 1, 2, '/40000040/45000000/16030620', 'park', 30, 41110, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 48140010, '企业门禁beta', 16040000, NULL, 'company-access', 1, 2, '/23000000/16040000/48140010', 'zuolin', 60, 41120, 3, 'system', 'module', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 48140020, '企业门禁beta', 48000000, NULL, 'company-access', 1, 2, '/40000010/48000000/48140020', 'park', 80, 41120, 3, 'system', 'module', NULL );
-- AUTHER：李清岩 20181019
-- 新增公共门禁beta子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( 41110, '公共门禁beta', '40000', '/200/40000/41110', '1', '3', '2', 10, NOW(), '{\"isSupportQR\":1,\"isSupportSmart\":0}', '78', NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'module' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41111', '门禁授权', '40000', '/200/40000/41110/41111', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41112', '门禁日志', '40000', '/200/40000/41110/41112', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41113', '数据统计', '40000', '/200/40000/41110/41113', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41114', '移动端管理', '40000', '/200/40000/41110/41114', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增企业门禁beta子模块
INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41120', '企业门禁beta', '310000', '/100/310000/41120', '1', '3', '2', '100', NOW(), '{\"isSupportQR\":1,\"isSupportSmart\":0}', '79', NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'module' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41121', '门禁授权', '310000', '/100/310000/41120/41121', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41122', '门禁日志', '310000', '/100/310000/41120/41122', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41123', '数据统计', '310000', '/100/310000/41120/41123', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );

INSERT INTO `eh_service_modules` ( `id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category` ) VALUES ( '41124', '移动端管理', '310000', '/100/310000/41120/41124', '1', '4', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control', '2', '1', 'subModule' );
-- AUTHER：李清岩 20181019
-- 新增公共门禁beta权限
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041110, 0, '公共门禁beta 全部权限', '公共门禁beta 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041111, 0, '公共门禁beta 门禁授权', '公共门禁beta 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041112, 0, '公共门禁beta 门禁日志', '公共门禁beta 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041113, 0, '公共门禁beta 数据统计', '公共门禁beta 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4111041114, 0, '公共门禁beta 移动端管理', '公共门禁beta 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 新增企业门禁beta权限
INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041120, 0, '企业门禁beta 全部权限', '企业门禁beta 全部权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041121, 0, '企业门禁beta 门禁授权', '企业门禁beta 门禁授权权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041122, 0, '企业门禁beta 门禁日志', '企业门禁beta 门禁日志权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041123, 0, '企业门禁beta 数据统计', '企业门禁beta 数据统计权限', NULL );

INSERT INTO `eh_acl_privileges` ( `id`, `app_id`, `name`, `description`, `tag` ) VALUES ( 4112041124, 0, '企业门禁beta 移动端管理', '企业门禁beta 移动端管理权限', NULL );
-- AUTHER：李清岩 20181019
-- 模块权限关联
SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41110', '0', 4111041110, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41111', '0', 4111041111, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41112', '0', 4111041112, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41113', '0', 4111041113, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41114', '0', 4111041114, '移动端管理权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41120', '0', 4112041120, '全部权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41121', '0', 4112041121, '门禁授权权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41122', '0', 4112041122, '门禁日志权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41123', '0', 4112041123, '数据统计权限', '0', NOW());

SET @mp_id = ( SELECT MAX(id) FROM eh_service_module_privileges );

INSERT INTO `eh_service_module_privileges` ( `id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time` ) VALUES ( @mp_id :=@mp_id + 1, '41124', '0', 4112041124, '移动端管理权限', '0', NOW());
-- AUTHER：李清岩 20181019
-- 新增左邻后台门禁管理模块
INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 79300000, '智能硬件', 15000000, NULL, NULL, 1, 2, '/15000000/79300000', 'zuolin', 60, NULL, 2, 'system', 'classify', NULL );

INSERT INTO `eh_web_menus` ( `id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type` ) VALUES ( 79410000, '门禁管理', 79300000, NULL, 'access-management', 1, 2, '/15000000/79300000/79410000', 'zuolin', 1, 70400, 3, 'system', 'module', NULL );
-- AUTHER：李清岩 20181019
-- 更新门禁设备所属城市id
UPDATE eh_door_access dc LEFT JOIN eh_communities c ON c.id = dc.owner_id SET dc.city_id = c.city_id WHERE dc.owner_type = 0;

UPDATE eh_door_access dc LEFT JOIN ( SELECT DISTINCT r.member_id organization_id, c.city_id city_id FROM eh_organization_community_requests r LEFT JOIN eh_communities c ON c.id = r.community_id ) t ON t.organization_id = dc.owner_id SET dc.city_id = t.city_id WHERE dc.owner_type = 1;
-- AUTHER：李清岩 20181019
-- 更新门禁设备所属域空间id
UPDATE eh_door_access dc LEFT JOIN eh_communities c ON c.id = dc.owner_id SET dc.namespace_id = c.namespace_id WHERE dc.owner_type = 0;

UPDATE eh_door_access dc LEFT JOIN eh_organizations t ON dc.owner_id = t.id SET dc.namespace_id = t.namespace_id WHERE dc.owner_type = 1;

-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '终止并重新提交', '终止并重新提交', '{"nodeType":"APPROVAL_CANCEL_AND_RESUMBIT"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '复制审批单', '复制审批单', '{"nodeType":"APPROVAL_COPY_FORM_VALUES"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '显示撤销规则', '显示撤销规则', '{"nodeType":"APPROVAL_SHOW_CANCEL_INFO"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '编辑当前节点表单', '编辑当前节点表单', '{"nodeType":"APPROVAL_EDIT_CURRENT_FORM"}', 2);

-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '30002', 'zh_CN', '关联表单需要填写才能进入下一步');

-- AUTHOR: 丁建民 20181031
-- REMARK: 缴费对接门禁。企业或者个人欠费将禁用该企业或个人门禁 定时器执行时间
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ((@id:=@id+1), 'asset.dooraccess.cronexpression', '0 0 3,5 * * ?', '欠费禁用门禁的定时任务执行时间', '0', NULL, '1');


-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 把基线的 2 域空间删掉，标准版不执行这个 sql
DELETE FROM eh_namespaces WHERE id=2;

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

-- AUTHOR: xq.tian
-- REMARK: 越空间独立部署的 root 用户的密码修改为: eh#1802
UPDATE eh_users SET password_hash='4eaded9b566765a1e70e2e0dc45204c14c4b9df41507a6b72c7cc7fe91d85341', salt='3023538e14053565b98fdfb2050c7709'
WHERE account_name='root' AND namespace_id=0;

-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR:梁燕龙  20181022
-- REMARK: 瑞安个人中心跳转URL
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.point.url','https://m.mallcoo.cn/a/user/10764/Point/List','瑞安积分跳转URL',999929, '瑞安积分跳转URL');
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.vip.url','https://m.mallcoo.cn/a/custom/10764/xtd/Rights','瑞安会员跳转URL',999929, '瑞安会员跳转URL');
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.order.url','/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix','瑞安订单跳转URL',999929, '瑞安订单跳转URL');
INSERT INTO eh_configurations (name, value, description)
VALUES ('ruian.coupon.url','https://inno.xintiandi.com/promotion/app-coupon?systemId=16#/','瑞安新天地卡券链接');
-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
